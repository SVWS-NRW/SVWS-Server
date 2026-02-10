<?php
/**
 * Löscht alle ENM Daten im ENM Server.
 *
 * @httpMethod POST
 * @auth (Basic) Lehrer Username und Kennwort benötigt
 *
 * @return void
 * @responseCode 200
 */
require_once dirname(__DIR__).'/../../autoload.php';

use wenom\Application;
use wenom\ImportManager;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "POST" ]);

// Prüfe, ob eine Authentifizierung mit einem gültigen Bearer-Token vorliegt
$app->auth->pruefeAccessToken();

// Entfernen aller ENM-Daten aus der Datenbank
ImportManager::clearENMDaten($app->db->conn);
