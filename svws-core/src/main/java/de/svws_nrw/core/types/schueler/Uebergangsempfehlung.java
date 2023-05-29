package de.svws_nrw.core.types.schueler;

import java.util.HashMap;

import de.svws_nrw.core.data.schueler.UebergangsempfehlungKatalogEintrag;
import de.svws_nrw.core.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die Übergangsempfehlung eines Schüler nach der 4. Klasse in die
 * 5. Klasse der Sekundarstufe I.
 */
public enum Uebergangsempfehlung {

	/** Übergangsempfehlung Hauptschule */
	HAUPTSCHULE(new UebergangsempfehlungKatalogEintrag[] {
		new UebergangsempfehlungKatalogEintrag(1, "H", "Hauptschule",
		    Schulform.H, null, null, null)
	}),

    /** Übergangsempfehlung Hauptschule / Realschule (eingeschränkt) */
    HAUPTSCHULE_REALSCHULE(new UebergangsempfehlungKatalogEintrag[] {
        new UebergangsempfehlungKatalogEintrag(5, "H/R", "Hauptschule / Realschule (eingeschränkt)",
            Schulform.H, Schulform.R, null, null)
    }),

    /** Übergangsempfehlung Realschule */
    REALSCHULE(new UebergangsempfehlungKatalogEintrag[] {
        new UebergangsempfehlungKatalogEintrag(2, "R", "Realschule",
            Schulform.R, null, null, null)
    }),

    /** Übergangsempfehlung Realschule / Gymnasium (eingeschränkt) */
    REALSCHULE_GYMNASIUM(new UebergangsempfehlungKatalogEintrag[] {
        new UebergangsempfehlungKatalogEintrag(6, "R/GY", "Realschule / Gymnasium (eingeschränkt)",
            Schulform.R, Schulform.GY, null, null)
    }),

    /** Übergangsempfehlung Gymnasium */
    GYMNASIUM(new UebergangsempfehlungKatalogEintrag[] {
        new UebergangsempfehlungKatalogEintrag(3, "GY", "Gymnasium",
            Schulform.GY, null, null, null)
    }),

    /** Keine Übergangsempfehlung */
    KEINE(new UebergangsempfehlungKatalogEintrag[] {
        new UebergangsempfehlungKatalogEintrag(4, "OHNE", "Keine Empfehlung",
            null, null, null, null)
    });


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der Übergangsempfehlung */
	public final @NotNull UebergangsempfehlungKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Übergangsempfehlung */
	public final @NotNull UebergangsempfehlungKatalogEintrag@NotNull[] historie;

	/** Eine Hashmap mit allen definierten Übergangsempfehlungen, zugeordnet zu ihren Kürzeln */
	private static final @NotNull HashMap<@NotNull String, Uebergangsempfehlung> _mapKuerzel = new HashMap<>();


	/**
	 * Erzeugt einen neuen Eintrag in der Aufzählung.
	 *
	 * @param historie   die Historie der Eintrags, welche ein Array von
	 *                   {@link UebergangsempfehlungKatalogEintrag} ist
	 */
	Uebergangsempfehlung(final @NotNull UebergangsempfehlungKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln auf den zugehörigen Core-Type-Wert.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf den zugehörigen Core-Type-Wert
	 */
	private static @NotNull HashMap<@NotNull String, Uebergangsempfehlung> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0) {
			for (final Uebergangsempfehlung s : Uebergangsempfehlung.values()) {
				if (s.daten != null)
					_mapKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return _mapKuerzel;
	}


	/**
	 * Gibt den Core-Type-Wert für das angegebene Kürzel der Übergangsempfehlung zurück.
	 *
	 * @param kuerzel   das Kürzel
	 *
	 * @return die Übergangsempfehlung oder null, falls das Kürzel ungültig ist
	 */
	public static Uebergangsempfehlung getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}

}
