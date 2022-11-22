package svws.gradle.node;

import org.gradle.api.Plugin 
import org.gradle.api.Project 
import org.gradle.api.tasks.Exec 


/**
 * Das Plugin für Node-basierte-Projekte. 
 * Die Konfiguration wird unter nodeconfig zur Verfügung gestellt.
 */
class NodePlugin implements Plugin<Project> {

	def project;
	
   	void apply(Project project) {
    	this.project = project;
    	
        // Erstelle die Konfiguration für das Plugin mit Default-Werten
        def buildDir = project.getBuildDir();
		def cfg = project.extensions.create('nodeconfig', NodePluginConfig, project);
 
 		// Füge Tasks als globale Typen für gradle hinzu
		[ NpmGlobalInstall.class, NpmInstall.class, NpmRun.class, NpmPublish.class, NpmTask.class, NpxTask.class, PnpmTask.class, PnpxTask.class ].each { 
			clazz -> this.project.extensions.extraProperties[clazz.getSimpleName()] = clazz;
		};
        
        // Füge Tasks zum Projekt hinzu
        [ NodeDownload.class, PnpmDownload.class, NodeWipe.class, TsDownload.class ].each {
			clazz -> this.project.tasks.register(clazz.getSimpleName().uncapitalize(), clazz);
		};
	
    }
    
}


