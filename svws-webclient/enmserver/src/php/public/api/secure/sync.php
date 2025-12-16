<?php
/**
 * Importiert und Exportert die ENM-Daten Lehrer aus dem SVWS Server und speichert diese in der SQLite Datenbank.
 *
 * @httpMethod POST
 * @auth (Basic) Lehrer Username und Kennwort benötigt
 *
 * @return GZIP mit allen ENM Daten für diesen Lehrer
 * @responseCode 200
 */
require_once dirname(__DIR__).'/../../autoload.php';

use wenom\Application;
use wenom\Http;
use wenom\ENMDatenManager;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "POST" ]);

// Prüfe, ob eine Authentifizierung mit einem gültigen Bearer-Token vorliegt
$app->auth->pruefeAccessToken();

// Einlesen der Daten aus der GZip-komprimierte Datei
$contentImport = Http::getMultipartGzipFileContent("file");

// Erstelle für die Durchführung des Imports ein ENMDaten-Objekt und rufe diesen auf
$enmDatenManagerImport = ENMDatenManager::createFromJson($contentImport);
$enmDatenManagerImport->doImport($app->db);

// Erstelle für die Durchführung des Exports ein ENMDaten-Objekt aus der Datenbank und rufe diesen auf
$enmDatenManagerExport = ENMDatenManager::createFromDatabase($app->db);
$contentExport = $enmDatenManagerExport->doExport();

// Exportieren des Inhaltes als gzip-Datei
header('Content-Type: application/gzip;');
echo gzencode($contentExport, 5);
