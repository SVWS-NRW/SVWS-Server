package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.DTOUtils;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.SetUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostBlockungsdaten}.<br>
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class GostBlockungsdatenManager {

	/** Die Blockungsdaten, die im Manager vorhanden sind. */
	private final @NotNull GostBlockungsdaten _daten;

	/** Der Fächermanager mit den Fächern der gymnasialen Oberstufe. */
	private final @NotNull GostFaecherManager _faecherManager;

	/** Ein Comparator für Kurse der Blockung. Dieser vergleicht nur die Kursnummern! */
	private static final @NotNull Comparator<GostBlockungKurs> _compKursnummer =
			(final @NotNull GostBlockungKurs a, final @NotNull GostBlockungKurs b) -> Integer.compare(a.nummer, b.nummer);

	/** Ein Comparator für Schienen der Blockung */
	private static final @NotNull Comparator<GostBlockungSchiene> _compSchiene =
			(final @NotNull GostBlockungSchiene a, final @NotNull GostBlockungSchiene b) -> Integer.compare(a.nummer, b.nummer);

	/** Ein Comparator für die Lehrkräfte eines Kurses */
	private static final @NotNull Comparator<GostBlockungKursLehrer> _compLehrkraefte =
			(final @NotNull GostBlockungKursLehrer a, final @NotNull GostBlockungKursLehrer b) -> {
				final int result = Integer.compare(a.reihenfolge, b.reihenfolge);
				if (result != 0)
					return result;
				return Long.compare(a.id, b.id);
			};

	/** Ein Comparator für die Ergebnisse sortiert nach ID. */
	private static final @NotNull Comparator<GostBlockungsergebnis> _compErgebnisseNachID =
			(final @NotNull GostBlockungsergebnis a, final @NotNull GostBlockungsergebnis b) -> Long.compare(a.id, b.id);

	/** Ein Comparator für die Schüler. */
	private final @NotNull Comparator<Schueler> _compSchueler;

	/** Ein Comparator für die Fachwahlen (SCHÜLERID, FACH, KURSART) */
	private final @NotNull Comparator<GostFachwahl> _compFachwahlen;

	/** Ein Comparator für die {@link GostBlockungsergebnis} nach ihrer Bewertung. */
	private final @NotNull Comparator<GostBlockungsergebnis> _compErgebnisse = new GostBlockungsergebnisComparator();

	/** Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER) */
	private final @NotNull Comparator<GostBlockungKurs> _compKurs_kursart_fach_kursnummer;

	/** Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER). */
	private final @NotNull Comparator<GostBlockungKurs> _compKurs_fach_kursart_kursnummer;

	/** Ein Comparator für Regeln der Blockung */
	private final @NotNull Comparator<GostBlockungRegel> _compRegel;

	/** Eine interne Hashmap zum schnellen Zugriff auf die Kurse anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<Long, GostBlockungKurs> _map_idKurs_kurs = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Listen der Kurse, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs. */
	private final @NotNull HashMap2D<Long, Integer, List<GostBlockungKurs>> _map2d_idFach_idKursart_kurse = new HashMap2D<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Listen der Fachwahlen, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs. */
	private final @NotNull HashMap2D<Long, Integer, List<GostFachwahl>> _map2d_idFach_idKursart_fachwahlen = new HashMap2D<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<Long, GostBlockungSchiene> _map_idSchiene_schiene = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<Long, GostBlockungRegel> _map_idRegel_regel = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}. */
	private final @NotNull Map<GostKursblockungRegelTyp, List<GostBlockungRegel>> _map_regeltyp_regeln = new ArrayMap<>(GostKursblockungRegelTyp.values());

	/** Eine interne Hashmap zum Multi-Key-Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}. */
	private final @NotNull HashMap<LongArrayKey, GostBlockungRegel> _map_multikey_regeln = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schueler anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<Long, Schueler> _map_idSchueler_schueler = new HashMap<>();

	/** Schüler-ID --> List<Fachwahl> = Die Fachwahlen des Schülers der jeweiligen Fachart. */
	private final @NotNull HashMap<Long, List<GostFachwahl>> _map_idSchueler_fachwahlen = new HashMap<>();

	/** (Schüler-ID, Fach-ID) --> Kursart = Die Fachwahl des Schülers die dem Fach die Kursart zuordnet. */
	private final @NotNull HashMap2D<Long, Long, GostFachwahl> _map2d_idSchueler_idFach_fachwahl = new HashMap2D<>();

	/** Fachart-ID --> List<Fachwahl> = Die Fachwahlen einer Fachart. */
	private final @NotNull HashMap<Long, List<GostFachwahl>> _map_idFachart_fachwahlen = new HashMap<>();

	/** Ergebnis-ID --> {@link GostBlockungsergebnis} */
	private final @NotNull HashMap<Long, GostBlockungsergebnis> _map_idErgebnis_Ergebnis = new HashMap<>();

	/** Ergebnis-ID --> {@link GostBlockungsergebnisManager} */
	private final @NotNull HashMap<Long, GostBlockungsergebnisManager> _map_idErgebnis_ErgebnisManager = new HashMap<>();

	/** Eine sortierte, gecachte Menge der Kurse nach: (FACH, KURSART, KURSNUMMER). */
	private final @NotNull List<GostBlockungKurs> _list_kurse_sortiert_fach_kursart_kursnummer = new ArrayList<>();

	/** Eine sortierte, gecachte Menge der Kurse nach: (KURSART, FACH, KURSNUMMER) */
	private final @NotNull List<GostBlockungKurs> _list_kurse_sortiert_kursart_fach_kursnummer = new ArrayList<>();

	/** Die maximale Zeit in Millisekunden die der Blockungsalgorithmus verwenden darf. */
	private long _maxTimeMillis = 1000;

	/** Alle Warnungen, welche die GUI darstellen sollte. */
	private @NotNull final List<String> _warnungen = new ArrayList<>();


	/**
	 * Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager.
	 *
	 * @param daten           die Blockungsdaten
	 * @param faecherManager  der Fächer-Manager
	 */
	public GostBlockungsdatenManager(final @NotNull GostBlockungsdaten daten, final @NotNull GostFaecherManager faecherManager) {
		_faecherManager = faecherManager;
		_compKurs_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_compKurs_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
		_compFachwahlen = createComparatorFachwahlen();
		_compRegel = createComparatorRegeln();
		_compSchueler = createComparatorSchueler();

		// Tiefe Kopie (deep copy) der GostBlockungsdaten.
		_daten = new GostBlockungsdaten();
		_daten.id = daten.id;
		_daten.name = daten.name;
		_daten.abijahrgang = daten.abijahrgang;
		_daten.gostHalbjahr = daten.gostHalbjahr;
		_daten.istAktiv = daten.istAktiv;

		// Kopieren und Mappings aufbauen.
		schieneAddListe(daten.schienen); 		// Muss vor den Kursen erzeugt werden.
		fachwahlAddListe(daten.fachwahlen); 	// Muss vor den Schülern erzeugt werden.
		schuelerAddListe(daten.schueler); 		// Muss vor den Regeln erzeugt werden.
		kursAddListe(daten.kurse);
		regelAddListe(daten.regeln);  			// Muss vor den Ergebnissen erzeugt werden.
		ergebnisAddListe(daten.ergebnisse);
	}

	/**
	 * Liefert alle Warnungen, die dem User angezeigt werden sollen.
	 *
	 * @return alle Warnungen, die dem User angezeigt werden sollen.
	 */
	public @NotNull List<String> getWarnungen() {
		return _warnungen;
	}

	/**
	 * Liefert eine Kurzdarstellung der Kursart mit der übergebenen ID.
	 *
	 * @param kursart  Die ID der Kursart.
	 *
	 * @return eine Kurzdarstellung der Kursart mit der übergebenen ID.
	 */
	public @NotNull String toStringKursartSimple(final int kursart) {
		final GostKursart gKursart = GostKursart.fromIDorNull(kursart);
		return (gKursart == null) ? ("[Kursart-ID = " + kursart + " (ohne Mapping)]") : gKursart.kuerzel;
	}

	/**
	 * Liefert möglichst viele Informationen zum Kurs mit der übergebenen ID.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return möglichst viele Informationen zum Kurs mit der übergebenen ID.
	 */
	public @NotNull String toStringKurs(final long idKurs) {
		final GostBlockungKurs kurs = _map_idKurs_kurs.get(idKurs);
		if (kurs == null)
			return "[Kurs (" + idKurs + ") ohne Mapping]";

		final GostFach gFach = _faecherManager.get(kurs.fach_id);
		@NotNull String sFach = "Fach-ID = " + kurs.fach_id + " (ohne Mapping)";
		if (gFach != null)
			sFach = (gFach.kuerzelAnzeige == null) ? ("Fach-ID = " + kurs.fach_id + " (ohne 'kuerzelAnzeige')") : gFach.kuerzelAnzeige;

		return "[Kurs " + sFach + "-" + toStringKursartSimple(kurs.kursart) + kurs.nummer + (kurs.suffix.isEmpty() ? "" : "-") + kurs.suffix + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung des Kurses mit der übergebenen ID.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return eine Kurzdarstellung des Kurses mit der übergebenen ID.
	 */
	public @NotNull String toStringKursSimple(final long idKurs) {
		final GostBlockungKurs kurs = _map_idKurs_kurs.get(idKurs);
		if (kurs == null)
			return "[Kurs (" + idKurs + ") ohne Mapping]";

		return "(" + kurs.id + ") " + toStringFachSimple(kurs.fach_id) + "-" + toStringKursartSimple(kurs.kursart) + kurs.nummer
				+ (kurs.suffix.isEmpty() ? "" : "-") + kurs.suffix;
	}

	/**
	 * Liefert eine Kurzdarstellung des Kurses (ohne ID, außer der ID ist kein Kurs zugeordnet).
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return eine Kurzdarstellung des Kurses (ohne ID, außer der ID ist kein Kurs zugeordnet).
	 */
	public @NotNull String toStringKursSimpleOhneID(final long idKurs) {
		final GostBlockungKurs kurs = _map_idKurs_kurs.get(idKurs);
		if (kurs == null)
			return "[Kurs (" + idKurs + ") ohne Mapping]";

		return toStringFachSimple(kurs.fach_id) + "-" + toStringKursartSimple(kurs.kursart) + kurs.nummer
				+ (kurs.suffix.isEmpty() ? "" : "-") + kurs.suffix;
	}

	/**
	 * Liefert eine Kurzdarstellung des Faches mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return eine Kurzdarstellung des Faches mit der übergebenen ID.
	 */
	public @NotNull String toStringFachSimple(final long idFach) {
		final GostFach gFach = _faecherManager.get(idFach);
		if (gFach == null)
			return "[Fach-ID = " + idFach + " (ohne Mapping)]";
		if (gFach.kuerzelAnzeige == null)
			return "[Fach-ID = " + idFach + " (ohne 'kuerzelAnzeige')]";
		return gFach.kuerzelAnzeige;
	}

	/**
	 * Liefert eine Kurzdarstellung der Fachart (Fach, Kursart).
	 *
	 * @param idFach   Die Datenbank-ID des Faches.
	 * @param kursart  Die Datenbank-ID der Kursart.
	 *
	 * @return eine Kurzdarstellung der Fachart (Fach, Kursart).
	 */
	public @NotNull String toStringFachartSimple(final long idFach, final int kursart) {
		return toStringFachSimple(idFach) + "-" + toStringKursartSimple(kursart);
	}

	/**
	 * Liefert eine Kurzdarstellung der Fachart (Fach, Kursart).
	 *
	 * @param idFachart  Die Fachart (zusammengesetzt aus Fach und Kursart)
	 *
	 * @return eine Kurzdarstellung der Fachart (Fach, Kursart).
	 */
	public @NotNull String toStringFachartSimpleByFachartID(final long idFachart) {
		final long idFach = GostKursart.getFachID(idFachart);
		final int kursart = GostKursart.getKursartID(idFachart);
		return toStringFachSimple(idFach) + "-" + toStringKursartSimple(kursart);
	}

	/**
	 * Liefert möglichst viele Informationen zum Schüler mit der übergebenen ID.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return möglichst viele Informationen zum Schüler mit der übergebenen ID.
	 */
	public @NotNull String toStringSchueler(final long idSchueler) {
		final Schueler schueler = _map_idSchueler_schueler.get(idSchueler);
		if (schueler == null)
			return "[Schüler (" + idSchueler + ") ohne Mapping]";
		return "[Schüler (" + schueler.id + "): " + schueler.nachname + ", " + schueler.vorname + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung des Schüler mit der übergebenen ID.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return eine Kurzdarstellung des Schüler mit der übergebenen ID.
	 */
	public @NotNull String toStringSchuelerSimple(final long idSchueler) {
		final Schueler schueler = _map_idSchueler_schueler.get(idSchueler);
		if (schueler == null)
			return "[Schüler (" + idSchueler + ") ohne Mapping]";
		return schueler.nachname + ", " + schueler.vorname;
	}

	/**
	 * Liefert möglichst viele Informationen zur Schiene mit der übergebenen ID.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return möglichst viele Informationen zur Schiene mit der übergebenen ID.
	 */
	public @NotNull String toStringSchiene(final long idSchiene) {
		final GostBlockungSchiene schiene = _map_idSchiene_schiene.get(idSchiene);
		if (schiene == null)
			return "[Schiene (" + idSchiene + ") ohne Mapping]";
		return "[Schiene: ID " + schiene.id + ", Nr. " + schiene.nummer + ", Bez. " + schiene.bezeichnung + ", Stunden " + schiene.wochenstunden + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung zur Schiene mit der übergebenen ID.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return eine Kurzdarstellung zur Schiene mit der übergebenen ID.
	 */
	public @NotNull String toStringSchieneSimple(final long idSchiene) {
		final GostBlockungSchiene schiene = _map_idSchiene_schiene.get(idSchiene);
		if (schiene == null)
			return "[Schiene (" + idSchiene + ") ohne Mapping]";
		return "Schiene Nr. " + schiene.nummer;
	}

	/**
	 * Liefert möglichst viele Informationen zur Lehrkraft mit der übergebenen ID.
	 *
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 * @param idLehrkraft  Die Datenbank-ID der Lehrkraft.
	 *
	 * @return möglichst viele Informationen zur Lehrkraft mit der übergebenen ID.
	 */
	public @NotNull String toStringKursLehrkraft(final long idKurs, final long idLehrkraft) {
		final GostBlockungKurs kurs = _map_idKurs_kurs.get(idKurs);
		if (kurs == null)
			return "[Lehrkraft (ID=" + idLehrkraft + ")]";
		for (final @NotNull GostBlockungKursLehrer lehrer : kurs.lehrer)
			if (lehrer.id == idLehrkraft)
				return "[Lehrkraft (ID=" + idLehrkraft + ") " + lehrer.kuerzel + "]";
		return "[Lehrkraft (ID=" + idLehrkraft + ")]";
	}

	/**
	 * Liefert eine Kurzdarstellung zur übergebenen Fachwahl eines Schülers.
	 *
	 * @param gFachwahl  Das {@link GostFachwahl}-Objekt.
	 *
	 * @return eine Kurzdarstellung zur übergebenen Fachwahl eines Schülers.
	 */
	public String toStringFachwahlSimple(final @NotNull GostFachwahl gFachwahl) {
		return toStringSchuelerSimple(gFachwahl.schuelerID) + " wählt " + toStringFachartSimple(gFachwahl.fachID, gFachwahl.kursartID);
	}

	/**
	 * Liefert möglichst viele Informationen zur Regel mit der übergebenen ID.
	 *
	 * @param idRegel  Die Datenbank-ID der Regel.
	 *
	 * @return möglichst viele Informationen zur Regel mit der übergebenen ID.
	 */
	public @NotNull String toStringRegel(final long idRegel) {
		final GostBlockungRegel regel = _map_idRegel_regel.get(idRegel);
		if (regel == null)
			return "[Regel (" + idRegel + ") ohne Mapping]";
		return "[Regel (" + regel.id + ", Nr. " + regel.typ + "): " + regel.parameter + "]";
	}

	private @NotNull Comparator<GostBlockungRegel> createComparatorRegeln() {
		final @NotNull Comparator<GostBlockungRegel> comp = (final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) -> {
			// 1. Kriterium Typ
			final int cmp1 = Integer.compare(a.typ, b.typ);
			if (cmp1 != 0)
				return cmp1;

			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(a.typ);
			final int cmp2 = switch (typ) {
				case KURS_FIXIERE_IN_SCHIENE -> compareRegel_Kurs_Nummer(a, b);
				case KURS_SPERRE_IN_SCHIENE -> compareRegel_Kurs_Nummer(a, b);
				case SCHUELER_FIXIEREN_IN_KURS -> compareRegel_Schueler_Kurs(a, b);
				case SCHUELER_VERBIETEN_IN_KURS -> compareRegel_Schueler_Kurs(a, b);
				case KURS_VERBIETEN_MIT_KURS -> compareRegel_Kurs_Kurs(a, b);
				case KURS_ZUSAMMEN_MIT_KURS -> compareRegel_Kurs_Kurs(a, b);
				case KURS_MIT_DUMMY_SUS_AUFFUELLEN -> compareRegel_Kurs(a, b);
				case KURS_MAXIMALE_SCHUELERANZAHL -> compareRegel_Kurs(a, b);
				case KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN -> compareRegel_Kurs(a, b);
				case SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH -> compareRegel_Schueler_Schueler_Fach(a, b);
				case SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH -> compareRegel_Schueler_Schueler_Fach(a, b);
				case SCHUELER_ZUSAMMEN_MIT_SCHUELER -> compareRegel_Schueler_Schueler(a, b);
				case SCHUELER_VERBIETEN_MIT_SCHUELER -> compareRegel_Schueler_Schueler(a, b);
				case SCHUELER_IGNORIEREN -> compareRegel_Schueler(a, b);

				default -> 0;
			};
			if (cmp2 != 0)
				return cmp2;

			return Long.compare(a.id, b.id);
		};

		return comp;
	}

	private @NotNull Comparator<Schueler> createComparatorSchueler() {
		final @NotNull Comparator<Schueler> comp = (final @NotNull Schueler a, final @NotNull Schueler b) -> {
			final int cmpSchueler = compareSchueler(a.id, b.id);
			if (cmpSchueler != 0)
				return cmpSchueler;

			return Long.compare(a.id, b.id);
		};

		return comp;
	}

	private @NotNull Comparator<GostFachwahl> createComparatorFachwahlen() {
		final @NotNull Comparator<GostFachwahl> comp = (final @NotNull GostFachwahl a, final @NotNull GostFachwahl b) -> {
			final int cmpSchueler = compareSchueler(a.schuelerID, b.schuelerID);
			if (cmpSchueler != 0)
				return cmpSchueler;

			final int cmpFach = compareFach(a.fachID, b.fachID);
			if (cmpFach != 0)
				return cmpFach;

			return Integer.compare(a.kursartID, b.kursartID);
		};

		return comp;
	}

	private @NotNull Comparator<GostBlockungKurs> createComparatorKursFachKursartNummer() {
		final @NotNull Comparator<GostBlockungKurs> comp = (final @NotNull GostBlockungKurs a, final @NotNull GostBlockungKurs b) -> {
			final int cmpFach = compareFach(a.fach_id, b.fach_id);
			if (cmpFach != 0)
				return cmpFach;

			final int cmpKursart = Integer.compare(a.kursart, b.kursart);
			if (cmpKursart != 0)
				return cmpKursart;

			return Integer.compare(a.nummer, b.nummer);
		};

		return comp;
	}

	private @NotNull Comparator<GostBlockungKurs> createComparatorKursKursartFachNummer() {
		final @NotNull Comparator<GostBlockungKurs> comp = (final @NotNull GostBlockungKurs a, final @NotNull GostBlockungKurs b) -> {
			final int k1 = (a.kursart == GostKursart.ZK.id) ? GostKursart.GK.id : a.kursart;
			final int k2 = (b.kursart == GostKursart.ZK.id) ? GostKursart.GK.id : b.kursart;
			final int cmpKursartGKZK = Integer.compare(k1, k2);
			if (cmpKursartGKZK != 0)
				return cmpKursartGKZK;

			final int cmpFach = compareFach(a.fach_id, b.fach_id);
			if (cmpFach != 0)
				return cmpFach;

			final int cmpKursart = Integer.compare(a.kursart, b.kursart);
			if (cmpKursart != 0)
				return cmpKursart;

			return Integer.compare(a.nummer, b.nummer);
		};

		return comp;
	}

	private int compareRegel_Kurs(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpKurs1 = compareKurs_Kursart_Fach_Nummer(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 != 0)
			return cmpKurs1;

		return Long.compare(a.id, b.id);
	}

	private int compareRegel_Kurs_Nummer(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpKurs1 = compareKurs_Kursart_Fach_Nummer(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 != 0)
			return cmpKurs1;

		final int cmpSchienenNr = Long.compare(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchienenNr != 0)
			return cmpSchienenNr;

		return Long.compare(a.id, b.id);
	}

	private int compareRegel_Schueler(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpSchueler1 = compareSchueler(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 != 0)
			return cmpSchueler1;

		return Long.compare(a.id, b.id);
	}

	private int compareRegel_Schueler_Kurs(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpSchueler1 = compareSchueler(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 != 0)
			return cmpSchueler1;

		final int cmpKurs1 = compareKurs_Kursart_Fach_Nummer(a.parameter.get(1), b.parameter.get(1));
		if (cmpKurs1 != 0)
			return cmpKurs1;

		return Long.compare(a.id, b.id);
	}

	private int compareRegel_Kurs_Kurs(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpKurs1 = compareKurs_Kursart_Fach_Nummer(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 != 0)
			return cmpKurs1;

		final int cmpKurs2 = compareKurs_Kursart_Fach_Nummer(a.parameter.get(1), b.parameter.get(1));
		if (cmpKurs2 != 0)
			return cmpKurs2;

		return Long.compare(a.id, b.id);
	}

	private int compareRegel_Schueler_Schueler_Fach(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpSchueler1 = compareSchueler(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 != 0)
			return cmpSchueler1;

		final int cmpSchueler2 = compareSchueler(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchueler2 != 0)
			return cmpSchueler2;

		final int cmpFach = compareFach(a.parameter.get(2), b.parameter.get(2));
		if (cmpFach != 0)
			return cmpFach;

		return Long.compare(a.id, b.id);
	}

	private int compareRegel_Schueler_Schueler(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpSchueler1 = compareSchueler(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 != 0)
			return cmpSchueler1;

		final int cmpSchueler2 = compareSchueler(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchueler2 != 0)
			return cmpSchueler2;

		return Long.compare(a.id, b.id);
	}

	private int compareSchueler(final long idSchueler1, final long idSchueler2) {
		final Schueler a = _map_idSchueler_schueler.get(idSchueler1);
		final Schueler b = _map_idSchueler_schueler.get(idSchueler2);

		if (a == null)
			return (b == null) ? 0 : -1;

		if (b == null)
			return +1;

		final int cNachname = a.nachname.compareTo(b.nachname);
		if (cNachname != 0)
			return cNachname;

		final int cVorname = a.vorname.compareTo(b.vorname);
		if (cVorname != 0)
			return cVorname;

		return Long.compare(a.id, b.id);
	}

	private int compareFach(final long idFach1, final long idFach2) {
		final GostFach aFach = _faecherManager.get(idFach1);
		final GostFach bFach = _faecherManager.get(idFach2);

		if (aFach == null)
			return (bFach == null) ? 0 : -1;

		return (bFach == null) ? +1 : GostFaecherManager.comp.compare(aFach, bFach);
	}

	private int compareKurs_Kursart_Fach_Nummer(final long idKurs1, final long idKurs2) {
		final GostBlockungKurs aKurs = _map_idKurs_kurs.get(idKurs1);
		final GostBlockungKurs bKurs = _map_idKurs_kurs.get(idKurs2);

		if (aKurs == null)
			return (bKurs == null) ? 0 : -1;

		if (bKurs == null)
			return +1;

		final int cmpKursart = Long.compare(aKurs.kursart, bKurs.kursart);
		if (cmpKursart != 0)
			return cmpKursart;

		final int cmpFach = compareFach(aKurs.fach_id, bKurs.fach_id);
		if (cmpFach != 0)
			return cmpFach;

		final int cmpNummer = Long.compare(aKurs.fach_id, bKurs.fach_id);
		if (cmpNummer != 0)
			return cmpNummer;

		return Long.compare(aKurs.id, bKurs.id);
	}

	/**
	 * Fügt das übergebenen Ergebnis der Blockung hinzu.
	 *
	 * @param ergebnis Das {@link GostBlockungsergebnis}-Objekt, welches hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls in den Daten Inkonsistenzen sind.
	 */
	public void ergebnisAdd(final @NotNull GostBlockungsergebnis ergebnis) throws DeveloperNotificationException {
		ergebnisAddListe(ListUtils.create1(ergebnis));
	}

	/**
	 * Fügt die Menge an Ergebnissen {@link GostBlockungsergebnis} hinzu.
	 *
	 * @param ergebnismenge Die Menge an Ergebnissen.
	 *
	 * @throws DeveloperNotificationException Falls in den Daten Inkonsistenzen sind.
	 */
	public void ergebnisAddListe(final @NotNull List<GostBlockungsergebnis> ergebnismenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen
		for (final @NotNull GostBlockungsergebnis ergebnis : ergebnismenge) {
			DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
			DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);
			DeveloperNotificationException.ifNull("GostHalbjahr.fromID(" + ergebnis.gostHalbjahr + ")", GostHalbjahr.fromID(ergebnis.gostHalbjahr));
			DeveloperNotificationException.ifMapContains("_map_idErgebnis_Ergebnis", _map_idErgebnis_Ergebnis, ergebnis.id);
			DeveloperNotificationException.ifMapContains("_map_idErgebnis_ErgebnisManager", _map_idErgebnis_ErgebnisManager, ergebnis.id);
		}

		// Hinzufügen
		for (final @NotNull GostBlockungsergebnis ergebnis : ergebnismenge) {
			final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(this, ergebnis);
			DeveloperNotificationException.ifMapPutOverwrites(_map_idErgebnis_Ergebnis, ergebnis.id, ergebnis);
			DeveloperNotificationException.ifMapPutOverwrites(_map_idErgebnis_ErgebnisManager, ergebnis.id, ergebnisManager);
			_daten.ergebnisse.add(ergebnis);
		}

		// Sortieren
		_daten.ergebnisse.sort(_compErgebnisse);
	}

	// #########################################################################
	// ##########                 Kurs-Anfragen                       ##########
	// #########################################################################

	/**
	 * Liefert einen {@link GostBlockungsergebnis} aus der Liste der Ergebnisse.
	 * Wirft eine Exception, falls es keinen Listeneintrag mit dieser ID gibt.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return einen {@link GostBlockungsergebnis} aus der Liste der Ergebnisse.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public @NotNull GostBlockungsergebnis ergebnisGet(final long idErgebnis) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Es wurde kein Ergebnis mit ID(" + idErgebnis + ") gefunden!", _map_idErgebnis_Ergebnis.get(idErgebnis));
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnisManager} für das Ergebnis mit der übergebenen ID.
	 * Wirft eine Exception, falls es keinen Manager für ein Ergebnis mit dieser ID gibt.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return einen {@link GostBlockungsergebnisManager} für das Ergebnis.
	 * @throws DeveloperNotificationException Falls es keinen Manager für ein Ergebnis mit dieser ID gibt.
	 */
	public @NotNull GostBlockungsergebnisManager ergebnisManagerGet(final long idErgebnis) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Es wurde kein Ergebnis mit ID(" + idErgebnis + ") gefunden!",
				_map_idErgebnis_ErgebnisManager.get(idErgebnis));
	}

	/**
	 * Liefert TRUE, falls ein {@link GostBlockungsergebnisManager}-Objekt mit der ID existiert.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return TRUE, falls ein {@link GostBlockungsergebnisManager}-Objekt mit der ID existiert.
	 */
	public boolean ergebnisManagerExists(final long idErgebnis) {
		return _map_idErgebnis_ErgebnisManager.containsKey(idErgebnis);
	}

	/**
	 * Liefert die sortierte Menge aller {@link GostBlockungsergebnisManager}.
	 *
	 * @return die sortierte Menge aller {@link GostBlockungsergebnisManager}.
	 */
	public @NotNull List<GostBlockungsergebnisManager> ergebnisManagerGetListeUnsortiert() {
		return new ArrayList<>(_map_idErgebnis_ErgebnisManager.values());
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer Bewertung.
	 *
	 * @return Eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer Bewertung.
	 */
	public @NotNull List<GostBlockungsergebnis> ergebnisGetListeSortiertNachBewertung() {
		return new ArrayList<>(_daten.ergebnisse);
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer ID.
	 *
	 * @return Eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer ID.
	 */
	public @NotNull List<GostBlockungsergebnis> ergebnisGetListeSortiertNachID() {
		final @NotNull List<GostBlockungsergebnis> list = new ArrayList<>(_daten.ergebnisse);
		list.sort(_compErgebnisseNachID);
		return list;
	}

	/**
	 * Entfernt die Menge an {@link GostBlockungsergebnis}-Objekten anhand ihrer ID.
	 *
	 * @param listeDerErgebnisIDs  Die IDs der Ergebnisse.
	 *
	 * @throws DeveloperNotificationException Falls es keine Ergebnisse mit diesen IDs gibt.
	 */
	public void ergebnisRemoveListeByIDs(final @NotNull Set<Long> listeDerErgebnisIDs) throws DeveloperNotificationException {
		// Überprüfen
		for (final long idErgebnis : listeDerErgebnisIDs) {
			DeveloperNotificationException.ifMapNotContains("_map_idErgebnis_Ergebnis", _map_idErgebnis_Ergebnis, idErgebnis);
			DeveloperNotificationException.ifMapNotContains("_map_idErgebnis_ErgebnisManager", _map_idErgebnis_ErgebnisManager, idErgebnis);
		}

		// Entfernen des Ergebnisses.
		for (final long idErgebnis : listeDerErgebnisIDs) {
			final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
			_daten.ergebnisse.remove(e);
			_map_idErgebnis_Ergebnis.remove(e.id);
			_map_idErgebnis_ErgebnisManager.remove(e.id);
		}

		// Neusortierung nicht nötig.
	}

	/**
	 * Entfernt die Menge an {@link GostBlockungsergebnis}-Objekten.
	 *
	 * @param ergebnismenge Die Menge an Ergebnissen.
	 *
	 * @throws DeveloperNotificationException Falls es keine Ergebnisse mit diesen IDs gibt.
	 */
	public void ergebnisRemoveListe(final @NotNull List<GostBlockungsergebnis> ergebnismenge) throws DeveloperNotificationException {
		// ID kopieren, da Löschen über Objektidentität nicht funktioniert!
		final @NotNull HashSet<Long> listIDs = new HashSet<>();
		for (final @NotNull GostBlockungsergebnis e : ergebnismenge)
			listIDs.add(e.id);

		ergebnisRemoveListeByIDs(listIDs);
	}

	/**
	 * Entfernt das Ergebnis mit der übergebenen ID aus der Blockung.
	 *
	 * @param idErgebnis  Die Datenbank-ID des zu entfernenden Ergebnisses.
	 *
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public void ergebnisRemoveByID(final long idErgebnis) throws DeveloperNotificationException {
		ergebnisRemoveListeByIDs(SetUtils.create1(idErgebnis));
	}

	/**
	 * Entfernt das übergebenen Ergebnis aus der Blockung.
	 *
	 * @param ergebnis   Das zu entfernende Ergebnis.
	 *
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public void ergebnisRemove(final @NotNull GostBlockungsergebnis ergebnis) throws DeveloperNotificationException {
		ergebnisRemoveListeByIDs(SetUtils.create1(ergebnis.id));
	}

	/**
	 * Sortiert alle Ergebnisse neu (nach ihrer Bewertung).
	 *
	 * @param ergebnis  Das Ergebnis mit der neuen Bewertung.
	 *
	 * @throws DeveloperNotificationException falls die Daten inkonsistent sind.
	 */
	public void ergebnisUpdateBewertung(final @NotNull GostBlockungsergebnis ergebnis) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
		DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);

		// Ergebnisse sortieren.
		_daten.ergebnisse.sort(_compErgebnisse);
	}

	/**
	 * Revalidiert alle Ergebnisse. Dies führt zur Aktualisierung aller Ergebnisse.
	 */
	public void ergebnisAlleRevalidieren() {
		for (final GostBlockungsergebnisManager ergebnisManager : _map_idErgebnis_ErgebnisManager.values())
			ergebnisManager.stateRevalidateEverything();
	}

	/**
	 * Liefert die aktuelle Anzahl an Ergebnissen, die im Manager gespeichert sind.
	 *
	 * @return die aktuelle Anzahl an Ergebnissen, die im Manager gespeichert sind.
	 */
	public int ergebnisGetAnzahl() {
		return _daten.ergebnisse.size();
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 1. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung1Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		int summe = 0;
		summe += e.bewertung.anzahlKurseNichtZugeordnet;
		summe += e.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung1Intervall(final long idErgebnis) throws DeveloperNotificationException {
		final double summe = ergebnisGetBewertung1Wert(idErgebnis);
		return 1 - (1 / ((0.25 * summe) + 1));
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 2. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung2Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		int summe = 0;
		summe += e.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += e.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung2Intervall(final long idErgebnis) throws DeveloperNotificationException {
		final double summe = ergebnisGetBewertung2Wert(idErgebnis);
		return 1 - (1 / ((0.25 * summe) + 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 3. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung3Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		return e.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung3Intervall(final long idErgebnis) throws DeveloperNotificationException {
		int wert = ergebnisGetBewertung3Wert(idErgebnis);
		if (wert > 0)
			wert--; // Jede Kursdifferenz wird um 1 reduziert, außer die 0.
		return 1 - (1 / ((0.25 * wert) + 1));
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 4. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung4Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		return e.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung4Intervall(final long idErgebnis) throws DeveloperNotificationException {
		final int wert = ergebnisGetBewertung4Wert(idErgebnis);
		return 1 - (1 / ((0.25 * wert) + 1));
	}

	private void kursAddKursOhneSortierung(final @NotNull GostBlockungKurs kurs) throws DeveloperNotificationException {
		// Hinzufügen des Kurses.
		DeveloperNotificationException.ifMapPutOverwrites(_map_idKurs_kurs, kurs.id, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("_kurse_sortiert_fach_kursart_kursnummer", _list_kurse_sortiert_fach_kursart_kursnummer, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("_kurse_sortiert_kursart_fach_kursnummer", _list_kurse_sortiert_kursart_fach_kursnummer, kurs);
		final List<GostBlockungKurs> liste = Map2DUtils.getOrCreateArrayList(_map2d_idFach_idKursart_kurse, kurs.fach_id, kurs.kursart);
		liste.add(kurs);
		liste.sort(GostBlockungsdatenManager._compKursnummer);
		_daten.kurse.add(kurs);
	}

	/**
	 * Fügt den übergebenen Kurs zu der Blockung hinzu.
	 *
	 * @param kurs  Das {@link GostBlockungKurs}-Objekt, welches hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException falls die Daten des Kurses inkonsistent sind.
	 */
	public void kursAdd(final @NotNull GostBlockungKurs kurs) throws DeveloperNotificationException {
		kursAddListe(ListUtils.create1(kurs));
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 *
	 * @param kursmenge Die Menge an Kursen.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Kurse inkonsistent sind.
	 */
	public void kursAddListe(final @NotNull List<GostBlockungKurs> kursmenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		final @NotNull HashSet<Long> setId = new HashSet<>();
		for (final @NotNull GostBlockungKurs kAlt : _daten.kurse)
			setId.add(kAlt.id);
		final int nSchienen = schieneGetAnzahl();
		for (final @NotNull GostBlockungKurs kNeu : kursmenge) {
			DeveloperNotificationException.ifInvalidID("pKurs.id", kNeu.id);
			DeveloperNotificationException.ifNull("_faecherManager.get(pKurs.fach_id)", _faecherManager.get(kNeu.fach_id));
			DeveloperNotificationException.ifNull("GostKursart.fromIDorNull(pKurs.kursart)", GostKursart.fromIDorNull(kNeu.kursart));
			DeveloperNotificationException.ifTrue("Kurs.wochenstunden " + kNeu.wochenstunden + " < 0", kNeu.wochenstunden < 0);
			DeveloperNotificationException.ifTrue("Kurs.anzahlSchienen " + kNeu.anzahlSchienen + " zu klein!", kNeu.anzahlSchienen < 1);
			DeveloperNotificationException.ifTrue("Kurs.anzahlSchienen " + kNeu.anzahlSchienen + " zu groß!", kNeu.anzahlSchienen > nSchienen);
			DeveloperNotificationException.ifTrue("Kurs.nummer " + kNeu.nummer + " zu klein!", kNeu.nummer < 1);
			DeveloperNotificationException.ifTrue("Kurs.id " + kNeu.id + " Dopplung!", !setId.add(kNeu.id));
		}

		// Hinzufügen der Kurse.
		for (final @NotNull GostBlockungKurs gKurs : kursmenge)
			kursAddKursOhneSortierung(gKurs);

		// Sortieren der Kursmengen.
		_list_kurse_sortiert_fach_kursart_kursnummer.sort(_compKurs_fach_kursart_kursnummer);
		_list_kurse_sortiert_kursart_fach_kursnummer.sort(_compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 *
	 * @param idKurs Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs mit der übergebenen ID existiert.
	 */
	public boolean kursGetExistiert(final long idKurs) {
		return _map_idKurs_kurs.get(idKurs) != null;
	}

	/**
	 * Liefert die Anzahl an Kursen.
	 *
	 * @return Die Anzahl an Kursen.
	 */
	public int kursGetAnzahl() {
		return _map_idKurs_kurs.size();
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][-Suffix], beispielsweise D-GK1.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return Den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][-Suffix], beispielsweise D-GK1.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull String kursGetName(final long idKurs) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull GostFach gFach = _faecherManager.getOrException(kurs.fach_id);
		final @NotNull String sSuffix = "".equals(kurs.suffix) ? "" : ("-" + kurs.suffix);
		final @NotNull GostKursart kursart = GostKursart.fromID(kurs.kursart);
		return gFach.kuerzelAnzeige + "-" + kursart.kuerzel + kurs.nummer + sSuffix;
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull String kursGetNameOhneSuffix(final long idKurs) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull GostFach gFach = _faecherManager.getOrException(kurs.fach_id);
		final @NotNull GostKursart kursart = GostKursart.fromID(kurs.kursart);
		return gFach.kuerzelAnzeige + "-" + kursart.kuerzel + kurs.nummer;
	}

	/**
	 * Liefert das {@link GostBlockungKurs}-Objekt mit der übergebenen ID.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses
	 *
	 * @return das {@link GostBlockungKurs}-Objekt mit der übergebenen ID.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungKurs kursGet(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_kurs, idKurs);
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene Nummer hat. <br>
	 * Wirft eine Exceptions, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param reihenfolgeNr  Die Lehrkraft mit der Nummer, die gesucht wird.
	 *
	 * @return die Lehrkraft des Kurses, welche die angegebene Nummer hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public GostBlockungKursLehrer kursGetLehrkraftMitNummer(final long idKurs, final int reihenfolgeNr) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.reihenfolge == reihenfolgeNr)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + toStringKurs(idKurs) + " keine Lehrkraft mit ReihenfolgeNr. " + reihenfolgeNr + "!");
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene ID hat.
	 *
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 * @param idLehrkraft  Die Datenbank-ID der gesuchten Lehrkraft.
	 *
	 * @return Die Lehrkraft des Kurses, welche die angegebene ID hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public GostBlockungKursLehrer kursGetLehrkraftMitID(final long idKurs, final long idLehrkraft) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.id == idLehrkraft)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + toStringKurs(idKurs) + " keine Lehrkraft mit ID " + idLehrkraft + "!");
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param reihenfolgeNr  Die Lehrkraft mit der Nummer, die gesucht wird.
	 *
	 * @return TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 * @throws DeveloperNotificationException  Falls der Kurs nicht in der Blockung existiert.
	 */
	public boolean kursGetLehrkraftMitNummerExists(final long idKurs, final int reihenfolgeNr) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.reihenfolge == reihenfolgeNr)
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 *
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 * @param idLehrkraft  Die Datenbank-ID der gesuchten Lehrkraft.
	 *
	 * @return TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 */
	public boolean kursGetLehrkraftMitIDExists(final long idKurs, final long idLehrkraft) {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.id == idLehrkraft)
				return true;
		return false;
	}

	/**
	 * Liefert alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull List<GostBlockungKursLehrer> kursGetLehrkraefteSortiert(final long idKurs) throws DeveloperNotificationException {
		return kursGet(idKurs).lehrer;
	}

	/**
	 * Fügt die übergebene Lehrkraft zum Kurs hinzu.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param neueLehrkraft  Das {@link GostBlockungKursLehrer}-Objekt.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder die Lehrkraft oder die ReihenfolgeNr bereits im Kurs existiert.
	 */
	public void kursAddLehrkraft(final long idKurs, final @NotNull GostBlockungKursLehrer neueLehrkraft) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull List<GostBlockungKursLehrer> listOfLehrer = kurs.lehrer;
		for (final @NotNull GostBlockungKursLehrer lehrkraft : listOfLehrer) {
			DeveloperNotificationException.ifTrue(toStringKurs(idKurs) + " hat bereits " + toStringKursLehrkraft(idKurs, lehrkraft.id),
					lehrkraft.id == neueLehrkraft.id);
			DeveloperNotificationException.ifTrue(
					toStringKurs(idKurs) + " hat bereits " + toStringKursLehrkraft(idKurs, lehrkraft.id) + " mit Reihenfolge " + lehrkraft.reihenfolge,
					lehrkraft.reihenfolge == neueLehrkraft.reihenfolge);
		}
		// Hinzufügen
		listOfLehrer.add(neueLehrkraft);
		listOfLehrer.sort(GostBlockungsdatenManager._compLehrkraefte);

		// Alle Ergebnisse revalidieren, damit die Bewertung aktuell ist.
		ergebnisAlleRevalidieren();
	}

	/**
	 * Löscht aus dem übergebenen Kurs die angegebene Lehrkraft.
	 *
	 * @param idKurs           Die Datenbank-ID des Kurses.
	 * @param idAlteLehrkraft  Die Datenbank-ID des {@link GostBlockungKursLehrer}-Objekt.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 */
	public void kursRemoveLehrkraft(final long idKurs, final long idAlteLehrkraft) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull List<GostBlockungKursLehrer> listOfLehrer = kurs.lehrer;
		for (int i = 0; i < listOfLehrer.size(); i++)
			if (listOfLehrer.get(i).id == idAlteLehrkraft) {
				listOfLehrer.remove(listOfLehrer.get(i));

				// Alle Ergebnisse revalidieren, damit die Bewertung aktuell ist.
				ergebnisAlleRevalidieren();
				return;
			}
		throw new DeveloperNotificationException(toStringKurs(idKurs) + " enthält nicht " + toStringKursLehrkraft(idKurs, idAlteLehrkraft));
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull List<GostBlockungKurs> kursGetListeSortiertNachFachKursartNummer() {
		return _list_kurse_sortiert_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull List<GostBlockungKurs> kursGetListeSortiertNachKursartFachNummer() {
		return _list_kurse_sortiert_kursart_fach_kursnummer;
	}

	/**
	 * Liefert eine nach Kursnummer sortiere Liste der Kurse für das angegebenen Fach und die angegebene Kursart.
	 *
	 * @param idFach      die ID des Fachs
	 * @param idKursart   die ID der Kursart
	 *
	 * @return die sortiere Liste der Kurse für das Fach und die Kursart
	 */
	public @NotNull List<GostBlockungKurs> kursGetListeByFachUndKursart(final long idFach, final int idKursart) {
		final List<GostBlockungKurs> liste = _map2d_idFach_idKursart_kurse.getOrNull(idFach, idKursart);
		if (liste == null)
			return new ArrayList<>();
		liste.sort(GostBlockungsdatenManager._compKursnummer);
		return liste;
	}

	/**
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Der Kurs muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public boolean kursGetIsRemoveAllowed(final long idKurs) throws DeveloperNotificationException {
		return (_map_idKurs_kurs.get(idKurs) != null) && getIstBlockungsVorlage();
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund von Regeln in der angegebenen Schiene verboten ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund von Regeln in der angegebenen Schiene verboten ist.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public boolean kursGetIstVerbotenInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		if (kursGetHatSperrungInSchiene(idKurs, idSchiene))
			return true;

		final int nummer = schieneGet(idSchiene).nummer;
		final int kursart = kursGet(idKurs).kursart;

		// KURSART_ALLEIN_IN_SCHIENEN_VON_BIS
		// Zugriff ist O(1) durch "regelGetListeOfTyp" und da jede Regel dieses Typs getestet werden muss.
		for (final @NotNull GostBlockungRegel regel : regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS))
			if ((nummer >= regel.parameter.get(1)) && (nummer <= regel.parameter.get(2))) { // Schiene im Intervall?
				if (regel.parameter.get(0) != kursart)
					return true;
			} else {
				if (regel.parameter.get(0) == kursart)
					return true;
			}

		// KURSART_SPERRE_SCHIENEN_VON_BIS
		// Zugriff ist O(1) durch "regelGetListeOfTyp" und da jede Regel dieses Typs getestet werden muss.
		for (final @NotNull GostBlockungRegel regel : regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS))
			if (((nummer >= regel.parameter.get(1)) && (nummer <= regel.parameter.get(2)))  // Schiene im Intervall?
					&& (regel.parameter.get(0) == kursart))
				return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 * @throws DeveloperNotificationException falls die Schiene nicht existiert.
	 */
	public boolean kursGetHatSperrungInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return _map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene gesperrt hat.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene gesperrt hat.
	 * @throws DeveloperNotificationException falls die Schiene oder die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel kursGetRegelGesperrtInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return DeveloperNotificationException.ifNull("" + toStringKurs(idKurs) + " ist nicht gesperrt in Schiene " + toStringSchiene(idSchiene) + "!",
				_map_multikey_regeln.get(key));
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist.
	 * @throws DeveloperNotificationException falls die Schiene nicht existiert.
	 */
	public boolean kursGetHatFixierungInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return _map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert hat.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene fixiert hat.
	 * @throws DeveloperNotificationException falls die Schiene oder die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel kursGetRegelFixierungInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return DeveloperNotificationException.ifNull(toStringKurs(idKurs) + " ist nicht fixiert in Schiene " + toStringSchiene(idSchiene) + "!",
				_map_multikey_regeln.get(key));
	}

	/**
	 * Liefert TRUE, falls der Kurs nicht nicht vollständig fixiert ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs nicht nicht vollständig fixiert ist.
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public boolean kursIstWeitereFixierungErlaubt(final long idKurs) throws DeveloperNotificationException {
		final int anzahlSchienen = kursGet(idKurs).anzahlSchienen;

		int anzahlFixierungen = 0;
		for (int nr = 1; nr <= schieneGetAnzahl(); nr++) {
			final @NotNull LongArrayKey kFixierungAlt = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr });
			final GostBlockungRegel rFixierungAlt = regelGetByLongArrayKeyOrNull(kFixierungAlt);
			if (rFixierungAlt != null)
				anzahlFixierungen++;
		}

		return anzahlFixierungen < anzahlSchienen;
	}

	/**
	 * Liefert die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL.
	 */
	public GostBlockungRegel kursGetRegelDummySchuelerOrNull(final long idKurs) {
		for (final @NotNull GostBlockungRegel regel : regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN))
			if (regel.parameter.get(0) == idKurs)
				return regel;
		return null;
	}

	/**
	 * Liefert ein Set aller Kurs-IDs.
	 *
	 * @return ein Set aller Kurs-IDs.
	 */
	public @NotNull Set<Long> kursmengeGetSetDerIDs() {
		final @NotNull HashSet<Long> setKursID = new HashSet<>();
		for (final @NotNull GostBlockungKurs kurs : _list_kurse_sortiert_fach_kursart_kursnummer)
			setKursID.add(kurs.id);
		return setKursID;
	}

	/**
	 * Entfernt alle Kurse mit den übergebenen IDs aus der Blockung.
	 * <br>(1) Überprüft, ob es eine Blockungsvorlage ist und ob alle IDs existieren, sonst Exception.
	 * <br>(2) Entfernt dann alle Kurse aus den Datenstrukturen.
	 * <br>(3) Entfernt dann alle Regeln, die einen der Kurse tangieren.
	 * <br>(4) Dann muss der Client den ErgebnisManager über die Löschung des Kurses informieren.
	 *
	 * @param idKurse  Die Datenbank-IDs der zu entfernenden Kurse.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void kurseRemoveByID(final @NotNull Set<Long> idKurse) throws DeveloperNotificationException {
		// (1) Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Ein Löschen von Kursen ist nur bei einer Blockungsvorlage erlaubt!", !getIstBlockungsVorlage());
		for (final long idKurs : idKurse)
			DeveloperNotificationException.ifTrue("Löschen von Kurs.id=" + idKurs + " nicht möglich, da nicht vorhanden!", !kursGetExistiert(idKurs));

		// (2) Entfernen des Kurses.
		for (final long idKurs : idKurse) {
			final @NotNull GostBlockungKurs kurs = this.kursGet(idKurs);
			_list_kurse_sortiert_fach_kursart_kursnummer.remove(kurs); // Neusortierung nicht nötig.
			_list_kurse_sortiert_kursart_fach_kursnummer.remove(kurs); // Neusortierung nicht nötig.
			Map2DUtils.removeFromListAndTrimOrException(_map2d_idFach_idKursart_kurse, kurs.fach_id, kurs.kursart, kurs);
			DeveloperNotificationException.ifMapRemoveFailes(_map_idKurs_kurs, idKurs);
			_daten.kurse.remove(kurs);
		}

		// (3) Sammle alle Regeln, welche die Kurse enthalten und lösche sie.
		final @NotNull HashSet<Long> regelIDs = new HashSet<>();
		for (final @NotNull GostBlockungRegel regel : _daten.regeln)
			for (final long idKurs : idKurse)
				if (regelGetHatKursIDs(regel, idKurs)) {
					regelIDs.add(regel.id);
					break;
				}
		regelRemoveListeByIDsOhneRevalidierung(regelIDs);

		// (4) Der Client informiert danach den EINEN ErgebnisManager, welcher dann alles revalidiert.
	}

	/**
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung.
	 *
	 * @param idKurs  Die Datenbank-ID des zu entfernenden Kurses.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public void kursRemoveByID(final long idKurs) throws DeveloperNotificationException {
		kurseRemoveByID(SetUtils.create1(idKurs));
	}

	/**
	 * Entfernt den übergebenen Kurs aus der Blockung.
	 *
	 * @param kurs  Der zu entfernende Kurs.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public void kursRemove(final @NotNull GostBlockungKurs kurs) throws DeveloperNotificationException {
		kurseRemoveByID(SetUtils.create1(kurs.id));
	}

	/**
	 * Entfernt alle {@link GostBlockungKurs}-Objekte.
	 *
	 * @param kurse  Die zu entfernenden {@link GostBlockungKurs}-Objekte.
	 *
	 * @throws DeveloperNotificationException falls einer der Kurse nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void kurseRemove(final @NotNull List<GostBlockungKurs> kurse) throws DeveloperNotificationException {
		// Kopieren der IDs.
		final @NotNull HashSet<Long> idKurse = new HashSet<>();
		for (final @NotNull GostBlockungKurs kursExtern : kurse)
			idKurse.add(kursExtern.id);

		// Delegieren an die andere Methode.
		kurseRemoveByID(idKurse);
	}

	/**
	 * Kombiniert zwei Kurse zu einem Kurs. Die Regel  {@link GostKursblockungRegelTyp#KURS_MIT_DUMMY_SUS_AUFFUELLEN}
	 * muss dabei ggf. auch kombiniert werden, wobei eine existierende Regel recycled wird.
	 *
	 * @param idKursID1keep    Die Kurs-ID des Ziel-Kurses (wird nicht gelöscht).
	 * @param idKursID2delete  Die Kurs-ID des Quell-Kurses (wird gelöscht).
	 * @throws DeveloperNotificationException falls es keine Blockungsvorlage ist, oder die Kurse nicht existieren, oder die Kurse identisch sind.
	 */
	public void kursMerge(final long idKursID1keep, final long idKursID2delete) throws DeveloperNotificationException {
		// (1) Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Kurse müssen sich unterscheiden!", idKursID1keep == idKursID2delete);
		DeveloperNotificationException.ifTrue("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!", !getIstBlockungsVorlage());
		DeveloperNotificationException.ifTrue("Die ID=" + idKursID1keep + " des Ziel Kurses-gibt es nicht!", !_map_idKurs_kurs.containsKey(idKursID1keep));
		DeveloperNotificationException.ifTrue("Die ID=" + idKursID2delete + " des Quell-Kurses gibt es nicht!", !_map_idKurs_kurs.containsKey(idKursID2delete));

		// (2) Zunächst wird die Regel "KURS_MIT_DUMMY_SUS_AUFFUELLEN" angepasst.
		final GostBlockungRegel regelKursKeep = regelGet_KURS_MIT_DUMMY_SUS_AUFFUELLEN(idKursID1keep);
		final GostBlockungRegel regelKursDelete = regelGet_KURS_MIT_DUMMY_SUS_AUFFUELLEN(idKursID2delete);

		if (regelKursDelete != null) {
			if (regelKursKeep != null) {
				// Keep-Regel += Delete-Regel --> Delete-Regel wird in (3) gelöscht.
				final long summe = regelKursDelete.parameter.get(1) + regelKursKeep.parameter.get(1);
				regelRemove(regelKursKeep);
				regelKursKeep.parameter.set(1, summe);
				regelAdd(regelKursKeep);
			} else {
				// Delete-Regel wird zur Keep-Regel. (Löschen, Verändern, Hinzufügen)
				regelRemove(regelKursDelete);
				regelKursDelete.parameter.set(0, idKursID1keep);
				regelAdd(regelKursDelete);
			}
		}

		// (3) Dann wird erst der Kurs komplett gelöscht.
		kurseRemoveByID(SetUtils.create1(idKursID2delete));
	}

	/**
	 * Setzt den Suffix des Kurses.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 * @param  suffix  Der neue Suffix des Kurses.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht in der Blockung existiert.
	 */
	public void kursSetSuffix(final long idKurs, final @NotNull String suffix) throws DeveloperNotificationException {
		kursGet(idKurs).suffix = suffix;
	}

	// #########################################################################
	// ##########                Schiene-Anfragen                     ##########
	// #########################################################################

	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 * <br>: Wichtig: Beim Ergebnismanager müssen danach die Schienen auch hinzugefügt werden!
	 *
	 * @param schiene  Die hinzuzufügende Schiene.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public void schieneAdd(final @NotNull GostBlockungSchiene schiene) throws DeveloperNotificationException {
		schieneAddListe(ListUtils.create1(schiene));
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 * <br>: Wichtig: Beim Ergebnismanager müssen danach die Schienen auch hinzugefügt werden!
	 *
	 * @param schienenmenge  Die Menge an Schienen.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public void schieneAddListe(final @NotNull List<GostBlockungSchiene> schienenmenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		final @NotNull HashSet<Integer> setNr = new HashSet<>();
		final @NotNull HashSet<Long> setId = new HashSet<>();
		for (final @NotNull GostBlockungSchiene sAlt : _daten.schienen) {
			setId.add(sAlt.id);
			setNr.add(sAlt.nummer);
		}
		for (final @NotNull GostBlockungSchiene sNeu : schienenmenge) {
			DeveloperNotificationException.ifInvalidID("Schiene.id", sNeu.id);
			DeveloperNotificationException.ifTrue("Schiene.bezeichnung darf nicht leer sein!", "".equals(sNeu.bezeichnung));
			DeveloperNotificationException.ifTrue("Schienen-Nr. " + sNeu.nummer + " < 1", sNeu.nummer < 1);
			DeveloperNotificationException.ifTrue("Schienen-WochenStd. " + sNeu.wochenstunden + " < 1", sNeu.wochenstunden < 1);
			DeveloperNotificationException.ifTrue("Schienen-ID-Dopplung " + sNeu.id, !setId.add(sNeu.id));
			DeveloperNotificationException.ifTrue("Schienen-Nr-Dopplung " + sNeu.id, !setNr.add(sNeu.nummer));
		}
		for (int nr = 1; nr <= _daten.schienen.size() + schienenmenge.size(); nr++)
			DeveloperNotificationException.ifTrue("Schienen-Nr. " + nr + " fehlt in der Reihenfolge!", !setNr.contains(nr));

		// Hinzufügen der Schienen.
		for (final @NotNull GostBlockungSchiene schiene : schienenmenge) {
			_map_idSchiene_schiene.put(schiene.id, schiene);
			_daten.schienen.add(schiene);
		}

		// Sortieren der Schienenmenge.
		_daten.schienen.sort(GostBlockungsdatenManager._compSchiene);
	}

	/**
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return Das zugehörige {@link GostBlockungSchiene} Objekt.
	 * @throws DeveloperNotificationException Falls die Schiene nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungSchiene schieneGet(final long idSchiene) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("_mapSchienen.get(" + idSchiene + ")", _map_idSchiene_schiene.get(idSchiene));
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 */
	public boolean schieneGetExistiert(final long idSchiene) {
		return _map_idSchiene_schiene.get(idSchiene) != null;
	}

	/**
	 * Liefert die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 *
	 * @return Die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 */
	public @NotNull List<GostBlockungSchiene> schieneGetListe() {
		return new ArrayList<>(_daten.schienen);
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die ID der Schiene nicht existiert.
	 */
	public boolean schieneGetIsRemoveAllowed(final long idSchiene) throws DeveloperNotificationException {
		schieneGet(idSchiene);
		return getIstBlockungsVorlage();
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#bezeichnung} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene    Die Datenbank-ID der Schiene.
	 * @param bezeichnung  Die neue Bezeichnung.
	 *
	 * @throws DeveloperNotificationException Falls die ID der Schiene nicht existiert.
	 */
	public void schienePatchBezeichnung(final long idSchiene, final @NotNull String bezeichnung) throws DeveloperNotificationException {
		schieneGet(idSchiene).bezeichnung = bezeichnung;
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#wochenstunden} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene      Die Datenbank-ID der Schiene.
	 * @param wochenstunden  Die neuen Wochenstunden.
	 */
	public void schienePatchWochenstunden(final long idSchiene, final int wochenstunden) {
		schieneGet(idSchiene).wochenstunden = wochenstunden;
	}

	/**
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung.
	 * Konsequenz: <br>
	 * (1) Das Löschen der Schiene muss erlaubt sein und die Schiene muss existieren, sonst Exception. <br>
	 * (2) Die Schiene wird entfernt und Schienen mit größerer Nr. werden um 1 reduziert. <br>
	 * (3) Die Regeln müssen bei Schienen-Nummern angepasst werden. <br>
	 *
	 * @param idSchiene  Die Datenbank-ID der zu entfernenden Schiene.
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public void schieneRemoveByID(final long idSchiene) throws DeveloperNotificationException {
		// (1)
		DeveloperNotificationException.ifTrue("Ein Löschen einer Schiene ist nur bei einer Blockungsvorlage erlaubt!", !getIstBlockungsVorlage());
		final @NotNull GostBlockungSchiene schieneR = this.schieneGet(idSchiene);
		for (final @NotNull GostBlockungsergebnisManager eManager : _map_idErgebnis_ErgebnisManager.values())
			DeveloperNotificationException.ifTrue("Schiene kann nicht gelöscht werden, da sie Kurse enthält!", !eManager.getOfSchieneIstLeer(idSchiene));

		// (2)
		_map_idSchiene_schiene.remove(idSchiene);
		_daten.schienen.remove(schieneR);
		for (final @NotNull GostBlockungSchiene schiene : _daten.schienen)
			if (schiene.nummer > schieneR.nummer)
				schiene.nummer--;

		// (3) Wichtig: Die Ergebnisse dürfen nicht revalidiert werden, da sonst die Schiene aus (2) fehlt!
		final @NotNull Set<Long> setLoeschen = new HashSet<>();
		final @NotNull List<GostBlockungRegel> listHinzufuegen = new ArrayList<>();
		for (final @NotNull GostBlockungRegel r : _daten.regeln) {
			final long[] a = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			// Löschen?
			if (a == null) {
				setLoeschen.add(r.id);
				continue;
			}
			// Manipulation?
			if (DTOUtils.testRegelParameterChanged(r, a)) {
				setLoeschen.add(r.id);
				listHinzufuegen.add(r);
			}
		}
		regelRemoveListeByIDsOhneRevalidierung(setLoeschen);
		for (final @NotNull GostBlockungRegel r : listHinzufuegen) {
			final long[] a = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a != null)
				for (int i = 0; i < a.length; i++)
					r.parameter.set(i, a[i]);
		}
		regelAddListeOhneRevalidierung(listHinzufuegen);
	}

	/**
	 * Entfernt die übergebene Schiene aus der Blockung.
	 * <br>Hinweis: Es muss nicht dasselbe Objekt sein, nur die ID muss übereinstimmen.
	 *
	 * @param schiene  Die zu entfernende Schiene.
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public void schieneRemove(final @NotNull GostBlockungSchiene schiene) throws DeveloperNotificationException {
		schieneRemoveByID(schiene.id);
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return Die Anzahl an Schienen.
	 */
	public int schieneGetAnzahl() {
		return _map_idSchiene_schiene.size();
	}

	/**
	 * Liefert die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 *
	 * @param  halbjahr  Das Halbjahr, für welches die Blockung angelegt werden soll.
	 *
	 * @return Die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 */
	public static int schieneGetDefaultAnzahl(final @NotNull GostHalbjahr halbjahr) {
		return (halbjahr.id < 2) ? 13 : 11;
	}

	private void regelAddOhneSortierung(final @NotNull GostBlockungRegel regel) throws DeveloperNotificationException {
		final @NotNull LongArrayKey multikey = GostBlockungsdatenManager.regelToMultikey(regel);
		final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
		DeveloperNotificationException.ifMapPutOverwrites(_map_idRegel_regel, regel.id, regel);
		MapUtils.getOrCreateArrayList(_map_regeltyp_regeln, typ).add(regel);
		_map_multikey_regeln.put(multikey, regel);
		_daten.regeln.add(regel);
	}

	/**
	 * Fügt die übergebene Regel zu der Blockung hinzu.
	 *
	 * @param regel  Die hinzuzufügende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regel inkonsistent sind.
	 */
	public void regelAdd(final @NotNull GostBlockungRegel regel) throws DeveloperNotificationException {
		regelAddListe(ListUtils.create1(regel));
	}

	private void regelAddListeOhneRevalidierung(final @NotNull List<GostBlockungRegel> regeln) throws DeveloperNotificationException {
		// Regeln überprüfen
		final @NotNull Set<LongArrayKey> setMultiKey = new HashSet<>();
		final @NotNull Set<Long> setIDs = new HashSet<>();
		final @NotNull List<GostBlockungRegel> menge1 = new ArrayList<>(regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS));
		final @NotNull List<GostBlockungRegel> menge6 = new ArrayList<>(regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS));
		final @NotNull List<GostBlockungRegel> menge9 = new ArrayList<>(regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN));
		final @NotNull List<GostBlockungRegel> menge10 = new ArrayList<>(regelGetListeOfTyp(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN));
		final @NotNull List<GostBlockungRegel> menge15 = new ArrayList<>(regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL));
		final @NotNull List<GostBlockungRegel> regelmengeOkay = new ArrayList<>();

		for (final @NotNull GostBlockungRegel r : regeln) {
			// Überprüfe Referenzen.
			final @NotNull String warnung1 = regelCheckParameterReferencesAndReturnWarnung(r);
			if (!warnung1.isEmpty()) {
				_warnungen.add(warnung1);
				continue;
			}
			// Überprüfe Werte.
			final @NotNull String warnung2 = regelCheckParameterValuesAndReturnWarnung(r);
			if (!warnung2.isEmpty()) {
				_warnungen.add(warnung2);
				continue;
			}
			// Überprüfe Duplikate.
			final @NotNull String warnung3 = regelCheckDuplicatesAndReturnWarnung(r, setIDs, setMultiKey, menge1, menge6, menge9, menge10, menge15);
			if (!warnung3.isEmpty()) {
				_warnungen.add(warnung3);
				continue;
			}
			// Regel ist okay.
			regelmengeOkay.add(r);
		}

		// Regeln hinzufügen.
		for (final @NotNull GostBlockungRegel regel : regelmengeOkay)
			regelAddOhneSortierung(regel);

		// Sortieren der Regelmenge.
		_daten.regeln.sort(_compRegel);

		// Sortieren der pro Regeltyp zugeordneten Regelmenge.
		for (final @NotNull List<GostBlockungRegel> listOfTyp : _map_regeltyp_regeln.values())
			listOfTyp.sort(_compRegel);
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 *
	 * @param regelmenge  Die Menge an Regeln.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public void regelAddListe(final @NotNull List<GostBlockungRegel> regelmenge) throws DeveloperNotificationException {
		regelAddListeOhneRevalidierung(regelmenge);

		// Alle Ergebnisse revalidieren, damit die Bewertung aktuell ist.
		ergebnisAlleRevalidieren();
	}

	private @NotNull String regelCheckParameterReferencesAndReturnWarnung(final @NotNull GostBlockungRegel r) {
		// Falsche Parameteranzahl?
		final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);
		final int paramCount = typ.getParamCount();
		if (paramCount != r.parameter.size())
			return toStringRegel(r.id) + " hat falsche Parameter-Anzahl!";

		// Ist eine Referenz falsch?
		for (int i = 0; i < paramCount; i++) {
			long value = r.parameter.get(i);
			if (typ.getParamType(i) == GostKursblockungRegelParameterTyp.SCHUELER_ID)
				if (schuelerGetOrNull(value) == null)
					return toStringRegel(r.id) + " hat falsche Schüler-ID-Referenz " + value + "!";
			if (typ.getParamType(i) == GostKursblockungRegelParameterTyp.KURS_ID)
				if (!kursGetExistiert(value))
					return toStringRegel(r.id) + " hat falsche Kurs-ID-Referenz " + value + "!";
			if (typ.getParamType(i) == GostKursblockungRegelParameterTyp.SCHIENEN_NR)
				if ((value < 1) || (value > schieneGetAnzahl()))
					return toStringRegel(r.id) + " hat falsche Schienen-Nr-Referenz " + value + "!";
			if (typ.getParamType(i) == GostKursblockungRegelParameterTyp.FACH_ID)
				if (_faecherManager.get(value) == null)
					return toStringRegel(r.id) + " hat falsche Fach-ID-Referenz " + value + "!";
			if (typ.getParamType(i) == GostKursblockungRegelParameterTyp.KURSART)
				if (GostKursart.fromIDorNull((int) value) == null)
					return toStringRegel(r.id) + " hat falsche Kursart-Referenz " + value + "!";
		}

		// Keine Warnung
		return "";
	}

	private @NotNull String regelCheckParameterValuesAndReturnWarnung(final @NotNull GostBlockungRegel r) {
		// Ist die Regel-ID ungültig?
		if (r.id < 0)
			return toStringRegel(r.id) + " hat eine ungültige ID " + r.id;

		// Falsche Parameteranzahl?
		final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);
		final int paramCount = typ.getParamCount();
		if (paramCount != r.parameter.size())
			return toStringRegel(r.id) + " hat falsche Parameter-Anzahl " + r.parameter.size() + "!";

		// Überprüfe jeden Typ einzeln.
		final @NotNull List<Long> p = r.parameter;
		switch (typ) {
			// Undefinierte Regel ist nicht erlaubt.
			case UNDEFINIERT: // 0
				return toStringRegel(r.id) + " hat unbekannten Typ (" + r.typ + ")!";

			// Diese Regeln können keine falschen Parameter haben
			// Referenzen wie z.B. SchienenNR, KursID, SchienenID, ... werden woanders geprüft.
			case
					KURS_FIXIERE_IN_SCHIENE, KURS_SPERRE_IN_SCHIENE,  // 2, 3
					SCHUELER_FIXIEREN_IN_KURS, SCHUELER_VERBIETEN_IN_KURS,  // 4, 5
					KURS_VERBIETEN_MIT_KURS, KURS_ZUSAMMEN_MIT_KURS, // 7, 8
					LEHRKRAEFTE_BEACHTEN, SCHUELER_IGNORIEREN, // 10, 16
					SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH, SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH, // 11, 12
					SCHUELER_ZUSAMMEN_MIT_SCHUELER, SCHUELER_VERBIETEN_MIT_SCHUELER, // 13, 14
					KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN: // 17
				break;

			// Schiene-Von und Schiene-Bis werden überprüft. SchienenNr wurde bereits geprüft.
			case KURSART_SPERRE_SCHIENEN_VON_BIS, KURSART_ALLEIN_IN_SCHIENEN_VON_BIS:  // 1, 6
				if (p.get(2) < p.get(1))
					return toStringRegel(r.id) + " Die BIS-Schiene " + p.get(2) + " kann nicht kleiner sein als die VON-Schiene " + p.get(1) + "!";
				break;

			// Der Wert wird überprüft.
			case KURS_MIT_DUMMY_SUS_AUFFUELLEN:  // 9
				if (p.get(1) < GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MIN)
					return toStringRegel(r.id) + " KURS_MIT_DUMMY_SUS_AUFFUELLEN ist mit " + p.get(1) + " zu klein!";
				if (p.get(1) > GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MAX)
					return toStringRegel(r.id) + " KURS_MIT_DUMMY_SUS_AUFFUELLEN ist mit " + p.get(1) + " zu groß!";
				break;

			// Der Wert wird überprüft.
			case KURS_MAXIMALE_SCHUELERANZAHL:  // 15
				if (p.get(1) < GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MIN)
					return toStringRegel(r.id) + " KURS_MAXIMALE_SCHUELERANZAHL ist mit " + p.get(1) + " zu klein!";
				if (p.get(1) > GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX)
					return toStringRegel(r.id) + " KURS_MAXIMALE_SCHUELERANZAHL ist mit " + p.get(1) + " zu groß!";
				break;

			// Der Wert wird überprüft.
			case FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE:  // 18
				if (p.get(2) < GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MIN)
					return toStringRegel(r.id) + " FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE ist mit " + p.get(2) + " zu klein!";
				if (p.get(2) > GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MAX)
					return toStringRegel(r.id) + " FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE ist mit " + p.get(2) + " zu groß!";
				break;

			default:
				return toStringRegel(r.id) + " Regeltypüberprüfung: Der Regeltyp ist unbekannt!";
		}

		// Keine Warnung
		return "";
	}

	private @NotNull String regelCheckDuplicatesAndReturnWarnung(final @NotNull GostBlockungRegel r, final @NotNull Set<Long> setIDs, final @NotNull Set<LongArrayKey> setMultiKey,
			final @NotNull List<GostBlockungRegel> menge1, final @NotNull List<GostBlockungRegel> menge6, final @NotNull List<GostBlockungRegel> menge9,
			final @NotNull List<GostBlockungRegel> menge10, final @NotNull List<GostBlockungRegel> menge15) {
		// Ist die Regel-ID doppelt?
		if (_map_idRegel_regel.containsKey(r.id) || !setIDs.add(r.id))
			return toStringRegel(r.id) + "Regel-ID " + r.id + " Dopplung!";

		// Existiert bereits exakt die selbe Regel?
		final @NotNull LongArrayKey multikey = GostBlockungsdatenManager.regelToMultikey(r);
		if (!setMultiKey.add(multikey) || _map_multikey_regeln.containsKey(multikey))
			return toStringRegel(r.id) + " existiert bereits als gleiche  (nicht als selbe) Regel im MultiMap!";

		// Regel  1 "KURSART_SPERRE_SCHIENEN_VON_BIS" Intervall-Überschneidung prüfen.
		if (r.typ == GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ) {
			for (GostBlockungRegel rAlt : menge1)
				if (regelKursartIntervallSchnitt(rAlt, r))
					return "Intervallschnitt bei " + toStringRegel(r.id) + " und " + toStringRegel(rAlt.id) + "!";
			menge1.add(r);
		}

		// Regel  6 "KURSART_ALLEIN_IN_SCHIENEN_VON_BIS" Intervall-Überschneidung prüfen.
		if (r.typ == GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ) {
			for (GostBlockungRegel rAlt : menge6)
				if (regelKursartIntervallSchnitt(rAlt, r))
					return "Intervallschnitt bei " + toStringRegel(r.id) + " und " + toStringRegel(rAlt.id) + "!";
			menge6.add(r);
		}

		// Regel  9 "KURS_MIT_DUMMY_SUS_AUFFUELLEN" darf es nur ein Mal pro Kurs geben.
		if (r.typ == GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ) {
			for (GostBlockungRegel rAlt : menge9)
				if (rAlt.parameter.get(0).equals(r.parameter.get(0)))
					return toStringRegel(r.id) + " Regel-Dopplung!";
			menge9.add(r);
		}

		// Regel 10 "LEHRKRAEFTE_BEACHTEN" darf es nur ein Mal pro Blockung geben.
		if (r.typ == GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ) {
			if (!menge10.isEmpty())
				return toStringRegel(r.id) + " Regel-Dopplung!";
			menge10.add(r);
		}

		// Regel 15 "KURS_MAXIMALE_SCHUELERANZAHL" darf es nur ein Mal pro Kurs geben.
		if (r.typ == GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ) {
			for (GostBlockungRegel rAlt : menge15)
				if (rAlt.parameter.get(0).equals(r.parameter.get(0)))
					return toStringRegel(r.id) + " Regel-Dopplung!";
			menge15.add(r);
		}

		// Keine Warnung
		return "";
	}

	private static boolean regelKursartIntervallSchnitt(final @NotNull GostBlockungRegel r1, final @NotNull GostBlockungRegel r2) {
		if (!r1.parameter.get(0).equals(r2.parameter.get(0)))
			return false;
		final long von1 = r1.parameter.get(1);
		final long bis1 = r1.parameter.get(2);
		final long von2 = r2.parameter.get(1);
		final long bis2 = r2.parameter.get(2);
		return !((bis1 < von2) || (bis2 < von1));
	}

	/**
	 * Liefert die Anzahl an Regeln.
	 *
	 * @return Die Anzahl an Regeln.
	 */
	public int regelGetAnzahl() {
		return _map_idRegel_regel.size();
	}

	/**
	 * Liefert die Regel mit der übergebenen ID zurück.
	 *
	 * @param idRegel   Die Datenbank-ID der Regel.
	 *
	 * @return die Regel mit der übergebenen ID zurück.
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel regelGet(final long idRegel) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("_mapRegeln.get(" + idRegel + ")", _map_idRegel_regel.get(idRegel));
	}

	/**
	 * Liefert die {@link GostBlockungRegel} anhand des {@link LongArrayKey}-Schlüssels, oder NULL falls keine existiert.
	 *
	 * @param key  Der {@link LongArrayKey}-Schlüssel.
	 *
	 * @return die {@link GostBlockungRegel} anhand des {@link LongArrayKey}-Schlüssels, oder NULL falls keine existiert.
	 */
	public GostBlockungRegel regelGetByLongArrayKeyOrNull(final @NotNull LongArrayKey key) {
		return _map_multikey_regeln.get(key);
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln.
	 * Das ist die interne Referenz zur Liste der Regeln im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach (TYP, ID).
	 *
	 * @return Die aktuelle Menge aller Regeln sortiert nach (TYP, id).
	 */
	public @NotNull List<GostBlockungRegel> regelGetListe() {
		return _daten.regeln;
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 *
	 * @param typ Der {@link GostKursblockungRegelTyp}.
	 *
	 * @return die aktuelle Menge aller  Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 */
	public @NotNull List<GostBlockungRegel> regelGetListeOfTyp(final @NotNull GostKursblockungRegelTyp typ) {
		return MapUtils.getOrCreateArrayList(_map_regeltyp_regeln, typ);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param nrSchiene   Die Nummer der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel regelGetRegelOrDummyKursGesperrtInSchiene(final long idKurs, final int nrSchiene) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene });

		final GostBlockungRegel regel = _map_multikey_regeln.get(key);
		if (regel != null)
			return regel;

		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param nrSchiene   Die Nummer der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel regelGetRegelOrDummyKursFixierungInSchiene(final long idKurs, final int nrSchiene) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene });

		final GostBlockungRegel regel = _map_multikey_regeln.get(key);
		if (regel != null)
			return regel;

		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel regelGetRegelOrDummySchuelerInKursFixierung(final long idSchueler, final long idKurs) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs });

		final GostBlockungRegel regel = _map_multikey_regeln.get(key);
		if (regel != null)
			return regel;

		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs);
	}

	/**
	 * Liefert TRUE, falls die Regel mit der übergebenen ID existiert.
	 *
	 * @param idRegel   Die Datenbank-ID der Regel.
	 *
	 * @return TRUE, falls die Regel mit der übergebenen ID existiert.
	 */
	public boolean regelGetExistiert(final long idRegel) {
		return _map_idRegel_regel.get(idRegel) != null;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Regel erlaubt ist.
	 * <br> Hinweis: Die alte Implementierung verlangte noch, dass es sich um eine Blockungsvorlage handelt,
	 *               nun reicht es, dass die Regel existiert.
	 *
	 * @param   idRegel Die Datenbank-ID der Regel.
	 *
	 * @return TRUE, falls ein Löschen der Regel erlaubt ist.
	 */
	public boolean regelGetIsRemoveAllowed(final long idRegel) {
		return _map_idRegel_regel.containsKey(idRegel);
	}

	private GostBlockungRegel regelGet_KURS_MIT_DUMMY_SUS_AUFFUELLEN(final long idKurs) {
		for (final @NotNull GostBlockungRegel r : regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN))
			if (r.parameter.get(0) == idKurs)
				return r;

		return null;
	}

	/**
	 * Liefert TRUE, falls der übergebene Kurs in der übergebenen Regeln enthalten ist.
	 *
	 * @param regel   Das {@link GostBlockungRegel}-Objekt.
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der übergebene Kurs in der übergebenen Regeln enthalten ist.
	 */
	private static boolean regelGetHatKursIDs(final @NotNull GostBlockungRegel regel, final long idKurs) {
		final @NotNull GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
		for (int i = 0; i < regelTyp.getParamCount(); i++)
			if ((regelTyp.getParamType(i) == GostKursblockungRegelParameterTyp.KURS_ID) && (regel.parameter.get(i) == idKurs))
				return true;
		return false;
	}

	/**
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung.
	 *
	 * @param idRegel   Die Datenbank-ID der zu entfernenden Regel.
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public void regelRemoveByID(final long idRegel) throws DeveloperNotificationException {
		regelRemoveListeByIDs(SetUtils.create1(idRegel));
	}

	/**
	 * Entfernt eine Menge von Regeln.
	 *
	 * @param regelmenge   Die Menge an Regeln, die entfernt werden soll.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public void regelRemoveListe(final @NotNull List<GostBlockungRegel> regelmenge) throws DeveloperNotificationException {
		// IDs im Set sammeln.
		final @NotNull HashSet<Long> setRegelIDs = new HashSet<>();
		for (final @NotNull GostBlockungRegel regel : regelmenge)
			setRegelIDs.add(regel.id);

		// Alle Regeln löschen.
		regelRemoveListeByIDs(setRegelIDs);
	}

	private void regelRemoveListeByIDsOhneRevalidierung(final @NotNull Set<Long> regelmenge) throws DeveloperNotificationException {
		// Überprüfen
		for (final long idRegel : regelmenge) {
			final @NotNull GostBlockungRegel regel = regelGet(idRegel);
			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
			DeveloperNotificationException.ifTrue("Der Regeltyp ist undefiniert!", typ == GostKursblockungRegelTyp.UNDEFINIERT);
			DeveloperNotificationException.ifTrue("Die Multi-Map enthält die Regel nicht!", !_map_multikey_regeln.containsKey(regelToMultikey(regel)));
		}

		// Löschen
		for (final long idRegel : regelmenge) {
			final @NotNull GostBlockungRegel regel = this.regelGet(idRegel);
			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
			final @NotNull LongArrayKey multikey = GostBlockungsdatenManager.regelToMultikey(regel);

			// Löschen aus den Datenstrukturen
			_map_idRegel_regel.remove(idRegel);
			MapUtils.getOrCreateArrayList(_map_regeltyp_regeln, typ).remove(regel);
			_map_multikey_regeln.remove(multikey);
			_daten.regeln.remove(regel);
		}
	}

	/**
	 * Löscht eine Menge an Regeln anhand ihrer IDs.
	 *
	 * @param regelmenge   Die Menge der IDs der Regeln.
	 *
	 * @throws DeveloperNotificationException falls mindestens eine Regel nicht existiert.
	 */
	public void regelRemoveListeByIDs(final @NotNull Set<Long> regelmenge) throws DeveloperNotificationException {
		// Delegieren.
		regelRemoveListeByIDsOhneRevalidierung(regelmenge);

		// Alle Ergebnisse revalidieren, damit die Bewertung aktuell ist.
		ergebnisAlleRevalidieren();
	}

	private static @NotNull LongArrayKey regelToMultikey(final @NotNull GostBlockungRegel regel) {
		long[] a = new long[regel.parameter.size() + 1];
		a[0] = regel.typ;
		for (int i = 1; i < a.length; i++)
			a[i] = regel.parameter.get(i - 1);
		return new LongArrayKey(a);
	}

	/**
	 * Entfernt die übergebene Regel aus der Blockung.
	 *
	 * @param regel   Die zu entfernende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public void regelRemove(final @NotNull GostBlockungRegel regel) throws DeveloperNotificationException {
		regelRemoveListeByIDs(SetUtils.create1(regel.id));
	}

	/**
	 * Liefert die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen.
	 *
	 * @param idFach   Die Datenbank-ID des Faches.
	 *
	 * @return die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen.
	 */
	public @NotNull List<GostKursart> fachGetMengeKursarten(final long idFach) {
		final @NotNull HashSet<Integer> idKursarten = new HashSet<>();

		if (_map2d_idFach_idKursart_kurse.containsKey1(idFach))
			idKursarten.addAll(_map2d_idFach_idKursart_kurse.getKeySetOf(idFach));

		if (_map2d_idFach_idKursart_fachwahlen.containsKey1(idFach))
			idKursarten.addAll(_map2d_idFach_idKursart_fachwahlen.getKeySetOf(idFach));

		final @NotNull List<GostKursart> list = new ArrayList<>();
		for (final @NotNull GostKursart kursart : GostKursart.values())
			if (idKursarten.contains(kursart.id))
				list.add(kursart);

		return list;
	}

	/**
	 * Fügt eine Fachwahl hinzu.
	 *
	 * @param fachwahl   Die Fachwahl, die hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public void fachwahlAdd(final @NotNull GostFachwahl fachwahl) throws DeveloperNotificationException {
		fachwahlAddListe(ListUtils.create1(fachwahl));
	}

	/**
	 * Fügt alle Fachwahlen hinzu.
	 *
	 * @param fachwahlmenge   Die Menge an Fachwahlen.
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public void fachwahlAddListe(final @NotNull List<GostFachwahl> fachwahlmenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen
		final @NotNull Set<LongArrayKey> setSchuelerFach = new HashSet<>();
		for (final @NotNull GostFachwahl fNeu : fachwahlmenge) {
			GostKursart.fromFachwahlOrException(fNeu);
			DeveloperNotificationException.ifTrue("Fachwahl verweist auf ungültig Fach " + fNeu.fachID, _faecherManager.get(fNeu.fachID) == null);
			DeveloperNotificationException.ifTrue("Fachwahl Duplikat!", _map2d_idSchueler_idFach_fachwahl.contains(fNeu.schuelerID, fNeu.fachID));
			DeveloperNotificationException.ifTrue("Fachwahl Duplikat!", !setSchuelerFach.add(new LongArrayKey(fNeu.schuelerID, fNeu.fachID)));
		}

		// Hinzufügen
		for (final @NotNull GostFachwahl fNeu : fachwahlmenge) {
			// _map_schulerID_fachID_fachwahl
			DeveloperNotificationException.ifMap2DPutOverwrites(_map2d_idSchueler_idFach_fachwahl, fNeu.schuelerID, fNeu.fachID, fNeu);

			// _map_schuelerID_fachwahlen
			final @NotNull List<GostFachwahl> fachwahlenDesSchuelers = MapUtils.getOrCreateArrayList(_map_idSchueler_fachwahlen, fNeu.schuelerID);
			fachwahlenDesSchuelers.add(fNeu);
			fachwahlenDesSchuelers.sort(_compFachwahlen);

			// _map_fachartID_fachwahlen
			final long fachartID = GostKursart.getFachartIDByFachwahl(fNeu);
			fachwahlGetListeOfFachart(fachartID).add(fNeu);

			// _map2d_idFach_idKursart_kurse
			Map2DUtils.getOrCreateArrayList(_map2d_idFach_idKursart_fachwahlen, fNeu.fachID, fNeu.kursartID).add(fNeu);

			// fachwahlen
			_daten.fachwahlen.add(fNeu);
		}

		_daten.fachwahlen.sort(_compFachwahlen);
	}

	/**
	 * Liefert die Anzahl an Fachwahlen.
	 *
	 * @return die Anzahl an Fachwahlen.
	 */
	public int fachwahlGetAnzahl() {
		return _daten.fachwahlen.size();
	}

	/**
	 * Liefert den Namen der Fachwahl (Fach-Kursart), beispielsweise 'M-GK'.
	 * <br> Die Information über den Schüler dieser Fachwahl wird nicht dargestellt.
	 *
	 * @param fachwahl   Das Fachwahl-Objekt.
	 *
	 * @return den Namen der Fachwahl (Fach-Kursart), beispielsweise 'M-GK'.
	 * @throws DeveloperNotificationException falls die Fach-Referenz oder die Kursart-Referenz nicht existiert.
	 */
	public @NotNull String fachwahlGetName(final @NotNull GostFachwahl fachwahl) throws DeveloperNotificationException {
		final @NotNull GostFach gFach = _faecherManager.getOrException(fachwahl.fachID);
		final @NotNull GostKursart gKursart = GostKursart.fromID(fachwahl.kursartID);
		return gFach.kuerzelAnzeige + "-" + gKursart.kuerzel;
	}

	/**
	 * Liefert die sortierte Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID.
	 * <br> Die Fachart-ID lässt sich mit {@link GostKursart#getFachartID} berechnen.
	 *
	 * @param idFachart Die Fachart-ID berechnet aus Fach-ID und Kursart-ID.
	 *
	 * @return die sortierte Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID.
	 */
	public @NotNull List<GostFachwahl> fachwahlGetListeOfFachart(final long idFachart) {
		final @NotNull List<GostFachwahl> list = MapUtils.getOrCreateArrayList(_map_idFachart_fachwahlen, idFachart);
		list.sort(_compFachwahlen);
		return list;
	}

	/**
	 * Liefert die Anzahl verschiedenen Kursarten.
	 *
	 * @return Die Anzahl verschiedenen Kursarten.
	 */
	public int fachwahlGetAnzahlVerwendeterKursarten() {
		final @NotNull HashSet<Integer> setKursartenIDs = new HashSet<>();
		for (final @NotNull GostFachwahl fachwahl : _daten.fachwahlen)
			setKursartenIDs.add(fachwahl.kursartID);
		return setKursartenIDs.size();
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls die Schüler Daten inkonsistent sind.
	 *
	 * @param schueler  Der Schüler, der hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	private void schuelerAddOhneSortierung(final @NotNull Schueler schueler) throws DeveloperNotificationException {
		// _map_schuelerID_schueler
		DeveloperNotificationException.ifMapPutOverwrites(_map_idSchueler_schueler, schueler.id, schueler);

		// _map_schuelerID_fachwahlen
		if (!_map_idSchueler_fachwahlen.containsKey(schueler.id))
			_map_idSchueler_fachwahlen.put(schueler.id, new ArrayList<>());

		// _map2d_schuelerID_fachID_fachwahl: Der Teil-Pfad muss nicht erzeugt werden.

		// _daten.schueler
		_daten.schueler.add(schueler);
	}

	/**
	 * Fügt einen Schüler hinzu.
	 *
	 * @param schueler   Der Schüler, der hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler-Daten inkonsistent sind.
	 */
	public void schuelerAdd(final @NotNull Schueler schueler) throws DeveloperNotificationException {
		schuelerAddListe(ListUtils.create1(schueler));
	}

	/**
	 * Fügt alle Schüler hinzu.
	 *
	 * @param schuelermenge  Die Menge an Schülern.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler-Daten inkonsistent sind.
	 */
	public void schuelerAddListe(final @NotNull List<Schueler> schuelermenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		final @NotNull HashSet<Long> setId = new HashSet<>();
		for (final @NotNull Schueler sAlt : _daten.schueler)
			setId.add(sAlt.id);
		for (final @NotNull Schueler sNeu : schuelermenge) {
			DeveloperNotificationException.ifInvalidID("schueler.id", sNeu.id);
			DeveloperNotificationException.ifNull("schueler.geschlecht", Geschlecht.fromValue(sNeu.geschlecht));
			DeveloperNotificationException.ifNull("schueler.status", SchuelerStatus.data().getWertByID(sNeu.status));
			DeveloperNotificationException.ifTrue("schueler.id " + sNeu.id + " Dopplung!", !setId.add(sNeu.id));
		}

		// hinzufügen
		for (final @NotNull Schueler schueler : schuelermenge)
			schuelerAddOhneSortierung(schueler);

		// sortieren
		_daten.schueler.sort(_compSchueler);
	}

	/**
	 * Liefert die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 *
	 * @return die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 */
	public int schuelerGetAnzahlMitMindestensEinerFachwahl() {
		final HashSet<Long> setSchuelerIDs = new HashSet<>();
		for (final @NotNull GostFachwahl fachwahl : _daten.fachwahlen)
			setSchuelerIDs.add(fachwahl.schuelerID);
		return setSchuelerIDs.size();
	}

	/**
	 * Liefert die Anzahl an Schülern.
	 *
	 * @return die Anzahl an Schülern.
	 */
	public int schuelerGetAnzahl() {
		return _daten.schueler.size();
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return Das zugehörige {@link Schueler}-Objekt.
	 * @throws DeveloperNotificationException  Falls die Schüler-ID unbekannt ist.
	 */
	public @NotNull Schueler schuelerGet(final long idSchueler) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("_map_id_schueler.get(" + idSchueler + ")", _map_idSchueler_schueler.get(idSchueler));
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. <br>
	 * Gibt null zurück, falls die Schüler-ID unbekannt ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return Das zugehörige {@link Schueler}-Objekt oder null
	 */
	public Schueler schuelerGetOrNull(final long idSchueler) {
		return _map_idSchueler_schueler.get(idSchueler);
	}

	/**
	 * Liefert die aktuelle Menge aller Schüler.
	 * Das ist die interne Referenz zur Liste der Schüler im {@link GostBlockungsdaten}-Objekt.
	 *
	 * @return Die aktuelle Menge aller Schüler.
	 */
	public @NotNull List<Schueler> schuelerGetListe() {
		return _daten.schueler;
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Kursart.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches.
	 *
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostKursart}.
	 * @throws DeveloperNotificationException falls der Schüler das Fach nicht gewählt hat.
	 */
	public @NotNull GostKursart schuelerGetOfFachKursart(final long idSchueler, final long idFach) throws DeveloperNotificationException {
		final @NotNull GostFachwahl fachwahl = schuelerGetOfFachFachwahl(idSchueler, idFach);
		return GostKursart.fromID(fachwahl.kursartID);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches.
	 *
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl}.
	 * @throws DeveloperNotificationException falls der Schüler das Fach nicht gewählt hat.
	 */
	public @NotNull GostFachwahl schuelerGetOfFachFachwahl(final long idSchueler, final long idFach) throws DeveloperNotificationException {
		return _map2d_idSchueler_idFach_fachwahl.getOrException(idSchueler, idFach);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl. <br>
	 * Gibt null zurück, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches.
	 *
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl} oder null.
	 */
	public GostFachwahl schuelerGetOfFachFachwahlOrNull(final long idSchueler, final long idFach) {
		return _map2d_idSchueler_idFach_fachwahl.getOrNull(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 *
	 * @param idSchueler   Die Datenbank.ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 */
	public boolean schuelerGetHatFach(final long idSchueler, final long idFach) {
		return _map2d_idSchueler_idFach_fachwahl.contains(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls beide Schüler bezogen auf das Fach die selbe Kursart haben oder eine Exception.
	 *
	 * @param idSchueler1   Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2   Die Datenbank-ID des 2. Schülers.
	 * @param idFach        Die Datenbank-ID des Faches
	 *
	 * @return TRUE, falls beide Schüler bezogen auf das Fach die selbe Kursart haben oder eine Exception.
	 * @throws DeveloperNotificationException falls einer der beiden Schüler das Fach nicht gewählt hat.
	 */
	public boolean schuelerGetHatDieSelbeKursartMitSchuelerInFach(final long idSchueler1, final long idSchueler2, final long idFach)
			throws DeveloperNotificationException {
		final @NotNull GostFachwahl fachwahl1 = _map2d_idSchueler_idFach_fachwahl.getOrException(idSchueler1, idFach);
		final @NotNull GostFachwahl fachwahl2 = _map2d_idSchueler_idFach_fachwahl.getOrException(idSchueler2, idFach);
		return fachwahl1.kursartID == fachwahl2.kursartID;
	}

	/**
	 * Liefert TRUE, falls es den Schüler gibt mit der entsprechenden Fachwahl (Fach + Kursart) gibt.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param idKursart    Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls es den Schüler gibt mit der entsprechenden Fachwahl (Fach + Kursart) gibt.
	 */
	public boolean schuelerGetHatFachart(final long idSchueler, final long idFach, final int idKursart) {
		if (!_map2d_idSchueler_idFach_fachwahl.contains(idSchueler, idFach))
			return false;
		return _map2d_idSchueler_idFach_fachwahl.getOrException(idSchueler, idFach).kursartID == idKursart;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers.
	 * <br> Bei ungültiger Schüler-ID wird eine leere Liste geliefert.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 *
	 * @return die Menge aller {@link GostFachwahl} des Schülers.
	 */
	public @NotNull List<GostFachwahl> schuelerGetListeOfFachwahlen(final long idSchueler) {
		final List<GostFachwahl> fachwahlen = _map_idSchueler_fachwahlen.get(idSchueler);
		return (fachwahlen == null) ? new ArrayList<>() : fachwahlen;
	}

	/**
	 * Liefert eine Liste der gemeinsamen Fächer (auch in der Kursart übereinstimmend) beider Schüler.
	 *
	 * @param idSchueler1   Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2   Die Datenbank-ID des 2. Schülers.
	 *
	 * @return eine Liste der gemeinsamen Fächer (auch in der Kursart übereinstimmend) beider Schüler.
	 */
	public @NotNull List<GostFach> schuelerGetFachListeGemeinsamerFacharten(final long idSchueler1, final long idSchueler2) {
		final @NotNull List<GostFach> temp = new ArrayList<>();

		for (final @NotNull GostFachwahl fachwahl1 : schuelerGetListeOfFachwahlen(idSchueler1))
			if (schuelerGetHatFachart(idSchueler2, fachwahl1.fachID, fachwahl1.kursartID))
				temp.add(_faecherManager.getOrException(fachwahl1.fachID)); // Problem, wenn es die Fach-Referenz nicht (mehr) gibt!

		return temp;
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist.
	 */
	public boolean schuelerGetIstVerbotenInKurs(final long idSchueler, final long idKurs) {
		final @NotNull LongArrayKey key = new LongArrayKey(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs);
		return _map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs verbietet.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs verbietet.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public @NotNull GostBlockungRegel schuelerGetRegelVerbotenInKurs(final long idSchueler, final long idKurs) throws DeveloperNotificationException {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs });
		return DeveloperNotificationException.ifNull(toStringSchueler(idSchueler) + " hat gar kein Verbot für " + toStringKurs(idKurs) + "!",
				_map_multikey_regeln.get(key));
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist.
	 */
	public boolean schuelerGetIstFixiertInKurs(final long idSchueler, final long idKurs) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs });
		return _map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public @NotNull GostBlockungRegel schuelerGetRegelFixiertInKurs(final long idSchueler, final long idKurs) throws DeveloperNotificationException {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs });
		return DeveloperNotificationException.ifNull(toStringSchueler(idSchueler) + " hat gar keine Fixierung für " + toStringKurs(idKurs) + "!",
				_map_multikey_regeln.get(key));
	}

	/**
	 * Gibt die ID der Blockung zurück.
	 *
	 * @return die ID der Blockung
	 */
	public long getID() {
		return _daten.id;
	}

	/**
	 * Setzt die ID dieser Blockung.
	 *
	 * @param   idNeu  Die Datenbank-ID, welche der Blockung zugewiesen wird.
	 * @throws DeveloperNotificationException Falls die übergebene ID ungültig ist.
	 */
	public void setID(final long idNeu) throws DeveloperNotificationException {
		DeveloperNotificationException.ifInvalidID("pBlockungsID", idNeu);
		_daten.id = idNeu;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden.
	 *
	 * @return Die maximale Blockungszeit in Millisekunden.
	 */
	public long getMaxTimeMillis() {
		return _maxTimeMillis;
	}

	/**
	 * Setzt die maximale Blockungszeit in Millisekunden.
	 *
	 * @param   blockungszeit   die maximale Blockungszeit in Millisekunden.
	 * @throws DeveloperNotificationException falls der Wert nicht positiv ist.
	 */
	public void setMaxTimeMillis(final long blockungszeit) throws DeveloperNotificationException {
		DeveloperNotificationException.ifTrue("Der Wert muss positiv sein!", blockungszeit <= 0);
		_maxTimeMillis = blockungszeit;
	}

	/**
	 * Liefert den Namen der Blockung.
	 *
	 * @return den Namen der Blockung.
	 */
	public @NotNull String getName() {
		return _daten.name;
	}

	/**
	 * Setzt den Namen der Blockung
	 *
	 * @param name   der Name, welcher der Blockung zugewiesen wird.
	 * @throws UserNotificationException Falls der übergebene String leer ist.
	 */
	public void setName(final @NotNull String name) throws UserNotificationException {
		UserNotificationException.ifTrue("Ein leerer Name ist für die Blockung nicht zulässig.", "".equals(name));
		_daten.name = name;
	}

	/**
	 * Liefert das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @return das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 */
	public @NotNull GostHalbjahr getHalbjahr() {
		return GostHalbjahr.fromIDorException(_daten.gostHalbjahr);
	}

	/**
	 * Setzt das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 */
	public void setHalbjahr(final @NotNull GostHalbjahr halbjahr) {
		_daten.gostHalbjahr = halbjahr.id;
	}

	/**
	 * Liefert TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 *
	 * @return TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 */
	public boolean getIstBlockungsVorlage() {
		return _daten.ergebnisse.size() == 1;
	}

	/**
	 * Liefert die Anzahl an Fächern.
	 *
	 * @return die Anzahl an Fächern.
	 */
	public int getFaecherAnzahl() {
		return _faecherManager.faecher().size();
	}

	/**
	 * Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 *
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager})
	 */
	public @NotNull GostFaecherManager faecherManager() {
		return this._faecherManager;
	}

	/**
	 * Gibt die Blockungsdaten zurück.
	 *
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten})
	 */
	public @NotNull GostBlockungsdaten daten() {
		return this._daten;
	}

	/**
	 * Liefert eine String-Representation vieler Daten.
	 *
	 * @return eine String-Representation vieler Daten.
	 */
	public @NotNull String getDebugString() {
		final @NotNull StringBuilder sb = new StringBuilder();

		sb.append("\nErgebnisse = " + _daten.ergebnisse.size() + "\n");

		sb.append("\nSchienen = " + _daten.schienen.size() + "\n");
		for (final @NotNull GostBlockungSchiene s : _daten.schienen) {
			sb.append("    ID=" + s.id + ", NR=" + s.nummer + ", BEZ=" + s.bezeichnung + ", W-STD=" + s.wochenstunden + "\n");
			for (final @NotNull GostBlockungsergebnis e : ergebnisGetListeSortiertNachID())
				sb.append("    Hat E " + e.id + " Schiene " + s.id + "--> " + ergebnisManagerGet(e.id).getOfSchieneExists(s.id) + "\n");
		}

		sb.append("\nSchülermenge = " + _daten.schueler.size() + "\n");
		for (final @NotNull Schueler s : _daten.schueler)
			sb.append("    " + s.id + ", " + s.nachname + ", " + s.vorname + "\n");

		sb.append("\nKurse = " + _daten.kurse.size() + "\n");
		for (final @NotNull GostBlockungKurs k : _daten.kurse)
			sb.append("    " + k.id + ", " + k.fach_id + ", " + k.kursart + ", " + k.nummer + "\n");

		sb.append("\nFachwahlen = " + _daten.fachwahlen.size() + "\n");
		for (final @NotNull GostFachwahl fw : _daten.fachwahlen)
			sb.append("    " + fw.fachID + ", " + fw.kursartID + ", " + fw.schuelerID + ", " + fw.abiturfach + ", " + fw.istSchriftlich + "\n");

		sb.append("\nRegeln = " + _daten.regeln.size() + "\n");
		for (final @NotNull GostBlockungRegel r : _daten.regeln)
			sb.append("    " + r.id + ", " + r.typ + ", " + r.parameter + "\n");

		return sb.toString();
	}

	/**
	 * Liefert TRUE, falls die Multimap den Test erfolgreich besteht.
	 *
	 * @return TRUE, falls die Multimap den Test erfolgreich besteht.
	 */
	public boolean testMultimap() {
		if (_daten.regeln.size() != _map_multikey_regeln.size())
			return false;

		for (final @NotNull GostBlockungRegel r : _daten.regeln) {
			final @NotNull LongArrayKey key = regelToMultikey(r);
			if (!_map_multikey_regeln.containsKey(key))
				return false;
		}
		return true;
	}

}
