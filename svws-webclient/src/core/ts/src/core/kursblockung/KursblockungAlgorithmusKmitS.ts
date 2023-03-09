import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { KursblockungAlgorithmusS, cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusS } from './KursblockungAlgorithmusS';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungDynDaten, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynDaten } from './KursblockungDynDaten';
import { KursblockungAlgorithmusK, cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusK } from './KursblockungAlgorithmusK';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../logger/Logger';
import { System, cast_java_lang_System } from '../../java/lang/System';

export class KursblockungAlgorithmusKmitS extends KursblockungAlgorithmusK {

	/**
	 *  Die Anzahl an Runden ohne Verbesserung, bevor es zum Abbruch kommt.
	 */
	private static readonly MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG : number = 2000;

	private readonly algorithmusS : KursblockungAlgorithmusS;


	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 * 
	 * @param pRandom       Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger       Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDat       Die dynamischen Blockungsdaten.
	 * @param pAlgorithmusS Der Algorithmus zum Verteilen der SuS.
	 */
	public constructor(pRandom : Random, pLogger : Logger, pDynDat : KursblockungDynDaten, pAlgorithmusS : KursblockungAlgorithmusS) {
		super(pRandom, pLogger, pDynDat);
		this.algorithmusS = pAlgorithmusS;
	}

	/**
	 * Der Algorithmus entfernt zunächst alle SuS aus ihren Kursen. Anschließend werden die Kurse zufällig verteilt.
	 * Anschließend verändert der Algorithmus die Lage eines zufälligen Kurses. Falls sich die Bewertung verschlechtert,
	 * wird die Veränderung rückgängig gemacht.
	 */
	public berechne(pMaxTimeMillis : number) : void {
		if (this.dynDaten.gibKurseDieFreiSindAnzahl() === 0) {
			return;
		}
		let timeStart : number = System.currentTimeMillis();
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		this.dynDaten.aktionKurseFreieZufaelligVerteilen();
		this.dynDaten.aktionZustandSpeichernK();
		let countKeineVerbesserung : number = 0;
		do {
			countKeineVerbesserung = this.verteileKurse(pMaxTimeMillis) ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < KursblockungAlgorithmusKmitS.MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG) && (System.currentTimeMillis() - timeStart < pMaxTimeMillis));
	}

	/**
	 * Die Lage einiger Kurse wird verändert. Falls sich die Bewertung verschlechtert, wird die Veränderung rückgängig
	 * gemacht.
	 * 
	 * @param pMaxTimeMillis Die maximale Blockungszeit für den S-Algorithmus.
	 * @return TRUE, falls es zur Verbesserung kam.
	 */
	private verteileKurse(pMaxTimeMillis : number) : boolean {
		do {
			this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			this.dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
			this.algorithmusS.berechne();
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
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusKmitS', 'de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusK'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusKmitS(obj : unknown) : KursblockungAlgorithmusKmitS {
	return obj as KursblockungAlgorithmusKmitS;
}
