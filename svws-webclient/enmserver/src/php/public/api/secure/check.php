<?php
/**
 * Prüft ob HTTP Methode GET ist und ob der mitgesendete Token valide ist.
 *
 * @httpMethod GET
 *
 * @return void
 * @responseCode 200
 */
require_once dirname(__DIR__).'/../../autoload.php';

use wenom\Application;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "GET" ]);

// Prüfe, ob eine Authentifizierung mit einem gültigen Bearer-Token vorliegt
$app->auth->pruefeAccessToken();
