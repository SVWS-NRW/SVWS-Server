import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';

export class StundenplanblockungManagerRaum extends JavaObject {

	/**
	 * Die Datenbank-ID des Raumes.
	 */
	private readonly _id : number;

	/**
	 * Das Kürzel des Raumes. Beispielsweise 'E01'.
	 */
	private _kuerzel : string = "";

	/**
	 * Alle Lerngruppen der Räume.
	 */
	private _lerngruppen : ArrayList<StundenplanblockungManagerLerngruppe | null> = new ArrayList();


	/**
	 * @param pRaumID   Die Datenbank-ID des Raumes.
	 * @param pKuerzel  Das Kürzel des Raumes.
	 */
	public constructor(pRaumID : number, pKuerzel : string) {
		super();
		this._id = pRaumID;
		this._kuerzel = pKuerzel;
		this._lerngruppen = new ArrayList();
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
	public setKuerzel(pKuerzel : string) : void {
		this._kuerzel = pKuerzel;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerRaum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerRaum(obj : unknown) : StundenplanblockungManagerRaum {
	return obj as StundenplanblockungManagerRaum;
}
