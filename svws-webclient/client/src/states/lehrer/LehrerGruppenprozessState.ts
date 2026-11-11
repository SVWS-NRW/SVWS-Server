import type { LehrerStammdaten } from "@core/asd/data/lehrer/LehrerStammdaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import type { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";

import type { GenericAuswahlMultiHost } from "../GenericAuswahlState";
import { GenericGruppenprozessStateImpl } from "../GenericGruppenprozessStateImpl";
import { api } from "~/router/Api";

/**
 * Der State für die Stammdaten der Mehrfachauswahl (Gruppenprozesse) der Lehrer
 */
export class LehrerGruppenprozessState extends GenericGruppenprozessStateImpl<LehrerListeEintrag, LehrerStammdaten> {

	/**
	 * Erzeugt einen neuen State für die Stammdaten der Mehrfachauswahl der Lehrer.
	 *
	 * @param auswahl   der Auswahl-State, an den sich der State anhängt
	 */
	public constructor(auswahl: GenericAuswahlMultiHost<LehrerListeEintrag>) {
		super(daten => daten.id, auswahl);
	}

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<LehrerStammdaten> | null> {
		if (ids.isEmpty()) {
			return null;
		}
		return await api.server.getLehrerStammdatenMultiple(ids, api.schema);
	}

	public override async patchMultiple(pendingStateManager: PendingStateManager<any>): Promise<void> {
		// Der Server bietet derzeit keinen Endpunkt zum Patchen mehrerer Lehrer an. Daher wird kein Patch lokal übernommen,
		// der den Server nie erreicht.
		throw new DeveloperNotificationException("Das Patchen mehrerer Lehrer wird vom Server noch nicht unterstützt.");
	}

}
