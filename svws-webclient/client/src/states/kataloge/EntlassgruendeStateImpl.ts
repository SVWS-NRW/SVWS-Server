
import type { KatalogEntlassgrund } from "@core/core/data/kataloge/KatalogEntlassgrund";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { EntlassgruendeState } from "@ui/states/kataloge/EntlassgruendeState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface EntlassgruendeReactiveState {
	entlassgruende: List<KatalogEntlassgrund>;
	entlassgruendeById: Map<number, KatalogEntlassgrund>;
}

const KATALOG_LABEL = "Entlassgründe";

/** Implementierung des States für den Entlassgründekatalog */
export class EntlassgruendeStateImpl extends StateManager<EntlassgruendeReactiveState> implements EntlassgruendeState {

	private readonly _entlassgruende: KatalogState<KatalogEntlassgrund>;

	public constructor() {
		super({
			entlassgruende: new ArrayList(),
			entlassgruendeById: new Map(),
		});

		this._entlassgruende = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.entlassgruende,
			getById: () => this.state.entlassgruendeById,
			updateState: (list, byId) => this.setPatchedState({ entlassgruende: list, entlassgruendeById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ entlassgruende: list, entlassgruendeById: byId }),
			apiGet: () => api.server.getEntlassgruende(api.schema),
			apiAdd: (data) => api.server.addEntlassgrund(data, api.schema),
			apiPatch: (id, data) => api.server.patchEntlassgrund(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteEntlassgruende(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.entlassgruende.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden der Kataloge '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get entlassgruende(): KatalogState<KatalogEntlassgrund> {
		return this._entlassgruende;
	}
}

export const entlassgruendeStateImpl = new EntlassgruendeStateImpl();
