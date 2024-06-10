import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Schulstufe } from '../../../core/types/schule/Schulstufe';
import { HashMap } from '../../../java/util/HashMap';
import { KAOAAnschlussoptionEintrag } from '../../../core/data/kaoa/KAOAAnschlussoptionEintrag';
import { Arrays } from '../../../java/util/Arrays';
import { KAOAZusatzmerkmal } from '../../../core/types/kaoa/KAOAZusatzmerkmal';

export class KAOAAnschlussoption extends JavaEnum<KAOAAnschlussoption> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAAnschlussoption> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAAnschlussoption> = new Map<string, KAOAAnschlussoption>();

	/**
	 * KAoA-Anschlussoption: Berufspraxisstufe einer Förderschule für Geistige Entwicklung
	 */
	public static readonly BE : KAOAAnschlussoption = new KAOAAnschlussoption("BE", 0, [new KAOAAnschlussoptionEintrag(22, "BE", "Berufspraxisstufe einer Förderschule für Geistige Entwicklung", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3), null, null)]);

	/**
	 * KAoA-Anschlussoption: Gymnasiale Oberstufe der Gesamtschule oder des Gymnasiums
	 */
	public static readonly GY : KAOAAnschlussoption = new KAOAAnschlussoption("GY", 1, [new KAOAAnschlussoptionEintrag(23, "GY", "Gymnasiale Oberstufe der Gesamtschule oder des Gymnasiums", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_8), null, null)]);

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Einjährige Ausbildungsvorbereitung in Vollzeit (AV) - Anlage A
	 */
	public static readonly AV : KAOAAnschlussoption = new KAOAAnschlussoption("AV", 2, [new KAOAAnschlussoptionEintrag(24, "AV", "Berufskolleg - Einjährige Ausbildungsvorbereitung in Vollzeit (AV) - Anlage A", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5), null, null)]);

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Einjährige Berufsfachschule Typ I (BFS I) - Anlage B
	 */
	public static readonly BFSI : KAOAAnschlussoption = new KAOAAnschlussoption("BFSI", 3, [new KAOAAnschlussoptionEintrag(25, "BFSI", "Berufskolleg - Einjährige Berufsfachschule Typ I (BFS I) - Anlage B", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_5), null, null)]);

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Einjährige Berufsfachschule Typ II (BFS II) - Anlage B
	 */
	public static readonly BFSII : KAOAAnschlussoption = new KAOAAnschlussoption("BFSII", 4, [new KAOAAnschlussoptionEintrag(26, "BFSII", "Berufskolleg - Einjährige Berufsfachschule Typ II (BFS II) - Anlage B", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10), null, null)]);

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Zweijährige [Höhere] Berufsfachschule - Anlage C
	 */
	public static readonly HOEBFS : KAOAAnschlussoption = new KAOAAnschlussoption("HOEBFS", 5, [new KAOAAnschlussoptionEintrag(27, "HÖBFS", "Berufskolleg - Zweijährige [Höhere] Berufsfachschule - Anlage C", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10), null, null)]);

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Zweijährige Fachoberschule (FOS 11/12) - Anlage C
	 */
	public static readonly FOS : KAOAAnschlussoption = new KAOAAnschlussoption("FOS", 6, [new KAOAAnschlussoptionEintrag(28, "FOS", "Berufskolleg - Zweijährige Fachoberschule (FOS 11/12) - Anlage C", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10), null, null)]);

	/**
	 * KAoA-Anschlussoption: Berufskolleg - Berufliches Gymnasium - Anlage D
	 */
	public static readonly BERUFGY : KAOAAnschlussoption = new KAOAAnschlussoption("BERUFGY", 7, [new KAOAAnschlussoptionEintrag(29, "BERUFGY", "Berufskolleg - Berufliches Gymnasium - Anlage D", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12), null, null)]);

	/**
	 * KAoA-Anschlussoption: Duale Berufsausbildung (inkl. Beamtenlaufbahn im mittleren Dienst)
	 */
	public static readonly DUAL : KAOAAnschlussoption = new KAOAAnschlussoption("DUAL", 8, [new KAOAAnschlussoptionEintrag(30, "DUAL", "Duale Berufsausbildung (inkl. Beamtenlaufbahn im mittleren Dienst)", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13), null, null)]);

	/**
	 * KAoA-Anschlussoption: Berufskolleg - schulische Ausbildung zwei- oder dreijährig in der Berufsfachschule Anlage B oder C oder am Beruflichen Gymnasium
	 */
	public static readonly SCHUAUS : KAOAAnschlussoption = new KAOAAnschlussoption("SCHUAUS", 9, [new KAOAAnschlussoptionEintrag(31, "SCHUAUS", "Berufskolleg - schulische Ausbildung zwei- oder dreijährig in der Berufsfachschule Anlage B oder C oder am Beruflichen Gymnasium", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13), null, null)]);

	/**
	 * KAoA-Anschlussoption: Schulische Ausbildung in Berufen des Gesundheitswesens und der Altenpflege
	 */
	public static readonly SCHUGA : KAOAAnschlussoption = new KAOAAnschlussoption("SCHUGA", 10, [new KAOAAnschlussoptionEintrag(32, "SCHUGA", "Schulische Ausbildung in Berufen des Gesundheitswesens und der Altenpflege", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13), null, null)]);

	/**
	 * KAoA-Anschlussoption: Berufsausbildung in einer außerbetrieblichen Einrichtung (BaE kooperativ/integrativ)
	 */
	public static readonly BAE : KAOAAnschlussoption = new KAOAAnschlussoption("BAE", 11, [new KAOAAnschlussoptionEintrag(33, "BAE", "Berufsausbildung in einer außerbetrieblichen Einrichtung (BaE kooperativ/integrativ)", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5), null, null)]);

	/**
	 * KAoA-Anschlussoption: Betriebliche Ausbildung in gesondert geregelten Berufen (Fachpraktikerausbildung/Werkerausbildung) für Jugendliche mit Behinderung
	 */
	public static readonly FACHAUS : KAOAAnschlussoption = new KAOAAnschlussoption("FACHAUS", 12, [new KAOAAnschlussoptionEintrag(34, "FACHAUS", "Betriebliche Ausbildung in gesondert geregelten Berufen (Fachpraktikerausbildung/Werkerausbildung) für Jugendliche mit Behinderung", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4), null, null)]);

	/**
	 * KAoA-Anschlussoption: Berufsvorbereitende Bildungsmaßnahme der Agentur für Arbeit (BvB), auch rehaspezifisch
	 */
	public static readonly BVB : KAOAAnschlussoption = new KAOAAnschlussoption("BVB", 13, [new KAOAAnschlussoptionEintrag(35, "BVB", "Berufsvorbereitende Bildungsmaßnahme der Agentur für Arbeit (BvB), auch rehaspezifisch", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10), null, null)]);

	/**
	 * KAoA-Anschlussoption: Einstiegsqualifizierung (EQ und EQ plus)
	 */
	public static readonly EQ : KAOAAnschlussoption = new KAOAAnschlussoption("EQ", 14, [new KAOAAnschlussoptionEintrag(36, "EQ", "Einstiegsqualifizierung (EQ und EQ plus)", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11), null, null)]);

	/**
	 * KAoA-Anschlussoption: Werkstattjahr.NRW
	 */
	public static readonly WERK : KAOAAnschlussoption = new KAOAAnschlussoption("WERK", 15, [new KAOAAnschlussoptionEintrag(37, "WERK", "Werkstattjahr.NRW", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10), null, null)]);

	/**
	 * KAoA-Anschlussoption: Jugendwerkstatt
	 */
	public static readonly JUWERK : KAOAAnschlussoption = new KAOAAnschlussoption("JUWERK", 16, [new KAOAAnschlussoptionEintrag(38, "JUWERK", "Jugendwerkstatt", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10), null, null)]);

	/**
	 * KAoA-Anschlussoption: weitere Maßnahmen gemäß SGB II/III/VIII (z. B. Aktivierungshilfen, Projekte der Jugendberufshilfe)
	 */
	public static readonly MASS : KAOAAnschlussoption = new KAOAAnschlussoption("MASS", 17, [new KAOAAnschlussoptionEintrag(39, "MASS", "weitere Maßnahmen gemäß SGB II/III/VIII (z. B. Aktivierungshilfen, Projekte der Jugendberufshilfe) ", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10), null, null)]);

	/**
	 * KAoA-Anschlussoption: Abendrealschule oder VHS zum Nachholen des Schulabschlusses
	 */
	public static readonly VHS : KAOAAnschlussoption = new KAOAAnschlussoption("VHS", 18, [new KAOAAnschlussoptionEintrag(40, "VHS", "Abendrealschule oder VHS zum Nachholen des Schulabschlusses", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6), null, null)]);

	/**
	 * KAoA-Anschlussoption: betriebliche Langzeitpraktika (ohne EQ) (z.B. zum Erwerb der vollen FHR)
	 */
	public static readonly PRAKT : KAOAAnschlussoption = new KAOAAnschlussoption("PRAKT", 19, [new KAOAAnschlussoptionEintrag(41, "PRAKT", "betriebliche Langzeitpraktika (ohne EQ) (z.B. zum Erwerb der vollen FHR)", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13), null, null)]);

	/**
	 * KAoA-Anschlussoption: Freiwilligendienste, Freiwilliger Wehrdienst/Laufbahn Bundeswehr und ähnliche Anschlussoptionen
	 */
	public static readonly FREI : KAOAAnschlussoption = new KAOAAnschlussoption("FREI", 20, [new KAOAAnschlussoptionEintrag(42, "FREI", "Freiwilligendienste, Freiwilliger Wehrdienst/Laufbahn Bundeswehr und ähnliche Anschlussoptionen", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13), null, null)]);

	/**
	 * KAoA-Anschlussoption: Sozialversicherungspflichtige Beschäftigung als ungelernte Kraft
	 */
	public static readonly SVP : KAOAAnschlussoption = new KAOAAnschlussoption("SVP", 21, [new KAOAAnschlussoptionEintrag(43, "SVP", "Sozialversicherungspflichtige Beschäftigung als ungelernte Kraft", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13), null, null)]);

	/**
	 * KAoA-Anschlussoption: Außerbetriebliche Ausbildung für Menschen mit Behinderung im Berufsbildungswerk (BBW)
	 */
	public static readonly BBW : KAOAAnschlussoption = new KAOAAnschlussoption("BBW", 22, [new KAOAAnschlussoptionEintrag(44, "BBW", "Außerbetriebliche Ausbildung für Menschen mit Behinderung im Berufsbildungswerk (BBW)", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4), null, null)]);

	/**
	 * KAoA-Anschlussoption: Begleitete betriebliche Ausbildung (bbA) für Menschen mit Behinderung
	 */
	public static readonly BBA : KAOAAnschlussoption = new KAOAAnschlussoption("BBA", 23, [new KAOAAnschlussoptionEintrag(45, "BBA", "Begleitete betriebliche Ausbildung (bbA) für Menschen mit Behinderung", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4), null, null)]);

	/**
	 * KAoA-Anschlussoption: Unterstützte Beschäftigung (UB) für Menschen mit Behinderung
	 */
	public static readonly UB : KAOAAnschlussoption = new KAOAAnschlussoption("UB", 24, [new KAOAAnschlussoptionEintrag(46, "UB", "Unterstützte Beschäftigung (UB) für Menschen mit Behinderung", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4), null, null)]);

	/**
	 * KAoA-Anschlussoption: Diagnose der Arbeitsmarktfähigkeit besonders betroffener behinderter Menschen (DIA-AM)
	 */
	public static readonly DIAAM : KAOAAnschlussoption = new KAOAAnschlussoption("DIAAM", 25, [new KAOAAnschlussoptionEintrag(47, "DIAAM", "Diagnose der Arbeitsmarktfähigkeit besonders betroffener behinderter Menschen (DIA-AM)", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4), null, null)]);

	/**
	 * KAoA-Anschlussoption: Maßnahmen im Eingangsverfahren und im Berufsbildungsbereich in Werkstätten für behinderte Menschen (WfbM)
	 */
	public static readonly WFBM : KAOAAnschlussoption = new KAOAAnschlussoption("WFBM", 26, [new KAOAAnschlussoptionEintrag(48, "WFBM", "Maßnahmen im Eingangsverfahren und im Berufsbildungsbereich in Werkstätten für behinderte Menschen (WfbM)", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4), null, null)]);

	/**
	 * KAoA-Anschlussoption: Betreuung in Tagesförderstätten für schwerst- und schwermehrfachbehinderte Menschen
	 */
	public static readonly TAG : KAOAAnschlussoption = new KAOAAnschlussoption("TAG", 27, [new KAOAAnschlussoptionEintrag(49, "TAG", "Betreuung in Tagesförderstätten für schwerst- und schwermehrfachbehinderte Menschen", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3), null, null)]);

	/**
	 * KAoA-Anschlussoption: Verbleib zu Hause bei einer Schwerst- und Schwermehrfachbehinderung
	 */
	public static readonly HAUSE : KAOAAnschlussoption = new KAOAAnschlussoption("HAUSE", 28, [new KAOAAnschlussoptionEintrag(50, "HAUSE", "Verbleib zu Hause bei einer Schwerst- und Schwermehrfachbehinderung", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3), null, null)]);

	/**
	 * KAoA-Anschlussoption: weitere rehaspezifische Maßnahmen für Menschen mit Behinderung
	 */
	public static readonly REHA : KAOAAnschlussoption = new KAOAAnschlussoption("REHA", 29, [new KAOAAnschlussoptionEintrag(51, "REHA", "weitere rehaspezifische Maßnahmen für Menschen mit Behinderung", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3), null, null)]);

	/**
	 * KAoA-Anschlussoption: Minijob
	 */
	public static readonly MINI : KAOAAnschlussoption = new KAOAAnschlussoption("MINI", 30, [new KAOAAnschlussoptionEintrag(52, "MINI", "Minijob", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11), null, null)]);

	/**
	 * KAoA-Anschlussoption: Schwangerschaft/Elternzeit
	 */
	public static readonly SCHWAN : KAOAAnschlussoption = new KAOAAnschlussoption("SCHWAN", 31, [new KAOAAnschlussoptionEintrag(53, "SCHWAN", "Schwangerschaft/Elternzeit", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11), null, null)]);

	/**
	 * KAoA-Anschlussoption: Schulabsenz, daher Verbleib unbekannt
	 */
	public static readonly ABSENZ : KAOAAnschlussoption = new KAOAAnschlussoption("ABSENZ", 32, [new KAOAAnschlussoptionEintrag(54, "ABSENZ", "Schulabsenz, daher Verbleib unbekannt", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11), null, null)]);

	/**
	 * KAoA-Anschlussoption: Verbleib unbekannt - andere Gründe
	 */
	public static readonly UNBE : KAOAAnschlussoption = new KAOAAnschlussoption("UNBE", 33, [new KAOAAnschlussoptionEintrag(55, "UNBE", "Verbleib unbekannt - andere Gründe", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13), null, null)]);

	/**
	 * KAoA-Anschlussoption: Noch kein Anschluss
	 */
	public static readonly NOKEAN : KAOAAnschlussoption = new KAOAAnschlussoption("NOKEAN", 34, [new KAOAAnschlussoptionEintrag(56, "NOKEAN", "Noch kein Anschluss", Arrays.asList(Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13), null, null)]);

	/**
	 * KAoA-Anschlussoption: Duales Studium (inkl. Beamtenausbildung im gehobenen Dienst)
	 */
	public static readonly DUASTUD : KAOAAnschlussoption = new KAOAAnschlussoption("DUASTUD", 35, [new KAOAAnschlussoptionEintrag(57, "DUASTUD", "Duales Studium (inkl. Beamtenausbildung im gehobenen Dienst)", Arrays.asList(Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13), null, null)]);

	/**
	 * KAoA-Anschlussoption: Hochschulstudium
	 */
	public static readonly STUD : KAOAAnschlussoption = new KAOAAnschlussoption("STUD", 36, [new KAOAAnschlussoptionEintrag(58, "STUD", "Hochschulstudium", Arrays.asList(Schulstufe.SEKUNDARSTUFE_II), Arrays.asList(KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Die aktuellsten Daten der KAoA-Anschlussoption
	 */
	public readonly daten : KAOAAnschlussoptionEintrag;

	/**
	 * Die Historie mit den Einträgen der KAoA-Anschlussoption
	 */
	public readonly historie : Array<KAOAAnschlussoptionEintrag>;

	/**
	 * Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _statusByID : HashMap<number, KAOAAnschlussoption> = new HashMap<number, KAOAAnschlussoption>();

	/**
	 * Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind.
	 */
	private static readonly _statusByKuerzel : HashMap<string, KAOAAnschlussoption> = new HashMap<string, KAOAAnschlussoption>();

	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 *
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAAnschlussoptionEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<KAOAAnschlussoptionEintrag>) {
		super(name, ordinal);
		KAOAAnschlussoption.all_values_by_ordinal.push(this);
		KAOAAnschlussoption.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von der ID auf die zugehörige Anschlussoption zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von der ID auf die zugehörige Anschlussoption
	 */
	private static getMapStatusByID() : HashMap<number, KAOAAnschlussoption> {
		if (KAOAAnschlussoption._statusByID.size() === 0)
			for (const g of KAOAAnschlussoption.values())
				KAOAAnschlussoption._statusByID.put(g.daten.id, g);
		return KAOAAnschlussoption._statusByID;
	}

	/**
	 * Gibt eine Map von dem Kürzel auf die zugehörige Anschlussoption zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von dem Kürzel auf die zugehörige Anschlussoption
	 */
	private static getMapStatusByKuerzel() : HashMap<string, KAOAAnschlussoption> {
		if (KAOAAnschlussoption._statusByKuerzel.size() === 0)
			for (const g of KAOAAnschlussoption.values())
				KAOAAnschlussoption._statusByKuerzel.put(g.daten.kuerzel, g);
		return KAOAAnschlussoption._statusByKuerzel;
	}

	/**
	 * Gibt die Anschlussoption anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID der Anschlussoption
	 *
	 * @return die Anschlussoption oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number | null) : KAOAAnschlussoption | null {
		return KAOAAnschlussoption.getMapStatusByID().get(id);
	}

	/**
	 * Gibt die Anschlussoption anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Anschlussoption
	 *
	 * @return die Anschlussoption oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : KAOAAnschlussoption | null {
		return KAOAAnschlussoption.getMapStatusByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAAnschlussoption> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAAnschlussoption | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.kaoa.KAOAAnschlussoption';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kaoa.KAOAAnschlussoption', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kaoa_KAOAAnschlussoption(obj : unknown) : KAOAAnschlussoption {
	return obj as KAOAAnschlussoption;
}
