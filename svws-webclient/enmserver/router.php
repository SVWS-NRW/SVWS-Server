<?php
	// Für den php-internen Webserver (bei Tests):
	// Prüfe bei URLs ohne ".php"-Erweiterung die Umleitung der Anfragen auf URLs mit ".php"-Erweiterung
	$script = $_SERVER["REQUEST_URI"];
	$path = pathinfo($script);
	if (!empty($path["extension"]))
		return false;
	$php = $_SERVER["DOCUMENT_ROOT"].$script.".php";
	if (!file_exists($php))
		return false;
	include($php);
?>
