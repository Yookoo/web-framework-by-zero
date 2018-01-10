<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="BASE" value="${pageContext.request.contextPath }" />
<html>
<head>
<title>客户管理 - 客户列表</title>
</head>
<body>
	<h1>客户管理 - 客户列表</h1>
	<table border="1">
		<thead>
			<tr>
				<th>客户名称</th>
				<th>联系人</th>
				<th>电话号码</th>
				<th>邮箱地址</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="customer" items="${customerList }">
			<tr>
				<td>${customer.name }</td>
				<td>${customer.contact }</td>
				<td>${customer.telephone }</td>
				<td>${customer.email }</td>
				<td>
					<a href="${BASE }/customer/edit?id=${name.id}">编辑</a>
					<a href="${BASE }/customer/delete?id=${name.id}">删除</a>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>