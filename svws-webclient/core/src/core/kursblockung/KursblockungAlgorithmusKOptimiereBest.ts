import { KursblockungAlgorithmusSSchnellW } from '../../core/kursblockung/KursblockungAlgorithmusSSchnellW';
import { Random } from '../../java/util/Random';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { KursblockungAlgorithmusK, cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusK } from '../../core/kursblockung/KursblockungAlgorithmusK';
import { Class } from '../../java/lang/Class';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';

export class KursblockungAlgorithmusKOptimiereBest extends KursblockungAlgorithmusK {

	/**
	 * Mit diesem Algorithmus werden die SuS verteilt.
	 */
	private readonly algoS: KursblockungAlgorithmusSSchnellW;


	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 *
	 * @param random     Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger     Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param dynDaten   Die dynamischen Blockungsdaten.
	 */
	public constructor(random: Random, logger: Logger, dynDaten: KursblockungDynDaten) {
		super(random, logger, dynDaten);
		this.algoS = new KursblockungAlgorithmusSSchnellW(random, logger, dynDaten);
	}

	public toString(): string {
		return "KursblockungAlgorithmusKOptimiereBest";
	}

	public berechne(pEndzeit: number): void {
		if (this.dynDaten.gibKurseDieFreiSindAnzahl() === 0) {
			return;
		}
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
	private veraendereDieKurslageZufaelligEinWenig(): void {
		do {
			this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
			this.dynDaten.aktionKursVerteilenEinenZufaelligenFreien();
			this.algoS.berechne();
			if (this.dynDaten.gibCompareZustandK1NW2KD3FW() > 0) {
				this.dynDaten.aktionZustandSpeichernK();
				return;
			}
		} while (this.rnd.nextBoolean());
		this.dynDaten.aktionZustandLadenK();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusKOptimiereBest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusKOptimiereBest', 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusK'].includes(name);
	}

	public static readonly class = new Class<KursblockungAlgorithmusKOptimiereBest>('de.svws_nrw.core.kursblockung.KursblockungAlgorithmusKOptimiereBest');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusKOptimiereBest(obj: unknown): KursblockungAlgorithmusKOptimiereBest {
	return obj as KursblockungAlgorithmusKOptimiereBest;
}
