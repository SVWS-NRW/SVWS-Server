import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { KAOAKategorie, cast_de_nrw_schule_svws_core_types_kaoa_KAOAKategorie } from './KAOAKategorie';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { Schulgliederung, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung } from '../schule/Schulgliederung';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { KAOAMerkmalEintrag, cast_de_nrw_schule_svws_core_data_kaoa_KAOAMerkmalEintrag } from '../../data/kaoa/KAOAMerkmalEintrag';
import { KAOAMerkmaleOptionsarten, cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmaleOptionsarten } from './KAOAMerkmaleOptionsarten';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';

export class KAOAMerkmal extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<KAOAMerkmal> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, KAOAMerkmal> = new Map<string, KAOAMerkmal>();

	/**
	 * KAoA-Merkmal: Schulische prozessorientierte Begleitung und Beratung 
	 */
	public static readonly SBO_2_1 : KAOAMerkmal = new KAOAMerkmal("SBO_2_1", 0, [new KAOAMerkmalEintrag(35, "SBO 2.1", "Schulische prozessorientierte Begleitung und Beratung", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: Berufsorientierende Angebote der Berufsberatung der Bundesagentur für Arbeit (BA) 
	 */
	public static readonly SBO_2_2 : KAOAMerkmal = new KAOAMerkmal("SBO_2_2", 1, [new KAOAMerkmalEintrag(36, "SBO 2.2", "Berufsorientierende Angebote der Berufsberatung der Bundesagentur für Arbeit (BA)", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: Individuelle Beratungsangebote außerschulischer Partner 
	 */
	public static readonly SBO_2_3 : KAOAMerkmal = new KAOAMerkmal("SBO_2_3", 2, [new KAOAMerkmalEintrag(37, "SBO 2.3", "Individuelle Beratungsangebote außerschulischer Partner", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: STAR - Berufswegekonferenz 
	 */
	public static readonly SBO_2_4 : KAOAMerkmal = new KAOAMerkmal("SBO_2_4", 3, [new KAOAMerkmalEintrag(38, "SBO 2.4", "STAR - Berufswegekonferenz", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: Einbindung von Eltern bzw. Erziehungsberechtigten 
	 */
	public static readonly SBO_2_5 : KAOAMerkmal = new KAOAMerkmal("SBO_2_5", 4, [new KAOAMerkmalEintrag(39, "SBO 2.5", "Einbindung von Eltern bzw. Erziehungsberechtigten", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: STAR - Einbindung von Eltern bzw. Erziehungsberechtigten 
	 */
	public static readonly SBO_2_6 : KAOAMerkmal = new KAOAMerkmal("SBO_2_6", 5, [new KAOAMerkmalEintrag(40, "SBO 2.6", "STAR - Einbindung von Eltern bzw. Erziehungsberechtigten", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: Portfolioinstrument 
	 */
	public static readonly SBO_3_4 : KAOAMerkmal = new KAOAMerkmal("SBO_3_4", 6, [new KAOAMerkmalEintrag(41, "SBO 3.4", "Portfolioinstrument", KAOAKategorie.SBO_3, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: Potenzialanalyse 1-tägig 
	 */
	public static readonly SBO_4_1 : KAOAMerkmal = new KAOAMerkmal("SBO_4_1", 7, [new KAOAMerkmalEintrag(42, "SBO 4.1", "Potenzialanalyse 1-tägig", KAOAKategorie.SBO_4, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Lernen und Emotionale soziale Entwicklung– 2-tägig 
	 */
	public static readonly SBO_4_2 : KAOAMerkmal = new KAOAMerkmal("SBO_4_2", 8, [new KAOAMerkmalEintrag(43, "SBO 4.2", "Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Lernen und Emotionale soziale Entwicklung– 2-tägig", KAOAKategorie.SBO_4, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: STAR – Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Geistige Entwicklung, Körperliche und motorische Entwicklung, Hören und Kommunikation und Sprache - 2-tägig 
	 */
	public static readonly SBO_4_3 : KAOAMerkmal = new KAOAMerkmal("SBO_4_3", 9, [new KAOAMerkmalEintrag(44, "SBO 4.3", "STAR – Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Geistige Entwicklung, Körperliche und motorische Entwicklung, Hören und Kommunikation und Sprache - 2-tägig", KAOAKategorie.SBO_4, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: STAR – Feststellung des funktionalen Sehvermögens im Förderschwerpunkt Sehen 
	 */
	public static readonly SBO_4_4 : KAOAMerkmal = new KAOAMerkmal("SBO_4_4", 10, [new KAOAMerkmalEintrag(45, "SBO 4.4", "STAR – Feststellung des funktionalen Sehvermögens im Förderschwerpunkt Sehen", KAOAKategorie.SBO_4, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: STAR – Potenzialanalyse im Förderschwerpunkt Sehen – 2-tägig 
	 */
	public static readonly SBO_4_5 : KAOAMerkmal = new KAOAMerkmal("SBO_4_5", 11, [new KAOAMerkmalEintrag(46, "SBO 4.5", "STAR – Potenzialanalyse im Förderschwerpunkt Sehen – 2-tägig", KAOAKategorie.SBO_4, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: Berufsfelderkundungen 
	 */
	public static readonly SBO_5_1 : KAOAMerkmal = new KAOAMerkmal("SBO_5_1", 12, [new KAOAMerkmalEintrag(47, "SBO 5.1", "Berufsfelderkundungen", KAOAKategorie.SBO_5, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: STAR – Berufsfelderkundungen 
	 */
	public static readonly SBO_5_2 : KAOAMerkmal = new KAOAMerkmal("SBO_5_2", 13, [new KAOAMerkmalEintrag(48, "SBO 5.2", "STAR – Berufsfelderkundungen", KAOAKategorie.SBO_5, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: STAR – Arbeitsplatzbezogenes Kommunikationstraining I im Förderschwerpunkt Hören und Kommunikation 
	 */
	public static readonly SBO_5_3 : KAOAMerkmal = new KAOAMerkmal("SBO_5_3", 14, [new KAOAMerkmalEintrag(49, "SBO 5.3", "STAR – Arbeitsplatzbezogenes Kommunikationstraining I im Förderschwerpunkt Hören und Kommunikation", KAOAKategorie.SBO_5, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: STAR - Berufsorientierungsseminar 
	 */
	public static readonly SBO_5_4 : KAOAMerkmal = new KAOAMerkmal("SBO_5_4", 15, [new KAOAMerkmalEintrag(50, "SBO 5.4", "STAR - Berufsorientierungsseminar", KAOAKategorie.SBO_5, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: Betriebspraktika in der Sekundarstufe I (ggf. 1 Woche verlagert aus der Oberstufe) 
	 */
	public static readonly SBO_6_1 : KAOAMerkmal = new KAOAMerkmal("SBO_6_1", 16, [new KAOAMerkmalEintrag(51, "SBO 6.1", "Betriebspraktika in der Sekundarstufe I (ggf. 1 Woche verlagert aus der Oberstufe)", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: STAR – Intensivtraining arbeitsrelevanter sozialer Kompetenzen (TASK) 
	 */
	public static readonly SBO_6_2 : KAOAMerkmal = new KAOAMerkmal("SBO_6_2", 17, [new KAOAMerkmalEintrag(52, "SBO 6.2", "STAR – Intensivtraining arbeitsrelevanter sozialer Kompetenzen (TASK)", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: STAR – Betriebspraktikum im Block 
	 */
	public static readonly SBO_6_3 : KAOAMerkmal = new KAOAMerkmal("SBO_6_3", 18, [new KAOAMerkmalEintrag(53, "SBO 6.3", "STAR – Betriebspraktikum im Block", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: Praxiskurse 
	 */
	public static readonly SBO_6_4 : KAOAMerkmal = new KAOAMerkmal("SBO_6_4", 19, [new KAOAMerkmalEintrag(54, "SBO 6.4", "Praxiskurse", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: Langzeitpraktikum 
	 */
	public static readonly SBO_6_5 : KAOAMerkmal = new KAOAMerkmal("SBO_6_5", 20, [new KAOAMerkmalEintrag(55, "SBO 6.5", "Langzeitpraktikum", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: STAR – Betriebspraktikum in Langzeit 
	 */
	public static readonly SBO_6_6 : KAOAMerkmal = new KAOAMerkmal("SBO_6_6", 21, [new KAOAMerkmalEintrag(56, "SBO 6.6", "STAR – Betriebspraktikum in Langzeit", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: KAoA-kompakt 
	 */
	public static readonly SBO_7_1 : KAOAMerkmal = new KAOAMerkmal("SBO_7_1", 22, [new KAOAMerkmalEintrag(57, "SBO 7.1", "KAoA-kompakt", KAOAKategorie.SBO_7, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07), null, null)]);

	/**
	 * KAoA-Merkmal: Standortbestimmung - Reflexionsworkshop Sek. II 
	 */
	public static readonly SBO_8_1 : KAOAMerkmal = new KAOAMerkmal("SBO_8_1", 23, [new KAOAMerkmalEintrag(58, "SBO 8.1", "Standortbestimmung - Reflexionsworkshop Sek. II", KAOAKategorie.SBO_8, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: Stärkung der Entscheidungskompetenz I – Sek. II 
	 */
	public static readonly SBO_8_2 : KAOAMerkmal = new KAOAMerkmal("SBO_8_2", 24, [new KAOAMerkmalEintrag(59, "SBO 8.2", "Stärkung der Entscheidungskompetenz I – Sek. II", KAOAKategorie.SBO_8, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: Praxiselemente in Betrieben, Hochschulen, Institutionen 
	 */
	public static readonly SBO_9_1 : KAOAMerkmal = new KAOAMerkmal("SBO_9_1", 25, [new KAOAMerkmalEintrag(60, "SBO 9.1", "Praxiselemente in Betrieben, Hochschulen, Institutionen", KAOAKategorie.SBO_9, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: Studienorientierung 
	 */
	public static readonly SBO_9_2 : KAOAMerkmal = new KAOAMerkmal("SBO_9_2", 26, [new KAOAMerkmalEintrag(61, "SBO 9.2", "Studienorientierung", KAOAKategorie.SBO_9, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: Stärkung der Entscheidungskompetenz II - Sek. II 
	 */
	public static readonly SBO_9_3 : KAOAMerkmal = new KAOAMerkmal("SBO_9_3", 27, [new KAOAMerkmalEintrag(62, "SBO 9.3", "Stärkung der Entscheidungskompetenz II - Sek. II", KAOAKategorie.SBO_9, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: Bewerbungsphase 
	 */
	public static readonly SBO_10_1 : KAOAMerkmal = new KAOAMerkmal("SBO_10_1", 28, [new KAOAMerkmalEintrag(63, "SBO 10.1", "Bewerbungsphase", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: STAR – Arbeitsplatzbezogenes Kommunikationstraining II im Förderschwerpunkt Hören und Kommunikation 
	 */
	public static readonly SBO_10_2 : KAOAMerkmal = new KAOAMerkmal("SBO_10_2", 29, [new KAOAMerkmalEintrag(64, "SBO 10.2", "STAR – Arbeitsplatzbezogenes Kommunikationstraining II im Förderschwerpunkt Hören und Kommunikation", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: STAR – Betriebsnahes Bewerbungstraining/Umgang mit Dolmetschenden und Technik im Förderschwerpunkt Hören und Kommunikation 
	 */
	public static readonly SBO_10_3 : KAOAMerkmal = new KAOAMerkmal("SBO_10_3", 30, [new KAOAMerkmalEintrag(65, "SBO 10.3", "STAR – Betriebsnahes Bewerbungstraining/Umgang mit Dolmetschenden und Technik im Förderschwerpunkt Hören und Kommunikation", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: Übergangsbegleitung 
	 */
	public static readonly SBO_10_4 : KAOAMerkmal = new KAOAMerkmal("SBO_10_4", 31, [new KAOAMerkmalEintrag(66, "SBO 10.4", "Übergangsbegleitung", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: STAR - Übergangsbegleitung 
	 */
	public static readonly SBO_10_5 : KAOAMerkmal = new KAOAMerkmal("SBO_10_5", 32, [new KAOAMerkmalEintrag(67, "SBO 10.5", "STAR - Übergangsbegleitung", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(), null, null)]);

	/**
	 * KAoA-Merkmal: Anschlussvereinbarung 
	 */
	public static readonly SBO_10_6 : KAOAMerkmal = new KAOAMerkmal("SBO_10_6", 33, [new KAOAMerkmalEintrag(68, "SBO 10.6", "Anschlussvereinbarung", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * KAoA-Merkmal: Koordinierte Übergangsgestaltung 
	 */
	public static readonly SBO_10_7 : KAOAMerkmal = new KAOAMerkmal("SBO_10_7", 34, [new KAOAMerkmalEintrag(69, "SBO 10.7", "Koordinierte Übergangsgestaltung", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. 
	 */
	public static VERSION : number = 1;

	/**
	 * Die aktuellsten Daten des KAoA-Merkmals 
	 */
	public readonly daten : KAOAMerkmalEintrag;

	/**
	 * Die Historie mit den Einträgen des KAoA-Merkmals 
	 */
	public readonly historie : Array<KAOAMerkmalEintrag>;

	/**
	 * Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind. 
	 */
	private static readonly _statusByID : HashMap<number, KAOAMerkmal> = new HashMap();

	/**
	 * Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind. 
	 */
	private static readonly _statusByKuerzel : HashMap<string, KAOAMerkmal> = new HashMap();

	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 * 
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAMerkmalEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<KAOAMerkmalEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		KAOAMerkmal.all_values_by_ordinal.push(this);
		KAOAMerkmal.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von der ID auf das zugehörige Merkmal zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von der ID auf das zugehörige Merkmal
	 */
	private static getMapStatusByID() : HashMap<number, KAOAMerkmal> {
		if (KAOAMerkmal._statusByID.size() === 0) 
			for (let g of KAOAMerkmal.values()) 
				KAOAMerkmal._statusByID.put(g.daten.id, g);
		return KAOAMerkmal._statusByID;
	}

	/**
	 * Gibt eine Map von dem Kürzel auf das zugehörige Merkmal zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von dem Kürzel auf das zugehörige Merkmal
	 */
	private static getMapStatusByKuerzel() : HashMap<string, KAOAMerkmal> {
		if (KAOAMerkmal._statusByKuerzel.size() === 0) 
			for (let g of KAOAMerkmal.values()) 
				KAOAMerkmal._statusByKuerzel.put(g.daten.kuerzel, g);
		return KAOAMerkmal._statusByKuerzel;
	}

	/**
	 * Gibt das Merkmal anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID des Merkmals
	 * 
	 * @return das Merkmal oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : KAOAMerkmal | null {
		return KAOAMerkmal.getMapStatusByID().get(id);
	}

	/**
	 * Gibt das Merkmal anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel des Merkmals
	 * 
	 * @return das Merkmal oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : KAOAMerkmal | null {
		return KAOAMerkmal.getMapStatusByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : string {
		return this.__name;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof KAOAMerkmal))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : KAOAMerkmal) : number {
		return this.__ordinal - other.__ordinal;
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
		let tmp : KAOAMerkmal | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.kaoa.KAOAMerkmal'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmal(obj : unknown) : KAOAMerkmal {
	return obj as KAOAMerkmal;
}
