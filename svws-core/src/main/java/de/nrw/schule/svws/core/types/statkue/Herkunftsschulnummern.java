package de.nrw.schule.svws.core.types.statkue;

import java.util.Arrays;
import java.util.HashMap;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.core.data.schule.HerkunftsschulnummerKatalogEintrag;
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
		new HerkunftsschulnummerKatalogEintrag(999001000L, 999001, "Schule aus Schleswig-Holstein", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Hamburg */
	HAMBURG(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999002000L, 999002, "Schule aus Hamburg", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Niedersachsen */
	NIEDERSACHSEN(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999003000L, 999003, "Schule aus Niedersachsen", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Bremen */
	BREMEN(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999004000L, 999004, "Schule aus Bremen", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Hessen */
	HESSEN(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999006000L, 999006, "Schule aus Hessen", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Rheinlandpfalz */
	RHEINLANDPFALZ(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999007000L, 999007, "Schule aus Rheinlandpfalz", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Baden-Württemberg */
	BADEN_WUERTTEMBERG(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999008000L, 999008, "Schule aus Baden-Württemberg", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Bayern */
	BAYERN(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999009000L, 999009, "Schule aus Bayern", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus dem Saarland */
	SAARLAND(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999010000L, 999010, "Schule aus dem Saarland", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Berlin */
	BERLIN(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999011000L, 999011, "Schule aus Berlin", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Brandenburg */
	BRANDENBURG(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999012000L, 999012, "Schule aus Brandenburg", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Mecklenburg-Vorpommern */
	MECKLENBURG_VORPOMMERN(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999013000L, 999013, "Schule aus Mecklenburg-Vorpommern", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Sachsen */
	SACHSEN(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999014000L, 999014, "Schule aus Sachsen", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Sachsen-Anhalt */
	SACHSEN_ANHALT(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999015000L, 999015, "Schule aus Sachsen-Anhalt", null, null, null)
	}),

	/** Herkunft Schulnummer : Schule aus Thüringen */
	THUERINGEN(new HerkunftsschulnummerKatalogEintrag[] {
		new HerkunftsschulnummerKatalogEintrag(999016000L, 999016, "Schule aus Thüringen", null, null, null)
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
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Herkunftsschulnummer, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull HerkunftsschulnummerKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Herkunftsschulnummer */
	public final @NotNull HerkunftsschulnummerKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Herkunftsschulnummern, zugeordnet zu ihren Schulnummern */ 
	private static final @NotNull HashMap<@NotNull Integer, Herkunftsschulnummern> _mapBySchulnummer = new HashMap<>();
	

	/**
	 * Erzeugt eine neue Herkunftsschulnummer in der Aufzählung.
	 * 
	 * @param historie   die Historie der Herkunftsschulnummer, welches ein Array von {@link HerkunftsschulnummerKatalogEintrag} ist  
	 */
	private Herkunftsschulnummern(@NotNull HerkunftsschulnummerKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}
	
	
	/**
	 * Gibt eine Map von den Kürzels der Herkunftsschulnummern auf die zugehörigen Herkunftsschulnummern
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Herkunftsschulnummern auf die zugehörigen Herkunftsschulnummern
	 */
	private static @NotNull HashMap<@NotNull Integer, Herkunftsschulnummern> getMapBySchulnummer() {
		if (_mapBySchulnummer.size() == 0) {
			for (Herkunftsschulnummern s : Herkunftsschulnummern.values()) {
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
	public static Herkunftsschulnummern getByKuerzel(Integer nummer) {
		return getMapBySchulnummer().get(nummer);
	}

}
