import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';

export class StundenplanblockungManagerFach extends JavaObject {

	/**
	 * Die Datenbank-ID des Faches.
	 */
	_id : number = 0;

	/**
	 * Das Kürzel des Faches. Beispielsweise 'D', 'E' oder 'M'.
	 */
	_kuerzel : string = "";

	/**
	 * Alle Lerngruppen in denen das Fach vertreten ist.
	 */
	_menge_gr : ArrayList<StundenplanblockungManagerLerngruppe> = new ArrayList<StundenplanblockungManagerLerngruppe>();


	/**
	 * Erzeugt ein neues Fach.
	 *
	 * @param pFachID   Die Datenbank-ID des Faches.
	 * @param pKuerzel  Das Kürzel des Faches.
	 */
	public constructor(pFachID : number, pKuerzel : string) {
		super();
		this._id = pFachID;
		this._kuerzel = pKuerzel;
	}

	/**
	 * Liefert die Datenbank-ID des Faches.
	 *
	 * @return Die Datenbank-ID des Faches.
	 */
	public getID() : number {
		return this._id;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerFach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerFach'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerFach(obj : unknown) : StundenplanblockungManagerFach {
	return obj as StundenplanblockungManagerFach;
}
