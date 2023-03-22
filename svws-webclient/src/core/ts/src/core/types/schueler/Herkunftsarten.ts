import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HerkunftsartKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_HerkunftsartKatalogEintrag } from '../../../core/data/schule/HerkunftsartKatalogEintrag';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { Schulform, cast_de_nrw_schule_svws_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { HerkunftsartKatalogEintragBezeichnung, cast_de_nrw_schule_svws_core_data_schule_HerkunftsartKatalogEintragBezeichnung } from '../../../core/data/schule/HerkunftsartKatalogEintragBezeichnung';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class Herkunftsarten extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Herkunftsarten> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Herkunftsarten> = new Map<string, Herkunftsarten>();

	/**
	 * 
	 *  Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)
	 */
	public static readonly NICHTVERSETZUNG : Herkunftsarten = new Herkunftsarten("NICHTVERSETZUNG", 0, [new HerkunftsartKatalogEintrag(0, "00", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)")), null, null)]);

	/**
	 * 
	 *  Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung
	 */
	public static readonly FREIWILLIGE_WIEDERHOLUNG : Herkunftsarten = new Herkunftsarten("FREIWILLIGE_WIEDERHOLUNG", 1, [new HerkunftsartKatalogEintrag(3000, "03", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung")), null, null)]);

	/**
	 * 
	 *  Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt
	 */
	public static readonly RUECKTRITT : Herkunftsarten = new Herkunftsarten("RUECKTRITT", 2, [new HerkunftsartKatalogEintrag(3100, "03", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt")), null, null)]);

	/**
	 * 
	 *  Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)
	 */
	public static readonly VERBLEIB_IN_SCHULSTUFE : Herkunftsarten = new Herkunftsarten("VERBLEIB_IN_SCHULSTUFE", 3, [new HerkunftsartKatalogEintrag(3200, "03", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)")), null, null)]);

	/**
	 * 
	 *  Verbleib in der Schuleingangsphase
	 */
	public static readonly VERBLEIB_IN_SCHULEINGANGSPHASE : Herkunftsarten = new Herkunftsarten("VERBLEIB_IN_SCHULEINGANGSPHASE", 4, [new HerkunftsartKatalogEintrag(4000, "04", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"), new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase")), null, null)]);

	/**
	 * 
	 *  Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung
	 */
	public static readonly VERSETZUNG : Herkunftsarten = new Herkunftsarten("VERSETZUNG", 5, [new HerkunftsartKatalogEintrag(11000, "11", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung")), null, null)]);

	/**
	 * 
	 *  Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform
	 */
	public static readonly UEBERGANG : Herkunftsarten = new Herkunftsarten("UEBERGANG", 6, [new HerkunftsartKatalogEintrag(11100, "11", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform")), null, null)]);

	/**
	 * 
	 *  Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg
	 */
	public static readonly SCHULFORMAUFSTIEG : Herkunftsarten = new Herkunftsarten("SCHULFORMAUFSTIEG", 7, [new HerkunftsartKatalogEintrag(11200, "11", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg")), null, null)]);

	/**
	 * 
	 *  Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule
	 */
	public static readonly WECHSEL_ZUR_GESAMTSCHULE : Herkunftsarten = new Herkunftsarten("WECHSEL_ZUR_GESAMTSCHULE", 8, [new HerkunftsartKatalogEintrag(11300, "11", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule")), null, null)]);

	/**
	 * 
	 *  Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung
	 */
	public static readonly VORVERSETZUNG : Herkunftsarten = new Herkunftsarten("VORVERSETZUNG", 9, [new HerkunftsartKatalogEintrag(12000, "12", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung")), null, null)]);

	/**
	 * 
	 *  Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)
	 */
	public static readonly SCHULFORMABSTIEG : Herkunftsarten = new Herkunftsarten("SCHULFORMABSTIEG", 10, [new HerkunftsartKatalogEintrag(13000, "13", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)")), null, null)]);

	/**
	 * 
	 *  Schüler, die aus dem Ausland zugezogen sind
	 */
	public static readonly ZUZUG_AUSLAND : Herkunftsarten = new Herkunftsarten("ZUZUG_AUSLAND", 11, [new HerkunftsartKatalogEintrag(99000, "99", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"), new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind")), null, null)]);

	/**
	 * 
	 *  Kein Abschluss
	 */
	public static readonly KEIN_ABSCHLUSS : Herkunftsarten = new Herkunftsarten("KEIN_ABSCHLUSS", 12, [new HerkunftsartKatalogEintrag(1000000, "A", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Kein Abschluss", "Kein Abschluss"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Kein Abschluss", "Kein Abschluss"), new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Kein Abschluss", "Abgangszeugnis ohne Abschluss"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Kein Abschluss", "Abgangszeugnis ohne Abschluss"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Kein Abschluss", "Abgangszeugnis ohne Abschluss"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Kein Abschluss", "Abgangszeugnis ohne Abschluss / Abgangszeugnis ohne Zuerkennung eines Abschlusses / Kooperationsklasse Hauptschule")), null, null)]);

	/**
	 * 
	 *  Hauptschulabschluss
	 */
	public static readonly HA9A : Herkunftsarten = new Herkunftsarten("HA9A", 13, [new HerkunftsartKatalogEintrag(2000000, "B", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "HA9", "Hauptschulabschluss, auch Hauptschulabschluss der Förderschule für Lernen"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "HA9", "Hauptschulabschluss, auch Hauptschulabschluss der Förderschule für Lernen"), new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "HA9", "Hauptschulabschluss"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "HA9", "Hauptschulabschluss"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "HA9", "Hauptschulabschluss"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "HA9", "Hauptschulabschluss / Abgangszeugnis mit Gleichstellung zum Hauptschulabschluss / Hauptschulabschluss der Schule für Lernbehinderte")), null, null)]);

	/**
	 * 
	 *  nach Klasse 9 (mit Berechtigung zum Besuch der Klasse 10 Typ B)
	 */
	public static readonly HA9 : Herkunftsarten = new Herkunftsarten("HA9", 14, [new HerkunftsartKatalogEintrag(3000000, "C", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "HA9-10B", "Hauptschulabschluss mit Berechtigung zum Besuch der Klasse 10, Typ B / Abgangszeugnis aus Klasse 10 ohne Mittlerer Abschluss"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "HA9-10B", "Hauptschulabschluss mit Berechtigung zum Besuch der Klasse 10, Typ B / Abgangszeugnis aus Klasse 10 ohne Mittlerer Abschluss"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "HA9-10B", "Hauptschulabschluss mit Berechtigung zum Besuch der Klasse 10, Typ B / Abgangszeugnis aus Klasse 10 ohne Gleichstellung zum Hauptschulabschluss nach Klasse 10")), null, null)]);

	/**
	 * 
	 *  Hauptschulabschluss nach Klasse 10
	 */
	public static readonly HA10 : Herkunftsarten = new Herkunftsarten("HA10", 15, [new HerkunftsartKatalogEintrag(4000000, "D", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "HA10", "Hauptschulabschluss nach Klasse 10"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "HA10", "Hauptschulabschluss nach Klasse 10"), new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "HA10", "Sekundarabschluss I (Hauptschulabschluss nach Klasse 10)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "HA10", "Sekundarabschluss I (Hauptschulabschluss nach Klasse 10)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "HA10", "Sekundarabschluss I (Hauptschulabschluss nach Klasse 10)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "HA10", "Hauptschulabschluss nach Klasse 10 / Abgangszeugnis mit Gleichstellung zum Hauptschulabschluss nach Klasse 10")), null, null)]);

	/**
	 * 
	 *  Mittlerer Schulabschluss
	 */
	public static readonly MSA : Herkunftsarten = new Herkunftsarten("MSA", 16, [new HerkunftsartKatalogEintrag(6000000, "F", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "MSA", "Mittlerer Abschluss, Fachoberschulreife ohne Berechtigung zum Besuch der gymnasialen Oberstufe"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "MSA", "Mittlerer Abschluss, Fachoberschulreife ohne Berechtigung zum Besuch der gymnasialen Oberstufe"), new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "MSA", "Sekundarabschluss I ohne Versetzungsvermerk"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "MSA", "Sekundarabschluss I ohne Versetzungsvermerk"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "MSA", "Sekundarabschluss I ohne Versetzungsvermerk"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "MSA", "Fachoberschulreife ohne Versetzungsvermerk")), null, null)]);

	/**
	 * 
	 *  Mittlerer Schulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe (Einführungsphase)
	 */
	public static readonly MSA_Q : Herkunftsarten = new Herkunftsarten("MSA_Q", 17, [new HerkunftsartKatalogEintrag(7000000, "G", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "MSA-Q", "Mittlerer Abschluss, Fachoberschulreife mit  Berechtigung zum Besuch der gymnasialen Oberstufe (Einführungsphase)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "MSA-Q", "Mittlerer Abschluss, Fachoberschulreife mit  Berechtigung zum Besuch der gymnasialen Oberstufe (Einführungsphase)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "MSA-Q", "Sekundarabschluss I mit Versetzungsvermerk"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "MSA-Q", "Sekundarabschluss I mit Versetzungsvermerk"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "MSA-Q", "Sekundarabschluss I mit Versetzungsvermerk"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "MSA-Q", "Fachoberschulreife mit  Versetzungsvermerk")), null, null)]);

	/**
	 * 
	 *  Fachhochschulreife (schulischer Teil)
	 */
	public static readonly FHR_S : Herkunftsarten = new Herkunftsarten("FHR_S", 18, [new HerkunftsartKatalogEintrag(8000000, "H", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)")), null, null)]);

	/**
	 * 
	 *  Mittlerer Schulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe (Qualifikationsphase 1)
	 */
	public static readonly MSA_Q1 : Herkunftsarten = new Herkunftsarten("MSA_Q1", 19, [new HerkunftsartKatalogEintrag(9000000, "I", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "MSA-Q1", "Mittlerer Abschluss, Fachoberschulreife mit  Berechtigung zum Besuch der Qualifikationsphase 1"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "MSA-Q1", "Mittlerer Abschluss, Fachoberschulreife mit  Berechtigung zum Besuch der Qualifikationsphase 1"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "MSA-Q1", "Fachoberschulreife mit Berechtigung zum Besuch der Qualifikationsphase")), null, null)]);

	/**
	 * 
	 *  Fachhochschulreife
	 */
	public static readonly FHR : Herkunftsarten = new Herkunftsarten("FHR", 20, [new HerkunftsartKatalogEintrag(10000000, "J", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "FHR", "Fachhochschulreife"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "FHR", "Fachhochschulreife"), new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "FHR", "Fachhochschulreife"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "FHR", "Fachhochschulreife"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "FHR", "Fachhochschulreife"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "FHR", "Fachhochschulreife")), null, null)]);

	/**
	 * 
	 *  Allgemeine Hochschulreife
	 */
	public static readonly AHR : Herkunftsarten = new Herkunftsarten("AHR", 21, [new HerkunftsartKatalogEintrag(11000000, "K", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "AHR", "Allgemeine Hochschulreife"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "AHR", "Allgemeine Hochschulreife"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "AHR", "Allgemeine Hochschulreife")), null, null)]);

	/**
	 * 
	 *  Hochschulreife für das Land NRW
	 */
	public static readonly HR_NRW : Herkunftsarten = new Herkunftsarten("HR_NRW", 22, [new HerkunftsartKatalogEintrag(12000000, "L", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Hochschulreife NRW", "Hochschulreife für das Land NRW"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Hochschulreife NRW", "Hochschulreife für das Land NRW"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Hochschulreife NRW", "Hochschulreife für das Land NRW")), null, null)]);

	/**
	 * 
	 *  Abschlusszeugnis (Förderschule, Förderschwerpunkt geistige Entwicklung)
	 */
	public static readonly ABSCHLUSS_FOEG : Herkunftsarten = new Herkunftsarten("ABSCHLUSS_FOEG", 23, [new HerkunftsartKatalogEintrag(13000000, "M", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Abschlusszeugnis Förderschule (geistige Entwicklung)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Abschlusszeugnis Förderschule (geistige Entwicklung)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt geistige Entwicklung)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Abschlusszeugnis Förderschule (geistige Entwicklung)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt geistige Entwicklung)")), null, null)]);

	/**
	 * 
	 *  Abschlusszeugnis (Förderschule, Förderschwerpunkt Lernen)
	 */
	public static readonly ABSCHLUSS_FOEL : Herkunftsarten = new Herkunftsarten("ABSCHLUSS_FOEL", 24, [new HerkunftsartKatalogEintrag(14000000, "N", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Abschlusszeugnis Förderschule (Lernen)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt Lernen)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Abschlusszeugnis Förderschule (Lernen)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt Lernen)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Abschlusszeugnis Förderschule (Lernen)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt Lernen)")), null, null)]);

	/**
	 * 
	 *  Hauptschulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe
	 */
	public static readonly HA9_Q : Herkunftsarten = new Herkunftsarten("HA9_Q", 25, [new HerkunftsartKatalogEintrag(15000000, "O", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "HA-Q", "Hauptschulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "HA-Q", "Hauptschulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "HA-Q", "Hauptschulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe (auch Versetzungszeugnis der Kl. 9 des Gymnasiums)")), null, null)]);

	/**
	 * 
	 *  Versetzung nach Klasse 11 FO
	 */
	public static readonly VS_11 : Herkunftsarten = new Herkunftsarten("VS_11", 26, [new HerkunftsartKatalogEintrag(16000000, "P", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Versetzung Kl. 11", "Versetzung nach Klasse 11 FO"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Versetzung Kl. 11", "Versetzung nach Klasse 11 FO"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Versetzung Kl. 11", "Versetzung nach Klasse 11 FO")), null, null)]);

	/**
	 * 
	 *  Fachgebundene Hochschulreife
	 */
	public static readonly FGHR : Herkunftsarten = new Herkunftsarten("FGHR", 27, [new HerkunftsartKatalogEintrag(17000000, "Q", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "FGHR", "Fachgebundene Hochschulreife"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "FGHR", "Fachgebundene Hochschulreife"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "FGHR", "Fachgebundene Hochschulreife")), null, null)]);

	/**
	 * 
	 *  Ein dem Hauptschulabschluss vergleichbarer Abschluss mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs im Berufskolleg (nur Internationale Förderklasse)
	 */
	public static readonly HA9_FOE : Herkunftsarten = new Herkunftsarten("HA9_FOE", 28, [new HerkunftsartKatalogEintrag(19000000, "S", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "HA-IntFö", "Ein dem Hauptschulabschluss vergleichbarer Abschluss mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs im Berufskolleg (nur Internationale Förderklasse)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "HA-IntFö", "Ein dem Hauptschulabschluss vergleichbarer Abschluss mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs im Berufskolleg (nur Internationale Förderklasse)")), null, null)]);

	/**
	 * 
	 *  Herkunft noch unbekannt (nur Gliederung A12, A13)
	 */
	public static readonly UNBEKANNT : Herkunftsarten = new Herkunftsarten("UNBEKANNT", 29, [new HerkunftsartKatalogEintrag(21000000, "U", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Unbekannt", "Herkunft noch unbekannt (nur Gliederung A12, A13)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Unbekannt", "Herkunft noch unbekannt (nur Gliederung A12, A13)")), null, null)]);

	/**
	 * 
	 *  Versetzte bzw. vorgerückte Schüler/-innen / Höheres Semester
	 */
	public static readonly VERSETZT : Herkunftsarten = new Herkunftsarten("VERSETZT", 30, [new HerkunftsartKatalogEintrag(22000000, "V", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Versetzt", "Versetzte bzw. vorgerückte Schüler/-innen"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Versetzt", "Versetzte bzw. vorgerückte Schüler/-innen"), new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Versetzt", "Höheres Semester"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Versetzt", "Höheres Semester"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Versetzt", "Höheres Semester"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Versetzt", "Höheres Semester")), null, null)]);

	/**
	 * 
	 *  Wiederholer / Gleiches oder niedrigeres Semester
	 */
	public static readonly WIEDERHOLER : Herkunftsarten = new Herkunftsarten("WIEDERHOLER", 31, [new HerkunftsartKatalogEintrag(23000000, "W", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Wiederholer", "Wiederholer"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Wiederholer", "Wiederholer"), new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Wiederholer", "Gleiches oder niedrigeres Semester"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Wiederholer", "Gleiches oder niedrigeres Semester"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Wiederholer", "Gleiches oder niedrigeres Semester"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Wiederholer", "Gleiches oder niedrigeres Semester")), null, null)]);

	/**
	 * 
	 *  Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)
	 */
	public static readonly SONSTIGE_QUALIFIKATION : Herkunftsarten = new Herkunftsarten("SONSTIGE_QUALIFIKATION", 32, [new HerkunftsartKatalogEintrag(24000000, "X", Arrays.asList(new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)"), new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)")), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Herkunftsart, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : HerkunftsartKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Herkunftsarten
	 */
	public readonly historie : Array<HerkunftsartKatalogEintrag>;

	/**
	 * Eine Map mit der Zuordnung der Herkunftsart zu dem Kürzel der Herkunftsart
	 */
	private static readonly _mapKuerzel : HashMap<string, Herkunftsarten | null> = new HashMap();

	/**
	 * Eine Map mit der Zuordnung der Herkunftsart zu der ID der Herkunftsart
	 */
	private static readonly _mapID : HashMap<number, Herkunftsarten | null> = new HashMap();

	/**
	 * Die Schulformen, bei welchen die Herkunftsart vorkommt, für die einzelnen Historieneinträge
	 */
	private schulformen : Array<Vector<Schulform | null>>;

	/**
	 * Die Bezeichnungen bei den Schulformen, bei welchen die Herkunftsart vorkommt, für die einzelnen Historieneinträge
	 */
	private bezeichnungen : Array<Vector<string>>;

	/**
	 * Erzeugt eine neue Herkunftsart in der Aufzählung.
	 * 
	 * @param historie   die Historie der Herkunftsart, welches ein Array von {@link HerkunftsartKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<HerkunftsartKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Herkunftsarten.all_values_by_ordinal.push(this);
		Herkunftsarten.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		this.schulformen = Array(historie.length).fill(null);
		this.bezeichnungen = Array(historie.length).fill(null);
		for (let i : number = 0; i < historie.length; i++){
			this.schulformen[i] = new Vector();
			this.bezeichnungen[i] = new Vector();
			for (let bez of historie[i].bezeichnungen) {
				let sf : Schulform | null = Schulform.getByKuerzel(bez.schulform);
				if (sf !== null)
					this.schulformen[i].add(sf);
				this.bezeichnungen[i].add(bez.bezeichnung);
			}
		}
	}

	/**
	 * Gibt eine Map von den Kürzeln der Herkunftsarten auf die zugehörigen Herkunftsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Herkunftsarten auf die zugehörigen Herkunftsarten
	 */
	private static getMapHerkunftsartByKuerzel() : HashMap<string, Herkunftsarten | null> {
		if (Herkunftsarten._mapKuerzel.size() === 0)
			for (let j of Herkunftsarten.values()) 
				if (!Herkunftsarten._mapKuerzel.containsKey(j.daten.kuerzel))
					Herkunftsarten._mapKuerzel.put(j.daten.kuerzel, j);
		return Herkunftsarten._mapKuerzel;
	}

	/**
	 * Gibt eine Map von den IDs der Herkunftsarten auf die zugehörigen Herkunftsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs der Herkunftsarten auf die zugehörigen Herkunftsarten
	 */
	private static getMapHerkunftsartByID() : HashMap<number, Herkunftsarten | null> {
		if (Herkunftsarten._mapID.size() === 0)
			for (let j of Herkunftsarten.values()) {
				for (let k of j.historie) 
					Herkunftsarten._mapID.put(k.id, j);
			}
		return Herkunftsarten._mapID;
	}

	/**
	 * Liefert die Herkunftsart anhand des übergebenen Kürzels zurück. 
	 * 
	 * @param kuerzel   das Kürzel der Herkunftsart
	 * 
	 * @return die Herkunftsart oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Herkunftsarten | null {
		return Herkunftsarten.getMapHerkunftsartByKuerzel().get(kuerzel);
	}

	/**
	 * Liefert die Herkunftsart anhand der übergebenen ID zurück. 
	 * 
	 * @param id   die ID der Herkunftsart
	 * 
	 * @return die Herkunftsart oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number | null) : Herkunftsarten | null {
		return Herkunftsarten.getMapHerkunftsartByID().get(id);
	}

	/**
	 * Liefert die Bezeichnung der Herkunftsart für die angebenene Schulform.
	 * 
	 * @param schulform   die Schulform
	 * 
	 * @return die Bezeichung der Herkunftsart oder null, falls die Schulform nicht zulässig ist
	 */
	public getBezeichnung(schulform : Schulform | null) : string | null {
		if ((schulform === null) || (schulform.daten === null))
			return null;
		if (this.daten.bezeichnungen !== null) {
			for (let i : number = 0; i < this.daten.bezeichnungen.size(); i++){
				let bez : HerkunftsartKatalogEintragBezeichnung | null = this.daten.bezeichnungen.get(i);
				let sfKuerzel : string | null = bez.schulform;
				if (JavaObject.equalsTranspiler(sfKuerzel, (schulform.daten.kuerzel)))
					return bez.bezeichnung;
			}
		}
		return null;
	}

	/**
	 * Liefert alle Schulformen zurück, bei welchen die Herkunftsart vorkommt.
	 * 
	 * @return eine Liste der Schulformen
	 */
	public getSchulformen() : List<Schulform | null> {
		return this.schulformen[this.historie.length - 1];
	}

	/**
	 * Liefert alle zulässigen Herkunftsarten für die angegebene Schulform.
	 * 
	 * @param schulform   die Schulform
	 * 
	 * @return die bei der Schulform zulässigen Herkunftsarten
	 */
	public static get(schulform : Schulform | null) : List<Herkunftsarten | null> {
		let result : Vector<Herkunftsarten | null> = new Vector();
		if (schulform === null)
			return result;
		let herkunftsarten : Array<Herkunftsarten> = Herkunftsarten.values();
		for (let i : number = 0; i < herkunftsarten.length; i++){
			let herkunftsart : Herkunftsarten = herkunftsarten[i];
			if (herkunftsart.hasSchulform(schulform))
				result.add(herkunftsart);
		}
		return result;
	}

	/**
	 * Prüft anhand des Schulform-Kürzels, ob die Schulform diese Herkunftsart
	 * hat oder nicht.
	 * 
	 * @param kuerzel   das Kürzel der Schulform
	 * 
	 * @return true, falls die Herkunftsart bei der Schulform existiert und ansonsten false
	 */
	public hasSchulformByKuerzel(kuerzel : string | null) : boolean {
		if ((kuerzel === null) || JavaObject.equalsTranspiler("", (kuerzel)))
			return false;
		if (this.daten.bezeichnungen !== null) {
			for (let i : number = 0; i < this.daten.bezeichnungen.size(); i++){
				let sfKuerzel : string | null = this.daten.bezeichnungen.get(i).schulform;
				if (JavaObject.equalsTranspiler(sfKuerzel, (kuerzel)))
					return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob die Schulform diese Herkunftsart hat oder nicht.
	 * 
	 * @param schulform   die Schulform
	 * 
	 * @return true, falls die Herkunftsart bei der Schulform existiert und ansonsten false
	 */
	public hasSchulform(schulform : Schulform | null) : boolean {
		if ((schulform === null) || (schulform.daten === null))
			return false;
		if (this.daten.bezeichnungen !== null) {
			for (let i : number = 0; i < this.daten.bezeichnungen.size(); i++){
				let sfKuerzel : string | null = this.daten.bezeichnungen.get(i).schulform;
				if (JavaObject.equalsTranspiler(sfKuerzel, (schulform.daten.kuerzel)))
					return true;
			}
		}
		return false;
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
		if (!(other instanceof Herkunftsarten))
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
	public compareTo(other : Herkunftsarten) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Herkunftsarten> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Herkunftsarten | null {
		let tmp : Herkunftsarten | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.schueler.Herkunftsarten'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_schueler_Herkunftsarten(obj : unknown) : Herkunftsarten {
	return obj as Herkunftsarten;
}
