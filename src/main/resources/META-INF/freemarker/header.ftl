<div class="header">
	<div class="container">
		<a id="logo" href="/"><img src="/resources/images/logo_pivotal.png" /></a>

		<ul id="user-links">
			<#if user??>

			<li class="name"><img height="20" width="20" src="${user.avatarUrl}&amp;s=40">${user.name}</li>
			<li><a href="/logout"><i class="icon-signout icon-large"></i></a></li>

			<#else>

			<li>Administer:</li>
			<li><a href="/agreements">Agreements</a></li>
			<li><a href="/repositories">Repositories</a></li>

			</#if>
		</ul>
	</div>
</div>

