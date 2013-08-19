<html>
<head>

<title>${agreement.name} - Versions Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">

<div class ="container">
	<div class="list">
		<h2>Agreement Versions</h2>

		<ul>
		<#list versions as version>
			<li><a href="/agreements/${version.agreement.id}/versions/${version.id}">${version.name}</a></li>
		</#list>
		</ul>
	</div>

	<div class="content">
		<form method="POST" action="/agreements/${agreement.id}/versions">
			<h2>Create a new version</h2>

			<fieldset>
				<label for="name">Name:</label>
				<input type="text" class="text" name="name" id ="name">

				<label for="individualContent">Individual Agreement Content:</label>
				<textarea name="individualContent" id="individualContent" class="text">Use Markdown in here</textarea>

				<label for="corporateContent">Corporate Agreement Content:</label>
				<textarea name="corporateContent" id="corporateContent" class="text">Use Markdown in here</textarea>

				<input type="hidden" name="${(_csrf.parameterName)!}" value="${(_csrf.token)!}"/>
				<button type="submit">Create</button>
			</fieldset>
		</form>
	</div>
</div>

</body>
</html>
