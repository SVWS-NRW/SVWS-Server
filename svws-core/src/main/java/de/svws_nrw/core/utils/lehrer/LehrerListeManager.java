package de.svws_nrw.core.utils.lehrer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Lehrer-Listen.
 */
public final class LehrerListeManager extends AuswahlManager<Long, LehrerListeEintrag, LehrerStammdaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<LehrerListeEintrag, Long> _lehrerToId = (final @NotNull LehrerListeEintrag k) -> k.id;
	private static final @NotNull Function<LehrerStammdaten, Long> _lehrerDatenToId = (final @NotNull LehrerStammdaten k) -> k.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<Boolean, Long, LehrerListeEintrag> _mapKlasseIstSichtbar = new HashMap2D<>();
	private final @NotNull HashMap2D<Boolean, Long, LehrerListeEintrag> _mapLehrerIstStatistikrelevant = new HashMap2D<>();
	private final @NotNull HashMap2D<PersonalTyp, Long, LehrerListeEintrag> _mapKlasseHatPersonaltyp = new HashMap2D<>();

	/** Sets mit Listen zur aktuellen Auswahl */
	private final @NotNull HashSet<Long> idsReferenzierterLehrer = new HashSet<>();

	/** Das Filter-Attribut für die Personal-Typen */
	public final @NotNull AttributMitAuswahl<Integer, PersonalTyp> personaltypen;
	private static final @NotNull Function<PersonalTyp, Integer> _personaltypToId = (final @NotNull PersonalTyp pt) -> pt.id;
	private static final @NotNull Comparator<PersonalTyp> _comparatorPersonaltypen =
			(final @NotNull PersonalTyp a, final @NotNull PersonalTyp b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut auf nur sichtbare Lehrer */
	private boolean _filterNurSichtbar = true;

	/** Das Filter-Attribut auf nur Statistik-relevante Lehrer */
	private boolean _filterNurStatistikrelevant = true;

	/** Die Personal-Daten, sofern eine Auswahl vorhanden ist. */
	private LehrerPersonaldaten _personaldaten = null;
	private final @NotNull Map<Long, LehrerPersonalabschnittsdaten> _mapAbschnittsdatenById = new HashMap<>();
	private final @NotNull Map<Long, LehrerPersonalabschnittsdaten> _mapAbschnittsdatenBySchuljahresabschnittsId = new HashMap<>();



	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Lahrerauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param lehrer                       die Liste der Lehrer
	 */
	public LehrerListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<LehrerListeEintrag> lehrer) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, lehrer, LehrerUtils.comparator, _lehrerToId, _lehrerDatenToId,
				Arrays.asList(new Pair<>("nachname", true), new Pair<>("vorname", true), new Pair<>("kuerzel", true)));
		this.personaltypen =
				new AttributMitAuswahl<>(Arrays.asList(PersonalTyp.values()), _personaltypToId, _comparatorPersonaltypen, _eventHandlerFilterChanged);
		initLehrer();
	}


	private void initLehrer() {
		for (final @NotNull LehrerListeEintrag l : this.liste.list()) {
			this._mapKlasseIstSichtbar.put(l.istSichtbar, l.id, l);
			this._mapLehrerIstStatistikrelevant.put(l.istRelevantFuerStatistik, l.id, l);
			final PersonalTyp personalTyp = PersonalTyp.fromKuerzel(l.personTyp);
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
		if (daten.istSichtbar != eintrag.istSichtbar) {
			eintrag.istSichtbar = daten.istSichtbar;
			updateEintrag = true;
		}
		if (daten.istRelevantFuerStatistik != eintrag.istRelevantFuerStatistik) {
			eintrag.istRelevantFuerStatistik = daten.istRelevantFuerStatistik;
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
		return PersonalTyp.fromKuerzel(this._daten.personalTyp);
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
	@Override
	protected int compareAuswahl(final @NotNull LehrerListeEintrag a, final @NotNull LehrerListeEintrag b) {
		for (final Pair<String, Boolean> criteria : _order) {
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

	@Override
	protected void onMehrfachauswahlChanged() {
		this.idsReferenzierterLehrer.clear();
		for (final @NotNull LehrerListeEintrag l : this.liste.auswahl())
			if ((l.referenziertInAnderenTabellen != null) && l.referenziertInAnderenTabellen)
				this.idsReferenzierterLehrer.add(l.id);
	}

	/** Gibt das Set mit den LehrerIds zurück, die in der Auswahl sind und in anderen Datenbanktabellen referenziert werden
	 *
	 * @return Das Set mit IDs von Lehrern, die in anderen Datenbanktabellen referenziert werden
	 */
	public @NotNull Set<Long> getIdsReferenzierterLehrer() {
		return this.idsReferenzierterLehrer;
	}


	@Override
	protected boolean checkFilter(final @NotNull LehrerListeEintrag eintrag) {
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;
		if (this._filterNurStatistikrelevant && !eintrag.istRelevantFuerStatistik)
			return false;
		if (this.personaltypen.auswahlExists()) {
			if ((eintrag.personTyp == null) || (eintrag.personTyp.isEmpty()))
				return false;
			final PersonalTyp personalTyp = PersonalTyp.fromKuerzel(eintrag.personTyp);
			if ((personalTyp == null) || (!this.personaltypen.auswahlHas(personalTyp)))
				return false;
		}
		return true;
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

	public void useFilter(final @NotNull LehrerListeManager srcManager) {
		this.personaltypen.setAuswahl(srcManager.personaltypen);
		this.setFilterNurStatistikRelevant(srcManager.filterNurStatistikRelevant());
		this.setFilterNurSichtbar(srcManager.filterNurSichtbar());
		this.setFilterAuswahlPermitted(srcManager.isFilterAuswahlPermitted());
	}

}
