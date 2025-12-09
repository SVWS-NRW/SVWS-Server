import { AuswahlManager } from "../../../AuswahlManager";
import type { Betrieb } from '../../../../../../core/src/core/data/schule/Betrieb';
import type { List } from '../../../../../../core/src/java/util/List';
import type { Schuljahresabschnitt } from '../../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Schulform } from '../../../../../../core/src/asd/types/schule/Schulform';
import { ArrayList } from '../../../../../../core/src/java/util/ArrayList';
import type { Comparator } from '../../../../../../core/src/java/util/Comparator';
import { JavaInteger } from '../../../../../../core/src/java/lang/JavaInteger';
import { JavaString } from '../../../../../../core/src/java/lang/JavaString';
import { JavaLong } from '../../../../../../core/src/java/lang/JavaLong';
import type { JavaFunction } from '../../../../../../core/src/java/util/function/JavaFunction';
import { HashSet } from "../../../../../../core/src/java/util/HashSet";
import type { JavaSet } from "../../../../../../core/src/java/util/JavaSet";


export class BetriebeListeManager extends AuswahlManager<number, Betrieb, Betrieb> {

	private static readonly _betriebToId: JavaFunction<Betrieb, number> = { apply: (a: Betrieb) => a.id };
	private readonly idsReferencedBetriebe: HashSet<number> = new HashSet<number>();

	/**
	 * Ein Default-Comparator für den Vergleich von Betrieben.
	 */
	public static readonly comparator: Comparator<Betrieb> = { compare: (a: Betrieb, b: Betrieb) => {
		let cmp;
		if ((a.sortierung !== null) && (b.sortierung !== null)) {
			cmp = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0)
				return cmp;
		}
		if ((a.name !== null) && (b.name !== null)) {
			cmp = JavaString.compareTo(a.name, b.name);
			if (cmp !== 0)
				return cmp;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  	der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           	die Liste der Schuljahresabschnitte
	 * @param schulform     				  	die Schulform der Schule
	 * @param betriebe							die Liste der Betriebe
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, beschaeftigungsarten: List<Betrieb>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, beschaeftigungsarten,
			BetriebeListeManager.comparator, BetriebeListeManager._betriebToId, BetriebeListeManager._betriebToId, ArrayList.of());
	}

	/**
	 *Gibt das Set mit den Ids der Betriebe zurück, die in der Auswahl sind und in anderen Datenbanktabellen referenziert werden
	 *
	 * @return Das Set mit IDs von Betrieben, die in anderen Datenbanktabellen referenziert werden
	 */
	public getIdsReferencedBetriebe(): JavaSet<number> {
		return this.idsReferencedBetriebe;
	}


	protected checkFilter(eintrag: any): boolean {
		return false;
	}

	protected compareAuswahl(a: any, b: any): number {
		return 0;
	}

}
