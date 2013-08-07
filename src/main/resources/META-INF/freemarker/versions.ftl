<html>
<head>

<title>${agreement.name} - Versions Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">
<div class ="container">
<#include "sidebar.ftl">
<div class = "content">
<div class = "box">
<h1>${agreement.name} Agreement Versions</h1>

<ul>
<#list versions as version>
	<li><a href="/agreements/${version.agreementId}/versions/${version.id}">${version.name}</a></li>
</#list>
</ul>
</div>
<h2>Create a new agreement version</h2>
<div class = "outer-box">
<div class = "box">
<form method="POST" action="/agreements/${agreement.id}/versions">
		<label for="name">Name:</label><input type="text" class="text" name="name" id="name">
		<label for="individualContent">Individual Agreement Content:</label><textarea name="individualContent" class="text">Use Markdown in here</textarea>
		<label for="corporateContent">Corporate Agreement Content:</label><textarea name="corporateContent" class="text">Use Markdown in here</textarea>
		<button type="submit">Create</button>
</form>
</div>
</div>
</div>
</div>
</body>
</html>
