
import type { Betrieb } from "@core/core/data/schule/Betrieb";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { BetriebeState } from "@ui/states/kataloge/BetriebeState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface BetriebeReactiveState {
	betriebe: List<Betrieb>;
	betriebeById: Map<number, Betrieb>;
}

/** Implementierung des States für den Betriebekatalog */
export class BetriebeStateImpl extends StateManager<BetriebeReactiveState> implements BetriebeState {

	private readonly _betriebe: KatalogState<Betrieb>;

	public constructor() {
		super({
			betriebe: new ArrayList(),
			betriebeById: new Map(),
		});

		this._betriebe = createKatalogState({
			katalogLabel: 'Betriebe',
			getList: () => this.state.betriebe,
			getById: () => this.state.betriebeById,
			updateState: (list, byId) => this.setPatchedState({ betriebe: list, betriebeById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ betriebe: list, betriebeById: byId }),
			apiGet: () => api.server.getBetriebe(api.schema),
			apiAdd: (data) => api.server.addBetrieb(data, api.schema),
			apiPatch: (id, data) => api.server.patchBetrieb(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteBetriebe(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.betriebe.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden der Kataloge 'Betrieb' ist fehlgeschlagen.`
			);
		}
	}

	public get betriebe(): KatalogState<Betrieb> {
		return this._betriebe;
	}
}

export const betriebeStateImpl = new BetriebeStateImpl();
