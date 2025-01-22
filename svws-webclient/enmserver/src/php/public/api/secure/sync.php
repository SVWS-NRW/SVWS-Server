<?php

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/ENMDatenManager.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "POST" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Bearer-Token vorliegt
	$auth->pruefeAccessToken();

	// Einlesen der Daten aus der GZip-komprimierte Datei
	$contentImport = Http::getMultipartGzipFileContent("file");

	// Erstelle für die Durchführung des Imports ein ENMDaten-Objekt und rufe diesen auf
	$enmDatenManagerImport = ENMDatenManager::createFromJson($contentImport);
	$enmDatenManagerImport->doImport($db);

	// Erstelle für die Durchführung des Exports ein ENMDaten-Objekt aus der Datenbank und rufe diesen auf
	$enmDatenManagerExport = ENMDatenManager::createFromDatabase($db);
	$contentExport = $enmDatenManagerExport->doExport();

	// Exportieren des Inhaltes als gzip-Datei
	header('Content-Type: application/gzip;');
	echo gzencode($contentExport, 5);

?>
