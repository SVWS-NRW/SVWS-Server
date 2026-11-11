import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { List } from "@core/java/util/List";
import type { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";

import type { GenericAuswahlMultiHost } from "../GenericAuswahlState";
import { GenericGruppenprozessStateImpl } from "../GenericGruppenprozessStateImpl";
import { api } from "~/router/Api";

/**
 * Der State für die Stammdaten der Mehrfachauswahl (Gruppenprozesse) der Schüler
 */
export class SchuelerGruppenprozessState extends GenericGruppenprozessStateImpl<SchuelerListeEintrag, SchuelerStammdaten> {

	/**
	 * Erzeugt einen neuen State für die Stammdaten der Mehrfachauswahl der Schüler.
	 *
	 * @param auswahl   der Auswahl-State, an den sich der State anhängt
	 */
	public constructor(auswahl: GenericAuswahlMultiHost<SchuelerListeEintrag>) {
		super(daten => daten.id, auswahl);
	}

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<SchuelerStammdaten> | null> {
		if (ids.isEmpty()) {
			return null;
		}
		return await api.server.getSchuelerStammdatenMultiple(ids, api.schema);
	}

	public override async patchMultiple(pendingStateManager: PendingStateManager<any>): Promise<void> {
		api.status.start();

		const partialsToPatch = pendingStateManager.partials;
		await api.server.patchSchuelerStammdatenMultiple(partialsToPatch, api.schema);

		// Übernehme nur geänderte SchuelerStammdaten Objekte in die Mehrfachauswahl, damit nicht alle Stammdaten neugeladen werden müssen
		for (const partialToPatch of partialsToPatch) {
			if (partialToPatch.id !== undefined) {
				const patchId = (partialToPatch as Record<string, any>)[pendingStateManager.idFieldName];
				const currentStammdaten = this.listeDaten.get(patchId);
				this.listeDaten.put(patchId, Object.assign(Object.assign({}, currentStammdaten), partialToPatch));
			}
		}

		pendingStateManager.resetPendingState();
		this.commit();
		api.status.stop();
	}

}
