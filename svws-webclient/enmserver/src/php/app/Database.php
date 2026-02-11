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

    /**
     * Erstellt eine neue Verbindung zu der SQLite-Datenbank, welche in der übergebenen Konfiguration
     * angegeben ist. Existiert diese Datenbank noch nicht, so wird sie mit Default-Werten initialisiert.
     */
    public function __construct(Config $config) {
        $this->config = $config;
        // Prüfe, ob die Datenbank bereits existiert. Wenn nicht, dann lege eine neue mit Default-Werten an
        $dbPath = $config->getAppRoot()."/".$config->getDatabaseFile();
        $dbNeedsInitialization = file_exists($dbPath);
        $this->conn = new DBConnection($config->getAppRoot(), $config->getDatabaseFile());
        if (!$dbNeedsInitialization) {
            $this->initDatabase();
        }
    }

    /**
     * Initialisiert die Datenbank mit Default-Werten
     */
    protected function initDatabase(): void {
        $this->conn->createTable('OAuth', 'CREATE TABLE OAuth(clientID INTEGER PRIMARY KEY, token TEXT, tokenTimestamp INTEGER, tokenValidForSecs INTEGER)');
        $this->conn->insertInto('OAuth', "INSERT INTO OAuth(clientID, token, tokenTimestamp, tokenValidForSecs) VALUES (1, NULL, NULL, NULL)");
        $this->conn->createTable('ServerConfig', 'CREATE TABLE ServerConfig(schluessel TEXT PRIMARY KEY, wert TEXT)');
        $this->conn->createTable('ClientConfig', 'CREATE TABLE ClientConfig(schluessel TEXT PRIMARY KEY, wert TEXT)');
        $this->conn->createTable('ClientLehrerConfig', 'CREATE TABLE ClientLehrerConfig(idLehrer INTEGER, schluessel TEXT, wert TEXT, PRIMARY KEY (idLehrer, schluessel))');
        $this->conn->createTable('Daten', 'CREATE TABLE Daten(ts INTEGER PRIMARY KEY, schulnummer INTEGER, daten TEXT)');
        $this->conn->createTable('Schueler', 'CREATE TABLE Schueler(id INTEGER, ts INTEGER, idJahrgang INTEGER, idKlasse INTEGER, daten TEXT, tsFehlstundenGesamt TEXT, tsFehlstundenGesamtUnentschuldigt TEXT, tsASV TEXT, tsAUE TEXT, tsZB TEXT, tsLELS TEXT, tsSchulformEmpf TEXT, tsIndividuelleVersetzungsbemerkungen TEXT, tsFoerderbemerkungen TEXT, PRIMARY KEY(id, ts))');
        $this->conn->createTable('Leistungsdaten', 'CREATE TABLE Leistungsdaten(id INTEGER, ts INTEGER, idSchueler INTEGER, idLerngruppe INTEGER, daten TEXT, tsNote TEXT, tsNoteQuartal TEXT, tsFehlstundenFach TEXT, tsFehlstundenUnentschuldigtFach TEXT, tsFachbezogeneBemerkungen TEXT, tsIstGemahnt TEXT, PRIMARY KEY(id, ts))');
        $this->conn->createTable('Teilleistungen', 'CREATE TABLE Teilleistungen(id INTEGER, ts INTEGER, idLeistung INTEGER, daten TEXT, tsArtID TEXT, tsDatum TEXT, tsBemerkung TEXT, tsNote TEXT, PRIMARY KEY(id, ts))');
        $this->conn->createTable('Ankreuzkompetenzen', 'CREATE TABLE Ankreuzkompetenzen(id INTEGER, ts INTEGER, idSchueler INTEGER, idKompetenz INTEGER, daten TEXT, tsStufe TEXT, PRIMARY KEY(id, ts))');
        $this->conn->createTable('Sprachenfolge', 'CREATE TABLE Sprachenfolge(id INTEGER, sprache TEXT, ts INTEGER, idSchueler INTEGER, daten TEXT, PRIMARY KEY (id, sprache, ts))');
        $this->conn->createTable('Lehrer', 'CREATE TABLE Lehrer(id INTEGER, ts INTEGER, daten TEXT, eMailDienstlich TEXT, passwordHash TEXT, tsPasswordHash TEXT, PRIMARY KEY(id, ts))');
        $this->conn->createTable('Lehrertoken', 'CREATE TABLE Lehrertoken(idLehrer INTEGER PRIMARY KEY, token TEXT, tokenTimestamp INTEGER, tokenValidForSecs INTEGER)');
    }

    /**
     * Reinitialisiert die Datenbank, indem das Client-Secret neu gesetzt wird und die vorhanden ENM-Daten gelöscht werden.
     */
    public function reinitDatbase(): void {
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
        $time = time();
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
        // Prüfe, ob bereits ein Eintrag vorliegt
        $stmt = $this->conn->prepareStatement("SELECT wert FROM $table WHERE schluessel = :schluessel");
        $this->conn->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
        $this->conn->executeStatement($stmt);
        $result = $stmt->fetchAll(PDO::FETCH_OBJ);
        $hatEintrag = (count($result) > 0);
        // Wenn der Wert null ist und kein Eintrag vorliegt, dann ist ein Einfügen nicht nötig
        if (!$hatEintrag && ($value === null)) {
            return;
        }
        // Wenn der Wert null ist und ein Eintrag vorliegt, dann muss dieser entfernt werden
        if ($hatEintrag && ($value === null)) {
            $stmt = $this->conn->prepareStatement("DELETE FROM $table WHERE schluessel = :schluessel");
            $this->conn->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
            $this->conn->executeStatement($stmt);
            return;
        }
        // Schreibe den Eintrag
        $this->conn->beginTransaction();
        $stmt = null;
        if ($hatEintrag) {
            // UPDATE...
            $stmt = $this->conn->prepareStatement("UPDATE $table SET wert=:wert WHERE schluessel = :schluessel");
        } else {
            // INSERT...
            $stmt = $this->conn->prepareStatement("INSERT INTO $table(schluessel, wert) VALUES (:schluessel, :wert)");
        }
        $this->conn->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":wert", $value, PDO::PARAM_STR);
        $this->conn->executeStatement($stmt);
        $this->conn->commitTransaction();
    }

    /**
     * Setzt einen Eintrag in der benutzerspezifischen Konfiguration anhand der übergebenen Lehrer-ID.
     *
     * @param int $idLehrer   die ID des Lehrers, dessen benutzerspezifische Konfiguration angepasst wird
     * @param string $key     der zu setzende Schlüssel
     * @param string $value   der zu setzende Wert für den Schlüssel
     */
    public function putClientUserConfig(int $idLehrer, string $key, string | null $value): void {
        // Prüfe, ob bereits ein Eintrag vorliegt
        $stmt = $this->conn->prepareStatement("SELECT wert FROM ClientLehrerConfig WHERE idLehrer = :idLehrer AND schluessel = :schluessel");
        $this->conn->bindStatementValue($stmt, ":idLehrer", $idLehrer, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
        $this->conn->executeStatement($stmt);
        $result = $stmt->fetchAll(PDO::FETCH_OBJ);
        $hatEintrag = (count($result) > 0);
        // Wenn der Wert null ist und kein Eintrag vorliegt, dann ist ein Einfügen nicht nötig
        if (!$hatEintrag && ($value === null)) {
            return;
        }
        // Wenn der Wert null ist und ein Eintrag vorliegt, dann muss dieser entfernt werden
        if ($hatEintrag && ($value === null)) {
            $stmt = $this->conn->prepareStatement("DELETE FROM ClientLehrerConfig WHERE idLehrer = :idLehrer AND schluessel = :schluessel");
            $this->conn->bindStatementValue($stmt, ":idLehrer", $idLehrer, PDO::PARAM_INT);
            $this->conn->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
            $this->conn->executeStatement($stmt);
            return;
        }
        // Schreibe den Eintrag
        $this->conn->beginTransaction();
        $stmt = null;
        if ($hatEintrag) {
            // UPDATE...
            $stmt = $this->conn->prepareStatement("UPDATE ClientLehrerConfig SET wert=:wert WHERE idLehrer = :idLehrer AND schluessel = :schluessel");
        } else {
            // INSERT...
            $stmt = $this->conn->prepareStatement("INSERT INTO ClientLehrerConfig(idLehrer, schluessel, wert) VALUES (:idLehrer, :schluessel, :wert)");
        }
        $this->conn->bindStatementValue($stmt, ":idLehrer", $idLehrer, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":wert", $value, PDO::PARAM_STR);
        $this->conn->executeStatement($stmt);
        $this->conn->commitTransaction();
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
     * Erstellt ein neues Password-Token. Ein zuvor bestendes Password-Token wird dabei ersetzt.
     *
     * @param int $lehrerId   Die ID des Lehrers
     * @return string $token  Das generierte und gespeicherte Password-Token
     */
    public function writeENMLehrerToken(int $lehrerId): string {
        $token = Config::generateRandomSecret();
        $time = time();
        $validFor = 600;
        $this->conn->beginTransaction();

        // Alten Token löschen
        $stmt = $this->conn->prepareStatement("DELETE FROM Lehrertoken WHERE idLehrer = :idLehrer");
        $this->conn->bindStatementValue($stmt, ":idLehrer", $lehrerId, PDO::PARAM_INT);
        $this->conn->executeStatement($stmt);

        // Neuen Token speichern
        $stmt = $this->conn->prepareStatement("INSERT INTO Lehrertoken (idLehrer, token, tokenTimestamp, tokenValidForSecs) VALUES (:id, :token, :ts, :valid)");
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
            $currentTime = time(); // Aktuelle Zeit
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

}
