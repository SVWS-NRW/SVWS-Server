import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';

export class StundenplanblockungManagerKopplung extends JavaObject {

	/**
	 * Die Datenbank-ID der Kopplung.
	 */
	private readonly _id : number;

	/**
	 * Das Kürzel der Kopplung. Beispielsweise '7RE'.
	 */
	private _kuerzel : string = "";

	/**
	 * Alle Lerngruppen der Kopplungen.
	 */
	private readonly _lerngruppen : ArrayList<StundenplanblockungManagerLerngruppe | null> = new ArrayList();


	/**
	 * Erzeugt eine neue Kopplung.
	 *
	 * @param pKopplungID  Die Datenbank-ID der Kopplung.
	 * @param pKuerzel     Das Kürzel der Kopplung.
	 */
	public constructor(pKopplungID : number, pKuerzel : string) {
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
	public setKuerzel(pKuerzel : string) : void {
		this._kuerzel = pKuerzel;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKopplung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKopplung'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerKopplung(obj : unknown) : StundenplanblockungManagerKopplung {
	return obj as StundenplanblockungManagerKopplung;
}
