<?php
	/**
	 * Endpunkt für einen einfachen is Alive Check (No Content).
	 *
	 * Dieser Endpunkt gibt keine Daten zurück, sondern signalisiert lediglich, dass die Anfrage erfolgreich verarbeitet wurde.
	 *
	 * @httpMethod GET
	 *
	 * @return void
	 * @responseCode 200
	 */

	http_response_code(204);
?>
