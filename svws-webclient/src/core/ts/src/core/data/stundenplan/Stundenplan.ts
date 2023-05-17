import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanKlasse } from '../../../core/data/stundenplan/StundenplanKlasse';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanKurs } from '../../../core/data/stundenplan/StundenplanKurs';
import { StundenplanJahrgang } from '../../../core/data/stundenplan/StundenplanJahrgang';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanPausenzeit } from '../../../core/data/stundenplan/StundenplanPausenzeit';
import { StundenplanSchueler } from '../../../core/data/stundenplan/StundenplanSchueler';
import { StundenplanAufsichtsbereich } from '../../../core/data/stundenplan/StundenplanAufsichtsbereich';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { StundenplanLehrer } from '../../../core/data/stundenplan/StundenplanLehrer';
import { StundenplanFach } from '../../../core/data/stundenplan/StundenplanFach';
import { List } from '../../../java/util/List';
import { StundenplanKalenderwochenzuordnung } from '../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';

export class Stundenplan extends JavaObject {

	/**
	 * Die ID des Stundenplans.
	 */
	public id : number = -1;

	/**
	 * Die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public idSchuljahresabschnitt : number = -1;

	/**
	 * Das Datum, ab dem der Stundenpland gültig ist.
	 */
	public gueltigAb : string = "";

	/**
	 * Das Datum, bis wann der Stundenplan gültig ist.
	 */
	public gueltigBis : string = "";

	/**
	 * Die textuelle Beschreibung des Stundenplans.
	 */
	public bezeichnungStundenplan : string = "";

	/**
	 * Das Modell für die Wochen des Stundenplans, d.h. ob es sich um einen Stundenplan für jede Woche handelt (0) oder ob es sich um einen unterschiedliche Stundenpläne in Abhängigkeit des Wochentyps handelt - z.B. A-/B-Wochen (2) handelt. Hier wird dann die maximale Anzahl der unterschiedlichen Wochentypen festgelegt.
	 */
	public wochenTypModell : number = 0;

	/**
	 * Das Zeitraster des Stundenplans.
	 */
	public zeitraster : List<StundenplanZeitraster> = new ArrayList();

	/**
	 * Die Liste der Räume, die für den Stundenplan zur Verfügung stehen.
	 */
	public raeume : List<StundenplanRaum> = new ArrayList();

	/**
	 * Die Liste der Pausenzeiten, bei welchen Aufsichten eingeteilt werden müssen.
	 */
	public pausenzeiten : List<StundenplanPausenzeit> = new ArrayList();

	/**
	 * Die Liste der Aufsichtsbereiche in Pausen, für welche Aufsichten eingeteilt werden müssen.
	 */
	public aufsichtsbereiche : List<StundenplanAufsichtsbereich> = new ArrayList();

	/**
	 * Die Liste der Kalenderwochen-Zuordnungen, sofern unterschiedliche Wochentypen in einer Woche genutzt werden.
	 */
	public kalenderwochenZuordnung : List<StundenplanKalenderwochenzuordnung> = new ArrayList();

	/**
	 * Die Liste der Lehrer, die für den Stundenplan zur Verfügung stehen.
	 */
	public lehrer : List<StundenplanLehrer> = new ArrayList();

	/**
	 * Die Liste der Schüler, die für den Stundenplan zur Verfügung stehen.
	 */
	public schueler : List<StundenplanSchueler> = new ArrayList();

	/**
	 * Die Liste der Fächer, die für den Stundenplan zur Verfügung stehen.
	 */
	public faecher : List<StundenplanFach> = new ArrayList();

	/**
	 * Die Liste der Jahrgänge, die für den Stundenplan zur Verfügung stehen.
	 */
	public jahrgaenge : List<StundenplanJahrgang> = new ArrayList();

	/**
	 * Die Liste der Klassen, die für den Stundenplan zur Verfügung stehen.
	 */
	public klassen : List<StundenplanKlasse> = new ArrayList();

	/**
	 * Die Liste der Kurse, die für den Stundenplan zur Verfügung stehen.
	 */
	public kurse : List<StundenplanKurs> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.Stundenplan'].includes(name);
	}

	public static transpilerFromJSON(json : string): Stundenplan {
		const obj = JSON.parse(json);
		const result = new Stundenplan();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (typeof obj.gueltigAb === "undefined")
			 throw new Error('invalid json format, missing attribute gueltigAb');
		result.gueltigAb = obj.gueltigAb;
		if (typeof obj.gueltigBis === "undefined")
			 throw new Error('invalid json format, missing attribute gueltigBis');
		result.gueltigBis = obj.gueltigBis;
		if (typeof obj.bezeichnungStundenplan === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungStundenplan');
		result.bezeichnungStundenplan = obj.bezeichnungStundenplan;
		if (typeof obj.wochenTypModell === "undefined")
			 throw new Error('invalid json format, missing attribute wochenTypModell');
		result.wochenTypModell = obj.wochenTypModell;
		if ((obj.zeitraster !== undefined) && (obj.zeitraster !== null)) {
			for (const elem of obj.zeitraster) {
				result.zeitraster?.add(StundenplanZeitraster.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.raeume !== undefined) && (obj.raeume !== null)) {
			for (const elem of obj.raeume) {
				result.raeume?.add(StundenplanRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.pausenzeiten !== undefined) && (obj.pausenzeiten !== null)) {
			for (const elem of obj.pausenzeiten) {
				result.pausenzeiten?.add(StundenplanPausenzeit.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.aufsichtsbereiche !== undefined) && (obj.aufsichtsbereiche !== null)) {
			for (const elem of obj.aufsichtsbereiche) {
				result.aufsichtsbereiche?.add(StundenplanAufsichtsbereich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.kalenderwochenZuordnung !== undefined) && (obj.kalenderwochenZuordnung !== null)) {
			for (const elem of obj.kalenderwochenZuordnung) {
				result.kalenderwochenZuordnung?.add(StundenplanKalenderwochenzuordnung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.lehrer !== undefined) && (obj.lehrer !== null)) {
			for (const elem of obj.lehrer) {
				result.lehrer?.add(StundenplanLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.schueler !== undefined) && (obj.schueler !== null)) {
			for (const elem of obj.schueler) {
				result.schueler?.add(StundenplanSchueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.faecher !== undefined) && (obj.faecher !== null)) {
			for (const elem of obj.faecher) {
				result.faecher?.add(StundenplanFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.jahrgaenge !== undefined) && (obj.jahrgaenge !== null)) {
			for (const elem of obj.jahrgaenge) {
				result.jahrgaenge?.add(StundenplanJahrgang.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.klassen !== undefined) && (obj.klassen !== null)) {
			for (const elem of obj.klassen) {
				result.klassen?.add(StundenplanKlasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.kurse !== undefined) && (obj.kurse !== null)) {
			for (const elem of obj.kurse) {
				result.kurse?.add(StundenplanKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : Stundenplan) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		result += '"gueltigAb" : ' + '"' + obj.gueltigAb! + '"' + ',';
		result += '"gueltigBis" : ' + '"' + obj.gueltigBis! + '"' + ',';
		result += '"bezeichnungStundenplan" : ' + '"' + obj.bezeichnungStundenplan! + '"' + ',';
		result += '"wochenTypModell" : ' + obj.wochenTypModell + ',';
		if (!obj.zeitraster) {
			result += '"zeitraster" : []';
		} else {
			result += '"zeitraster" : [ ';
			for (let i = 0; i < obj.zeitraster.size(); i++) {
				const elem = obj.zeitraster.get(i);
				result += StundenplanZeitraster.transpilerToJSON(elem);
				if (i < obj.zeitraster.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.raeume) {
			result += '"raeume" : []';
		} else {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += StundenplanRaum.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.pausenzeiten) {
			result += '"pausenzeiten" : []';
		} else {
			result += '"pausenzeiten" : [ ';
			for (let i = 0; i < obj.pausenzeiten.size(); i++) {
				const elem = obj.pausenzeiten.get(i);
				result += StundenplanPausenzeit.transpilerToJSON(elem);
				if (i < obj.pausenzeiten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.aufsichtsbereiche) {
			result += '"aufsichtsbereiche" : []';
		} else {
			result += '"aufsichtsbereiche" : [ ';
			for (let i = 0; i < obj.aufsichtsbereiche.size(); i++) {
				const elem = obj.aufsichtsbereiche.get(i);
				result += StundenplanAufsichtsbereich.transpilerToJSON(elem);
				if (i < obj.aufsichtsbereiche.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kalenderwochenZuordnung) {
			result += '"kalenderwochenZuordnung" : []';
		} else {
			result += '"kalenderwochenZuordnung" : [ ';
			for (let i = 0; i < obj.kalenderwochenZuordnung.size(); i++) {
				const elem = obj.kalenderwochenZuordnung.get(i);
				result += StundenplanKalenderwochenzuordnung.transpilerToJSON(elem);
				if (i < obj.kalenderwochenZuordnung.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.lehrer) {
			result += '"lehrer" : []';
		} else {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += StundenplanLehrer.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.schueler) {
			result += '"schueler" : []';
		} else {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += StundenplanSchueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.faecher) {
			result += '"faecher" : []';
		} else {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += StundenplanFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.jahrgaenge) {
			result += '"jahrgaenge" : []';
		} else {
			result += '"jahrgaenge" : [ ';
			for (let i = 0; i < obj.jahrgaenge.size(); i++) {
				const elem = obj.jahrgaenge.get(i);
				result += StundenplanJahrgang.transpilerToJSON(elem);
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.klassen) {
			result += '"klassen" : []';
		} else {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += StundenplanKlasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kurse) {
			result += '"kurse" : []';
		} else {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += StundenplanKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Stundenplan>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idSchuljahresabschnitt !== "undefined") {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (typeof obj.gueltigAb !== "undefined") {
			result += '"gueltigAb" : ' + '"' + obj.gueltigAb + '"' + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + '"' + obj.gueltigBis + '"' + ',';
		}
		if (typeof obj.bezeichnungStundenplan !== "undefined") {
			result += '"bezeichnungStundenplan" : ' + '"' + obj.bezeichnungStundenplan + '"' + ',';
		}
		if (typeof obj.wochenTypModell !== "undefined") {
			result += '"wochenTypModell" : ' + obj.wochenTypModell + ',';
		}
		if (typeof obj.zeitraster !== "undefined") {
			if (!obj.zeitraster) {
				result += '"zeitraster" : []';
			} else {
				result += '"zeitraster" : [ ';
				for (let i = 0; i < obj.zeitraster.size(); i++) {
					const elem = obj.zeitraster.get(i);
					result += StundenplanZeitraster.transpilerToJSON(elem);
					if (i < obj.zeitraster.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.raeume !== "undefined") {
			if (!obj.raeume) {
				result += '"raeume" : []';
			} else {
				result += '"raeume" : [ ';
				for (let i = 0; i < obj.raeume.size(); i++) {
					const elem = obj.raeume.get(i);
					result += StundenplanRaum.transpilerToJSON(elem);
					if (i < obj.raeume.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.pausenzeiten !== "undefined") {
			if (!obj.pausenzeiten) {
				result += '"pausenzeiten" : []';
			} else {
				result += '"pausenzeiten" : [ ';
				for (let i = 0; i < obj.pausenzeiten.size(); i++) {
					const elem = obj.pausenzeiten.get(i);
					result += StundenplanPausenzeit.transpilerToJSON(elem);
					if (i < obj.pausenzeiten.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.aufsichtsbereiche !== "undefined") {
			if (!obj.aufsichtsbereiche) {
				result += '"aufsichtsbereiche" : []';
			} else {
				result += '"aufsichtsbereiche" : [ ';
				for (let i = 0; i < obj.aufsichtsbereiche.size(); i++) {
					const elem = obj.aufsichtsbereiche.get(i);
					result += StundenplanAufsichtsbereich.transpilerToJSON(elem);
					if (i < obj.aufsichtsbereiche.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kalenderwochenZuordnung !== "undefined") {
			if (!obj.kalenderwochenZuordnung) {
				result += '"kalenderwochenZuordnung" : []';
			} else {
				result += '"kalenderwochenZuordnung" : [ ';
				for (let i = 0; i < obj.kalenderwochenZuordnung.size(); i++) {
					const elem = obj.kalenderwochenZuordnung.get(i);
					result += StundenplanKalenderwochenzuordnung.transpilerToJSON(elem);
					if (i < obj.kalenderwochenZuordnung.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.lehrer !== "undefined") {
			if (!obj.lehrer) {
				result += '"lehrer" : []';
			} else {
				result += '"lehrer" : [ ';
				for (let i = 0; i < obj.lehrer.size(); i++) {
					const elem = obj.lehrer.get(i);
					result += StundenplanLehrer.transpilerToJSON(elem);
					if (i < obj.lehrer.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.schueler !== "undefined") {
			if (!obj.schueler) {
				result += '"schueler" : []';
			} else {
				result += '"schueler" : [ ';
				for (let i = 0; i < obj.schueler.size(); i++) {
					const elem = obj.schueler.get(i);
					result += StundenplanSchueler.transpilerToJSON(elem);
					if (i < obj.schueler.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.faecher !== "undefined") {
			if (!obj.faecher) {
				result += '"faecher" : []';
			} else {
				result += '"faecher" : [ ';
				for (let i = 0; i < obj.faecher.size(); i++) {
					const elem = obj.faecher.get(i);
					result += StundenplanFach.transpilerToJSON(elem);
					if (i < obj.faecher.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.jahrgaenge !== "undefined") {
			if (!obj.jahrgaenge) {
				result += '"jahrgaenge" : []';
			} else {
				result += '"jahrgaenge" : [ ';
				for (let i = 0; i < obj.jahrgaenge.size(); i++) {
					const elem = obj.jahrgaenge.get(i);
					result += StundenplanJahrgang.transpilerToJSON(elem);
					if (i < obj.jahrgaenge.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.klassen !== "undefined") {
			if (!obj.klassen) {
				result += '"klassen" : []';
			} else {
				result += '"klassen" : [ ';
				for (let i = 0; i < obj.klassen.size(); i++) {
					const elem = obj.klassen.get(i);
					result += StundenplanKlasse.transpilerToJSON(elem);
					if (i < obj.klassen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kurse !== "undefined") {
			if (!obj.kurse) {
				result += '"kurse" : []';
			} else {
				result += '"kurse" : [ ';
				for (let i = 0; i < obj.kurse.size(); i++) {
					const elem = obj.kurse.get(i);
					result += StundenplanKurs.transpilerToJSON(elem);
					if (i < obj.kurse.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_Stundenplan(obj : unknown) : Stundenplan {
	return obj as Stundenplan;
}
