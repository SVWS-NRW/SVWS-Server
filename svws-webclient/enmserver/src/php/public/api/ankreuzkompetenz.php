<?php
/**
 * Endpunkt für das Aktualisieren von Schüler-Ankreuzkompetenzen.
 *
 * Dieser Endpunkt ermöglicht es autorisierten Lehrern, die Ankreuzkompetenzen von Schülern zu aktualisieren.
 * Die Aktualisierung erfolgt über eine PATCH-Anfrage, die ein JSON-Objekt mit den zu ändernden Daten enthält.
 *
 * @httpMethod POST
 * @auth (Basic) Lehrer Username und Kennwort benötigt
 * @param {id: number, patch: {Partial<ENMAnkreuzkompetenzen>}} Das Patch-Objekt, das die zu aktualisierenden Ankreuzkompetenzen enthält.
 * Folgende Werte können durch das Patch Objekt überschrieben werden: Stufen
 * @return void
 * @responseCode 200
 */
require_once dirname(__DIR__).'/../autoload.php';

use wenom\Application;
use wenom\Http;
use wenom\ENMDatenManager;
use wenom\PatchManager;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "POST" ]);

// Prüfe, ob eine Authentifizierung mit einem gültigen Lehrer-Kennwort vorliegt
$lehrer = $app->auth->pruefeLehrerBasicAuth();

// Erstelle für die Durchführung ein ENMDaten-Objekt aus der Datenbank und rufe dieses auf
$enmDatenManager = ENMDatenManager::createFromDatabase($app->db);
$patchManager = new PatchManager($enmDatenManager);

// Lese den Patch aus dem Request ein
$patch = Http::getBodyJsonObject();
$patchManager->patchENMSchuelerAnkreuzkompetenzen($lehrer, $patch);
