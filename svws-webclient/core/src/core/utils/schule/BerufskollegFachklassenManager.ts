import { JavaObject } from '../../../java/lang/JavaObject';
import { BerufskollegFachklassenKatalogEintrag } from '../../../core/data/schule/BerufskollegFachklassenKatalogEintrag';
import { BerufskollegFachklassenKatalog } from '../../../core/data/schule/BerufskollegFachklassenKatalog';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { BerufskollegFachklassenKatalogIndex } from '../../../core/data/schule/BerufskollegFachklassenKatalogIndex';
import { BerufskollegFachklassenKatalogDaten } from '../../../core/data/schule/BerufskollegFachklassenKatalogDaten';
import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import { SchulgliederungKatalogEintrag } from '../../../asd/data/schule/SchulgliederungKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';
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
	private readonly _values : List<BerufskollegFachklassenKatalogEintrag> = new ArrayList<BerufskollegFachklassenKatalogEintrag>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf einen Teilkatalog anhand eines Index.
	 */
	private readonly _mapByIndex : JavaMap<number, BerufskollegFachklassenKatalogIndex> = new HashMap<number, BerufskollegFachklassenKatalogIndex>();

	/**
	 * Eine HashMap für den Zugriff auf den Index anhand eines Eintrags.
	 */
	private readonly _mapIndexByEintrag : JavaMap<BerufskollegFachklassenKatalogEintrag, number> = new HashMap<BerufskollegFachklassenKatalogEintrag, number>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Fachklassen anhand des Fachklassen-Schlüssels.
	 */
	private readonly _mapByKuerzel : JavaMap<string, BerufskollegFachklassenKatalogEintrag> = new HashMap<string, BerufskollegFachklassenKatalogEintrag>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Fachklassen anhand der ID.
	 */
	private readonly _mapByID : JavaMap<number, BerufskollegFachklassenKatalogEintrag> = new HashMap<number, BerufskollegFachklassenKatalogEintrag>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Daten der Fachklasse anhand der ID.
	 */
	private readonly _mapDatenByID : JavaMap<number, BerufskollegFachklassenKatalogDaten> = new HashMap<number, BerufskollegFachklassenKatalogDaten>();


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
						throw new DeveloperNotificationException("Fehlerhafter Katalog: Doppelte ID '" + daten.id + "' bei der Fachklasse '" + kuerzel + "'")
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
	public getVersion() : number {
		return this._version;
	}

	/**
	 * Gibt die Version der Daten eines Teilkatalog für einen Index zurück.
	 *
	 * @param index   der Index für die Fachklassen
	 *
	 * @return die Version des Teilkatalogs
	 */
	public getVersionByIndex(index : number) : number {
		const katIndex : BerufskollegFachklassenKatalogIndex | null = this._mapByIndex.get(index);
		if (katIndex === null)
			throw new IllegalArgumentException("Ungültiger Fachklassen-Index.")
		return katIndex.version;
	}

	/**
	 * Gibt die Version der Daten des Teilkatalog für den Index
	 * der angegebenen Schulgliederung zurück.
	 *
	 * @param schuljahr    das Schuljahr
	 * @param gliederung   die Schulgliederung
	 *
	 * @return die Version des Teilkatalogs
	 */
	public getVersionBySchuljahrAndGliederung(schuljahr : number, gliederung : Schulgliederung) : number {
		const sglke : SchulgliederungKatalogEintrag | null = gliederung.daten(schuljahr);
		if (sglke === null)
			throw new IllegalArgumentException(JavaString.format("Die Schulgliederung %s ist in dem Schuljahr %d nicht gültig.", gliederung.name(), schuljahr))
		if (sglke.bkIndex === null)
			throw new IllegalArgumentException("Die Schulgliederung " + sglke.kuerzel + " hat keinen Fachklassen-Index.")
		const katIndex : BerufskollegFachklassenKatalogIndex | null = this._mapByIndex.get(sglke.bkIndex);
		if (katIndex === null)
			throw new IllegalArgumentException("Keine Fachklassen für den Fachklassen-Index " + sglke.bkIndex + " der Schulgliederung " + sglke.kuerzel + " bekannt.")
		return katIndex.version;
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
	public getByKuerzel(kuerzel : string) : BerufskollegFachklassenKatalogEintrag | null {
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
	public getDatenByKuerzelAndSchuljahr(kuerzel : string, schuljahr : number) : BerufskollegFachklassenKatalogDaten | null {
		const eintrag : BerufskollegFachklassenKatalogEintrag | null = this._mapByKuerzel.get(kuerzel);
		if (eintrag === null)
			return null;
		for (const daten of eintrag.historie)
			if (((daten.gueltigVon === null) || (daten.gueltigVon <= schuljahr)) && ((daten.gueltigBis === null) || (daten.gueltigBis >= schuljahr)))
				return daten;
		return null;
	}

	/**
	 * Gibt die Katalog-Daten für die Fachklasse zurück.
	 *
	 * @param id   die die des Katalog-Eintrags
	 *
	 * @return die Daten für die ID oder null bei einer fehlerhaften ID
	 */
	public getDatenByID(id : number) : BerufskollegFachklassenKatalogDaten | null {
		return this._mapDatenByID.get(id);
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
		return ((eintrag === null) || (index === null)) ? null : ("" + index + "-" + eintrag.schluessel + "-" + eintrag.schluessel2);
	}

	/**
	 * Gibt den Teilkatalog für den angegebenen Fachklassen-Index zurück.
	 *
	 * @param index   der Fachklassen-Index des Teilkatalogs
	 *
	 * @return der Teilkatalog
	 */
	public getTeilKatalog(index : number) : BerufskollegFachklassenKatalogIndex {
		const katIndex : BerufskollegFachklassenKatalogIndex | null = this._mapByIndex.get(index);
		if (katIndex === null)
			throw new IllegalArgumentException("Ungültiger Fachklassen-Index.")
		return katIndex;
	}

	/**
	 * Gibt den Teilkatalog des Fachklassen-Index
	 * für die angegebene Schulgliederung zurück.
	 *
	 * @param schuljahr    das Schuljahr, für welches der Teilkatalog bestimmt werden soll
	 * @param gliederung   die Schulgliederung
	 *
	 * @return der Teilkatalog
	 */
	public getTeilKatalogBySchuljahrAndGliederung(schuljahr : number, gliederung : Schulgliederung) : BerufskollegFachklassenKatalogIndex {
		const sglke : SchulgliederungKatalogEintrag | null = gliederung.daten(schuljahr);
		if (sglke === null)
			throw new IllegalArgumentException(JavaString.format("Die Schulgliederung %s ist in dem Schuljahr %d nicht gültig.", gliederung.name(), schuljahr))
		if (sglke.bkIndex === null)
			throw new IllegalArgumentException("Die Schulgliederung " + sglke.kuerzel + " hat keinen Fachklassen-Index.")
		const katIndex : BerufskollegFachklassenKatalogIndex | null = this._mapByIndex.get(sglke.bkIndex);
		if (katIndex === null)
			throw new IllegalArgumentException("Keine Fachklassen für den Fachklassen-Index " + sglke.bkIndex + " der Schulgliederung " + sglke.kuerzel + " bekannt.")
		return katIndex;
	}

	/**
	 * Gibt den Katalog zurück.
	 *
	 * @return der Katalog
	 */
	public getKatalog() : BerufskollegFachklassenKatalog {
		return this._katalog;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.schule.BerufskollegFachklassenManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schule.BerufskollegFachklassenManager'].includes(name);
	}

	public static class = new Class<BerufskollegFachklassenManager>('de.svws_nrw.core.utils.schule.BerufskollegFachklassenManager');

}

export function cast_de_svws_nrw_core_utils_schule_BerufskollegFachklassenManager(obj : unknown) : BerufskollegFachklassenManager {
	return obj as BerufskollegFachklassenManager;
}
