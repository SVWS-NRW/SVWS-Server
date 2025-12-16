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
require_once dirname(__DIR__).'/../autoload.php';

use wenom\Application;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "GET" ]);

// Prüfe, ob eine Authentifizierung mit einem gültigen Lehrer-Kennwort vorliegt
$lehrer = $app->auth->pruefeLehrerBasicAuth();

// Gib die Schulform zurück
$enmDaten = json_decode($app->db->getJsonENMDaten()->daten);
echo $enmDaten->schulform;
