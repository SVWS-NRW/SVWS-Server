<?php

	require_once 'Http.php';

	/**
	 * Diese Klasse stellt die Funktionalität für den Zugriff auf die Konfiguration des Servers zur Verfügung
	 */
	class Config {

		// Der Root-Pfad für die Applikation
		protected $app_root = null;

		// Der vollständige Pfad, wo sich die Konfigurationsdatei befindet
		protected $configfile = null;

		// Die Konfiguration aus der JSON-Datei, mit welcher diese Klasse initialisiert wurde
		protected $config = null;

		// Der Speicherort der SQLite-Datenbank
		protected string $dbfile = "db/app.sqlite";

		// Der Dateiname, wo das Client-Secret für die Verbindung des SVWS-Servers zu dem ENM-Server gespeichert wird
		protected string $secretfile = "db/client.sec";

		// Das Client-Secret, sobald es vom Konstruktor eingelesen (und ggf. erzeugt) wurde
		protected ?string $secret = null;

		// Gibt an, ob die Anwendung im Debug-Modus betrieben wird oder nicht
		protected $debugMode = false;

		// Die SMTP-Konfiguration für das Senden von Mails zum Neusetzen des Kennwortes
		protected $smtpConfig = [];

		/**
		 * Erstellt ein neues Konfigurationsobjekt, indem die übergebene JSON-Datei eingelesen
		 * und überprüft wird.
		 *
		 * @param string $jsonfile   der Name der JSON-Datei, ggf. mit Pfad
		 */
		public function __construct(string $jsonfile) {
			// Bestimme zunächst das Root-Verzeichnis der Anwendung
			$this->app_root = substr($_SERVER["DOCUMENT_ROOT"], 0, -strlen("/public"));

			// Prüfe dann, ob die JSON-Datei existiert und lese den Inhalt
			if (!is_string($jsonfile))
				Http::exit500("Config - Konstruktor: Es wurde für den Dateinamen kein string übergeben.");
			$this->configfile = "$this->app_root/$jsonfile";
			
			if (file_exists($this->configfile) !== 1) {
				// Versuche eine neue Datei anzulegen...
				$newconfig = (object)[
					'debugMode' => "false",
					'smtp' => (object)[
						'host' => '',
						'port' => 587,
						'username' => '',
						'password' => '',
						'useTLS' => false,
						'fromEmail' => '',
						'fromName' => ''
					]
				];
				$success = file_put_contents($this->configfile, json_encode($newconfig, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES));
				if ($success === false)
					Http::exit500("Config - Konstruktor: Es wurde keine Konfigurationsdatei unter dem Pfad $this->configfile gefunden und es konnte auch keine neue erstellt werden.");
			}

			// Lese das Client-Secret ein. Wenn nich keines existiert, dann erzeuge es zuvor 
			$secretfile = "$this->app_root/$this->secretfile";
			if (!file_exists($secretfile)) {
				// Versuche eine neues Secret anzulegen anzulegen...
				$secret = Config::generateRandomSecret();
				$success = file_put_contents($secretfile, $secret);
				if ($success === false)
					Http::exit500("Es konnte kein Client-Secret unter $this->secretfile generiert werden. Überprüfen Sie, ob die, beim Web-Server, konfigurierten Rechte ausreichend sind, um diese Datei anzulegen.");
			}
			$this->secret = file_get_contents($secretfile);

			// Lese die JSON-Datei ein
			$this->config = json_decode(file_get_contents($this->configfile), true);
			if (is_null($this->config)) 
				Http::exit500("Config - Konstruktor: Die Konfigurationsdatei unter dem Pfad $this->configfile ist leer.");
			
			// Initialisiere Debugging-Einstellung anhand der Konfiguration
			$this->debugMode = (!is_null($this->config["debugMode"])) && (strcasecmp($this->config["debugMode"], "true") == 0);
			if ($this->debugMode) {
				ini_set('display_errors', '1');
			}

			// Initialisiere SMTP-Konfiguration
			$this->smtpConfig = $this->config['smtp'] ?? [];
			if (empty($this->smtpConfig) || !isset($this->smtpConfig['host'], $this->smtpConfig['port'], $this->smtpConfig['useTLS'])) {
				// Http::exit500("Die SMTP-Konfiguration ist fehlerhaft oder unvollständig.");
				// TODO Die SMTP-Konfiguration kann unvollständig sein. In diesem Fall sollte die Option des Kennwort-Neu-Setzens im Client nicht angeboten werden
				// TODO ggf. Endpunkt, um die Existenz einer validen SMTP-Konfiguration abzufragen
			}
		}

		/**
		 * Entlädt das Konfigurationsobject.
		 */
		public function __destruct() {
			$this->config = null;
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
			return $this->dbfile;
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
		 * Gibt die SMTP-Konfiguration zurück.
		 * 
		 * @return die SMTP-Konfiguration
		 */
		public function getSMTPConfig(): array {
			return $this->smtpConfig;
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
