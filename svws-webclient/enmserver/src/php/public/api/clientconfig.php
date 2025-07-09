<?php
	/**
	 * Endpunkt welcher die aktuelle Client Config zurück gibt oder aktualisiert.
	 *
	 * Bei "GET" wird die gesamte Konfiguration (benutzerspezifisch und global) zurückgegeben.
	 * Bei "PUT" wird ein Eintrag in der benutzerspezifischen Konfiguration gesetzt oder entfernt.
	 *
	 * @httpMethod GET, PUT
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 * @param {key: der Key, value: die Value} Das Patch-Objekt, das die neue Konfiguration enthält.
	 *
	 * @return void (bei PUT)
	 * @return object Clientconfig Json (bei GET)
	 *
	 * @responseCode 200
	 * @responseCode 400 Error Bei ungültigen Anfragen.
	 * @responseCode 500 Error Falls HTTP Methode weder GET noch PUT ist
	 */

	// Initialisierung
	require_once __DIR__.'/../../app/init.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "GET", "PUT" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Lehrer-Kennwort vorliegt
	$lehrer = $auth->pruefeLehrerBasicAuth();

	// Reagiere auf die Anfrage je nach HTTP-Methode
	if (strcmp($auth->getHTTPMethod(), "GET") === 0) {
		// Liefere die gesamte Konfiguration - benutzerspezifisch und global zurück
		echo $db->getClientConfig($lehrer->id);
		exit;
	} else if (strcmp($auth->getHTTPMethod(), "PUT") === 0) {
		// Setze den Eintrag bei der benutzerspezifischen Konfiguration
		$obj = Http::getBodyJsonObject();
		if (!property_exists($obj, "key") || !property_exists($obj, "value"))
			Http::exit400BadRequest("Fehlerhafte Anfrage: Es muss ein Schlüsselwert angegeben sein und ein Wert muss entweder gültig gesetzt oder explizit null für ein Entfernen des Eintrags sein.");
		$keytype = gettype($obj->key);
		if (strcmp($keytype, "string") !== 0)
			Http::exit400BadRequest("Fehlerhafte Anfrage: Der Schlüsselwert muss eine Zeichenkette sein.");
		$valuetype = gettype($obj->value);
		if ((strcmp($valuetype, "string") !== 0) && (strcmp($valuetype, "NULL") !== 0))
			Http::exit400BadRequest("Fehlerhafte Anfrage: Der Wert muss entweder eine Zeichenkette oder NULL sein.");
		$db->putClientUserConfig($lehrer->id, $obj->key, $obj->value);
		exit;
	} else {
		// Unbekannte HTTP-Methode - sollte nicht vorkommen...
		Http::exit500("Unerwartete HTTP-Methode.");
	}

?>
