import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { HaltestellenState } from "@ui/states/kataloge/HaltestellenState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface HaltestellenReactiveState {
	haltestellen: List<Haltestelle>;
	haltestellenById: Map<number, Haltestelle>;
}

const KATALOG_LABEL = "Haltestellen";

/** Implementierung des States für den Haltestellenkatalog */
export class HaltestellenStateImpl extends StateManager<HaltestellenReactiveState> implements HaltestellenState {

	private readonly _haltestellen: KatalogState<Haltestelle>;

	public constructor() {
		super({
			haltestellen: new ArrayList(),
			haltestellenById: new Map(),
		});

		this._haltestellen = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.haltestellen,
			getById: () => this.state.haltestellenById,
			updateState: (list, byId) => this.setPatchedState({ haltestellen: list, haltestellenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ haltestellen: list, haltestellenById: byId }),
			apiGet: () => api.server.getHaltestellen(api.schema),
			apiAdd: (data) => api.server.addHaltestelle(data, api.schema),
			apiPatch: (id, data) => api.server.patchHaltestelle(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteHaltestellen(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.haltestellen.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get haltestellen(): KatalogState<Haltestelle> {
		return this._haltestellen;
	}
}

export const haltestellenStateImpl = new HaltestellenStateImpl();
