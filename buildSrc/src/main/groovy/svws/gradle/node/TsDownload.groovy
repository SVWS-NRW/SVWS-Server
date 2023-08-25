package svws.gradle.node;

import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Exec;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.RegularFileProperty;

abstract class TsDownload extends Exec {

	TsDownload() {
		group "node"
		dependsOn project.rootProject.tasks.getByPath('nodeDownload')
	}

	@OutputFile
	String getTsc() {
		def cfg = project.nodeconfig;
		return cfg.getTscExecutable();
	}

	@OutputFile
	String getTsserver() {
		def cfg = project.nodeconfig;
		return cfg.getTsserverExecutable();
	}

 	@OutputDirectory
 	public String getTSDir() {
		def cfg = project.nodeconfig;
		return cfg.getNodeDirectory() + "/node_modules/typescript";
 	}

 	@OutputDirectory
 	public String getTSNodeTypesDir() {
		def cfg = project.nodeconfig;
		return cfg.getNodeDirectory() + "/node_modules/@types";
 	}

 	@Input
 	public String getTSVersion() {
 		def cfg = project.nodeconfig;
 		return cfg.getTsVersion().get();
	}

 	@Input
 	public String getTSNodeTypesVersion() {
 		def cfg = project.nodeconfig;
 		return cfg.getTsNodeTypesVersion().get();
	}


	@TaskAction
    @Override
    protected void exec() {
        def cmdLine = this.getCommandLine();
		def cfg = project.nodeconfig;
		cfg.addEnvironment(this);
		cmdLine.set(0, '@types/node@' + this.getTSNodeTypesVersion());
		cmdLine.add(0, 'typescript@' + this.getTSVersion());
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
