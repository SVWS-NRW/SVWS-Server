package svws.gradle.java

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Dieses Plugin führt projektspezifische Einstellungen
 * in der Intellij-Entwicklungsumgebung durch.*/
class SvwsIntelliJPlugin implements Plugin<Project> {

	def project

	void addSetIntellijFormatterMethod() {
		project.ext.setIntellijFormatter = { File projectXml, File formatterProfile ->
			createCodeStyleConfig()

			if (!projectXml.exists()) {
				projectXml.parentFile.mkdirs()
				projectXml.createNewFile()
			}

			projectXml.write("<component name=\"ProjectCodeStyleConfiguration\">\n")
			def line
			formatterProfile.withReader { reader ->
				while ((line = reader.readLine()) != null) {
					projectXml.append("  ")
					projectXml.append(line)
					projectXml.append( "\n")
				}
			}
			projectXml.append("</component>")
		}
	}

	void addSetIntellijCleanupMethod() {
		project.ext.setIntellijCleanup = { File projectXml, File cleanupProfile ->
			createInspectionProfilesConfig()

			if (!projectXml.exists()) {
				projectXml.parentFile.mkdirs()
				projectXml.createNewFile()
			}

			projectXml.text = cleanupProfile.text
		}
	}

	private void createCodeStyleConfig() {
		def configFile = project.file('.idea/codeStyles/codeStyleConfig.xml')
		if (!configFile.exists()) {
			configFile.parentFile.mkdirs()
			configFile.createNewFile()
		}
		configFile.write("<component name=\"ProjectCodeStyleConfiguration\">\n" +
				"  <state>\n" +
				"    <option name=\"USE_PER_PROJECT_SETTINGS\" value=\"true\" />\n" +
				"  </state>\n" +
				"</component>")
	}

	private void createInspectionProfilesConfig() {
		def configFile = project.file('.idea/inspectionProfiles/profiles_settings.xml')
		if (!configFile.exists()) {
			configFile.parentFile.mkdirs()
			configFile.createNewFile()
		}
		configFile.write("<component name=\"InspectionProjectProfileManager\">\n" +
				"  <settings>\n" +
				"    <option name=\"PROJECT_PROFILE\" value=\"SVWS-Server-Cleanup\" />\n" +
				"    <option name=\"USE_PROJECT_PROFILE\" value=\"true\" />\n" +
				"    <version value=\"1.0\" />\n" +
				"    <list size=\"7\">\n" +
				"      <item index=\"0\" class=\"java.lang.String\" itemvalue=\"INFORMATION\" />\n" +
				"      <item index=\"1\" class=\"java.lang.String\" itemvalue=\"TEXT ATTRIBUTES\" />\n" +
				"      <item index=\"2\" class=\"java.lang.String\" itemvalue=\"SERVER PROBLEM\" />\n" +
				"      <item index=\"3\" class=\"java.lang.String\" itemvalue=\"INFO\" />\n" +
				"      <item index=\"4\" class=\"java.lang.String\" itemvalue=\"WEAK WARNING\" />\n" +
				"      <item index=\"5\" class=\"java.lang.String\" itemvalue=\"WARNING\" />\n" +
				"      <item index=\"6\" class=\"java.lang.String\" itemvalue=\"ERROR\" />\n" +
				"    </list>\n" +
				"  </settings>\n" +
				"</component>")
	}

	void configureIntellij() {
		project.logger.info('Info: Aktualisiere Intellij-Codestyle-Konfiguration für Projekt ' + project.name);
		project.ext.setIntellijFormatter(project.file('.idea/codeStyles/Project.xml'), project.getRootProject().file('config/intellij/IntelliJ_Formatter.xml'))
		project.ext.setIntellijCleanup(project.file('.idea/inspectionProfiles/Project_Default.xml'), project.getRootProject().file('config/intellij/IntelliJ_Cleanup.xml'))
	}

	void apply(Project project) {
		this.project = project
		project.pluginManager.apply "svws.gradle.svwsintellij.plugin"

		this.addSetIntellijFormatterMethod()
		this.addSetIntellijCleanupMethod()

		this.configureIntellij()
	}
}
