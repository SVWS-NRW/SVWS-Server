import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Betrieb extends JavaObject {

	/**
	 * Die ID des Betriebs.
	 */
	public id: number = 0;

	/**
	 * Das Name des Betriebs.
	 */
	public name: string | null = null;

	/**
	 * Die Namenszusatz des Betriebs.
	 */
	public nameZusatz: string | null = null;

	/**
	 * Bemerkungen
	 */
	public bemerkungen: string | null = null;

	/**
	 * Die Branche des Betriebs.
	 */
	public branche: string | null = null;

	/**
	 * Die ID der Betriebsart des Betriebs.
	 */
	public idBetriebsart: number | null = null;

	/**
	 * Gibt an, ob es sich bei dem Betrieb um einen Ausbildungsbetrieb handelt.
	 */
	public isAusbildungsbetrieb: boolean | null = null;

	/**
	 * Gibt an, ob es sich bei dem Betrieb um einen Maßnahmenträger handelt.
	 */
	public isMassnahmentraeger: boolean | null = null;

	/**
	 * Gibt an, ob bei dem Betrieb eine Belehrung nach Infektionsschutzgesetz erforderlich ist.
	 */
	public belehrungNachISGErforderlich: boolean | null = null;

	/**
	 * Gibt an, ob bei dem Betrieb eine erweitertes Führungszeugnis erforderlich ist.
	 */
	public erweitertesFuehrungszeugnisErforderlich: boolean | null = null;

	/**
	 * Gibt an, ob der Betrieb Praktikumsplätze anbietet.
	 */
	public bietetPraktikumsplaetzeAn: boolean | null = null;

	/**
	 * Die Straße des Betriebs.
	 */
	public strasse: string | null = null;

	/**
	 * Die Hausnummer des Betriebs.
	 */
	public hausnummer: string | null = null;

	/**
	 * Der Hausnummerzusatz des Betriebs.
	 */
	public hausnummerZusatz: string | null = null;

	/**
	 * Die ID des Betriebsortes.
	 */
	public idOrt: number | null = null;

	/**
	 * Erste Telefonnummer des Betriebs.
	 */
	public telefon1: string | null = null;

	/**
	 * Zweite Telefonnummer des Betriebs.
	 */
	public telefon2: string | null = null;

	/**
	 * Faxnummer des Betriebs.
	 */
	public fax: string | null = null;

	/**
	 * E-Mail des Betriebs.
	 */
	public eMail: string | null = null;

	/**
	 * Gibt an, ob der Betrieb in der Anwendung sichtbar sein soll oder nicht.
	 */
	public isSichtbar: boolean = false;

	/**
	 * Die Sortierreihenfolge des Betriebs.
	 */
	public sortierung: number | null = null;

	/**
	 * Gibt an, ob der Betrieb in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Betrieb';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Betrieb'].includes(name);
	}

	public static readonly class = new Class<Betrieb>('de.svws_nrw.core.data.schule.Betrieb');

	public static transpilerFromJSON(json: string): Betrieb {
		const obj = JSON.parse(json) as Partial<Betrieb>;
		const result = new Betrieb();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.name = (obj.name === undefined) ? null : obj.name === null ? null : obj.name;
		result.nameZusatz = (obj.nameZusatz === undefined) ? null : obj.nameZusatz === null ? null : obj.nameZusatz;
		result.bemerkungen = (obj.bemerkungen === undefined) ? null : obj.bemerkungen === null ? null : obj.bemerkungen;
		result.branche = (obj.branche === undefined) ? null : obj.branche === null ? null : obj.branche;
		result.idBetriebsart = (obj.idBetriebsart === undefined) ? null : obj.idBetriebsart === null ? null : obj.idBetriebsart;
		result.isAusbildungsbetrieb = (obj.isAusbildungsbetrieb === undefined) ? null : obj.isAusbildungsbetrieb === null ? null : obj.isAusbildungsbetrieb;
		result.isMassnahmentraeger = (obj.isMassnahmentraeger === undefined) ? null : obj.isMassnahmentraeger === null ? null : obj.isMassnahmentraeger;
		result.belehrungNachISGErforderlich = (obj.belehrungNachISGErforderlich === undefined) ? null : obj.belehrungNachISGErforderlich === null ? null : obj.belehrungNachISGErforderlich;
		result.erweitertesFuehrungszeugnisErforderlich = (obj.erweitertesFuehrungszeugnisErforderlich === undefined) ? null : obj.erweitertesFuehrungszeugnisErforderlich === null ? null : obj.erweitertesFuehrungszeugnisErforderlich;
		result.bietetPraktikumsplaetzeAn = (obj.bietetPraktikumsplaetzeAn === undefined) ? null : obj.bietetPraktikumsplaetzeAn === null ? null : obj.bietetPraktikumsplaetzeAn;
		result.strasse = (obj.strasse === undefined) ? null : obj.strasse === null ? null : obj.strasse;
		result.hausnummer = (obj.hausnummer === undefined) ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = (obj.hausnummerZusatz === undefined) ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.idOrt = (obj.idOrt === undefined) ? null : obj.idOrt === null ? null : obj.idOrt;
		result.telefon1 = (obj.telefon1 === undefined) ? null : obj.telefon1 === null ? null : obj.telefon1;
		result.telefon2 = (obj.telefon2 === undefined) ? null : obj.telefon2 === null ? null : obj.telefon2;
		result.fax = (obj.fax === undefined) ? null : obj.fax === null ? null : obj.fax;
		result.eMail = (obj.eMail === undefined) ? null : obj.eMail === null ? null : obj.eMail;
		if (obj.isSichtbar === undefined)
			throw new Error('invalid json format, missing attribute isSichtbar');
		result.isSichtbar = obj.isSichtbar;
		result.sortierung = (obj.sortierung === undefined) ? null : obj.sortierung === null ? null : obj.sortierung;
		if (obj.referenziertInAnderenTabellen === undefined)
			throw new Error('invalid json format, missing attribute referenziertInAnderenTabellen');
		result.referenziertInAnderenTabellen = obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: Betrieb): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"name" : ' + ((obj.name === null) ? 'null' : JSON.stringify(obj.name)) + ',';
		result += '"nameZusatz" : ' + ((obj.nameZusatz === null) ? 'null' : JSON.stringify(obj.nameZusatz)) + ',';
		result += '"bemerkungen" : ' + ((obj.bemerkungen === null) ? 'null' : JSON.stringify(obj.bemerkungen)) + ',';
		result += '"branche" : ' + ((obj.branche === null) ? 'null' : JSON.stringify(obj.branche)) + ',';
		result += '"idBetriebsart" : ' + ((obj.idBetriebsart === null) ? 'null' : obj.idBetriebsart.toString()) + ',';
		result += '"isAusbildungsbetrieb" : ' + ((obj.isAusbildungsbetrieb === null) ? 'null' : obj.isAusbildungsbetrieb.toString()) + ',';
		result += '"isMassnahmentraeger" : ' + ((obj.isMassnahmentraeger === null) ? 'null' : obj.isMassnahmentraeger.toString()) + ',';
		result += '"belehrungNachISGErforderlich" : ' + ((obj.belehrungNachISGErforderlich === null) ? 'null' : obj.belehrungNachISGErforderlich.toString()) + ',';
		result += '"erweitertesFuehrungszeugnisErforderlich" : ' + ((obj.erweitertesFuehrungszeugnisErforderlich === null) ? 'null' : obj.erweitertesFuehrungszeugnisErforderlich.toString()) + ',';
		result += '"bietetPraktikumsplaetzeAn" : ' + ((obj.bietetPraktikumsplaetzeAn === null) ? 'null' : obj.bietetPraktikumsplaetzeAn.toString()) + ',';
		result += '"strasse" : ' + ((obj.strasse === null) ? 'null' : JSON.stringify(obj.strasse)) + ',';
		result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"idOrt" : ' + ((obj.idOrt === null) ? 'null' : obj.idOrt.toString()) + ',';
		result += '"telefon1" : ' + ((obj.telefon1 === null) ? 'null' : JSON.stringify(obj.telefon1)) + ',';
		result += '"telefon2" : ' + ((obj.telefon2 === null) ? 'null' : JSON.stringify(obj.telefon2)) + ',';
		result += '"fax" : ' + ((obj.fax === null) ? 'null' : JSON.stringify(obj.fax)) + ',';
		result += '"eMail" : ' + ((obj.eMail === null) ? 'null' : JSON.stringify(obj.eMail)) + ',';
		result += '"isSichtbar" : ' + obj.isSichtbar.toString() + ',';
		result += '"sortierung" : ' + ((obj.sortierung === null) ? 'null' : obj.sortierung.toString()) + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Betrieb>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + ((obj.name === null) ? 'null' : JSON.stringify(obj.name)) + ',';
		}
		if (obj.nameZusatz !== undefined) {
			result += '"nameZusatz" : ' + ((obj.nameZusatz === null) ? 'null' : JSON.stringify(obj.nameZusatz)) + ',';
		}
		if (obj.bemerkungen !== undefined) {
			result += '"bemerkungen" : ' + ((obj.bemerkungen === null) ? 'null' : JSON.stringify(obj.bemerkungen)) + ',';
		}
		if (obj.branche !== undefined) {
			result += '"branche" : ' + ((obj.branche === null) ? 'null' : JSON.stringify(obj.branche)) + ',';
		}
		if (obj.idBetriebsart !== undefined) {
			result += '"idBetriebsart" : ' + ((obj.idBetriebsart === null) ? 'null' : obj.idBetriebsart.toString()) + ',';
		}
		if (obj.isAusbildungsbetrieb !== undefined) {
			result += '"isAusbildungsbetrieb" : ' + ((obj.isAusbildungsbetrieb === null) ? 'null' : obj.isAusbildungsbetrieb.toString()) + ',';
		}
		if (obj.isMassnahmentraeger !== undefined) {
			result += '"isMassnahmentraeger" : ' + ((obj.isMassnahmentraeger === null) ? 'null' : obj.isMassnahmentraeger.toString()) + ',';
		}
		if (obj.belehrungNachISGErforderlich !== undefined) {
			result += '"belehrungNachISGErforderlich" : ' + ((obj.belehrungNachISGErforderlich === null) ? 'null' : obj.belehrungNachISGErforderlich.toString()) + ',';
		}
		if (obj.erweitertesFuehrungszeugnisErforderlich !== undefined) {
			result += '"erweitertesFuehrungszeugnisErforderlich" : ' + ((obj.erweitertesFuehrungszeugnisErforderlich === null) ? 'null' : obj.erweitertesFuehrungszeugnisErforderlich.toString()) + ',';
		}
		if (obj.bietetPraktikumsplaetzeAn !== undefined) {
			result += '"bietetPraktikumsplaetzeAn" : ' + ((obj.bietetPraktikumsplaetzeAn === null) ? 'null' : obj.bietetPraktikumsplaetzeAn.toString()) + ',';
		}
		if (obj.strasse !== undefined) {
			result += '"strasse" : ' + ((obj.strasse === null) ? 'null' : JSON.stringify(obj.strasse)) + ',';
		}
		if (obj.hausnummer !== undefined) {
			result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (obj.hausnummerZusatz !== undefined) {
			result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		}
		if (obj.idOrt !== undefined) {
			result += '"idOrt" : ' + ((obj.idOrt === null) ? 'null' : obj.idOrt.toString()) + ',';
		}
		if (obj.telefon1 !== undefined) {
			result += '"telefon1" : ' + ((obj.telefon1 === null) ? 'null' : JSON.stringify(obj.telefon1)) + ',';
		}
		if (obj.telefon2 !== undefined) {
			result += '"telefon2" : ' + ((obj.telefon2 === null) ? 'null' : JSON.stringify(obj.telefon2)) + ',';
		}
		if (obj.fax !== undefined) {
			result += '"fax" : ' + ((obj.fax === null) ? 'null' : JSON.stringify(obj.fax)) + ',';
		}
		if (obj.eMail !== undefined) {
			result += '"eMail" : ' + ((obj.eMail === null) ? 'null' : JSON.stringify(obj.eMail)) + ',';
		}
		if (obj.isSichtbar !== undefined) {
			result += '"isSichtbar" : ' + obj.isSichtbar.toString() + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + ((obj.sortierung === null) ? 'null' : obj.sortierung.toString()) + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Betrieb(obj: unknown): Betrieb {
	return obj as Betrieb;
}
