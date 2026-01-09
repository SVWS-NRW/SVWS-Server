import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import type { Lernplattform } from '../../../../../core/src/core/data/schule/Lernplattform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";

export class LernplattformListeManager extends AuswahlManager<number, Lernplattform, Lernplattform> {

	private static readonly _lernplattformenToId: JavaFunction<Lernplattform, number> = { apply: (ea: Lernplattform) => ea.id };
	private readonly idsReferencedLernplattformen: HashSet<number> = new HashSet<number>();

	/**
	 * Ein Default-Comparator für den Vergleich von Lernplattformen in Lernplattformlisten.
	 */
	public static readonly comparator: Comparator<Lernplattform> = { compare: (a: Lernplattform, b: Lernplattform) => {
		const cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		if (cmp !== 0) {
			return cmp;
		}
		return JavaLong.compare(a.id, b.id);
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Lernplattform bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param lernplattformen     	   die Liste der Lernplattform
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, lernplattformen: List<Lernplattform>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, lernplattformen, LernplattformListeManager.comparator,
			LernplattformListeManager._lernplattformenToId, LernplattformListeManager._lernplattformenToId, ArrayList.of());
	}

	/**
	 *Gibt das Set mit den Ids der Lernplattformen zurück, die in der Auswahl sind und in anderen Datenbanktabellen referenziert werden
	 *
	 * @return Das Set mit IDs von Lernplattformen, die in anderen Datenbanktabellen referenziert werden
	 */
	public getIdsReferencedLernplattformen(): JavaSet<number> {
		return this.idsReferencedLernplattformen;
	}

	protected onMehrfachauswahlChanged(): void {
		this.idsReferencedLernplattformen.clear();
		for (const l of this.liste.auswahl()) {
			if ((l.referenziertInAnderenTabellen !== null) && l.referenziertInAnderenTabellen) {
				this.idsReferencedLernplattformen.add(l.id);
			}
		}
	}

	protected compareAuswahl(a: Lernplattform, b: Lernplattform): number {
		return LernplattformListeManager.comparator.compare(a, b);
	}

	protected checkFilter(): boolean {
		return true;
	}
}

