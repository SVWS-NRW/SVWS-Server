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

abstract class NpmPublish extends AbstractExecTask<NpmPublish> {

	@Internal
	NodePluginConfig cfg;

	public String repository = null
	public String actor = null
	public String token = null
	public String npmrc = null
	public boolean scopePublic = false
	public boolean tokenOnly = false

	NpmPublish() {
		super(NpmPublish.class);
		dependsOn project.rootProject.tasks.getByPath('nodeDownload')
		this.cfg = project.nodeconfig;
	}

	@TaskAction
	@Override
	protected void exec() {
		if ((!tokenOnly) && (actor == null))
			throw new Exception("Es wurde kein Benutzername/Actor angegeben.");
		if (token == null)
			throw new Exception("Es wurde kein Benutzertoken bzw. -kennwort angegeben.");
		def cmdLine = this.getCommandLine();
		this.cfg.addEnvironment(this);
		if (tokenOnly) {
			this.environment('NPM_TOKEN_BASE64', this.token);
		} else {
			this.environment('NPM_ACTOR', this.actor);
			this.environment('NPM_TOKEN', this.token);
			this.environment('NPM_TOKEN_BASE64', (this.actor + ':' + this.token).bytes.encodeBase64().toString());
		}
		if (npmrc != null) {
			this.environment('NPM_CONFIG_GLOBALCONFIG', this.npmrc);
		}
		if (scopePublic) {
			cmdLine.set(0, 'public');
			cmdLine.add(0, '--access');
			cmdLine.add(0, 'publish');
		} else {
			cmdLine.set(0, 'publish');
		}
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
