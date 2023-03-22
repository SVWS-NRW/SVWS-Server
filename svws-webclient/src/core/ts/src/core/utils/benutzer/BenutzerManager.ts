import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { BenutzerKompetenz, cast_de_nrw_schule_svws_core_types_benutzer_BenutzerKompetenz } from '../../../core/types/benutzer/BenutzerKompetenz';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { BenutzergruppeDaten, cast_de_nrw_schule_svws_core_data_benutzer_BenutzergruppeDaten } from '../../../core/data/benutzer/BenutzergruppeDaten';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { BenutzerDaten, cast_de_nrw_schule_svws_core_data_benutzer_BenutzerDaten } from '../../../core/data/benutzer/BenutzerDaten';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { IllegalArgumentException, cast_java_lang_IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { HashSet, cast_java_util_HashSet } from '../../../java/util/HashSet';

export class BenutzerManager extends JavaObject {

	/**
	 * Die Daten des Benutzers, die im Manager vorhanden sind.
	 */
	private readonly _daten : BenutzerDaten;

	/**
	 * Eine Map zum schnellen Zugriff auf die Benutzergruppendaten
	 */
	private readonly _mapGruppen : HashMap<number, BenutzergruppeDaten> = new HashMap();

	/**
	 * Die Ids der Benutzergruppen des Benutzers
	 */
	private readonly _setGruppenIDs : HashSet<number> = new HashSet();

	/**
	 *  Eine Map, welche zu einer Kompetenz eine Liste zuordnet, welche alle
	 *  Benutzer-Gruppen beinhaltet, von denen
	 *  der Benutzer die Kompetenz erhalten hat.
	 */
	private readonly _mapKompetenzenVonGruppe : HashMap<BenutzerKompetenz, Vector<BenutzergruppeDaten>> = new HashMap();

	/**
	 * Die Menge an Kompetenzen, die diesem Benutzer direkt zugeordnet ist.
	 */
	private readonly _setKompetenzen : HashSet<BenutzerKompetenz> = new HashSet();

	/**
	 *  Die Menge an Kompetenzen, die diesem Benutzer entweder direkt oder über
	 *  mindestens eine Gruppe zugeordnet ist.
	 */
	private readonly _setKompetenzenAlle : HashSet<BenutzerKompetenz> = new HashSet();


	/**
	 * Erstellt einen neuen Manager mit leeren Daten für einen Benutzer
	 *
	 * @param id die ID des Benutzers
	 *
	 */
	public constructor(id : number);

	/**
	 * Erstellt einen neuen Manager mit den Daten eines Benutzers
	 *
	 * @param pDaten die BenutzerDaten
	 */
	public constructor(pDaten : BenutzerDaten);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : BenutzerDaten | number) {
		super();
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			let id : number = __param0 as number;
			this.init();
			this._daten = new BenutzerDaten();
			this._daten.id = id;
			this._daten.istAdmin = false;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.benutzer.BenutzerDaten'))))) {
			let pDaten : BenutzerDaten = cast_de_nrw_schule_svws_core_data_benutzer_BenutzerDaten(__param0);
			this.init();
			this._daten = pDaten;
			for (let kID of pDaten.kompetenzen) {
				if (kID === null)
					throw new NullPointerException("Fehlerhafte Daten: Die Liste der Kompetenzen darf keine Null-Werte enthalten.")
				let komp : BenutzerKompetenz | null = BenutzerKompetenz.getByID(kID!);
				if (komp === null)
					throw new NullPointerException("Fehlerhafte Daten: Die Kompetenz mit der ID " + kID! + " existiert nicht.")
				if (this._setKompetenzen.contains(komp))
					throw new IllegalArgumentException("Die Kompetenz mit der ID " + kID! + " wurde mehrfach bei der Gruppe eingetragen.")
				this._setKompetenzen.add(komp);
				this._setKompetenzenAlle.add(komp);
			}
			for (let bgd of this._daten.gruppen) 
				this.addGruppe(bgd);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Aktualisiere die lokalen Datenstrukturen - die über Gruppen zugeordneten  Kompetenzen
	 *
	 * @param bgd die Benutzergruppendaten
	 */
	private addGruppe(bgd : BenutzergruppeDaten | null) : void {
		if (bgd === null)
			return;
		this._mapGruppen.put(bgd.id, bgd);
		this._setGruppenIDs.add(bgd.id);
		for (let kid of bgd.kompetenzen) {
			let komp : BenutzerKompetenz | null = BenutzerKompetenz.getByID(kid!);
			if (komp === null)
				throw new NullPointerException("Fehlerhafte Daten: Die Kompetenz mit der ID " + kid! + " existiert nicht.")
			this._setKompetenzenAlle.add(komp);
			let gruppen : Vector<BenutzergruppeDaten> | null = this._mapKompetenzenVonGruppe.get(komp);
			if (gruppen === null)
				throw new NullPointerException("Vector existiert nicht, müsste aber zuvor initialisiert worden sein.")
			gruppen.add(bgd);
		}
	}

	/**
	 * Aktualisiere die lokalen Datenstrukturen - die über Gruppen zugeordneten  Kompetenzen
	 *
	 * @param bgd die Benutzergruppendaten
	 */
	private removeGruppe(bgd : BenutzergruppeDaten | null) : void {
		if (bgd === null)
			return;
		this._mapGruppen.remove(bgd.id);
		this._setGruppenIDs.remove(bgd.id);
		for (let kid of bgd.kompetenzen) {
			let komp : BenutzerKompetenz | null = BenutzerKompetenz.getByID(kid!);
			if (komp !== null) {
				let gruppen : Vector<BenutzergruppeDaten> | null = this._mapKompetenzenVonGruppe.get(komp);
				if (gruppen === null)
					throw new NullPointerException("Vector existiert nicht, müsste aber zuvor initialisiert worden sein.")
				for (let i : number = gruppen.size() - 1; i >= 0; i--)
					if (gruppen.elementAt(i).id === bgd.id)
						gruppen.removeElementAt(i);
				if (gruppen.isEmpty() && !this._setKompetenzen.contains(komp))
					this._setKompetenzenAlle.remove(komp);
			}
		}
	}

	/**
	 * Liefert true, wenn der Benutzer in einer adminstrativen Gruppe ist, sonst false.
	 *
	 * @return true, wenn der Benutzer in einer administrativen Gruppe ist.
	 */
	public istInAdminGruppe() : boolean {
		for (let bg of this._mapGruppen.values()) {
			if (bg.istAdmin)
				return true;
		}
		return false;
	}

	/**
	 * Initialisiert die lokalen Datenstrukturen.
	 */
	private init() : void {
		for (let p of BenutzerKompetenz.values()) 
			this._mapKompetenzenVonGruppe.put(p, new Vector());
	}

	/**
	 * Gibt für die übergebene Benutzerkompetenz eine Liste der
	 * Benutzergruppen-Daten
	 * zurück, welche dem Benutzer die Kompetenz zu geordnet haben.
	 *
	 * @param kompetenz die Benutzerkompetenz
	 *
	 * @return die Liste der Benutzergruppen-Daten
	 */
	public getGruppen(kompetenz : BenutzerKompetenz) : List<BenutzergruppeDaten> {
		let gruppen : Vector<BenutzergruppeDaten> | null = this._mapKompetenzenVonGruppe.get(kompetenz);
		if (gruppen === null)
			throw new NullPointerException("Die interne Datenstruktur _mapKompetenzenVonGruppe wurde nich korrekt initialisiert.")
		return gruppen;
	}

	/**
	 * Gibt die Benutzer-Daten zurück.
	 *
	 * @return die Benutzer-Daten (siehe {@link BenutzerDaten})
	 */
	public daten() : BenutzerDaten {
		return this._daten;
	}

	/**
	 * Gibt die ID des Benutzers zurück.
	 *
	 * @return die ID des Benutzers
	 */
	public getID() : number {
		return this._daten.id;
	}

	/**
	 * Gibt die BenutzerGruppen des Benutzers zurück.
	 *
	 * @return Gibt die BenutzerGruppen des Benutzers zurück.
	 */
	public getBenutzerGruppen() : List<BenutzergruppeDaten> {
		return this._daten.gruppen;
	}

	/**
	 * Gibt den Anmeldenamen des Benutzers zurück.
	 *
	 * @return Gibt den Anmeldenamen des Benutzers zurück.
	 */
	public getAnmeldename() : string {
		return this._daten.name;
	}

	/**
	 * Setzt den Anmeldenamen des Benutzers.
	 *
	 * @param name der neue Anmeldename des Benutzers
	 */
	public setAnmeldename(name : string) : void {
		if (JavaObject.equalsTranspiler("", (name)))
			throw new IllegalArgumentException("Der Anmeldename eines Benutzers darf nicht leer sein.")
		this._daten.name = name;
	}

	/**
	 * Gibt denAnzeigenamen des Benutzers zurück.
	 *
	 * @return Gibt den Anzeigenamen des Benutzers zurück.
	 */
	public getAnzeigename() : string {
		return this._daten.anzeigename;
	}

	/**
	 * Setzt den Anzeigenamen des Benutzers.
	 *
	 * @param name der neue Anzeigenamen des Benutzers
	 */
	public setAnzeigename(name : string) : void {
		if (JavaObject.equalsTranspiler("", (name)))
			throw new IllegalArgumentException("Der Anmeldename eines Benutzers darf nicht leer sein.")
		this._daten.anzeigename = name;
	}

	/**
	 * Setzt, ob es sich um einen administrativen Benutzer handelt oder nicht
	 *
	 * @param istAdmin true, falls der Benutzer administrativ ist und ansonsten
	 *                 false
	 */
	public setAdmin(istAdmin : boolean) : void {
		this._daten.istAdmin = istAdmin;
	}

	/**
	 * Gibt zurück, ob es sich um einen administrativen Benutzer handelt oder nicht.
	 *
	 * @return true, falls es sich um einen administrativen Benutzer handelt und
	 *         ansonsten false
	 */
	public istAdmin() : boolean {
		return this._daten.istAdmin || this.istInAdminGruppe();
	}

	/**
	 * Prüft, ob der Benutzer die angebene Kompetenz direkt oder über eine Gruppe
	 * besitzt oder nicht.
	 *
	 * @param kompetenz die zu prüfende Kompetenz
	 *
	 * @return true, falls der Benutzer die Kompetenz besitzt.
	 */
	public hatKompetenz(kompetenz : BenutzerKompetenz) : boolean {
		if (this._daten.istAdmin || this.istInAdminGruppe())
			return true;
		return this._setKompetenzenAlle.contains(kompetenz);
	}

	/**
	 * Prüft, ob der Benutzer alle angebenen Kompetenzen direkt oder über eine
	 * Gruppe
	 * besitzt oder nicht.
	 *
	 * @param kompetenzen die zu prüfenden Kompetenzen
	 *
	 * @return true, falls der Benutzer die Kompetenzen besitzt.
	 */
	public hatKompetenzen(kompetenzen : List<BenutzerKompetenz>) : boolean {
		if (this._daten.istAdmin)
			return true;
		for (let kompetenz of kompetenzen) 
			if (!this._setKompetenzenAlle.contains(kompetenz))
				return false;
		return true;
	}

	/**
	 * Prüft, ob der Benutzer mindestens eine der angebenen Kompetenzen direkt oder
	 * über eine Gruppe
	 * besitzt oder nicht.
	 *
	 * @param kompetenzen die zu prüfenden Kompetenzen
	 *
	 * @return true, falls der Benutzer mindestens eine der Kompetenzen besitzt.
	 */
	public hatKompetenzenMindestensEine(kompetenzen : List<BenutzerKompetenz>) : boolean {
		if (this._daten.istAdmin)
			return true;
		for (let kompetenz of kompetenzen) 
			if (this._setKompetenzenAlle.contains(kompetenz))
				return true;
		return false;
	}

	/**
	 * Fügt die übergebene Kompetenz direkt bei dem Benutzer hinzu.
	 *
	 * @param kompetenz die Kompetenz, die hinzugefügt wird
	 *
	 * @throws IllegalArgumentException wenn der Benutzer die Kompetenz bereits hat
	 */
	public addKompetenz(kompetenz : BenutzerKompetenz | null) : void {
		if (kompetenz === null)
			throw new NullPointerException("Die übergebene Kompetenz darf nicht null sein.")
		if (this._setKompetenzen.contains(kompetenz))
			throw new IllegalArgumentException("Die Kompetenz mit der ID " + kompetenz.daten.id + " wurde bereits zuvor zu dem Benutzer hinzugefügt.")
		this._daten.kompetenzen.add(kompetenz.daten.id);
		this._setKompetenzen.add(kompetenz);
		this._setKompetenzenAlle.add(kompetenz);
	}

	/**
	 * Entfernt die übergebene Kompetenz bei dem Benutzer, sofern diese Kompetenz
	 * ihm direkt
	 * zugeordnet wure. Sollte die Kompetenz zusätzlich über eine Gruppe zugeordnet
	 * sein,
	 * so bleibt diese Zuordnung erhalten.
	 *
	 * @param kompetenz die zu entfernende Kompetenz
	 *
	 * @throws IllegalArgumentException wenn der Benutzer die Kompetenz nicht hat
	 *                                  oder nur über eine Gruppe hat
	 */
	public removeKompetenz(kompetenz : BenutzerKompetenz) : void {
		if (!this._setKompetenzen.contains(kompetenz))
			throw new IllegalArgumentException("Die Kompetenz mit der ID " + kompetenz.daten.id + " ist nicht direkt beim Benutzer vorhanden.")
		this._daten.kompetenzen.removeElement(kompetenz.daten.id);
		this._setKompetenzen.remove(kompetenz);
		let gruppen : List<BenutzergruppeDaten> = this.getGruppen(kompetenz);
		if (gruppen.size() === 0)
			this._setKompetenzenAlle.remove(kompetenz);
	}

	/**
	 * Überprüft, ob der Benutzer in einer Grupper mit der id Mitglied ist.
	 *
	 * @param id  ID der Gruppe
	 *
	 * @return true, falls der Benutzer in der Gruppe ist.
	 */
	public IstInGruppe(id : number) : boolean {
		return this._setGruppenIDs.contains(id);
	}

	/**
	 * Liefert die Anzahl der Gruppen des Benutzers
	 *
	 *
	 * @return anzahl   der Gruppen
	 */
	public anzahlGruppen() : number {
		return this._setGruppenIDs.size();
	}

	/**
	 * Fügt den Benutzer in eine Gruppe ein
	 *
	 * @param bgd die Benutzergruppe
	 *
	 * @throws IllegalArgumentException wenn der Benutzer die Kompetenz bereits hat
	 */
	public addToGruppe(bgd : BenutzergruppeDaten) : void {
		if (bgd !== null) {
			this.addGruppe(bgd);
		} else
			throw new IllegalArgumentException("Der Benutzer ist bereits in der Gruppe ")
	}

	/**
	 * Entfernt den Benutzer aus der Gruppe
	 *
	 * @param bgd  dei Benutzergruppe
	 *
	 * @throws IllegalArgumentException wenn der Benutzer die Kompetenz bereits hat
	 */
	public removeFromGruppe(bgd : BenutzergruppeDaten) : void {
		this.removeGruppe(bgd);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.benutzer.BenutzerManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_benutzer_BenutzerManager(obj : unknown) : BenutzerManager {
	return obj as BenutzerManager;
}
