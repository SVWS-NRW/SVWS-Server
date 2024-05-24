import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { SchuldateiKatalogeintrag } from '../../../schuldatei/v1/data/SchuldateiKatalogeintrag';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import type { JavaMap } from '../../../java/util/JavaMap';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { HashSet } from '../../../java/util/HashSet';

export class SchuldateiKatalogManager extends JavaObject {

	/**
	 * Der Name des Katalogs
	 */
	private readonly _name : string;

	/**
	 * Die Liste aller Katalog-Einträge dieses Katalogs
	 */
	private readonly _eintraege : List<SchuldateiKatalogeintrag> = new ArrayList<SchuldateiKatalogeintrag>();

	/**
	 * Eine Map von dem Wert der Katalog-Einträge auf diese
	 */
	private readonly _mapEintragByWert : JavaMap<string, SchuldateiKatalogeintrag> = new HashMap<string, SchuldateiKatalogeintrag>();

	/**
	 * Eine Map von dem Wert (als Integer) der Katalog-Einträge auf diese
	 */
	private readonly _mapEintragByIntegerWert : JavaMap<number, SchuldateiKatalogeintrag> = new HashMap<number, SchuldateiKatalogeintrag>();

	/**
	 * Eine Map von dem Schlüssel der Katalog-Einträge auf eine Menge von zugeordneten Katalog-Einträgen
	 */
	private readonly _mapEintraegeBySchluessel : JavaMap<string, JavaSet<SchuldateiKatalogeintrag>> = new HashMap<string, JavaSet<SchuldateiKatalogeintrag>>();


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
	 * Fügt einen neuen Eintrag zum Manager hinzu.
	 *
	 * @param eintrag   der Eintrag
	 */
	addEintrag(eintrag : SchuldateiKatalogeintrag) : void {
		this._eintraege.add(eintrag);
		if (this._mapEintragByWert.containsKey(eintrag.wert))
			throw new IllegalArgumentException("Katalog " + this._name + ": Es existiert bereits ein anderer Katalog-Eintrag mit dem angegebenen Wert " + eintrag.wert + ".")
		this._mapEintragByWert.put(eintrag.wert, eintrag);
		try {
			this._mapEintragByIntegerWert.put(JavaInteger.parseInt(eintrag.wert), eintrag);
		} catch(nfe) {
			// empty block
		}
		let tmpSetEintraege : JavaSet<SchuldateiKatalogeintrag> | null = this._mapEintraegeBySchluessel.get(eintrag.schluessel);
		if (tmpSetEintraege === null) {
			tmpSetEintraege = new HashSet<SchuldateiKatalogeintrag>();
			this._mapEintraegeBySchluessel.put(eintrag.schluessel, tmpSetEintraege);
		}
		tmpSetEintraege.add(eintrag);
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
	 * Gibt die Katalog-Einträge zu dem Schlüssel zurück, sofern der Schlüssel gültig ist.
	 *
	 * @param schluessel   der Schlüssel der gesuchten Katalog-Einträge
	 *
	 * @return die Liste der Katalog-Eintrag für den Schlüssel existiert der Schlüssel nicht,
	 *         so wird eine leere Menge zurückgegeben
	 */
	public getEintraege(schluessel : string | null) : JavaSet<SchuldateiKatalogeintrag> {
		const tmp : JavaSet<SchuldateiKatalogeintrag> | null = this._mapEintraegeBySchluessel.get(schluessel);
		return (tmp === null) ? new HashSet<SchuldateiKatalogeintrag>() : tmp;
	}

	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den Wert existiert.
	 *
	 * @param wert   der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public hatEintrag(wert : string | null) : boolean;

	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den Wert existiert.
	 *
	 * @param wert   der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public hatEintrag(wert : number) : boolean;

	/**
	 * Implementation for method overloads of 'hatEintrag'
	 */
	public hatEintrag(__param0 : null | number | string) : boolean {
		if (((typeof __param0 !== "undefined") && (typeof __param0 === "string") || (__param0 === null))) {
			const wert : string | null = __param0;
			if (wert === null)
				return false;
			return this._mapEintragByWert.containsKey(wert);
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			const wert : number = __param0 as number;
			return this._mapEintragByIntegerWert.containsKey(wert);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt den Katalog-Eintrag zu dem Wert zurück, sofern der Wert gültig ist.
	 *
	 * @param wert   der Wert des gesuchten Katalog-Eintrags
	 *
	 * @return der Katalog-Eintrag oder null, wenn es keinen für den Wert gibt.
	 */
	public getEintrag(wert : string | null) : SchuldateiKatalogeintrag | null;

	/**
	 * Gibt den Katalog-Eintrag zu dem Wert zurück, sofern der Wert gültig ist.
	 *
	 * @param wert   der Wert des gesuchten Katalog-Eintrags
	 *
	 * @return der Katalog-Eintrag oder null, wenn es keinen für den Wert gibt.
	 */
	public getEintrag(wert : number) : SchuldateiKatalogeintrag | null;

	/**
	 * Implementation for method overloads of 'getEintrag'
	 */
	public getEintrag(__param0 : null | number | string) : SchuldateiKatalogeintrag | null {
		if (((typeof __param0 !== "undefined") && (typeof __param0 === "string") || (__param0 === null))) {
			const wert : string | null = __param0;
			return this._mapEintragByWert.get(wert);
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			const wert : number = __param0 as number;
			return this._mapEintragByIntegerWert.get(wert);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt die Bezeichnung des Katalog-Eintrag zu dem Wert zurück, sofern
	 * der Wert gültig ist.
	 *
	 * @param wert   der Wert des Katalog-Eintrags
	 *
	 * @return die Bezeichnung
	 *
	 * @throws IllegalArgumentException   falls der Wert ungültig ist
	 */
	public getBezeichnung(wert : string | null) : string {
		const eintrag : SchuldateiKatalogeintrag | null = this.getEintrag(wert);
		if (eintrag === null)
			throw new IllegalArgumentException("Es konnte kein Katalog-Eintrag für den Wert " + wert! + " gefunden werden.")
		return eintrag.bezeichnung;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.utils.SchuldateiKatalogManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.utils.SchuldateiKatalogManager'].includes(name);
	}

}

export function cast_de_svws_nrw_schuldatei_v1_utils_SchuldateiKatalogManager(obj : unknown) : SchuldateiKatalogManager {
	return obj as SchuldateiKatalogManager;
}
