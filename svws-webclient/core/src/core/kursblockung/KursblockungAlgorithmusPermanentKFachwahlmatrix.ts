import { KursblockungAlgorithmusPermanentK, cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentK } from '../../core/kursblockung/KursblockungAlgorithmusPermanentK';
import { Random } from '../../java/util/Random';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';

export class KursblockungAlgorithmusPermanentKFachwahlmatrix extends KursblockungAlgorithmusPermanentK {


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
		this.dynDaten.aktionZustandSpeichernK();
	}

	public toString() : string {
		return "KursblockungAlgorithmusPermanentKFachwahlmatrix";
	}

	public next(zeitEnde : number) : void {
		do {
			this.optimiere();
		} while (System.currentTimeMillis() < zeitEnde);
	}

	/**
	 * Die Lage einiger Kurse wird verändert. Falls sich die Bewertung verschlechter, wird die Veränderung rückgängig gemacht.
	 */
	private optimiere() : void {
		do {
			this.dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
			if (this.dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				this.dynDaten.aktionZustandSpeichernK();
				return;
			}
		} while (this._random.nextBoolean());
		this.dynDaten.aktionZustandLadenK();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKFachwahlmatrix';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentK', 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKFachwahlmatrix'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentKFachwahlmatrix(obj : unknown) : KursblockungAlgorithmusPermanentKFachwahlmatrix {
	return obj as KursblockungAlgorithmusPermanentKFachwahlmatrix;
}
