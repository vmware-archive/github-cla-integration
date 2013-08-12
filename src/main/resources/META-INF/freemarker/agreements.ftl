<html>
<head>

<title>Agreements Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">
<div class="container">
<#include "sidebar.ftl">
<div class = "content">
<div class ="box">
<h1>Contributor License Agreements</h1>

<ul>
<#list agreements as agreement>
	<li><a href="/agreements/${agreement.id}/versions">${agreement.name}</a></li>
</#list>
</ul>
</div>
<h2>Create new agreement</h2>
<div class = "box-outer">
<div class ="box">
<form method="POST" action="/agreements">

		<label for="name">Name:</label><input type="text" class="text" name="name">
		<button type="submit">Create</button>
</form>
</div>
</div>
</div>
</div>
</body>
</html>
