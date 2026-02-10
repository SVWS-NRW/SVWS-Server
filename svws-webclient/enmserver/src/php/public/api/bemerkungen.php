<?php
/**
 * Endpunkt zum Aktualisieren von Schülerbemerkungen.
 *
 * Dieser Endpunkt ermöglicht es autorisierten Lehrern, Bemerkungen zu Schülern über einen PATCH-Request zu aktualisieren.
 *
 * @httpMethod POST
 * @auth (Basic) Lehrer Username und Kennwort benötigt
 * @param int $id Die ID des Schülers, dessen Bemerkungen aktualisiert werden sollen.
 * @param {id: number, patch: {Partial<ENMAnkreuzkompetenzen>}} Das Patch-Objekt, das die zu aktualisierenden Bemerkungen enthält.
 * Folgende Werte können durch das Patch Objekt überschrieben werden: ASV, AUE, ZB, LELS, schulformEmpf, individuelleVersetzungsbemerkungen, foerderbemerkungen
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

// Lese den Patch aus dem Request ein, hier liegt die ID als Attribut 'id' vor und der eigentliche Patch als Attribut 'patch'
$patch = Http::getBodyJsonObject();
$patchManager->patchENMSchuelerBemerkungen($lehrer, $patch->id, $patch->patch);
