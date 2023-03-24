package de.nrw.schule.svws.core.types.schule;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.BildungsgangTypKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den (Schul-)Typ des Bildungsgangs am Weiterbildungskolleg.
 */
public enum WeiterbildungskollegBildungsgangTyp {

	/** Abendgymnasium */
	ABENDGYMNASIUM(new BildungsgangTypKatalogEintrag[] {
		new BildungsgangTypKatalogEintrag(1000, "AG", "Abendgymnasium", null, null)
	}),

	/** Abendrealschule */
	ABENDREALSCHULE(new BildungsgangTypKatalogEintrag[] {
		new BildungsgangTypKatalogEintrag(2000, "AR", "Abendrealschule", null, null)
	}),

	/** Kolleg */
	KOLLEG(new BildungsgangTypKatalogEintrag[] {
		new BildungsgangTypKatalogEintrag(3000, "KL", "Kolleg", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Typen von Bildungsgängen, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull BildungsgangTypKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Typen von Bildungsgängen */
	public final @NotNull BildungsgangTypKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Typen von Bildungsgängen, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull WeiterbildungskollegBildungsgangTyp> _ebenen = new HashMap<>();


	/**
	 * Erzeugt einen neuen Typ von Bildungsgängen in der Aufzählung.
	 * 
	 * @param historie   die Historie der Typen von Bildungsgängen, welches ein Array von 
	 *                   {@link BildungsgangTypKatalogEintrag} ist  
	 */
	private WeiterbildungskollegBildungsgangTyp(final @NotNull BildungsgangTypKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln der Typen von Bildungsgängen auf die 
	 * zugehörigen Typen von Bildungsgängen zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Typen auf die zugehörigen Typen von Bildungsgängen
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull WeiterbildungskollegBildungsgangTyp> getMapByKuerzel() {
		if (_ebenen.size() == 0) {
			for (final WeiterbildungskollegBildungsgangTyp s : WeiterbildungskollegBildungsgangTyp.values()) {
				if (s.daten != null)
					_ebenen.put(s.daten.kuerzel, s);
			}
		}
		return _ebenen;
	}


	/**
	 * Gibt den Typ von Bildungsgängen für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel des Typs von Bildungsgängen
	 * 
	 * @return der Typ von Bildungsgängen oder null, falls das Kürzel ungültig ist
	 */
	public static WeiterbildungskollegBildungsgangTyp getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}

}
