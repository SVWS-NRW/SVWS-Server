import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanblockungManagerSchule extends JavaObject {

	private static readonly _tage_pro_woche : number = 5;

	private static readonly _stunden_pro_tag : number = 9;


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
		return StundenplanblockungManagerSchule._tage_pro_woche;
	}

	/**
	 * Liefert die maximale Anzahl an Stunden pro Tag.
	 * @return Die maximale Anzahl an Stunden pro Tag.
	 */
	public get_stunden_pro_tag() : number {
		return StundenplanblockungManagerSchule._stunden_pro_tag;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerSchule';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerSchule'].includes(name);
	}

	public static class = new Class<StundenplanblockungManagerSchule>('de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerSchule');

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerSchule(obj : unknown) : StundenplanblockungManagerSchule {
	return obj as StundenplanblockungManagerSchule;
}
