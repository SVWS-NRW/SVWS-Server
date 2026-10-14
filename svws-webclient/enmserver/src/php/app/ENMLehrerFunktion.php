<?php

namespace wenom;

enum ENMLehrerFunktion: int {
    /** Fachlehrer */
    case FACHLEHRER = 1;

    /** Klassenleitung */
    case KLASSENLEITUNG = 2;

    /** Abteilungsleitung */
    case ABTEILUNGSLEITUNG = 3;

    /** Schulleitung */
    case SCHULLEITUNG = 4;

    /** Notenmoduladministrator */
    case NOTENMODULADMIN = 5;

    /**
     * Gibt die lesbare Bezeichnung der Funktion zurück
     */
    public function text(): string {
        return match($this) {
            self::FACHLEHRER        => 'Fachlehrer/-in',
            self::KLASSENLEITUNG    => 'Klassenleitung',
            self::ABTEILUNGSLEITUNG => 'Abteilungsleitung',
            self::SCHULLEITUNG      => 'Schulleitung',
            self::NOTENMODULADMIN   => 'Notenmoduladministrator',
        };
    }
}
