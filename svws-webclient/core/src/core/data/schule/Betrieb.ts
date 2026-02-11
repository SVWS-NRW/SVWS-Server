import { JavaObject } from '../../../java/lang/JavaObject';
import { BetriebeAnsprechpartner } from '../../../core/data/schule/BetriebeAnsprechpartner';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
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
	public istAusbildungsbetrieb: boolean = false;

	/**
	 * Gibt an, ob es sich bei dem Betrieb um einen Maßnahmenträger handelt.
	 */
	public istMassnahmentraeger: boolean = false;

	/**
	 * Gibt an, ob bei dem Betrieb eine Belehrung nach Infektionsschutzgesetz erforderlich ist.
	 */
	public belehrungNachISGErforderlich: boolean = false;

	/**
	 * Gibt an, ob bei dem Betrieb eine erweitertes Führungszeugnis erforderlich ist.
	 */
	public erweitertesFuehrungszeugnisErforderlich: boolean = false;

	/**
	 * Gibt an, ob der Betrieb Praktikumsplätze anbietet.
	 */
	public bietetPraktikumsplaetzeAn: boolean = false;

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
	public istSichtbar: boolean = false;

	/**
	 * Die Sortierreihenfolge des Betriebs.
	 */
	public sortierung: number = 0;

	/**
	 * Gibt an, ob der Betrieb in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;

	/**
	 * Die Ansprechpartner des Betriebs.
	 */
	public ansprechpartner: List<BetriebeAnsprechpartner> = new ArrayList<BetriebeAnsprechpartner>();


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
		if (obj.istAusbildungsbetrieb === undefined)
			throw new Error('invalid json format, missing attribute istAusbildungsbetrieb');
		result.istAusbildungsbetrieb = obj.istAusbildungsbetrieb;
		if (obj.istMassnahmentraeger === undefined)
			throw new Error('invalid json format, missing attribute istMassnahmentraeger');
		result.istMassnahmentraeger = obj.istMassnahmentraeger;
		if (obj.belehrungNachISGErforderlich === undefined)
			throw new Error('invalid json format, missing attribute belehrungNachISGErforderlich');
		result.belehrungNachISGErforderlich = obj.belehrungNachISGErforderlich;
		if (obj.erweitertesFuehrungszeugnisErforderlich === undefined)
			throw new Error('invalid json format, missing attribute erweitertesFuehrungszeugnisErforderlich');
		result.erweitertesFuehrungszeugnisErforderlich = obj.erweitertesFuehrungszeugnisErforderlich;
		if (obj.bietetPraktikumsplaetzeAn === undefined)
			throw new Error('invalid json format, missing attribute bietetPraktikumsplaetzeAn');
		result.bietetPraktikumsplaetzeAn = obj.bietetPraktikumsplaetzeAn;
		result.strasse = (obj.strasse === undefined) ? null : obj.strasse === null ? null : obj.strasse;
		result.hausnummer = (obj.hausnummer === undefined) ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = (obj.hausnummerZusatz === undefined) ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.idOrt = (obj.idOrt === undefined) ? null : obj.idOrt === null ? null : obj.idOrt;
		result.telefon1 = (obj.telefon1 === undefined) ? null : obj.telefon1 === null ? null : obj.telefon1;
		result.telefon2 = (obj.telefon2 === undefined) ? null : obj.telefon2 === null ? null : obj.telefon2;
		result.fax = (obj.fax === undefined) ? null : obj.fax === null ? null : obj.fax;
		result.eMail = (obj.eMail === undefined) ? null : obj.eMail === null ? null : obj.eMail;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.referenziertInAnderenTabellen === undefined)
			throw new Error('invalid json format, missing attribute referenziertInAnderenTabellen');
		result.referenziertInAnderenTabellen = obj.referenziertInAnderenTabellen;
		if (obj.ansprechpartner !== undefined) {
			for (const elem of obj.ansprechpartner) {
				result.ansprechpartner.add(BetriebeAnsprechpartner.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
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
		result += '"istAusbildungsbetrieb" : ' + obj.istAusbildungsbetrieb.toString() + ',';
		result += '"istMassnahmentraeger" : ' + obj.istMassnahmentraeger.toString() + ',';
		result += '"belehrungNachISGErforderlich" : ' + obj.belehrungNachISGErforderlich.toString() + ',';
		result += '"erweitertesFuehrungszeugnisErforderlich" : ' + obj.erweitertesFuehrungszeugnisErforderlich.toString() + ',';
		result += '"bietetPraktikumsplaetzeAn" : ' + obj.bietetPraktikumsplaetzeAn.toString() + ',';
		result += '"strasse" : ' + ((obj.strasse === null) ? 'null' : JSON.stringify(obj.strasse)) + ',';
		result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"idOrt" : ' + ((obj.idOrt === null) ? 'null' : obj.idOrt.toString()) + ',';
		result += '"telefon1" : ' + ((obj.telefon1 === null) ? 'null' : JSON.stringify(obj.telefon1)) + ',';
		result += '"telefon2" : ' + ((obj.telefon2 === null) ? 'null' : JSON.stringify(obj.telefon2)) + ',';
		result += '"fax" : ' + ((obj.fax === null) ? 'null' : JSON.stringify(obj.fax)) + ',';
		result += '"eMail" : ' + ((obj.eMail === null) ? 'null' : JSON.stringify(obj.eMail)) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result += '"ansprechpartner" : [ ';
		for (let i = 0; i < obj.ansprechpartner.size(); i++) {
			const elem = obj.ansprechpartner.get(i);
			result += BetriebeAnsprechpartner.transpilerToJSON(elem);
			if (i < obj.ansprechpartner.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
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
		if (obj.istAusbildungsbetrieb !== undefined) {
			result += '"istAusbildungsbetrieb" : ' + obj.istAusbildungsbetrieb.toString() + ',';
		}
		if (obj.istMassnahmentraeger !== undefined) {
			result += '"istMassnahmentraeger" : ' + obj.istMassnahmentraeger.toString() + ',';
		}
		if (obj.belehrungNachISGErforderlich !== undefined) {
			result += '"belehrungNachISGErforderlich" : ' + obj.belehrungNachISGErforderlich.toString() + ',';
		}
		if (obj.erweitertesFuehrungszeugnisErforderlich !== undefined) {
			result += '"erweitertesFuehrungszeugnisErforderlich" : ' + obj.erweitertesFuehrungszeugnisErforderlich.toString() + ',';
		}
		if (obj.bietetPraktikumsplaetzeAn !== undefined) {
			result += '"bietetPraktikumsplaetzeAn" : ' + obj.bietetPraktikumsplaetzeAn.toString() + ',';
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
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		}
		if (obj.ansprechpartner !== undefined) {
			result += '"ansprechpartner" : [ ';
			for (let i = 0; i < obj.ansprechpartner.size(); i++) {
				const elem = obj.ansprechpartner.get(i);
				result += BetriebeAnsprechpartner.transpilerToJSON(elem);
				if (i < obj.ansprechpartner.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Betrieb(obj: unknown): Betrieb {
	return obj as Betrieb;
}
