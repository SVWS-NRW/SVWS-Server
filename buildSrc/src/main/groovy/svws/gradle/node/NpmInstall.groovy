package svws.gradle.node;

import org.apache.tools.ant.Project
import org.gradle.api.DefaultTask;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.AbstractExecTask;
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.file.RelativePath;
import org.gradle.api.provider.Property

abstract class NpmInstall extends AbstractExecTask<NpmInstall> {

	NpmInstall() {
		super(NpmInstall.class);
		dependsOn project.tasks.getByPath('nodeDownload')
		def cfg = project.nodeconfig;
	}
	
	@InputFile
	public String getPackageJson() {
		return "$workingDir/package.json";
	}

 	@InputFile
	public String getPackageLockJson() {
		return "$workingDir/package-lock.json";
	}
 
 	@OutputDirectory
 	public String getNodeModules() {
 		return "$workingDir/node_modules";
 	}
	
	@TaskAction
    @Override
    protected void exec() {
        def cmdLine = this.getCommandLine();
		def cfg = project.nodeconfig;
		cfg.addEnvironment(this);
		cmdLine.set(0, 'install');
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
