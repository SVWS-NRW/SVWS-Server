<?php

namespace wenom\api;

use wenom\Config;
use wenom\Database;
use wenom\ENMAuth;
use wenom\Http;

/**
 * Diese Klasse verwaltet die OAuth2-Schnittstelle des WeNoM-Servers.
 */
class ApiOAuth2 {

    /**
     * Prüft den HTTP-Request und delegiert den Aufruf - sofern gültig an die konkrete API-Methode
     * Eine Prüfung der HTTP-Methode erfolgt hier vor dem konkreten Methodenaufruf.
     *
     * @param string $endpoint   der Name des Endpunkts (z.B. 'token')
     */
    public function handle(string $endpoint): void {
        $method = $_SERVER['REQUEST_METHOD'];
        if ($endpoint === 'token') {
            if ($method === 'POST') {
                $this->token();
            } else {
                Http::exit405MethodNotAllowed();
            }
        } else {
            Http::exit404NotFound();
        }
    }

    /**
     * Erstellt ein neues Access Token
     */
    private function token(): void {
        $config = new Config();
        $db = new Database($config);
        $auth = new ENMAuth($db, $config);

        // Prüfe, die Authentifizierung über Basic Auth mit dem Client-Secret
        $clientID = $auth->pruefeClientSecret();

        // Erzeuge ein neues Token und gebe diese im Erfolgsfall zurück
        $newToken = $db->createClientAccessToken($clientID);
        if ($newToken === null) {
            Http::exit403Forbidden();
        } else {
            Http::exit200OKJson(json_encode($newToken, JSON_UNESCAPED_SLASHES));
        }
    }

}
