<?php

namespace wenom;

/**
 * Diese Klasse stellt die Funktionalität für den Zugriff auf die Konfiguration des Servers zur Verfügung
 */
class Config {

    // Der Root-Pfad für die Applikation
    protected $appRoot = null;

    // Default Speicherort von Datenbank/Secret/Server-Mode
    protected static string $defaultDBFolder = 'db';

    // Speicherort von Datenbank/Secret/Server-Mode
    protected ?string $dbfolder = null;

    // Der Speicherort der SQLite-Datenbank
    protected static string $dbfile = "app.sqlite";

    // Der Dateiname, wo das Client-Secret für die Verbindung des SVWS-Servers zu dem ENM-Server gespeichert wird
    protected static string $secretfile = "client.sec";

    // Der Dateiname, wo die Information zum Server-Mode gesetzt werden kann. Ist diese Information nicht vorhanden, so wird 'stable' angenommen.
    protected static string $servermodefile = "server.mode";

    // Der Modus, in welchem der Server betrieben wird ('dev', 'alpha', 'beta', 'stable')
    protected string $serverMode = "stable";

    // Das Client-Secret, sobald es vom Konstruktor eingelesen (und ggf. erzeugt) wurde
    protected ?string $secret = null;

    // Gibt an, ob die Anwendung im Debug-Modus betrieben wird oder nicht
    protected bool $debugMode = false;

    // Die Lebensdauer für ein Access-Token einer Client-Verbindung (Default: 8 h)
    protected int $lifetimeAccessToken = 8 * 3600;

    // Die Lebensdauer für ein Access-Token einer Client-Verbindung (Default: 2 min)
    protected int $lifetimeTotpAccessToken = 120;

    // Die Größe des Zeitfensters bei der TOTP-Token-Prüfung in Sekunden (Default: 30 sec)
    protected int $totpTimeslice = 30;

    // Die Toleranz in Zeitfenstern bei der TOTP-Token-Prüfung (Default: 1)
    protected int $totpTolerance = 1;


    /**
     * Erstellt ein neues Konfigurationsobjekt, indem die übergebene JSON-Datei eingelesen
     * und überprüft wird.
     */
    public function __construct() {
        // Bestimme zunächst das Root-Verzeichnis der Anwendung
        $this->appRoot = Config::determineAppRoot();

        // ersetze dbfolder durch $_SERVER['ENM_DB_DIR'] sofern gesetzt.
        $this->dbfolder = $_SERVER['ENM_DB_DIR'] ?? Config::$defaultDBFolder;
        $dbDir = $this->appRoot."/".$this->dbfolder;
        if (!is_dir($dbDir) && !@mkdir($dbDir, 0755, true)) {
            Http::exit500("Konnte den Ordner $dbDir nicht automatisch erstellen. Bitte legen Sie diesen manuell an und vergeben Sie Schreibrechte.");
        }

        $testFile = $dbDir . '/.write_test';
        $isReallyWritable = is_writable($dbDir) || ((@file_put_contents($testFile, 'test') !== false) && @unlink($testFile));
        if (!$isReallyWritable) {
            Http::exit500("Der Ordner $dbDir ist nicht beschreibbar. Bitte vergeben Sie Schreibrechte.");
        }

        // Lese das Client-Secret ein. Wenn nich keines existiert, dann erzeuge es zuvor
        $secretfile = $dbDir."/".Config::$secretfile;
        if (!file_exists($secretfile)) {
            // Versuche eine neues Secret anzulegen anzulegen...
            $secret = Config::generateRandomSecret();
            $success = file_put_contents($secretfile, $secret);
            if ($success === false) {
                Http::exit500("Es konnte kein Client-Secret unter $secretfile generiert werden. Überprüfen Sie, ob die, beim Web-Server, konfigurierten Rechte ausreichend sind, um diese Datei anzulegen.");
            }
        }
        $this->secret = file_get_contents($secretfile);

        // Setze den Server-Mode, welcher auch an den Client weitergegeben wird
        $servermodefile = $dbDir."/".Config::$servermodefile;
        $serverMode = file_exists($servermodefile) ? file_get_contents($servermodefile) : 'stable';
        $serverMode = strtolower($serverMode);
        $validModes = ['stable', 'beta', 'alpha', 'dev'];
        if (!in_array($serverMode, $validModes)) {
            Http::exit500("Der konfigurierte Server-Mode ist ungültig. Überprüfen Sie, die Datei $servermodefile auf dem Web-Server");
        }
        $this->serverMode = $serverMode;

        // Initialisiere Debugging-Einstellung anhand des Server-Mode
        $this->debugMode = (strcmp($serverMode, 'stable') !== 0);
        if ($this->debugMode) {
            @ini_set('display_errors', '1');
            error_reporting(E_ALL);
        } else {
            @ini_set('display_errors', '0');
            @ini_set('log_errors', '0');
            error_reporting(E_ALL & ~E_NOTICE & ~E_DEPRECATED);
        }
    }

    /**
     * Bestimmt das Verzeichnis, in dem sich die Applikation befindet
     *
     * @return string der absolute Pfad, wo sich die Applikation befindet
     */
    protected static function determineAppRoot(): string {
        return dirname(__DIR__, 1);
    }

    /**
     * Prüft, ob die Anwendung bereits initialisiert wurde, in dem geprüft wird, ob
     * das Client-Secret und die SQLite-Datenbank beide vorliegen.
     */
    public static function isAppInitialized() : bool {
        $dbfolder = $_SERVER['ENM_DB_DIR'] ?? Config::$defaultDBFolder;
        $appRoot = self::determineAppRoot();
        $dbDir = $appRoot."/".$dbfolder;
        return file_exists($dbDir."/".Config::$secretfile) && file_exists($dbDir."/".Config::$dbfile);
    }

    /**
     * Gibt den Root-Pfad für die Applikation zurück
     *
     * @return string der root-Pfad
     */
    public function getAppRoot(): string {
        return $this->appRoot;
    }

    /**
     * Gibt den Speicherort der SQLite-Datenbank zurück
     *
     * @return string der Speicherort
     */
    public function getDatabasePath(): string {
        return $this->appRoot."/".$this->dbfolder;
    }

    /**
     * Gibt den Speicherort der SQLite-Datenbank zurück
     *
     * @return string der Speicherort
     */
    public function getDatabaseFilename(): string {
        return Config::$dbfile;
    }

    /**
     * Gibt den Modus zurück, in dem der Server betrieben wird.
     *
     * @return string der Modus 'stable', 'beta', 'alpha' oder 'dev'
     */
    public function getServerMode(): string {
        return $this->serverMode;
    }

    /**
     * Gibt das Client-Secret zurück.
     *
     * @return string das Client-Secret
     */
    public function getClientSecret(): string {
        return $this->secret;
    }


    /**
     * Erzeugt einen neuen, vom Client-Secret abgeleiteten, Schlüssel für Client-Sessions.
     * Dies ist eine zusätzliche Schutz-Maßnahme für das Client-Secret.
     */
    public function getClientSessionKey(): string {
        return hash_hmac('sha256', 'WeNoM-Client-Session', $this->getClientSecret());
    }


    /**
     * Erzeugt einen neuen, vom Client-Secret abgeleiteten, Schlüssel für Client-Login-TOTP-Sessions.
     * Dies ist eine zusätzliche Schutz-Maßnahme für das Client-Secret.
     */
    public function getClientTotpAuthSessionKey(): string {
        return hash_hmac('sha256', 'WeNoM-TOTP-Auth-Session', $this->getClientSecret());
    }


    /**
     * Gibt die Lebendsdauer für ein Access-Token für den Client-Zugriff eines vollständig angemeldeten Benutzers zurück.
     *
     * @return int die Lebensdauer
     */
    public function getLifetimeAccessToken(): int {
        return $this->lifetimeAccessToken;
    }


    /**
     * Gibt die Lebendsdauer für ein Access-Token für den Login-Vorgangs für einen bereits mit dem Kennwort angemeldeten
     * Benutzers zurück.
     *
     * @return int die Lebensdauer
     */
    public function getLifetimeTotpAccessToken(): int {
        return $this->lifetimeTotpAccessToken;
    }

    /**
     * Gibt die Größe des Zeitfensters bei der TOTP-Token-Prüfung in Sekunden zurück.
     *
     * @return int die Größe des Zeitfensters in Sekunden
     */
    public function getTotpTimeslice(): int {
        return $this->totpTimeslice;
    }

    /**
     * Gibt die Toleranz in Zeitfenstern bei der TOTP-Token-Prüfung zurück.
     *
     * @return int die Anzahl der Zeitfenster für die Toleranz
     */
    public function getTotpTolerance(): int {
        return $this->totpTolerance;
    }

    /**
     * Erzeugt einen zufälligen, URL-sicheren String, der für Kennwörter verwendet werden kann.
     *
     * @return string das neue Kennwort
     */
    public static function generateRandomSecret(): string {
        return rtrim(strtr(base64_encode(random_bytes(32)), '+/', '-_'), '=');
    }

    /**
     * Prüft, ob ein Passwort die folgenden Kriterien erfüllt:
     * - min. 16 Zeichen
     * - min. ein Großbuchstabe
     * - min. ein Kleinbuchstabe
     * - min. eine Zahl
     *
     * @param string $password Das zu prüfende Passwort
     * @return bool true, wenn das Passwort den Anforderungen entspricht, sonst false
     */
    public static function validatePassword(string $password): bool {
        // Prüft, ob die Länge des Passworts mindestens 16 Zeichen beträgt
        //   und mindestens einen Großbuchstaben enthält,
        //   und mindestens einen Kleinbuchstaben enthält
        //   und mindestens eine Zahl enthält
        return (strlen($password) >= 16) && preg_match('/[A-Z]/', $password) && preg_match('/[a-z]/', $password) && preg_match('/\d/', $password);
    }

}
