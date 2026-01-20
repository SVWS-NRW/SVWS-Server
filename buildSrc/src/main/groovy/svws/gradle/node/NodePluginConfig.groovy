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
	def rootProjectDir
	def node_download_url
	def node_download_user
	def node_download_passwd

	abstract Property<String> getUrl()
	abstract Property<String> getVersion()
	abstract Property<String> getNpmVersion()
	abstract Property<String> getTsVersion()
	abstract Property<String> getTsNodeTypesVersion()

	NodePluginConfig(Project p) {
		this.rootProjectDir = p.rootProject.projectDir
		this.node_download_url = p.hasProperty('node_download_url') ? p.node_download_url : System.getenv("NODE_DOWNLOAD_URL")
		this.node_download_user = p.hasProperty('node_download_user') ? p.node_download_user : System.getenv("NODE_DOWNLOAD_USER")
		this.node_download_passwd = p.hasProperty('node_download_passwd') ? p.node_download_passwd : System.getenv("NODE_DOWNLOAD_PASSWD")
		url.convention('https://nodejs.org/dist/v')
		version.convention('24.13.0') // https://nodejs.org/en/download/prebuilt-installer
		npmVersion.convention('11.6.2')
		tsVersion.convention('5.9.3') // https://github.com/microsoft/TypeScript/releases
		tsNodeTypesVersion.convention('25.0.2') // https://www.npmjs.com/package/@types/node
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
println "${this.node_download_url}${version.get()}/${getCompressedFilenameExt()}"
		if (this.node_download_url != null)
			return new URI("${this.node_download_url}${version.get()}/${getCompressedFilenameExt()}").toURL()
		return new URI(url.get() + version.get() + "/" + getCompressedFilenameExt()).toURL()
	}

	String getDownloadUser() {
		return this.node_download_user
	}

	String getDownloadPasswd() {
		return (this.node_download_passwd == null) ? "" : this.node_download_passwd
	}

	String getDownloadDirectory() {
		return "${rootProjectDir}/download"
	}

	String getNodeDirectory() {
		return useSystemNode() ? "" : "${rootProjectDir}/node"
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
