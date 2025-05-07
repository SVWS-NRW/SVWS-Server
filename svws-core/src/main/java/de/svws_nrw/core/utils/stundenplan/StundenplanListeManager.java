package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.DateUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der Stundenplan-Listen.
 */
public final class StundenplanListeManager extends AuswahlManager<Long, StundenplanListeEintrag, StundenplanManager> {

	/** Ein Default-Comparator für den Vergleich von Stundenplänen in Stundenplanlisten. */
	public static final @NotNull Comparator<StundenplanListeEintrag> comparator =
			(final @NotNull StundenplanListeEintrag a, final @NotNull StundenplanListeEintrag b) -> {
				int cmp = a.schuljahr - b.schuljahr;
				if (cmp != 0)
					return cmp;
				cmp = a.abschnitt - b.abschnitt;
				if (cmp != 0)
					return cmp;
				cmp = (a.aktiv == b.aktiv) ? 0 : (a.aktiv ? -1 : 1);
				if (cmp != 0)
					return cmp;
				if ((a.gueltigAb != null) && (b.gueltigAb != null))
					cmp = a.gueltigAb.compareTo(b.gueltigAb);
				return cmp != 0 ? cmp : Long.compare(a.id, b.id);
			};

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<StundenplanListeEintrag, Long> _listeEintragToId = (final @NotNull StundenplanListeEintrag s) -> s.id;
	private static final @NotNull Function<StundenplanManager, Long> _stundenplanToId = (final @NotNull StundenplanManager s) -> s.stundenplanGetID();

	/** Das Filter-Attribut auf nur aktive Stundenpläne */
	private boolean _filterNurAktiv = false;
	/** Die gefilterte Liste, sofern sie schon berechnet wurde */
	protected List<StundenplanListeEintrag> _aktive = null;

	private StundenplanListeEintrag _stundenplanVorlage;

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param stundenplanListe    die Liste der Stundenpläne
	 * @param createVorlage ob eine Vorlage erstellt werden soll
	 */
	public StundenplanListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<StundenplanListeEintrag> stundenplanListe, final boolean createVorlage) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, stundenplanListe, comparator, _listeEintragToId,
				_stundenplanToId,
				Arrays.asList());
		if (createVorlage)
			addStundenplanVorlage();
	}

	@Override
	protected boolean checkFilter(final @NotNull StundenplanListeEintrag eintrag) {
		if (this._filterNurAktiv && !eintrag.aktiv)
			return false;
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull StundenplanListeEintrag a, final @NotNull StundenplanListeEintrag b) {
		int cmp = a.schuljahr - b.schuljahr;
		if (cmp != 0)
			return cmp;
		cmp = a.abschnitt - b.abschnitt;
		if (cmp != 0)
			return cmp;
		if (a == _stundenplanVorlage)
			return -1;
		if (b == _stundenplanVorlage)
			return 1;
		cmp = (a.aktiv == b.aktiv) ? 0 : (a.aktiv ? -1 : 1);
		if (cmp != 0)
			return cmp;
		for (final Pair<String, Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			if ("gueltigAb".equals(field))
				cmp = a.gueltigAb.compareTo(b.gueltigAb);
			else if ("bezeichnung".equals(field))
				cmp = a.bezeichnung.compareTo(b.bezeichnung);
			else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);

	}

	/**
	 * Gibt eine gefilterte Auswahl-Liste zurück. Für die Filterung
	 * muss der Manager die Methode onFilter überschreiben.
	 *
	 * @return die gefilterte Liste
	 */
	@Override
	public @NotNull List<StundenplanListeEintrag> filtered() {
		final boolean hasCache = _filtered != null;
		final List<StundenplanListeEintrag> filtered = super.filtered();
		if (hasCache)
			return filtered;
		if (_stundenplanVorlage != null && !_filterNurAktiv)
			filtered.addFirst(_stundenplanVorlage);
		return filtered;
	}

	/**
	 * Gibt eine Auswahl-Liste aller aktiven Stundenpläne zurück.
	 *
	 * @return die Liste der aktiven Stundenpläne
	 */
	public @NotNull List<StundenplanListeEintrag> aktive() {
		if (_filtered == null || _aktive == null) {
			_aktive = new ArrayList<>();
			for (final StundenplanListeEintrag stundenplan : liste.list()) {
				if (stundenplan.aktiv)
					_aktive.add(stundenplan);
			}
		}
		return _aktive;
	}

	/**
	 * Setzt die Daten. Dabei wird ggf. die Auswahl angepasst. Die vorherige Auswahl wird gespeichert.
	 *
	 * @param daten   die neuen Daten
	 *
	 * @throws DeveloperNotificationException   falls die Daten nicht in der Auswahlliste vorhanden ist
	 */
	@Override
	public void setDaten(final StundenplanManager daten) throws DeveloperNotificationException {
		if (daten != null && daten.stundenplanGetID() == -1) {
			// vorherige Auswahl speichern, um diese ggf. wiederherstellen zu können
			this._vorherigeAuswahl = this._daten;
			// ... und setze die neue Daten
			this._daten = daten;
			// ... und triggere, dass der Filter neu angewendet wird
			this._filtered = null;
		} else {
			super.setDaten(daten);
		}
	}

	/**
	 * Gibt den Eintrag der aktuellen Auswahl in der Liste zurück. Hiefür muss eine
	 * gültige Auswahl vorliegen. Dies kann ggf. vorher über hasDaten geprüft werden.
	 *
	 * @return der Eintrag in der Auswahlliste
	 *
	 * @throws DeveloperNotificationException wenn keine gültige Auswahl vorliegt
	 */
	@Override
	public @NotNull StundenplanListeEintrag auswahl() {
		if (this._daten == null || this._daten.stundenplanGetID() != -1)
			return super.auswahl();
		if (_stundenplanVorlage == null)
			throw new DeveloperNotificationException("Es existiert kein Vorlagen-Stundenplan.");
		return _stundenplanVorlage;
	}

	/**
	 * Die Bezeichnung ist obligatorisch und darf maximal 150 Zeichen lang sein.
	 *
	 * @param bezeichnung die Bezeichnung des Stundenplans
	 *
	 * @return <code>true</code> wenn die Bezeichnung des Stundenplans gültig ist, ansonsten <code>false</code>
	 */
	public static boolean validateBezeichnung(final String bezeichnung) {
		if (bezeichnung == null)
			return false;
		return bezeichnung.trim().length() <= 150;
	}

	/**
	 * Prüft für die aktuelle Auswahl eine neue Gültigkeit. Wenn das Datum leer oder hinter dem Gültigkeitsende befindet,
	 * wird <code>false</code>, andernfalls <code>true</code> zurückgegeben. Je nach Parameter aktiv wird auch geprüft, ob es sich innerhalb der Gültigkeit eines anderen aktiven Stundenplans befindet.
	 *
	 * @param gueltigAb das Datum, ab wann der Stundenplan gültig sein soll
	 * @param gueltigBis das Datum, bis wann der Stundenplan gültig sein soll. Falls null übergeben wird, wird das Datum der Auswahl verwendet.
	 * @param aktiv falls true, werden zusätzlich die anderen aktiven Stundenpläne geprüft
	 * @param checkUeberschneidung falls true, wird zusätzlich geprüft, ob es eine Überschneidung mit einem anderen Stundenplan gibt
	 *
	 * @return <code>true</code> wenn das Datum gültig ist, ansonsten <code>false</code>
	 */
	public boolean validateGueltigAb(final String gueltigAb, final String gueltigBis, final boolean aktiv, final boolean checkUeberschneidung) {
		if (gueltigAb == null || !DateUtils.isValidDate(gueltigAb))
			return false;
		final String gueltigBisComputed = (gueltigBis != null ? gueltigBis : auswahl().gueltigBis);
		if (gueltigAb.compareTo(gueltigBisComputed) > 0)
			return false;
		if (aktiv || checkUeberschneidung) {
			for (final StundenplanListeEintrag stundenplan : aktive())
				if ((!hasDaten() || stundenplan.id != auswahl().id) && (stundenplan.gueltigAb.compareTo(gueltigAb) <= 0)
						&& (stundenplan.gueltigBis.compareTo(gueltigAb) >= 0))
					return false;
			if (checkUeberschneidung)
				return istKonfliktfreiZuAktivenStundenplaenen(gueltigAb, gueltigBisComputed);
		}
		return true;
	}

	/**
	 * Prüft für die aktuelle Auswahl eine neue Gültigkeit. Wenn das Datum leer oder sich vor dem Gültigkeitsbeginn befindet,
	 * wird <code>false</code>, andernfalls <code>true</code> zurückgegeben.  Je nach Parameter aktiv wird auch geprüft, ob es sich innerhalb der Gültigkeit eines anderen aktiven Stundenplans befindet.
	 *
	 * @param gueltigAb das Datum, ab wann der Stundenplan gültig sein soll. Falls null übergeben wird, wird das Datum der Auswahl verwendet.
	 * @param gueltigBis das Datum, bis zu dem der Stundenplan gültig sein soll
	 * @param aktiv falls true, werden zusätzlich die anderen aktiven Stundenpläne geprüft
	 *
	 * @return <code>true</code> wenn das Datum gültig ist, ansonsten <code>false</code>
	 */
	public boolean validateGueltigBis(final String gueltigAb, final String gueltigBis, final boolean aktiv) {
		if (gueltigBis == null || !DateUtils.isValidDate(gueltigBis))
			return false;
		final String gueltigAbComputed = (gueltigAb != null ? gueltigAb : auswahl().gueltigAb);
		if (gueltigBis.compareTo(gueltigAbComputed) < 0)
			return false;
		if (aktiv) {
			for (final StundenplanListeEintrag stundenplan : aktive())
				if ((!hasDaten() || stundenplan.id != auswahl().id) && (stundenplan.gueltigAb.compareTo(gueltigBis) <= 0)
						&& (stundenplan.gueltigBis.compareTo(gueltigBis) >= 0))
					return false;
		}
		return true;
	}

	/**
	 * Prüft, ob der aktuell ausgewählte Stundenplan mit den übergebenen Gültigkeitsdaten eine Überschneidung mit einem anderen Stundenplan gibt.
	 *
	 * @param gueltigAb das Datum, ab wann der Stundenplan gültig sein soll
	 * @param gueltigBis das Datum, bis zu dem der Stundenplan gültig sein soll

	 *
	 * @return <code>true</code> wenn es eine Überschneidung gibt, ansonsten <code>false</code>
	 */
	public boolean istKonfliktfreiZuAktivenStundenplaenen(final String gueltigAb, final String gueltigBis) {
		for (final StundenplanListeEintrag sp : aktive())
			if ((!hasDaten() || auswahl().id != sp.id)
					&& (DateUtils.berechneGemeinsameTage((gueltigAb != null ? gueltigAb : auswahl().gueltigAb), (gueltigBis != null ? gueltigBis : auswahl().gueltigBis), sp.gueltigAb, sp.gueltigBis).length > 0))
				return false;
		return true;
	}

	/**
	 * Gibt den auf die Gültigkeit bezogen letzten Stundenplan in der Liste zurück.
	 *
	 * @return der letzte Stundenplan in der Liste oder <code>null</code>, falls die Liste leer ist.
	 */
	public StundenplanListeEintrag getLastAktivStundenplan() {
		return aktive().isEmpty() ? null : aktive().getLast();
	}

	/**
	 * Erstellt eine Stundenplan-Vorlage und fügt sie der Auswahl-Liste hinzu.
	 */
	private void addStundenplanVorlage() {
		_stundenplanVorlage = new StundenplanListeEintrag();
		_stundenplanVorlage.id = -1;
		_stundenplanVorlage.bezeichnung = "Allgemeine Vorlagen";
	}

	/**
	 * Gibt den Vorlagen-Stundenplan zurück.
	 *
	 * @return den Vorlagen-Stundenplan.
	 * @throws DeveloperNotificationException wenn kein Vorlagen-Stundenplan gesetzt ist.
	 */
	public @NotNull StundenplanListeEintrag getStundenplanVorlage() {
		if (_stundenplanVorlage == null)
			throw new DeveloperNotificationException("Kein Vorlagen-Stundenplan gesetzt.");
		return _stundenplanVorlage;
	}

	/**
	 * Überprüft, ob es sich beim ausgewählten Stundenplan um die Vorlage handelt.
	 *
	 * @return <code>true</code> wenn es sich um die Vorlage handelt, ansonsten <code>false</code>
	 */
	public boolean auswahlIsVorlage() {
		return hasDaten() && auswahl() == _stundenplanVorlage;
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur aktive Stundenpläne zurück.
	 *
	 * @return true, wenn nur aktive Stundenpläne angezeigt werden und ansonsten false
	 */
	public boolean filterNurAktiv() {
		return this._filterNurAktiv;
	}


	/**
	 * Setzt die Filtereinstellung auf nur aktive Stundenpläne.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public void setFilterNurAktiv(final boolean value) {
		this._filterNurAktiv = value;
		this._eventHandlerFilterChanged.run();
	}

	@Override
	protected boolean onSetDaten(final @NotNull StundenplanListeEintrag eintrag, final @NotNull StundenplanManager daten) {
		return true;
	}

}
