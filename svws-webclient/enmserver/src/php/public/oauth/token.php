<?php
	/**
	 * API-Endpunkt für die Erstellung eines neuen Access Tokens.
	 *
	 * Dieser Endpunkt prüft die HTTP-Methode, die Authentifizierung des Clients und erstellt ein neues Access Token.
	 * Das Token wird dann als JSON-Response zurückgegeben.
	 *
	 * HTTP Method: POST
	 * URL: /path/to/endpoint
	 *
	 * @throws Http::exit403Forbidden Wenn die Authentifizierung fehlschlägt oder das Token nicht erstellt werden kann.
	 *
	 * @return void
	 */

	require_once __DIR__.'/../../app/init.php';

	// Prüfe die HTTP-Methode ...
	$auth->pruefeHTTPMethod([ "POST" ]);

	// ... dann, ob eine Authentifizierung mit dem Client-Secret vorliegt
	$clientID = $auth->pruefeClientSecret();

	// ... und erzeuge dann ein neues Token
	$newToken = $db->createClientAccessToken($clientID);
	if ($newToken == null)
		Http::exit403Forbidden();

	// ... und gebe dieses zurück
	header("Content-type: application/json; charset=utf-8");
	echo json_encode($newToken, JSON_UNESCAPED_SLASHES);

?>
