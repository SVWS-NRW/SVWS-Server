<?php
/**
 * Endpunkt zum exportieren der ENM-Daten Lehrer aus der Datenbank.
 *
 * @httpMethod GET
 * @auth (Basic) Lehrer Username und Kennwort benötigt
 *
 * @return GZIP mit allen ENM Daten für diesen Lehrer
 */
require_once dirname(__DIR__).'/../autoload.php';

use wenom\Application;
use wenom\ENMDatenManager;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "GET" ]);

// Prüfe, ob eine Authentifizierung mit einem gültigen Lehrer-Kennwort vorliegt
$lehrer = $app->auth->pruefeLehrerBasicAuth();

// Erstelle für die Durchführung ein ENMDaten-Objekt aus der Datenbank und rufe dieses auf
$enmDatenManager = ENMDatenManager::createFromDatabase($app->db);
$content = $enmDatenManager->getENMDatenForLehrer($lehrer);

// Exportieren des Inhaltes als gzip-Datei
header('Content-Type: application/octet-stream;');
echo gzencode($content, 5);
