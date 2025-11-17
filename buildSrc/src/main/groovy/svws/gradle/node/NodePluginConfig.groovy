package svws.gradle.node


import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.AbstractExecTask
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

import java.nio.file.Path

/**
 * Die Konfigurationserweiterung des Node-Plugins.
 * Aufrufbar über npmconfig.<config> mit folgenden Optionen:
 *
 *   url         - die URL, von wo die NodeJS-Version herunterladbar ist
 *   version     - die Version von Node-JS
 *   npmVersion  - die Version von NPM
 */
abstract class NodePluginConfig {

	def os = DefaultNativePlatform.getCurrentOperatingSystem()
	def arch = DefaultNativePlatform.getCurrentArchitecture().getName()
	def rootProject
	def project

	abstract Property<String> getUrl()
	abstract Property<String> getVersion()
	abstract Property<String> getNpmVersion()
	abstract Property<String> getTsVersion()
	abstract Property<String> getTsNodeTypesVersion()

	NodePluginConfig(Project p) {
		this.project = p
		this.rootProject = p.rootProject
		url.convention('https://nodejs.org/dist/v')
		version.convention('24.11.1') // https://nodejs.org/en/download/prebuilt-installer
		npmVersion.convention('11.6.2')
		tsVersion.convention('5.9.3') // https://github.com/microsoft/TypeScript/releases
		tsNodeTypesVersion.convention('24.9.1') // https://www.npmjs.com/package/@types/node
	}

	boolean isLinux() {
		return os.isLinux()
	}

	boolean isWindows() {
		return os.isWindows()
	}

	boolean isMacOsX() {
		return os.isMacOsX()
	}

	static boolean useSystemNode() {
		return System.getenv("USE_SYSTEM_NODE") == "1"
	}

	String getOSString() {
		if (isWindows())
			return "win"
		else if (isLinux())
			return "linux"
		else if (isMacOsX())
			return "darwin"
		else
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!")
	}

	String getArchString() {
		if (["x86_64", "amd64", "x64", "x86-64"].contains(arch))
			return "x64"
		else if (["arm64", "arm-v8", "aarch64"].contains(arch))
			return "arm64"
		else if (["ppc64"].contains(arch))
			return "ppc64le"
		else
			throw new Exception("Unsupported operating system architecture ${os.getName()} ${arch} for the node plugin!")
	}

	String getCompressedFileType() {
		if (isWindows())
			return "zip"
		else if (isMacOsX() || isLinux())
			return "tar.gz"
		else
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!")
	}

	String getCompressedFilenameExt() {
		return "node-v" + version.get() + "-" + getOSString() + "-" + getArchString() + "." + getCompressedFileType()
	}

	URL getDownloadURL() {
		if (project.hasProperty('node_download_url'))
			return new URI("${project.node_download_url}${version.get()}/${getCompressedFilenameExt()}").toURL()
		def downloadUrl = System.getenv("NODE_DOWNLOAD_URL")
		if (downloadUrl != null)
			return new URI(downloadUrl + version.get() + "/" + getCompressedFilenameExt()).toURL()
		return new URI(url.get() + version.get() + "/" + getCompressedFilenameExt()).toURL()
	}

	String getDownloadUser() {
		if (project.hasProperty('node_download_user'))
			return project.node_download_user
		def downloadUser = System.getenv("NODE_DOWNLOAD_USER")
		if (downloadUser != null)
			return downloadUser
		return null
	}

	String getDownloadPasswd() {
		if (project.hasProperty('node_download_passwd'))
			return project.node_download_passwd
		def downloadPasswd = System.getenv("NODE_DOWNLOAD_PASSWD")
		if (downloadPasswd != null)
			return downloadPasswd
		return ""
	}

	String getDownloadDirectory() {
		return "${project.rootProject.projectDir}/download"
	}

	String getNodeDirectory() {
		return useSystemNode() ? "" : "${project.rootProject.projectDir}/node"
	}

	String getNpmExecutable() {
		if (isWindows()) {
	   		return useSystemNode() ? "npm.cmd" : Path.of(getNodeDirectory(), "npm.cmd").toString()
		} else if (isLinux() || isMacOsX()) {
			return useSystemNode() ? "npm" : Path.of(getNodeDirectory(), "bin", "npm").toString()
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!")
		}
	}

	String getNpxExecutable() {
		if (isWindows()) {
			return useSystemNode() ? "npx.cmd" : Path.of(getNodeDirectory(), "npx.cmd").toString()
		} else if (isLinux() || isMacOsX()) {
			return useSystemNode() ? "npx" : Path.of(getNodeDirectory(), "bin", "npx").toString()
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!")
		}
	}

	String getPnpmExecutable() {
		if (isWindows()) {
			return useSystemNode() ? "pnpm.cmd" : Path.of(getNodeDirectory(), "pnpm.cmd").toString()
		} else if (isLinux() || isMacOsX()) {
			return useSystemNode() ? "pnpm" : Path.of(getNodeDirectory(), "lib", "node_modules", "corepack", "shims", "pnpm").toString()
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!")
		}
	}

	String getPnpxExecutable() {
		if (isWindows()) {
			return useSystemNode() ? "pnpx.cmd" : Path.of(getNodeDirectory(), "pnpx.cmd").toString()
		} else if (isLinux() || isMacOsX()) {
			return useSystemNode() ? "pnpx" : Path.of(getNodeDirectory(), "lib", "node_modules", "corepack", "shims", "pnpx").toString()
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!")
		}
	}

	String getTscExecutable() {
		if (isWindows()) {
			return useSystemNode() ? "tsc.cmd" : Path.of(getNodeDirectory(), "tsc.cmd").toString()
		} else if (isLinux() || isMacOsX()) {
			return useSystemNode() ? "tsc" : Path.of(getNodeDirectory(), "lib", "node_modules", "typescript", "bin", "tsc").toString()
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!")
		}
	}

	String getTsserverExecutable() {
		if (isWindows()) {
			return useSystemNode() ? "tsserver.cmd" : Path.of(getNodeDirectory(), "tsserver.cmd").toString()
		} else if (isLinux() || isMacOsX()) {
			return useSystemNode() ? "tsserver" : Path.of(getNodeDirectory(), "lib", "node_modules", "typescript", "bin", "tsserver").toString()
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!")
		}
	}

	void addEnvironment(AbstractExecTask<? extends AbstractExecTask> task) {
		String path = null
		String nodePath = null
		for (entry in task.getEnvironment()) {
			if ("PATH" == entry.key.toUpperCase()) {
				path = entry.value
			} else if ("NODE_PATH" == entry.key.toUpperCase()) {
				nodePath = entry.value
			}
		}
		if ((nodePath == null) || nodePath.isBlank()) {
			task.environment('NODE_PATH', getNodeDirectory())
		} else if (isWindows()) {
			task.environment('NODE_PATH', getNodeDirectory() + ";" + nodePath)
		} else {
			task.environment('NODE_PATH', getNodeDirectory() + ":" + nodePath)
		}
		if ((path == null) || path.isBlank()) {
			task.environment('PATH', getNodeDirectory())
		} else if (isWindows()) {
			task.environment('PATH', getNodeDirectory() + ";" + path)
		} else {
			task.environment('PATH', getNodeDirectory() + "/bin:" + path)
		}
	}

}
