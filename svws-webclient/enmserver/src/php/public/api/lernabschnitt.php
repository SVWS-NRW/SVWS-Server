<?php
	/**
	 * Endpunkt für das Patchen von ENM-Lernabschnitte.
	 *
	 * @httpMethod POST
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 * @param {id: number, patch: {Partial<ENMLernabschnitt>}} Das Patch-Objekt, das die zu aktualisierenden Lernabschnittsdaten enthält.
	 * Folgende Werte können durch das Patch Objekt überschrieben werden: fehlstundenGesamt, fehlstundenGesamtUnentschuldigt
	 *
	 * @return void
	 * @responseCode 200
	 */

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'] . '/../app/init.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/../app/ENMDatenManager.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "POST" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Lehrer-Kennwort vorliegt
	$lehrer = $auth->pruefeLehrerBasicAuth();

	// Erstelle für die Durchführung ein ENMDaten-Objekt aus der Datenbank und rufe dieses auf
	$enmDatenManager = ENMDatenManager::createFromDatabase($db);

	// Lese den Patch aus dem Request ein
	$patch = Http::getBodyJsonObject();
	$enmDatenManager->patchENMSchuelerLernabschnitt($db, $lehrer, $patch);

?>
