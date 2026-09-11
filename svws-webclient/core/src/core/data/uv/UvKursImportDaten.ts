import { JavaObject } from '../../../java/lang/JavaObject';
import { UvLerngruppeCreateRequest } from '../../../core/data/uv/UvLerngruppeCreateRequest';
import { UvPlanungsabschnittLehrerCreateRequest } from '../../../core/data/uv/UvPlanungsabschnittLehrerCreateRequest';
import { UvSchieneCreateRequest } from '../../../core/data/uv/UvSchieneCreateRequest';
import { ArrayList } from '../../../java/util/ArrayList';
import { UvSchuelergruppeSchuelerCreateRequest } from '../../../core/data/uv/UvSchuelergruppeSchuelerCreateRequest';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { UvKursCreateRequest } from '../../../core/data/uv/UvKursCreateRequest';
import { UvSchuelergruppeCreateRequest } from '../../../core/data/uv/UvSchuelergruppeCreateRequest';
import { UvLerngruppenLehrerCreateRequest } from '../../../core/data/uv/UvLerngruppenLehrerCreateRequest';
import { UvLerngruppenSchieneCreateRequest } from '../../../core/data/uv/UvLerngruppenSchieneCreateRequest';

export class UvKursImportDaten extends JavaObject {

	/**
	 * Die anzulegenden Planungsabschnitt-Lehrer-Zuordnungen.
	 */
	public planungsabschnittlehrer: List<UvPlanungsabschnittLehrerCreateRequest> = new ArrayList<UvPlanungsabschnittLehrerCreateRequest>();

	/**
	 * Die anzulegenden Schienen.
	 */
	public schienen: List<UvSchieneCreateRequest> = new ArrayList<UvSchieneCreateRequest>();

	/**
	 * Die anzulegenden Schülergruppen.
	 */
	public schuelergruppen: List<UvSchuelergruppeCreateRequest> = new ArrayList<UvSchuelergruppeCreateRequest>();

	/**
	 * Die anzulegenden Kurse.
	 */
	public kurse: List<UvKursCreateRequest> = new ArrayList<UvKursCreateRequest>();

	/**
	 * Die anzulegenden Lerngruppen.
	 */
	public lerngruppen: List<UvLerngruppeCreateRequest> = new ArrayList<UvLerngruppeCreateRequest>();

	/**
	 * Die anzulegenden Lehrer-Lerngruppen-Zuordnungen.
	 */
	public lerngruppenlehrer: List<UvLerngruppenLehrerCreateRequest> = new ArrayList<UvLerngruppenLehrerCreateRequest>();

	/**
	 * Die anzulegenden Schienen-Lerngruppen-Zuordnungen.
	 */
	public lerngruppenschienen: List<UvLerngruppenSchieneCreateRequest> = new ArrayList<UvLerngruppenSchieneCreateRequest>();

	/**
	 * Die anzulegenden Schüler-Schülergruppen-Zuordnungen.
	 */
	public schuelergruppenschueler: List<UvSchuelergruppeSchuelerCreateRequest> = new ArrayList<UvSchuelergruppeSchuelerCreateRequest>();


	/**
	 *Erzeugt ein leeres Importdatenobjekt.
	 */
	public constructor() {
		super();
	}

	/**
	 * Prüft, ob alle beim Import neu anzulegenden Objekte negative temporäre IDs besitzen.
	 *
	 * @return {@code true}, wenn alle temporären IDs negativ sind
	 */
	public isGueltigeTempIds(): boolean {
		for (const kurs of this.kurse) {
			if ((kurs.id === null) || (kurs.id >= 0)) {
				return false;
			}
		}
		for (const lerngruppe of this.lerngruppen) {
			if ((lerngruppe.id === null) || (lerngruppe.id >= 0)) {
				return false;
			}
		}
		for (const schuelergruppe of this.schuelergruppen) {
			if ((schuelergruppe.id === null) || (schuelergruppe.id >= 0)) {
				return false;
			}
		}
		for (const schiene of this.schienen) {
			if ((schiene.id === null) || (schiene.id >= 0)) {
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvKursImportDaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvKursImportDaten'].includes(name);
	}

	public static readonly class = new Class<UvKursImportDaten>('de.svws_nrw.core.data.uv.UvKursImportDaten');

	public static transpilerFromJSON(json: string): UvKursImportDaten {
		const obj = JSON.parse(json) as Partial<UvKursImportDaten>;
		const result = new UvKursImportDaten();
		if (obj.planungsabschnittlehrer !== undefined) {
			for (const elem of obj.planungsabschnittlehrer) {
				result.planungsabschnittlehrer.add(UvPlanungsabschnittLehrerCreateRequest.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schienen !== undefined) {
			for (const elem of obj.schienen) {
				result.schienen.add(UvSchieneCreateRequest.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelergruppen !== undefined) {
			for (const elem of obj.schuelergruppen) {
				result.schuelergruppen.add(UvSchuelergruppeCreateRequest.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kurse !== undefined) {
			for (const elem of obj.kurse) {
				result.kurse.add(UvKursCreateRequest.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppen !== undefined) {
			for (const elem of obj.lerngruppen) {
				result.lerngruppen.add(UvLerngruppeCreateRequest.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppenlehrer !== undefined) {
			for (const elem of obj.lerngruppenlehrer) {
				result.lerngruppenlehrer.add(UvLerngruppenLehrerCreateRequest.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppenschienen !== undefined) {
			for (const elem of obj.lerngruppenschienen) {
				result.lerngruppenschienen.add(UvLerngruppenSchieneCreateRequest.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelergruppenschueler !== undefined) {
			for (const elem of obj.schuelergruppenschueler) {
				result.schuelergruppenschueler.add(UvSchuelergruppeSchuelerCreateRequest.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvKursImportDaten): string {
		let result = '{';
		result += '"planungsabschnittlehrer" : [ ';
		for (let i = 0; i < obj.planungsabschnittlehrer.size(); i++) {
			const elem = obj.planungsabschnittlehrer.get(i);
			result += UvPlanungsabschnittLehrerCreateRequest.transpilerToJSON(elem);
			if (i < obj.planungsabschnittlehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schienen" : [ ';
		for (let i = 0; i < obj.schienen.size(); i++) {
			const elem = obj.schienen.get(i);
			result += UvSchieneCreateRequest.transpilerToJSON(elem);
			if (i < obj.schienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelergruppen" : [ ';
		for (let i = 0; i < obj.schuelergruppen.size(); i++) {
			const elem = obj.schuelergruppen.get(i);
			result += UvSchuelergruppeCreateRequest.transpilerToJSON(elem);
			if (i < obj.schuelergruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kurse" : [ ';
		for (let i = 0; i < obj.kurse.size(); i++) {
			const elem = obj.kurse.get(i);
			result += UvKursCreateRequest.transpilerToJSON(elem);
			if (i < obj.kurse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppen" : [ ';
		for (let i = 0; i < obj.lerngruppen.size(); i++) {
			const elem = obj.lerngruppen.get(i);
			result += UvLerngruppeCreateRequest.transpilerToJSON(elem);
			if (i < obj.lerngruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppenlehrer" : [ ';
		for (let i = 0; i < obj.lerngruppenlehrer.size(); i++) {
			const elem = obj.lerngruppenlehrer.get(i);
			result += UvLerngruppenLehrerCreateRequest.transpilerToJSON(elem);
			if (i < obj.lerngruppenlehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppenschienen" : [ ';
		for (let i = 0; i < obj.lerngruppenschienen.size(); i++) {
			const elem = obj.lerngruppenschienen.get(i);
			result += UvLerngruppenSchieneCreateRequest.transpilerToJSON(elem);
			if (i < obj.lerngruppenschienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelergruppenschueler" : [ ';
		for (let i = 0; i < obj.schuelergruppenschueler.size(); i++) {
			const elem = obj.schuelergruppenschueler.get(i);
			result += UvSchuelergruppeSchuelerCreateRequest.transpilerToJSON(elem);
			if (i < obj.schuelergruppenschueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvKursImportDaten>): string {
		let result = '{';
		if (obj.planungsabschnittlehrer !== undefined) {
			result += '"planungsabschnittlehrer" : [ ';
			for (let i = 0; i < obj.planungsabschnittlehrer.size(); i++) {
				const elem = obj.planungsabschnittlehrer.get(i);
				result += UvPlanungsabschnittLehrerCreateRequest.transpilerToJSON(elem);
				if (i < obj.planungsabschnittlehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schienen !== undefined) {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += UvSchieneCreateRequest.transpilerToJSON(elem);
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelergruppen !== undefined) {
			result += '"schuelergruppen" : [ ';
			for (let i = 0; i < obj.schuelergruppen.size(); i++) {
				const elem = obj.schuelergruppen.get(i);
				result += UvSchuelergruppeCreateRequest.transpilerToJSON(elem);
				if (i < obj.schuelergruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kurse !== undefined) {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += UvKursCreateRequest.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppen !== undefined) {
			result += '"lerngruppen" : [ ';
			for (let i = 0; i < obj.lerngruppen.size(); i++) {
				const elem = obj.lerngruppen.get(i);
				result += UvLerngruppeCreateRequest.transpilerToJSON(elem);
				if (i < obj.lerngruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppenlehrer !== undefined) {
			result += '"lerngruppenlehrer" : [ ';
			for (let i = 0; i < obj.lerngruppenlehrer.size(); i++) {
				const elem = obj.lerngruppenlehrer.get(i);
				result += UvLerngruppenLehrerCreateRequest.transpilerToJSON(elem);
				if (i < obj.lerngruppenlehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppenschienen !== undefined) {
			result += '"lerngruppenschienen" : [ ';
			for (let i = 0; i < obj.lerngruppenschienen.size(); i++) {
				const elem = obj.lerngruppenschienen.get(i);
				result += UvLerngruppenSchieneCreateRequest.transpilerToJSON(elem);
				if (i < obj.lerngruppenschienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelergruppenschueler !== undefined) {
			result += '"schuelergruppenschueler" : [ ';
			for (let i = 0; i < obj.schuelergruppenschueler.size(); i++) {
				const elem = obj.schuelergruppenschueler.get(i);
				result += UvSchuelergruppeSchuelerCreateRequest.transpilerToJSON(elem);
				if (i < obj.schuelergruppenschueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvKursImportDaten(obj: unknown): UvKursImportDaten {
	return obj as UvKursImportDaten;
}
