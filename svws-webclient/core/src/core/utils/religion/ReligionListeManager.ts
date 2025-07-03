import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { ReligionEintrag } from '../../../core/data/schule/ReligionEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../java/util/Comparator';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { JavaLong } from '../../../java/lang/JavaLong';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';

export class ReligionListeManager extends AuswahlManager<number, ReligionEintrag, ReligionEintrag> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _religionToId : JavaFunction<ReligionEintrag, number> = { apply : (r: ReligionEintrag) => r.id };

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _mapReligionEintragIstSichtbar : HashMap2D<boolean, number, ReligionEintrag> = new HashMap2D<boolean, number, ReligionEintrag>();

	private readonly _mapReligionEintragByKuerzel : HashMap<string, ReligionEintrag> = new HashMap<string, ReligionEintrag>();

	/**
	 * Das Filter-Attribut auf nur sichtbare ReligionEintragen
	 */
	private _filterNurSichtbar : boolean = true;

	/**
	 * Ein Default-Comparator für den Vergleich von Religion-Einträgen.
	 */
	public static readonly _comparatorKuerzel : Comparator<ReligionEintrag> = { compare : (a: ReligionEintrag, b: ReligionEintrag) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		if ((a.kuerzel === null) || (b.kuerzel === null)) {
			if ((a.kuerzel === null) && (b.kuerzel === null))
				return 0;
			return (a.kuerzel === null) ? -1 : 1;
		}
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Comparator für den Vergleich von Religion-Einträgen anhand ihres Textes.
	 */
	public static readonly _comparatorText : Comparator<ReligionEintrag> = { compare : (a: ReligionEintrag, b: ReligionEintrag) => {
		if ((a.bezeichnung === null) || (b.bezeichnung === null)) {
			if ((a.bezeichnung === null) && (b.bezeichnung === null))
				return 0;
			return (a.bezeichnung === null) ? -1 : 1;
		}
		const cmp : number = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param religionen                   die Liste der Katalog-Einträge
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, religionen : List<ReligionEintrag>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, religionen, ReligionListeManager._comparatorKuerzel, ReligionListeManager._religionToId, ReligionListeManager._religionToId, Arrays.asList());
		this.initEintrage();
	}

	private initEintrage() : void {
		for (const r of this.liste.list()) {
			this._mapReligionEintragIstSichtbar.put(r.istSichtbar, r.id, r);
			if (r.kuerzel !== null)
				this._mapReligionEintragByKuerzel.put(r.kuerzel, r);
		}
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag : ReligionEintrag, daten : ReligionEintrag) : boolean {
		let updateEintrag : boolean = false;
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
	public filterNurSichtbar() : boolean {
		return this._filterNurSichtbar;
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Fächer.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value : boolean) : void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	/**
	 * Vergleicht zwei Religionslisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : ReligionEintrag, b : ReligionEintrag) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("kuerzel", (field))) {
				cmp = ReligionListeManager._comparatorKuerzel.compare(a, b);
			} else
				if (JavaObject.equalsTranspiler("text", (field))) {
					cmp = ReligionListeManager._comparatorText.compare(a, b);
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return ReligionListeManager._comparatorKuerzel.compare(a, b);
	}

	protected checkFilter(eintrag : ReligionEintrag) : boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;
		return true;
	}

	/**
	 * Gibt die Religion anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel  das Kürzel
	 *
	 * @return die Religion oder null
	 */
	public getByKuerzelOrNull(kuerzel : string) : ReligionEintrag | null {
		return this._mapReligionEintragByKuerzel.get(kuerzel);
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link AuswahlManager}
	 *
	 * @param srcManager Manager, aus dem die Filterinformationen übernommen werden
	 */
	public useFilter(srcManager : ReligionListeManager) : void {
		this.setFilterNurSichtbar(srcManager.filterNurSichtbar());
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.religion.ReligionListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.religion.ReligionListeManager'].includes(name);
	}

	public static class = new Class<ReligionListeManager>('de.svws_nrw.core.utils.religion.ReligionListeManager');

}

export function cast_de_svws_nrw_core_utils_religion_ReligionListeManager(obj : unknown) : ReligionListeManager {
	return obj as ReligionListeManager;
}
