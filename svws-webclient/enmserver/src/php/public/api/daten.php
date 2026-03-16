<?php
/**
 * Endpunkt zum exportieren der ENM-Daten Lehrer aus der Datenbank.
 *
 * @httpMethod GET
 * @auth (Bearer) Json-Web-Token für einen Lehrer benötigt
 *
 * @return GZIP mit allen ENM Daten für diesen Lehrer
 */
require_once dirname(__DIR__).'/../autoload.php';

use wenom\Application;
use wenom\ENMDatenManager;
use wenom\Http;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "GET" ]);

// Prüfe, ob der Client die GZip-Komprimierung unterstützt
if (!Http::checkAcceptGZipEncoding()) {
	Http::exit400BadRequest("Der Client unterstützt laut Header (accept-encoding) keine GZip-Komprimierung.");
}

// Prüfe, ob eine Authentifizierung mit einem gültigen Json-Web-Token vorliegt
$lehrer = $app->auth->pruefeLehrerSession();

// Erstelle für die Durchführung ein ENMDaten-Objekt aus der Datenbank und rufe dieses auf
$enmDatenManager = ENMDatenManager::createFromDatabase($app->db);
$content = $enmDatenManager->getENMDatenForLehrer($lehrer);

// Exportieren des Inhaltes als gzip
header('Content-Encoding: gzip');
header('Content-Type: application/json; charset=utf-8');
echo gzencode($content, 5);
