import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { JahrgaengeState } from "@ui/states/kataloge/JahrgaengeState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface JahrgaengeReactiveState {
	jahrgaenge: List<JahrgangsDaten>;
	jahrgaengeById: Map<number, JahrgangsDaten>;
}

const KATALOG_LABEL = "Jahrgänge";

/** Implementierung des States für den Jahrgaengekatalog */
export class JahrgaengeStateImpl extends StateManager<JahrgaengeReactiveState> implements JahrgaengeState {

	private readonly _jahrgaenge: KatalogState<JahrgangsDaten>;

	public constructor() {
		super({
			jahrgaenge: new ArrayList(),
			jahrgaengeById: new Map(),
		});

		this._jahrgaenge = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.jahrgaenge,
			getById: () => this.state.jahrgaengeById,
			updateState: (list, byId) => this.setPatchedState({ jahrgaenge: list, jahrgaengeById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ jahrgaenge: list, jahrgaengeById: byId }),
			apiGet: () => api.server.getJahrgaenge(api.schema),
			apiAdd: (data) => api.server.addJahrgang(data, api.schema),
			apiPatch: (id, data) => api.server.patchJahrgang(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteJahrgaenge(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.jahrgaenge.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get jahrgaenge(): KatalogState<JahrgangsDaten> {
		return this._jahrgaenge;
	}
}

export const jahrgaengeStateImpl = new JahrgaengeStateImpl();
