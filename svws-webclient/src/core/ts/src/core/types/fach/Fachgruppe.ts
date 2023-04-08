import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { FachgruppenKatalogEintrag } from '../../../core/data/fach/FachgruppenKatalogEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { RGBFarbe } from '../../../core/data/RGBFarbe';

export class Fachgruppe extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Fachgruppe> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Fachgruppe> = new Map<string, Fachgruppe>();

	/**
	 * Fachgruppe Deutsch
	 */
	public static readonly FG_D : Fachgruppe = new Fachgruppe("FG_D", 0, [new FachgruppenKatalogEintrag(1, 1, 110, "Deutsch", "D", new RGBFarbe(253, 233, 217), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 0, true, null, null)]);

	/**
	 * Fachgruppe Arbeitslehre
	 */
	public static readonly FG_AL : Fachgruppe = new Fachgruppe("FG_AL", 1, [new FachgruppenKatalogEintrag(2, 2, 400, "Arbeitslehre", "AL", new RGBFarbe(253, 221, 195), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 12, true, null, null)]);

	/**
	 * Fachgruppe Fremdsprachen
	 */
	public static readonly FG_FS : Fachgruppe = new Fachgruppe("FG_FS", 2, [new FachgruppenKatalogEintrag(3, 2, 100, "Fremdsprachen", "FS", new RGBFarbe(253, 221, 195), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 9, true, null, null)]);

	/**
	 * Fachgruppe Kunst und Musik
	 */
	public static readonly FG_MS : Fachgruppe = new Fachgruppe("FG_MS", 3, [new FachgruppenKatalogEintrag(4, 3, 500, "Kunst und Musik", "MS", new RGBFarbe(252, 204, 165), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 13, true, null, null)]);

	/**
	 * Fachgruppe Literatur, instrumental- oder vokalpraktischer Kurs
	 */
	public static readonly FG_ME : Fachgruppe = new Fachgruppe("FG_ME", 4, [new FachgruppenKatalogEintrag(5, 4, null, "Literatur, instrumental- oder vokalpraktischer Kurs", "ME", new RGBFarbe(252, 204, 165), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 13, false, null, null)]);

	/**
	 * Fachgruppe Gesellschaftswissenschaft
	 */
	public static readonly FG_GS : Fachgruppe = new Fachgruppe("FG_GS", 5, [new FachgruppenKatalogEintrag(6, 5, 300, "Gesellschaftswissenschaft", "GS", new RGBFarbe(234, 241, 222), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 11, true, null, null)]);

	/**
	 * Fachgruppe Philosophie
	 */
	public static readonly FG_PL : Fachgruppe = new Fachgruppe("FG_PL", 6, [new FachgruppenKatalogEintrag(7, 5, null, "Philosophie", "PL", new RGBFarbe(234, 241, 222), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 11, false, null, null)]);

	/**
	 * Fachgruppe Religion
	 */
	public static readonly FG_RE : Fachgruppe = new Fachgruppe("FG_RE", 7, [new FachgruppenKatalogEintrag(8, 6, 900, "Religion", "RE", new RGBFarbe(215, 228, 188), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 6, true, null, null)]);

	/**
	 * Fachgruppe Mathematik
	 */
	public static readonly FG_M : Fachgruppe = new Fachgruppe("FG_M", 8, [new FachgruppenKatalogEintrag(9, 7, 700, "Mathematik", "M", new RGBFarbe(197, 217, 241), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 15, true, null, null)]);

	/**
	 * Fachgruppe Naturwissenschaften
	 */
	public static readonly FG_NW : Fachgruppe = new Fachgruppe("FG_NW", 9, [new FachgruppenKatalogEintrag(10, 8, 200, "Naturwissenschaften", "NW", new RGBFarbe(141, 180, 227), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 10, true, null, null)]);

	/**
	 * Fachgruppe weiteres naturwissenschaftliches / technisches Fach
	 */
	public static readonly FG_WN : Fachgruppe = new Fachgruppe("FG_WN", 10, [new FachgruppenKatalogEintrag(11, 8, null, "weiteres naturwissenschaftliches / technisches Fach", "WN", new RGBFarbe(141, 180, 227), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 10, false, null, null)]);

	/**
	 * Fachgruppe Sport
	 */
	public static readonly FG_SP : Fachgruppe = new Fachgruppe("FG_SP", 11, [new FachgruppenKatalogEintrag(12, 9, 600, "Sport", "SP", new RGBFarbe(255, 255, 255), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 14, true, null, null)]);

	/**
	 * Fachgruppe Vertiefungskurs
	 */
	public static readonly FG_VX : Fachgruppe = new Fachgruppe("FG_VX", 12, [new FachgruppenKatalogEintrag(13, 10, 1500, "Vertiefungskurs", "VX", new RGBFarbe(216, 216, 216), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 0, false, null, null)]);

	/**
	 * Fachgruppe Projektkurs
	 */
	public static readonly FG_PX : Fachgruppe = new Fachgruppe("FG_PX", 13, [new FachgruppenKatalogEintrag(14, 11, 1600, "Projektkurs", "PX", new RGBFarbe(191, 191, 191), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 0, false, null, null)]);

	/**
	 * Fachgruppe Berufsübergreifender Bereich
	 */
	public static readonly FG_BUE : Fachgruppe = new Fachgruppe("FG_BUE", 14, [new FachgruppenKatalogEintrag(15, null, 10, "Berufsübergreifender Bereich", "BUE", new RGBFarbe(), Arrays.asList(Schulform.BK, Schulform.SB), 1, false, null, null)]);

	/**
	 * Fachgruppe Berufsbezogener Bereich
	 */
	public static readonly FG_BBS : Fachgruppe = new Fachgruppe("FG_BBS", 15, [new FachgruppenKatalogEintrag(16, null, 20, "Berufsbezogener Bereich", "BBS", new RGBFarbe(), Arrays.asList(Schulform.BK, Schulform.SB), 2, false, null, null)]);

	/**
	 * Fachgruppe Berufsbezogener Bereich (Schwerpunkt)
	 */
	public static readonly FG_BBS_SCHWERPUNKT : Fachgruppe = new Fachgruppe("FG_BBS_SCHWERPUNKT", 16, [new FachgruppenKatalogEintrag(17, null, 25, "Berufsbezogener Bereich (Schwerpunkt)", "BBS", new RGBFarbe(), Arrays.asList(Schulform.BK, Schulform.SB), 0, false, null, null)]);

	/**
	 * Fachgruppe Differenzierungsbereich
	 */
	public static readonly FG_DF : Fachgruppe = new Fachgruppe("FG_DF", 17, [new FachgruppenKatalogEintrag(18, null, 30, "Differenzierungsbereich", "DF", new RGBFarbe(), Arrays.asList(Schulform.BK, Schulform.SB), 3, false, null, null)]);

	/**
	 * Fachgruppe Berufspraktikum
	 */
	public static readonly FG_BP : Fachgruppe = new Fachgruppe("FG_BP", 18, [new FachgruppenKatalogEintrag(19, null, 40, "Berufspraktikum", "BP", new RGBFarbe(), Arrays.asList(Schulform.BK, Schulform.SB), 4, false, null, null)]);

	/**
	 * Fachgruppe besondere Lernleistung
	 */
	public static readonly FG_BLL : Fachgruppe = new Fachgruppe("FG_BLL", 19, [new FachgruppenKatalogEintrag(20, null, 60, "besondere Lernleistung", "BLL", new RGBFarbe(), Arrays.asList(Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 8, false, null, null)]);

	/**
	 * Fachgruppe Wahlpflichtbereich
	 */
	public static readonly FG_WP : Fachgruppe = new Fachgruppe("FG_WP", 20, [new FachgruppenKatalogEintrag(21, null, 800, "Wahlpflichtbereich", "WP", new RGBFarbe(), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 16, false, null, null)]);

	/**
	 * Fachgruppe Zusätzliche Unterrichtsveranstaltungen
	 */
	public static readonly FG_ZUV : Fachgruppe = new Fachgruppe("FG_ZUV", 21, [new FachgruppenKatalogEintrag(22, null, 1000, "Zusätzliche Unterrichtsveranstaltungen", "ZUV", new RGBFarbe(), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 0, false, null, null)]);

	/**
	 * Fachgruppe Angleichungskurse
	 */
	public static readonly FG_ANG : Fachgruppe = new Fachgruppe("FG_ANG", 22, [new FachgruppenKatalogEintrag(23, null, 1100, "Angleichungskurse", "ANG", new RGBFarbe(), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 0, false, null, null)]);

	/**
	 * Fachgruppe Sprache
	 */
	public static readonly FG_D_SP : Fachgruppe = new Fachgruppe("FG_D_SP", 23, [new FachgruppenKatalogEintrag(24, null, 1200, "Sprache", "D_SP", new RGBFarbe(), Arrays.asList(Schulform.G, Schulform.KS, Schulform.S, Schulform.V), 0, true, null, null)]);

	/**
	 * Fachgruppe Sachunterricht
	 */
	public static readonly FG_SU : Fachgruppe = new Fachgruppe("FG_SU", 24, [new FachgruppenKatalogEintrag(25, null, 1300, "Sachunterricht", "SU", new RGBFarbe(), Arrays.asList(Schulform.G, Schulform.KS, Schulform.S, Schulform.V), 0, true, null, null)]);

	/**
	 * Fachgruppe Förderunterricht
	 */
	public static readonly FG_FOE : Fachgruppe = new Fachgruppe("FG_FOE", 25, [new FachgruppenKatalogEintrag(26, null, 1400, "Förderunterricht", "FOE", new RGBFarbe(), Arrays.asList(Schulform.G, Schulform.KS, Schulform.S, Schulform.V), 0, true, null, null)]);

	/**
	 * Fachgruppe Abschlussarbeit
	 */
	public static readonly FG_ABA : Fachgruppe = new Fachgruppe("FG_ABA", 26, [new FachgruppenKatalogEintrag(27, null, 1700, "Abschlussarbeit", "ABA", new RGBFarbe(), Arrays.asList(Schulform.BK, Schulform.SB), 0, false, null, null)]);

	/**
	 * Fachgruppe Projektarbeit
	 */
	public static readonly FG_PA : Fachgruppe = new Fachgruppe("FG_PA", 27, [new FachgruppenKatalogEintrag(28, null, 1800, "Projektarbeit", "PA", new RGBFarbe(), Arrays.asList(Schulform.BK, Schulform.SB), 0, false, null, null)]);

	/**
	 * Fachgruppe Informatik (Sek I)
	 */
	public static readonly FG_IF : Fachgruppe = new Fachgruppe("FG_IF", 28, [new FachgruppenKatalogEintrag(29, null, 1900, "Informatik (Sek I)", "IF", new RGBFarbe(141, 180, 227), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 10, true, null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Fachgruppe
	 */
	public readonly daten : FachgruppenKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Fachgruppe
	 */
	public readonly historie : Array<FachgruppenKatalogEintrag>;

	/**
	 * Eine Map, welche der ID der Fachgruppe die Instanz dieser Aufzählung zuordnet.
	 */
	private static readonly _mapEintragByID : HashMap<number, FachgruppenKatalogEintrag> = new HashMap();

	/**
	 * Eine Map, welche der ID der Fachgruppe die Instanz dieser Aufzählung zuordnet.
	 */
	private static readonly _mapByID : HashMap<number, Fachgruppe> = new HashMap();

	/**
	 * Eine Map, welche dem Kürzel der Fachgruppe die Instanz dieser Aufzählung zuordnet.
	 */
	private static readonly _mapByKuerzel : HashMap<string, Fachgruppe> = new HashMap();

	/**
	 * Die Schulformen, bei welchen die Fachgruppe vorkommt
	 */
	private schulformen : Array<ArrayList<Schulform>>;

	/**
	 * Erzeugt eine neue Fachgruppe in der Aufzählung.
	 *
	 * @param historie   die Historie der Fachgruppe, welche ein Array von
	 *                   {@link FachgruppenKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<FachgruppenKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Fachgruppe.all_values_by_ordinal.push(this);
		Fachgruppe.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		this.schulformen = Array(historie.length).fill(null);
		for (let i : number = 0; i < historie.length; i++) {
			this.schulformen[i] = new ArrayList();
			for (const kuerzel of historie[i].schulformen) {
				const sf : Schulform | null = Schulform.getByKuerzel(kuerzel);
				if (sf !== null)
					this.schulformen[i].add(sf);
			}
		}
	}

	/**
	 * Gibt eine Map von den IDs der Fachgruppen auf die zugehörigen Katalog-Einträge
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Fachgruppen auf die zugehörigen Katalog-Einträge
	 */
	private static getMapEintragByID() : HashMap<number, FachgruppenKatalogEintrag> {
		if (Fachgruppe._mapEintragByID.size() === 0)
			for (const g of Fachgruppe.values())
				for (const k of g.historie)
					Fachgruppe._mapEintragByID.put(k.id, k);
		return Fachgruppe._mapEintragByID;
	}

	/**
	 * Gibt eine Map von den IDs der Fachgruppen auf die zugehörigen Fachgruppen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Fachgruppen auf die zugehörigen Fachgruppen
	 */
	private static getMapByID() : HashMap<number, Fachgruppe> {
		if (Fachgruppe._mapByID.size() === 0)
			for (const g of Fachgruppe.values())
				Fachgruppe._mapByID.put(g.daten.id, g);
		return Fachgruppe._mapByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Fachgruppen auf die zugehörigen Fachgruppen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Fachgruppen auf die zugehörigen Fachgruppen
	 */
	private static getMapByKuerzel() : HashMap<string, Fachgruppe> {
		if (Fachgruppe._mapByKuerzel.size() === 0)
			for (const g of Fachgruppe.values())
				Fachgruppe._mapByKuerzel.put(g.daten.kuerzel, g);
		return Fachgruppe._mapByKuerzel;
	}

	/**
	 * Liefert alle Schulformen zurück, bei welchen die Fachgruppe vorkommt.
	 *
	 * @return eine Liste der Schulformen
	 */
	public getSchulformen() : List<Schulform> {
		return this.schulformen[this.historie.length - 1];
	}

	/**
	 * Prüft, ob die Schulform bei diesem Fach in irgendeiner Fachgruppe der
	 * angegebenen Schulform zulässig ist.
	 *
	 * @param schulform    die Schulform
	 *
	 * @return true, falls die Fachgruppe in der Schulform zulässig ist, ansonsten false.
	 */
	private hasSchulform(schulform : Schulform | null) : boolean {
		if ((schulform === null) || (schulform.daten === null))
			return false;
		if (this.daten.schulformen !== null) {
			for (let i : number = 0; i < this.daten.schulformen.size(); i++) {
				const sfKuerzel : string | null = this.daten.schulformen.get(i);
				if (JavaObject.equalsTranspiler(sfKuerzel, (schulform.daten.kuerzel)))
					return true;
			}
		}
		return false;
	}

	/**
	 * Liefert den Katalog-Eintrag der Fachgruppe zu der übergebenen ID zurück.
	 *
	 * @param id   die ID des Fachgruppen-Katalog-Eintrags
	 *
	 * @return der Fachgruppen-Katalog-Eintrag oder null, falls die ID ungültig ist
	 */
	public static getKatalogEintragByID(id : number) : FachgruppenKatalogEintrag | null {
		return Fachgruppe.getMapEintragByID().get(id);
	}

	/**
	 * Liefert die Fachgruppe zu der übergebenen ID der Fachgruppe zurück.
	 *
	 * @param id   die ID der Fachgruppe
	 *
	 * @return die Fachgruppe oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : Fachgruppe | null {
		return Fachgruppe.getMapByID().get(id);
	}

	/**
	 * Liefert die Fachgruppe zu der übergebenen ID der Fachgruppe zurück.
	 *
	 * @param kuerzel   das Kürzel der Fachgruppe
	 *
	 * @return die Fachgruppe oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Fachgruppe | null {
		return Fachgruppe.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Bestimmt alle Fachgruppen, die in der angegebenen Schulform zulässig sind.
	 *
	 * @param schulform    die Schulform
	 *
	 * @return die Fachgruppen in der angegebenen Schulform
	 */
	public static get(schulform : Schulform | null) : List<Fachgruppe> {
		const faecher : ArrayList<Fachgruppe> = new ArrayList();
		if (schulform === null)
			return faecher;
		const fachgruppen : Array<Fachgruppe> = Fachgruppe.values();
		for (let i : number = 0; i < fachgruppen.length; i++) {
			const fg : Fachgruppe | null = fachgruppen[i];
			if (fg.hasSchulform(schulform))
				faecher.add(fg);
		}
		return faecher;
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
		if (!(other instanceof Fachgruppe))
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
	public compareTo(other : Fachgruppe) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Fachgruppe> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Fachgruppe | null {
		const tmp : Fachgruppe | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.fach.Fachgruppe'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_fach_Fachgruppe(obj : unknown) : Fachgruppe {
	return obj as Fachgruppe;
}
