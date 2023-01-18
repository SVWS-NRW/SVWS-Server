import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { KursblockungAlgorithmusSSchnellW, cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusSSchnellW } from '../../core/kursblockung/KursblockungAlgorithmusSSchnellW';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungDynDaten, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { KursblockungAlgorithmusK, cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusK } from '../../core/kursblockung/KursblockungAlgorithmusK';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';
import { System, cast_java_lang_System } from '../../java/lang/System';

export class KursblockungAlgorithmusKOptimiereBest extends KursblockungAlgorithmusK {

	/**
	 *  Mit diesem Algorithmus werden die SuS verteilt.
	 */
	private algoS : KursblockungAlgorithmusSSchnellW;


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
		this.algoS = new KursblockungAlgorithmusSSchnellW(pRandom, pLogger, pDynDat);
	}

	public toString() : String {
		return "OptimiereBest";
	}

	public berechne(pEndzeit : number) : void {
		if (this.dynDaten.gibKurseDieFreiSindAnzahl() === 0) 
			return;
		this.dynDaten.aktionZustandLadenG();
		this.dynDaten.aktionZustandSpeichernK();
		do {
			this.veraendereDieKurslageZufaelligEinWenig();
		} while (System.currentTimeMillis() < pEndzeit);
	}

	/**
	 * Kurslage wird ein wenig zufällig verändert und bewertet. Falls sich die Bewertung verschlechtert, wird die
	 * Veränderung rückgängig gemacht.
	 */
	private veraendereDieKurslageZufaelligEinWenig() : void {
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusKOptimiereBest', 'de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusK'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusKOptimiereBest(obj : unknown) : KursblockungAlgorithmusKOptimiereBest {
	return obj as KursblockungAlgorithmusKOptimiereBest;
}
