import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class LernplattformListeManager extends AuswahlManager<number, Lernplattform, Lernplattform> {

	private static readonly _lernplattformenToId = (ea: Lernplattform) => ea.id;
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
			LernplattformListeManager._lernplattformenToId, LernplattformListeManager._lernplattformenToId, []);
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

