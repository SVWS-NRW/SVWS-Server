<?php
	/**
	 * Endpunkt für das Patchen von ENM-Teilleistungen.
	 *
	 * @httpMethod POST
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 * @param {id: number, patch: {Partial<ENMTeilleistung>}} Das Patch-Objekt, das die zu aktualisierenden Teilleistungen enthält.
	 * Folgende Werte können durch das Patch Objekt überschrieben werden: artID, datum, bemerkung, note
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
	$enmDatenManager->patchENMTeilleistung($db, $lehrer, $patch);
?>
