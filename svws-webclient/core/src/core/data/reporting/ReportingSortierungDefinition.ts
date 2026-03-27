import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ReportingSortierungDefinition extends JavaObject {

	/**
	 * Die Bezeichnung der Sortierdefinition, die auch zur Anzeige in der UI verwendet werden kann.
	 */
	public bezeichnung: string = "";

	/**
	 * Der Typname des zu sortierenden Reporting-Datentyps, z. B. 'ReportingSchueler' oder 'ReportingKlasse'.
	 */
	public typ: string = "";

	/**
	 * Die Angabe legt fest, ob die definierte Standardsortierung für diesen Typ verwendet werden soll.
	 */
	public verwendeStandardsortierung: boolean = true;

	/**
	 * Liste von Attributnamen für eine benutzerdefinierte Sortierung dieses Typs.
	 */
	public attribute: List<string> = new ArrayList<string>();


	/**
	 * Der Konstruktor der Klasse ReportingSortierungDefinition.
	 * Erzeugt eine Instanz der Klasse und initialisiert sie mit Standardwerten.
	 * Die Klasse beschreibt eine typenbasierte Sortierdefinition für einen Reporting-Datentyp.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingSortierungDefinition';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingSortierungDefinition'].includes(name);
	}

	public static readonly class = new Class<ReportingSortierungDefinition>('de.svws_nrw.core.data.reporting.ReportingSortierungDefinition');

	public static transpilerFromJSON(json: string): ReportingSortierungDefinition {
		const obj = JSON.parse(json) as Partial<ReportingSortierungDefinition>;
		const result = new ReportingSortierungDefinition();
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.typ === undefined)
			throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (obj.verwendeStandardsortierung === undefined)
			throw new Error('invalid json format, missing attribute verwendeStandardsortierung');
		result.verwendeStandardsortierung = obj.verwendeStandardsortierung;
		if (obj.attribute !== undefined) {
			for (const elem of obj.attribute) {
				result.attribute.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ReportingSortierungDefinition): string {
		let result = '{';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"typ" : ' + JSON.stringify(obj.typ) + ',';
		result += '"verwendeStandardsortierung" : ' + obj.verwendeStandardsortierung.toString() + ',';
		result += '"attribute" : [ ';
		for (let i = 0; i < obj.attribute.size(); i++) {
			const elem = obj.attribute.get(i);
			result += '"' + elem + '"';
			if (i < obj.attribute.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingSortierungDefinition>): string {
		let result = '{';
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.typ !== undefined) {
			result += '"typ" : ' + JSON.stringify(obj.typ) + ',';
		}
		if (obj.verwendeStandardsortierung !== undefined) {
			result += '"verwendeStandardsortierung" : ' + obj.verwendeStandardsortierung.toString() + ',';
		}
		if (obj.attribute !== undefined) {
			result += '"attribute" : [ ';
			for (let i = 0; i < obj.attribute.size(); i++) {
				const elem = obj.attribute.get(i);
				result += '"' + elem + '"';
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

export function cast_de_svws_nrw_core_data_reporting_ReportingSortierungDefinition(obj: unknown): ReportingSortierungDefinition {
	return obj as ReportingSortierungDefinition;
}
