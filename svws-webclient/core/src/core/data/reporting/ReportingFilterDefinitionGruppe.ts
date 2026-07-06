import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingFilterVerknuepfung } from '../../../core/types/reporting/ReportingFilterVerknuepfung';
import { ReportingFilterDefinition, cast_de_svws_nrw_core_data_reporting_ReportingFilterDefinition } from '../../../core/data/reporting/ReportingFilterDefinition';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ReportingFilterDefinitionGruppe extends JavaObject {

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
	 * Gibt an, ob mehrere Filterdefinitionen ausgewählt werden können.
	 */
	public uiIstFilterMultiselect: boolean = false;

	/**
	 * Gibt an, wie bei Mehrfachauswahl die verschiedenen Definitionen miteinander verknüpft werden sollen.
	 */
	public uiFilterMultiselectVerknuepfung: number = ReportingFilterVerknuepfung.AND.getId();

	/**
	 * Der mindestens erforderliche ServerMode (stable|beta|alpha|dev), damit die Gruppe in der UI verfügbar ist. Leer = in allen Modi verfügbar.
	 */
	public uiErforderlicherServerMode: string = "";

	/**
	 * Die IDs der Benutzerkompetenzen (OR-verknüpft), die zur Nutzung der Gruppe erforderlich sind. Leer = keine Kompetenz erforderlich.
	 */
	public uiErforderlicheKompetenzen: List<number> = new ArrayList<number>();

	/**
	 * Eine Liste von Filterdefinitionen, die in dieser Gruppe zur Verfügung stehen.
	 */
	public filterDefinitionenOptionen: List<ReportingFilterDefinition> = new ArrayList<ReportingFilterDefinition>();

	/**
	 * Eine Liste von Filterdefinitionen, die in dieser Gruppe ausgewählt wurden.
	 */
	public filterDefinitionen: List<ReportingFilterDefinition> = new ArrayList<ReportingFilterDefinition>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe'].includes(name);
	}

	public static readonly class = new Class<ReportingFilterDefinitionGruppe>('de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe');

	public static transpilerFromJSON(json: string): ReportingFilterDefinitionGruppe {
		const obj = JSON.parse(json) as Partial<ReportingFilterDefinitionGruppe>;
		const result = new ReportingFilterDefinitionGruppe();
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.typ === undefined)
			throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (obj.uiIstSichtbar === undefined)
			throw new Error('invalid json format, missing attribute uiIstSichtbar');
		result.uiIstSichtbar = obj.uiIstSichtbar;
		if (obj.uiIstFilterMultiselect === undefined)
			throw new Error('invalid json format, missing attribute uiIstFilterMultiselect');
		result.uiIstFilterMultiselect = obj.uiIstFilterMultiselect;
		if (obj.uiFilterMultiselectVerknuepfung === undefined)
			throw new Error('invalid json format, missing attribute uiFilterMultiselectVerknuepfung');
		result.uiFilterMultiselectVerknuepfung = obj.uiFilterMultiselectVerknuepfung;
		if (obj.uiErforderlicherServerMode === undefined)
			throw new Error('invalid json format, missing attribute uiErforderlicherServerMode');
		result.uiErforderlicherServerMode = obj.uiErforderlicherServerMode;
		if (obj.uiErforderlicheKompetenzen !== undefined) {
			for (const elem of obj.uiErforderlicheKompetenzen) {
				result.uiErforderlicheKompetenzen.add(elem);
			}
		}
		if (obj.filterDefinitionenOptionen !== undefined) {
			for (const elem of obj.filterDefinitionenOptionen) {
				result.filterDefinitionenOptionen.add(ReportingFilterDefinition.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.filterDefinitionen !== undefined) {
			for (const elem of obj.filterDefinitionen) {
				result.filterDefinitionen.add(ReportingFilterDefinition.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ReportingFilterDefinitionGruppe): string {
		let result = '{';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"typ" : ' + JSON.stringify(obj.typ) + ',';
		result += '"uiIstSichtbar" : ' + obj.uiIstSichtbar.toString() + ',';
		result += '"uiIstFilterMultiselect" : ' + obj.uiIstFilterMultiselect.toString() + ',';
		result += '"uiFilterMultiselectVerknuepfung" : ' + obj.uiFilterMultiselectVerknuepfung.toString() + ',';
		result += '"uiErforderlicherServerMode" : ' + JSON.stringify(obj.uiErforderlicherServerMode) + ',';
		result += '"uiErforderlicheKompetenzen" : [ ';
		for (let i = 0; i < obj.uiErforderlicheKompetenzen.size(); i++) {
			const elem = obj.uiErforderlicheKompetenzen.get(i);
			result += elem.toString();
			if (i < obj.uiErforderlicheKompetenzen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"filterDefinitionenOptionen" : [ ';
		for (let i = 0; i < obj.filterDefinitionenOptionen.size(); i++) {
			const elem = obj.filterDefinitionenOptionen.get(i);
			result += ReportingFilterDefinition.transpilerToJSON(elem);
			if (i < obj.filterDefinitionenOptionen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"filterDefinitionen" : [ ';
		for (let i = 0; i < obj.filterDefinitionen.size(); i++) {
			const elem = obj.filterDefinitionen.get(i);
			result += ReportingFilterDefinition.transpilerToJSON(elem);
			if (i < obj.filterDefinitionen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingFilterDefinitionGruppe>): string {
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
		if (obj.uiIstFilterMultiselect !== undefined) {
			result += '"uiIstFilterMultiselect" : ' + obj.uiIstFilterMultiselect.toString() + ',';
		}
		if (obj.uiFilterMultiselectVerknuepfung !== undefined) {
			result += '"uiFilterMultiselectVerknuepfung" : ' + obj.uiFilterMultiselectVerknuepfung.toString() + ',';
		}
		if (obj.uiErforderlicherServerMode !== undefined) {
			result += '"uiErforderlicherServerMode" : ' + JSON.stringify(obj.uiErforderlicherServerMode) + ',';
		}
		if (obj.uiErforderlicheKompetenzen !== undefined) {
			result += '"uiErforderlicheKompetenzen" : [ ';
			for (let i = 0; i < obj.uiErforderlicheKompetenzen.size(); i++) {
				const elem = obj.uiErforderlicheKompetenzen.get(i);
				result += elem.toString();
				if (i < obj.uiErforderlicheKompetenzen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.filterDefinitionenOptionen !== undefined) {
			result += '"filterDefinitionenOptionen" : [ ';
			for (let i = 0; i < obj.filterDefinitionenOptionen.size(); i++) {
				const elem = obj.filterDefinitionenOptionen.get(i);
				result += ReportingFilterDefinition.transpilerToJSON(elem);
				if (i < obj.filterDefinitionenOptionen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.filterDefinitionen !== undefined) {
			result += '"filterDefinitionen" : [ ';
			for (let i = 0; i < obj.filterDefinitionen.size(); i++) {
				const elem = obj.filterDefinitionen.get(i);
				result += ReportingFilterDefinition.transpilerToJSON(elem);
				if (i < obj.filterDefinitionen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingFilterDefinitionGruppe(obj: unknown): ReportingFilterDefinitionGruppe {
	return obj as ReportingFilterDefinitionGruppe;
}
