import { JavaObject } from '../../../../java/lang/JavaObject';
import { UVv1StundentafelExport } from '../../../../core/data/uv/export/UVv1StundentafelExport';
import { UVv1RaumExport } from '../../../../core/data/uv/export/UVv1RaumExport';
import { UVv1LehrerExport } from '../../../../core/data/uv/export/UVv1LehrerExport';
import { UVv1RaumgruppeExport } from '../../../../core/data/uv/export/UVv1RaumgruppeExport';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { UVv1ZeitrasterExport } from '../../../../core/data/uv/export/UVv1ZeitrasterExport';
import { UVv1FachExport } from '../../../../core/data/uv/export/UVv1FachExport';

export class UVv1GrunddatenExport extends JavaObject {

	/**
	 * Ein Array mit den Lehrern der UV.
	 */
	public lehrer: List<UVv1LehrerExport> = new ArrayList<UVv1LehrerExport>();

	/**
	 * Ein Array mit den Räumen der UV.
	 */
	public raeume: List<UVv1RaumExport> = new ArrayList<UVv1RaumExport>();

	/**
	 * Ein Array mit den Raumgruppen der UV.
	 */
	public raumgruppen: List<UVv1RaumgruppeExport> = new ArrayList<UVv1RaumgruppeExport>();

	/**
	 * Ein Array mit den Stundentafeln der UV.
	 */
	public stundentafeln: List<UVv1StundentafelExport> = new ArrayList<UVv1StundentafelExport>();

	/**
	 * Ein Array mit den Zeitrastern der UV.
	 */
	public zeitraster: List<UVv1ZeitrasterExport> = new ArrayList<UVv1ZeitrasterExport>();

	/**
	 * Ein Array mit den Fächern der UV.
	 */
	public faecher: List<UVv1FachExport> = new ArrayList<UVv1FachExport>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1GrunddatenExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1GrunddatenExport'].includes(name);
	}

	public static readonly class = new Class<UVv1GrunddatenExport>('de.svws_nrw.core.data.uv.export.UVv1GrunddatenExport');

	public static transpilerFromJSON(json: string): UVv1GrunddatenExport {
		const obj = JSON.parse(json) as Partial<UVv1GrunddatenExport>;
		const result = new UVv1GrunddatenExport();
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(UVv1LehrerExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raeume !== undefined) {
			for (const elem of obj.raeume) {
				result.raeume.add(UVv1RaumExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raumgruppen !== undefined) {
			for (const elem of obj.raumgruppen) {
				result.raumgruppen.add(UVv1RaumgruppeExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.stundentafeln !== undefined) {
			for (const elem of obj.stundentafeln) {
				result.stundentafeln.add(UVv1StundentafelExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.zeitraster !== undefined) {
			for (const elem of obj.zeitraster) {
				result.zeitraster.add(UVv1ZeitrasterExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(UVv1FachExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1GrunddatenExport): string {
		let result = '{';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += UVv1LehrerExport.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raeume" : [ ';
		for (let i = 0; i < obj.raeume.size(); i++) {
			const elem = obj.raeume.get(i);
			result += UVv1RaumExport.transpilerToJSON(elem);
			if (i < obj.raeume.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raumgruppen" : [ ';
		for (let i = 0; i < obj.raumgruppen.size(); i++) {
			const elem = obj.raumgruppen.get(i);
			result += UVv1RaumgruppeExport.transpilerToJSON(elem);
			if (i < obj.raumgruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"stundentafeln" : [ ';
		for (let i = 0; i < obj.stundentafeln.size(); i++) {
			const elem = obj.stundentafeln.get(i);
			result += UVv1StundentafelExport.transpilerToJSON(elem);
			if (i < obj.stundentafeln.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"zeitraster" : [ ';
		for (let i = 0; i < obj.zeitraster.size(); i++) {
			const elem = obj.zeitraster.get(i);
			result += UVv1ZeitrasterExport.transpilerToJSON(elem);
			if (i < obj.zeitraster.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += UVv1FachExport.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1GrunddatenExport>): string {
		let result = '{';
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += UVv1LehrerExport.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raeume !== undefined) {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += UVv1RaumExport.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raumgruppen !== undefined) {
			result += '"raumgruppen" : [ ';
			for (let i = 0; i < obj.raumgruppen.size(); i++) {
				const elem = obj.raumgruppen.get(i);
				result += UVv1RaumgruppeExport.transpilerToJSON(elem);
				if (i < obj.raumgruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.stundentafeln !== undefined) {
			result += '"stundentafeln" : [ ';
			for (let i = 0; i < obj.stundentafeln.size(); i++) {
				const elem = obj.stundentafeln.get(i);
				result += UVv1StundentafelExport.transpilerToJSON(elem);
				if (i < obj.stundentafeln.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.zeitraster !== undefined) {
			result += '"zeitraster" : [ ';
			for (let i = 0; i < obj.zeitraster.size(); i++) {
				const elem = obj.zeitraster.get(i);
				result += UVv1ZeitrasterExport.transpilerToJSON(elem);
				if (i < obj.zeitraster.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += UVv1FachExport.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1GrunddatenExport(obj: unknown): UVv1GrunddatenExport {
	return obj as UVv1GrunddatenExport;
}
