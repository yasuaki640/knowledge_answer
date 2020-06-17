<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>チャンネル一覧</title>
	<script type="text/javascript">
	<!--
		function deleteConfirm(channelName) {
			return window.confirm("「" + channelName + "」チャンネルを本当に削除しますか？");
		}
	// -->
	</script>
	<%@ include file="header.jsp"%>
</head>
<body>
	<%@ include file="navbar.jsp"%>
	<div class="container">
		<div class="row">
			<div class="mx-auto text-primary">
				&nbsp;<c:out value="${ message }" />
			</div>
		</div>

		<div class="row">
			<div class="col-12">
				<p class="h4 mt-3 p-3 bg-light text-info rounded">チャンネル一覧</p>
				<table class="table table-striped">
					<thead>
						<tr>
							<th scope="col">#</th>
							<th scope="col">チャンネル</th>
							<th scope="col">概要</th>
							<c:if test="${ not empty user }">
								<th scope="col"><c:choose>
										<c:when test="${ not empty user }">
											<a href="add-channel" class="btn btn-primary btn-sm">登録</a>
										</c:when>
										<c:otherwise>
											<br>
										</c:otherwise>
									</c:choose></th>
								<th scope="col"><br></th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ channelList }" var="channelDto" varStatus="status">
							<tr>
								<th scope="row" style="height:50px"><c:out value="${ status.count }" /></th>
								<td><a href="list-knowledge?channelId=${ channelDto.channelId }">${ channelDto.channelName }</a></td>
								<td><span style="white-space:pre-wrap"><c:out value="${ channelDto.overview }" /></span></td>
								<c:if test="${ user.userId eq channelDto.userId or user.administratorFlg }">
									<td>
										<form action="edit-channel" method="get">
											<input type="hidden" name="channelId" value="${ channelDto.channelId }">
											<button type="submit" class="btn btn-secondary btn-sm">編集</button>
										</form>
									</td>
									<td>
										<form action="delete-channel" method="post">
											<input type="hidden" name="channelId" value="${ channelDto.channelId }">
											<input type="hidden" name="updateNumber" value="${ channelDto.updateNumber }">
											<button type="submit" class="btn btn-danger btn-sm" onclick="return deleteConfirm('${ channelDto.channelName }')">削除</button>
										</form>
									</td>
								</c:if>
								<c:if test="${ user.userId ne channelDto.userId and not user.administratorFlg }">
									<td><br></td>
									<td><br></td>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>