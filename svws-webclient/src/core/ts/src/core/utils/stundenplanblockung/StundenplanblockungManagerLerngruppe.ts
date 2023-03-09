import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { StundenplanblockungManagerFach, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerFach } from './StundenplanblockungManagerFach';
import { StundenplanblockungManagerRaum, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerRaum } from './StundenplanblockungManagerRaum';
import { StundenplanblockungManagerLehrkraft, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLehrkraft } from './StundenplanblockungManagerLehrkraft';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { StundenplanblockungManagerKlasse, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerKlasse } from './StundenplanblockungManagerKlasse';
import { StundenplanblockungManagerKopplung, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerKopplung } from './StundenplanblockungManagerKopplung';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class StundenplanblockungManagerLerngruppe extends JavaObject {

	/**
	 * Die Datenbank-ID der Lerngruppe. 
	 */
	_id : number = 0;

	/**
	 * Die Anzahl an Stunden im Stundenplan. 
	 */
	stunden : number = 0;

	/**
	 * Die Lehrkräfte der Lerngruppe. 
	 */
	_menge_le : Vector<StundenplanblockungManagerLehrkraft | null> = new Vector();

	/**
	 * Die Klassen der Lerngruppe. 
	 */
	_menge_kl : Vector<StundenplanblockungManagerKlasse | null> = new Vector();

	/**
	 * Die Fächer Lerngruppe. 
	 */
	_menge_fa : Vector<StundenplanblockungManagerFach | null> = new Vector();

	/**
	 * Die Räume Lerngruppe. 
	 */
	_menge_ra : Vector<StundenplanblockungManagerRaum | null> = new Vector();

	/**
	 * Die Kopplungen Lerngruppe. 
	 */
	_menge_ko : Vector<StundenplanblockungManagerKopplung | null> = new Vector();


	/**
	 * Erzeugt eine neue Lerngruppe mit der übergebenen ID.
	 * 
	 * @param pID  Die Datenbank-ID der Lerngruppe.
	 */
	public constructor(pID : number) {
		super();
		this._id = pID;
	}

	/**
	 * Liefert die Datenbank-ID der Lerngruppe.
	 * 
	 * @return Die Datenbank-ID der Lerngruppe.
	 */
	public getID() : number {
		return this._id;
	}

	/**
	 * Fügt der Lerngruppe die Lehrkraft hinzu (und umgekehrt).
	 * 
	 * @param pLe Das Objekt, welches hinzugefügt werden soll.
	 */
	public addLehrkraftOrException(pLe : StundenplanblockungManagerLehrkraft) : void {
		if (this._menge_le.contains(pLe) === true) 
			throw new NullPointerException("Lerngruppe " + this._id + " hat bereits Lehrkraft " + pLe._kuerzel + " (" + pLe._id + ")!")
		this._menge_le.add(pLe);
		pLe._menge_gr.add(this);
	}

	/**
	 * Fügt der Lerngruppe die Klasse hinzu (und umgekehrt).
	 * 
	 * @param pKl Das Objekt, welches hinzugefügt werden soll.
	 */
	public addKlasseOrException(pKl : StundenplanblockungManagerKlasse) : void {
		if (this._menge_kl.contains(pKl) === true) 
			throw new NullPointerException("Lerngruppe " + this._id + " hat bereits Klasse " + pKl._kuerzel + " (" + pKl._id + ")!")
		this._menge_kl.add(pKl);
		pKl._menge_gr.add(this);
	}

	/**
	 * Fügt der Lerngruppe das Fach hinzu (und umgekehrt).
	 * 
	 * @param pFa Das Objekt, welches hinzugefügt werden soll.
	 */
	public addFachOrException(pFa : StundenplanblockungManagerFach) : void {
		if (this._menge_fa.contains(pFa) === true) 
			throw new NullPointerException("Lerngruppe " + this._id + " hat bereits Fach " + pFa._kuerzel + " (" + pFa._id + ")!")
		this._menge_fa.add(pFa);
		pFa._menge_gr.add(this);
	}

	/**
	 * Liefert TRUE, falls der Lerngruppe die Lehrkraft zugeordnet ist.
	 * 
	 * @param pLe  Das Objekt, nach dem gesucht wird.
	 * @return TRUE, falls der Lerngruppe die Lehrkraft zugeordnet ist.
	 */
	public hasLehrkraft(pLe : StundenplanblockungManagerLehrkraft) : boolean {
		return this._menge_le.contains(pLe);
	}

	/**
	 * Liefert TRUE, falls der Lerngruppe der Klasse zugeordnet ist.
	 * 
	 * @param pKl  Das Objekt, nach dem gesucht wird.
	 * @return TRUE, falls der Lerngruppe der Klasse zugeordnet ist.
	 */
	public hasKlasse(pKl : StundenplanblockungManagerKlasse) : boolean {
		return this._menge_kl.contains(pKl);
	}

	/**
	 * Liefert TRUE, falls der Lerngruppe das Fach zugeordnet ist.
	 * 
	 * @param pFa  Das Objekt, nach dem gesucht wird..
	 * @return TRUE, falls der Lerngruppe das Fach zugeordnet ist.
	 */
	public hasFach(pFa : StundenplanblockungManagerFach) : boolean {
		return this._menge_fa.contains(pFa);
	}

	/**
	 * Entfernt aus der Lerngruppe die Lehrkraft (und umgekehrt).
	 * 
	 * @param pLe  Das Objekt, welches entfernt werden soll.
	 */
	public removeLehrkraftOrException(pLe : StundenplanblockungManagerLehrkraft) : void {
		if (this._menge_le.contains(pLe) === false) 
			throw new NullPointerException("Lerngruppe " + this._id + " hat nicht Lehrkraft " + pLe._kuerzel + " (" + pLe._id + ")!")
		this._menge_le.remove(pLe);
		pLe._menge_gr.remove(this);
	}

	/**
	 * Entfernt aus der Lerngruppe die Lehrkraft (und umgekehrt).
	 * 
	 * @param pKl  Das Objekt, welches entfernt werden soll.
	 */
	public removeKlasseOrException(pKl : StundenplanblockungManagerKlasse) : void {
		if (this._menge_kl.contains(pKl) === false) 
			throw new NullPointerException("Lerngruppe " + this._id + " hat nicht Klasse " + pKl._kuerzel + " (" + pKl._id + ")!")
		this._menge_kl.remove(pKl);
		pKl._menge_gr.remove(this);
	}

	/**
	 * Entfernt aus der Lerngruppe das Fach (und umgekehrt).
	 * 
	 * @param pFa  Das Objekt, welches entfernt werden soll.
	 */
	public removeFachOrException(pFa : StundenplanblockungManagerFach) : void {
		if (this._menge_fa.contains(pFa) === false) 
			throw new NullPointerException("Lerngruppe " + this._id + " hat nicht Fach " + pFa._kuerzel + " (" + pFa._id + ")!")
		this._menge_fa.remove(pFa);
		pFa._menge_gr.remove(this);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerLerngruppe'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppe(obj : unknown) : StundenplanblockungManagerLerngruppe {
	return obj as StundenplanblockungManagerLerngruppe;
}
