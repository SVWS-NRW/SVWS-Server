import type { Merkmal } from "@core/core/data/schule/Merkmal";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import type { MerkmaleState } from "@ui/states/kataloge/MerkmaleState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface MerkmaleReactiveState {
	merkmale: List<Merkmal>;
	merkmaleById: Map<number, Merkmal>;
}

const KATALOG_LABEL = "Merkmale";

/** Implementierung des States für den Merkmalekatalog */
export class MerkmaleStateImpl extends StateManager<MerkmaleReactiveState> implements MerkmaleState {

	private readonly _merkmale: KatalogState<Merkmal>;

	public constructor() {
		super({
			merkmale: new ArrayList(),
			merkmaleById: new Map(),
		});

		this._merkmale = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.merkmale,
			getById: () => this.state.merkmaleById,
			updateState: (list, byId) => this.setPatchedState({ merkmale: list, merkmaleById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ merkmale: list, merkmaleById: byId }),
			apiGet: () => api.server.getMerkmale(api.schema),
			apiAdd: (data) => api.server.addMerkmal(data, api.schema),
			apiPatch: (id, data) => api.server.patchMerkmal(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteMerkmale(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.merkmale.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get merkmale(): KatalogState<Merkmal> {
		return this._merkmale;
	}
}

export const merkmaleStateImpl = new MerkmaleStateImpl();
