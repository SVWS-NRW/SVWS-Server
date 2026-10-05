import type { FachDaten } from "@core/core/data/fach/FachDaten";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { FaecherState } from "@ui/states/kataloge/FaecherState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface FaecherReactiveState {
	faecher: List<FachDaten>;
	faecherById: Map<number, FachDaten>;
}

const KATALOG_LABEL = "Fächer";

/** Implementierung des States für den Faecherkatalog */
export class FaecherStateImpl extends StateManager<FaecherReactiveState> implements FaecherState {

	private readonly _faecher: KatalogState<FachDaten>;

	public constructor() {
		super({
			faecher: new ArrayList(),
			faecherById: new Map(),
		});

		this._faecher = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.faecher,
			getById: () => this.state.faecherById,
			updateState: (list, byId) => this.setPatchedState({ faecher: list, faecherById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ faecher: list, faecherById: byId }),
			apiGet: () => api.server.getFaecher(api.schema),
			apiAdd: (data) => api.server.addFach(data, api.schema),
			apiPatch: (id, data) => api.server.patchFach(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteFaecher(ids, api.schema);
			},
		});
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.faecher.update(true);
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get faecher(): KatalogState<FachDaten> {
		return this._faecher;
	}
}

export const faecherStateImpl = new FaecherStateImpl();
