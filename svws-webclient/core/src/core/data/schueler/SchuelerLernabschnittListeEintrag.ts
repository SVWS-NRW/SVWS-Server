import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerLernabschnittListeEintrag extends JavaObject {

	/**
	 * Die ID des Lernabschnitts in der Datenbank.
	 */
	public id : number = 0;

	/**
	 * Die ID des Schülers, zu dem diese Lernabschnittdaten gehören.
	 */
	public schuelerID : number = 0;

	/**
	 * Die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören.
	 */
	public schuljahresabschnitt : number = 0;

	/**
	 * Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 */
	public schuljahr : number = 0;

	/**
	 * Die Nummer des Abschnitts im Schuljahr
	 */
	public abschnitt : number = 0;

	/**
	 * Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.).
	 */
	public wechselNr : number = 0;

	/**
	 * Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht
	 */
	public istGewertet : boolean = true;

	/**
	 * Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht
	 */
	public istWiederholung : boolean = false;

	/**
	 * Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.
	 */
	public pruefungsOrdnung : string = "";

	/**
	 * Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist.
	 */
	public klassenID : number | null = null;

	/**
	 * Die Bezeichnung der Klasse des Schülers
	 */
	public klasse : string = "";

	/**
	 * Die Statistik-Bezeichnung der Klasse des Schülers
	 */
	public klasseStatistik : string = "";

	/**
	 * Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist
	 */
	public jahrgangID : number | null = null;

	/**
	 * Die Statistik-Bezeichnung des Jahrgangs des Schülers
	 */
	public jahrgang : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerLernabschnittListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerLernabschnittListeEintrag'].includes(name);
	}

	public static class = new Class<SchuelerLernabschnittListeEintrag>('de.svws_nrw.core.data.schueler.SchuelerLernabschnittListeEintrag');

	public static transpilerFromJSON(json : string): SchuelerLernabschnittListeEintrag {
		const obj = JSON.parse(json) as Partial<SchuelerLernabschnittListeEintrag>;
		const result = new SchuelerLernabschnittListeEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schuelerID === undefined)
			throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (obj.schuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute schuljahresabschnitt');
		result.schuljahresabschnitt = obj.schuljahresabschnitt;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (obj.wechselNr === undefined)
			throw new Error('invalid json format, missing attribute wechselNr');
		result.wechselNr = obj.wechselNr;
		if (obj.istGewertet === undefined)
			throw new Error('invalid json format, missing attribute istGewertet');
		result.istGewertet = obj.istGewertet;
		if (obj.istWiederholung === undefined)
			throw new Error('invalid json format, missing attribute istWiederholung');
		result.istWiederholung = obj.istWiederholung;
		if (obj.pruefungsOrdnung === undefined)
			throw new Error('invalid json format, missing attribute pruefungsOrdnung');
		result.pruefungsOrdnung = obj.pruefungsOrdnung;
		result.klassenID = (obj.klassenID === undefined) ? null : obj.klassenID === null ? null : obj.klassenID;
		if (obj.klasse === undefined)
			throw new Error('invalid json format, missing attribute klasse');
		result.klasse = obj.klasse;
		if (obj.klasseStatistik === undefined)
			throw new Error('invalid json format, missing attribute klasseStatistik');
		result.klasseStatistik = obj.klasseStatistik;
		result.jahrgangID = (obj.jahrgangID === undefined) ? null : obj.jahrgangID === null ? null : obj.jahrgangID;
		if (obj.jahrgang === undefined)
			throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernabschnittListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"wechselNr" : ' + obj.wechselNr.toString() + ',';
		result += '"istGewertet" : ' + obj.istGewertet.toString() + ',';
		result += '"istWiederholung" : ' + obj.istWiederholung.toString() + ',';
		result += '"pruefungsOrdnung" : ' + JSON.stringify(obj.pruefungsOrdnung) + ',';
		result += '"klassenID" : ' + ((obj.klassenID === null) ? 'null' : obj.klassenID.toString()) + ',';
		result += '"klasse" : ' + JSON.stringify(obj.klasse) + ',';
		result += '"klasseStatistik" : ' + JSON.stringify(obj.klasseStatistik) + ',';
		result += '"jahrgangID" : ' + ((obj.jahrgangID === null) ? 'null' : obj.jahrgangID.toString()) + ',';
		result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernabschnittListeEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schuelerID !== undefined) {
			result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		}
		if (obj.schuljahresabschnitt !== undefined) {
			result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.wechselNr !== undefined) {
			result += '"wechselNr" : ' + obj.wechselNr.toString() + ',';
		}
		if (obj.istGewertet !== undefined) {
			result += '"istGewertet" : ' + obj.istGewertet.toString() + ',';
		}
		if (obj.istWiederholung !== undefined) {
			result += '"istWiederholung" : ' + obj.istWiederholung.toString() + ',';
		}
		if (obj.pruefungsOrdnung !== undefined) {
			result += '"pruefungsOrdnung" : ' + JSON.stringify(obj.pruefungsOrdnung) + ',';
		}
		if (obj.klassenID !== undefined) {
			result += '"klassenID" : ' + ((obj.klassenID === null) ? 'null' : obj.klassenID.toString()) + ',';
		}
		if (obj.klasse !== undefined) {
			result += '"klasse" : ' + JSON.stringify(obj.klasse) + ',';
		}
		if (obj.klasseStatistik !== undefined) {
			result += '"klasseStatistik" : ' + JSON.stringify(obj.klasseStatistik) + ',';
		}
		if (obj.jahrgangID !== undefined) {
			result += '"jahrgangID" : ' + ((obj.jahrgangID === null) ? 'null' : obj.jahrgangID.toString()) + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerLernabschnittListeEintrag(obj : unknown) : SchuelerLernabschnittListeEintrag {
	return obj as SchuelerLernabschnittListeEintrag;
}
