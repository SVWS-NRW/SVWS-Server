package svws.gradle.java


import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Dieses Plugin führt projektspezifische Einstellungen
 * in der Eclipse-Entwicklungsumgebung durch. Es nutzt
 * dazu die Funktionen des Eclipse Buildship Plugins.
 */
class SvwsEclipsePlugin implements Plugin<Project> {

	def project

	void addSetPropertyMethod() {
		project.ext.setProperty = { File file, String key, String value ->
			def s = file.text.replaceAll("(?ms)(^\\Q${key}\\E\\s*=).*?\$", '')
			s = s.replaceAll('\n{2,}', '\n')
			file.text = s + key + '=' + value + '\n'
		}
	}

	void addSetEclipsePreferenceMethod(){
		project.ext.setEclipsePreference = { File prefsFile, String key, String value ->
			if (!prefsFile.exists()) {
				prefsFile.parentFile.mkdirs()
				prefsFile.write("eclipse.preferences.version=1\n")
			}
			project.ext.setProperty(prefsFile, key, value)
		}
	}

	void configureEclipse(){
		project.eclipse.project.file.beforeMerged { gp ->
			setEclipsePreference(file('.settings/org.eclipse.core.resources.prefs'), 'encoding/<project>', 'UTF-8')
			setEclipsePreference(file('.settings/org.eclipse.core.runtime.prefs'), 'line.separator', '\\n')
			setEclipsePreference(file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.potentialNullReference', 'info')
		}
	}

	void apply(Project project) {
		this.project = project
		project.pluginManager.apply "eclipse"

		this.addSetPropertyMethod()
		this.addSetEclipsePreferenceMethod()

		this.configureEclipse()
	}

}
