import { JavaObject } from '../../../java/lang/JavaObject';
import { EmailAttachment } from '../../../core/data/email/EmailAttachment';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class EmailSendRequest extends JavaObject {

	/**
	 * Der Betreff der E-Mail.
	 */
	public subject: string = "";

	/**
	 * Der Text der E-Mail als reiner Text.
	 */
	public text: string = "";

	/**
	 * Die E-Mail-Adressen der Empfänger.
	 */
	public recipients: List<string> = new ArrayList<string>();

	/**
	 * Die Dateianhänge, die jeder Empfänger erhält.
	 */
	public attachments: List<EmailAttachment> = new ArrayList<EmailAttachment>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.email.EmailSendRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.email.EmailSendRequest'].includes(name);
	}

	public static readonly class = new Class<EmailSendRequest>('de.svws_nrw.core.data.email.EmailSendRequest');

	public static transpilerFromJSON(json: string): EmailSendRequest {
		const obj = JSON.parse(json) as Partial<EmailSendRequest>;
		const result = new EmailSendRequest();
		if (obj.subject === undefined)
			throw new Error('invalid json format, missing attribute subject');
		result.subject = obj.subject;
		if (obj.text === undefined)
			throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		if (obj.recipients !== undefined) {
			for (const elem of obj.recipients) {
				result.recipients.add(elem);
			}
		}
		if (obj.attachments !== undefined) {
			for (const elem of obj.attachments) {
				result.attachments.add(EmailAttachment.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: EmailSendRequest): string {
		let result = '{';
		result += '"subject" : ' + JSON.stringify(obj.subject) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"recipients" : [ ';
		for (let i = 0; i < obj.recipients.size(); i++) {
			const elem = obj.recipients.get(i);
			result += JSON.stringify(elem);
			if (i < obj.recipients.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"attachments" : [ ';
		for (let i = 0; i < obj.attachments.size(); i++) {
			const elem = obj.attachments.get(i);
			result += EmailAttachment.transpilerToJSON(elem);
			if (i < obj.attachments.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<EmailSendRequest>): string {
		let result = '{';
		if (obj.subject !== undefined) {
			result += '"subject" : ' + JSON.stringify(obj.subject) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + JSON.stringify(obj.text) + ',';
		}
		if (obj.recipients !== undefined) {
			result += '"recipients" : [ ';
			for (let i = 0; i < obj.recipients.size(); i++) {
				const elem = obj.recipients.get(i);
				result += JSON.stringify(elem);
				if (i < obj.recipients.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.attachments !== undefined) {
			result += '"attachments" : [ ';
			for (let i = 0; i < obj.attachments.size(); i++) {
				const elem = obj.attachments.get(i);
				result += EmailAttachment.transpilerToJSON(elem);
				if (i < obj.attachments.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_email_EmailSendRequest(obj: unknown): EmailSendRequest {
	return obj as EmailSendRequest;
}
