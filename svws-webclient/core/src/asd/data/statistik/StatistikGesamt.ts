import { JavaObject } from '../../../java/lang/JavaObject';
import { OrteStatistikGesamt } from '../../../asd/data/statistik/OrteStatistikGesamt';
import { KlassenStatistikGesamt } from '../../../asd/data/statistik/KlassenStatistikGesamt';
import { SchuelerStatistikGesamt } from '../../../asd/data/statistik/SchuelerStatistikGesamt';
import { FoerderschwerpunktStatistikGesamt } from '../../../asd/data/statistik/FoerderschwerpunktStatistikGesamt';
import { ReligionStatistikGesamt } from '../../../asd/data/statistik/ReligionStatistikGesamt';
import { ArrayList } from '../../../java/util/ArrayList';
import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import { SchuleStatistikGesamt } from '../../../asd/data/statistik/SchuleStatistikGesamt';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { JahrgaengeStatistikGesamt } from '../../../asd/data/statistik/JahrgaengeStatistikGesamt';

export class StatistikGesamt extends JavaObject {

	/**
	 * Die Daten der Schule.
	 */
	public schule: SchuleStatistikGesamt = new SchuleStatistikGesamt();

	/**
	 * Die Daten der Lehrer.
	 */
	public lehrer: List<LehrerStatistikGesamt> = new ArrayList<LehrerStatistikGesamt>();

	/**
	 * Die Daten der Klassen.
	 */
	public klassen: List<KlassenStatistikGesamt> = new ArrayList<KlassenStatistikGesamt>();

	/**
	 * Die Daten der Schüler.
	 */
	public schueler: List<SchuelerStatistikGesamt> = new ArrayList<SchuelerStatistikGesamt>();

	/**
	 * Der Katalog der Jahrgänge.
	 */
	public jahrgaenge: List<JahrgaengeStatistikGesamt> = new ArrayList<JahrgaengeStatistikGesamt>();

	/**
	 * Der Katalog der Orte.
	 */
	public orte: List<OrteStatistikGesamt> = new ArrayList<OrteStatistikGesamt>();

	/**
	 * Der Katalog der Förderschwerpunkte.
	 */
	public foederschwerpunkte: List<FoerderschwerpunktStatistikGesamt> = new ArrayList<FoerderschwerpunktStatistikGesamt>();

	/**
	 * Der Katalog der Religionen.
	 */
	public religionen: List<ReligionStatistikGesamt> = new ArrayList<ReligionStatistikGesamt>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.StatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.StatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<StatistikGesamt>('de.svws_nrw.asd.data.statistik.StatistikGesamt');

	public static transpilerFromJSON(json: string): StatistikGesamt {
		const obj = JSON.parse(json) as Partial<StatistikGesamt>;
		const result = new StatistikGesamt();
		if (obj.schule === undefined)
			throw new Error('invalid json format, missing attribute schule');
		result.schule = SchuleStatistikGesamt.transpilerFromJSON(JSON.stringify(obj.schule));
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(LehrerStatistikGesamt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(KlassenStatistikGesamt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(SchuelerStatistikGesamt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.jahrgaenge !== undefined) {
			for (const elem of obj.jahrgaenge) {
				result.jahrgaenge.add(JahrgaengeStatistikGesamt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.orte !== undefined) {
			for (const elem of obj.orte) {
				result.orte.add(OrteStatistikGesamt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.foederschwerpunkte !== undefined) {
			for (const elem of obj.foederschwerpunkte) {
				result.foederschwerpunkte.add(FoerderschwerpunktStatistikGesamt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.religionen !== undefined) {
			for (const elem of obj.religionen) {
				result.religionen.add(ReligionStatistikGesamt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: StatistikGesamt): string {
		let result = '{';
		result += '"schule" : ' + SchuleStatistikGesamt.transpilerToJSON(obj.schule) + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += LehrerStatistikGesamt.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += KlassenStatistikGesamt.transpilerToJSON(elem);
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += SchuelerStatistikGesamt.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"jahrgaenge" : [ ';
		for (let i = 0; i < obj.jahrgaenge.size(); i++) {
			const elem = obj.jahrgaenge.get(i);
			result += JahrgaengeStatistikGesamt.transpilerToJSON(elem);
			if (i < obj.jahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"orte" : [ ';
		for (let i = 0; i < obj.orte.size(); i++) {
			const elem = obj.orte.get(i);
			result += OrteStatistikGesamt.transpilerToJSON(elem);
			if (i < obj.orte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"foederschwerpunkte" : [ ';
		for (let i = 0; i < obj.foederschwerpunkte.size(); i++) {
			const elem = obj.foederschwerpunkte.get(i);
			result += FoerderschwerpunktStatistikGesamt.transpilerToJSON(elem);
			if (i < obj.foederschwerpunkte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"religionen" : [ ';
		for (let i = 0; i < obj.religionen.size(); i++) {
			const elem = obj.religionen.get(i);
			result += ReligionStatistikGesamt.transpilerToJSON(elem);
			if (i < obj.religionen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<StatistikGesamt>): string {
		let result = '{';
		if (obj.schule !== undefined) {
			result += '"schule" : ' + SchuleStatistikGesamt.transpilerToJSON(obj.schule) + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += LehrerStatistikGesamt.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += KlassenStatistikGesamt.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += SchuelerStatistikGesamt.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.jahrgaenge !== undefined) {
			result += '"jahrgaenge" : [ ';
			for (let i = 0; i < obj.jahrgaenge.size(); i++) {
				const elem = obj.jahrgaenge.get(i);
				result += JahrgaengeStatistikGesamt.transpilerToJSON(elem);
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.orte !== undefined) {
			result += '"orte" : [ ';
			for (let i = 0; i < obj.orte.size(); i++) {
				const elem = obj.orte.get(i);
				result += OrteStatistikGesamt.transpilerToJSON(elem);
				if (i < obj.orte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.foederschwerpunkte !== undefined) {
			result += '"foederschwerpunkte" : [ ';
			for (let i = 0; i < obj.foederschwerpunkte.size(); i++) {
				const elem = obj.foederschwerpunkte.get(i);
				result += FoerderschwerpunktStatistikGesamt.transpilerToJSON(elem);
				if (i < obj.foederschwerpunkte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.religionen !== undefined) {
			result += '"religionen" : [ ';
			for (let i = 0; i < obj.religionen.size(); i++) {
				const elem = obj.religionen.get(i);
				result += ReligionStatistikGesamt.transpilerToJSON(elem);
				if (i < obj.religionen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_StatistikGesamt(obj: unknown): StatistikGesamt {
	return obj as StatistikGesamt;
}
