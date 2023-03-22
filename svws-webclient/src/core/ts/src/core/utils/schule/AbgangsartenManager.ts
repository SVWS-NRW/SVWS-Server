import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { RuntimeException, cast_java_lang_RuntimeException } from '../../../java/lang/RuntimeException';
import { SchulabschlussAllgemeinbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussAllgemeinbildend } from '../../../core/types/schule/SchulabschlussAllgemeinbildend';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { AbgangsartKatalog, cast_de_nrw_schule_svws_core_data_schule_AbgangsartKatalog } from '../../../core/data/schule/AbgangsartKatalog';
import { AbgangsartKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_AbgangsartKatalogEintrag } from '../../../core/data/schule/AbgangsartKatalogEintrag';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { SchulabschlussBerufsbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussBerufsbildend } from '../../../core/types/schule/SchulabschlussBerufsbildend';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { AbgangsartKatalogDaten, cast_de_nrw_schule_svws_core_data_schule_AbgangsartKatalogDaten } from '../../../core/data/schule/AbgangsartKatalogDaten';

export class AbgangsartenManager extends JavaObject {

	/**
	 * der Katalog für die allgemeinbildenden Schulformen
	 */
	private readonly _katalogAllgemein : AbgangsartKatalog;

	/**
	 * der Katalog für die berufsbildenden Schulformen
	 */
	private readonly _katalogBeruf : AbgangsartKatalog;

	/**
	 * Die Version der Daten für die Kombination der beiden Kataloge
	 */
	private readonly _version : number;

	/**
	 * Die kombinierten Daten der beiden Kataloge
	 */
	private readonly _alle : Vector<AbgangsartKatalogEintrag> = new Vector();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Abgangsarten anhand des Kürzels.
	 */
	private readonly _mapByKuerzel : HashMap<string, AbgangsartKatalogEintrag> = new HashMap();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Abgangsarten anhand der ID.
	 */
	private readonly _mapByID : HashMap<number, AbgangsartKatalogEintrag> = new HashMap();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Daten der Abgangsarten anhand der ID.
	 */
	private readonly _mapDatenByID : HashMap<number, AbgangsartKatalogDaten> = new HashMap();


	/**
	 * Erstellt einen neuen Manager für die möglichen Abgangsarten
	 *
	 * @param katalogAllgemein   der Katalog für die allgemeinbildenden Schulformen
	 * @param katalogBeruf       der Katalog für die berufsbildenden Schulformen
	 */
	public constructor(katalogAllgemein : AbgangsartKatalog, katalogBeruf : AbgangsartKatalog) {
		super();
		this._katalogAllgemein = katalogAllgemein;
		this._katalogBeruf = katalogBeruf;
		this._version = katalogAllgemein.version + katalogBeruf.version;
		this._alle.addAll(katalogAllgemein.eintraege);
		this._alle.addAll(katalogBeruf.eintraege);
		for (let eintrag of this._alle) {
			this._mapByKuerzel.put(eintrag.kuerzel, eintrag);
			for (let daten of eintrag.historie) {
				let alt : AbgangsartKatalogEintrag | null = this._mapByID.put(daten.id, eintrag);
				if (alt !== null)
					throw new RuntimeException("Fehlerhafter Katalog: Doppelte ID \'" + daten.id + "\' bei den Abgangsarten \'" + eintrag.kuerzel + "\' und \'" + alt.kuerzel + "\'")
				this._mapDatenByID.put(daten.id, daten);
			}
		}
	}

	/**
	 * Gibt die Version der Daten im kombinierten Katalog für die allgemeinbildenden
	 * und dir berufsbildenden Schule zurück.
	 *
	 * @return die Version
	 */
	public getVersion() : number {
		return this._version;
	}

	/**
	 * Gibt den Katalog-Eintrag für das übergebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Katalog-Eintrags
	 *
	 * @return der Katalog-Eintrag oder null, falls das Kürzel ungültig ist.
	 */
	public get(kuerzel : string) : AbgangsartKatalogEintrag | null {
		return this._mapByKuerzel.get(kuerzel);
	}

	/**
	 * Gibt alle Katalog-Einträge zurück.
	 *
	 * @return eine Liste mit allen Katalog-Einträgen
	 */
	public getAll() : List<AbgangsartKatalogEintrag | null> | null {
		return this._alle;
	}

	/**
	 * Gibt die Katalog-Daten für das übergebene Kürzel
	 * und das angegebene Schuljahr zurück.
	 *
	 * @param kuerzel     das Kürzel des Katalog-Eintrags
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return der Katalog-Eintrag oder null, falls das Kürzel ungültig ist oder der Katalog-Eintrag
	 *         keine Daten für das übergebene Schuljahr hat
	 */
	public getDaten(kuerzel : string, schuljahr : number) : AbgangsartKatalogDaten | null;

	/**
	 * Gibt die Katalog-Daten für die Abgangsart zurück.
	 *
	 * @param id   die die des Katalog-Eintrags
	 *
	 * @return die Daten für die ID oder null bei einer fehlerhaften ID
	 */
	public getDaten(id : number) : AbgangsartKatalogDaten | null;

	/**
	 * Implementation for method overloads of 'getDaten'
	 */
	public getDaten(__param0 : number | string, __param1? : number) : AbgangsartKatalogDaten | null {
		if (((typeof __param0 !== "undefined") && (typeof __param0 === "string")) && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			let kuerzel : string = __param0;
			let schuljahr : number = __param1 as number;
			let eintrag : AbgangsartKatalogEintrag | null = this._mapByKuerzel.get(kuerzel);
			if (eintrag === null)
				return null;
			for (let daten of eintrag.historie)
				if (((daten.gueltigVon === null) || (daten.gueltigVon <= schuljahr)) && ((daten.gueltigBis === null) || (daten.gueltigBis >= schuljahr)))
					return daten;
			return null;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && (typeof __param1 === "undefined")) {
			let id : number = __param0 as number;
			return this._mapDatenByID.get(id);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt das Kürzel für die Abgangsart mit der angebenen ID zurück.
	 *
	 * @param id   die ID der Abgangsart
	 *
	 * @return das Kürzel der Abgangsart oder null, falls die ID ungültig ist
	 */
	public getKuerzel(id : number) : string | null {
		let eintrag : AbgangsartKatalogEintrag | null = this._mapByID.get(id);
		return eintrag === null ? null : eintrag.kuerzel;
	}

	/**
	 * Gibt den Katalog für allgemeinbildende Schulformen zurück.
	 *
	 * @return der Katalog für allgemeinbildende Schulformen
	 */
	public getKatalogAllgemeinbildend() : AbgangsartKatalog {
		return this._katalogAllgemein;
	}

	/**
	 * Gibt den Katalog für berufsbildende Schulformen zurück.
	 *
	 * @return der Katalog für berufsbildende Schulformen
	 */
	public getKatalogBerufsbildend() : AbgangsartKatalog {
		return this._katalogBeruf;
	}

	/**
	 * Bestimmt den Allgemeinbildenden Abschluss der Abschlussart.
	 *
	 * @param abschlussart   die Abschlussart
	 *
	 * @return der allgemeinbildende Abschluss oder null in einem unerwarteten Fehlerfall
	 */
	public static getAbschlussAllgemeinbildend(abschlussart : AbgangsartKatalogEintrag) : SchulabschlussAllgemeinbildend | null {
		if ((abschlussart.kuerzel.length < 0) || (abschlussart.kuerzel.length > 2))
			throw new RuntimeException("Fehlerhafter Katalog-Eintrag: Das Kürzel einer Abgangsart muss entweder ein- oder zweistelig sein.")
		let kuerzelAbschluss : string = abschlussart.kuerzel.length === 1 ? abschlussart.kuerzel : abschlussart.kuerzel.substring(1, 2);
		return SchulabschlussAllgemeinbildend.getByKuerzelStatistik(kuerzelAbschluss);
	}

	/**
	 * Bestimmt den Berufsbildenden Abschluss der Abschlussart.
	 *
	 * @param abschlussart   die Abschlussart
	 *
	 * @return der berufsbildende Abschluss oder null, wenn nur ein allgemeinbildender Abschluss vorliegt.
	 */
	public static getAbschlussBerufsbildend(abschlussart : AbgangsartKatalogEintrag) : SchulabschlussBerufsbildend | null {
		if ((abschlussart.kuerzel.length < 0) || (abschlussart.kuerzel.length > 2))
			throw new RuntimeException("Fehlerhafter Katalog-Eintrag: Das Kürzel einer Abgangsart muss entweder ein- oder zweistelig sein.")
		if (abschlussart.kuerzel.length === 1)
			return null;
		return SchulabschlussBerufsbildend.getByKuerzelStatistik(abschlussart.kuerzel.substring(0, 1));
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.schule.AbgangsartenManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_schule_AbgangsartenManager(obj : unknown) : AbgangsartenManager {
	return obj as AbgangsartenManager;
}
