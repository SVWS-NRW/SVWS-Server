<?php
	/**
	 * Endpunkt für das Fetchen von Mode.
	 *
	 * @httpMethod GET
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 *
	 * @return string : servermode (dev / stable)
	 * @responseCode 200
	 */

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "GET" ]);

	// Gib den konfigurierten Server-Modus zurück
	echo $config->getServerMode();

?>
