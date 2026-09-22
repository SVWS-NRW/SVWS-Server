import type { Beschaeftigungsart } from "@core/core/data/schule/Beschaeftigungsart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { BeschaeftigungsartenState } from "@ui/states/kataloge/BeschaeftigungsartenState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface BeschaeftigungsartenReactiveState {
	beschaeftigungsarten: List<Beschaeftigungsart>;
	beschaeftigungsartenById: Map<number, Beschaeftigungsart>;
}

/** Implementierung des States für Beschäftigungsarten */
export class BeschaeftigungsartenStateImpl extends StateManager<BeschaeftigungsartenReactiveState> implements BeschaeftigungsartenState {

	private readonly _beschaeftigungsarten: KatalogState<Beschaeftigungsart>;

	public constructor() {
		super({
			beschaeftigungsarten: new ArrayList(),
			beschaeftigungsartenById: new Map(),
		});

		this._beschaeftigungsarten = createKatalogState<Beschaeftigungsart>({
			katalogLabel: 'Beschäftigungsarten',
			getList: () => this.state.beschaeftigungsarten,
			getById: () => this.state.beschaeftigungsartenById,
			updateState: (list, byId) => this.setPatchedState({ beschaeftigungsarten: list, beschaeftigungsartenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ beschaeftigungsarten: list, beschaeftigungsartenById: byId }),
			apiGet: () => api.server.getBeschaeftigungsarten(api.schema),
			apiAdd: (data) => api.server.addBeschaeftigungsart(data, api.schema),
			apiPatch: (id, data) => api.server.patchBeschaeftigungsart(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteBeschaeftigungsarten(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this._beschaeftigungsarten.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs 'Beschäftigungsarten' ist fehlgeschlagen.`
			);
		}
	}

	public get beschaeftigungsarten(): KatalogState<Beschaeftigungsart> {
		return this._beschaeftigungsarten;
	}

}

export const beschaeftigungsartenStateImpl = new BeschaeftigungsartenStateImpl();
