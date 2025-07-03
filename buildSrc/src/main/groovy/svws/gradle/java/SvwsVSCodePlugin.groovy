package svws.gradle.java

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

/**
 * Dieses Plugin führt projektspezifische Einstellungen
 * in der Visual Studio Code-Entwicklungsumgebung durch.*/
class SvwsVSCodePlugin implements Plugin<Project> {

	// Aktuell betrachtetes Projekt/Modul innerhalb des SVWS Projekts
	def project

	// Merged die beiden JSON Dateien oder kopiert Source in Target, falls Target nicht existiert
	void addSetVisualStudioCodeSettingsMethod() {
		project.ext.setVSCSettings = { File target, File source ->
			if (!target.exists()) {
				target.parentFile.mkdirs()
				target.text = source.text
				return
			}

			def jsonSlurper = new JsonSlurper()

			def sourceJson
			try {
				sourceJson = jsonSlurper.parse(source)
			} catch (Exception e) {
				throw new GradleException("JSON ist ungültig: ${source.path} - ${e}")
			}

			def targetJson
			try {
				targetJson = jsonSlurper.parse(target)
			} catch (Exception e) {
				throw new GradleException("JSON ist ungültig: ${target.path} - ${e}")
			}

			// Merge die JSON-Daten aus targetJson und sourceJson. Werte aus sourceJson überschreiben
			// bei Schlüssel-Übereinstimmungen die Werte aus targetJson.
			def mergedJson = targetJson + sourceJson

			target.text = JsonOutput.prettyPrint(JsonOutput.toJson(mergedJson))
		}
	}


	/**
	 *	Konfiguration der VSCode Settings für das angegebene Projekt
	 */
	void configureVisualStudioCode() {
		project.logger.info('Info: Aktualisiere Visual Studio Code-Konfiguration für Projekt ' + project.name.toString())
		project.ext.setVSCSettings(project.file('.vscode/settings.json'), project.getRootProject().file('config/vscode/settings.json'))
	}

	/**
	 * Fügt dem Gradle-Projekt die Aufgabe 'initVSCode' hinzu.
	 *
	 * @param project das Gradle-Projekt, auf das dieses Plugin angewendet wird.
	 */
	@Override
	void apply(Project project) {
		this.project = project

		def initVSCode = project.task('initVSCode') {
			group = "ide"
			description = 'Konfiguriert die Code Styles und die Inspections in IntelliJ'
			// Definition der Plugin ID
			project.pluginManager.apply "svws.gradle.svwsvscode.plugin"

			this.addSetVisualStudioCodeSettingsMethod()
			this.configureVisualStudioCode()

		}

		// Führe initVSCode bei Reload Gradle Projekte aus
		project.gradle.projectsLoaded {
			initVSCode()
		}
	}
}
