package de.nrw.schule.svws.core.types.schule;

import java.util.Arrays;
import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.OrganisationsformKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen 
 * Organisationsformen an Weiterbildungskollegs.
 */
public enum WeiterbildungskollegOrganisationsformen {

	/** Organisationsform: Teilbeleger */
	TEILZEIT(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(2001000, "T", "Teilbeleger", Arrays.asList(Schulform.WB), null, null)
	}),

	/** Organisationsform: Vollbeleger */
	VOLLZEIT(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(2002000, "V", "Vollbeleger", Arrays.asList(Schulform.WB), null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	

	/** Der aktuellen Daten der Organisationsform */
	public final @NotNull OrganisationsformKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Organisationsform */
	public final @NotNull OrganisationsformKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Organisationsformen, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull WeiterbildungskollegOrganisationsformen> _mapKuerzel = new HashMap<>();


	/**
	 * Erzeugt eine neue Organisationsform in der Aufzählung.
	 * 
	 * @param historie   die Historie der Organisationsform, welche ein Array von 
	 *                   {@link OrganisationsformKatalogEintrag} ist  
	 */
	private WeiterbildungskollegOrganisationsformen(final @NotNull OrganisationsformKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Organisationsformen auf die 
	 * zugehörigen Organisationsformen zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen Organisationsformen
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull WeiterbildungskollegOrganisationsformen> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0) {
			for (final WeiterbildungskollegOrganisationsformen s : WeiterbildungskollegOrganisationsformen.values()) {
				if (s.daten != null)
					_mapKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return _mapKuerzel;
	}


	/**
	 * Gibt die Organisationsform für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Organisationsform
	 * 
	 * @return die Organisationsform oder null, falls das Kürzel ungültig ist
	 */
	public static WeiterbildungskollegOrganisationsformen getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}
	
}
