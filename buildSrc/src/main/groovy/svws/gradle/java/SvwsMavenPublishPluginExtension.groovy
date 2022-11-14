package svws.gradle.java

/**
 * Extension zum Plugin SvwsMavenPublishPlugin.
 * Ermöglicht die Konfiguration der URLs von Maven-Repositories
 * für die Veröffentlichung von SVWS-Artefakten in den build.gradle-Dateien.
 */
class SvwsMavenPublishPluginExtension {

	// URL zum Snapshot-Repository im SVWS-Nexus
	private String nexusSnapshotRepositoryUrl = ""
	// URL zum Release-Repository im SVWS-Nexus
	private String nexusReleasesRepositoryUrl = ""
	// URL zum Package-Repository von SVWS auf Github
	private String githubReleasesRepositoryUrl = ""

	String getNexusSnapshotRepositoryUrl() {
		return nexusSnapshotRepositoryUrl
	}

	void setNexusSnapshotRepositoryUrl(String nexusSnapshotRepositoryUrl) {
		this.nexusSnapshotRepositoryUrl = nexusSnapshotRepositoryUrl
	}

	String getNexusReleasesRepositoryUrl() {
		return nexusReleasesRepositoryUrl
	}

	void setNexusReleasesRepositoryUrl(String nexusReleasesRepositoryUrl) {
		this.nexusReleasesRepositoryUrl = nexusReleasesRepositoryUrl
	}

	String getGithubReleasesRepositoryUrl() {
		return githubReleasesRepositoryUrl
	}

	void setGithubReleasesRepositoryUrl(String githubReleasesRepositoryUrl) {
		this.githubReleasesRepositoryUrl = githubReleasesRepositoryUrl
	}
}
