<html>
<head>

<title>Repository Agreement Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>
<#include "header.ftl">
<div class="container">
<#include "sidebar.ftl">
<div class = "content">
<div class = "box">
<h1>Linked Repositories</h1>
<br style="clear: both;" /> 
<table class="repositories_table">
	<tbody>
	<#list repositoryMapping?keys?sort as repository>
		<tr>
			<td>${repository} <p class="url">${hrefPrefix}/${repository}</p> </td>
			<td>${repositoryMapping[repository]}</td>
		</tr>
	</#list>
	</tbody>
</table>
</div>
<h2>Link a repository to an agreement</h2>
<div class="box-outer">
<div class = "box">
<form method="POST" action="/repositories">
		

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
</form>
</div>
</div>
</div>
</div>
</body>
</html>
