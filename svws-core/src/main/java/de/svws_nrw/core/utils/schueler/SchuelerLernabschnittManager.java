package de.svws_nrw.core.utils.schueler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.klassen.KlassenUtils;
import de.svws_nrw.core.utils.kurse.KursUtils;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Schüler-Lernabschnittsdaten eines Schülers.
 */
public class SchuelerLernabschnittManager {

	private final @NotNull Schulform _schulform;
	private final @NotNull SchuelerListeEintrag _schueler;

	private final @NotNull SchuelerLernabschnittsdaten _lernabschnittsdaten;
	private final @NotNull Schuljahresabschnitt _schuljahresabschnitt;
	private final @NotNull Map<Long, SchuelerLeistungsdaten> _mapLeistungById = new HashMap<>();

	private final @NotNull List<FachDaten> _faecher = new ArrayList<>();
	private final @NotNull Map<Long, FachDaten> _mapFachByID = new HashMap<>();

	private final @NotNull List<FoerderschwerpunktEintrag> _foerderschwerpunkte = new ArrayList<>();
	private final @NotNull Map<Long, FoerderschwerpunktEintrag> _mapFoerderschwerpunktByID = new HashMap<>();

	private final @NotNull List<JahrgangsDaten> _jahrgaenge = new ArrayList<>();
	private final @NotNull Map<Long, JahrgangsDaten> _mapJahrgangByID = new HashMap<>();

	private final @NotNull List<KlassenDaten> _klassen = new ArrayList<>();
	private final @NotNull Map<Long, KlassenDaten> _mapKlasseByID = new HashMap<>();

	private final @NotNull List<KursDaten> _kurse = new ArrayList<>();
	private final @NotNull Map<Long, KursDaten> _mapKursByID = new HashMap<>();

	private final @NotNull List<LehrerListeEintrag> _lehrer = new ArrayList<>();
	private final @NotNull Map<Long, LehrerListeEintrag> _mapLehrerByID = new HashMap<>();


	private static final @NotNull Comparator<FachDaten> _compFach =
			(final @NotNull FachDaten a, final @NotNull FachDaten b) -> {
				int cmp = a.sortierung - b.sortierung;
				if (cmp != 0)
					return cmp;
				if ((a.kuerzel == null) || (b.kuerzel == null))
					throw new DeveloperNotificationException("Fachkürzel dürfen nicht null sein");
				cmp = a.kuerzel.compareTo(b.kuerzel);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	private static final @NotNull Comparator<FoerderschwerpunktEintrag> _compFoerderschwerpunkte =
			(final @NotNull FoerderschwerpunktEintrag a, final @NotNull FoerderschwerpunktEintrag b) -> {
				if (a.text == null)
					return -1;
				if (b.text == null)
					return 1;
				return a.text.compareTo(b.text);
			};

	private static final @NotNull Comparator<LehrerListeEintrag> _compLehrer =
			(final @NotNull LehrerListeEintrag a, final @NotNull LehrerListeEintrag b) -> {
				int cmp = a.sortierung - b.sortierung;
				if (cmp != 0)
					return cmp;
				cmp = a.nachname.compareTo(b.nachname);
				if (cmp != 0)
					return cmp;
				cmp = a.vorname.compareTo(b.vorname);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	private final @NotNull Comparator<SchuelerLeistungsdaten> _compLeistungenByFach =
			(final @NotNull SchuelerLeistungsdaten a, final @NotNull SchuelerLeistungsdaten b) -> {
				final @NotNull FachDaten aFach = DeveloperNotificationException.ifMapGetIsNull(_mapFachByID, a.fachID);
				final @NotNull FachDaten bFach = DeveloperNotificationException.ifMapGetIsNull(_mapFachByID, b.fachID);
				return _compFach.compare(aFach, bFach);
			};



	/**
	 * Erstellt einen neuen Manager mit den übergebenen Lernabschnittsdaten und den übergebenen Katalogen
	 *
	 * @param schulform             die Schulform der Schule des Schülers
	 * @param schueler              Informationen zu dem Schüler
	 * @param lernabschnittsdaten   die Lernabschnittsdaten
	 * @param schuljahresabschnitt  der Schuljahresabschnitt der Lernabschnittsdaten
	 * @param faecher               der Katalog der Fächer
	 * @param jahrgaenge            der Katalog der Jahrgänge
	 * @param klassen               der Katalog der Klassen
	 * @param kurse                 der Katalog der Kurse
	 * @param lehrer                der Katalog der Lehrer
	 * @param foerderschwerpunkte   der Katalog der Förderschwerpunkte
	 */
	public SchuelerLernabschnittManager(final @NotNull Schulform schulform,
			final @NotNull SchuelerListeEintrag schueler,
			final @NotNull SchuelerLernabschnittsdaten lernabschnittsdaten,
			final @NotNull Schuljahresabschnitt schuljahresabschnitt,
			final @NotNull List<FachDaten> faecher,
			final @NotNull List<FoerderschwerpunktEintrag> foerderschwerpunkte,
			final @NotNull List<JahrgangsDaten> jahrgaenge,
			final @NotNull List<KlassenDaten> klassen,
			final @NotNull List<KursDaten> kurse,
			final @NotNull List<LehrerListeEintrag> lehrer) {
		this._schulform = schulform;
		this._schueler = schueler;
		this._lernabschnittsdaten = lernabschnittsdaten;
		this._schuljahresabschnitt = schuljahresabschnitt;
		initLeistungsdaten(lernabschnittsdaten.leistungsdaten);
		initFaecher(faecher);
		initFoerderschwerpunkte(foerderschwerpunkte);
		initJahrgaenge(jahrgaenge);
		initKlassen(klassen);
		initKurse(kurse);
		initLehrer(lehrer);
	}

	private void initLeistungsdaten(final @NotNull List<SchuelerLeistungsdaten> leistungsdaten) {
		for (final @NotNull SchuelerLeistungsdaten leistung : leistungsdaten)
			this.leistungAddInternal(leistung);
	}

	private void initFaecher(final @NotNull List<FachDaten> faecher) {
		this._faecher.clear();
		this._faecher.addAll(faecher);
		this._faecher.sort(_compFach);
		this._mapFachByID.clear();
		for (final @NotNull FachDaten f : faecher)
			this._mapFachByID.put(f.id, f);
	}

	private void initFoerderschwerpunkte(final @NotNull List<FoerderschwerpunktEintrag> foerderschwerpunkte) {
		this._foerderschwerpunkte.clear();
		this._foerderschwerpunkte.addAll(foerderschwerpunkte);
		this._foerderschwerpunkte.sort(_compFoerderschwerpunkte);
		this._mapFoerderschwerpunktByID.clear();
		for (final @NotNull FoerderschwerpunktEintrag f : foerderschwerpunkte)
			this._mapFoerderschwerpunktByID.put(f.id, f);
	}

	private void initJahrgaenge(final @NotNull List<JahrgangsDaten> jahrgaenge) {
		this._jahrgaenge.clear();
		this._jahrgaenge.addAll(jahrgaenge);
		this._jahrgaenge.sort(JahrgangsUtils.comparator);
		this._mapJahrgangByID.clear();
		for (final @NotNull JahrgangsDaten j : jahrgaenge)
			this._mapJahrgangByID.put(j.id, j);
	}

	private void initKlassen(final @NotNull List<KlassenDaten> klassen) {
		this._klassen.clear();
		this._klassen.addAll(klassen);
		this._klassen.sort(KlassenUtils.comparator);
		this._mapKlasseByID.clear();
		for (final @NotNull KlassenDaten k : klassen)
			this._mapKlasseByID.put(k.id, k);
	}

	private void initKurse(final @NotNull List<KursDaten> kurse) {
		this._kurse.clear();
		this._kurse.addAll(kurse);
		this._kurse.sort(KursUtils.comparator);
		this._mapKursByID.clear();
		for (final @NotNull KursDaten k : kurse)
			this._mapKursByID.put(k.id, k);
	}

	private void initLehrer(final @NotNull List<LehrerListeEintrag> lehrer) {
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


	/**
	 * Gibt die Schulgliederung zurück, die dem Lernabschnitt zugeordnet ist oder
	 * null, falls keine Zuordnung existiert.
	 *
	 * @return die Schulgliederung oder null
	 */
	public Schulgliederung lernabschnittGetGliederung() {
		if (this._lernabschnittsdaten.schulgliederung == null)
			return null;
		return Schulgliederung.data().getWertByKuerzel(this._lernabschnittsdaten.schulgliederung);
	}


	/**
	 * Gibt den Statistik-Jahrgang zurück, der dem Lernabschnitt zugeordnet ist oder null,
	 * falls kein Jahrgang zugeordnet ist.
	 *
	 * @return der Statistik-Jahrgang
	 */
	public Jahrgaenge lernabschnittGetStatistikJahrgang() {
		if (this._lernabschnittsdaten.jahrgangID == null)
			return null;
		final JahrgangsDaten eintrag = _mapJahrgangByID.get(this._lernabschnittsdaten.jahrgangID);
		if ((eintrag == null) || (eintrag.kuerzelStatistik == null))
			return null;
		return Jahrgaenge.data().getWertByKuerzel(eintrag.kuerzelStatistik);
	}


	/**
	 * Gibt die Bezeichnung für die Lernbereichtsnote 1 zurück, sofern eine angegeben werden kann.
	 *
	 * @return die Bezeichnung für die Lernbereichtsnote 1
	 */
	public String lernabschnittGetLernbereichsnote1Bezeichnung() {
		final Jahrgaenge jg = lernabschnittGetStatistikJahrgang();
		if (jg == null)
			return null;
		return jg.getLernbereichsnote1Bezeichnung(_schulform, lernabschnittGetGliederung(), _schuljahresabschnitt.schuljahr);
	}


	/**
	 * Gibt die Bezeichnung für die Lernbereichtsnote 2 zurück, sofern eine angegeben werden kann.
	 *
	 * @return die Bezeichnung für die Lernbereichtsnote 2
	 */
	public String lernabschnittGetLernbereichsnote2Bezeichnung() {
		final Jahrgaenge jg = lernabschnittGetStatistikJahrgang();
		if (jg == null)
			return null;
		return jg.getLernbereichsnote2Bezeichnung(_schulform, lernabschnittGetGliederung(), _schuljahresabschnitt.schuljahr);
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
	public @NotNull List<SchuelerLeistungsdaten> leistungGetMengeAsListSortedByFach() {
		final @NotNull List<SchuelerLeistungsdaten> result = new ArrayList<>();
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
	public @NotNull FachDaten fachGetByIdOrException(final long id) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapFachByID, id);
	}

	/**
	 * Ermittelt die Informationen zum Fach, welche mit den Leistungsdaten verknüpft sind.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Fach-Informationen oder null, wenn kein Fach zugeordnet ist
	 */
	public FachDaten fachGetByLeistungId(final long idLeistung) {
		final SchuelerLeistungsdaten leistung = _mapLeistungById.get(idLeistung);
		if (leistung == null)
			return null;
		return _mapFachByID.get(leistung.fachID);
	}

	/**
	 * Ermittelt die Informationen zum Fach, welche mit den Leistungsdaten verknüpft sind.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Fach-Informationen.
	 * @throws DeveloperNotificationException falls kein Fach zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist
	 */
	public @NotNull FachDaten fachGetByLeistungIdOrException(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		return DeveloperNotificationException.ifMapGetIsNull(_mapFachByID, leistung.fachID);
	}

	/**
	 * Ermittelt die Informationen zu der Fach-Farbe, welche den Leistungsdaten zugeordnet ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Farbe und falls kein Fach zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist, die Default-Farbe rgb(220,220,220)
	 */
	public @NotNull String fachFarbeGetByLeistungsIdOrDefault(final long idLeistung) {
		final FachDaten fachDaten = fachGetByLeistungId(idLeistung);
		if (fachDaten == null)
			return "rgb(220,220,220)";
		return Fach.getBySchluesselOrDefault(fachDaten.kuerzel).getHMTLFarbeRGB(_schuljahresabschnitt.schuljahr);
	}

	/**
	 * Gibt die Liste der Fächer zurück.
	 *
	 * @return die Liste der Fächer
	 */
	public @NotNull List<FachDaten> fachGetMenge() {
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
	public @NotNull List<FoerderschwerpunktEintrag> foerderschwerpunktGetMenge() {
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
	public @NotNull JahrgangsDaten jahrgangGetByIdOrException(final long id) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapJahrgangByID, id);
	}

	/**
	 * Gibt die Liste der Jahrgänge zurück.
	 *
	 * @return die Liste der Jahrgänge
	 */
	public @NotNull List<JahrgangsDaten> jahrgangGetMenge() {
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
	public @NotNull KlassenDaten klasseGetByIdOrException(final long id) {
		return DeveloperNotificationException.ifMapGetIsNull(_mapKlasseByID, id);
	}

	/**
	 * Gibt die Liste der Klassen zurück.
	 *
	 * @return die Liste der Klassen
	 */
	public @NotNull List<KlassenDaten> klasseGetMenge() {
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
	public @NotNull KursDaten kursGetByIdOrException(final long id) {
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
	public KursDaten kursGetByLeistungIdOrNull(final long idLeistung) {
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
	public @NotNull KursDaten kursGetByLeistungIdOrException(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		return DeveloperNotificationException.ifMapGetIsNull(_mapKursByID, leistung.kursID);
	}

	/**
	 * Gibt die Liste der Kurse zurück.
	 *
	 * @return die Liste der Kurse
	 */
	public @NotNull List<KursDaten> kursGetMenge() {
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
	public @NotNull List<KursDaten> kursGetMengeFilteredByLeistung(final long idLeistung) {
		final @NotNull SchuelerLeistungsdaten leistung = DeveloperNotificationException.ifMapGetIsNull(_mapLeistungById, idLeistung);
		final @NotNull List<KursDaten> result = new ArrayList<>();
		for (final @NotNull KursDaten k : this._kurse) {
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
	public @NotNull List<LehrerListeEintrag> lehrerGetMenge() {
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
	 * Gibt die Schulform der Schule des Schülers zurück.
	 *
	 * @return die Schulform der Schule des Schülers
	 */
	public @NotNull Schulform schulformGet() {
		return this._schulform;
	}


	/**
	 * Gibt den Schuljahresabschnitt des Lernabschnittes zurück.
	 *
	 * @return der Schuljahresabschnitt des Lernabschnittes
	 */
	public @NotNull Schuljahresabschnitt schuljahresabschnittGet() {
		return this._schuljahresabschnitt;
	}


	/**
	 * Gibt das Schuljahr des Lernabschnittes zurück.
	 *
	 * @return das Schuljahr des Lernabschnittes
	 */
	public int schuljahrGet() {
		return this._schuljahresabschnitt.schuljahr;
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
