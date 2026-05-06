<?php
/**
 * Diese Datei die als Einstiegpunkt für die Rest-API-Methoden des WeNoM-Servers.
 */
require_once dirname(__DIR__, 1).'/autoload.php';

use wenom\Http;
use wenom\api\ApiOAuth2;
use wenom\api\ApiSecure;
use wenom\api\ApiClient;

// Zerlegen der Adresse aus dem Request, so das die genutzt API daraus ermittelt werden kann
$path = ltrim(parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH), '/');
$parts = explode('/', $path);
$base = $parts[0] ?? '';

// Zuordnung des Request zu einer der der API-Schnittstellen
try {
    if (($base === 'oauth') && isset($parts[1])) {
        $api = new ApiOAuth2();
        $api->handle($parts[1]);
    } elseif ($base === 'api' && isset($parts[1]) && $parts[1] === 'secure' && isset($parts[2])) {
        $api = new ApiSecure();
        $api->handle($parts[2]);
    } elseif ($base === 'api' && isset($parts[1])) {
        $api = new ApiClient();
        $api->handle($parts[1]);
    } else {
        Http::exit404NotFound();
    }
} catch (\Exception $e) {
    Http::exit500($e->getMessage());
}
