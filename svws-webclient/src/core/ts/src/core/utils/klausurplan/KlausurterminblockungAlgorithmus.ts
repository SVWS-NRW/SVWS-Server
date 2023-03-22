import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { KlausurterminblockungAlgorithmusConfig, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungAlgorithmusConfig } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusConfig';
import { GostKursklausur, cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { KlausurterminblockungAlgorithmusGreedy1, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungAlgorithmusGreedy1 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy1';
import { KlausurterminblockungAlgorithmusGreedy2, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungAlgorithmusGreedy2 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy2';
import { DeveloperNotificationException, cast_de_nrw_schule_svws_core_exceptions_DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { KlausurterminblockungAlgorithmusGreedy3, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungAlgorithmusGreedy3 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy3';
import { System, cast_java_lang_System } from '../../../java/lang/System';
import { Random, cast_java_util_Random } from '../../../java/util/Random';
import { KlausurterminblockungDynDaten, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungDynDaten } from '../../../core/utils/klausurplan/KlausurterminblockungDynDaten';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { KlausurterminblockungAlgorithmusGreedy1b, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungAlgorithmusGreedy1b } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy1b';
import { KlausurterminblockungAlgorithmusAbstract, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungAlgorithmusAbstract } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusAbstract';
import { KlausurterminblockungAlgorithmusGreedy2b, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungAlgorithmusGreedy2b } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy2b';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KlausurterminblockungAlgorithmus extends JavaObject {


	/**
	 * Der Konstruktor ist leer und erstellt auch keine Datenstrukturen.
	 */
	public constructor() {
		super();
	}

	/**
	 * @param pInput   Die Eingabe beinhaltet alle Klausuren, welche die SuS beinhalten.
	 * @param pConfig  Das Konfigurationsobjekt für den Algorithmus.
	 * @return Eine Liste (Termine) von Listen (KlausurIDs)
	 */
	public berechne(pInput : List<GostKursklausur>, pConfig : KlausurterminblockungAlgorithmusConfig) : List<List<number>> {
		let out : List<List<number>> = new Vector();
		switch (pConfig.get_lk_gk_modus()) {
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_BEIDE: {
				KlausurterminblockungAlgorithmus.berechne_helper(pInput, pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_LK: {
				let nurLK : List<GostKursklausur> = KlausurterminblockungAlgorithmus.filter(pInput, true);
				KlausurterminblockungAlgorithmus.berechne_helper(nurLK, pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_GK: {
				let nurGK : List<GostKursklausur> = KlausurterminblockungAlgorithmus.filter(pInput, false);
				KlausurterminblockungAlgorithmus.berechne_helper(nurGK, pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_GETRENNT: {
				let nurLK : List<GostKursklausur> = KlausurterminblockungAlgorithmus.filter(pInput, true);
				let nurGK : List<GostKursklausur> = KlausurterminblockungAlgorithmus.filter(pInput, false);
				KlausurterminblockungAlgorithmus.berechne_helper(nurLK, pConfig, out);
				KlausurterminblockungAlgorithmus.berechne_helper(nurGK, pConfig, out);
				break;
			}
			default: {
				throw new DeveloperNotificationException("Der LK-GK-Modus ist unbekannt!")
			}
		}
		return out;
	}

	private static filter(pInput : List<GostKursklausur>, pLK : boolean) : List<GostKursklausur> {
		let temp : List<GostKursklausur> = new Vector();
		for (let gostKursklausur of pInput) 
			if (JavaObject.equalsTranspiler(gostKursklausur.kursart, ("LK")) === pLK)
				temp.add(gostKursklausur);
		return temp;
	}

	private static berechne_helper(pInput : List<GostKursklausur>, pConfig : KlausurterminblockungAlgorithmusConfig, out : List<List<number>>) : void {
		let zeitEndeGesamt : number = System.currentTimeMillis() + pConfig.get_max_time_millis();
		let seed : number = new Random().nextLong();
		let random : Random = new Random(seed);
		let dynDaten : KlausurterminblockungDynDaten | null = new KlausurterminblockungDynDaten(random, pInput, pConfig);
		let algorithmen : Array<KlausurterminblockungAlgorithmusAbstract> = [new KlausurterminblockungAlgorithmusGreedy1(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy1b(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy2(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy2b(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy3(random, dynDaten)];
		dynDaten.aktion_Clear_TermineNacheinander_GruppeZufaellig();
		dynDaten.aktionZustand2Speichern();
		let zeitProAlgorithmus : number = 10;
		do {
			for (let iAlgo : number = 0; iAlgo < algorithmen.length; iAlgo++){
				let zeitEndeRunde : number = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmen[iAlgo].berechne(zeitEndeRunde);
			}
			zeitProAlgorithmus *= 2;
		} while (System.currentTimeMillis() + algorithmen.length * zeitProAlgorithmus <= zeitEndeGesamt);
		dynDaten.aktionZustand2Laden();
		out.addAll(dynDaten.gibErzeugeOutput());
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.klausurplan.KlausurterminblockungAlgorithmus'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungAlgorithmus(obj : unknown) : KlausurterminblockungAlgorithmus {
	return obj as KlausurterminblockungAlgorithmus;
}
