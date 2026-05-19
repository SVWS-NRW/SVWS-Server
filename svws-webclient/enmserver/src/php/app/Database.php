<?php

namespace wenom;

use \PDO as PDO;
use \PDOException as PDOException;

/**
 * Diese Klasse dient dem Zugriff auf die SQLite-Datenbank aus der Konfiguration.
 */
class Database {

    // Die Konfiguration der Anwendung
    public $config;

    // Die Datenbank-Verbindung
    public $conn;

    // Die Revision des Datenbank-Schema. Muss bei jeder strukturellen Änderung um eins erhöht werden.
    public const SCHEMA_REVISION = 3;

    /**
     * Definiert das Soll-Schema: Tabelle => Spalten-Definitionen
     */
    private const TABLES = [
        'SchemaInfo' => [
            'Revision' => 'INTEGER PRIMARY KEY'
        ],
        'LoginFehlversuche' => [
            'ip' => 'TEXT',
            'idLehrer' => 'INTEGER',
            'zeitpunkt' => 'INTEGER',
            'PRIMARY KEY(ip, idLehrer, zeitpunkt)'
        ],
        'OAuth' => [
            'clientID' => 'INTEGER PRIMARY KEY',
            'token' => 'TEXT',
            'tokenTimestamp' => 'INTEGER',
            'tokenValidForSecs' => 'INTEGER'
        ],
        'ServerConfig' => [
            'schluessel' => 'TEXT PRIMARY KEY',
            'wert' => 'TEXT'
        ],
        'ClientConfig' => [
            'schluessel' => 'TEXT PRIMARY KEY',
            'wert' => 'TEXT'
        ],
        'ClientLehrerConfig' => [
            'idLehrer' => 'INTEGER',
            'schluessel' => 'TEXT',
            'wert' => 'TEXT',
            'PRIMARY KEY(idLehrer, schluessel)'
        ],
        'Daten' => [
            'ts' => 'INTEGER PRIMARY KEY',
            'schulnummer' => 'INTEGER',
            'daten' => 'TEXT'
        ],
        'Schueler' => [
            'id' => 'INTEGER',
            'ts' => 'INTEGER',
            'idJahrgang' => 'INTEGER',
            'idKlasse' => 'INTEGER',
            'daten' => 'TEXT',
            'tsFehlstundenGesamt' => 'TEXT',
            'tsFehlstundenGesamtUnentschuldigt' => 'TEXT',
            'tsASV' => 'TEXT',
            'tsAUE' => 'TEXT',
            'tsZB' => 'TEXT',
            'tsLELS' => 'TEXT',
            'tsSchulformEmpf' => 'TEXT',
            'tsIndividuelleVersetzungsbemerkungen' => 'TEXT',
            'tsFoerderbemerkungen' => 'TEXT',
            'PRIMARY KEY(id, ts)'
        ],
        'Leistungsdaten' => [
            'id' => 'INTEGER',
            'ts' => 'INTEGER',
            'idSchueler' => 'INTEGER',
            'idLerngruppe' => 'INTEGER',
            'daten' => 'TEXT',
            'tsNote' => 'TEXT',
            'tsNoteQuartal' => 'TEXT',
            'tsFehlstundenFach' => 'TEXT',
            'tsFehlstundenUnentschuldigtFach' => 'TEXT',
            'tsFachbezogeneBemerkungen' => 'TEXT',
            'tsIstGemahnt' => 'TEXT',
            'PRIMARY KEY(id, ts)'
        ],
        'Teilleistungen' => [
            'id' => 'INTEGER',
            'ts' => 'INTEGER',
            'idLeistung' => 'INTEGER',
            'daten' => 'TEXT',
            'tsArtID' => 'TEXT',
            'tsDatum' => 'TEXT',
            'tsBemerkung' => 'TEXT',
            'tsNote' => 'TEXT',
            'PRIMARY KEY(id, ts)'
        ],
        'ZP10' => [
            'id' => 'INTEGER',
            'ts' => 'INTEGER',
            'idSchueler' => 'INTEGER',
            'idLehrer' => 'INTEGER',
            'daten' => 'TEXT',
            'tsVornote' => 'TEXT',
            'tsNoteSchriftlichePruefung' => 'TEXT',
            'tsMuendlichePruefung' => 'TEXT',
            'tsMuendlichePruefungFreiwillig' => 'TEXT',
            'tsNoteMuendlichePruefung' => 'TEXT',
            'tsAbschlussnote' => 'TEXT',
            'PRIMARY KEY(id, ts)'
        ],
        'Ankreuzkompetenzen' => [
            'id' => 'INTEGER',
            'ts' => 'INTEGER',
            'idSchueler' => 'INTEGER',
            'idKompetenz' => 'INTEGER',
            'daten' => 'TEXT',
            'tsStufe' => 'TEXT',
            'PRIMARY KEY(id, ts)'
        ],
        'Sprachenfolge' => [
            'id' => 'INTEGER',
            'sprache' => 'TEXT',
            'ts' => 'INTEGER',
            'idSchueler' => 'INTEGER',
            'daten' => 'TEXT',
            'PRIMARY KEY(id, sprache, ts)'
        ],
        'Lehrer' => [
            'id' => 'INTEGER',
            'ts' => 'INTEGER',
            'daten' => 'TEXT',
            'eMailDienstlich' => 'TEXT',
            'passwordHash' => 'TEXT',
            'tsPasswordHash' => 'TEXT',
            'art2FA' => 'INTEGER DEFAULT 0',
            'tsArt2FA' => 'TEXT',
            'totpSecret' => 'TEXT',
            'istErstanmeldung' => 'INTEGER DEFAULT 1',
            'tsIstErstanmeldung' => 'TEXT',
            'tokenVersion' => 'INTEGER NOT NULL DEFAULT 1',
            'PRIMARY KEY(id, ts)'
        ],
        'Lehrertoken' => [
            'idLehrer' => 'INTEGER PRIMARY KEY',
            'token' => 'TEXT',
            'tokenTimestamp' => 'INTEGER',
            'tokenValidForSecs' => 'INTEGER'
        ],
    ];

    /**
     * Erstellt eine neue Verbindung zu der SQLite-Datenbank, welche in der übergebenen Konfiguration
     * angegeben ist. Existiert diese Datenbank noch nicht, so wird sie mit Default-Werten initialisiert.
     */
    public function __construct(Config $config) {
        $this->config = $config;
        $this->conn = new DBConnection($config->getDatabasePath(), $config->getDatabaseFilename());

        $this->checkAndRepair();
    }


    /**
     * Prüft, ob die Datenbank eine gültige Revision hat. Ist dies nicht der Fall, so wird eine Reperatur
     * anhand der Schema-Defintion gestartet.
     */
    protected function checkAndRepair(): void {
        // Stelle sicher, dass die Tabelle SchemaInfo immer vorhanden ist
        $this->conn->createTable('SchemaInfo', 'CREATE TABLE IF NOT EXISTS SchemaInfo(Revision INTEGER PRIMARY KEY)');

        // Prüfe die Revision des Datenbank-Schemas
        $res = $this->conn->querySingleOrNull("SELECT Revision FROM SchemaInfo");
        $rev = $res === null ? 0 : (int)$res->Revision;

        // Eine Prüfung findet statt, wenn die gefundene Revision von der erwarteten abweicht
        if ($rev !== self::SCHEMA_REVISION) {
            foreach (self::TABLES as $tableName => $columns) {
                $this->repairTable($tableName, $columns);
            }

            // Stelle sicher, dass die Tabellen auch mit initialen Daten befüllt sind.
            $this->setInitialData();

            // Aktualisiere die Schema-Revision
            $this->conn->clearTable('SchemaInfo');
            $this->conn->insertInto('SchemaInfo', "INSERT INTO SchemaInfo(Revision) VALUES (" . self::SCHEMA_REVISION . ")");
        }
    }


    /**
     * Repariert eine Datenbank-Tabelle mit dem übergebenen Namen, sofern sie fehlerhaft ist mit den übergebenen
     * Spalteninformationen.
     *
     * @param string $tableName        der Tabellenname
     * @param array $expectedColumns   die erwarteten Spalten, ggf. mit einer zusätzlichen Primärschlüsseldefinition
     */
    private function repairTable(string $tableName, array $expectedColumns): void {
        // Erstelle die Tabelle, wenn sich noch nicht existiert
        $colDefs = [];
        foreach ($expectedColumns as $name => $def) {
            if (is_string($name)) {
                // normale Spaltendefinition
                $colDefs[] = "$name $def";
            } else {
                // Primärschlüsseldefintion am Ende berücksichtigen
                $colDefs[] = $def;
            }
        }
        $sql = "CREATE TABLE IF NOT EXISTS $tableName (" . implode(', ', $colDefs) . ")";
        $this->conn->createTable($tableName, $sql);

        // Prüfe die Spalten einzeln und füge sie ggf. hinzu - ein entfernen findet nicht statt...
        $existingColumns = $this->conn->queryAllOrNull("PRAGMA table_info($tableName)");
        $existingNames = array_map(fn($c) => $c->name, $existingColumns ?? []);
        foreach ($expectedColumns as $colName => $colDef) {
            // Überspringe ggf. die zusammengesetzte Primärschlüssel-Definitionen (keine Spalten)
            if (!is_string($colName)) {
                continue;
            }
            // Füge bei Bedarf die Spalten hinzu
            if (!in_array($colName, $existingNames)) {
                $this->conn->execUpdate("ALTER TABLE $tableName ADD COLUMN $colName $colDef");
            }
        }
    }

    /**
     * Befüllt die Datenbank mit den initialen Daten für ein schema.
     */
    private function setInitialData(): void {
        // Prüfe, ob der OAuth-Eintrag für clientID 1 existiert
        $oauthEntry = $this->conn->querySingleOrNull("SELECT clientID FROM OAuth WHERE clientID = 1");
        if ($oauthEntry === null) {
            $this->conn->insertInto('OAuth', "INSERT INTO OAuth(clientID, token, tokenTimestamp, tokenValidForSecs) VALUES (1, NULL, NULL, NULL)");
        }
    }

    /**
     * Reinitialisiert die Datenbank, indem das Client-Secret neu gesetzt wird und die vorhanden ENM-Daten gelöscht werden.
     */
    public function reinitDatabase(): void {
        ImportManager::clearENMDaten($this->conn);
        $this->conn->clearTable('ServerConfig');
        $this->conn->clearTable('ClientConfig');
        $this->conn->clearTable('ClientLehrerConfig');
        $this->conn->clearTable('OAuth');
        $this->conn->insertInto('OAuth', "INSERT INTO OAuth(clientID, token, tokenTimestamp, tokenValidForSecs) VALUES (1, NULL, NULL, NULL)");
    }

    /**
     * Erstellt ein neues Token und gibt dieses zurück. Ein zuvor bestendes Token wird dabei ersetzt.
     *
     * @param int $id   die ID für welche der Access-Token generiert werden soll
     *
     * @return object die Informationen zum Token oder null, wenn das Erstellen nicht erfolgreich war
     */
    public function createClientAccessToken(int $id): object | null {
        $token = Config::generateRandomSecret();
        $time = TimeUtils::timestamp();
        $validFor = 3600; // eine Stunde (in Sekunden)
        if (!$this->conn->execUpdate("UPDATE OAuth SET token='$token', tokenTimestamp=$time, tokenValidForSecs=$validFor WHERE clientID = $id")) {
            return null;
        }
        return (object)[
            'token_type' => 'Bearer',
            'access_token' => $token,
            'expires_in' => $validFor,
        ];
    }

    /**
     * Bestimmt den Client-Eintrag anhand des übergebenen Access-Tokens und gibt diesen zurück.
     *
     * @param string $token   das Token anhand welchem der Client-Eintrag ermittelt werden soll
     *
     * @return object der Client-Eintrag oder null, falls keiner gefunden wird
     */
    public function getClientByAccessToken(string | null $token): object | null {
        if ($token == null) {
            return null;
        }
        try {
            // Verwende $token nicht direkt, um SQL-Injection zu verhindern
            $rows = $this->conn->queryAllOrNull("SELECT clientID, token, tokenTimestamp, tokenValidForSecs FROM OAuth");
            if ($rows !== null) {
                foreach ($rows as $row) {
                    if (($row->token != null) && (strcmp($row->token, $token) == 0)) {
                        return $row;
                    }
                }
            }
        } catch (PDOException $e) {
            // do nothing
        }
        return null;
    }

    /**
     * Lädt die SMTP-Konfiguration aus der Datenbank und initialisiert den SMTP-Client damit.
     *
     * @return ?SMTPClient   der SMTP-Client oder null, wenn keine Konfiguration vorliegt
     */
    public function getSMTPClient(): ?SMTPClient {
        $result = $this->conn->queryAllOrNull("SELECT wert AS value FROM ServerConfig WHERE schluessel='smtp'");
        if (($result === null) || (count($result) !== 1)) {
            return null;
        }
        $json = $result[0]->value;
        $client = new SMTPClient($json);
        // Prüfe noch, ob der Client eine vollständige und plausible Konfiguration hat
        return $client->isValid() ? $client : null;
    }


    /**
     * Lädt die Konfiguration für die Sperrung der Noteneingabe aus der Datenbank.
     *
     * @param DBConnection $conn   die Datenbank-Verbindung
     *
     * @return array   die Konfiguration der einzelnen Klassen als Map von der ID auf den Konfigurationseintrag
     */
    public static function getConfigSperrungNoteneingabe(DBConnection $conn): array {
        $result = $conn->queryAllOrNull("SELECT wert AS value FROM ClientConfig WHERE schluessel='noteneingabe.gesperrt'");
        if (($result === null) || (count($result) !== 1)) {
            return [];
        }
        $json = $result[0]->value;
        $list = json_decode($json);
        $map = [];
        foreach ($list as $entry) {
            $map[$entry->id] = $entry;
        }
        return $map;
    }


    /**
     * Ermittelt die globale Konfiguration und die benutzerspezifische Konfiguration anhand der
     * übergebenen Lehrer-ID und gibt diese als JSON-String zurück.
     *
     * @param DBConnection $conn   die Datenbank-Verbindung
     * @param int $idLehrer   die ID des Lehrers, dessen benutzerspezifische Konfiguration ermittelt wird
     *
     * @return string ein JSON mit der globalen und der benutzerspezifischen Konfiguration
     */
    public static function getClientConfig(DBConnection $conn, int $idLehrer): string {
        $configBenutzer = $conn->queryAllOrExit500("SELECT schluessel AS key, wert AS value FROM ClientLehrerConfig WHERE idLehrer=$idLehrer", "Fehler beim Lesen der benutzerspezifischen Konfigurationsdaten");
        $configGlobal = $conn->queryAllOrExit500("SELECT schluessel AS key, wert AS value FROM ClientConfig", "Fehler beim Lesen der globalen Konfigurationsdaten");
        $jsonBenutzer = json_encode($configBenutzer, JSON_UNESCAPED_SLASHES);
        $jsonGlobal = json_encode($configGlobal, JSON_UNESCAPED_SLASHES);
        return "{ \"user\": $jsonBenutzer, \"global\": $jsonGlobal }";
    }

    /**
     * Ermittelt die Konfiguration des Server, d.h. die Server-sepzifische Konfiguration und die globale Konfiguration für
     * den Client und gibt diese als JSON-String zurück.
     *
     * @param DBConnection $conn   die Datenbank-Verbindung
     *
     * @return string ein JSON mit den beiden Konfigurationen
     */
    public static function getServerConfig(DBConnection $conn): string {
        $configServer = $conn->queryAllOrExit500("SELECT schluessel AS key, wert AS value FROM ServerConfig", "Fehler beim Lesen der Server-spezifischen Konfigurationsdaten");
        $configGlobal = $conn->queryAllOrExit500("SELECT schluessel AS key, wert AS value FROM ClientConfig", "Fehler beim Lesen der globalen Konfigurationsdaten");
        $jsonServer = json_encode($configServer, JSON_UNESCAPED_SLASHES);
        $jsonGlobal = json_encode($configGlobal, JSON_UNESCAPED_SLASHES);
        return "{ \"server\": $jsonServer, \"global\": $jsonGlobal }";
    }

    /**
     * Setzt einen Eintrag in der globalen Konfiguration.
     *
     * @param bool $nurServer   gibt an, ob der Konfigurationseintrag nur den Server betrifft oder global ist.
     * @param string $key       der zu setzende Schlüssel
     * @param string $value     der zu setzende Wert für den Schlüssel
     */
    public function putConfig(bool $nurServer, string $key, string | null $value): void {
        $table = $nurServer ? "ServerConfig" : "ClientConfig";
        // Wenn der Wert null ist und ein Eintrag vorliegt, dann muss dieser ggf. entfernt werden
        if ($value === null) {
            $stmt = $this->conn->prepareStatement("DELETE FROM $table WHERE schluessel = :schluessel");
            $this->conn->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
            $this->conn->executeStatement($stmt);
            return;
        }
        // Schreibe den Eintrag
        $stmt = $this->conn->prepareStatement("REPLACE INTO $table(schluessel, wert) VALUES (:schluessel, :wert)");
        $this->conn->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":wert", $value, PDO::PARAM_STR);
        $this->conn->executeStatement($stmt);
    }

    /**
     * Setzt einen Eintrag in der benutzerspezifischen Konfiguration anhand der übergebenen Lehrer-ID.
     *
     * @param int $idLehrer   die ID des Lehrers, dessen benutzerspezifische Konfiguration angepasst wird
     * @param string $key     der zu setzende Schlüssel
     * @param string $value   der zu setzende Wert für den Schlüssel
     */
    public function putClientUserConfig(int $idLehrer, string $key, string | null $value): void {
        // Wenn der Wert null ist und ein Eintrag vorliegt, dann muss dieser ggf. entfernt werden
        if ($value === null) {
            $stmt = $this->conn->prepareStatement("DELETE FROM ClientLehrerConfig WHERE idLehrer = :idLehrer AND schluessel = :schluessel");
            $this->conn->bindStatementValue($stmt, ":idLehrer", $idLehrer, PDO::PARAM_INT);
            $this->conn->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
            $this->conn->executeStatement($stmt);
            return;
        }
        // Schreibe den Eintrag
        $stmt = $this->conn->prepareStatement("REPLACE INTO ClientLehrerConfig(idLehrer, schluessel, wert) VALUES (:idLehrer, :schluessel, :wert)");
        $this->conn->bindStatementValue($stmt, ":idLehrer", $idLehrer, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":wert", $value, PDO::PARAM_STR);
        $this->conn->executeStatement($stmt);
    }

    /**
     * Ermittelt den Zeitstempel und die grundlegenden ENM-Daten aus der Datenbank und gibt
     * diese in einem Array zurück.
     *
     * @return object der Zeitstempel und die ENM-Daten als JSON-Objekt
     */
    public function getJsonENMDaten(): object {
        $results = $this->conn->queryAllOrExit500("SELECT ts, daten FROM Daten", "Fehler Lesen der ENM-Daten");
        if (empty($results)) {
            Http::exit404NotFound("Keine zu exportierenden Daten in der Datenbank vorhanden.");
        }
        if (count($results) > 1) {
            Http::exit500("Zu viele Einträge für ENM-Daten in den Datenbank vorhanden.");
        }
        return $results[0];
    }

    /**
     * Ermittelt die Lehrerdaten aus der Datenbank und gibt diese in einem Array zurück.
     *
     * @return array die Lehrer-Daten
     */
    public function getENMLehrerdaten(): array {
        $results = $this->conn->queryAllOrExit500("SELECT daten FROM Lehrer", "Fehler beim Lesen der Lehrer-Daten");
        $result = [];
        foreach ($results as $row) {
            $result[] = json_decode($row->daten);
        }
        return $result;
    }

    /**
     * Ermittelt die Lehrerdaten anhand der ID.
     *
     * @param int $id   die ID des Lehrers
     *
     * @return ?object die Lehrer-Daten
     */
    public function getENMLehrerByID(int $id): ?object {
        $result = $this->conn->querySingleOrNull("SELECT daten FROM Lehrer WHERE id = $id");
        if ($result === null) {
            return null;
        }
        return json_decode($result->daten);
    }


    /**
     * Ermittelt die Lehrerdaten aus der Datenbank und gibt diese in einem Array zurück.
     *
     * @return ?object die Lehrer-Daten
     */
    public function getENMLehrerByEmail(string $email): object | null {
        // Lese die Email-Adresse zur Vermeidung von SQL-Injection nicht direkt aus der DB
        $results = $this->conn->queryAllOrExit500("SELECT daten FROM Lehrer", "Fehler beim Lesen der Lehrer-Daten");
        foreach ($results as $row) {
            $tmp = json_decode($row->daten);
            if (($tmp->eMailDienstlich !== null) && (strcasecmp($tmp->eMailDienstlich, $email) === 0)) {
                return $tmp;
            }
        }
        return null;
    }

    /**
         * Ermittelt die Lehrerdaten aus der Datenbank und gibt bei einem gefundenen Datensatz 'true' zurück,
        * andernfalls bei null oder mehreren Datensätzen 'false'.
        *
        * @return bool true bei genau einem gefundenen Datensatz, false bei keinem oder mehreren
        */
    public function checkENMLehrerByEmail(string $email): bool {
        // Lese die Email-Adresse zur Vermeidung von SQL-Injection nicht direkt aus der DB
        $results = $this->conn->queryAllOrExit500("SELECT daten FROM Lehrer", "Fehler beim Lesen der Lehrer-Daten");
        $matchingCount = 0; // Zähler für passende Datensätze
        foreach ($results as $row) {
            $tmp = json_decode($row->daten);
            if (($tmp->eMailDienstlich !== null) && (strcasecmp($tmp->eMailDienstlich, $email) === 0)) {
                $matchingCount++;
            }
        }
        // Wenn genau ein Datensatz gefunden wurde, return true, sonst false
        return $matchingCount === 1;
    }

    /**
     * Ermittelt die Schülerdaten aus der Datenbank und gibt diese in einem Array zurück.
     *
     * @return array die Schüler-Daten
     */
    public function getENMSchuelerdaten(): array {
        $results = $this->conn->queryAllOrExit500("SELECT daten FROM Schueler", "Fehler beim Lesen der Schüler-Daten");
        $result = [];
        $mapSchueler = [];
        foreach ($results as $row) {
            $schueler = json_decode($row->daten);
            $result[] = $schueler;
            $mapSchueler[$schueler->id] = $schueler;
        }
        // Integration der Leistungsdaten...
        $results = $this->conn->queryAllOrNull("SELECT idSchueler, daten FROM Leistungsdaten");
        if ($results != null) {
            $mapLeistung = [];
            foreach ($results as $row) {
                $schueler = $mapSchueler[$row->idSchueler];
                $leistung = json_decode($row->daten);
                $schueler->leistungsdaten[] = $leistung;
                $mapLeistung[$leistung->id] = $leistung;
            }
            // ... und deren Teilleistungen
            $results = $this->conn->queryAllOrNull("SELECT idLeistung, daten FROM Teilleistungen");
            if ($results != null) {
                foreach ($results as $row) {
                    $leistung = $mapLeistung[$row->idLeistung];
                    $leistung->teilleistungen[] = json_decode($row->daten);
                }
            }
        }
        // Integration der Ankreuzkompetenzen
        $results = $this->conn->queryAllOrNull("SELECT idSchueler, daten FROM Ankreuzkompetenzen");
        if ($results != null) {
            foreach ($results as $row) {
                $schueler = $mapSchueler[$row->idSchueler];
                $schueler->ankreuzkompetenzen[] = json_decode($row->daten);
            }
        }
        // Integration der ZP10-Daten
        $results = $this->conn->queryAllOrNull("SELECT idSchueler, daten FROM ZP10");
        if ($results != null) {
            foreach ($results as $row) {
                $schueler = $mapSchueler[$row->idSchueler];
                $schueler->zp10[] = json_decode($row->daten);
            }
        }
        // Integration der Sprachenfolge
        $results = $this->conn->queryAllOrNull("SELECT idSchueler, daten FROM Sprachenfolge");
        if ($results != null) {
            foreach ($results as $row) {
                $schueler = $mapSchueler[$row->idSchueler];
                $schueler->sprachenfolge[] = json_decode($row->daten);
            }
        }
        return $result;
    }


    /**
     * Fragt die aktuell verwendete Token-Version für den Lehrer mit der übergebenen ID ab.
     *
     * @param int $lehrerId   Die ID des Lehrers
     *
     * @return int die Token-Version oder -1 im Fehlerfall
     */
    public function getENMLehrerCurrentTokenVersion(int $lehrerId): int {
        try {
            $stmt = $this->conn->prepareStatement("SELECT tokenVersion FROM Lehrer WHERE id = :idLehrer");
            $this->conn->bindStatementValue($stmt, ":idLehrer", $lehrerId, PDO::PARAM_INT);
            $this->conn->executeStatement($stmt);
            $result = $stmt->fetchColumn();
            return ($result !== false) ? (int)$result : -1;
        } catch (PDOException $e) {
            return -1;
        }
    }


    /**
     * Prüfe die aktuelle Token-Version auf Übereinstimmung mit der erwarteten Version (Funktionsparameter)
     * und gibt die Version zurück.
     *
     * @param int $lehrerId       die ID des Lehrers
     * @param int $tokenVersion   die zu prüfende Token-Version
     *
     * @return int | false   die Token-Version oder false im Fehlerfall
     */
    public function checkLehrerCurrentTokenVersion(int $lehrerId, int $tokenVersion): int | false {
        try {
            $curTokenVersion = $this->getENMLehrerCurrentTokenVersion($lehrerId);
            if ($curTokenVersion !== $tokenVersion) {
                $this->conn->rollbackTransaction();
                return false;
            }
            return $curTokenVersion;
        } catch (PDOException $e) {
            return false;
        }
    }


    /**
     * Erhöht die Token-Version für den Lehrer um eins, sofern der übergebene Wert mit dem Datenbank-Wert übereinstimmt
     * und gibt die neue Version zurück.
     *
     * @param int $lehrerId       die ID des Lehrers
     * @param int $tokenVersion   die zu prüfende Token-Version oder null, falls die Prüfung ausgelassen werden soll
     *
     * @return int | false   die neue Token-Version oder false im Fehlerfall
     */
    public function checkAndIncrementLehrerCurrentTokenVersion(int $lehrerId, int | null $tokenVersion): int | false {
        try {
            $this->conn->beginTransaction();

            // Prüfe die aktuelle Token-Version auf Übereinstimmung mit der erwarteten Version (Funktionsparameter)
            $curTokenVersion = ($tokenVersion === null)
                ? $this->getENMLehrerCurrentTokenVersion($lehrerId)
                : $this->checkLehrerCurrentTokenVersion($lehrerId, $tokenVersion);

            // Prüfe, ob die Token-Version bestimmt werden konnte. Wenn nicht, dann brich die Transaktion ab.
            if ($curTokenVersion === false) {
                $this->conn->rollbackTransaction();
                return false;
            }

            // Erhöhe die Token-Version in der Datenbank, so dass in der Folge darauf zugegriffen werden kann
            $newTokenVersion = $curTokenVersion + 1;
            $stmt = $this->conn->prepareStatement("UPDATE Lehrer SET tokenVersion = :tokenVersion WHERE id = :idLehrer");
            $this->conn->bindStatementValue($stmt, ":tokenVersion", $newTokenVersion, PDO::PARAM_INT);
            $this->conn->bindStatementValue($stmt, ":idLehrer", $lehrerId, PDO::PARAM_INT);
            $this->conn->executeStatement($stmt);
            
            $this->conn->commitTransaction();
            return $newTokenVersion;
        } catch (PDOException $e) {
            $this->conn->rollbackTransaction();
            return false;
        }
    }


    /**
     * Erstellt ein neues Password-Token. Ein zuvor bestendes Password-Token wird dabei ersetzt.
     *
     * @param int $lehrerId   Die ID des Lehrers
     * @return string $token  Das generierte und gespeicherte Password-Token
     */
    public function writeENMLehrerToken(int $lehrerId): string {
        $token = Config::generateRandomSecret();
        $time = TimeUtils::timestamp();
        $validFor = 600;
        $this->conn->beginTransaction();

        // Neuen Token speichern
        $stmt = $this->conn->prepareStatement("REPLACE INTO Lehrertoken (idLehrer, token, tokenTimestamp, tokenValidForSecs) VALUES (:id, :token, :ts, :valid)");
        $this->conn->bindStatementValue($stmt, ":id", $lehrerId, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":token", $token, PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":ts", $time, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":valid", $validFor, PDO::PARAM_STR);
        $this->conn->executeStatement($stmt);

        $this->conn->commitTransaction();
        return $token;
    }

    /**
     * Überprüft, ob zu einer LehrerId ein gültiger Token vorliegt oder prüft, ob ein übergebener Token noch gültig ist.
     *
     * @param string|int $identifier   Kann die LehrerId oder ein Token sein
     * @return boolean                 Token gültig true; Token abgelaufen oder nicht vorhanden false
     */
    public function isENMLehrerTokenValid($identifier): bool {
        // Überprüfen, ob $identifier eine ID oder ein Token ist
        if (is_int($identifier)) {
            // Abfrage nach ID
            $stmt = $this->conn->prepareStatement("SELECT * FROM Lehrertoken WHERE idLehrer = :idLehrer");
            $this->conn->bindStatementValue($stmt, ":idLehrer", $identifier, PDO::PARAM_INT);
        } else {
            // Abfrage nach Token
            $stmt = $this->conn->prepareStatement("SELECT * FROM Lehrertoken WHERE token = :token");
            $this->conn->bindStatementValue($stmt, ":token", $identifier, PDO::PARAM_STR);
        }
        $this->conn->executeStatement($stmt);
        $result = $stmt->fetchAll(PDO::FETCH_OBJ);

        // Prüfen, ob ein Ergebnis vorliegt
        if (empty($result)) {
            return false;
        }

        // Das erste und einzige Ergebnis
        $tokenObj = $result[0];

        // Überprüfe, ob ein Token existiert und ob es noch gültig ist
        if (isset($tokenObj->token)) {
            $tokenTimestamp = $tokenObj->tokenTimestamp; // Zeitstempel des Tokens
            $tokenValidForSecs = $tokenObj->tokenValidForSecs; // Gültigkeitsdauer in Sekunden
            // Berechne, ob das Token noch gültig ist
            $tokenExpiryTime = $tokenTimestamp + $tokenValidForSecs; // Ablaufzeit des Tokens
            $currentTime = TimeUtils::timestamp(); // Die aktuelle Zeit in UTC
            if ($currentTime < $tokenExpiryTime) {
                return true;
            }
        }
        return false;
    }

    /**
     * Löscht das Password-Token eines Lehrers aus der Datenbank.
     *
     * @param int $lehrerId   Die ID des Lehrers
     */
    public function deleteENMLehrerToken(int $lehrerId): void {
        $this->conn->beginTransaction();

        // Token für den Lehrer löschen
        $stmt = $this->conn->prepareStatement("DELETE FROM Lehrertoken WHERE idLehrer = :idLehrer");
        $this->conn->bindStatementValue($stmt, ":idLehrer", $lehrerId, PDO::PARAM_INT);
        $this->conn->executeStatement($stmt);

        $this->conn->commitTransaction();
    }

    /**
     * Prüft, ob der Login für die übergebenen IP-Adresse und die ID des Lehrers aktuell gesperrt ist, um Brute-Force-Angriffe
     * zu vermeiden.
     *
     * @param string $ip      die IP-Adresse
     * @param int $idLehrer   die ID des Lehrers
     *
     * @return boolean true, falls der Login für die IP und den Lehrer aktuell gesperrt ist
     */
    public function istLoginGesperrt(string $ip, int $idLehrer): bool {
        // Prüfe zunächst die Login-Versuche eines Lehrers auf einer IP-Adresse (maximal 3 Versuche in 5 Minuten)
        $maxTries = 3; // maximal 3 Versuche
        $limit = TimeUtils::timestamp() - 300; // pro 5 Minuten (300 Sekunden)
        $stmt = $this->conn->prepareStatement("SELECT COUNT(*) as versuche FROM LoginFehlversuche WHERE (ip = :ip AND idLehrer = :idLehrer) AND zeitpunkt > :limit");
        $this->conn->bindStatementValue($stmt, ":ip", $ip, PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":idLehrer", $idLehrer, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":limit", $limit, PDO::PARAM_INT);
        $this->conn->executeStatement($stmt);
        $res = $stmt->fetch(PDO::FETCH_OBJ);
        if ((int)$res->versuche >= $maxTries) {
            return true;
        }

        // Prüfe, ob von einer IP-Adresse insgesamt zu viele Fehlversuche stammen
        $maxTries = 100; // maximal 100 Versuche
        $limit = TimeUtils::timestamp() - 300; // pro 5 Minuten (300 Sekunden)
        $stmt = $this->conn->prepareStatement("SELECT COUNT(*) as versuche FROM LoginFehlversuche WHERE ip = :ip AND zeitpunkt > :limit");
        $this->conn->bindStatementValue($stmt, ":ip", $ip, PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":limit", $limit, PDO::PARAM_INT);
        $this->conn->executeStatement($stmt);
        $res = $stmt->fetch(PDO::FETCH_OBJ);
        if ((int)$res->versuche >= $maxTries) {
            return true;
        }

        return false;
    }

    /**
     * Fügt einen neuen Eintrag zu den fehlgeschlagenen Login-Versuchen für die angebene IP und
     * den angebenen Lehrer hinzu.
     *
     * @param string $ip      die IP-Adresse
     * @param int $idLehrer   die ID des Lehrers
     */
    public function updateLoginFailures(string $ip, int $idLehrer): void {
        $time = TimeUtils::timestamp();
        $stmt = $this->conn->prepareStatement("INSERT OR IGNORE INTO LoginFehlversuche(ip, idLehrer, zeitpunkt) VALUES (:ip, :id, :zeit)");
        $this->conn->bindStatementValue($stmt, ":ip", $ip, PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":id", $idLehrer, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":zeit", $time, PDO::PARAM_INT);
        $this->conn->executeStatement($stmt);
    
        // Räume die Tabelle mit den Fehlversuche immer wieder gelegentlich auf... (nicht immer, da dies nicht performant ist)
        if (rand(1, 100) === 1) {
            $this->conn->execUpdate("DELETE FROM LoginFehlversuche WHERE zeitpunkt < " . ($time - 3600));
        }
    }

    /**
     * Leert die Tabelle mit den fehlgeschlagenen Login-Versuchen für die angebene IP und den angebenen Lehrer.
     *
     * @param string $ip      die IP-Adresse
     * @param int $idLehrer   die ID des Lehrers
     */
    public function clearLoginFailures(string $ip, int $idLehrer): void {
        $stmt = $this->conn->prepareStatement("DELETE FROM LoginFehlversuche WHERE ip = :ip AND idLehrer = :idLehrer");
        $this->conn->bindStatementValue($stmt, ":ip", $ip, PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":idLehrer", $idLehrer, PDO::PARAM_INT);
        $this->conn->executeStatement($stmt);
    }


    public function setLehrerErstanmeldungAbgeschlossen(int $idLehrer): void {
        $lehrer = $this->getENMLehrerByID($idLehrer);
        if ($lehrer === null) {
            Http::exit500("Fehler beim Zugriff auf die Lehrer-Daten");
        }
        $lehrer->istErstanmeldung = false;
        $lehrer->tsIstErstanmeldung = TimeUtils::now();
        $updatedLehrer = json_encode($lehrer, JSON_UNESCAPED_SLASHES);
        $this->conn->beginTransaction();
        $stmt = $this->conn->prepareStatement("UPDATE Lehrer SET istErstanmeldung=:istErstanmeldung, tsIstErstanmeldung='$lehrer->tsIstErstanmeldung', daten=:daten WHERE id=:id");
        $this->conn->bindStatementValue($stmt, ":id", $idLehrer, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":istErstanmeldung", $lehrer->istErstanmeldung, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":daten", $updatedLehrer, PDO::PARAM_STR);
        $this->conn->executeStatement($stmt);
        $this->conn->commitTransaction();
    }


    public function setLehrerKennwort(int $idLehrer, string $newPassword): void {
        $lehrer = $this->getENMLehrerByID($idLehrer);
        if ($lehrer === null) {
            Http::exit404NotFound("Die Lehrer-Daten wurden nicht gefunden.");
        }
        $hash = password_hash($newPassword, PASSWORD_DEFAULT);
        $lehrer->passwordHash = str_replace('$2y$', '$2a$', $hash);
        $lehrer->istInitialPassword = false;
        $lehrer->tsPasswordHash = TimeUtils::now();
        $updatedLehrer = json_encode($lehrer, JSON_UNESCAPED_SLASHES);
        $this->conn->beginTransaction();
        $stmt = $this->conn->prepareStatement("UPDATE Lehrer SET passwordHash=:passwordHash, tsPasswordHash='$lehrer->tsPasswordHash', daten=:daten WHERE id=:id");
        $this->conn->bindStatementValue($stmt, ":id", $idLehrer, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":passwordHash", $lehrer->passwordHash, PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":daten", $updatedLehrer, PDO::PARAM_STR);
        $this->conn->executeStatement($stmt);
        $this->conn->commitTransaction();
    }

}
