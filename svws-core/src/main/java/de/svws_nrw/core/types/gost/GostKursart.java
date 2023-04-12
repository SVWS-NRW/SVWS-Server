package de.svws_nrw.core.types.gost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.kurse.ZulaessigeKursart;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Aufzählung für die Kursarten in
 * der gymnasialen Oberstufe zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum GostKursart {

	/** Leistungskurs = LK */
	LK(1, "LK", "Leistungskurs", Arrays.asList(
			ZulaessigeKursart.LK1, ZulaessigeKursart.LK2
			)),

	/** Grundkurs = GK */
	GK(2, "GK", "Grundkurs", Arrays.asList(
			ZulaessigeKursart.GKM, ZulaessigeKursart.GKS, ZulaessigeKursart.AB3, ZulaessigeKursart.AB4, ZulaessigeKursart.EFSP
			)),

	/** Zusatzkurs = ZK */
	ZK(3, "ZK", "Zusatzkurs", Arrays.asList(
			ZulaessigeKursart.ZK
			)),

	/** Projektkurs = PJK */
	PJK(4, "PJK", "Projektkurs", Arrays.asList(
			ZulaessigeKursart.PJK
			)),

	/** Vertiefungskurs = VTF */
	VTF(5, "VTF", "Vertiefungskurs", Arrays.asList(
			ZulaessigeKursart.VTF
			));


	private static final long FACHART_ID_FAKTOR = 1000L;

	/** Die Zuordnung der Kursarten zu dem Kürzel der Kursart */
	private static final @NotNull HashMap<@NotNull String, @NotNull GostKursart> _mapKuerzel = new HashMap<>();

	/** Die Zuordnung der Kursarten zu der jeweiligen zulässigen Kursart */
	private static final @NotNull Map<@NotNull ZulaessigeKursart, @NotNull GostKursart> _mapZulKursart = new ArrayMap<>(ZulaessigeKursart.values());

	/** Die eindeutige ID der Kursart der Gymnasialen Oberstufe*/
	public final @NotNull int id;

	/** Das Kürzel der Kursart der Gymnasialen Oberstufe */
	public final @NotNull String kuerzel;

	/** Die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe */
	public final @NotNull String beschreibung;

	/** Die Liste der Kursarten, welche zu dieser Gost-Kursart gehören */
	private final @NotNull List<@NotNull ZulaessigeKursart> kursarten;


	/**
	 * Erzeugt eine neue Kursart für die Aufzählung.
	 *
	 * @param id             die eindeutige ID der Kursart der Gymnasialen Oberstufe
	 * @param kuerzel        das Kürzel der Kursart der Gymnasialen Oberstufe
	 * @param beschreibung   die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe
	 * @param kursarten      die zulässigen Kursarten, die dieser Kursart der gymnasialen Oberstufe zugeordnet sind
	 */
	GostKursart(final @NotNull int id, final @NotNull String kuerzel, final @NotNull String beschreibung,
			final @NotNull List<@NotNull ZulaessigeKursart> kursarten) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		this.kursarten = kursarten;
	}


	/**
	 * Prüft die Anzahl der Wochenstunden zu der Kursart.
	 *
	 * @param anzahl   Anzahl der Wochenstunden
	 *
	 * @return         Anzahl der Wochenstunden der Kursart korrekt, true oder false
	 */
	public boolean pruefeWochenstunden(final int anzahl) {
		switch (kuerzel) {
			case "GK":  return (anzahl == 3) || (anzahl == 4);  // neu einsetzende Fremdsprachen können 4-stündig sein
			case "LK":  return (anzahl == 5);
			case "PJK": return (anzahl == 2) || (anzahl == 3);
			case "VTF": return (anzahl == 2);
			case "ZK":  return (anzahl == 3);
			default:    return false;
		}
	}


	/**
	 * Gibt eine Map von den Kürzeln auf die Gost-Kursart zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die Gost-Kursarten
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull GostKursart> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0)
			for (final @NotNull GostKursart k : GostKursart.values())
				_mapKuerzel.put(k.kuerzel, k);
		return _mapKuerzel;
	}


	/**
	 * Gibt eine Map von den zulässigen Kursarten auf die Gost-Kursart zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den zulässigen Kursarten auf die Gost-Kursarten
	 */
	private static @NotNull Map<@NotNull ZulaessigeKursart, @NotNull GostKursart> getMapByZulKursart() {
		if (_mapZulKursart.size() == 0)
			for (final @NotNull GostKursart k : GostKursart.values())
				for (final @NotNull ZulaessigeKursart zulKursart : k.kursarten)
					_mapZulKursart.put(zulKursart, k);
		return _mapZulKursart;
	}


	/**
	 * Gibt die Liste der zulässigen Kursarten zurück.
	 *
	 * @return die Liste der zulässigen Kursarten
	 */
	public @NotNull List<@NotNull ZulaessigeKursart> getKursarten() {
		return kursarten;
	}


	/**
	 * Gibt die Kursart aus der ID Kursart zurück.
	 *
	 * @param id    die ID der Kursart
	 *
	 * @return die Kursart
	 *
	 * @throws DeveloperNotificationException falls die ID ungültig ist
	 */
	public static @NotNull GostKursart fromID(final int id) throws DeveloperNotificationException {
		switch (id) {
			case 1: return GostKursart.LK;
			case 2: return GostKursart.GK;
			case 3: return GostKursart.ZK;
			case 4: return GostKursart.PJK;
			case 5: return GostKursart.VTF;
			default: throw new DeveloperNotificationException("Invalid ID value.");
		}
	}

	/**
	 * Liefert die Kursart anhand der Kursart-ID der Fachwahl.
	 *
	 * @param pFachwahl Das Fachwahl-Objekt.
	 * @return die Kursart anhand der Kursart-ID der Fachwahl.
	 * @throws DeveloperNotificationException falls die ID ungültig ist
	 */
	public static @NotNull GostKursart fromFachwahlOrException(final @NotNull GostFachwahl pFachwahl) throws DeveloperNotificationException {
		return fromID(pFachwahl.kursartID);
	}

	/**
	 * Gibt die Kursart aus der ID Kursart zurück.
	 *
	 * @param id    die ID der Kursart
	 *
	 * @return die Kursart oder null falls die ID ungültig ist
	 */
	public static GostKursart fromIDorNull(final int id) {
		switch (id) {
			case 1: return GostKursart.LK;
			case 2: return GostKursart.GK;
			case 3: return GostKursart.ZK;
			case 4: return GostKursart.PJK;
			case 5: return GostKursart.VTF;
			default: return null;
		}
	}

	/**
	 * Gibt die Gost-Kursart aus dem Kürzel der Kursart zurück.
	 *
	 * @param kuerzel    das Kürzel der Kursart
	 *
	 * @return die Kursart oder null, falls das Kürzel ungültig ist
	 */
	public static GostKursart fromKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}


	/**
	 * Bestimmt die Gost-Kursart anhand der übergebenen zulässigen Kursart
	 *
	 * @param kursart   die Kursart
	 *
	 * @return die Gost-Kursart
	 */
	public static GostKursart fromKursart(final ZulaessigeKursart kursart) {
		return getMapByZulKursart().get(kursart);
	}


	/**
	 * Berechnet mit der Formel pFachID * {@link #FACHART_ID_FAKTOR} + pKursartID die ID der Fachart.
	 *
	 * @param  pFachID    Die DatenbankID des Faches.
	 * @param  pKursartID Die DatenbankID der Kursart.
	 *
	 * @return pFachID * {@link #FACHART_ID_FAKTOR} + pKursartID
	 */
	public static long getFachartID(final long pFachID, final int pKursartID) {
		return pFachID * FACHART_ID_FAKTOR + pKursartID;
	}

	/**
	 * Berechnet anhand des Fachwahl-Objektes die FachartID.
	 * @param pFachwahl Das Fachwahl-Objekt.
	 *
	 * @return pFachwahl.fachID * {@link #FACHART_ID_FAKTOR} + pFachwahl.kursartID
	 */
	public static long getFachartIDByFachwahl(final @NotNull GostFachwahl pFachwahl) {
		return getFachartID(pFachwahl.fachID, pFachwahl.kursartID);
	}

	/**
	 * Berechnet anhand des Kurs-Objektes die FachartID.
	 *
	 * @param pKurs Das Kurs-Objekt.
	 *
	 * @return pKurs.fachID * {@link #FACHART_ID_FAKTOR} + pKurs.kursartID
	 */
	public static long getFachartIDByKurs(final @NotNull GostBlockungKurs pKurs) {
		return getFachartID(pKurs.fach_id, pKurs.kursart);
	}

	/**
	 * Berechnet anhand der Fachart-ID die Fach-ID.
	 *
	 * @param pFachartID Die ID der Fachart, welche das Fach und die Kursart kodiert.
	 *
	 * @return Ganzzahlige Division von pFachartID durch {@link #FACHART_ID_FAKTOR}
	 */
	public static long getFachID(final long pFachartID) {
		return pFachartID / FACHART_ID_FAKTOR;
	}

	/**
	 * Berechnet anhand der Fachart-ID die Kursart-ID.
	 *
	 * @param pFachartID Die ID der Fachart, welche das Fach und die Kursart kodiert.
	 *
	 * @return Rest der ganzzahligen Division von pFachartID durch {@link #FACHART_ID_FAKTOR}
	 */
	public static int getKursartID(final long pFachartID) {
		return (int) (pFachartID % FACHART_ID_FAKTOR);
	}


	@Override
	public @NotNull String toString() {
		return kuerzel;
	}

}
