<?php
	/**
	 * Endpunkt für das Fetchen der Schulform.
	 *
	 * @httpMethod GET
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 *
	 * @return string : kürzel der schulform
	 * @responseCode 200
	 */

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "GET" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Lehrer-Kennwort vorliegt
	$lehrer = $auth->pruefeLehrerBasicAuth();

	// Gib die Schulform zurück
	$enmDaten = json_decode($db->getJsonENMDaten()->daten);
	echo $enmDaten->schulform;

?>
