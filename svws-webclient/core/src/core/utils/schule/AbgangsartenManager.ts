import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchulabschlussBerufsbildend } from '../../../asd/types/schule/SchulabschlussBerufsbildend';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { AbgangsartKatalogDaten } from '../../../core/data/schule/AbgangsartKatalogDaten';
import { SchulabschlussAllgemeinbildend } from '../../../asd/types/schule/SchulabschlussAllgemeinbildend';
import { AbgangsartKatalog } from '../../../core/data/schule/AbgangsartKatalog';
import { AbgangsartKatalogEintrag } from '../../../core/data/schule/AbgangsartKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';

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
	private readonly _alle : List<AbgangsartKatalogEintrag> = new ArrayList<AbgangsartKatalogEintrag>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Abgangsarten anhand des Kürzels.
	 */
	private readonly _mapByKuerzel : JavaMap<string, AbgangsartKatalogEintrag> = new HashMap<string, AbgangsartKatalogEintrag>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Abgangsarten anhand der ID.
	 */
	private readonly _mapByID : JavaMap<number, AbgangsartKatalogEintrag> = new HashMap<number, AbgangsartKatalogEintrag>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Daten der Abgangsarten anhand der ID.
	 */
	private readonly _mapDatenByID : JavaMap<number, AbgangsartKatalogDaten> = new HashMap<number, AbgangsartKatalogDaten>();


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
		for (const eintrag of this._alle) {
			this._mapByKuerzel.put(eintrag.kuerzel, eintrag);
			for (const daten of eintrag.historie) {
				const alt : AbgangsartKatalogEintrag | null = this._mapByID.put(daten.id, eintrag);
				if (alt !== null)
					throw new DeveloperNotificationException("Fehlerhafter Katalog: Doppelte ID '" + daten.id + "' bei den Abgangsarten '" + eintrag.kuerzel + "' und '" + alt.kuerzel + "'")
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
	public getAll() : List<AbgangsartKatalogEintrag> | null {
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
		if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && typeof __param1 === "number")) {
			const kuerzel : string = __param0;
			const schuljahr : number = __param1 as number;
			const eintrag : AbgangsartKatalogEintrag | null = this._mapByKuerzel.get(kuerzel);
			if (eintrag === null)
				return null;
			for (const daten of eintrag.historie)
				if (((daten.gueltigVon === null) || (daten.gueltigVon <= schuljahr)) && ((daten.gueltigBis === null) || (daten.gueltigBis >= schuljahr)))
					return daten;
			return null;
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && (__param1 === undefined)) {
			const id : number = __param0 as number;
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
		const eintrag : AbgangsartKatalogEintrag | null = this._mapByID.get(id);
		return (eintrag === null) ? null : eintrag.kuerzel;
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
			throw new DeveloperNotificationException("Fehlerhafter Katalog-Eintrag: Das Kürzel einer Abgangsart muss entweder ein- oder zweistelig sein.")
		const kuerzelAbschluss : string = (abschlussart.kuerzel.length === 1) ? abschlussart.kuerzel : abschlussart.kuerzel.substring(1, 2);
		return SchulabschlussAllgemeinbildend.data().getWertBySchluessel(kuerzelAbschluss);
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
			throw new DeveloperNotificationException("Fehlerhafter Katalog-Eintrag: Das Kürzel einer Abgangsart muss entweder ein- oder zweistelig sein.")
		if (abschlussart.kuerzel.length === 1)
			return null;
		return SchulabschlussBerufsbildend.data().getWertBySchluessel(abschlussart.kuerzel.substring(0, 1));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.schule.AbgangsartenManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schule.AbgangsartenManager'].includes(name);
	}

	public static class = new Class<AbgangsartenManager>('de.svws_nrw.core.utils.schule.AbgangsartenManager');

}

export function cast_de_svws_nrw_core_utils_schule_AbgangsartenManager(obj : unknown) : AbgangsartenManager {
	return obj as AbgangsartenManager;
}
