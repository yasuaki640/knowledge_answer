<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>パスワード変更</title>
<%@ include file="header.jsp"%>
</head>
<body>
	<%@ include file="navbar.jsp"%>
	<div class="container">
		<div class="row">
			<div class="mx-auto text-danger">
				&nbsp;<c:out value="${ errorMessage }" />
			</div>
		</div>
		<div class="row">
			<div class="col-12">
				<form action="change-password" method="post">
					<p class="h4 mt-3 p-3 bg-light text-info rounded">パスワード変更</p>
					<div class="form-group row">
						<input type="hidden" name="userId" value="${ user.userId }" />
					</div>
					<div class="form-group row">
						<label class="col-form-label col-2" for="password1">新しいパスワード</label>
						<div class="col-10">
							<input type="password" id="password1" name="password1" class="form-control" maxlength="30" />
						</div>
					</div>
					<div class="form-group row">
						<label class="col-form-label col-2" for="password2">パスワード（確認用）</label>
						<div class="col-10">
							<input type="password" id="password2" name="password2" class="form-control" maxlength="30" />
						</div>
					</div>
					<div class="form-group row">
						<div class="mx-auto">
							<button type="submit" class="btn btn-primary">登録</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>