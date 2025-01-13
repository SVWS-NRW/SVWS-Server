<?php

	require_once '../app/Http.php';

	class SMTPClient {

		// Die Serveradresse des SMTP-Servers
		private string $smtpHost = "";

		// Der Port des SMTP-Servers (465 oder 587)
		private int $smtpPort = 465;

		// Der Benutzername für den SMTP-Login
		private string $username = "";

		// Das Password für den SMTP-Login
		private string $password = "";

		// Der Absender der E-Mail
		private string $fromEmail = "";

		// Der Name des Absenders
		private string $fromName = "Webnotenmanager";
		
		// Der Empfänger der E-Mail
		private string $to = "";

		// Der Betreffe der E-Mail
		private string $subject = "";

		// Der Text der E-Mail
		private string $message = "";

		// Das Zeichenencoding der E-Mail
		private string $contentType = "text/plain"; // oder text/html

		// Der MIME-Type des Inhalts
		private string $charset = "utf-8";

		// Timeout für den Verbindungsaufbau
		private int $timeout = 30;

		// Die intern genutzte Verbindung zum SMTP-Server
		private $socket = null;

		/**
		 * Konstruktor: Initialisiert die SMTP-Parameter.
		 *
		 * @param string $config Das Konfigurationsobjekt mit den SMTP-Daten als JSON-String.
		 */
		public function __construct(string $json) {
			$smtpConfig = json_decode($json, true);	//assoziiertes Array

			$this->smtpHost = $smtpConfig['smtpHost'];
			$this->smtpPort = $smtpConfig['smtpPort'];
			$this->username = $smtpConfig['username'];
			$this->password = $smtpConfig['password'];
			$this->fromEmail = $smtpConfig['fromEmail'];
		}

		/**
		 * Setzt die Empfängeradresse, den Betreff und den Nachrichtentext für die E-Mail.
		 *
		 * @param string $to
		 * @param string $subject
		 * @param string $message
		 * @return void
		 */
		public function setEmail($to, $subject, $message): void {
			// Prüfe SMTP-Konfiguration
			if (!$this->isValid())
				Http::exit500("Ungültige SMTP-Konfiguration.");

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
		 * Verbindungsaufbau zum SMTP-Server uns senden der E-Mail.
		 *
		 * @return void
		 */
		public function sendEmail(): void {
			// Überprüfen, ob der Port 465 (SMTPS) oder 587 (STARTTLS) ist
			if ($this->smtpPort == 465) {
				// Verbindung zum SMTP-Server über ssl (oder tls) herstellen
				$this->socket = stream_socket_client("ssl://$this->smtpHost:$this->smtpPort", $errno, $errstr, $this->timeout);
				if (!$this->socket) {
					Http::exit500("Fehler beim Verbinden: $errstr ($errno)");
				}
			} elseif ($this->smtpPort == 587) {
				// Verbindung zum SMTP-Server über tcp herstellen
				$this->socket = fsockopen($this->smtpHost, $this->smtpPort, $errno, $errstr, $this->timeout);
				if (!$this->socket) {
					Http::exit500("Fehler beim Verbinden: $errstr ($errno)");
				}
			}

			// Serverantwort lesen und auswerten
			$this->checkResponse();

			// HELO/EHLO senden
			$hostname = $this->getHostname();
			$this->checkResponse("EHLO $hostname\r\n");

			// STARTTLS unterstützen
			if ($this->smtpPort == 587) {
				$this->checkResponse("STARTTLS\r\n");
				
				// TLS aktivieren
				stream_socket_enable_crypto($this->socket, true, STREAM_CRYPTO_METHOD_TLS_CLIENT);
				
				// Nach dem Starten von TLS erneut EHLO senden
				$this->checkResponse("EHLO $hostname\r\n");
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
				"Content-Type: $this->contentType; " .
				"charset=$this->charset\r\n" . 
				"\r\n" . // Leerzeile zwischen Header und Body
				"$this->message\r\n" .
				".\r\n"; // Beendet die Nachricht
			$this->checkResponse($emailContent);

			
			// Verbindung schließen
			$this->checkResponse("QUIT\r\n");
			fclose($this->socket);
		}
		
		/**
		 * Funktion zum Senden und Überprüfen der Serverantwort
		 *
		 * @param string $command
		 * @return void
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
		 * Prüft, ob die SMTP-Konfiguration vollständig und plausibel ist. Wenn nicht, dann wird false zurückgegeben.
		 * 
		 * @return bool true, wenn die Konfiguration vollständig und plausibel ist und ansonsten false.
		 */
		private function isValid(): bool {
			// Überprüfe, ob alle erforderlichen Felder gefüllt sind
			$requiredFields = [
				$this->smtpHost, 
				$this->smtpPort,
				$this->username, 
				$this->password, 
				$this->fromEmail, 
			];

			foreach ($requiredFields as $field) {
				if (empty($field))
					return false;
			}

			// Überprüfe die Gültigkeit des Ports (465 und 587)
			if ($this->smtpPort != 465 && $this->smtpPort != 587)
				return false;
			
			// Überprüfe die Gültigkeit der E-Mail-Adresse
			if (!filter_var($this->fromEmail, FILTER_VALIDATE_EMAIL))
				return false;
			
			return true;
		}

		private function getHostname(): string {
			return gethostname();
		}

	}
?>