import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanblockungManagerSchule extends JavaObject {

	private readonly _tage_pro_woche : number = 5;

	private readonly _stunden_pro_tag : number = 9;


	/**
	 * Erzeugt ein neues Objekt.
	 */
	public constructor() {
		super();
		// empty block
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
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerSchule'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerSchule(obj : unknown) : StundenplanblockungManagerSchule {
	return obj as StundenplanblockungManagerSchule;
}
