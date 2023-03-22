import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { GostFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { GostKursart, cast_de_nrw_schule_svws_core_types_gost_GostKursart } from '../../../core/types/gost/GostKursart';
import { HashSet, cast_java_util_HashSet } from '../../../java/util/HashSet';

export class GostFachwahlManager extends JavaObject {

	/**
	 * Die Liste mit den einzelnen Fachwahlen
	 */
	private readonly fachwahlen : Vector<GostFachwahl> = new Vector();

	/**
	 * Eine Map, mit einer Zuordnung der Schüler-IDs zu der FachID und der Kursart
	 */
	private readonly mapFachKursart : HashMap<number, HashMap<GostKursart, HashSet<number>>> = new HashMap();

	/**
	 * Eine Map, mit einer Zuordnung der Fachwahlen zu der FachID
	 */
	private readonly mapFach : HashMap<number, Vector<GostFachwahl>> = new HashMap();

	/**
	 * Eine Map, mit einer Zuordnung der Fachwahlen zu der Schüler-ID
	 */
	private readonly mapSchueler : HashMap<number, Vector<GostFachwahl>> = new HashMap();


	/**
	 * Erzeugt einen leeren Fachwahl-Manager
	 */
	public constructor();

	/**
	 * Erzeugt einen neuen Fachwahl-Manager mit den übergebenen Fachwahlen
	 * 
	 * @param fachwahlen   die Fachwahlen
	 */
	public constructor(fachwahlen : List<GostFachwahl>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : List<GostFachwahl>) {
		super();
		if ((typeof __param0 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.List'))) || (__param0 === null))) {
			let fachwahlen : List<GostFachwahl> = cast_java_util_List(__param0);
			for (let fw of fachwahlen) 
				this.add(fw);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Fügt eine weitere Fachwahl zu dem Manager hinzu
	 * 
	 * @param fachwahl   die hinzuzufügende Fachwahl
	 */
	public add(fachwahl : GostFachwahl | null) : void {
		if (fachwahl === null)
			return;
		this.fachwahlen.add(fachwahl);
		let fwFach : Vector<GostFachwahl> | null = this.mapFach.get(fachwahl.fachID);
		if (fwFach === null) {
			fwFach = new Vector();
			this.mapFach.put(fachwahl.fachID, fwFach);
		}
		fwFach.add(fachwahl);
		let fwSchueler : Vector<GostFachwahl> | null = this.mapSchueler.get(fachwahl.schuelerID);
		if (fwSchueler === null) {
			fwSchueler = new Vector();
			this.mapSchueler.put(fachwahl.schuelerID, fwSchueler);
		}
		fwSchueler.add(fachwahl);
		let mapKursart : HashMap<GostKursart, HashSet<number>> | null = this.mapFachKursart.get(fachwahl.fachID);
		if (mapKursart === null) {
			mapKursart = new HashMap();
			this.mapFachKursart.put(fachwahl.fachID, mapKursart);
		}
		let kursart : GostKursart | null = GostKursart.fromFachwahlOrException(fachwahl);
		let schueler : HashSet<number> | null = mapKursart.get(kursart);
		if (schueler === null) {
			schueler = new HashSet();
			mapKursart.put(kursart, schueler);
		}
		schueler.add(fachwahl.schuelerID);
	}

	/**
	 * Ermittelt die Fachwahlen zu der übergebenen Fach-ID.
	 * Sind keine Fachwahlen vorhanden, so wird ein leerer Vektor 
	 * zurückgegeben.
	 * 
	 * @param idFach   die ID des Faches
	 * 
	 * @return die Liste der Fachwahlen des Faches
	 */
	public getFachwahlen(idFach : number) : List<GostFachwahl> {
		let fwFach : Vector<GostFachwahl> | null = this.mapSchueler.get(idFach);
		return (fwFach === null) ? new Vector() : fwFach;
	}

	/**
	 * Ermittelt die Fachwahlen zu der übergebenen Schüler ID.
	 * Sind keine Fachwahlen vorhanden, so wird ein leerer Vektor zurückgegeben.
	 * 
	 * @param idSchueler   die ID des Schülers
	 * 
	 * @return die Liste der Fachwahlen des Schülers
	 */
	public getSchuelerFachwahlen(idSchueler : number) : List<GostFachwahl> {
		let fwSchueler : Vector<GostFachwahl> | null = this.mapSchueler.get(idSchueler);
		return (fwSchueler === null) ? new Vector() : fwSchueler;
	}

	/**
	 * Prüft, ob eine Fachwahl mit dem angegebenen Schüler, Fach und der angegebenen Kursart existiert.
	 *    
	 * @param idSchueler   die ID des Schülers
	 * @param idFach       die ID des Faches
	 * @param kursart      die Kursart der gymnasialen Oberstufe
	 * 
	 * @return true, falls die Fachwahl existiert und ansonsten false
	 */
	public hatFachwahl(idSchueler : number, idFach : number, kursart : GostKursart) : boolean {
		let mapKursart : HashMap<GostKursart, HashSet<number>> | null = this.mapFachKursart.get(idFach);
		if (mapKursart === null)
			return false;
		let schueler : HashSet<number> | null = mapKursart.get(kursart);
		if (schueler === null)
			return false;
		return schueler.contains(idSchueler);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.gost.GostFachwahlManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_gost_GostFachwahlManager(obj : unknown) : GostFachwahlManager {
	return obj as GostFachwahlManager;
}
