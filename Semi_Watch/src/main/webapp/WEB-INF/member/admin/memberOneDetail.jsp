<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>
<jsp:include page="../../header1.jsp" />

<style type="text/css">

<style type="text/css">
    body {
        font-family: Arial, sans-serif;
        background-color: #f8f9fa;
    }

    .container {
        background-color: #fff;
        border-radius: 10px;
        padding: 2% 3%;
    }

    .h3, .h5 {
        color: #343a40;
        font-weight: 700;
    }

    table.table-bordered {
        width: 60%;
        margin: 0 auto;
        border-collapse: collapse;
    }

    table.table-bordered th, 
    table.table-bordered td {
        text-align: left;
        padding: 1rem;
        border: 1px solid #dee2e6;
    }

    table.table-bordered th {
        background-color: #343a40;
        color: #fff;
        font-size: 1rem;
    }

    table.table-bordered tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    table.table-bordered > tbody > tr > td:nth-child(1) {
        width: 25%;
        font-weight: bold;
        text-align: right;
        background-color: #e9ecef;
    }

    .border {
        background-color: #fff;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        border-radius: 10px;
        padding: 1.5rem;
    }

    .bg-info {
        background-color: #17a2b8 !important;
    }

    .text-white {
        color: #fff !important;
    }

    .bg-danger {
        background-color: #dc3545 !important;
    }

    .text-center {
        text-align: center;
    }

    .btn {
        padding: 0.5rem 1rem;
        border-radius: 5px;
        border: none;
    }

    .btn-secondary {
        background-color: #6c757d;
        color: #fff;
    }

    .btn-secondary:hover {
        background-color: #5a6268;
    }

    .btn-success {
        background-color: #28a745;
        color: #fff;
    }

    .btn-success:hover {
        background-color: #218838;
    }

    .mx-2 {
        margin-left: 0.5rem !important;
        margin-right: 0.5rem !important;
    }

    .mx-5 {
        margin-left: 3rem !important;
        margin-right: 3rem !important;
    }

    .my-5 {
        margin-top: 3rem !important;
        margin-bottom: 3rem !important;
    }

    .m

</style>

<script type="text/javascript">
  
  $(document).ready(function(){ 
  	$("div#smsResult").hide();
  	$("button#btnSend").click(()=>{
  		// console.log( $("input#reservedate").val() + " " + $("input#reservetime").val() );
  		//2024-05-14 09:54 <이 - 와 : 를 빼서 202405140954 이렇게 예약시간을 만들어야만 해당오픈api가 동작
  		let reservedate = $("input#reservedate").val();
  		reservedate = reservedate.split("-").join("");
  		//["2024","05","14"] =>"20240514"
  		let reservetime = $("input#reservetime").val();
  		reservetime = reservetime.split(":").join("");
  		
  		const datetime = reservedate + reservetime;
  		
  		//console.log(datetime);
  		
  		let dataObj;
        
        if( reservedate == "" || reservetime == "" ) { //예약날짜, 예약 시간이 없는 경우를 뜻한다. 즉 문자를 바로 보내는 경우
           // 문자를 바로 보내기인 경우 
           dataObj = {"mobile":"${requestScope.mvo.mobile}", //모바일의 경우 원래 db에는 01091924325 이렇게 들어가 있다.
                    "smsContent":$("textarea#smsContent").val()};
        }
        else {
           // 예약문자 보내기인 경우
           dataObj = {"mobile":"${requestScope.mvo.mobile}",
                    "smsContent":$("textarea#smsContent").val(),
                    "datetime":datetime};
        }
  		
  		
  		$.ajax({
            url:"${pageContext.request.contextPath}/member/smsSend.flex",
            // /MyMVC
            type:"get",
            data:dataObj, // {"aaaa":"dsadsadsdas", "asdsadd":"엉너ㅓㅇㄴㄴ엉너"} 이런식으로 전송이 된다.
            dataType:"json",
            success:function(json){ 
               // json 은 {"group_id":"R2GWPBT7UoW308sI","success_count":1,"error_count":0} 처럼 된다. 
               
               if(json.success_count == 1) {
                  $("div#smsResult").html("<span style='color:red; font-weight:bold;'>문자전송이 성공되었습니다.^^</span>");
               }
               else if(json.error_count != 0) {
                  $("div#smsResult").html("<span style='color:red; font-weight:bold;'>문자전송이 실패되었습니다.ㅜㅜ</span>");
               }
               
               $("div#smsResult").show();
               $("textarea#smsContent").val("");
            },
            error: function(request, status, error){
               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
         });
  		
  		
  		
  		
  	});
  	
  });// end of $(document).ready(function(){})-----------
  
</script>
<div class="container">
	<c:if test= "${empty requestScope.mvo}">
		존재하지 않는 회원입니다<br>
	</c:if>
	
	<c:if test= "${not empty requestScope.mvo}">
	
		<c:set var="mobile" value="${requestScope.mvo.mobile}"/>
		<p class="h3 text-center mt-5 mb-4">::: ${requestScope.mvo.username} 님의 회원 상세정보 :::</p>
		<table class="table table-bordered" style="width: 60%; margin: 0 auto;">
         <tr>
            <td>아이디&nbsp;:&nbsp;</td>
            <td>${requestScope.mvo.userid}</td>
         </tr>
         <tr>
            <td>회원명&nbsp;:&nbsp;</td>
            <td>${requestScope.mvo.username}</td>
         </tr>
         <tr>
            <td>이메일&nbsp;:&nbsp;</td>
            <td>${requestScope.mvo.email}</td>
         </tr>
         <tr>
            <td>휴대폰&nbsp;:&nbsp;</td>
            <td>${fn:substring(mobile, 0, 3)}-${fn:substring(mobile, 3, 7)}-${fn:substring(mobile, 7,11)}</td>
         </tr>
         <tr>
            <td>우편번호&nbsp;:&nbsp;</td>
            <td>${requestScope.mvo.postcode}</td>
         </tr>
         <tr>
            <td>주소&nbsp;:&nbsp;</td>
            <td>${requestScope.mvo.address}&nbsp;
                ${requestScope.mvo.detail_address}&nbsp;
                ${requestScope.mvo.extra_address}
            </td>
         </tr>
         <tr>
            <td>성별&nbsp;:&nbsp;</td>
            <td>
            	<c:choose>
            		<c:when test="${requestScope.mvo.gender == '1'}">남</c:when>
            		<c:otherwise>여</c:otherwise>
            	</c:choose>
            </td>
         </tr>
         <tr>
            <td>생년월일&nbsp;:&nbsp;</td>
            <td>${requestScope.mvo.birthday}</td>
         </tr>
         <tr>
            <td>만나이&nbsp;:&nbsp;</td>
            <td>${requestScope.mvo.age}&nbsp;세</td>
         </tr>
         <tr>
         <tr>
            <td>가입일자&nbsp;:&nbsp;</td>
            <td>${requestScope.mvo.registerday}</td>
         </tr>
		</table>
		<%-- ==== 휴대폰 SMS(문자) 보내기 ==== --%>
      <div class="border my-5 text-center" style="width: 60%; margin: 0 auto;">
           <p class="h5 bg-info text-white">
             &gt;&gt;&nbsp;&nbsp;휴대폰 SMS(문자) 보내기 내용 입력란&nbsp;&nbsp;&lt;&lt;
           </p>
           <div class="mt-4 mb-3">
              <span class="bg-danger text-white" style="font-size: 14pt;">문자발송 예약일자</span>
              <input type="date" id="reservedate" class="mx-2" />
              <input type="time" id="reservetime" />
           </div>
           <div style="display: flex;">
              <div style="border: solid 0px red; width: 81%; margin: auto;">
                 <textarea rows="4" id="smsContent" style="width: 100%;"></textarea>
              </div>
              <div style="border: solid 0px blue; width: 19%; margin: auto;">
                 <button id="btnSend" class="btn btn-secondary">문자전송</button>
              </div>
           </div>
           <div id="smsResult" class="p-3"></div>
      </div>
	</c:if>
	<div class="text-center mb-5">
       <button type="button" class="btn btn-secondary" onclick="javascript:location.href='memberList.flex'">회원목록[처음으로]</button> 
       <button type="button" class="btn btn-success mx-5" onclick="javascript:location.href='${pageContext.request.contextPath}${requestScope.goBackURL}'">회원목록[검색된결과]</button>
    	<!-- ctxPath인 http://localhost:9090/MyMVC/까지가 빠져있으므로 이렇게 끼워넣어 준다. -->
    </div>
</div>
<jsp:include page="../../footer.jsp" /> 