import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import type { LeitungsfunktionenState } from "@ui/states/kataloge/LeitungsfunktionenState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface LeitungsfunktionenReactiveState {
	leitungsfunktionen: List<Leitungsfunktion>;
	leitungsfunktionenById: Map<number, Leitungsfunktion>;
}

/** Implementierung des States für Leitungsfunktionen */
export class LeitungsfunktionenStateImpl extends StateManager<LeitungsfunktionenReactiveState> implements LeitungsfunktionenState {

	private readonly _leitungsfunktionen: KatalogState<Leitungsfunktion>;

	public constructor() {
		super({
			leitungsfunktionen: new ArrayList(),
			leitungsfunktionenById: new Map(),
		});

		this._leitungsfunktionen = createKatalogState<Leitungsfunktion>({
			katalogLabel: 'Leitungsfunktionen',
			getList: () => this.state.leitungsfunktionen,
			getById: () => this.state.leitungsfunktionenById,
			updateState: (list, byId) => this.setPatchedState({ leitungsfunktionen: list, leitungsfunktionenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ leitungsfunktionen: list, leitungsfunktionenById: byId }),
			apiGet: () => api.server.getLeitungsfunktionen(api.schema),
			apiAdd: (data) => api.server.addLeitungsfunktion(data, api.schema),
			apiPatch: (id, data) => api.server.patchLeitungsfunktion(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteLeitungsfunktionen(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this._leitungsfunktionen.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden der Kataloge 'Leitungsfunktionen' ist fehlgeschlagen.`
			);
		}
	}

	public get leitungsfunktionen(): KatalogState<Leitungsfunktion> {
		return this._leitungsfunktionen;
	}

}

export const leitungsfunktionenStateImpl = new LeitungsfunktionenStateImpl();
