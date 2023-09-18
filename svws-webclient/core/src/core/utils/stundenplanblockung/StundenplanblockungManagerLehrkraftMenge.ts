import { JavaObject } from '../../../java/lang/JavaObject';
import { Random } from '../../../java/util/Random';
import { HashMap } from '../../../java/util/HashMap';
import { StundenplanblockungManagerLehrkraft, cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerLehrkraft } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLehrkraft';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import type { JavaMap } from '../../../java/util/JavaMap';

export class StundenplanblockungManagerLehrkraftMenge extends JavaObject {

	private readonly _menge : List<StundenplanblockungManagerLehrkraft> = new ArrayList();

	private readonly _map : JavaMap<number, StundenplanblockungManagerLehrkraft> = new HashMap();


	public constructor() {
		super();
	}

	/**
	 * Fügt die Lehrkraft hinzu. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID bereits existiert.
	 *
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @param pKuerzel               Das Kürzel der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID bereits existiert.
	 */
	public addOrException(pLehrkraftID : number, pKuerzel : string) : void {
		if (this._map.containsKey(pLehrkraftID))
			throw new NullPointerException("Die Lehrkraft-ID " + pLehrkraftID + " existiert bereits!")
		const le : StundenplanblockungManagerLehrkraft | null = new StundenplanblockungManagerLehrkraft(pLehrkraftID, pKuerzel);
		this._map.put(pLehrkraftID, le);
		this._menge.add(le);
	}

	/**
	 * Liefert das {@link StundenplanblockungLehrkraft}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 *
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @return                       Das {@link StundenplanblockungLehrkraft}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public getOrException(pLehrkraftID : number) : StundenplanblockungManagerLehrkraft {
		const lehrkraft : StundenplanblockungManagerLehrkraft | null = this._map.get(pLehrkraftID);
		if (lehrkraft === null)
			throw new NullPointerException("Lehrkraft-ID " + pLehrkraftID + " unbekannt!")
		return lehrkraft;
	}

	/**
	 * Liefert eine zufällige Lehrkraft. <br>
	 * Liefert eine Exception, falls die Menge der Lehrkräfte leer ist.
	 *
	 * @param pRandom  Das Random-Objekt zum Erzeugen von Zufallszahlen.
	 * @return         Liefert eine zufällige Lehrkraft.
	 */
	public getRandomOrException(pRandom : Random) : StundenplanblockungManagerLehrkraft {
		const size : number = this._menge.size();
		if (size <= 0)
			throw new NullPointerException("Es gibt keine Lehrkräfte!")
		return this._menge.get(pRandom.nextInt(size));
	}

	/**
	 * Löscht die übergebene Lehrkraft. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 *
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public removeOrException(pLehrkraftID : number) : void {
		const lehrkraft : StundenplanblockungManagerLehrkraft = this.getOrException(pLehrkraftID);
		this._map.remove(pLehrkraftID);
		this._menge.remove(lehrkraft);
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft-ID existiert.
	 *
	 * @param pLehrkraftID  Die Datenbank-ID der Lehrkraft.
	 * @return              TRUE, falls die Lehrkraft-ID existiert.
	 */
	public exists(pLehrkraftID : number) : boolean {
		return this._map.containsKey(pLehrkraftID);
	}

	/**
	 * Liefert die Menge aller Lehrkräfte.
	 *
	 * @return Die Menge aller Lehrkräfte.
	 */
	public getMenge() : List<StundenplanblockungManagerLehrkraft | null> | null {
		return this._menge;
	}

	/**
	 * Liefert die Anzahl an Lehrkräften.
	 *
	 * @return Die Anzahl an Lehrkräften.
	 */
	public size() : number {
		return this._menge.size();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLehrkraftMenge'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerLehrkraftMenge(obj : unknown) : StundenplanblockungManagerLehrkraftMenge {
	return obj as StundenplanblockungManagerLehrkraftMenge;
}
