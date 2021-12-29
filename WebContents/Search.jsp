<%@page import="mvc.model.SearchDTO"%>
<%@page import="mvc.model.SearchDAO"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="com.mysql.cj.PreparedQuery"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.mysql.cj.xdevapi.PreparableStatement"%>
<%@page import="dto.RecentProduct"%><%@page import="dao.ProductRepository"%><%@page import="dto.Product"%><%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<% request.setCharacterEncoding("utf-8"); %>       
<!DOCTYPE html><html><head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<meta charset="UTF-8">
<fmt:setLocale value='<%=request.getParameter("language") %>'/>
<title>상품 목록</title>
<style>
	body{
		font-family: Arial, Helvetica, sans-serif;
	}
	li a{
		color:black;
	}
</style>
</head>
<body>
<jsp:include page="menu.jsp"/>
<div class="jumbotron" style="background:white;">
  <div class="container">
     <h4 style="text-align:center;">${param.searchText }에 대한 검색결과입니다.</h4>
  </div>
</div>
<div class="container">
   <div class="row" align="center">
    <%@ include file="dbconn.jsp" %>
     <%  /* DB로 부터 상품 정보 리스트 얻기 */
     	SearchDAO dao = SearchDAO.getInstance();
     	String SearchText =request.getParameter("searchText");
		ArrayList<SearchDTO> list = dao.getSearch(SearchText);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from product where p_name like '%"+SearchText+"%'order by p_id";
        	 pstmt = conn.prepareStatement(sql);
             rs = pstmt.executeQuery();
         
             while(rs.next()){   %>
             <div class="col-md-4">
             <a href="./product.jsp?id=<%=rs.getString("p_id")%>" style="color:black;">
             <img src="/resources/images/<%=rs.getString("p_filename")%>"  style="width:100%;border-radius: 30px;padding-bottom:10px;">
             <h4><%=rs.getString("p_name") %></h4></a>
             <p style="font-size:13px;"><%=rs.getString("p_description") %></p>
             <p><%=rs.getInt("p_Price") %>원</p>
             
           </div>
            <%
		}%>
       
     
   </div>
   <hr>   
</div>
<jsp:include page="footer.jsp"/>

</body>
</html>