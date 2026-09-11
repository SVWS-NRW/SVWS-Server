import { JavaObject } from '../../../java/lang/JavaObject';
import { UvPlanungsabschnitt } from '../../../core/data/uv/UvPlanungsabschnitt';
import { UvLerngruppenLehrer } from '../../../core/data/uv/UvLerngruppenLehrer';
import { UvAlgorithmusDynDaten } from '../../../core/utils/uvblockung/UvAlgorithmusDynDaten';
import { UvRegelManager } from '../../../core/utils/uv/UvRegelManager';
import { UvManager } from '../../../core/utils/uv/UvManager';
import { Logger } from '../../../core/logger/Logger';
import { JavaMath } from '../../../java/lang/JavaMath';
import { LogLevel } from '../../../core/logger/LogLevel';
import { System } from '../../../java/lang/System';
import { UvAlgorithmusImplAbstract } from '../../../core/utils/uvblockung/UvAlgorithmusImplAbstract';
import { Random } from '../../../java/util/Random';
import { UvAlgorithmusImplRandomWalk } from '../../../core/utils/uvblockung/UvAlgorithmusImplRandomWalk';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusImpl extends JavaObject {

	/**
	 * Ein Logger für Debug-Zwecke.
	 */
	private readonly log: Logger;

	/**
	 * Ein Random-Objekt für Zufallsentscheidungen bei der Berechnung.
	 */
	private readonly rnd: Random;

	/**
	 * Die Eingabedaten von der GUI.
	 */
	private readonly man: UvManager;

	/**
	 * Zur dynamischen Bewertung der aktuellen Zuordnung.
	 */
	private readonly dyn: UvAlgorithmusDynDaten;

	/**
	 * Alle Algorithmen, die für die Lehrkraft-Lerngruppen-Zuordnung implementiert wurden.
	 */
	private readonly alg: Array<UvAlgorithmusImplAbstract>;


	/**
	 * Der Konstruktor.
	 *
	 * @param manager             Ein {@link UvManager}-Objekt, welches alle Daten hat.
	 * @param manRegeln           Ein {@link UvRegelManager}-Objekt, der alle Regeln hat.
	 * @param planungsabschnitt   Der aktuelle Planungsabschnitt.
	 */
	public constructor(manager: UvManager, manRegeln: UvRegelManager, planungsabschnitt: UvPlanungsabschnitt) {
		super();
		this.log = Logger.global();
		this.man = manager;
		const seed: number = new Random().nextLong();
		this.rnd = new Random(seed);
		this.log.logLn(LogLevel.INFO, "UvAlgorithmus startet mit Seed " + seed + ".");
		this.dyn = new UvAlgorithmusDynDaten(this.log, this.rnd, manager, manRegeln, planungsabschnitt);
		this.alg = [new UvAlgorithmusImplRandomWalk(this.log, this.rnd, this.dyn)];
	}

	/**
	 * Optimiert die derzeitige Lehrkraft-Lerngruppen-Zuordnung.
	 *
	 * @param zeitlimit   Die Zeitspanne (in ms), die für die Berechnung zur Verfügung steht.
	 */
	public berechneInnerhalb(zeitlimit: number): void {
		const zeitEndeGesamt: number = System.currentTimeMillis() + zeitlimit;
		const zeitProAlgorithmus: number = Math.max(10, Math.trunc(zeitlimit / (this.alg.length)));
		this.log.logLn(LogLevel.INFO, "Schleife-Vorher: Malus = " + this.dyn.gibMalusBeschreibung());
		while (System.currentTimeMillis() < zeitEndeGesamt) {
			for (let i: number = 0; i < this.alg.length; i++) {
				this.alg[i].berechneInnerhalb(zeitProAlgorithmus);
			}
		}
		this.log.logLn(LogLevel.INFO, "Schleife-Danach: Malus1 = " + this.dyn.gibMalusBeschreibung());
	}

	/**
	 * Liefert die bisher beste Lehrkraft-Lerngruppen-Zuordnung.
	 *
	 * @return die bisher beste Lehrkraft-Lerngruppen-Zuordnung.
	 */
	public gibBestesAktuellesErgebnis(): List<UvLerngruppenLehrer> {
		return this.dyn.gibAktuelleZuordnung();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImpl';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImpl'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusImpl>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusImpl');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusImpl(obj: unknown): UvAlgorithmusImpl {
	return obj as UvAlgorithmusImpl;
}
