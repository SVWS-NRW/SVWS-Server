import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { KAOAAnschlussoptionenKatalogEintrag } from '../../../asd/data/kaoa/KAOAAnschlussoptionenKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class KAOAAnschlussoptionen extends JavaEnum<KAOAAnschlussoptionen> implements CoreType<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAAnschlussoptionen> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAAnschlussoptionen> = new Map<string, KAOAAnschlussoptionen>();

	/**
	 * KAoA-Anschlussoption: Berufspraxisstufe einer Förderschule für Geistige Entwicklung
	 */
	public static readonly BE : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("BE", 0, );

	/**
	 * KAoA-Anschlussoption: Gymnasiale Oberstufe der Gesamtschule oder des Gymnasiums
	 */
	public static readonly GY : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("GY", 1, );

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Einjährige Ausbildungsvorbereitung in Vollzeit (AV) - Anlage A
	 */
	public static readonly AV : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("AV", 2, );

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Einjährige Berufsfachschule Typ I (BFS I) - Anlage B
	 */
	public static readonly BFSI : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("BFSI", 3, );

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Einjährige Berufsfachschule Typ II (BFS II) - Anlage B
	 */
	public static readonly BFSII : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("BFSII", 4, );

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Zweijährige [Höhere] Berufsfachschule - Anlage C
	 */
	public static readonly HOEBFS : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("HOEBFS", 5, );

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Zweijährige Fachoberschule (FOS 11/12) - Anlage C
	 */
	public static readonly FOS : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("FOS", 6, );

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Berufliches Gymnasium - Anlage D
	 */
	public static readonly BERUFGY : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("BERUFGY", 7, );

	/**
	 * KAoA-Anschlussoption: Duale Berufsausbildung (inkl. Beamtenlaufbahn im mittleren Dienst)
	 */
	public static readonly DUAL : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("DUAL", 8, );

	/**
	 * KAoA-Anschlussoption: Berufskolleg - schulische Ausbildung zwei- oder dreijährig in der Berufsfachschule Anlage B oder C oder am Beruflichen Gymnasium
	 */
	public static readonly SCHUAUS : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("SCHUAUS", 9, );

	/**
	 * KAoA-Anschlussoption: Schulische Ausbildung in Berufen des Gesundheitswesens und der Altenpflege
	 */
	public static readonly SCHUGA : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("SCHUGA", 10, );

	/**
	 * KAoA-Anschlussoption: Berufsausbildung in einer außerbetrieblichen Einrichtung (BaE kooperativ/integrativ)
	 */
	public static readonly BAE : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("BAE", 11, );

	/**
	 * KAoA-Anschlussoption: Betriebliche Ausbildung in gesondert geregelten Berufen (Fachpraktikerausbildung/Werkerausbildung) für Jugendliche mit Behinderung
	 */
	public static readonly FACHAUS : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("FACHAUS", 12, );

	/**
	 * KAoA-Anschlussoption: Berufsvorbereitende Bildungsmaßnahme der Agentur für Arbeit (BvB), auch rehaspezifisch
	 */
	public static readonly BVB : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("BVB", 13, );

	/**
	 * KAoA-Anschlussoption: Einstiegsqualifizierung (EQ und EQ plus)
	 */
	public static readonly EQ : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("EQ", 14, );

	/**
	 * KAoA-Anschlussoption: Werkstattjahr.NRW
	 */
	public static readonly WERK : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("WERK", 15, );

	/**
	 * KAoA-Anschlussoption: Jugendwerkstatt
	 */
	public static readonly JUWERK : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("JUWERK", 16, );

	/**
	 * KAoA-Anschlussoption: weitere Maßnahmen gemäß SGB II/III/VIII (z. B. Aktivierungshilfen, Projekte der Jugendberufshilfe)
	 */
	public static readonly MASS : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("MASS", 17, );

	/**
	 * KAoA-Anschlussoption: Abendrealschule oder VHS zum Nachholen des Schulabschlusses
	 */
	public static readonly VHS : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("VHS", 18, );

	/**
	 * KAoA-Anschlussoption: betriebliche Langzeitpraktika (ohne EQ) (z.B. zum Erwerb der vollen FHR)
	 */
	public static readonly PRAKT : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("PRAKT", 19, );

	/**
	 * KAoA-Anschlussoption: Freiwilligendienste, Freiwilliger Wehrdienst/Laufbahn Bundeswehr und ähnliche Anschlussoptionen
	 */
	public static readonly FREI : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("FREI", 20, );

	/**
	 * KAoA-Anschlussoption: Sozialversicherungspflichtige Beschäftigung als ungelernte Kraft
	 */
	public static readonly SVP : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("SVP", 21, );

	/**
	 * KAoA-Anschlussoption: Außerbetriebliche Ausbildung für Menschen mit Behinderung im Berufsbildungswerk (BBW)
	 */
	public static readonly BBW : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("BBW", 22, );

	/**
	 * KAoA-Anschlussoption: Begleitete betriebliche Ausbildung (bbA) für Menschen mit Behinderung
	 */
	public static readonly BBA : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("BBA", 23, );

	/**
	 * KAoA-Anschlussoption: Unterstützte Beschäftigung (UB) für Menschen mit Behinderung
	 */
	public static readonly UB : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("UB", 24, );

	/**
	 * KAoA-Anschlussoption: Diagnose der Arbeitsmarktfähigkeit besonders betroffener behinderter Menschen (DIA-AM)
	 */
	public static readonly DIAAM : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("DIAAM", 25, );

	/**
	 * KAoA-Anschlussoption: Maßnahmen im Eingangsverfahren und im Berufsbildungsbereich in Werkstätten für behinderte Menschen (WfbM)
	 */
	public static readonly WFBM : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("WFBM", 26, );

	/**
	 * KAoA-Anschlussoption: Betreuung in Tagesförderstätten für schwerst- und schwermehrfachbehinderte Menschen
	 */
	public static readonly TAG : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("TAG", 27, );

	/**
	 * KAoA-Anschlussoption: Verbleib zu Hause bei einer Schwerst- und Schwermehrfachbehinderung
	 */
	public static readonly HAUSE : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("HAUSE", 28, );

	/**
	 * KAoA-Anschlussoption: weitere rehaspezifische Maßnahmen für Menschen mit Behinderung
	 */
	public static readonly REHA : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("REHA", 29, );

	/**
	 * KAoA-Anschlussoption: Minijob
	 */
	public static readonly MINI : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("MINI", 30, );

	/**
	 * KAoA-Anschlussoption: Schwangerschaft/Elternzeit
	 */
	public static readonly SCHWAN : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("SCHWAN", 31, );

	/**
	 * KAoA-Anschlussoption: Schulabsenz, daher Verbleib unbekannt
	 */
	public static readonly ABSENZ : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("ABSENZ", 32, );

	/**
	 * KAoA-Anschlussoption: Verbleib unbekannt - andere Gründe
	 */
	public static readonly UNBE : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("UNBE", 33, );

	/**
	 * KAoA-Anschlussoption: Noch kein Anschluss
	 */
	public static readonly NOKEAN : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("NOKEAN", 34, );

	/**
	 * KAoA-Anschlussoption: Duales Studium (inkl. Beamtenausbildung im gehobenen Dienst)
	 */
	public static readonly DUASTUD : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("DUASTUD", 35, );

	/**
	 * KAoA-Anschlussoption: Hochschulstudium
	 */
	public static readonly STUD : KAOAAnschlussoptionen = new KAOAAnschlussoptionen("STUD", 36, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		KAOAAnschlussoptionen.all_values_by_ordinal.push(this);
		KAOAAnschlussoptionen.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen>) : void {
		CoreTypeDataManager.putManager(KAOAAnschlussoptionen.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen> {
		return CoreTypeDataManager.getManager(KAOAAnschlussoptionen.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAAnschlussoptionen> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAAnschlussoptionen | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : KAOAAnschlussoptionenKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<KAOAAnschlussoptionenKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.kaoa.KAOAAnschlussoptionen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.kaoa.KAOAAnschlussoptionen', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<KAOAAnschlussoptionen>('de.svws_nrw.asd.types.kaoa.KAOAAnschlussoptionen');

}

export function cast_de_svws_nrw_asd_types_kaoa_KAOAAnschlussoptionen(obj : unknown) : KAOAAnschlussoptionen {
	return obj as KAOAAnschlussoptionen;
}
