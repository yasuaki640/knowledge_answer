<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="k" uri="http://kronos.jp/jsp/string" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ナレッジ一覧</title>
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
				<p class="h4 mt-3 p-3 bg-light text-info rounded">ナレッジ一覧</p>
				<c:if test="${ fn:length(knowledgeList) eq 0 }">
					<div class="row">
						<div class="mx-auto text-secondary">検索キーワードに該当するナレッジはありません</div>
					</div>
				</c:if>
				<c:forEach items="${ knowledgeList }" var="knowledgeDto">
					<c:set value="${ knowledgeDto.knowledge }" var="knowledge" />
					<c:forEach items="${ queries }" var="query">
						<c:set var="knowledge">
							<k:hightLight data="${ knowledge }" keyword="${ query }" />
						</c:set>
					</c:forEach>
					<div class="card mb-2">
						<div class="card-body">
							<div class="card-title">
								<a href="list-knowledge?channelId=${ knowledgeDto.channelId }"><c:out value="${ knowledgeDto.channelName }" /></a>
							</div>
							<span style="white-space:pre-wrap"><c:out escapeXml="false" value="${ knowledge }" /></span>
							<footer class="blockquote-footer text-right">
								<c:choose>
									<c:when test="${ user.userId eq knowledgeDto.userId or user.administratorFlg }">
										<a href="edit-knowledge?knowledgeId=${ knowledgeDto.knowledgeId }"><c:out value="${ knowledgeDto.updateAt }" /></a>
									</c:when>
									<c:otherwise>
										<c:out value="${ knowledgeDto.updateAt }" />
									</c:otherwise>
								</c:choose>
							</footer>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
</html>