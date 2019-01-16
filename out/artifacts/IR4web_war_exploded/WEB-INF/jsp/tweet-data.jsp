<%--
  Created by IntelliJ IDEA.
  User: siddharthpandey
  Date: 11/26/18
  Time: 5:40 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import = "java.io.*,java.util.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"
           prefix="fn" %>
<html>
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <title>Search Results</title>
     <style>

        a.linker {
            text-decoration: none;
        }

        #tweets { position: absolute; visibility: visible; left: 350px; top: 100px; z-index: 200; }

        #filters { position: relative; visibility: visible; top: -450px; left:25px}

        #hashtags {
            position: relative;
            visibility: visible;
            top: -860px;
            left: 25px
            }

        #all_topics {
            position: relative;
            visibility: visible;
            top: -555px;
            left: 25px
        }


        #cities {
            position: relative;
            visibility: visible;
            top: -515px;
            left: 25px
        }

        hr {
            position: absolute;
            visibility: visible;
            float: left;
        }

        hr#towp {
            position: absolute;
            visibility: visible;
            right: -10px;
            top: 60px;
        }
    </style>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        .dropbtn {

            color: #2f4f4f;
            font-size: 16px;
            border: none;
            cursor: pointer;
        }

        .dropdown-date {
            position: relative;
            display: block;
            visibility: visible;
            top: -860px;
            left:50px;
            color:#2f4f4f;

        }

        .dropdown-hash {
            position: relative;
            display: block;
            visibility: visible;
            top: -835px;
            left: 50px;
            color:#2f4f4f;
        }

        .dropdown-topic {
            position: relative;
            display: block;
            visibility: visible;
            top: -808px;
            left: 50px;
            color:#2f4f4f;
        }



        .dropdown-city {
            position: relative;
            display: block;
            visibility: visible;
            top: -778px;
            left: 50px;
            color:#2f4f4f;
        }




        .dropdown-city.hr{
            width: 12%;
        }
        .dropdown-city.ci{
            width: 12%;
        }

        .dropdown-content-date {
            max-height: 0;
            overflow: hidden;
            font-family: 'Oswald', sans-serif;
            position : relative;
            cursor: pointer;
            font-size: 14px;
            -webkit-transition: max-height 0.7s ease-in;
            -moz-transition: max-height 0.7s ease-in;
            -o-transition: max-height 0.7s ease-in;
            -ms-transition: max-height 0.7s ease-in;
            transition: max-height 0.7s ease-in;
            transition-timing-function: ease-out;
            z-index: 100;


        }
        .dropdown-content-hash {
            max-height: 0;
            overflow: hidden;
            font-family: 'Oswald', sans-serif;
            position : relative;
            cursor: pointer;
            font-size: 14px;
            -webkit-transition: max-height 0.7s ease-in;
            -moz-transition: max-height 0.7s ease-in;
            -o-transition: max-height 0.7s ease-in;
            -ms-transition: max-height 0.7s ease-in;
            transition: max-height 0.7s ease-in;
            transition-timing-function: ease-out;

        }


        .dropdown-content-topic {
            max-height: 0;
            overflow: hidden;
            font-family: 'Oswald', sans-serif;
            position : relative;
            font-size: 14px;
            -webkit-transition: max-height 0.7s ease-in;
            -moz-transition: max-height 0.7s ease-in;
            -o-transition: max-height 0.7s ease-in;
            -ms-transition: max-height 0.7s ease-in;
            transition: max-height 0.7s ease-in;
            transition-timing-function:  ease-out;
            cursor: pointer;

        }

        .dropdown-content-city {
            max-height: 0;
            font-family: 'Oswald', sans-serif;
            position : relative;
            cursor: pointer;
            font-size: 14px;
            overflow: hidden;
            -webkit-transition: max-height 0.7s ease-in;
            -moz-transition: max-height 0.7s ease-in;
            -o-transition: max-height 0.7s ease-in;
            -ms-transition: max-height 0.7s ease-in;
            transition: max-height 0.7s ease-in;
            transition-timing-function: ease-out;

        }


        #trigger-topic  {
            display: none;
        }

        #trigger-city {
            display: none;
        }

        #trigger-date {
            display: none;
        }

        #trigger-hash{
            display: none;
        }


        #trigger-date:checked ~  .dropdown-content-date  {
            max-height: 100%;


        }
        #trigger-topic:checked ~  .dropdown-content-topic  {
            max-height: 100%;

        }
        #trigger-city:checked ~  .dropdown-content-city  {
            max-height: 100%;

        }
        #trigger-hash:checked ~  .dropdown-content-hash  {
            max-height: 100%;

        }

        #label-date{
            cursor: pointer;
        }

        #label-topic{
            cursor: pointer;
        }

        #label-hash{
            cursor: pointer;
        }

        #label-city{
            cursor: pointer;
        }

        div#sidebar{
            position: absolute;
            bottom: -300px;
            left: 0;
            max-height: 0;


        }

        .paginate{
            position: absolute;
            display: inline-block;
            visibility: visible;
            top: -985px;
            left: 345px;
            color:#2f4f4f;
            font-size: 16px;
            width:500%;
            border-radius: 25px;
            border-color: darkslategrey;
            -webkit-transform: translateZ(0);




        }

        .paginate a{

            padding: 8px 16px;
            text-decoration: none;
            transition: background-color .3s;
            color:#2f4f4f;
        }



        .paginate a:hover:not(.active) {background-color: #ddd;}


        html {
            height:100%;
            width:100%;

        }
        body {
            height:100%;
            width:100%;
            background-color: gainsboro;
            background-image: url("../../images/nn2.png") ;
            background-size: auto;
        }

        .subm {
            background-color: dodgerblue;
            border: none;
            color: white;
            padding: 10px 27px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            cursor: pointer;
            border-radius: 25px;
            width:187px;
        }
/*
        body{
            background-color: gainsboro;
        }*/

        .btnl{
            position: absolute;
            display: block;
            visibility: visible;
            top: -800px;
            left: 500px;
            background-color: dodgerblue;
            border-radius: 25px;
            font-size: 16px;
            border: none;
            color: white;
            padding: 15px 32px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            cursor: pointer;
            -webkit-transform: translateZ(0);
        }

       /* body{
            background-image: url("../../images/nn2.png") ;
            background-size: auto;
        }*/
    </style>


</head>
<body>
<div style="min-width: 960px; margin: 0 auto;">
<script>

    function newPopup(url) {
        popupWindow = window.open(
            url,'popUpWindow','height=600,width=800,left=160,top=85,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
    }




</script>




<form action="/Web?page=1" method="get" id="demo-2">


<div id="sidebar">

<div class="dropdown-date">
    <input type="checkbox" id="trigger-date" />
    <label for="trigger-date" id="label-date"><font size="4" color="#2f4f4f">Date</font></label>

   <!-- <button class="dropbtn">Date</button>-->
    <br>

    <div class="dropdown-content-date">

        <!--<font size="5" color="#2f4f4f">Date</font> <br> -->

        <br>
        <c:forEach items="${tweetData}" var="dateEntries">
            <input type="checkbox" value="date=${dateEntries.value['tweet_date']}" name="check">${dateEntries.value['tweet_date']}<br>
        </c:forEach>
        <c:set var="tweetData" value="${tweetData}" scope="session" />
    </div>
</div>



<div class="dropdown-hash">
    <input type="checkbox" id="trigger-hash" />
    <label for="trigger-hash" id="label-hash"><font size="4" color="#2f4f4f">Hashtags</font></label>
    <br>

    <div class="dropdown-content-hash">
<!--<div id="hashtags" class="hashtag">-->
   <!-- <font size="5" color="#2f4f4f">Hashtags</font> <br> -->
        <br>

    <br>
    <c:forEach items="${hashtags}" var="hastg">
        <input type="checkbox" value="hashtag=${hastg.key}" name="check">${hastg.key}   (${hastg.value})<br>
    </c:forEach>
        <c:set var="hashtags" value="${hashtags}" scope="session" />
<!--</div>-->
    </div>
</div>

<div class="dropdown-topic">
    <input type="checkbox" id="trigger-topic" />
    <label for="trigger-topic" id="label-topic" ><font size="4" color="#2f4f4f">Topic</font></label>
    <br>

    <div class="dropdown-content-topic">
<!--<div id="all_topics" class="all_topic"> -->


    <br>
    <c:forEach items="${all_topics}" var="altop">
        <input type="checkbox" value="topic=${fn:toLowerCase(altop.key)}" name="check">${altop.key}   (${altop.value})<br>
    </c:forEach>
        <c:set var="all_topics" value="${all_topics}" scope="session" />
</div>
</div>


<div class="dropdown-city">
    <input type="checkbox" id="trigger-city" />
    <label for="trigger-city" id="label-city"><font size="4" color="#2f4f4f">City</font></label>
    <br>
    <hr class="ci">
    <div class="dropdown-content-city">


    <br>
    <c:forEach items="${cities}" var="city">
        <input type="checkbox" value="city=${fn:toLowerCase(city.key)}" name="check">${city.key}   (${city.value})<br>
    </c:forEach>
        <c:set var="cities" value="${cities}" scope="session" />

</div>


    <c:set var="txt" value="${txt}"  scope="request"/> <br><br>
    <input type="submit"  class="subm"> <br><Br>
    <a href="../../tab-nn.jsp" style=" background-color: dodgerblue;
            border: none;
            color: white;
            padding: 10px 27px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            cursor: pointer;
            border-radius: 25px;" target="_blank">Analyse My Search</a> <br><br>
    <a href="../../index.jsp" style=" background-color: dodgerblue;
            border: none;
            color: white;
            padding: 10px 27px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            cursor: pointer;
            border-radius: 25px; width:187px" target="_self">Home</a>

</div>



    <c:set var="p" value="${currPage}" /> <%-- current page (1-based) --%>
    <c:set var="l" value="5" /> <%-- amount of page links to be displayed --%>
    <c:set var="r" value="${l / 2}" /> <%-- minimum link range ahead/behind --%>
    <c:set var="t" value="${noOfPages}" /> <%-- total amount of pages --%>

    <c:set var="begin" value="${((p - r) > 0 ? ((p - r) < (t - l + 1) ? (p - r) : (t - l)) : 0) + 1}" />
    <c:set var="end" value="${(p + r) < t ? ((p + r) > l ? (p + r) : l) : t}" />
    <div class="paginate">
        Results for Pages :

            <c:forEach begin="${begin}" end="${end}" var="i">

              <a class="page-link"
                   href="/Web?&page=${i}&txt=${txt}" >${i}&nbsp</a>
            </c:forEach>


    </div>


</div>

</form>


<div id="tweets" class="tweet">
<c:forEach items="${tweetData}" var="entry">


        <font color="#696969" size="4" ><b>${entry.value['userName']}</b></font></a>
        <font size="4" color="#808080">@${entry.value['userScreenName']} </font><br>
        <a href="JavaScript:newPopup('${entry.value['tweet_url']}');" class="linker"> <font size="3" color="#1e90ff">${entry.value['tweet_sumr_init']}...show tweet</font></a>
        <br>
        <font size="2">Topic : ${entry.value['tweet_topic']} | City : ${entry.value['city']}</font> <br><br>
      <!--  <font size="2">Sentiment : ${entry.value['sentiment']} </font><br><br> -->



</c:forEach>

</div>

<%--div class="dropdown">
    <button class="dropbtn">Dropdown</button> <br>
    <div class="dropdown-content">
        <c:forEach items="${cities}" var="city">
            <input type="checkbox" value="vale">${city.key}   (${city.value})<br>
        </c:forEach>
    </div>
</div>

<%--For displaying Previous link except for the 1st page --%>

<br><Br><Br><Br><Br>

<%--
<nav aria-label="Navigation for countries">


        <c:forEach begin="1" end="${noOfPages}" var="i">
                    <a class="page-link"
                                             href="/Web?&page=${i}&txt=${txt}">${i}&nbsp</a>

        </c:forEach>

</nav>--%>

<br><Br>


<br><br>

   <%--<a class="page-link"
    href="/Web?page=0&txt=${txt}">Result</a>e
<a href='/Web?page=1&txt=${txt}'>1</a>
<a href='/Web?page=2&txt=${txt}'>2</a>
<a href='/Web?page=3&txt=${txt}'>3</a>
<a href='/Web?page=4&txt=${txt}'>4</a>
<a href='/Web?page=5&txt=${txt}'>5</a>
<c:if test="${page lt 10000000}">
    <a class="page-link"
                             href="/Web?page=${page+1}${txt}">Next</a>

</c:if>--%>
<%--<script>
    function openChart() {
        window.open("../../tab-nn.jsp");
    }
</script>--%>

</div>
</body>
</html>


