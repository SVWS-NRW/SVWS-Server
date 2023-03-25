package de.svws_nrw.core.types.schueler;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.schule.FoerderschwerpunktKatalogEintrag;
import de.svws_nrw.core.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Förderschwerpunkte zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum Foerderschwerpunkt {

	/** Förderschwerpunkt - kein Förderschwerpunkt */
	KEINER(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(0, "**", "kein Förderschwerpunkt", Arrays.asList(
			Schulform.BK, 
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.V, 
			Schulform.WB
		), null, null)
	}),

	/** Förderschwerpunkt - Sehen (Blinde) */
	BL(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(1000, "BL", "Sehen (Blinde)", Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.V
		), 2011, null)
	}),

	/** Förderschwerpunkt - Emotionale und soziale Entwicklung */
	EZ(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(2000, "EZ", "Emotionale und soziale Entwicklung", Arrays.asList(
			Schulform.BK, Schulform.SB, 
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.V
		), 2011, null)
	}),

	/** Förderschwerpunkt - Geistige Entwicklung */
	GB(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(3000, "GB", "Geistige Entwicklung", Arrays.asList(
			Schulform.BK, Schulform.SB, 
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.V
		), 2011, null)
	}),

	/** Förderschwerpunkt - Hören und Kommunikation (Gehörlose) */
	GH(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(4000, "GH", "Hören und Kommunikation (Gehörlose)", Arrays.asList(
			Schulform.BK, Schulform.SB, 
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.SR, 
			Schulform.V
		), 2011, null)
	}),

	/** Förderschwerpunkt - Körperliche und motorische Entwicklung */
	KB(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(5000, "KB", "Körperliche und motorische Entwicklung", Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.SG, 
			Schulform.V
		), 2011, null)
	}),

	/** Förderschwerpunkt - Schule für Kranke */
	KR(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(6000, "KR", "Schule für Kranke", Arrays.asList(
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.S, Schulform.KS
		), 2011, null)
	}),

	/** Förderschwerpunkt - Lernen */
	LB(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(7000, "LB", "Lernen", Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.V
		), 2011, null)
	}),

	/** Förderschwerpunkt - Präventive Förderung im Bereich Emotionale und soziale Entwicklung */
	PE(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(8000, "PE", "Präventive Förderung im Bereich Emotionale und soziale Entwicklung", Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.V
		), null, 2010)
	}),

	/** Förderschwerpunkt - Präventive Förderung */
	PF(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(9000, "PF", "Präventive Förderung", Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.V
		), null, 2010)
	}),

	/** Förderschwerpunkt - Präventive Förderung im Bereich Lernen */
	PL(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(10000, "PL", "Präventive Förderung im Bereich Lernen", Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.V
		), null, 2010)
	}),

	/** Förderschwerpunkt - Präventive Förderung im Bereich Sprache */
	PS(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(11000, "PS", "Präventive Förderung im Bereich Sprache", Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.V
		), null, 2010)
	}),

	/** Förderschwerpunkt - Sprache */
	SB(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(12000, "SB", "Sprache", Arrays.asList(
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.V
		), 2011, null)
	}),

	/** Förderschwerpunkt - Hören und Kommunikation (Schwerhörige) */
	SG(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(13000, "SG", "Hören und Kommunikation (Schwerhörige)", Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.SR, 
			Schulform.V
		), 2011, null)
	}),

	/** Förderschwerpunkt - Sehen (Sehbehinderte) */
	SH(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(14000, "SH", "Sehen (Sehbehinderte)", Arrays.asList(
			Schulform.BK, Schulform.SB,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.R, 
			Schulform.S, Schulform.KS, 
			Schulform.V
		), 2011, null)
	}),

	/** Förderschwerpunkt - Kein Förderschwerpunkt */
	XX(new FoerderschwerpunktKatalogEintrag[] {
		new FoerderschwerpunktKatalogEintrag(15000, "XX", "Kein Förderschwerpunkt", Arrays.asList(
			Schulform.SB, 
			Schulform.SG
		), null, null)
	});

	
	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten des Förderschwerpunktes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull FoerderschwerpunktKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen des Förderschwerpunktes */
	public final @NotNull FoerderschwerpunktKatalogEintrag@NotNull[] historie;	

	/** Eine Map mit der Zuordnung des Förderschwerpunktes zu dem Kürzel des Förderschwerpunktes */
	private static final @NotNull HashMap<@NotNull String, @NotNull Foerderschwerpunkt> _foerderschwerpunkteKuerzel = new HashMap<>();

	/** Eine Map mit der Zuordnung des Förderschwerpunktes zu der ID des Förderschwerpunktes */
	private static final @NotNull HashMap<@NotNull Long, @NotNull Foerderschwerpunkt> _foerderschwerpunkteID = new HashMap<>();
	
	/** Die Schulformen, bei welchen der Förderschwerpunkt vorkommt */
	private @NotNull Vector<@NotNull Schulform>@NotNull[] schulformen;


	/**
	 * Erzeugt einen neuen Förderschwerpunkt in der Aufzählung.
	 * 
	 * @param historie   die Historie des Förderschwerpunktes, welches ein Array von {@link FoerderschwerpunktKatalogEintrag} ist  
	 */
	@SuppressWarnings("unchecked")
	private Foerderschwerpunkt(final @NotNull FoerderschwerpunktKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
		// Erzeuge ein zweites Array mit der Schulformzuordnung für dei Historie
		this.schulformen = (@NotNull Vector<@NotNull Schulform>@NotNull[])Array.newInstance(Vector.class, historie.length); 
		for (int i = 0; i < historie.length; i++) {
			this.schulformen[i] = new Vector<>();
			for (final @NotNull String kuerzel : historie[i].schulformen) {
				final Schulform sf = Schulform.getByKuerzel(kuerzel);
				if (sf != null)
					this.schulformen[i].add(sf);
			}
		}
	}

	
	
	/**
	 * Gibt eine Map von den Kürzeln der Förderschwerpunkte auf die zugehörigen Förderschwerpunkte
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Förderschwerpunkte auf die zugehörigen Förderschwerpunkte
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Foerderschwerpunkt> getMapFoerderschwerpunktByKuerzel() {
		if (_foerderschwerpunkteKuerzel.size() == 0)
			for (final Foerderschwerpunkt s : Foerderschwerpunkt.values())
				_foerderschwerpunkteKuerzel.put(s.daten.kuerzel, s);				
		return _foerderschwerpunkteKuerzel;
	}
	

	/**
	 * Gibt eine Map von den IDs der Förderschwerpunkte auf die zugehörigen Förderschwerpunkte
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs der Förderschwerpunkte auf die zugehörigen Förderschwerpunkte
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull Foerderschwerpunkt> getMapFoerderschwerpunktByID() {
		if (_foerderschwerpunkteID.size() == 0)
			for (final Foerderschwerpunkt s : Foerderschwerpunkt.values()) {
				for (final FoerderschwerpunktKatalogEintrag k : s.historie)
					_foerderschwerpunkteID.put(k.id, s);
			}
		return _foerderschwerpunkteID;
	}
	

	/**
	 * Liefert den Förderschwerpunkt anhand des übergebenen Kürzels zurück. 
	 * 
	 * @param kuerzel   das Kürzel des Förderschwerpunktes
	 * 
	 * @return der Förderschwerpunkt oder null, falls das Kürzel ungültig ist
	 */
	public static Foerderschwerpunkt getByKuerzel(final String kuerzel) {
		return getMapFoerderschwerpunktByKuerzel().get(kuerzel);
	}



	/**
	 * Liefert den Förderschwerpunkt anhand der übergebenen ID zurück. 
	 * 
	 * @param id   die ID des Förderschwerpunktes
	 * 
	 * @return der Förderschwerpunkt oder null, falls die ID ungültig ist
	 */
	public static Foerderschwerpunkt getByID(final Long id) {
		return getMapFoerderschwerpunktByID().get(id);
	}



	/**
	 * Liefert alle Schulformen zurück, bei welchen der Förderschwerpunkt vorkommt.
	 * 
	 * @return eine Liste der Schulformen
	 */
	@JsonIgnore
	public @NotNull List<@NotNull Schulform> getSchulformen() {
		return schulformen[historie.length - 1];
	}



	/**
	 * Liefert alle zulässigen Förderschwerpunkte für die angegebene Schulform.
	 * 
	 * @param schulform   die Schulform
	 * 
	 * @return die bei der Schulform zulässigen Förderschwerpunkte
	 */
	public static @NotNull List<@NotNull Foerderschwerpunkt> get(final Schulform schulform) {
		final @NotNull Vector<@NotNull Foerderschwerpunkt> result = new Vector<>();
		if (schulform == null)
			return result;
		final @NotNull Foerderschwerpunkt@NotNull[] fs = Foerderschwerpunkt.values();
		for (int i = 0; i < fs.length; i++) {
			final @NotNull Foerderschwerpunkt gliederung = fs[i];
			if (gliederung.hasSchulform(schulform))
				result.add(gliederung);
		}
		return result;
	}
	
	
	/**
	 * Prüft anhand des Förderschwerpunkts-Kürzels, ob die Schulform diesen Förderschwerpunkt
	 * hat oder nicht.
	 * 
	 * @param kuerzel   das Kürzel der Schulform
	 * 
	 * @return true, falls der Förderschwerpunkt bei der Schulform existiert und ansonsten false
	 */
	@JsonIgnore
	public boolean hasSchulformByKuerzel(final String kuerzel) {
		if ((kuerzel == null) || "".equals(kuerzel))
			return false;
		if (daten.schulformen != null) {
			for (int i = 0; i < daten.schulformen.size(); i++) {
				final String sfKuerzel = daten.schulformen.get(i);
				if (sfKuerzel.equals(kuerzel))
					return true;
			}			
		}
		return false;
	}
	
	
	/**
	 * Prüft, ob die Schulform diesen Förderschwerpunkt hat oder nicht.
	 * 
	 * @param schulform   die Schulform
	 * 
	 * @return true, falls der Förderschwerpunkt bei der Schulform existiert und ansonsten false
	 */
	@JsonIgnore
	public boolean hasSchulform(final Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return false;
		if (daten.schulformen != null) {
			for (int i = 0; i < daten.schulformen.size(); i++) {
				final String sfKuerzel = daten.schulformen.get(i);
				if (sfKuerzel.equals(schulform.daten.kuerzel))
					return true;
			}			
		}
		return false;
	}


	/**
	 * Gibt die Förderschwerpunkte der angegebenen Schulform mit dem übergegebenen Kürzel zurück.
	 * 
	 * @param sf        die Schulform
	 * @param kuerzel   das Kürzel des Förderschwerpunktes
	 * 
	 * @return der Förderschwerpunkt, falls die Parameter gültige Werte sind und ansonsten null 
	 */
	public static Foerderschwerpunkt getBySchulformAndKuerzel(final Schulform sf, final String kuerzel) {
		if (sf == null)
			return null;
		// Ist das Kürzel null, so ist der Standard für die Schulform zurückzugeben
		if ((kuerzel == null) || "".equals(kuerzel))
			return Foerderschwerpunkt.KEINER;
		// Prüfe, ob der Förderschwerpunkt bei der Schulform existiert
		final @NotNull List<@NotNull Foerderschwerpunkt> schwerpunkte = get(sf); 
		for (int i = 0; i < schwerpunkte.size(); i++) {
			final Foerderschwerpunkt fs = schwerpunkte.get(i);
			if ((fs.daten.kuerzel).equalsIgnoreCase(kuerzel))
				return fs;
		}
		return null;
	}

}
