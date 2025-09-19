package svws.gradle.node;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.AbstractExecTask;
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

/**
 * Die Konfigurationserweiterung des Node-Plugins.
 * Aufrufbar über npmconfig.<config> mit folgenden Optionen:
 *
 *   url         - die URL, von wo die NodeJS-Version herunterladbar ist
 *   version     - die Version von Node-JS
 *   npmVersion  - die Version von NPM
 */
abstract class NodePluginConfig {

	def os = DefaultNativePlatform.getCurrentOperatingSystem();
	def arch = DefaultNativePlatform.getCurrentArchitecture().getName();
	def rootProject;
	def project;

	abstract Property<String> getUrl();
	abstract Property<String> getVersion();
	abstract Property<String> getNpmVersion();
	abstract Property<String> getTsVersion();
	abstract Property<String> getTsNodeTypesVersion();

	NodePluginConfig(Project p) {
		this.project = p;
		this.rootProject = p.rootProject;
		url.convention('https://nodejs.org/dist/v');
		version.convention('22.19.0'); // https://nodejs.org/en/download/prebuilt-installer
		npmVersion.convention('10.9.3');
		tsVersion.convention('5.9.2'); // https://github.com/microsoft/TypeScript/releases
		tsNodeTypesVersion.convention('24.4.0'); // https://www.npmjs.com/package/@types/node
	}

	boolean isLinux() {
		return os.isLinux();
	}

	boolean isWindows() {
		return os.isWindows();
	}

	boolean isMacOsX() {
		return os.isMacOsX();
	}

	String getOSString() {
		if (os.isLinux())
			return "linux";
		if (os.isWindows())
			return "win";
		if (os.isMacOsX())
			return "darwin";
		throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!");
	}

	String getArchString() {
		if (["x86_64", "amd64", "x64", "x86-64"].contains(arch))
			return "x64";
		if (["arm64", "arm-v8", "aarch64"].contains(arch))
			return "arm64";
		if (["ppc64"].contains(arch))
			return "ppc64le";
		throw new Exception("Unsupported operating system architecture ${os.getName()} ${arch} for the node plugin!")
	}

	String getCompressedFileType() {
		if (os.isMacOsX() || this.os.isLinux())
			return "tar.gz";
		if (os.isWindows())
			return "zip";
		throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!");
	}

	String getCompressedFilename() {
		return "node-v" + this.version.get() + "-" + this.getOSString() + "-" + this.getArchString();
	}

	String getCompressedFilenameExt() {
		return "node-v" + this.version.get() + "-" + this.getOSString() + "-" + this.getArchString() + "." + this.getCompressedFileType();
	}

	URL getDownloadURL() {
		if (project.hasProperty('node_download_url'))
			return new URL(project.node_download_url + this.version.get() + "/" + this.getCompressedFilenameExt());
		def downloadUrl = System.getenv("NODE_DOWNLOAD_URL")
		if (downloadUrl != null)
			return new URL(downloadUrl + this.version.get() + "/" + this.getCompressedFilenameExt());
		return new URL(this.url.get() + this.version.get() + "/" + this.getCompressedFilenameExt());
	}

	String getDownloadUser() {
		if (project.hasProperty('node_download_user'))
			return project.node_download_user;
		def downloadUser = System.getenv("NODE_DOWNLOAD_USER")
		if (downloadUser != null)
			return downloadUser;
		return null;
	}

	String getDownloadPasswd() {
		if (project.hasProperty('node_download_passwd'))
			return project.node_download_passwd;
		def downloadPasswd = System.getenv("NODE_DOWNLOAD_PASSWD")
		if (downloadPasswd != null)
			return downloadPasswd;
		return "";
	}

	String getDownloadDirectory() {
		return "${project.rootProject.projectDir}/download";
	}

	String getNodeDirectory() {
		return "${project.rootProject.projectDir}/node";
	}


	String getNpmExectuable() {
		if (os.isWindows()) {
	   		return "${project.rootProject.projectDir}/node/npm.cmd";
		} else if (os.isLinux()) {
	   		return "${project.rootProject.projectDir}/node/bin/npm";
		} else if (os.isMacOsX()) {
	   		return "${project.rootProject.projectDir}/node/bin/npm";
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!");
		}
	}

	String getNpxExectuable() {
		if (os.isWindows()) {
	   		return "${project.rootProject.projectDir}/node/npx.cmd";
		} else if (os.isLinux()) {
	   		return "${project.rootProject.projectDir}/node/bin/npx";
		} else if (os.isMacOsX()) {
	   		return "${project.rootProject.projectDir}/node/bin/npx";
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!");
		}
	}

	String getPnpmExectuable() {
		if (os.isWindows()) {
	   		return "${project.rootProject.projectDir}/node/pnpm.cmd";
		} else if (os.isLinux()) {
	   		return "${project.rootProject.projectDir}/node/lib/node_modules/corepack/shims/pnpm";
		} else if (os.isMacOsX()) {
	   		return "${project.rootProject.projectDir}/node/lib/node_modules/corepack/shims/pnpm";
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!");
		}
	}

	String getPnpxExectuable() {
		if (os.isWindows()) {
	   		return "${project.rootProject.projectDir}/node/pnpx.cmd";
		} else if (os.isLinux()) {
	   		return "${project.rootProject.projectDir}/node/lib/node_modules/corepack/shims/pnpx";
		} else if (os.isMacOsX()) {
	   		return "${project.rootProject.projectDir}/node/lib/node_modules/corepack/shims/pnpx";
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!");
		}
	}

	String getTscExecutable() {
		if (os.isWindows()) {
	   		return "${project.rootProject.projectDir}/node/tsc.cmd";
		} else if (os.isLinux()) {
	   		return "${project.rootProject.projectDir}/node/lib/node_modules/typescript/bin/tsc";
		} else if (os.isMacOsX()) {
	   		return "${project.rootProject.projectDir}/node/lib/node_modules/typescript/bin/tsc";
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!");
		}
	}

	String getTsserverExecutable() {
		if (os.isWindows()) {
	   		return "${project.rootProject.projectDir}/node/tsserver.cmd";
		} else if (os.isLinux()) {
	   		return "${project.rootProject.projectDir}/node/lib/node_modules/typescript/bin/tsserver";
		} else if (os.isMacOsX()) {
	   		return "${project.rootProject.projectDir}/node/lib/node_modules/typescript/bin/tsserver";
		} else {
			throw new Exception("Unsupported operating system ${os.getName()} for the node plugin!");
		}
	}

	void addEnvironment(AbstractExecTask<?> t) {
		def path = null;
		def nodePath = null;
		for (entry in t.getEnvironment()) {
			if ("PATH".equals(entry.key.toUpperCase())) {
				path = entry.value;
				break;
			}
			if ("NODE_PATH".equals(entry.key.toUpperCase())) {
				nodePath = entry.value;
				break;
			}
		}
		def rootNodePath = "${project.rootProject.projectDir}/node";
		if ((nodePath == null) || ("".equals(nodePath.trim()))) {
			t.environment('NODE_PATH', rootNodePath);
		} else if (this.isWindows()) {
			t.environment('NODE_PATH', rootNodePath + ";" + nodePath);
		} else {
			t.environment('NODE_PATH', rootNodePath + ":" + nodePath);
		}
		if ((path == null) || ("".equals(path.trim()))) {
			t.environment('PATH', this.getNodeDirectory());
		} else if (this.isWindows()) {
			t.environment('PATH', this.getNodeDirectory() + ";" + path);
		} else {
			t.environment('PATH', this.getNodeDirectory() + "/bin:" + path);
		}
	}

}
