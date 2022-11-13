import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { KursblockungStatic, cast_de_nrw_schule_svws_core_kursblockung_KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungDynDaten, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { KursblockungAlgorithmusK, cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusK } from '../../core/kursblockung/KursblockungAlgorithmusK';
import { KursblockungDynSchueler, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';
import { System, cast_java_lang_System } from '../../java/lang/System';

export class KursblockungAlgorithmusKMatching2 extends KursblockungAlgorithmusK {

	private static readonly MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG : number = 2000;

	private readonly schuelerAlle : Array<KursblockungDynSchueler>;


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
		this.schuelerAlle = this.dynDaten.gibSchuelerArray(false);
	}

	/**
	 * Der Algorithmus entfernt zunächst alle SuS aus ihren Kursen. Anschließend werden die Kurse zufällig verteilt.
	 * Anschließend verändert der Algorithmus die Lage eines zufälligen Kurses. Falls sich die Bewertung verschlechter,
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
		let runden : number = 0;
		do {
			runden++;
			countKeineVerbesserung = this.verteileKurse() ? 0 : countKeineVerbesserung + 1;
		} while ((countKeineVerbesserung < KursblockungAlgorithmusKMatching2.MAX_RUNDEN_IN_FOLGE_OHNE_VERBESSERUNG) && (System.currentTimeMillis() - timeStart < pMaxTimeMillis));
		console.log(JSON.stringify("runden = " + runden));
	}

	/**
	 * Die Lage einiger Kurse wird verändert. Falls sich die Bewertung verschlechtert, wird die Veränderung rückgängig
	 * gemacht.
	 */
	private verteileKurse() : boolean {
		do {
			this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			this.dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
			this.verteileSuS();
			let b1 : boolean = (this.dynDaten.gibCompareZustandK_NW_KD_FW() > 0) && (this.dynDaten.gibBewertungK_FW_NW_KD_JetztBesser() >= 0);
			let b2 : boolean = (this.dynDaten.gibCompareZustandK_NW_KD_FW() >= 0) && (this.dynDaten.gibBewertungK_FW_NW_KD_JetztBesser() > 0);
			if (b1 || b2) {
				this.dynDaten.aktionZustandSpeichernK();
				return true;
			}
		} while (this._random.nextBoolean());
		this.dynDaten.aktionZustandLadenK();
		return false;
	}

	/**
	 * Verteilt die SuS. Multikurse der SuS werden zufällig verteilt, die anderen Kurse werden mit Hilfe eines
	 * Matching-Algorithmus verteilt.
	 */
	private verteileSuS() : void {
		let perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this.schuelerAlle.length);
		for (let p : number = 0; p < perm.length; p++){
			let i : number = perm[p];
			let schueler : KursblockungDynSchueler | null = this.schuelerAlle[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenMitBipartiteMatching();
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusKMatching2', 'de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusK'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusKMatching2(obj : unknown) : KursblockungAlgorithmusKMatching2 {
	return obj as KursblockungAlgorithmusKMatching2;
}
