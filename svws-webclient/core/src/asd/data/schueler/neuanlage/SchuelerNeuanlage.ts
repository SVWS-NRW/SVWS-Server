import { JavaObject } from '../../../../java/lang/JavaObject';
import { SchuelerStammdaten } from '../../../../asd/data/schueler/SchuelerStammdaten';
import { SchuelerLernabschnittsdaten } from '../../../../asd/data/schueler/SchuelerLernabschnittsdaten';
import { SchuelerSchulbesuchsdaten } from '../../../../asd/data/schueler/SchuelerSchulbesuchsdaten';
import { Class } from '../../../../java/lang/Class';

export class SchuelerNeuanlage extends JavaObject {

	/**
	 * Die Stammdaten eines Schuelers.
	 */
	public schuelerStammdaten: SchuelerStammdaten | null = null;

	/**
	 * Die Lernabschnittsdaten eines Schuelers.
	 */
	public schuelerLernabschnittsdaten: SchuelerLernabschnittsdaten | null = null;

	/**
	 * Die SchuelerSchulbesuchsdaten eines Schuelers.
	 */
	public schuelerSchulbesuchsdaten: SchuelerSchulbesuchsdaten | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.neuanlage.SchuelerNeuanlage';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.schueler.neuanlage.SchuelerNeuanlage'].includes(name);
	}

	public static readonly class = new Class<SchuelerNeuanlage>('de.svws_nrw.asd.data.schueler.neuanlage.SchuelerNeuanlage');

	public static transpilerFromJSON(json: string): SchuelerNeuanlage {
		const obj = JSON.parse(json) as Partial<SchuelerNeuanlage>;
		const result = new SchuelerNeuanlage();
		result.schuelerStammdaten = ((obj.schuelerStammdaten === undefined) || (obj.schuelerStammdaten === null)) ? null : SchuelerStammdaten.transpilerFromJSON(JSON.stringify(obj.schuelerStammdaten));
		result.schuelerLernabschnittsdaten = ((obj.schuelerLernabschnittsdaten === undefined) || (obj.schuelerLernabschnittsdaten === null)) ? null : SchuelerLernabschnittsdaten.transpilerFromJSON(JSON.stringify(obj.schuelerLernabschnittsdaten));
		result.schuelerSchulbesuchsdaten = ((obj.schuelerSchulbesuchsdaten === undefined) || (obj.schuelerSchulbesuchsdaten === null)) ? null : SchuelerSchulbesuchsdaten.transpilerFromJSON(JSON.stringify(obj.schuelerSchulbesuchsdaten));
		return result;
	}

	public static transpilerToJSON(obj: SchuelerNeuanlage): string {
		let result = '{';
		result += '"schuelerStammdaten" : ' + ((obj.schuelerStammdaten === null) ? 'null' : SchuelerStammdaten.transpilerToJSON(obj.schuelerStammdaten)) + ',';
		result += '"schuelerLernabschnittsdaten" : ' + ((obj.schuelerLernabschnittsdaten === null) ? 'null' : SchuelerLernabschnittsdaten.transpilerToJSON(obj.schuelerLernabschnittsdaten)) + ',';
		result += '"schuelerSchulbesuchsdaten" : ' + ((obj.schuelerSchulbesuchsdaten === null) ? 'null' : SchuelerSchulbesuchsdaten.transpilerToJSON(obj.schuelerSchulbesuchsdaten)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerNeuanlage>): string {
		let result = '{';
		if (obj.schuelerStammdaten !== undefined) {
			result += '"schuelerStammdaten" : ' + ((obj.schuelerStammdaten === null) ? 'null' : SchuelerStammdaten.transpilerToJSON(obj.schuelerStammdaten)) + ',';
		}
		if (obj.schuelerLernabschnittsdaten !== undefined) {
			result += '"schuelerLernabschnittsdaten" : ' + ((obj.schuelerLernabschnittsdaten === null) ? 'null' : SchuelerLernabschnittsdaten.transpilerToJSON(obj.schuelerLernabschnittsdaten)) + ',';
		}
		if (obj.schuelerSchulbesuchsdaten !== undefined) {
			result += '"schuelerSchulbesuchsdaten" : ' + ((obj.schuelerSchulbesuchsdaten === null) ? 'null' : SchuelerSchulbesuchsdaten.transpilerToJSON(obj.schuelerSchulbesuchsdaten)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_neuanlage_SchuelerNeuanlage(obj: unknown): SchuelerNeuanlage {
	return obj as SchuelerNeuanlage;
}
