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
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Unterrichte-Listen.
 */
public final class StundenplanUnterrichtListeManager extends AuswahlManager<Long, StundenplanUnterricht, StundenplanUnterricht> {

	/** Der StundenplanManager */
	private final @NotNull StundenplanManager stundenplanManager;

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<StundenplanUnterricht, Long> _unterrichtToId = (final @NotNull StundenplanUnterricht s) -> s.id;

	/** Das Filter-Attribut für die Lehrer */
	public final @NotNull AttributMitAuswahl<Long, StundenplanLehrer> lehrer;
	private static final @NotNull Function<StundenplanLehrer, Long> _lehrerToId = (final @NotNull StundenplanLehrer l) -> l.id;

	/** Das Filter-Attribut für die Schüler */
	public final @NotNull AttributMitAuswahl<Long, StundenplanSchueler> schueler;
	private static final @NotNull Function<StundenplanSchueler, Long> _schuelerToId = (final @NotNull StundenplanSchueler s) -> s.id;

	/** Das Filter-Attribut für die Klassen */
	public final @NotNull AttributMitAuswahl<Long, StundenplanKlasse> klassen;
	private static final @NotNull Function<StundenplanKlasse, Long> _klasseToId = (final @NotNull StundenplanKlasse k) -> k.id;

	/** Das Filter-Attribut für die Kurse */
	public final @NotNull AttributMitAuswahl<Long, StundenplanKurs> kurse;
	private static final @NotNull Function<StundenplanKurs, Long> _kursToId = (final @NotNull StundenplanKurs k) -> k.id;

	/** Das Filter-Attribut für die Räume */
	public final @NotNull AttributMitAuswahl<Long, StundenplanRaum> raeume;
	private static final @NotNull Function<StundenplanRaum, Long> _raumToId = (final @NotNull StundenplanRaum r) -> r.id;

	/** Das Filter-Attribut für die Schienen */
	public final @NotNull AttributMitAuswahl<Long, StundenplanSchiene> schienen;
	private static final @NotNull Function<StundenplanSchiene, Long> _schieneToId = (final @NotNull StundenplanSchiene s) -> s.id;

	/** Das Filter-Attribut für die Fächer */
	public final @NotNull AttributMitAuswahl<Long, StundenplanFach> faecher;
	private static final @NotNull Function<StundenplanFach, Long> _fachToId = (final @NotNull StundenplanFach f) -> f.id;

	/** Das Filter-Attribut für die Wochentage */
	public final @NotNull AttributMitAuswahl<Integer, Wochentag> wochentage;
	private static final @NotNull Function<Wochentag, Integer> _wochentagToId = (final @NotNull Wochentag w) -> w.id;

	/** Das Filter-Attribut für die Zeitraster */
	public final @NotNull AttributMitAuswahl<Long, StundenplanZeitraster> zeitraster;
	private static final @NotNull Function<StundenplanZeitraster, Long> _zeitrasterToId = (final @NotNull StundenplanZeitraster z) -> z.id;

	/** Das Filter-Attribut für die Stunden */
	public final @NotNull AttributMitAuswahl<Integer, Integer> stunden;
	private static final @NotNull Function<Integer, Integer> _stundeToStunde = (final @NotNull Integer s) -> s;

	/** Das Filter-Attribut für die Wochentypen */
	public final @NotNull AttributMitAuswahl<Integer, Integer> wochentypen;
	private static final @NotNull Function<Integer, Integer> _wochentypToWochentyp = (final @NotNull Integer w) -> w;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform                    die Schulform der Schule
	 * @param stundenplanManager           der StundenplanManager
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in dem sich die Schule aktuell befindet
	 */
	public StundenplanUnterrichtListeManager(final Schulform schulform, final @NotNull StundenplanManager stundenplanManager,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte,
			final long schuljahresabschnittSchule) {
		super(stundenplanManager.getIDSchuljahresabschnitt(), schuljahresabschnittSchule, schuljahresabschnitte, schulform,
				stundenplanManager.unterrichtGetMengeAsList(), StundenplanUnterrichtUtils.comparator,
				_unterrichtToId, _unterrichtToId, Arrays.asList());
		this.stundenplanManager = stundenplanManager;
		this.klassen = new AttributMitAuswahl<>(stundenplanManager.klasseGetMengeVerwendetAsList(), _klasseToId, StundenplanUnterrichtUtils.comparatorKlassen,
				_eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl<>(stundenplanManager.lehrerGetMengeVerwendetAsList(), _lehrerToId, StundenplanUnterrichtUtils.comparatorLehrer,
				_eventHandlerFilterChanged);
		this.schueler = new AttributMitAuswahl<>(stundenplanManager.schuelerGetMengeAsList(), _schuelerToId, StundenplanUnterrichtUtils.comparatorSchueler,
				_eventHandlerFilterChanged);
		this.faecher = new AttributMitAuswahl<>(stundenplanManager.fachGetMengeVerwendetAsList(), _fachToId, StundenplanUnterrichtUtils.comparatorFaecher,
				_eventHandlerFilterChanged);
		this.kurse = new AttributMitAuswahl<>(stundenplanManager.kursGetMengeVerwendetAsList(), _kursToId, StundenplanUnterrichtUtils.comparatorKurse,
				_eventHandlerFilterChanged);
		this.wochentage = new AttributMitAuswahl<>(Arrays.asList(stundenplanManager.zeitrasterGetWochentageAlsEnumRange()), _wochentagToId,
				StundenplanUnterrichtUtils.comparatorWochentage, _eventHandlerFilterChanged);
		final @NotNull List<Integer> tmpStunden = new ArrayList<>();
		for (final int s : stundenplanManager.zeitrasterGetStundenRange())
			tmpStunden.add(s);
		this.stunden = new AttributMitAuswahl<>(tmpStunden, _stundeToStunde, StundenplanUnterrichtUtils.comparatorStunden, _eventHandlerFilterChanged);
		final @NotNull List<Integer> tmpWochentypen = new ArrayList<>();
		for (int w = 0; w <= stundenplanManager.getWochenTypModell(); w++)
			tmpWochentypen.add(w);
		this.wochentypen =
				new AttributMitAuswahl<>(tmpWochentypen, _wochentypToWochentyp, StundenplanUnterrichtUtils.comparatorWochentypen, _eventHandlerFilterChanged);
		this.raeume = new AttributMitAuswahl<>(stundenplanManager.raumGetMengeVerwendetAsList(), _raumToId, StundenplanUnterrichtUtils.comparatorRaeume,
				_eventHandlerFilterChanged);
		this.schienen = new AttributMitAuswahl<>(stundenplanManager.schieneGetMengeVerwendetAsList(), _schieneToId, StundenplanUnterrichtUtils.comparatorSchienen,
				_eventHandlerFilterChanged);
		this.zeitraster = new AttributMitAuswahl<>(stundenplanManager.getListZeitraster(), _zeitrasterToId, StundenplanUnterrichtUtils.comparatorZeitraster,
				_eventHandlerFilterChanged);
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
	 * die in Unterrichten verwendeten Attributwerte. Diese Methode sollten bei allen Änderungen an der Menge der
	 * Unterrichte aufgerufen werden.
	 */
	public void updateAttributAuswahl() {
		// TODO
	}


	@Override
	protected void onFilterChanged() {
		// TODO Diese Methode kann genutzt werden, um auf Querbezüge zwischen den Filtern zu reagieren und die Attributemengen ggf. anzupassen
	}


	private static <T> boolean checkContains(final @NotNull AttributMitAuswahl<Long, T> attr, final @NotNull List<Long> list) {
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
		if (this.zeitraster.auswahlExists() && (!this.zeitraster.auswahlHasKey(eintrag.idZeitraster)))
			return false;
		if (this.wochentypen.auswahlExists() && (!this.wochentypen.auswahlHasKey(eintrag.wochentyp)))
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
		final @NotNull List<StundenplanSchueler> listeSchueler = stundenplanManager.schuelerGetMengeByUnterrichtIdAsList(eintrag.id);
		final @NotNull List<Long> listIdSchueler = new ArrayList<>();
		for (final StundenplanSchueler s : listeSchueler)
			listIdSchueler.add(s.id);
		if (!checkContains(this.schueler, listIdSchueler))
			return false;
		return true;
	}

}
