<?php
	/**
	 * Prüft ob HTTP Methode GET ist und ob der mitgesendete Token valide ist.
	 *
	 * @httpMethod GET
	 *
	 * @return void
	 * @responseCode 200
	 */

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "GET" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Bearer-Token vorliegt
	$auth->pruefeAccessToken()

?>
