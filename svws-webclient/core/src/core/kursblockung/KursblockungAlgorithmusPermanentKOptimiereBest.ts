import { KursblockungAlgorithmusSSchnellW } from '../../core/kursblockung/KursblockungAlgorithmusSSchnellW';
import { KursblockungAlgorithmusPermanentK, cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentK } from '../../core/kursblockung/KursblockungAlgorithmusPermanentK';
import { Random } from '../../java/util/Random';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { Class } from '../../java/lang/Class';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';

export class KursblockungAlgorithmusPermanentKOptimiereBest extends KursblockungAlgorithmusPermanentK {

	/**
	 * Mit diesem Algorithmus werden die SuS verteilt.
	 */
	private readonly algoS : KursblockungAlgorithmusSSchnellW;


	/**
	 * Im Konstruktor wird das derzeit beste Ergebnis geladen.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param input   Die dynamischen Blockungsdaten.
	 * @param best    Der Zustand des derzeit besten Ergebnisses.
	 */
	public constructor(random : Random, logger : Logger, input : GostBlockungsdatenManager, best : KursblockungDynDaten | null) {
		super(random, logger, input);
		this.algoS = new KursblockungAlgorithmusSSchnellW(random, logger, super.gibDynDaten());
		if (this.dynDaten.gibKurseDieFreiSindAnzahl() === 0)
			return;
		if (best === null) {
			this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			this.dynDaten.aktionKurseFreieZufaelligVerteilen();
			this.dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
			this.dynDaten.aktionZustandSpeichernK();
		} else {
			this.dynDaten.aktionZustandLadenVon(best);
			this.dynDaten.aktionZustandSpeichernK();
		}
	}

	public toString() : string {
		return "KursblockungAlgorithmusPermanentKOptimiereBest";
	}

	public next(zeitEnde : number) : void {
		do {
			this.verteileKurse();
		} while (System.currentTimeMillis() < zeitEnde);
	}

	private verteileKurse() : void {
		do {
			this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			this.dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
			this.algoS.berechne();
			if (this.dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				this.dynDaten.aktionZustandSpeichernK();
				return;
			}
		} while (this._random.nextBoolean());
		this.dynDaten.aktionZustandLadenK();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKOptimiereBest';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentK', 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKOptimiereBest'].includes(name);
	}

	public static class = new Class<KursblockungAlgorithmusPermanentKOptimiereBest>('de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKOptimiereBest');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentKOptimiereBest(obj : unknown) : KursblockungAlgorithmusPermanentKOptimiereBest {
	return obj as KursblockungAlgorithmusPermanentKOptimiereBest;
}
