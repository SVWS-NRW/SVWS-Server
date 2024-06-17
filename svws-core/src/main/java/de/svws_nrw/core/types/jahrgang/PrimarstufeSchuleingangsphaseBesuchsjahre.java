package de.svws_nrw.core.types.jahrgang;

import java.util.HashMap;

import de.svws_nrw.core.data.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Besuchsjahre in der Schuleingangsphase
 * von Schülern der Primarstufe zur Verfügung.
 *
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum PrimarstufeSchuleingangsphaseBesuchsjahre {

	/** E1: Das erste Besuchsjahr in der Schuleingangsphase */
	E1(new PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag[] {
			new PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag(1000L, "E1", "Schuleingangsphase, 1. Schulbesuchsjahr", null, null)
	}),


	/** E2: Das zweite Besuchsjahr in der Schuleingangsphase */
	E2(new PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag[] {
			new PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag(2000L, "E2", "Schuleingangsphase, 2. Schulbesuchsjahr", null, null)
	}),


	/** E3: Das dritte Besuchsjahr in der Schuleingangsphase */
	E3(new PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag[] {
			new PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag(3000L, "E3", "Schuleingangsphase, 3. Schulbesuchsjahr", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der Besuchsjahre, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Besuchsjahre */
	public final @NotNull PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag @NotNull [] historie;

	/** Eine Map mit der Zuordnung der Besuchsjahre zu dem Kürzel der Besuchsjahre */
	private static final @NotNull HashMap<@NotNull String, PrimarstufeSchuleingangsphaseBesuchsjahre> _mapKuerzel = new HashMap<>();

	/** Eine Map mit der Zuordnung der Besuchsjahre zu der ID der Besuchsjahre */
	private static final @NotNull HashMap<@NotNull Long, PrimarstufeSchuleingangsphaseBesuchsjahre> _mapID = new HashMap<>();


	/**
	 * Erzeugt einen neuen Eintrage für Besuchsjahre in der Aufzählung.
	 *
	 * @param historie   die Historie für die Besuchsjahre, welches ein Array von {@link PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag} ist
	 */
	PrimarstufeSchuleingangsphaseBesuchsjahre(final @NotNull PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag @NotNull [] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln der Besuchsjahre auf die zugehörigen Besuchsjahre
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzel der Besuchsjahre auf die zugehörigen Besuchsjahre
	 */
	private static @NotNull HashMap<@NotNull String, PrimarstufeSchuleingangsphaseBesuchsjahre> getMapJahrgangByKuerzel() {
		if (_mapKuerzel.size() == 0)
			for (final PrimarstufeSchuleingangsphaseBesuchsjahre j : PrimarstufeSchuleingangsphaseBesuchsjahre.values())
				_mapKuerzel.put(j.daten.kuerzel, j);
		return _mapKuerzel;
	}


	/**
	 * Gibt eine Map von den IDs der Besuchsjahre auf die zugehörigen Besuchsjahre
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Besuchsjahre auf die zugehörigen Besuchsjahre
	 */
	private static @NotNull HashMap<@NotNull Long, PrimarstufeSchuleingangsphaseBesuchsjahre> getMapJahrgangByID() {
		if (_mapID.size() == 0)
			for (final PrimarstufeSchuleingangsphaseBesuchsjahre j : PrimarstufeSchuleingangsphaseBesuchsjahre.values()) {
				for (final PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag k : j.historie)
					_mapID.put(k.id, j);
			}
		return _mapID;
	}


	/**
	 * Liefert die Besuchsjahre anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Besuchsjahre
	 *
	 * @return die Besuchsjahre oder null, falls das Kürzel ungültig ist
	 */
	public static PrimarstufeSchuleingangsphaseBesuchsjahre getByKuerzel(final String kuerzel) {
		return getMapJahrgangByKuerzel().get(kuerzel);
	}


	/**
	 * Liefert die Besuchsjahre anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID der Besuchsjahre
	 *
	 * @return die Besuchsjahre oder null, falls die ID ungültig ist
	 */
	public static PrimarstufeSchuleingangsphaseBesuchsjahre getByID(final Long id) {
		return getMapJahrgangByID().get(id);
	}

}
