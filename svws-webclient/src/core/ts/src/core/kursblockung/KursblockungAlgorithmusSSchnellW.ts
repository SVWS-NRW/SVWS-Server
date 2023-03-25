import { KursblockungAlgorithmusS, cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusS } from '../../core/kursblockung/KursblockungAlgorithmusS';
import { KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { Random } from '../../java/util/Random';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';
import { Logger } from '../../core/logger/Logger';

export class KursblockungAlgorithmusSSchnellW extends KursblockungAlgorithmusS {

	/**
	 *  Array der SuS, deren Kurse verteilt werden sollen.
	 */
	private readonly schuelerArr : Array<KursblockungDynSchueler>;

	/**
	 *  Zur Speicherung einer zufälligen Permutation der Indizes der Schüler.
	 */
	private readonly perm : Array<number>;


	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in dieser Methode noch nicht
	 * auf Schienen verteilt werden.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger zum Protokollieren von Warnungen und Fehlern.
	 * @param pDynDat Die dynamischen Blockungsdaten.
	 */
	public constructor(pRandom : Random, pLogger : Logger, pDynDat : KursblockungDynDaten) {
		super(pRandom, pLogger, pDynDat);
		this.schuelerArr = pDynDat.gibSchuelerArrayAlle();
		this.perm = KursblockungStatic.gibPermutation(this._random, this.schuelerArr.length);
	}

	/**
	 * Der Algorithmus verteilt die SuS auf ihre Kurse zufällig. Kommt es während des Verteilens zur Kollision, so wird
	 * der Kurs nicht gewählt.
	 */
	public berechne() : void {
		this.dynDaten.gibStatistik().aktionBewertungSpeichernS();
		for (let i : number = 0; i < 10; i++)
			this.verteileSchuelerAlle();
	}

	/**
	 * Der Algorithmus verteilt die SuS in zufälliger Reihenfolge ein weiteres Mal zufällig. Falls die Verteilung
	 * schlechter ist, wird der vorherige Zustand wiederhergestellt.
	 *
	 * @return TRUE, falls der Zustand sich verbessert hat.
	 */
	private verteileSchuelerAlle() : boolean {
		let verbesserung : boolean = false;
		KursblockungStatic.aktionPermutiere(this._random, this.perm);
		for (let p : number = 0; p < this.schuelerArr.length; p++) {
			const i : number = this.perm[p];
			verbesserung = verbesserung || this.verteileSchuelerEiner(this.schuelerArr[i]);
		}
		return verbesserung;
	}

	private verteileSchuelerEiner(schueler : KursblockungDynSchueler) : boolean {
		this.dynDaten.gibStatistik().aktionBewertungSpeichernS();
		schueler.aktionZustandSpeichernS();
		schueler.aktionKurseAlleEntfernen();
		schueler.aktionKurseVerteilenNurMultikurseZufaellig();
		schueler.aktionKurseVerteilenNurFachartenMitEinemKurs();
		schueler.aktionKurseVerteilenMitBipartiteMatchingGewichtetem();
		const cmp : number = this.dynDaten.gibStatistik().gibBewertungZustandS_NW_KD();
		if (cmp < 0)
			schueler.aktionZustandLadenS();
		return cmp > 0;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusSSchnellW', 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusS'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusSSchnellW(obj : unknown) : KursblockungAlgorithmusSSchnellW {
	return obj as KursblockungAlgorithmusSSchnellW;
}
