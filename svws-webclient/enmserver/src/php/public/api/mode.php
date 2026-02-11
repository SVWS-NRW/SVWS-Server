<?php
/**
 * Endpunkt für das Fetchen von Mode.
 *
 * @httpMethod GET
 *
 * @return string : servermode (dev / stable)
 * @responseCode 200
 */
require_once dirname(__DIR__).'/../autoload.php';

use wenom\Application;

$app = new Application();

// Prüfe die HTTP-Methode
$app->auth->pruefeHTTPMethod([ "GET" ]);

// Gib den konfigurierten Server-Modus zurück
echo $app->config->getServerMode();
