<?php

	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/Config.php';
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/Database.php';

	// Wenn die Anwendung bereits initialisiert ist, dann gib den Code 409 (Conflict) zurück.
	if (Config::isAppInitialized()) {
		http_response_code(409);
		exit();
	}

	// Initialisiere die Konfiguration
	$config = new Config("config.json");

	// Initialisiere die Datenbank-Verbindung
	$db = new Database($config);

	// Wenn kein Fehler aufgetreten ist, dann wurde der Server initialisiert
	http_response_code(204);

?>
