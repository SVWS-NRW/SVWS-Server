import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { UVv1LehrerPflichtstundensollExport } from '../../../../core/data/uv/export/UVv1LehrerPflichtstundensollExport';
import { Class } from '../../../../java/lang/Class';
import { UVv1LehrerAnrechnungsstundenExport } from '../../../../core/data/uv/export/UVv1LehrerAnrechnungsstundenExport';
import { UVv1LehrerUnterrichtsfachExport } from '../../../../core/data/uv/export/UVv1LehrerUnterrichtsfachExport';

export class UVv1LehrerExport extends JavaObject {

	/**
	 * Die eindeutige UV-ID des Lehrers im Planungsabschnitt (planungsspezifisch).
	 */
	public uvId: number = -1;

	/**
	 * Die ID des Lehrers in den Lehrerdaten.
	 */
	public idLehrer: number | null = null;

	/**
	 * Das Lehrer-Kürzel für eine eindeutige Identifikation.
	 */
	public kuerzel: string = "";

	/**
	 * Der Nachname des Lehrers.
	 */
	public nachname: string | null = null;

	/**
	 * Der Vorname (bzw. Rufname) des Lehrers.
	 */
	public vorname: string | null = null;

	/**
	 * Ein Array mit den Unterrichtsfächern des Lehrers
	 */
	public unterrichtsfaecher: List<UVv1LehrerUnterrichtsfachExport> = new ArrayList<UVv1LehrerUnterrichtsfachExport>();

	/**
	 * Ein Array mit den Anrechnungsstunden des Lehrers
	 */
	public anrechnungsstunden: List<UVv1LehrerAnrechnungsstundenExport> = new ArrayList<UVv1LehrerAnrechnungsstundenExport>();

	/**
	 * Ein Array mit dem Pflichtstundensoll des Lehrers
	 */
	public pflichtstundensoll: List<UVv1LehrerPflichtstundensollExport> = new ArrayList<UVv1LehrerPflichtstundensollExport>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1LehrerExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1LehrerExport'].includes(name);
	}

	public static readonly class = new Class<UVv1LehrerExport>('de.svws_nrw.core.data.uv.export.UVv1LehrerExport');

	public static transpilerFromJSON(json: string): UVv1LehrerExport {
		const obj = JSON.parse(json) as Partial<UVv1LehrerExport>;
		const result = new UVv1LehrerExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		result.idLehrer = (obj.idLehrer === undefined) ? null : obj.idLehrer === null ? null : obj.idLehrer;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.nachname = (obj.nachname === undefined) ? null : obj.nachname === null ? null : obj.nachname;
		result.vorname = (obj.vorname === undefined) ? null : obj.vorname === null ? null : obj.vorname;
		if (obj.unterrichtsfaecher !== undefined) {
			for (const elem of obj.unterrichtsfaecher) {
				result.unterrichtsfaecher.add(UVv1LehrerUnterrichtsfachExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.anrechnungsstunden !== undefined) {
			for (const elem of obj.anrechnungsstunden) {
				result.anrechnungsstunden.add(UVv1LehrerAnrechnungsstundenExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.pflichtstundensoll !== undefined) {
			for (const elem of obj.pflichtstundensoll) {
				result.pflichtstundensoll.add(UVv1LehrerPflichtstundensollExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1LehrerExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"idLehrer" : ' + ((obj.idLehrer === null) ? 'null' : obj.idLehrer.toString()) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result += '"unterrichtsfaecher" : [ ';
		for (let i = 0; i < obj.unterrichtsfaecher.size(); i++) {
			const elem = obj.unterrichtsfaecher.get(i);
			result += UVv1LehrerUnterrichtsfachExport.transpilerToJSON(elem);
			if (i < obj.unterrichtsfaecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"anrechnungsstunden" : [ ';
		for (let i = 0; i < obj.anrechnungsstunden.size(); i++) {
			const elem = obj.anrechnungsstunden.get(i);
			result += UVv1LehrerAnrechnungsstundenExport.transpilerToJSON(elem);
			if (i < obj.anrechnungsstunden.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"pflichtstundensoll" : [ ';
		for (let i = 0; i < obj.pflichtstundensoll.size(); i++) {
			const elem = obj.pflichtstundensoll.get(i);
			result += UVv1LehrerPflichtstundensollExport.transpilerToJSON(elem);
			if (i < obj.pflichtstundensoll.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1LehrerExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + ((obj.idLehrer === null) ? 'null' : obj.idLehrer.toString()) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		if (obj.unterrichtsfaecher !== undefined) {
			result += '"unterrichtsfaecher" : [ ';
			for (let i = 0; i < obj.unterrichtsfaecher.size(); i++) {
				const elem = obj.unterrichtsfaecher.get(i);
				result += UVv1LehrerUnterrichtsfachExport.transpilerToJSON(elem);
				if (i < obj.unterrichtsfaecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.anrechnungsstunden !== undefined) {
			result += '"anrechnungsstunden" : [ ';
			for (let i = 0; i < obj.anrechnungsstunden.size(); i++) {
				const elem = obj.anrechnungsstunden.get(i);
				result += UVv1LehrerAnrechnungsstundenExport.transpilerToJSON(elem);
				if (i < obj.anrechnungsstunden.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.pflichtstundensoll !== undefined) {
			result += '"pflichtstundensoll" : [ ';
			for (let i = 0; i < obj.pflichtstundensoll.size(); i++) {
				const elem = obj.pflichtstundensoll.get(i);
				result += UVv1LehrerPflichtstundensollExport.transpilerToJSON(elem);
				if (i < obj.pflichtstundensoll.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1LehrerExport(obj: unknown): UVv1LehrerExport {
	return obj as UVv1LehrerExport;
}
