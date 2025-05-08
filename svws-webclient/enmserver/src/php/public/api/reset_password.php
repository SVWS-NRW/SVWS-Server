<?php

	/**
	 * Endpunkt zum Zurücksetzen des Passworts eines Lehrers.
	 *
	 * Dieser Endpunkt ermöglicht es, das Passwort eines Lehrers zurückzusetzen, wenn ein gültiger Token vorliegt.
	 *
	 * @httpMethod PUT
	 * @auth Keine Authentifizierung erforderlich, nur gültiges Token
	 * @param object JSON-Objekt mit {eMailDienstlich: string, password: string, token: string} Die benötigten Daten zum Zurücksetzen des Passworts.
	 * @param {id: number, passwordHash: string, tsPasswordHash: string} Das Patch-Objekt, das die zu aktualisierenden Daten des Lehrers enthält.
	 * Folgende Werte können durch das Patch Objekt überschrieben werden: tsPasswordHash, passwordHash
	 * 
	 * @return void
	 * @responseCode 204 Erfolgreiche Änderung ohne Inhalt.
	 * @responseCode 400 Fehlerhafte Anfrage, wenn Pflichtfelder im JSON-Objekt fehlen.
	 * @responseCode 409 Fehlerhafte Anfrage, wenn Token ungültig oder Passwort nicht regelkonform ist.
	 */
	// Initialisierung
	require_once __DIR__.'/../../app/init.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod(["PUT"]);

	// Lehrerdaten aus der Datenbank lesen
	$data = Http::getBodyJsonObject();
	$eMailDienstlich = $data->eMailDienstlich ?? null;
	$password = $data->password ?? null;
	$token = $data->token ?? null;

	// Überprüfen, ob die erforderlichen Daten vorhanden sind
	if (empty($eMailDienstlich) || empty($password) || empty($token)) {
		http_response_code(400); // Bad Request
		echo json_encode(['fehler' => 'Die Dienst-E-Mail und das Passwort sind erforderlich.']);
		exit;
	}

	// Gültigkeit des Tokens prüfen (Dauer und Token vorhanden)
	if (!$db->isENMLehrerTokenValid($token)) {
		http_response_code(409); // Conflict
		echo json_encode(['fehler' => 'Der Token ist nicht gültig.']);
		exit;
	}

	// Passwort validieren
	if (!Config::validatePassword($password)) {
		http_response_code(409); // Conflict
		echo json_encode(['fehler' => 'Das Passwort entspricht nicht den Konventionen.']);
		exit;
	}

	// Lehrerdaten anhand der E-Mail-Adresse holen
	if (!$db->checkENMLehrerByEmail($eMailDienstlich)) {
		http_response_code(409); // Conflict
		echo json_encode(['fehler' => 'Mehrere Lehrer mit dieser E-Mail-Adresse gefunden.']);
		exit;
	}

	$lehrerDaten = $db->getENMLehrerByEmail($eMailDienstlich);
	$ts = date('Y-m-d H:i:s.v', time());
	$lehrerId = $lehrerDaten->id;
	$passwordHash = password_hash($password, PASSWORD_DEFAULT);
	$lehrerPatch = (object)[
		'id' => $lehrerId,
		'passwordHash' => $passwordHash,
		'tsPasswordHash' =>  $ts,
	];

	// Daten in die Datenbank zurückschreiben
	$db->patchENMLehrerPassword($ts, $lehrerDaten, $lehrerPatch);
	$db->deleteENMLehrerToken($lehrerId);

	// HTTP-Statuscode 204 zurückgeben (keine Inhalte)
	http_response_code(204);
	exit;

?>
