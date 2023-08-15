import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { KAOAZusatzmerkmalEintrag } from '../../../core/data/kaoa/KAOAZusatzmerkmalEintrag';
import { KAOAZusatzmerkmaleOptionsarten } from '../../../core/types/kaoa/KAOAZusatzmerkmaleOptionsarten';
import { HashMap } from '../../../java/util/HashMap';
import { KAOAMerkmal } from '../../../core/types/kaoa/KAOAMerkmal';

export class KAOAZusatzmerkmal extends JavaObject implements JavaEnum<KAOAZusatzmerkmal> {

	/** the name of the enumeration value */
	readonly __name : string;

	/** the ordinal value for the enumeration value */
	readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAZusatzmerkmal> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAZusatzmerkmal> = new Map<string, KAOAZusatzmerkmal>();

	/**
	 * KAoA-Zusatzmerkmal: Schulisches individuelles Beratungsgespräch durchgeführt
	 */
	public static readonly SBO_2_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_1_1", 0, [new KAOAZusatzmerkmalEintrag(13, "SBO 2.1.1", "Schulisches individuelles Beratungsgespräch durchgeführt", KAOAMerkmal.SBO_2_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Schulisches individuelles Beratungsgespräch nicht durchgeführt
	 */
	public static readonly SBO_2_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_1_2", 1, [new KAOAZusatzmerkmalEintrag(14, "SBO 2.1.2", "Schulisches individuelles Beratungsgespräch nicht durchgeführt", KAOAMerkmal.SBO_2_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Teilnahme an einem berufsorientierenden Angebote der Berufsberatung
	 */
	public static readonly SBO_2_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_2_1", 2, [new KAOAZusatzmerkmalEintrag(15, "SBO 2.2.1", "Teilnahme an einem berufsorientierenden Angebote der Berufsberatung", KAOAMerkmal.SBO_2_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an einem berufsorientierenden Angebote der Berufsberatung
	 */
	public static readonly SBO_2_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_2_2", 3, [new KAOAZusatzmerkmalEintrag(16, "SBO 2.2.2", "Keine Teilnahme an einem berufsorientierenden Angebote der Berufsberatung", KAOAMerkmal.SBO_2_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Berufsberatung der Agentur für Arbeit (BA)
	 */
	public static readonly SBO_2_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_1", 4, [new KAOAZusatzmerkmalEintrag(17, "SBO 2.3.1", "Berufsberatung der Agentur für Arbeit (BA)", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Beratungsgespräch der Jugendberufsagentur
	 */
	public static readonly SBO_2_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_2", 5, [new KAOAZusatzmerkmalEintrag(18, "SBO 2.3.2", "Beratungsgespräch der Jugendberufsagentur", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Beratungsgespräch des Jobcenters
	 */
	public static readonly SBO_2_3_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_3", 6, [new KAOAZusatzmerkmalEintrag(19, "SBO 2.3.3", "Beratungsgespräch des Jobcenters", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Beratungsgespräch Jugendsozialarbeit
	 */
	public static readonly SBO_2_3_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_4", 7, [new KAOAZusatzmerkmalEintrag(20, "SBO 2.3.4", "Beratungsgespräch Jugendsozialarbeit", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Beratungsgespräch eines anderen außerschulischen Partners
	 */
	public static readonly SBO_2_3_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_5", 8, [new KAOAZusatzmerkmalEintrag(21, "SBO 2.3.5", "Beratungsgespräch eines anderen außerschulischen Partners", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Kein Beratungskontakt zu einem außerschulischen Partner
	 */
	public static readonly SBO_2_3_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_3_6", 9, [new KAOAZusatzmerkmalEintrag(22, "SBO 2.3.6", "Kein Beratungskontakt zu einem außerschulischen Partner", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: STAR - Berufswegekonferenz durchgeführt
	 */
	public static readonly SBO_2_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_4_1", 10, [new KAOAZusatzmerkmalEintrag(23, "SBO 2.4.1", "STAR - Berufswegekonferenz durchgeführt", KAOAMerkmal.SBO_2_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: STAR - Berufswegekonferenz nicht durchgeführt
	 */
	public static readonly SBO_2_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_4_2", 11, [new KAOAZusatzmerkmalEintrag(24, "SBO 2.4.2", "STAR - Berufswegekonferenz nicht durchgeführt", KAOAMerkmal.SBO_2_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen
	 */
	public static readonly SBO_2_5_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_5_1", 12, [new KAOAZusatzmerkmalEintrag(25, "SBO 2.5.1", "Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen", KAOAMerkmal.SBO_2_5, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen
	 */
	public static readonly SBO_2_5_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_5_2", 13, [new KAOAZusatzmerkmalEintrag(26, "SBO 2.5.2", "Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen", KAOAMerkmal.SBO_2_5, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen
	 */
	public static readonly SBO_2_6_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_6_1", 14, [new KAOAZusatzmerkmalEintrag(27, "SBO 2.6.1", "Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen", KAOAMerkmal.SBO_2_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen
	 */
	public static readonly SBO_2_6_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_2_6_2", 15, [new KAOAZusatzmerkmalEintrag(28, "SBO 2.6.2", "Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen", KAOAMerkmal.SBO_2_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Berufwahlpass NRW SekI/II erhalten
	 */
	public static readonly SBO_3_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_3_4_1", 16, [new KAOAZusatzmerkmalEintrag(29, "SBO 3.4.1", "Berufwahlpass NRW SekI/II erhalten", KAOAMerkmal.SBO_3_4, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Berufwahlpass NRW Leichte Sprache erhalten
	 */
	public static readonly SBO_3_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_3_4_2", 17, [new KAOAZusatzmerkmalEintrag(30, "SBO 3.4.2", "Berufwahlpass NRW Leichte Sprache erhalten", KAOAMerkmal.SBO_3_4, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Berufswahlpass NRW kompakt erhalten
	 */
	public static readonly SBO_3_4_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_3_4_3", 18, [new KAOAZusatzmerkmalEintrag(31, "SBO 3.4.3", "Berufswahlpass NRW kompakt erhalten", KAOAMerkmal.SBO_3_4, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: anderes Portfolioinstrument  erhalten
	 */
	public static readonly SBO_3_4_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_3_4_4", 19, [new KAOAZusatzmerkmalEintrag(32, "SBO 3.4.4", "anderes Portfolioinstrument  erhalten", KAOAMerkmal.SBO_3_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: kein Portfolioinstrument erhalten
	 */
	public static readonly SBO_3_4_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_3_4_5", 20, [new KAOAZusatzmerkmalEintrag(33, "SBO 3.4.5", "kein Portfolioinstrument erhalten", KAOAMerkmal.SBO_3_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der Potenzialanalyse teilgenommen
	 */
	public static readonly SBO_4_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_1_1", 21, [new KAOAZusatzmerkmalEintrag(34, "SBO 4.1.1", "An der Potenzialanalyse teilgenommen", KAOAMerkmal.SBO_4_1, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse
	 */
	public static readonly SBO_4_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_1_2", 22, [new KAOAZusatzmerkmalEintrag(35, "SBO 4.1.2", "Keine Teilnahme an der Potenzialanalyse", KAOAMerkmal.SBO_4_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der Potenzialanalyse teilgenommen
	 */
	public static readonly SBO_4_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_2_1", 23, [new KAOAZusatzmerkmalEintrag(36, "SBO 4.2.1", "An der Potenzialanalyse teilgenommen", KAOAMerkmal.SBO_4_2, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse
	 */
	public static readonly SBO_4_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_2_2", 24, [new KAOAZusatzmerkmalEintrag(37, "SBO 4.2.2", "Keine Teilnahme an der Potenzialanalyse", KAOAMerkmal.SBO_4_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der STAR-Potenzialanalyse teilgenommen
	 */
	public static readonly SBO_4_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_3_1", 25, [new KAOAZusatzmerkmalEintrag(38, "SBO 4.3.1", "An der STAR-Potenzialanalyse teilgenommen", KAOAMerkmal.SBO_4_3, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse
	 */
	public static readonly SBO_4_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_3_2", 26, [new KAOAZusatzmerkmalEintrag(39, "SBO 4.3.2", "Keine Teilnahme an der Potenzialanalyse", KAOAMerkmal.SBO_4_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der Festellung des funktionalen Sehvermögens teilgenommen
	 */
	public static readonly SBO_4_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_4_1", 27, [new KAOAZusatzmerkmalEintrag(40, "SBO 4.4.1", "An der Festellung des funktionalen Sehvermögens teilgenommen", KAOAMerkmal.SBO_4_4, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an der Feststellung des funktionalen Sehrvermögens
	 */
	public static readonly SBO_4_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_4_2", 28, [new KAOAZusatzmerkmalEintrag(41, "SBO 4.4.2", "Keine Teilnahme an der Feststellung des funktionalen Sehrvermögens", KAOAMerkmal.SBO_4_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der STAR-Potenzialanalyse teilgenommen
	 */
	public static readonly SBO_4_5_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_5_1", 29, [new KAOAZusatzmerkmalEintrag(42, "SBO 4.5.1", "An der STAR-Potenzialanalyse teilgenommen", KAOAMerkmal.SBO_4_5, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse
	 */
	public static readonly SBO_4_5_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_4_5_2", 30, [new KAOAZusatzmerkmalEintrag(43, "SBO 4.5.2", "Keine Teilnahme an der Potenzialanalyse", KAOAMerkmal.SBO_4_5, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 1. Tag
	 */
	public static readonly SBO_5_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_1", 31, [new KAOAZusatzmerkmalEintrag(44, "SBO 5.1.1", "An der betrieblichen Berufsfelderkundung teilgenommen - 1. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 2. Tag
	 */
	public static readonly SBO_5_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_2", 32, [new KAOAZusatzmerkmalEintrag(45, "SBO 5.1.2", "An der betrieblichen Berufsfelderkundung teilgenommen - 2. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 3. Tag
	 */
	public static readonly SBO_5_1_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_3", 33, [new KAOAZusatzmerkmalEintrag(46, "SBO 5.1.3", "An der betrieblichen Berufsfelderkundung teilgenommen - 3. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - mehr als drei Tage
	 */
	public static readonly SBO_5_1_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_4", 34, [new KAOAZusatzmerkmalEintrag(47, "SBO 5.1.4", "An der betrieblichen Berufsfelderkundung teilgenommen - mehr als drei Tage", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 1. Tag
	 */
	public static readonly SBO_5_1_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_5", 35, [new KAOAZusatzmerkmalEintrag(48, "SBO 5.1.5", "An der trägergestützten Berufsfelderkundung teilgenommen - 1. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 2. Tag
	 */
	public static readonly SBO_5_1_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_6", 36, [new KAOAZusatzmerkmalEintrag(49, "SBO 5.1.6", "An der trägergestützten Berufsfelderkundung teilgenommen - 2. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 3. Tag
	 */
	public static readonly SBO_5_1_7 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_7", 37, [new KAOAZusatzmerkmalEintrag(50, "SBO 5.1.7", "An der trägergestützten Berufsfelderkundung teilgenommen - 3. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an den betrieblichen oder trägergestütztenBerufsfelderkundungen
	 */
	public static readonly SBO_5_1_8 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_1_8", 38, [new KAOAZusatzmerkmalEintrag(51, "SBO 5.1.8", "Keine Teilnahme an den betrieblichen oder trägergestütztenBerufsfelderkundungen", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 1. Tag
	 */
	public static readonly SBO_5_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_1", 39, [new KAOAZusatzmerkmalEintrag(52, "SBO 5.2.1", "An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 1. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 2. Tag
	 */
	public static readonly SBO_5_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_2", 40, [new KAOAZusatzmerkmalEintrag(53, "SBO 5.2.2", "An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 2. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 3. Tag
	 */
	public static readonly SBO_5_2_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_3", 41, [new KAOAZusatzmerkmalEintrag(54, "SBO 5.2.3", "An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 3. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - weitere Tage
	 */
	public static readonly SBO_5_2_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_4", 42, [new KAOAZusatzmerkmalEintrag(55, "SBO 5.2.4", "An der betrieblichen STAR - Berufsfelderkundung teilgenommen - weitere Tage", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 1. Tag
	 */
	public static readonly SBO_5_2_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_5", 43, [new KAOAZusatzmerkmalEintrag(56, "SBO 5.2.5", "An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 1. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 2. Tag
	 */
	public static readonly SBO_5_2_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_6", 44, [new KAOAZusatzmerkmalEintrag(57, "SBO 5.2.6", "An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 2. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 3. Tag
	 */
	public static readonly SBO_5_2_7 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_7", 45, [new KAOAZusatzmerkmalEintrag(58, "SBO 5.2.7", "An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 3. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an den betrieblichen oder trägergestützten STAR - Berufsfelderkundungen
	 */
	public static readonly SBO_5_2_8 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_2_8", 46, [new KAOAZusatzmerkmalEintrag(59, "SBO 5.2.8", "Keine Teilnahme an den betrieblichen oder trägergestützten STAR - Berufsfelderkundungen", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Arbeitsplatzbezogenes Kommunikationstraining I teilgenommen
	 */
	public static readonly SBO_5_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_3_1", 47, [new KAOAZusatzmerkmalEintrag(60, "SBO 5.3.1", "Am STAR - Arbeitsplatzbezogenes Kommunikationstraining I teilgenommen", KAOAMerkmal.SBO_5_3, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Arbeitsplatzbezogenes Kommunikationstraining I
	 */
	public static readonly SBO_5_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_3_2", 48, [new KAOAZusatzmerkmalEintrag(61, "SBO 5.3.2", "Keine Teilnahme am STAR - Arbeitsplatzbezogenes Kommunikationstraining I", KAOAMerkmal.SBO_5_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Berufsorientierungsseminar teilgenommen
	 */
	public static readonly SBO_5_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_4_1", 49, [new KAOAZusatzmerkmalEintrag(62, "SBO 5.4.1", "Am STAR - Berufsorientierungsseminar teilgenommen", KAOAMerkmal.SBO_5_4, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Berufsorientierungsseminar
	 */
	public static readonly SBO_5_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_5_4_2", 50, [new KAOAZusatzmerkmalEintrag(63, "SBO 5.4.2", "Keine Teilnahme am STAR - Berufsorientierungsseminar", KAOAMerkmal.SBO_5_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 1 Woche
	 */
	public static readonly SBO_6_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_1_1", 51, [new KAOAZusatzmerkmalEintrag(64, "SBO 6.1.1", "Am Betriebspraktikum teilgenommen - 1 Woche", KAOAMerkmal.SBO_6_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 2 Wochen
	 */
	public static readonly SBO_6_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_1_2", 52, [new KAOAZusatzmerkmalEintrag(65, "SBO 6.1.2", "Am Betriebspraktikum teilgenommen - 2 Wochen", KAOAMerkmal.SBO_6_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am Betriebspraktikum Sek I teilgenommen - mehr als zwei Wochen
	 */
	public static readonly SBO_6_1_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_1_3", 53, [new KAOAZusatzmerkmalEintrag(66, "SBO 6.1.3", "Am Betriebspraktikum Sek I teilgenommen - mehr als zwei Wochen", KAOAMerkmal.SBO_6_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Zusätzliche Praktika wie z. B. Schnupperpraktika
	 */
	public static readonly SBO_6_1_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_1_4", 54, [new KAOAZusatzmerkmalEintrag(67, "SBO 6.1.4", "Zusätzliche Praktika wie z. B. Schnupperpraktika", KAOAMerkmal.SBO_6_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am Betriebspraktikum
	 */
	public static readonly SBO_6_1_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_1_5", 55, [new KAOAZusatzmerkmalEintrag(68, "SBO 6.1.5", "Keine Teilnahme am Betriebspraktikum", KAOAMerkmal.SBO_6_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am Intensivtraining TASK
	 */
	public static readonly SBO_6_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_2_2", 56, [new KAOAZusatzmerkmalEintrag(69, "SBO 6.2.2", "Keine Teilnahme am Intensivtraining TASK", KAOAMerkmal.SBO_6_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - eine Woche teilgenommen
	 */
	public static readonly SBO_6_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_1", 57, [new KAOAZusatzmerkmalEintrag(70, "SBO 6.3.1", "Am STAR - Betriebspraktikum im Block - eine Woche teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - zwei Wochen teilgenommen
	 */
	public static readonly SBO_6_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_2", 58, [new KAOAZusatzmerkmalEintrag(71, "SBO 6.3.2", "Am STAR - Betriebspraktikum im Block - zwei Wochen teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - drei Wochen teilgenommen
	 */
	public static readonly SBO_6_3_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_3", 59, [new KAOAZusatzmerkmalEintrag(72, "SBO 6.3.3", "Am STAR - Betriebspraktikum im Block - drei Wochen teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - vier Wochen teilgenommen
	 */
	public static readonly SBO_6_3_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_4", 60, [new KAOAZusatzmerkmalEintrag(73, "SBO 6.3.4", "Am STAR - Betriebspraktikum im Block - vier Wochen teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - fünf Wochen teilgenommen
	 */
	public static readonly SBO_6_3_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_5", 61, [new KAOAZusatzmerkmalEintrag(74, "SBO 6.3.5", "Am STAR - Betriebspraktikum im Block - fünf Wochen teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - sechs Wochen teilgenommen
	 */
	public static readonly SBO_6_3_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_6", 62, [new KAOAZusatzmerkmalEintrag(75, "SBO 6.3.6", "Am STAR - Betriebspraktikum im Block - sechs Wochen teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Betriebspraktikum im Block
	 */
	public static readonly SBO_6_3_7 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_3_7", 63, [new KAOAZusatzmerkmalEintrag(76, "SBO 6.3.7", "Keine Teilnahme am STAR - Betriebspraktikum im Block", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An trägergestützten Praxiskursen teilgenommen
	 */
	public static readonly SBO_6_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_4_1", 64, [new KAOAZusatzmerkmalEintrag(77, "SBO 6.4.1", "An trägergestützten Praxiskursen teilgenommen", KAOAMerkmal.SBO_6_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An betrieblichen Praxiskursen teilgenommen
	 */
	public static readonly SBO_6_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_4_2", 65, [new KAOAZusatzmerkmalEintrag(78, "SBO 6.4.2", "An betrieblichen Praxiskursen teilgenommen", KAOAMerkmal.SBO_6_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an Praxiskursen
	 */
	public static readonly SBO_6_4_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_4_3", 66, [new KAOAZusatzmerkmalEintrag(79, "SBO 6.4.3", "Keine Teilnahme an Praxiskursen", KAOAMerkmal.SBO_6_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am Langzeitpraktikum teilgenommen
	 */
	public static readonly SBO_6_5_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_5_1", 67, [new KAOAZusatzmerkmalEintrag(80, "SBO 6.5.1", "Am Langzeitpraktikum teilgenommen", KAOAMerkmal.SBO_6_5, KAOAZusatzmerkmaleOptionsarten.SBO_EBENE_4, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Langzeitpraktikum abgebrochen
	 */
	public static readonly SBO_6_5_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_5_2", 68, [new KAOAZusatzmerkmalEintrag(81, "SBO 6.5.2", "Langzeitpraktikum abgebrochen", KAOAMerkmal.SBO_6_5, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum in Langzeit 1-tägig teilgenommen
	 */
	public static readonly SBO_6_6_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_6_1", 69, [new KAOAZusatzmerkmalEintrag(82, "SBO 6.6.1", "Am STAR - Betriebspraktikum in Langzeit 1-tägig teilgenommen", KAOAMerkmal.SBO_6_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum in Langzeit 2-tägig teilgenommen
	 */
	public static readonly SBO_6_6_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_6_2", 70, [new KAOAZusatzmerkmalEintrag(83, "SBO 6.6.2", "Am STAR - Betriebspraktikum in Langzeit 2-tägig teilgenommen", KAOAMerkmal.SBO_6_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: STAR - Betriebspraktikum in Langzeit abgebrochen
	 */
	public static readonly SBO_6_6_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_6_6_3", 71, [new KAOAZusatzmerkmalEintrag(84, "SBO 6.6.3", "STAR - Betriebspraktikum in Langzeit abgebrochen", KAOAMerkmal.SBO_6_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An KAoA-kompakt teilgenommen
	 */
	public static readonly SBO_7_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_7_1_1", 72, [new KAOAZusatzmerkmalEintrag(85, "SBO 7.1.1", "An KAoA-kompakt teilgenommen", KAOAMerkmal.SBO_7_1, KAOAZusatzmerkmaleOptionsarten.SBO_EBENE_4, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an KAoA-kompakt
	 */
	public static readonly SBO_7_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_7_1_2", 73, [new KAOAZusatzmerkmalEintrag(86, "SBO 7.1.2", "Keine Teilnahme an KAoA-kompakt", KAOAMerkmal.SBO_7_1, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am Workshop "Standortbestimmung Reflexionsworkshop Sek. II" teilgenommen
	 */
	public static readonly SBO_8_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_8_1_1", 74, [new KAOAZusatzmerkmalEintrag(87, "SBO 8.1.1", "Am Workshop \"Standortbestimmung Reflexionsworkshop Sek. II\" teilgenommen", KAOAMerkmal.SBO_8_1, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Standortbestimmung Reflexionsworkshop Sek. II"
	 */
	public static readonly SBO_8_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_8_1_2", 75, [new KAOAZusatzmerkmalEintrag(88, "SBO 8.1.2", "Keine Teilnahme am Workshop \"Standortbestimmung Reflexionsworkshop Sek. II\"", KAOAMerkmal.SBO_8_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am Workshop "Stärkung der Entscheidungskompetenz I - Sek. II" teilgenommen
	 */
	public static readonly SBO_8_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_8_2_1", 76, [new KAOAZusatzmerkmalEintrag(89, "SBO 8.2.1", "Am Workshop \"Stärkung der Entscheidungskompetenz I - Sek. II\" teilgenommen", KAOAMerkmal.SBO_8_2, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Stärkung der Entscheidungskompetenz I - Sek. II"
	 */
	public static readonly SBO_8_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_8_2_2", 77, [new KAOAZusatzmerkmalEintrag(90, "SBO 8.2.2", "Keine Teilnahme am Workshop \"Stärkung der Entscheidungskompetenz I - Sek. II\"", KAOAMerkmal.SBO_8_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: An Praxiselementen in Betrieben, Hochschulen, Institutionen teilgenommen
	 */
	public static readonly SBO_9_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_1_1", 78, [new KAOAZusatzmerkmalEintrag(91, "SBO 9.1.1", "An Praxiselementen in Betrieben, Hochschulen, Institutionen teilgenommen", KAOAMerkmal.SBO_9_1, KAOAZusatzmerkmaleOptionsarten.SBO_EBENE_4, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an den Praxiselementen
	 */
	public static readonly SBO_9_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_1_2", 79, [new KAOAZusatzmerkmalEintrag(92, "SBO 9.1.2", "Keine Teilnahme an den Praxiselementen", KAOAMerkmal.SBO_9_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 1 Woche (Sek II)
	 */
	public static readonly SBO_9_1_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_1_3", 80, [new KAOAZusatzmerkmalEintrag(93, "SBO 9.1.3", "Am Betriebspraktikum teilgenommen - 1 Woche (Sek II) ", KAOAMerkmal.SBO_9_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 2 Wochen (Sek II)
	 */
	public static readonly SBO_9_1_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_1_4", 81, [new KAOAZusatzmerkmalEintrag(94, "SBO 9.1.4", "Am Betriebspraktikum teilgenommen - 2 Wochen (Sek II) ", KAOAMerkmal.SBO_9_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Teilnahme an den Veranstaltungen zur Studienorientierung
	 */
	public static readonly SBO_9_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_2_1", 82, [new KAOAZusatzmerkmalEintrag(95, "SBO 9.2.1", "Teilnahme an den Veranstaltungen zur Studienorientierung", KAOAMerkmal.SBO_9_2, KAOAZusatzmerkmaleOptionsarten.SBO_EBENE_4, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an den Veranstaltungen zur Studienorientierung
	 */
	public static readonly SBO_9_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_2_2", 83, [new KAOAZusatzmerkmalEintrag(96, "SBO 9.2.2", "Keine Teilnahme an den Veranstaltungen zur Studienorientierung", KAOAMerkmal.SBO_9_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am Workshop "Stärkung der Entscheidungskompetenz II - Sek II" teilgenommen
	 */
	public static readonly SBO_9_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_3_1", 84, [new KAOAZusatzmerkmalEintrag(97, "SBO 9.3.1", "Am Workshop \"Stärkung der Entscheidungskompetenz II - Sek II\" teilgenommen", KAOAMerkmal.SBO_9_3, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Stärkung der Entscheidungskompetenz II - Sek II"
	 */
	public static readonly SBO_9_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_9_3_2", 85, [new KAOAZusatzmerkmalEintrag(98, "SBO 9.3.2", "Keine Teilnahme am Workshop \"Stärkung der Entscheidungskompetenz II - Sek II\"", KAOAMerkmal.SBO_9_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Bewerbungstraining wurde durchgeführt
	 */
	public static readonly SBO_10_1_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_1_1", 86, [new KAOAZusatzmerkmalEintrag(99, "SBO 10.1.1", "Bewerbungstraining wurde durchgeführt", KAOAMerkmal.SBO_10_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme an einem Bewerbungstraining
	 */
	public static readonly SBO_10_1_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_1_2", 87, [new KAOAZusatzmerkmalEintrag(100, "SBO 10.1.2", "Keine Teilnahme an einem Bewerbungstraining", KAOAMerkmal.SBO_10_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Arbeitsplatzbezogenen Kommunikationstraining II teilgenommen
	 */
	public static readonly SBO_10_2_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_2_1", 88, [new KAOAZusatzmerkmalEintrag(101, "SBO 10.2.1", "Am STAR - Arbeitsplatzbezogenen Kommunikationstraining II teilgenommen", KAOAMerkmal.SBO_10_2, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Arbeitsplatzbezogenen Kommunikationstraining II
	 */
	public static readonly SBO_10_2_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_2_2", 89, [new KAOAZusatzmerkmalEintrag(102, "SBO 10.2.2", "Keine Teilnahme am STAR - Arbeitsplatzbezogenen Kommunikationstraining II", KAOAMerkmal.SBO_10_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Am STAR - Bewerbungstraining teilgenommen
	 */
	public static readonly SBO_10_3_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_3_1", 90, [new KAOAZusatzmerkmalEintrag(103, "SBO 10.3.1", "Am STAR - Bewerbungstraining teilgenommen", KAOAMerkmal.SBO_10_3, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Bewerbungstraining
	 */
	public static readonly SBO_10_3_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_3_2", 91, [new KAOAZusatzmerkmalEintrag(104, "SBO 10.3.2", "Keine Teilnahme am STAR - Bewerbungstraining", KAOAMerkmal.SBO_10_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: durch die Jugendhilfe
	 */
	public static readonly SBO_10_4_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_1", 92, [new KAOAZusatzmerkmalEintrag(105, "SBO 10.4.1", "durch die Jugendhilfe", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: durch die Schulsozialarbeit
	 */
	public static readonly SBO_10_4_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_2", 93, [new KAOAZusatzmerkmalEintrag(106, "SBO 10.4.2", "durch die Schulsozialarbeit", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: durch die Berufseinstiegsbegleitung
	 */
	public static readonly SBO_10_4_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_3", 94, [new KAOAZusatzmerkmalEintrag(107, "SBO 10.4.3", "durch die Berufseinstiegsbegleitung", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: durch die Einstiegsbegleitung über die Kommune finanziert
	 */
	public static readonly SBO_10_4_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_4", 95, [new KAOAZusatzmerkmalEintrag(108, "SBO 10.4.4", "durch die Einstiegsbegleitung über die Kommune finanziert", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: durch eine ehrenamtlich tätige Person
	 */
	public static readonly SBO_10_4_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_5", 96, [new KAOAZusatzmerkmalEintrag(109, "SBO 10.4.5", "durch eine ehrenamtlich tätige Person", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: durch andere Institution
	 */
	public static readonly SBO_10_4_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_4_6", 97, [new KAOAZusatzmerkmalEintrag(110, "SBO 10.4.6", "durch andere Institution", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Eine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD) findet statt
	 */
	public static readonly SBO_10_5_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_5_1", 98, [new KAOAZusatzmerkmalEintrag(111, "SBO 10.5.1", "Eine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD) findet statt", KAOAMerkmal.SBO_10_5, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Keine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD)
	 */
	public static readonly SBO_10_5_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_5_2", 99, [new KAOAZusatzmerkmalEintrag(112, "SBO 10.5.2", "Keine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD)", KAOAMerkmal.SBO_10_5, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. I ist ausgefüllt worden
	 */
	public static readonly SBO_10_6_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_6_1", 100, [new KAOAZusatzmerkmalEintrag(113, "SBO 10.6.1", "Die Anschlussvereinbarung Sek. I ist ausgefüllt worden", KAOAMerkmal.SBO_10_6, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. I ist nicht ausgefüllt worden
	 */
	public static readonly SBO_10_6_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_6_2", 101, [new KAOAZusatzmerkmalEintrag(114, "SBO 10.6.2", "Die Anschlussvereinbarung Sek. I ist nicht ausgefüllt worden", KAOAMerkmal.SBO_10_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. II ist ausgefüllt worden
	 */
	public static readonly SBO_10_6_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_6_3", 102, [new KAOAZusatzmerkmalEintrag(115, "SBO 10.6.3", "Die Anschlussvereinbarung Sek. II ist ausgefüllt worden", KAOAMerkmal.SBO_10_6, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. II ist nicht ausgefüllt worden
	 */
	public static readonly SBO_10_6_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_6_4", 103, [new KAOAZusatzmerkmalEintrag(116, "SBO 10.6.4", "Die Anschlussvereinbarung Sek. II ist nicht ausgefüllt worden", KAOAMerkmal.SBO_10_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Abgangszeugnis für Schüler/innen mit sonderpädagogischem Unterstützungsbedarf
	 */
	public static readonly SBO_10_7_1 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_1", 104, [new KAOAZusatzmerkmalEintrag(117, "SBO 10.7.1", "Anschlüsse bei einem Abgangszeugnis für Schüler/innen mit sonderpädagogischem Unterstützungsbedarf", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Abgangszeugnis
	 */
	public static readonly SBO_10_7_2 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_2", 105, [new KAOAZusatzmerkmalEintrag(118, "SBO 10.7.2", "Anschlüsse bei einem Abgangszeugnis", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Geistige Entwicklung
	 */
	public static readonly SBO_10_7_3 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_3", 106, [new KAOAZusatzmerkmalEintrag(119, "SBO 10.7.3", "Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Geistige Entwicklung", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Lernen
	 */
	public static readonly SBO_10_7_4 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_4", 107, [new KAOAZusatzmerkmalEintrag(120, "SBO 10.7.4", "Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Lernen", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss (HA9) oder diesem gleichwertig
	 */
	public static readonly SBO_10_7_5 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_5", 108, [new KAOAZusatzmerkmalEintrag(121, "SBO 10.7.5", "Anschlüsse bei einem Hauptschulabschluss (HA9) oder diesem gleichwertig", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 oder diesem gleichwertig
	 */
	public static readonly SBO_10_7_6 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_6", 109, [new KAOAZusatzmerkmalEintrag(122, "SBO 10.7.6", "Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 oder diesem gleichwertig", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Mittlerem Schulabschluss (FOR)
	 */
	public static readonly SBO_10_7_7 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_7", 110, [new KAOAZusatzmerkmalEintrag(123, "SBO 10.7.7", "Anschlüsse bei einem Mittlerem Schulabschluss (FOR)", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Mittlerem Schulabschluss (FOR) mit der Qualifikation für die Oberstufe
	 */
	public static readonly SBO_10_7_8 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_8", 111, [new KAOAZusatzmerkmalEintrag(124, "SBO 10.7.8", "Anschlüsse bei einem Mittlerem Schulabschluss (FOR) mit der Qualifikation für die Oberstufe", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss (HA9) mit der Qualifikation für die Oberstufe
	 */
	public static readonly SBO_10_7_9 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_9", 112, [new KAOAZusatzmerkmalEintrag(125, "SBO 10.7.9", "Anschlüsse bei einem Hauptschulabschluss (HA9) mit der Qualifikation für die Oberstufe", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 mit der Qualifikation für die Oberstufe
	 */
	public static readonly SBO_10_7_10 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_10", 113, [new KAOAZusatzmerkmalEintrag(126, "SBO 10.7.10", "Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 mit der Qualifikation für die Oberstufe", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse bei dem schulischen Teil der Fachhochschulreife
	 */
	public static readonly SBO_10_7_11 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_11", 114, [new KAOAZusatzmerkmalEintrag(127, "SBO 10.7.11", "Anschlüsse bei dem schulischen Teil der Fachhochschulreife", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse mit der Fachhochschulreife
	 */
	public static readonly SBO_10_7_12 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_12", 115, [new KAOAZusatzmerkmalEintrag(128, "SBO 10.7.12", "Anschlüsse mit der Fachhochschulreife", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * KAoA-Zusatzmerkmal: Anschlüsse mit der allgemeinen Hochschulreife
	 */
	public static readonly SBO_10_7_13 : KAOAZusatzmerkmal = new KAOAZusatzmerkmal("SBO_10_7_13", 116, [new KAOAZusatzmerkmalEintrag(129, "SBO 10.7.13", "Anschlüsse mit der allgemeinen Hochschulreife", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Die aktuellsten Daten des KAoA-Merkmals
	 */
	public readonly daten : KAOAZusatzmerkmalEintrag;

	/**
	 * Die Historie mit den Einträgen des KAoA-Merkmals
	 */
	public readonly historie : Array<KAOAZusatzmerkmalEintrag>;

	/**
	 * Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _statusByID : HashMap<number, KAOAZusatzmerkmal> = new HashMap();

	/**
	 * Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind.
	 */
	private static readonly _statusByKuerzel : HashMap<string, KAOAZusatzmerkmal> = new HashMap();

	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 *
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAZusatzmerkmalEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<KAOAZusatzmerkmalEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		KAOAZusatzmerkmal.all_values_by_ordinal.push(this);
		KAOAZusatzmerkmal.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von der ID auf das zugehörige Merkmal zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von der ID auf das zugehörige Merkmal
	 */
	private static getMapStatusByID() : HashMap<number, KAOAZusatzmerkmal> {
		if (KAOAZusatzmerkmal._statusByID.size() === 0)
			for (const g of KAOAZusatzmerkmal.values())
				KAOAZusatzmerkmal._statusByID.put(g.daten.id, g);
		return KAOAZusatzmerkmal._statusByID;
	}

	/**
	 * Gibt eine Map von dem Kürzel auf das zugehörige Merkmal zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von dem Kürzel auf das zugehörige Merkmal
	 */
	private static getMapStatusByKuerzel() : HashMap<string, KAOAZusatzmerkmal> {
		if (KAOAZusatzmerkmal._statusByKuerzel.size() === 0)
			for (const g of KAOAZusatzmerkmal.values())
				KAOAZusatzmerkmal._statusByKuerzel.put(g.daten.kuerzel, g);
		return KAOAZusatzmerkmal._statusByKuerzel;
	}

	/**
	 * Gibt das Merkmal anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Merkmals
	 *
	 * @return das Merkmal oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number | null) : KAOAZusatzmerkmal | null {
		return KAOAZusatzmerkmal.getMapStatusByID().get(id);
	}

	/**
	 * Gibt das Merkmal anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Merkmals
	 *
	 * @return das Merkmal oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : KAOAZusatzmerkmal | null {
		return KAOAZusatzmerkmal.getMapStatusByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
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
		if (!(other instanceof KAOAZusatzmerkmal))
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
	public compareTo(other : KAOAZusatzmerkmal) : number {
		return this.__ordinal - other.__ordinal;
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
		const tmp : KAOAZusatzmerkmal | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kaoa.KAOAZusatzmerkmal', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kaoa_KAOAZusatzmerkmal(obj : unknown) : KAOAZusatzmerkmal {
	return obj as KAOAZusatzmerkmal;
}
