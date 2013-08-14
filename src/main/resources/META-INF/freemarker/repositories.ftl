<html>
<head>

<title>Repository Agreement Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">
<div class="container">
	<div class="list">
		<h2>Linked Repositories</h2>

		<ul>
		<#list linkedRepositories as linkedRepository>
			<li class="linked-repository">
				${linkedRepository.name} | ${linkedRepository.agreement.name}<br />
				<span class="url">${hrefPrefix}/${linkedRepository.name}</span>
			</li>
		</#list>
		</ul>
	</div>

	<div class="content">
		<form method="POST" action="/repositories">
			<h2>Link a repository to an agreement</h2>

			<fieldset>
				<label for="repository">Repository:</label>
				<select name="repository" required="true">
				<#list candidateRepositories as candidateRepository>
					<option value="${candidateRepository.fullName}">${candidateRepository.fullName}</option>
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
	</div>
</div>

</body>
</html>
