import { JavaEnum } from '../../../java/lang/JavaEnum';
import { KAOAZusatzmerkmalKatalogEintrag } from '../../../asd/data/kaoa/KAOAZusatzmerkmalKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class KAOAZusatzmerkmal extends JavaEnum<KAOAZusatzmerkmal> implements CoreType<KAOAZusatzmerkmalKatalogEintrag, KAOAZusatzmerkmal> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAZusatzmerkmal> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAZusatzmerkmal> = new Map<string, KAOAZusatzmerkmal>();

	/**
	 * KAoA-Zusatzmerkmal: Schulisches individuelles Beratungsgespräch durchgeführt
	 */
	public static readonly SBO_2_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_1_1", 0, );

	/**
	 * KAoA-Zusatzmerkmal: Schulisches individuelles Beratungsgespräch nicht durchgeführt
	 */
	public static readonly SBO_2_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_1_2", 1, );

	/**
	 * KAoA-Zusatzmerkmal: Teilnahme an einem berufsorientierenden Angebote der Berufsberatung
	 */
	public static readonly SBO_2_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_2_1", 2, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an einem berufsorientierenden Angebote der Berufsberatung
	 */
	public static readonly SBO_2_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_2_2", 3, );

	/**
	 * KAoA-Zusatzmerkmal: Berufsberatung der Agentur für Arbeit (BA)
	 */
	public static readonly SBO_2_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_1", 4, );

	/**
	 * KAoA-Zusatzmerkmal: Beratungsgespräch der Jugendberufsagentur
	 */
	public static readonly SBO_2_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_2", 5, );

	/**
	 * KAoA-Zusatzmerkmal: Beratungsgespräch des Jobcenters
	 */
	public static readonly SBO_2_3_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_3", 6, );

	/**
	 * KAoA-Zusatzmerkmal: Beratungsgespräch Jugendsozialarbeit
	 */
	public static readonly SBO_2_3_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_4", 7, );

	/**
	 * KAoA-Zusatzmerkmal: Beratungsgespräch eines anderen außerschulischen Partners
	 */
	public static readonly SBO_2_3_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_5", 8, );

	/**
	 * KAoA-Zusatzmerkmal: Kein Beratungskontakt zu einem außerschulischen Partner
	 */
	public static readonly SBO_2_3_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_6", 9, );

	/**
	 * KAoA-Zusatzmerkmal: STAR - Berufswegekonferenz durchgeführt
	 */
	public static readonly SBO_2_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_4_1", 10, );

	/**
	 * KAoA-Zusatzmerkmal: STAR - Berufswegekonferenz nicht durchgeführt
	 */
	public static readonly SBO_2_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_4_2", 11, );

	/**
	 * KAoA-Zusatzmerkmal: Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen
	 */
	public static readonly SBO_2_5_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_5_1", 12, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen
	 */
	public static readonly SBO_2_5_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_5_2", 13, );

	/**
	 * KAoA-Zusatzmerkmal: Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen
	 */
	public static readonly SBO_2_6_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_6_1", 14, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen
	 */
	public static readonly SBO_2_6_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_6_2", 15, );

	/**
	 * KAoA-Zusatzmerkmal: Berufwahlpass NRW SekI/II erhalten
	 */
	public static readonly SBO_3_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_3_4_1", 16, );

	/**
	 * KAoA-Zusatzmerkmal: Berufwahlpass NRW Leichte Sprache erhalten
	 */
	public static readonly SBO_3_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_3_4_2", 17, );

	/**
	 * KAoA-Zusatzmerkmal: Berufswahlpass NRW kompakt erhalten
	 */
	public static readonly SBO_3_4_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_3_4_3", 18, );

	/**
	 * KAoA-Zusatzmerkmal: anderes Portfolioinstrument  erhalten
	 */
	public static readonly SBO_3_4_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_3_4_4", 19, );

	/**
	 * KAoA-Zusatzmerkmal: kein Portfolioinstrument erhalten
	 */
	public static readonly SBO_3_4_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_3_4_5", 20, );

	/**
	 * KAoA-Zusatzmerkmal: An der Potenzialanalyse teilgenommen
	 */
	public static readonly SBO_4_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_1_1", 21, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse
	 */
	public static readonly SBO_4_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_1_2", 22, );

	/**
	 * KAoA-Zusatzmerkmal: An der Potenzialanalyse teilgenommen
	 */
	public static readonly SBO_4_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_2_1", 23, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse
	 */
	public static readonly SBO_4_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_2_2", 24, );

	/**
	 * KAoA-Zusatzmerkmal: An der STAR-Potenzialanalyse teilgenommen
	 */
	public static readonly SBO_4_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_3_1", 25, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse
	 */
	public static readonly SBO_4_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_3_2", 26, );

	/**
	 * KAoA-Zusatzmerkmal: An der Festellung des funktionalen Sehvermögens teilgenommen
	 */
	public static readonly SBO_4_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_4_1", 27, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an der Feststellung des funktionalen Sehrvermögens
	 */
	public static readonly SBO_4_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_4_2", 28, );

	/**
	 * KAoA-Zusatzmerkmal: An der STAR-Potenzialanalyse teilgenommen
	 */
	public static readonly SBO_4_5_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_5_1", 29, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse
	 */
	public static readonly SBO_4_5_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_5_2", 30, );

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 1. Tag
	 */
	public static readonly SBO_5_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_1", 31, );

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 2. Tag
	 */
	public static readonly SBO_5_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_2", 32, );

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 3. Tag
	 */
	public static readonly SBO_5_1_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_3", 33, );

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - mehr als drei Tage
	 */
	public static readonly SBO_5_1_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_4", 34, );

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 1. Tag
	 */
	public static readonly SBO_5_1_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_5", 35, );

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 2. Tag
	 */
	public static readonly SBO_5_1_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_6", 36, );

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 3. Tag
	 */
	public static readonly SBO_5_1_7 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_7", 37, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an den betrieblichen oder trägergestütztenBerufsfelderkundungen
	 */
	public static readonly SBO_5_1_8 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_8", 38, );

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 1. Tag
	 */
	public static readonly SBO_5_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_1", 39, );

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 2. Tag
	 */
	public static readonly SBO_5_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_2", 40, );

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 3. Tag
	 */
	public static readonly SBO_5_2_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_3", 41, );

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - weitere Tage
	 */
	public static readonly SBO_5_2_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_4", 42, );

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 1. Tag
	 */
	public static readonly SBO_5_2_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_5", 43, );

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 2. Tag
	 */
	public static readonly SBO_5_2_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_6", 44, );

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 3. Tag
	 */
	public static readonly SBO_5_2_7 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_7", 45, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an den betrieblichen oder trägergestützten STAR - Berufsfelderkundungen
	 */
	public static readonly SBO_5_2_8 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_8", 46, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Arbeitsplatzbezogenes Kommunikationstraining I teilgenommen
	 */
	public static readonly SBO_5_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_3_1", 47, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Arbeitsplatzbezogenes Kommunikationstraining I
	 */
	public static readonly SBO_5_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_3_2", 48, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Berufsorientierungsseminar teilgenommen
	 */
	public static readonly SBO_5_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_4_1", 49, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Berufsorientierungsseminar
	 */
	public static readonly SBO_5_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_4_2", 50, );

	/**
	 * KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 1 Woche
	 */
	public static readonly SBO_6_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_1_1", 51, );

	/**
	 * KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 2 Wochen
	 */
	public static readonly SBO_6_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_1_2", 52, );

	/**
	 * KAoA-Zusatzmerkmal: Am Betriebspraktikum Sek I teilgenommen - mehr als zwei Wochen
	 */
	public static readonly SBO_6_1_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_1_3", 53, );

	/**
	 * KAoA-Zusatzmerkmal: Zusätzliche Praktika wie z. B. Schnupperpraktika
	 */
	public static readonly SBO_6_1_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_1_4", 54, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am Betriebspraktikum
	 */
	public static readonly SBO_6_1_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_1_5", 55, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am Intensivtraining TASK
	 */
	public static readonly SBO_6_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_2_2", 56, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - eine Woche teilgenommen
	 */
	public static readonly SBO_6_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_1", 57, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - zwei Wochen teilgenommen
	 */
	public static readonly SBO_6_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_2", 58, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - drei Wochen teilgenommen
	 */
	public static readonly SBO_6_3_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_3", 59, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - vier Wochen teilgenommen
	 */
	public static readonly SBO_6_3_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_4", 60, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - fünf Wochen teilgenommen
	 */
	public static readonly SBO_6_3_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_5", 61, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - sechs Wochen teilgenommen
	 */
	public static readonly SBO_6_3_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_6", 62, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Betriebspraktikum im Block
	 */
	public static readonly SBO_6_3_7 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_7", 63, );

	/**
	 * KAoA-Zusatzmerkmal: An trägergestützten Praxiskursen teilgenommen
	 */
	public static readonly SBO_6_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_4_1", 64, );

	/**
	 * KAoA-Zusatzmerkmal: An betrieblichen Praxiskursen teilgenommen
	 */
	public static readonly SBO_6_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_4_2", 65, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an Praxiskursen
	 */
	public static readonly SBO_6_4_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_4_3", 66, );

	/**
	 * KAoA-Zusatzmerkmal: Am Langzeitpraktikum teilgenommen
	 */
	public static readonly SBO_6_5_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_5_1", 67, );

	/**
	 * KAoA-Zusatzmerkmal: Langzeitpraktikum abgebrochen
	 */
	public static readonly SBO_6_5_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_5_2", 68, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum in Langzeit 1-tägig teilgenommen
	 */
	public static readonly SBO_6_6_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_6_1", 69, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum in Langzeit 2-tägig teilgenommen
	 */
	public static readonly SBO_6_6_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_6_2", 70, );

	/**
	 * KAoA-Zusatzmerkmal: STAR - Betriebspraktikum in Langzeit abgebrochen
	 */
	public static readonly SBO_6_6_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_6_3", 71, );

	/**
	 * KAoA-Zusatzmerkmal: An KAoA-kompakt teilgenommen
	 */
	public static readonly SBO_7_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_7_1_1", 72, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an KAoA-kompakt
	 */
	public static readonly SBO_7_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_7_1_2", 73, );

	/**
	 * KAoA-Zusatzmerkmal: Am Workshop "Standortbestimmung Reflexionsworkshop Sek. II" teilgenommen
	 */
	public static readonly SBO_8_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_8_1_1", 74, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Standortbestimmung Reflexionsworkshop Sek. II"
	 */
	public static readonly SBO_8_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_8_1_2", 75, );

	/**
	 * KAoA-Zusatzmerkmal: Am Workshop "Stärkung der Entscheidungskompetenz I - Sek. II" teilgenommen
	 */
	public static readonly SBO_8_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_8_2_1", 76, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Stärkung der Entscheidungskompetenz I - Sek. II"
	 */
	public static readonly SBO_8_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_8_2_2", 77, );

	/**
	 * KAoA-Zusatzmerkmal: An Praxiselementen in Betrieben, Hochschulen, Institutionen teilgenommen
	 */
	public static readonly SBO_9_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_1_1", 78, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an den Praxiselementen
	 */
	public static readonly SBO_9_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_1_2", 79, );

	/**
	 * KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 1 Woche (Sek II)
	 */
	public static readonly SBO_9_1_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_1_3", 80, );

	/**
	 * KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 2 Wochen (Sek II)
	 */
	public static readonly SBO_9_1_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_1_4", 81, );

	/**
	 * KAoA-Zusatzmerkmal: Teilnahme an den Veranstaltungen zur Studienorientierung
	 */
	public static readonly SBO_9_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_2_1", 82, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an den Veranstaltungen zur Studienorientierung
	 */
	public static readonly SBO_9_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_2_2", 83, );

	/**
	 * KAoA-Zusatzmerkmal: Am Workshop "Stärkung der Entscheidungskompetenz II - Sek II" teilgenommen
	 */
	public static readonly SBO_9_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_3_1", 84, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Stärkung der Entscheidungskompetenz II - Sek II"
	 */
	public static readonly SBO_9_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_3_2", 85, );

	/**
	 * KAoA-Zusatzmerkmal: Bewerbungstraining wurde durchgeführt
	 */
	public static readonly SBO_10_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_1_1", 86, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an einem Bewerbungstraining
	 */
	public static readonly SBO_10_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_1_2", 87, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Arbeitsplatzbezogenen Kommunikationstraining II teilgenommen
	 */
	public static readonly SBO_10_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_2_1", 88, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Arbeitsplatzbezogenen Kommunikationstraining II
	 */
	public static readonly SBO_10_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_2_2", 89, );

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Bewerbungstraining teilgenommen
	 */
	public static readonly SBO_10_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_3_1", 90, );

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Bewerbungstraining
	 */
	public static readonly SBO_10_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_3_2", 91, );

	/**
	 * KAoA-Zusatzmerkmal: durch die Jugendhilfe
	 */
	public static readonly SBO_10_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_1", 92, );

	/**
	 * KAoA-Zusatzmerkmal: durch die Schulsozialarbeit
	 */
	public static readonly SBO_10_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_2", 93, );

	/**
	 * KAoA-Zusatzmerkmal: durch die Berufseinstiegsbegleitung
	 */
	public static readonly SBO_10_4_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_3", 94, );

	/**
	 * KAoA-Zusatzmerkmal: durch die Einstiegsbegleitung über die Kommune finanziert
	 */
	public static readonly SBO_10_4_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_4", 95, );

	/**
	 * KAoA-Zusatzmerkmal: durch eine ehrenamtlich tätige Person
	 */
	public static readonly SBO_10_4_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_5", 96, );

	/**
	 * KAoA-Zusatzmerkmal: durch andere Institution
	 */
	public static readonly SBO_10_4_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_6", 97, );

	/**
	 * KAoA-Zusatzmerkmal: Eine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD) findet statt
	 */
	public static readonly SBO_10_5_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_5_1", 98, );

	/**
	 * KAoA-Zusatzmerkmal: Keine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD)
	 */
	public static readonly SBO_10_5_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_5_2", 99, );

	/**
	 * KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. I ist ausgefüllt worden
	 */
	public static readonly SBO_10_6_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_6_1", 100, );

	/**
	 * KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. I ist nicht ausgefüllt worden
	 */
	public static readonly SBO_10_6_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_6_2", 101, );

	/**
	 * KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. II ist ausgefüllt worden
	 */
	public static readonly SBO_10_6_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_6_3", 102, );

	/**
	 * KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. II ist nicht ausgefüllt worden
	 */
	public static readonly SBO_10_6_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_6_4", 103, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Abgangszeugnis für Schüler/innen mit sonderpädagogischem Unterstützungsbedarf
	 */
	public static readonly SBO_10_7_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_1", 104, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Abgangszeugnis
	 */
	public static readonly SBO_10_7_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_2", 105, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Geistige Entwicklung
	 */
	public static readonly SBO_10_7_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_3", 106, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Lernen
	 */
	public static readonly SBO_10_7_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_4", 107, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss (HA9) oder diesem gleichwertig
	 */
	public static readonly SBO_10_7_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_5", 108, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 oder diesem gleichwertig
	 */
	public static readonly SBO_10_7_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_6", 109, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Mittlerem Schulabschluss (FOR)
	 */
	public static readonly SBO_10_7_7 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_7", 110, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Mittlerem Schulabschluss (FOR) mit der Qualifikation für die Oberstufe
	 */
	public static readonly SBO_10_7_8 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_8", 111, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss (HA9) mit der Qualifikation für die Oberstufe
	 */
	public static readonly SBO_10_7_9 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_9", 112, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 mit der Qualifikation für die Oberstufe
	 */
	public static readonly SBO_10_7_10 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_10", 113, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei dem schulischen Teil der Fachhochschulreife
	 */
	public static readonly SBO_10_7_11 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_11", 114, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse mit der Fachhochschulreife
	 */
	public static readonly SBO_10_7_12 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_12", 115, );

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse mit der allgemeinen Hochschulreife
	 */
	public static readonly SBO_10_7_13 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_13", 116, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		KAOAZusatzmerkmal.all_values_by_ordinal.push(this);
		KAOAZusatzmerkmal.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<KAOAZusatzmerkmalKatalogEintrag, KAOAZusatzmerkmal>) : void {
		CoreTypeDataManager.putManager(KAOAZusatzmerkmal.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<KAOAZusatzmerkmalKatalogEintrag, KAOAZusatzmerkmal> {
		return CoreTypeDataManager.getManager(KAOAZusatzmerkmal.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAZusatzmerkmal> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAZusatzmerkmal | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<KAOAZusatzmerkmalKatalogEintrag, KAOAZusatzmerkmal> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : KAOAZusatzmerkmalKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<KAOAZusatzmerkmalKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmal';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmal', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<KAOAZusatzmerkmal>('de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmal');

}

export function cast_de_svws_nrw_asd_types_kaoa_KAOAZusatzmerkmal(obj : unknown) : KAOAZusatzmerkmal {
	return obj as KAOAZusatzmerkmal;
}