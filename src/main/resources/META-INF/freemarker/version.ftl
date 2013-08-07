<html>
<head>

<title>${agreement.name} ${version.name} -  Version Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">
<div class ="container">
<#include "sidebar.ftl">
<div class = "content">

<h2>${agreement.name} ${version.name} Version</h2>
<div class="box fixed-box">
<h1>Individual Agreement Content</h1>
${individualContent}
</div>
<div class="box fixed-box">
<h1>Corporate Agreement Content</h1>
${corporateContent}
</div>
</div>
</div>
</body>
</html>
