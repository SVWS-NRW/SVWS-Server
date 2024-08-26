import { JavaObject } from '../../../java/lang/JavaObject';

export class WiedervorlageEintrag extends JavaObject {

	/**
	 * Die ID des Eintrags.
	 */
	public id : number = -1;

	/**
	 * Die ID des Benutzers, der den Eintrag erstellt hat.
	 */
	public idBenutzer : number = -1;

	/**
	 * Die ID der Benutzergruppe, welcher der Eintrag zugeordnet ist, oder null, falls keine Zuordnung vorliegt.
	 */
	public idBenutzergruppe : number | null = null;

	/**
	 * Die ID des Person-Typs, auf den sich die Wiedervorlage bezieht (1 = Lehrer, 2 = Schueler, 3 = Erzieher oder NULL für keinen)
	 */
	public typPerson : number | null = null;

	/**
	 * Die ID der Person in Abhängigkeit des Person-Typs, auf den sich die Wiedervorlage bezieht (ggf. Lehrer-ID, Schüler-ID oder Erzieher-ID)
	 */
	public idPerson : number | null = null;

	/**
	 * Die Bemerkung der Wiedervorlage, die bem Benutzer angezeigt wird.
	 */
	public bemerkung : string = "";

	/**
	 * Gibt den Zeitstempel an, wann der Eintrag angelegt wurde.
	 */
	public tsAngelegt : string | null = null;

	/**
	 * Gibt den Zeitstempel an, wann der Eintrag angelegt wurde.
	 */
	public tsWiedervorlage : string | null = null;

	/**
	 * Gibt den Zeitstempel an, wann der Eintrag als erledigt markiert wurde.
	 */
	public tsErledigt : string | null = null;

	/**
	 * Die ID des Benutzers, der den Eintrag als erledigt markiert hat, oder null.
	 */
	public idBenutzerErledigt : number | null = null;

	/**
	 * Gibt an, ob der Eintrag automatisch als erledigt markiert werden soll, wenn er einem Benutzer nach dem Wiedervorlage-Zeitpunkt angezeigt wurde.
	 */
	public automatischErledigt : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.WiedervorlageEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.WiedervorlageEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): WiedervorlageEintrag {
		const obj = JSON.parse(json) as Partial<WiedervorlageEintrag>;
		const result = new WiedervorlageEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idBenutzer === undefined)
			throw new Error('invalid json format, missing attribute idBenutzer');
		result.idBenutzer = obj.idBenutzer;
		result.idBenutzergruppe = (obj.idBenutzergruppe === undefined) ? null : obj.idBenutzergruppe === null ? null : obj.idBenutzergruppe;
		result.typPerson = (obj.typPerson === undefined) ? null : obj.typPerson === null ? null : obj.typPerson;
		result.idPerson = (obj.idPerson === undefined) ? null : obj.idPerson === null ? null : obj.idPerson;
		if (obj.bemerkung === undefined)
			throw new Error('invalid json format, missing attribute bemerkung');
		result.bemerkung = obj.bemerkung;
		result.tsAngelegt = (obj.tsAngelegt === undefined) ? null : obj.tsAngelegt === null ? null : obj.tsAngelegt;
		result.tsWiedervorlage = (obj.tsWiedervorlage === undefined) ? null : obj.tsWiedervorlage === null ? null : obj.tsWiedervorlage;
		result.tsErledigt = (obj.tsErledigt === undefined) ? null : obj.tsErledigt === null ? null : obj.tsErledigt;
		result.idBenutzerErledigt = (obj.idBenutzerErledigt === undefined) ? null : obj.idBenutzerErledigt === null ? null : obj.idBenutzerErledigt;
		if (obj.automatischErledigt === undefined)
			throw new Error('invalid json format, missing attribute automatischErledigt');
		result.automatischErledigt = obj.automatischErledigt;
		return result;
	}

	public static transpilerToJSON(obj : WiedervorlageEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idBenutzer" : ' + obj.idBenutzer.toString() + ',';
		result += '"idBenutzergruppe" : ' + ((!obj.idBenutzergruppe) ? 'null' : obj.idBenutzergruppe.toString()) + ',';
		result += '"typPerson" : ' + ((!obj.typPerson) ? 'null' : obj.typPerson.toString()) + ',';
		result += '"idPerson" : ' + ((!obj.idPerson) ? 'null' : obj.idPerson.toString()) + ',';
		result += '"bemerkung" : ' + JSON.stringify(obj.bemerkung) + ',';
		result += '"tsAngelegt" : ' + ((!obj.tsAngelegt) ? 'null' : JSON.stringify(obj.tsAngelegt)) + ',';
		result += '"tsWiedervorlage" : ' + ((!obj.tsWiedervorlage) ? 'null' : JSON.stringify(obj.tsWiedervorlage)) + ',';
		result += '"tsErledigt" : ' + ((!obj.tsErledigt) ? 'null' : JSON.stringify(obj.tsErledigt)) + ',';
		result += '"idBenutzerErledigt" : ' + ((!obj.idBenutzerErledigt) ? 'null' : obj.idBenutzerErledigt.toString()) + ',';
		result += '"automatischErledigt" : ' + obj.automatischErledigt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<WiedervorlageEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idBenutzer !== undefined) {
			result += '"idBenutzer" : ' + obj.idBenutzer.toString() + ',';
		}
		if (obj.idBenutzergruppe !== undefined) {
			result += '"idBenutzergruppe" : ' + ((!obj.idBenutzergruppe) ? 'null' : obj.idBenutzergruppe.toString()) + ',';
		}
		if (obj.typPerson !== undefined) {
			result += '"typPerson" : ' + ((!obj.typPerson) ? 'null' : obj.typPerson.toString()) + ',';
		}
		if (obj.idPerson !== undefined) {
			result += '"idPerson" : ' + ((!obj.idPerson) ? 'null' : obj.idPerson.toString()) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + JSON.stringify(obj.bemerkung) + ',';
		}
		if (obj.tsAngelegt !== undefined) {
			result += '"tsAngelegt" : ' + ((!obj.tsAngelegt) ? 'null' : JSON.stringify(obj.tsAngelegt)) + ',';
		}
		if (obj.tsWiedervorlage !== undefined) {
			result += '"tsWiedervorlage" : ' + ((!obj.tsWiedervorlage) ? 'null' : JSON.stringify(obj.tsWiedervorlage)) + ',';
		}
		if (obj.tsErledigt !== undefined) {
			result += '"tsErledigt" : ' + ((!obj.tsErledigt) ? 'null' : JSON.stringify(obj.tsErledigt)) + ',';
		}
		if (obj.idBenutzerErledigt !== undefined) {
			result += '"idBenutzerErledigt" : ' + ((!obj.idBenutzerErledigt) ? 'null' : obj.idBenutzerErledigt.toString()) + ',';
		}
		if (obj.automatischErledigt !== undefined) {
			result += '"automatischErledigt" : ' + obj.automatischErledigt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_WiedervorlageEintrag(obj : unknown) : WiedervorlageEintrag {
	return obj as WiedervorlageEintrag;
}
