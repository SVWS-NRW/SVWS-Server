import { JavaLong } from '../../../../java/lang/JavaLong';
import { JavaObject } from '../../../../java/lang/JavaObject';

export class GostKlausurvorgabe extends JavaObject {

	/**
	 * Die ID der Klausurvorgabe.
	 */
	public idVorgabe : number = -1;

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird, -1 für die Vorlage.
	 */
	public abiJahrgang : number = -1;

	/**
	 * Das Gost-Halbjahr, in dem die Klausurg geschrieben wird.
	 */
	public halbjahr : number = -1;

	/**
	 * Das Quartal, in welchem die Klausur gechrieben wird.
	 */
	public quartal : number = -1;

	/**
	 * Die ID des Faches.
	 */
	public idFach : number = -1;

	/**
	 * Das Kürzel einer verallgemeinerten Kursart.
	 */
	public kursart : string = "";

	/**
	 * Die Dauer der Klausur in Minuten.
	 */
	public dauer : number = 0;

	/**
	 * Die Auswahlzeit in Minuten, sofern vorhanden.
	 */
	public auswahlzeit : number = 0;

	/**
	 * Die Information, ob es sich um eine mündliche Prüfung handelt.
	 */
	public istMdlPruefung : boolean = false;

	/**
	 * Die Information, ob Audioequipment nötig ist, z.B. für Klasuren mit Hörverstehensanteilen.
	 */
	public istAudioNotwendig : boolean = false;

	/**
	 * Die Information, ob Videoequipment nötig ist, z.B. für Klasuren mit Videoanalyse.
	 */
	public istVideoNotwendig : boolean = false;

	/**
	 * Die textuelle Bemerkung zur Klausurvorgabe, sofern vorhanden.
	 */
	public bemerkungVorgabe : string | null = null;


	public constructor() {
		super();
	}

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	public equals(another : unknown | null) : boolean {
		return (another !== null) && (((another instanceof JavaObject) && ((another as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe')))) && (this.idVorgabe === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurvorgabe(another)).idVorgabe);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der idVorgabe.
	 *
	 * @return den HashCode
	 */
	public hashCode() : number {
		return JavaLong.hashCode((this.idVorgabe));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurvorgabe {
		const obj = JSON.parse(json) as Partial<GostKlausurvorgabe>;
		const result = new GostKlausurvorgabe();
		if (obj.idVorgabe === undefined)
			throw new Error('invalid json format, missing attribute idVorgabe');
		result.idVorgabe = obj.idVorgabe;
		if (obj.abiJahrgang === undefined)
			throw new Error('invalid json format, missing attribute abiJahrgang');
		result.abiJahrgang = obj.abiJahrgang;
		if (obj.halbjahr === undefined)
			throw new Error('invalid json format, missing attribute halbjahr');
		result.halbjahr = obj.halbjahr;
		if (obj.quartal === undefined)
			throw new Error('invalid json format, missing attribute quartal');
		result.quartal = obj.quartal;
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.kursart === undefined)
			throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (obj.dauer === undefined)
			throw new Error('invalid json format, missing attribute dauer');
		result.dauer = obj.dauer;
		if (obj.auswahlzeit === undefined)
			throw new Error('invalid json format, missing attribute auswahlzeit');
		result.auswahlzeit = obj.auswahlzeit;
		if (obj.istMdlPruefung === undefined)
			throw new Error('invalid json format, missing attribute istMdlPruefung');
		result.istMdlPruefung = obj.istMdlPruefung;
		if (obj.istAudioNotwendig === undefined)
			throw new Error('invalid json format, missing attribute istAudioNotwendig');
		result.istAudioNotwendig = obj.istAudioNotwendig;
		if (obj.istVideoNotwendig === undefined)
			throw new Error('invalid json format, missing attribute istVideoNotwendig');
		result.istVideoNotwendig = obj.istVideoNotwendig;
		result.bemerkungVorgabe = (obj.bemerkungVorgabe === undefined) ? null : obj.bemerkungVorgabe === null ? null : obj.bemerkungVorgabe;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurvorgabe) : string {
		let result = '{';
		result += '"idVorgabe" : ' + obj.idVorgabe.toString() + ',';
		result += '"abiJahrgang" : ' + obj.abiJahrgang.toString() + ',';
		result += '"halbjahr" : ' + obj.halbjahr.toString() + ',';
		result += '"quartal" : ' + obj.quartal.toString() + ',';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		result += '"dauer" : ' + obj.dauer.toString() + ',';
		result += '"auswahlzeit" : ' + obj.auswahlzeit.toString() + ',';
		result += '"istMdlPruefung" : ' + obj.istMdlPruefung.toString() + ',';
		result += '"istAudioNotwendig" : ' + obj.istAudioNotwendig.toString() + ',';
		result += '"istVideoNotwendig" : ' + obj.istVideoNotwendig.toString() + ',';
		result += '"bemerkungVorgabe" : ' + ((!obj.bemerkungVorgabe) ? 'null' : JSON.stringify(obj.bemerkungVorgabe)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurvorgabe>) : string {
		let result = '{';
		if (obj.idVorgabe !== undefined) {
			result += '"idVorgabe" : ' + obj.idVorgabe.toString() + ',';
		}
		if (obj.abiJahrgang !== undefined) {
			result += '"abiJahrgang" : ' + obj.abiJahrgang.toString() + ',';
		}
		if (obj.halbjahr !== undefined) {
			result += '"halbjahr" : ' + obj.halbjahr.toString() + ',';
		}
		if (obj.quartal !== undefined) {
			result += '"quartal" : ' + obj.quartal.toString() + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		}
		if (obj.dauer !== undefined) {
			result += '"dauer" : ' + obj.dauer.toString() + ',';
		}
		if (obj.auswahlzeit !== undefined) {
			result += '"auswahlzeit" : ' + obj.auswahlzeit.toString() + ',';
		}
		if (obj.istMdlPruefung !== undefined) {
			result += '"istMdlPruefung" : ' + obj.istMdlPruefung.toString() + ',';
		}
		if (obj.istAudioNotwendig !== undefined) {
			result += '"istAudioNotwendig" : ' + obj.istAudioNotwendig.toString() + ',';
		}
		if (obj.istVideoNotwendig !== undefined) {
			result += '"istVideoNotwendig" : ' + obj.istVideoNotwendig.toString() + ',';
		}
		if (obj.bemerkungVorgabe !== undefined) {
			result += '"bemerkungVorgabe" : ' + ((!obj.bemerkungVorgabe) ? 'null' : JSON.stringify(obj.bemerkungVorgabe)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurvorgabe(obj : unknown) : GostKlausurvorgabe {
	return obj as GostKlausurvorgabe;
}
