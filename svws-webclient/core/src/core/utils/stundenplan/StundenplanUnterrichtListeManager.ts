import { AttributMitAuswahl } from '../../../core/utils/AttributMitAuswahl';
import { StundenplanKlasse } from '../../../core/data/stundenplan/StundenplanKlasse';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanManager } from '../../../core/utils/stundenplan/StundenplanManager';
import { StundenplanKurs } from '../../../core/data/stundenplan/StundenplanKurs';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanUnterrichtUtils } from '../../../core/utils/stundenplan/StundenplanUnterrichtUtils';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { StundenplanSchueler } from '../../../core/data/stundenplan/StundenplanSchueler';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { StundenplanLehrer } from '../../../core/data/stundenplan/StundenplanLehrer';
import { StundenplanSchiene } from '../../../core/data/stundenplan/StundenplanSchiene';
import { StundenplanFach } from '../../../core/data/stundenplan/StundenplanFach';
import { StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import type { List } from '../../../java/util/List';
import { Wochentag } from '../../../core/types/Wochentag';
import { Arrays } from '../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';

export class StundenplanUnterrichtListeManager extends AuswahlManager<number, StundenplanUnterricht, StundenplanUnterricht> {

	/**
	 * Der StundenplanManager
	 */
	private readonly stundenplanManager : StundenplanManager;

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _unterrichtToId : JavaFunction<StundenplanUnterricht, number> = { apply : (s: StundenplanUnterricht) => s.id };

	/**
	 * Das Filter-Attribut für die Lehrer
	 */
	public readonly lehrer : AttributMitAuswahl<number, StundenplanLehrer>;

	private static readonly _lehrerToId : JavaFunction<StundenplanLehrer, number> = { apply : (l: StundenplanLehrer) => l.id };

	/**
	 * Das Filter-Attribut für die Schüler
	 */
	public readonly schueler : AttributMitAuswahl<number, StundenplanSchueler>;

	private static readonly _schuelerToId : JavaFunction<StundenplanSchueler, number> = { apply : (s: StundenplanSchueler) => s.id };

	/**
	 * Das Filter-Attribut für die Klassen
	 */
	public readonly klassen : AttributMitAuswahl<number, StundenplanKlasse>;

	private static readonly _klasseToId : JavaFunction<StundenplanKlasse, number> = { apply : (k: StundenplanKlasse) => k.id };

	/**
	 * Das Filter-Attribut für die Kurse
	 */
	public readonly kurse : AttributMitAuswahl<number, StundenplanKurs>;

	private static readonly _kursToId : JavaFunction<StundenplanKurs, number> = { apply : (k: StundenplanKurs) => k.id };

	/**
	 * Das Filter-Attribut für die Räume
	 */
	public readonly raeume : AttributMitAuswahl<number, StundenplanRaum>;

	private static readonly _raumToId : JavaFunction<StundenplanRaum, number> = { apply : (r: StundenplanRaum) => r.id };

	/**
	 * Das Filter-Attribut für die Schienen
	 */
	public readonly schienen : AttributMitAuswahl<number, StundenplanSchiene>;

	private static readonly _schieneToId : JavaFunction<StundenplanSchiene, number> = { apply : (s: StundenplanSchiene) => s.id };

	/**
	 * Das Filter-Attribut für die Fächer
	 */
	public readonly faecher : AttributMitAuswahl<number, StundenplanFach>;

	private static readonly _fachToId : JavaFunction<StundenplanFach, number> = { apply : (f: StundenplanFach) => f.id };

	/**
	 * Das Filter-Attribut für die Wochentage
	 */
	public readonly wochentage : AttributMitAuswahl<number, Wochentag>;

	private static readonly _wochentagToId : JavaFunction<Wochentag, number> = { apply : (w: Wochentag) => w.id };

	/**
	 * Das Filter-Attribut für die Stunden
	 */
	public readonly stunden : AttributMitAuswahl<number, number>;

	private static readonly _stundeToStunde : JavaFunction<number, number> = { apply : (s: number) => s };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform                    die Schulform der Schule
	 * @param stundenplanManager           der StundenplanManager
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in dem sich die Schule aktuell befindet
	 */
	public constructor(schulform : Schulform | null, stundenplanManager : StundenplanManager, schuljahresabschnitte : List<Schuljahresabschnitt>, schuljahresabschnittSchule : number) {
		super(stundenplanManager.getIDSchuljahresabschnitt(), schuljahresabschnittSchule, schuljahresabschnitte, schulform, stundenplanManager.unterrichtGetMengeAsList(), StundenplanUnterrichtUtils.comparator, StundenplanUnterrichtListeManager._unterrichtToId, StundenplanUnterrichtListeManager._unterrichtToId, Arrays.asList());
		this.stundenplanManager = stundenplanManager;
		this.klassen = new AttributMitAuswahl(stundenplanManager.klasseGetMengeAsList(), StundenplanUnterrichtListeManager._klasseToId, StundenplanUnterrichtUtils.comparatorKlassen, this._eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl(stundenplanManager.lehrerGetMengeAsList(), StundenplanUnterrichtListeManager._lehrerToId, StundenplanUnterrichtUtils.comparatorLehrer, this._eventHandlerFilterChanged);
		this.schueler = new AttributMitAuswahl(stundenplanManager.schuelerGetMengeAsList(), StundenplanUnterrichtListeManager._schuelerToId, StundenplanUnterrichtUtils.comparatorSchueler, this._eventHandlerFilterChanged);
		this.faecher = new AttributMitAuswahl(stundenplanManager.fachGetMengeAsList(), StundenplanUnterrichtListeManager._fachToId, StundenplanUnterrichtUtils.comparatorFaecher, this._eventHandlerFilterChanged);
		this.kurse = new AttributMitAuswahl(stundenplanManager.kursGetMengeAsList(), StundenplanUnterrichtListeManager._kursToId, StundenplanUnterrichtUtils.comparatorKurse, this._eventHandlerFilterChanged);
		this.wochentage = new AttributMitAuswahl(Arrays.asList(...stundenplanManager.zeitrasterGetWochentageAlsEnumRange()), StundenplanUnterrichtListeManager._wochentagToId, StundenplanUnterrichtUtils.comparatorWochentag, this._eventHandlerFilterChanged);
		const tmpStunden : List<number> = new ArrayList<number>();
		for (const s of stundenplanManager.zeitrasterGetStundenRange())
			tmpStunden.add(s);
		this.stunden = new AttributMitAuswahl(tmpStunden, StundenplanUnterrichtListeManager._stundeToStunde, StundenplanUnterrichtUtils.comparatorStunden, this._eventHandlerFilterChanged);
		this.raeume = new AttributMitAuswahl(stundenplanManager.raumGetMengeAsList(), StundenplanUnterrichtListeManager._raumToId, StundenplanUnterrichtUtils.comparatorRaeume, this._eventHandlerFilterChanged);
		this.schienen = new AttributMitAuswahl(stundenplanManager.schieneGetMengeAsList(), StundenplanUnterrichtListeManager._schieneToId, StundenplanUnterrichtUtils.comparatorSchienen, this._eventHandlerFilterChanged);
	}

	/**
	 * Vergleicht zwei Fächerlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : StundenplanUnterricht, b : StundenplanUnterricht) : number {
		return StundenplanUnterrichtUtils.comparator.compare(a, b);
	}

	/**
	 * Aktualisiert die einzelnen Filter-Attribute des Managers mithilfe des Stundenplan-Managers in Bezug auf
	 * die in Unterichten verwendeten Attributwerte. Diese Methode sollten bei allen Änderungen an der Menge der
	 * Unterrichte aufgerufen werden.
	 */
	public updateAttributAuswahl() : void {
		// empty block
	}

	protected onFilterChanged() : void {
		// empty block
	}

	private static checkContains<T>(attr : AttributMitAuswahl<number, T>, list : List<number>) : boolean {
		if (!attr.auswahlExists())
			return true;
		for (const id of list)
			if (attr.auswahlHasKey(id))
				return true;
		return false;
	}

	protected checkFilter(eintrag : StundenplanUnterricht) : boolean {
		if (this.faecher.auswahlExists() && (!this.faecher.auswahlHasKey(eintrag.idFach)))
			return false;
		if (this.kurse.auswahlExists() && ((eintrag.idKurs === null) || (!this.kurse.auswahlHasKey(eintrag.idKurs))))
			return false;
		const zeitraster : StundenplanZeitraster = this.stundenplanManager.zeitrasterGetByIdOrException(eintrag.idZeitraster);
		if (this.wochentage.auswahlExists() && (!this.wochentage.auswahlHasKey(zeitraster.wochentag)))
			return false;
		if (this.stunden.auswahlExists() && (!this.stunden.auswahlHasKey(zeitraster.unterrichtstunde)))
			return false;
		if (!StundenplanUnterrichtListeManager.checkContains(this.klassen, eintrag.klassen))
			return false;
		if (!StundenplanUnterrichtListeManager.checkContains(this.lehrer, eintrag.lehrer))
			return false;
		if (!StundenplanUnterrichtListeManager.checkContains(this.raeume, eintrag.raeume))
			return false;
		if (!StundenplanUnterrichtListeManager.checkContains(this.schienen, eintrag.schienen))
			return false;
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplan.StundenplanUnterrichtListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.stundenplan.StundenplanUnterrichtListeManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanUnterrichtListeManager(obj : unknown) : StundenplanUnterrichtListeManager {
	return obj as StundenplanUnterrichtListeManager;
}
