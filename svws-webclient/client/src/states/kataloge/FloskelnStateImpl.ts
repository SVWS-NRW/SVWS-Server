import type { Floskel } from "@core/core/data/schule/Floskel";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { FloskelnState } from "@ui/states/kataloge/FloskelnState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface FloskelnReactiveState {
	floskeln: List<Floskel>;
	floskelnById: Map<number, Floskel>;
}

const KATALOG_LABEL = "Floskeln";

/** Implementierung des States für den Floskelnkatalog */
export class FloskelnStateImpl extends StateManager<FloskelnReactiveState> implements FloskelnState {

	private readonly _floskeln: KatalogState<Floskel>;

	public constructor() {
		super({
			floskeln: new ArrayList(),
			floskelnById: new Map(),
		});

		this._floskeln = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.floskeln,
			getById: () => this.state.floskelnById,
			updateState: (list, byId) => this.setPatchedState({ floskeln: list, floskelnById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ floskeln: list, floskelnById: byId }),
			apiGet: () => api.server.getFloskeln(api.schema),
			apiAdd: (data) => api.server.addFloskel(data, api.schema),
			apiPatch: (id, data) => api.server.patchFloskeln(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteFloskeln(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.floskeln.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get floskeln(): KatalogState<Floskel> {
		return this._floskeln;
	}
}

export const floskelnStateImpl = new FloskelnStateImpl();
