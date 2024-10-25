package de.svws_nrw.core.kursblockung.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.SetUtils;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisComparator;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Dummy-Manager zur Handhabung von Daten des Typs {@link GostBlockungsdaten} und {@link GostBlockungsergebnis}.
 * Dieser Manager ist für Unit-Tests-Zwecke gedacht.
 */
public class DummyGostBlockungsdatenManager {

	/** Ein Comparator für Schienen der Blockung */
	private static final @NotNull Comparator<GostBlockungSchiene> _compSchiene =
			(final @NotNull GostBlockungSchiene a, final @NotNull GostBlockungSchiene b) -> Integer.compare(a.nummer, b.nummer);

	/** Ein Comparator für die {@link GostBlockungsergebnis} nach ihrer Bewertung. */
	private final @NotNull Comparator<GostBlockungsergebnis> _compErgebnisse = new GostBlockungsergebnisComparator();

	/** Ein Comparator für die Lehrkräfte eines Kurses */
	private static final @NotNull Comparator<GostBlockungKursLehrer> _compLehrkraefte =
			(final @NotNull GostBlockungKursLehrer a, final @NotNull GostBlockungKursLehrer b) -> {
				final int result = Integer.compare(a.reihenfolge, b.reihenfolge);
				if (result != 0)
					return result;
				return Long.compare(a.id, b.id);
			};

	/** Ein Comparator für Kurse der Blockung. Dieser vergleicht nur die Kursnummern! */
	private static final @NotNull Comparator<GostBlockungKurs> _compKursnummer =
			(final @NotNull GostBlockungKurs a, final @NotNull GostBlockungKurs b) -> Integer.compare(a.nummer, b.nummer);

	/** Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER) */
	private final @NotNull Comparator<GostBlockungKurs> _compKurs_kursart_fach_kursnummer;

	/** Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER). */
	private final @NotNull Comparator<GostBlockungKurs> _compKurs_fach_kursart_kursnummer;

	/** Die Blockungsdaten, die im Manager vorhanden sind. */
	private final @NotNull GostBlockungsdaten _daten;

	/** Der Fächermanager mit den Fächern der gymnasialen Oberstufe. */
	private final @NotNull GostFaecherManager _faecherManager;

	/** Ergebnis-ID --> {@link GostBlockungsergebnis} */
	private final @NotNull HashMap<Long, DummyGostBlockungsergebnisManager> _mapEM = new HashMap<>();

	/**
	 * Erstellt einen neuen Manager mit den leeren Daten.
	 *
	 * @param daten    Die Blockungsdaten.
	 * @param faecher  Der Fächer-Manager.
	 */
	public DummyGostBlockungsdatenManager(final @NotNull GostBlockungsdaten daten, final @NotNull GostFaecherManager faecher) {
		_daten = daten;
		_faecherManager = faecher;
		_compKurs_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_compKurs_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
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

	private int compareFach(final long idFach1, final long idFach2) {
		final GostFach aFach = _faecherManager.get(idFach1);
		final GostFach bFach = _faecherManager.get(idFach2);

		if (aFach == null)
			return (bFach == null) ? 0 : -1;

		return (bFach == null) ? +1 : GostFaecherManager.comp.compare(aFach, bFach);
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
		if (!kursGetExistiert(idKurs))
			return "[Kurs (" + idKurs + ") ohne Mapping]";

		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);

		@NotNull String sFach = "Fach-ID = " + kurs.fach_id + " (ohne Mapping)";
		final GostFach gFach = _faecherManager.get(kurs.fach_id);
		if (gFach != null)
			sFach = (gFach.kuerzelAnzeige == null) ? ("Fach-ID = " + kurs.fach_id + " (ohne 'kuerzelAnzeige')") : gFach.kuerzelAnzeige;

		final @NotNull String sKursart = toStringKursartSimple(kurs.kursart);

		return "[Kurs " + sFach + "-" + sKursart + kurs.nummer + (kurs.suffix.isEmpty() ? "" : "-") + kurs.suffix + "]";
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
		for (final @NotNull GostBlockungKurs kurs : _daten.kurse)
			if (kurs.id == idKurs)
				for (final @NotNull GostBlockungKursLehrer lehrer : kurs.lehrer)
					if (lehrer.id == idLehrkraft)
						return "[Lehrkraft (ID=" + idLehrkraft + ") " + lehrer.kuerzel + "]";
		return "[Lehrkraft (ID=" + idLehrkraft + ")]";
	}

	/**
	 * Liefert möglichst viele Informationen zur Schiene mit der übergebenen ID.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return möglichst viele Informationen zur Schiene mit der übergebenen ID.
	 */
	public @NotNull String toStringSchiene(final long idSchiene) {
		for (final @NotNull GostBlockungSchiene schiene : _daten.schienen)
			if (schiene.id == idSchiene)
				return "[Schiene (" + schiene.id + ", Nr. " + schiene.nummer + "): " + schiene.bezeichnung + ", " + schiene.wochenstunden + "]";
		return "[Schiene (" + idSchiene + ") ohne Mapping]";
	}

	/**
	 * Liefert möglichst viele Informationen zur Regel mit der übergebenen ID.
	 *
	 * @param idRegel  Die Datenbank-ID der Regel.
	 *
	 * @return möglichst viele Informationen zur Regel mit der übergebenen ID.
	 */
	public @NotNull String toStringRegel(final long idRegel) {
		for (final @NotNull GostBlockungRegel regel : _daten.regeln)
			if (regel.id == idRegel)
				return "[Regel (" + regel.id + ", Nr. " + regel.typ + "): " + regel.parameter + "]";
		return "[Regel (" + idRegel + ") ohne Mapping]";
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
			DeveloperNotificationException.ifMapContains("_map_idErgebnis_ErgebnisManager", _mapEM, ergebnis.id);

		}

		// Hinzufügen
		for (final @NotNull GostBlockungsergebnis ergebnis : ergebnismenge) {
			final DummyGostBlockungsergebnisManager em = new DummyGostBlockungsergebnisManager();
			DeveloperNotificationException.ifMapPutOverwrites(_mapEM, ergebnis.id, em);
			_daten.ergebnisse.add(ergebnis);
		}

		// Sortieren
		_daten.ergebnisse.sort(_compErgebnisse);
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnis} aus der Liste der Ergebnisse.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return einen {@link GostBlockungsergebnis} aus der Liste der Ergebnisse.
	 * @throws DeveloperNotificationException Falls das Ergebnis nicht existiert.
	 */
	public @NotNull GostBlockungsergebnis ergebnisGet(final long idErgebnis) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungsergebnis ergebnis : _daten.ergebnisse)
			if (ergebnis.id == idErgebnis)
				return ergebnis;
		throw new DeveloperNotificationException("Es wurde kein Ergebnis mit ID=" + idErgebnis + " gefunden!");
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
	 * Entfernt die Menge an {@link GostBlockungsergebnis}-Objekten anhand ihrer ID.
	 *
	 * @param listeDerErgebnisIDs  Die IDs der Ergebnisse.
	 *
	 * @throws DeveloperNotificationException Falls es keine Ergebnisse mit diesen IDs gibt.
	 */
	public void ergebnisRemoveListeByIDs(final @NotNull Set<Long> listeDerErgebnisIDs) throws DeveloperNotificationException {
		// Konsistenz überprüfen
		for (final long idErgebnis : listeDerErgebnisIDs)
			DeveloperNotificationException.ifTrue("Das Ergebnis ID=" + idErgebnis + " existiert nicht!", !ergebnisManagerExists(idErgebnis));

		// Entfernen des Ergebnisses.
		for (final long idErgebnis : listeDerErgebnisIDs) {
			final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
			_daten.ergebnisse.remove(e);
			_mapEM.remove(e.id);
		}

		// Neusortierung nicht nötig.
	}

	/**
	 * Liefert TRUE, falls ein {@link GostBlockungsergebnisManager}-Objekt mit der ID existiert.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return TRUE, falls ein {@link GostBlockungsergebnisManager}-Objekt mit der ID existiert.
	 */
	public boolean ergebnisManagerExists(final long idErgebnis) {
		return _mapEM.containsKey(idErgebnis);
	}

	/**
	 * Revalidiert alle Ergebnis. Dies führt zur Aktualisierung aller Ergebnisse.
	 */
	public void ergebnisAlleRevalidieren() {
		for (final DummyGostBlockungsergebnisManager ergebnisManager : _mapEM.values())
			ergebnisManager.stateRevalidateEverything();
	}

	/**
	 * Liefert die Anzahl an Kursen.
	 *
	 * @return Die Anzahl an Kursen.
	 */
	public int kursGetAnzahl() {
		return _daten.kurse.size();
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

		// Hinzufügen.
		for (final @NotNull GostBlockungKurs kurs : kursmenge) {
			_daten.kurse.add(kurs);
		}
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
		for (final @NotNull GostBlockungKurs kurs : _daten.kurse)
			if (kurs.id == idKurs)
				return kurs;

		throw new DeveloperNotificationException("Es gibt keinen Kurs mit ID = " + idKurs + ".");
	}

	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 *
	 * @param idKurs Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs mit der übergebenen ID existiert.
	 */
	public boolean kursGetExistiert(final long idKurs) {
		for (final @NotNull GostBlockungKurs kurs : _daten.kurse)
			if (kurs.id == idKurs)
				return true;

		return false;
	}

	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 *
	 * @param schiene  Die hinzuzufügende Schiene.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public void schieneAdd(final @NotNull GostBlockungSchiene schiene) throws DeveloperNotificationException {
		schieneAddListe(ListUtils.create1(schiene));
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
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

		// Hinzufügen.
		for (final @NotNull GostBlockungSchiene schiene : schienenmenge)
			_daten.schienen.add(schiene);

		// Sortieren
		_daten.schienen.sort(_compSchiene);
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 */
	public boolean schieneGetExistiert(final long idSchiene) {
		for (final @NotNull GostBlockungSchiene schiene : _daten.schienen)
			if (schiene.id == idSchiene)
				return true;
		return false;
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return Die Anzahl an Schienen.
	 */
	public int schieneGetAnzahl() {
		return _daten.schienen.size();
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
		for (final @NotNull GostBlockungSchiene schiene : _daten.schienen)
			if (schiene.id == idSchiene)
				return schiene;

		throw new DeveloperNotificationException("Es gibt keine Schiene mit ID = " + idSchiene + ".");
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

	/**
	 * Liefert die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 *
	 * @return die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
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
	 * Liefert TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 *
	 * @return TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 */
	public boolean getIstBlockungsVorlage() {
		return _daten.ergebnisse.size() == 1;
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

		// (2)
		_daten.schienen.remove(schieneR);
		for (final @NotNull GostBlockungSchiene schiene : _daten.schienen)
			if (schiene.nummer > schieneR.nummer)
				schiene.nummer--;

		// (3)
		final Iterator<GostBlockungRegel> iRegel = _daten.regeln.iterator();
		if (iRegel == null)
			return;
		while (iRegel.hasNext()) {
			final @NotNull GostBlockungRegel r = iRegel.next();
			final long[] a = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a == null)
				iRegel.remove();
			else
				for (int i = 0; i < a.length; i++)
					r.parameter.set(i, a[i]);
		}
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
	 * Liefert die Lehrkraft des Kurses, welche die angegebene Reihenfolge-Nummer hat.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param reihenfolgeNr  Die Lehrkraft mit der Reihenfolge-Nummer, die gesucht wird.
	 *
	 * @return die Lehrkraft des Kurses, welche die angegebene Reihenfolge-Nummer hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public GostBlockungKursLehrer kursGetLehrkraftMitNummer(final long idKurs, final int reihenfolgeNr) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.reihenfolge == reihenfolgeNr)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + toStringKurs(idKurs) + " keine Lehrkraft mit ReihenfolgeNr. " + reihenfolgeNr + "!");
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
	 * Entfernt alle Kurse mit den übergebenen IDs aus der Blockung.
	 *
	 * @param idKurse  Die Datenbank-IDs der zu entfernenden Kurse.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void kurseRemoveByID(final @NotNull Set<Long> idKurse) throws DeveloperNotificationException {
		// (1) Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!", !getIstBlockungsVorlage());
		for (final long idKurs : idKurse)
			kursGet(idKurs);

		// (2) Entfernen der Kurse
		for (final long idKurs : idKurse) {
			final @NotNull GostBlockungKurs kurs = this.kursGet(idKurs);
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

	private void regelRemoveListeByIDsOhneRevalidierung(final @NotNull Set<Long> regelmenge) throws DeveloperNotificationException {
		_daten.regeln.removeIf(regel -> regelmenge.contains(regel.id));
	}

	/**
	 * Löscht eines Regelmenge anhand ihrer IDs.
	 *
	 * @param regelmenge  Die Menge der IDs der Regeln.
	 *
	 * @throws DeveloperNotificationException falls die Regel nicht gefunden wird.
	 */
	public void regelRemoveListeByIDs(final @NotNull Set<Long> regelmenge) {
		regelRemoveListeByIDsOhneRevalidierung(regelmenge);
		// revalidierung
	}

	/**
	 * Setzt die ID dieser Blockung.
	 *
	 * @param idNeu  Die Datenbank-ID, welche der Blockung zugewiesen wird.
	 * @throws DeveloperNotificationException Falls die übergebene ID ungültig ist.
	 */
	public void setID(final long idNeu) throws DeveloperNotificationException {
		DeveloperNotificationException.ifInvalidID("pBlockungsID", idNeu);
		_daten.id = idNeu;
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
			DeveloperNotificationException.ifTrue(
					toStringKurs(idKurs) + " hat bereits " + toStringKursLehrkraft(idKurs, lehrkraft.id),
					lehrkraft.id == neueLehrkraft.id);
			DeveloperNotificationException.ifTrue(
					toStringKurs(idKurs) + " hat bereits " + toStringKursLehrkraft(idKurs, lehrkraft.id) + " mit Reihenfolge " + lehrkraft.reihenfolge,
					lehrkraft.reihenfolge == neueLehrkraft.reihenfolge);
		}
		// Hinzufügen
		listOfLehrer.add(neueLehrkraft);
		listOfLehrer.sort(_compLehrkraefte);
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
		final @NotNull List<GostBlockungKurs> list = new ArrayList<>(_daten.kurse);
		list.sort(_compKurs_fach_kursart_kursnummer);
		return list;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull List<GostBlockungKurs> kursGetListeSortiertNachKursartFachNummer() {
		final @NotNull List<GostBlockungKurs> list = new ArrayList<>(_daten.kurse);
		list.sort(_compKurs_kursart_fach_kursnummer);
		return list;
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
		final List<GostBlockungKurs> list = new ArrayList<>();
		for (final @NotNull GostBlockungKurs kurs : _daten.kurse)
			if ((kurs.fach_id == idFach) && (kurs.kursart == idKursart))
				list.add(kurs);
		list.sort(_compKursnummer);
		return list;
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
		if (!getIstBlockungsVorlage())
			return false;

		for (final @NotNull GostBlockungKurs kurs : _daten.kurse)
			if (kurs.id == idKurs)
				return true;

		return false;
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
		// Konsistenz überprüfen
		final int kursart = kursGet(idKurs).kursart;
		final int nummer = schieneGet(idSchiene).nummer;

		// Regeln scannen
		for (final @NotNull GostBlockungRegel regel : _daten.regeln) {
			// KURS_SPERRE_IN_SCHIENE
			if (kursGetHatSperrungInSchiene(idKurs, idSchiene))
				return true; // Es gibt eine individuelle Sperrung für diesen Kurs in dieser Schiene.

			// KURSART_ALLEIN_IN_SCHIENEN_VON_BIS
			if (regel.typ == GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ) {
				if ((nummer >= regel.parameter.get(1)) && (nummer <= regel.parameter.get(2))) {
					if (regel.parameter.get(0) != kursart)
						return true; // Meine Kursart ist in einem fremden Intervall.
				} else {
					if (regel.parameter.get(0) == kursart)
						return true; // Meine Kursart liegt außerhalb des Intervalls.
				}
			}

			// KURSART_SPERRE_SCHIENEN_VON_BIS
			if ((regel.typ == GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ)
					&& (regel.parameter.get(0) == kursart)
					&& (nummer >= regel.parameter.get(1))
					&& (nummer <= regel.parameter.get(2)))
				return true; // Meine Kursart ist im Intervall gesperrt.

		}

		return false;
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 * @throws DeveloperNotificationException falls die Schiene in der Blockung nicht existiert.
	 */
	public boolean kursGetHatSperrungInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		// Konsistenz überprüfen
		final int nummer = schieneGet(idSchiene).nummer;

		// Regeln scannen
		for (final @NotNull GostBlockungRegel regel : _daten.regeln) {
			if ((regel.typ == GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ)
					&& (regel.parameter.get(0) == idKurs)
					&& (regel.parameter.get(1) == nummer))
				return true;
		}

		return false;
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
		// Konsistenz überprüfen
		final int nummer = schieneGet(idSchiene).nummer;

		// Regeln scannen
		for (final @NotNull GostBlockungRegel regel : _daten.regeln) {
			if ((regel.typ == GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ)
					&& (regel.parameter.get(0) == idKurs)
					&& (regel.parameter.get(1) == nummer))
				return regel;
		}


		throw new DeveloperNotificationException("" + toStringKurs(idKurs) + " ist nicht gesperrt in Schiene " + toStringSchiene(idSchiene) + "!");
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
		// Konsistenz überprüfen
		final int nummer = schieneGet(idSchiene).nummer;

		// Regeln scannen
		for (final @NotNull GostBlockungRegel regel : _daten.regeln) {
			if ((regel.typ == GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ)
					&& (regel.parameter.get(0) == idKurs)
					&& (regel.parameter.get(1) == nummer))
				return true;
		}

		return false;
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
		// Konsistenz überprüfen
		final int nummer = schieneGet(idSchiene).nummer;

		// Regeln scannen
		for (final @NotNull GostBlockungRegel regel : _daten.regeln) {
			if ((regel.typ == GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ)
					&& (regel.parameter.get(0) == idKurs)
					&& (regel.parameter.get(1) == nummer))
				return regel;
		}

		throw new DeveloperNotificationException("" + toStringKurs(idKurs) + " ist nicht fixiert in Schiene " + toStringSchiene(idSchiene) + "!");
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

		// Regeln scannen und Fixierungen zählen
		int anzahlFixierungen = 0;
		for (final @NotNull GostBlockungRegel regel : _daten.regeln) {
			if ((regel.typ == GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ)
					&& (regel.parameter.get(0) == idKurs))
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
		// Regeln scannen
		for (final @NotNull GostBlockungRegel regel : _daten.regeln) {
			if ((regel.typ == GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ)
					&& (regel.parameter.get(0) == idKurs))
				return regel;
		}

		return null;
	}

	/**
	 * Liefert ein Set aller Kurs-IDs.
	 *
	 * @return ein Set aller Kurs-IDs.
	 */
	public @NotNull Set<Long> kursmengeGetSetDerIDs() {
		final @NotNull HashSet<Long> setKursID = new HashSet<>();
		for (final @NotNull GostBlockungKurs kurs : _daten.kurse)
			setKursID.add(kurs.id);
		return setKursID;
	}

	private GostBlockungRegel regelGet_KURS_MIT_DUMMY_SUS_AUFFUELLEN(final long idKurs) {
		for (final @NotNull GostBlockungRegel regel : _daten.regeln)
			if ((regel.typ == GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ) && (regel.parameter.get(0) == idKurs))
				return regel;
		return null;
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
		DeveloperNotificationException.ifTrue("Die ID=" + idKursID1keep + " des Ziel Kurses-gibt es nicht!", !kursGetExistiert(idKursID1keep));
		DeveloperNotificationException.ifTrue("Die ID=" + idKursID2delete + " des Quell-Kurses gibt es nicht!", !kursGetExistiert(idKursID2delete));

		// (2) Regel "KURS_MIT_DUMMY_SUS_AUFFUELLEN" anpassen.
		final GostBlockungRegel regelKursKeep = regelGet_KURS_MIT_DUMMY_SUS_AUFFUELLEN(idKursID1keep);
		final GostBlockungRegel regelKursDelete = regelGet_KURS_MIT_DUMMY_SUS_AUFFUELLEN(idKursID2delete);

		if (regelKursDelete != null)
			if (regelKursKeep != null) {
				// Keep-Regel += Delete-Regel --> Delete-Regel wird in (3) gelöscht.
				final long summe = regelKursDelete.parameter.get(1) + regelKursKeep.parameter.get(1);
				regelKursKeep.parameter.set(1, summe);
			} else {
				// Delete-Regel wird zur Keep-Regel. (Löschen, Verändern, Hinzufügen)
				regelRemoveListeByIDs(SetUtils.create1(regelKursDelete.id));
				regelKursDelete.parameter.set(0, idKursID1keep);
				regelAddListe(ListUtils.create1(regelKursDelete));
			}

		// (3) Dann wird erst der Kurs komplett gelöscht (mit potentiellen Regeln).
		kurseRemoveByID(SetUtils.create1(idKursID2delete));
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

	private void regelCheck(final @NotNull GostBlockungRegel regel) throws DeveloperNotificationException {
		// Ist die ID gültig?
		DeveloperNotificationException.ifInvalidID("Regel.id", regel.id);

		// Ist der Regel-Typ gültig?
		final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
		DeveloperNotificationException.ifTrue(toStringRegel(regel.id) + " hat unbekannten Typ (" + regel.typ + ")!",
				typ == GostKursblockungRegelTyp.UNDEFINIERT);

		// Existiert bereits exakt die selbe Regel?
		for (final @NotNull GostBlockungRegel regelAlt : _daten.regeln)
			if (regel.parameter.size() == regelAlt.parameter.size()) {
				boolean allEqual = true;
				for (int i = 0; i < regel.parameter.size(); i++)
					if (regel.parameter.get(i) != regelAlt.parameter.get(i))
						allEqual = false;
				DeveloperNotificationException.ifTrue(toStringRegel(regel.id) + " existiert bereits!", allEqual);
			}
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 *
	 * @param regelmenge  Die Menge an Regeln.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public void regelAddListe(final @NotNull List<GostBlockungRegel> regelmenge) throws DeveloperNotificationException {
		// Regeln überprüfen
		for (final @NotNull GostBlockungRegel regel : regelmenge)
			regelCheck(regel);

		// Regeln hinzufügen.
		for (final @NotNull GostBlockungRegel regel : regelmenge)
			_daten.regeln.add(regel);

		// Alle Ergebnisse revalidieren, damit die Bewertung aktuell ist.
		ergebnisAlleRevalidieren();
	}

}
