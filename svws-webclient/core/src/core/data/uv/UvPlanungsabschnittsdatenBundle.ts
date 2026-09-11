import { JavaObject } from '../../../java/lang/JavaObject';
import { UvPlanungsabschnitt, cast_de_svws_nrw_core_data_uv_UvPlanungsabschnitt } from '../../../core/data/uv/UvPlanungsabschnitt';
import { UvSchuelergruppe } from '../../../core/data/uv/UvSchuelergruppe';
import { UvLerngruppenLehrer } from '../../../core/data/uv/UvLerngruppenLehrer';
import { UvKurs } from '../../../core/data/uv/UvKurs';
import { UvLerngruppenSchiene } from '../../../core/data/uv/UvLerngruppenSchiene';
import { UvPlanungsabschnittSchueler } from '../../../core/data/uv/UvPlanungsabschnittSchueler';
import { ArrayList } from '../../../java/util/ArrayList';
import { UvPlanungsabschnittZeitraster } from '../../../core/data/uv/UvPlanungsabschnittZeitraster';
import { UvSchiene } from '../../../core/data/uv/UvSchiene';
import { UvSchuelergruppeSchueler } from '../../../core/data/uv/UvSchuelergruppeSchueler';
import { UvUnterrichtLerngruppenlehrer } from '../../../core/data/uv/UvUnterrichtLerngruppenlehrer';
import { UvPlanungsabschnittLehrer } from '../../../core/data/uv/UvPlanungsabschnittLehrer';
import { UvLerngruppe } from '../../../core/data/uv/UvLerngruppe';
import { UvUnterrichtRaum } from '../../../core/data/uv/UvUnterrichtRaum';
import { UvKlasse } from '../../../core/data/uv/UvKlasse';
import type { List } from '../../../java/util/List';
import { UvKlassenLehrer } from '../../../core/data/uv/UvKlassenLehrer';
import { Class } from '../../../java/lang/Class';
import { UvUnterricht } from '../../../core/data/uv/UvUnterricht';

export class UvPlanungsabschnittsdatenBundle extends JavaObject {

	/**
	 * Der Planungsabschnitt
	 */
	public planungsabschnitt: UvPlanungsabschnitt = new UvPlanungsabschnitt();

	/**
	 * Ein Array mit
	 */
	public planungsabschnittlehrer: List<UvPlanungsabschnittLehrer> = new ArrayList<UvPlanungsabschnittLehrer>();

	/**
	 * Ein Array mit
	 */
	public planungsabschnittschueler: List<UvPlanungsabschnittSchueler> = new ArrayList<UvPlanungsabschnittSchueler>();

	/**
	 * Ein Array mit
	 */
	public planungsabschnittzeitraster: List<UvPlanungsabschnittZeitraster> = new ArrayList<UvPlanungsabschnittZeitraster>();

	/**
	 * Ein Array mit
	 */
	public klassen: List<UvKlasse> = new ArrayList<UvKlasse>();

	/**
	 * Ein Array mit den Klassenlehrer-Zuordnungen des Planungsabschnitts
	 */
	public klassenlehrer: List<UvKlassenLehrer> = new ArrayList<UvKlassenLehrer>();

	/**
	 * Ein Array mit
	 */
	public kurse: List<UvKurs> = new ArrayList<UvKurs>();

	/**
	 * Ein Array mit
	 */
	public schuelergruppen: List<UvSchuelergruppe> = new ArrayList<UvSchuelergruppe>();

	/**
	 * Ein Array mit
	 */
	public schuelergruppenschueler: List<UvSchuelergruppeSchueler> = new ArrayList<UvSchuelergruppeSchueler>();

	/**
	 * Ein Array mit
	 */
	public schienen: List<UvSchiene> = new ArrayList<UvSchiene>();

	/**
	 * Ein Array mit
	 */
	public lerngruppenlehrer: List<UvLerngruppenLehrer> = new ArrayList<UvLerngruppenLehrer>();

	/**
	 * Ein Array mit
	 */
	public lerngruppenschienen: List<UvLerngruppenSchiene> = new ArrayList<UvLerngruppenSchiene>();

	/**
	 * Ein Array mit
	 */
	public lerngruppen: List<UvLerngruppe> = new ArrayList<UvLerngruppe>();

	/**
	 * Ein Array mit
	 */
	public unterrichte: List<UvUnterricht> = new ArrayList<UvUnterricht>();

	/**
	 * Ein Array mit
	 */
	public unterrichtraeume: List<UvUnterrichtRaum> = new ArrayList<UvUnterrichtRaum>();

	/**
	 * Ein Array mit
	 */
	public unterrichtlerngruppenlehrer: List<UvUnterrichtLerngruppenlehrer> = new ArrayList<UvUnterrichtLerngruppenlehrer>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle'].includes(name);
	}

	public static readonly class = new Class<UvPlanungsabschnittsdatenBundle>('de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle');

	public static transpilerFromJSON(json: string): UvPlanungsabschnittsdatenBundle {
		const obj = JSON.parse(json) as Partial<UvPlanungsabschnittsdatenBundle>;
		const result = new UvPlanungsabschnittsdatenBundle();
		if (obj.planungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute planungsabschnitt');
		result.planungsabschnitt = UvPlanungsabschnitt.transpilerFromJSON(JSON.stringify(obj.planungsabschnitt));
		if (obj.planungsabschnittlehrer !== undefined) {
			for (const elem of obj.planungsabschnittlehrer) {
				result.planungsabschnittlehrer.add(UvPlanungsabschnittLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.planungsabschnittschueler !== undefined) {
			for (const elem of obj.planungsabschnittschueler) {
				result.planungsabschnittschueler.add(UvPlanungsabschnittSchueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.planungsabschnittzeitraster !== undefined) {
			for (const elem of obj.planungsabschnittzeitraster) {
				result.planungsabschnittzeitraster.add(UvPlanungsabschnittZeitraster.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(UvKlasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassenlehrer !== undefined) {
			for (const elem of obj.klassenlehrer) {
				result.klassenlehrer.add(UvKlassenLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kurse !== undefined) {
			for (const elem of obj.kurse) {
				result.kurse.add(UvKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelergruppen !== undefined) {
			for (const elem of obj.schuelergruppen) {
				result.schuelergruppen.add(UvSchuelergruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelergruppenschueler !== undefined) {
			for (const elem of obj.schuelergruppenschueler) {
				result.schuelergruppenschueler.add(UvSchuelergruppeSchueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schienen !== undefined) {
			for (const elem of obj.schienen) {
				result.schienen.add(UvSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppenlehrer !== undefined) {
			for (const elem of obj.lerngruppenlehrer) {
				result.lerngruppenlehrer.add(UvLerngruppenLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppenschienen !== undefined) {
			for (const elem of obj.lerngruppenschienen) {
				result.lerngruppenschienen.add(UvLerngruppenSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppen !== undefined) {
			for (const elem of obj.lerngruppen) {
				result.lerngruppen.add(UvLerngruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.unterrichte !== undefined) {
			for (const elem of obj.unterrichte) {
				result.unterrichte.add(UvUnterricht.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.unterrichtraeume !== undefined) {
			for (const elem of obj.unterrichtraeume) {
				result.unterrichtraeume.add(UvUnterrichtRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.unterrichtlerngruppenlehrer !== undefined) {
			for (const elem of obj.unterrichtlerngruppenlehrer) {
				result.unterrichtlerngruppenlehrer.add(UvUnterrichtLerngruppenlehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvPlanungsabschnittsdatenBundle): string {
		let result = '{';
		result += '"planungsabschnitt" : ' + UvPlanungsabschnitt.transpilerToJSON(obj.planungsabschnitt) + ',';
		result += '"planungsabschnittlehrer" : [ ';
		for (let i = 0; i < obj.planungsabschnittlehrer.size(); i++) {
			const elem = obj.planungsabschnittlehrer.get(i);
			result += UvPlanungsabschnittLehrer.transpilerToJSON(elem);
			if (i < obj.planungsabschnittlehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"planungsabschnittschueler" : [ ';
		for (let i = 0; i < obj.planungsabschnittschueler.size(); i++) {
			const elem = obj.planungsabschnittschueler.get(i);
			result += UvPlanungsabschnittSchueler.transpilerToJSON(elem);
			if (i < obj.planungsabschnittschueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"planungsabschnittzeitraster" : [ ';
		for (let i = 0; i < obj.planungsabschnittzeitraster.size(); i++) {
			const elem = obj.planungsabschnittzeitraster.get(i);
			result += UvPlanungsabschnittZeitraster.transpilerToJSON(elem);
			if (i < obj.planungsabschnittzeitraster.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += UvKlasse.transpilerToJSON(elem);
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassenlehrer" : [ ';
		for (let i = 0; i < obj.klassenlehrer.size(); i++) {
			const elem = obj.klassenlehrer.get(i);
			result += UvKlassenLehrer.transpilerToJSON(elem);
			if (i < obj.klassenlehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kurse" : [ ';
		for (let i = 0; i < obj.kurse.size(); i++) {
			const elem = obj.kurse.get(i);
			result += UvKurs.transpilerToJSON(elem);
			if (i < obj.kurse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelergruppen" : [ ';
		for (let i = 0; i < obj.schuelergruppen.size(); i++) {
			const elem = obj.schuelergruppen.get(i);
			result += UvSchuelergruppe.transpilerToJSON(elem);
			if (i < obj.schuelergruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelergruppenschueler" : [ ';
		for (let i = 0; i < obj.schuelergruppenschueler.size(); i++) {
			const elem = obj.schuelergruppenschueler.get(i);
			result += UvSchuelergruppeSchueler.transpilerToJSON(elem);
			if (i < obj.schuelergruppenschueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schienen" : [ ';
		for (let i = 0; i < obj.schienen.size(); i++) {
			const elem = obj.schienen.get(i);
			result += UvSchiene.transpilerToJSON(elem);
			if (i < obj.schienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppenlehrer" : [ ';
		for (let i = 0; i < obj.lerngruppenlehrer.size(); i++) {
			const elem = obj.lerngruppenlehrer.get(i);
			result += UvLerngruppenLehrer.transpilerToJSON(elem);
			if (i < obj.lerngruppenlehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppenschienen" : [ ';
		for (let i = 0; i < obj.lerngruppenschienen.size(); i++) {
			const elem = obj.lerngruppenschienen.get(i);
			result += UvLerngruppenSchiene.transpilerToJSON(elem);
			if (i < obj.lerngruppenschienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppen" : [ ';
		for (let i = 0; i < obj.lerngruppen.size(); i++) {
			const elem = obj.lerngruppen.get(i);
			result += UvLerngruppe.transpilerToJSON(elem);
			if (i < obj.lerngruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"unterrichte" : [ ';
		for (let i = 0; i < obj.unterrichte.size(); i++) {
			const elem = obj.unterrichte.get(i);
			result += UvUnterricht.transpilerToJSON(elem);
			if (i < obj.unterrichte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"unterrichtraeume" : [ ';
		for (let i = 0; i < obj.unterrichtraeume.size(); i++) {
			const elem = obj.unterrichtraeume.get(i);
			result += UvUnterrichtRaum.transpilerToJSON(elem);
			if (i < obj.unterrichtraeume.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"unterrichtlerngruppenlehrer" : [ ';
		for (let i = 0; i < obj.unterrichtlerngruppenlehrer.size(); i++) {
			const elem = obj.unterrichtlerngruppenlehrer.get(i);
			result += UvUnterrichtLerngruppenlehrer.transpilerToJSON(elem);
			if (i < obj.unterrichtlerngruppenlehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvPlanungsabschnittsdatenBundle>): string {
		let result = '{';
		if (obj.planungsabschnitt !== undefined) {
			result += '"planungsabschnitt" : ' + UvPlanungsabschnitt.transpilerToJSON(obj.planungsabschnitt) + ',';
		}
		if (obj.planungsabschnittlehrer !== undefined) {
			result += '"planungsabschnittlehrer" : [ ';
			for (let i = 0; i < obj.planungsabschnittlehrer.size(); i++) {
				const elem = obj.planungsabschnittlehrer.get(i);
				result += UvPlanungsabschnittLehrer.transpilerToJSON(elem);
				if (i < obj.planungsabschnittlehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.planungsabschnittschueler !== undefined) {
			result += '"planungsabschnittschueler" : [ ';
			for (let i = 0; i < obj.planungsabschnittschueler.size(); i++) {
				const elem = obj.planungsabschnittschueler.get(i);
				result += UvPlanungsabschnittSchueler.transpilerToJSON(elem);
				if (i < obj.planungsabschnittschueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.planungsabschnittzeitraster !== undefined) {
			result += '"planungsabschnittzeitraster" : [ ';
			for (let i = 0; i < obj.planungsabschnittzeitraster.size(); i++) {
				const elem = obj.planungsabschnittzeitraster.get(i);
				result += UvPlanungsabschnittZeitraster.transpilerToJSON(elem);
				if (i < obj.planungsabschnittzeitraster.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += UvKlasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassenlehrer !== undefined) {
			result += '"klassenlehrer" : [ ';
			for (let i = 0; i < obj.klassenlehrer.size(); i++) {
				const elem = obj.klassenlehrer.get(i);
				result += UvKlassenLehrer.transpilerToJSON(elem);
				if (i < obj.klassenlehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kurse !== undefined) {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += UvKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelergruppen !== undefined) {
			result += '"schuelergruppen" : [ ';
			for (let i = 0; i < obj.schuelergruppen.size(); i++) {
				const elem = obj.schuelergruppen.get(i);
				result += UvSchuelergruppe.transpilerToJSON(elem);
				if (i < obj.schuelergruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelergruppenschueler !== undefined) {
			result += '"schuelergruppenschueler" : [ ';
			for (let i = 0; i < obj.schuelergruppenschueler.size(); i++) {
				const elem = obj.schuelergruppenschueler.get(i);
				result += UvSchuelergruppeSchueler.transpilerToJSON(elem);
				if (i < obj.schuelergruppenschueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schienen !== undefined) {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += UvSchiene.transpilerToJSON(elem);
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppenlehrer !== undefined) {
			result += '"lerngruppenlehrer" : [ ';
			for (let i = 0; i < obj.lerngruppenlehrer.size(); i++) {
				const elem = obj.lerngruppenlehrer.get(i);
				result += UvLerngruppenLehrer.transpilerToJSON(elem);
				if (i < obj.lerngruppenlehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppenschienen !== undefined) {
			result += '"lerngruppenschienen" : [ ';
			for (let i = 0; i < obj.lerngruppenschienen.size(); i++) {
				const elem = obj.lerngruppenschienen.get(i);
				result += UvLerngruppenSchiene.transpilerToJSON(elem);
				if (i < obj.lerngruppenschienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppen !== undefined) {
			result += '"lerngruppen" : [ ';
			for (let i = 0; i < obj.lerngruppen.size(); i++) {
				const elem = obj.lerngruppen.get(i);
				result += UvLerngruppe.transpilerToJSON(elem);
				if (i < obj.lerngruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.unterrichte !== undefined) {
			result += '"unterrichte" : [ ';
			for (let i = 0; i < obj.unterrichte.size(); i++) {
				const elem = obj.unterrichte.get(i);
				result += UvUnterricht.transpilerToJSON(elem);
				if (i < obj.unterrichte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.unterrichtraeume !== undefined) {
			result += '"unterrichtraeume" : [ ';
			for (let i = 0; i < obj.unterrichtraeume.size(); i++) {
				const elem = obj.unterrichtraeume.get(i);
				result += UvUnterrichtRaum.transpilerToJSON(elem);
				if (i < obj.unterrichtraeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.unterrichtlerngruppenlehrer !== undefined) {
			result += '"unterrichtlerngruppenlehrer" : [ ';
			for (let i = 0; i < obj.unterrichtlerngruppenlehrer.size(); i++) {
				const elem = obj.unterrichtlerngruppenlehrer.get(i);
				result += UvUnterrichtLerngruppenlehrer.transpilerToJSON(elem);
				if (i < obj.unterrichtlerngruppenlehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvPlanungsabschnittsdatenBundle(obj: unknown): UvPlanungsabschnittsdatenBundle {
	return obj as UvPlanungsabschnittsdatenBundle;
}
