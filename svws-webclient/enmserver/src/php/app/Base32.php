<?php

namespace wenom;

use wenom\Http;

/**
 * Diese Klasse stellt Methode für die Base32-Kodierung zur Verfügung
 */
class Base32 {

    /** Das Base-32-Alphabet (A-Z und 2-7) */
    private const ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    /**
     * Eine vordefinierte Tabelle für die Dekodierung. Hier werden
     * nur die ASCII-Codes erlaubt, da Base32 eine Teilmenge davon ist.
     */
    private const DECODE_TABLE = [
        'A' => 0, 'B' => 1, 'C' => 2, 'D' => 3, 'E' => 4, 'F' => 5, 'G' => 6, 'H' => 7,
        'I' => 8, 'J' => 9, 'K' => 10, 'L' => 11, 'M' => 12, 'N' => 13, 'O' => 14, 'P' => 15,
        'Q' => 16, 'R' => 17, 'S' => 18, 'T' => 19, 'U' => 20, 'V' => 21, 'W' => 22, 'X' => 23,
        'Y' => 24, 'Z' => 25, '2' => 26, '3' => 27, '4' => 28, '5' => 29, '6' => 30, '7' => 31,
        'a' => 0, 'b' => 1, 'c' => 2, 'd' => 3, 'e' => 4, 'f' => 5, 'g' => 6, 'h' => 7,
        'i' => 8, 'j' => 9, 'k' => 10, 'l' => 11, 'm' => 12, 'n' => 13, 'o' => 14, 'p' => 15,
        'q' => 16, 'r' => 17, 's' => 18, 't' => 19, 'u' => 20, 'v' => 21, 'w' => 22, 'x' => 23,
        'y' => 24, 'z' => 25
    ];

    /**
     * Kodiert das übergeben Byte-Array mit einer Base32-Kodierung.
     *
     * @param string data   die zu kodierenden Daten
     *
     * @return die Base32-kodierten Daten
     */
    public static function encode(?string $data): string {
        // Leere Daten werden als leerer String kodiert.
        if (($data === null) || ($data === "")) {
            return "";
        }

        // Nutze einen String als Buffer für den Aufbau der Base-32-Kodierung
        $sb = "";
        $buffer = 0;
        $bitsLeft = 0;

        // Durchwandere die Daten  und nutze Bit-Shifting für effiziente Kodierung
        $len = strlen($data);
        for ($i = 0; $i < $len; $i++) {
            $buffer = ($buffer << 8) | ord($data[$i]);
            $bitsLeft += 8;
            while ($bitsLeft >= 5) {
                $bitsLeft -= 5;
                $sb .= self::ALPHABET[($buffer >> $bitsLeft) & 0x1F];
            }
        }

        // Kodiere auch noch die letzte Bits, wenn welche übrig geblieben sind
        if ($bitsLeft > 0) {
            $sb .= self::ALPHABET[($buffer << (5 - $bitsLeft)) & 0x1F];
        }

        // Ergänze ggf. noch das Padding
        $padding = (8 - (strlen($sb) % 8)) % 8;
        return $sb.str_repeat('=', $padding);
    }

    /**
     * Dekodiert den übergebenen Base32-String.
     *
     * @param ?string base32   der zu dekodierende Base32-String
     *
     * @return string die dekodierten Daten
     */
    public static function decode(?string $base32): string {
        // Wenn der Base32-String leer ist, dann ist auch das resultierende Byte-Array leer
        if (empty($base32)) {
            return "";
        }

        // Nutze einen String als Buffer für das Dekodieren
        $base32 = rtrim($base32, '=');
        $result = "";
        $buffer = 0;
        $bitsLeft = 0;

        // Dekodiere den Input-String und nutze im Ziel Bit-Shifting für das effiziente Schreiben der dekodierten Daten
        foreach (str_split($base32) as $char) {
            if (!isset(self::DECODE_TABLE[$char])) {
                Http::exit400BadRequest("Fehler beim Dekodieren des Base32-Strings");
            }

            $buffer = ($buffer << 5) | self::DECODE_TABLE[$char];
            $bitsLeft += 5;

            if ($bitsLeft >= 8) {
                $bitsLeft -= 8;
                $result .= chr(($buffer >> $bitsLeft) & 0xFF);
            }
        }
        return $result;
    }

}
