import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { EinwilligungsartenState } from "@ui/states/kataloge/EinwilligungsartenState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface EinwilligungsartenReactiveState {
	einwilligungsarten: List<Einwilligungsart>;
	einwilligungsartenById: Map<number, Einwilligungsart>;
}

const KATALOG_LABEL = "Einwilligungsarten";

/** Implementierung des States für den Einwilligungsartenkatalog */
export class EinwilligungsartenStateImpl extends StateManager<EinwilligungsartenReactiveState> implements EinwilligungsartenState {

	private readonly _einwilligungsarten: KatalogState<Einwilligungsart>;

	public constructor() {
		super({
			einwilligungsarten: new ArrayList(),
			einwilligungsartenById: new Map(),
		});

		this._einwilligungsarten = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.einwilligungsarten,
			getById: () => this.state.einwilligungsartenById,
			updateState: (list, byId) => this.setPatchedState({ einwilligungsarten: list, einwilligungsartenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ einwilligungsarten: list, einwilligungsartenById: byId }),
			apiGet: () => api.server.getEinwilligungsarten(api.schema),
			apiAdd: (data) => api.server.createEinwilligungsart(data, api.schema),
			apiPatch: (id, data) => api.server.patchEinwilligungsart(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteEinwilligungsarten(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.einwilligungsarten.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get einwilligungsarten(): KatalogState<Einwilligungsart> {
		return this._einwilligungsarten;
	}
}

export const einwilligungsartenStateImpl = new EinwilligungsartenStateImpl();
