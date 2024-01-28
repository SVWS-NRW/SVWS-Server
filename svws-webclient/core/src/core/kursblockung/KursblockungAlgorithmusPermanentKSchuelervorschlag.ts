import { KursblockungAlgorithmusPermanentK, cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentK } from '../../core/kursblockung/KursblockungAlgorithmusPermanentK';
import { Random } from '../../java/util/Random';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';

export class KursblockungAlgorithmusPermanentKSchuelervorschlag extends KursblockungAlgorithmusPermanentK {


	/**
	 * Im Konstruktor wird ein zufälliger Anfangszustand erzeugt.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param input   Die dynamischen Blockungsdaten.
	 */
	public constructor(random : Random, logger : Logger, input : GostBlockungsdatenManager) {
		super(random, logger, input);
		if (this.dynDaten.gibKurseDieFreiSindAnzahl() === 0)
			return;
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		this.dynDaten.aktionKurseFreieZufaelligVerteilen();
		this.dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
		this.dynDaten.aktionZustandSpeichernK();
	}

	public toString() : string {
		return "KursblockungAlgorithmusPermanentKSchuelervorschlag";
	}

	public next(zeitEnde : number) : void {
		const current : number = System.currentTimeMillis();
		const halbzeit : number = current + Math.trunc((zeitEnde - current) / 2);
		do {
			this.verteileKurseMitSchuelerwunsch();
		} while (System.currentTimeMillis() < halbzeit);
		do {
			this.verteileKurseMitMatchingW();
		} while (System.currentTimeMillis() < zeitEnde);
	}

	private verteileKurseMitSchuelerwunsch() : void {
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		this.dynDaten.aktionKurseVerteilenNachSchuelerwunsch();
		this.dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
		const compare : number = this.dynDaten.gibCompareZustandK_NW_KD_FW();
		if (compare >= 0) {
			this.dynDaten.aktionZustandSpeichernK();
			return;
		}
		this.dynDaten.aktionZustandLadenK();
	}

	private verteileKurseMitMatchingW() : void {
		do {
			this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			this.dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
			this.dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
			if (this.dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				this.dynDaten.aktionZustandSpeichernK();
				return;
			}
		} while (this._random.nextBoolean());
		this.dynDaten.aktionZustandLadenK();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKSchuelervorschlag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKSchuelervorschlag', 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentK'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentKSchuelervorschlag(obj : unknown) : KursblockungAlgorithmusPermanentKSchuelervorschlag {
	return obj as KursblockungAlgorithmusPermanentKSchuelervorschlag;
}
