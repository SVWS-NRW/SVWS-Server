<?php

	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';

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

