<html>
<head>
	<title>${agreement.name} (${agreement.type.displayString}) Contributor License Agreement Administration</title>
</head>
<body>
	<#include "header.ftl">

	<h1>${agreement.name} (${agreement.type.displayString}) Contributor License Agreement</h1>

	<h2>Versions</h2>
	<ul>
	<#list versions as version>
		<li><a href="/admin/agreements/${version.agreementId}/versions/${version.id}">${version.version}</a></li>
	</#list>
	</ul>

	<hr />

	<form method="POST" action="/admin/agreements/${agreement.id}/versions">
		<h3>Create a new Version</h3>
		<label for="version">Version:</label><input type="text" name="version" id="version">
		<br/>

		<label for="content">Content:</label><textarea name="content" id="content" rows="10" cols="50">Use Markdown in here</textarea>

		<button type="submit">Create</button>

	</form>

</body>
</html>
