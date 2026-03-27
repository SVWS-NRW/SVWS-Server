import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ReportingSortierungDefinition, cast_de_svws_nrw_core_data_reporting_ReportingSortierungDefinition } from '../../../core/data/reporting/ReportingSortierungDefinition';

export class ReportingSortierungDefinitionGruppe extends JavaObject {

	/**
	 * Die Bezeichnung der Sortierdefinition, die auch zur Anzeige in der UI verwendet werden kann.
	 */
	public bezeichnung: string = "";

	/**
	 * Der Typname des zu sortierenden Reporting-Datentyps dieser Gruppe, z. B. 'ReportingSchueler' oder 'ReportingKlasse'.
	 */
	public typ: string = "";

	/**
	 * Gibt an, ob die Gruppe in der UI sichtbar sein soll.
	 */
	public uiIstSichtbar: boolean = true;

	/**
	 * Eine Liste von Sortierdefinitionen, die in dieser Gruppe zur Verfügung stehen.
	 */
	public sortierungDefinitionenOptionen: List<ReportingSortierungDefinition> = new ArrayList<ReportingSortierungDefinition>();

	/**
	 * Eine Liste von Sortierdefinitionen, die in dieser Gruppe ausgewählt wurden.
	 */
	public sortierungDefinitionen: List<ReportingSortierungDefinition> = new ArrayList<ReportingSortierungDefinition>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe'].includes(name);
	}

	public static readonly class = new Class<ReportingSortierungDefinitionGruppe>('de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe');

	public static transpilerFromJSON(json: string): ReportingSortierungDefinitionGruppe {
		const obj = JSON.parse(json) as Partial<ReportingSortierungDefinitionGruppe>;
		const result = new ReportingSortierungDefinitionGruppe();
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.typ === undefined)
			throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (obj.uiIstSichtbar === undefined)
			throw new Error('invalid json format, missing attribute uiIstSichtbar');
		result.uiIstSichtbar = obj.uiIstSichtbar;
		if (obj.sortierungDefinitionenOptionen !== undefined) {
			for (const elem of obj.sortierungDefinitionenOptionen) {
				result.sortierungDefinitionenOptionen.add(ReportingSortierungDefinition.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.sortierungDefinitionen !== undefined) {
			for (const elem of obj.sortierungDefinitionen) {
				result.sortierungDefinitionen.add(ReportingSortierungDefinition.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ReportingSortierungDefinitionGruppe): string {
		let result = '{';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"typ" : ' + JSON.stringify(obj.typ) + ',';
		result += '"uiIstSichtbar" : ' + obj.uiIstSichtbar.toString() + ',';
		result += '"sortierungDefinitionenOptionen" : [ ';
		for (let i = 0; i < obj.sortierungDefinitionenOptionen.size(); i++) {
			const elem = obj.sortierungDefinitionenOptionen.get(i);
			result += ReportingSortierungDefinition.transpilerToJSON(elem);
			if (i < obj.sortierungDefinitionenOptionen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"sortierungDefinitionen" : [ ';
		for (let i = 0; i < obj.sortierungDefinitionen.size(); i++) {
			const elem = obj.sortierungDefinitionen.get(i);
			result += ReportingSortierungDefinition.transpilerToJSON(elem);
			if (i < obj.sortierungDefinitionen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingSortierungDefinitionGruppe>): string {
		let result = '{';
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.typ !== undefined) {
			result += '"typ" : ' + JSON.stringify(obj.typ) + ',';
		}
		if (obj.uiIstSichtbar !== undefined) {
			result += '"uiIstSichtbar" : ' + obj.uiIstSichtbar.toString() + ',';
		}
		if (obj.sortierungDefinitionenOptionen !== undefined) {
			result += '"sortierungDefinitionenOptionen" : [ ';
			for (let i = 0; i < obj.sortierungDefinitionenOptionen.size(); i++) {
				const elem = obj.sortierungDefinitionenOptionen.get(i);
				result += ReportingSortierungDefinition.transpilerToJSON(elem);
				if (i < obj.sortierungDefinitionenOptionen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.sortierungDefinitionen !== undefined) {
			result += '"sortierungDefinitionen" : [ ';
			for (let i = 0; i < obj.sortierungDefinitionen.size(); i++) {
				const elem = obj.sortierungDefinitionen.get(i);
				result += ReportingSortierungDefinition.transpilerToJSON(elem);
				if (i < obj.sortierungDefinitionen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingSortierungDefinitionGruppe(obj: unknown): ReportingSortierungDefinitionGruppe {
	return obj as ReportingSortierungDefinitionGruppe;
}
