import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { KAOAMerkmalKatalogEintrag } from '../../../asd/data/kaoa/KAOAMerkmalKatalogEintrag';

export class KAOAMerkmal extends JavaEnum<KAOAMerkmal> implements CoreType<KAOAMerkmalKatalogEintrag, KAOAMerkmal> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAMerkmal> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAMerkmal> = new Map<string, KAOAMerkmal>();

	/**
	 * KAoA-Merkmal: Schulische prozessorientierte Begleitung und Beratung
	 */
	public static readonly SBO_2_1 : KAOAMerkmal = new KAOAMerkmal("SBO_2_1", 0, );

	/**
	 * KAoA-Merkmal: Berufsorientierende Angebote der Berufsberatung der Bundesagentur für Arbeit (BA)
	 */
	public static readonly SBO_2_2 : KAOAMerkmal = new KAOAMerkmal("SBO_2_2", 1, );

	/**
	 * KAoA-Merkmal: Individuelle Beratungsangebote außerschulischer Partner
	 */
	public static readonly SBO_2_3 : KAOAMerkmal = new KAOAMerkmal("SBO_2_3", 2, );

	/**
	 * KAoA-Merkmal: STAR - Berufswegekonferenz
	 */
	public static readonly SBO_2_4 : KAOAMerkmal = new KAOAMerkmal("SBO_2_4", 3, );

	/**
	 * KAoA-Merkmal: Einbindung von Eltern bzw. Erziehungsberechtigten
	 */
	public static readonly SBO_2_5 : KAOAMerkmal = new KAOAMerkmal("SBO_2_5", 4, );

	/**
	 * KAoA-Merkmal: STAR - Einbindung von Eltern bzw. Erziehungsberechtigten
	 */
	public static readonly SBO_2_6 : KAOAMerkmal = new KAOAMerkmal("SBO_2_6", 5, );

	/**
	 * KAoA-Merkmal: Portfolioinstrument
	 */
	public static readonly SBO_3_4 : KAOAMerkmal = new KAOAMerkmal("SBO_3_4", 6, );

	/**
	 * KAoA-Merkmal: Potenzialanalyse 1-tägig
	 */
	public static readonly SBO_4_1 : KAOAMerkmal = new KAOAMerkmal("SBO_4_1", 7, );

	/**
	 * KAoA-Merkmal: Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Lernen und Emotionale soziale Entwicklung– 2-tägig
	 */
	public static readonly SBO_4_2 : KAOAMerkmal = new KAOAMerkmal("SBO_4_2", 8, );

	/**
	 * KAoA-Merkmal: STAR – Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Geistige Entwicklung, Körperliche und motorische Entwicklung, Hören und Kommunikation und Sprache - 2-tägig
	 */
	public static readonly SBO_4_3 : KAOAMerkmal = new KAOAMerkmal("SBO_4_3", 9, );

	/**
	 * KAoA-Merkmal: STAR – Feststellung des funktionalen Sehvermögens im Förderschwerpunkt Sehen
	 */
	public static readonly SBO_4_4 : KAOAMerkmal = new KAOAMerkmal("SBO_4_4", 10, );

	/**
	 * KAoA-Merkmal: STAR – Potenzialanalyse im Förderschwerpunkt Sehen – 2-tägig
	 */
	public static readonly SBO_4_5 : KAOAMerkmal = new KAOAMerkmal("SBO_4_5", 11, );

	/**
	 * KAoA-Merkmal: Berufsfelderkundungen
	 */
	public static readonly SBO_5_1 : KAOAMerkmal = new KAOAMerkmal("SBO_5_1", 12, );

	/**
	 * KAoA-Merkmal: STAR – Berufsfelderkundungen
	 */
	public static readonly SBO_5_2 : KAOAMerkmal = new KAOAMerkmal("SBO_5_2", 13, );

	/**
	 * KAoA-Merkmal: STAR – Arbeitsplatzbezogenes Kommunikationstraining I im Förderschwerpunkt Hören und Kommunikation
	 */
	public static readonly SBO_5_3 : KAOAMerkmal = new KAOAMerkmal("SBO_5_3", 14, );

	/**
	 * KAoA-Merkmal: STAR - Berufsorientierungsseminar
	 */
	public static readonly SBO_5_4 : KAOAMerkmal = new KAOAMerkmal("SBO_5_4", 15, );

	/**
	 * KAoA-Merkmal: Betriebspraktika in der Sekundarstufe I (ggf. 1 Woche verlagert aus der Oberstufe)
	 */
	public static readonly SBO_6_1 : KAOAMerkmal = new KAOAMerkmal("SBO_6_1", 16, );

	/**
	 * KAoA-Merkmal: STAR – Intensivtraining arbeitsrelevanter sozialer Kompetenzen (TASK)
	 */
	public static readonly SBO_6_2 : KAOAMerkmal = new KAOAMerkmal("SBO_6_2", 17, );

	/**
	 * KAoA-Merkmal: STAR – Betriebspraktikum im Block
	 */
	public static readonly SBO_6_3 : KAOAMerkmal = new KAOAMerkmal("SBO_6_3", 18, );

	/**
	 * KAoA-Merkmal: Praxiskurse
	 */
	public static readonly SBO_6_4 : KAOAMerkmal = new KAOAMerkmal("SBO_6_4", 19, );

	/**
	 * KAoA-Merkmal: Langzeitpraktikum
	 */
	public static readonly SBO_6_5 : KAOAMerkmal = new KAOAMerkmal("SBO_6_5", 20, );

	/**
	 * KAoA-Merkmal: STAR – Betriebspraktikum in Langzeit
	 */
	public static readonly SBO_6_6 : KAOAMerkmal = new KAOAMerkmal("SBO_6_6", 21, );

	/**
	 * KAoA-Merkmal: KAoA-kompakt
	 */
	public static readonly SBO_7_1 : KAOAMerkmal = new KAOAMerkmal("SBO_7_1", 22, );

	/**
	 * KAoA-Merkmal: Standortbestimmung - Reflexionsworkshop Sek. II
	 */
	public static readonly SBO_8_1 : KAOAMerkmal = new KAOAMerkmal("SBO_8_1", 23, );

	/**
	 * KAoA-Merkmal: Stärkung der Entscheidungskompetenz I – Sek. II
	 */
	public static readonly SBO_8_2 : KAOAMerkmal = new KAOAMerkmal("SBO_8_2", 24, );

	/**
	 * KAoA-Merkmal: Praxiselemente in Betrieben, Hochschulen, Institutionen
	 */
	public static readonly SBO_9_1 : KAOAMerkmal = new KAOAMerkmal("SBO_9_1", 25, );

	/**
	 * KAoA-Merkmal: Studienorientierung
	 */
	public static readonly SBO_9_2 : KAOAMerkmal = new KAOAMerkmal("SBO_9_2", 26, );

	/**
	 * KAoA-Merkmal: Stärkung der Entscheidungskompetenz II - Sek. II
	 */
	public static readonly SBO_9_3 : KAOAMerkmal = new KAOAMerkmal("SBO_9_3", 27, );

	/**
	 * KAoA-Merkmal: Bewerbungsphase
	 */
	public static readonly SBO_10_1 : KAOAMerkmal = new KAOAMerkmal("SBO_10_1", 28, );

	/**
	 * KAoA-Merkmal: STAR – Arbeitsplatzbezogenes Kommunikationstraining II im Förderschwerpunkt Hören und Kommunikation
	 */
	public static readonly SBO_10_2 : KAOAMerkmal = new KAOAMerkmal("SBO_10_2", 29, );

	/**
	 * KAoA-Merkmal: STAR – Betriebsnahes Bewerbungstraining/Umgang mit Dolmetschenden und Technik im Förderschwerpunkt Hören und Kommunikation
	 */
	public static readonly SBO_10_3 : KAOAMerkmal = new KAOAMerkmal("SBO_10_3", 30, );

	/**
	 * KAoA-Merkmal: Übergangsbegleitung
	 */
	public static readonly SBO_10_4 : KAOAMerkmal = new KAOAMerkmal("SBO_10_4", 31, );

	/**
	 * KAoA-Merkmal: STAR - Übergangsbegleitung
	 */
	public static readonly SBO_10_5 : KAOAMerkmal = new KAOAMerkmal("SBO_10_5", 32, );

	/**
	 * KAoA-Merkmal: Anschlussvereinbarung
	 */
	public static readonly SBO_10_6 : KAOAMerkmal = new KAOAMerkmal("SBO_10_6", 33, );

	/**
	 * KAoA-Merkmal: Koordinierte Übergangsgestaltung
	 */
	public static readonly SBO_10_7 : KAOAMerkmal = new KAOAMerkmal("SBO_10_7", 34, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		KAOAMerkmal.all_values_by_ordinal.push(this);
		KAOAMerkmal.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<KAOAMerkmalKatalogEintrag, KAOAMerkmal>) : void {
		CoreTypeDataManager.putManager(KAOAMerkmal.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<KAOAMerkmalKatalogEintrag, KAOAMerkmal> {
		return CoreTypeDataManager.getManager(KAOAMerkmal.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAMerkmal> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAMerkmal | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<KAOAMerkmalKatalogEintrag, KAOAMerkmal> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : KAOAMerkmalKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<KAOAMerkmalKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.kaoa.KAOAMerkmal';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.kaoa.KAOAMerkmal', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<KAOAMerkmal>('de.svws_nrw.asd.types.kaoa.KAOAMerkmal');

}

export function cast_de_svws_nrw_asd_types_kaoa_KAOAMerkmal(obj : unknown) : KAOAMerkmal {
	return obj as KAOAMerkmal;
}
