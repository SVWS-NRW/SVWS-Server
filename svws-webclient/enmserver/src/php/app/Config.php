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

		// Lese den Speicherort der SQLite-Datenbank ein
		protected $dbfile = null;

		// Gibt an, ob die Anwendung im Debug-Modus betrieben wird oder nicht
		protected $debugMode = false;

		// Der Benutzername des administrativen Benutzers
		protected $adminUser = null;

		// Das Kennwort des administrativen Benutzers
		protected $adminPassword = null;

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
			
			if (file_exists($this->configfile) != 1) {
				// Versuche eine neue Datei anzulegen...
				$newconfig = (object)[
					'debugMode' => "false",
					'database' => "db/app.sqlite",
					'adminUser' => "admin",
					'adminPassword' => base64_encode(random_bytes(16)),
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

			// Lese die JSON-Datei ein
			$this->config = json_decode(file_get_contents($this->configfile), true);
			if (is_null($this->config)) 
				Http::exit500("Config - Konstruktor: Die Konfigurationsdatei unter dem Pfad $this->configfile ist leer.");
			
			// Lese den Speicherort für die Datenbank ein
			if ((is_null($this->config["database"])) || (!is_string($this->config["database"])))
				Http::exit500("Die Konfiguration ($this->configfile) enhält keine Pfad-Angabe für die SQLite-Datenbank (z.B. \"database\"=\"db/app.sqlite\").");
			$this->dbfile = $this->config["database"];
			
			// Initialisiere Debugging-Einstellung anhand der Konfiguration
			$this->debugMode = (!is_null($this->config["debugMode"])) && (strcasecmp($this->config["debugMode"], "true") == 0);
			if ($this->debugMode) {
				ini_set('display_errors', '1');
			}

			// Initialisiere Admin-Daten
			if ((is_null($this->config["adminUser"])) || (!is_string($this->config["adminUser"]) || (strlen($this->config["adminUser"]) == 0))
				|| (is_null($this->config["adminPassword"])) || (!is_string($this->config["adminPassword"]) || (strlen($this->config["adminPassword"]) < 10)))
				Http::exit500("Es wurde kein Admin-Benutzer mit Kennwort für die Anwendung festgelegt. Diese müssen in der Konfiguration unter adminUser und adminPassword festgelegt werden.");
			$this->adminUser = $this->config["adminUser"];
			$this->adminPassword = $this->config["adminPassword"];

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
		 * @return der root-Pfad
		 */
		public function getAppRoot(): string {
			return $this->app_root;
		}

		/**
		 * Gibt den Speicherort der SQLite-Datenbank zurück
		 * 
		 * @return der Speicherort
		 */
		public function getDatabaseFile(): string {
			return $this->dbfile;
		}

		/**
		 * Gibt den Benutzernamen des Admin-Benutzers zurück.
		 * 
		 * @return der Admin-Benutzername
		 */
		public function getAdminUsername(): string {
			return $this->adminUser;
		}

		/**
		 * Gibt das Kennwort des Admin-Benutzers zurück.
		 * 
		 * @return das Kennwort des Admin-Benutzers
		 */
		public function getAdminPassword(): string {
			return $this->adminPassword;
		}

		/**
		 * Gibt die SMTP-Konfiguration zurück.
		 */
		public function getSMTPConfig(): array {
			return $this->smtpConfig;
		}

	}

?>
