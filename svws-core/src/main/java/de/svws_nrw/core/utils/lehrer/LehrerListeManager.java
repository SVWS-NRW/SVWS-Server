package de.svws_nrw.core.utils.lehrer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.core.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.core.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der Lehrer-Listen.
 */
public class LehrerListeManager extends AuswahlManager<@NotNull Long, @NotNull LehrerListeEintrag, @NotNull LehrerStammdaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<@NotNull LehrerListeEintrag, @NotNull Long> _lehrerToId = (final @NotNull LehrerListeEintrag k) -> k.id;
	private static final @NotNull Function<@NotNull LehrerStammdaten, @NotNull Long> _lehrerDatenToId = (final @NotNull LehrerStammdaten k) -> k.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<@NotNull Boolean, @NotNull Long, @NotNull LehrerListeEintrag> _mapKlasseIstSichtbar = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Boolean, @NotNull Long, @NotNull LehrerListeEintrag> _mapLehrerIstStatistikrelevant = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull PersonalTyp, @NotNull Long, @NotNull LehrerListeEintrag> _mapKlasseHatPersonaltyp = new HashMap2D<>();

	/** Das Filter-Attribut für die Personal-Typen */
	public final @NotNull AttributMitAuswahl<@NotNull Integer, @NotNull PersonalTyp> personaltypen;
	private static final @NotNull Function<@NotNull PersonalTyp, @NotNull Integer> _personaltypToId = (final @NotNull PersonalTyp pt) -> pt.id;
	private static final @NotNull Comparator<@NotNull PersonalTyp> _comparatorPersonaltypen = (final @NotNull PersonalTyp a, final @NotNull PersonalTyp b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut auf nur sichtbare Lehrer */
	private boolean _filterNurSichtbar = true;

	/** Das Filter-Attribut auf nur Statistik-relevante Lehrer */
	private boolean _filterNurStatistikrelevant = true;

	/** Die Personal-Daten, sofern eine Auswahl vorhanden ist. */
	private LehrerPersonaldaten _personaldaten = null;
	private final @NotNull Map<@NotNull Long, @NotNull LehrerPersonalabschnittsdaten> _mapAbschnittsdatenById = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull LehrerPersonalabschnittsdaten> _mapAbschnittsdatenBySchuljahresabschnittsId = new HashMap<>();



	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform     die Schulform der Schule
	 * @param lehrer        die Liste der Lehrer
	 */
	public LehrerListeManager(final Schulform schulform,
			final @NotNull List<@NotNull LehrerListeEintrag> lehrer) {
		super(schulform, lehrer, LehrerUtils.comparator, _lehrerToId, _lehrerDatenToId,
				Arrays.asList(new Pair<>("nachname", true), new Pair<>("vorname", true), new Pair<>("kuerzel", true)));
		this.personaltypen = new AttributMitAuswahl<>(Arrays.asList(PersonalTyp.values()), _personaltypToId, _comparatorPersonaltypen, _eventHandlerFilterChanged);
		initLehrer();
	}


	private void initLehrer() {
		for (final @NotNull LehrerListeEintrag l : this.liste.list()) {
			this._mapKlasseIstSichtbar.put(l.istSichtbar, l.id, l);
			this._mapLehrerIstStatistikrelevant.put(l.istRelevantFuerStatistik, l.id, l);
			final PersonalTyp personalTyp = PersonalTyp.fromBezeichnung(l.personTyp);
			if (personalTyp != null)
				this._mapKlasseHatPersonaltyp.put(personalTyp, l.id, l);
		}
	}


	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 * Prüfe auch, ob die Lehrer-ID mit der ID der Personaldaten übereinstimmt
	 * und setzt diese ggf. auf null.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull LehrerListeEintrag eintrag, final @NotNull LehrerStammdaten daten) {
		// Prüfe, ob die Personaldaten zu der aktuellen Auswahl passen und setze diese ggf. zurück.
		if ((this._personaldaten != null) && (this._personaldaten.id != eintrag.id))
			clearPersonalDaten();
		// Passe ggf. die Daten in der Lehrerliste an ... (beim Patchen der Daten)
		boolean updateEintrag = false;
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!daten.nachname.equals(eintrag.nachname)) {
			eintrag.nachname = daten.nachname;
			updateEintrag = true;
		}
		if (!daten.vorname.equals(eintrag.vorname)) {
			eintrag.vorname = daten.vorname;
			updateEintrag = true;
		}
		return updateEintrag;
	}


	/**
	 * Gibt den Personaltyp für den aktuell ausgewählten Lehrer zurück.
	 *
	 * @return der Personaltyp
	 */
	public PersonalTyp datenGetPersonalTyp() {
		if ((this._daten == null) || (this._daten.personalTyp == null))
			return null;
		return PersonalTyp.fromBezeichnung(this._daten.personalTyp);
	}


	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Lehrer zurück.
	 *
	 * @return true, wenn nur nichtbare Lehrer angezeigt werden und ansonsten false
	 */
	public boolean filterNurSichtbar() {
		return this._filterNurSichtbar;
	}


	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Lehrer.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public void setFilterNurSichtbar(final boolean value) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}


	/**
	 * Gibt die aktuelle Filtereinstellung auf nur Statistik-relevante Lehrer zurück.
	 *
	 * @return true, wenn nur Statistik-relevante Lehrer angezeigt werden und ansonsten false
	 */
	public boolean filterNurStatistikRelevant() {
		return this._filterNurStatistikrelevant;
	}


	/**
	 * Setzt die Filtereinstellung auf nur Statistik-relevante Lehrer.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public void setFilterNurStatistikRelevant(final boolean value) {
		this._filterNurStatistikrelevant = value;
		this._eventHandlerFilterChanged.run();
	}


	/**
	 * Vergleicht zwei Lehrerlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected int compare(final @NotNull LehrerListeEintrag a, final @NotNull LehrerListeEintrag b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("kuerzel".equals(field)) {
				cmp = a.kuerzel.compareTo(b.kuerzel);
			} else if ("nachname".equals(field)) {
				cmp = a.nachname.compareTo(b.nachname);
			} else if ("vorname".equals(field)) {
				cmp = a.vorname.compareTo(b.vorname);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}


	/**
	 * Gibt eine gefilterte Liste der Lehrer zurück. Als Filter werden dabei
	 * die Personaltypen, ein Filter auf nur sichtbare Lehrer und einer
	 * auf nur Statistik-relevante Lehrer beachtet.
	 *
	 * @return die gefilterte Liste
	 */
	@Override
	protected @NotNull List<@NotNull LehrerListeEintrag> onFilter() {
		final @NotNull List<@NotNull LehrerListeEintrag> tmpList = new ArrayList<>();
		for (final @NotNull LehrerListeEintrag eintrag : this.liste.list()) {
			if (this._filterNurSichtbar && !eintrag.istSichtbar)
				continue;
			if (this._filterNurStatistikrelevant && !eintrag.istRelevantFuerStatistik)
				continue;
			if (this.personaltypen.auswahlExists()) {
				if ((eintrag.personTyp == null) || (eintrag.personTyp.isEmpty()))
					continue;
				final PersonalTyp personalTyp = PersonalTyp.fromBezeichnung(eintrag.personTyp);
				if ((personalTyp == null) || (!this.personaltypen.auswahlHas(personalTyp)))
					continue;
			}
			tmpList.add(eintrag);
		}
		final @NotNull Comparator<@NotNull LehrerListeEintrag> comparator = (final @NotNull LehrerListeEintrag a, final @NotNull LehrerListeEintrag b) -> this.compare(a, b);
		tmpList.sort(comparator);
		return tmpList;
	}


	/**
	 * Entfernt die Personalabschnittsdaten und leert die zugehörigen Maps.
	 */
	private void clearPersonalDaten() {
		this._personaldaten = null;
		this._mapAbschnittsdatenById.clear();
		this._mapAbschnittsdatenBySchuljahresabschnittsId.clear();
	}


	/**
	 * Gibt zurück, ob Personal-Daten vorliegen.
	 *
	 * @return true, wenn Personal-Daten vorliegen, und ansonsten false
	 */
	public boolean hasPersonalDaten() {
		return this._personaldaten != null;
	}


	/**
	 * Gibt die Personal-Daten zurück, sofern diese zur aktuellen Auswahl
	 * geladen wurden.
	 *
	 * @return die Personal-Daten
	 */
	public @NotNull LehrerPersonaldaten personalDaten() {
		if (this._personaldaten == null)
			throw new DeveloperNotificationException("Es exitsieren derzeit keine Personal-Daten");
		return this._personaldaten;
	}


	/**
	 * Setzt die Personal-Daten. Dabei wird ggf. die Auswahl angepasst.
	 *
	 * @param personaldaten   die neuen Personal-Daten
	 *
	 * @throws DeveloperNotificationException   falls die Personal-Daten nicht zu der Auswahl passen
	 */
	public void setPersonalDaten(final LehrerPersonaldaten personaldaten) throws DeveloperNotificationException {
		// Die Personal-Daten werden zurückgesetzt
		if (personaldaten == null) {
			clearPersonalDaten();
			return;
		}
		// Bestimme den Auswahl-Eintrag. Dieser sollte immer vorhanden sein. Wenn nicht, dann liegt ein Fehler vor...
		this.liste.getOrException(personaldaten.id);
		// ... und setze die neue Daten
		final boolean neueLehrerId = ((this._personaldaten == null) || (this._personaldaten.id != personaldaten.id));
		this._personaldaten = personaldaten;
		if (neueLehrerId) {
			this._mapAbschnittsdatenById.clear();
			this._mapAbschnittsdatenBySchuljahresabschnittsId.clear();
			for (final @NotNull LehrerPersonalabschnittsdaten abschnittsdaten : personaldaten.abschnittsdaten) {
				this._mapAbschnittsdatenById.put(abschnittsdaten.id, abschnittsdaten);
				this._mapAbschnittsdatenBySchuljahresabschnittsId.put(abschnittsdaten.idSchuljahresabschnitt, abschnittsdaten);
			}
		}
	}


	/**
	 * Gibt die Personalabschnittsdaten für den Abschnitt mit der angegebenen ID zurück.
	 *
	 * @param id   die ID der Personalabschnittsdaten
	 *
	 * @return die Personalabschnittsdaten oder null, falls für die ID keine vorhanden sind.
	 */
	public LehrerPersonalabschnittsdaten getAbschnittById(final long id) {
		return this._mapAbschnittsdatenById.get(id);
	}


	/**
	 * Gibt die Personalabschnittsdaten für den Abschnitt mit der angegebenen Schuljahresabschnitts-ID zurück.
	 *
	 * @param id   die ID des Schuljahresabschnitts
	 *
	 * @return die Personalabschnittsdaten oder null, falls für die Schuljahresabschnitts-ID keine vorhanden sind.
	 */
	public LehrerPersonalabschnittsdaten getAbschnittBySchuljahresabschnittsId(final long id) {
		return this._mapAbschnittsdatenBySchuljahresabschnittsId.get(id);
	}

}
