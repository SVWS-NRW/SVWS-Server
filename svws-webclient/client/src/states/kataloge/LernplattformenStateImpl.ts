import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import type { LernplattformenState } from "@ui/states/kataloge/LernplattformenState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface LernplattformenReactiveState {
	lernplattformen: List<Lernplattform>;
	lernplattformenById: Map<number, Lernplattform>;
}

const KATALOG_LABEL = "Lernplattformen";

/** Implementierung des States für den Lernplattformenkatalog */
export class LernplattformenStateImpl extends StateManager<LernplattformenReactiveState> implements LernplattformenState {

	private readonly _lernplattformen: KatalogState<Lernplattform>;

	public constructor() {
		super({
			lernplattformen: new ArrayList(),
			lernplattformenById: new Map(),
		});

		this._lernplattformen = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.lernplattformen,
			getById: () => this.state.lernplattformenById,
			updateState: (list, byId) => this.setPatchedState({ lernplattformen: list, lernplattformenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ lernplattformen: list, lernplattformenById: byId }),
			apiGet: () => api.server.getLernplattformen(api.schema),
			apiAdd: (data) => api.server.addLernplattform(data, api.schema),
			apiPatch: (id, data) => api.server.patchLernplattform(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteLernplattformen(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.lernplattformen.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get lernplattformen(): KatalogState<Lernplattform> {
		return this._lernplattformen;
	}
}

export const lernplattformenStateImpl = new LernplattformenStateImpl();
