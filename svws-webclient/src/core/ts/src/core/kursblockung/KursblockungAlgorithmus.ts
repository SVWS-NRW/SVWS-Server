import { GostBlockungsergebnisManager } from '../../core/utils/gost/GostBlockungsergebnisManager';
import { KursblockungAlgorithmusKOptimiereBest } from '../../core/kursblockung/KursblockungAlgorithmusKOptimiereBest';
import { KursblockungAlgorithmusS } from '../../core/kursblockung/KursblockungAlgorithmusS';
import { KursblockungAlgorithmusSSchnellW } from '../../core/kursblockung/KursblockungAlgorithmusSSchnellW';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { KursblockungAlgorithmusKSchnellW } from '../../core/kursblockung/KursblockungAlgorithmusKSchnellW';
import { KursblockungAlgorithmusKMatching } from '../../core/kursblockung/KursblockungAlgorithmusKMatching';
import { KursblockungAlgorithmusKFachwahlmatrix } from '../../core/kursblockung/KursblockungAlgorithmusKFachwahlmatrix';
import { Service } from '../../core/Service';
import { KursblockungAlgorithmusSMatching } from '../../core/kursblockung/KursblockungAlgorithmusSMatching';
import { KursblockungAlgorithmusKSchuelervorschlag } from '../../core/kursblockung/KursblockungAlgorithmusKSchuelervorschlag';
import { LogLevel } from '../../core/logger/LogLevel';
import { System } from '../../java/lang/System';
import { KursblockungAlgorithmusSMatchingW } from '../../core/kursblockung/KursblockungAlgorithmusSMatchingW';
import { Random } from '../../java/util/Random';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { KursblockungAlgorithmusSZufaellig } from '../../core/kursblockung/KursblockungAlgorithmusSZufaellig';
import { KursblockungAlgorithmusK } from '../../core/kursblockung/KursblockungAlgorithmusK';
import { Vector } from '../../java/util/Vector';

export class KursblockungAlgorithmus extends Service<GostBlockungsdatenManager, Vector<GostBlockungsergebnisManager>> {


	public constructor() {
		super();
	}

	public handle(pInput : GostBlockungsdatenManager) : Vector<GostBlockungsergebnisManager> {
		this.logger.modifyIndent(+4);
		let seed : number = new Random().nextLong();
		let random : Random = new Random(seed);
		this.logger.log(LogLevel.APP, "Erster nextInt() Aufruf liefert " + seed);
		let dynDaten : KursblockungDynDaten = new KursblockungDynDaten(random, this.logger, pInput);
		let zeitBedarf : number = dynDaten.gibBlockungszeitMillis();
		let zeitEndeGesamt : number = System.currentTimeMillis() + zeitBedarf;
		let kursblockungOutputs : Vector<GostBlockungsergebnisManager> = new Vector();
		let algorithmenK : Array<KursblockungAlgorithmusK> = [new KursblockungAlgorithmusKSchnellW(random, this.logger, dynDaten), new KursblockungAlgorithmusKFachwahlmatrix(random, this.logger, dynDaten), new KursblockungAlgorithmusKMatching(random, this.logger, dynDaten), new KursblockungAlgorithmusKSchuelervorschlag(random, this.logger, dynDaten), new KursblockungAlgorithmusKOptimiereBest(random, this.logger, dynDaten)];
		let algorithmenS : Array<KursblockungAlgorithmusS> = [new KursblockungAlgorithmusSSchnellW(random, this.logger, dynDaten), new KursblockungAlgorithmusSZufaellig(random, this.logger, dynDaten), new KursblockungAlgorithmusSMatching(random, this.logger, dynDaten), new KursblockungAlgorithmusSMatchingW(random, this.logger, dynDaten)];
		let zeitProK : number = 100;
		do {
			for (let iK : number = 0; iK < algorithmenK.length; iK++) {
				let zeitEndeK : number = System.currentTimeMillis() + zeitProK;
				do {
					KursblockungAlgorithmus.verwendeAlgorithmusK(algorithmenK[iK], zeitEndeK, dynDaten, algorithmenS, kursblockungOutputs, pInput);
				} while (System.currentTimeMillis() < zeitEndeK);
				if (System.currentTimeMillis() + zeitProK > zeitEndeGesamt)
					break;
			}
			zeitProK *= 2;
		} while (System.currentTimeMillis() < zeitEndeGesamt);
		this.logger.modifyIndent(-4);
		return kursblockungOutputs;
	}

	private static verwendeAlgorithmusK(kursblockungAlgorithmusK : KursblockungAlgorithmusK, zeitEndeK : number, dynDaten : KursblockungDynDaten, algorithmenS : Array<KursblockungAlgorithmusS>, outputs : Vector<GostBlockungsergebnisManager>, pInput : GostBlockungsdatenManager) : void {
		kursblockungAlgorithmusK.berechne(zeitEndeK);
		dynDaten.aktionZustandSpeichernK();
		for (let iS : number = 0; iS < algorithmenS.length; iS++) {
			algorithmenS[iS].berechne();
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0)
				dynDaten.aktionZustandSpeichernK();
		}
		dynDaten.aktionZustandLadenK();
		if (dynDaten.gibCompareZustandG_NW_KD_FW() > 0) {
			dynDaten.aktionZustandSpeichernG();
		}
		let out : GostBlockungsergebnisManager = dynDaten.gibErzeugtesKursblockungOutput(pInput, outputs.size() + 1);
		outputs.add(out);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.Service', 'de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmus'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmus(obj : unknown) : KursblockungAlgorithmus {
	return obj as KursblockungAlgorithmus;
}
