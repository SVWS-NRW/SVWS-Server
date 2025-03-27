<?php
	/**
	 * Importiert die ENM-Daten Lehrer aus dem SVWS Server und speichert diese in der SQLite Datenbank.
	 *
	 * @httpMethod POST
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 *
	 * @return void
	 * @responseCode 200
	 */
	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/ENMDatenManager.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "POST" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Bearer-Token vorliegt
	$auth->pruefeAccessToken();

	// Einlesen der Daten aus der GZip-komprimierte Datei
	$content = Http::getMultipartGzipFileContent("file");

	// Erstelle für die Durchführung des Imports ein ENMDaten-Objekt und rufe diesen auf
	$enmDatenManager = ENMDatenManager::createFromJson($content);
	$enmDatenManager->doImport($db);

?>
