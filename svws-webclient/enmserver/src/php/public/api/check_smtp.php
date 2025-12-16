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
require_once dirname(__DIR__).'/../autoload.php';

use wenom\Application;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod(["GET"]);

$smtpClient = $app->db->getSMTPClient();
$isValid = ($smtpClient !== null);
	
// Rückgabe als JSON
header('Content-Type: application/json');
http_response_code(200);
echo json_encode(['isValid' => $isValid]);
