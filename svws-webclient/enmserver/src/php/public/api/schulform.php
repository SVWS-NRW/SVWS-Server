<?php
	/**
	 * Endpunkt für das Fetchen der Schulform.
	 *
	 * @httpMethod GET
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 *
	 * @return string : kürzel der schulform (dev / stable)
	 * @responseCode 200
	 */

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "GET" ]);

	// Gib den konfigurierten Server-Modus zurück
	$enmDaten = json_decode($db->getJsonENMDaten()->daten);
	echo $enmDaten->schulform;

?>
