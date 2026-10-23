import type { SchulinterneFunktion } from "@core/core/data/schule/SchulinterneFunktion";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import type { SchulinterneFunktionenState } from "@ui/states/kataloge/SchulinterneFunktionenState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface SchulinterneFunktionenReactiveState {
	schulinterneFunktionen: List<SchulinterneFunktion>;
	schulinterneFunktionenById: Map<number, SchulinterneFunktion>;
}

/** Implementierung des States für schulinterne Funktionen */
export class SchulinterneFunktionenStateImpl extends StateManager<SchulinterneFunktionenReactiveState> implements SchulinterneFunktionenState {

	private readonly _schulinterneFunktionen: KatalogState<SchulinterneFunktion>;

	public constructor() {
		super({
			schulinterneFunktionen: new ArrayList(),
			schulinterneFunktionenById: new Map(),
		});

		this._schulinterneFunktionen = createKatalogState<SchulinterneFunktion>({
			katalogLabel: 'Schulinterne Funktionen',
			getList: () => this.state.schulinterneFunktionen,
			getById: () => this.state.schulinterneFunktionenById,
			updateState: (list, byId) => this.setPatchedState({ schulinterneFunktionen: list, schulinterneFunktionenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ schulinterneFunktionen: list, schulinterneFunktionenById: byId }),
			apiGet: () => api.server.getSchulinterneFunktionen(api.schema),
			apiAdd: (data) => api.server.addSchulinterneFunktion(data, api.schema),
			apiPatch: (id, data) => api.server.patchSchulinterneFunktion(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteSchulinterneFunktionen(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this._schulinterneFunktionen.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs 'Schulinterne Funktionen' ist fehlgeschlagen.`
			);
		}
	}

	public get schulinterneFunktionen(): KatalogState<SchulinterneFunktion> {
		return this._schulinterneFunktionen;
	}

}

export const schulinterneFunktionenStateImpl = new SchulinterneFunktionenStateImpl();
