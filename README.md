# GitHub CLA Integration

This project is a self-hostable application that provides an easy way for contributors to sign Individual and Corporate Contributor License Agreements (CLAs).  This information is then used to place CLA status information in the pull requests and commits of your projects.  The content of the CLAs presented to signatories is customizable and versioned allowing for evolution over time.

In order to facilitate self-hosting, the application is designed to work in [Cloud Foundry][].


## Requirements

### GitHub Application Registration
Since this application is self-hosted, each deployment must have it's own GitHub application credentials.  To register a new application go to <https://github.com/settings/applications/new> and fill in the required information.  You should use the same URL for both the 'Homepage URL' and 'Authorization Callback URL'.  Be sure to note the values for **Client ID** and **Client Secret** as they will be used later.

### PostgreSQL Database
The application stores its internal data model in a [PostgreSQL][] database.  This database needs to be provisioned before the application is started, but the application manages the creation and modification of the schemas itself so no other setup is needed. Since the application is designed to work in Cloud Foundry, it is highly recommended that you use a database-as-a-service provided by Cloud Foundry.

### Java, Maven
The application is written in Java and packaged as a self executable JAR file. This enables it to run in anywhere that Java is available. Building the application (required for deployment) requires [Maven][].

### Environment Variables
Since the application is designed to work in a PaaS environment, all configuration is done with environment variables.

| Key | Description
| --- | -----------
| `ENCRYPTION_KEY` | The key used to encrypt data in the database.  The key must be at least 50 characters long to deter brute-force attacks.  It is recommend that you use a tool such as [1Password][] to generate this key.
| `GITHUB_CLIENT_ID` | The Client ID assigned to your registered application.
| `GITHUB_CLIENT_SECRET` | The Client Secret assigned to your registered application.


## Deployment
_The following instructions assume that you have [created an account][cloud-foundry-account] and [installed the `cf` command line tool][]._

In order to automate the deployment process as much as possible, the project contains a Cloud Foundry [manifest][].  To deploy run the following commands:

```bash
mvn -Dmaven.test.skip=true package
cf push --no-start
cf set-env gopivotal-cla ENCRYPTION_KEY <value>
cf set-env gopivotal-cla GITHUB_CLIENT_ID <value>
cf set-env gopivotal-cla GITHUB_CLIENT_SECRET <value>
cf start
```

## Developing
The project is set up as a Maven project and doesn’t have any special requirements beyond that.

### Localtunnel
The one gotcha for running the application locally (i.e. while developing) is that the interesting bits of the application all require the OAuth dance.  To complete this dance, the GitHub infrastructure must be able to make a `POST` request to the originating server.  Generally when developing locally, your servlet container is not exposed to the internet so this dance will fail.  To expose your server to the internet (and enable the OAuth dance to complete), you should use <http://www.localtunnel.me>.  Remember to update your [application's configuration](#github-application-registration) to point at exposed `localtunnel` URL.


## License
The project is released under version 2.0 of the [Apache License][].


[1Password]: https://agilebits.com/onepassword
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[Cloud Foundry]: http://run.pivotal.io
[cloud-foundry-account]: http://docs.cloudfoundry.com/docs/dotcom/getting-started.html#signup
[installed the `cf` command line tool]: http://docs.cloudfoundry.com/docs/dotcom/getting-started.html#install-cf
[manifest]: manifest.yml
[Maven]: http://maven.apache.org
[new-application]: https://github.com/settings/applications/new
[PostgreSQL]: http://www.postgresql.org
[Apache Tomcat]: http://tomcat.apache.org
