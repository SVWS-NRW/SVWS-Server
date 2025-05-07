<?php
	/**
	 * Endpunkt für das Fetchen von ENM-Setup. Erstellt beim ersten Aufruf das Client Secret.
	 * Falls der Endpunkt erneut aufgerufen wird wirft der Endpunkt einen Fehler, falls ein Fehler aufgetreten ist.
	 *
	 * @httpMethod GET
	 *
	 * @return void
	 *
	 * @responseCode 204
	 * @responseCode 403 Falls die HTTP Methode kein GET ist
	 * @responseCode 409 Falls der Server bereits initiiert wurde
 	 */

	require_once __DIR__.'/../../app/Config.php';
	require_once __DIR__.'/../../app/Database.php';

	if (strcmp($_SERVER['REQUEST_METHOD'], 'GET') !== 0) {
		http_response_code(403);
		exit();
	}

	// Wenn die Anwendung bereits initialisiert ist, dann gib den Code 409 (Conflict) zurück.
	if (Config::isAppInitialized()) {
		http_response_code(409);
		exit();
	}

	// Initialisiere die Konfiguration
	$config = new Config();

	// Initialisiere die Datenbank-Verbindung
	$db = new Database($config);

	// Wenn kein Fehler aufgetreten ist, dann wurde der Server initialisiert
	http_response_code(204);

?>
