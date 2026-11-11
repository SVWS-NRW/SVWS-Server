import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

import type { GenericAuswahlMultiHost } from "../GenericAuswahlState";
import { GenericGruppenprozessStateImpl } from "../GenericGruppenprozessStateImpl";

/**
 * Der State für die Stammdaten der Mehrfachauswahl (Gruppenprozesse) der Kurse. Die Stammdaten eines Kurses sind
 * vollständig in der Auswahlliste enthalten, d.h. der Eintrag der Liste und die Stammdaten sind dasselbe Objekt.
 */
export class KurseGruppenprozessState extends GenericGruppenprozessStateImpl<KursDaten, KursDaten> {

	/**
	 * Erzeugt einen neuen State für die Stammdaten der Mehrfachauswahl der Kurse.
	 *
	 * @param auswahl   der Auswahl-State, an den sich der State anhängt
	 */
	public constructor(auswahl: GenericAuswahlMultiHost<KursDaten>) {
		super(daten => daten.id, auswahl);
	}

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<KursDaten> | null> {
		// Die Daten sind vollständig in der Liste enthalten, kein Aufruf der API notwendig
		const eintraege = new ArrayList<KursDaten>();
		for (const id of ids) {
			eintraege.add(this.getEintragOrException(id));
		}
		return eintraege;
	}

}
