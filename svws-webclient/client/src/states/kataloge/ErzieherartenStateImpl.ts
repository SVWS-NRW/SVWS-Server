import type { Erzieherart } from "@core/core/data/erzieher/Erzieherart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { ErzieherartenState } from "@ui/states/kataloge/ErzieherartenState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface ErzieherartenReactiveState {
	erzieherarten: List<Erzieherart>;
	erzieherartenById: Map<number, Erzieherart>;
}

const KATALOG_LABEL = "Erzieherarten";

/** Implementierung des States für den Erzieherartenkatalog */
export class ErzieherartenStateImpl extends StateManager<ErzieherartenReactiveState> implements ErzieherartenState {

	private readonly _erzieherarten: KatalogState<Erzieherart>;

	public constructor() {
		super({
			erzieherarten: new ArrayList(),
			erzieherartenById: new Map(),
		});

		this._erzieherarten = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.erzieherarten,
			getById: () => this.state.erzieherartenById,
			updateState: (list, byId) => this.setPatchedState({ erzieherarten: list, erzieherartenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ erzieherarten: list, erzieherartenById: byId }),
			apiGet: () => api.server.getErzieherArten(api.schema),
			apiAdd: (data) => api.server.addErzieherart(data, api.schema),
			apiPatch: (id, data) => api.server.patchErzieherart(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteErzieherarten(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.erzieherarten.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get erzieherarten(): KatalogState<Erzieherart> {
		return this._erzieherarten;
	}
}

export const erzieherartenStateImpl = new ErzieherartenStateImpl();
