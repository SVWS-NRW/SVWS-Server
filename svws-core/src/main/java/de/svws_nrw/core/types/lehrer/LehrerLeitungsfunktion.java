package de.svws_nrw.core.types.lehrer;

import java.util.HashMap;

import de.svws_nrw.core.data.lehrer.LehrerKatalogLeitungsfunktionenEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Core-Type enthält die unterschiedlichen Leitungsfunktionen von Lehrern.
 */
public enum LehrerLeitungsfunktion {

    /** Schulleitung */
    SL(new LehrerKatalogLeitungsfunktionenEintrag[] {
        new LehrerKatalogLeitungsfunktionenEintrag(1, "SL", "Schulleitung", null, null)
    }),

    /** Stellvertretende Schulleitung */
    SL_STV(new LehrerKatalogLeitungsfunktionenEintrag[] {
        new LehrerKatalogLeitungsfunktionenEintrag(2, "Stv. SL", "Stellvertretende Schulleitung", null, null)
    });


    /** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
    public static final long VERSION = 1;

    /** Der aktuellen Daten der Leitungsfunktionen von Lehrern */
    public final @NotNull LehrerKatalogLeitungsfunktionenEintrag daten;

    /** Die Historie mit den Einträgen der Leitungsfunktionen von Lehrern */
    public final @NotNull LehrerKatalogLeitungsfunktionenEintrag@NotNull[] historie;

    /** Eine Hashmap mit allen definierten Leitungsfunktion, zugeordnet zu ihren Kürzeln */
    private static final @NotNull HashMap<@NotNull String, LehrerLeitungsfunktion> _mapByKuerzel = new HashMap<>();

    /** Eine Hashmap mit allen definierten Leitungsfunktion, zugeordnet zu ihren IDs */
    private static final @NotNull HashMap<@NotNull Long, LehrerLeitungsfunktion> _mapByID = new HashMap<>();


    /**
     * Erzeugt eine neue Leitungsfunktion in der Aufzählung.
     *
     * @param historie   die Historie der Leitungsfunktion, welches ein Array von {@link LehrerKatalogLeitungsfunktionenEintrag} ist
     */
    LehrerLeitungsfunktion(final @NotNull LehrerKatalogLeitungsfunktionenEintrag@NotNull[] historie) {
        this.historie = historie;
        // TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
        this.daten = historie[historie.length - 1];
    }


    /**
     * Gibt eine Map von den Kürzeln der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
     * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
     *
     * @return die Map von den Kürzeln der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
     */
    private static @NotNull HashMap<@NotNull String, LehrerLeitungsfunktion> getMapByKuerzel() {
        if (_mapByKuerzel.size() == 0) {
            for (final LehrerLeitungsfunktion s : LehrerLeitungsfunktion.values()) {
                if (s.daten != null)
                    _mapByKuerzel.put(s.daten.kuerzel, s);
            }
        }
        return _mapByKuerzel;
    }


    /**
     * Gibt eine Map von den IDs der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
     * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
     *
     * @return die Map von den IDs der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
     */
    private static @NotNull HashMap<@NotNull Long, LehrerLeitungsfunktion> getMapByID() {
        if (_mapByID.size() == 0) {
            for (final LehrerLeitungsfunktion s : LehrerLeitungsfunktion.values()) {
                if (s.daten != null)
                    _mapByID.put(s.daten.id, s);
            }
        }
        return _mapByID;
    }


    /**
     * Gibt die Leitungsfunktion für das angegebene Kürzel zurück.
     *
     * @param kuerzel   das Kürzel der Leitungsfunktion
     *
     * @return die Leitungsfunktion oder null, falls das Kürzel ungültig ist
     */
    public static LehrerLeitungsfunktion getByKuerzel(final String kuerzel) {
        return getMapByKuerzel().get(kuerzel);
    }


    /**
     * Gibt die Leitungsfunktion für die angegebene ID zurück.
     *
     * @param id   die ID der Leitungsfunktion
     *
     * @return die Leitungsfunktion oder null, falls die ID ungültig ist
     */
    public static LehrerLeitungsfunktion getByID(final long id) {
        return getMapByID().get(id);
    }

}
