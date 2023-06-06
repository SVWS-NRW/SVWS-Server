import { JavaObject } from '../../../java/lang/JavaObject';
import { Random } from '../../../java/util/Random';
import { HashMap } from '../../../java/util/HashMap';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { StundenplanblockungManagerKopplung, cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerKopplung } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerKopplung';
import type { JavaMap } from '../../../java/util/JavaMap';

export class StundenplanblockungManagerKopplungMenge extends JavaObject {

	private readonly _menge : List<StundenplanblockungManagerKopplung> = new ArrayList();

	private readonly _map : JavaMap<number, StundenplanblockungManagerKopplung> = new HashMap();


	public constructor() {
		super();
	}

	/**
	 * Fügt die Kopplung hinzu. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID bereits existiert.
	 *
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @param pKuerzel               Das Kürzel der Kopplung.
	 * @throws NullPointerException  Falls die Kopplung-ID bereits existiert.
	 */
	public addOrException(pKopplungID : number, pKuerzel : string) : void {
		if (this._map.containsKey(pKopplungID))
			throw new NullPointerException("Die Kopplung-ID " + pKopplungID + " existiert bereits!")
		const ko : StundenplanblockungManagerKopplung | null = new StundenplanblockungManagerKopplung(pKopplungID, pKuerzel);
		this._map.put(pKopplungID, ko);
		this._menge.add(ko);
	}

	/**
	 * Liefert das {@link StundenplanblockungManagerKopplung}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID unbekannt ist.
	 *
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @throws NullPointerException  Falls die Kopplung-ID unbekannt ist.
	 * @return Das {@link StundenplanblockungManagerKopplung}-Objekt zur übergebenen ID.
	 */
	public getOrException(pKopplungID : number) : StundenplanblockungManagerKopplung {
		const ko : StundenplanblockungManagerKopplung | null = this._map.get(pKopplungID);
		if (ko === null)
			throw new NullPointerException("Kopplung-ID " + pKopplungID + " unbekannt!")
		return ko;
	}

	/**
	 * Liefert eine zufällige Kopplung. <br>
	 * Liefert eine Exception, falls die Menge der Kopplungen leer ist.
	 *
	 * @param pRandom  Das Random-Objekt zum Erzeugen von Zufallszahlen.
	 * @return         Liefert eine zufällige Kopplung.
	 */
	public getRandomOrException(pRandom : Random) : StundenplanblockungManagerKopplung {
		const size : number = this._menge.size();
		if (size <= 0)
			throw new NullPointerException("Es gibt keine Kopplungen!")
		return this._menge.get(pRandom.nextInt(size));
	}

	/**
	 * Löscht die übergebene Kopplung. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID unbekannt ist.
	 *
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @throws NullPointerException  Falls die Kopplung-ID unbekannt ist.
	 */
	public removeOrException(pKopplungID : number) : void {
		const ko : StundenplanblockungManagerKopplung = this.getOrException(pKopplungID);
		this._map.remove(pKopplungID);
		this._menge.remove(ko);
	}

	/**
	 * Liefert TRUE, falls die Kopplung-ID existiert.
	 *
	 * @param pKopplungID  Die Datenbank-ID der Kopplung.
	 * @return TRUE, falls die Kopplung-ID existiert.
	 */
	public exists(pKopplungID : number) : boolean {
		return this._map.containsKey(pKopplungID);
	}

	/**
	 * Liefert die Menge aller Kopplungen.
	 *
	 * @return Die Menge aller Kopplungen.
	 */
	public getMenge() : List<StundenplanblockungManagerKopplung | null> | null {
		return this._menge;
	}

	/**
	 * Liefert die Anzahl an Kopplungen.
	 *
	 * @return Die Anzahl an Kopplungen.
	 */
	public size() : number {
		return this._menge.size();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKopplungMenge'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerKopplungMenge(obj : unknown) : StundenplanblockungManagerKopplungMenge {
	return obj as StundenplanblockungManagerKopplungMenge;
}
