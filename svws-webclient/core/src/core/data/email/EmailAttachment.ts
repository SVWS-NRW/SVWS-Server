import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class EmailAttachment extends JavaObject {

	/**
	 * Der Dateiname des Anhangs mit Endung.
	 */
	public filename: string = "";

	/**
	 * Der Inhalt der Datei, Base64-kodiert.
	 */
	public data: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.email.EmailAttachment';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.email.EmailAttachment'].includes(name);
	}

	public static readonly class = new Class<EmailAttachment>('de.svws_nrw.core.data.email.EmailAttachment');

	public static transpilerFromJSON(json: string): EmailAttachment {
		const obj = JSON.parse(json) as Partial<EmailAttachment>;
		const result = new EmailAttachment();
		if (obj.filename === undefined)
			throw new Error('invalid json format, missing attribute filename');
		result.filename = obj.filename;
		if (obj.data === undefined)
			throw new Error('invalid json format, missing attribute data');
		result.data = obj.data;
		return result;
	}

	public static transpilerToJSON(obj: EmailAttachment): string {
		let result = '{';
		result += '"filename" : ' + JSON.stringify(obj.filename) + ',';
		result += '"data" : ' + JSON.stringify(obj.data) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<EmailAttachment>): string {
		let result = '{';
		if (obj.filename !== undefined) {
			result += '"filename" : ' + JSON.stringify(obj.filename) + ',';
		}
		if (obj.data !== undefined) {
			result += '"data" : ' + JSON.stringify(obj.data) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_email_EmailAttachment(obj: unknown): EmailAttachment {
	return obj as EmailAttachment;
}
