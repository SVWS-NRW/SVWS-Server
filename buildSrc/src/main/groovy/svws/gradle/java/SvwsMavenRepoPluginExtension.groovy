package svws.gradle.java

/**
 * Extension zum Plugin SvwsMavenRepoPlugin.
 * Ermöglicht die Konfiguration der URLs für die Maven-Repositories
 * für den Download von dependencies in den build.gradle-Dateien.
 */
class SvwsMavenRepoPluginExtension {

	// URL zum Proxy-Repository für Maven-Central im SVWS-Nexus
	private String nexusMavenCentralProxyRepositoryUrl = ""
	// URL zum Package-Repository von SVWS auf Github
	private String githubMavenPackagesUrl = ""

	String getNexusMavenCentralProxyRepositoryUrl() {
		return nexusMavenCentralProxyRepositoryUrl
	}

	void setNexusMavenCentralProxyRepositoryUrl(String nexusMavenCentralProxyRepositoryUrl) {
		this.nexusMavenCentralProxyRepositoryUrl = nexusMavenCentralProxyRepositoryUrl
	}

	String getGithubMavenPackagesUrl() {
		return githubMavenPackagesUrl
	}

	void setGithubMavenPackagesUrl(String githubMavenPackagesUrl) {
		this.githubMavenPackagesUrl = githubMavenPackagesUrl
	}

}
