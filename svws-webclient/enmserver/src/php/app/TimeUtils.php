<?php

namespace wenom;

use \DateTime as DateTime;
use \DateTimeZone as DateTimeZone;

/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf die Zeit des Servers zur Verfügung.
 */
class TimeUtils {

    /**
     * Gibt das aktuelle Datum als formattierten String in der Zeitzone "Europe/Berlin".
     *
     * @return string   das aktuelle Datum als String
     */
    public static function now(): string {
        $tz = new DateTimeZone('Europe/Berlin');
        $now = new DateTime('now', $tz);
        return $now->format('Y-m-d H:i:s.v');
    }

    /**
     * Gibt den aktuellen Zeitstempel in Sekunden seit dem 1.1.1970 (UTC) zurück.
     *
     * @return int die Anzahl an Sekunden
     */
    public static function timestamp(): int {
        return time();
    }

}
