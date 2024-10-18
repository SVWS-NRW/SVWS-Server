package svws.gradle.java


import org.gradle.api.Plugin
import org.gradle.api.Project
import groovy.xml.XmlParser

/**
 * Dieses Plugin führt projektspezifische Einstellungen
 * in der Eclipse-Entwicklungsumgebung durch. Es nutzt
 * dazu die Funktionen des Eclipse Buildship Plugins.*/
class SvwsEclipsePlugin implements Plugin<Project> {

	// Aktuell betrachtetes Projekt/Modul innerhalb des SVWS Projekts
	def project

	/**
	 * Fügt die Methode hinzu, mit der eine Property in den Eclipse config Files gesetzt wird. Besitzt die config File bereits den angegebenen key,
	 * dann wird von diesem nur der value überschrieben.
	 * Diese Methode wird auf alle Projekte/Module angewendet.
	 */
	void addSetPropertyMethod() {
		project.ext.setProperty = { File file, String key, String value ->
			file = createFileIfAbsent(file)
			def newLines = file.readLines().findAll { !it.trim().startsWith("${key}=") }
			newLines.add("${key}=${value}")
			file.text = newLines.join('\n') + '\n'
		}
	}

	/**
	 * Fügt die Methode hinzu, die Eclipse Core Preferences in die config File der IDE schreibt. Darunter fallen Formatter und Compiler Optionen.
	 * Diese Methode wird auf alle Projekte/Module angewendet.
	 */
	void addSetEclipseCorePreferenceMethod() {
		// Entfernt alle Einstellungen zum Formatter
		project.ext.setEclipseCleanFormatter = { File prefsFile ->
			if (prefsFile.exists()) {
				def lines = prefsFile.readLines()
				def newLines = lines.findAll { !it.trim().startsWith('org.eclipse.jdt.core.formatter') }

				prefsFile.text = newLines.join('\n') + '\n'
			}
		}

		// Fügt die Formatter Optionen in die config File hinzu
		project.ext.setEclipseFormatter = { File prefsFile, File formatterProfile ->
			def profiles = new XmlParser().parse(formatterProfile)
			profiles.children()[0].each {
				def settings = (Node) it
				def key = settings.attributes().id
				def value = settings.attributes().value
				project.ext.setProperty(prefsFile, key, value)
			}
		}

		// Fügt die Compiler Optionen in die config File hinzu
		project.ext.setEclipseCompiler = { File prefsFile, File compilerProfile ->
			def configLines = compilerProfile.readLines().findAll { it.startsWith('/instance/org.eclipse.jdt.core/') }
					.collect { it.replaceFirst('/instance/org.eclipse.jdt.core/', '') }
			configLines.each { line ->
				def parts = line.split('=', 2)
				if (parts.length == 2) {
					def key = parts[0].trim()
					def value = parts[1].trim()
					project.ext.setProperty(prefsFile, key, value)
				}
			}
		}
	}

	/**
	 * Fügt die Methode hinzu, die Eclipse UI Preferences in die config File der IDE schreibt. Darunter fallen Cleanup Optionen.
	 * Diese Methode wird auf alle Projekte/Module angewendet.
	 */
	void addSetEclipseUiPreferenceMethod() {
		project.ext.setEclipseCleanup = { File prefsFile, File cleanupProfile ->
			def profiles = new XmlParser().parse(cleanupProfile)
			profiles.children()[0].each {
				def settings = (Node) it
				def key = settings.attributes().id
				def value = settings.attributes().value
				project.ext.setProperty(prefsFile, key, value)
			}
		}
	}

	/**
	 * Erstellt eine Datei, falls diese nicht existiert
	 * @param file Die zu erstellende Datei
	 * @return Die erstellte oder bereits bestehende Datei
	 */
	static File createFileIfAbsent(File file) {
		if (!file.exists()) {
			file.parentFile.mkdirs()
			file.createNewFile()
		}
		return file
	}

	/**
	 * Konfiguration alles Eclipse Einstellungen.
	 * Diese Methode wird auf alle Projekte/Module angewendet.
	 */
	void configureEclipse() {
		project.eclipse.project.file.whenMerged { gp ->
			if (gp.name != null) {
				project.logger.info('Info: Aktualisiere Eclipse-Konfiguration für Projekt ' + gp.name.toString())
				project.ext.setProperty(project.file('.settings/org.eclipse.core.resources.prefs'), 'encoding/<project>', 'UTF-8')
				project.ext.setProperty(project.file('.settings/org.eclipse.core.runtime.prefs'), 'line.separator', '\\n')
				project.ext.setEclipseCleanFormatter(project.file('.settings/org.eclipse.jdt.core.prefs'))
				project.ext.setEclipseFormatter(project.file('.settings/org.eclipse.jdt.core.prefs'), project.getRootProject().file('config/eclipse/Eclipse_Formatter.xml'))
				project.ext.setEclipseCompiler(project.file('.settings/org.eclipse.jdt.core.prefs'), project.getRootProject().file('config/eclipse/Eclipse_Compiler.epf'))
				project.ext.setEclipseCleanup(project.file('.settings/org.eclipse.jdt.ui.prefs'), project.getRootProject().file('config/eclipse/Eclipse_Cleanup.xml'))
			}
		}
	}

	/**
	 * Fügt dem Gradle-Projekt die Aufgaben 'eclipse' hinzu.
	 *
	 * @param project das Gradle-Projekt, auf das dieses Plugin angewendet wird.
	 */
	void apply(Project project) {
		this.project = project
		// Definition der Plugin ID. Basiert auf dem Eclipse Buildship Plugin
		project.pluginManager.apply "eclipse"

		this.addSetPropertyMethod()
		this.addSetEclipseCorePreferenceMethod()
		this.addSetEclipseUiPreferenceMethod()

		this.configureEclipse()
	}

}
