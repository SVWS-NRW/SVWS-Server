import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { ArrayMap } from '../../../core/adt/map/ArrayMap';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import type { JavaMap } from '../../../java/util/JavaMap';
import { HashSet } from '../../../java/util/HashSet';

export class GostFachwahlManager extends JavaObject {

	/**
	 * Die Liste mit den einzelnen Fachwahlen
	 */
	private readonly fachwahlen : ArrayList<GostFachwahl> = new ArrayList();

	/**
	 * Eine Map, mit einer Zuordnung der Schüler-IDs zu der FachID und der Kursart
	 */
	private readonly mapFachKursart : HashMap<number, ArrayMap<GostKursart, HashSet<number>>> = new HashMap();

	/**
	 * Eine Map, mit einer Zuordnung der Fachwahlen zu der FachID
	 */
	private readonly mapFach : HashMap<number, ArrayList<GostFachwahl>> = new HashMap();

	/**
	 * Eine Map, mit einer Zuordnung der Fachwahlen zu der Schüler-ID
	 */
	private readonly mapSchueler : HashMap<number, ArrayList<GostFachwahl>> = new HashMap();


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
			// empty method body
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param0 === null))) {
			const fachwahlen : List<GostFachwahl> = cast_java_util_List(__param0);
			for (const fw of fachwahlen)
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
		let fwFach : ArrayList<GostFachwahl> | null = this.mapFach.get(fachwahl.fachID);
		if (fwFach === null) {
			fwFach = new ArrayList();
			this.mapFach.put(fachwahl.fachID, fwFach);
		}
		fwFach.add(fachwahl);
		let fwSchueler : ArrayList<GostFachwahl> | null = this.mapSchueler.get(fachwahl.schuelerID);
		if (fwSchueler === null) {
			fwSchueler = new ArrayList();
			this.mapSchueler.put(fachwahl.schuelerID, fwSchueler);
		}
		fwSchueler.add(fachwahl);
		let mapKursart : ArrayMap<GostKursart, HashSet<number>> | null = this.mapFachKursart.get(fachwahl.fachID);
		if (mapKursart === null) {
			mapKursart = new ArrayMap(GostKursart.values());
			this.mapFachKursart.put(fachwahl.fachID, mapKursart);
		}
		const kursart : GostKursart | null = GostKursart.fromFachwahlOrException(fachwahl);
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
		const fwFach : ArrayList<GostFachwahl> | null = this.mapSchueler.get(idFach);
		return (fwFach === null) ? new ArrayList() : fwFach;
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
		const fwSchueler : ArrayList<GostFachwahl> | null = this.mapSchueler.get(idSchueler);
		return (fwSchueler === null) ? new ArrayList() : fwSchueler;
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
		const mapKursart : JavaMap<GostKursart, HashSet<number>> | null = this.mapFachKursart.get(idFach);
		if (mapKursart === null)
			return false;
		const schueler : HashSet<number> | null = mapKursart.get(kursart);
		if (schueler === null)
			return false;
		return schueler.contains(idSchueler);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostFachwahlManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostFachwahlManager(obj : unknown) : GostFachwahlManager {
	return obj as GostFachwahlManager;
}
