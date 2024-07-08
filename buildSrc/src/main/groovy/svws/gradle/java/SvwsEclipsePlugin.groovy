package svws.gradle.java


import org.gradle.api.Plugin
import org.gradle.api.Project
import groovy.xml.XmlParser

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

		project.ext.setEclipsePreferenceFormatter = { File prefsFile, File formatterProfile ->
			if (!prefsFile.exists()) {
				prefsFile.parentFile.mkdirs()
				prefsFile.write("eclipse.preferences.version=1\n")
			}
			def profiles = new XmlParser().parse(formatterProfile)
			profiles.children()[0].each {
				def settings = (Node) it
				def key = settings.attributes().id
				def value = settings.attributes().value
				project.ext.setProperty(prefsFile, key, value)
			}
		}
	}

	void addSetEclipseUiPreferenceMethod(){
		project.ext.setEclipseUiPreference = { File prefsFile, File cleanupProfile ->
			if (!prefsFile.exists()) {
				prefsFile.parentFile.mkdirs()
				prefsFile.createNewFile()
			}
			def profiles = new XmlParser().parse(cleanupProfile)
			profiles.children()[0].each {
				def settings = (Node) it
				def key = settings.attributes().id
				def value = settings.attributes().value
				project.ext.setProperty(prefsFile, key, value)
			}
		}
	}

	void configureEclipse(){
		project.eclipse.project.file.whenMerged { gp ->
			if (gp.name != null) {
				project.logger.info('Info: Aktualisiere Eclipse-Konfiguration für Projekt ' + gp.name);
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.core.resources.prefs'), 'encoding/<project>', 'UTF-8')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.core.runtime.prefs'), 'line.separator', '\\n')
				project.ext.setEclipseUiPreference(project.file('.settings/org.eclipse.jdt.ui.prefs'), project.getRootProject().file('config/eclipse/Eclipse_Cleanup.xml'))
				project.ext.setEclipsePreferenceFormatter(project.file('.settings/org.eclipse.jdt.core.prefs'), project.getRootProject().file('config/eclipse/Eclipse_Formatter.xml'))
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.annotation.inheritNullAnnotations', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.annotation.missingNonNullByDefaultAnnotation', 'ignore')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.annotation.nonnull', 'de.svws_nrw.base.annotations.NonNull')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.annotation.nonnull.secondary', '')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.annotation.nonnullbydefault', 'de.svws_nrw.base.annotations.NonNullByDefault')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.annotation.nonnullbydefault.secondary', '')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.annotation.nullable', 'de.svws_nrw.base.annotations.Nullable')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.annotation.nullable.secondary', '')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.annotation.nullanalysis', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.APILeak', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.annotatedTypeArgumentToUnannotated', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.annotationSuperInterface', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.assertIdentifier', 'error')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.autoboxing', 'ignore')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.comparingIdentical', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.deadCode', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.deprecation', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.deprecationInDeprecatedCode', 'enabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.deprecationWhenOverridingDeprecatedMethod', 'enabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.discouragedReference', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.emptyStatement', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.enablePreviewFeatures', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.enumIdentifier', 'error')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.explicitlyClosedAutoCloseable', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.fallthroughCase', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.fatalOptionalError', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.fieldHiding', 'ignore')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.finalParameterBound', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.finallyBlockNotCompletingNormally', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.forbiddenReference', 'error')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.hiddenCatchBlock', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.includeNullInfoFromAsserts', 'enabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.incompatibleNonInheritedInterfaceMethod', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.incompleteEnumSwitch', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.indirectStaticAccess', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.localVariableHiding', 'ignore')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.methodWithConstructorName', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.missingDefaultCase', 'ignore')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.missingDeprecatedAnnotation', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.missingEnumCaseDespiteDefault', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.missingHashCodeMethod', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.missingOverrideAnnotation', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.missingOverrideAnnotationForInterfaceMethodImplementation', 'enabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.missingSerialVersion', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.missingSynchronizedOnInheritedMethod', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.noEffectAssignment', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.noImplicitStringConversion', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.nonExternalizedStringLiteral', 'ignore')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.nonnullParameterAnnotationDropped', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.nonnullTypeVariableFromLegacyInvocation', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.nullAnnotationInferenceConflict', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.nullReference', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.nullSpecViolation', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.nullUncheckedConversion', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.overridingPackageDefaultMethod', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.parameterAssignment', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.pessimisticNullAnalysisForFreeTypeVariables', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.possibleAccidentalBooleanAssignment', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.potentialNullReference', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.potentiallyUnclosedCloseable', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.rawTypeReference', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.redundantNullAnnotation', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.redundantNullCheck', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.redundantSpecificationOfTypeArguments', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.redundantSuperinterface', 'ignore')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.reportMethodCanBePotentiallyStatic', 'ignore')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.reportMethodCanBeStatic', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.reportPreviewFeatures', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.specialParameterHidingField', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.staticAccessReceiver', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.suppressOptionalErrors', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.suppressWarnings', 'enabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.suppressWarningsNotFullyAnalysed', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.syntacticNullAnalysisForFields', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.syntheticAccessEmulation', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.terminalDeprecation', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.typeParameterHiding', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unavoidableGenericTypeProblems', 'enabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.uncheckedTypeOperation', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unclosedCloseable', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.undocumentedEmptyBlock', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unhandledWarningToken', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unlikelyCollectionMethodArgumentType', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unlikelyCollectionMethodArgumentTypeStrict', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unlikelyEqualsArgumentType', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unnecessaryElse', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unnecessaryTypeCheck', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unqualifiedFieldAccess', 'ignore')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unstableAutoModuleName', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedDeclaredThrownException', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedDeclaredThrownExceptionExemptExceptionAndThrowable', 'enabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedDeclaredThrownExceptionIncludeDocCommentReference', 'enabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedDeclaredThrownExceptionWhenOverriding', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedExceptionParameter' ,'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedImport', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedLabel', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedLocal', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedObjectAllocation', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedParameter', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedParameterIncludeDocCommentReference', 'enabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedParameterWhenImplementingAbstract', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedParameterWhenOverridingConcrete', 'disabled')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedPrivateMember', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedTypeParameter', 'info')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.unusedWarningToken', 'warning')
				project.ext.setEclipsePreference(project.file('.settings/org.eclipse.jdt.core.prefs'), 'org.eclipse.jdt.core.compiler.problem.varargsArgumentNeedCast', 'warning')
			}
		}
	}

	void apply(Project project) {
		this.project = project
		project.pluginManager.apply "eclipse"

		this.addSetPropertyMethod()
		this.addSetEclipsePreferenceMethod()
		this.addSetEclipseUiPreferenceMethod()

		this.configureEclipse()
	}

}
