package svws.gradle.java

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Dieses Plugin steuert die Verwendung von Maven-Repositories
 * im SVWS zum Download von Dependencies.
 */
class SvwsMavenRepoPlugin extends SvwsMavenRepoCredentialsPlugin implements Plugin<Project> {

	void chooseMavenRepository(Project project) {
		def nexus_user = project.ext.getNexusActor()
		def nexus_token = project.ext.getNexusToken()

		if (nexus_user?.trim() && nexus_token?.trim()) {
			project.logger.lifecycle('Projekt {} verwendet das Artefakt-Repository: SVWS-Nexus', project.name)
			project.repositories {
				maven {
					name = "svwsmavencentral"
					url = SVWS_MAVEN_CENTRAL_PROXY_URL
					allowInsecureProtocol = true //TODO: Option entfernen, wenn von lokaler entwicklung auf svws-nexus umgestellt wird
					credentials {
						username = nexus_user
						password = nexus_token
					}
				}
				maven {
					name = "githubpackages"
					url = GITHUB_PACKAGES_URL
					credentials {
						username = project.ext.getGithubActor()
						password = project.ext.getGithubToken()
					}
				}
			}
		} else {
			project.repositories {
				project.logger.lifecycle('Projekt {} verwendet das Artefakt-Repository: Maven-Central', project.name)
				mavenCentral()
				maven {
					name = "githubpackages"
					url = GITHUB_PACKAGES_URL
					credentials {
						username = project.ext.getGithubActor()
						password = project.ext.getGithubToken()
					}
				}
			}
		}

	}

  	void apply(Project project) {
    	this.project = project
		super.apply(project)
		this.chooseMavenRepository(project)
    }

}
