package de.nrw.schule.svws.core.types.schule;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.SchulstufeKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Core-Type enthält die unterschiedlichen Schulstufen. 
 */
public enum Schulstufe {

    /** Die Vorschulstufe */
    VORSCHULSTUFE(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(0, "V", "Vorschulstufe", null, null)
    }),

    /** Die Primarstufe */
    PRIMARSTUFE(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(1000, "P", "Primarstufe", null, null)
    }),

    /** Die Sekundarstufe I */
    SEKUNDARSTUFE_I(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(2000, "SI", "Sekundarstufe I", null, null)
    }),

    /** Die Sekundarstufe II */
    SEKUNDARSTUFE_II(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(3000, "SII", "Sekundarstufe II", null, null)
    }),

    /** Die Tertiärstufe */
    TERTIAERSTUFE(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(4000, "T", "Tertiärstufe", null, null)
    });


    /** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
    public static long VERSION = 1;

    /** Der aktuellen Daten der Schulstufe, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
    public final @NotNull SchulstufeKatalogEintrag daten;

    /** Die Historie mit den Einträgen der Schulstufe */
    public final @NotNull SchulstufeKatalogEintrag@NotNull[] historie;

    /** Eine Hashmap mit allen definierten Schulstufe, zugeordnet zu ihren Kürzeln */ 
    private static final @NotNull HashMap<@NotNull String, @NotNull Schulstufe> _mapByKuerzel = new HashMap<>();


    /**
     * Erzeugt eine neue Schulstufe in der Aufzählung.
     * 
     * @param historie   die Historie der Schulstufe, welches ein Array von {@link SchulstufeKatalogEintrag} ist  
     */
    private Schulstufe(@NotNull SchulstufeKatalogEintrag@NotNull[] historie) {
        this.historie = historie;
        // TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
        this.daten = historie[historie.length - 1];
    }


    /**
     * Gibt eine Map von den Kürzeln der Schulstufen auf die zugehörigen Schulstufen
     * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
     *    
     * @return die Map von den Kürzeln der Schulstufen auf die zugehörigen Schulstufen
     */
    private static @NotNull HashMap<@NotNull String, @NotNull Schulstufe> getMapByKuerzel() {
        if (_mapByKuerzel.size() == 0) {
            for (Schulstufe s : Schulstufe.values()) {
                if (s.daten != null)
                    _mapByKuerzel.put(s.daten.kuerzel, s);
            }
        }
        return _mapByKuerzel;
    }


    /**
     * Gibt die Schulstufe für das angegebene Kürzel zurück.
     * 
     * @param kuerzel   das Kürzel der Schulstufe
     * 
     * @return die Schulstufe oder null, falls das Kürzel ungültig ist
     */
    public static Schulstufe getByKuerzel(String kuerzel) {
        return getMapByKuerzel().get(kuerzel);
    }

}
