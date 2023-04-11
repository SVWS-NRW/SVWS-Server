import { JavaObject } from '../../../java/lang/JavaObject';
import { KlausurblockungSchienenAlgorithmusGreedy3 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy3';
import { KlausurblockungSchienenDynDaten } from '../../../core/utils/klausurplan/KlausurblockungSchienenDynDaten';
import { KlausurblockungSchienenAlgorithmusGreedy2 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy2';
import { GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { KlausurblockungSchienenAlgorithmusGreedy5 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy5';
import { KlausurblockungSchienenAlgorithmusGreedy4 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy4';
import { KlausurblockungSchienenAlgorithmusGreedy1 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy1';
import { KlausurblockungSchienenAlgorithmusGreedy7 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy7';
import { KlausurblockungSchienenAlgorithmusGreedy6 } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy6';
import { System } from '../../../java/lang/System';
import { KlausurblockungSchienenAlgorithmusGreedy1b } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy1b';
import { KlausurblockungSchienenAlgorithmusAbstract } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusAbstract';
import { KlausurblockungSchienenAlgorithmusGreedy2b } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusGreedy2b';
import { Random } from '../../../java/util/Random';
import { List } from '../../../java/util/List';

export class KlausurblockungSchienenAlgorithmus extends JavaObject {

	private static readonly _random : Random = new Random();


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
		const zeitEndeGesamt : number = System.currentTimeMillis() + pMaxTimeMillis;
		const seed : number = KlausurblockungSchienenAlgorithmus._random.nextLong();
		const random : Random = new Random(seed);
		const dynDaten : KlausurblockungSchienenDynDaten | null = new KlausurblockungSchienenDynDaten(random, pInput);
		const algorithmen : Array<KlausurblockungSchienenAlgorithmusAbstract> = [new KlausurblockungSchienenAlgorithmusGreedy3(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy4(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy1(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy1b(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy2(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy2b(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy5(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy6(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy7(random, dynDaten)];
		dynDaten.aktion_EntferneAlles_SchienenNacheinander_KlausurenZufaellig();
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
		return dynDaten.gibErzeugeOutput();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplan.KlausurblockungSchienenAlgorithmus'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplan_KlausurblockungSchienenAlgorithmus(obj : unknown) : KlausurblockungSchienenAlgorithmus {
	return obj as KlausurblockungSchienenAlgorithmus;
}
