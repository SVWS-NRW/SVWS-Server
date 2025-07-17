package svws.gradle.node;

import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.AbstractExecTask;
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.file.RelativePath;
import org.gradle.api.provider.Property

abstract class NpmGlobalInstall extends AbstractExecTask<NpmGlobalInstall> {

	@Internal
	NodePluginConfig cfg;

	NpmGlobalInstall() {
		super(NpmGlobalInstall.class);
		dependsOn project.rootProject.tasks.getByPath('nodeDownload')
		this.cfg = project.nodeconfig;
	}
	
	@TaskAction
	@Override
	protected void exec() {
		// Make convention mapping work
		def cmdLine = this.getCommandLine();
		if (this.cfg.isWindows())
			environment 'PATH', this.cfg.getNodeDirectory() + ";${environment.PATH}"
		else	
			environment 'PATH', this.cfg.getNodeDirectory() + "/bin:${environment.PATH}"
		cmdLine.set(0, '--global');
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
