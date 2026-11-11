import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { List } from "@core/java/util/List";

import type { GenericAuswahlMultiHost } from "../GenericAuswahlState";
import { GenericGruppenprozessStateImpl } from "../GenericGruppenprozessStateImpl";
import { api } from "~/router/Api";

/**
 * Der State für die Stammdaten der Mehrfachauswahl (Gruppenprozesse) der Klassen
 */
export class KlassenGruppenprozessState extends GenericGruppenprozessStateImpl<KlassenListeEintrag, KlassenDaten> {

	/**
	 * Erzeugt einen neuen State für die Stammdaten der Mehrfachauswahl der Klassen.
	 *
	 * @param auswahl   der Auswahl-State, an den sich der State anhängt
	 */
	public constructor(auswahl: GenericAuswahlMultiHost<KlassenListeEintrag>) {
		super(daten => daten.id, auswahl);
	}

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<KlassenDaten> | null> {
		if (ids.isEmpty()) {
			return null;
		}
		return await api.server.getKlassenMultiple(ids, api.schema);
	}

}
