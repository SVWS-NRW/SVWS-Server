import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ReligionszugehoerigkeitenStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Ein Schüler-Jahrgang, der an der Schule unterrichtet wird.
	 */
	public jahrgang: string | null = "";

	/**
	 * Satzschlüssel: Ein Bildungsgang, der an der Schule unterrichtet wird.
	 */
	public schulgliederung: string | null = "";

	/**
	 * Satzschlüssel: Ein Bildungsbereich, der an der Schule unterrichtet wird.
	 */
	public bildungsbereich: string | null = "";

	/**
	 * Satzschlüssel: Ein Förderschwerpunkt, der an der Schule vorkommt.
	 */
	public foerderschwerpunkt: string | null = "";

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Evangelisch.
	 */
	public evZusammen: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Evangelisch Weiblich.
	 */
	public evWeiblich: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Katholisch.
	 */
	public kathZusammen: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Katholisch Weiblich.
	 */
	public kathWeiblich: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Jüdisch.
	 */
	public juedischZusammen: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Jüdisch Weiblich.
	 */
	public juedischWeiblich: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Sonstige Orthodoxe.
	 */
	public sonstOrthZusammen: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Sonstige Orthodoxe Weiblich.
	 */
	public sonstOrthWeiblich: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Syrisch Orthodoxe.
	 */
	public syrOrthZusammen: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Syrisch Orthodoxe Weiblich.
	 */
	public syrOrthWeiblich: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Islamisch.
	 */
	public islamischZusammen: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Islamisch Weiblich.
	 */
	public islamischWeiblich: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Alevitisch.
	 */
	public alevitischZusammen: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Alevitisch Weiblich.
	 */
	public alevitischWeiblich: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Mennoniten BG.NRW.
	 */
	public mennonitenZusammen: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Mennoniten BG.NRW Weiblich.
	 */
	public mennonitenWeiblich: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Andere.
	 */
	public andereZusammen: number = 0;

	/**
	 * Die Summe aller Schüler mit der Religionszugehörigkeit Andere Weiblich.
	 */
	public andereWeiblich: number = 0;

	/**
	 * Die Summe aller Schüler ohne Religionszugehörigkeit.
	 */
	public ohneZusammen: number = 0;

	/**
	 * Die Summe aller Schüler ohne Religionszugehörigkeit Weiblich.
	 */
	public ohneWeiblich: number = 0;

	/**
	 * Die Summe aller Schüler dieses Satzes ingesamt.
	 */
	public insgesamtZusammen: number = 0;

	/**
	 * Die Summe aller Schüler dieses Satzes ingesamt Weiblich.
	 */
	public insgesamtWeiblich: number = 0;

	/**
	 * Die Summe aller evangelischen Schüler, die sich vom Religionsunterricht abgemeldet haben.
	 */
	public abmeldungenEvZusammen: number = 0;

	/**
	 * Die Summe aller evangelischen Schüler, die sich vom Religionsunterricht abgemeldet haben Weiblich.
	 */
	public abmeldungenEvWeiblich: number = 0;

	/**
	 * Die Summe aller katholischen Schüler, die sich vom Religionsunterricht abgemeldet haben.
	 */
	public abmeldungenKathZusammen: number = 0;

	/**
	 * Die Summe aller katholischen Schüler, die sich vom Religionsunterricht abgemeldet haben Weiblich.
	 */
	public abmeldungenKathWeiblich: number = 0;

	/**
	 * Die Summe aller evanglischer Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten.
	 */
	public ohneUnterrichtEvangelischZusammen: number = 0;

	/**
	 * Die Summe aller evanglischer Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten Weiblich.
	 */
	public ohneUnterrichtEvangelischWeiblich: number = 0;

	/**
	 * Die Summe aller katholischen Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten.
	 */
	public ohneUnterrichtKatholischZusammen: number = 0;

	/**
	 * Die Summe aller katholischen Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten Weiblich.
	 */
	public ohneUnterrichtKatholischWeiblich: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.ReligionszugehoerigkeitenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.ReligionszugehoerigkeitenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<ReligionszugehoerigkeitenStatistikExport>('de.svws_nrw.asd.export.data.ReligionszugehoerigkeitenStatistikExport');

	public static transpilerFromJSON(json: string): ReligionszugehoerigkeitenStatistikExport {
		const obj = JSON.parse(json) as Partial<ReligionszugehoerigkeitenStatistikExport>;
		const result = new ReligionszugehoerigkeitenStatistikExport();
		result.jahrgang = (obj.jahrgang === undefined) ? null : obj.jahrgang === null ? null : obj.jahrgang;
		result.schulgliederung = (obj.schulgliederung === undefined) ? null : obj.schulgliederung === null ? null : obj.schulgliederung;
		result.bildungsbereich = (obj.bildungsbereich === undefined) ? null : obj.bildungsbereich === null ? null : obj.bildungsbereich;
		result.foerderschwerpunkt = (obj.foerderschwerpunkt === undefined) ? null : obj.foerderschwerpunkt === null ? null : obj.foerderschwerpunkt;
		if (obj.evZusammen === undefined)
			throw new Error('invalid json format, missing attribute evZusammen');
		result.evZusammen = obj.evZusammen;
		if (obj.evWeiblich === undefined)
			throw new Error('invalid json format, missing attribute evWeiblich');
		result.evWeiblich = obj.evWeiblich;
		if (obj.kathZusammen === undefined)
			throw new Error('invalid json format, missing attribute kathZusammen');
		result.kathZusammen = obj.kathZusammen;
		if (obj.kathWeiblich === undefined)
			throw new Error('invalid json format, missing attribute kathWeiblich');
		result.kathWeiblich = obj.kathWeiblich;
		if (obj.juedischZusammen === undefined)
			throw new Error('invalid json format, missing attribute juedischZusammen');
		result.juedischZusammen = obj.juedischZusammen;
		if (obj.juedischWeiblich === undefined)
			throw new Error('invalid json format, missing attribute juedischWeiblich');
		result.juedischWeiblich = obj.juedischWeiblich;
		if (obj.sonstOrthZusammen === undefined)
			throw new Error('invalid json format, missing attribute sonstOrthZusammen');
		result.sonstOrthZusammen = obj.sonstOrthZusammen;
		if (obj.sonstOrthWeiblich === undefined)
			throw new Error('invalid json format, missing attribute sonstOrthWeiblich');
		result.sonstOrthWeiblich = obj.sonstOrthWeiblich;
		if (obj.syrOrthZusammen === undefined)
			throw new Error('invalid json format, missing attribute syrOrthZusammen');
		result.syrOrthZusammen = obj.syrOrthZusammen;
		if (obj.syrOrthWeiblich === undefined)
			throw new Error('invalid json format, missing attribute syrOrthWeiblich');
		result.syrOrthWeiblich = obj.syrOrthWeiblich;
		if (obj.islamischZusammen === undefined)
			throw new Error('invalid json format, missing attribute islamischZusammen');
		result.islamischZusammen = obj.islamischZusammen;
		if (obj.islamischWeiblich === undefined)
			throw new Error('invalid json format, missing attribute islamischWeiblich');
		result.islamischWeiblich = obj.islamischWeiblich;
		if (obj.alevitischZusammen === undefined)
			throw new Error('invalid json format, missing attribute alevitischZusammen');
		result.alevitischZusammen = obj.alevitischZusammen;
		if (obj.alevitischWeiblich === undefined)
			throw new Error('invalid json format, missing attribute alevitischWeiblich');
		result.alevitischWeiblich = obj.alevitischWeiblich;
		if (obj.mennonitenZusammen === undefined)
			throw new Error('invalid json format, missing attribute mennonitenZusammen');
		result.mennonitenZusammen = obj.mennonitenZusammen;
		if (obj.mennonitenWeiblich === undefined)
			throw new Error('invalid json format, missing attribute mennonitenWeiblich');
		result.mennonitenWeiblich = obj.mennonitenWeiblich;
		if (obj.andereZusammen === undefined)
			throw new Error('invalid json format, missing attribute andereZusammen');
		result.andereZusammen = obj.andereZusammen;
		if (obj.andereWeiblich === undefined)
			throw new Error('invalid json format, missing attribute andereWeiblich');
		result.andereWeiblich = obj.andereWeiblich;
		if (obj.ohneZusammen === undefined)
			throw new Error('invalid json format, missing attribute ohneZusammen');
		result.ohneZusammen = obj.ohneZusammen;
		if (obj.ohneWeiblich === undefined)
			throw new Error('invalid json format, missing attribute ohneWeiblich');
		result.ohneWeiblich = obj.ohneWeiblich;
		if (obj.insgesamtZusammen === undefined)
			throw new Error('invalid json format, missing attribute insgesamtZusammen');
		result.insgesamtZusammen = obj.insgesamtZusammen;
		if (obj.insgesamtWeiblich === undefined)
			throw new Error('invalid json format, missing attribute insgesamtWeiblich');
		result.insgesamtWeiblich = obj.insgesamtWeiblich;
		if (obj.abmeldungenEvZusammen === undefined)
			throw new Error('invalid json format, missing attribute abmeldungenEvZusammen');
		result.abmeldungenEvZusammen = obj.abmeldungenEvZusammen;
		if (obj.abmeldungenEvWeiblich === undefined)
			throw new Error('invalid json format, missing attribute abmeldungenEvWeiblich');
		result.abmeldungenEvWeiblich = obj.abmeldungenEvWeiblich;
		if (obj.abmeldungenKathZusammen === undefined)
			throw new Error('invalid json format, missing attribute abmeldungenKathZusammen');
		result.abmeldungenKathZusammen = obj.abmeldungenKathZusammen;
		if (obj.abmeldungenKathWeiblich === undefined)
			throw new Error('invalid json format, missing attribute abmeldungenKathWeiblich');
		result.abmeldungenKathWeiblich = obj.abmeldungenKathWeiblich;
		if (obj.ohneUnterrichtEvangelischZusammen === undefined)
			throw new Error('invalid json format, missing attribute ohneUnterrichtEvangelischZusammen');
		result.ohneUnterrichtEvangelischZusammen = obj.ohneUnterrichtEvangelischZusammen;
		if (obj.ohneUnterrichtEvangelischWeiblich === undefined)
			throw new Error('invalid json format, missing attribute ohneUnterrichtEvangelischWeiblich');
		result.ohneUnterrichtEvangelischWeiblich = obj.ohneUnterrichtEvangelischWeiblich;
		if (obj.ohneUnterrichtKatholischZusammen === undefined)
			throw new Error('invalid json format, missing attribute ohneUnterrichtKatholischZusammen');
		result.ohneUnterrichtKatholischZusammen = obj.ohneUnterrichtKatholischZusammen;
		if (obj.ohneUnterrichtKatholischWeiblich === undefined)
			throw new Error('invalid json format, missing attribute ohneUnterrichtKatholischWeiblich');
		result.ohneUnterrichtKatholischWeiblich = obj.ohneUnterrichtKatholischWeiblich;
		return result;
	}

	public static transpilerToJSON(obj: ReligionszugehoerigkeitenStatistikExport): string {
		let result = '{';
		result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		result += '"bildungsbereich" : ' + ((obj.bildungsbereich === null) ? 'null' : JSON.stringify(obj.bildungsbereich)) + ',';
		result += '"foerderschwerpunkt" : ' + ((obj.foerderschwerpunkt === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt)) + ',';
		result += '"evZusammen" : ' + obj.evZusammen.toString() + ',';
		result += '"evWeiblich" : ' + obj.evWeiblich.toString() + ',';
		result += '"kathZusammen" : ' + obj.kathZusammen.toString() + ',';
		result += '"kathWeiblich" : ' + obj.kathWeiblich.toString() + ',';
		result += '"juedischZusammen" : ' + obj.juedischZusammen.toString() + ',';
		result += '"juedischWeiblich" : ' + obj.juedischWeiblich.toString() + ',';
		result += '"sonstOrthZusammen" : ' + obj.sonstOrthZusammen.toString() + ',';
		result += '"sonstOrthWeiblich" : ' + obj.sonstOrthWeiblich.toString() + ',';
		result += '"syrOrthZusammen" : ' + obj.syrOrthZusammen.toString() + ',';
		result += '"syrOrthWeiblich" : ' + obj.syrOrthWeiblich.toString() + ',';
		result += '"islamischZusammen" : ' + obj.islamischZusammen.toString() + ',';
		result += '"islamischWeiblich" : ' + obj.islamischWeiblich.toString() + ',';
		result += '"alevitischZusammen" : ' + obj.alevitischZusammen.toString() + ',';
		result += '"alevitischWeiblich" : ' + obj.alevitischWeiblich.toString() + ',';
		result += '"mennonitenZusammen" : ' + obj.mennonitenZusammen.toString() + ',';
		result += '"mennonitenWeiblich" : ' + obj.mennonitenWeiblich.toString() + ',';
		result += '"andereZusammen" : ' + obj.andereZusammen.toString() + ',';
		result += '"andereWeiblich" : ' + obj.andereWeiblich.toString() + ',';
		result += '"ohneZusammen" : ' + obj.ohneZusammen.toString() + ',';
		result += '"ohneWeiblich" : ' + obj.ohneWeiblich.toString() + ',';
		result += '"insgesamtZusammen" : ' + obj.insgesamtZusammen.toString() + ',';
		result += '"insgesamtWeiblich" : ' + obj.insgesamtWeiblich.toString() + ',';
		result += '"abmeldungenEvZusammen" : ' + obj.abmeldungenEvZusammen.toString() + ',';
		result += '"abmeldungenEvWeiblich" : ' + obj.abmeldungenEvWeiblich.toString() + ',';
		result += '"abmeldungenKathZusammen" : ' + obj.abmeldungenKathZusammen.toString() + ',';
		result += '"abmeldungenKathWeiblich" : ' + obj.abmeldungenKathWeiblich.toString() + ',';
		result += '"ohneUnterrichtEvangelischZusammen" : ' + obj.ohneUnterrichtEvangelischZusammen.toString() + ',';
		result += '"ohneUnterrichtEvangelischWeiblich" : ' + obj.ohneUnterrichtEvangelischWeiblich.toString() + ',';
		result += '"ohneUnterrichtKatholischZusammen" : ' + obj.ohneUnterrichtKatholischZusammen.toString() + ',';
		result += '"ohneUnterrichtKatholischWeiblich" : ' + obj.ohneUnterrichtKatholischWeiblich.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReligionszugehoerigkeitenStatistikExport>): string {
		let result = '{';
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (obj.schulgliederung !== undefined) {
			result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		}
		if (obj.bildungsbereich !== undefined) {
			result += '"bildungsbereich" : ' + ((obj.bildungsbereich === null) ? 'null' : JSON.stringify(obj.bildungsbereich)) + ',';
		}
		if (obj.foerderschwerpunkt !== undefined) {
			result += '"foerderschwerpunkt" : ' + ((obj.foerderschwerpunkt === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt)) + ',';
		}
		if (obj.evZusammen !== undefined) {
			result += '"evZusammen" : ' + obj.evZusammen.toString() + ',';
		}
		if (obj.evWeiblich !== undefined) {
			result += '"evWeiblich" : ' + obj.evWeiblich.toString() + ',';
		}
		if (obj.kathZusammen !== undefined) {
			result += '"kathZusammen" : ' + obj.kathZusammen.toString() + ',';
		}
		if (obj.kathWeiblich !== undefined) {
			result += '"kathWeiblich" : ' + obj.kathWeiblich.toString() + ',';
		}
		if (obj.juedischZusammen !== undefined) {
			result += '"juedischZusammen" : ' + obj.juedischZusammen.toString() + ',';
		}
		if (obj.juedischWeiblich !== undefined) {
			result += '"juedischWeiblich" : ' + obj.juedischWeiblich.toString() + ',';
		}
		if (obj.sonstOrthZusammen !== undefined) {
			result += '"sonstOrthZusammen" : ' + obj.sonstOrthZusammen.toString() + ',';
		}
		if (obj.sonstOrthWeiblich !== undefined) {
			result += '"sonstOrthWeiblich" : ' + obj.sonstOrthWeiblich.toString() + ',';
		}
		if (obj.syrOrthZusammen !== undefined) {
			result += '"syrOrthZusammen" : ' + obj.syrOrthZusammen.toString() + ',';
		}
		if (obj.syrOrthWeiblich !== undefined) {
			result += '"syrOrthWeiblich" : ' + obj.syrOrthWeiblich.toString() + ',';
		}
		if (obj.islamischZusammen !== undefined) {
			result += '"islamischZusammen" : ' + obj.islamischZusammen.toString() + ',';
		}
		if (obj.islamischWeiblich !== undefined) {
			result += '"islamischWeiblich" : ' + obj.islamischWeiblich.toString() + ',';
		}
		if (obj.alevitischZusammen !== undefined) {
			result += '"alevitischZusammen" : ' + obj.alevitischZusammen.toString() + ',';
		}
		if (obj.alevitischWeiblich !== undefined) {
			result += '"alevitischWeiblich" : ' + obj.alevitischWeiblich.toString() + ',';
		}
		if (obj.mennonitenZusammen !== undefined) {
			result += '"mennonitenZusammen" : ' + obj.mennonitenZusammen.toString() + ',';
		}
		if (obj.mennonitenWeiblich !== undefined) {
			result += '"mennonitenWeiblich" : ' + obj.mennonitenWeiblich.toString() + ',';
		}
		if (obj.andereZusammen !== undefined) {
			result += '"andereZusammen" : ' + obj.andereZusammen.toString() + ',';
		}
		if (obj.andereWeiblich !== undefined) {
			result += '"andereWeiblich" : ' + obj.andereWeiblich.toString() + ',';
		}
		if (obj.ohneZusammen !== undefined) {
			result += '"ohneZusammen" : ' + obj.ohneZusammen.toString() + ',';
		}
		if (obj.ohneWeiblich !== undefined) {
			result += '"ohneWeiblich" : ' + obj.ohneWeiblich.toString() + ',';
		}
		if (obj.insgesamtZusammen !== undefined) {
			result += '"insgesamtZusammen" : ' + obj.insgesamtZusammen.toString() + ',';
		}
		if (obj.insgesamtWeiblich !== undefined) {
			result += '"insgesamtWeiblich" : ' + obj.insgesamtWeiblich.toString() + ',';
		}
		if (obj.abmeldungenEvZusammen !== undefined) {
			result += '"abmeldungenEvZusammen" : ' + obj.abmeldungenEvZusammen.toString() + ',';
		}
		if (obj.abmeldungenEvWeiblich !== undefined) {
			result += '"abmeldungenEvWeiblich" : ' + obj.abmeldungenEvWeiblich.toString() + ',';
		}
		if (obj.abmeldungenKathZusammen !== undefined) {
			result += '"abmeldungenKathZusammen" : ' + obj.abmeldungenKathZusammen.toString() + ',';
		}
		if (obj.abmeldungenKathWeiblich !== undefined) {
			result += '"abmeldungenKathWeiblich" : ' + obj.abmeldungenKathWeiblich.toString() + ',';
		}
		if (obj.ohneUnterrichtEvangelischZusammen !== undefined) {
			result += '"ohneUnterrichtEvangelischZusammen" : ' + obj.ohneUnterrichtEvangelischZusammen.toString() + ',';
		}
		if (obj.ohneUnterrichtEvangelischWeiblich !== undefined) {
			result += '"ohneUnterrichtEvangelischWeiblich" : ' + obj.ohneUnterrichtEvangelischWeiblich.toString() + ',';
		}
		if (obj.ohneUnterrichtKatholischZusammen !== undefined) {
			result += '"ohneUnterrichtKatholischZusammen" : ' + obj.ohneUnterrichtKatholischZusammen.toString() + ',';
		}
		if (obj.ohneUnterrichtKatholischWeiblich !== undefined) {
			result += '"ohneUnterrichtKatholischWeiblich" : ' + obj.ohneUnterrichtKatholischWeiblich.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_ReligionszugehoerigkeitenStatistikExport(obj: unknown): ReligionszugehoerigkeitenStatistikExport {
	return obj as ReligionszugehoerigkeitenStatistikExport;
}
