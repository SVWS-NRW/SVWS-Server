package svws.gradle.node;

import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Exec;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.RegularFileProperty;

abstract class TsDownload extends Exec {

	@Internal
	NodePluginConfig cfg;

	TsDownload() {
		group = "node"
		dependsOn project.rootProject.tasks.getByPath('nodeDownload')
		this.cfg = project.nodeconfig;
	}

	@OutputFile
	String getTsc() {
		return this.cfg.getTscExecutable();
	}

	@OutputFile
	String getTsserver() {
		return this.cfg.getTsserverExecutable();
	}

	@OutputDirectory
	public String getTSDir() {
		return this.cfg.getNodeDirectory() + "/node_modules/typescript";
 	}

	@OutputDirectory
	public String getTSNodeTypesDir() {
		return this.cfg.getNodeDirectory() + "/node_modules/@types";
 	}

	@Input
	public String getTSVersion() {
		return this.cfg.getTsVersion().get();
	}

	@Input
	public String getTSNodeTypesVersion() {
		return this.cfg.getTsNodeTypesVersion().get();
	}


	@TaskAction
	@Override
	protected void exec() {
		def cmdLine = this.getCommandLine();
		this.cfg.addEnvironment(this);
		cmdLine.set(0, '@types/node@' + this.getTSNodeTypesVersion());
		cmdLine.add(0, 'typescript@' + this.getTSVersion());
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
