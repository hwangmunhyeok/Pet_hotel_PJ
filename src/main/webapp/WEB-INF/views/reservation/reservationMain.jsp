<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"  %>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />

<%
  request.setCharacterEncoding("UTF-8");
%>    


<html>

<head>
    <meta charset="UTF-8">
    <title>호텔 예약창(예약단계1)</title>
    <style>
        /* * {
            margine: 10%
        } */


        #pet_table {
            margin: auto;
            text-align: center;
            width: 80%;
            table-layout: fixed;
        }


        #checkin {
            margin-left: 10%;
            margin-right: 10%;
        }

        #checkin2 {
            margin-left: 10%;
            margin-right: 10%;
            cursor: pointer;
        }

        #checkout {
            margin-left: 10%;
            margin-right: 10%;
        }

        #checkout2 {
            margin-left: 10%;
            margin-right: 10%;
            cursor: pointer;
        }

        #mypet {
            margin-left: 10%;
            margin-right: 10%;
        }

        #mypet2 {
            margin-left: 10%;
            margin-right: 10%;
            cursor: pointer;
        }

        #btn_addpet {
            margin-top: 10px;
            text-align: center;
        }

        #btn_pet1 {
            margin-top: 50px;
            text-align: center;
        }

        #btn_pet2 {
            width: 90%;
            margin-top: 50px;
            text-align: right;
        }

        #wrap {
            text-align: center;


        }

        #wrap li,
        #wrap ul {
            display: inline-block;

        }


        .reservationtext1 {
            list-style: none;
            display: inline-block;
            color: brown;
            font-size: 20px;
        }

        .reservationtext2 {
            list-style: none;
            display: inline-block;
            color: black;
            font-size: 20px;
        }

        #div1 ul li {
            list-style: none;
            display: inline-block;
            color: brown;
            font-size: 20px;

        }

        #div2 ul li {
            list-style: none;
            display: inline-block;
            color: black;
            font-size: 20px;
        }

        .reservationtext {
            margin: 15px;
        }



        #btn_pet1_1,
        #btn_pet2_1,
        #btn_pet2_2 {
            color: #444444;
            background: #F3F3F3;
            border: 1px #DADADA solid;
            padding: 5px 10px;
            border-radius: 2px;
            font-weight: bold;
            font-size: 9pt;
            outline: none;
        }

        #btn_pet1_1:hover,
        #btn_pet2_1:hover,
        #btn_pet2_2:hover {
            border: 1px #C6C6C6 solid;
            box-shadow: 1px 1px 1px #EAEAEA;
            color: #333333;
            background: #F7F7F7;
        }

        #btn_pet1_1:active,
        #btn_pet2_1:active,
        #btn_pet2_2:active {
            box-shadow: inset 1px 1px 1px #DFDFDF;
        }
    </style>

    <link rel="stylesheet" href="${contextPath}/resources/css/calendar.css">

    <script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="${contextPath}/resources/js/reservationCal.js"></script>

    
    <script>
        var test1 = document.getElementById('beauty');
        var test2 = document.getElementById('spa');
        var test1Result = test1.getAttribute("checked")
        var test2Result = test2.getAttribute("checked")
        function handleOnChange(e) {
            const value = e.value;
        }
        function addRow() {

            // table element 찾기
            var table = document.getElementById('pet_table');

            // 새 행(Row) 추가 (테이블 중간에)
            var newRow = table.insertRow();

            // 새 행(Row)에 Cell 추가
            var Cell1 = newRow.insertCell();
            var Cell2 = newRow.insertCell();
            var Cell3 = newRow.insertCell();
            var Cell4 = newRow.insertCell();
            var Cell5 = newRow.insertCell();
            var Cell6 = newRow.insertCell();

            // Cell에 텍스트 추가
            Cell1.innerHTML = '<input id="name="petname" type="textbox" style="text-align:center; width:150px;">';
            Cell2.innerHTML = '<input id="name="petage" type="textbox" style="text-align:center; width:80px;">';
            Cell3.innerHTML = '<select name="petsex" id="Pet_Sex" onchange="handleOnChange(this)"><option>male</option><option>female</option></select>';
            Cell4.innerHTML = '<select name="petroom" id="Pet_Room" onchange="handleOnChange(this)"><option>Standard</option><option>Superior</option><option>Deluxe</option></select>';
            Cell5.innerHTML = '미용 <input id="beauty" type="checkbox"> 스파 <input id="spa" type="checkbox">';
            Cell6.innerHTML = '<select name="pet" id="Pet_Room" onchange="handleOnChange(this)"><option>Used</option><option>Not Used</option></select>';
        }
    </script>
</head>

<body>

    <hr>
    </hr>

    <div id="wrap">
        <ul>
            <li id="checkinBox" class="reservationBox">
                <div id="checkin" class="reservationtext reservationtext1">| CHECK IN |</div>
                <div id="checkin2" class="reservationtext reservationtext2"><span id="start_dayBox">0000. 00. 00.</span></div>
            </li>
            <li id="checkoutBox" class="reservationBox">
                <div id="mypet" class="reservationtext reservationtext1">| CHECK OUT |</div>
                <div id="checkout2" class="reservationtext reservationtext2"><span id="end_dayBox">0000. 00. 00.</span></div>
            </li>
            <li id="mypetBox" class="reservationBox">
                <div id="mypet" class="reservationtext reservationtext1">| MY PET |</div>
                <div id="mypet2" class="reservationtext reservationtext2"><span>데려오기(펫정보db)</span></div>
            </li>
        </ul>
    </div>

    <table id='pet_table' align="center" width="80%">
        <tr align="center" bgcolor="bisque">
            <td><b>Pet Name</b></td>
            <td><b>Pet Age</b></td>
            <td><b>Pet sex</b></td>
            <td><b>Room</b></td>
            <td><b>Service</b></td>
            <td><b>Use</b></td>
        </tr>
        <div id="calendarForm"></div>
        <c:forEach var="member" items="${membersList}">
            <tr align="center">
                <td>${Pet.Name}</td>
                <td>${Pet.Age}</td>
                <td>${Pet.Sex}</td>
                <td>
                    <select name="petroom" id="Pet_Room" onchange="handleOnChange(this)">
                        <option>Standard</option>
                        <option>Superior</option>
                        <option>Deluxe</option>
                    </select>
                </td>
                <!-- 체크박스 -->

                <td>
                    미용 <input id="beauty" type="checkbox">
                    스파 <input id="spa" type="checkbox">
                </td>

                <td>
                    <select name="pet" id="Pet_Room" onchange="handleOnChange(this)">
                        <option>Used</option>
                        <option>Not Used</option>
                    </select>
                </td>
            </tr>
        </c:forEach>
    </table>
    <div id="btn_pet1">
        <input type='button' value='추가하기' id="btn_pet1_1" onclick='addRow()' />
    </div>

    <br></br>
    
    <div id="petpayment">
    	<li>
        	<ul>| 결제금액 |</ul>
        	<ul id="roomcount">| Room |</ul>
        	<ul>| Service |</ul>
        	<ul id="beautycount">미용</ul>
        	<ul id="spacount">스파</ul>
        </li>
    </div>


    <div id="btn_pet2">
        <input type='button' value='요청사항' id="btn_pet2_1" onclick=' ' />
        <input type='button' value='다음' id="btn_pet2_2" onclick=' ' />
    </div>
    <br></br>
    <br></br>

</body>

</html>