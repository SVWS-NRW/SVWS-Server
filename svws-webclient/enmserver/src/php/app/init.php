<?php

	/* In dieser Datei wird die Anwendung initialisiert. Diese muss bei jedem API-Aufruf erfolgen. */

	// Überprüfung auf CORS-Header
	require_once 'Http.php';
	Http::checkCORS();

	// Lade die Konfiguration der Anwendung
	require_once 'Config.php';
	$config = new Config();

	// Initialisiere die Datenbank-Verbindung
	require_once 'Database.php';
	$db = new Database($config);

	// Bestimme die Informationen zur Authentifizierung
	require_once 'ENMAuth.php';
	$auth = new ENMAuth($db, $config);

?>