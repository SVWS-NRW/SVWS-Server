<?php
	/**
	 * Löscht alle ENM Daten im ENM Server.
	 *
	 * @httpMethod POST
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 *
	 * @return void
	 * @responseCode 200
	 */

	// Initialisierung
	require_once __DIR__.'/../../../app/init.php';
	require_once __DIR__.'/../../../app/ENMDatenManager.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "POST" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Bearer-Token vorliegt
	$auth->pruefeAccessToken();

	// Entfernen aller ENM-Daten aus der Datenbank
	$db->reinitDatbase();

?>
