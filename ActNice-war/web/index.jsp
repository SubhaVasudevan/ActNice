<%-- 
    Document   : index.jsp
    Created on : Dec 15, 2015, 7:32:13 AM
    Author     : Subha
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ACT NICE!</title>
    </head>
    <body bgcolor="#F5F6CE">
        <h1 align="center" >ACT NICE!</h1>
        
        <form method="post" action="ActNiceServlet" >
            <div align="center">
                Artist1: <input type="text" name="actor1"> &nbsp; &nbsp; &nbsp;
                Artist2: <input type="text" name="actor2"> <br>
                <br>
                <br>
                <input type="submit" value="Get Common Movies">  </input>
            </div>

        </form>
    </body>
</html>