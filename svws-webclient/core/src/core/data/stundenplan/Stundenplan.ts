import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanAufsichtsbereich } from '../../../core/data/stundenplan/StundenplanAufsichtsbereich';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { StundenplanSchiene } from '../../../core/data/stundenplan/StundenplanSchiene';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { StundenplanKalenderwochenzuordnung } from '../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { StundenplanJahrgang } from '../../../core/data/stundenplan/StundenplanJahrgang';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanPausenzeit } from '../../../core/data/stundenplan/StundenplanPausenzeit';

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
	 * Das Schuljahr, in dem der Stundenplan gültig ist.
	 */
	public schuljahr : number = -1;

	/**
	 * Der Abschnitt, in dem der Stundenplan gültig ist.
	 */
	public abschnitt : number = -1;

	/**
	 * Das Datum, ab dem der Stundenplan gültig ist.
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
	 * Das Modell für die Wochen des Stundenplans, d.h. ob es sich um einen Stundenplan für jede Woche handelt (0) oder ob es sich um einen unterschiedliche Stundenpläne in Abhängigkeit des Wochentyps handelt - z.B. A-/B-Wochen (2) handelt. Hier wird dann die maximale Anzahl der unterschiedlichen Wochentypen festgelegt. Der Wert 1 ist ungültig!
	 */
	public wochenTypModell : number = 0;

	/**
	 * Das Zeitraster des Stundenplans.
	 */
	public zeitraster : List<StundenplanZeitraster> = new ArrayList<StundenplanZeitraster>();

	/**
	 * Die Liste der Räume, die für den Stundenplan zur Verfügung stehen.
	 */
	public raeume : List<StundenplanRaum> = new ArrayList<StundenplanRaum>();

	/**
	 * Die Liste der Schienen, die für den Stundenplan angelegt sind.
	 */
	public schienen : List<StundenplanSchiene> = new ArrayList<StundenplanSchiene>();

	/**
	 * Die Liste der Pausenzeiten, bei welchen Aufsichten eingeteilt werden müssen.
	 */
	public pausenzeiten : List<StundenplanPausenzeit> = new ArrayList<StundenplanPausenzeit>();

	/**
	 * Die Liste der Aufsichtsbereiche in Pausen, für welche Aufsichten eingeteilt werden müssen.
	 */
	public aufsichtsbereiche : List<StundenplanAufsichtsbereich> = new ArrayList<StundenplanAufsichtsbereich>();

	/**
	 * Die Liste der Kalenderwochen-Zuordnungen, sofern unterschiedliche Wochentypen in einer Woche genutzt werden.
	 */
	public kalenderwochenZuordnung : List<StundenplanKalenderwochenzuordnung> = new ArrayList<StundenplanKalenderwochenzuordnung>();

	/**
	 * Die Liste der Jahrgänge, die für den Stundenplan zur Verfügung stehen.
	 */
	public jahrgaenge : List<StundenplanJahrgang> = new ArrayList<StundenplanJahrgang>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.Stundenplan';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.Stundenplan'].includes(name);
	}

	public static class = new Class<Stundenplan>('de.svws_nrw.core.data.stundenplan.Stundenplan');

	public static transpilerFromJSON(json : string): Stundenplan {
		const obj = JSON.parse(json) as Partial<Stundenplan>;
		const result = new Stundenplan();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (obj.gueltigAb === undefined)
			throw new Error('invalid json format, missing attribute gueltigAb');
		result.gueltigAb = obj.gueltigAb;
		if (obj.gueltigBis === undefined)
			throw new Error('invalid json format, missing attribute gueltigBis');
		result.gueltigBis = obj.gueltigBis;
		if (obj.bezeichnungStundenplan === undefined)
			throw new Error('invalid json format, missing attribute bezeichnungStundenplan');
		result.bezeichnungStundenplan = obj.bezeichnungStundenplan;
		if (obj.wochenTypModell === undefined)
			throw new Error('invalid json format, missing attribute wochenTypModell');
		result.wochenTypModell = obj.wochenTypModell;
		if (obj.zeitraster !== undefined) {
			for (const elem of obj.zeitraster) {
				result.zeitraster.add(StundenplanZeitraster.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raeume !== undefined) {
			for (const elem of obj.raeume) {
				result.raeume.add(StundenplanRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schienen !== undefined) {
			for (const elem of obj.schienen) {
				result.schienen.add(StundenplanSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.pausenzeiten !== undefined) {
			for (const elem of obj.pausenzeiten) {
				result.pausenzeiten.add(StundenplanPausenzeit.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.aufsichtsbereiche !== undefined) {
			for (const elem of obj.aufsichtsbereiche) {
				result.aufsichtsbereiche.add(StundenplanAufsichtsbereich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kalenderwochenZuordnung !== undefined) {
			for (const elem of obj.kalenderwochenZuordnung) {
				result.kalenderwochenZuordnung.add(StundenplanKalenderwochenzuordnung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.jahrgaenge !== undefined) {
			for (const elem of obj.jahrgaenge) {
				result.jahrgaenge.add(StundenplanJahrgang.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : Stundenplan) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"gueltigAb" : ' + JSON.stringify(obj.gueltigAb) + ',';
		result += '"gueltigBis" : ' + JSON.stringify(obj.gueltigBis) + ',';
		result += '"bezeichnungStundenplan" : ' + JSON.stringify(obj.bezeichnungStundenplan) + ',';
		result += '"wochenTypModell" : ' + obj.wochenTypModell.toString() + ',';
		result += '"zeitraster" : [ ';
		for (let i = 0; i < obj.zeitraster.size(); i++) {
			const elem = obj.zeitraster.get(i);
			result += StundenplanZeitraster.transpilerToJSON(elem);
			if (i < obj.zeitraster.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raeume" : [ ';
		for (let i = 0; i < obj.raeume.size(); i++) {
			const elem = obj.raeume.get(i);
			result += StundenplanRaum.transpilerToJSON(elem);
			if (i < obj.raeume.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schienen" : [ ';
		for (let i = 0; i < obj.schienen.size(); i++) {
			const elem = obj.schienen.get(i);
			result += StundenplanSchiene.transpilerToJSON(elem);
			if (i < obj.schienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"pausenzeiten" : [ ';
		for (let i = 0; i < obj.pausenzeiten.size(); i++) {
			const elem = obj.pausenzeiten.get(i);
			result += StundenplanPausenzeit.transpilerToJSON(elem);
			if (i < obj.pausenzeiten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"aufsichtsbereiche" : [ ';
		for (let i = 0; i < obj.aufsichtsbereiche.size(); i++) {
			const elem = obj.aufsichtsbereiche.get(i);
			result += StundenplanAufsichtsbereich.transpilerToJSON(elem);
			if (i < obj.aufsichtsbereiche.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kalenderwochenZuordnung" : [ ';
		for (let i = 0; i < obj.kalenderwochenZuordnung.size(); i++) {
			const elem = obj.kalenderwochenZuordnung.get(i);
			result += StundenplanKalenderwochenzuordnung.transpilerToJSON(elem);
			if (i < obj.kalenderwochenZuordnung.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"jahrgaenge" : [ ';
		for (let i = 0; i < obj.jahrgaenge.size(); i++) {
			const elem = obj.jahrgaenge.get(i);
			result += StundenplanJahrgang.transpilerToJSON(elem);
			if (i < obj.jahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Stundenplan>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.gueltigAb !== undefined) {
			result += '"gueltigAb" : ' + JSON.stringify(obj.gueltigAb) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + JSON.stringify(obj.gueltigBis) + ',';
		}
		if (obj.bezeichnungStundenplan !== undefined) {
			result += '"bezeichnungStundenplan" : ' + JSON.stringify(obj.bezeichnungStundenplan) + ',';
		}
		if (obj.wochenTypModell !== undefined) {
			result += '"wochenTypModell" : ' + obj.wochenTypModell.toString() + ',';
		}
		if (obj.zeitraster !== undefined) {
			result += '"zeitraster" : [ ';
			for (let i = 0; i < obj.zeitraster.size(); i++) {
				const elem = obj.zeitraster.get(i);
				result += StundenplanZeitraster.transpilerToJSON(elem);
				if (i < obj.zeitraster.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raeume !== undefined) {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += StundenplanRaum.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schienen !== undefined) {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += StundenplanSchiene.transpilerToJSON(elem);
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.pausenzeiten !== undefined) {
			result += '"pausenzeiten" : [ ';
			for (let i = 0; i < obj.pausenzeiten.size(); i++) {
				const elem = obj.pausenzeiten.get(i);
				result += StundenplanPausenzeit.transpilerToJSON(elem);
				if (i < obj.pausenzeiten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.aufsichtsbereiche !== undefined) {
			result += '"aufsichtsbereiche" : [ ';
			for (let i = 0; i < obj.aufsichtsbereiche.size(); i++) {
				const elem = obj.aufsichtsbereiche.get(i);
				result += StundenplanAufsichtsbereich.transpilerToJSON(elem);
				if (i < obj.aufsichtsbereiche.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kalenderwochenZuordnung !== undefined) {
			result += '"kalenderwochenZuordnung" : [ ';
			for (let i = 0; i < obj.kalenderwochenZuordnung.size(); i++) {
				const elem = obj.kalenderwochenZuordnung.get(i);
				result += StundenplanKalenderwochenzuordnung.transpilerToJSON(elem);
				if (i < obj.kalenderwochenZuordnung.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.jahrgaenge !== undefined) {
			result += '"jahrgaenge" : [ ';
			for (let i = 0; i < obj.jahrgaenge.size(); i++) {
				const elem = obj.jahrgaenge.get(i);
				result += StundenplanJahrgang.transpilerToJSON(elem);
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_Stundenplan(obj : unknown) : Stundenplan {
	return obj as Stundenplan;
}
