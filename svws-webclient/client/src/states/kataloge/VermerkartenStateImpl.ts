import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import type { VermerkartenState } from "@ui/states/kataloge/VermerkartenState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface VermerkartenReactiveState {
	vermerkarten: List<VermerkartEintrag>;
	vermerkartenById: Map<number, VermerkartEintrag>;
}

const KATALOG_LABEL = "Vermerkarten";

/** Implementierung des States für den Vermerkartenkatalog */
export class VermerkartenStateImpl extends StateManager<VermerkartenReactiveState> implements VermerkartenState {

	private readonly _vermerkarten: KatalogState<VermerkartEintrag>;

	public constructor() {
		super({
			vermerkarten: new ArrayList(),
			vermerkartenById: new Map(),
		});

		this._vermerkarten = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.vermerkarten,
			getById: () => this.state.vermerkartenById,
			updateState: (list, byId) => this.setPatchedState({ vermerkarten: list, vermerkartenById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ vermerkarten: list, vermerkartenById: byId }),
			apiGet: () => api.server.getVermerkarten(api.schema),
			apiAdd: (data) => api.server.createVermerkart(data, api.schema),
			apiPatch: (id, data) => api.server.patchVermerkart(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteVermerkartEintraege(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.vermerkarten.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get vermerkarten(): KatalogState<VermerkartEintrag> {
		return this._vermerkarten;
	}
}

export const vermerkartenStateImpl = new VermerkartenStateImpl();
