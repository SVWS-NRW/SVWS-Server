package svws.gradle.node;

import javax.inject.Inject
import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.file.RelativePath;

abstract class NodeDownload extends DefaultTask {

	@Internal
	NodePluginConfig cfg;
	
	@Inject
	abstract FileSystemOperations getFs()

	NodeDownload() {
		group = 'node'
		this.cfg = project.nodeconfig;
		outputs.dir(this.cfg.getNodeDirectory());
	}

	@TaskAction
	def download() {
		def downloadPath = this.cfg.getDownloadDirectory();
		def filename = this.cfg.getCompressedFilenameExt();
		def file = new File("${downloadPath}/${filename}");
		if (file.exists()) {
			println "Datei ${filename} wurde bereits heruntergeladen, skipping download"
		} else {
			fs.mkdir(downloadPath);
			def url = this.cfg.getDownloadURL();
			println "Downloading ${filename} from ${url}...";
			def username = this.cfg.getDownloadUser();
			if (username == null) {
			    ant.get(src: url, dest: file)
			} else {
				def userpasswd = this.cfg.getDownloadPasswd();
			    ant.get(src: url, dest: file, username: username, password: userpasswd)
			}
		}

		def compFilename = this.cfg.getCompressedFilename();
		if ("zip".equals(this.cfg.getCompressedFileType())) {
			fs.copy {
				eachFile { f ->	f.relativePath = new RelativePath(true, f.relativePath.segments.drop(1)) }
				from project.zipTree("${downloadPath}/${filename}").matching{ include "${compFilename}/**" };				
				includeEmptyDirs = false    
				into this.cfg.getNodeDirectory()
			}
		} else if ("tar.gz".equals(this.cfg.getCompressedFileType())) {
			fs.exec {
				commandLine 'tar', '-xzf', "${downloadPath}/${filename}", '--strip-components=1','-C', this.cfg.getNodeDirectory() + "/";
			}
		} else if ("tar.xz".equals(this.cfg.getCompressedFileType())) {
			throw new Exception("Archive type tar.xz not yet suppported by this gradle node plugin!");			
		} else {
			throw new Exception("Archive type not supported by this gradle node plugin!");
		}
	}

}
