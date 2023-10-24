import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { BKLernfeld } from '../../../core/data/bk/BKLernfeld';
import { BKLehrplanKatalog } from '../../../core/data/bk/BKLehrplanKatalog';
import { ArrayList } from '../../../java/util/ArrayList';
import { BKLehrplan } from '../../../core/data/bk/BKLehrplan';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { Map3DUtils } from '../../../core/utils/Map3DUtils';
import { BKFBFach } from '../../../core/data/bk/BKFBFach';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { BKFachklassenSchluessel, cast_de_svws_nrw_core_data_bk_BKFachklassenSchluessel } from '../../../core/data/bk/BKFachklassenSchluessel';
import type { JavaMap } from '../../../java/util/JavaMap';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { UserNotificationException } from '../../../core/exceptions/UserNotificationException';
import { HashMap3D } from '../../../core/adt/map/HashMap3D';
import { HashSet } from '../../../java/util/HashSet';

export class BerufskollegLehrplanManager extends JavaObject {

	/**
	 * der Katalog
	 */
	private readonly _katalog : BKLehrplanKatalog;

	/**
	 * Die Version der Daten
	 */
	private readonly _version : number;

	/**
	 * Ein Vektor mit allen Katalog-Einträgen
	 */
	private readonly _values : ArrayList<BKLehrplan> = new ArrayList();

	/**
	 * Eine HashMap für den schnellen Zugriff auf ein Fach anhand des Kürzels
	 */
	private readonly _mapFachByKuerzel : HashMap<string, BKFBFach> = new HashMap();

	/**
	 * Eine HashMap für den Zugriff auf einen Lehrplan anhand der ID.
	 */
	private readonly _mapByID : HashMap<number, BKLehrplan> = new HashMap();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Lehrpläne anhand des Fachklassen-Schlüssels.
	 */
	private readonly _mapLehrplanByFachklasse : HashMap3D<number, string, number, List<BKLehrplan>> = new HashMap3D();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Fächer anhand des Fachklassen-Schlüssels.
	 */
	private readonly _mapFachByFachklasse : HashMap3D<number, string, string, HashSet<BKFBFach>> = new HashMap3D();


	/**
	 * Erstellt einen neuen Manager für den Katalog der berufsbezogenen Fächer im BK
	 *
	 * @param katalog   der Katalog der berufsbezogenen Fächer
	 */
	public constructor(katalog : BKLehrplanKatalog) {
		super();
		this._katalog = katalog;
		this._version = katalog.version;
		for (const eintrag of katalog.lehrplaene) {
			this._values.addAll(eintrag.historie);
			for (const lehrplan of eintrag.historie) {
				if (lehrplan.fachklasse.index as unknown !== eintrag.index as unknown || lehrplan.fachklasse.schluessel as unknown !== eintrag.schluessel as unknown)
					throw new DeveloperNotificationException("Fehlerhafter Katalog: Fachklasse in Historie mit ID '" + lehrplan.id + "' ungleich Fachklasse des Lehrplans")
				const alt : BKLehrplan | null = this._mapByID.put(lehrplan.id, lehrplan);
				if (alt !== null)
					throw new DeveloperNotificationException("Fehlerhafter Katalog: Doppelte ID '" + lehrplan.id)
				Map3DUtils.getOrCreateArrayList(this._mapLehrplanByFachklasse, eintrag.index, eintrag.schluessel, lehrplan.id).add(lehrplan);
				for (const fach of lehrplan.fbFaecher) {
					this._mapFachByKuerzel.put(fach.kuerzel, fach);
					Map3DUtils.getOrCreateHashSet(this._mapFachByFachklasse, eintrag.index, eintrag.schluessel, fach.kuerzel).add(fach);
				}
			}
		}
	}

	/**
	 * Gibt die Version der Katalog-Daten zurück.
	 *
	 * @return die Version
	 */
	public getVersion() : number {
		return this._version;
	}

	/**
	 * Gibt alle Katalog-Einträge zurück.
	 *
	 * @return ein Array mit allen Katalog-Einträgen
	 */
	public values() : Array<BKLehrplan | null> | null {
		return this._values.toArray(Array(0).fill(null));
	}

	/**
	 * Gibt die Lehrpläne für ein Schuljahr zurück
	 *
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne für das angegebene Schuljahr
	 */
	public getLehrplaeneBySchuljahr(schuljahr : number) : Array<BKLehrplan | null> | null {
		const lehrplaene : ArrayList<BKLehrplan> = new ArrayList();
		for (const lehrplan of this._values) {
			if (((lehrplan.gueltigVon === null) || (lehrplan.gueltigVon <= schuljahr)) && ((lehrplan.gueltigBis === null) || (lehrplan.gueltigBis >= schuljahr)))
				lehrplaene.add(lehrplan);
		}
		return lehrplaene.toArray(Array(0).fill(null));
	}

	/**
	 * Gibt die Lehrpläne eines Index für ein Schuljahr zurück
	 *
	 * @param index  	  der Schulgliederungs-Index des Teilkatalogs
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Index für das angegebene Schuljahr
	 */
	public getLehrplaeneByIndexSchuljahr(index : number, schuljahr : number) : Array<BKLehrplan | null> | null {
		const lehrplaene : ArrayList<BKLehrplan> = new ArrayList();
		const lehrplaeneOfIndex : List<List<BKLehrplan>> = this._mapLehrplanByFachklasse.getNonNullValuesOfMap2AsList(index);
		for (const list of lehrplaeneOfIndex) {
			for (const lehrplan of list) {
				if (((lehrplan.gueltigVon === null) || (lehrplan.gueltigVon <= schuljahr)) && ((lehrplan.gueltigBis === null) || (lehrplan.gueltigBis >= schuljahr)))
					lehrplaene.add(lehrplan);
			}
		}
		return lehrplaene.toArray(Array(0).fill(null));
	}

	/**
	 * Gibt die Lehrpläne eines Index der angegebenen Schulgliederung für ein Schuljahr zurück
	 *
	 * @param gliederung   die Schulgliederung
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Gliederungsindex für das angegebene Schuljahr
	 */
	public getLehrplaeneBySchulgliederungSchuljahr(gliederung : Schulgliederung, schuljahr : number) : Array<BKLehrplan | null> | null {
		if (gliederung.daten.bkIndex === null)
			throw new IllegalArgumentException("Die Schulgliederung " + gliederung.daten.kuerzel + " hat keinen Schulgliederungs-Index.")
		return this.getLehrplaeneByIndexSchuljahr(gliederung.daten.bkIndex, schuljahr);
	}

	/**
	 * Gibt die Lehrpläne eines Index für ein Schuljahr zurück, wobei auch die auslaufenden
	 * Lehrpläne mit ausgegeben werden.
	 *
	 * @param index  	  der Schulgliederungs-Index des Teilkatalogs
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Gliederungsindex für das angegebene Schuljahr
	 */
	public getLehrplaeneByIndexSchuljahrAll(index : number, schuljahr : number) : Array<BKLehrplan | null> | null {
		const lehrplaene : ArrayList<BKLehrplan> = new ArrayList();
		const lehrplaeneOfIndex : List<List<BKLehrplan>> = this._mapLehrplanByFachklasse.getNonNullValuesOfMap2AsList(index);
		for (const list of lehrplaeneOfIndex) {
			for (const lehrplan of list) {
				if (((lehrplan.gueltigVon === null) || (lehrplan.gueltigVon <= schuljahr)) && ((lehrplan.gueltigBis === null) || (lehrplan.gueltigBis + Math.trunc(lehrplan.dauer / 2) + 1 >= schuljahr)))
					lehrplaene.add(lehrplan);
			}
		}
		return lehrplaene.toArray(Array(0).fill(null));
	}

	/**
	 * Gibt die Lehrpläne eines Index der angegebenen Schulgliederung für ein Schuljahr zurück
	 *
	 * @param gliederung  die Schulgliederung
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Gliederungsindex für das angegebene Schuljahr
	 */
	public getLehrplaeneBySchulgliederungSchuljahrAll(gliederung : Schulgliederung, schuljahr : number) : Array<BKLehrplan | null> | null {
		if (gliederung.daten.bkIndex === null)
			throw new IllegalArgumentException("Die Schulgliederung " + gliederung.daten.kuerzel + " hat keinen Schulgliederungs-Index.")
		return this.getLehrplaeneByIndexSchuljahrAll(gliederung.daten.bkIndex, schuljahr);
	}

	/**
	 * Gibt den Lehrplan mit der gegebenen ID zurück.
	 *
	 * @param id   die ID des Katalog-Eintrags
	 *
	 * @return den Lehrplan für die ID oder null bei einer fehlerhaften ID
	 */
	public getLehrplanById(id : number) : BKLehrplan | null {
		return this._mapByID.get(id);
	}

	/**
	 * Gibt den Lehrplan zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 *
	 * @return den Lehrplan für die ID oder null bei einer fehlerhaften ID
	 */
	public getLehrplanByFachklassenschluesselSchuljahr(schluessel : BKFachklassenSchluessel, schuljahr : number) : BKLehrplan | null {
		return this.getLehrplanByIndexFachklasseSchuljahr(schluessel.index, schluessel.schluessel, schuljahr);
	}

	/**
	 * Gibt den Lehrplan zum Tupel(Schulgliederungsindex,Fachklasse) in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden soll
	 *
	 * @return den Lehrplan für eine Fachklasse im angegebenen Schuljahr
	 */
	public getLehrplanByIndexFachklasseSchuljahr(index : number, schluessel : string, schuljahr : number) : BKLehrplan | null {
		const mapById : JavaMap<number, List<BKLehrplan>> | null = this._mapLehrplanByFachklasse.getMap3OrNull(index, schluessel);
		if (mapById === null)
			return null;
		for (let lehrplaene of mapById.values())
			for (let lehrplan of lehrplaene)
				if (((lehrplan.gueltigVon === null) || (lehrplan.gueltigVon <= schuljahr)) && ((lehrplan.gueltigBis === null) || (lehrplan.gueltigBis >= schuljahr)))
					return lehrplan;
		return null;
	}

	/**
	 * Gibt den Lehrplan zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return den Lehrplan für eine Fachklasse im angegebenen Schuljahr und Jahrgang
	 */
	public getLehrplanByFachklassenschluesselSchuljahrJahrgang(schluessel : BKFachklassenSchluessel, schuljahr : number, jahrgang : number) : BKLehrplan | null {
		return this.getLehrplanByIndexFachklasseSchuljahrJahrgang(schluessel.index, schluessel.schluessel, schuljahr, jahrgang);
	}

	/**
	 * Gibt den Lehrplan zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return den Lehrplan für eine Fachklasse im angegebenen Schuljahr und Jahrgang
	 */
	public getLehrplanByIndexFachklasseSchuljahrJahrgang(index : number, schluessel : string, schuljahr : number, jahrgang : number) : BKLehrplan | null {
		const lehrplan : BKLehrplan | null = this.getLehrplanByIndexFachklasseSchuljahr(index, schluessel, schuljahr - jahrgang);
		if (lehrplan === null)
			return null;
		if (Math.trunc((lehrplan.dauer + 1) / 2) < jahrgang)
			throw new UserNotificationException("Fehlerhafter Jahrgang: Der Jahrgang " + jahrgang + " ist zu groß für den Bildungsgang mit einer Dauer von " + lehrplan.dauer + " Monaten!")
		return lehrplan;
	}

	/**
	 * Gibt alle bekannten Bündelfächer zurück.
	 *
	 * @return die Liste aller bekannten Bündelfächer
	 */
	public getFaecherByFachklassenschuesselSchuljahr() : Array<BKFBFach | null> | null;

	/**
	 * Gibt die Bündelfächer zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 *
	 * @return die Liste der Bündelfächer für die Fachklasse und Schuljahr oder null falls keine Lehrplan für Fachklasse vorhanden ist.
	 */
	public getFaecherByFachklassenschuesselSchuljahr(schluessel : BKFachklassenSchluessel, schuljahr : number) : Array<BKFBFach | null> | null;

	/**
	 * Implementation for method overloads of 'getFaecherByFachklassenschuesselSchuljahr'
	 */
	public getFaecherByFachklassenschuesselSchuljahr(__param0? : BKFachklassenSchluessel, __param1? : number) : Array<BKFBFach | null> | null {
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
			return this._mapFachByKuerzel.values().toArray(Array(0).fill(null));
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.bk.BKFachklassenSchluessel')))) && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			const schluessel : BKFachklassenSchluessel = cast_de_svws_nrw_core_data_bk_BKFachklassenSchluessel(__param0);
			const schuljahr : number = __param1 as number;
			return this.getFaecherByIndexFachklasseSchuljahr(schluessel.index, schluessel.schluessel, schuljahr);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt ein Bündelfach zu einem Kürzel zurück
	 *
	 * @param kuerzel  	   das Kürzel des Fachs
	 *
	 * @return das Fach zu einem Kürzel,
	 */
	public getFachByKuerzel(kuerzel : string) : BKFBFach | null {
		return this._mapFachByKuerzel.get(kuerzel);
	}

	/**
	 * Gibt die Bündelfächer zu dem Tupel(Schulgliederungsindex,Fachklasse) in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden soll
	 *
	 * @return die Liste der Bündelfächer für die Fachklasse und Schuljahr oder null falls keine Leh
	 */
	public getFaecherByIndexFachklasseSchuljahr(index : number, schluessel : string, schuljahr : number) : Array<BKFBFach | null> | null {
		const mapById : JavaMap<number, List<BKLehrplan>> | null = this._mapLehrplanByFachklasse.getMap3OrNull(index, schluessel);
		if (mapById === null)
			return null;
		for (let lehrplaene of mapById.values())
			for (let lehrplan of lehrplaene)
				if (((lehrplan.gueltigVon === null) || (lehrplan.gueltigVon <= schuljahr)) && ((lehrplan.gueltigBis === null) || (lehrplan.gueltigBis >= schuljahr)))
					return lehrplan.fbFaecher.toArray(Array(0).fill(null));
		return null;
	}

	/**
	 * Gibt die Bündelfächer zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return die Liste der Bündelfächer für Fachklasse, Schuljahr und Jahrgang oder null bei einer fehlerhaften ID
	 */
	public getFaecherByFachklassenschluesselSchuljahrJahrgang(schluessel : BKFachklassenSchluessel, schuljahr : number, jahrgang : number) : Array<BKFBFach | null> | null {
		return this.getFaecherByIndexFachklasseSchuljahrJahrgang(schluessel.index, schluessel.schluessel, schuljahr, jahrgang);
	}

	/**
	 * Gibt die Bündelfächer zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return die Liste der Bündelfächer für Fachklasse, Schuljahr und Jahrgang oder null bei einer fehlerhaften ID
	 */
	public getFaecherByIndexFachklasseSchuljahrJahrgang(index : number, schluessel : string, schuljahr : number, jahrgang : number) : Array<BKFBFach | null> | null {
		const lehrplan : BKLehrplan | null = this.getLehrplanByIndexFachklasseSchuljahrJahrgang(index, schluessel, schuljahr, jahrgang);
		if (lehrplan === null)
			return null;
		return lehrplan.fbFaecher.toArray(Array(0).fill(null));
	}

	/**
	 * Gibt die Lernfelder zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 *
	 * @return die Liste der Lernfelder für eine Fachklasse und Schuljahr oder null, wenn nicht vorhanden
	 */
	public getLernfelderByFachklassenschluesselSchuljahr(schluessel : BKFachklassenSchluessel, schuljahr : number) : Array<BKLernfeld | null> | null {
		return this.getLernfelderByIndexFachklasseSchuljahr(schluessel.index, schluessel.schluessel, schuljahr);
	}

	/**
	 * Gibt die Lernfelder zu dem Tupel(Schulgliederungsindex,Fachklasse) in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden soll
	 *
	 * @return die Liste der Lernfelder für eine Fachklasse und Schuljahr oder null, wenn nicht vorhanden
	 */
	public getLernfelderByIndexFachklasseSchuljahr(index : number, schluessel : string, schuljahr : number) : Array<BKLernfeld | null> | null {
		const mapById : JavaMap<number, List<BKLehrplan>> | null = this._mapLehrplanByFachklasse.getMap3OrNull(index, schluessel);
		if (mapById === null)
			return null;
		for (let lehrplaene of mapById.values())
			for (let lehrplan of lehrplaene)
				if (((lehrplan.gueltigVon === null) || (lehrplan.gueltigVon <= schuljahr)) && ((lehrplan.gueltigBis === null) || (lehrplan.gueltigBis >= schuljahr)))
					return lehrplan.lernfelder.toArray(Array(0).fill(null));
		return null;
	}

	/**
	 * Gibt die Lernfelder zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return die Liste der Lernfelder für eine Fachklasse und Schuljahr im angegebenen Jahrgang oder null, wenn nicht vorhanden
	 */
	public getLernfelderByFachklassenschluesselSchuljahrJahrgang(schluessel : BKFachklassenSchluessel, schuljahr : number, jahrgang : number) : Array<BKLernfeld | null> | null {
		return this.getLernfelderByIndexFachklasseSchuljahrJahrgang(schluessel.index, schluessel.schluessel, schuljahr, jahrgang);
	}

	/**
	 * Gibt die Lernfelder zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return die Liste der Lernfelder für eine Fachklasse und Schuljahr im angegebenen Jahrgang oder null, wenn nicht vorhanden
	 */
	public getLernfelderByIndexFachklasseSchuljahrJahrgang(index : number, schluessel : string, schuljahr : number, jahrgang : number) : Array<BKLernfeld | null> | null {
		const lehrplan : BKLehrplan | null = this.getLehrplanByIndexFachklasseSchuljahrJahrgang(index, schluessel, schuljahr, jahrgang);
		if (lehrplan === null)
			return null;
		return lehrplan.lernfelder.toArray(Array(0).fill(null));
	}

	/**
	 * Gibt den Katalog zurück.
	 *
	 * @return der Katalog
	 */
	public getKatalog() : BKLehrplanKatalog {
		return this._katalog;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schule.BerufskollegLehrplanManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schule_BerufskollegLehrplanManager(obj : unknown) : BerufskollegLehrplanManager {
	return obj as BerufskollegLehrplanManager;
}
