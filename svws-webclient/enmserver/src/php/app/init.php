<?php

	/* In dieser Datei wird die Anwendung initialisiert. Diese muss bei jedem API-Aufruf erfolgen. */

	// Überprüfung auf CORS-Header
	require_once 'Http.php';
	Http::checkCORS();

	// Lade die Konfiguration der Anwendung - Die Pfadangabe ist relativ zum root-Verzeichnis der Anwendung
	require_once 'Config.php';
	$config = new Config("config.json");

	// Initialisiere die Datenbank-Verbindung
	require_once 'Database.php';
	$db = new Database($config);

	// Bestimme die Informationen zur Authentifizierung
	require_once 'Auth.php';
	$auth = new Auth($db, $config);

?>