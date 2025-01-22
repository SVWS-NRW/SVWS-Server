<?php

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
