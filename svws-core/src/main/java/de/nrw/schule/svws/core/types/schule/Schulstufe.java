package de.nrw.schule.svws.core.types.schule;

import java.util.Arrays;
import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.SchulstufeKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Core-Type enthält die unterschiedlichen Schulstufen.
 * Siehe auch: 
 * - https://de.wikipedia.org/wiki/Bildungssystem_in_Deutschland
 * - https://www.schulministerium.nrw/schule-bildung/schulorganisation/schulformen
 */
public enum Schulstufe {

    /** Die Vorschulstufe */
    VORSCHULSTUFE(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(0, "V", "Vorschulstufe", Arrays.asList(
                Schulform.S, Schulform.KS
            ), null, null)
    }),

    /** Die Primarstufe */
    PRIMARSTUFE(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(1000, "P", "Primarstufe", Arrays.asList(
                Schulform.FW, Schulform.HI, Schulform.WF,
                Schulform.G, 
                Schulform.PS, 
                Schulform.R, 
                Schulform.S, Schulform.KS, 
                Schulform.V
            ), null, null)
    }),

    /** Die Sekundarstufe I */
    SEKUNDARSTUFE_I(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(2000, "SI", "Sekundarstufe I", Arrays.asList(
                Schulform.FW, Schulform.HI, Schulform.WF,
                Schulform.PS, 
                Schulform.S, Schulform.KS, 
                Schulform.GE, 
                Schulform.GM, 
                Schulform.GY, 
                Schulform.H, 
                Schulform.R, 
                Schulform.SG, 
                Schulform.SK, 
                Schulform.SR, 
                Schulform.V
            ), null, null)
    }),

    /** Die Sekundarstufe II */
    SEKUNDARSTUFE_II(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(3000, "SII", "Sekundarstufe II", Arrays.asList(
                Schulform.FW, Schulform.HI, Schulform.WF,
                Schulform.GE, 
                Schulform.GM, 
                Schulform.GY, 
                Schulform.PS, 
                Schulform.SG 
            ),null, null)
    }),

    /** Die Tertiärstufe */
    TERTIAERSTUFE(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(4000, "T", "Tertiärstufe", Arrays.asList(
            ), null, null)
    }),

    /** Die Quartärstufe */
    QUARTAERSTUFE(new SchulstufeKatalogEintrag[] {
        new SchulstufeKatalogEintrag(4000, "Q", "Quartärstufe", Arrays.asList(
                Schulform.WB
            ), null, null)
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
