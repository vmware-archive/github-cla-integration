<html>
<head>

<title>${version.agreement.name} ${version.name} -  Version Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">

<div class ="container">
	<h1>${version.agreement.name} ${version.name} Version</h1>

	<div class="agreement-content">
		<h2>Individual Agreement Content</h2>
		<div class="markdown">${individualContent}</div>
	</div>

	<div class="agreement-content">
		<h2>Corporate Agreement Content</h2>
		<div class="markdown">${corporateContent}</div>
	</div>
</div>

</body>
</html>
