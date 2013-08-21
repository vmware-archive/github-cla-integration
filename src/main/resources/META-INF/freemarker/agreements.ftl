<html>
<head>

<title>Agreements Administration</title>
<link rel="stylesheet" type="text/css" href="/resources/styles/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="/resources/styles/style.css">

</head>
<body>

<#include "header.ftl">

<div class="container">
	<div class="list">
		<h2>Agreements</h2>

		<ul>
		<#list agreements as agreement>
			<li><a href="/agreements/${agreement.id}/versions">${agreement.name}</a></li>
		</#list>
		</ul>
	</div>

	<div class="content">
		<form method="POST" action="/agreements">
			<h2>Create a new agreement</h2>

			<fieldset>
				<label for="name">Name:</label>
				<input type="text" class="text" name="name">

				<button type="submit">Create</button>
			</fieldset>
		</form>
	</div>
</div>

</body>
</html>
