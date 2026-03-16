<?php
/**
 * Endpunkt für das Login auf dem WeNoM-Server
 *
 * @httpMethod POST
 * @auth (Basic) Lehrer Username und Kennwort benötigt
 *
 * @return string das Json-Web-Token
 * @responseCode 200
 */
require_once dirname(__DIR__).'/../autoload.php';

use wenom\Application;
use wenom\Http;

$app = new Application();
$app->auth->pruefeHTTPMethod(["POST"]);

// Prüfe die Authentifizierung des Lehrers per Basic-Auth (BCrypt-Hash)
$lehrer = $app->auth->pruefeLehrerBasicAuth();

// Erstelle das Json-Web-Token mit einer Gültigkeit von 8 Stunden
$expiration_time = 8 * 3600;
$payload = [
    'sub' => $lehrer->id,
    'exp' => time() + $expiration_time,
    'iat' => time()
];
$jwt = Http::createJsonWebToken($payload, $app->config->getClientSessionKey());

header('Content-Type: application/json');
echo json_encode(['token' => $jwt, 'id' => $lehrer->id]);
