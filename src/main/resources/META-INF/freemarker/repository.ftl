<html>
<head>

<title>Sign the GoPivotal, Inc. ${repository.agreement.name} Contributor License Agreement</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">

<div class ="container">

	<div class="agreement-selector">
		<div id="individual">
			<form action="/${repository.name}/individual">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<button type="submit">Sign the Individual Contributor License Agreement</button>
			</form>
		</div>

		<div id="corporate">
			<form action="/${repository.name}/corporate">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<button type="submit">Sign the Corporate Contributor License Agreement</button>
			</form>
		</div>
	</div>

</div>

</body>
</html>
