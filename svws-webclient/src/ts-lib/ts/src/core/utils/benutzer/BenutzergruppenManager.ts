import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { BenutzerKompetenz, cast_de_nrw_schule_svws_core_types_benutzer_BenutzerKompetenz } from '../../../core/types/benutzer/BenutzerKompetenz';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { BenutzergruppeDaten, cast_de_nrw_schule_svws_core_data_benutzer_BenutzergruppeDaten } from '../../../core/data/benutzer/BenutzergruppeDaten';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { IllegalArgumentException, cast_java_lang_IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { HashSet, cast_java_util_HashSet } from '../../../java/util/HashSet';

export class BenutzergruppenManager extends JavaObject {

	/**
	 * Die Daten der Benutzergruppe, die im Manager vorhanden sind. 
	 */
	private readonly _daten : BenutzergruppeDaten;

	/**
	 * Die Menge an Kompetenzen, die dieser Gruppe zugeordnet ist. 
	 */
	private readonly _setKompetenzen : HashSet<number> = new HashSet();


	/**
	 *
	 * Erstellt einen neuen Manager mit leeren Daten für eine Benutzergruppe.
	 * 
	 * @param id            die ID der Benutzergruppe
	 * @param bezeichnung   die Bezeichnung der Benutzergruppe
	 */
	public constructor(id : number, bezeichnung : string);

	/**
	 *
	 * Erstellt einen neuen Manager mit den Daten einer Benutzergruppe
	 * 
	 * @param pDaten          die Benutzergruppendaten
	 */
	public constructor(pDaten : BenutzergruppeDaten);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : BenutzergruppeDaten | number, __param1? : string) {
		super();
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string"))) {
			let id : number = __param0 as number;
			let bezeichnung : string = __param1;
			this._daten = new BenutzergruppeDaten();
			this._daten.id = id;
			this._daten.bezeichnung = bezeichnung;
			this._daten.istAdmin = false;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.benutzer.BenutzergruppeDaten')))) && (typeof __param1 === "undefined")) {
			let pDaten : BenutzergruppeDaten = cast_de_nrw_schule_svws_core_data_benutzer_BenutzergruppeDaten(__param0);
			this._daten = pDaten;
			for (let kID of pDaten.kompetenzen) {
				if (kID === null) 
					throw new NullPointerException("Fehlerhafte Daten: Die Liste der Kompetenzen darf keine Null-Werte enthalten.")
				if (this._setKompetenzen.contains(kID)) 
					throw new IllegalArgumentException("Die Kompetenz mit der ID " + kID! + " wurde mehrfach bei der Gruppe eingetragen.")
				this._setKompetenzen.add(kID);
			}
		} else throw new Error('invalid method overload');
	}

	/**
	 *
	 * Gibt die Benutzergruppen-Daten zurück.
	 * 
	 * @return die Benutzergruppen-Daten (siehe {@link BenutzergruppeDaten}) 
	 */
	public daten() : BenutzergruppeDaten {
		return this._daten;
	}

	/**
	 * Gibt die ID der Benutzergruppe zurück.
	 * 
	 * @return die ID der Benutzergruppe
	 */
	public getID() : number {
		return this._daten.id;
	}

	/**
	 * Gibt die Bezeichnung der Benutzergruppe zurück.
	 * 
	 * @return die Bezeichnung der Benutzergruppe
	 */
	public getBezeichnung() : string {
		return this._daten.bezeichnung;
	}

	/**
	 * Setzt die Bezeichnung der Benutzergruppe.
	 * 
	 * @param bezeichnung  die neue Bezeichnung der Benutzergruppe
	 */
	public setBezeichnung(bezeichnung : string) : void {
		if (JavaObject.equalsTranspiler("", (bezeichnung))) 
			throw new IllegalArgumentException("Die Bezeichnung einer Benutzergruppe darf nicht leer sein.")
		this._daten.bezeichnung = bezeichnung;
	}

	/**
	 * Setzt, ob es sich um eine administrative Gruppe handelt oder nicht
	 * 
	 * @param istAdmin   true, falls die Gruppe administrativ ist und ansonsten
	 */
	public setAdmin(istAdmin : boolean) : void {
		this._daten.istAdmin = istAdmin;
	}

	/**
	 * Gibt zurück, ob es sich um eine administrative Gruppe handelt oder nicht.
	 *  
	 * @return true, falls es sich um eine administrative Gruppe handelt und ansonsten false
	 */
	public istAdmin() : boolean {
		return this._daten.istAdmin;
	}

	/**
	 * Prüft, ob die Gruppe die angebene Kompetenz besitzt oder nicht.
	 * 
	 * @param kompetenz   die zu prüfende Kompetenz
	 * 
	 * @return true, falls die Gruppe die Kompetenz besitzt.
	 */
	public hatKompetenz(kompetenz : BenutzerKompetenz) : boolean {
		if (this._daten.istAdmin) 
			return true;
		return this._setKompetenzen.contains(kompetenz.daten.id);
	}

	/**
	 * Prüft, ob die Gruppe alle angebenen Kompetenzen besitzt oder nicht.
	 * 
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 * 
	 * @return true, falls die Gruppe die Kompetenzen besitzt.
	 */
	public hatKompetenzen(kompetenzen : List<BenutzerKompetenz>) : boolean {
		if (this._daten.istAdmin) 
			return true;
		for (let kompetenz of kompetenzen) 
			if (!this._setKompetenzen.contains(kompetenz.daten.id)) 
				return false;
		return true;
	}

	/**
	 * Prüft, ob die Gruppe mindestens eine der angebenen Kompetenzen besitzt oder nicht.
	 * 
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 * 
	 * @return true, falls die Gruppe mindestens eine der Kompetenzen besitzt.
	 */
	public hatKompetenzenMindestensEine(kompetenzen : List<BenutzerKompetenz>) : boolean {
		if (this._daten.istAdmin) 
			return true;
		for (let kompetenz of kompetenzen) 
			if (this._setKompetenzen.contains(kompetenz.daten.id)) 
				return true;
		return false;
	}

	/**
	 * Fügt die übergebene Kompetenz zu der Gruppe hinzu.
	 * 
	 * @param kompetenz   die Kompetenz, die hinzugefügt wird
	 * 
	 * @throws IllegalArgumentException   wenn die Gruppe die Kompetenz bereits enthält 
	 */
	public addKompetenz(kompetenz : BenutzerKompetenz | null) : void {
		if (kompetenz === null) 
			throw new NullPointerException("Die übergenene Kompetenz darf nicht null sein.")
		if (this._setKompetenzen.contains(kompetenz.daten.id)) 
			throw new IllegalArgumentException("Die Kompetenz mit der ID " + kompetenz.daten.id + " wurde bereits zuvor zu der Gruppe hinzugefügt.")
		this._daten.kompetenzen.add(kompetenz.daten.id);
		this._setKompetenzen.add(kompetenz.daten.id);
	}

	/**
	 * Entfernt die übergebene Kompetenz aus der Gruppe.
	 * 
	 * @param kompetenz   die Kompetenz, die entfernt wird
	 * 
	 * @throws IllegalArgumentException   wenn die Gruppe die Kompetenz nicht enthält 
	 */
	public removeKompetenz(kompetenz : BenutzerKompetenz) : void {
		if (!this._setKompetenzen.contains(kompetenz.daten.id)) 
			throw new IllegalArgumentException("Die Kompetenz mit der ID " + kompetenz.daten.id + " ist in der Gruppe nicht vorhanden.")
		this._daten.kompetenzen.removeElement(kompetenz.daten.id);
		this._setKompetenzen.remove(kompetenz.daten.id);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.benutzer.BenutzergruppenManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_benutzer_BenutzergruppenManager(obj : unknown) : BenutzergruppenManager {
	return obj as BenutzergruppenManager;
}
