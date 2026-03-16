<?php
/**
 * Endpunkt für das Fetchen der Schulform.
 *
 * @httpMethod GET
 * @auth (Bearer) Json-Web-Token für einen Lehrer benötigt
 *
 * @return string : kürzel der schulform
 * @responseCode 200
 */
require_once dirname(__DIR__).'/../autoload.php';

use wenom\Application;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "GET" ]);

// Prüfe, ob eine Authentifizierung mit einem gültigen Json-Web-Token vorliegt
$lehrer = $app->auth->pruefeLehrerSession();

// Gib die Schulform zurück
$enmDaten = json_decode($app->db->getJsonENMDaten()->daten);
echo $enmDaten->schulform;
