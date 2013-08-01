<html>
<head>

<title>${agreement.name} - Versions Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">

<h1>${agreement.name} Agreement Versions</h1>

<ul>
<#list versions as version>
	<li><a href="/agreements/${version.agreementId}/versions/${version.id}">${version.name}</a></li>
</#list>
</ul>

<form method="POST" action="/agreements/${agreement.id}/versions">
	<fieldset>
		<legend>Create a new agreement version</legend>

		<label for="name">Name:</label><input type="text" name="name" id="name">
		<label for="individualContent">Individual Agreement Content:</label><textarea name="individualContent">Use Markdown in here</textarea>
		<label for="corporateContent">Corporate Agreement Content:</label><textarea name="corporateContent">Use Markdown in here</textarea>
		<button type="submit">Create</button>
	</fieldset>
</form>

</body>
</html>
