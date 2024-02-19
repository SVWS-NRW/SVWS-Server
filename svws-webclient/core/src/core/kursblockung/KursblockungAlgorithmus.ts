import { GostBlockungsergebnisManager } from '../../core/utils/gost/GostBlockungsergebnisManager';
import { KursblockungAlgorithmusKOptimiereBest } from '../../core/kursblockung/KursblockungAlgorithmusKOptimiereBest';
import { KursblockungAlgorithmusS } from '../../core/kursblockung/KursblockungAlgorithmusS';
import { KursblockungAlgorithmusSSchnellW } from '../../core/kursblockung/KursblockungAlgorithmusSSchnellW';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { KursblockungAlgorithmusKSchnellW } from '../../core/kursblockung/KursblockungAlgorithmusKSchnellW';
import { KursblockungAlgorithmusKMatching } from '../../core/kursblockung/KursblockungAlgorithmusKMatching';
import { KursblockungAlgorithmusKFachwahlmatrix } from '../../core/kursblockung/KursblockungAlgorithmusKFachwahlmatrix';
import { ArrayList } from '../../java/util/ArrayList';
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

export class KursblockungAlgorithmus extends Service<GostBlockungsdatenManager, ArrayList<GostBlockungsergebnisManager>> {

	private readonly _random : Random = new Random();


	public constructor() {
		super();
	}

	public handle(pInput : GostBlockungsdatenManager) : ArrayList<GostBlockungsergebnisManager> {
		this.logger.modifyIndent(+4);
		const seed : number = this._random.nextLong();
		const random : Random = new Random(seed);
		this.logger.log(LogLevel.APP, "Erster nextInt() Aufruf liefert " + seed);
		const dynDaten : KursblockungDynDaten = new KursblockungDynDaten(random, this.logger, pInput);
		const zeitBedarf : number = dynDaten.gibBlockungszeitMillis();
		const zeitEndeGesamt : number = System.currentTimeMillis() + zeitBedarf;
		const kursblockungOutputs : ArrayList<GostBlockungsergebnisManager> = new ArrayList();
		const algorithmenK : Array<KursblockungAlgorithmusK> = [new KursblockungAlgorithmusKSchnellW(random, this.logger, dynDaten), new KursblockungAlgorithmusKFachwahlmatrix(random, this.logger, dynDaten), new KursblockungAlgorithmusKMatching(random, this.logger, dynDaten), new KursblockungAlgorithmusKSchuelervorschlag(random, this.logger, dynDaten), new KursblockungAlgorithmusKOptimiereBest(random, this.logger, dynDaten)];
		const algorithmenS : Array<KursblockungAlgorithmusS> = [new KursblockungAlgorithmusSSchnellW(random, this.logger, dynDaten), new KursblockungAlgorithmusSZufaellig(random, this.logger, dynDaten), new KursblockungAlgorithmusSMatching(random, this.logger, dynDaten), new KursblockungAlgorithmusSMatchingW(random, this.logger, dynDaten)];
		let zeitProK : number = 100;
		do {
			for (let iK : number = 0; iK < algorithmenK.length; iK++) {
				const zeitEndeK : number = System.currentTimeMillis() + zeitProK;
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

	private static verwendeAlgorithmusK(kursblockungAlgorithmusK : KursblockungAlgorithmusK, zeitEndeK : number, dynDaten : KursblockungDynDaten, algorithmenS : Array<KursblockungAlgorithmusS>, outputs : ArrayList<GostBlockungsergebnisManager>, pInput : GostBlockungsdatenManager) : void {
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
		const out : GostBlockungsergebnisManager = dynDaten.gibErzeugtesKursblockungOutput(pInput, outputs.size() + 1);
		outputs.add(out);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmus', 'de.svws_nrw.core.Service'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmus(obj : unknown) : KursblockungAlgorithmus {
	return obj as KursblockungAlgorithmus;
}
