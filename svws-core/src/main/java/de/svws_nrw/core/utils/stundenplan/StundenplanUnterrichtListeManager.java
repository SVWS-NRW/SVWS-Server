package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.stundenplan.StundenplanSchueler;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Unterrichte-Listen.
 */
public final class StundenplanUnterrichtListeManager extends AuswahlManager<@NotNull Long, @NotNull StundenplanUnterricht, @NotNull StundenplanUnterricht> {

	/** Der StundenplanManager */
	private final @NotNull StundenplanManager stundenplanManager;

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<@NotNull StundenplanUnterricht, @NotNull Long> _unterrichtToId = (final @NotNull StundenplanUnterricht s) -> s.id;

	/** Das Filter-Attribut für die Lehrer */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull StundenplanLehrer> lehrer;
	private static final @NotNull Function<@NotNull StundenplanLehrer, @NotNull Long> _lehrerToId = (final @NotNull StundenplanLehrer l) -> l.id;

	/** Das Filter-Attribut für die Schüler */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull StundenplanSchueler> schueler;
	private static final @NotNull Function<@NotNull StundenplanSchueler, @NotNull Long> _schuelerToId = (final @NotNull StundenplanSchueler s) -> s.id;

	/** Das Filter-Attribut für die Klassen */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull StundenplanKlasse> klassen;
	private static final @NotNull Function<@NotNull StundenplanKlasse, @NotNull Long> _klasseToId = (final @NotNull StundenplanKlasse k) -> k.id;

	/** Das Filter-Attribut für die Kurse */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull StundenplanKurs> kurse;
	private static final @NotNull Function<@NotNull StundenplanKurs, @NotNull Long> _kursToId = (final @NotNull StundenplanKurs k) -> k.id;

	/** Das Filter-Attribut für die Räume */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull StundenplanRaum> raeume;
	private static final @NotNull Function<@NotNull StundenplanRaum, @NotNull Long> _raumToId = (final @NotNull StundenplanRaum r) -> r.id;

	/** Das Filter-Attribut für die Schienen */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull StundenplanSchiene> schienen;
	private static final @NotNull Function<@NotNull StundenplanSchiene, @NotNull Long> _schieneToId = (final @NotNull StundenplanSchiene s) -> s.id;

	/** Das Filter-Attribut für die Fächer */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull StundenplanFach> faecher;
	private static final @NotNull Function<@NotNull StundenplanFach, @NotNull Long> _fachToId = (final @NotNull StundenplanFach f) -> f.id;

	/** Das Filter-Attribut für die Wochentage */
	public final @NotNull AttributMitAuswahl<@NotNull Integer, @NotNull Wochentag> wochentage;
	private static final @NotNull Function<@NotNull Wochentag, @NotNull Integer> _wochentagToId = (final @NotNull Wochentag w) -> w.id;

	/** Das Filter-Attribut für die Stunden */
	public final @NotNull AttributMitAuswahl<@NotNull Integer, @NotNull Integer> stunden;
	private static final @NotNull Function<@NotNull Integer, @NotNull Integer> _stundeToStunde = (final @NotNull Integer s) -> s;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform                    die Schulform der Schule
	 * @param stundenplanManager           der StundenplanManager
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in dem sich die Schule aktuell befindet
	 */
	public StundenplanUnterrichtListeManager(final Schulform schulform, final @NotNull StundenplanManager stundenplanManager,
			final @NotNull List<@NotNull Schuljahresabschnitt> schuljahresabschnitte,
			final long schuljahresabschnittSchule) {
		super(stundenplanManager.getIDSchuljahresabschnitt(), schuljahresabschnittSchule, schuljahresabschnitte, schulform, stundenplanManager.unterrichtGetMengeAsList(), StundenplanUnterrichtUtils.comparator,
				_unterrichtToId, _unterrichtToId, Arrays.asList());
		this.stundenplanManager = stundenplanManager;
		this.klassen = new AttributMitAuswahl<>(stundenplanManager.klasseGetMengeAsList(), _klasseToId, StundenplanUnterrichtUtils.comparatorKlassen, _eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl<>(stundenplanManager.lehrerGetMengeAsList(), _lehrerToId, StundenplanUnterrichtUtils.comparatorLehrer, _eventHandlerFilterChanged);
		this.schueler = new AttributMitAuswahl<>(stundenplanManager.schuelerGetMengeAsList(), _schuelerToId, StundenplanUnterrichtUtils.comparatorSchueler, _eventHandlerFilterChanged);
		this.faecher = new AttributMitAuswahl<>(stundenplanManager.fachGetMengeAsList(), _fachToId, StundenplanUnterrichtUtils.comparatorFaecher, _eventHandlerFilterChanged);
		this.kurse = new AttributMitAuswahl<>(stundenplanManager.kursGetMengeAsList(), _kursToId, StundenplanUnterrichtUtils.comparatorKurse, _eventHandlerFilterChanged);
		this.wochentage = new AttributMitAuswahl<>(Arrays.asList(stundenplanManager.zeitrasterGetWochentageAlsEnumRange()), _wochentagToId, StundenplanUnterrichtUtils.comparatorWochentag, _eventHandlerFilterChanged);
		final @NotNull List<@NotNull Integer> tmpStunden = new ArrayList<>();
		for (final int s : stundenplanManager.zeitrasterGetStundenRange())
			tmpStunden.add(s);
		this.stunden = new AttributMitAuswahl<>(tmpStunden, _stundeToStunde, StundenplanUnterrichtUtils.comparatorStunden, _eventHandlerFilterChanged);
		this.raeume = new AttributMitAuswahl<>(stundenplanManager.raumGetMengeAsList(), _raumToId, StundenplanUnterrichtUtils.comparatorRaeume, _eventHandlerFilterChanged);
		this.schienen = new AttributMitAuswahl<>(stundenplanManager.schieneGetMengeAsList(), _schieneToId, StundenplanUnterrichtUtils.comparatorSchienen, _eventHandlerFilterChanged);
	}

	/**
	 * Vergleicht zwei Fächerlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull StundenplanUnterricht a, final @NotNull StundenplanUnterricht b) {
		return StundenplanUnterrichtUtils.comparator.compare(a, b);
	}


	/**
	 * Aktualisiert die einzelnen Filter-Attribute des Managers mithilfe des Stundenplan-Managers in Bezug auf
	 * die in Unterichten verwendeten Attributwerte. Diese Methode sollten bei allen Änderungen an der Menge der
	 * Unterrichte aufgerufen werden.
	 */
	public void updateAttributAuswahl() {
		// TODO
	}


	protected void onFilterChanged() {
		// TODO Diese Methode kann genutzt werden, um auf Querbezüge zwischen den Filtern zu reagieren und die Attributemengen ggf. anzupassen
	}


	private static <@NotNull T> boolean checkContains(final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull T> attr, final @NotNull List<@NotNull Long> list) {
		if (!attr.auswahlExists())
			return true;
		for (final long id : list)
			if (attr.auswahlHasKey(id))
				return true;
		return false;
	}


	@Override
	protected boolean checkFilter(final @NotNull StundenplanUnterricht eintrag) {
		if (this.faecher.auswahlExists() && (!this.faecher.auswahlHasKey(eintrag.idFach)))
			return false;
		if (this.kurse.auswahlExists() && ((eintrag.idKurs == null) || (!this.kurse.auswahlHasKey(eintrag.idKurs))))
			return false;
		final @NotNull StundenplanZeitraster zeitraster = stundenplanManager.zeitrasterGetByIdOrException(eintrag.idZeitraster);
		if (this.wochentage.auswahlExists() && (!this.wochentage.auswahlHasKey(zeitraster.wochentag)))
			return false;
		if (this.stunden.auswahlExists() && (!this.stunden.auswahlHasKey(zeitraster.unterrichtstunde)))
			return false;
		if (!checkContains(this.klassen, eintrag.klassen))
			return false;
		if (!checkContains(this.lehrer, eintrag.lehrer))
			return false;
		if (!checkContains(this.raeume, eintrag.raeume))
			return false;
		if (!checkContains(this.schienen, eintrag.schienen))
			return false;
		// final @NotNull List<@NotNull StundenplanSchueler> listeSchueler = stundenplanManager.schuelerGetMengeByUnterrichtIdAsList(eintrag.id);
		// if (!checkContains(this.schueler, listeSchueler))
		// 	return false;
		return true;
	}

}
