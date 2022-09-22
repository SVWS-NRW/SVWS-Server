package svws.gradle.node;

import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.file.RelativePath;

abstract class NodeWipe extends DefaultTask {

	NodeWipe() {
	    group 'node'
	}

	@TaskAction
	def download() {
		def cfg = project.nodeconfig;
		def downloadDir = new File(cfg.getDownloadDirectory());
		def nodeDir = new File(cfg.getNodeDirectory());
		if (downloadDir.exists())
			project.delete(downloadDir);
		if (nodeDir.exists())
			project.delete(nodeDir);
	}

}
