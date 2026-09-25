import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import type { KindergaertenState } from "@ui/states/kataloge/KindergaertenState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface KindergaertenReactiveState {
	kindergaerten: List<Kindergarten>;
	kindergaertenById: Map<number, Kindergarten>;
}

const KATALOG_LABEL = "Kindergärten";

/** Implementierung des States für den Kindergaertenkatalog */
export class KindergaertenStateImpl extends StateManager<KindergaertenReactiveState> implements KindergaertenState {

	private readonly _kindergaerten: KatalogState<Kindergarten>;

	public constructor() {
		super({
			kindergaerten: new ArrayList(),
			kindergaertenById: new Map(),
		});

		this._kindergaerten = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.kindergaerten,
			getById: () => this.state.kindergaertenById,
			updateState: (list, byId) => this.setPatchedState({ kindergaerten: list, kindergaertenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ kindergaerten: list, kindergaertenById: byId }),
			apiGet: () => api.server.getKindergaerten(api.schema),
			apiAdd: (data) => api.server.addKindergarten(data, api.schema),
			apiPatch: (id, data) => api.server.patchKindergarten(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteKindergaerten(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.kindergaerten.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get kindergaerten(): KatalogState<Kindergarten> {
		return this._kindergaerten;
	}
}

export const kindergaertenStateImpl = new KindergaertenStateImpl();
