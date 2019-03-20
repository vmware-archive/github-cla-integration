# GitHub CLA Integration

This project is a self-hostable application that provides an easy way for contributors to sign Individual and Corporate Contributor License Agreements (CLAs).  This information is then used to place CLA status information in the pull requests and commits of your projects.  The content of the CLAs presented to signatories is customizable and versioned allowing for evolution over time.  Information about signatories is emailed to a configurable email address for record keeping.

In order to facilitate self-hosting, the application is designed to work in a PaaS environment such as [Cloud Foundry][] or [Heroku][].


## Requirements

### GitHub Application Registration
Since this application is self-hosted, each deployment must have it's own GitHub application credentials.  To register a new application go to <https://github.com/settings/applications/new> and fill in the required information.  You should use the same URL for both the 'Homepage URL' and 'Authorization Callback URL'.  Be sure to note the values for **Client ID** and **Client Secret** as they will be used later.

### PostgreSQL Database
The application stores its internal data model in a [PostgreSQL][] database.  This database needs to be provisioned before the application is started, but the application manages the creation and modification of the schemas itself so no other setup is needed.  Since the application is designed to work in a PaaS environment, it is highly recommended that you use a database-as-a-service available from your PaaS provider.

### Java, Maven, Servlet Container
The application is written and Java and packaged as a simple WAR file.  This enables it to run in any standard Java Servlet container with both Cloud Foundry and Heroku support.  Building the application (required for deployment) requires [Maven][].

### Environment Variables
Since the application is designed to work in a PaaS environment, all configuration is done with environment variables.

| Key | Description
| --- | -----------
| `ADMIN_EMAIL_DOMAINS` | A comma delimited list of domains.  These domains are compared against the list of email addresses that a potential administrator has in their profile and if there is no match, the user is not allowed to administer the instance.  The domains should not have an `@` symbol before them (e.g `gopivotal.com,pivotallabs.com`).
| `DATABASE_URL` | The URL used to connect to a PostgreSQL database.  This URL should include the username and password used as credentials for the database connection and be in the form of `postgres://<username>[:<password>]@<host>[:<port>]/<database>[?<options>]`.
| `ENCRYPTION_KEY` | The key used to encrypt data in the database.  The key must be at least 50 characters long to deter brute-force attacks.  It is recommend that you use a tool such as [1Password][] to generate this key.
| `GITHUB_CLIENT_ID` | The Client ID assigned to your registered application.
| `GITHUB_CLIENT_SECRET` | The Client Secret assigned to your registered application.


## Deployment

### Cloud Foundry
_The following instructions assume that you have [created an account][cloud-foundry-account] and [installed the `cf` command line tool][]._

In order to automate the deployment process as much as possible, the project contains a Cloud Foundry [manifest][].  To deploy run the following commands:

```bash
mvn -Dmaven.test.skip=true package
cf push --no-start
cf set-env gopivotal-cla ADMIN_EMAIL_DOMAINS <value>
cf set-env gopivotal-cla DATABASE_URL <value>
cf set-env gopivotal-cla ENCRYPTION_KEY <value>
cf set-env gopivotal-cla GITHUB_CLIENT_ID <value>
cf set-env gopivotal-cla GITHUB_CLIENT_SECRET <value>
cf start
```

### Heroku
_The following instructions assume that you have [created an account][heroku-account] and [installed the `heroku` command line tool][]._

To deploy run the following commands:

```bash
heroku create gopivotal-cla
heroku addons:add heroku-postgresql:dev
heroku config:set ADMIN_EMAIL_DOMAINS=<value>
heroku config:set DATABASE_URL=$(heroku config:get HEROKU_POSTGRESQL_GOLD_URL)
heroku config:set ENCRYPTION_KEY=<value>
heroku config:set GITHUB_CLIENT_ID=<value>
heroku config:set GITHUB_CLIENT_SECRET=<value>
git push heroku master
```

## Developing
The project is set up as a Maven WAR project and doesn't have any special requirements beyond that.  An IDE such as the [SpringSource Tool Suite][] and a servlet container such as [Apache Tomcat][] are the recommended development environments.

### Localtunnel
The one gotcha for running the application locally (i.e. while developing) is that the interesting bits of the application all require the OAuth dance.  To complete this dance, the GitHub infrastructure must be able to make a `POST` request to the originating server.  Generally when developing locally, your servlet container is not exposed to the internet so this dance will fail.  To expose your server to the internet (and enable the OAuth dance to complete), you should use <https://www.localtunnel.me>.  Remember to update your [application's configuration](#github-application-registration) to point at exposed `localtunnel` URL.


## License
The project is released under version 2.0 of the [Apache License][].


[1Password]: https://agilebits.com/onepassword
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[applications]: https://github.com/settings/applications
[Cloud Foundry]: https://run.pivotal.io
[cloud-foundry-account]: https://docs.cloudfoundry.com/docs/dotcom/getting-started.html#signup
[installed the `cf` command line tool]: https://docs.cloudfoundry.com/docs/dotcom/getting-started.html#install-cf
[installed the `heroku` command line tool]: https://devcenter.heroku.com/articles/quickstart#step-2-install-the-heroku-toolbelt
[Heroku]: https://www.heroku.com
[heroku-account]: https://id.heroku.com/signup
[manifest]: manifest.yml
[Maven]: https://maven.apache.org
[new-application]: https://github.com/settings/applications/new
[PostgreSQL]: https://www.postgresql.org
[SpringSource Tool Suite]: https://www.springsource.org/sts
[Apache Tomcat]: https://tomcat.apache.org
