import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { StundenplanFach } from "@core/core/data/stundenplan/StundenplanFach";
import type { StundenplanKlasse } from "@core/core/data/stundenplan/StundenplanKlasse";
import type { StundenplanKurs } from "@core/core/data/stundenplan/StundenplanKurs";
import type { StundenplanLehrer } from "@core/core/data/stundenplan/StundenplanLehrer";
import type { StundenplanRaum } from "@core/core/data/stundenplan/StundenplanRaum";
import type { StundenplanSchiene } from "@core/core/data/stundenplan/StundenplanSchiene";
import type { StundenplanSchueler } from "@core/core/data/stundenplan/StundenplanSchueler";
import type { StundenplanUnterricht } from "@core/core/data/stundenplan/StundenplanUnterricht";
import type { StundenplanZeitraster } from "@core/core/data/stundenplan/StundenplanZeitraster";
import type { Wochentag } from "@core/core/types/Wochentag";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import { StundenplanUnterrichtUtils } from "@core/core/utils/stundenplan/StundenplanUnterrichtUtils";
import { Class } from "@core/java/lang/Class";
import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";
import { ListeMitAuswahl } from "../ListeMitAuswahl";

export class StundenplanUnterrichtListeManager extends AuswahlManager<number, StundenplanUnterricht, StundenplanUnterricht> {

	/**
	 * Der StundenplanManager
	 */
	private readonly stundenplanManager: StundenplanManager;

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _unterrichtToId = (s: StundenplanUnterricht) => s.id;

	/**
	 * Das Filter-Attribut für die Lehrer
	 */
	public readonly lehrer: ListeMitAuswahl<number, StundenplanLehrer>;

	private static readonly _lehrerToId = (l: StundenplanLehrer) => l.id;

	/**
	 * Das Filter-Attribut für die Schüler
	 */
	public readonly schueler: ListeMitAuswahl<number, StundenplanSchueler>;

	private static readonly _schuelerToId = (s: StundenplanSchueler) => s.id;

	/**
	 * Das Filter-Attribut für die Klassen
	 */
	public readonly klassen: ListeMitAuswahl<number, StundenplanKlasse>;

	private static readonly _klasseToId = (k: StundenplanKlasse) => k.id;

	/**
	 * Das Filter-Attribut für die Kurse
	 */
	public readonly kurse: ListeMitAuswahl<number, StundenplanKurs>;

	private static readonly _kursToId = (k: StundenplanKurs) => k.id;

	/**
	 * Das Filter-Attribut für die Räume
	 */
	public readonly raeume: ListeMitAuswahl<number, StundenplanRaum>;

	private static readonly _raumToId = (r: StundenplanRaum) => r.id;

	/**
	 * Das Filter-Attribut für die Schienen
	 */
	public readonly schienen: ListeMitAuswahl<number, StundenplanSchiene>;

	private static readonly _schieneToId = (s: StundenplanSchiene) => s.id;

	/**
	 * Das Filter-Attribut für die Fächer
	 */
	public readonly faecher: ListeMitAuswahl<number, StundenplanFach>;

	private static readonly _fachToId = (f: StundenplanFach) => f.id;

	/**
	 * Das Filter-Attribut für die Wochentage
	 */
	public readonly wochentage: ListeMitAuswahl<number, Wochentag>;

	private static readonly _wochentagToId = (w: Wochentag) => w.id;

	/**
	 * Das Filter-Attribut für die Zeitraster
	 */
	public readonly zeitraster: ListeMitAuswahl<number, StundenplanZeitraster>;

	private static readonly _zeitrasterToId = (z: StundenplanZeitraster) => z.id;

	/**
	 * Das Filter-Attribut für die Stunden
	 */
	public readonly stunden: ListeMitAuswahl<number, number>;

	private static readonly _stundeToStunde = (s: number) => s;

	/**
	 * Das Filter-Attribut für die Wochentypen
	 */
	public readonly wochentypen: ListeMitAuswahl<number, number>;

	private static readonly _wochentypToWochentyp = (w: number) => w;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform                    die Schulform der Schule
	 * @param stundenplanManager           der StundenplanManager
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in dem sich die Schule aktuell befindet
	 */
	public constructor(schulform: Schulform | null, stundenplanManager: StundenplanManager, schuljahresabschnitte: List<Schuljahresabschnitt>, schuljahresabschnittSchule: number) {
		super(stundenplanManager.getIDSchuljahresabschnitt(), schuljahresabschnittSchule, schuljahresabschnitte, schulform, stundenplanManager.unterrichtGetMengeAsList(), StundenplanUnterrichtUtils.comparator, StundenplanUnterrichtListeManager._unterrichtToId, StundenplanUnterrichtListeManager._unterrichtToId, []);
		this.stundenplanManager = stundenplanManager;
		this.klassen = new ListeMitAuswahl(stundenplanManager.klasseGetMengeVerwendetAsList(), StundenplanUnterrichtListeManager._klasseToId, StundenplanUnterrichtUtils.comparatorKlassen, this._eventHandlerFilterChanged);
		this.lehrer = new ListeMitAuswahl(stundenplanManager.lehrerGetMengeVerwendetAsList(), StundenplanUnterrichtListeManager._lehrerToId, StundenplanUnterrichtUtils.comparatorLehrer, this._eventHandlerFilterChanged);
		this.schueler = new ListeMitAuswahl(stundenplanManager.schuelerGetMengeAsList(), StundenplanUnterrichtListeManager._schuelerToId, StundenplanUnterrichtUtils.comparatorSchueler, this._eventHandlerFilterChanged);
		this.faecher = new ListeMitAuswahl(stundenplanManager.fachGetMengeVerwendetAsList(), StundenplanUnterrichtListeManager._fachToId, StundenplanUnterrichtUtils.comparatorFaecher, this._eventHandlerFilterChanged);
		this.kurse = new ListeMitAuswahl(stundenplanManager.kursGetMengeVerwendetAsList(), StundenplanUnterrichtListeManager._kursToId, StundenplanUnterrichtUtils.comparatorKurse, this._eventHandlerFilterChanged);
		this.wochentage = new ListeMitAuswahl(Arrays.asList(...stundenplanManager.zeitrasterGetWochentageAlsEnumRange()), StundenplanUnterrichtListeManager._wochentagToId, StundenplanUnterrichtUtils.comparatorWochentage, this._eventHandlerFilterChanged);
		const tmpStunden: List<number> = new ArrayList<number>();
		for (const s of stundenplanManager.zeitrasterGetStundenRange()) {
			tmpStunden.add(s);
		}
		this.stunden = new ListeMitAuswahl(tmpStunden, StundenplanUnterrichtListeManager._stundeToStunde, StundenplanUnterrichtUtils.comparatorStunden, this._eventHandlerFilterChanged);
		const tmpWochentypen: List<number> = new ArrayList<number>();
		for (let w: number = 0; w <= stundenplanManager.getWochenTypModell(); w++) {
			tmpWochentypen.add(w);
		}
		this.wochentypen = new ListeMitAuswahl(tmpWochentypen, StundenplanUnterrichtListeManager._wochentypToWochentyp, StundenplanUnterrichtUtils.comparatorWochentypen, this._eventHandlerFilterChanged);
		this.raeume = new ListeMitAuswahl(stundenplanManager.raumGetMengeVerwendetAsList(), StundenplanUnterrichtListeManager._raumToId, StundenplanUnterrichtUtils.comparatorRaeume, this._eventHandlerFilterChanged);
		this.schienen = new ListeMitAuswahl(stundenplanManager.schieneGetMengeVerwendetAsList(), StundenplanUnterrichtListeManager._schieneToId, StundenplanUnterrichtUtils.comparatorSchienen, this._eventHandlerFilterChanged);
		this.zeitraster = new ListeMitAuswahl(stundenplanManager.getListZeitraster(), StundenplanUnterrichtListeManager._zeitrasterToId, StundenplanUnterrichtUtils.comparatorZeitraster, this._eventHandlerFilterChanged);
	}

	/**
	 * Vergleicht zwei Fächerlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: StundenplanUnterricht, b: StundenplanUnterricht): number {
		return StundenplanUnterrichtUtils.comparator.compare(a, b);
	}

	/**
	 * Aktualisiert die einzelnen Filter-Attribute des Managers mithilfe des Stundenplan-Managers in Bezug auf
	 * die in Unterrichten verwendeten Attributwerte. Diese Methode sollten bei allen Änderungen an der Menge der
	 * Unterrichte aufgerufen werden.
	 */
	public updateAttributAuswahl(): void {
		// empty block
	}

	protected onFilterChanged(): void {
		// empty block
	}

	private static checkContains<T>(attr: ListeMitAuswahl<number, T>, list: List<number>): boolean {
		if (!attr.auswahlExists()) {
			return true;
		}
		for (const id of list) {
			if (attr.auswahlHasKey(id)) {
				return true;
			}
		}
		return false;
	}

	protected checkFilter(eintrag: StundenplanUnterricht): boolean {
		if (this.faecher.auswahlExists() && (!this.faecher.auswahlHasKey(eintrag.idFach))) {
			return false;
		}
		if (this.kurse.auswahlExists() && ((eintrag.idKurs === null) || (!this.kurse.auswahlHasKey(eintrag.idKurs)))) {
			return false;
		}
		if (this.zeitraster.auswahlExists() && (!this.zeitraster.auswahlHasKey(eintrag.idZeitraster))) {
			return false;
		}
		if (this.wochentypen.auswahlExists() && (!this.wochentypen.auswahlHasKey(eintrag.wochentyp))) {
			return false;
		}
		const zeitraster: StundenplanZeitraster = this.stundenplanManager.zeitrasterGetByIdOrException(eintrag.idZeitraster);
		if (this.wochentage.auswahlExists() && (!this.wochentage.auswahlHasKey(zeitraster.wochentag))) {
			return false;
		}
		if (this.stunden.auswahlExists() && (!this.stunden.auswahlHasKey(zeitraster.unterrichtstunde))) {
			return false;
		}
		if (!StundenplanUnterrichtListeManager.checkContains(this.klassen, eintrag.klassen)) {
			return false;
		}
		if (!StundenplanUnterrichtListeManager.checkContains(this.lehrer, eintrag.lehrer)) {
			return false;
		}
		if (!StundenplanUnterrichtListeManager.checkContains(this.raeume, eintrag.raeume)) {
			return false;
		}
		if (!StundenplanUnterrichtListeManager.checkContains(this.schienen, eintrag.schienen)) {
			return false;
		}
		const listeSchueler: List<StundenplanSchueler> = this.stundenplanManager.schuelerGetMengeByUnterrichtIdAsList(eintrag.id);
		const listIdSchueler: List<number> = new ArrayList<number>();
		for (const s of listeSchueler) {
			listIdSchueler.add(s.id);
		}
		if (!StundenplanUnterrichtListeManager.checkContains(this.schueler, listIdSchueler)) {
			return false;
		}
		return true;
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link AuswahlManager}
	 *
	 * @param srcManager Manager, aus dem die Filterinformationen übernommen werden
	 */
	public useFilter(srcManager: StundenplanUnterrichtListeManager): void {
		this.faecher.setAuswahl(srcManager.faecher);
		this.klassen.setAuswahl(srcManager.klassen);
		this.kurse.setAuswahl(srcManager.kurse);
		this.lehrer.setAuswahl(srcManager.lehrer);
		this.raeume.setAuswahl(srcManager.raeume);
		this.schienen.setAuswahl(srcManager.schienen);
		this.schueler.setAuswahl(srcManager.schueler);
		this.stunden.setAuswahl(srcManager.stunden);
		this.wochentage.setAuswahl(srcManager.wochentage);
		this.wochentypen.setAuswahl(srcManager.wochentypen);
		this.zeitraster.setAuswahl(srcManager.zeitraster);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplan.StundenplanUnterrichtListeManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.stundenplan.StundenplanUnterrichtListeManager'].includes(name);
	}

	public static class = new Class<StundenplanUnterrichtListeManager>('de.svws_nrw.core.utils.stundenplan.StundenplanUnterrichtListeManager');

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanUnterrichtListeManager(obj: unknown): StundenplanUnterrichtListeManager {
	return obj as StundenplanUnterrichtListeManager;
}
