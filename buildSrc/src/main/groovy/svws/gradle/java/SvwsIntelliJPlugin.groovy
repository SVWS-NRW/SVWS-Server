package svws.gradle.java

import groovy.xml.XmlNodePrinter
import groovy.xml.XmlParser
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * Dieses Plugin führt projektspezifische Einstellungen in der Intellij-Entwicklungsumgebung durch.
 */
class SvwsIntelliJPlugin implements Plugin<Project> {

	// Aktuell betrachtetes Projekt/Modul innerhalb des SVWS Projekts
	def project

	/**
	 * Fügt die Methode zum setzen der IntelliJ Formatter Optionen hinzu. Diese Methode wird auf alle Projekte/Module angewandt
	 */
	void addSetIntellijFormatterMethod() {
		project.ext.setIntellijFormatter = { File projectXml, File configXml ->
			// Erstellt oder überschreibt die Einstellungen für den Eclipse Formatter Adapter
			overrideFile((File) project.file('.idea/eclipseCodeFormatter.xml'), (File) project.getRootProject().file('config/intellij/IntelliJ_Eclipse_Formatter_Adapter_config.xml'))
			// Erstellt oder überschreibt die Code Styles config
			overrideFile((File) project.file('.idea/codeStyles/codeStyleConfig.xml'), (File) project.getRootProject().file('config/intellij/IntelliJ_CodeStyle_config.xml'))

			if (!projectXml.exists()) {
				createFile(projectXml)
			}

			// Erstellt den parent "component" für die Konfiguration
			Node codeStyleXml = new Node(null, 'codeStyleXml', [name: 'ProjectCodeStyleConfiguration'])
			Node configParsed = new XmlParser().parse(configXml)

			codeStyleXml.append(configParsed)

			printXml(projectXml, codeStyleXml)
		}
	}

	/**
	 * Fügt die Methode zum Merge der IntelliJ Inspections hinzu. Diese Methode wird auf alle Projekte/Module angewandt
	 */
	void addSetIntellijInspectionsMethod() {
		project.ext.setIntellijInspections = { File projectXml, File configXml ->
			createInspectionProfilesConfig()

			// Wenn keine projectXml existiert, wird diese erstellt und der Inhalt von configXml übertragen
			if (!projectXml.exists()) {
				overrideFile(projectXml, configXml)
				return
			}

			// Parsen der configXml für den Zugriff auf die XML Elemente
			def (configInspections) = getParsedInspections(configXml)

			// Hat configXml keine Inspection Konfigurationen, kann der Vorgang abgebrochen werden
			if (configInspections.isEmpty()) {
				return
			}

			// Parsen der projectXml für den Zugriff auf die XML Elemente
			def (List<Object> projectInspections, Node projectParsed) = getParsedInspections(projectXml)

			// Hat projectXml keine Inspection Konfigurationen, kann alles aus configXml übernommen werden
			if (projectInspections.isEmpty()) {
				overrideFile(projectXml, configXml)
				return
			}

			mergeInspections(configInspections, projectInspections, projectParsed)

			printXml(projectXml, projectParsed)
		}
	}

	/**
	 * Erstellt die Datei im angegebenen Pfad
	 *
	 * @param file Zu erstellende Datei
	 */
	private void createFile(File file) {
		if (file.exists()) {
			project.logger.info('Die Datei ' + file.name + ' existiert bereits und wird daher nicht erstellt.')
			return
		}
		file.parentFile.mkdirs()
		file.createNewFile()
	}

	/**
	 * Fügt eine Inspection Konfiguration in die Projekt konfiguration hinzu. Dabei wird die alphabetische Reihenfolge des class Attributs berücksichtigt
	 *
	 * @param projectParsed Das parsed XML in welches die Konfiguration configInspection gemerged werden soll
	 * @param configInspection Die Konfiguration, die in content übertragen werden soll
	 */
	private static void addInspectionToProject(Node projectParsed, configInspection) {
		// Ermittle den richtigen Index basierend auf der alphabetischen Reihenfolge der Konfigurationen (abhängig von 'class')
		def insertIndex = projectParsed.profile.first().children().findIndexOf { projectInspection ->
			projectInspection.'@class' > configInspection.'@class'
		}

		// Füge die Konfiguration hinzu
		if (insertIndex == -1) {
			projectParsed.profile.first().append(configInspection)
		} else {
			projectParsed.profile.first().children().add(insertIndex, configInspection)
		}
	}

	/**
	 * Parsed die XML und generiert eine Liste der Inspection Konfigurationen
	 *
	 * @param xml Die XML Datei, die geparsed werden soll
	 * @return Die Liste aller Inspection Konfigurationen sowie die parsed XML Datei
	 */
	private static List getParsedInspections(File xml) {
		Node projectParsed = new XmlParser().parse(xml)
		List<Object> projectInspections = projectParsed.depthFirst().findAll { it.name() == 'inspection_tool' }
		[projectInspections, projectParsed]
	}

	/**
	 * Übertrage Inhalt in eine XML File
	 *
	 * @param xml XML Datei, die überschrieben werden soll
	 * @param content Der Inhalt, der in die XML Datei geschrieben werden soll
	 */
	private static void printXml(File xml, Object content) {
		xml.withWriter('UTF-8') { writer ->
			def printer = new XmlNodePrinter(new PrintWriter(writer))
			printer.setPreserveWhitespace(false) // Keine Leerzeilen erhalten
			printer.print(content)
		}
	}

	/**
	 * Überschreibt target mit dem Inhalt von source. Falls target nicht existiert, wird es erst erstellt.
	 *
	 * @param target Datei, die überschrieben werden soll
	 * @param source Inhalt, der in target geschrieben werden soll
	 */
	private void overrideFile(File target, File source) {
		// Wenn die Quelldatei nicht existiert, kann der Vorgang abgebrochen werden
		if (!source.exists()) {
			return
		}

		if (!target.exists()) {
			createFile(target)
		}

		// Inhalt der Zieldatei überschreiben
		target.text = source.text
	}

	/**
	 * Erstellt die config Datei, die die spezielle Severity "Cleanup" zur Verfügung stellt und das merged Inspections Profil als Default verwendet.
	 * Falls die config bereits existiert, wird sie gemerged.
	 */
	private void createInspectionProfilesConfig() {
		// Die Quelldatei mit den vorgegebenen Severities
		File configFile = project.getRootProject().file('config/intellij/IntelliJ_Inspections_config.xml')
		// Wenn keine Source File existiert, wird der Vorgang abgebrochen
		if (!configFile.exists()) {
			return
		}

		// Die Zieldatei innerhalb des Projekts, in die die Cleanup Severity der projectFile germerged werden soll
		File projectFile = project.file('.idea/inspectionProfiles/profiles_settings.xml')
		// Wenn im Projekt keine Konfiguration existiert, kann diese einfach kopiert werden
		if (!projectFile.exists()) {
			overrideFile(projectFile, configFile)
			return
		}

		boolean updateNecessary = false
		Node projectParsed = new XmlParser().parse(projectFile)
		def configProfile = new XmlParser().parse(configFile).settings.option.find { it.@name == 'PROJECT_PROFILE' }
		def projectProfile = projectParsed.settings.option.find { it.@name == 'PROJECT_PROFILE' }
		def projectSettings = projectParsed.settings.first().children()


		// Ist im Projekt kein Profil gesetzt oder nicht das SVWS Profil, dann wird dies überschrieben
		if (!projectProfile) {
			projectSettings.add(0, configProfile)
			updateNecessary = true
		} else if (projectProfile.'@value' != configProfile.'@value') {
			projectProfile.'@value' = configProfile.'@value'
			updateNecessary = true
		}


		def projectColor = projectParsed.settings.info.option.find { it.@name == 'myName' && it.@value == 'Cleanup' }

		// Falls im Projekt bereits eine Severity mit Namen "Cleanup" definiert ist, bleibt diese unberührt
		if (!projectColor) {

			// Sollte in der configFile keine Cleanup-Severity definiert sein, wird der Vorgang abgebrochen
			def configColor = new XmlParser().parse(configFile).settings.info.option.find { it.@name == 'myName' && it.@value == 'Cleanup' }
			if (configColor == null) {
				return
			}

			// Neue Farbe an der richtigen Stelle über der Liste hinzufügen
			def index = projectSettings.indexOf(projectParsed.settings.list.first())
			if (index != -1) {
				projectSettings.add(index, configColor.parent())
			} else {
				projectSettings.add(configColor.parent())
			}

			// Hinzufügen der Cleanup Severity zur Liste der Errors und Warnings (am Ende der Liste)
			def list = projectParsed.settings.list.first()
			// Der neue Index ist die aktuelle Größe der Liste
			String newIndex = list.item.size()
			Node newItem = new XmlParser().parseText('<item index="' + newIndex + '" class="java.lang.String" itemvalue="Cleanup" />')
			list.append(newItem)

			// Aktualisieren der "size" des list Elements
			list.@size = (newIndex + 1).toString()
			updateNecessary = true
		}


		if (updateNecessary) {
			// Schreibt das modifizierte XML in die projectFile
			printXml(projectFile, projectParsed)
		}

	}

	/**
	 *  Merge der Inspections aus der config file und der project file.
	 *
	 *  Jede Inspections Konfiguration der configInspections wird überprüft und mit den projectInspections verglichen.
	 * 	configInspections enthält ausschließlich Inspection Konfigurationen, die für Cleanups in Java relevant sind. projectInspections hingegen kann weitere
	 * 	Konfigurationen enthalten.
	 * 	Konfigurationen, die für Cleanups in Java relevant sind, dürfen ausschließlich folgende Level haben: Cleanup, TEXT ATTRIBUTES und INFORMATION.
	 * 	Andernfalls werden beim Cleanup Verbesserungen ausgeführt, die nicht vorgesehen sind.
	 * 	Das Level Cleanup durch projectInspections zu setzen ist nicht erlaubt. Dieses Level darf ausschließlich durch configInspections gesetzt werden
	 *
	 * @param configInspections Alle konfigurierten Inspections der config file
	 * @param projectInspections Alle konfigurierten Inspections der project file
	 * @param projectParsed Die parsed project file, in die alle Inspections gemerged werden
	 */
	private static void mergeInspections(List<Object> configInspections, List<Object> projectInspections, Node projectParsed) {
		configInspections.forEach(configInspection -> {
			// Suche in xml, ob die gleiche Konfiguration enthalten ist
			def matchingProjectInspection = projectInspections.find { projectInspection ->
				projectInspection.'@class' == configInspection.'@class'
			}
			// Die Konfiguration ist nicht in projectInspections enthalten und muss an der richtigen Stelle eingefügt werden
			if (!matchingProjectInspection) {
				addInspectionToProject(projectParsed, configInspection)
				return
			}

			/* Die Konfiguration aus configInspections ist auch in projectInspections vorhanden */

			// Ist in configInspections das Level Cleanup gesetzt, so muss die komplette Konfiguration dieser Inspection in projectInspections überschrieben werden
			if (configInspection.'@level' == 'Cleanup') {
				matchingProjectInspection.replaceNode(configInspection)
				return
			}

			// Haben beide Konfigurationen dasselbe Level, dann werden die Einstellungen aus projectInspections beibehalten
			if (matchingProjectInspection.'@level' == configInspection.'@level') {
				return
			}

			// Haben beide Konfigurationen nicht dasselbe Level, aber die Konfiguration in projectInspections enthält das zulässige Level TEXT ATTRIBUTES oder
			// INFORMATION, dann werden auch hier die Konfigurationen der projectInspections beibehalten
			if (matchingProjectInspection.'@level' == 'TEXT ATTRIBUTES' || matchingProjectInspection.'@level' == 'INFORMATION') {
				return
			}

			// Die Konfiguration aus configInspections enthält nicht das zulässige Level TEXT ATTRIBUTES oder INFORMATION.
			// Das Level muss korrigiert werden und wird auf INFORMATION gesetzt (Fix wird im Editor angeboten)
			// Die restliche Konfiguration der Inspection (z.B. Highlighting im Editor) werden von projectInspections beibehalten
			if (configInspection.'@level' == 'TEXT ATTRIBUTES' || configInspection.'@level' == 'INFORMATION') {
				matchingProjectInspection.'@level' = 'TEXT ATTRIBUTES'
			}
		})
	}

	/**
	 * Konfiguriert projektspezifisch die IntelliJ IDE
	 */
	void configureIntellij() {
		project.logger.info('Info: Aktualisiere Intellij-Konfigurationen für Codestyles und Inspections für Projekt ' + project.name.toString())
		project.ext.setIntellijFormatter(project.file('.idea/codeStyles/Project.xml'), project.getRootProject().file('config/intellij/IntelliJ_Formatter.xml'))
		project.ext.setIntellijInspections(project.file('.idea/inspectionProfiles/SVWS_Server_Inspections.xml'), project.getRootProject().file('config/intellij/IntelliJ_Inspections.xml'))
	}

	void apply(Project project) {
		this.project = project
		// ID des Plugins
		project.pluginManager.apply "svws.gradle.svwsintellij.plugin"

		// Füge Methode zum Überschreiben der Formatter Optionen hinzu
		this.addSetIntellijFormatterMethod()
		// Füge Methode zum Merge der Inspections Optionen hinzu
		this.addSetIntellijInspectionsMethod()

		// Starte die Konfiguration der IntelliJ IDE
		this.configureIntellij()
	}
}
