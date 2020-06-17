<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<a class="navbar-brand" href="index.jsp">
	<img src="img/logo.png" height="30" width="150" /></a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>

	<c:set value="" var="q" />
	<c:forEach items="${ queries }" var="query">
		<c:set value="${ q.concat(' ').concat(query) }" var="q" />
	</c:forEach>

	<div class="collapse navbar-collapse" id="navbarSupportedContent">
		<ul class="navbar-nav mr-auto">
			<li class="nav-item">
				<form action="search-knowledge" method="get" class="form-inline">
					<input type="text" name="q" style="width: 300px;" value="${ fn:trim(q) }" class="form-control form-control-sm" placeholder="ナレッジ検索" autofocus="autofocus">
					<button type="submit" class="btn btn-outline-primary btn-sm">検索</button>
				</form>
			</li>
			<c:if test="${ not empty user }">
				<li><a href="add-knowledge"><button class="btn btn-link btn-sm ml-2">ナレッジ追加</button></a></li>
			</c:if>
		</ul>
		<div class="navbar-text small mr-3 text-danger">
			<c:out value="${ navbarMessage }" />
		</div>
		<div class="navbar-text small mr-3 text-secondary">
			<c:out value="${ not empty user.lastName ? user.lastName += ' ' += user.firstName += 'さん、こんにちは' : '' }" />
		</div>
		<c:choose>
			<c:when test="${ empty user }">
				<form id="form-nav" action="login" method="post" class="form-inline">
					<input type="email" name="id" class="form-control form-control-sm" placeholder="メールアドレス">
					<input type="password" name="password" class="form-control form-control-sm" placeholder="パスワード">
					<input type="hidden" name="uri" value="${ requestScope.uri }">
					<button type="submit" class="btn btn-outline-primary btn-sm">ログイン</button>
				</form>
			</c:when>
			<c:otherwise>
				<form action="logout" method="post" class="form-inline">
					<button type="submit" class="btn btn-outline-danger btn-sm">ログアウト</button>
				</form>
				<c:if test="${ user.administratorFlg }">
					<a href="list-user" class="btn btn-outline-secondary btn-sm ml-2">ユーザ管理</a>
				</c:if>
			</c:otherwise>
		</c:choose>
	</div>
</nav>
