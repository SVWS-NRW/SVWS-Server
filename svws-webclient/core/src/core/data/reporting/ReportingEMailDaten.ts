import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingEMailEmpfaengerTyp } from '../../../core/types/reporting/ReportingEMailEmpfaengerTyp';
import { Class } from '../../../java/lang/Class';

export class ReportingEMailDaten extends JavaObject {

	/**
	 * Steuert, an wen versendet wird. Die Werte sind gemäß {@link ReportingEMailEmpfaengerTyp} zu wählen.
	 */
	public empfaengerTyp: number = ReportingEMailEmpfaengerTyp.UNDEFINED.getId();

	/**
	 * Gibt an, ob bei fehlender oder fehlerhafter schulischer E-Mail-Adresse die private E-Mail-Adresse genutzt werden soll.
	 */
	public istPrivateEmailAlternative: boolean = false;

	/**
	 * Der Betreff der E-Mail.
	 */
	public betreff: string = "";

	/**
	 * Der Text der E-Mail.
	 */
	public text: string = "";


	/**
	 * Der Konstruktor der Klasse ReportingEMailDaten.
	 * Erzeugt eine Instanz der Klasse und initialisiert sie mit Standardwerten.
	 * Diese Klasse enthält Daten für den Versand von E-Mails im Reporting.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingEMailDaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingEMailDaten'].includes(name);
	}

	public static readonly class = new Class<ReportingEMailDaten>('de.svws_nrw.core.data.reporting.ReportingEMailDaten');

	public static transpilerFromJSON(json: string): ReportingEMailDaten {
		const obj = JSON.parse(json) as Partial<ReportingEMailDaten>;
		const result = new ReportingEMailDaten();
		if (obj.empfaengerTyp === undefined)
			throw new Error('invalid json format, missing attribute empfaengerTyp');
		result.empfaengerTyp = obj.empfaengerTyp;
		if (obj.istPrivateEmailAlternative === undefined)
			throw new Error('invalid json format, missing attribute istPrivateEmailAlternative');
		result.istPrivateEmailAlternative = obj.istPrivateEmailAlternative;
		if (obj.betreff === undefined)
			throw new Error('invalid json format, missing attribute betreff');
		result.betreff = obj.betreff;
		if (obj.text === undefined)
			throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		return result;
	}

	public static transpilerToJSON(obj: ReportingEMailDaten): string {
		let result = '{';
		result += '"empfaengerTyp" : ' + obj.empfaengerTyp.toString() + ',';
		result += '"istPrivateEmailAlternative" : ' + obj.istPrivateEmailAlternative.toString() + ',';
		result += '"betreff" : ' + JSON.stringify(obj.betreff) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingEMailDaten>): string {
		let result = '{';
		if (obj.empfaengerTyp !== undefined) {
			result += '"empfaengerTyp" : ' + obj.empfaengerTyp.toString() + ',';
		}
		if (obj.istPrivateEmailAlternative !== undefined) {
			result += '"istPrivateEmailAlternative" : ' + obj.istPrivateEmailAlternative.toString() + ',';
		}
		if (obj.betreff !== undefined) {
			result += '"betreff" : ' + JSON.stringify(obj.betreff) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + JSON.stringify(obj.text) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingEMailDaten(obj: unknown): ReportingEMailDaten {
	return obj as ReportingEMailDaten;
}
