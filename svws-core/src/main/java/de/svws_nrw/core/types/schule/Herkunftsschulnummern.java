package de.svws_nrw.core.types.schule;

import java.util.Arrays;
import java.util.HashMap;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.schule.HerkunftsschulnummerKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für weitere Schulnummer in Bezug auf die Herkunft von Schülern. Dies
 * betrifft allgemeine Schulnummern für andere Bundesländer und Länder.
 */
public enum Herkunftsschulnummern {

	/** Herkunft Schulnummer : Schule aus dem sonstigen Ausland */
	SONSTIGES_AUSLAND(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(999000000L, 999000, "Schule aus dem sonstigen Ausland", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Schleswig-Holstein */
	SCHLESWIG_HOLSTEIN(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980001000L, 980001, "Schule aus Schleswig-Holstein", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Hamburg */
	HAMBURG(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980002000L, 980002, "Schule aus Hamburg", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Niedersachsen */
	NIEDERSACHSEN(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980003000L, 980003, "Schule aus Niedersachsen", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Bremen */
	BREMEN(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980004000L, 980004, "Schule aus Bremen", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Hessen */
	HESSEN(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980006000L, 980006, "Schule aus Hessen", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Rheinland-Pfalz */
	RHEINLANDPFALZ(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980007000L, 980007, "Schule aus Rheinland-Pfalz", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Baden-Württemberg */
	BADEN_WUERTTEMBERG(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980008000L, 980008, "Schule aus Baden-Württemberg", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Bayern */
	BAYERN(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980009000L, 980009, "Schule aus Bayern", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus dem Saarland */
	SAARLAND(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980010000L, 980010, "Schule aus dem Saarland", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Berlin */
	BERLIN(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980011000L, 980011, "Schule aus Berlin", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Brandenburg */
	BRANDENBURG(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980012000L, 980012, "Schule aus Brandenburg", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Mecklenburg-Vorpommern */
	MECKLENBURG_VORPOMMERN(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980013000L, 980013, "Schule aus Mecklenburg-Vorpommern", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Sachsen */
	SACHSEN(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980014000L, 980014, "Schule aus Sachsen", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Sachsen-Anhalt */
	SACHSEN_ANHALT(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980015000L, 980015, "Schule aus Sachsen-Anhalt", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Thüringen */
	THUERINGEN(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980016000L, 980016, "Schule aus Thüringen", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Belgien */
	BELGIEN(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(991000000L, 991000, "Schule aus Belgien", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Luxemburg */
	LUXEMBURG(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(992000000L, 992000, "Schule aus Luxemburg", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus den Niederlanden */
	NIEDERLANDE(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(993000000L, 993000, "Schule aus den Niederlanden", null, null, null)
	}),

	/** Herkunft Schulnummer : Sonstige/keine Schule */
	SONSTIGE(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(980500000L, 980500, "Sonstige/keine Schule", null, null, null)
	}),


	/** Herkunft Schulnummer : Herkunft noch unbekannt (nur A12, A13) */
	UNBEKANNT(new HerkunftsschulnummerKatalogEintrag[] {
			new HerkunftsschulnummerKatalogEintrag(999500000L, 999500, "Herkunft noch unbekannt (nur A12, A13)", Arrays.asList(
					new Pair<>(Schulform.BK, Schulgliederung.A12),
					new Pair<>(Schulform.BK, Schulgliederung.A13),
					new Pair<>(Schulform.SB, Schulgliederung.A12),
					new Pair<>(Schulform.SB, Schulgliederung.A13)
			), null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 2;

	/** Der aktuellen Daten der Herkunftsschulnummer, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull HerkunftsschulnummerKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Herkunftsschulnummer */
	public final @NotNull HerkunftsschulnummerKatalogEintrag @NotNull [] historie;

	/** Eine Hashmap mit allen definierten Herkunftsschulnummern, zugeordnet zu ihren Schulnummern */
	private static final @NotNull HashMap<Integer, Herkunftsschulnummern> _mapBySchulnummer = new HashMap<>();


	/**
	 * Erzeugt eine neue Herkunftsschulnummer in der Aufzählung.
	 *
	 * @param historie   die Historie der Herkunftsschulnummer, welches ein Array von {@link HerkunftsschulnummerKatalogEintrag} ist
	 */
	Herkunftsschulnummern(final @NotNull HerkunftsschulnummerKatalogEintrag @NotNull [] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzels der Herkunftsschulnummern auf die zugehörigen Herkunftsschulnummern
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Herkunftsschulnummern auf die zugehörigen Herkunftsschulnummern
	 */
	private static @NotNull HashMap<Integer, Herkunftsschulnummern> getMapBySchulnummer() {
		if (_mapBySchulnummer.size() == 0) {
			for (final Herkunftsschulnummern s : Herkunftsschulnummern.values()) {
				if (s.daten != null)
					_mapBySchulnummer.put(s.daten.schulnummer, s);
			}
		}
		return _mapBySchulnummer;
	}


	/**
	 * Gibt die Herkunftsschulnummer für die angegebene Schulnummer zurück.
	 *
	 * @param nummer   die Schulnummer
	 *
	 * @return die Herkunftsschulnummer oder null, falls die Schulnummer hier nicht vohanden ist
	 */
	public static Herkunftsschulnummern getByKuerzel(final Integer nummer) {
		return getMapBySchulnummer().get(nummer);
	}

}
