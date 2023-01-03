import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { StundenplanblockungManagerLerngruppe, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class StundenplanblockungManagerRaum extends JavaObject {

	private _id : number = 0;

	private _kuerzel : String = "";

	private _lerngruppen : Vector<StundenplanblockungManagerLerngruppe | null> = new Vector();


	/**
	 * @param pRaumID   Die Datenbank-ID des Raumes.
	 * @param pKuerzel  Das Kürzel des Raumes.
	 */
	public constructor(pRaumID : number, pKuerzel : String) {
		super();
		this._id = pRaumID;
		this._kuerzel = pKuerzel;
		this._lerngruppen = new Vector();
	}

	/**
	 * Liefert die Datenbank-ID des Raumes.
	 * 
	 * @return Die Datenbank-ID des Raumes.
	 */
	public getID() : number {
		return this._id;
	}

	/**
	 * Setzt das Kürzel des Raumes.
	 * 
	 * @param pKuerzel  Das neue Kürzel des Raumes.
	 */
	public setKuerzel(pKuerzel : String) : void {
		this._kuerzel = pKuerzel;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerRaum'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerRaum(obj : unknown) : StundenplanblockungManagerRaum {
	return obj as StundenplanblockungManagerRaum;
}
