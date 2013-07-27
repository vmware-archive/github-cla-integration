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
| `DATABASE_URL` | The URL used to connect to a PostgreSQL database.  This URL should include the username and password used as credentials for the database connection and be in the form of `postgresql://<username>[:<password>]@<host>[:<port>]/<database>`.
| `GITHUB_CLIENT_ID` | The Client ID assigned to your registered application.
| `GITHUB_CLIENT_SECRET` | The Client Secret assigned to your registered application.


## Deployment

### Cloud Foundry
_The following instructions assume that you have [created an account][] and [installed the `cf` command line tool][]._

In order to automate the deployment process as much as possible, the project contains a Cloud Foundry [manifest][].  This means that deployment can be as simple as:

```bash
mvn -Dmaven.test.skip=true package
cf push --no-start
cf set-env gopivotal-cla DATABASE_URL <value>
cf set-env gopivotal-cla GITHUB_CLIENT_ID <value>
cf set-env gopivotal-cla GITHUB_CLIENT_SECRET <value>
cf push
```

## License
The project is released under version 2.0 of the [Apache License][].


[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[applications]: https://github.com/settings/applications
[Cloud Foundry]: http://run.pivotal.io
[created an account]: http://docs.cloudfoundry.com/docs/dotcom/getting-started.html#signup
[installed the `cf` command line tool]: http://docs.cloudfoundry.com/docs/dotcom/getting-started.html#install-cf
[Heroku]: https://www.heroku.com
[manifest]: manifest.yml
[Maven]: http://maven.apache.org
[new-application]: https://github.com/settings/applications/new
[PostgreSQL]: http://www.postgresql.org
