import { JavaObject } from '../../../../java/lang/JavaObject';
import { UVv1KlasseExport } from '../../../../core/data/uv/export/UVv1KlasseExport';
import { UVv1LerngruppeExport } from '../../../../core/data/uv/export/UVv1LerngruppeExport';
import { UVv1UnterrichtExport } from '../../../../core/data/uv/export/UVv1UnterrichtExport';
import { UVv1KursExport } from '../../../../core/data/uv/export/UVv1KursExport';
import { ArrayList } from '../../../../java/util/ArrayList';
import { UVv1SchuelergruppeExport } from '../../../../core/data/uv/export/UVv1SchuelergruppeExport';
import { UVv1PlanungsabschnittZeitrasterExport } from '../../../../core/data/uv/export/UVv1PlanungsabschnittZeitrasterExport';
import type { List } from '../../../../java/util/List';
import { UVv1PlanungsabschnittSchuelerExport } from '../../../../core/data/uv/export/UVv1PlanungsabschnittSchuelerExport';
import { Class } from '../../../../java/lang/Class';
import { UVv1SchieneExport } from '../../../../core/data/uv/export/UVv1SchieneExport';

export class UVv1PlanungsabschnittExport extends JavaObject {

	/**
	 * Die UV-ID des Planungsabschnitts.
	 */
	public uvId: number = -1;

	/**
	 * Das Schuljahr, zu dem der Planungsabschnitt gehört.
	 */
	public schuljahr: number = -1;

	/**
	 * Gibt an, ob der Planungsabschnitt aktiv ist. An einem Stichtag darf nur ein Planungsabschnitt aktiv sein.
	 */
	public aktiv: boolean = false;

	/**
	 * Das Datum des Gültigkeitsbeginns des Planungsabschnitts.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum des Gültigkeitsendes des Planungsabschnitts.
	 */
	public gueltigBis: string | null = "";

	/**
	 * Die optionale Beschreibung oder der Kommentar zum Planungsabschnitt.
	 */
	public beschreibung: string | null = null;

	/**
	 * Ein Array mit den UV-IDs der Lehrer des Planungsabschnitts.
	 */
	public lehrerUvIds: List<number> = new ArrayList<number>();

	/**
	 * Ein Array mit den Schülern des Planungsabschnitts.
	 */
	public schueler: List<UVv1PlanungsabschnittSchuelerExport> = new ArrayList<UVv1PlanungsabschnittSchuelerExport>();

	/**
	 * Ein Array mit den Zeitraster-Zuordnungen des Planungsabschnitts.
	 */
	public zeitraster: List<UVv1PlanungsabschnittZeitrasterExport> = new ArrayList<UVv1PlanungsabschnittZeitrasterExport>();

	/**
	 * Ein Array mit den Klassen des Planungsabschnitts.
	 */
	public klassen: List<UVv1KlasseExport> = new ArrayList<UVv1KlasseExport>();

	/**
	 * Ein Array mit den Kursen des Planungsabschnitts.
	 */
	public kurse: List<UVv1KursExport> = new ArrayList<UVv1KursExport>();

	/**
	 * Ein Array mit den Schülergruppen des Planungsabschnitts.
	 */
	public schuelergruppen: List<UVv1SchuelergruppeExport> = new ArrayList<UVv1SchuelergruppeExport>();

	/**
	 * Ein Array mit den Schienen des Planungsabschnitts.
	 */
	public schienen: List<UVv1SchieneExport> = new ArrayList<UVv1SchieneExport>();

	/**
	 * Ein Array mit den Lerngruppen des Planungsabschnitts.
	 */
	public lerngruppen: List<UVv1LerngruppeExport> = new ArrayList<UVv1LerngruppeExport>();

	/**
	 * Ein Array mit den Unterrichtseinheiten des Planungsabschnitts.
	 */
	public unterrichte: List<UVv1UnterrichtExport> = new ArrayList<UVv1UnterrichtExport>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittExport'].includes(name);
	}

	public static readonly class = new Class<UVv1PlanungsabschnittExport>('de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittExport');

	public static transpilerFromJSON(json: string): UVv1PlanungsabschnittExport {
		const obj = JSON.parse(json) as Partial<UVv1PlanungsabschnittExport>;
		const result = new UVv1PlanungsabschnittExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.aktiv === undefined)
			throw new Error('invalid json format, missing attribute aktiv');
		result.aktiv = obj.aktiv;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		if (obj.lehrerUvIds !== undefined) {
			for (const elem of obj.lehrerUvIds) {
				result.lehrerUvIds.add(elem);
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(UVv1PlanungsabschnittSchuelerExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.zeitraster !== undefined) {
			for (const elem of obj.zeitraster) {
				result.zeitraster.add(UVv1PlanungsabschnittZeitrasterExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(UVv1KlasseExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kurse !== undefined) {
			for (const elem of obj.kurse) {
				result.kurse.add(UVv1KursExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelergruppen !== undefined) {
			for (const elem of obj.schuelergruppen) {
				result.schuelergruppen.add(UVv1SchuelergruppeExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schienen !== undefined) {
			for (const elem of obj.schienen) {
				result.schienen.add(UVv1SchieneExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppen !== undefined) {
			for (const elem of obj.lerngruppen) {
				result.lerngruppen.add(UVv1LerngruppeExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.unterrichte !== undefined) {
			for (const elem of obj.unterrichte) {
				result.unterrichte.add(UVv1UnterrichtExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1PlanungsabschnittExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"aktiv" : ' + obj.aktiv.toString() + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"lehrerUvIds" : [ ';
		for (let i = 0; i < obj.lehrerUvIds.size(); i++) {
			const elem = obj.lehrerUvIds.get(i);
			result += elem.toString();
			if (i < obj.lehrerUvIds.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += UVv1PlanungsabschnittSchuelerExport.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"zeitraster" : [ ';
		for (let i = 0; i < obj.zeitraster.size(); i++) {
			const elem = obj.zeitraster.get(i);
			result += UVv1PlanungsabschnittZeitrasterExport.transpilerToJSON(elem);
			if (i < obj.zeitraster.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += UVv1KlasseExport.transpilerToJSON(elem);
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kurse" : [ ';
		for (let i = 0; i < obj.kurse.size(); i++) {
			const elem = obj.kurse.get(i);
			result += UVv1KursExport.transpilerToJSON(elem);
			if (i < obj.kurse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelergruppen" : [ ';
		for (let i = 0; i < obj.schuelergruppen.size(); i++) {
			const elem = obj.schuelergruppen.get(i);
			result += UVv1SchuelergruppeExport.transpilerToJSON(elem);
			if (i < obj.schuelergruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schienen" : [ ';
		for (let i = 0; i < obj.schienen.size(); i++) {
			const elem = obj.schienen.get(i);
			result += UVv1SchieneExport.transpilerToJSON(elem);
			if (i < obj.schienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppen" : [ ';
		for (let i = 0; i < obj.lerngruppen.size(); i++) {
			const elem = obj.lerngruppen.get(i);
			result += UVv1LerngruppeExport.transpilerToJSON(elem);
			if (i < obj.lerngruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"unterrichte" : [ ';
		for (let i = 0; i < obj.unterrichte.size(); i++) {
			const elem = obj.unterrichte.get(i);
			result += UVv1UnterrichtExport.transpilerToJSON(elem);
			if (i < obj.unterrichte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1PlanungsabschnittExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.aktiv !== undefined) {
			result += '"aktiv" : ' + obj.aktiv.toString() + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.lehrerUvIds !== undefined) {
			result += '"lehrerUvIds" : [ ';
			for (let i = 0; i < obj.lehrerUvIds.size(); i++) {
				const elem = obj.lehrerUvIds.get(i);
				result += elem.toString();
				if (i < obj.lehrerUvIds.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += UVv1PlanungsabschnittSchuelerExport.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.zeitraster !== undefined) {
			result += '"zeitraster" : [ ';
			for (let i = 0; i < obj.zeitraster.size(); i++) {
				const elem = obj.zeitraster.get(i);
				result += UVv1PlanungsabschnittZeitrasterExport.transpilerToJSON(elem);
				if (i < obj.zeitraster.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += UVv1KlasseExport.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kurse !== undefined) {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += UVv1KursExport.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelergruppen !== undefined) {
			result += '"schuelergruppen" : [ ';
			for (let i = 0; i < obj.schuelergruppen.size(); i++) {
				const elem = obj.schuelergruppen.get(i);
				result += UVv1SchuelergruppeExport.transpilerToJSON(elem);
				if (i < obj.schuelergruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schienen !== undefined) {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += UVv1SchieneExport.transpilerToJSON(elem);
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppen !== undefined) {
			result += '"lerngruppen" : [ ';
			for (let i = 0; i < obj.lerngruppen.size(); i++) {
				const elem = obj.lerngruppen.get(i);
				result += UVv1LerngruppeExport.transpilerToJSON(elem);
				if (i < obj.lerngruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.unterrichte !== undefined) {
			result += '"unterrichte" : [ ';
			for (let i = 0; i < obj.unterrichte.size(); i++) {
				const elem = obj.unterrichte.get(i);
				result += UVv1UnterrichtExport.transpilerToJSON(elem);
				if (i < obj.unterrichte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1PlanungsabschnittExport(obj: unknown): UVv1PlanungsabschnittExport {
	return obj as UVv1PlanungsabschnittExport;
}
