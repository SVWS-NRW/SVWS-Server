<?php
/**
 * Endpunkt zur Anforderung eines neuen Passworts für Lehrer.
 *
 * Bei "POST" wird eine E-Mail mit einem Passwort-Reset-Token versendet, sofern die E-Mail-Adresse eindeutig ist.
 *
 * @httpMethod POST
 * @auth Keine Authentifizierung erforderlich
 * @param object JSON-Objekt mit { eMailDienstlich: string } Dienstliche E-Mail-Adresse des Lehrers
 *
 * @return void
 * @responseCode 204 Erfolgreich - E-Mail wurde versendet.
 * @responseCode 400 Fehlerhafte Anfrage, wenn die E-Mail-Adresse fehlt.
 * @responseCode 409 Konflikt, falls mehrere Lehrer mit dieser E-Mail-Adresse existieren.
 * @responseCode 429 Zu viele Anfragen, falls bereits ein gültiges Token existiert.
 */
require_once dirname(__DIR__).'/../autoload.php';

use wenom\Application;
use wenom\Http;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "POST" ]);

// Lehrerdaten aus der Datenbank lesen
$data = Http::getBodyJsonObject();
$eMailDienstlich = $data->eMailDienstlich ?? null;

// Überprüfen, ob die erforderlichen Daten vorhanden sind
if (empty($eMailDienstlich)) {
    http_response_code(400); // Bad Request
    echo json_encode(['fehler' => 'Die Dienst-E-Mail ist erforderlich.']);
    exit;
}

// Lehrerdaten anhand der E-Mail-Adresse holen
if (!$app->db->checkENMLehrerByEmail($eMailDienstlich)) {
    http_response_code(409); // Conflict
    echo json_encode(['fehler' => 'Mehrere Lehrer mit dieser E-Mail-Adresse gefunden.']);
    exit;
}

$lehrerDaten = $app->db->getENMLehrerByEmail($eMailDienstlich);
$lehrerId = $lehrerDaten->id;

// Abbrechen, wenn das Token ungültig ist
if ($app->db->isENMLehrerTokenValid($lehrerId)) {
    // Token ist noch gültig
    http_response_code(429);
    echo json_encode(['error' => 'Es wurde bereits eine E-Mail zum Zurücksetzen des Passworts versendet. Bitte warten Sie, bevor Sie es erneut versuchen.']);
    exit;
}

// Token in DB speichern/aktualisieren
$token = $app->db->writeENMLehrerToken($lehrerId);

// Email Attribute setzen
$to = $eMailDienstlich;
$subject = 'WeNoM - neue Passwortanforderung';
$body = "Sie haben ein neues Passwort für Ihr WeNoM-Account angefordert. \r\n";
$body .= "Ihr Passwort-Token: ".$token;

// SMTP-Client prüfen
$smtpClient = $app->db->getSMTPClient();

// Prüfen, ob der SMTP-Client nicht null ist
if ($smtpClient !== null) {
    // E-Mail versenden
    $smtpClient->setEmail($to, $subject, $body);
    $smtpClient->sendEmail();
}

// HTTP-Statuscode 204 zurückgeben (keine Inhalte)
http_response_code(204);
