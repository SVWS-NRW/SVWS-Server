<?php
	/**
	 * Endpunkt für das Patchen von ENM-Leistungen.
	 *
	 * @httpMethod POST
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 * @param {id: number, patch: {Partial<ENMLeistungen>}} Das Patch-Objekt, das die zu aktualisierenden Leistungen enthält.
	 * Folgende Werte können durch das Patch Objekt überschrieben werden: tsNote, tsNoteQuartal,
	 *                                                                    tsFehlstundenFach, tsFehlstundenUnentschuldigtFach,
	 *                                                                    fehlstundenUnentschuldigtFach, fachbezogeneBemerkungen, istGemahnt
	 *
	 * @return void
	 * @responseCode 200
	 */

	// Initialisierung
	require_once __DIR__.'/../../app/init.php';
	require_once __DIR__.'/../../app/ENMDatenManager.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "POST" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Lehrer-Kennwort vorliegt
	$lehrer = $auth->pruefeLehrerBasicAuth();

	// Erstelle für die Durchführung ein ENMDaten-Objekt aus der Datenbank und rufe dieses auf
	$enmDatenManager = ENMDatenManager::createFromDatabase($db);

	// Lese den Patch aus dem Request ein
	$patch = Http::getBodyJsonObject();
	$enmDatenManager->patchENMLeistung($db, $lehrer, $patch);

?>
