import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisSchiene } from '../../../core/data/gost/GostBlockungsergebnisSchiene';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../core/data/gost/GostFach';
import { GostBlockungsdatenManager, cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsdatenManager } from '../../../core/utils/gost/GostBlockungsdatenManager';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { GostBlockungsergebnisSchuelerzuordnung, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisSchuelerzuordnung } from '../../../core/data/gost/GostBlockungsergebnisSchuelerzuordnung';
import { GostBlockungsergebnisKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';
import { GostKursart, cast_de_nrw_schule_svws_core_types_gost_GostKursart } from '../../../core/types/gost/GostKursart';
import { GostBlockungsergebnis, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { Schueler, cast_de_nrw_schule_svws_core_data_schueler_Schueler } from '../../../core/data/schueler/Schueler';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { GostBlockungSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { HashSet, cast_java_util_HashSet } from '../../../java/util/HashSet';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';

export class GostBlockungsergebnisManager extends JavaObject {

	private readonly datenManager : GostBlockungsdatenManager;

	private readonly ergebnis : GostBlockungsergebnis;

	private readonly mapSchuelerKursZuordnungen : HashMap<Number, HashSet<GostBlockungsergebnisKurs>> = new HashMap();

	private readonly mapKursSchuelerZuordnungen : HashMap<Number, GostBlockungsergebnisKurs> = new HashMap();

	private readonly mapKursSchuelerZuordnungenFuerFach : HashMap<Number, Vector<GostBlockungsergebnisKurs>> = new HashMap();

	private readonly mapSchienenKursZuordnungen : HashMap<Number, GostBlockungsergebnisSchiene> = new HashMap();

	private readonly mapKursSchienenZuordnungen : HashMap<Number, GostBlockungsergebnisSchiene> = new HashMap();

	private readonly mapKursSchuelermenge : HashMap<Number, HashSet<Number>> = new HashMap();

	private readonly mapSchueler : HashMap<Number, Schueler> = new HashMap();

	private readonly mapSchuelerFachKursZuordnungen : HashMap<Number, HashMap<Number, GostBlockungsergebnisKurs> | null> = new HashMap();

	private readonly mapFaecher : HashMap<Number, GostFach> = new HashMap();

	private readonly mapSchienen : HashMap<Number, GostBlockungSchiene> = new HashMap();

	private readonly mapKurse : HashMap<Number, GostBlockungKurs> = new HashMap();


	/**
	 * Erstellt einen neuen Manager mit dem übergebenen Daten und erstellt ein neuen
	 * Objekt vom Typ {@link GostBlockungsergebnis}. Die Kurs-Schienen und Kurs-Schülerzuordnung 
	 * bei dem Blockungsergebnis ist leer.
	 * 
	 * @param datenManager   der Daten-Manager für die grundlegenden Definitionen der Blockung  
	 * @param id             die ID des Zwischenergebnis 
	 * @param blockungID     die ID der zugehörigen Blockung
	 * @param schueler       die Liste der Schüler 
	 */
	public constructor(datenManager : GostBlockungsdatenManager, id : number, blockungID : number, schueler : List<Schueler>);

	/**
	 * Erstellt einen neuen Manager mit den Daten aus dem übergebenen Ergebnis
	 * 
	 * @param datenManager   der Daten-Manager für die grundlegenden Definitionen der Blockung  
	 * @param ergebnis     die Daten des Blockungsergebnis 
	 * @param schueler     die Liste der Schüler 
	 */
	public constructor(datenManager : GostBlockungsdatenManager, ergebnis : GostBlockungsergebnis, schueler : List<Schueler>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : GostBlockungsdatenManager, __param1 : GostBlockungsergebnis | number, __param2 : List<Schueler> | number, __param3? : List<Schueler>) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager')))) && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && ((typeof __param2 !== "undefined") && typeof __param2 === "number") && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('java.util.List'))) || (__param3 === null))) {
			let datenManager : GostBlockungsdatenManager = cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsdatenManager(__param0);
			let id : number = __param1 as number;
			let blockungID : number = __param2 as number;
			let schueler : List<Schueler> = cast_java_util_List(__param3);
			this.datenManager = datenManager;
			this.ergebnis = new GostBlockungsergebnis();
			this.ergebnis.id = id;
			this.ergebnis.blockungID = blockungID;
			this.ergebnis.name = datenManager.daten().name;
			this.ergebnis.gostHalbjahr = datenManager.daten().gostHalbjahr;
			this.initSchueler(schueler);
			this.initFaecher(datenManager.faecherManager().toVector());
			this.initSchienen(datenManager.daten().schienen);
			this.initKurse(datenManager.daten().kurse);
			this.update();
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostBlockungsergebnis')))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && (typeof __param3 === "undefined")) {
			let datenManager : GostBlockungsdatenManager = cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsdatenManager(__param0);
			let ergebnis : GostBlockungsergebnis = cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnis(__param1);
			let schueler : List<Schueler> = cast_java_util_List(__param2);
			this.datenManager = datenManager;
			this.ergebnis = ergebnis;
			this.initSchueler(schueler);
			this.initFaecher(datenManager.faecherManager().toVector());
			this.initSchienen(datenManager.daten().schienen);
			for (let schiene of ergebnis.schienen) {
				this.mapSchienenKursZuordnungen.put(schiene.id, schiene);
				for (let kurs of schiene.kurse) {
					this.mapKursSchienenZuordnungen.put(kurs.id, schiene);
					this.mapKursSchuelerZuordnungen.put(kurs.id, kurs);
					let fachKurse : Vector<GostBlockungsergebnisKurs> | null = this.mapKursSchuelerZuordnungenFuerFach.get(kurs.fachID);
					if (fachKurse !== null) 
						fachKurse.add(kurs);
				}
			}
			this.initKurse(datenManager.daten().kurse);
			this.update();
		} else throw new Error('invalid method overload');
	}

	/**
	 * Initialisiert die Map für die Schüler, um einen schnellen Zugriff auf deren Daten zu ermöglichen.
	 * 
	 * @param schueler   die Liste mit der Definition der Schüler
	 */
	private initSchueler(schueler : List<Schueler>) : void {
		for (let s of schueler) {
			this.mapSchueler.put(s.id, s);
			this.mapSchuelerKursZuordnungen.put(s.id, new HashSet());
			this.mapSchuelerFachKursZuordnungen.put(s.id, new HashMap());
		}
	}

	/**
	 * Initialisiert die Map für die Fächer, um einen schnellen Zugriff auf deren Daten zu ermöglichen.
	 * 
	 * @param faecher   die Liste mit der Definition der Fächer
	 */
	private initFaecher(faecher : List<GostFach>) : void {
		for (let fach of faecher) {
			this.mapFaecher.put(fach.id, fach);
			this.mapKursSchuelerZuordnungenFuerFach.put(fach.id, new Vector());
		}
	}

	/**
	 * Initialisiert die Map für die Kurse, um einen schnellen Zugriff auf dessen Daten zu ermöglichen.
	 * 
	 * @param kurse   die Liste mit der Definition der Kurse der Blockung
	 */
	private initKurse(kurse : List<GostBlockungKurs>) : void {
		for (let kurs of kurse) {
			this.mapKurse.put(kurs.id, kurs);
			let erg : GostBlockungsergebnisKurs | null = this.mapKursSchuelerZuordnungen.get(kurs.id);
			if (erg === null) {
				let fach : GostFach = this.getFach(kurs.fach_id);
				erg = new GostBlockungsergebnisKurs();
				erg.id = kurs.id;
				erg.schienenID = null;
				erg.fachID = kurs.fach_id;
				erg.kursart = GostKursart.fromID(kurs.kursart).kuerzel;
				erg.name = fach.kuerzelAnzeige + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer;
				if (!JavaObject.equalsTranspiler("", (kurs.suffix))) 
					erg.name += "-" + kurs.suffix;
				erg.anzahlKollisionen = 0;
				this.mapKursSchuelerZuordnungen.put(kurs.id, erg);
				let fachKurse : Vector<GostBlockungsergebnisKurs> | null = this.mapKursSchuelerZuordnungenFuerFach.get(kurs.fach_id);
				if (fachKurse !== null) 
					fachKurse.add(erg);
			}
			let schuelerIDs : HashSet<Number> = new HashSet();
			for (let schueler of erg.schueler) {
				schuelerIDs.add(schueler.id);
				let schuelerKursZuordnung : HashSet<GostBlockungsergebnisKurs> = this.getSchuelerKursZuordnung(schueler.id);
				schuelerKursZuordnung.add(erg);
				let sfk : HashMap<Number, GostBlockungsergebnisKurs> | null = this.mapSchuelerFachKursZuordnungen.get(schueler.id);
				if (sfk !== null) 
					sfk.put(erg.fachID, erg);
			}
			this.mapKursSchuelermenge.put(kurs.id, schuelerIDs);
		}
	}

	/**
	 * Initialisiert die Map für die Schienen, um einen schnellen Zugriff auf deren Daten zu ermöglichen.
	 * 
	 * @param schienen   die Liste mit der Definition der Schienen der Blockung
	 */
	private initSchienen(schienen : List<GostBlockungSchiene>) : void {
		let doAdd : boolean = (this.ergebnis.schienen.size() === 0);
		for (let schiene of schienen) {
			this.mapSchienen.put(schiene.id, schiene);
			if (doAdd) {
				let zuordnungen : GostBlockungsergebnisSchiene = new GostBlockungsergebnisSchiene();
				zuordnungen.id = schiene.id;
				zuordnungen.name = schiene.bezeichnung;
				zuordnungen.anzahlKollisionen = 0;
				this.ergebnis.schienen.add(zuordnungen);
			}
		}
		for (let zuordnungen of this.ergebnis.schienen) 
			this.mapSchienenKursZuordnungen.put(zuordnungen.id, zuordnungen);
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. Erzeugt eine NullPointerException
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Schülers
	 * 
	 * @return die Daten zu dem Schüler der Blockung
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public getSchueler(id : number) : Schueler {
		let schueler : Schueler | null = this.mapSchueler.get(id);
		if (schueler === null) 
			throw new NullPointerException("ID des Schülers ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.")
		return schueler;
	}

	/**
	 * Ermittelt den Kurs für die angegebene ID. Erzeugt eine NullPointerException
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return die Daten zu dem Kurs der Blockung
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public getKurs(id : number) : GostBlockungKurs {
		let kurs : GostBlockungKurs | null = this.mapKurse.get(id);
		if (kurs === null) 
			throw new NullPointerException("ID des Kurses ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.")
		return kurs;
	}

	/**
	 * Ermittelt das Fach für die angegebene ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Faches
	 * 
	 * @return die Daten zu dem Fach
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public getFach(id : number) : GostFach {
		let fach : GostFach | null = this.mapFaecher.get(id);
		if (fach === null) 
			throw new NullPointerException("ID des Faches ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.")
		return fach;
	}

	/**
	 * Ermittelt die Schiene für die angegebene ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID der Schiene
	 * 
	 * @return die Daten zu der Schiene
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public getSchiene(id : number) : GostBlockungSchiene {
		let schiene : GostBlockungSchiene | null = this.mapSchienen.get(id);
		if (schiene === null) 
			throw new NullPointerException("ID der Schiene ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.")
		return schiene;
	}

	/**
	 * Gibt die Gesamtzahl der Kollisionen aggregiert über alle Schienen zurück.
	 *  
	 * @return die Anzahl der Kollisionen
	 */
	public getAnzahlKollisionen() : number {
		let anzahlKollisionen : number = 0;
		for (let schiene of this.mapSchienenKursZuordnungen.values()) 
			anzahlKollisionen += schiene.anzahlKollisionen;
		return anzahlKollisionen;
	}

	/**
	 * Gibt die Anzahl der Kollisionen in einer Schiene mit der übergebenen ID zurück.
	 *  
	 * @param id   die ID der Schiene
	 * 
	 * @return die Anzahl der Kollisionen in der Schiene
	 */
	public getAnzahlKollisionenSchiene(id : number) : number {
		let schiene : GostBlockungsergebnisSchiene | null = this.mapSchienenKursZuordnungen.get(id);
		if (schiene === null) 
			return 0;
		return schiene.anzahlKollisionen;
	}

	/**
	 * Gibt die Anzahl der Schüler in einer Schiene mit der übergebenen ID zurück.
	 *  
	 * @param id   die ID der Schiene
	 * 
	 * @return die Anzahl der Schüler in der Schiene
	 */
	public getAnzahlSchuelerSchiene(id : number) : number {
		let schiene : GostBlockungsergebnisSchiene | null = this.mapSchienenKursZuordnungen.get(id);
		if (schiene === null) 
			return 0;
		let result : number = 0;
		for (let kurs of schiene.kurse) 
			result += kurs.schueler.size();
		return result;
	}

	/**
	 * Ermittelt die Schienen-Kurs-Zuordnungen für die Schiene mit der angegebenen ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID der Schiene
	 * 
	 * @return die Schienen-Kurs-Zuordnungen zu der Schiene
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public getSchienenKursZuordnung(id : number) : GostBlockungsergebnisSchiene {
		let zuordnungen : GostBlockungsergebnisSchiene | null = this.mapSchienenKursZuordnungen.get(id);
		if (zuordnungen === null) 
			throw new NullPointerException("ID der Schiene ist für die Zuordnungen nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.")
		return zuordnungen;
	}

	/**
	 * Ermittelt die Schienen-Zuordnung des Kurses mit der angegebenen ID. Liegt keine Zuordnung vor,
	 * so wird null zurückgegeben.
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return die Kurs-Schienen-Zuordnungen der Schiene, zu welcher der Kurs zugeordnet ist
	 */
	public getKursSchienenZuordnung(id : number) : GostBlockungsergebnisSchiene | null {
		return this.mapKursSchienenZuordnungen.get(id);
	}

	/**
	 * Ermittelt die Kurs-Schüler-Zuordnungen für den Kurs mit der angegebenen ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return die Kurs-Schüler-Zuordnungen des Kurses
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public getKursSchuelerZuordnung(id : number) : GostBlockungsergebnisKurs {
		let zuordnungen : GostBlockungsergebnisKurs | null = this.mapKursSchuelerZuordnungen.get(id);
		if (zuordnungen === null) 
			throw new NullPointerException("ID des Kurses ist für die Zuordnungen nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.")
		return zuordnungen;
	}

	/**
	 * Ermittelt die Kurs-Schüler-Zuordnungen für die Kurse mit der angegebenen Fach-ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Faches
	 * 
	 * @return die Kurs-Schüler-Zuordnungen der Kurse mit der angegebenen Fach-ID
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public getKursSchuelerZuordnungenFuerFach(id : number) : Vector<GostBlockungsergebnisKurs> {
		let zuordnungen : Vector<GostBlockungsergebnisKurs> | null = this.mapKursSchuelerZuordnungenFuerFach.get(id);
		if (zuordnungen === null) 
			throw new NullPointerException("ID des Faches ist für die Zuordnungen nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.")
		return zuordnungen;
	}

	/**
	 * Ermittelt die Kurs-Schüler-Zuordnungen für den Schüler und das Fach mit den angegebenen IDs.
	 * 
	 * @param idSchueler   die ID des Schülers
	 * @param idFach       die ID des Faches
	 * 
	 * @return die Kurs-Schüler-Zuordnungen des Schülers und des Faches oder null, falls die Kombination nicht vorliegt
	 */
	public getKursSchuelerZuordnungFuerSchuelerUndFach(idSchueler : number, idFach : number) : GostBlockungsergebnisKurs | null {
		let sfk : HashMap<Number, GostBlockungsergebnisKurs> | null = this.mapSchuelerFachKursZuordnungen.get(idSchueler);
		if (sfk === null) 
			return null;
		return sfk.get(idFach);
	}

	/**
	 * Ermittelt die Schüler-Kurs-Zuordnungen für den Schüler mit der angegebenen ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Schülers
	 * 
	 * @return die Schüler-Kurs-Zuordnungen des Schülers
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public getSchuelerKursZuordnung(id : number) : HashSet<GostBlockungsergebnisKurs> {
		let zuordnungen : HashSet<GostBlockungsergebnisKurs> | null = this.mapSchuelerKursZuordnungen.get(id);
		if (zuordnungen === null) 
			throw new NullPointerException("ID des Schülers ist für die Zuordnungen nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.")
		return zuordnungen;
	}

	/**
	 * Ermittelt die Kurs-Schülermenge für den Kurs mit der angegebenen ID. Erzeugt eine NullPointerException 
	 * im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return die Kurs-Schülermenge des Kurses
	 * 
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public getKursSchuelermenge(id : number) : HashSet<Number> {
		let zuordnungen : HashSet<Number> | null = this.mapKursSchuelermenge.get(id);
		if (zuordnungen === null) 
			throw new NullPointerException("ID des Kurses ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.")
		return zuordnungen;
	}

	/**
	 * Liefert das Blockungsergebnis zurück.
	 * 
	 * @return das Blockungsergebnis
	 */
	public getErgebnis() : GostBlockungsergebnis {
		return this.ergebnis;
	}

	/**
	 * Aktualisiert für alle Schienen-Kurs-Zuordnungen die Anzahl der Kollisionen
	 * und die Kollisionseinträge bei den Schülern
	 */
	private update() : void {
		for (let schienenKursZuordnung of this.ergebnis.schienen) 
			this.updateSchiene(schienenKursZuordnung);
	}

	/**
	 * Aktualisiert für die übergebene Schienen-Kurs-Zuordnung die Anzahl der Kollisionen
	 * und die Kollisionseinträge bei den Schülern
	 * 
	 * @param schienenKursZuordnung   die Schienen-Kurs-Zuordnung
	 */
	private updateSchiene(schienenKursZuordnung : GostBlockungsergebnisSchiene) : void {
		schienenKursZuordnung.anzahlKollisionen = 0;
		if (schienenKursZuordnung.kurse.size() < 1) 
			return;
		for (let i : number = 0; i < schienenKursZuordnung.kurse.size(); i++){
			let andereSchueler : HashSet<Number> = new HashSet();
			for (let j : number = 0; j < schienenKursZuordnung.kurse.size(); j++){
				if (i === j) 
					continue;
				let andererKurs : GostBlockungsergebnisKurs = schienenKursZuordnung.kurse.get(j);
				andereSchueler.addAll(this.getKursSchuelermenge(andererKurs.id));
			}
			let kurs : GostBlockungsergebnisKurs = schienenKursZuordnung.kurse.get(i);
			let dieseSchueler : HashSet<Number> = this.getKursSchuelermenge(kurs.id);
			let kollisionenSchueler : HashSet<Number> = new HashSet();
			kollisionenSchueler.addAll(dieseSchueler);
			kollisionenSchueler.retainAll(andereSchueler);
			kurs.anzahlKollisionen = kollisionenSchueler.size();
			schienenKursZuordnung.anzahlKollisionen += kurs.anzahlKollisionen;
			for (let schueler of kurs.schueler) 
				schueler.hatKollisionen = kollisionenSchueler.contains(schueler.id);
		}
	}

	/**
	 * Ordnet einen Kurs einer Schiene zu. Ist die ID der Schiene null,
	 * so wird eine vorherige Zuordnung aufgehoben.
	 *  
	 * @param idKurs      die ID des Kurses
	 * @param idSchiene   die ID der Schiene
	 *  
	 * @return true, falls der Kurs der Schiene zugeordnet wurde 
	 */
	public assignKursSchiene(idKurs : number, idSchiene : Number | null) : boolean {
		let kursSchienenZuordnung : GostBlockungsergebnisSchiene | null = this.getKursSchienenZuordnung(idKurs);
		if ((kursSchienenZuordnung !== null) && (kursSchienenZuordnung.id === idSchiene)) 
			return false;
		let kursSchuelerZuordnung : GostBlockungsergebnisKurs = this.getKursSchuelerZuordnung(idKurs);
		if (kursSchienenZuordnung !== null) {
			kursSchienenZuordnung.kurse.remove(kursSchuelerZuordnung);
			if (kursSchuelerZuordnung.anzahlKollisionen > 0) {
				this.updateSchiene(kursSchienenZuordnung);
			}
		}
		kursSchuelerZuordnung.schienenID = idSchiene;
		if (idSchiene !== null) {
			let schienenKursZuordnung : GostBlockungsergebnisSchiene = this.getSchienenKursZuordnung(idSchiene.valueOf());
			schienenKursZuordnung.kurse.add(kursSchuelerZuordnung);
			this.updateSchiene(schienenKursZuordnung);
		}
		return true;
	}

	/**
	 * Ordnet einen Schüler einem Kurs zu.
	 * 
	 * @param idSchueler   die ID des Schüler
	 * @param idKurs       die ID des Kurses
	 * @param undo         normally false, but true if the assignment should be removed
	 * 
	 * @return true, falls der Schüler dem Kurs zugeordnet wurde 
	 */
	public assignSchuelerKurs(idSchueler : number, idKurs : number, undo : boolean) : boolean {
		let schueler : Schueler = this.getSchueler(idSchueler);
		let kursSchuelerZuordnung : GostBlockungsergebnisKurs = this.getKursSchuelerZuordnung(idKurs);
		let schuelerMenge : HashSet<Number | null> = this.getKursSchuelermenge(idKurs);
		let isAssigned : boolean = schuelerMenge.contains(idSchueler);
		if (undo) {
			if (!isAssigned) 
				return false;
			schuelerMenge.remove(idSchueler);
			for (let i : number = 0; i < kursSchuelerZuordnung.schueler.size(); i++){
				let zuordnung : GostBlockungsergebnisSchuelerzuordnung = kursSchuelerZuordnung.schueler.get(i);
				if (zuordnung.id === idSchueler) {
					kursSchuelerZuordnung.schueler.remove(i);
					break;
				}
			}
			let schuelerKursZuordnungen : HashSet<GostBlockungsergebnisKurs> = this.getSchuelerKursZuordnung(idSchueler);
			schuelerKursZuordnungen.remove(kursSchuelerZuordnung);
		} else {
			if (isAssigned) 
				return false;
			schuelerMenge.add(idSchueler);
			let zuordnung : GostBlockungsergebnisSchuelerzuordnung = new GostBlockungsergebnisSchuelerzuordnung();
			zuordnung.id = schueler.id;
			zuordnung.name = schueler.nachname + ", " + schueler.vorname;
			kursSchuelerZuordnung.schueler.add(zuordnung);
			let schuelerKursZuordnungen : HashSet<GostBlockungsergebnisKurs> = this.getSchuelerKursZuordnung(idSchueler);
			schuelerKursZuordnungen.add(kursSchuelerZuordnung);
		}
		if (kursSchuelerZuordnung.schienenID !== null) {
			let idSchiene : number = kursSchuelerZuordnung.schienenID.valueOf();
			this.updateSchiene(this.getSchienenKursZuordnung(idSchiene));
		}
		return true;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.gost.GostBlockungsergebnisManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsergebnisManager(obj : unknown) : GostBlockungsergebnisManager {
	return obj as GostBlockungsergebnisManager;
}
