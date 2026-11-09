package svws.gradle.docker

import java.nio.charset.StandardCharsets

import org.gradle.api.GradleException
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Exec

/**
 * Hilfsmethoden, um Docker-Kommandos (Build, Pull und Push) in Exec-Tasks zu konfigurieren.
 * Die Kommandos werden erst zur Ausführung des Tasks gesetzt, damit Credentials und Registry-Hosts
 * aus Providern erst dann aufgelöst werden.
 */
class DockerCommands {

	private static final boolean IS_WINDOWS = System.getProperty('os.name').toLowerCase(Locale.ROOT).contains('windows')

	private DockerCommands() {
	}

	/**
	 * Baut das Image aus dem Arbeitsverzeichnis des Tasks und versieht es mit dem Tag.
	 *
	 * @param exec       der Exec-Task, dessen Kommando gesetzt wird
	 * @param imageTag   der Tag des lokalen Images
	 */
	static void build(Exec exec, String imageTag) {
		setCommand(exec, "docker build . -t ${imageTag}")
	}

	/**
	 * Meldet sich an der Registry an, damit nachfolgende Builds und Compose-Aufrufe Images aus ihr beziehen können.
	 * Das Token wird über stdin an docker login übergeben.
	 *
	 * @param exec             der Exec-Task, dessen Kommando gesetzt wird
	 * @param registry         der Host der Registry
	 * @param registryUsername der Username für die Registry
	 * @param registryToken    das Token für die Registry
	 */
	static void login(Exec exec, Provider<String> registry, Provider<String> registryUsername, Provider<String> registryToken) {
		exec.doFirst {
			requireCredentials(registry, registryUsername, registryToken)

			println "Anmeldung an der Registry ${registry.get()}"

			setCommandWithToken(exec, loginCommand(registry, registryUsername), registryToken)
		}
	}

	/**
	 * Login, optionales Tagging (wenn remoteTag vom lokalen imageTag abweicht) und Push laufen in einem Kommando,
	 * da ein zweiter commandLine-Aufruf den ersten überschreiben würde. Das Token wird über stdin an docker login übergeben.
	 *
	 * @param exec                der Exec-Task, dessen Kommando gesetzt wird
	 * @param imageTag            der Tag des lokalen Images
	 * @param remoteTagOrProvider der Tag in der Registry als String oder als Provider (erst zur Ausführung aufgelöst,
	 *                            z. B. wenn der Registry-Host im Tag steht)
	 * @param registry            der Host der Registry
	 * @param registryUsername    der Username für die Registry
	 * @param registryToken       das Token für die Registry
	 */
	static void loginAndPush(Exec exec, String imageTag, Object remoteTagOrProvider, Provider<String> registry,
			Provider<String> registryUsername, Provider<String> registryToken) {
		exec.doFirst {
			requireCredentials(registry, registryUsername, registryToken)

			def remoteTag = (remoteTagOrProvider instanceof Provider ? remoteTagOrProvider.get() : remoteTagOrProvider) as String

			println "Veröffentlichung des Images ${remoteTag} in die Registry ${registry.get()}"

			def tagCmd = (imageTag == remoteTag) ? "" : " && docker tag ${imageTag} ${remoteTag}"

			setCommandWithToken(exec, "${loginCommand(registry, registryUsername)}${tagCmd} && docker push ${remoteTag}", registryToken)
		}
	}

	/**
	 * Login, Pull und Tagging als lokales Image laufen in einem Kommando. Damit wird ein Image aus einer Registry
	 * bezogen, statt es selbst zu bauen.
	 *
	 * @param exec             der Exec-Task, dessen Kommando gesetzt wird
	 * @param remoteTag        der Tag in der Registry als Provider, da der Registry-Host im Tag steht
	 * @param imageTag         der Tag des lokalen Images
	 * @param registry         der Host der Registry
	 * @param registryUsername der Username für die Registry
	 * @param registryToken    das Token für die Registry
	 */
	static void loginAndPull(Exec exec, Provider<String> remoteTag, String imageTag, Provider<String> registry,
			Provider<String> registryUsername, Provider<String> registryToken) {
		exec.doFirst {
			requireCredentials(registry, registryUsername, registryToken)

			println "Bezug des Images ${remoteTag.get()} aus der Registry ${registry.get()}"

			setCommandWithToken(exec, "${loginCommand(registry, registryUsername)} && docker pull ${remoteTag.get()} && docker tag ${remoteTag.get()} ${imageTag}", registryToken)
		}
	}

	private static String loginCommand(Provider<String> registry, Provider<String> registryUsername) {
		return "docker login ${registry.get()} --username ${registryUsername.get()} --password-stdin"
	}

	private static void requireCredentials(Provider<String> registry, Provider<String> registryUsername, Provider<String> registryToken) {
		if (!registry.isPresent())
			throw new GradleException('Es existiert kein Host für die Registry.')

		if (!registryUsername.isPresent())
			throw new GradleException('Es existiert kein Username für die Registry.')

		if (!registryToken.isPresent())
			throw new GradleException('Es existiert kein Token für die Registry.')
	}

	private static void setCommand(Exec exec, String command) {
		if (IS_WINDOWS)
			exec.commandLine "cmd", "/c", command
		else
			exec.commandLine "sh", "-c", command
	}

	private static void setCommandWithToken(Exec exec, String command, Provider<String> registryToken) {
		setCommand(exec, command)
		exec.standardInput = new ByteArrayInputStream((registryToken.get() + System.lineSeparator()).getBytes(StandardCharsets.UTF_8) as byte[])
	}

}
