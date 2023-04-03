package de.svws_nrw.core.types.schule;

import java.util.HashMap;

import de.svws_nrw.core.data.schule.BerufskollegBerufsebeneKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Berufsebenen der Ebene 1 am Berufskolleg.
 */
public enum BerufskollegBerufsebene1 {

	/** Berufsebene 1 : Agrarwirtschaft */
	AW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100001000, 1, "AW", "Agrarwirtschaft", null, null)
	}),

	/** Berufsebene 1 : Agrarwirtschaft, Bio- und Umwelttechnologie */
	AB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100002000, 1, "AB", "Agrarwirtschaft, Bio- und Umwelttechnologie", null, null)
	}),

	/** Berufsebene 1 : Ernährung */
	ER(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100003000, 1, "ER", "Ernährung", null, null)
	}),

	/** Berufsebene 1 : Ernährung und Hauswirtschaft */
	EU(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100004000, 1, "EU", "Ernährung und Hauswirtschaft", null, null)
	}),

	/** Berufsebene 1 : Ernährung/Hauswirtschaft */
	EH(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100005000, 1, "EH", "Ernährung/Hauswirtschaft", null, null)
	}),

	/** Berufsebene 1 : Ernährungs- und Versorgungsmanagement */
	EV(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100006000, 1, "EV", "Ernährungs- und Versorgungsmanagement", null, null)
	}),

	/** Berufsebene 1 : Gestaltung */
	GT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100007000, 1, "GT", "Gestaltung", null, null)
	}),

	/** Berufsebene 1 : Gesundheit und Soziales */
	GU(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100008000, 1, "GU", "Gesundheit und Soziales", null, null)
	}),

	/** Berufsebene 1 : Gesundheit/Erziehung und Soziales */
	GE(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100009000, 1, "GE", "Gesundheit/Erziehung und Soziales", null, null)
	}),

	/** Berufsebene 1 : Gesundheit/Soziales */
	GS(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100010000, 1, "GS", "Gesundheit/Soziales", null, null)
	}),

	/** Berufsebene 1 : Informatik */
	IF(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100011000, 1, "IF", "Informatik", null, null)
	}),

	/** Berufsebene 1 : ohne besondere Zuordnung */
	OZ(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100012000, 1, "OZ", "ohne besondere Zuordnung", null, null)
	}),

	/** Berufsebene 1 : ohne Fachbereich / Schulversuch */
	SV(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100013000, 1, "SV", "ohne Fachbereich / Schulversuch", null, null)
	}),

	/** Berufsebene 1 : Sozialwesen */
	SW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100014000, 1, "SW", "Sozialwesen", null, null)
	}),

	/** Berufsebene 1 : Technik */
	TE(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100015000, 1, "TE", "Technik", null, null)
	}),

	/** Berufsebene 1 : Technik/Naturwissenschaften */
	TN(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100016000, 1, "TN", "Technik/Naturwissenschaften", null, null)
	}),

	/** Berufsebene 1 : Wirtschaft */
	WI(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100017000, 1, "WI", "Wirtschaft", null, null)
	}),

	/** Berufsebene 1 : Wirtschaft und Verwaltung */
	WV(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(100018000, 1, "WV", "Wirtschaft und Verwaltung", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der Berufsebene, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull BerufskollegBerufsebeneKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Berufsebene */
	public final @NotNull BerufskollegBerufsebeneKatalogEintrag@NotNull[] historie;

	/** Eine Hashmap mit allen definierten Berufsebenen der Ebene 1, zugeordnet zu ihren Kürzeln */
	private static final @NotNull HashMap<@NotNull String, @NotNull BerufskollegBerufsebene1> _ebenen = new HashMap<>();


	/**
	 * Erzeugt eine neue Berufsebene in der Aufzählung.
	 *
	 * @param historie   die Historie der Berufsebene, welches ein Array von {@link BerufskollegBerufsebeneKatalogEintrag} ist
	 */
	BerufskollegBerufsebene1(final @NotNull BerufskollegBerufsebeneKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzels der Berufsebenen auf die zugehörigen Berufsebenen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Berufsebene auf die zugehörigen Berufsebene
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull BerufskollegBerufsebene1> getMapBerufsebenenByKuerzel() {
		if (_ebenen.size() == 0) {
			for (final BerufskollegBerufsebene1 s : BerufskollegBerufsebene1.values()) {
				if (s.daten != null)
					_ebenen.put(s.daten.kuerzel, s);
			}
		}
		return _ebenen;
	}


	/**
	 * Gibt die Berufsebene für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Berufsebene
	 *
	 * @return die Berufsebene oder null, falls das Kürzel ungültig ist
	 */
	public static BerufskollegBerufsebene1 getByKuerzel(final String kuerzel) {
		return getMapBerufsebenenByKuerzel().get(kuerzel);
	}

}
