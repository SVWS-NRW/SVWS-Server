import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerZahlenStatistikExport extends JavaObject {

	/**
	 * Die Schüler Insgesamt Zusammen.
	 */
	public insgesamtZusammen: number = 0;

	/**
	 * Die Schüler Insgesamt Weiblich.
	 */
	public insgesamtWeiblich: number = 0;

	/**
	 * Die ausländischen Schüler Zusammen.
	 */
	public auslaenderZusammen: number = 0;

	/**
	 * Die ausländischen Schüler Weiblich.
	 */
	public auslaenderWeiblich: number = 0;

	/**
	 * Die Schüler mit Schwerstbehinderung Zusammen.
	 */
	public schwerstbehinderteZusammen: number = 0;

	/**
	 * Die Schüler mit Schwerstbehinderung Zusammen Weiblich.
	 */
	public schwerstbehinderteWeiblich: number = 0;

	/**
	 * Die Vollbeleger Zusammen.
	 */
	public vollbelegerZusammen: number = 0;

	/**
	 * Die Vollbeleger Weiblich.
	 */
	public vollbelegerWeiblich: number = 0;

	/**
	 * Die Teilbeleger Zusammen.
	 */
	public teilbelegerZusammen: number = 0;

	/**
	 * Die Teilbeleger Weiblich.
	 */
	public teilbelegerWeiblich: number = 0;

	/**
	 * Die ausländischen Schüler von der Berufsschule Teilzeit Zusammen.
	 */
	public auslaenderBsTeilzeitZusammen: number = 0;

	/**
	 * Die ausländischen Schüler von der Berufsschule Teilzeit Weiblich.
	 */
	public auslaenderBsTeilzeitWeiblich: number = 0;

	/**
	 * Die beurlaubten Studenten Zusammen.
	 */
	public studentenBeurlaubtZusammen: number = 0;

	/**
	 * Die beurlaubten Studenten Weiblich.
	 */
	public studentenBeurlaubtWeiblich: number = 0;

	/**
	 * Die Schüler mit Förderschwerpunkt Zusammen.
	 */
	public foerderschwerpunktZusammen: number = 0;

	/**
	 * Die Schüler mit Förderschwerpunkt Weiblich.
	 */
	public foerderschwerpunktWeiblich: number = 0;

	/**
	 * Die ausländischen Schüler von der Berufsschule Vollzeit Zusammen.
	 */
	public auslaenderBsVollzeitZusammen: number = 0;

	/**
	 * Die ausländischen Schüler von der Berufsschule Vollzeit Weiblich.
	 */
	public auslaenderBsVollzeitWeiblich: number = 0;

	/**
	 * Die zur Zeit angemeldeten Schüler A12 Zusammen.
	 */
	public zurZeitAngemeldetA12Zusammen: number = 0;

	/**
	 * Die zur Zeit angemeldeten Schüler A12 Weiblich.
	 */
	public zurZeitAngemeldetA12Weiblich: number = 0;

	/**
	 * Die weiteren zu erwartenden Schüler A12 Zusammen.
	 */
	public weitereErwarteteSchuelerA12Zusammen: number = 0;

	/**
	 * Die weiteren zu erwartenden Schüler A12 Weiblich.
	 */
	public weitereErwarteteSchuelerA12Weiblich: number = 0;

	/**
	 * Die zur Zeit angemeldeten Schüler A13 Zusammen.
	 */
	public zurZeitAngemeldetA13Zusammen: number = 0;

	/**
	 * Die zur Zeit angemeldeten Schüler A13 Weiblich.
	 */
	public zurZeitAngemeldetA13Weiblich: number = 0;

	/**
	 * Die zu erwartenden Schüler A13 Zusammen.
	 */
	public zuErwartendeSchuelerA13Zusammen: number = 0;

	/**
	 * Die zu erwartenden Schüler A13 Weiblich.
	 */
	public zuErwartendeSchuelerA13Weiblich: number = 0;

	/**
	 * Die weiteren zu erwartenden Schüler A13 Zusammen.
	 */
	public weitereErwarteteSchuelerA13Zusammen: number = 0;

	/**
	 * Die weiteren zu erwartenden Schüler A13 Weiblich.
	 */
	public weitereErwarteteSchuelerA13Weiblich: number = 0;

	/**
	 * Die Schüler mit dem Geschlecht 'Divers'.
	 */
	public schuelerDivers: number = 0;

	/**
	 * Die Schüler mit der Geschlechtsangabe 'ohne Angabe'.
	 */
	public schuelerOhneAngabe: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.SchuelerZahlenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.SchuelerZahlenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<SchuelerZahlenStatistikExport>('de.svws_nrw.asd.export.data.SchuelerZahlenStatistikExport');

	public static transpilerFromJSON(json: string): SchuelerZahlenStatistikExport {
		const obj = JSON.parse(json) as Partial<SchuelerZahlenStatistikExport>;
		const result = new SchuelerZahlenStatistikExport();
		if (obj.insgesamtZusammen === undefined)
			throw new Error('invalid json format, missing attribute insgesamtZusammen');
		result.insgesamtZusammen = obj.insgesamtZusammen;
		if (obj.insgesamtWeiblich === undefined)
			throw new Error('invalid json format, missing attribute insgesamtWeiblich');
		result.insgesamtWeiblich = obj.insgesamtWeiblich;
		if (obj.auslaenderZusammen === undefined)
			throw new Error('invalid json format, missing attribute auslaenderZusammen');
		result.auslaenderZusammen = obj.auslaenderZusammen;
		if (obj.auslaenderWeiblich === undefined)
			throw new Error('invalid json format, missing attribute auslaenderWeiblich');
		result.auslaenderWeiblich = obj.auslaenderWeiblich;
		if (obj.schwerstbehinderteZusammen === undefined)
			throw new Error('invalid json format, missing attribute schwerstbehinderteZusammen');
		result.schwerstbehinderteZusammen = obj.schwerstbehinderteZusammen;
		if (obj.schwerstbehinderteWeiblich === undefined)
			throw new Error('invalid json format, missing attribute schwerstbehinderteWeiblich');
		result.schwerstbehinderteWeiblich = obj.schwerstbehinderteWeiblich;
		if (obj.vollbelegerZusammen === undefined)
			throw new Error('invalid json format, missing attribute vollbelegerZusammen');
		result.vollbelegerZusammen = obj.vollbelegerZusammen;
		if (obj.vollbelegerWeiblich === undefined)
			throw new Error('invalid json format, missing attribute vollbelegerWeiblich');
		result.vollbelegerWeiblich = obj.vollbelegerWeiblich;
		if (obj.teilbelegerZusammen === undefined)
			throw new Error('invalid json format, missing attribute teilbelegerZusammen');
		result.teilbelegerZusammen = obj.teilbelegerZusammen;
		if (obj.teilbelegerWeiblich === undefined)
			throw new Error('invalid json format, missing attribute teilbelegerWeiblich');
		result.teilbelegerWeiblich = obj.teilbelegerWeiblich;
		if (obj.auslaenderBsTeilzeitZusammen === undefined)
			throw new Error('invalid json format, missing attribute auslaenderBsTeilzeitZusammen');
		result.auslaenderBsTeilzeitZusammen = obj.auslaenderBsTeilzeitZusammen;
		if (obj.auslaenderBsTeilzeitWeiblich === undefined)
			throw new Error('invalid json format, missing attribute auslaenderBsTeilzeitWeiblich');
		result.auslaenderBsTeilzeitWeiblich = obj.auslaenderBsTeilzeitWeiblich;
		if (obj.studentenBeurlaubtZusammen === undefined)
			throw new Error('invalid json format, missing attribute studentenBeurlaubtZusammen');
		result.studentenBeurlaubtZusammen = obj.studentenBeurlaubtZusammen;
		if (obj.studentenBeurlaubtWeiblich === undefined)
			throw new Error('invalid json format, missing attribute studentenBeurlaubtWeiblich');
		result.studentenBeurlaubtWeiblich = obj.studentenBeurlaubtWeiblich;
		if (obj.foerderschwerpunktZusammen === undefined)
			throw new Error('invalid json format, missing attribute foerderschwerpunktZusammen');
		result.foerderschwerpunktZusammen = obj.foerderschwerpunktZusammen;
		if (obj.foerderschwerpunktWeiblich === undefined)
			throw new Error('invalid json format, missing attribute foerderschwerpunktWeiblich');
		result.foerderschwerpunktWeiblich = obj.foerderschwerpunktWeiblich;
		if (obj.auslaenderBsVollzeitZusammen === undefined)
			throw new Error('invalid json format, missing attribute auslaenderBsVollzeitZusammen');
		result.auslaenderBsVollzeitZusammen = obj.auslaenderBsVollzeitZusammen;
		if (obj.auslaenderBsVollzeitWeiblich === undefined)
			throw new Error('invalid json format, missing attribute auslaenderBsVollzeitWeiblich');
		result.auslaenderBsVollzeitWeiblich = obj.auslaenderBsVollzeitWeiblich;
		if (obj.zurZeitAngemeldetA12Zusammen === undefined)
			throw new Error('invalid json format, missing attribute zurZeitAngemeldetA12Zusammen');
		result.zurZeitAngemeldetA12Zusammen = obj.zurZeitAngemeldetA12Zusammen;
		if (obj.zurZeitAngemeldetA12Weiblich === undefined)
			throw new Error('invalid json format, missing attribute zurZeitAngemeldetA12Weiblich');
		result.zurZeitAngemeldetA12Weiblich = obj.zurZeitAngemeldetA12Weiblich;
		if (obj.weitereErwarteteSchuelerA12Zusammen === undefined)
			throw new Error('invalid json format, missing attribute weitereErwarteteSchuelerA12Zusammen');
		result.weitereErwarteteSchuelerA12Zusammen = obj.weitereErwarteteSchuelerA12Zusammen;
		if (obj.weitereErwarteteSchuelerA12Weiblich === undefined)
			throw new Error('invalid json format, missing attribute weitereErwarteteSchuelerA12Weiblich');
		result.weitereErwarteteSchuelerA12Weiblich = obj.weitereErwarteteSchuelerA12Weiblich;
		if (obj.zurZeitAngemeldetA13Zusammen === undefined)
			throw new Error('invalid json format, missing attribute zurZeitAngemeldetA13Zusammen');
		result.zurZeitAngemeldetA13Zusammen = obj.zurZeitAngemeldetA13Zusammen;
		if (obj.zurZeitAngemeldetA13Weiblich === undefined)
			throw new Error('invalid json format, missing attribute zurZeitAngemeldetA13Weiblich');
		result.zurZeitAngemeldetA13Weiblich = obj.zurZeitAngemeldetA13Weiblich;
		if (obj.zuErwartendeSchuelerA13Zusammen === undefined)
			throw new Error('invalid json format, missing attribute zuErwartendeSchuelerA13Zusammen');
		result.zuErwartendeSchuelerA13Zusammen = obj.zuErwartendeSchuelerA13Zusammen;
		if (obj.zuErwartendeSchuelerA13Weiblich === undefined)
			throw new Error('invalid json format, missing attribute zuErwartendeSchuelerA13Weiblich');
		result.zuErwartendeSchuelerA13Weiblich = obj.zuErwartendeSchuelerA13Weiblich;
		if (obj.weitereErwarteteSchuelerA13Zusammen === undefined)
			throw new Error('invalid json format, missing attribute weitereErwarteteSchuelerA13Zusammen');
		result.weitereErwarteteSchuelerA13Zusammen = obj.weitereErwarteteSchuelerA13Zusammen;
		if (obj.weitereErwarteteSchuelerA13Weiblich === undefined)
			throw new Error('invalid json format, missing attribute weitereErwarteteSchuelerA13Weiblich');
		result.weitereErwarteteSchuelerA13Weiblich = obj.weitereErwarteteSchuelerA13Weiblich;
		if (obj.schuelerDivers === undefined)
			throw new Error('invalid json format, missing attribute schuelerDivers');
		result.schuelerDivers = obj.schuelerDivers;
		if (obj.schuelerOhneAngabe === undefined)
			throw new Error('invalid json format, missing attribute schuelerOhneAngabe');
		result.schuelerOhneAngabe = obj.schuelerOhneAngabe;
		return result;
	}

	public static transpilerToJSON(obj: SchuelerZahlenStatistikExport): string {
		let result = '{';
		result += '"insgesamtZusammen" : ' + obj.insgesamtZusammen.toString() + ',';
		result += '"insgesamtWeiblich" : ' + obj.insgesamtWeiblich.toString() + ',';
		result += '"auslaenderZusammen" : ' + obj.auslaenderZusammen.toString() + ',';
		result += '"auslaenderWeiblich" : ' + obj.auslaenderWeiblich.toString() + ',';
		result += '"schwerstbehinderteZusammen" : ' + obj.schwerstbehinderteZusammen.toString() + ',';
		result += '"schwerstbehinderteWeiblich" : ' + obj.schwerstbehinderteWeiblich.toString() + ',';
		result += '"vollbelegerZusammen" : ' + obj.vollbelegerZusammen.toString() + ',';
		result += '"vollbelegerWeiblich" : ' + obj.vollbelegerWeiblich.toString() + ',';
		result += '"teilbelegerZusammen" : ' + obj.teilbelegerZusammen.toString() + ',';
		result += '"teilbelegerWeiblich" : ' + obj.teilbelegerWeiblich.toString() + ',';
		result += '"auslaenderBsTeilzeitZusammen" : ' + obj.auslaenderBsTeilzeitZusammen.toString() + ',';
		result += '"auslaenderBsTeilzeitWeiblich" : ' + obj.auslaenderBsTeilzeitWeiblich.toString() + ',';
		result += '"studentenBeurlaubtZusammen" : ' + obj.studentenBeurlaubtZusammen.toString() + ',';
		result += '"studentenBeurlaubtWeiblich" : ' + obj.studentenBeurlaubtWeiblich.toString() + ',';
		result += '"foerderschwerpunktZusammen" : ' + obj.foerderschwerpunktZusammen.toString() + ',';
		result += '"foerderschwerpunktWeiblich" : ' + obj.foerderschwerpunktWeiblich.toString() + ',';
		result += '"auslaenderBsVollzeitZusammen" : ' + obj.auslaenderBsVollzeitZusammen.toString() + ',';
		result += '"auslaenderBsVollzeitWeiblich" : ' + obj.auslaenderBsVollzeitWeiblich.toString() + ',';
		result += '"zurZeitAngemeldetA12Zusammen" : ' + obj.zurZeitAngemeldetA12Zusammen.toString() + ',';
		result += '"zurZeitAngemeldetA12Weiblich" : ' + obj.zurZeitAngemeldetA12Weiblich.toString() + ',';
		result += '"weitereErwarteteSchuelerA12Zusammen" : ' + obj.weitereErwarteteSchuelerA12Zusammen.toString() + ',';
		result += '"weitereErwarteteSchuelerA12Weiblich" : ' + obj.weitereErwarteteSchuelerA12Weiblich.toString() + ',';
		result += '"zurZeitAngemeldetA13Zusammen" : ' + obj.zurZeitAngemeldetA13Zusammen.toString() + ',';
		result += '"zurZeitAngemeldetA13Weiblich" : ' + obj.zurZeitAngemeldetA13Weiblich.toString() + ',';
		result += '"zuErwartendeSchuelerA13Zusammen" : ' + obj.zuErwartendeSchuelerA13Zusammen.toString() + ',';
		result += '"zuErwartendeSchuelerA13Weiblich" : ' + obj.zuErwartendeSchuelerA13Weiblich.toString() + ',';
		result += '"weitereErwarteteSchuelerA13Zusammen" : ' + obj.weitereErwarteteSchuelerA13Zusammen.toString() + ',';
		result += '"weitereErwarteteSchuelerA13Weiblich" : ' + obj.weitereErwarteteSchuelerA13Weiblich.toString() + ',';
		result += '"schuelerDivers" : ' + obj.schuelerDivers.toString() + ',';
		result += '"schuelerOhneAngabe" : ' + obj.schuelerOhneAngabe.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerZahlenStatistikExport>): string {
		let result = '{';
		if (obj.insgesamtZusammen !== undefined) {
			result += '"insgesamtZusammen" : ' + obj.insgesamtZusammen.toString() + ',';
		}
		if (obj.insgesamtWeiblich !== undefined) {
			result += '"insgesamtWeiblich" : ' + obj.insgesamtWeiblich.toString() + ',';
		}
		if (obj.auslaenderZusammen !== undefined) {
			result += '"auslaenderZusammen" : ' + obj.auslaenderZusammen.toString() + ',';
		}
		if (obj.auslaenderWeiblich !== undefined) {
			result += '"auslaenderWeiblich" : ' + obj.auslaenderWeiblich.toString() + ',';
		}
		if (obj.schwerstbehinderteZusammen !== undefined) {
			result += '"schwerstbehinderteZusammen" : ' + obj.schwerstbehinderteZusammen.toString() + ',';
		}
		if (obj.schwerstbehinderteWeiblich !== undefined) {
			result += '"schwerstbehinderteWeiblich" : ' + obj.schwerstbehinderteWeiblich.toString() + ',';
		}
		if (obj.vollbelegerZusammen !== undefined) {
			result += '"vollbelegerZusammen" : ' + obj.vollbelegerZusammen.toString() + ',';
		}
		if (obj.vollbelegerWeiblich !== undefined) {
			result += '"vollbelegerWeiblich" : ' + obj.vollbelegerWeiblich.toString() + ',';
		}
		if (obj.teilbelegerZusammen !== undefined) {
			result += '"teilbelegerZusammen" : ' + obj.teilbelegerZusammen.toString() + ',';
		}
		if (obj.teilbelegerWeiblich !== undefined) {
			result += '"teilbelegerWeiblich" : ' + obj.teilbelegerWeiblich.toString() + ',';
		}
		if (obj.auslaenderBsTeilzeitZusammen !== undefined) {
			result += '"auslaenderBsTeilzeitZusammen" : ' + obj.auslaenderBsTeilzeitZusammen.toString() + ',';
		}
		if (obj.auslaenderBsTeilzeitWeiblich !== undefined) {
			result += '"auslaenderBsTeilzeitWeiblich" : ' + obj.auslaenderBsTeilzeitWeiblich.toString() + ',';
		}
		if (obj.studentenBeurlaubtZusammen !== undefined) {
			result += '"studentenBeurlaubtZusammen" : ' + obj.studentenBeurlaubtZusammen.toString() + ',';
		}
		if (obj.studentenBeurlaubtWeiblich !== undefined) {
			result += '"studentenBeurlaubtWeiblich" : ' + obj.studentenBeurlaubtWeiblich.toString() + ',';
		}
		if (obj.foerderschwerpunktZusammen !== undefined) {
			result += '"foerderschwerpunktZusammen" : ' + obj.foerderschwerpunktZusammen.toString() + ',';
		}
		if (obj.foerderschwerpunktWeiblich !== undefined) {
			result += '"foerderschwerpunktWeiblich" : ' + obj.foerderschwerpunktWeiblich.toString() + ',';
		}
		if (obj.auslaenderBsVollzeitZusammen !== undefined) {
			result += '"auslaenderBsVollzeitZusammen" : ' + obj.auslaenderBsVollzeitZusammen.toString() + ',';
		}
		if (obj.auslaenderBsVollzeitWeiblich !== undefined) {
			result += '"auslaenderBsVollzeitWeiblich" : ' + obj.auslaenderBsVollzeitWeiblich.toString() + ',';
		}
		if (obj.zurZeitAngemeldetA12Zusammen !== undefined) {
			result += '"zurZeitAngemeldetA12Zusammen" : ' + obj.zurZeitAngemeldetA12Zusammen.toString() + ',';
		}
		if (obj.zurZeitAngemeldetA12Weiblich !== undefined) {
			result += '"zurZeitAngemeldetA12Weiblich" : ' + obj.zurZeitAngemeldetA12Weiblich.toString() + ',';
		}
		if (obj.weitereErwarteteSchuelerA12Zusammen !== undefined) {
			result += '"weitereErwarteteSchuelerA12Zusammen" : ' + obj.weitereErwarteteSchuelerA12Zusammen.toString() + ',';
		}
		if (obj.weitereErwarteteSchuelerA12Weiblich !== undefined) {
			result += '"weitereErwarteteSchuelerA12Weiblich" : ' + obj.weitereErwarteteSchuelerA12Weiblich.toString() + ',';
		}
		if (obj.zurZeitAngemeldetA13Zusammen !== undefined) {
			result += '"zurZeitAngemeldetA13Zusammen" : ' + obj.zurZeitAngemeldetA13Zusammen.toString() + ',';
		}
		if (obj.zurZeitAngemeldetA13Weiblich !== undefined) {
			result += '"zurZeitAngemeldetA13Weiblich" : ' + obj.zurZeitAngemeldetA13Weiblich.toString() + ',';
		}
		if (obj.zuErwartendeSchuelerA13Zusammen !== undefined) {
			result += '"zuErwartendeSchuelerA13Zusammen" : ' + obj.zuErwartendeSchuelerA13Zusammen.toString() + ',';
		}
		if (obj.zuErwartendeSchuelerA13Weiblich !== undefined) {
			result += '"zuErwartendeSchuelerA13Weiblich" : ' + obj.zuErwartendeSchuelerA13Weiblich.toString() + ',';
		}
		if (obj.weitereErwarteteSchuelerA13Zusammen !== undefined) {
			result += '"weitereErwarteteSchuelerA13Zusammen" : ' + obj.weitereErwarteteSchuelerA13Zusammen.toString() + ',';
		}
		if (obj.weitereErwarteteSchuelerA13Weiblich !== undefined) {
			result += '"weitereErwarteteSchuelerA13Weiblich" : ' + obj.weitereErwarteteSchuelerA13Weiblich.toString() + ',';
		}
		if (obj.schuelerDivers !== undefined) {
			result += '"schuelerDivers" : ' + obj.schuelerDivers.toString() + ',';
		}
		if (obj.schuelerOhneAngabe !== undefined) {
			result += '"schuelerOhneAngabe" : ' + obj.schuelerOhneAngabe.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_SchuelerZahlenStatistikExport(obj: unknown): SchuelerZahlenStatistikExport {
	return obj as SchuelerZahlenStatistikExport;
}
