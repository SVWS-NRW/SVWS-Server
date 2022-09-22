package de.nrw.schule.svws.core.types.statkue;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.ReligionKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik erhobenen Religionen.
 */
public enum Religion {

	/** Religion: alevitisch */
	AR(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(1000, "AR", "alevitisch", null, null)
	}),

	/** Religion: evangelisch */
	ER(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(2000, "ER", "evangelisch", null, null)
	}),

	/** Religion: jüdisch */
	HR(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(3000, "HR", "jüdisch", null, null)
	}),

	/** Religion: islamisch */
	IR(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(4000, "IR", "islamisch", null, null)
	}),

	/** Religion: katholisch */
	KR(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(5000, "KR", "katholisch", null, null)
	}),

	/** Religion: mennonitische BG NRW */
	ME(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(6000, "ME", "mennonitische BG NRW", null, null)
	}),

	/** Religion: ohne Bekenntnis */
	OH(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(7000, "OH", "ohne Bekenntnis", null, null)
	}),

	/** Religion: griechisch-orthodox */
	OR(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(8000, "OR", "griechisch-orthodox", null, null)
	}),

	/** Religion: syrisch-orthodox */
	SO(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(9000, "SO", "syrisch-orthodox", null, null)
	}),

	/** Religion: sonstige orthodoxe */
	XO(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(10000, "XO", "sonstige orthodoxe", null, null)
	}),
	
	/** Religion: andere Religionen */
	XR(new ReligionKatalogEintrag[] {
		new ReligionKatalogEintrag(11000, "XR", "andere Religionen", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	

	/** Der aktuellen Daten der Religionen */
	public final @NotNull ReligionKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Religionen */
	public final @NotNull ReligionKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Einschulungsarten, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull Religion> _mapKuerzel = new HashMap<>();


	/**
	 * Erzeugt eine neue Religion in der Aufzählung.
	 * 
	 * @param historie   die Historie der Religionen, welche ein Array von 
	 *                   {@link ReligionKatalogEintrag} ist  
	 */
	private Religion(@NotNull ReligionKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Religionen auf die 
	 * zugehörigen Religionen zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen Religionen
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Religion> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0) {
			for (Religion s : Religion.values()) {
				if (s.daten != null)
					_mapKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return _mapKuerzel;
	}


	/**
	 * Gibt die Religion für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Religion
	 * 
	 * @return die Religion oder null, falls das Kürzel ungültig ist
	 */
	public static Religion getByKuerzel(String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}
	
}
