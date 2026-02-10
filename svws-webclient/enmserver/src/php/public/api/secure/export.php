<?php
/**
 * Exportiert die ENM-Daten Lehrer aus der Datenbank gzip-Datei.
 *
 * @httpMethod GET
 * @auth (Basic) Lehrer Username und Kennwort benötigt
 *
 * @return GZIP mit allen ENM Daten für diesen Lehrer
 * @responseCode 200
 */
require_once dirname(__DIR__).'/../../autoload.php';

use wenom\Application;
use wenom\ENMDatenManager;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "GET" ]);

// Prüfe, ob eine Authentifizierung mit einem gültigen Bearer-Token vorliegt
$app->auth->pruefeAccessToken();

// Erstelle für die Durchführung des Exports ein ENMDaten-Objekt aus der Datenbank und rufe diesen auf
$enmDatenManager = ENMDatenManager::createFromDatabase($app->db);
$content = $enmDatenManager->doExport();

// Exportieren des Inhaltes als gzip-Datei
header('Content-Type: application/gzip;');
echo gzencode($content, 5);
