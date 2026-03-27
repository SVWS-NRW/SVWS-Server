<?php

namespace wenom\api;

use wenom\Config;
use wenom\Database;
use wenom\ENMAuth;
use wenom\Http;
use wenom\ImportManager;
use wenom\ENMDatenManager;

/**
 * Diese Klasse verwaltet die Secure-Schnittstelle für die Kommunikation mit dem SVWS-Server
 * Die Endpunkte befinden sich unter /api/secure/*
 */
class ApiSecure {

    /**
     * Prüft den HTTP-Request und delegiert den Aufruf - sofern gültig an die konkrete API-Methode
     * Eine Prüfung der HTTP-Methode erfolgt hier vor dem konkreten Methodenaufruf.
     *
     * @param string $endpoint   der Name des Endpunkts (z.B. 'Check')
     */
    public function handle(string $endpoint): void {
        // Erzeuge alle wichtigen Objekte für die Secure-API ...
        $config = new Config();
        $db = new Database($config);
        $auth = new ENMAuth($db, $config);

        // Alle Endpunkte erfordern einen gültigen Access-Tokens - prüfe diesen
        $auth->pruefeAccessToken();

        // Mapping von dem Endpunkt zu [Methode, HTTP-Verb]
        $routes = [
            'check'        => ['GET'  => fn() => $this->check()],
            'reset'        => ['POST' => fn() => $this->reset($db)],
            'truncate'     => ['POST' => fn() => $this->truncate($db)],
            'serverconfig' => [
                'GET'  => fn() => $this->getServerConfig($db),
                'PUT' => fn() => $this->putServerConfig($db)
            ],
            'export'       => ['GET'  => fn() => $this->export($db)],
            'import'       => ['POST' => fn() => $this->import($db)],
            'sync'         => ['POST' => fn() => $this->sync($db)],
        ];

        // Prüfe, ob der Endpunkt vorhanden ist
        if (!isset($routes[$endpoint])) {
            Http::exit404NotFound();
        }

        // Prüfe, ob die Request.Methode erlaubt ist
        $method = $_SERVER['REQUEST_METHOD'];
        if (!isset($routes[$endpoint][$method])) {
            Http::exit405MethodNotAllowed();
        }

        // Delegiere die Ausführung an die fachlichen Endpunkte
        $routes[$endpoint][$method]();
    }

    /**
     * Prüft die Erreichbarkeit und die Gültigkeit des Access-Tokens.
     */
    private function check(): void {
        Http::exit200OKJson("OK");
    }

    /**
     * Entfernt alle fachlichen ENM-Daten aus der Datenbank (Reset).
     *
     * @param Database db   die aktuelle Datenbank-Verbindung
     */
    private function reset(Database $db): void {
        ImportManager::clearENMDaten($db->conn);
        Http::exit200OKJson("OK");
    }

    /**
     * Führt eine Reinitialisierung der Datenbank durch (Truncate) und reinitialisiert
     * dabei die Datenbank vollständig.
     *
     * @param Database db   die aktuelle Datenbank-Verbindung
     */
    private function truncate(Database $db): void {
        $db->reinitDatbase();
        Http::exit200OKJson("OK");
    }

    /**
     * Liest die Server-Konfiguration aus
     *
     * @param Database db   die aktuelle Datenbank-Verbindung
     */
    private function getServerConfig(Database $db): void {
        Http::exit200OKJson(Database::getServerConfig($db->conn));
    }

    /**
     * Schreibt einen Konfigurationseintrag
     *
     * @param Database db   die aktuelle Datenbank-Verbindung
     */
    private function putServerConfig(Database $db): void {
        $obj = Http::getBodyJsonObject();
        if (!property_exists($obj, "key") || !property_exists($obj, "value")) {
            Http::exit400BadRequest("Fehlerhafte Anfrage: Es muss ein Schlüsselwert angegeben sein und ein Wert muss entweder gültig gesetzt oder explizit null für ein Entfernen des Eintrags sein.");
        }
        if (!property_exists($obj, "type") || (strcmp(gettype($obj->type), "string") !== 0) || ((strcmp($obj->type, "server") !== 0) && (strcmp($obj->type, "global") !== 0))) {
            Http::exit400BadRequest("Fehlerhafte Anfrage: Es muss ein Typ für den Konfigurationseintrag gesetzt sein und der muss entweder 'server' oder 'global' sein.");
        }

        $nurServer = (strcmp($obj->type, "server") === 0);
        $keytype = gettype($obj->key);
        if (strcmp($keytype, "string") !== 0) {
            Http::exit400BadRequest("Fehlerhafte Anfrage: Der Schlüsselwert muss eine Zeichenkette sein.");
        }
        $valuetype = gettype($obj->value);
        if ((strcmp($valuetype, "string") !== 0) && (strcmp($valuetype, "NULL") !== 0)) {
            Http::exit400BadRequest("Fehlerhafte Anfrage: Der Wert muss entweder eine Zeichenkette oder NULL sein.");
        }
        $db->putConfig($nurServer, $obj->key, $obj->value);

        Http::exit200OKJson("OK");
    }

    /**
     * Exportiert alle ENM-Daten als GZip
     *
     * @param Database db   die aktuelle Datenbank-Verbindung
     */
    private function export(Database $db): void {
        $enmDatenManager = ENMDatenManager::createFromDatabase($db);
        $content = $enmDatenManager->doExport();
        Http::exit200OKGZip($content);
    }

    /**
     * Importiert ENM-Daten via Multipart-GZip
     *
     * @param Database db   die aktuelle Datenbank-Verbindung
     */
    private function import(Database $db): void {
        $content = Http::getMultipartGzipFileContent("file");
        $importManager = ImportManager::createFromJson($db->conn, $content);
        $importManager->doImport();

        Http::exit200OKJson("OK");
    }

    /**
     * Synchronisiert die Daten durch einen Import der Request-Daten und einen Export des neuen Standes
     *
     * @param Database db   die aktuelle Datenbank-Verbindung
     */
    private function sync(Database $db): void {
        // Import
        $contentImport = Http::getMultipartGzipFileContent("file");
        $importManager = ImportManager::createFromJson($db->conn, $contentImport);
        $importManager->doImport();

        // Export
        $enmDatenManager = ENMDatenManager::createFromDatabase($db);
        $contentExport = $enmDatenManager->doExport();
        Http::exit200OKGZip($contentExport);
    }

}
