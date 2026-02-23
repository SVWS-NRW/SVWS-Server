import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';
import { KlassenHerkunftAnrechungenStatistikExport } from '../../../asd/export/data/KlassenHerkunftAnrechungenStatistikExport';

export class KlassenHerkunftStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Die Herkunftsschulnummer.
	 */
	public herkunftsSchulNr: string = "";

	/**
	 * Satzschlüssel: Die Herkunftsschulform.
	 */
	public herkunftsschulform: string = "";

	/**
	 * Satzschlüssel: Die Herkunftsart.
	 */
	public herkunftsart: string = "";

	/**
	 * Satzschlüssel: Die Grundschulempfehlung.
	 */
	public kuerzelGrundschuleUebergangsempfehlung: string = "";

	/**
	 * Die Schüler des Herkunftsatzes insgesamt.
	 */
	public schuelerInsgesamt: number = 0;

	/**
	 * Die Schüler des Herkunftsatzes weiblich.
	 */
	public schuelerWeiblich: number = 0;

	/**
	 * Die ausländischen Schüler des Herkunftsatzes zusammen.
	 */
	public schuelerAuslaendischZusammen: number = 0;

	/**
	 * Die ausländischen Schüler des Herkunftsatzes weiblich.
	 */
	public schuelerAuslaendischWeiblich: number = 0;

	/**
	 * Die Anrechnungen des Herkunftssatzes (B-Schulen K86).
	 */
	public klassenHerkunftAnrechungenStatistikExport: KlassenHerkunftAnrechungenStatistikExport = new KlassenHerkunftAnrechungenStatistikExport();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlassenHerkunftStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlassenHerkunftStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlassenHerkunftStatistikExport>('de.svws_nrw.asd.export.data.KlassenHerkunftStatistikExport');

	public static transpilerFromJSON(json: string): KlassenHerkunftStatistikExport {
		const obj = JSON.parse(json) as Partial<KlassenHerkunftStatistikExport>;
		const result = new KlassenHerkunftStatistikExport();
		if (obj.herkunftsSchulNr === undefined)
			throw new Error('invalid json format, missing attribute herkunftsSchulNr');
		result.herkunftsSchulNr = obj.herkunftsSchulNr;
		if (obj.herkunftsschulform === undefined)
			throw new Error('invalid json format, missing attribute herkunftsschulform');
		result.herkunftsschulform = obj.herkunftsschulform;
		if (obj.herkunftsart === undefined)
			throw new Error('invalid json format, missing attribute herkunftsart');
		result.herkunftsart = obj.herkunftsart;
		if (obj.kuerzelGrundschuleUebergangsempfehlung === undefined)
			throw new Error('invalid json format, missing attribute kuerzelGrundschuleUebergangsempfehlung');
		result.kuerzelGrundschuleUebergangsempfehlung = obj.kuerzelGrundschuleUebergangsempfehlung;
		if (obj.schuelerInsgesamt === undefined)
			throw new Error('invalid json format, missing attribute schuelerInsgesamt');
		result.schuelerInsgesamt = obj.schuelerInsgesamt;
		if (obj.schuelerWeiblich === undefined)
			throw new Error('invalid json format, missing attribute schuelerWeiblich');
		result.schuelerWeiblich = obj.schuelerWeiblich;
		if (obj.schuelerAuslaendischZusammen === undefined)
			throw new Error('invalid json format, missing attribute schuelerAuslaendischZusammen');
		result.schuelerAuslaendischZusammen = obj.schuelerAuslaendischZusammen;
		if (obj.schuelerAuslaendischWeiblich === undefined)
			throw new Error('invalid json format, missing attribute schuelerAuslaendischWeiblich');
		result.schuelerAuslaendischWeiblich = obj.schuelerAuslaendischWeiblich;
		if (obj.klassenHerkunftAnrechungenStatistikExport === undefined)
			throw new Error('invalid json format, missing attribute klassenHerkunftAnrechungenStatistikExport');
		result.klassenHerkunftAnrechungenStatistikExport = KlassenHerkunftAnrechungenStatistikExport.transpilerFromJSON(JSON.stringify(obj.klassenHerkunftAnrechungenStatistikExport));
		return result;
	}

	public static transpilerToJSON(obj: KlassenHerkunftStatistikExport): string {
		let result = '{';
		result += '"herkunftsSchulNr" : ' + JSON.stringify(obj.herkunftsSchulNr) + ',';
		result += '"herkunftsschulform" : ' + JSON.stringify(obj.herkunftsschulform) + ',';
		result += '"herkunftsart" : ' + JSON.stringify(obj.herkunftsart) + ',';
		result += '"kuerzelGrundschuleUebergangsempfehlung" : ' + JSON.stringify(obj.kuerzelGrundschuleUebergangsempfehlung) + ',';
		result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		result += '"schuelerWeiblich" : ' + obj.schuelerWeiblich.toString() + ',';
		result += '"schuelerAuslaendischZusammen" : ' + obj.schuelerAuslaendischZusammen.toString() + ',';
		result += '"schuelerAuslaendischWeiblich" : ' + obj.schuelerAuslaendischWeiblich.toString() + ',';
		result += '"klassenHerkunftAnrechungenStatistikExport" : ' + KlassenHerkunftAnrechungenStatistikExport.transpilerToJSON(obj.klassenHerkunftAnrechungenStatistikExport) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenHerkunftStatistikExport>): string {
		let result = '{';
		if (obj.herkunftsSchulNr !== undefined) {
			result += '"herkunftsSchulNr" : ' + JSON.stringify(obj.herkunftsSchulNr) + ',';
		}
		if (obj.herkunftsschulform !== undefined) {
			result += '"herkunftsschulform" : ' + JSON.stringify(obj.herkunftsschulform) + ',';
		}
		if (obj.herkunftsart !== undefined) {
			result += '"herkunftsart" : ' + JSON.stringify(obj.herkunftsart) + ',';
		}
		if (obj.kuerzelGrundschuleUebergangsempfehlung !== undefined) {
			result += '"kuerzelGrundschuleUebergangsempfehlung" : ' + JSON.stringify(obj.kuerzelGrundschuleUebergangsempfehlung) + ',';
		}
		if (obj.schuelerInsgesamt !== undefined) {
			result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		}
		if (obj.schuelerWeiblich !== undefined) {
			result += '"schuelerWeiblich" : ' + obj.schuelerWeiblich.toString() + ',';
		}
		if (obj.schuelerAuslaendischZusammen !== undefined) {
			result += '"schuelerAuslaendischZusammen" : ' + obj.schuelerAuslaendischZusammen.toString() + ',';
		}
		if (obj.schuelerAuslaendischWeiblich !== undefined) {
			result += '"schuelerAuslaendischWeiblich" : ' + obj.schuelerAuslaendischWeiblich.toString() + ',';
		}
		if (obj.klassenHerkunftAnrechungenStatistikExport !== undefined) {
			result += '"klassenHerkunftAnrechungenStatistikExport" : ' + KlassenHerkunftAnrechungenStatistikExport.transpilerToJSON(obj.klassenHerkunftAnrechungenStatistikExport) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_KlassenHerkunftStatistikExport(obj: unknown): KlassenHerkunftStatistikExport {
	return obj as KlassenHerkunftStatistikExport;
}
