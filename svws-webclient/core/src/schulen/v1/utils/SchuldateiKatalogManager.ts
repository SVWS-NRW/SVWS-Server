import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiKatalogeintrag } from '../../../schulen/v1/data/SchuldateiKatalogeintrag';
import { SchuldateiEintrag, cast_de_svws_nrw_schulen_v1_data_SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { JavaString } from '../../../java/lang/JavaString';
import type { JavaMap } from '../../../java/util/JavaMap';
import { SchuldateiUtils } from '../../../schulen/v1/utils/SchuldateiUtils';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import type { Comparator } from '../../../java/util/Comparator';

export class SchuldateiKatalogManager extends JavaObject {

	/**
	 * Der Name des Katalogs
	 */
	private readonly _name : string;

	/**
	 * Die Liste aller Katalog-Einträge dieses Katalogs
	 */
	private readonly _katalogeintraege : List<SchuldateiKatalogeintrag> = new ArrayList<SchuldateiKatalogeintrag>();

	/**
	 * Eine Map von dem Wert der Katalog-Einträge auf die Liste der Katalog-Einträge mit diesem Wert
	 */
	private readonly _mapKatalogeintraegeByWert : JavaMap<string, List<SchuldateiKatalogeintrag>> = new HashMap<string, List<SchuldateiKatalogeintrag>>();

	/**
	 * Eine Map von dem Schlüssel der Katalog-Einträge auf eine Liste der Katalog-Einträge mit diesem Schlüssel
	 */
	private readonly _mapKatalogeintraegeBySchluessel : JavaMap<string, List<SchuldateiKatalogeintrag>> = new HashMap<string, List<SchuldateiKatalogeintrag>>();

	/**
	 * Cache: Eine Map der Einträge anhand des Schuljahres
	 */
	private readonly _mapKatalogeintraegeBySchuljahr : JavaMap<number, List<SchuldateiKatalogeintrag>> = new HashMap<number, List<SchuldateiKatalogeintrag>>();

	/**
	 * Cache: Eine Map nach Schuljahren für Maps von Wert auf einen SchuldateiKatalogeintrag
	 */
	private readonly _mapKatalogEintraegeBySchuljahrAndWert : JavaMap<number, JavaMap<string, SchuldateiKatalogeintrag>> = new HashMap<number, JavaMap<string, SchuldateiKatalogeintrag>>();

	/**
	 * Cache: Eine Map nach Schuljahren für Maps von Schlüssel auf eine Liste von SchuldateiKatalogeinträgen
	 */
	private readonly _mapKatalogEintraegeBySchuljahrAndSchluessel : JavaMap<number, JavaMap<string, List<SchuldateiKatalogeintrag>>> = new HashMap<number, JavaMap<string, List<SchuldateiKatalogeintrag>>>();

	/**
	 * Der Comparator zur Sortierung der Zeiträume gueltigab - gueltigbis in absteigender Reihenfolge
	 */
	private static readonly _comparatorZeitraumDesc : Comparator<SchuldateiKatalogeintrag> = { compare : (a: SchuldateiKatalogeintrag, b: SchuldateiKatalogeintrag) => {
		if (JavaObject.equalsTranspiler(b.gueltigab, (a.gueltigab)))
			return SchuldateiUtils.compare(b.gueltigbis, a.gueltigbis);
		return SchuldateiUtils.compare(b.gueltigab, a.gueltigab);
	} };


	/**
	 * Erstellt einen neuen Katalog-Manager.
	 *
	 * @param name   der Name des Katalogs
	 */
	public constructor(name : string) {
		super();
		this._name = name;
	}

	/**
	 * Fügt diesem Katalog einen neuen Eintrag hinzu.
	 * Einträge eines Kataloges werden in der Schuldatei über die Eigenschaft "wert" referenziert.
	 * Es können mehrere Einträge eines Kataloges mit demselben Wert vorkommen, wobei sich der Zeitraum unterscheiden muss.
	 * Die entsprechende Überprüfung führt die Methode validate aus, die auch die Liste der Einträge in _mapEintraegeByWert sortiert.
	 *
	 * @param eintrag   der Eintrag
	 */
	addEintrag(eintrag : SchuldateiKatalogeintrag) : void {
		this._katalogeintraege.add(eintrag);
		const eintraegeByWert : List<SchuldateiKatalogeintrag> | null = this._mapKatalogeintraegeByWert.computeIfAbsent(eintrag.wert, { apply : (k: string) => new ArrayList<SchuldateiKatalogeintrag>() });
		if (eintraegeByWert !== null)
			eintraegeByWert.add(eintrag);
		if (!JavaString.isBlank(eintrag.schluessel)) {
			const eintraegeBySchluessel : List<SchuldateiKatalogeintrag> | null = this._mapKatalogeintraegeBySchluessel.computeIfAbsent(eintrag.schluessel, { apply : (k: string) => new ArrayList<SchuldateiKatalogeintrag>() });
			if (eintraegeBySchluessel !== null)
				eintraegeBySchluessel.add(eintrag);
		}
	}

	/**
	 * Gibt den Namen des Katalogs zurück.
	 *
	 * @return der Name des Katalogs
	 */
	public getName() : string {
		return this._name;
	}

	/**
	 * Gibt alle Katalog-Einträge zurück
	 *
	 * @return die Liste mit allen Katalog-Einträgen
	 */
	public getEintraege() : List<SchuldateiKatalogeintrag> {
		return this._katalogeintraege;
	}

	/**
	 * Gibt die Katalog-Einträge für das angegebene Schuljahr zurück
	 *
	 * @param schuljahr    das Schuljahr, zu dem die Werte geliefert werden
	 *
	 * @return die Liste der Katalog-Einträge, die in dem Schuljahr gültig sind
	 */
	public getEintraegeBySchuljahr(schuljahr : number) : List<SchuldateiKatalogeintrag> {
		const list : List<SchuldateiKatalogeintrag> | null = this._mapKatalogeintraegeBySchuljahr.get(schuljahr);
		if (list !== null)
			return list;
		const listEintraege : List<SchuldateiKatalogeintrag> = new ArrayList<SchuldateiKatalogeintrag>();
		for (const eintrag of this._katalogeintraege)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				listEintraege.add(eintrag);
		this._mapKatalogeintraegeBySchuljahr.put(schuljahr, listEintraege);
		return listEintraege;
	}

	/**
	 * Gibt die Katalog-Einträge zu dem Wert zurück, sofern der Wert gültig ist.
	 * Es werden mehrere Einträge zurückgegeben, wenn für verschiedene Zeiträume entsprechende Einträge im Katalog sind.
	 *
	 * @param wert   der Wert des gesuchten Katalog-Eintrags
	 *
	 * @return die Katalog-Einträge oder null, wenn es keinen für den Wert gibt.
	 */
	public getEintraegeByWert(wert : string | null) : List<SchuldateiKatalogeintrag> | null {
		return this._mapKatalogeintraegeByWert.get(wert);
	}

	/**
	 * Gibt die Katalog-Einträge zu dem Wert zurück, sofern der Wert gültig ist.
	 *
	 * @param wert   der Wert des gesuchten Katalog-Eintrags
	 *
	 * @return die Katalog-Einträge oder null, wenn es keinen für den Wert gibt.
	 */
	public getEintraegeByIntegerWert(wert : number) : List<SchuldateiKatalogeintrag> | null {
		return this.getEintraegeByWert("" + wert);
	}

	/**
	 * Gibt den Katalog-Eintrag für das Schuljahr und den Wert zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param wert        der Wert
	 *
	 * @return der Katalog-Eintrag für das Schuljahr und den Wert falls er existiert und ansonsten null.
	 */
	public getEintragBySchuljahrAndWert(schuljahr : number, wert : string | null) : SchuldateiKatalogeintrag | null {
		return this.getCacheBySchuljahrAndWert(schuljahr).get(wert);
	}

	/**
	 * Gibt den Katalog-Eintrag für das Schuljahr und den numerischen Wert zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param wert        der numerische Wert
	 *
	 * @return der Katalog-Eintrag für das Schuljahr und den numerischen Wert falls er existiert und ansonsten null.
	 */
	public getEintragBySchuljahrAndIntegerWert(schuljahr : number, wert : number) : SchuldateiKatalogeintrag | null {
		return this.getEintragBySchuljahrAndWert(schuljahr, "" + wert);
	}

	/**
	 * Gibt die Katalog-Einträge zu dem Schlüssel zurück, sofern der Schlüssel gültig ist.
	 *
	 * @param schluessel   der Schlüssel der gesuchten Katalog-Einträge
	 *
	 * @return die Liste der Katalog-Einträge für den Schlüssel existiert der Schlüssel nicht, so wird null zurückgegeben
	 */
	public getEintraegeBySchluessel(schluessel : string | null) : List<SchuldateiKatalogeintrag> | null {
		return this._mapKatalogeintraegeBySchluessel.get(schluessel);
	}

	/**
	 * Gibt die Katalog-Einträge zu dem Schlüssel für ein bestimmtes Schuljahr zurück, sofern der Schlüssel gültig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schluessel   der Schlüssel der gesuchten Katalog-Einträge
	 *
	 * @return die Liste der Katalog-Eintrag für den Schlüssel existiert der Schlüssel nicht, so wird null zurückgegeben
	 */
	public getEintraegeBySchuljahrAndSchluessel(schuljahr : number, schluessel : string | null) : List<SchuldateiKatalogeintrag> | null {
		if ((schluessel === null) || (!this._mapKatalogeintraegeBySchluessel.containsKey(schluessel)))
			return null;
		const map : JavaMap<string, List<SchuldateiKatalogeintrag>> | null = this._mapKatalogEintraegeBySchuljahrAndSchluessel.get(schuljahr);
		if (map !== null)
			return map.get(schluessel);
		const neueMap : JavaMap<string, List<SchuldateiKatalogeintrag>> = new HashMap<string, List<SchuldateiKatalogeintrag>>();
		for (const eintrag of this._katalogeintraege)
			if ((!JavaString.isBlank(eintrag.schluessel)) && (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))) {
				const eintraegeBySchluessel : List<SchuldateiKatalogeintrag> | null = neueMap.computeIfAbsent(eintrag.schluessel, { apply : (k: string) => new ArrayList<SchuldateiKatalogeintrag>() });
				if (eintraegeBySchluessel !== null)
					eintraegeBySchluessel.add(eintrag);
			}
		this._mapKatalogEintraegeBySchuljahrAndSchluessel.put(schuljahr, neueMap);
		return neueMap.get(schluessel);
	}

	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den Wert existiert.
	 *
	 * @param wert   der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public hasEintrag(wert : string | null) : boolean;

	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den Wert existiert.
	 *
	 * @param wert   der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public hasEintrag(wert : number) : boolean;

	/**
	 * Implementation for method overloads of 'hasEintrag'
	 */
	public hasEintrag(__param0 : null | number | string) : boolean {
		if (((__param0 !== undefined) && (typeof __param0 === "string") || (__param0 === null))) {
			const wert : string | null = __param0;
			if (wert === null)
				return false;
			return this._mapKatalogeintraegeByWert.containsKey(wert);
		} else if (((__param0 !== undefined) && typeof __param0 === "number")) {
			const wert : number = __param0 as number;
			return this.hasEintrag("" + wert);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den Wert in einem Schuljahr existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param wert        der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag im angegebenen Schuljahr existiert und ansonsten false.
	 */
	public hasEintragBySchuljahr(schuljahr : number, wert : string) : boolean;

	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den numerischen Wert existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param wert        der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public hasEintragBySchuljahr(schuljahr : number, wert : number) : boolean;

	/**
	 * Implementation for method overloads of 'hasEintragBySchuljahr'
	 */
	public hasEintragBySchuljahr(__param0 : number, __param1 : number | string) : boolean {
		if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && (typeof __param1 === "string"))) {
			const schuljahr : number = __param0 as number;
			const wert : string = __param1;
			return this.getCacheBySchuljahrAndWert(schuljahr).containsKey(wert);
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number")) {
			const schuljahr : number = __param0 as number;
			const wert : number = __param1 as number;
			return this.hasEintragBySchuljahr(schuljahr, ("" + wert));
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den Wert in einem Zeitraum existiert.
	 *
	 * @param abBis  Der Schuldateieintrag, der den Zeitraum definiert, für den der Eintrag komplett vorliegen muss.
	 * @param wert   der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public hasEintragInZeitraum(abBis : SchuldateiEintrag | null, wert : string | null) : boolean;

	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den numerischen Wert in einem Zeitraum existiert.
	 *
	 * @param abBis  Der Schuldateieintrag, der den Zeitraum definiert, für den der Eintrag komplett vorliegen muss.
	 * @param wert   der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public hasEintragInZeitraum(abBis : SchuldateiEintrag | null, wert : number) : boolean;

	/**
	 * Prüft ob ein Katalog-Eintrag für den Wert in einem Zeitraum existiert
	 * Ist der Parameter mitTeilgueltigkeit auf TRUE reicht es wenn der Wert nur teilweise im Zeitraum existiert
	 * Ist er auf FALSE muss der Wert im gesamten Zeitraum definiert sein.
	 *
	 * @param schuljahrAb			das erste Schuljahr
	 * @param schuljahrBis			das letzte Schuljahr
	 * @param wert                  der Wert, auf den geprüft wird
	 * @param mitTeilgueltigkeit	wenn true, reichts es, wenn der Wert nicht im gesamten Zeitraum definiert ist.
	 *
	 * @return boolean, true wenn Eintrag entsprechend vorliegt, sonst false
	 */
	public hasEintragInZeitraum(schuljahrAb : number, schuljahrBis : number, wert : string | null, mitTeilgueltigkeit : boolean) : boolean;

	/**
	 * Implementation for method overloads of 'hasEintragInZeitraum'
	 */
	public hasEintragInZeitraum(__param0 : SchuldateiEintrag | null | number, __param1 : null | number | string, __param2? : null | string, __param3? : boolean) : boolean {
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.schulen.v1.data.SchuldateiEintrag'))) || (__param0 === null)) && ((__param1 !== undefined) && (typeof __param1 === "string") || (__param1 === null)) && (__param2 === undefined) && (__param3 === undefined)) {
			const abBis : SchuldateiEintrag | null = cast_de_svws_nrw_schulen_v1_data_SchuldateiEintrag(__param0);
			const wert : string | null = __param1;
			if (wert === null)
				return false;
			const ab : number = abBis.gueltigab === null ? SchuldateiUtils._immerGueltigAb : SchuldateiUtils.schuljahrAusDatum(abBis.gueltigab);
			const bis : number = abBis.gueltigbis === null ? SchuldateiUtils._immerGueltigBis : SchuldateiUtils.schuljahrAusDatum(abBis.gueltigbis);
			return this.hasEintragInZeitraum(ab, bis, wert, false);
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.schulen.v1.data.SchuldateiEintrag'))) || (__param0 === null)) && ((__param1 !== undefined) && typeof __param1 === "number") && (__param2 === undefined) && (__param3 === undefined)) {
			const abBis : SchuldateiEintrag | null = cast_de_svws_nrw_schulen_v1_data_SchuldateiEintrag(__param0);
			const wert : number = __param1 as number;
			return this.hasEintragInZeitraum(abBis, "" + wert);
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number") && ((__param2 !== undefined) && (typeof __param2 === "string") || (__param2 === null)) && ((__param3 !== undefined) && typeof __param3 === "boolean")) {
			const schuljahrAb : number = __param0 as number;
			const schuljahrBis : number = __param1 as number;
			const wert : string | null = __param2;
			const mitTeilgueltigkeit : boolean = __param3 as boolean;
			const list : List<SchuldateiKatalogeintrag> | null = this.getEintraegeByWert(wert);
			if (list === null)
				return false;
			list.sort(SchuldateiKatalogManager._comparatorZeitraumDesc);
			let ab : number = schuljahrAb < SchuldateiUtils._immerGueltigAb ? SchuldateiUtils._immerGueltigAb : schuljahrAb;
			let bis : number = schuljahrBis > SchuldateiUtils._immerGueltigBis ? SchuldateiUtils._immerGueltigBis : schuljahrBis;
			for (let eintrag of list) {
				if ((eintrag.gueltigbis === null) || SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigbis) < bis)
					return false;
				const vonSchuljahr : number = (eintrag.gueltigab === null ? SchuldateiUtils._immerGueltigAb : SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigab));
				if (vonSchuljahr <= bis) {
					if (mitTeilgueltigkeit || (vonSchuljahr <= ab))
						return true;
					bis = vonSchuljahr - 1;
				}
			}
			return false;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt die Bezeichnung des Katalog-Eintrag zu dem Wert im angegebenen Schuljahr zurück, sofern
	 * der Wert gültig ist.
	 *
	 * @param schuljahr	das Schuljahr
	 * @param wert   	der Wert des Katalog-Eintrags
	 *
	 * @return die Bezeichnung oder null, wenn sie nicht gültig ist
	 */
	public getBezeichnung(schuljahr : number, wert : string | null) : string | null {
		const eintrag : SchuldateiKatalogeintrag | null = this.getEintragBySchuljahrAndWert(schuljahr, wert);
		if (eintrag === null)
			return null;
		return eintrag.bezeichnung;
	}

	/**
	 * gibt die Daten aus dem Cache zurück und baut ihn ggfs. auf, wenn er noch nicht existiert
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return Der gefüllte Cache für das übergebene Schuljahr
	 */
	private getCacheBySchuljahrAndWert(schuljahr : number) : JavaMap<string, SchuldateiKatalogeintrag> {
		const map : JavaMap<string, SchuldateiKatalogeintrag> | null = this._mapKatalogEintraegeBySchuljahrAndWert.get(schuljahr);
		if (map !== null)
			return map;
		const neueMap : JavaMap<string, SchuldateiKatalogeintrag> = new HashMap<string, SchuldateiKatalogeintrag>();
		for (const eintrag of this._katalogeintraege)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				neueMap.put(eintrag.wert, eintrag);
		this._mapKatalogEintraegeBySchuljahrAndWert.put(schuljahr, neueMap);
		return neueMap;
	}

	/**
	 * sortiert die Listen in _mapKatalogeintraegeByWert und prüft, dass die Werte nicht überlappend sind.
	 * (Die Liste wird absteigend sortiert!)
	 *
	 * @throws IllegalArgumentException   falls eine Überlappung festgestellt wird.
	 */
	public validate() : void {
		for (const list of this._mapKatalogeintraegeByWert.values()) {
			let eintrag : SchuldateiKatalogeintrag;
			if (list.size() > 1) {
				list.sort(SchuldateiKatalogManager._comparatorZeitraumDesc);
				eintrag = list.getFirst();
				let schuljahrBis : number = eintrag.gueltigbis === null ? SchuldateiUtils._immerGueltigBis : SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigbis);
				let schuljahrAb : number = eintrag.gueltigab === null ? SchuldateiUtils._immerGueltigAb : SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigab);
				for (let i : number = 1; i < list.size(); i++) {
					if (schuljahrBis < schuljahrAb)
						throw new IllegalArgumentException("Dieser Katalogeintrag Katalog='" + eintrag.katalog + "', Wert='" + eintrag.wert + "' hat einen ungültigen Gültigkeitszeitraum.")
					eintrag = list.get(i);
					schuljahrBis = eintrag.gueltigbis === null ? SchuldateiUtils._immerGueltigBis : SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigbis);
					if (schuljahrBis >= schuljahrAb)
						throw new IllegalArgumentException("Dieser Katalogeintrag Katalog='" + eintrag.katalog + "', Wert='" + eintrag.wert + "' hat überlappende Gültigkeitszeiträume.")
					schuljahrAb = eintrag.gueltigab === null ? SchuldateiUtils._immerGueltigAb : SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigab);
				}
				if (schuljahrBis < schuljahrAb)
					throw new IllegalArgumentException("Dieser Katalogeintrag Katalog='" + eintrag.katalog + "', Wert='" + eintrag.wert + "' hat einen ungültigen Gültigkeitszeitraum.")
			} else {
				eintrag = list.getFirst();
				if (SchuldateiUtils.istFrueher(eintrag.gueltigbis, eintrag.gueltigab))
					throw new IllegalArgumentException("Dieser Katalogeintrag Katalog='" + eintrag.katalog + "', Wert='" + eintrag.wert + "' hat einen ungültigen Gültigkeitszeitraum.")
			}
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.utils.SchuldateiKatalogManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.utils.SchuldateiKatalogManager'].includes(name);
	}

}

export function cast_de_svws_nrw_schulen_v1_utils_SchuldateiKatalogManager(obj : unknown) : SchuldateiKatalogManager {
	return obj as SchuldateiKatalogManager;
}
