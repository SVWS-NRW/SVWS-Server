import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { FahrschuelerartenState } from "@ui/states/kataloge/FahrschuelerartenState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface FahrschuelerartenReactiveState {
	fahrschuelerarten: List<Fahrschuelerart>;
	fahrschuelerartenById: Map<number, Fahrschuelerart>;
}

/** Implementierung des States für den Fahrschülerartenkatalog */
export class FahrschuelerartenStateImpl extends StateManager<FahrschuelerartenReactiveState> implements FahrschuelerartenState {

	private readonly _fahrschuelerarten: KatalogState<Fahrschuelerart>;

	public constructor() {
		super({
			fahrschuelerarten: new ArrayList(),
			fahrschuelerartenById: new Map(),
		});

		this._fahrschuelerarten = createKatalogState<Fahrschuelerart>({
			katalogLabel: 'Fahrschülerarten',
			getList: () => this.state.fahrschuelerarten,
			getById: () => this.state.fahrschuelerartenById,
			updateState: (list, byId) => this.setPatchedState({ fahrschuelerarten: list, fahrschuelerartenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ fahrschuelerarten: list, fahrschuelerartenById: byId }),
			apiGet: () => api.server.getFahrschuelerarten(api.schema),
			apiAdd: (data) => api.server.addFahrschuelerart(data, api.schema),
			apiPatch: (id, data) => api.server.patchFahrschuelerart(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteFahrschuelerarten(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten des Katalogs */
	public async init(): Promise<void> {
		try {
			await this.fahrschuelerarten.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden der Kataloge 'Fahrschülerarten' ist fehlgeschlagen.`
			);
		}
	}

	public get fahrschuelerarten(): KatalogState<Fahrschuelerart> {
		return this._fahrschuelerarten;
	}
}

export const fahrschuelerartenStateImpl = new FahrschuelerartenStateImpl();
