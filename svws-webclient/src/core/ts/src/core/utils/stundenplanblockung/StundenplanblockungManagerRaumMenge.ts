import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanblockungManagerRaum, cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerRaum } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerRaum';
import { Random } from '../../../java/util/Random';
import { HashMap } from '../../../java/util/HashMap';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ArrayList } from '../../../java/util/ArrayList';
import { List } from '../../../java/util/List';
import { JavaMap } from '../../../java/util/JavaMap';

export class StundenplanblockungManagerRaumMenge extends JavaObject {

	private readonly _menge : List<StundenplanblockungManagerRaum> = new ArrayList();

	private readonly _map : JavaMap<number, StundenplanblockungManagerRaum> = new HashMap();


	public constructor() {
		super();
	}

	/**
	 * Fügt den Raum hinzu. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID bereits existiert.
	 *
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 * @param pKuerzel               Das Kürzel des Raumes.
	 *
	 * @throws NullPointerException  Falls die Raum-ID bereits existiert.
	 */
	public addOrException(pRaumID : number, pKuerzel : string) : void {
		if (this._map.containsKey(pRaumID))
			throw new NullPointerException("Die Raum-ID " + pRaumID + " existiert bereits!")
		const ra : StundenplanblockungManagerRaum = new StundenplanblockungManagerRaum(pRaumID, pKuerzel);
		this._map.put(pRaumID, ra);
		this._menge.add(ra);
	}

	/**
	 * Liefert das {@link StundenplanblockungManagerRaum}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID unbekannt ist.
	 *
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 * @throws NullPointerException  Falls die Raum-ID unbekannt ist.
	 * @return Das {@link StundenplanblockungManagerRaum}-Objekt zur übergebenen ID.
	 */
	public getOrException(pRaumID : number) : StundenplanblockungManagerRaum {
		const ra : StundenplanblockungManagerRaum | null = this._map.get(pRaumID);
		if (ra === null)
			throw new NullPointerException("Raum-ID " + pRaumID + " unbekannt!")
		return ra;
	}

	/**
	 * Liefert einen zufälligen Raum. <br>
	 * Liefert eine Exception, falls die Menge der Räume leer ist.
	 *
	 * @param pRandom  Das Random-Objekt zum Erzeugen von Zufallszahlen.
	 *
	 * @return         Liefert einen zufälligen Raum.
	 */
	public getRandomOrException(pRandom : Random) : StundenplanblockungManagerRaum {
		const size : number = this._menge.size();
		if (size <= 0)
			throw new NullPointerException("Es gibt keine Räume!")
		return this._menge.get(pRandom.nextInt(size));
	}

	/**
	 * Löscht den übergebenen Raum. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID unbekannt ist.
	 *
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 *
	 * @throws NullPointerException  Falls die Raum-ID unbekannt ist.
	 */
	public removeOrException(pRaumID : number) : void {
		const ra : StundenplanblockungManagerRaum = this.getOrException(pRaumID);
		this._map.remove(pRaumID);
		this._menge.remove(ra);
	}

	/**
	 * Liefert die Menge aller Räume.
	 *
	 * @return Die Menge aller Räume.
	 */
	public getMenge() : List<StundenplanblockungManagerRaum | null> | null {
		return this._menge;
	}

	/**
	 * Liefert TRUE, falls die Raum-ID existiert.
	 *
	 * @param pRaumID Die Datenbank-ID des Raumes.
	 * @return TRUE, falls die Raum-ID existiert.
	 */
	public exists(pRaumID : number) : boolean {
		return this._map.containsKey(pRaumID);
	}

	/**
	 * Liefert die Anzahl an Räume.
	 *
	 * @return Die Anzahl an Räume.
	 */
	public size() : number {
		return this._menge.size();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerRaumMenge'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerRaumMenge(obj : unknown) : StundenplanblockungManagerRaumMenge {
	return obj as StundenplanblockungManagerRaumMenge;
}
