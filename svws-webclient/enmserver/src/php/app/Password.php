<?php

declare(strict_types=1);

namespace wenom;

/**
 * Eine Utility-Klasse mit Methoden rund um Passwörter
 */
class Password {

    /**
     * Der Pool an Zeichen, welche vom Passwort-Generator verwendet wird. Ziel sind leichte Kennwörter,
     * wo eine gute Entropie über eine Länge des Kennwortes erreicht wird. Es werden 54 unterschiedliche Zeichen
     * verwendet.
     * - 22 Klein-
     * - 24 Großbuchstaben und
     * - 8 Zahlen
     * Verwechselbare Zeichen wie l, I, 1, 0, O werden nicht verwendet
     */
    private const POOL = 'abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789';

    /**
     * Erzeugt ein Passwort mit der übergebenen Länge.
     * Eine Länge von 16 Zeichen, ergibt mit der Pool-Größe
     * von 54 Zeichen eine Entropie von ~92 Bit.
     */
    public static function generate(int $length = 16): string {
        $pool = self::POOL;
        $maxIndex = strlen($pool) - 1;
        $password = '';

        for ($i = 0; $i < $length; $i++) {
            $password .= $pool[random_int(0, $maxIndex)];
        }

        return $password;
    }

}
