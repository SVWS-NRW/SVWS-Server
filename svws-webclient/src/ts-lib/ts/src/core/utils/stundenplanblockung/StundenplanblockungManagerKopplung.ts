import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { StundenplanblockungManagerLerngruppe, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class StundenplanblockungManagerKopplung extends JavaObject {

	private _id : number = 0;

	private _kuerzel : String = "";

	private _lerngruppen : Vector<StundenplanblockungManagerLerngruppe | null> = new Vector();


	/**
	 * Erzeugt eine neue Kopplung.
	 * 
	 * @param pKopplungID  Die Datenbank-ID der Kopplung.
	 * @param pKuerzel     Das Kürzel der Kopplung.
	 */
	public constructor(pKopplungID : number, pKuerzel : String) {
		super();
		this._id = pKopplungID;
		this._kuerzel = pKuerzel;
	}

	/**
	 * Liefert die Datenbank-ID der Kopplung.
	 * 
	 * @return Die Datenbank-ID der Kopplung.
	 */
	public getID() : number {
		return this._id;
	}

	/**
	 * Setzt das Kürzel der Kopplung.
	 * 
	 * @param pKuerzel  Das neue Kürzel der Kopplung.
	 */
	public setKuerzel(pKuerzel : String) : void {
		this._kuerzel = pKuerzel;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerKopplung'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerKopplung(obj : unknown) : StundenplanblockungManagerKopplung {
	return obj as StundenplanblockungManagerKopplung;
}
