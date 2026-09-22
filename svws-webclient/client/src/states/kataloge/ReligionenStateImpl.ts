
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import type { ReligionenState } from "@ui/states/kataloge/ReligionenState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface ReligionenReactiveState {
	religionen: List<ReligionEintrag>;
	religionenById: Map<number, ReligionEintrag>;
}

const KATALOG_LABEL = "Religionen";

/** Implementierung des States für den Religionenkatalog */
export class ReligionenStateImpl extends StateManager<ReligionenReactiveState> implements ReligionenState {

	private readonly _religionen: KatalogState<ReligionEintrag>;

	public constructor() {
		super({
			religionen: new ArrayList(),
			religionenById: new Map(),
		});

		this._religionen = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.religionen,
			getById: () => this.state.religionenById,
			updateState: (list, byId) => this.setPatchedState({ religionen: list, religionenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ religionen: list, religionenById: byId }),
			apiGet: () => api.server.getReligionen(api.schema),
			apiAdd: (data) => api.server.addReligion(data, api.schema),
			apiPatch: (id, data) => api.server.patchReligion(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteReligionen(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.religionen.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get religionen(): KatalogState<ReligionEintrag> {
		return this._religionen;
	}
}

export const religionenStateImpl = new ReligionenStateImpl();
