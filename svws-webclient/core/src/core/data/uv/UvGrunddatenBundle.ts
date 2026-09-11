import { JavaObject } from '../../../java/lang/JavaObject';
import { UvLehrer } from '../../../core/data/uv/UvLehrer';
import { LehrerUnterrichtsfach } from '../../../core/data/lehrer/LehrerUnterrichtsfach';
import { UvStundentafel } from '../../../core/data/uv/UvStundentafel';
import { UvLehrerPflichtstundensoll } from '../../../core/data/uv/UvLehrerPflichtstundensoll';
import { ArrayList } from '../../../java/util/ArrayList';
import { UvRaumgruppe } from '../../../core/data/uv/UvRaumgruppe';
import { UvStundentafelFach } from '../../../core/data/uv/UvStundentafelFach';
import { UvFach } from '../../../core/data/uv/UvFach';
import { UvZeitraster } from '../../../core/data/uv/UvZeitraster';
import { UvLehrerAnrechnungsstunden } from '../../../core/data/uv/UvLehrerAnrechnungsstunden';
import { UvRaum } from '../../../core/data/uv/UvRaum';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { UvZeitrasterEintrag } from '../../../core/data/uv/UvZeitrasterEintrag';

export class UvGrunddatenBundle extends JavaObject {

	/**
	 * Ein Array mit
	 */
	public lehrer: List<UvLehrer> = new ArrayList<UvLehrer>();

	/**
	 * Ein Array mit den Unterrichtsfächern der Lehrer
	 */
	public lehrerUnterrichtsfaecher: List<LehrerUnterrichtsfach> = new ArrayList<LehrerUnterrichtsfach>();

	/**
	 * Ein Array mit
	 */
	public raeume: List<UvRaum> = new ArrayList<UvRaum>();

	/**
	 * Ein Array mit
	 */
	public raumgruppen: List<UvRaumgruppe> = new ArrayList<UvRaumgruppe>();

	/**
	 * Ein Array mit
	 */
	public stundentafeln: List<UvStundentafel> = new ArrayList<UvStundentafel>();

	/**
	 * Ein Array mit
	 */
	public stundentafelfaecher: List<UvStundentafelFach> = new ArrayList<UvStundentafelFach>();

	/**
	 * Ein Array mit
	 */
	public zeitraster: List<UvZeitraster> = new ArrayList<UvZeitraster>();

	/**
	 * Ein Array mit
	 */
	public zeitrastereintraege: List<UvZeitrasterEintrag> = new ArrayList<UvZeitrasterEintrag>();

	/**
	 * Ein Array mit
	 */
	public faecher: List<UvFach> = new ArrayList<UvFach>();

	/**
	 * Ein Array mit den Anrechnungsstunden der Lehrer
	 */
	public lehrerAnrechnungsstunden: List<UvLehrerAnrechnungsstunden> = new ArrayList<UvLehrerAnrechnungsstunden>();

	/**
	 * Ein Array mit dem Pflichtstundensoll der Lehrer
	 */
	public lehrerPflichtstundensoll: List<UvLehrerPflichtstundensoll> = new ArrayList<UvLehrerPflichtstundensoll>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvGrunddatenBundle';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvGrunddatenBundle'].includes(name);
	}

	public static readonly class = new Class<UvGrunddatenBundle>('de.svws_nrw.core.data.uv.UvGrunddatenBundle');

	public static transpilerFromJSON(json: string): UvGrunddatenBundle {
		const obj = JSON.parse(json) as Partial<UvGrunddatenBundle>;
		const result = new UvGrunddatenBundle();
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(UvLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrerUnterrichtsfaecher !== undefined) {
			for (const elem of obj.lehrerUnterrichtsfaecher) {
				result.lehrerUnterrichtsfaecher.add(LehrerUnterrichtsfach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raeume !== undefined) {
			for (const elem of obj.raeume) {
				result.raeume.add(UvRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raumgruppen !== undefined) {
			for (const elem of obj.raumgruppen) {
				result.raumgruppen.add(UvRaumgruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.stundentafeln !== undefined) {
			for (const elem of obj.stundentafeln) {
				result.stundentafeln.add(UvStundentafel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.stundentafelfaecher !== undefined) {
			for (const elem of obj.stundentafelfaecher) {
				result.stundentafelfaecher.add(UvStundentafelFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.zeitraster !== undefined) {
			for (const elem of obj.zeitraster) {
				result.zeitraster.add(UvZeitraster.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.zeitrastereintraege !== undefined) {
			for (const elem of obj.zeitrastereintraege) {
				result.zeitrastereintraege.add(UvZeitrasterEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(UvFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrerAnrechnungsstunden !== undefined) {
			for (const elem of obj.lehrerAnrechnungsstunden) {
				result.lehrerAnrechnungsstunden.add(UvLehrerAnrechnungsstunden.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrerPflichtstundensoll !== undefined) {
			for (const elem of obj.lehrerPflichtstundensoll) {
				result.lehrerPflichtstundensoll.add(UvLehrerPflichtstundensoll.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvGrunddatenBundle): string {
		let result = '{';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += UvLehrer.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrerUnterrichtsfaecher" : [ ';
		for (let i = 0; i < obj.lehrerUnterrichtsfaecher.size(); i++) {
			const elem = obj.lehrerUnterrichtsfaecher.get(i);
			result += LehrerUnterrichtsfach.transpilerToJSON(elem);
			if (i < obj.lehrerUnterrichtsfaecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raeume" : [ ';
		for (let i = 0; i < obj.raeume.size(); i++) {
			const elem = obj.raeume.get(i);
			result += UvRaum.transpilerToJSON(elem);
			if (i < obj.raeume.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raumgruppen" : [ ';
		for (let i = 0; i < obj.raumgruppen.size(); i++) {
			const elem = obj.raumgruppen.get(i);
			result += UvRaumgruppe.transpilerToJSON(elem);
			if (i < obj.raumgruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"stundentafeln" : [ ';
		for (let i = 0; i < obj.stundentafeln.size(); i++) {
			const elem = obj.stundentafeln.get(i);
			result += UvStundentafel.transpilerToJSON(elem);
			if (i < obj.stundentafeln.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"stundentafelfaecher" : [ ';
		for (let i = 0; i < obj.stundentafelfaecher.size(); i++) {
			const elem = obj.stundentafelfaecher.get(i);
			result += UvStundentafelFach.transpilerToJSON(elem);
			if (i < obj.stundentafelfaecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"zeitraster" : [ ';
		for (let i = 0; i < obj.zeitraster.size(); i++) {
			const elem = obj.zeitraster.get(i);
			result += UvZeitraster.transpilerToJSON(elem);
			if (i < obj.zeitraster.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"zeitrastereintraege" : [ ';
		for (let i = 0; i < obj.zeitrastereintraege.size(); i++) {
			const elem = obj.zeitrastereintraege.get(i);
			result += UvZeitrasterEintrag.transpilerToJSON(elem);
			if (i < obj.zeitrastereintraege.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += UvFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrerAnrechnungsstunden" : [ ';
		for (let i = 0; i < obj.lehrerAnrechnungsstunden.size(); i++) {
			const elem = obj.lehrerAnrechnungsstunden.get(i);
			result += UvLehrerAnrechnungsstunden.transpilerToJSON(elem);
			if (i < obj.lehrerAnrechnungsstunden.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrerPflichtstundensoll" : [ ';
		for (let i = 0; i < obj.lehrerPflichtstundensoll.size(); i++) {
			const elem = obj.lehrerPflichtstundensoll.get(i);
			result += UvLehrerPflichtstundensoll.transpilerToJSON(elem);
			if (i < obj.lehrerPflichtstundensoll.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvGrunddatenBundle>): string {
		let result = '{';
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += UvLehrer.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrerUnterrichtsfaecher !== undefined) {
			result += '"lehrerUnterrichtsfaecher" : [ ';
			for (let i = 0; i < obj.lehrerUnterrichtsfaecher.size(); i++) {
				const elem = obj.lehrerUnterrichtsfaecher.get(i);
				result += LehrerUnterrichtsfach.transpilerToJSON(elem);
				if (i < obj.lehrerUnterrichtsfaecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raeume !== undefined) {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += UvRaum.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raumgruppen !== undefined) {
			result += '"raumgruppen" : [ ';
			for (let i = 0; i < obj.raumgruppen.size(); i++) {
				const elem = obj.raumgruppen.get(i);
				result += UvRaumgruppe.transpilerToJSON(elem);
				if (i < obj.raumgruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.stundentafeln !== undefined) {
			result += '"stundentafeln" : [ ';
			for (let i = 0; i < obj.stundentafeln.size(); i++) {
				const elem = obj.stundentafeln.get(i);
				result += UvStundentafel.transpilerToJSON(elem);
				if (i < obj.stundentafeln.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.stundentafelfaecher !== undefined) {
			result += '"stundentafelfaecher" : [ ';
			for (let i = 0; i < obj.stundentafelfaecher.size(); i++) {
				const elem = obj.stundentafelfaecher.get(i);
				result += UvStundentafelFach.transpilerToJSON(elem);
				if (i < obj.stundentafelfaecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.zeitraster !== undefined) {
			result += '"zeitraster" : [ ';
			for (let i = 0; i < obj.zeitraster.size(); i++) {
				const elem = obj.zeitraster.get(i);
				result += UvZeitraster.transpilerToJSON(elem);
				if (i < obj.zeitraster.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.zeitrastereintraege !== undefined) {
			result += '"zeitrastereintraege" : [ ';
			for (let i = 0; i < obj.zeitrastereintraege.size(); i++) {
				const elem = obj.zeitrastereintraege.get(i);
				result += UvZeitrasterEintrag.transpilerToJSON(elem);
				if (i < obj.zeitrastereintraege.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += UvFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrerAnrechnungsstunden !== undefined) {
			result += '"lehrerAnrechnungsstunden" : [ ';
			for (let i = 0; i < obj.lehrerAnrechnungsstunden.size(); i++) {
				const elem = obj.lehrerAnrechnungsstunden.get(i);
				result += UvLehrerAnrechnungsstunden.transpilerToJSON(elem);
				if (i < obj.lehrerAnrechnungsstunden.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrerPflichtstundensoll !== undefined) {
			result += '"lehrerPflichtstundensoll" : [ ';
			for (let i = 0; i < obj.lehrerPflichtstundensoll.size(); i++) {
				const elem = obj.lehrerPflichtstundensoll.get(i);
				result += UvLehrerPflichtstundensoll.transpilerToJSON(elem);
				if (i < obj.lehrerPflichtstundensoll.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvGrunddatenBundle(obj: unknown): UvGrunddatenBundle {
	return obj as UvGrunddatenBundle;
}
