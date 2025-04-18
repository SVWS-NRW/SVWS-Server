import { JavaObject } from '../../../../java/lang/JavaObject';
import { SchildReportingDatenquelleAttribut } from '../../../../core/data/schild3/reporting/SchildReportingDatenquelleAttribut';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

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
	public attribute : List<SchildReportingDatenquelleAttribut> = new ArrayList<SchildReportingDatenquelleAttribut>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.reporting.SchildReportingDatenquelle';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.reporting.SchildReportingDatenquelle'].includes(name);
	}

	public static class = new Class<SchildReportingDatenquelle>('de.svws_nrw.core.data.schild3.reporting.SchildReportingDatenquelle');

	public static transpilerFromJSON(json : string): SchildReportingDatenquelle {
		const obj = JSON.parse(json) as Partial<SchildReportingDatenquelle>;
		const result = new SchildReportingDatenquelle();
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (obj.datenart === undefined)
			throw new Error('invalid json format, missing attribute datenart');
		result.datenart = obj.datenart;
		result.master = (obj.master === undefined) ? null : obj.master === null ? null : obj.master;
		result.masterattribut = (obj.masterattribut === undefined) ? null : obj.masterattribut === null ? null : obj.masterattribut;
		result.mastertyp = (obj.mastertyp === undefined) ? null : obj.mastertyp === null ? null : obj.mastertyp;
		result.linkattribut = (obj.linkattribut === undefined) ? null : obj.linkattribut === null ? null : obj.linkattribut;
		if (obj.attribute !== undefined) {
			for (const elem of obj.attribute) {
				result.attribute.add(SchildReportingDatenquelleAttribut.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingDatenquelle) : string {
		let result = '{';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"datenart" : ' + JSON.stringify(obj.datenart) + ',';
		result += '"master" : ' + ((obj.master === null) ? 'null' : JSON.stringify(obj.master)) + ',';
		result += '"masterattribut" : ' + ((obj.masterattribut === null) ? 'null' : JSON.stringify(obj.masterattribut)) + ',';
		result += '"mastertyp" : ' + ((obj.mastertyp === null) ? 'null' : JSON.stringify(obj.mastertyp)) + ',';
		result += '"linkattribut" : ' + ((obj.linkattribut === null) ? 'null' : JSON.stringify(obj.linkattribut)) + ',';
		result += '"attribute" : [ ';
		for (let i = 0; i < obj.attribute.size(); i++) {
			const elem = obj.attribute.get(i);
			result += SchildReportingDatenquelleAttribut.transpilerToJSON(elem);
			if (i < obj.attribute.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingDatenquelle>) : string {
		let result = '{';
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.datenart !== undefined) {
			result += '"datenart" : ' + JSON.stringify(obj.datenart) + ',';
		}
		if (obj.master !== undefined) {
			result += '"master" : ' + ((obj.master === null) ? 'null' : JSON.stringify(obj.master)) + ',';
		}
		if (obj.masterattribut !== undefined) {
			result += '"masterattribut" : ' + ((obj.masterattribut === null) ? 'null' : JSON.stringify(obj.masterattribut)) + ',';
		}
		if (obj.mastertyp !== undefined) {
			result += '"mastertyp" : ' + ((obj.mastertyp === null) ? 'null' : JSON.stringify(obj.mastertyp)) + ',';
		}
		if (obj.linkattribut !== undefined) {
			result += '"linkattribut" : ' + ((obj.linkattribut === null) ? 'null' : JSON.stringify(obj.linkattribut)) + ',';
		}
		if (obj.attribute !== undefined) {
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

}

export function cast_de_svws_nrw_core_data_schild3_reporting_SchildReportingDatenquelle(obj : unknown) : SchildReportingDatenquelle {
	return obj as SchildReportingDatenquelle;
}
