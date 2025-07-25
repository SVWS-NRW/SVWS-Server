<?php
	/**
	 * Endpunkt zum exportieren der ENM-Daten Lehrer aus der Datenbank.
	 *
	 * @httpMethod GET
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 *
	 * @return GZIP mit allen ENM Daten für diesen Lehrer
	 */

	// Initialisierung
	require_once __DIR__.'/../../app/init.php';
	require_once __DIR__.'/../../app/ENMDatenManager.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "GET" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Lehrer-Kennwort vorliegt
	$lehrer = $auth->pruefeLehrerBasicAuth();

	// Erstelle für die Durchführung ein ENMDaten-Objekt aus der Datenbank und rufe dieses auf
	$enmDatenManager = ENMDatenManager::createFromDatabase($db);
	$content = $enmDatenManager->getENMDatenForLehrer($lehrer);

	// Exportieren des Inhaltes als gzip-Datei
	header('Content-Type: application/octet-stream;');
	echo gzencode($content, 5);

?>
