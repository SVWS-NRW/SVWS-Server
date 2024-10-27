import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';

export class StundenplanblockungManagerLehrkraft extends JavaObject {

	/**
	 * Die Datenbank-ID der Lehrkraft.
	 */
	readonly _id : number;

	/**
	 * Das Kürzel der Lehrkraft. Beispielsweise 'BAR'.
	 */
	_kuerzel : string = "";

	/**
	 * TRUE, falls die Lehrkraft zur Vertretung in Springstunden herangezogen werden kann.
	 */
	_darf_vertreten : boolean = false;

	/**
	 * Alle Lerngruppen der Lehrkraft.
	 */
	readonly _menge_gr : List<StundenplanblockungManagerLerngruppe>;


	/**
	 * Konstrukur zur Erstellung eines {@link StundenplanblockungManagerLehrkraft}-Objektes.
	 *
	 * @param pLehrkraftID  Die Datenbank-ID der Lehrkraft.
	 * @param pKuerzel      Das Kürzel der Lehrkraft.
	 */
	public constructor(pLehrkraftID : number, pKuerzel : string) {
		super();
		this._id = pLehrkraftID;
		this._kuerzel = pKuerzel;
		this._menge_gr = new ArrayList();
	}

	/**
	 * Liefert die Datenbank-ID der Lehrkraft.
	 *
	 * @return Die Datenbank-ID der Lehrkraft.
	 */
	public getID() : number {
		return this._id;
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft zur Vertretung in Springstunden herangezogen werden kann.
	 *
	 * @return TRUE, falls die Lehrkraft zur Vertretung in Springstunden herangezogen werden kann.
	 */
	public getDarfVertreten() : boolean {
		return this._darf_vertreten;
	}

	/**
	 * Setzt das Kürzel der Lehrkraft.
	 *
	 * @param pKuerzel  Das neue Kürzel der Lehrkraft.
	 */
	public setKuerzel(pKuerzel : string) : void {
		this._kuerzel = pKuerzel;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLehrkraft';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLehrkraft'].includes(name);
	}

	public static class = new Class<StundenplanblockungManagerLehrkraft>('de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLehrkraft');

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerLehrkraft(obj : unknown) : StundenplanblockungManagerLehrkraft {
	return obj as StundenplanblockungManagerLehrkraft;
}
