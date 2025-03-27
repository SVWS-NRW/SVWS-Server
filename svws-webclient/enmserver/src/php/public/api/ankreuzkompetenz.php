<?php
	/**
	 * Endpunkt für das Aktualisieren von Schüler-Ankreuzkompetenzen.
	 *
	 * Dieser Endpunkt ermöglicht es autorisierten Lehrern, die Ankreuzkompetenzen von Schülern zu aktualisieren.
	 * Die Aktualisierung erfolgt über eine PATCH-Anfrage, die ein JSON-Objekt mit den zu ändernden Daten enthält.
	 *
	 * @httpMethod POST
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 * @param {id: number, patch: {Partial<ENMAnkreuzkompetenzen>}} Das Patch-Objekt, das die zu aktualisierenden Ankreuzkompetenzen enthält.
	 * Folgende Werte können durch das Patch Objekt überschrieben werden: Stufen
	 * @return void
	 * @responseCode 200
	 */

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/ENMDatenManager.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "POST" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Lehrer-Kennwort vorliegt
	$lehrer = $auth->pruefeLehrerBasicAuth();

	// Erstelle für die Durchführung ein ENMDaten-Objekt aus der Datenbank und rufe dieses auf
	$enmDatenManager = ENMDatenManager::createFromDatabase($db);

	// Lese den Patch aus dem Request ein
	$patch = Http::getBodyJsonObject();
	$enmDatenManager->patchENMSchuelerAnkreuzkompetenzen($db, $lehrer, $patch);

?>
