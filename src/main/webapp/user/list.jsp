<%--
  Created by IntelliJ IDEA.
  User: toanv
  Date: 23/06/2020
  Time: 17:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management Application</title>
    <style>
        .red {
            color: #ff0000;
            padding: 8px;
            margin: 0;
        }
    </style>
</head>
<body>
<center>
    <h1>User Management</h1>
    <h2>
        <a href="/users?action=create">Add New User</a>
    </h2>
    <%
        String sort = "id";
        if (request.getParameter("sort") != null) {
            sort = (String) request.getParameter("sort");
        }
    %>
    <form action="/users" method="GET">
        <input type="text" name="search" value='${requestScope["search"]}' />
        <select name="type">
            <option value="name">Name</option>
            <option value="country">Country</option>
        </select>
        <input type="hidden" name="sort" value="<%= sort %>" />
        <input type="submit" value="Search" />
    </form>
</center>
<div align="center">
    <table border="1" cellpadding="5">
        <caption><h2>List of Users</h2></caption>
        <tr>
            <th><a href="/users?search=<%= request.getAttribute("search") %>&type=<%= request.getAttribute("type") %>&sort=id">ID</a>
                <c:if test='${requestScope.sort      == "id"}'><span class="red">↓</span></c:if></th>
            <th><a href="/users?search=<%= request.getAttribute("search") %>&type=<%= request.getAttribute("type") %>&sort=name">Name</a>
                <c:if test='${requestScope.sort    == "name"}'><span class="red">↓</span></c:if></th>
            <th><a href="/users?search=<%= request.getAttribute("search") %>&type=<%= request.getAttribute("type") %>&sort=email">Email</a>
                <c:if test='${requestScope.sort   == "email"}'><span class="red">↓</span></c:if></th>
            <th><a href="/users?search=<%= request.getAttribute("search") %>&type=<%= request.getAttribute("type") %>&sort=country">Country</a>
                <c:if test='${requestScope.sort == "country"}'><span class="red">↓</span></c:if></th>
            <th>Actions</th>
        </tr>
        <c:forEach var="user" items="${listUser}">
            <tr>
                <td><c:out value="${user.id}"/></td>
                <td><c:out value="${user.name}"/></td>
                <td><c:out value="${user.email}"/></td>
                <td><c:out value="${user.country}"/></td>
                <td>
                    <a href="/users?action=edit&id=${user.id}">Edit</a>
                    <a href="/users?action=delete&id=${user.id}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>