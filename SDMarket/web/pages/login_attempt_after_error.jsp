<%--
  Created by IntelliJ IDEA.
  User: Maor
  Date: 04-Oct-20
  Time: 11:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@page import="utils.*" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Super Duper Market</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="../css/login.css">
</head>
<body>



<div class="jumbotron text-center">
    <h1>Super Duper Market</h1>
    <p>The perfect place for your shopping!</p>
</div>

<div class="container1">
    <% String usernameFromSession = SessionUtils.getUsername(request);%>
    <% String usernameFromParameter = request.getParameter(ConstantsUtils.USERNAME) != null ? request.getParameter(ConstantsUtils.USERNAME) : "";%>
    <% if (usernameFromSession == null) {%>
    <div class="title" align="center">Login</div>

    <form method="GET" action="login">

        <div class="username">
            <svg class="svg-icon" viewBox="0 0 20 20">
                <path d="M12.075,10.812c1.358-0.853,2.242-2.507,2.242-4.037c0-2.181-1.795-4.618-4.198-4.618S5.921,4.594,5.921,6.775c0,1.53,0.884,3.185,2.242,4.037c-3.222,0.865-5.6,3.807-5.6,7.298c0,0.23,0.189,0.42,0.42,0.42h14.273c0.23,0,0.42-0.189,0.42-0.42C17.676,14.619,15.297,11.677,12.075,10.812 M6.761,6.775c0-2.162,1.773-3.778,3.358-3.778s3.359,1.616,3.359,3.778c0,2.162-1.774,3.778-3.359,3.778S6.761,8.937,6.761,6.775 M3.415,17.69c0.218-3.51,3.142-6.297,6.704-6.297c3.562,0,6.486,2.787,6.705,6.297H3.415z"></path>
            </svg>
            <input type="text" placeholder="Enter Username" name="<%=ConstantsUtils.USERNAME%>" value="<%=usernameFromParameter%>" required />
        </div>
        <% Object errorMessage = request.getAttribute(ConstantsUtils.ERROR_USERNAME);%>
        <% if (errorMessage != null) {%>
        <div id="login-error">
            <label><%=errorMessage%></label>
        </div>
        <% } %>
        <div id="usertypes">
            <input type="radio" id="store-owner" name="usertype" value="StoreOwner" required>
            <label for="store-owner">Store Owner</label>
            <input type="radio" id="customer" name="usertype" value="Customer" required>
            <label for="customer">Customer</label>
        </div>

        <input class="login-btn" type="submit" value="LOGIN"/>

    </form>

    <% } else {%>
    <h1>Welcome back, <%=usernameFromSession%></h1>
    <a href="../chatroom/chatroom.html">Click here to enter the chat room</a>
    <br/>
    <a href="login?logout=true" id="logout">logout</a>
    <% }%>
</div>
</body>
</html>
