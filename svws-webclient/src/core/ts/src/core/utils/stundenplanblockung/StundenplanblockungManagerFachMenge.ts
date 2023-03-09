import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { StundenplanblockungManagerFach, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerFach } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerFach';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class StundenplanblockungManagerFachMenge extends JavaObject {

	private readonly _menge : Vector<StundenplanblockungManagerFach>;

	private readonly _map : HashMap<number, StundenplanblockungManagerFach>;


	/**
	 * Erzeugt eine neues Objekt zur Verwaltung der Menge aller Fächer.
	 */
	public constructor() {
		super();
		this._menge = new Vector();
		this._map = new HashMap();
	}

	/**
	 * Fügt das Fach hinzu. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID bereits existiert.
	 * 
	 * @param pFachID                Die Datenbank-ID des Fach. 
	 * @param pKuerzel               Das Kürzel des Faches.
	 * @throws NullPointerException  Falls die Fach-ID bereits existiert.
	 */
	public addOrException(pFachID : number, pKuerzel : string) : void {
		if (this._map.containsKey(pFachID) === true) 
			throw new NullPointerException("Die Fach-ID " + pFachID + " existiert bereits!")
		let fa : StundenplanblockungManagerFach = new StundenplanblockungManagerFach(pFachID, pKuerzel);
		this._map.put(pFachID, fa);
		this._menge.add(fa);
	}

	/**
	 * Liefert das {@link StundenplanblockungFach}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @return                       Das {@link StundenplanblockungFach}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public getOrException(pFachID : number) : StundenplanblockungManagerFach {
		let fa : StundenplanblockungManagerFach | null = this._map.get(pFachID);
		if (fa === null) 
			throw new NullPointerException("Fach-ID " + pFachID + " unbekannt!")
		return fa;
	}

	/**
	 * Löscht das übergebene Fach. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public removeOrException(pFachID : number) : void {
		let fa : StundenplanblockungManagerFach = this.getOrException(pFachID);
		this._map.remove(pFachID);
		this._menge.remove(fa);
	}

	/**
	 * Liefert TRUE, falls die Fach-ID existiert. 
	 * 
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return TRUE, falls die Fach-ID existiert.
	 */
	public exists(pFachID : number) : boolean {
		return this._map.containsKey(pFachID);
	}

	/**
	 * Liefert die Anzahl an Lehrkräften.
	 * 
	 * @return Die Anzahl an Lehrkräften.
	 */
	public size() : number {
		return this._menge.size();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerFachMenge'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerFachMenge(obj : unknown) : StundenplanblockungManagerFachMenge {
	return obj as StundenplanblockungManagerFachMenge;
}
