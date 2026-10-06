
import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { BetriebsartenState } from "@ui/states/kataloge/BetriebsartenState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface BetriebsartenReactiveState {
	betriebsarten: List<Betriebsart>;
	betriebsartenById: Map<number, Betriebsart>;
}

const KATALOG_LABEL = "Betriebsarten";

/** Implementierung des States für den Betriebsartenkatalog */
export class BetriebsartenStateImpl extends StateManager<BetriebsartenReactiveState> implements BetriebsartenState {

	private readonly _betriebsarten: KatalogState<Betriebsart>;

	public constructor() {
		super({
			betriebsarten: new ArrayList(),
			betriebsartenById: new Map(),
		});

		this._betriebsarten = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.betriebsarten,
			getById: () => this.state.betriebsartenById,
			updateState: (list, byId) => this.setPatchedState({ betriebsarten: list, betriebsartenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ betriebsarten: list, betriebsartenById: byId }),
			apiGet: () => api.server.getBetriebsarten(api.schema),
			apiAdd: (data) => api.server.addBetriebsart(data, api.schema),
			apiPatch: (id, data) => api.server.patchBetriebsart(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteBetriebsarten(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.betriebsarten.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get betriebsarten(): KatalogState<Betriebsart> {
		return this._betriebsarten;
	}
}

export const betriebsartenStateImpl = new BetriebsartenStateImpl();
