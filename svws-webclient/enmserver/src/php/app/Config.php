<?php

	require_once 'Http.php';

	/**
	 * Diese Klasse stellt die Funktionalität für den Zugriff auf die Konfiguration des Servers zur Verfügung
	 */
	class Config {

		// Der Root-Pfad für die Applikation
		protected $app_root = null;

		// Der Speicherort der SQLite-Datenbank
		protected static string $dbfile = "db/app.sqlite";

		// Der Dateiname, wo das Client-Secret für die Verbindung des SVWS-Servers zu dem ENM-Server gespeichert wird
		protected static string $secretfile = "db/client.sec";

		// Der Dateiname, wo die Information zum Server-Mode gesetzt werden kann. Ist diese Information nicht vorhanden, so wird 'stable' angenommen.
		protected static string $servermodefile = "db/server.mode";

		// Der Modus, in welchem der Server betrieben wird ('dev', 'alpha', 'beta', 'stable')
		protected string $serverMode = "stable";

		// Das Client-Secret, sobald es vom Konstruktor eingelesen (und ggf. erzeugt) wurde
		protected ?string $secret = null;

		// Gibt an, ob die Anwendung im Debug-Modus betrieben wird oder nicht
		protected $debugMode = false;


		/**
		 * Erstellt ein neues Konfigurationsobjekt, indem die übergebene JSON-Datei eingelesen
		 * und überprüft wird.
		 */
		public function __construct() {
			// Bestimme zunächst das Root-Verzeichnis der Anwendung
			$this->app_root = Config::determineAppRoot();

			// Lese das Client-Secret ein. Wenn nich keines existiert, dann erzeuge es zuvor 
			$secretfile = $this->app_root."/".Config::$secretfile;
			if (!file_exists($secretfile)) {
				// Versuche eine neues Secret anzulegen anzulegen...
				$secret = Config::generateRandomSecret();
				$success = file_put_contents($secretfile, $secret);
				if ($success === false)
					Http::exit500("Es konnte kein Client-Secret unter $this->secretfile generiert werden. Überprüfen Sie, ob die, beim Web-Server, konfigurierten Rechte ausreichend sind, um diese Datei anzulegen.");
			}
			$this->secret = file_get_contents($secretfile);

			// Setze den Server-Mode, welcher auch an den Client weitergegeben wird
			$servermodefile = $this->app_root."/".Config::$servermodefile;
			$serverMode = file_exists($servermodefile) ? file_get_contents($servermodefile) : 'stable';
			$serverMode = strtolower($serverMode);
			if ((strcmp($serverMode, 'stable') !== 0) && (strcmp($serverMode, 'beta') !== 0) && (strcmp($serverMode, 'alpha') !== 0) && (strcmp($serverMode, 'dev') !== 0))
				Http::exit500("Der konfigurierte Server-Mode ist ungültig. Überprüfen Sie, die Datei $servermodefile auf dem Web-Server");
			$this->serverMode = $serverMode;

			// Initialisiere Debugging-Einstellung anhand des Server-Mode
			$this->debugMode = (strcmp($serverMode, 'stable') !== 0);
			if ($this->debugMode) {
				ini_set('display_errors', '1');
			}

		}

		/**
		 * Bestimmt das Verzeichnis, in dem sich die Applikation befindet
		 * 
		 * @return string der absolute Pfad, wo sich die Applikation befindet
		 */
		protected static function determineAppRoot(): string {
			return substr($_SERVER["DOCUMENT_ROOT"], 0, -strlen("/public"));
		}

		/**
		 * Prüft, ob die Anwendung bereits initialisiert wurde, in dem geprüft wird, ob
		 * das Client-Secret und die SQLite-Datenbank beide vorliegen.
		 */
		public static function isAppInitialized() : bool {
			return file_exists(Config::determineAppRoot()."/".Config::$secretfile) && file_exists(Config::determineAppRoot()."/".Config::$dbfile);
		}

		/**
		 * Gibt den Root-Pfad für die Applikation zurück
		 * 
		 * @return string der root-Pfad
		 */
		public function getAppRoot(): string {
			return $this->app_root;
		}

		/**
		 * Gibt den Speicherort der SQLite-Datenbank zurück
		 * 
		 * @return string der Speicherort
		 */
		public function getDatabaseFile(): string {
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
		 * Erzeugt einen zufälligen String, der für Kennwörter verwendet werden kann.
		 * 
		 * @return string das neue Kennwort
		 */
		public static function generateRandomSecret() : string {
			return base64_encode(random_bytes(32));
		}

	}

?>
