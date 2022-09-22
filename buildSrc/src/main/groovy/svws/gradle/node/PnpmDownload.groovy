package svws.gradle.node;

import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Exec;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.RegularFileProperty;

abstract class PnpmDownload extends Exec {

	PnpmDownload() {
		group "pnpm"
		dependsOn project.tasks.getByPath('nodeDownload')
		    
		def cfg = project.nodeconfig;
		if (cfg.isWindows()) {
			outputs.dir(cfg.getNodeDirectory() + "/node_modules/pnpm");
		} else if (!cfg.isLinux() && !cfg.isMacOsX()) {
			throw new Exception("Unsupported operating system for the node plugin!");
		}
	    outputs.files(cfg.getPnpmExectuable(), cfg.getPnpxExectuable()); 
	}

	@TaskAction
    @Override
    protected void exec() {
        // Make convention mapping work
        def cmdLine = this.getCommandLine();
		def cfg = project.nodeconfig;
		cfg.addEnvironment(this);
		cmdLine.set(0, 'pnpm');
		cmdLine.add(0, '--global');
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
