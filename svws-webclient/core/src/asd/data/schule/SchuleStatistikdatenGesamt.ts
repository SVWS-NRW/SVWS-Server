import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { SchuleStammdaten } from '../../../asd/data/schule/SchuleStammdaten';
import { SchuelerLernabschnittsdaten } from '../../../asd/data/schueler/SchuelerLernabschnittsdaten';
import { SchuelerSchulbesuchsdaten } from '../../../asd/data/schueler/SchuelerSchulbesuchsdaten';
import { ArrayList } from '../../../java/util/ArrayList';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { SchuelerBetriebsdaten } from '../../../asd/data/schueler/SchuelerBetriebsdaten';
import { Sprachendaten } from '../../../asd/data/schueler/Sprachendaten';

export class SchuleStatistikdatenGesamt extends JavaObject {

	/**
	 * Die Stammdaten der Schule
	 */
	public stammdaten : SchuleStammdaten = new SchuleStammdaten();

	/**
	 * Die Stammdaten der Lehrer.
	 */
	public lehrerStammdaten : List<LehrerStammdaten> = new ArrayList<LehrerStammdaten>();

	/**
	 * Die Personaldaten der Lehrer.
	 */
	public lehrerPersonaldaten : List<LehrerPersonaldaten> = new ArrayList<LehrerPersonaldaten>();

	/**
	 * Die allgemeinen Angaben zu dem Lernabschnitt der Schüler.
	 */
	public schuelerLernabschnittsdaten : List<SchuelerLernabschnittsdaten> = new ArrayList<SchuelerLernabschnittsdaten>();

	/**
	 * Die Schulbesuchsdaten der Schüler.
	 */
	public schuelerSchulbesuchsdaten : List<SchuelerSchulbesuchsdaten> = new ArrayList<SchuelerSchulbesuchsdaten>();

	/**
	 * Die Betriebsdaten der Schüler in einem Betrieb.
	 */
	public schuelerBetriebsdaten : List<SchuelerBetriebsdaten> = new ArrayList<SchuelerBetriebsdaten>();

	/**
	 * Die Informationen zu den Sprachbelegungen und den Sprachprüfungen der Schüler.
	 */
	public schuelerSprachendaten : List<Sprachendaten> = new ArrayList<Sprachendaten>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt'].includes(name);
	}

	public static class = new Class<SchuleStatistikdatenGesamt>('de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt');

	public static transpilerFromJSON(json : string): SchuleStatistikdatenGesamt {
		const obj = JSON.parse(json) as Partial<SchuleStatistikdatenGesamt>;
		const result = new SchuleStatistikdatenGesamt();
		if (obj.stammdaten === undefined)
			throw new Error('invalid json format, missing attribute stammdaten');
		result.stammdaten = SchuleStammdaten.transpilerFromJSON(JSON.stringify(obj.stammdaten));
		if (obj.lehrerStammdaten !== undefined) {
			for (const elem of obj.lehrerStammdaten) {
				result.lehrerStammdaten.add(LehrerStammdaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrerPersonaldaten !== undefined) {
			for (const elem of obj.lehrerPersonaldaten) {
				result.lehrerPersonaldaten.add(LehrerPersonaldaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelerLernabschnittsdaten !== undefined) {
			for (const elem of obj.schuelerLernabschnittsdaten) {
				result.schuelerLernabschnittsdaten.add(SchuelerLernabschnittsdaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelerSchulbesuchsdaten !== undefined) {
			for (const elem of obj.schuelerSchulbesuchsdaten) {
				result.schuelerSchulbesuchsdaten.add(SchuelerSchulbesuchsdaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelerBetriebsdaten !== undefined) {
			for (const elem of obj.schuelerBetriebsdaten) {
				result.schuelerBetriebsdaten.add(SchuelerBetriebsdaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelerSprachendaten !== undefined) {
			for (const elem of obj.schuelerSprachendaten) {
				result.schuelerSprachendaten.add(Sprachendaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuleStatistikdatenGesamt) : string {
		let result = '{';
		result += '"stammdaten" : ' + SchuleStammdaten.transpilerToJSON(obj.stammdaten) + ',';
		result += '"lehrerStammdaten" : [ ';
		for (let i = 0; i < obj.lehrerStammdaten.size(); i++) {
			const elem = obj.lehrerStammdaten.get(i);
			result += LehrerStammdaten.transpilerToJSON(elem);
			if (i < obj.lehrerStammdaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrerPersonaldaten" : [ ';
		for (let i = 0; i < obj.lehrerPersonaldaten.size(); i++) {
			const elem = obj.lehrerPersonaldaten.get(i);
			result += LehrerPersonaldaten.transpilerToJSON(elem);
			if (i < obj.lehrerPersonaldaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelerLernabschnittsdaten" : [ ';
		for (let i = 0; i < obj.schuelerLernabschnittsdaten.size(); i++) {
			const elem = obj.schuelerLernabschnittsdaten.get(i);
			result += SchuelerLernabschnittsdaten.transpilerToJSON(elem);
			if (i < obj.schuelerLernabschnittsdaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelerSchulbesuchsdaten" : [ ';
		for (let i = 0; i < obj.schuelerSchulbesuchsdaten.size(); i++) {
			const elem = obj.schuelerSchulbesuchsdaten.get(i);
			result += SchuelerSchulbesuchsdaten.transpilerToJSON(elem);
			if (i < obj.schuelerSchulbesuchsdaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelerBetriebsdaten" : [ ';
		for (let i = 0; i < obj.schuelerBetriebsdaten.size(); i++) {
			const elem = obj.schuelerBetriebsdaten.get(i);
			result += SchuelerBetriebsdaten.transpilerToJSON(elem);
			if (i < obj.schuelerBetriebsdaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelerSprachendaten" : [ ';
		for (let i = 0; i < obj.schuelerSprachendaten.size(); i++) {
			const elem = obj.schuelerSprachendaten.get(i);
			result += Sprachendaten.transpilerToJSON(elem);
			if (i < obj.schuelerSprachendaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuleStatistikdatenGesamt>) : string {
		let result = '{';
		if (obj.stammdaten !== undefined) {
			result += '"stammdaten" : ' + SchuleStammdaten.transpilerToJSON(obj.stammdaten) + ',';
		}
		if (obj.lehrerStammdaten !== undefined) {
			result += '"lehrerStammdaten" : [ ';
			for (let i = 0; i < obj.lehrerStammdaten.size(); i++) {
				const elem = obj.lehrerStammdaten.get(i);
				result += LehrerStammdaten.transpilerToJSON(elem);
				if (i < obj.lehrerStammdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrerPersonaldaten !== undefined) {
			result += '"lehrerPersonaldaten" : [ ';
			for (let i = 0; i < obj.lehrerPersonaldaten.size(); i++) {
				const elem = obj.lehrerPersonaldaten.get(i);
				result += LehrerPersonaldaten.transpilerToJSON(elem);
				if (i < obj.lehrerPersonaldaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelerLernabschnittsdaten !== undefined) {
			result += '"schuelerLernabschnittsdaten" : [ ';
			for (let i = 0; i < obj.schuelerLernabschnittsdaten.size(); i++) {
				const elem = obj.schuelerLernabschnittsdaten.get(i);
				result += SchuelerLernabschnittsdaten.transpilerToJSON(elem);
				if (i < obj.schuelerLernabschnittsdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelerSchulbesuchsdaten !== undefined) {
			result += '"schuelerSchulbesuchsdaten" : [ ';
			for (let i = 0; i < obj.schuelerSchulbesuchsdaten.size(); i++) {
				const elem = obj.schuelerSchulbesuchsdaten.get(i);
				result += SchuelerSchulbesuchsdaten.transpilerToJSON(elem);
				if (i < obj.schuelerSchulbesuchsdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelerBetriebsdaten !== undefined) {
			result += '"schuelerBetriebsdaten" : [ ';
			for (let i = 0; i < obj.schuelerBetriebsdaten.size(); i++) {
				const elem = obj.schuelerBetriebsdaten.get(i);
				result += SchuelerBetriebsdaten.transpilerToJSON(elem);
				if (i < obj.schuelerBetriebsdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelerSprachendaten !== undefined) {
			result += '"schuelerSprachendaten" : [ ';
			for (let i = 0; i < obj.schuelerSprachendaten.size(); i++) {
				const elem = obj.schuelerSprachendaten.get(i);
				result += Sprachendaten.transpilerToJSON(elem);
				if (i < obj.schuelerSprachendaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_SchuleStatistikdatenGesamt(obj : unknown) : SchuleStatistikdatenGesamt {
	return obj as SchuleStatistikdatenGesamt;
}
