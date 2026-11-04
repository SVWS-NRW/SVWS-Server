import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostSchuelerWiederholung extends JavaObject {

	/**
	 * Die ID des Schülers
	 */
	public idSchueler: number = -1;

	/**
	 * Das Abiturjahr des Jahrgangs, in dem der Schüler wiederholt, oder null, wenn keine Wiederholung besteht
	 */
	public abiturjahr: number | null = null;

	/**
	 *  Das Halbjahr der gymnasialen Oberstufe, mit dem der Schüler im Jahrgang der Wiederholung einsteigt
	 *  (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2), oder null, wenn keine Wiederholung besteht
	 */
	public gostHalbjahr: number | null = null;

	/**
	 * Das Abiturjahr, das sich aus dem aktuellen Lernabschnitt des Schülers ergibt, oder null, wenn es nicht bestimmt werden kann
	 */
	public abiturjahrLernabschnitt: number | null = null;

	/**
	 * Gibt an, ob die Schule das Einstiegshalbjahr erreicht hat und der Wechsel damit fällig ist
	 */
	public wechselFaellig: boolean = false;

	/**
	 * Gibt an, ob der aktuelle Lernabschnitt des Schülers zum Jahrgang der Wiederholung passt und der Wechsel damit vollzogen ist
	 */
	public wechselVollzogen: boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostSchuelerWiederholung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.GostSchuelerWiederholung'].includes(name);
	}

	public static readonly class = new Class<GostSchuelerWiederholung>('de.svws_nrw.core.data.gost.GostSchuelerWiederholung');

	public static transpilerFromJSON(json: string): GostSchuelerWiederholung {
		const obj = JSON.parse(json) as Partial<GostSchuelerWiederholung>;
		const result = new GostSchuelerWiederholung();
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.abiturjahr = (obj.abiturjahr === undefined) ? null : obj.abiturjahr === null ? null : obj.abiturjahr;
		result.gostHalbjahr = (obj.gostHalbjahr === undefined) ? null : obj.gostHalbjahr === null ? null : obj.gostHalbjahr;
		result.abiturjahrLernabschnitt = (obj.abiturjahrLernabschnitt === undefined) ? null : obj.abiturjahrLernabschnitt === null ? null : obj.abiturjahrLernabschnitt;
		if (obj.wechselFaellig === undefined)
			throw new Error('invalid json format, missing attribute wechselFaellig');
		result.wechselFaellig = obj.wechselFaellig;
		if (obj.wechselVollzogen === undefined)
			throw new Error('invalid json format, missing attribute wechselVollzogen');
		result.wechselVollzogen = obj.wechselVollzogen;
		return result;
	}

	public static transpilerToJSON(obj: GostSchuelerWiederholung): string {
		let result = '{';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"abiturjahr" : ' + ((obj.abiturjahr === null) ? 'null' : obj.abiturjahr.toString()) + ',';
		result += '"gostHalbjahr" : ' + ((obj.gostHalbjahr === null) ? 'null' : obj.gostHalbjahr.toString()) + ',';
		result += '"abiturjahrLernabschnitt" : ' + ((obj.abiturjahrLernabschnitt === null) ? 'null' : obj.abiturjahrLernabschnitt.toString()) + ',';
		result += '"wechselFaellig" : ' + obj.wechselFaellig.toString() + ',';
		result += '"wechselVollzogen" : ' + obj.wechselVollzogen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostSchuelerWiederholung>): string {
		let result = '{';
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.abiturjahr !== undefined) {
			result += '"abiturjahr" : ' + ((obj.abiturjahr === null) ? 'null' : obj.abiturjahr.toString()) + ',';
		}
		if (obj.gostHalbjahr !== undefined) {
			result += '"gostHalbjahr" : ' + ((obj.gostHalbjahr === null) ? 'null' : obj.gostHalbjahr.toString()) + ',';
		}
		if (obj.abiturjahrLernabschnitt !== undefined) {
			result += '"abiturjahrLernabschnitt" : ' + ((obj.abiturjahrLernabschnitt === null) ? 'null' : obj.abiturjahrLernabschnitt.toString()) + ',';
		}
		if (obj.wechselFaellig !== undefined) {
			result += '"wechselFaellig" : ' + obj.wechselFaellig.toString() + ',';
		}
		if (obj.wechselVollzogen !== undefined) {
			result += '"wechselVollzogen" : ' + obj.wechselVollzogen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostSchuelerWiederholung(obj: unknown): GostSchuelerWiederholung {
	return obj as GostSchuelerWiederholung;
}
