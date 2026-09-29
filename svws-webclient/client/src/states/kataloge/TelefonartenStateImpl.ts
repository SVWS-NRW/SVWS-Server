import type { Telefonart } from "@core/core/data/schule/Telefonart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import type { TelefonartenState } from "@ui/states/kataloge/TelefonartenState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface TelefonartenReactiveState {
	telefonarten: List<Telefonart>;
	telefonartenById: Map<number, Telefonart>;
}

const KATALOG_LABEL = "Telefonarten";

/** Implementierung des States für den Telefonartenkatalog */
export class TelefonartenStateImpl extends StateManager<TelefonartenReactiveState> implements TelefonartenState {

	private readonly _telefonarten: KatalogState<Telefonart>;

	public constructor() {
		super({
			telefonarten: new ArrayList(),
			telefonartenById: new Map(),
		});

		this._telefonarten = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.telefonarten,
			getById: () => this.state.telefonartenById,
			updateState: (list, byId) => this.setPatchedState({ telefonarten: list, telefonartenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ telefonarten: list, telefonartenById: byId }),
			apiGet: () => api.server.getTelefonarten(api.schema),
			apiAdd: (data) => api.server.addTelefonart(data, api.schema),
			apiPatch: (id, data) => api.server.patchTelefonart(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteTelefonarten(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.telefonarten.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get telefonarten(): KatalogState<Telefonart> {
		return this._telefonarten;
	}
}

export const telefonartenStateImpl = new TelefonartenStateImpl();
