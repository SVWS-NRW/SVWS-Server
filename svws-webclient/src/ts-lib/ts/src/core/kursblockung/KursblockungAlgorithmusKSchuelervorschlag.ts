import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungDynDaten, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { KursblockungAlgorithmusK, cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusK } from '../../core/kursblockung/KursblockungAlgorithmusK';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Logger, cast_de_nrw_schule_svws_logger_Logger } from '../../logger/Logger';
import { System, cast_java_lang_System } from '../../java/lang/System';

export class KursblockungAlgorithmusKSchuelervorschlag extends KursblockungAlgorithmusK {

	private static readonly MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG : number = 2000;


	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDat Die dynamischen Blockungsdaten.
	 */
	public constructor(pRandom : Random, pLogger : Logger, pDynDat : KursblockungDynDaten) {
		super(pRandom, pLogger, pDynDat);
	}

	public toString() : String {
		return "Schülervorschlag";
	}

	/**
	 * Der Algorithmus entfernt zunächst alle SuS aus ihren Kursen. Anschließend werden die Kurse zufällig verteilt.
	 * Anschließend verändert der Algorithmus die Lage eines zufälligen Kurses. Falls sich die Bewertung verschlechter,
	 * wird die Veränderung rückgängig gemacht.
	 */
	public berechne(pEndzeit : number) : void {
		let current : number = System.currentTimeMillis();
		let halbzeit : number = current + Math.trunc((pEndzeit - current) / 2);
		if (this.dynDaten.gibKurseDieFreiSindAnzahl() === 0) 
			return;
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		this.dynDaten.aktionKurseFreieZufaelligVerteilen();
		this.dynDaten.aktionZustandSpeichernK();
		let countKeineVerbesserung : number = 0;
		do {
			countKeineVerbesserung = this.verteileKurseMitSchuelerwunsch() ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < KursblockungAlgorithmusKSchuelervorschlag.MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG) && (System.currentTimeMillis() < halbzeit));
		countKeineVerbesserung = 0;
		do {
			countKeineVerbesserung = this.verteileKurseZufaelligEinWenig() ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < KursblockungAlgorithmusKSchuelervorschlag.MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG) && (System.currentTimeMillis() < pEndzeit));
	}

	/**
	 * Kurslage wird durch Schüler-Wünsche verändert. Falls sich die Bewertung verschlechter, wird die Veränderung
	 * rückgängig gemacht.
	 */
	private verteileKurseMitSchuelerwunsch() : boolean {
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		this.dynDaten.aktionKurseVerteilenNachSchuelerwunsch();
		this.dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
		let compare : number = this.dynDaten.gibCompareZustandK_NW_KD_FW();
		if (compare >= 0) {
			this.dynDaten.aktionZustandSpeichernK();
			return compare > 0;
		}
		this.dynDaten.aktionZustandLadenK();
		return false;
	}

	/**
	 * Kurslage wird ein wenig zufällig verändert und bewertet. Falls sich die Bewertung verschlechter, wird die
	 * Veränderung rückgängig gemacht.
	 */
	private verteileKurseZufaelligEinWenig() : boolean {
		do {
			this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			this.dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
			this.dynDaten.aktionSchuelerVerteilenMitGewichtetenBipartitemMatching();
			let cmp : number = this.dynDaten.gibCompareZustandK_NW_KD_FW();
			if (cmp > 0) {
				this.dynDaten.aktionZustandSpeichernK();
				return true;
			}
		} while (this._random.nextBoolean());
		this.dynDaten.aktionZustandLadenK();
		return false;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusKSchuelervorschlag', 'de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusK'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusKSchuelervorschlag(obj : unknown) : KursblockungAlgorithmusKSchuelervorschlag {
	return obj as KursblockungAlgorithmusKSchuelervorschlag;
}
