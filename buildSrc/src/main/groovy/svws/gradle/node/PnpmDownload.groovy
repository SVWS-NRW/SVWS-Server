package svws.gradle.node;

import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Exec;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.RegularFileProperty;

abstract class PnpmDownload extends Exec {

	@Internal
	NodePluginConfig cfg;

	PnpmDownload() {
		group = "pnpm"
		dependsOn project.rootProject.tasks.getByPath('nodeDownload')

		this.cfg = project.nodeconfig;
		if (this.cfg.isWindows()) {
			outputs.dir(this.cfg.getNodeDirectory() + "/node_modules/pnpm");
		} else if (!this.cfg.isLinux() && !this.cfg.isMacOsX()) {
			throw new Exception("Unsupported operating system for the node plugin!");
		}
		outputs.files(this.cfg.getPnpmExectuable(), this.cfg.getPnpxExectuable()); 
	}

	@TaskAction
	@Override
	protected void exec() {
		// Make convention mapping work
		def cmdLine = this.getCommandLine();
		this.cfg.addEnvironment(this);
		cmdLine.set(0, 'pnpm');
		cmdLine.add(0, '--global');
		cmdLine.add(0, 'install');
		cmdLine.add(0, this.cfg.getNpmExectuable());
		if (this.cfg.isWindows()) {
			cmdLine.add(0, '/c');
			cmdLine.add(0, 'cmd');
		} else if (!this.cfg.isLinux() && !this.cfg.isMacOsX()) {
			throw new Exception("Unsupported operating system for the node plugin!");
		}
		this.setCommandLine(cmdLine);
		super.exec();
	}

}
