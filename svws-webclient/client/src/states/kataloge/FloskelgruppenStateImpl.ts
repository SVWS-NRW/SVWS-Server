import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { FloskelgruppenState } from "@ui/states/kataloge/FloskelgruppenState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface FloskelgruppenReactiveState {
	floskelgruppen: List<Floskelgruppe>;
	floskelgruppenById: Map<number, Floskelgruppe>;
}

const KATALOG_LABEL = "Floskelgruppen";

/** Implementierung des States für den Floskelgruppenkatalog */
export class FloskelgruppenStateImpl extends StateManager<FloskelgruppenReactiveState> implements FloskelgruppenState {

	private readonly _floskelgruppen: KatalogState<Floskelgruppe>;

	public constructor() {
		super({
			floskelgruppen: new ArrayList(),
			floskelgruppenById: new Map(),
		});

		this._floskelgruppen = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.floskelgruppen,
			getById: () => this.state.floskelgruppenById,
			updateState: (list, byId) => this.setPatchedState({ floskelgruppen: list, floskelgruppenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ floskelgruppen: list, floskelgruppenById: byId }),
			apiGet: () => api.server.getFloskelgruppen(api.schema),
			apiAdd: (data) => api.server.addFloskelgruppe(data, api.schema),
			apiPatch: (id, data) => api.server.patchFloskelgruppe(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteFloskelgruppen(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.floskelgruppen.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get floskelgruppen(): KatalogState<Floskelgruppe> {
		return this._floskelgruppen;
	}
}

export const floskelgruppenStateImpl = new FloskelgruppenStateImpl();
