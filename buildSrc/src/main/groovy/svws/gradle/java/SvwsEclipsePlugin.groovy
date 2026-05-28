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
	 * Konfiguriert die Eclipse-Annotation-Processing-Settings.
	 * Wird nur auf Module angewendet, die das com.diffplug.eclipse.apt Plugin anwenden.
	 */
	void configureEclipseApt() {
		def gp = project
		gp.plugins.withId('com.diffplug.eclipse.apt') {
			gp.sourceSets.main.java.srcDirs += '.apt_generated'
			gp.sourceSets.test.java.srcDirs += '.apt_generated_tests'

			gp.eclipse {
				synchronizationTasks 'eclipseJdt', 'eclipseJdtApt', 'eclipseFactorypath'
			}

			gp.tasks.named('eclipseJdtApt').configure {
				doFirst {
					gp.file('.apt_generated').mkdirs()
					gp.file('.apt_generated_tests').mkdirs()
				}
				doLast {
					def prefsFile = gp.file('.settings/org.eclipse.jdt.apt.core.prefs')
					if (!prefsFile.exists()) {
						return
					}

					def props = new Properties()
					prefsFile.withInputStream {
						props.load(it)
					}
					props.setProperty('org.eclipse.jdt.apt.aptEnabled',       'true')
					props.setProperty('org.eclipse.jdt.apt.reconcileEnabled', 'true')
					props.setProperty('org.eclipse.jdt.apt.genSrcDir',        '.apt_generated')
					props.setProperty('org.eclipse.jdt.apt.genTestSrcDir',    '.apt_generated_tests')
					prefsFile.withOutputStream {
						os -> props.store(os, null)
					}
				}
			}

			gp.tasks.named('eclipseJdt').configure {
				doLast {
					def prefsFile = gp.file('.settings/org.eclipse.jdt.core.prefs')
					def props = new Properties()
					if (prefsFile.exists()) {
						prefsFile.withInputStream {
							props.load(it)
						}
					}

					props.setProperty('org.eclipse.jdt.core.compiler.processAnnotations', 'enabled')
					prefsFile.parentFile.mkdirs()
					prefsFile.withOutputStream { os -> props.store(os, null) }
				}
			}
		}
	}

	/**
	 * Fügt dem Gradle-Projekt die Aufgaben 'eclipse' hinzu.
	 *
	 * @param project das Gradle-Projekt, auf das dieses Plugin angewendet wird.
	 */
	@Override
	void apply(Project project) {
		this.project = project
		// Definition der Plugin ID. Basiert auf dem Eclipse Buildship Plugin
		project.pluginManager.apply "eclipse"

		this.addSetPropertyMethod()
		this.addSetEclipseCorePreferenceMethod()
		this.addSetEclipseUiPreferenceMethod()

		this.configureEclipse()
		this.configureEclipseApt()
	}

}
