import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostSchuelerklausurTerminRich } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTerminRich';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { GostKlausurraumRich } from '../../../../core/data/gost/klausurplanung/GostKlausurraumRich';
import { Class } from '../../../../java/lang/Class';

export class GostKlausurraumblockungKonfiguration extends JavaObject {

	/**
	 * Die maximale Zeit, welche für die Blockung verwendet wird
	 */
	public maxTimeMillis : number = 1000;

	/**
	 * Die Liste der angereicherten Schülerklausurtermine.
	 */
	public schuelerklausurtermine : List<GostSchuelerklausurTerminRich> = new ArrayList<GostSchuelerklausurTerminRich>();

	/**
	 * Die Liste der angereicherten Klausurräume.
	 */
	public raeume : List<GostKlausurraumRich> = new ArrayList<GostKlausurraumRich>();

	/**
	 * TRUE, dann werden so wenig Räume wie möglich genutzt.
	 *   Falls {@link #_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume} auch TRUE, dann gilt diese Regel primär.
	 */
	public _regel_optimiere_blocke_in_moeglichst_wenig_raeume : boolean = true;

	/**
	 * TRUE, dann werden wird auf die Räume gleichmäßig verteilt.
	 */
	public _regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume : boolean = true;

	/**
	 * TRUE, dann müssen die selben Kursklausuren im selben Raum geschrieben werden.
	 */
	public _regel_forciere_selbe_kursklausur_im_selben_raum : boolean = true;

	/**
	 * TRUE, dann dürfen nur die selben Klausurdauern in einen Raum.
	 */
	public _regel_forciere_selbe_klausurdauer_pro_raum : boolean = false;

	/**
	 * TRUE, dann dürfen nur die selben Klausurstart-Zeiten in einen Raum.
	 */
	public _regel_forciere_selben_klausurstart_pro_raum : boolean = true;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumblockungKonfiguration';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumblockungKonfiguration'].includes(name);
	}

	public static class = new Class<GostKlausurraumblockungKonfiguration>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumblockungKonfiguration');

	public static transpilerFromJSON(json : string): GostKlausurraumblockungKonfiguration {
		const obj = JSON.parse(json) as Partial<GostKlausurraumblockungKonfiguration>;
		const result = new GostKlausurraumblockungKonfiguration();
		if (obj.maxTimeMillis === undefined)
			throw new Error('invalid json format, missing attribute maxTimeMillis');
		result.maxTimeMillis = obj.maxTimeMillis;
		if (obj.schuelerklausurtermine !== undefined) {
			for (const elem of obj.schuelerklausurtermine) {
				result.schuelerklausurtermine.add(GostSchuelerklausurTerminRich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raeume !== undefined) {
			for (const elem of obj.raeume) {
				result.raeume.add(GostKlausurraumRich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj._regel_optimiere_blocke_in_moeglichst_wenig_raeume === undefined)
			throw new Error('invalid json format, missing attribute _regel_optimiere_blocke_in_moeglichst_wenig_raeume');
		result._regel_optimiere_blocke_in_moeglichst_wenig_raeume = obj._regel_optimiere_blocke_in_moeglichst_wenig_raeume;
		if (obj._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume === undefined)
			throw new Error('invalid json format, missing attribute _regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume');
		result._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume = obj._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume;
		if (obj._regel_forciere_selbe_kursklausur_im_selben_raum === undefined)
			throw new Error('invalid json format, missing attribute _regel_forciere_selbe_kursklausur_im_selben_raum');
		result._regel_forciere_selbe_kursklausur_im_selben_raum = obj._regel_forciere_selbe_kursklausur_im_selben_raum;
		if (obj._regel_forciere_selbe_klausurdauer_pro_raum === undefined)
			throw new Error('invalid json format, missing attribute _regel_forciere_selbe_klausurdauer_pro_raum');
		result._regel_forciere_selbe_klausurdauer_pro_raum = obj._regel_forciere_selbe_klausurdauer_pro_raum;
		if (obj._regel_forciere_selben_klausurstart_pro_raum === undefined)
			throw new Error('invalid json format, missing attribute _regel_forciere_selben_klausurstart_pro_raum');
		result._regel_forciere_selben_klausurstart_pro_raum = obj._regel_forciere_selben_klausurstart_pro_raum;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurraumblockungKonfiguration) : string {
		let result = '{';
		result += '"maxTimeMillis" : ' + obj.maxTimeMillis.toString() + ',';
		result += '"schuelerklausurtermine" : [ ';
		for (let i = 0; i < obj.schuelerklausurtermine.size(); i++) {
			const elem = obj.schuelerklausurtermine.get(i);
			result += GostSchuelerklausurTerminRich.transpilerToJSON(elem);
			if (i < obj.schuelerklausurtermine.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raeume" : [ ';
		for (let i = 0; i < obj.raeume.size(); i++) {
			const elem = obj.raeume.get(i);
			result += GostKlausurraumRich.transpilerToJSON(elem);
			if (i < obj.raeume.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"_regel_optimiere_blocke_in_moeglichst_wenig_raeume" : ' + obj._regel_optimiere_blocke_in_moeglichst_wenig_raeume.toString() + ',';
		result += '"_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume" : ' + obj._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume.toString() + ',';
		result += '"_regel_forciere_selbe_kursklausur_im_selben_raum" : ' + obj._regel_forciere_selbe_kursklausur_im_selben_raum.toString() + ',';
		result += '"_regel_forciere_selbe_klausurdauer_pro_raum" : ' + obj._regel_forciere_selbe_klausurdauer_pro_raum.toString() + ',';
		result += '"_regel_forciere_selben_klausurstart_pro_raum" : ' + obj._regel_forciere_selben_klausurstart_pro_raum.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurraumblockungKonfiguration>) : string {
		let result = '{';
		if (obj.maxTimeMillis !== undefined) {
			result += '"maxTimeMillis" : ' + obj.maxTimeMillis.toString() + ',';
		}
		if (obj.schuelerklausurtermine !== undefined) {
			result += '"schuelerklausurtermine" : [ ';
			for (let i = 0; i < obj.schuelerklausurtermine.size(); i++) {
				const elem = obj.schuelerklausurtermine.get(i);
				result += GostSchuelerklausurTerminRich.transpilerToJSON(elem);
				if (i < obj.schuelerklausurtermine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raeume !== undefined) {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += GostKlausurraumRich.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj._regel_optimiere_blocke_in_moeglichst_wenig_raeume !== undefined) {
			result += '"_regel_optimiere_blocke_in_moeglichst_wenig_raeume" : ' + obj._regel_optimiere_blocke_in_moeglichst_wenig_raeume.toString() + ',';
		}
		if (obj._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume !== undefined) {
			result += '"_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume" : ' + obj._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume.toString() + ',';
		}
		if (obj._regel_forciere_selbe_kursklausur_im_selben_raum !== undefined) {
			result += '"_regel_forciere_selbe_kursklausur_im_selben_raum" : ' + obj._regel_forciere_selbe_kursklausur_im_selben_raum.toString() + ',';
		}
		if (obj._regel_forciere_selbe_klausurdauer_pro_raum !== undefined) {
			result += '"_regel_forciere_selbe_klausurdauer_pro_raum" : ' + obj._regel_forciere_selbe_klausurdauer_pro_raum.toString() + ',';
		}
		if (obj._regel_forciere_selben_klausurstart_pro_raum !== undefined) {
			result += '"_regel_forciere_selben_klausurstart_pro_raum" : ' + obj._regel_forciere_selben_klausurstart_pro_raum.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraumblockungKonfiguration(obj : unknown) : GostKlausurraumblockungKonfiguration {
	return obj as GostKlausurraumblockungKonfiguration;
}
