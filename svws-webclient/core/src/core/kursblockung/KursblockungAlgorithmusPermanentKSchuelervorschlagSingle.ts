import { KursblockungAlgorithmusPermanentK, cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentK } from '../../core/kursblockung/KursblockungAlgorithmusPermanentK';
import { Random } from '../../java/util/Random';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { Class } from '../../java/lang/Class';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';

export class KursblockungAlgorithmusPermanentKSchuelervorschlagSingle extends KursblockungAlgorithmusPermanentK {


	/**
	 * Im Konstruktor wird ein zufälliger Anfangszustand erzeugt.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param input   Die dynamischen Blockungsdaten.
	 */
	public constructor(random: Random, logger: Logger, input: GostBlockungsdatenManager) {
		super(random, logger, input);
		if (this.dynDaten.gibKurseDieFreiSindAnzahl() === 0)
			return;
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		this.dynDaten.aktionKurseFreieZufaelligVerteilen();
		this.dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
		this.dynDaten.aktionZustandSpeichernK();
	}

	public toString(): string {
		return "KursblockungAlgorithmusPermanentKSchuelervorschlagSingle";
	}

	public next(zeitEnde: number): void {
		do {
			this.verteileKurse();
		} while (System.currentTimeMillis() < zeitEnde);
	}

	/**
	 * Ein bestimmer Schüler entscheidet, wie die Kurse neuverteilt werden.
	 */
	private verteileKurse(): void {
		do {
			this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			let kurslagenveraenderung: boolean = this.dynDaten.aktionKurseVerteilenNachSchuelerwunschSingle();
			if (!kurslagenveraenderung)
				this.dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
			this.dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
			if (this.dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				this.dynDaten.aktionZustandSpeichernK();
				return;
			}
			this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			this.dynDaten.aktionSchuelerVerteilenMitBipartitemMatching();
			if (this.dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				this.dynDaten.aktionZustandSpeichernK();
				return;
			}
		} while (this._random.nextBoolean());
		this.dynDaten.aktionZustandLadenK();
	}

	public ladeBestMitSchuelerverteilung(): void {
		this.dynDaten.aktionZustandLadenK();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKSchuelervorschlagSingle';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentK', 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKSchuelervorschlagSingle'].includes(name);
	}

	public static readonly class = new Class<KursblockungAlgorithmusPermanentKSchuelervorschlagSingle>('de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKSchuelervorschlagSingle');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentKSchuelervorschlagSingle(obj: unknown): KursblockungAlgorithmusPermanentKSchuelervorschlagSingle {
	return obj as KursblockungAlgorithmusPermanentKSchuelervorschlagSingle;
}
