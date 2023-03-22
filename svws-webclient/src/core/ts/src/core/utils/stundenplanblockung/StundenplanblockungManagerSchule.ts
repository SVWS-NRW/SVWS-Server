import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';

export class StundenplanblockungManagerSchule extends JavaObject {

	private _tage_pro_woche : number = 5;

	private _stunden_pro_tag : number = 9;


	/**
	 * Erzeugt ein neues Objekt.
	 */
	public constructor() {
		super();
	}

	/**
	 * Liefert die Anzahl an Unterrichtstagen in der Woche.
	 *
	 * @return Die Anzahl an Unterrichtstagen in der Woche.
	 */
	public get_tage_pro_woche() : number {
		return this._tage_pro_woche;
	}

	/**
	 * Liefert die maximale Anzahl an Stunden pro Tag.
	 * @return Die maximale Anzahl an Stunden pro Tag.
	 */
	public get_stunden_pro_tag() : number {
		return this._stunden_pro_tag;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerSchule'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerSchule(obj : unknown) : StundenplanblockungManagerSchule {
	return obj as StundenplanblockungManagerSchule;
}
