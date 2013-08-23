<html>
<head>

<title>Repository Agreement Administration</title>
<link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">
<script type='text/javascript'>
   function load(){
    document.getElementById('repository').selectedIndex = -1;
    document.getElementById('agreement').selectedIndex = -1;
  }
</script>
</head>
<body onload="load()">

<#include "header.ftl">
<div class="container">
	<div class="list">
		<h2>Linked Repositories</h2>

		<ul>
		<#list linkedRepositories as linkedRepository>
			<li class="linked-repository">
				${linkedRepository.name} | ${linkedRepository.agreement.name}<br />
				<a href="/${linkedRepository.name}" class="url">${hrefPrefix}/${linkedRepository.name}</a>
			</li>
		</#list>
		</ul>
	</div>

	<div class="content">
		<form method="POST" action="/repositories">
			<h2>Link a repository to an agreement</h2>

			<fieldset>
				<label for="repository">Repository:</label>
				<select id="repository" name="repository" required="true">
				<#list candidateRepositories as candidateRepository>
					<option value="${candidateRepository.fullName}">${candidateRepository.fullName}</option>
				</#list>
				</select>
				<label for="agreement">Agreement:</label>
				<select id="agreement" name="agreement" required="true">
				<#list candidateAgreements as candidateAgreement>
					<option value="${candidateAgreement.id}">${candidateAgreement.name}</option>
				</#list>
				</select>

				<input type="hidden" name="${(_csrf.parameterName)!}" value="${(_csrf.token)!}"/>
				<button type="submit">Link</button>
			</fieldset>
		</form>
	</div>
</div>

</body>
</html>
