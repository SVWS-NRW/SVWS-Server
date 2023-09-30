package de.svws_nrw.core.utils.schueler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.core.data.kurse.KursListeEintrag;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Schüler-Lernabschnittsdaten eines Schülers.
 */
public class SchuelerLernabschnittManager {

	private final @NotNull SchuelerListeEintrag _schueler;

	private final @NotNull SchuelerLernabschnittsdaten _lernabschnittsdaten;
	private final @NotNull Map<@NotNull Long, @NotNull SchuelerLeistungsdaten> _mapLeistungById = new HashMap<>();

	private final @NotNull List<@NotNull FaecherListeEintrag> faecher = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull FaecherListeEintrag> _mapFachByID = new HashMap<>();

	private final @NotNull List<@NotNull KursListeEintrag> kurse = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull KursListeEintrag> _mapKursByID = new HashMap<>();

	private final @NotNull List<@NotNull LehrerListeEintrag> lehrer = new ArrayList<>();
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

	private static final @NotNull Comparator<@NotNull KursListeEintrag> _compKurs = (final @NotNull KursListeEintrag a, final @NotNull KursListeEintrag b) -> {
		int cmp = a.sortierung - b.sortierung;
		if (cmp != 0)
			return cmp;
		cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
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
	 * @param kurse                 der Katalog der Kurse
	 * @param lehrer                der Katalog der Lehrer
	 */
	public SchuelerLernabschnittManager(final @NotNull SchuelerListeEintrag schueler,
			final @NotNull SchuelerLernabschnittsdaten lernabschnittsdaten,
			final @NotNull List<@NotNull FaecherListeEintrag> faecher,
			final @NotNull List<@NotNull KursListeEintrag> kurse,
			final @NotNull List<@NotNull LehrerListeEintrag> lehrer) {
		this._schueler = schueler;
		this._lernabschnittsdaten = lernabschnittsdaten;
		initLeistungsdaten(lernabschnittsdaten.leistungsdaten);
		initFaecher(faecher);
		initKurse(kurse);
		initLehrer(lehrer);
	}

	private void initLeistungsdaten(final @NotNull List<@NotNull SchuelerLeistungsdaten> leistungsdaten) {
		for (final @NotNull SchuelerLeistungsdaten leistung : leistungsdaten) {
			leistungAdd(leistung);
		}
	}

	private void initFaecher(final @NotNull List<@NotNull FaecherListeEintrag> faecher) {
		this.faecher.clear();
		this.faecher.addAll(faecher);
		this.faecher.sort(_compFach);
		this._mapFachByID.clear();
		for (final @NotNull FaecherListeEintrag f: faecher)
			this._mapFachByID.put(f.id, f);
	}

	private void initKurse(final @NotNull List<@NotNull KursListeEintrag> kurse) {
		this.kurse.clear();
		this.kurse.addAll(kurse);
		this.kurse.sort(_compKurs);
		this._mapKursByID.clear();
		for (final @NotNull KursListeEintrag k: kurse)
			this._mapKursByID.put(k.id, k);
	}

	private void initLehrer(final @NotNull List<@NotNull LehrerListeEintrag> lehrer) {
		this.lehrer.clear();
		this.lehrer.addAll(lehrer);
		this.lehrer.sort(_compLehrer);
		this._mapLehrerByID.clear();
		for (final @NotNull LehrerListeEintrag l: lehrer)
			this._mapLehrerByID.put(l.id, l);
	}



	/**
	 * Fügt die übergebenen Leistungsdaten zu dem Lernabschnitt hinzu
	 *
	 * @param leistungsdaten   die hinzuzufügenden Leistungsdaten
	 */
	public void leistungAdd(final @NotNull SchuelerLeistungsdaten leistungsdaten) {
		this._mapLeistungById.put(leistungsdaten.id, leistungsdaten);
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
		return this.faecher;
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
		return this.kurse;
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
		return this.lehrer;
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

}
