package de.nrw.schule.svws.core.types.schule;

import java.util.Arrays;
import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.OrganisationsformKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen 
 * Organisationsformen bei allgemeinbildenden Schulformen.
 */
public enum AllgemeinbildendOrganisationsformen {

	/** Organisationsform: Nicht zuordbar (Früherziehung für Hör- und Sehgeschädigte, Ambulante Maßnahmen) */
	NICHT_ZUGEORDNET(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(3000000, "*", "Nicht zuordbar (Früherziehung für Hör- und Sehgeschädigte, Ambulante Maßnahmen)", Arrays.asList(
			Schulform.S, Schulform.KS
		), null, null)
	}),

	/** Organisationsform: Halbtagsunterricht */
	HALBTAG(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(3001000, "1", "Halbtagsunterricht", Arrays.asList(
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G,
			Schulform.GE,
			Schulform.GM,
			Schulform.GY,
			Schulform.H,
			Schulform.PS,
			Schulform.R,
			Schulform.S, Schulform.KS,
			Schulform.SG,
			Schulform.SK,
			Schulform.SR,
			Schulform.V
		), null, null)
	}),

	/** Organisationsform: Teilnahme am gebundenen Ganztag */
	GANZTAG(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(3002000, "2", "Teilnahme am gebundenen Ganztag", Arrays.asList(
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G,
			Schulform.GE,
			Schulform.GM,
			Schulform.GY,
			Schulform.H,
			Schulform.PS,
			Schulform.R,
			Schulform.S, Schulform.KS,
			Schulform.SG,
			Schulform.SK,
			Schulform.SR,
			Schulform.V
		), null, null)
	}),

	/** Organisationsform: Teilnahme am erweiterten Ganztag */
	GANZTAG_ERWEITERT(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(3003000, "3", "Teilnahme am erweiterten Ganztag", Arrays.asList(
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.H,
			Schulform.R,
			Schulform.S, Schulform.KS,
			Schulform.SK
		), null, null)
	}),

	/** Organisationsform: Teilnahme am offenen Ganztag */
	GANZTAG_OFFEN(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(3004000, "4", "Teilnahme am offenen Ganztag", Arrays.asList(
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G,
			Schulform.PS,
			Schulform.S, Schulform.KS
		), null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	

	/** Der aktuellen Daten der Organisationsform */
	public final @NotNull OrganisationsformKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Organisationsform */
	public final @NotNull OrganisationsformKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Organisationsformen, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull AllgemeinbildendOrganisationsformen> _mapKuerzel = new HashMap<>();


	/**
	 * Erzeugt eine neue Organisationsform in der Aufzählung.
	 * 
	 * @param historie   die Historie der Organisationsform, welche ein Array von 
	 *                   {@link OrganisationsformKatalogEintrag} ist  
	 */
	private AllgemeinbildendOrganisationsformen(@NotNull OrganisationsformKatalogEintrag@NotNull[] historie) {
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
	private static @NotNull HashMap<@NotNull String, @NotNull AllgemeinbildendOrganisationsformen> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0) {
			for (AllgemeinbildendOrganisationsformen s : AllgemeinbildendOrganisationsformen.values()) {
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
	public static AllgemeinbildendOrganisationsformen getByKuerzel(String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}
	
}
