import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerFoerderempfehlung extends JavaObject {

	/**
	 * Die GUID der Förderempfehlung
	 */
	public guid: string | null = "";

	/**
	 * Die ID des Schülerlernabschnittes, welchem die Förderempfehlung zugeordnet ist
	 */
	public idLernabschnitt: number | null = null;

	/**
	 * Die ID der Klasse auf welche sich die Förderempfehlung bezieht
	 */
	public idKlasse: number | null = null;

	/**
	 * Die ID des Lehrers, welcher die Förderempfehlung erstellt hat.
	 */
	public idLehrer: number | null = null;

	/**
	 * Das Datum, wann die Förderempfehlung angelegt wurde
	 */
	public datumAngelegt: string | null = "";

	/**
	 * Das Datum, wann die Förderempfehlung zuletzt angepasst wurde
	 */
	public datumLetzteAenderung: string | null = "";

	/**
	 * Die diagnostizierten Förderbedarfe im Bereich der Prozessbezogenen Kompetenzen
	 */
	public diagnoseKompetenzenInhaltlichProzessbezogen: string | null = "";

	/**
	 * Die diagnostizierten Förderbedarfe im Bereich der methodische Kompetenzen
	 */
	public diagnoseKompetenzenMethodisch: string | null = "";

	/**
	 * Die diagnostizierten Förderbedarfe im Bereich in Bezug auf das Lern- und Arbeitsverhalten
	 */
	public diagnoseLernUndArbeitsverhalten: string | null = "";

	/**
	 * Die geplanten Maßnahmen in Bezug auf die Förderbedarfe im Bereich der Prozessbezogenen Kompetenzen
	 */
	public massnahmeKompetenzenInhaltlichProzessbezogen: string | null = "";

	/**
	 * Die geplanten Maßnahmen in Bezug auf die Förderbedarfe im Bereich der methodische Kompetenzen
	 */
	public massnahmeKompetenzenMethodische: string | null = "";

	/**
	 * Die geplanten Maßnahmen in Bezug auf die Förderbedarfe in Bezug auf das Lern- und Arbeitsverhalten
	 */
	public massnahmeLernArbeitsverhalten: string | null = "";

	/**
	 * Die Verantwortlichkeiten der Eltern in Bezug auf die Förderempfehlung
	 */
	public verantwortlichkeitEltern: string | null = "";

	/**
	 * Die Verantwortlichkeiten des Schülers in Bezug auf die Förderempfehlung
	 */
	public verantwortlichkeitSchueler: string | null = "";

	/**
	 * Das Startdatum des Zeitrahmens für die Umsetzung der Förderempfehlung.
	 */
	public datumUmsetzungVon: string | null = "";

	/**
	 * Das Enddatum des Zeitrahmens für die Umsetzung der Förderempfehlung
	 */
	public datumUmsetzungBis: string | null = "";

	/**
	 * Das Datum, wann die Erfolge bei der Umsetzung der Förderempfehlung überprüft werden sollen
	 */
	public datumUeberpruefung: string | null = "";

	/**
	 * Das Datum für das nächste geplante Beratungsgespräch in Bezug auf die Förderung
	 */
	public datumNaechstesBeratungsgespraech: string | null = "";

	/**
	 * Gibt an, ob die Eingabe der Förderempfehlung abgeschlossen wurde oder nicht
	 */
	public eingabeFertig: boolean = false;

	/**
	 * Die Kürzel der Fächer, auf welche sich die Förderempfehlung bezieht, als komma-separierter String
	 */
	public faecher: string | null = "";

	/**
	 * Gibt an, ob die Umsetzung der Förderempfehlung beendet wurde oder nicht
	 */
	public abgeschlossen: boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerFoerderempfehlung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerFoerderempfehlung'].includes(name);
	}

	public static readonly class = new Class<SchuelerFoerderempfehlung>('de.svws_nrw.asd.data.schueler.SchuelerFoerderempfehlung');

	public static transpilerFromJSON(json: string): SchuelerFoerderempfehlung {
		const obj = JSON.parse(json) as Partial<SchuelerFoerderempfehlung>;
		const result = new SchuelerFoerderempfehlung();
		result.guid = (obj.guid === undefined) ? null : obj.guid === null ? null : obj.guid;
		result.idLernabschnitt = (obj.idLernabschnitt === undefined) ? null : obj.idLernabschnitt === null ? null : obj.idLernabschnitt;
		result.idKlasse = (obj.idKlasse === undefined) ? null : obj.idKlasse === null ? null : obj.idKlasse;
		result.idLehrer = (obj.idLehrer === undefined) ? null : obj.idLehrer === null ? null : obj.idLehrer;
		result.datumAngelegt = (obj.datumAngelegt === undefined) ? null : obj.datumAngelegt === null ? null : obj.datumAngelegt;
		result.datumLetzteAenderung = (obj.datumLetzteAenderung === undefined) ? null : obj.datumLetzteAenderung === null ? null : obj.datumLetzteAenderung;
		result.diagnoseKompetenzenInhaltlichProzessbezogen = (obj.diagnoseKompetenzenInhaltlichProzessbezogen === undefined) ? null : obj.diagnoseKompetenzenInhaltlichProzessbezogen === null ? null : obj.diagnoseKompetenzenInhaltlichProzessbezogen;
		result.diagnoseKompetenzenMethodisch = (obj.diagnoseKompetenzenMethodisch === undefined) ? null : obj.diagnoseKompetenzenMethodisch === null ? null : obj.diagnoseKompetenzenMethodisch;
		result.diagnoseLernUndArbeitsverhalten = (obj.diagnoseLernUndArbeitsverhalten === undefined) ? null : obj.diagnoseLernUndArbeitsverhalten === null ? null : obj.diagnoseLernUndArbeitsverhalten;
		result.massnahmeKompetenzenInhaltlichProzessbezogen = (obj.massnahmeKompetenzenInhaltlichProzessbezogen === undefined) ? null : obj.massnahmeKompetenzenInhaltlichProzessbezogen === null ? null : obj.massnahmeKompetenzenInhaltlichProzessbezogen;
		result.massnahmeKompetenzenMethodische = (obj.massnahmeKompetenzenMethodische === undefined) ? null : obj.massnahmeKompetenzenMethodische === null ? null : obj.massnahmeKompetenzenMethodische;
		result.massnahmeLernArbeitsverhalten = (obj.massnahmeLernArbeitsverhalten === undefined) ? null : obj.massnahmeLernArbeitsverhalten === null ? null : obj.massnahmeLernArbeitsverhalten;
		result.verantwortlichkeitEltern = (obj.verantwortlichkeitEltern === undefined) ? null : obj.verantwortlichkeitEltern === null ? null : obj.verantwortlichkeitEltern;
		result.verantwortlichkeitSchueler = (obj.verantwortlichkeitSchueler === undefined) ? null : obj.verantwortlichkeitSchueler === null ? null : obj.verantwortlichkeitSchueler;
		result.datumUmsetzungVon = (obj.datumUmsetzungVon === undefined) ? null : obj.datumUmsetzungVon === null ? null : obj.datumUmsetzungVon;
		result.datumUmsetzungBis = (obj.datumUmsetzungBis === undefined) ? null : obj.datumUmsetzungBis === null ? null : obj.datumUmsetzungBis;
		result.datumUeberpruefung = (obj.datumUeberpruefung === undefined) ? null : obj.datumUeberpruefung === null ? null : obj.datumUeberpruefung;
		result.datumNaechstesBeratungsgespraech = (obj.datumNaechstesBeratungsgespraech === undefined) ? null : obj.datumNaechstesBeratungsgespraech === null ? null : obj.datumNaechstesBeratungsgespraech;
		if (obj.eingabeFertig === undefined)
			throw new Error('invalid json format, missing attribute eingabeFertig');
		result.eingabeFertig = obj.eingabeFertig;
		result.faecher = (obj.faecher === undefined) ? null : obj.faecher === null ? null : obj.faecher;
		if (obj.abgeschlossen === undefined)
			throw new Error('invalid json format, missing attribute abgeschlossen');
		result.abgeschlossen = obj.abgeschlossen;
		return result;
	}

	public static transpilerToJSON(obj: SchuelerFoerderempfehlung): string {
		let result = '{';
		result += '"guid" : ' + ((obj.guid === null) ? 'null' : JSON.stringify(obj.guid)) + ',';
		result += '"idLernabschnitt" : ' + ((obj.idLernabschnitt === null) ? 'null' : obj.idLernabschnitt.toString()) + ',';
		result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		result += '"idLehrer" : ' + ((obj.idLehrer === null) ? 'null' : obj.idLehrer.toString()) + ',';
		result += '"datumAngelegt" : ' + ((obj.datumAngelegt === null) ? 'null' : JSON.stringify(obj.datumAngelegt)) + ',';
		result += '"datumLetzteAenderung" : ' + ((obj.datumLetzteAenderung === null) ? 'null' : JSON.stringify(obj.datumLetzteAenderung)) + ',';
		result += '"diagnoseKompetenzenInhaltlichProzessbezogen" : ' + ((obj.diagnoseKompetenzenInhaltlichProzessbezogen === null) ? 'null' : JSON.stringify(obj.diagnoseKompetenzenInhaltlichProzessbezogen)) + ',';
		result += '"diagnoseKompetenzenMethodisch" : ' + ((obj.diagnoseKompetenzenMethodisch === null) ? 'null' : JSON.stringify(obj.diagnoseKompetenzenMethodisch)) + ',';
		result += '"diagnoseLernUndArbeitsverhalten" : ' + ((obj.diagnoseLernUndArbeitsverhalten === null) ? 'null' : JSON.stringify(obj.diagnoseLernUndArbeitsverhalten)) + ',';
		result += '"massnahmeKompetenzenInhaltlichProzessbezogen" : ' + ((obj.massnahmeKompetenzenInhaltlichProzessbezogen === null) ? 'null' : JSON.stringify(obj.massnahmeKompetenzenInhaltlichProzessbezogen)) + ',';
		result += '"massnahmeKompetenzenMethodische" : ' + ((obj.massnahmeKompetenzenMethodische === null) ? 'null' : JSON.stringify(obj.massnahmeKompetenzenMethodische)) + ',';
		result += '"massnahmeLernArbeitsverhalten" : ' + ((obj.massnahmeLernArbeitsverhalten === null) ? 'null' : JSON.stringify(obj.massnahmeLernArbeitsverhalten)) + ',';
		result += '"verantwortlichkeitEltern" : ' + ((obj.verantwortlichkeitEltern === null) ? 'null' : JSON.stringify(obj.verantwortlichkeitEltern)) + ',';
		result += '"verantwortlichkeitSchueler" : ' + ((obj.verantwortlichkeitSchueler === null) ? 'null' : JSON.stringify(obj.verantwortlichkeitSchueler)) + ',';
		result += '"datumUmsetzungVon" : ' + ((obj.datumUmsetzungVon === null) ? 'null' : JSON.stringify(obj.datumUmsetzungVon)) + ',';
		result += '"datumUmsetzungBis" : ' + ((obj.datumUmsetzungBis === null) ? 'null' : JSON.stringify(obj.datumUmsetzungBis)) + ',';
		result += '"datumUeberpruefung" : ' + ((obj.datumUeberpruefung === null) ? 'null' : JSON.stringify(obj.datumUeberpruefung)) + ',';
		result += '"datumNaechstesBeratungsgespraech" : ' + ((obj.datumNaechstesBeratungsgespraech === null) ? 'null' : JSON.stringify(obj.datumNaechstesBeratungsgespraech)) + ',';
		result += '"eingabeFertig" : ' + obj.eingabeFertig.toString() + ',';
		result += '"faecher" : ' + ((obj.faecher === null) ? 'null' : JSON.stringify(obj.faecher)) + ',';
		result += '"abgeschlossen" : ' + obj.abgeschlossen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerFoerderempfehlung>): string {
		let result = '{';
		if (obj.guid !== undefined) {
			result += '"guid" : ' + ((obj.guid === null) ? 'null' : JSON.stringify(obj.guid)) + ',';
		}
		if (obj.idLernabschnitt !== undefined) {
			result += '"idLernabschnitt" : ' + ((obj.idLernabschnitt === null) ? 'null' : obj.idLernabschnitt.toString()) + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + ((obj.idLehrer === null) ? 'null' : obj.idLehrer.toString()) + ',';
		}
		if (obj.datumAngelegt !== undefined) {
			result += '"datumAngelegt" : ' + ((obj.datumAngelegt === null) ? 'null' : JSON.stringify(obj.datumAngelegt)) + ',';
		}
		if (obj.datumLetzteAenderung !== undefined) {
			result += '"datumLetzteAenderung" : ' + ((obj.datumLetzteAenderung === null) ? 'null' : JSON.stringify(obj.datumLetzteAenderung)) + ',';
		}
		if (obj.diagnoseKompetenzenInhaltlichProzessbezogen !== undefined) {
			result += '"diagnoseKompetenzenInhaltlichProzessbezogen" : ' + ((obj.diagnoseKompetenzenInhaltlichProzessbezogen === null) ? 'null' : JSON.stringify(obj.diagnoseKompetenzenInhaltlichProzessbezogen)) + ',';
		}
		if (obj.diagnoseKompetenzenMethodisch !== undefined) {
			result += '"diagnoseKompetenzenMethodisch" : ' + ((obj.diagnoseKompetenzenMethodisch === null) ? 'null' : JSON.stringify(obj.diagnoseKompetenzenMethodisch)) + ',';
		}
		if (obj.diagnoseLernUndArbeitsverhalten !== undefined) {
			result += '"diagnoseLernUndArbeitsverhalten" : ' + ((obj.diagnoseLernUndArbeitsverhalten === null) ? 'null' : JSON.stringify(obj.diagnoseLernUndArbeitsverhalten)) + ',';
		}
		if (obj.massnahmeKompetenzenInhaltlichProzessbezogen !== undefined) {
			result += '"massnahmeKompetenzenInhaltlichProzessbezogen" : ' + ((obj.massnahmeKompetenzenInhaltlichProzessbezogen === null) ? 'null' : JSON.stringify(obj.massnahmeKompetenzenInhaltlichProzessbezogen)) + ',';
		}
		if (obj.massnahmeKompetenzenMethodische !== undefined) {
			result += '"massnahmeKompetenzenMethodische" : ' + ((obj.massnahmeKompetenzenMethodische === null) ? 'null' : JSON.stringify(obj.massnahmeKompetenzenMethodische)) + ',';
		}
		if (obj.massnahmeLernArbeitsverhalten !== undefined) {
			result += '"massnahmeLernArbeitsverhalten" : ' + ((obj.massnahmeLernArbeitsverhalten === null) ? 'null' : JSON.stringify(obj.massnahmeLernArbeitsverhalten)) + ',';
		}
		if (obj.verantwortlichkeitEltern !== undefined) {
			result += '"verantwortlichkeitEltern" : ' + ((obj.verantwortlichkeitEltern === null) ? 'null' : JSON.stringify(obj.verantwortlichkeitEltern)) + ',';
		}
		if (obj.verantwortlichkeitSchueler !== undefined) {
			result += '"verantwortlichkeitSchueler" : ' + ((obj.verantwortlichkeitSchueler === null) ? 'null' : JSON.stringify(obj.verantwortlichkeitSchueler)) + ',';
		}
		if (obj.datumUmsetzungVon !== undefined) {
			result += '"datumUmsetzungVon" : ' + ((obj.datumUmsetzungVon === null) ? 'null' : JSON.stringify(obj.datumUmsetzungVon)) + ',';
		}
		if (obj.datumUmsetzungBis !== undefined) {
			result += '"datumUmsetzungBis" : ' + ((obj.datumUmsetzungBis === null) ? 'null' : JSON.stringify(obj.datumUmsetzungBis)) + ',';
		}
		if (obj.datumUeberpruefung !== undefined) {
			result += '"datumUeberpruefung" : ' + ((obj.datumUeberpruefung === null) ? 'null' : JSON.stringify(obj.datumUeberpruefung)) + ',';
		}
		if (obj.datumNaechstesBeratungsgespraech !== undefined) {
			result += '"datumNaechstesBeratungsgespraech" : ' + ((obj.datumNaechstesBeratungsgespraech === null) ? 'null' : JSON.stringify(obj.datumNaechstesBeratungsgespraech)) + ',';
		}
		if (obj.eingabeFertig !== undefined) {
			result += '"eingabeFertig" : ' + obj.eingabeFertig.toString() + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : ' + ((obj.faecher === null) ? 'null' : JSON.stringify(obj.faecher)) + ',';
		}
		if (obj.abgeschlossen !== undefined) {
			result += '"abgeschlossen" : ' + obj.abgeschlossen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerFoerderempfehlung(obj: unknown): SchuelerFoerderempfehlung {
	return obj as SchuelerFoerderempfehlung;
}
