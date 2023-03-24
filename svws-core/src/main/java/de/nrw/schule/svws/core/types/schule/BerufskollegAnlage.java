package de.nrw.schule.svws.core.types.schule;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.BerufskollegAnlageKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Arten von Anlagen am Berufskolleg.
 */
public enum BerufskollegAnlage {

	/** Anlage A: Fachklassen duales System und Ausbildungsvorbereitung */
	A(new BerufskollegAnlageKatalogEintrag[] {
		new BerufskollegAnlageKatalogEintrag(1000, "A", "Fachklassen duales System und Ausbildungsvorbereitung", null, null)
	}),

	/** Anlage B: Berufsfachschule */
	B(new BerufskollegAnlageKatalogEintrag[] {
		new BerufskollegAnlageKatalogEintrag(2000, "B", "Berufsfachschule", null, null)
	}),

	/** Anlage C: Berufsfachschule und Fachoberschule */
	C(new BerufskollegAnlageKatalogEintrag[] {
		new BerufskollegAnlageKatalogEintrag(3000, "C", "Berufsfachschule und Fachoberschule", null, null)
	}),
	
	/** Anlage D: Berufliches Gymnasium und Fachoberschule */
	D(new BerufskollegAnlageKatalogEintrag[] {
		new BerufskollegAnlageKatalogEintrag(4000, "D", "Berufliches Gymnasium und Fachoberschule", null, null)
	}),

	/** Anlage E: Fachschule */
	E(new BerufskollegAnlageKatalogEintrag[] {
		new BerufskollegAnlageKatalogEintrag(5000, "E", "Fachschule", null, null)
	}),

	/** Anlage H: Berufsgrundbildung und Berufsausbildung an einer freien Waldorfschule / Hiberniakolleg */
	H(new BerufskollegAnlageKatalogEintrag[] {
		new BerufskollegAnlageKatalogEintrag(6000, "H", "Bildungsgänge an freien Waldorfschulen / Hiberniakolleg", null, null)
	}),

	/** Anlage X: Ehemalige Kollegschule */
	X(new BerufskollegAnlageKatalogEintrag[] {
		new BerufskollegAnlageKatalogEintrag(24000, "X", "Ehemalige Kollegschule", null, null)
	}),

	/** Anlage Z: TODO */
	Z(new BerufskollegAnlageKatalogEintrag[] {
		new BerufskollegAnlageKatalogEintrag(26000, "Z", "Kooperationsklasse Hauptschule", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Anlage, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull BerufskollegAnlageKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Anlage*/
	public final @NotNull BerufskollegAnlageKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Anlagen, zugeordnet zu ihren Kürzeln*/ 
	private static final @NotNull HashMap<@NotNull String, @NotNull BerufskollegAnlage> _anlagen = new HashMap<>();
	

	/**
	 * Erzeugt eine neue Anlage in der Aufzählung.
	 * 
	 * @param historie   die Historie der Anlage, welches ein Array von {@link BerufskollegAnlageKatalogEintrag} ist  
	 */
	private BerufskollegAnlage(final @NotNull BerufskollegAnlageKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}
	
	
	/**
	 * Gibt eine Map von den Kürzels der Anlagen auf die zugehörigen Anlagenarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Anlagen auf die zugehörigen Anlagen
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull BerufskollegAnlage> getMapAnlageByKuerzel() {
		if (_anlagen.size() == 0) {
			for (final BerufskollegAnlage s : BerufskollegAnlage.values()) {
				if (s.daten != null)
					_anlagen.put(s.daten.kuerzel, s);
			}
		}
		return _anlagen;
	}


	/**
	 * Gibt die Anlage für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Anlage
	 * 
	 * @return die Anlage oder null, falls das Kürzel ungültig ist
	 */
	public static BerufskollegAnlage getByKuerzel(final String kuerzel) {
		return getMapAnlageByKuerzel().get(kuerzel);
	}

}
