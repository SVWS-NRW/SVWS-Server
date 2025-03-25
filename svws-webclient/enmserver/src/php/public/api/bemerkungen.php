<?php
	/**
	 * Endpunkt zum Aktualisieren von Schülerbemerkungen.
	 *
	 * Dieser Endpunkt ermöglicht es autorisierten Lehrern, Bemerkungen zu Schülern über einen PATCH-Request zu aktualisieren.
	 *
	 * @httpMethod POST
	 * @auth (Basic) Lehrer Username und Kennwort benötigt
	 * @param int $id Die ID des Schülers, dessen Bemerkungen aktualisiert werden sollen.
	 * @param {id: number, patch: {Partial<ENMAnkreuzkompetenzen>}} Das Patch-Objekt, das die zu aktualisierenden Bemerkungen enthält.
	 * Folgende Werte können durch das Patch Objekt überschrieben werden: ASV, AUE, ZB, LELS, schulformEmpf, individuelleVersetzungsbemerkungen, foerderbemerkungen
	 *
	 * @return void
	 * @responseCode 200
 	 */

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/ENMDatenManager.php';

	// Prüfe die HTTP-Methode
	$auth->pruefeHTTPMethod([ "POST" ]);

	// Prüfe, ob eine Authentifizierung mit einem gültigen Lehrer-Kennwort vorliegt
	$lehrer = $auth->pruefeLehrerBasicAuth();

	// Erstelle für die Durchführung ein ENMDaten-Objekt aus der Datenbank und rufe dieses auf
	$enmDatenManager = ENMDatenManager::createFromDatabase($db);

	// Lese den Patch aus dem Request ein, hier liegt die ID als Attribut 'id' vor und der eigentlich Patch als Attribut 'patch'
	$patch = Http::getBodyJsonObject();
	$enmDatenManager->patchENMSchuelerBemerkungen($db, $lehrer, $patch->id, $patch->patch);

?>
