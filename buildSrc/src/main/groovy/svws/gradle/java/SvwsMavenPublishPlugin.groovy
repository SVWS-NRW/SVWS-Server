package svws.gradle.java


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository

/**
 * Dieses Plugin steuert die Veröffentlichung von SVWS-Artefakten
 * in Maven-Repositories.
 */
class SvwsMavenPublishPlugin extends SvwsMavenRepoCredentialsPlugin implements Plugin<Project> {

	void chooseMavenRepository(Project project) {
		def nexus_user = project.ext.getNexusActor()
		def nexus_token = project.ext.getNexusToken()

		if (nexus_user?.trim() && nexus_token?.trim()) {
			project.publishing.repositories {
				maven {
					name = "svwssnapshots"
					url = SVWS_MAVEN_SNAPSHOTS_URL
					allowInsecureProtocol = true
					credentials {
						username = nexus_user
						password = nexus_token
					}
				}
				maven {
					name = "svwsreleases"
					url = SVWS_MAVEN_RELEASES_URL
					allowInsecureProtocol = true
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
		}
	}

  	void apply(Project project) {
    	this.project = project
		super.apply(project)
		project.pluginManager.apply "maven-publish"
		this.chooseMavenRepository(project)

		project.tasks.register('publishSnapshot') {
			group = "publishing"
			description = 'Publishes all Maven publications to the Nexus Maven snapshot repository.'
			dependsOn project.tasks.withType(PublishToMavenRepository).matching {
				it.repository == project.publishing.repositories.svwssnapshots
			}
		}
		project.tasks.register('publishRelease') {
			group = "publishing"
			description = 'Publishes all Maven publications to the Nexus Maven release repository.'
			dependsOn project.tasks.withType(PublishToMavenRepository).matching {
				it.repository == project.publishing.repositories.svwsreleases
			}
		}
		project.tasks.register('publishReleaseGithub') {
			group = "publishing"
			description = 'Publishes all Maven publications to the Nexus Maven release repository AND to Github repository.'
			dependsOn project.tasks.withType(PublishToMavenRepository).matching {
				it.repository == project.publishing.repositories.svwsreleases ||
				it.repository == project.publishing.repositories.githubpackages
			}
		}
    }

}
