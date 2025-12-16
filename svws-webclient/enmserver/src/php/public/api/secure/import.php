<?php
/**
 * Importiert die ENM-Daten Lehrer aus dem SVWS Server und speichert diese in der SQLite Datenbank.
 *
 * @httpMethod POST
 * @auth (Basic) Lehrer Username und Kennwort benötigt
 *
 * @return void
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
$content = Http::getMultipartGzipFileContent("file");

// Erstelle für die Durchführung des Imports ein ENMDaten-Objekt und rufe diesen auf
$enmDatenManager = ENMDatenManager::createFromJson($content);
$enmDatenManager->doImport($app->db);
