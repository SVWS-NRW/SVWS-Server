import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingFilterVerknuepfung } from '../../../core/types/reporting/ReportingFilterVerknuepfung';
import { ReportingFilterEintrag } from '../../../core/data/reporting/ReportingFilterEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ReportingFilterKriterium extends JavaObject {

	/**
	 * Die Art der logischen Verknüpfung für die Listen 'eintraege' und 'unterkriterien'.
	 */
	public verknuepfung: number = ReportingFilterVerknuepfung.AND.getId();

	/**
	 * Liste von konkreten Filter-Einträgen (Attributvergleiche), die gemäß 'verknuepfung' verbunden werden.
	 */
	public eintraege: List<ReportingFilterEintrag> = new ArrayList<ReportingFilterEintrag>();

	/**
	 * Liste von Unterkriterien, die gemäß 'verknuepfung' mit den Einträgen verbunden werden.
	 */
	public unterkriterien: List<ReportingFilterKriterium> = new ArrayList<ReportingFilterKriterium>();

	/**
	 * Gibt an, ob das Gesamtergebnis dieser Gruppe negiert werden soll (NICHT).
	 */
	public nicht: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingFilterKriterium';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingFilterKriterium'].includes(name);
	}

	public static readonly class = new Class<ReportingFilterKriterium>('de.svws_nrw.core.data.reporting.ReportingFilterKriterium');

	public static transpilerFromJSON(json: string): ReportingFilterKriterium {
		const obj = JSON.parse(json) as Partial<ReportingFilterKriterium>;
		const result = new ReportingFilterKriterium();
		if (obj.verknuepfung === undefined)
			throw new Error('invalid json format, missing attribute verknuepfung');
		result.verknuepfung = obj.verknuepfung;
		if (obj.eintraege !== undefined) {
			for (const elem of obj.eintraege) {
				result.eintraege.add(ReportingFilterEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.unterkriterien !== undefined) {
			for (const elem of obj.unterkriterien) {
				result.unterkriterien.add(ReportingFilterKriterium.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.nicht === undefined)
			throw new Error('invalid json format, missing attribute nicht');
		result.nicht = obj.nicht;
		return result;
	}

	public static transpilerToJSON(obj: ReportingFilterKriterium): string {
		let result = '{';
		result += '"verknuepfung" : ' + obj.verknuepfung.toString() + ',';
		result += '"eintraege" : [ ';
		for (let i = 0; i < obj.eintraege.size(); i++) {
			const elem = obj.eintraege.get(i);
			result += ReportingFilterEintrag.transpilerToJSON(elem);
			if (i < obj.eintraege.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"unterkriterien" : [ ';
		for (let i = 0; i < obj.unterkriterien.size(); i++) {
			const elem = obj.unterkriterien.get(i);
			result += ReportingFilterKriterium.transpilerToJSON(elem);
			if (i < obj.unterkriterien.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"nicht" : ' + obj.nicht.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingFilterKriterium>): string {
		let result = '{';
		if (obj.verknuepfung !== undefined) {
			result += '"verknuepfung" : ' + obj.verknuepfung.toString() + ',';
		}
		if (obj.eintraege !== undefined) {
			result += '"eintraege" : [ ';
			for (let i = 0; i < obj.eintraege.size(); i++) {
				const elem = obj.eintraege.get(i);
				result += ReportingFilterEintrag.transpilerToJSON(elem);
				if (i < obj.eintraege.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.unterkriterien !== undefined) {
			result += '"unterkriterien" : [ ';
			for (let i = 0; i < obj.unterkriterien.size(); i++) {
				const elem = obj.unterkriterien.get(i);
				result += ReportingFilterKriterium.transpilerToJSON(elem);
				if (i < obj.unterkriterien.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.nicht !== undefined) {
			result += '"nicht" : ' + obj.nicht.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingFilterKriterium(obj: unknown): ReportingFilterKriterium {
	return obj as ReportingFilterKriterium;
}
