import { JavaObject } from '../../../../java/lang/JavaObject';
import { SchildReportingDatenquelleAttribut } from '../../../../core/data/schild3/reporting/SchildReportingDatenquelleAttribut';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';

export class SchildReportingDatenquelle extends JavaObject {

	/**
	 * Der Name der Datenquelle
	 */
	public name : string = "";

	/**
	 * Die Beschreibung der Datenquelle
	 */
	public beschreibung : string = "";

	/**
	 * Die Art der Daten, welche von der Datenquelle verwaltet werden
	 */
	public datenart : string = "";

	/**
	 * Der Name der Master-Datenquelle
	 */
	public master : string | null = null;

	/**
	 * Der Name des identifizierenden Attributs der Master-Datenquelle
	 */
	public masterattribut : string | null = null;

	/**
	 * Der Typ des Attributs der Master-Datenquelle (z.B. die ID)
	 */
	public mastertyp : string | null = null;

	/**
	 * Der Name des Attributs dieser Datenquelle, welches für die Verbindung zu der Master-Datenquelle genutzt wird
	 */
	public linkattribut : string | null = null;

	/**
	 * Die Liste der JSON-Attribute für diese Datenquelle.
	 */
	public attribute : List<SchildReportingDatenquelleAttribut> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.reporting.SchildReportingDatenquelle';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.reporting.SchildReportingDatenquelle'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingDatenquelle {
		const obj = JSON.parse(json);
		const result = new SchildReportingDatenquelle();
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (typeof obj.datenart === "undefined")
			 throw new Error('invalid json format, missing attribute datenart');
		result.datenart = obj.datenart;
		result.master = typeof obj.master === "undefined" ? null : obj.master === null ? null : obj.master;
		result.masterattribut = typeof obj.masterattribut === "undefined" ? null : obj.masterattribut === null ? null : obj.masterattribut;
		result.mastertyp = typeof obj.mastertyp === "undefined" ? null : obj.mastertyp === null ? null : obj.mastertyp;
		result.linkattribut = typeof obj.linkattribut === "undefined" ? null : obj.linkattribut === null ? null : obj.linkattribut;
		if ((obj.attribute !== undefined) && (obj.attribute !== null)) {
			for (const elem of obj.attribute) {
				result.attribute?.add(SchildReportingDatenquelleAttribut.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingDatenquelle) : string {
		let result = '{';
		result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung!) + ',';
		result += '"datenart" : ' + JSON.stringify(obj.datenart!) + ',';
		result += '"master" : ' + ((!obj.master) ? 'null' : JSON.stringify(obj.master)) + ',';
		result += '"masterattribut" : ' + ((!obj.masterattribut) ? 'null' : JSON.stringify(obj.masterattribut)) + ',';
		result += '"mastertyp" : ' + ((!obj.mastertyp) ? 'null' : JSON.stringify(obj.mastertyp)) + ',';
		result += '"linkattribut" : ' + ((!obj.linkattribut) ? 'null' : JSON.stringify(obj.linkattribut)) + ',';
		if (!obj.attribute) {
			result += '"attribute" : []';
		} else {
			result += '"attribute" : [ ';
			for (let i = 0; i < obj.attribute.size(); i++) {
				const elem = obj.attribute.get(i);
				result += SchildReportingDatenquelleAttribut.transpilerToJSON(elem);
				if (i < obj.attribute.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingDatenquelle>) : string {
		let result = '{';
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung!) + ',';
		}
		if (typeof obj.datenart !== "undefined") {
			result += '"datenart" : ' + JSON.stringify(obj.datenart!) + ',';
		}
		if (typeof obj.master !== "undefined") {
			result += '"master" : ' + ((!obj.master) ? 'null' : JSON.stringify(obj.master)) + ',';
		}
		if (typeof obj.masterattribut !== "undefined") {
			result += '"masterattribut" : ' + ((!obj.masterattribut) ? 'null' : JSON.stringify(obj.masterattribut)) + ',';
		}
		if (typeof obj.mastertyp !== "undefined") {
			result += '"mastertyp" : ' + ((!obj.mastertyp) ? 'null' : JSON.stringify(obj.mastertyp)) + ',';
		}
		if (typeof obj.linkattribut !== "undefined") {
			result += '"linkattribut" : ' + ((!obj.linkattribut) ? 'null' : JSON.stringify(obj.linkattribut)) + ',';
		}
		if (typeof obj.attribute !== "undefined") {
			if (!obj.attribute) {
				result += '"attribute" : []';
			} else {
				result += '"attribute" : [ ';
				for (let i = 0; i < obj.attribute.size(); i++) {
					const elem = obj.attribute.get(i);
					result += SchildReportingDatenquelleAttribut.transpilerToJSON(elem);
					if (i < obj.attribute.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_reporting_SchildReportingDatenquelle(obj : unknown) : SchildReportingDatenquelle {
	return obj as SchildReportingDatenquelle;
}
