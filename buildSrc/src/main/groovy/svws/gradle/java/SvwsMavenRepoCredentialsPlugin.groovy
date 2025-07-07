package svws.gradle.java

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Diese Klasse dient der Ermittlung von Credentials und URLs
 * für den Zugriff auf Maven-Repositories in der SVWS-Entwicklung.
 */
abstract class SvwsMavenRepoCredentialsPlugin implements Plugin<Project> {

	def project

	void addGithubActor() {
		project.ext.getGithubActor = { ->
			if (project.hasProperty('github_actor'))
				return project.github_actor
			def username = System.getenv("GITHUB_ACTOR")
			if (username != null)
				return username
			project.logger.info('GitHub kann nicht für Publishing verwendet werden. Der Github-Benutzer wurde weder in USERHOME/.gradle/gradle.properties als github_actor, noch als Umgebungsvariable GITHUB_ACTOR festgelegt!')
			return null
		}
	}

	void addGithubToken() {
		project.ext.getGithubToken = { ->
			if (project.hasProperty('github_token'))
				return project.github_token
			def token = System.getenv("GITHUB_TOKEN")
			if (token != null)
				return token
			project.logger.info('GitHub kann nicht für Publishing verwendet werden. Der Github-Token wurde weder in USERHOME/.gradle/gradle.properties als github_token, noch als Umgebungsvariable GITHUB_TOKEN festgelegt!')
			return null
		}
	}

	void addNexusActor() {
		project.ext.getNexusActor = { ->
			if (project.hasProperty('nexus_actor'))
				return project.nexus_actor
			def username = System.getenv("NEXUS_ACTOR")
			if (username != null)
				return username
			project.logger.info('Info: Der Nexus Repository Manager kann nicht genutzt werden, weil die Zugangsdaten nicht hinterlegt sind. Der Nexus-Benutzer wurde weder in USERHOME/.gradle/gradle.properties als nexus_actor, noch als Umgebungsvariable NEXUS_ACTOR festgelegt!')
			return null
		}
	}

	void addNexusToken() {
		project.ext.getNexusToken = { ->
			if (project.hasProperty('nexus_token'))
				return project.nexus_token
			def token = System.getenv("NEXUS_TOKEN")
			if (token != null)
				return token
			project.logger.info('Info: Der Nexus Repository Manager kann nicht genutzt werden, weil die Zugangsdaten nicht hinterlegt sind. Der Nexus-Token wurde weder in USERHOME/.gradle/gradle.properties als nexus_token, noch als Umgebungsvariable NEXUS_TOKEN festgelegt!')
			return null
		}
	}

	void addOssrhActor() {
		project.ext.getOssrhActor = { ->
			if (project.hasProperty('ossrh_actor'))
				return project.ossrh_actor
			def username = System.getenv("OSSRH_ACTOR")
			if (username != null)
				return username
			project.logger.info('Info: Der Ossrh Repository Manager kann nicht genutzt werden, weil die Zugangsdaten nicht hinterlegt sind. Der Ossrh-Benutzer wurde weder in USERHOME/.gradle/gradle.properties als nexus_actor, noch als Umgebungsvariable NEXUS_ACTOR festgelegt!')
			return null
		}
	}

	void addOssrhToken() {
		project.ext.getOssrhToken = { ->
			if (project.hasProperty('ossrh_token'))
				return project.ossrh_token
			def token = System.getenv("OSSRH_TOKEN")
			if (token != null)
				return token
			project.logger.info('Info: Der Ossrh Repository Manager kann nicht genutzt werden, weil die Zugangsdaten nicht hinterlegt sind. Der Ossrh-Token wurde weder in USERHOME/.gradle/gradle.properties als nexus_token, noch als Umgebungsvariable NEXUS_TOKEN festgelegt!')
			return null
		}
	}

	void addNexusNpmBase64Token() {
		def nexus_actor = project.ext.getNexusActor()
		def nexus_token = project.ext.getNexusToken()
		project.ext.getNexusNpmToken = { ->
			if ((nexus_actor == null) || (nexus_token == null))
				return null
			return (nexus_actor + ':' + nexus_token).bytes.encodeBase64().toString()
		}
	}

	void addNpmToken() {
		project.ext.getNpmToken = { ->
			if (project.hasProperty('npm_token'))
				return project.npm_token
			def token = System.getenv("NPM_TOKEN")
			if (token != null)
				return token
			project.logger.info('Info: Das Npm Repository kann nicht genutzt werden, weil die Zugangsdaten nicht hinterlegt sind. Der Npm-Token wurde weder in USERHOME/.gradle/gradle.properties als npm_token, noch als Umgebungsvariable NPM_TOKEN festgelegt!')
			return null
		}
	}


	@Override
	void apply(Project project) {
		this.project = project
		this.addGithubActor()
		this.addGithubToken()
		this.addNexusActor()
		this.addNexusToken()
		this.addOssrhActor()
		this.addOssrhToken()
		this.addNexusNpmBase64Token()
		this.addNpmToken()
	}

}
