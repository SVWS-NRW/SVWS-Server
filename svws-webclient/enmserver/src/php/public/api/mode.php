<?php
	/**
	 * Endpunkt für das Fetchen von Mode.
	 *
	 * @httpMethod GET
	 *
	 * @return string : servermode (dev / stable)
	 * @responseCode 200
	 */

	// Initialisierung
	require_once __DIR__.'/../../app/init.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "GET" ]);

	// Gib den konfigurierten Server-Modus zurück
	echo $config->getServerMode();

?>
