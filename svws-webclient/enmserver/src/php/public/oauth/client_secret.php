<?php

	// Initialisierung
	require_once $_SERVER['DOCUMENT_ROOT'].'/../app/init.php';

	// Prüfe die HTTP-Methode ...
	$auth->pruefeHTTPMethod([ "GET" ]);

	// ... und, ob eine Authentifizierung mit Basic-Auth des Admin-Benutzers vorliegt,
	$auth->pruefeAdminBasicAuth($config);

	// ... und gib das Client-Secret zurück.
	header('Content-Type: text/plain; charset=utf-8');
	echo $db->getClientSecret(1);

?>
