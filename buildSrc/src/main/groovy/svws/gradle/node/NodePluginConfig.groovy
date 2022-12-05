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
    	version.convention('18.12.1'); // https://nodejs.org/en/download/
    	npmVersion.convention('8.19.2');
    	tsVersion.convention('4.9.3'); // https://github.com/microsoft/TypeScript/releases
    	tsNodeTypesVersion.convention('18.11.10'); // https://www.npmjs.com/package/@types/node
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
		throw new Exception("Unsupported operating system ' + os.getName() + ' for the node plugin!");
	}
	
	String getArchString() { 
    	if ("x86_64".equals(arch) || "amd64".equals(arch) || "x64".equals(arch) || "x86-64".equals(arch))
			return "x64";
    	if ("arm64".equals(arch) || "arm-v8".equals(arch))
			return "arm64";
		if ("ppc64".equals(arch))
		    return "ppc64le";
		throw new Exception("Unsupported operating system architecture ' + os.getName() + ' ' + arch + ' for the node plugin!")
	}
	
	String getCompressedFileType() {
		if (os.isMacOsX() || this.os.isLinux())
			return "tar.gz";
		if (os.isWindows())
			return "zip";
		throw new Exception("Unsupported operating system ' + os.getName() + ' for the node plugin!");
	}
	
	String getCompressedFilename() {
		return "node-v" + this.version.get() + "-" + this.getOSString() + "-" + this.getArchString();
	}
	
	String getCompressedFilenameExt() {
        return "node-v" + this.version.get() + "-" + this.getOSString() + "-" + this.getArchString() + "." + this.getCompressedFileType();
	}
	
	URL getDownloadURL() {
		return new URL(this.url.get() + this.version.get() + "/" + this.getCompressedFilenameExt());
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
			throw new Exception("Unsupported operating system ' + os.getName() + ' for the node plugin!");
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
			throw new Exception("Unsupported operating system ' + os.getName() + ' for the node plugin!");
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
			throw new Exception("Unsupported operating system ' + os.getName() + ' for the node plugin!");
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
			throw new Exception("Unsupported operating system ' + os.getName() + ' for the node plugin!");
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
			throw new Exception("Unsupported operating system ' + os.getName() + ' for the node plugin!");
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
			throw new Exception("Unsupported operating system ' + os.getName() + ' for the node plugin!");
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
