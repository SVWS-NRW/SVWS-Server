package de.nrw.schule.svws.core.types.gost;

import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Aufzählung für die Kursarten in 
 * der gymnasialen Oberstufe zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public class GostKursart {

	private static final long FACHART_ID_FAKTOR = 1000L; 
	
	/** Die Zuordnung der Kursarten zu dem Kürzel der Kursart */
	private static final @NotNull HashMap<@NotNull String, @NotNull GostKursart> map = new HashMap<>();



	/** Leistungskurs = LK */
	public static final @NotNull GostKursart LK = new GostKursart(1, "LK", "Leistungskurs");

	/** Grundkurs = GK */
	public static final @NotNull GostKursart GK = new GostKursart(2, "GK", "Grundkurs");

	/** Zusatzkurs = ZK */
	public static final @NotNull GostKursart ZK = new GostKursart(3, "ZK", "Zusatzkurs");

	/** Projektkurs = PJK */
	public static final @NotNull GostKursart PJK = new GostKursart(4, "PJK", "Projektkurs");

	/** Vertiefungskurs = VTF */
	public static final @NotNull GostKursart VTF = new GostKursart(5, "VTF", "Vertiefungskurs");

	/** Die eindeutige ID der Kursart der Gymnasialen Oberstufe*/
	public final @NotNull int id;

	/** Das Kürzel der Kursart der Gymnasialen Oberstufe */
	public final @NotNull String kuerzel;

	/** Die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe */
	public final @NotNull String beschreibung; 



	/**
	 * Erzeugt eine neue Kursart für die Aufzählung.
	 * 
	 * @param id             die eindeutige ID der Kursart der Gymnasialen Oberstufe
	 * @param kuerzel        das Kürzel der Kursart der Gymnasialen Oberstufe
	 * @param beschreibung   die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe
	 */
	private GostKursart(final @NotNull int id, final @NotNull String kuerzel, final @NotNull String beschreibung) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		map.put(kuerzel, this);
	}


    /**
     * Prüft die Anzahl der Wochenstunden zu der Kursart.
     * 
     * @param anzahl   Anzahl der Wochenstunden
     * 
     * @return         Anzahl der Wochenstunden der Kursart korrekt, true oder false
     */
	@JsonIgnore
	public boolean pruefeWochenstunden(int anzahl) {
		switch (kuerzel) {
			case "GK":
				return (anzahl == 3) || (anzahl == 4);  // neu einsetzende Fremdsprachen können 4-stündig sein
			case "LK":
				return (anzahl == 5);
			case "PJK":
				return (anzahl == 2) || (anzahl == 3);
			case "VTF":
				return (anzahl == 2);
			case "ZK":
				return (anzahl == 3);
		}
		return false;
	}


	/**
	 * Gibt alle Kursarten der gymnasialen Oberstufe zurück.
	 * 
	 * @return eine {@link Collection} mit den Kursarten.
	 */
	public static final @NotNull Collection<@NotNull GostKursart> values() {
		return map.values();
	}

	
    /**
     * Gibt die Kursart aus der ID Kursart zurück.
     * 
     * @param id    die ID der Kursart
     * 
     * @return die Kursart
     * 
     * @throws IllegalArgumentException falls die ID ungültig ist 
     */
	public static @NotNull GostKursart fromID(int id) throws IllegalArgumentException {
		switch (id) {
			case 1: return GostKursart.LK; 
			case 2: return GostKursart.GK; 
			case 3: return GostKursart.ZK; 
			case 4: return GostKursart.PJK; 
			case 5: return GostKursart.VTF; 
		}
		throw new IllegalArgumentException("Invalid ID value.");
	}
	
    /**
     * Liefert die Kursart anhand der Kursart-ID der Fachwahl.
     * 
     * @param pFachwahl Das Fachwahl-Objekt.
     * @return die Kursart anhand der Kursart-ID der Fachwahl.
     * @throws IllegalArgumentException falls die ID ungültig ist 
     */
	public static @NotNull GostKursart fromFachwahlOrException(@NotNull GostFachwahl pFachwahl) throws IllegalArgumentException {
		return fromID(pFachwahl.kursartID);
	}

    /**
     * Gibt die Kursart aus der ID Kursart zurück.
     * 
     * @param id    die ID der Kursart
     * 
     * @return die Kursart oder null falls die ID ungültig ist 
     */
	public static GostKursart fromIDorNull(int id) {
		switch (id) {
			case 1: return GostKursart.LK; 
			case 2: return GostKursart.GK; 
			case 3: return GostKursart.ZK; 
			case 4: return GostKursart.PJK; 
			case 5: return GostKursart.VTF; 
		}
		return null;
	}
	
    /**
     * Gibt die Kursart aus dem Kürzel der Kursart zurück.
     * 
     * @param kuerzel    das Kürzel der Kursart
     * 
     * @return die Kursart oder null, falls das Kürzel ungültig ist 
     */
	public static GostKursart fromKuerzel(String kuerzel) {
		return map.get(kuerzel);
	}

	
	/**
	 * Berechnet mit der Formel pFachID * {@link #FACHART_ID_FAKTOR} + pKursartID die ID der Fachart.
	 * 
	 * @param  pFachID    Die DatenbankID des Faches.
	 * @param  pKursartID Die DatenbankID der Kursart.
	 * 
	 * @return pFachID * {@link #FACHART_ID_FAKTOR} + pKursartID
	 */
	public static long getFachartID(long pFachID, int pKursartID) {
		return pFachID * FACHART_ID_FAKTOR + pKursartID;
	}
	
	/**
	 * Berechnet anhand des Fachwahl-Objektes die FachartID.
	 * @param pFachwahl Das Fachwahl-Objekt.
	 * 
	 * @return pFachwahl.fachID * {@link #FACHART_ID_FAKTOR} + pFachwahl.kursartID
	 */
	public static long getFachartID(@NotNull GostFachwahl pFachwahl) {
		return getFachartID(pFachwahl.fachID, pFachwahl.kursartID);
	}
	
	/**
	 * Berechnet anhand des Kurs-Objektes die FachartID.
	 * @param pKurs Das Kurs-Objekt.
	 * 
	 * @return pKurs.fachID * {@link #FACHART_ID_FAKTOR} + pKurs.kursartID
	 */
	public static long getFachartID(GostBlockungKurs pKurs) {
		return getFachartID(pKurs.fach_id, pKurs.kursart);
	}
	
	/**
	 * Berechnet anhand der Fachart-ID die Fach-ID.
	 *  
	 * @param pFachartID Die ID der Fachart, welche das Fach und die Kursart kodiert.
	 * 
	 * @return Ganzzahlige Division von pFachartID durch {@link #FACHART_ID_FAKTOR}
	 */
	public static long getFachID(long pFachartID) {
		return pFachartID / FACHART_ID_FAKTOR;
	}

	/**
	 * Berechnet anhand der Fachart-ID die Kursart-ID.
	 *  
	 * @param pFachartID Die ID der Fachart, welche das Fach und die Kursart kodiert.
	 * 
	 * @return Rest der ganzzahligen Division von pFachartID durch {@link #FACHART_ID_FAKTOR}
	 */
	public static int getKursartID(long pFachartID) {
		return (int) (pFachartID % FACHART_ID_FAKTOR);
	}

	@Override
	@JsonIgnore
	public @NotNull String toString() {
		return kuerzel;
	}




}
