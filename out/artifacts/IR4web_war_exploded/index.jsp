<%--
  Created by IntelliJ IDEA.
  User: siddharthpandey
  Date: 11/25/18
  Time: 3:33 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Maverick Search</title>
<style>

    input {
        outline: none;
    }
    input[type=search] {
        -webkit-appearance: textfield;
        -webkit-box-sizing: content-box;
        font-family: inherit;
        font-size: larger;
    }
    input::-webkit-search-decoration,
    input::-webkit-search-cancel-button {
        display: none;
    }


    input[type=search] {
        background: url('../../images/search.png') no-repeat center;

        padding: 30px 50px 30px 30px;
        width: 100px;
        border: solid 1px #ccc;
        background-size: 50px 50px;
        background-position: left 15 top 10;
        background-color: white;
        -webkit-border-radius: 100em;
        -moz-border-radius: 100em;
        border-radius: 100em;
        font-size: larger;

        -webkit-transition: all .4s;
        -moz-transition: all .4s;
        transition: all .4s;
    }


    /* Demo 2 */
    #demo-2 input[type=search] {
        width: 15px;
        padding-left: 20px;
        color: transparent;
        cursor: pointer;
        content: none;
        font-size: larger;
    }
    #demo-2 input[type=search]:hover {
        background-color: #fff;
        font-size: larger;
    }
    #demo-2 input[type=search]:focus {
        width: 220px;
        padding-left: 80px;
        color: #000;
        background-color: #fff;
        cursor: auto;
        font-size: larger;
    }
    #demo-2 input:-moz-placeholder {
        color: #A9A9A9;
        padding-left: 40px;
        font-size: 1.2em;
    }
    #demo-2 input::-webkit-input-placeholder {
        color: #A9A9A9;
        padding-left: 40px;
        font-size: 1.2em;

    }

    body{
        background-image: url("../../images/nn.jpeg") ;
        background-size: auto;
    }


</style>




  </head>
  <body >
  <form action="/Web?page=1" method="get" id="demo-2">
<br><br><br><br><br><br><br><br><br><br><br><br><br><br>
      <center>
          <input type="search" placeholder="What's Trendin?" name="txt" >

      </center>


  </form>
  </body>
</html>
