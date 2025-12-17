<?php
/**
 * Endpunkt für das Patchen von ENM-Teilleistungen.
 *
 * @httpMethod POST
 * @auth (Basic) Lehrer Username und Kennwort benötigt
 * @param {id: number, patch: {Partial<ENMTeilleistung>}} Das Patch-Objekt, das die zu aktualisierenden Teilleistungen enthält.
 * Folgende Werte können durch das Patch Objekt überschrieben werden: artID, datum, bemerkung, note
 *
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
$patchManager->patchENMTeilleistung($lehrer, $patch);
