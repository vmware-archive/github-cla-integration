<html>
<head>

<title>Agreements Administration</title>

</head>
<body>

<#include "header.ftl">

<h1>Contributor License Agreements</h1>

<ul>
<#list agreements as agreement>
	<li><a href="/agreements/${agreement.id}/versions">${agreement.name}</a></li>
</#list>
</ul>

<form method="POST" action="/agreements">
	<fieldset>
		<legend>Create new agreement</legend>

		<label for="name">Name:</label><input type="text" name="name">
		<button type="submit">Create</button>
	</fieldset>
</form>

</body>
</html>
