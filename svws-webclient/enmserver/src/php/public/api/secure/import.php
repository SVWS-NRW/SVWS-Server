<?php

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/ENMDatenManager.php';

	// Erhöhe das Memory-Limit, da diese Operation ggf. viel Speicher benötigt
	Http::increaseMemoryLimit();

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod("POST");

	// Prüfe, ob eine Authentifizierung mit einem gültigen Bearer-Token vorliegt
	$auth->pruefeAccessToken();

	// Einlesen der Daten aus der GZip-komprimierte Datei
	$content = Http::getMultipartGzipFileContent("file");

	// Erstelle für die Durchführung des Imports ein ENMDaten-Objekt und rufe diesen auf
	$enmDatenManager = ENMDatenManager::createFromJson($content);
	$enmDatenManager->doImport($db);

?>
