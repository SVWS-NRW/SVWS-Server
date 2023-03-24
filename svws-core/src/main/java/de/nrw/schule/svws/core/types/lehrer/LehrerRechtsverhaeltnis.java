package de.nrw.schule.svws.core.types.lehrer;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogRechtsverhaeltnisEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Arten von Rechtsverhältnissen für
 * Lehrer an der Schule zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerRechtsverhaeltnis {

	/** Rechtsverhältnis 'Beamter auf Lebenszeit' */
	L(new LehrerKatalogRechtsverhaeltnisEintrag[]{
		new LehrerKatalogRechtsverhaeltnisEintrag(3, "L", "Beamter auf Lebenszeit", null, null)
	}),

	/** Rechtsverhältnis 'Beamter auf Probe' */
	P(new LehrerKatalogRechtsverhaeltnisEintrag[]{
		new LehrerKatalogRechtsverhaeltnisEintrag(2, "P", "Beamter auf Probe", null, null)
	}),

	/** Rechtsverhältnis 'Beamter auf Probe zur Anstellung' */
	A(new LehrerKatalogRechtsverhaeltnisEintrag[]{
		new LehrerKatalogRechtsverhaeltnisEintrag(1, "A", "Beamter auf Probe zur Anstellung", null, null)
	}),

	/** Rechtsverhältnis 'Beamter, nebenamtlich (nicht hauptamtlich im Schuldienst)' */
	N(new LehrerKatalogRechtsverhaeltnisEintrag[]{
		new LehrerKatalogRechtsverhaeltnisEintrag(7, "N", "Beamter, nebenamtlich (nicht hauptamtlich im Schuldienst)", null, null)
	}),

	/** Rechtsverhältnis 'Beamter auf Widerruf (LAA)' */
	W(new LehrerKatalogRechtsverhaeltnisEintrag[]{
		new LehrerKatalogRechtsverhaeltnisEintrag(9, "W", "Beamter auf Widerruf (LAA)", null, null)
	}),

	/** Rechtsverhältnis 'Angestellte, unbefristet (BAT-Vertrag)' */
	U(new LehrerKatalogRechtsverhaeltnisEintrag[]{
		new LehrerKatalogRechtsverhaeltnisEintrag(4, "U", "Angestellte, unbefristet (BAT-Vertrag)", null, null)
	}),

	/** Rechtsverhältnis 'Angestellte, befristet (BAT-Vertrag)' */
	B(new LehrerKatalogRechtsverhaeltnisEintrag[]{
		new LehrerKatalogRechtsverhaeltnisEintrag(5, "B", "Angestellte, befristet (BAT-Vertrag)", null, null)
	}),

	/** Rechtsverhältnis 'Angestellte, nicht BAT-Vertrag' */
	J(new LehrerKatalogRechtsverhaeltnisEintrag[]{
		new LehrerKatalogRechtsverhaeltnisEintrag(6, "J", "Angestellte, nicht BAT-Vertrag", null, null)
	}),

	/** Rechtsverhältnis 'Gestellungsvertrag' */
	S(new LehrerKatalogRechtsverhaeltnisEintrag[]{
		new LehrerKatalogRechtsverhaeltnisEintrag(8, "S", "Gestellungsvertrag", null, null)
	}),

	/** Rechtsverhältnis 'Unentgeltlich Beschäftigte' */
	X(new LehrerKatalogRechtsverhaeltnisEintrag[]{
		new LehrerKatalogRechtsverhaeltnisEintrag(10, "X", "Unentgeltlich Beschäftigte", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten des Rechtsverhältnisses, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogRechtsverhaeltnisEintrag daten;
	
	/** Die Historie mit den Einträgen der Rechtsverhältnisse */
	public final @NotNull LehrerKatalogRechtsverhaeltnisEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Arten von Rechtsverhältnissen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerRechtsverhaeltnis> _rechtsverhaeltnisByID = new HashMap<>();

	/** Eine Hashmap mit allen Arten von Rechtsverhältnissen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerRechtsverhaeltnis> _rechtsverhaeltnisByKuerzel = new HashMap<>();


	/**
	 * Erzeugt eine neue Art von Rechtsverhältnissen in der Aufzählung.
	 * 
	 * @param historie   die Historie des Rechtsverhältnisses, welches ein Array von {@link LehrerKatalogRechtsverhaeltnisEintrag} ist  
	 */
	private LehrerRechtsverhaeltnis(final @NotNull LehrerKatalogRechtsverhaeltnisEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Rechtsverhältnisse auf die zugehörigen Rechtsverhältnisse
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Rechtsverhältnisse auf die zugehörigen Rechtsverhältnisse
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerRechtsverhaeltnis> getMapRechtsverhaeltnisByID() {
		if (_rechtsverhaeltnisByID.size() == 0)
			for (final LehrerRechtsverhaeltnis l : LehrerRechtsverhaeltnis.values())
				_rechtsverhaeltnisByID.put(l.daten.id, l);				
		return _rechtsverhaeltnisByID;
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Rechtsverhältnisse auf die zugehörigen Rechtsverhältnisse
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Rechtsverhältnisse auf die zugehörigen Rechtsverhältnisse
	 */
	private static @NotNull HashMap<@NotNull String, LehrerRechtsverhaeltnis> getMapRechtsverhaeltnisByKuerzel() {
		if (_rechtsverhaeltnisByKuerzel.size() == 0)
			for (final LehrerRechtsverhaeltnis l : LehrerRechtsverhaeltnis.values())
				_rechtsverhaeltnisByKuerzel.put(l.daten.kuerzel, l);				
		return _rechtsverhaeltnisByKuerzel;
	}
	

	/**
	 * Gibt die Art von Rechtsverhältnissen anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Art von Rechtsverhältnissen
	 * 
	 * @return die Art von Rechtsverhältnissen oder null, falls die ID ungültig ist
	 */
	public static LehrerRechtsverhaeltnis getByID(final long id) {
		return getMapRechtsverhaeltnisByID().get(id);
	}


	/**
	 * Gibt die Art von Rechtsverhältnissen anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Art von Rechtsverhältnissen
	 * 
	 * @return die Art von Rechtsverhältnissen oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerRechtsverhaeltnis getByKuerzel(final String kuerzel) {
		return getMapRechtsverhaeltnisByKuerzel().get(kuerzel);
	}

}
