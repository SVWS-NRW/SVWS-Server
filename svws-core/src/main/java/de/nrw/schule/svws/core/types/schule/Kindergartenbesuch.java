package de.nrw.schule.svws.core.types.schule;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.KindergartenbesuchKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die Dauer des Kindergartenbesuchs.
 */
public enum Kindergartenbesuch {

	/** Kein Kindergartenbesuch */
	KEINER(new KindergartenbesuchKatalogEintrag[] {
		new KindergartenbesuchKatalogEintrag(1, 0, "kein Kindergarten", null, null)
	}),

	/** Kindergartenbesuch unter einem Jahr */
	MAX_1_JAHR(new KindergartenbesuchKatalogEintrag[] {
		new KindergartenbesuchKatalogEintrag(2, 1, "unter 1 Jahr", null, null)
	}),

    /** Kindergartenbesuch unter einem Jahr */
    MAX_2_JAHRE(new KindergartenbesuchKatalogEintrag[] {
        new KindergartenbesuchKatalogEintrag(3, 2, "1 bis unter 2 Jahre", null, null)
    }),

    /** Kindergartenbesuch unter einem Jahr */
    MAX_3_JAHRE(new KindergartenbesuchKatalogEintrag[] {
        new KindergartenbesuchKatalogEintrag(4, 3, "2 bis unter 3 Jahre", null, null)
    }),

    /** Kindergartenbesuch unter einem Jahr */
    MIN_3_JAHRE(new KindergartenbesuchKatalogEintrag[] {
        new KindergartenbesuchKatalogEintrag(5, 4, "3 Jahre und mehr Jahre", null, null)
    });

	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	

	/** Der aktuellen Daten der Dauer des Kindergartenbesuchs */
	public final @NotNull KindergartenbesuchKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Dauer des Kindergartenbesuchs */
	public final @NotNull KindergartenbesuchKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Dauern des Kindergartenbesuchs, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull Long, Kindergartenbesuch> _mapKuerzel = new HashMap<>();


	/**
	 * Erzeugt einen neuen Eintrag in der Aufzählung.
	 * 
	 * @param historie   die Historie der Eintrags, welche ein Array von 
	 *                   {@link KindergartenbesuchKatalogEintrag} ist  
	 */
	private Kindergartenbesuch(@NotNull KindergartenbesuchKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln auf den zugehörigen Core-Type-Wert. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf den zugehörigen Core-Type-Wert
	 */
	private static @NotNull HashMap<@NotNull Long, Kindergartenbesuch> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0) {
			for (Kindergartenbesuch s : Kindergartenbesuch.values()) {
				if (s.daten != null)
					_mapKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return _mapKuerzel;
	}


	/**
	 * Gibt den Core-Type-Wert für das angegebe Kürzel der Dauer des Kindergartenbesuchs zurück.
	 * 
	 * @param kuerzel   das Kürzel der Dauer
	 * 
	 * @return der Core-Type-Wert oder null, falls das Kürzel ungültig ist
	 */
	public static Kindergartenbesuch getByKuerzel(long kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}

}
