import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanblockungManagerFach } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerFach';
import { StundenplanblockungManagerRaum } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerRaum';
import { StundenplanblockungManagerLehrkraft } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLehrkraft';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanblockungManagerKlasse } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerKlasse';
import { StundenplanblockungManagerKopplung } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerKopplung';

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
	_menge_le : ArrayList<StundenplanblockungManagerLehrkraft | null> = new ArrayList();

	/**
	 * Die Klassen der Lerngruppe.
	 */
	_menge_kl : ArrayList<StundenplanblockungManagerKlasse | null> = new ArrayList();

	/**
	 * Die Fächer Lerngruppe.
	 */
	_menge_fa : ArrayList<StundenplanblockungManagerFach | null> = new ArrayList();

	/**
	 * Die Räume Lerngruppe.
	 */
	_menge_ra : ArrayList<StundenplanblockungManagerRaum | null> = new ArrayList();

	/**
	 * Die Kopplungen Lerngruppe.
	 */
	_menge_ko : ArrayList<StundenplanblockungManagerKopplung | null> = new ArrayList();


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
		if (this._menge_le.contains(pLe))
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
		if (this._menge_kl.contains(pKl))
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
		if (this._menge_fa.contains(pFa))
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
		if (!this._menge_le.contains(pLe))
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
		if (!this._menge_kl.contains(pKl))
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
		if (!this._menge_fa.contains(pFa))
			throw new NullPointerException("Lerngruppe " + this._id + " hat nicht Fach " + pFa._kuerzel + " (" + pFa._id + ")!")
		this._menge_fa.remove(pFa);
		pFa._menge_gr.remove(this);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLerngruppe'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppe(obj : unknown) : StundenplanblockungManagerLerngruppe {
	return obj as StundenplanblockungManagerLerngruppe;
}
