package de.svws_nrw.core.utils.schueler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.core.data.jahrgang.JahrgangsListeEintrag;
import de.svws_nrw.core.data.klassen.KlassenListeEintrag;
import de.svws_nrw.core.data.kurse.KursListeEintrag;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.klassen.KlassenUtils;
import de.svws_nrw.core.utils.kurse.KursUtils;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Schüler-Lernabschnittsdaten eines Schülers.
 */
public class SchuelerLernabschnittManager {

	private final @NotNull SchuelerListeEintrag _schueler;

	private final @NotNull SchuelerLernabschnittsdaten _lernabschnittsdaten;
	private final @NotNull Map<@NotNull Long, @NotNull SchuelerLeistungsdaten> _mapLeistungById = new HashMap<>();

	private final @NotNull List<@NotNull FaecherListeEintrag> _faecher = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull FaecherListeEintrag> _mapFachByID = new HashMap<>();

	private final @NotNull List<@NotNull FoerderschwerpunktEintrag> _foerderschwerpunkte = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull FoerderschwerpunktEintrag> _mapFoerderschwerpunktByID = new HashMap<>();

	private final @NotNull List<@NotNull JahrgangsListeEintrag> _jahrgaenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull JahrgangsListeEintrag> _mapJahrgangByID = new HashMap<>();

	private final @NotNull List<@NotNull KlassenListeEintrag> _klassen = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull KlassenListeEintrag> _mapKlasseByID = new HashMap<>();

	private final @NotNull List<@NotNull KursListeEintrag> _kurse = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull KursListeEintrag> _mapKursByID = new HashMap<>();

	private final @NotNull List<@NotNull LehrerListeEintrag> _lehrer = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull LehrerListeEintrag> _mapLehrerByID = new HashMap<>();


	private static final @NotNull Comparator<@NotNull FaecherListeEintrag> _compFach = (final @NotNull FaecherListeEintrag a, final @NotNull FaecherListeEintrag b) -> {
		int cmp = a.sortierung - b.sortierung;
		if (cmp != 0)
			return cmp;
		if ((a.kuerzel == null) || (b.kuerzel == null))
			throw new DeveloperNotificationException("Fachkürzel dürfen nicht null sein");
		cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	private static final @NotNull Comparator<@NotNull FoerderschwerpunktEintrag> _compFoerderschwerpunkte = (final @NotNull FoerderschwerpunktEintrag a, final @NotNull FoerderschwerpunktEintrag b) -> {
		if (a.text == null)
			return -1;
		if (b.text == null)
			return 1;
		return a.text.compareTo(b.text);
	};

	private static final @NotNull Comparator<@NotNull LehrerListeEintrag> _compLehrer = (final @NotNull LehrerListeEintrag a, final @NotNull LehrerListeEintrag b) -> {
		int cmp = a.sortierung - b.sortierung;
		if (cmp != 0)
			return cmp;
		cmp = a.nachname.compareTo(b.nachname);
		if (cmp != 0)
			return cmp;
		cmp = a.vorname.compareTo(b.vorname);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	private final @NotNull Comparator<@NotNull SchuelerLeistungsdaten> _compLeistungenByFach = (final @NotNull SchuelerLeistungsdaten a, final @NotNull SchuelerLeistungsdaten b) -> {
		final @NotNull FaecherListeEintrag aFach = DeveloperNotificationException.ifMapGetIsNull(_mapFachByID, a.fachID);
		final @NotNull FaecherListeEintrag bFach = DeveloperNotificationException.ifMapGetIsNull(_mapFachByID, b.fachID);
		return _compFach.compare(aFach, bFach);
	};



	/**
	 * Erstellt einen neuen Manager mit den übergebenen Lernabschnittsdaten und den übergebenen Katalogen
	 *
	 * @param schueler              Informationen zu dem Schüler
	 * @param lernabschnittsdaten   die Lernabschnittsdaten
	 * @param faecher               der Katalog der Fächer
	 * @param jahrgaenge            der Katalog der Jahrgänge
	 * @param klassen               der Katalog der Klassen
	 * @param kurse                 der Katalog der Kurse
	 * @param lehrer                der Katalog der Lehrer
	 * @param foerderschwerpunkte   der Katalog der Förderschwerpunkte
	 */
	public SchuelerLernabschnittManager(final @NotNull SchuelerListeEintrag schueler,
			final @NotNull SchuelerLernabschnittsdaten lernabschnittsdaten,
			final @NotNull List<@NotNull FaecherListeEintrag> faecher,
			final @NotNull List<@NotNull FoerderschwerpunktEintrag> foerderschwerpunkte,
			final @NotNull List<@NotNull JahrgangsListeEintrag> jahrgaenge,
			final @NotNull List<@NotNull KlassenListeEintrag> klassen,
			final @NotNull List<@NotNull KursListeEintrag> kurse,
			final @NotNull List<@NotNull LehrerListeEintrag> lehrer) {
		this._schueler = schueler;
		this._lernabschnittsdaten = lernabschnittsdaten;
		initLeistungsdaten(lernabschnittsdaten.leistungsdaten);
		initFaecher(faecher);
		initFoerderschwerpunkte(foerderschwerpunkte);
		initJahrgaenge(jahrgaenge);
		initKlassen(klassen);
		initKurse(kurse);
		initLehrer(lehrer);
	}

	private void initLeistungsdaten(final @NotNull List<@NotNull SchuelerLeistungsdaten> leistungsdaten) {
		for (final @NotNull SchuelerLeistungsdaten leistung : leistungsdaten)
			this.leistungAddInternal(leistung);
	}

	private void initFaecher(final @NotNull List<@NotNull FaecherListeEintrag> faecher) {
		this._faecher.clear();
		this._faecher.addAll(faecher);
		this._faecher.sort(_compFach);
		this._mapFachByID.clear();
		for (final @NotNull FaecherListeEintrag f : faecher)
			this._mapFachByID.put(f.id, f);
	}

	private void initFoerderschwerpunkte(final @NotNull List<@NotNull FoerderschwerpunktEintrag> foerderschwerpunkte) {
		this._foerderschwerpunkte.clear();
		this._foerderschwerpunkte.addAll(foerderschwerpunkte);
		this._foerderschwerpunkte.sort(_compFoerderschwerpunkte);
		this._mapFoerderschwerpunktByID.clear();
		for (final @NotNull FoerderschwerpunktEintrag f : foerderschwerpunkte)
			this._mapFoerderschwerpunktByID.put(f.id, f);
	}

	private void initJahrgaenge(final @NotNull List<@NotNull JahrgangsListeEintrag> jahrgaenge) {
		this._jahrgaenge.clear();
		this._jahrgaenge.addAll(jahrgaenge);
		this._jahrgaenge.sort(JahrgangsUtils.comparator);
		this._mapJahrgangByID.clear();
		for (final @NotNull JahrgangsListeEintrag j : jahrgaenge)
			this._mapJahrgangByID.put(j.id, j);
	}

	private void initKlassen(final @NotNull List<@NotNull KlassenListeEintrag> klassen) {
		this._klassen.clear();
		this._klassen.addAll(klassen);
		this._klassen.sort(KlassenUtils.comparator);
		this._mapKlasseByID.clear();
		for (final @NotNull KlassenListeEintrag k : klassen)
			this._mapKlasseByID.put(k.id, k);
	}

	private void initKurse(final @NotNull List<@NotNull KursListeEintrag> kurse) {
		this._kurse.clear();
		this._kurse.addAll(kurse);
		this._kurse.sort(KursUtils.comparator);
		this._mapKursByID.clear();
		for (final @NotNull KursListeEintrag k : kurse)
			this._mapKursByID.put(k.id, k);
	}

	private void initLehrer(final @NotNull List<@NotNull LehrerListeEintrag> lehrer) {
		this._lehrer.clear();
		this._lehrer.addAll(lehrer);
		this._lehrer.sort(_compLehrer);
		this._mapLehrerByID.clear();
		for (final @NotNull LehrerListeEintrag l : lehrer)
			this._mapLehrerByID.put(l.id, l);
	}


	/**
	 * Gibt die Lernabschnittsdaten dieses Managers zurück.
	 *
	 * @return die Lernabschnittsdaten
	 */
	public @NotNull SchuelerLernabschnittsdaten lernabschnittGet() {
		return this._lernabschnittsdaten;
	}


	private void leistungAddInternal(final @NotNull SchuelerLeistungsdaten leistungsdaten) {
		this._mapLeistungById.put(leistungsdaten.id, leistungsdaten);
	}


	/**
	 * Fügt die übergebenen Leistungsdaten zu dem Lernabschnitt hinzu
	 *
	 * @param leistungsdaten   die hinzuzufügenden Leistungsdaten
	 */
	public void leistungAdd(final @NotNull SchuelerLeistungsdaten leistungsdaten) {
		this._lernabschnittsdaten.leistungsdaten.add(leistungsdaten);
		this.leistungAddInternal(leistungsdaten);
	}


	/**
	 * Entfernt die übergebenen Leistungsdaten anhand der ID aus dem Lernabschnitt
	 *
	 * @param idLeistungsdaten   die ID der zu entfernenden Leistungsdaten
	 */
	public void leistungRemoveByID(final long idLeistungsdaten) {
		for (int i = this._lernabschnittsdaten.leistungsdaten.size() - 1; i >= 0; i--) {
			final @NotNull SchuelerLeistungsdaten leistung = this._lernabschnittsdaten.leistungsdaten.get(i);
			if (leistung.id == idLeistungsdaten)
				this._lernabschnittsdaten.leistungsdaten.remove(leistung);
		}
		this._mapLeistungById.remove(idLeistungsdaten);
	}


	/**
	 * Gibt die Leistungsdaten für die übergebene ID zurück.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Leistungsdaten
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public @NotNull SchuelerLeistungsdaten leistungGetByIdOrException(final long idLeistung) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
	}

	/**
	 * Gibt die Menge der Leistungsdaten sortiert anhand des Faches zurück.
	 *
	 * @return die Menge der Leistungsdaten
	 */
	public @NotNull List<@NotNull SchuelerLeistungsdaten> leistungGetMengeAsListSortedByFach() {
		final @NotNull List<@NotNull SchuelerLeistungsdaten> result = new ArrayList<>();
		result.addAll(_lernabschnittsdaten.leistungsdaten);
		result.sort(_compLeistungenByFach);
		return result;
	}

	/**
	 * Prüft, ob ein Kurs mit den Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return true, falls ein Kurs mit den Leistungsdaten verknüpft ist
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public boolean leistungHatKurs(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		return _mapKursByID.get(leistung.kursID) != null;
	}

	/**
	 * Prüft, ob ein Lehrer mit den Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return true, falls ein Lehrer mit den Leistungsdaten verknüpft ist
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public boolean leistungHatLehrer(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		return _mapLehrerByID.get(leistung.lehrerID) != null;
	}



	/**
	 * Ermittelt die Informationen zu dem Fach mit der angegebenen ID.
	 *
	 * @param id   die ID des Faches
	 *
	 * @return die Fach-Informationen
	 * @throws DeveloperNotificationException falls kein Fach mit der ID existiert
	 */
	public @NotNull FaecherListeEintrag fachGetByIdOrException(final long id) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapFachByID, id);
	}

	/**
	 * Ermittelt die Informationen zum Fach, welche mit den Leistungsdaten verknüpft sind.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Fach-Informationen.
	 * @throws DeveloperNotificationException falls kein Fach zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist
	 */
	public @NotNull FaecherListeEintrag fachGetByLeistungIdOrException(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		return DeveloperNotificationException.ifMapGetIsNull(_mapFachByID, leistung.fachID);
	}

	/**
	 * Ermittelt die Informationen zu der Fach-Farbe, welche den Leistungsdaten zugeordnet ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Farbe
	 * @throws DeveloperNotificationException falls kein Fach zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist
	 */
	public @NotNull String fachFarbeGetByLeistungsIdOrException(final long idLeistung) {
		final @NotNull FaecherListeEintrag fach = fachGetByLeistungIdOrException(idLeistung);
		return ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getHMTLFarbeRGB();
	}

	/**
	 * Gibt die Liste der Fächer zurück.
	 *
	 * @return die Liste der Fächer
	 */
	public @NotNull List<@NotNull FaecherListeEintrag> fachGetMenge() {
		return this._faecher;
	}


	/**
	 * Ermittelt die Informationen zu dem Förderschwerpunkt mit der angegebenen ID.
	 *
	 * @param id   die ID des Förderschwerpunktes
	 *
	 * @return die Förderschwerpunkt-Informationen
	 * @throws DeveloperNotificationException falls kein Förderschwerpunkt mit der ID existiert
	 */
	public @NotNull FoerderschwerpunktEintrag foerderschwerpunktGetByIdOrException(final long id) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapFoerderschwerpunktByID, id);
	}

	/**
	 * Gibt die Liste der Förderschwerpunkte zurück.
	 *
	 * @return die Liste der Förderschwerpunkte
	 */
	public @NotNull List<@NotNull FoerderschwerpunktEintrag> foerderschwerpunktGetMenge() {
		return this._foerderschwerpunkte;
	}


	/**
	 * Ermittelt die Informationen zu dem Jahrgang mit der angegebenen ID.
	 *
	 * @param id   die ID des Jahrgangs
	 *
	 * @return die Jahrgangs-Informationen
	 * @throws DeveloperNotificationException falls kein Jahrgang mit der ID existiert
	 */
	public @NotNull JahrgangsListeEintrag jahrgangGetByIdOrException(final long id) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapJahrgangByID, id);
	}

	/**
	 * Gibt die Liste der Jahrgänge zurück.
	 *
	 * @return die Liste der Jahrgänge
	 */
	public @NotNull List<@NotNull JahrgangsListeEintrag> jahrgangGetMenge() {
		return this._jahrgaenge;
	}


	/**
	 * Ermittelt die Informationen zu der Klasse mit der angegebenen ID.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Klassen-Informationen
	 * @throws DeveloperNotificationException falls keine Klasse mit der ID existiert
	 */
	public @NotNull KlassenListeEintrag klasseGetByIdOrException(final long id) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapKlasseByID, id);
	}

	/**
	 * Gibt die Liste der Klassen zurück.
	 *
	 * @return die Liste der Klassen
	 */
	public @NotNull List<@NotNull KlassenListeEintrag> klasseGetMenge() {
		return this._klassen;
	}


	/**
	 * Ermittelt die Informationen zu dem Kurs mit der angegebenen ID.
	 *
	 * @param id   die ID des Kurses
	 *
	 * @return die Kurs-Informationen
	 * @throws DeveloperNotificationException falls kein Kurs mit der ID existiert
	 */
	public @NotNull KursListeEintrag kursGetByIdOrException(final long id) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapKursByID, id);
	}

	/**
	 * Ermittelt die Informationen zu dem Kurs, sofern einer mit diesen Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Kurs-Informationen oder null, falls kein Kurs zugeordnet ist.
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public KursListeEintrag kursGetByLeistungIdOrNull(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		return _mapKursByID.get(leistung.kursID);
	}

	/**
	 * Ermittelt die Informationen zu dem Kurs, sofern einer mit diesen Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Kurs-Informationen
	 * @throws DeveloperNotificationException falls kein Kurs zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist
	 */
	public @NotNull KursListeEintrag kursGetByLeistungIdOrException(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		return DeveloperNotificationException.ifMapGetIsNull(_mapKursByID, leistung.kursID);
	}

	/**
	 * Gibt die Liste der Kurse zurück.
	 *
	 * @return die Liste der Kurse
	 */
	public @NotNull List<@NotNull KursListeEintrag> kursGetMenge() {
		return this._kurse;
	}

	/**
	 * Gibt die Liste der Kurse zurück und filtert diese anhand des Jahrgangs des Schülers sowie
	 * des Faches der Leistungsdaten.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die gefilterte Liste der Kurse
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public @NotNull List<@NotNull KursListeEintrag> kursGetMengeFilteredByLeistung(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		final @NotNull List<@NotNull KursListeEintrag> result = new ArrayList<>();
		for (final @NotNull KursListeEintrag k : this._kurse) {
			if ((k.idFach == leistung.fachID) && (k.idJahrgaenge.isEmpty() || k.idJahrgaenge.contains(this._lernabschnittsdaten.jahrgangID)))
				result.add(k);
		}
		return result;
	}


	/**
	 * Ermittelt die Informationen zu dem Lehrer mit der angegebenen ID.
	 *
	 * @param id   die ID des Lehrers
	 *
	 * @return die Lehrer-Informationen
	 * @throws DeveloperNotificationException falls kein Lehrer mit der ID existiert
	 */
	public @NotNull LehrerListeEintrag lehrerGetByIdOrException(final long id) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapLehrerByID, id);
	}

	/**
	 * Ermittelt die Informationen zu dem Lehrer, sofern einer mit diesen Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Lehrer-Informationen oder null, falls kein Lehrer zugeordnet ist.
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public LehrerListeEintrag lehrerGetByLeistungIdOrNull(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		return _mapLehrerByID.get(leistung.lehrerID);
	}

	/**
	 * Ermittelt die Informationen zu dem Lehrer, sofern einer mit diesen Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Lehrer-Informationen
	 * @throws DeveloperNotificationException falls kein Lehrer zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist
	 */
	public @NotNull LehrerListeEintrag lehrerGetByLeistungIdOrException(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		return DeveloperNotificationException.ifMapGetIsNull(_mapLehrerByID, leistung.lehrerID);
	}


	/**
	 * Gibt die Liste der Lehrer zurück.
	 *
	 * @return die Liste der Lehrer
	 */
	public @NotNull List<@NotNull LehrerListeEintrag> lehrerGetMenge() {
		return this._lehrer;
	}


	/**
	 * Ermittelt die Note, welche den Leistungsdaten zugewiesen ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die zugewiesene Note - falls keine zugewiesen ist wird Note.KEINE oder eine Pseudonote zurückgegeben
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public @NotNull Note noteGetByLeistungIdOrException(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		return Note.fromKuerzel(leistung.note);
	}


	/**
	 * Gibt die Informationen des Schülers zurück, zu dem die Lernabschnittsdaten gehören.
	 *
	 * @return die Informationen des Schülers
	 */
	public @NotNull SchuelerListeEintrag schuelerGet() {
		return this._schueler;
	}

}
