package de.svws_nrw.core.types.schule;

import java.util.Arrays;
import java.util.HashMap;

import de.svws_nrw.core.data.schule.OrganisationsformKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen
 * Organisationsformen an Berufskollegs.
 */
public enum BerufskollegOrganisationsformen {

	/** Organisationsform: Teilzeitunterricht (außerhalb der TZ-Berufsschule) */
	TEILZEIT(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1001000, "T", "Teilzeitunterricht (außerhalb der TZ-Berufsschule)", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Teilzeitunterricht ohne Blockunterricht (Normalklasse) TZ-Berufsschule */
	TEILZEIT_NORMALKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1002000, "A", "Teilzeitunterricht ohne Blockunterricht (Normalklasse) TZ-Berufsschule", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Teilzeitunterricht ohne Blockunterricht (Bezirksfachklasse) TZ-Berufsschule */
	TEILZEIT_BEZIRKSFACHKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1003000, "C", "Teilzeitunterricht ohne Blockunterricht (Bezirksfachklasse) TZ-Berufsschule", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Teilzeitunterricht ohne Blockunterricht (Landesfachklasse) TZ-Berufsschule */
	TEILZEIT_LANDESFACHKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1004000, "I", "Teilzeitunterricht ohne Blockunterricht (Landesfachklasse) TZ-Berufsschule", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Vollzeitunterricht */
	VOLLZEIT(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1005000, "V", "Vollzeitunterricht", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Blockunterricht z.Zt. im Unterricht (Normalklasse) im dualen System */
	DUAL_BLOCK_IM_UNTERRICHT(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1006000, "B", "Blockunterricht z.Zt. im Unterricht (Normalklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Blockunterricht z.Zt. nicht im Unterricht (Normalklasse) im dualen System */
	DUAL_BLOCK_NICHT_IM_UNTERRICHT(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1007000, "E", "Blockunterricht z.Zt. nicht im Unterricht (Normalklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Normalklasse) im dualen System */
	DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1008000, "G", "Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Normalklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Ganztagsunterricht (Normalklasse) im dualen System */
	DUAL_GANZTAG(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1000, "K", "Ganztagsunterricht (Normalklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Blockunterricht z.Zt. im Unterricht (Bezirksfachklasse) im dualen System */
	DUAL_BLOCK_IM_UNTERRICHT_BEZIRKSFACHKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1009000, "D", "Blockunterricht z.Zt. im Unterricht (Bezirksfachklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Blockunterricht z.Zt. nicht im Unterricht (Bezirksfachklasse) im dualen System */
	DUAL_BLOCK_NICHT_IM_UNTERRICHT_BEZIRKSFACHKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1010000, "F", "Blockunterricht z.Zt. nicht im Unterricht (Bezirksfachklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Bezirksfachklasse) im dualen System */
	DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT_BEZIRKSFACHKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1011000, "H", "Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Bezirksfachklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Ganztagsunterricht (Bezirksfachklasse) im dualen System */
	DUAL_GANZTAG_BEZIRKSFACHKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1012000, "L", "Ganztagsunterricht (Bezirksfachklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Blockunterricht z.Zt. im Unterricht (Landesfachklasse) im dualen System */
	DUAL_BLOCK_IM_UNTERRICHT_LANDESFACHKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1013000, "O", "Blockunterricht z.Zt. im Unterricht (Landesfachklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Blockunterricht z.Zt. nicht im Unterricht (Landesfachklasse) im dualen System */
	DUAL_BLOCK_NICHT_IM_UNTERRICHT_LANDESFACHKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1014000, "P", "Blockunterricht z.Zt. nicht im Unterricht (Landesfachklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Landesfachklasse) im dualen System */
	DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT_LANDESFACHKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1015000, "U", "Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Landesfachklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	}),

	/** Organisationsform: Ganztagsunterricht (Landesfachklasse) im dualen System */
	DUAL_GANZTAG_LANDESFACHKLASSE(new OrganisationsformKatalogEintrag[] {
		new OrganisationsformKatalogEintrag(1016000, "M", "Ganztagsunterricht (Landesfachklasse) im dualen System", Arrays.asList(
			Schulform.BK, Schulform.SB
		), null, null)
	});




	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der Organisationsform */
	public final @NotNull OrganisationsformKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Organisationsform */
	public final @NotNull OrganisationsformKatalogEintrag@NotNull[] historie;

	/** Eine Hashmap mit allen definierten Organisationsformen, zugeordnet zu ihren IDs */
	private static final @NotNull HashMap<@NotNull Long, @NotNull BerufskollegOrganisationsformen> _mapByID = new HashMap<>();

	/** Eine Hashmap mit allen definierten Organisationsformen, zugeordnet zu ihren Kürzeln */
	private static final @NotNull HashMap<@NotNull String, @NotNull BerufskollegOrganisationsformen> _mapKuerzel = new HashMap<>();


	/**
	 * Erzeugt eine neue Organisationsform in der Aufzählung.
	 *
	 * @param historie   die Historie der Organisationsform, welche ein Array von
	 *                   {@link OrganisationsformKatalogEintrag} ist
	 */
	BerufskollegOrganisationsformen(final @NotNull OrganisationsformKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Organisationsformen auf die
	 * zugehörigen Organisationsformen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs auf die zugehörigen Organisationsformen
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull BerufskollegOrganisationsformen> getMapByID() {
		if (_mapByID.size() == 0)
			for (final BerufskollegOrganisationsformen s : BerufskollegOrganisationsformen.values())
				for (final OrganisationsformKatalogEintrag k : s.historie)
					_mapByID.put(k.id, s);
		return _mapByID;
	}


	/**
	 * Gibt eine Map von den Kürzeln der Organisationsformen auf die
	 * zugehörigen Organisationsformen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Organisationsformen
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull BerufskollegOrganisationsformen> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0) {
			for (final BerufskollegOrganisationsformen s : BerufskollegOrganisationsformen.values()) {
				if (s.daten != null)
					_mapKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return _mapKuerzel;
	}


	/**
	 * Gibt die Organisationsform für die angegebene ID zurück.
	 *
	 * @param id   die ID der Organisationsform
	 *
	 * @return die Organisationsform oder null, falls die ID ungültig ist
	 */
	public static BerufskollegOrganisationsformen getByID(final Long id) {
		if (id == null)
			return null;
		return getMapByID().get(id);
	}


	/**
	 * Gibt die Organisationsform für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Organisationsform
	 *
	 * @return die Organisationsform oder null, falls das Kürzel ungültig ist
	 */
	public static BerufskollegOrganisationsformen getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}

}
