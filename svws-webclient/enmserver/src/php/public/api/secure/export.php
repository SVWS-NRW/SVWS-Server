<?php

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/ENMDatenManager.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "GET" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Bearer-Token vorliegt
	$auth->pruefeAccessToken();

	// Erstelle für die Durchführung des Exports ein ENMDaten-Objekt aus der Datenbank und rufe diesen auf
	$enmDatenManager = ENMDatenManager::createFromDatabase($db);
	$content = $enmDatenManager->doExport();

	// Exportieren des Inhaltes als gzip-Datei
	header('Content-Type: application/gzip;');
	echo gzencode($content, 5);

?>
