import { JavaObject } from '../../../java/lang/JavaObject';

export class DruckGostLaufbahnplanungSchuelerFachwahlen extends JavaObject {

	/**
	 * Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören.
	 */
	public SchuelerID : number = 0;

	/**
	 * Die Bezeichnung des Faches
	 */
	public Bezeichnung : string = "";

	/**
	 * Das Kürzel des Faches
	 */
	public Kuerzel : string = "";

	/**
	 * Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache
	 */
	public FachIstFortfuehrbareFremdspracheInGOSt : boolean | null = false;

	/**
	 * Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung
	 */
	public JahrgangFremdsprachenbeginn : string = "";

	/**
	 * Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk
	 */
	public PositionFremdsprachenfolge : string = "";

	/**
	 * Fachbelegung in der EF.1
	 */
	public BelegungEF1 : string = "";

	/**
	 * Fachbelegung in der EF.2
	 */
	public BelegungEF2 : string = "";

	/**
	 * Fachbelegung in der Q1.1
	 */
	public BelegungQ11 : string = "";

	/**
	 * Fachbelegung in der Q1.2
	 */
	public BelegungQ12 : string = "";

	/**
	 * Fachbelegung in der Q2.1
	 */
	public BelegungQ21 : string = "";

	/**
	 * Fachbelegung in der Q2.2
	 */
	public BelegungQ22 : string = "";

	/**
	 * Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde
	 */
	public Abiturfach : string = "";

	/**
	 * Fach ist in mindestens einem Halbjahr der GOSt belegt.
	 */
	public FachIstBelegtInGOSt : boolean | null = false;

	/**
	 * Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde
	 */
	public Aufgabenfeld : number = 0;

	/**
	 * Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde
	 */
	public Fachgruppe : string = "";

	/**
	 * Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde
	 */
	public FarbeClientRGB : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerFachwahlen'].includes(name);
	}

	public static transpilerFromJSON(json : string): DruckGostLaufbahnplanungSchuelerFachwahlen {
		const obj = JSON.parse(json);
		const result = new DruckGostLaufbahnplanungSchuelerFachwahlen();
		if (typeof obj.SchuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute SchuelerID');
		result.SchuelerID = obj.SchuelerID;
		if (typeof obj.Bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute Bezeichnung');
		result.Bezeichnung = obj.Bezeichnung;
		if (typeof obj.Kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute Kuerzel');
		result.Kuerzel = obj.Kuerzel;
		result.FachIstFortfuehrbareFremdspracheInGOSt = typeof obj.FachIstFortfuehrbareFremdspracheInGOSt === "undefined" ? null : obj.FachIstFortfuehrbareFremdspracheInGOSt === null ? null : obj.FachIstFortfuehrbareFremdspracheInGOSt;
		if (typeof obj.JahrgangFremdsprachenbeginn === "undefined")
			 throw new Error('invalid json format, missing attribute JahrgangFremdsprachenbeginn');
		result.JahrgangFremdsprachenbeginn = obj.JahrgangFremdsprachenbeginn;
		if (typeof obj.PositionFremdsprachenfolge === "undefined")
			 throw new Error('invalid json format, missing attribute PositionFremdsprachenfolge');
		result.PositionFremdsprachenfolge = obj.PositionFremdsprachenfolge;
		if (typeof obj.BelegungEF1 === "undefined")
			 throw new Error('invalid json format, missing attribute BelegungEF1');
		result.BelegungEF1 = obj.BelegungEF1;
		if (typeof obj.BelegungEF2 === "undefined")
			 throw new Error('invalid json format, missing attribute BelegungEF2');
		result.BelegungEF2 = obj.BelegungEF2;
		if (typeof obj.BelegungQ11 === "undefined")
			 throw new Error('invalid json format, missing attribute BelegungQ11');
		result.BelegungQ11 = obj.BelegungQ11;
		if (typeof obj.BelegungQ12 === "undefined")
			 throw new Error('invalid json format, missing attribute BelegungQ12');
		result.BelegungQ12 = obj.BelegungQ12;
		if (typeof obj.BelegungQ21 === "undefined")
			 throw new Error('invalid json format, missing attribute BelegungQ21');
		result.BelegungQ21 = obj.BelegungQ21;
		if (typeof obj.BelegungQ22 === "undefined")
			 throw new Error('invalid json format, missing attribute BelegungQ22');
		result.BelegungQ22 = obj.BelegungQ22;
		if (typeof obj.Abiturfach === "undefined")
			 throw new Error('invalid json format, missing attribute Abiturfach');
		result.Abiturfach = obj.Abiturfach;
		result.FachIstBelegtInGOSt = typeof obj.FachIstBelegtInGOSt === "undefined" ? null : obj.FachIstBelegtInGOSt === null ? null : obj.FachIstBelegtInGOSt;
		if (typeof obj.Aufgabenfeld === "undefined")
			 throw new Error('invalid json format, missing attribute Aufgabenfeld');
		result.Aufgabenfeld = obj.Aufgabenfeld;
		if (typeof obj.Fachgruppe === "undefined")
			 throw new Error('invalid json format, missing attribute Fachgruppe');
		result.Fachgruppe = obj.Fachgruppe;
		if (typeof obj.FarbeClientRGB === "undefined")
			 throw new Error('invalid json format, missing attribute FarbeClientRGB');
		result.FarbeClientRGB = obj.FarbeClientRGB;
		return result;
	}

	public static transpilerToJSON(obj : DruckGostLaufbahnplanungSchuelerFachwahlen) : string {
		let result = '{';
		result += '"SchuelerID" : ' + obj.SchuelerID + ',';
		result += '"Bezeichnung" : ' + JSON.stringify(obj.Bezeichnung!) + ',';
		result += '"Kuerzel" : ' + JSON.stringify(obj.Kuerzel!) + ',';
		result += '"FachIstFortfuehrbareFremdspracheInGOSt" : ' + ((!obj.FachIstFortfuehrbareFremdspracheInGOSt) ? 'null' : obj.FachIstFortfuehrbareFremdspracheInGOSt) + ',';
		result += '"JahrgangFremdsprachenbeginn" : ' + JSON.stringify(obj.JahrgangFremdsprachenbeginn!) + ',';
		result += '"PositionFremdsprachenfolge" : ' + JSON.stringify(obj.PositionFremdsprachenfolge!) + ',';
		result += '"BelegungEF1" : ' + JSON.stringify(obj.BelegungEF1!) + ',';
		result += '"BelegungEF2" : ' + JSON.stringify(obj.BelegungEF2!) + ',';
		result += '"BelegungQ11" : ' + JSON.stringify(obj.BelegungQ11!) + ',';
		result += '"BelegungQ12" : ' + JSON.stringify(obj.BelegungQ12!) + ',';
		result += '"BelegungQ21" : ' + JSON.stringify(obj.BelegungQ21!) + ',';
		result += '"BelegungQ22" : ' + JSON.stringify(obj.BelegungQ22!) + ',';
		result += '"Abiturfach" : ' + JSON.stringify(obj.Abiturfach!) + ',';
		result += '"FachIstBelegtInGOSt" : ' + ((!obj.FachIstBelegtInGOSt) ? 'null' : obj.FachIstBelegtInGOSt) + ',';
		result += '"Aufgabenfeld" : ' + obj.Aufgabenfeld + ',';
		result += '"Fachgruppe" : ' + JSON.stringify(obj.Fachgruppe!) + ',';
		result += '"FarbeClientRGB" : ' + JSON.stringify(obj.FarbeClientRGB!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<DruckGostLaufbahnplanungSchuelerFachwahlen>) : string {
		let result = '{';
		if (typeof obj.SchuelerID !== "undefined") {
			result += '"SchuelerID" : ' + obj.SchuelerID + ',';
		}
		if (typeof obj.Bezeichnung !== "undefined") {
			result += '"Bezeichnung" : ' + JSON.stringify(obj.Bezeichnung!) + ',';
		}
		if (typeof obj.Kuerzel !== "undefined") {
			result += '"Kuerzel" : ' + JSON.stringify(obj.Kuerzel!) + ',';
		}
		if (typeof obj.FachIstFortfuehrbareFremdspracheInGOSt !== "undefined") {
			result += '"FachIstFortfuehrbareFremdspracheInGOSt" : ' + ((!obj.FachIstFortfuehrbareFremdspracheInGOSt) ? 'null' : obj.FachIstFortfuehrbareFremdspracheInGOSt) + ',';
		}
		if (typeof obj.JahrgangFremdsprachenbeginn !== "undefined") {
			result += '"JahrgangFremdsprachenbeginn" : ' + JSON.stringify(obj.JahrgangFremdsprachenbeginn!) + ',';
		}
		if (typeof obj.PositionFremdsprachenfolge !== "undefined") {
			result += '"PositionFremdsprachenfolge" : ' + JSON.stringify(obj.PositionFremdsprachenfolge!) + ',';
		}
		if (typeof obj.BelegungEF1 !== "undefined") {
			result += '"BelegungEF1" : ' + JSON.stringify(obj.BelegungEF1!) + ',';
		}
		if (typeof obj.BelegungEF2 !== "undefined") {
			result += '"BelegungEF2" : ' + JSON.stringify(obj.BelegungEF2!) + ',';
		}
		if (typeof obj.BelegungQ11 !== "undefined") {
			result += '"BelegungQ11" : ' + JSON.stringify(obj.BelegungQ11!) + ',';
		}
		if (typeof obj.BelegungQ12 !== "undefined") {
			result += '"BelegungQ12" : ' + JSON.stringify(obj.BelegungQ12!) + ',';
		}
		if (typeof obj.BelegungQ21 !== "undefined") {
			result += '"BelegungQ21" : ' + JSON.stringify(obj.BelegungQ21!) + ',';
		}
		if (typeof obj.BelegungQ22 !== "undefined") {
			result += '"BelegungQ22" : ' + JSON.stringify(obj.BelegungQ22!) + ',';
		}
		if (typeof obj.Abiturfach !== "undefined") {
			result += '"Abiturfach" : ' + JSON.stringify(obj.Abiturfach!) + ',';
		}
		if (typeof obj.FachIstBelegtInGOSt !== "undefined") {
			result += '"FachIstBelegtInGOSt" : ' + ((!obj.FachIstBelegtInGOSt) ? 'null' : obj.FachIstBelegtInGOSt) + ',';
		}
		if (typeof obj.Aufgabenfeld !== "undefined") {
			result += '"Aufgabenfeld" : ' + obj.Aufgabenfeld + ',';
		}
		if (typeof obj.Fachgruppe !== "undefined") {
			result += '"Fachgruppe" : ' + JSON.stringify(obj.Fachgruppe!) + ',';
		}
		if (typeof obj.FarbeClientRGB !== "undefined") {
			result += '"FarbeClientRGB" : ' + JSON.stringify(obj.FarbeClientRGB!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_druck_DruckGostLaufbahnplanungSchuelerFachwahlen(obj : unknown) : DruckGostLaufbahnplanungSchuelerFachwahlen {
	return obj as DruckGostLaufbahnplanungSchuelerFachwahlen;
}
