import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { FachDaten } from '../../../core/data/fach/FachDaten';
import type { List } from '../../../java/util/List';
import { FachUtils } from '../../../core/utils/fach/FachUtils';
import { Arrays } from '../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';

export class FachListeManager extends AuswahlManager<number, FachDaten, FachDaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _fachToId : JavaFunction<FachDaten, number> = { apply : (f: FachDaten) => f.id };

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _mapFachIstSichtbar : HashMap2D<boolean, number, FachDaten> = new HashMap2D<boolean, number, FachDaten>();

	private readonly _mapFachByKuerzel : HashMap<string, FachDaten> = new HashMap<string, FachDaten>();

	/**
	 * Das Filter-Attribut auf nur sichtbare Fächer
	 */
	private _filterNurSichtbar : boolean = true;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param faecher       die Liste der Fächer
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, faecher : List<FachDaten>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, faecher, FachUtils.comparator, FachListeManager._fachToId, FachListeManager._fachToId, Arrays.asList());
		this.initFaecher();
	}

	private initFaecher() : void {
		for (const f of this.liste.list()) {
			this._mapFachIstSichtbar.put(f.istSichtbar, f.id, f);
			if (f.kuerzel !== null)
				this._mapFachByKuerzel.put(f.kuerzel, f);
		}
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag : FachDaten, daten : FachDaten) : boolean {
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
	 * Vergleicht zwei Fächerlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : FachDaten, b : FachDaten) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("kuerzel", (field))) {
				cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			} else
				if (JavaObject.equalsTranspiler("bezeichnung", (field))) {
					cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return FachUtils.comparator.compare(a, b);
	}

	protected checkFilter(eintrag : FachDaten) : boolean {
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
	public getByKuerzelOrNull(kuerzel : string) : FachDaten | null {
		return this._mapFachByKuerzel.get(kuerzel);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.fach.FachListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.fach.FachListeManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_fach_FachListeManager(obj : unknown) : FachListeManager {
	return obj as FachListeManager;
}
