import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { FoerderschwerpunkteState } from "@ui/states/kataloge/FoerderschwerpunkteState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface FoerderschwerpunkteReactiveState {
	foerderschwerpunkte: List<FoerderschwerpunktEintrag>;
	foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag>;
}

const KATALOG_LABEL = "Foerderschwerpunkte";

/** Implementierung des States für den Foerderschwerpunktekatalog */
export class FoerderschwerpunkteStateImpl extends StateManager<FoerderschwerpunkteReactiveState> implements FoerderschwerpunkteState {

	private readonly _foerderschwerpunkte: KatalogState<FoerderschwerpunktEintrag>;

	public constructor() {
		super({
			foerderschwerpunkte: new ArrayList(),
			foerderschwerpunkteById: new Map(),
		});

		this._foerderschwerpunkte = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.foerderschwerpunkte,
			getById: () => this.state.foerderschwerpunkteById,
			updateState: (list, byId) => this.setPatchedState({ foerderschwerpunkte: list, foerderschwerpunkteById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ foerderschwerpunkte: list, foerderschwerpunkteById: byId }),
			apiGet: () => api.server.getKatalogFoerderschwerpunkte(api.schema),
			apiAdd: (data) => api.server.addKatalogFoerderschwerpunkt(data, api.schema),
			apiPatch: (id, data) => api.server.patchKatalogFoerderschwerpunkt(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteKatalogFoerderschwerpunkte(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this._foerderschwerpunkte.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get foerderschwerpunkte(): KatalogState<FoerderschwerpunktEintrag> {
		return this._foerderschwerpunkte;
	}
}

export const foerderschwerpunkteStateImpl = new FoerderschwerpunkteStateImpl();