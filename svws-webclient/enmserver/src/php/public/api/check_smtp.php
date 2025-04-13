<?php

	/**
	 * Endpunkt zur Überprüfung der SMTP-Client-Konfiguration.
	 *
	 * Bei "GET" wird geprüft, ob ein SMTP-Client vorhanden ist, und das Ergebnis als JSON-Objekt zurückgegeben.
	 *
	 * @httpMethod GET
	 * @auth Keine Authentifizierung erforderlich
	 *
	 * @return object JSON-Objekt mit { isValid: true/false }
	 * @responseCode 200 Erfolgreiche Anfrage mit JSON-Antwort.
	 */

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod(["GET"]);
	
	$smtpClient = $db->getSMTPClient();
	$isValid = ($smtpClient !== null);
		
	// Rückgabe als JSON
	header('Content-Type: application/json');
	http_response_code(200);
	echo json_encode(['isValid' => $isValid]);

?>
