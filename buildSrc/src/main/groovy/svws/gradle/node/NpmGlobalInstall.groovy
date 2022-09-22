package svws.gradle.node;

import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.AbstractExecTask;
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.file.RelativePath;
import org.gradle.api.provider.Property

abstract class NpmGlobalInstall extends AbstractExecTask<NpmGlobalInstall> {

	NpmGlobalInstall() {
		super(NpmGlobalInstall.class);
		dependsOn project.tasks.getByPath('nodeDownload')
	}
	
	@TaskAction
    @Override
    protected void exec() {
        // Make convention mapping work
        def cmdLine = this.getCommandLine();
		def cfg = project.nodeconfig;
		if (cfg.isWindows())
			environment 'PATH', cfg.getNodeDirectory() + ";${environment.PATH}"
		else	
			environment 'PATH', cfg.getNodeDirectory() + "/bin:${environment.PATH}"
		cmdLine.set(0, '--global');
		cmdLine.add(0, 'install');
		cmdLine.add(0, cfg.getNpmExectuable());
		if (cfg.isWindows()) {
			cmdLine.add(0, '/c');
			cmdLine.add(0, 'cmd');
		} else if (!cfg.isLinux() && !cfg.isMacOsX()) {
			throw new Exception("Unsupported operating system for the node plugin!");
		}
		this.setCommandLine(cmdLine);
        super.exec();
    }	

}
