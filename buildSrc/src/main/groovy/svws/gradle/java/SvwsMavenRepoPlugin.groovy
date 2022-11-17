package svws.gradle.java

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Dieses Plugin steuert die Verwendung von Maven-Repositories
 * im SVWS zum Download von Dependencies.
 */
class SvwsMavenRepoPlugin extends SvwsMavenRepoCredentialsPlugin implements Plugin<Project> {

	// Extension zur Konfiguration des Plugins
	SvwsMavenRepoPluginExtension extension

	/**
	 * Legt die Maven-Repos für den Download von dependencies fest.
	 * Standard: Nutzung von Maven-Central.
	 * Alternativ: SVWS-Nexus als Maven-Proxy, sofern entsprechende Zugangsdaten
	 * hinterlegt sind.
	 *
	 * @param project Projekt
	 */
	void chooseMavenRepository() {
		def nexus_user = project.ext.getNexusActor()
		def nexus_token = project.ext.getNexusToken()
		def maven_proxy_url = this.extension.getNexusMavenCentralProxyRepositoryUrl()

		if (nexus_user?.trim() && nexus_token?.trim() && maven_proxy_url.trim()) {
			project.logger.info('Projekt {} verwendet das Artefakt-Repository: SVWS-Nexus', project.name)
			project.repositories {
				maven {
					name = "svwsmavencentral"
					url = this.extension.getNexusMavenCentralProxyRepositoryUrl()
					allowInsecureProtocol = true
					credentials {
						username = nexus_user
						password = nexus_token
					}
				}
				maven {
					name = "githubpackages"
					url = this.extension.getGithubMavenPackagesUrl()
				}
			}
		} else {
			project.repositories {
				project.logger.info('Projekt {} verwendet das Artefakt-Repository: Maven-Central', project.name)
				mavenCentral()
				maven {
					name = "githubpackages"
					url = this.extension.getGithubMavenPackagesUrl()
				}
			}
		}
	}

  	void apply(Project project) {
		super.apply(project)
		this.project = project
		this.extension = project.getExtensions()
			.create("svwsmavenrepo", SvwsMavenRepoPluginExtension.class)

		project.gradle.projectsEvaluated {
			this.chooseMavenRepository()
		}
    }

}
