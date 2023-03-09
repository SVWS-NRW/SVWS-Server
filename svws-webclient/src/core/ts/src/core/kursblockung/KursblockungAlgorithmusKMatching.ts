import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungDynDaten, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynDaten } from './KursblockungDynDaten';
import { KursblockungAlgorithmusK, cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusK } from './KursblockungAlgorithmusK';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../logger/Logger';
import { System, cast_java_lang_System } from '../../java/lang/System';

export class KursblockungAlgorithmusKMatching extends KursblockungAlgorithmusK {

	/**
	 *  Die Anzahl an Runden ohne Verbesserung, bevor es zum Abbruch kommt.
	 */
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

	public toString() : string {
		return "Matching";
	}

	/**
	 * Der Algorithmus verändert die Kurslage zufällig und bewertet die neue Lage mit dem Algorithmus
	 * {@link KursblockungAlgorithmusSMatching}. Falls sich die Bewertung verschlechtert, wird die Veränderung
	 * rückgängig gemacht.
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
			countKeineVerbesserung = this.verteileKurseMitMatching() ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < KursblockungAlgorithmusKMatching.MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG) && (System.currentTimeMillis() < halbzeit));
		countKeineVerbesserung = 0;
		do {
			countKeineVerbesserung = this.verteileKurseMitMatchingW() ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < KursblockungAlgorithmusKMatching.MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG) && (System.currentTimeMillis() < pEndzeit));
	}

	/**
	 * Die Lage einiger Kurse wird verändert. Falls sich die Bewertung verschlechtert, wird die Veränderung rückgängig
	 * gemacht.
	 */
	private verteileKurseMitMatching() : boolean {
		do {
			this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			this.dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
			this.dynDaten.aktionSchuelerVerteilenMitBipartitemMatching();
			if (this.dynDaten.gibCompareZustandK_NW_KD_FW() > 0) {
				this.dynDaten.aktionZustandSpeichernK();
				return true;
			}
		} while (this._random.nextBoolean());
		this.dynDaten.aktionZustandLadenK();
		return false;
	}

	/**
	 * Kurslage wird ein wenig zufällig verändert und bewertet. Falls sich die Bewertung verschlechtert, wird die
	 * Veränderung rückgängig gemacht.
	 */
	private verteileKurseMitMatchingW() : boolean {
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
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusK', 'de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusKMatching'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusKMatching(obj : unknown) : KursblockungAlgorithmusKMatching {
	return obj as KursblockungAlgorithmusKMatching;
}
