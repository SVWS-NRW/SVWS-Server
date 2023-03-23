import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanblockungManagerLerngruppe, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';

export class StundenplanblockungManagerLerngruppeStunde extends JavaObject {

	/**
	 * Die Datenbank-ID des Stundenelementes.
	 */
	_id : number = 0;

	/**
	 * Die Anzahl an Stunden in der Lerngruppe.
	 */
	wochenstunden : number = 1;

	/**
	 * Die Anzahl an Stunden im Stundenplan.
	 */
	typ : number = 1;

	/**
	 * Das Eltern-Objekt.
	 */
	readonly _gr : StundenplanblockungManagerLerngruppe;


	/**
	 * Erzeugt ein neues Stundenelement.
	 *
	 * @param pID     Die Datenbank-ID der Lerngruppe.
	 * @param pParent Das Eltern-Objekt.
	 */
	public constructor(pID : number, pParent : StundenplanblockungManagerLerngruppe) {
		super();
		this._id = pID;
		this._gr = pParent;
	}

	/**
	 * Liefert die Datenbank-ID des Objektes.
	 *
	 * @return Die Datenbank-ID des Objektes.
	 */
	public getID() : number {
		return this._id;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerLerngruppeStunde'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppeStunde(obj : unknown) : StundenplanblockungManagerLerngruppeStunde {
	return obj as StundenplanblockungManagerLerngruppeStunde;
}
