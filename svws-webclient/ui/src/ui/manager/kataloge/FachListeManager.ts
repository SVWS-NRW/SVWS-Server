import { JavaObject } from '../../../../../core/src/java/lang/JavaObject';
import { HashMap2D } from '../../../../../core/src/core/adt/map/HashMap2D';
import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import { HashMap } from '../../../../../core/src/java/util/HashMap';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../../../core/src/../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { FachDaten } from '../../../../../core/src/core/data/fach/FachDaten';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import { Class } from '../../../../../core/src/java/lang/Class';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';

export class FachListeManager extends AuswahlManager<number, FachDaten, FachDaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _fachToId: JavaFunction<FachDaten, number> = { apply: (f: FachDaten) => f.id };

	private static readonly _fachDatenToId: JavaFunction<FachDaten, number> = { apply: (f: FachDaten) => f.id };

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _mapFachIstSichtbar: HashMap2D<boolean, number, FachDaten> = new HashMap2D<boolean, number, FachDaten>();

	private readonly _mapFachByKuerzel: HashMap<string, FachDaten> = new HashMap<string, FachDaten>();

	/**
	 * Das Filter-Attribut auf nur sichtbare Fächer
	 */
	private _filterNurSichtbar: boolean = true;

	/**
	 * Sets mit Listen zur aktuellen Auswahl
	 */
	private readonly idsReferenzierterFaecher: HashSet<number> = new HashSet<number>();

	/**
	 * Ein Default-Comparator für den Vergleich von Fächern in Fächerlisten.
	 */
	public static readonly comparator: Comparator<FachDaten> = { compare: (a: FachDaten, b: FachDaten) => {
		let cmp: number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param faecher       die Liste der Fächer
	 */
	public constructor(schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, faecher: List<FachDaten>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, faecher, FachListeManager.comparator, FachListeManager._fachToId, FachListeManager._fachDatenToId, Arrays.asList());
		this.initFaecher();
	}

	private initFaecher(): void {
		for (const f of this.liste.list()) {
			this._mapFachIstSichtbar.put(f.istSichtbar, f.id, f);
			this._mapFachByKuerzel.put(f.kuerzel, f);
		}
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag: FachDaten, daten: FachDaten): boolean {
		let updateEintrag: boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung))) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Fächer zurück.
	 *
	 * @return true, wenn nur sichtbare Fächer angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Fächer.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value: boolean): void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	/**
	 * Vergleicht zwei Fächerlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: FachDaten, b: FachDaten): number {
		for (const criteria of this._order) {
			const field: string | null = criteria.a;
			const asc: boolean = (criteria.b === null) || criteria.b;
			let cmp: number = 0;
			if (JavaObject.equalsTranspiler("kuerzel", (field))) {
				cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			} else
				if (JavaObject.equalsTranspiler("bezeichnung", (field))) {
					cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return FachListeManager.comparator.compare(a, b);
	}

	protected onMehrfachauswahlChanged(): void {
		this.idsReferenzierterFaecher.clear();
		for (const f of this.liste.auswahl())
			if ((f.referenziertInAnderenTabellen !== null) && f.referenziertInAnderenTabellen)
				this.idsReferenzierterFaecher.add(f.id);
	}

	/**
	 *Gibt das Set mit den FächerIds zurück, die in der Auswahl sind und in anderen Datenbanktabellen referenziert werden
	 *
	 * @return Das Set mit IDs von Fächern, die in anderen Datenbanktabellen referenziert werden
	 */
	public getIdsReferenzierterFaecher(): JavaSet<number> {
		return this.idsReferenzierterFaecher;
	}

	protected checkFilter(eintrag: FachDaten): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;
		return true;
	}

	/**
	 * Gibt die FachDaten anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel  das Kürzel
	 *
	 * @return die FachDaten oder null
	 */
	public getByKuerzelOrNull(kuerzel: string): FachDaten | null {
		return this._mapFachByKuerzel.get(kuerzel);
	}

	/**
	 * Wenn das Kürzel nicht leer, bislang nicht verwendet und zwischen 1 und 20 Zeichen lang ist,
	 * wird <code>true</code>, andernfalls <code>false</code> zurückgegeben.
	 *
	 * @param kuerzel   das gegebene Kürzel
	 *
	 * @return <code>true</code> wenn das Kürzel gültig ist, ansonsten <code>false</code>
	 */
	public validateKuerzel(kuerzel: string | null): boolean {
		if ((kuerzel === null) || JavaString.isBlank(kuerzel) || (kuerzel.trim().length > 20))
			return false;
		for (const listeEintrag of this.liste.list()) {
			if (JavaObject.equalsTranspiler(listeEintrag.kuerzel, (kuerzel)))
				return false;
		}
		return true;
	}

	/**
	 * Wenn die Bezeichnung nicht leer, bislang nicht verwendet und zwischen 1 und 255 Zeichen lang ist,
	 * wird <code>true</code>, andernfalls <code>false</code> zurückgegeben.
	 *
	 * @param bezeichnung   die gegebene Bezeichnung
	 *
	 * @return <code>true</code> wenn die Bezeichnung gültig ist, ansonsten <code>false</code>
	 */
	public validateBezeichnung(bezeichnung: string | null): boolean {
		if ((bezeichnung === null) || JavaString.isBlank(bezeichnung) || (bezeichnung.trim().length > 255))
			return false;
		for (const listeEintrag of this.liste.list()) {
			if (JavaObject.equalsTranspiler(listeEintrag.bezeichnung, (bezeichnung)))
				return false;
		}
		return true;
	}

	/**
	 * Wenn die maxZeichenInFachbemerkungen leer oder einen positiven Wert hat,
	 * wird <code>true</code>, andernfalls <code>false</code> zurückgegeben.
	 *
	 * @param maxZeichenInFachbemerkungen   die gegebene maxZeichenInFachbemerkungen
	 *
	 * @return <code>true</code> wenn die maxZeichenInFachbemerkungen gültig sind, ansonsten <code>false</code>
	 */
	public validateMaxZeichenInFachbemerkungen(maxZeichenInFachbemerkungen: number | null): boolean {
		if (maxZeichenInFachbemerkungen === null)
			return true;
		return (maxZeichenInFachbemerkungen > 0);
	}

	/**
	 * Wenn die Sortierung einen positiven Wert hat,
	 * wird <code>true</code>, andernfalls <code>false</code> zurückgegeben.
	 *
	 * @param sortierung   die gegebene sortierung
	 *
	 * @return <code>true</code> wenn die sortierung gültig ist, ansonsten <code>false</code>
	 */
	public validateSortierung(sortierung: number | null): boolean {
		if (sortierung === null)
			return false;
		return (sortierung > 0) && (sortierung < JavaInteger.MAX_VALUE);
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link AuswahlManager}
	 *
	 * @param srcManager Manager, aus dem die Filterinformationen übernommen werden
	 */
	public useFilter(srcManager: FachListeManager): void {
		this.setFilterAuswahlPermitted(srcManager.isFilterAuswahlPermitted());
		this.setFilterNurSichtbar(srcManager.filterNurSichtbar());
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.fach.FachListeManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.fach.FachListeManager'].includes(name);
	}

	public static class = new Class<FachListeManager>('de.svws_nrw.core.utils.fach.FachListeManager');

}

export function cast_de_svws_nrw_core_utils_fach_FachListeManager(obj: unknown): FachListeManager {
	return obj as FachListeManager;
}
