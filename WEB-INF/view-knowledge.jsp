<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><c:out value="${ not empty isAdd ? 'ナレッジ追加' : 'ナレッジ編集'}" /></title>
<%@ include file="header.jsp"%>
<script type="text/javascript">
<!--
	$(function() {
		// 削除ボタン
		$(document).on('click', '#delete', function() {
			if (!confirm("本当に削除しますか？")) {
				return;
			}
			// 送信先URLを設定
			$('#form').attr('action', 'delete-knowledge');
			// 送信
			$('#form').submit();
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
				&nbsp;
				<c:out value="${ message }" />
			</div>
		</div>
		<div class="row">
			<div class="col-12">
				<form id="form" action="${ not empty isAdd ? 'add-knowledge' : 'edit-knowledge' }" method="post">
					<p class="h4 mt-3 p-3 bg-light text-info rounded">
						<c:out value="${ not empty isAdd ? 'ナレッジ追加' : 'ナレッジ編集'}" />
					</p>
					<div class="form-group row">
						<input type="hidden" name="knowledgeId" value="${ knowledgeDto.knowledgeId }" />
						<input type="hidden" name="updateNumber" value="${ knowledgeDto.updateNumber }" />
					</div>
					<div class="form-group row">
						<label class="col-form-label col-2" for="channelId">チャンネル</label>
						<div class="col-10">
							<select id="channelId" id="channelId" name="channelId" class="form-control">
								<c:forEach items="${ channelList }" var="channelDto">
									<c:choose>
										<c:when test="${ channelDto.channelId eq knowledgeDto.channelId }">
											<option value="${ channelDto.channelId }" selected="selected"><c:out value="${ channelDto.channelName }" /></option>
										</c:when>
										<c:otherwise>
											<option value="${ channelDto.channelId }"><c:out value="${ channelDto.channelName }" /></option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
					</div>
					<c:set var="newLine" value="\r\n" />
					<div class="form-group row">
						<label class="col-form-label col-2" for="knowledge">ナレッジ</label>
						<div class="col-10">
							<textarea id="knowledge" name="knowledge" class="form-control" rows="15"><c:out value="${ fn:replace(knowledgeDto.knowledge, newLine, '<br>') }" /></textarea>
						</div>
					</div>
					<div class="form-group row">
						<div class="mx-auto">
							<c:if test="${ empty isAdd }">
								<button type="reset" class="btn btn-secondary">リセット</button>
							</c:if>
							<button type="submit" class="btn btn-primary">${ not empty isAdd ? '追加' : '更新'}</button>
							<c:if test="${ empty isAdd }">
								<button id="delete" type="button" class="btn btn-danger">削除</button>
							</c:if>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>