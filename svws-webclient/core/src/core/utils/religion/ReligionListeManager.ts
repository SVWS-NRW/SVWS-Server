import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { ReligionEintrag } from '../../../core/data/schule/ReligionEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import type { List } from '../../../java/util/List';
import { ReligionUtils } from '../../../core/utils/religion/ReligionUtils';
import { Arrays } from '../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';

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
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param religionen    die Liste der ReligionEintragen
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, religionen : List<ReligionEintrag>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, religionen, ReligionUtils.comparator, ReligionListeManager._religionToId, ReligionListeManager._religionToId, Arrays.asList());
		this.initFaecher();
	}

	private initFaecher() : void {
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
		if (!JavaObject.equalsTranspiler(daten.text, (eintrag.text))) {
			eintrag.text = daten.text;
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
				if ((a.kuerzel === null) || (b.kuerzel === null)) {
					if ((a.kuerzel === null) && (b.kuerzel === null))
						cmp = 0;
					else
						cmp = (a.kuerzel === null) ? -1 : 1;
				} else
					cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			} else
				if (JavaObject.equalsTranspiler("text", (field))) {
					if ((a.text === null) || (b.text === null)) {
						if ((a.text === null) && (b.text === null))
							cmp = 0;
						else
							cmp = (a.text === null) ? -1 : 1;
					} else
						cmp = JavaString.compareTo(a.text, b.text);
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return ReligionUtils.comparator.compare(a, b);
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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.religion.ReligionListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.religion.ReligionListeManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_religion_ReligionListeManager(obj : unknown) : ReligionListeManager {
	return obj as ReligionListeManager;
}
