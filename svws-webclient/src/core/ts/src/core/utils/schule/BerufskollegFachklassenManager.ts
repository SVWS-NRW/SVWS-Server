import { JavaObject } from '../../../java/lang/JavaObject';
import { BerufskollegFachklassenKatalogEintrag } from '../../../core/data/schule/BerufskollegFachklassenKatalogEintrag';
import { BerufskollegFachklassenKatalog } from '../../../core/data/schule/BerufskollegFachklassenKatalog';
import { HashMap } from '../../../java/util/HashMap';
import { Schulgliederung, cast_de_svws_nrw_core_types_schule_Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { BerufskollegFachklassenKatalogIndex } from '../../../core/data/schule/BerufskollegFachklassenKatalogIndex';
import { Vector } from '../../../java/util/Vector';
import { BerufskollegFachklassenKatalogDaten } from '../../../core/data/schule/BerufskollegFachklassenKatalogDaten';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class BerufskollegFachklassenManager extends JavaObject {

	/**
	 * der Katalog
	 */
	private readonly _katalog : BerufskollegFachklassenKatalog;

	/**
	 * Die Version der Daten
	 */
	private readonly _version : number;

	/**
	 * Ein Vektor mit allen Katalog-Einträgen
	 */
	private readonly _values : Vector<BerufskollegFachklassenKatalogEintrag> = new Vector();

	/**
	 * Eine HashMap für den schnellen Zugriff auf einen Teilkatalog anhand eines Index.
	 */
	private readonly _mapByIndex : HashMap<number, BerufskollegFachklassenKatalogIndex> = new HashMap();

	/**
	 * Eine HashMap für den Zugriff auf den Index anhand eines Eintrags.
	 */
	private readonly _mapIndexByEintrag : HashMap<BerufskollegFachklassenKatalogEintrag, number> = new HashMap();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Fachklassen anhand des Fachklassen-Schlüssels.
	 */
	private readonly _mapByKuerzel : HashMap<string, BerufskollegFachklassenKatalogEintrag> = new HashMap();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Fachklassen anhand der ID.
	 */
	private readonly _mapByID : HashMap<number, BerufskollegFachklassenKatalogEintrag> = new HashMap();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Daten der Fachklasse anhand der ID.
	 */
	private readonly _mapDatenByID : HashMap<number, BerufskollegFachklassenKatalogDaten> = new HashMap();


	/**
	 * Erstellt einen neuen Manager für den Katalog der Fachklassen
	 *
	 * @param katalog   der Katalog der Fachklassen
	 */
	public constructor(katalog : BerufskollegFachklassenKatalog) {
		super();
		this._katalog = katalog;
		this._version = katalog.version;
		for (const katIndex of katalog.indizes) {
			this._values.addAll(katIndex.fachklassen);
			this._mapByIndex.put(katIndex.index, katIndex);
			for (const eintrag of katIndex.fachklassen) {
				this._mapIndexByEintrag.put(eintrag, katIndex.index);
				const kuerzel : string | null = "" + katIndex.index + "-" + eintrag.schluessel + "-" + eintrag.schluessel2;
				this._mapByKuerzel.put(kuerzel, eintrag);
				for (const daten of eintrag.historie) {
					const alt : BerufskollegFachklassenKatalogEintrag | null = this._mapByID.put(daten.id, eintrag);
					if (alt !== null)
						throw new DeveloperNotificationException("Fehlerhafter Katalog: Doppelte ID '" + daten.id + "' bei der Fachklasse '" + kuerzel! + "'")
					this._mapDatenByID.put(daten.id, daten);
				}
			}
		}
	}

	/**
	 * Gibt die Version der Katalog-Daten zurück.
	 *
	 * @return die Version
	 */
	public getVersion() : number;

	/**
	 * Gibt die Version der Daten eines Teilkatalog für einen Index zurück.
	 *
	 * @param index   der Index für die Fachklassen
	 *
	 * @return die Version des Teilkatalogs
	 */
	public getVersion(index : number) : number;

	/**
	 * Gibt die Version der Daten des Teilkatalog für den Index
	 * der angegebenen Schulgliederung zurück.
	 *
	 * @param gliederung   die Schulgliederung
	 *
	 * @return die Version des Teilkatalogs
	 */
	public getVersion(gliederung : Schulgliederung | null) : number;

	/**
	 * Implementation for method overloads of 'getVersion'
	 */
	public getVersion(__param0? : Schulgliederung | null | number) : number {
		if ((typeof __param0 === "undefined")) {
			return this._version;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			const index : number = __param0 as number;
			const katIndex : BerufskollegFachklassenKatalogIndex | null = this._mapByIndex.get(index);
			if (katIndex === null)
				throw new IllegalArgumentException("Ungültiger Fachklassen-Index.")
			return katIndex.version;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.types.schule.Schulgliederung'))) || (__param0 === null))) {
			const gliederung : Schulgliederung | null = cast_de_svws_nrw_core_types_schule_Schulgliederung(__param0);
			if (gliederung.daten.bkIndex === null)
				throw new IllegalArgumentException("Die Schulgliederung " + gliederung.daten.kuerzel + " hat keinen Fachklassen-Index.")
			const katIndex : BerufskollegFachklassenKatalogIndex | null = this._mapByIndex.get(gliederung.daten.bkIndex);
			if (katIndex === null)
				throw new IllegalArgumentException("Keine Fachklassen für den Fachklassen-Index " + gliederung.daten.bkIndex + " der Schulgliederung " + gliederung.daten.kuerzel + " bekannt.")
			return katIndex.version;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt den Katalog-Eintrag für das übergebene Kürzel zurück. Das Kürzel setzt
	 * sich zusammen aus dem Index und den beiden Teilschlüsseln der Fachklasse:
	 * "Index-Schlüssel1-Schlüssel2".
	 *
	 * @param kuerzel   das Kürzel des Katalog-Eintrags
	 *
	 * @return der Katalog-Eintrag oder null, falls das Kürzel ungültig ist.
	 */
	public get(kuerzel : string) : BerufskollegFachklassenKatalogEintrag | null {
		return this._mapByKuerzel.get(kuerzel);
	}

	/**
	 * Gibt alle Katalog-Einträge zurück.
	 *
	 * @return ein Array mit allen Katalog-Einträgen
	 */
	public values() : Array<BerufskollegFachklassenKatalogEintrag | null> | null {
		return this._values.toArray(Array(0).fill(null));
	}

	/**
	 * Gibt die Katalog-Daten für das übergebene Kürzel und das angegebene Schuljahr zurück.
	 * Das Kürzel setzt sich zusammen aus dem Index und den beiden Teilschlüsseln der Fachklasse:
	 * "Index-Schlüssel1-Schlüssel2".
	 *
	 * @param kuerzel     das Kürzel des Katalog-Eintrags
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return der Katalog-Eintrag oder null, falls das Kürzel ungültig ist oder der Katalog-Eintrag
	 *         keine Daten für das übergebene Schuljahr hat
	 */
	public getDaten(kuerzel : string, schuljahr : number) : BerufskollegFachklassenKatalogDaten | null;

	/**
	 * Gibt die Katalog-Daten für die Fachklasse zurück.
	 *
	 * @param id   die die des Katalog-Eintrags
	 *
	 * @return die Daten für die ID oder null bei einer fehlerhaften ID
	 */
	public getDaten(id : number) : BerufskollegFachklassenKatalogDaten | null;

	/**
	 * Implementation for method overloads of 'getDaten'
	 */
	public getDaten(__param0 : number | string, __param1? : number) : BerufskollegFachklassenKatalogDaten | null {
		if (((typeof __param0 !== "undefined") && (typeof __param0 === "string")) && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			const kuerzel : string = __param0;
			const schuljahr : number = __param1 as number;
			const eintrag : BerufskollegFachklassenKatalogEintrag | null = this._mapByKuerzel.get(kuerzel);
			if (eintrag === null)
				return null;
			for (const daten of eintrag.historie)
				if (((daten.gueltigVon === null) || (daten.gueltigVon <= schuljahr)) && ((daten.gueltigBis === null) || (daten.gueltigBis >= schuljahr)))
					return daten;
			return null;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && (typeof __param1 === "undefined")) {
			const id : number = __param0 as number;
			return this._mapDatenByID.get(id);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt das Kürzel für die Fachklasse mit der angebenen ID zurück. Das Kürzel setzt
	 * sich zusammen aus dem Index und den beiden Teilschlüsseln der Fachklasse:
	 * "Index-Schlüssel1-Schlüssel2".
	 *
	 * @param id   die ID der Fachklasse
	 *
	 * @return das Kürzel der Fachklasse oder null, falls die ID ungültig ist
	 */
	public getKuerzel(id : number) : string | null {
		const eintrag : BerufskollegFachklassenKatalogEintrag | null = this._mapByID.get(id);
		const index : number | null = this._mapIndexByEintrag.get(eintrag);
		return (eintrag === null) || (index === null) ? null : "" + index! + "-" + eintrag.schluessel + "-" + eintrag.schluessel2;
	}

	/**
	 * Gibt den Teilkatalog für den angegebenen Fachklassen-Index zurück.
	 *
	 * @param index   der Fachklassen-Index des Teilkatalogs
	 *
	 * @return der Teilkatalog
	 */
	public getTeilKatalog(index : number) : BerufskollegFachklassenKatalogIndex;

	/**
	 * Gibt den Teilkatalog des Fachklassen-Index
	 * für die angegebene Schulgliederung zurück.
	 *
	 * @param gliederung   die Schulgliederung
	 *
	 * @return der Teilkatalog
	 */
	public getTeilKatalog(gliederung : Schulgliederung | null) : BerufskollegFachklassenKatalogIndex;

	/**
	 * Implementation for method overloads of 'getTeilKatalog'
	 */
	public getTeilKatalog(__param0 : Schulgliederung | null | number) : BerufskollegFachklassenKatalogIndex {
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			const index : number = __param0 as number;
			const katIndex : BerufskollegFachklassenKatalogIndex | null = this._mapByIndex.get(index);
			if (katIndex === null)
				throw new IllegalArgumentException("Ungültiger Fachklassen-Index.")
			return katIndex;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.types.schule.Schulgliederung'))) || (__param0 === null))) {
			const gliederung : Schulgliederung | null = cast_de_svws_nrw_core_types_schule_Schulgliederung(__param0);
			if (gliederung.daten.bkIndex === null)
				throw new IllegalArgumentException("Die Schulgliederung " + gliederung.daten.kuerzel + " hat keinen Fachklassen-Index.")
			const katIndex : BerufskollegFachklassenKatalogIndex | null = this._mapByIndex.get(gliederung.daten.bkIndex);
			if (katIndex === null)
				throw new IllegalArgumentException("Keine Fachklassen für den Fachklassen-Index " + gliederung.daten.bkIndex + " der Schulgliederung " + gliederung.daten.kuerzel + " bekannt.")
			return katIndex;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt den Katalog zurück.
	 *
	 * @return der Katalog
	 */
	public getKatalog() : BerufskollegFachklassenKatalog {
		return this._katalog;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schule.BerufskollegFachklassenManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schule_BerufskollegFachklassenManager(obj : unknown) : BerufskollegFachklassenManager {
	return obj as BerufskollegFachklassenManager;
}
