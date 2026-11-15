import { KursblockungAlgorithmusPermanentK, cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentK } from '../../core/kursblockung/KursblockungAlgorithmusPermanentK';
import { Random } from '../../java/util/Random';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { Class } from '../../java/lang/Class';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';

export class KursblockungAlgorithmusPermanentKMixVier extends KursblockungAlgorithmusPermanentK {

	/**
	 * Zweck: Damit die Initialisierung nur einmal ausgeführt wird.
	 */
	private initialisiert: boolean = false;


	/**
	 * Erzeugt den Algorithmus.
	 *
	 * @param random   das Zufallsobjekt
	 * @param logger   der Logger
	 * @param input    die Eingabedaten
	 */
	public constructor(random: Random, logger: Logger, input: GostBlockungsdatenManager) {
		super(random, logger, input);
	}

	public next(zeitEnde: number): void {
		if (!this.initialisiert) {
			this.init();
			this.initialisiert = true;
		}
		while (System.currentTimeMillis() < zeitEnde) {
			if (this.schritt()) {
				this.dynDaten.aktionZustandSpeichernK();
			} else {
				this.dynDaten.aktionZustandLadenK();
			}
		}
	}

	public ladeBestMitSchuelerverteilung(): void {
		this.dynDaten.aktionZustandLadenK();
	}

	private init(): void {
		this.dynDaten.aktionStartzustandWunschkaskade();
	}

	private schritt(): boolean {
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		this.zuege();
		this.dynDaten.aktionKurseVerteilenNachSchuelerwunsch();
		for (let i: number = 0; (i < 8) && this.dynDaten.aktionKurseVerteilenNachSchuelerwunschSingle(); i++) {
			// empty block
		}
		this.dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
		this.dynDaten.aktionSchuelerVerteilenMitBipartitemMatching();
		return this.dynDaten.gibCompareZustandK1NW2KD3FW() >= 0;
	}

	private zuege(): void {
		const r: number = this.rnd.nextInt(100);
		if (r < 40) {
			this.dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
		} else
			if (r < 50) {
				this.dynDaten.aktionKursVerteilenEineZufaelligeFachgruppe();
			} else
				if (r < 70) {
					this.dynDaten.aktionKurseTauschen(12);
				} else {
					this.dynDaten.aktionKurseTauschen(1);
				}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKMixVier';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentK', 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKMixVier'].includes(name);
	}

	public static readonly class = new Class<KursblockungAlgorithmusPermanentKMixVier>('de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKMixVier');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentKMixVier(obj: unknown): KursblockungAlgorithmusPermanentKMixVier {
	return obj as KursblockungAlgorithmusPermanentKMixVier;
}
