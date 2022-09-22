package svws.gradle.node;

import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.file.RelativePath;

abstract class NodeDownload extends DefaultTask {

	NodeDownload() {
	    group 'node'
	    def cfg = project.nodeconfig;
	    outputs.dir(cfg.getNodeDirectory());
	}

	@TaskAction
	def download() {
		def cfg = project.nodeconfig;
		def downloadPath = cfg.getDownloadDirectory();
		def filename = cfg.getCompressedFilenameExt();
		def file = new File("${downloadPath}/${filename}");
		if (file.exists()) {
			println "Datei ${filename} wurde bereits heruntergeladen, skipping download"
		} else {
			project.mkdir(downloadPath);
			def url = cfg.getDownloadURL();
			println "Downloading ${filename} from ${url}...";
			url.withInputStream { i -> file.withOutputStream { it << i } }
		}

    	def compFilename = cfg.getCompressedFilename();
		if ("zip".equals(cfg.getCompressedFileType())) {
			project.copy {
				eachFile { f ->	f.relativePath = new RelativePath(true, f.relativePath.segments.drop(1)) }
				from project.zipTree("${downloadPath}/${filename}").matching{ include "${compFilename}/**" };				
				includeEmptyDirs = false    
				into cfg.getNodeDirectory()
			}
		} else if ("tar.gz".equals(cfg.getCompressedFileType())) {
			project.exec {
				commandLine 'tar', '-xzf', "${downloadPath}/${filename}", '--strip-components=1','-C', cfg.getNodeDirectory() + "/";
			}
		} else if ("tar.xz".equals(cfg.getCompressedFileType())) {
			throw new Exception("Archive type tar.xz not yet suppported by this gradle node plugin!");			
		} else {
			throw new Exception("Archive type not supported by this gradle node plugin!");
		}
	}

}
