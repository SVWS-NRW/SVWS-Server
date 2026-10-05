package svws.gradle.scripts

class GitUtils {

	/**
	 * Ermittelt den aktuellen Git-Hash (HEAD) für das übergebene Projektverzeichnis.
	 *
	 * Auslesen des Git-Hashs primär durch "git rev-parse HEAD" (funktioniert auch im Kontext von
	 * Git-Submodulen und -Worktrees). Ist der Git-Client nicht verfügbar, schlägt der Aufruf fehl
	 * oder wird das Warten auf den Prozess unterbrochen, wird als Fallback der Git-Hash direkt aus
	 * den Dateien im ".git"-Verzeichnis ausgelesen.
	 *
	 * @param rootDir - Verzeichnis, in dem der Git-Hash ermittelt werden soll
	 *
	 * @return  Git-Hash als String oder null
	 */
	def static getGitHash = { File rootDir ->
		try {
			def gitProcess = ["git", "rev-parse", "HEAD"].execute(null, rootDir)
			// stdout und stderr müssen parallel zum Warten auf den Prozess konsumiert werden, um einen
			// Deadlock bei vollem Pipe-Puffer zu verhindern.
			def out = new StringBuilder()
			def err = new StringBuilder()
			gitProcess.consumeProcessOutput(out, err)
			gitProcess.waitFor()
			if (gitProcess.exitValue() == 0)
				return out.toString().trim()
			// Fehlerursache sichtbar machen, bevor auf das dateibasierte Verfahren zurückgefallen wird
			println "Warnung: 'git rev-parse HEAD' lieferte Exit-Code ${gitProcess.exitValue()}: ${err.toString().trim()}"
		} catch (IOException ignored) {
			// Git nicht verfügbar (z. B. kein git im PATH) - auf das dateibasierte Verfahren zurückfallen
		} catch (InterruptedException ignored) {
			// Bei Unterbrechung des Git-Prozesses Interrupt-Status wiederherstellen und zurückfallen,
			// damit der Build weiterlaufen kann
			Thread.currentThread().interrupt()
		}
		return getGitHashFromFiles(rootDir)
	}

	/**
	 * Fallback-Verfahren, das den Git-Hash direkt aus den Dateien im ".git"-Verzeichnis ausliest,
	 * ohne den Git-Client aufzurufen.
	 *
	 * @param rootDir das Wurzelverzeichnis des Projekts, in dem der Git-Hash ermittelt werden soll
	 *
	 * @return der Git-Hash als String oder null, falls er nicht ermittelt werden konnte
	 */
	def static getGitHashFromFiles = { File rootDir ->
		def gitHASH = null
		def gitHeadFile = new File(rootDir, ".git/HEAD")
		if (gitHeadFile.exists()) {
			def gitHEADFileString = gitHeadFile.text
			if (gitHEADFileString.trim().length() == 40) {
				// HEAD enthält bereits direkt den Hash (detached HEAD)
				gitHASH = gitHEADFileString.trim()
			} else {
				// HEAD enthält einen Verweis auf einen Branch ("ref: refs/heads/...")
				def gitHEADRefParts = gitHEADFileString.split(":")
				if ((gitHEADRefParts.length == 2) && (gitHEADRefParts[0].trim().equals("ref"))) {
					def gitRefFile = new File(rootDir, ".git/" + gitHEADRefParts[1].trim())
					if (gitRefFile.exists()) {
						def gitHEADHASHFileString = gitRefFile.text
						if (gitHEADHASHFileString.trim().length() == 40)
							gitHASH = gitHEADHASHFileString.trim()
					} else {
						// Der Branch wurde gepackt, der Hash muss aus "packed-refs" gelesen werden
						def gitPackedRefsFile = new File(rootDir, ".git/packed-refs")
						if (gitPackedRefsFile.exists()) {
							def gitPackedRefsFileString = gitPackedRefsFile.text
							gitPackedRefsFileString.eachLine {
								String[] tokens = it.split(" ")
								if ((tokens.length == 2) && (tokens[1].trim().equals(gitHEADRefParts[1].trim())))
									gitHASH = tokens[0].trim()
							}
						}
					}
				}
			}
		}
		return gitHASH
	}
}
