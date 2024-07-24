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
			createEclipseCodeFormatterConfig()
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
					projectXml.append("\n")
				}
			}
			projectXml.append("</component>\n")
		}
	}

	void addSetIntellijInspectionsMethod() {
		project.ext.setIntellijCleanup = { File projectXml, File inspectionsProfile ->
			createInspectionProfilesConfig()

			if (!projectXml.exists()) {
				projectXml.parentFile.mkdirs()
				projectXml.createNewFile()
			}

			projectXml.text = inspectionsProfile.text
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

	private void createEclipseCodeFormatterConfig() {
		def configFile = project.file('.idea/eclipseCodeFormatter.xml')
		if (!configFile.exists()) {
			configFile.parentFile.mkdirs()
			configFile.createNewFile()
		}
		configFile.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<project version=\"4\">\n" +
				"  <component name=\"EclipseCodeFormatterProjectSettings\">\n" +
				"    <option name=\"projectSpecificProfile\">\n" +
				"      <ProjectSpecificProfile>\n" +
				"        <option name=\"formatter\" value=\"ECLIPSE\" />\n" +
				"        <option name=\"optimizeImports\" value=\"false\" />\n" +
				"        <option name=\"pathToConfigFileJava\" value=\"\$PROJECT_DIR\$/config/eclipse/Eclipse_Formatter.xml\" />\n" +
				"        <option name=\"selectedJavaProfile\" value=\"SVWS-Server-Formatter\" />\n" +
				"      </ProjectSpecificProfile>\n" +
				"    </option>\n" +
				"    <option name=\"selectedGlobalProfileReference\">\n" +
				"      <GlobalProfileReference>\n" +
				"        <option name=\"id\" value=\"1716449183454\" />\n" +
				"        <option name=\"name\" value=\"SVWS\" />\n" +
				"      </GlobalProfileReference>\n" +
				"    </option>\n" +
				"  </component>\n" +
				"</project>")
	}

	private void createInspectionProfilesConfig() {
		def configFile = project.file('.idea/inspectionProfiles/profiles_settings.xml')
		if (!configFile.exists()) {
			configFile.parentFile.mkdirs()
			configFile.createNewFile()
		}
		configFile.write("<component name=\"InspectionProjectProfileManager\">\n" +
				"  <settings>\n" +
				"    <option name=\"PROJECT_PROFILE\" value=\"SVWS-Server-Inspections\" />\n" +
				"    <option name=\"USE_PROJECT_PROFILE\" value=\"true\" />\n" +
				"    <version value=\"1.0\" />\n" +
				"    <info color=\"913394\">\n" +
				"      <option name=\"FOREGROUND\" value=\"ffffff\" />\n" +
				"      <option name=\"BACKGROUND\" value=\"913394\" />\n" +
				"      <option name=\"ERROR_STRIPE_COLOR\" value=\"913394\" />\n" +
				"      <option name=\"myName\" value=\"Cleanup\" />\n" +
				"      <option name=\"myVal\" value=\"50\" />\n" +
				"      <option name=\"myExternalName\" value=\"Cleanup\" />\n" +
				"      <option name=\"myDefaultAttributes\">\n" +
				"        <option name=\"ERROR_STRIPE_COLOR\" value=\"913394\" />\n" +
				"      </option>\n" +
				"    </info>\n" +
				"    <list size=\"13\">\n" +
				"      <item index=\"0\" class=\"java.lang.String\" itemvalue=\"INFORMATION\" />\n" +
				"      <item index=\"1\" class=\"java.lang.String\" itemvalue=\"STYLE_SUGGESTION\" />\n" +
				"      <item index=\"2\" class=\"java.lang.String\" itemvalue=\"STYLE_WARNING\" />\n" +
				"      <item index=\"3\" class=\"java.lang.String\" itemvalue=\"STYLE_ERROR\" />\n" +
				"      <item index=\"4\" class=\"java.lang.String\" itemvalue=\"Cleanup\" />\n" +
				"      <item index=\"5\" class=\"java.lang.String\" itemvalue=\"TEXT ATTRIBUTES\" />\n" +
				"      <item index=\"6\" class=\"java.lang.String\" itemvalue=\"TYPO\" />\n" +
				"      <item index=\"7\" class=\"java.lang.String\" itemvalue=\"GRAMMAR_ERROR\" />\n" +
				"      <item index=\"8\" class=\"java.lang.String\" itemvalue=\"SERVER PROBLEM\" />\n" +
				"      <item index=\"9\" class=\"java.lang.String\" itemvalue=\"WEAK WARNING\" />\n" +
				"      <item index=\"10\" class=\"java.lang.String\" itemvalue=\"INFO\" />\n" +
				"      <item index=\"11\" class=\"java.lang.String\" itemvalue=\"WARNING\" />\n" +
				"      <item index=\"12\" class=\"java.lang.String\" itemvalue=\"ERROR\" />\n" +
				"    </list>\n" +
				"  </settings>\n" +
				"</component>")
	}

	void configureIntellij() {
		project.logger.info('Info: Aktualisiere Intellij-Codestyle-Konfiguration für Projekt ' + project.name);
		project.ext.setIntellijFormatter(project.file('.idea/codeStyles/Project.xml'), project.getRootProject().file('config/intellij/IntelliJ_Formatter.xml'))
		project.ext.setIntellijCleanup(project.file('.idea/inspectionProfiles/Project_Default.xml'), project.getRootProject().file('config/intellij/IntelliJ_Inspections.xml'))
	}

	void apply(Project project) {
		this.project = project
		project.pluginManager.apply "svws.gradle.svwsintellij.plugin"

		this.addSetIntellijFormatterMethod()
		this.addSetIntellijInspectionsMethod()

		this.configureIntellij()
	}
}
