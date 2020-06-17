<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ユーザ管理</title>
<%@ include file="header.jsp"%>
<script type="text/javascript">
<!--
	// 編集時の行データを保持するリスト
	var list = {};

	$(function() {
		// 追加ボタン
		$('#btnAdd').on(
				'click',
				function() {
					// 入力チェック
					if (!$('#email')[0].checkValidity()
							|| !$('#lastName')[0].checkValidity()
							|| !$('#firstName')[0].checkValidity()) {
						return;
					}
					;
					// 送信先URLを設定
					$('#form').attr('action', 'add-user');
					// 送信
					$('#form').submit();
				});

		// 削除ボタン
		$(document).on(
				'click',
				'.btnDelete',
				function() {
					if (!confirm("ユーザ「".concat(
							$("#email".concat(this.value)).text()).concat(
							"」を本当に削除しますか？"))) {
						return;
					}
					// 行番号をhiddenに設定
					$('#index').val(this.value);
					// 送信先URLを設定
					$('#form').attr('action', 'delete-user');
					// 送信
					$('#form').submit();
				});

		// パスワード初期化ボタン
		$(document).on('click', '.btnResetPassword', function() {
			// パスワードを初期化するユーザIDをhiddenに設定
			$('#userId').val(this.value);
			// alert(this.value);
			// 送信先URLを設定
			$('#form').attr('action', 'reset-password');
			// 送信
			$('#form').submit();
		});

		// 編集ボタン
		$(document).on(
			'click',
			'.btnEdit',
			function() {
				var index = this.value;
				var line = {};
				line.email = $("#email" + index).text();
				line.firstName = $("#firstName" + index).text();
				line.lastName = $("#lastName" + index).text();
				line.administratorFlg = $("#administratorFlg" + index).is(':checked');
				line.userId = $('#userId' + index).val();
				line.updateNumber = $('#updateNumber' + index).val();

				// 編集前の情報を保持する（TODO タイムスタンプを保持する）
				list[index] = $("#tr" + index).html();

				$("#tr" + index).html("<td><input class=\"form-control\" type=\"email\" required=\"required\" name=\"email" + index + "\" placeholder=\"例）sample@example.com\" value=\"" + line.email + "\" /></td>");
				$("#tr" + index).append("<td><input class=\"form-control\" type=\"text\" required=\"required\" name=\"lastName" + index + "\" value=\"" + line.lastName + "\"/></td>");
				$("#tr" + index).append("<td><input class=\"form-control\" type=\"text\" required=\"required\" name=\"firstName" + index + "\" value=\"" + line.firstName + "\"/></td>");
				$("#tr" + index).append("<td><input type=\"checkbox\" name=\"administratorFlg" + index + "\" class=\"ml-4\" value=\"true\" " + (line.administratorFlg ? "checked=\"checked\"" : "") + "/></td>");
				$("#tr" + index).append("<td><button type=\"button\" class=\"btn btn-secondaly btn-sm btnCancel\" value=\"" + index + "\">戻す</button></td>");
				$("#tr" + index).append("<td><button type=\"submit\" class=\"btn btn-primary btn-sm btnUpdate\" value=\"" + index + "\">更新</button></td>");
				$("#tr" + index).append("<td><input type=\"hidden\" name=\"userId" + index + "\" value=\"" + line.userId + "\" /><input type=\"hidden\" name=\"updateNumber" + index + "\" value=\"" + line.updateNumber + "\" /></td>");
			});

		// 更新ボタン
		$(document).on('click', '.btnUpdate', function() {
			// 行番号をhiddenに設定
			$('#index').val(this.value);
			// 送信先URLを設定
			$('#form').attr('action', 'edit-user');
			// 送信
			$('#form').submit();
		});

		// キャンセルボタン
		$(document).on('click', '.btnCancel', function() {
			var index = this.value;
			$("#tr" + index).html(list[index]);
		});
	});
// -->
</script>
</head>
<body>
	<%@ include file="navbar.jsp"%>
	<div class="container">
		<c:forEach items="${ errorMessageList }" var="errorMessage">
			<div class="row">
				<div class="mx-auto text-danger">
					&nbsp;<c:out value="${ errorMessage }" />
				</div>
			</div>
		</c:forEach>
		<div class="row">
			<div class="mx-auto text-primary">
				&nbsp;<c:out value="${ message }" />
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<p class="h4 mt-3 p-3 bg-light text-info rounded">ユーザ管理</p>
				<form id="form" method="post">
					<div class="form-group">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>メールアドレス</th>
									<th>姓</th>
									<th>名</th>
									<th>管理権限</th>
									<th><br></th>
									<th><br></th>
									<th>パスワード</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td><input class="form-control" type="email" id="email" name="email" placeholder="例）sample@example.com" /></td>
									<td><input class="form-control" type="text" id="lastName" name="lastName" /></td>
									<td><input class="form-control" type="text" id="firstName" name="firstName" /></td>
									<td><span class="ml-4"><input type="checkbox" name="administratorFlg" value="true" /></span></td>
									<td><button id="btnAdd" type="submit" class="btn btn-primary btn-sm">追加</button></td>
									<td><br></td>
									<td><br></td>
								</tr>
								<c:forEach items="${ userList }" var="userDto" varStatus="status">
									<tr id="tr${ status.index }">
										<td><span id="email${ status.index }" class="ml-3"><c:out value="${ userDto.email }"></c:out></span></td>
										<td><span id="lastName${ status.index }" class="ml-3"><c:out value="${ userDto.lastName }"></c:out></span></td>
										<td><span id="firstName${ status.index }" class="ml-3"><c:out value="${ userDto.firstName }"></c:out></span></td>
										<td><input type="checkbox" id="administratorFlg${ status.index }" class="ml-4"<c:out value="${ userDto.administratorFlg ? 'checked=checked' : ''}" /> disabled="disabled"></td>
										<td>
											<input id="userId${ status.index }" name="userId${ status.index }" type="hidden" value="${ userDto.userId }" />
											<input id="updateNumber${ status.index }" name="updateNumber${ status.index }" type="hidden" value="${ userDto.updateNumber }" />
											<button type="button" class="btn btn-secondaly btn-sm btnEdit" value="${ status.index }" <c:out value="${ user.userId eq userDto.userId ? 'disabled=\"disabled\"' : '' }" />>編集</button>
										</td>
										<td><button type="button" class="btn btn-danger btn-sm btnDelete" value="${ status.index }" <c:out value="${ user.userId eq userDto.userId ? 'disabled=\"disabled\"' : '' }" />>削除</button></td>
										<td><button type="button" class="btn btn-secondaly btn-sm btnResetPassword" value="${ userDto.userId }" <c:out value="${ user.userId eq userDto.userId ? 'disabled=\"disabled\"' : '' }" />>初期化</button></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<input id="userId" name="userId" type="hidden" />
						<input id="index" name="index" type="hidden" />
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>