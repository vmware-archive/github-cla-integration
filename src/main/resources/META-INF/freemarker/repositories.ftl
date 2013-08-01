<html>
<head>

<title>Repository Agreement Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">

<h1>Linked Repositories</h1>

<table>
	<tbody>
	<#list repositoryMapping?keys?sort as repository>
		<tr>
			<td>${repository}</td><td>${repositoryMapping[repository]}</td>
		</tr>
	</#list>
	</tbody>
</table>

<form method="POST" action="/repositories">
	<fieldset>
		<legend>Link a repository to an agreement</legend>

		<label for="repository">Repository:</label>
		<select name="repository" required="true">
		<#list candidateRepositories as candidateRepository>
			<option value="${candidateRepository}">${candidateRepository}</option>
		</#list>
		</select>
		<label for="agreement">Agreement:</label>
		<select name="agreement" required="true">
		<#list candidateAgreements as candidateAgreement>
			<option value="${candidateAgreement.id}">${candidateAgreement.name}</option>
		</#list>
		</select>

		<button type="submit">Link</button>
	</fieldset>
</form>

</body>
</html>
