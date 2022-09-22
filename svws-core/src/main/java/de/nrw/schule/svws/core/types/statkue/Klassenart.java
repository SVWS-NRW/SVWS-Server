package de.nrw.schule.svws.core.types.statkue;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.core.data.klassen.KlassenartKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulformSchulgliederung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die zulässigen Kursarten der einzelnen 
 * Schulformen und Schulgliederungen zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum Klassenart {

	/** Klassenart: Kein Eintrag */
	UNDEFINIERT(new KlassenartKatalogEintrag[] {
		new KlassenartKatalogEintrag(0, "**", "Kein Eintrag", Arrays.asList(
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),

	/** Klassenart: Hauptschulklasse 1A */
	HA_1A(new KlassenartKatalogEintrag[] {
		new KlassenartKatalogEintrag(1000, "1A", "Klasse 10 Typ A (Hauptschule)", Arrays.asList(
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.GM, Schulgliederung.H),
			new Pair<>(Schulform.R, Schulgliederung.H),
			new Pair<>(Schulform.SK, Schulgliederung.H),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Klassenart: Hauptschulklasse 1B */
	HA_1B(new KlassenartKatalogEintrag[] {
		new KlassenartKatalogEintrag(2000, "1B", "Klasse 10 Typ B (Hauptschule)", Arrays.asList(
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.GM, Schulgliederung.H),
			new Pair<>(Schulform.SK, Schulgliederung.H),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Klassenart: Hauptschuleklasse ohne Differenzierung nach A und B */
	HA_AB(new KlassenartKatalogEintrag[] {
		new KlassenartKatalogEintrag(3000, "AB", "Klassen im Jahrgang 10 ohne Differenzierung in Typ A und Typ B (Hauptschule)", Arrays.asList(
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.GM, Schulgliederung.H),
			new Pair<>(Schulform.SK, Schulgliederung.H),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Klassenart: Frühförderung: SKG (Ambulante Maßnahmen für blinde, gehörlose, sehbeh. und schwerh. Kinder) */
	AM(new KlassenartKatalogEintrag[] {
		new KlassenartKatalogEintrag(4000, "AM", "Frühförderung: SKG (Ambulante Maßnahmen für blinde, gehörlose, sehbeh. und schwerh. Kinder)", Arrays.asList(
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null)
		), null, null)
	}),

	/** Klassenart: Frühförderung: SKG (Präsenzgruppe) */
	PG(new KlassenartKatalogEintrag[] {
		new KlassenartKatalogEintrag(5000, "PG", "Frühförderung: SKG (Präsenzgruppe)", Arrays.asList(
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null)
		), null, null)
	}),

	/** Klassenart: Profilklasse (gemäß § 21 Abs. 3 APO-S I) */
	PK(new KlassenartKatalogEintrag[] {
		new KlassenartKatalogEintrag(6000, "PK", "Profilklasse (gemäß § 21 Abs. 3 APO-S I)", Arrays.asList(
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.GE, Schulgliederung.GY9),
			new Pair<>(Schulform.SK, Schulgliederung.GY)
		), null, null)
	}),

	/** Klassenart: Regelklasse */
	RK(new KlassenartKatalogEintrag[] {
		new KlassenartKatalogEintrag(7000, "RK", "Regelklasse", Arrays.asList(
			new Pair<>(Schulform.FW, (Schulgliederung) null),
			new Pair<>(Schulform.HI, (Schulgliederung) null),
			new Pair<>(Schulform.WF, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Klassenart: Deutschförderklasse (gemäß BASS 13-63 Nr. 3, Nummer 3.5.1) */
	SG(new KlassenartKatalogEintrag[] {
		new KlassenartKatalogEintrag(8000, "SG", "Deutschförderklasse (gemäß BASS 13-63 Nr. 3, Nummer 3.5.1)", Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Prozess die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Klassenart */
	public final @NotNull KlassenartKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Klassenart */
	public final @NotNull KlassenartKatalogEintrag@NotNull[] historie;	

	/** Eine HashMap mit allen zulässigen Klassenarten. Der Zugriff erfolgt dabei über die ID */ 
	private static final @NotNull HashMap<@NotNull Long, @NotNull Klassenart> _mapID = new HashMap<>();

	/** Eine HashMap mit zulässigen Klassenarten. Der Zugriff erfolgt dabei über das Kürzel */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull Klassenart> _mapKuerzel = new HashMap<>();

	/** Die Informationen zu den Kombinationen aus Schulformen und -gliederungen, wo die Klassenart zulässig ist */
	private @NotNull Vector<@NotNull Pair<@NotNull Schulform, Schulgliederung>>@NotNull[] zulaessig;

	
	/**
	 * Erzeugt eine zulässige Klassenart in der Aufzählung.
	 * 
	 * @param historie   die Historie der Klassenart, welches ein Array von {@link KlassenartKatalogEintrag} ist  
	 */
	@SuppressWarnings("unchecked")
	private Klassenart(@NotNull KlassenartKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		// Erzeuge zwei Felder mit den Schulformen und Schulgliederungen für die Historie
		this.zulaessig = (@NotNull Vector<@NotNull Pair<@NotNull Schulform, Schulgliederung>>@NotNull[])Array.newInstance(Vector.class, historie.length); 
		for (int i = 0; i < historie.length; i++) {
			this.zulaessig[i] = new Vector<>();
			for (@NotNull SchulformSchulgliederung kuerzelSfSgl : historie[i].zulaessig) {
				Schulform sf = Schulform.getByKuerzel(kuerzelSfSgl.schulform);
				if (sf == null)
					continue;
				Schulgliederung sgl = kuerzelSfSgl.gliederung == null ? null : Schulgliederung.getByKuerzel(kuerzelSfSgl.gliederung);
				this.zulaessig[i].add(new Pair<>(sf, sgl));
			}
		}
	}

	
	/**
	 * Gibt eine Map von den IDs der Klassenarten auf die zugehörigen Klassenarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs auf die zugehörigen Klassenarten
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull Klassenart> getMapByID() {
		if (_mapID.size() == 0)
			for (Klassenart s : Klassenart.values())
				if (s.daten != null)
					_mapID.put(s.daten.id, s);				
		return _mapID;
	}
	
	
	/**
	 * Gibt eine Map von den Kürzeln der Klassenarten auf die zugehörigen Klassenarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen Klassenarten
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Klassenart> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0)
			for (Klassenart s : Klassenart.values())
				if (s.daten != null)
					_mapKuerzel.put(s.daten.kuerzel, s);				
		return _mapKuerzel;
	}
	
	
	/**
	 * Prüft, ob die Schulform bei dieser Klassenart in irgendeiner Gliederung der 
	 * angegebenen Schulform zulässig ist.
	 * 
	 * @param schulform    die Schulform
	 * 
	 * @return true, falls die Klassenart in der Schulform zulässig ist, ansonsten false.
	 */
	private boolean hasSchulform(Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return false;
		for (@NotNull Pair<@NotNull Schulform, Schulgliederung> sfsgl : zulaessig[0]) {
			if (sfsgl.a == schulform)
				return true;
		}
		return false;
	}


	/**
	 * Bestimmt alle Klassenarten, die in irgendeiner Gliederung der angegebenen Schulform
	 * zulässig sind.
	 *  
	 * @param schulform    die Schulform
	 * 
	 * @return die zulässigen Klassenarten in der angegebenen Schulform
	 */
	public static @NotNull List<@NotNull Klassenart> get(Schulform schulform) {
		@NotNull Vector<@NotNull Klassenart> kursarten = new Vector<>();
		if (schulform == null)
			return kursarten;
		for (Klassenart kursart : Klassenart.values())
			if (kursart.hasSchulform(schulform))
				kursarten.add(kursart);
		return kursarten;
	}


	/**
	 * Liefert alle Kombinationen aus Schulformen und Schulgliederungen zurück,
	 * bei denen die Klassenart zulässig ist.
	 * 
	 * @return eine Liste der Kombinationen aus Schulformen und Schulgliederungen
	 */
	public @NotNull List<@NotNull Pair<@NotNull Schulform, Schulgliederung>> getGliederungen() {
		return zulaessig[0];
	}


	/**
	 * Bestimmt anhand des Kürzels, die zulässige Klassenart. 
	 * 
	 * @param kursart   das Kürzel
	 * 
	 * @return die Klassenart oder null, wenn keine Zuordnung für das übergebene Kürzel vorhanden ist
	 */
	public static Klassenart getByASDKursart(String kursart) {
		return getMapByKuerzel().get(kursart);
	}

}
