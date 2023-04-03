import { JavaObject } from '../../../java/lang/JavaObject';
import { KlausurterminblockungAlgorithmusConfig } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusConfig';
import { GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { KlausurterminblockungAlgorithmusGreedy1 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy1';
import { KlausurterminblockungAlgorithmusGreedy2 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy2';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { KlausurterminblockungAlgorithmusGreedy3 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy3';
import { System } from '../../../java/lang/System';
import { Random } from '../../../java/util/Random';
import { KlausurterminblockungDynDaten } from '../../../core/utils/klausurplan/KlausurterminblockungDynDaten';
import { KlausurterminblockungAlgorithmusGreedy1b } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy1b';
import { KlausurterminblockungAlgorithmusAbstract } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusAbstract';
import { KlausurterminblockungAlgorithmusGreedy2b } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy2b';
import { List } from '../../../java/util/List';
import { Vector } from '../../../java/util/Vector';

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
		const out : List<List<number>> = new Vector();
		switch (pConfig.get_lk_gk_modus()) {
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_BEIDE: {
				KlausurterminblockungAlgorithmus.berechne_helper(pInput, pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_LK: {
				KlausurterminblockungAlgorithmus.berechne_helper(KlausurterminblockungAlgorithmus.filter(pInput, true), pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_GK: {
				KlausurterminblockungAlgorithmus.berechne_helper(KlausurterminblockungAlgorithmus.filter(pInput, false), pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_GETRENNT: {
				KlausurterminblockungAlgorithmus.berechne_helper(KlausurterminblockungAlgorithmus.filter(pInput, true), pConfig, out);
				KlausurterminblockungAlgorithmus.berechne_helper(KlausurterminblockungAlgorithmus.filter(pInput, false), pConfig, out);
				break;
			}
			default: {
				throw new DeveloperNotificationException("Der LK-GK-Modus ist unbekannt!")
			}
		}
		return out;
	}

	private static filter(pInput : List<GostKursklausur>, pLK : boolean) : List<GostKursklausur> {
		const temp : List<GostKursklausur> = new Vector();
		for (const gostKursklausur of pInput)
			if (JavaObject.equalsTranspiler(gostKursklausur.kursart, ("LK")) === pLK)
				temp.add(gostKursklausur);
		return temp;
	}

	private static berechne_helper(pInput : List<GostKursklausur>, pConfig : KlausurterminblockungAlgorithmusConfig, out : List<List<number>>) : void {
		const zeitEndeGesamt : number = System.currentTimeMillis() + pConfig.get_max_time_millis();
		const seed : number = new Random().nextLong();
		const random : Random = new Random(seed);
		const dynDaten : KlausurterminblockungDynDaten | null = new KlausurterminblockungDynDaten(random, pInput, pConfig);
		const algorithmen : Array<KlausurterminblockungAlgorithmusAbstract> = [new KlausurterminblockungAlgorithmusGreedy1(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy1b(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy2(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy2b(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy3(random, dynDaten)];
		dynDaten.aktion_Clear_TermineNacheinander_GruppeZufaellig();
		dynDaten.aktionZustand2Speichern();
		let zeitProAlgorithmus : number = 10;
		do {
			for (let iAlgo : number = 0; iAlgo < algorithmen.length; iAlgo++) {
				const zeitEndeRunde : number = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmen[iAlgo].berechne(zeitEndeRunde);
			}
			zeitProAlgorithmus *= 2;
		} while (System.currentTimeMillis() + algorithmen.length * zeitProAlgorithmus <= zeitEndeGesamt);
		dynDaten.aktionZustand2Laden();
		out.addAll(dynDaten.gibErzeugeOutput());
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplan.KlausurterminblockungAlgorithmus'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplan_KlausurterminblockungAlgorithmus(obj : unknown) : KlausurterminblockungAlgorithmus {
	return obj as KlausurterminblockungAlgorithmus;
}
