package svws.gradle.node;

import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.file.RelativePath;

abstract class NodeWipe extends DefaultTask {

	@Internal
	NodePluginConfig cfg;

	NodeWipe() {
		group = 'node'
		this.cfg = project.nodeconfig;
	}

	@TaskAction
	def download() {
		def downloadDir = new File(this.cfg.getDownloadDirectory());
		def nodeDir = new File(this.cfg.getNodeDirectory());
		if (downloadDir.exists())
			project.delete(downloadDir);
		if (nodeDir.exists())
			project.delete(nodeDir);
	}

}
