<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String sessionId = (String) session.getAttribute("sessionId");
%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<head>
<meta charset="UTF-8">
<style type="text/css">
body {
	margin: 0;
	font-family: Arial, Helvetica, sans-serif;
}

.con {
	position: fixed;
	z-index: 1;
	padding-bottom: 5px;
	width: 100%;
	display: grid;
	grid-template-rows: 50px;
	grid-template-columns: 12.5% 12.5% 50% 12.5% 12.5%;
	align-items: center;
	justify-items: center;
	width: 100%;
	border-bottom: 1px solid black;
	background: white;
}

.items {
	font-weight: 1000;
	font-size: 20px;
}

.dropdown {
	position: relative;
	display: inline-block;
}

.dropdown-content {
	display: none;
	position: absolute;
	z-index: 1; /*다른 요소들보다 앞에 배치*/
}

.dropdown-content a {
	display: block;
	color: black;
	border: 1px solid black;
}

.dropdown:hover .dropdown-content {
	display: block;
}

</style>
<title>Insert title here</title>
</head>
<body>
	<div class="con">
		<div class="items dropdown" id="menu">
			<span class="dropbtn" style="padding-left: 10px;">MENU</span>
			<div class="dropdown-content" style="padding-top: 20px;">
				<a class="nav-link" href="./products.jsp?category=Top">TOP</a>
				<a class="nav-link" href="./products.jsp?category=BOTTOM">BOTTOM</a>
				<a class="nav-link" href="./products.jsp?category=OUTER">OUTER</a>
				<a class="nav-link" href="<c:url value="/ReviewBbsListAction.to?pageNum=1"/>">REVIEW</a>
			</div>
		</div>
		<div class="items">
<!-- 			<span id="search">SEARCH</span> -->
			<form method="post" name="search" action="Search.jsp">
				<input type="text" id="searchBar"class="form-control" placeholder="검색어를 입력하세요" name="searchText" maxlength="100"">
			</form>
		</div>
		<div class="items">
			<a href="./welcome.jsp">
				<img src="./images/mainTop.png" style="height: 50px;">
			</a>
		</div>
		
 <c:if test="${sessionId=='admin'}"> 
		<div class="items dropdown" id="admin-menu"> 
			<span class="dropbtn" style="padding-left: 10px;">관리자용</span>
			<div class="dropdown-content" style="padding-top: 20px;">
				<a class="nav-link" href="./addProduct.jsp">상품등록</a>
				<a class="nav-link" href="./editProduct.jsp?edit=update">상품수정</a>
				<a class="nav-link" href="./editProduct.jsp?edit=delete">상품삭제</a>
				
			</div>
		</div>
 </c:if>
		
		
		<div class="items dropdown" id="account"> 
			<span class="dropbtn" style="padding-left: 10px;">ACCOUNT</span>
			<div class="dropdown-content" style="padding-top: 20px;">
				<a class="nav-link" href="./member/loginMember.jsp">로그인</a>
				<a class="nav-link" href="./member/logoutMember.jsp">로그아웃</a>
				<a class="nav-link" href="./member/addMember.jsp">회원가입</a>
			</div>
		</div>
		
		<div class="items"><a href="./cart.jsp">CART</a></div>		
		
		
		
		<div class="items"><a href="./cart.jsp">CART</a></div>
	</div>
</body>
</html>