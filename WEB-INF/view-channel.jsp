<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><c:out value="${ not empty isAdd ? 'チャンネル登録' : 'チャンネル編集'}" /></title>
	<%@ include file="header.jsp"%>
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
			<div class="col-12">
				<form action="${ not empty isAdd ? 'add-channel' : 'edit-channel' }"
					method="post">
					<p class="h4 mt-3 p-3 bg-light text-info rounded">
						<c:out value="${ not empty isAdd ? 'チャンネル登録' : 'チャンネル編集'}" />
					</p>
					<div class="form-group row">
						<input type="hidden" name="channelId" value="${ channelDto.channelId }" />
						<input type="hidden" name="updateNumber" value="${ channelDto.updateNumber }" />
					</div>
					<div class="form-group row">
						<label class="col-form-label col-2" for="channelName">チャンネル</label>
						<div class="col-10">
							<input type="text" id="channelName" name="channelName" class="form-control" maxlength="30" value="${ channelDto.channelName }" />
						</div>
					</div>
					<div class="form-group row">
						<label class="col-form-label col-2" for="overview">概要</label>
						<div class="col-10">
							<textarea id="overview" name="overview" style="resize: none;" class="form-control" maxlength="200" rows="4"><c:out value="${ channelDto.overview }" /></textarea>
						</div>
					</div>
					<div class="form-group row">
						<div class="mx-auto">
							<c:if test="${ empty isAdd }">
								<button type="reset" class="btn btn-secondary">リセット</button>
							</c:if>
							<button type="submit" class="btn btn-primary">${ not empty isAdd ? '登録' : '更新'}</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>