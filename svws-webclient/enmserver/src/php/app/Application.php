<?php

namespace wenom;

use wenom\Http;
use wenom\Config;
use wenom\Database;
use wenom\ENMAuth;

/**
 * Die Klasse repräsentiert Applikation des Web-Notenmanagers.
 */
class Application {

    // Die Konfiguration
    public $config;

    // Die Klasse für den Datenbank-Zugriff
    public $db;

    // Die Klasse für den Zugriff auf die Authentifizierung
    public $auth;


    /**
     * Erstellt und initialisiert den Web-Notenmanager
     */
    public function __construct() {
        // Überprüfung auf CORS-Header
        Http::checkCORS();

        // Lade die Konfiguration der Anwendung
        $this->config = new Config();

        // Initialisiere die Datenbank-Verbindung
        $this->db = new Database($this->config);

        // Bestimme die Informationen zur Authentifizierung
        $this->auth = new ENMAuth($this->db, $this->config);
    }

}
