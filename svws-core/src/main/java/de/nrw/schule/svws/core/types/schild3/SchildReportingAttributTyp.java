package de.nrw.schule.svws.core.types.schild3;

import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die unterschiedlichen Attribut-JSON-Datentypen,
 * welche in den Datenquellen vorkommen können.
 */
public enum SchildReportingAttributTyp {

    /** Boolean */
    BOOLEAN("boolean"),

    /** Ganzzahl */
    INT("integer"),

    /** Zahl, auch Kommazahlen */
    NUMBER("number"),

    /** Zeichenkette */
    STRING("string"),
    /** Mehrzeilige Zeichenkette */
    MEMO("memo"),
    
    /** Datumsangabe */
    DATE("date");


    /** Der JSON-Typ als String */
    private final @NotNull String type;


    /**
     * Initialisiert den Datentype für die Aufzählung
     * 
     * @param type   der JSON-Datentyp
     */
    private SchildReportingAttributTyp(final @NotNull String type) {
        this.type = type;
    }

    @Override
    public @NotNull String toString() {
        return this.type;
    }

}
