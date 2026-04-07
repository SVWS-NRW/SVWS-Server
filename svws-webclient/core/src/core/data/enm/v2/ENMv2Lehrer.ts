import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class ENMv2Lehrer extends JavaObject {

	/**
	 * Die ID des Lehrers aus der SVWS-DB (z.B. 42)
	 */
	public id: number = 0;

	/**
	 * Das Kürzel des Lehrers für die Anzeige im Notenmodel (z.B. Mus)
	 */
	public kuerzel: string | null = null;

	/**
	 * Der Nachname des Lehrers (z.B. Mustermann)
	 */
	public nachname: string | null = null;

	/**
	 * Der Vorname des Lehrers (z.B. Max)
	 */
	public vorname: string | null = null;

	/**
	 * Das Geschlecht des Lehrers (m,w,d,x)
	 */
	public geschlecht: string | null = null;

	/**
	 * Die Dienst-EMail-Adresse des Lehrers
	 */
	public eMailDienstlich: string | null = null;

	/**
	 * Der BCrypt-Kennwort-Hash des Lehrerkennwortes
	 */
	public passwordHash: string = "";

	/**
	 * Der Zeitstempel der letzten Änderung an dem Password-Hash
	 */
	public tsPasswordHash: string | null = null;

	/**
	 * Gibt an, es sich bei dem Password-Hash um den Hash des Initialkennwortes handelt oder nicht.
	 */
	public istInitialPassword: boolean = false;

	/**
	 * Gibt die Art der verwendeten Zwei-Faktor-Authentifizierung an (0 = Keine, 1 = TOTP, 2 = Mail).
	 */
	public art2FA: number = 0;

	/**
	 * Der Zeitstempel für die konfigurierte Art der Zwei-Faktor-Authentifzierung
	 */
	public tsArt2FA: string | null = null;

	/**
	 * Das Shared-Secret für TOTP
	 */
	public totpSecret: string = "";

	/**
	 * Gibt an, ob eine Erstanmeldung bei einem Server bereits erfolgt ist oder nicht.
	 */
	public istErstanmeldung: boolean = false;

	/**
	 * Der Zeitstempel für die Information zur Erstanmeldung
	 */
	public tsIstErstanmeldung: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2Lehrer';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2Lehrer'].includes(name);
	}

	public static readonly class = new Class<ENMv2Lehrer>('de.svws_nrw.core.data.enm.v2.ENMv2Lehrer');

	public static transpilerFromJSON(json: string): ENMv2Lehrer {
		const obj = JSON.parse(json) as Partial<ENMv2Lehrer>;
		const result = new ENMv2Lehrer();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.nachname = (obj.nachname === undefined) ? null : obj.nachname === null ? null : obj.nachname;
		result.vorname = (obj.vorname === undefined) ? null : obj.vorname === null ? null : obj.vorname;
		result.geschlecht = (obj.geschlecht === undefined) ? null : obj.geschlecht === null ? null : obj.geschlecht;
		result.eMailDienstlich = (obj.eMailDienstlich === undefined) ? null : obj.eMailDienstlich === null ? null : obj.eMailDienstlich;
		if (obj.passwordHash === undefined)
			throw new Error('invalid json format, missing attribute passwordHash');
		result.passwordHash = obj.passwordHash;
		result.tsPasswordHash = (obj.tsPasswordHash === undefined) ? null : obj.tsPasswordHash === null ? null : obj.tsPasswordHash;
		if (obj.istInitialPassword === undefined)
			throw new Error('invalid json format, missing attribute istInitialPassword');
		result.istInitialPassword = obj.istInitialPassword;
		if (obj.art2FA === undefined)
			throw new Error('invalid json format, missing attribute art2FA');
		result.art2FA = obj.art2FA;
		result.tsArt2FA = (obj.tsArt2FA === undefined) ? null : obj.tsArt2FA === null ? null : obj.tsArt2FA;
		if (obj.totpSecret === undefined)
			throw new Error('invalid json format, missing attribute totpSecret');
		result.totpSecret = obj.totpSecret;
		if (obj.istErstanmeldung === undefined)
			throw new Error('invalid json format, missing attribute istErstanmeldung');
		result.istErstanmeldung = obj.istErstanmeldung;
		result.tsIstErstanmeldung = (obj.tsIstErstanmeldung === undefined) ? null : obj.tsIstErstanmeldung === null ? null : obj.tsIstErstanmeldung;
		return result;
	}

	public static transpilerToJSON(obj: ENMv2Lehrer): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result += '"geschlecht" : ' + ((obj.geschlecht === null) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		result += '"eMailDienstlich" : ' + ((obj.eMailDienstlich === null) ? 'null' : JSON.stringify(obj.eMailDienstlich)) + ',';
		result += '"passwordHash" : ' + JSON.stringify(obj.passwordHash) + ',';
		result += '"tsPasswordHash" : ' + ((obj.tsPasswordHash === null) ? 'null' : JSON.stringify(obj.tsPasswordHash)) + ',';
		result += '"istInitialPassword" : ' + obj.istInitialPassword.toString() + ',';
		result += '"art2FA" : ' + obj.art2FA.toString() + ',';
		result += '"tsArt2FA" : ' + ((obj.tsArt2FA === null) ? 'null' : JSON.stringify(obj.tsArt2FA)) + ',';
		result += '"totpSecret" : ' + JSON.stringify(obj.totpSecret) + ',';
		result += '"istErstanmeldung" : ' + obj.istErstanmeldung.toString() + ',';
		result += '"tsIstErstanmeldung" : ' + ((obj.tsIstErstanmeldung === null) ? 'null' : JSON.stringify(obj.tsIstErstanmeldung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2Lehrer>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + ((obj.geschlecht === null) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		}
		if (obj.eMailDienstlich !== undefined) {
			result += '"eMailDienstlich" : ' + ((obj.eMailDienstlich === null) ? 'null' : JSON.stringify(obj.eMailDienstlich)) + ',';
		}
		if (obj.passwordHash !== undefined) {
			result += '"passwordHash" : ' + JSON.stringify(obj.passwordHash) + ',';
		}
		if (obj.tsPasswordHash !== undefined) {
			result += '"tsPasswordHash" : ' + ((obj.tsPasswordHash === null) ? 'null' : JSON.stringify(obj.tsPasswordHash)) + ',';
		}
		if (obj.istInitialPassword !== undefined) {
			result += '"istInitialPassword" : ' + obj.istInitialPassword.toString() + ',';
		}
		if (obj.art2FA !== undefined) {
			result += '"art2FA" : ' + obj.art2FA.toString() + ',';
		}
		if (obj.tsArt2FA !== undefined) {
			result += '"tsArt2FA" : ' + ((obj.tsArt2FA === null) ? 'null' : JSON.stringify(obj.tsArt2FA)) + ',';
		}
		if (obj.totpSecret !== undefined) {
			result += '"totpSecret" : ' + JSON.stringify(obj.totpSecret) + ',';
		}
		if (obj.istErstanmeldung !== undefined) {
			result += '"istErstanmeldung" : ' + obj.istErstanmeldung.toString() + ',';
		}
		if (obj.tsIstErstanmeldung !== undefined) {
			result += '"tsIstErstanmeldung" : ' + ((obj.tsIstErstanmeldung === null) ? 'null' : JSON.stringify(obj.tsIstErstanmeldung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2Lehrer(obj: unknown): ENMv2Lehrer {
	return obj as ENMv2Lehrer;
}
