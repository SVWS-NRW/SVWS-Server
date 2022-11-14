package svws.gradle.java


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository

/**
 * Dieses Plugin steuert die Veröffentlichung von SVWS-Artefakten
 * in Maven-Repositories.
 */
class SvwsMavenPublishPlugin extends SvwsMavenRepoCredentialsPlugin implements Plugin<Project> {

	// Extension zur Konfiguration des Plugins
	SvwsMavenPublishPluginExtension extension

	/**
	 * Wendet die Konfiguration für das Signieren von JARs an.
	 */
	void configSigning() {
		// Das Signieren von JARs soll nur bei RELEASES erfolgen, nicht bei SNAPSHOTS.
		project.ext.isReleaseVersion = !project.version.endsWith('SNAPSHOT')
		project.signing {
			required { project.ext.isReleaseVersion && project.gradle.taskGraph.hasTask("publish") }
			sign project.publishing.publications
		}
	}

	/**
	 * Legt die Maven-Repos für den publish-Task fest.
	 *
	 * @param project Projekt
	 */
	void chooseMavenRepository() {
		def nexus_user = project.ext.getNexusActor()
		def nexus_token = project.ext.getNexusToken()

		if (nexus_user?.trim() && nexus_token?.trim()) {
			project.publishing.repositories {
				maven {
					name = "svwssnapshots"
					url = this.extension.getNexusSnapshotRepositoryUrl()
					allowInsecureProtocol = true
					credentials {
						username = nexus_user
						password = nexus_token
					}
				}
				maven {
					name = "svwsreleases"
					url = this.extension.getNexusReleasesRepositoryUrl()
					allowInsecureProtocol = true
					credentials {
						username = nexus_user
						password = nexus_token
					}
				}
				maven {
					name = "githubreleases"
					url = this.extension.getGithubReleasesRepositoryUrl()
					credentials {
						username = project.ext.getGithubActor()
						password = project.ext.getGithubToken()
					}
				}
			}
		} else {
			project.logger.warn('WARNUNG: Der Nexus Repository Manager kann nicht für die Veröffentlichung von SVWS-Artefakten genutzt werden, weil die Zugangsdaten nicht hinterlegt sind.')
		}
	}

	void registerPublishingTasks(){
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
		project.tasks.register('publishReleaseAll') {
			group = "publishing"
			description = 'Publishes all Maven publications to the Nexus Maven release repository AND to Github repository additionally.'
			dependsOn project.tasks.withType(PublishToMavenRepository).matching {
				it.repository == project.publishing.repositories.svwsreleases ||
					it.repository == project.publishing.repositories.githubreleases
			}
		}
	}

  	void apply(Project project) {
		super.apply(project)
		this.project = project
		this.extension = project.getExtensions()
			.create("svwsmavenpublish", SvwsMavenPublishPluginExtension.class)

		project.pluginManager.apply "maven-publish"
		project.pluginManager.apply "signing"

		project.gradle.projectsEvaluated {
			this.chooseMavenRepository()
		}
		this.configSigning()
		this.registerPublishingTasks()
    }

}
