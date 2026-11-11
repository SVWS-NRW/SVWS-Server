import type { Schulleitung } from "@core/asd/data/schule/Schulleitung";
import type { List } from "@core/java/util/List";

import { GenericDetailListStateImpl } from "../GenericDetailListStateImpl";
import { api } from "~/router/Api";

/**
 * Der State für die Leitungsfunktionen eines Lehrers
 */
export class LehrerLeitungsfunktionenState extends GenericDetailListStateImpl<Schulleitung> {

	public constructor() {
		super(leitungsfunktion => leitungsfunktion.id);
	}

	protected async loadEntries(idAuswahl: number): Promise<List<Schulleitung>> {
		return await api.server.getAllSchulleitungenByLehrer(api.schema, idAuswahl);
	}

	/**
	 * Fügt dem Lehrer eine Leitungsfunktion hinzu.
	 *
	 * @param data       die Daten der Leitungsfunktion
	 * @param idLehrer   die ID des Lehrers
	 */
	public async add(data: Partial<Schulleitung>, idLehrer: number): Promise<void> {
		this.applyAdd(await api.server.addSchulleitung({ ...data, idLehrer }, api.schema));
	}

	/**
	 * Passt eine Leitungsfunktion an.
	 *
	 * @param data        die Änderungen
	 * @param idEintrag   die ID der Leitungsfunktion
	 */
	public async patch(data: Partial<Schulleitung>, idEintrag: number): Promise<void> {
		await api.server.patchSchulleitung(data, api.schema, idEintrag);
		this.applyPatch(idEintrag, data);
	}

	/**
	 * Entfernt Leitungsfunktionen.
	 *
	 * @param idsEintraege   die IDs der Leitungsfunktionen
	 */
	public async remove(idsEintraege: List<number>): Promise<void> {
		await api.server.deleteSchulleitungen(idsEintraege, api.schema);
		this.applyRemove(idsEintraege);
	}

}
