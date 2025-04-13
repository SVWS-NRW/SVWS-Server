<?php

	require_once 'Http.php';

	/**
	 * Die Klasse ermöglicht die Verbindung zu einem SMTP-Server, das Versenden von 
	 * E-Mails mit Authentifizierung und optionaler TLS-Verschlüsselung.
	 * 
	 * Beispiel zur Nutzung des SMTP-Clients:
 	 * 
 	 * $config = json_encode([
 	 *		'smtpHost' => 'smtp.example.com',
 	 *		'smtpPort' => 587,
 	 *		'username' => 'user',
 	 *		'password' => 'password',
 	 *		'useTLS' => true,
 	 *		'fromEmail' => 'noreply@example.com',
 	 *		'fromName' => 'Mein Name'
 	 * ]);
 	 *
 	 * $mailer = new SMTPClient($config);
 	 * if (!$mailer->isValid())
 	 * 		die("SMTP-Konfiguration fehlerhaft.");
 	 *
 	 * $mailer->setEmail('recipient@example.com', 'Betreff', 'Hallo, dies ist eine Test-E-Mail.');
 	 * $mailer->sendEmail();
	 */
	class SMTPClient {

		/**
		 * Die Serveradresse des SMTP-Servers.
		 *
		 * @var string|null
		 */
		private ?string $host = null;

		/**
		 * Der Port des SMTP-Servers (Standard: 587).
		 *
		 * @var int
		 */
		private int $port = 587;

		/**
		 * Der Benutzername für die SMTP-Authentifizierung.
		 *
		 * @var string|null
		 */
		private ?string $username = null;

		/**
		 * Das Passwort für die SMTP-Authentifizierung.
		 *
		 * @var string|null
		 */
		private ?string $password = null;

		/**
		 * Gibt an, ob TLS verwendet werden soll (true für TLS, false für unverschlüsselt).
		 *
		 * @var bool
		 */
		private bool $useTLS = true;

		/**
		 * Die E-Mail-Adresse des Absenders.
		 *
		 * @var string|null
		 */
		private ?string $fromEmail = null;

		/**
		 * Der Name des Absenders.
		 *
		 * @var string|null
		 */
		private ?string $fromName = null;
		
		/**
		 * Die Empfänger-E-Mail-Adresse.
		 *
		 * @var string|null
		 */
		private ?string $to = null;

		/**
		 * Der Betreff der E-Mail.
		 *
		 * @var string|null
		 */
		private ?string $subject = null;

		/**
		 * Der Nachrichtentext der E-Mail.
		 *
		 * @var string|null
		 */
		private ?string $message = null;

		/**
		 * Die interne Verbindung zum SMTP-Server.
		 *
		 * @var resource|null
		 */
		private $socket = null;

		/**
		 * Konstruktor: Initialisiert die SMTP-Parameter aus einer JSON-Konfigurationsdatei.
		 *
		 * @param string|null $config Ein JSON-String mit den SMTP-Konfigurationswerten.
		 */
		public function __construct(?string $config) {
			// Überprüfen, ob der Parameter null oder leer ist
			if (empty($config))
				Http::exit500("Keine SMTP-Konfiguration vorhanden.");

			$smtpConfig = json_decode($config, true);

			$this->host = $smtpConfig['host'];
			$this->port = $smtpConfig['port'];
			$this->username = $smtpConfig['username'];
			$this->password = $smtpConfig['password'];
			$this->useTLS = $smtpConfig['useTLS'];
			$this->fromEmail = $smtpConfig['fromEmail'];
			$this->fromName = $smtpConfig['fromName'];
		}

		/**
		 * Überprüft, ob die SMTP-Konfiguration vollständig und gültig ist.
		 * 
		 * @return bool true, wenn die Konfiguration gültig ist, sonst false.
		 */
		public function isValid(): bool {
			// Überprüfe, ob alle erforderlichen Felder gefüllt sind
			$requiredFields = [
				$this->host, 
				$this->port,
				$this->username, 
				$this->password,
				$this->useTLS, 
				$this->fromEmail,
				$this->fromName 
			];

			foreach ($requiredFields as $field) {
				if (empty($field))
					return false;
			}

			// Überprüfe die Gültigkeit der Host-Adresse
			if (!filter_var($this->host, FILTER_VALIDATE_DOMAIN, FILTER_FLAG_HOSTNAME) && !filter_var($this->host, FILTER_VALIDATE_IP))
				return false;
			
				// Überprüfe die Gültigkeit des Ports (zwischen 1 und 65535)
			if (!is_int($this->port) || $this->port < 1 || $this->port > 65535)
				return false;
			
			// Überprüfe die Gültigkeit der E-Mail-Adresse
			if (!filter_var($this->fromEmail, FILTER_VALIDATE_EMAIL))
				return false;
			
			return true;
		}

		/**
		 * Setzt die E-Mail-Daten (Empfänger, Betreff und Nachricht).
		 *
		 * @param string $to      Die Empfänger-E-Mail-Adresse.
		 * @param string $subject Der Betreff der E-Mail.
		 * @param string $message Der Nachrichtentext der E-Mail.
		 */
		public function setEmail($to, $subject, $message): void {
			// Überprüfe die Gültigkeit der E-Mail-Adresse $to
			if (!filter_var($to, FILTER_VALIDATE_EMAIL))
				Http::exit500("Ungültige Empfänger E-Mail-Adresse.");

			// Überprüfen, ob der Betreff gesetzt ist
			if (empty($subject))
				Http::exit500("Es wurde kein Betreff angegeben");

			// Überprüfen, ob der Nachrichtentext gesetzt ist
			if (empty($message))
				Http::exit500("Es wurde kein Nachrichtentext angegeben");

			$this->to = $to;
			$this->subject = $subject;
			$this->message = $message;
		}

		/**
		 * Baut eine Verbindung zum SMTP-Server auf und sendet die E-Mail.
		 */
		public function sendEmail(): void {

			$contentType = "text/plain"; // alternativ text/html
			$charset = "utf-8";
			$timeout = 30;

			// Verbindung basierend auf Port und TLS-Einstellung aufbauen
			if ($this->useTLS)
				if ($this->port == 465)
					// SSL/TLS direkte Verbindung
					$this->socket = stream_socket_client("ssl://$this->host:$this->port", $errno, $errstr, $timeout);
				else
					// Unverschlüsselte Verbindung, später STARTTLS aktivieren
					$this->socket = fsockopen($this->host, $this->port, $errno, $errstr, $timeout);
			else
				// Unverschlüsselte Verbindung
				$this->socket = fsockopen($this->host, $this->port, $errno, $errstr, $timeout);

			if (!$this->socket)
				Http::exit500("Fehler beim Verbinden: $errstr ($errno)");

			// Serverantwort lesen und auswerten
			$this->checkResponse();

			// HELO/EHLO senden
			$hostname = $this->getHostname();
			$this->checkResponse("EHLO $hostname\r\n");

			// Falls TLS aktiviert ist und der Port nicht 465 ist, dann STARTTLS starten
			if ($this->useTLS && $this->port != 465) {
				$this->checkResponse("STARTTLS\r\n");
				stream_socket_enable_crypto($this->socket, true, STREAM_CRYPTO_METHOD_TLS_CLIENT);
				$this->checkResponse("EHLO $hostname\r\n"); // Nach TLS erneut EHLO senden
			}

			// Authentifizierung
			$this->checkResponse("AUTH LOGIN\r\n");
			$this->checkResponse(base64_encode($this->username) . "\r\n");
			$this->checkResponse(base64_encode($this->password) . "\r\n");

			// Absender und Empfänger festlegen
			$this->checkResponse("MAIL FROM: <$this->fromEmail>\r\n");
			$this->checkResponse("RCPT TO: <$this->to>\r\n");
			
			// Nachricht senden
			$this->checkResponse("DATA\r\n");

			// E-Mail-Inhalt zusammenstellen
			$emailContent = "Subject: $this->subject\r\n" .
				"From: \"$this->fromName\" <$this->fromEmail>\r\n" .
				"To: $this->to\r\n" .
				"Content-Type: $contentType; " .
				"charset=$charset\r\n" . 
				"\r\n" . // Leerzeile zwischen Header und Body
				"$this->message\r\n" .
				".\r\n"; // Beendet die Nachricht
			$this->checkResponse($emailContent);

			// Verbindung schließen
			$this->checkResponse("QUIT\r\n");
			fclose($this->socket);
		}
		
		/**
		 * Sendet einen Befehl an den SMTP-Server und prüft die Antwort.
		 *
		 * @param string|null $command Der Befehl, der an den Server gesendet wird (optional).
		 */
		private function checkResponse($command = null): void {
			if (isset($command))
				fwrite($this->socket, $command);
			
			$response = '';
			while ($line = fgets($this->socket, 512)) {
				$response .= $line;
				if (substr($line, 3, 1) == ' ')
					break;
			}
			
			// Überprüfen, ob die Antwort gültig ist (2xx oder 3xx)
			if (substr($response, 0, 1) != '2' && substr($response, 0, 1) != '3') {
				// Standardfehlermeldung, wenn kein Befehl angegeben ist
				$errorMessage = isset($command) ? "Fehler bei Befehl '" . trim($command) . "': " . trim($response) : "Fehler bei der Serverantwort: " . trim($response);
				Http::exit500($errorMessage);
			}
		}

		/**
		 * Gibt den Hostnamen des aktuellen Systems zurück.
		 *
		 * @return string Der Hostname.
		 */
		private function getHostname(): string {
			return gethostname();
		}

	}
?>