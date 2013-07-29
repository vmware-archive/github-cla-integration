<html>
<head>
	<title>Go Pivotal, Inc. Contributor License Agreement Administration</title>
</head>
<body>

	<img src="${avatar_url}&amp;s=200" /> ${name}
	<a href="/logout">Logout</a>

	<h2>Agreements</h2>
	<#list agreements as agreement>
		<a href="/admin/agreement/${agreement.id}">${agreement.name} (${agreement.type.cap_first})</a>
	</#list>

</body>
</html>
