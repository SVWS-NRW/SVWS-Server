import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Random, cast_java_util_Random } from '../../../java/util/Random';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { StundenplanblockungManagerKopplung, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerKopplung } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerKopplung';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class StundenplanblockungManagerKopplungMenge extends JavaObject {

	private readonly _menge : Vector<StundenplanblockungManagerKopplung>;

	private readonly _map : HashMap<number, StundenplanblockungManagerKopplung>;


	/**
	 * Erzeugt eine neues Objekt zur Verwaltung der Menge aller Kopplungen.
	 */
	public constructor() {
		super();
		this._menge = new Vector();
		this._map = new HashMap();
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
		if (this._map.containsKey(pKopplungID) === true)
			throw new NullPointerException("Die Kopplung-ID " + pKopplungID + " existiert bereits!")
		let ko : StundenplanblockungManagerKopplung | null = new StundenplanblockungManagerKopplung(pKopplungID, pKuerzel);
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
		let ko : StundenplanblockungManagerKopplung | null = this._map.get(pKopplungID);
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
		let size : number = this._menge.size();
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
		let ko : StundenplanblockungManagerKopplung = this.getOrException(pKopplungID);
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
	public getMenge() : Vector<StundenplanblockungManagerKopplung | null> | null {
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
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerKopplungMenge'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerKopplungMenge(obj : unknown) : StundenplanblockungManagerKopplungMenge {
	return obj as StundenplanblockungManagerKopplungMenge;
}
