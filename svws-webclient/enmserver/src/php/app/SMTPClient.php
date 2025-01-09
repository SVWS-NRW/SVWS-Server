<?php

	require_once 'Http.php';
	require_once 'Config.php';

	/**
	 * Diese Klasse stellt die Funktionalität für den Versand von E-Mails über SMTP zur Verfügung.
	 */
	class SMTPClient {

		// Die Serveradresse des SMTP-Servers
		protected ?string $host = null;

		// Der Port des SMTP-Servers
		protected ?int $port = null;

		// Der Benutzername für den SMTP-Login
		protected ?string $username = null;

		// Das Passwort für den SMTP-Login
		protected ?string $password = null;

		// Verwendung TLS - true für TLS bzw. false für unverschlüsselt
		protected bool $useTLS = false;

		// Der Absender der E-Mail
		protected ?string $fromEmail = null;

		// Der Name des Absenders
		protected ?string $fromName = null;

		// Die intern genutzte Verbindung zum SMTP-Server
		private $connection = null;


		/**
		 * Konstruktor: Initialisiert die SMTP-Parameter.
		 *
		 * @param Config $config Konfigurationsobjekt mit SMTP-Daten.
		 */
		public function __construct(Config $config) {
			$smtpConfig = $config->getSMTPConfig();
			$requiredKeys = ['host', 'port', 'username', 'password', 'useTLS', 'fromEmail', 'fromName'];

			foreach ($requiredKeys as $key)
				if (empty($smtpConfig[$key]))
					Http::exit500("SMTP-Konfiguration fehlt: $key");

			$this->host = $smtpConfig['host'];
			$this->port = $smtpConfig['port'];
			$this->username = $smtpConfig['username'];
			$this->password = $smtpConfig['password'];
			$this->useTLS = $smtpConfig['useTLS'];
			$this->fromEmail = $smtpConfig['fromEmail'];
			$this->fromName = $smtpConfig['fromName'];
		}

		/**
		 * Sendet eine E-Mail über den SMTP-Server.
		 *
		 * @param string $to Empfänger-Adresse
		 * @param string $subject Betreff der E-Mail
		 * @param string $body Inhalt der E-Mail
		 */
		public function sendMail(string $to, string $subject, string $body): void {
			try {
				$this->connect();
				$this->authenticate();
				$this->send($this->fromEmail, $to, $subject, $body);
			} catch (Exception $e) {
				Http::exit500("SMTP-Fehler: " . $e->getMessage());
			} finally {
				$this->close();
			}
		}

		/**
		 * Stellt die Verbindung zum SMTP-Server her.
		 */
		private function connect(): void {
			// Verbindungsprotokoll basierend auf useTLS
			$protocol = $this->useTLS ? 'ssl://' : 'tcp://';
			
			// Starten mit unverschlüsseltem Socket, um STARTTLS zu unterstützen
			$this->connection = @stream_socket_client("{$protocol}{$this->host}:{$this->port}", $errno, $errstr, 30);

			if (!$this->connection)
				Http::exit500("Verbindung fehlgeschlagen ($errno - $errstr).");

			// SMTP-Handshake (EHLO senden und Antwort auswerten)
			if (!$this->sendCommand("EHLO " . gethostname()))
				Http::exit500("EHLO-Befehl fehlgeschlagen.");
	
			// Nur bei unverschlüsselter Verbindung STARTTLS prüfen und aktivieren
			if (!$this->useTLS) {
				$supportsSTARTTLS = false;
				// Auf 250-STARTTLS prüfen
				while ($response = fgets($this->connection, 1024)) {
					if (strpos($response, '250-STARTTLS') !== false) {
						$supportsSTARTTLS = true;
						break;
					}
				}

				if ($supportsSTARTTLS) {
					// STARTTLS aktivieren
					if (!$this->sendCommand("STARTTLS"))
						Http::exit500("STARTTLS-Befehl fehlgeschlagen.");

					// TLS/SSL aktivieren
					$cryptoEnabled = @stream_socket_enable_crypto($this->connection, true, STREAM_CRYPTO_METHOD_TLS_CLIENT);
					if (!$cryptoEnabled)
						Http::exit500("TLS/SSL konnte nicht für die Verbindung aktiviert werden.");
				}
			}
		}
		
		/**
		 * Authentifiziert sich beim SMTP-Server.
		 */
		private function authenticate(): void {
			if (empty($this->username) || empty($this->password))
				Http::exit400BadRequest("Benutzername oder Passwort ist leer.");
			
			if (!$this->sendCommand("AUTH LOGIN"))
					Http::exit401Unauthorized("AUTH LOGIN fehlgeschlagen.");
			
			if (!$this->sendCommand(base64_encode($this->username)))
					Http::exit401Unauthorized("Benutzername wurde nicht akzeptiert.");
			
			if (!$this->sendCommand(base64_encode($this->password)))
					Http::exit401Unauthorized("Passwort wurde nicht akzeptiert.");
		}

		/**
		 * Sendet die E-Mail an den Empfänger.
		 *
		 * @param string $to Empfänger-Adresse
		 * @param string $subject Betreff der E-Mail
		 * @param string $body Inhalt der E-Mail
		 */
		private function send(string $to, string $subject, string $body): void {
			if (!$this->sendCommand("MAIL FROM:<$this->fromEmail>"))
				Http::exit400BadRequest("MAIL FROM-Befehl fehlgeschlagen.");

			if (!$this->sendCommand("RCPT TO:<$to>"))
				Http::exit400BadRequest("RCPT TO-Befehl fehlgeschlagen.");

			if (!$this->sendCommand("DATA"))
				Http::exit500("DATA-Befehl fehlgeschlagen.");

			$message = "Subject: $subject\r\nFrom: {$this->fromName} <{$this->fromEmail}>\r\nTo: $to\r\n\r\n$body\r\n.";
			if (!$this->sendCommand($message))
				Http::exit500("Nachricht konnte nicht gesendet werden.");
		}

		/**
		 * Schließt die Verbindung zum SMTP-Server.
		 */
		private function close(): void {
			if ($this->connection) {
				// Versuche den QUIT-Befehl zu senden und überprüfe das Ergebnis
				if (!$this->sendCommand("QUIT"))
					Http::exit500("SMTP-Fehler: QUIT-Befehl konnte nicht gesendet werden.");

				// Schließe die Verbindung und setze die Variable auf null
				fclose($this->connection);
				$this->connection = null;
			}
		}

		/**
		 * Sendet einen Befehl an den SMTP-Server und prüft die Antwort.
		 *
		 * @param string $command Der Befehl, der gesendet werden soll.
		 * @return bool true, wenn der Befehl erfolgreich war, andernfalls false.
		 */
		private function sendCommand(string $command): bool {
			fwrite($this->connection, $command . "\r\n");
			$response = fgets($this->connection, 512);

			// Akzeptiere Antworten mit Statuscodes 2xx oder 3xx
			return str_starts_with($response, "2") || str_starts_with($response, "3");
		}
	}

?>