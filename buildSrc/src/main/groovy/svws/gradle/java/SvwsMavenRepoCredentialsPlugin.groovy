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

  	void apply(Project project) {
    	this.project = project
		this.addGithubActor()
		this.addGithubToken()
		this.addNexusActor()
		this.addNexusToken()
    }

}
