<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html><html><head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<meta charset="UTF-8">
<title>배송지 상세보기</title>
</head>
<body>
<jsp:include page="menu.jsp" />
<div class="jumbotron">
   <div class="container">
   		<h1 class="display-3">배송지 상세보기</h1>
   </div>
</div>
<%@include file="dbconn.jsp" %>
<%
    String seq=request.getParameter("seq");
    String sql="select * from delivery where seq=?";
   
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1,seq);
	
	ResultSet rs=pstmt.executeQuery();
	if(rs.next()){
%>
<div class="container">
   <form action="./processDeliveryUpdate.jsp" class="form-horizontal" method="post">
         <input type="hidden" name="seq" value="<%=seq%>">
         <div class="form-group row">
             <label class="col-sm-2">배송지명</label>
             <div class="col-sm-3">
                 <input name="name" type="text" class="form-control" value="<%=rs.getString("nickName")%>" >
             </div>
         </div>
         <div class="form-group row">
             <label class="col-sm-2">국가</label>
             <div class="col-sm-3">
                 <input name="country" type="text" class="form-control" value="<%=rs.getString("country")%>" >
             </div>
         </div>
         <div class="form-group row">
             <label class="col-sm-2">우편번호</label>
             <div class="col-sm-3">
                 <input name="zipcode" id="zipcode" type="text" class="form-control" value="<%=rs.getString("zipcode")%>">
                 <input type="button" onclick="Postcode()" value="우편번호 찾기"><br>
             </div>
         </div>
          <div class="form-group row">
             <label class="col-sm-2">도로명주소</label>
             <div class="col-sm-3">
                 <input name="roadAddress" id="roadAddress"  type="text" class="form-control" value="<%=rs.getString("roadAddress")%>" >
             </div>
         </div>
         <div class="form-group row">
             <label class="col-sm-2">지번주소</label>
             <div class="col-sm-3">
                 <input name="jibunAddress" id="jibunAddress"  type="text" class="form-control" value="<%=rs.getString("jibunAddress")%>" >
             </div>
         </div>
         <span id="guide" style="color:#999;display:none"></span>
         <div class="form-group row">
             <label class="col-sm-2">상세주소</label>
             <div class="col-sm-3">
                 <input name="detailAddress"  id="detailAddress" type="text" class="form-control" value="<%=rs.getString("detailAddress")%>">
             </div>
         </div>
         <div class="form-group row">
             <label class="col-sm-2">참고항목</label>
             <div class="col-sm-3">
                 <input name="extraAddress"id="extraAddress" type="text" class="form-control" value="<%=rs.getString("extraAddress")%>">
             </div>
         </div>
         <div class="form-group row">
             <div class="col-sm-offset-2 col-sm-10">
                <input type="submit" class="btn btn-primary" value="수정">
                <input type="reset"  class="btn btn-secondary" value="취소">             
             </div>
         </div>
   </form>
   <% } %>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    //본 예제에서는 도로명 주소 표기 방식에 대한 법령에 따라, 내려오는 데이터를 조합하여 올바른 주소를 구성하는 방법을 설명합니다.
    function Postcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 참고 항목 변수

                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zipcode').value = data.zonecode;
                document.getElementById("roadAddress").value = roadAddr;
                document.getElementById("jibunAddress").value = data.jibunAddress;
                
                // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                if(roadAddr !== ''){
                    document.getElementById("extraAddress").value = extraRoadAddr;
                } else {
                    document.getElementById("extraAddress").value = '';
                }

                var guideTextBox = document.getElementById("guide");
                // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
                if(data.autoRoadAddress) {
                    var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                    guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                    guideTextBox.style.display = 'block';

                } else if(data.autoJibunAddress) {
                    var expJibunAddr = data.autoJibunAddress;
                    guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                    guideTextBox.style.display = 'block';
                } else {
                    guideTextBox.innerHTML = '';
                    guideTextBox.style.display = 'none';
                }
            }
        }).open();
    }
</script>