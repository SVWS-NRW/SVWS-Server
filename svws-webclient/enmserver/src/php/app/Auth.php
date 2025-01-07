<?php

	require_once 'Http.php';
	require_once 'Config.php';
	require_once 'Database.php';

	/**
	 * Diese Klasse stellt die Funktionalität für die Authentifizierung zur Verfügung.
	 */
	class Auth {
		
		// Die Datenbank für die Überprüfung von Credentials
		protected $db = null;

		// Die Authentifizierungsmethode (Unerstützt werden aktuell "Basic" und "Bearer")
		protected $authMethod = null;

		// Der Benutzername bei einer Basic-Authentifizierung
		protected $authUser = null;

		// Das Kennwort bei einer Basic-Authentifizierung
		protected $authPassword = null;

		// Das Token bei einer Bearer-Authentifizierung
		protected $authToken = null;


		/**
		 * Erstellt ein neues Authentifizierungsobjekt mit den Informationen aus dem HTTP-Request
		 */
		public function __construct(Database $db) {
			$this->db = $db;
			if (!array_key_exists("HTTP_AUTHORIZATION", $_SERVER))
				Http::exit500("HTTP-Authorization-Header kann nicht gelesen werden. Überprüfen sie die Anfrage oder die Server-Konfiguration.");
			$parts = explode(" ", $_SERVER["HTTP_AUTHORIZATION"], 2);
			if (strcasecmp($parts[0], "Basic") == 0) {
				$this->authMethod = "Basic";
				$creds = explode(":", base64_decode($parts[1]));
				if (count($creds) != 2)
					Http::exit500("Fehler bei dem HTTP-Authorization-Header. Die Kodierung von Benutzername und Kennwort ist fehlerhaft.");
				$this->authUser = $creds[0];
				$this->authPassword = $creds[1];
			} else if (strcasecmp($parts[0], "Bearer") == 0) {
				$this->authMethod = "Bearer";
				$this->authToken = $parts[1];
			} else {
				Http::exit500("Die Authentifizierungsmethode wird aktuell noch nicht unterstützt.");
			}
		}

		/**
		 * Prüfe den Authorization-Header, ob dieser eine Basic-Authentifizierung mit dem übergebenen Benutzernamen und
		 * dem übergebenen Benutzer-Kennwort hat.
		 * Tritt ein Fehler bei der Prüfung auf, so wird ein Fehlercode 401 zurückgegeben.
		 * 
		 * @param string $username   der Benutzername
		 * @param string $password   das Kennwort
		 */
		public function pruefeBasicAuth(string $username, string $password) {
			if ((strcmp($this->authMethod, "Basic") != 0)
				|| (strcasecmp($this->authUser, $username) != 0)
				|| (strcmp($this->authPassword, $password) != 0))
				Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", charset="UTF-8"');
		}

		/**
		 * Prüfe den Authorization-Header, ob dieser eine Basic-Authentifizierung mit den Credentials
		 * des Admin-Benutzers hat.
		 * Tritt ein Fehler bei der Prüfung auf, so wird ein Fehlercode 401 zurückgegeben.
		 */
		public function pruefeAdminBasicAuth(Config $config) {
			$this->pruefeBasicAuth($config->getAdminUsername(), $config->getAdminPassword());
		}

		/**
		 * Prüfe den Authorization-Header, ob dieser eine Basic-Authentifizierung mit den Crendentials
		 * eines Lehrers hat.
		 * Tritt ein Fehler bei der Prüfung auf, so wird ein Fehlercode 401 zurückgegeben.
		 */
		public function pruefeLehrerBasicAuth() : object {
			if (strcmp($this->authMethod, "Basic") != 0)
				Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", charset="UTF-8"');
			$lehrer = $this->db->getENMLehrerByEmail($this->authUser);
			if ($lehrer === null)
				Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", charset="UTF-8"');
			if (!password_verify($this->authPassword, $lehrer->passwordHash))
				Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", charset="UTF-8"');
			return $lehrer;
		}

		/**
		 * Prüft, ob das Access-Token der Anfrage zu einem Client gehört und gültig ist.
		 * Tritt ein Fehler bei der Prüfung auf, so wird ein Fehlercode 401 zurückgegeben.
		 */
		public function pruefeAccessToken() {
			if (strcmp($this->authMethod, "Bearer") != 0)
				Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_request", error_description="An access token is required"');
			$client = $this->db->getClientByAccessToken($this->authToken);
			if ($client == null)
				Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_token", error_description="The access token is not valid"');
			$elapsed = time() - $client->tokenTimestamp;
			if ($elapsed < 0)
				Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_token", error_description="The access token has an invalid timestamp"');
			if ($elapsed > $client->tokenValidForSecs)
				Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_token", error_description="The access token has expired"');
		}

		/**
		 * Prüfe den Authorization-Header, ob die Methode "Basic" vorliegt, der Benutzername
		 * eine gültige Client-ID ist und das Client-Secret zu dem Secret in der Datenbank passt.
		 * Tritt ein Fehler bei der Prüfung auf, so wird ein Fehlercode 401 zurückgegeben.
		 * 
		 * @return int im Erfolgsfall wird die authorisierte Client-ID zurückgegeben
		 */
		public function pruefeClientSecret(): int {
			if (strcmp($this->authMethod, "Basic") != 0)
				Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", error="invalid_client", error_description="Client authentication is required"');
			$clientID = intval($this->authUser);
			if ($clientID <= 0)
				Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", error="invalid_client", error_description="Client is unknown"');
			$dbSecret = $this->db->getClientSecret($clientID);
			if ($dbSecret == null)
				Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", error="invalid_client", error_description="Client secret does not exist"');
			if (strcmp($this->authPassword, $dbSecret) != 0)
				Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", error="invalid_client", error_description="Invalid client secret"');
			return $clientID;
		}

		/**
		 * Gibt die HTTP-Methode zurück.
		 */
		public function getHTTPMethod() {
			return $_SERVER['REQUEST_METHOD'];
		}

		/**
		 * Prüft, ob die HTTP-Methode erlaubt ist oder nicht.
		 */
		public function pruefeHTTPMethod(string | array $allowed) {
			if (strcmp(gettype($allowed), "array") === 0) {
				$hasMethod = false;
				foreach ($allowed as $tmp)
					if (strcmp($_SERVER['REQUEST_METHOD'], $tmp) != 0)
						$hasMethod = true;
				if (!$hasMethod)
					Http::exit403Forbidden();
			} else {
				if (strcmp($_SERVER['REQUEST_METHOD'], $allowed) != 0)
					Http::exit403Forbidden();
			}
		}

	}

?>