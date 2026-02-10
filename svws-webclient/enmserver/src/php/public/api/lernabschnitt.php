<?php
/**
 * Endpunkt für das Patchen von ENM-Lernabschnitte.
 *
 * @httpMethod POST
 * @auth (Basic) Lehrer Username und Kennwort benötigt
 * @param {id: number, patch: {Partial<ENMLernabschnitt>}} Das Patch-Objekt, das die zu aktualisierenden Lernabschnittsdaten enthält.
 * Folgende Werte können durch das Patch Objekt überschrieben werden: fehlstundenGesamt, fehlstundenGesamtUnentschuldigt
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
$patchManager->patchENMSchuelerLernabschnitt($lehrer, $patch);
