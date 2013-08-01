<html>
<head>

<title>${agreement.name} ${version.name} -  Version Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">

<h1>${agreement.name} ${version.name} Version</h1>

<h2>Individual Agreement Content</h2>
${individualContent}

<h2>Corporate Agreement Content</h2>
${corporateContent}

</body>
</html>
