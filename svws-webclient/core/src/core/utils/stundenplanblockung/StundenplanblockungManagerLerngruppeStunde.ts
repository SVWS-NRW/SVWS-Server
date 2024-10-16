import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';
import { StundenplanblockungManagerLerngruppe, cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLerngruppeStunde';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLerngruppeStunde'].includes(name);
	}

	public static class = new Class<StundenplanblockungManagerLerngruppeStunde>('de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLerngruppeStunde');

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppeStunde(obj : unknown) : StundenplanblockungManagerLerngruppeStunde {
	return obj as StundenplanblockungManagerLerngruppeStunde;
}
