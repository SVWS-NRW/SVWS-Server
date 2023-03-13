import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { KlausurblockungSchienenAlgorithmusGreedy3, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusGreedy3 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy3';
import { KlausurblockungSchienenDynDaten, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenDynDaten } from '../../../core/utils/klausurplan/KlausurblockungSchienenDynDaten';
import { KlausurblockungSchienenAlgorithmusGreedy2, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusGreedy2 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy2';
import { GostKursklausur, cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { KlausurblockungSchienenAlgorithmusGreedy5, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusGreedy5 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy5';
import { KlausurblockungSchienenAlgorithmusGreedy4, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusGreedy4 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy4';
import { KlausurblockungSchienenAlgorithmusGreedy1, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusGreedy1 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy1';
import { KlausurblockungSchienenAlgorithmusGreedy7, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusGreedy7 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy7';
import { KlausurblockungSchienenAlgorithmusGreedy6, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusGreedy6 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy6';
import { System, cast_java_lang_System } from '../../../java/lang/System';
import { KlausurblockungSchienenAlgorithmusGreedy1b, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusGreedy1b } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy1b';
import { KlausurblockungSchienenAlgorithmusAbstract, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusAbstract } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusAbstract';
import { KlausurblockungSchienenAlgorithmusGreedy2b, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusGreedy2b } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy2b';
import { Random, cast_java_util_Random } from '../../../java/util/Random';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';

export class KlausurblockungSchienenAlgorithmus extends JavaObject {


	/**
	 * Der Konstruktor ist leer und erstellt auch keine Datenstrukturen.
	 */
	public constructor() {
		super();
	}

	/**
	 * @param pInput          Die Eingabe beinhaltet alle Klausuren, welche die SuS beinhalten.
	 * @param pMaxTimeMillis  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @return Eine Liste von Listen: 1. Ebene = Schienen, 2. Ebene = KlausurIDs
	 */
	public berechne(pInput : List<GostKursklausur>, pMaxTimeMillis : number) : List<List<number>> {
		let zeitEndeGesamt : number = System.currentTimeMillis() + pMaxTimeMillis;
		let seed : number = new Random().nextLong();
		let random : Random = new Random(seed);
		let dynDaten : KlausurblockungSchienenDynDaten | null = new KlausurblockungSchienenDynDaten(random, pInput);
		let algorithmen : Array<KlausurblockungSchienenAlgorithmusAbstract> = [new KlausurblockungSchienenAlgorithmusGreedy3(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy4(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy1(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy1b(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy2(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy2b(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy5(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy6(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy7(random, dynDaten)];
		dynDaten.aktion_EntferneAlles_SchienenNacheinander_KlausurenZufaellig();
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
		return dynDaten.gibErzeugeOutput();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.klausurplan.KlausurblockungSchienenAlgorithmus'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmus(obj : unknown) : KlausurblockungSchienenAlgorithmus {
	return obj as KlausurblockungSchienenAlgorithmus;
}
