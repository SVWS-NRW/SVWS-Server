import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Random, cast_java_util_Random } from '../../../java/util/Random';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { StundenplanblockungManagerLerngruppe, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class StundenplanblockungManagerLerngruppeMenge extends JavaObject {

	/**
	 * Alle Lerngruppen.
	 */
	private readonly _menge : Vector<StundenplanblockungManagerLerngruppe>;

	private readonly _map : HashMap<number, StundenplanblockungManagerLerngruppe>;


	/**
	 * Erzeugt eine neue Menge an Lerngruppen.
	 */
	public constructor() {
		super();
		this._menge = new Vector();
		this._map = new HashMap();
	}

	/**
	 * Liefert die zuvor erzeugte Lerngruppe. <br>
	 * Wirft eine NullPointerException, falls die Lerngruppe-ID bereits existiert.
	 * 
	 * @param pLerngruppeID          Die Datenbank-ID der Lerngruppe. 
	 * @throws NullPointerException  Falls die Lerngruppe-ID bereits existiert.
	 * @return Die zuvor erzeugte Lerngruppe.
	 */
	public createOrException(pLerngruppeID : number) : StundenplanblockungManagerLerngruppe {
		if (this._map.containsKey(pLerngruppeID) === true)
			throw new NullPointerException("Die Lerngruppe-ID " + pLerngruppeID + " existiert bereits!")
		let gr : StundenplanblockungManagerLerngruppe | null = new StundenplanblockungManagerLerngruppe(pLerngruppeID);
		this._map.put(pLerngruppeID, gr);
		this._menge.add(gr);
		return gr;
	}

	/**
	 * Liefert das {@link StundenplanblockungLerngruppe}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Lerngruppe-ID unbekannt ist.
	 * 
	 * @param pLerngruppeID          Die Datenbank-ID der Lerngruppe.
	 * @throws NullPointerException  Falls die Lerngruppe-ID unbekannt ist.
	 * @return Das {@link StundenplanblockungLerngruppe}-Objekt zur übergebenen ID.
	 */
	public getOrException(pLerngruppeID : number) : StundenplanblockungManagerLerngruppe {
		let gr : StundenplanblockungManagerLerngruppe | null = this._map.get(pLerngruppeID);
		if (gr === null)
			throw new NullPointerException("Lerngruppe-ID " + pLerngruppeID + " unbekannt!")
		return gr;
	}

	/**
	 * Liefert eine zufällige Lerngruppe. <br>
	 * Liefert eine Exception, falls die Menge der Lerngruppen leer ist. 
	 * 
	 * @param pRandom  Das Random-Objekt zum Erzeugen von Zufallszahlen. 
	 * @return         Liefert eine zufällige Lerngruppe.
	 */
	public getRandomOrException(pRandom : Random) : StundenplanblockungManagerLerngruppe {
		let size : number = this._menge.size();
		if (size <= 0)
			throw new NullPointerException("Es gibt keine Lerngruppen!")
		return this._menge.get(pRandom.nextInt(size));
	}

	/**
	 * Löscht die übergebene Lerngruppe. <br>
	 * Wirft eine NullPointerException, falls die Lerngruppe-ID unbekannt ist.
	 * 
	 * @param pLerngruppeID          Die Datenbank-ID der Lerngruppe.
	 * @throws NullPointerException  Falls die Lerngruppe-ID unbekannt ist.
	 */
	public removeOrException(pLerngruppeID : number) : void {
		let gr : StundenplanblockungManagerLerngruppe = this.getOrException(pLerngruppeID);
		this._map.remove(pLerngruppeID);
		this._menge.remove(gr);
	}

	/**
	 * Liefert die Menge aller Lerngruppen.
	 * 
	 * @return Die Menge aller Lerngruppen.
	 */
	public getMenge() : Vector<StundenplanblockungManagerLerngruppe | null> | null {
		return this._menge;
	}

	/**
	 * Liefert TRUE, falls die Lerngruppe-ID existiert. 
	 * 
	 * @param pRaumID Die Datenbank-ID des Raumes.
	 * @return TRUE, falls die Lerngruppe-ID existiert.
	 */
	public exists(pRaumID : number) : boolean {
		return this._map.containsKey(pRaumID);
	}

	/**
	 * Liefert die Anzahl an Lerngruppen.
	 * 
	 * @return Die Anzahl an Lerngruppen.
	 */
	public size() : number {
		return this._menge.size();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerLerngruppeMenge'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppeMenge(obj : unknown) : StundenplanblockungManagerLerngruppeMenge {
	return obj as StundenplanblockungManagerLerngruppeMenge;
}
