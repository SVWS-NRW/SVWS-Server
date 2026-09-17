import type { OrtKatalogEintrag } from "@core/core/data/kataloge/OrtKatalogEintrag";
import type { OrtsteilKatalogEintrag } from "@core/core/data/kataloge/OrtsteilKatalogEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState, toMap } from "@ui/states/kataloge/katalogStateUtils";
import type { OrteState, OrtsteileKatalogState } from "@ui/states/kataloge/OrteState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface OrteReactiveState {
	orte: List<OrtKatalogEintrag>;
	orteById: Map<number, OrtKatalogEintrag>;
	ortsteile: List<OrtsteilKatalogEintrag>;
	ortsteileById: Map<number, OrtsteilKatalogEintrag>;
}

/** Implementierung des States für den Ortekatalog */
export class OrteStateImpl extends StateManager<OrteReactiveState> implements OrteState {

	private readonly _orte: KatalogState<OrtKatalogEintrag>;
	private readonly _ortsteile: OrtsteileKatalogState;

	public constructor() {
		super({
			orte: new ArrayList(),
			orteById: new Map(),
			ortsteile: new ArrayList(),
			ortsteileById: new Map(),
		});

		this._orte = createKatalogState({
			katalogLabel: 'Betriebe',
			getList: () => this.state.orte,
			getById: () => this.state.orteById,
			updateState: (list, byId) => this.setPatchedState({ orte: list, orteById: byId }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ orte: list, orteById: byId }),
			apiGet: () => api.server.getOrte(api.schema),
			apiAdd: (data) => api.server.addOrt(data, api.schema),
			apiPatch: (id, data) => api.server.patchOrt(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteOrte(ids, api.schema);
			},
		});

		this._ortsteile = {
			...createKatalogState({
				katalogLabel: 'Betriebe',
				getList: () => this.state.ortsteile,
				getById: () => this.state.ortsteileById,
				updateState: (list, byId) => this.setPatchedState({ ortsteile: list, ortsteileById: byId }),
				updateDefaultState: (list, byId) => this.setPatchedDefaultState({ ortsteile: list, ortsteileById: byId }),
				apiGet: () => api.server.getOrtsteile(api.schema),
				apiAdd: (data) => api.server.addOrtsteil(data, api.schema),
				apiPatch: (id, data) => api.server.patchOrtsteil(data, api.schema, id),
				apiDelete: async (id) => {
					const ids = new ArrayList<number>();
					ids.add(id);
					await api.server.deleteOrtsteile(ids, api.schema);
				},
			}),
			listByOrtId(idOrt: number | null): List<OrtsteilKatalogEintrag> {
				const result = new ArrayList<OrtsteilKatalogEintrag>();
				if (idOrt === null) {
					return result;
				}
				for (const ortsteil of this.list) {
					if (ortsteil.idOrt === idOrt) {
						result.add(ortsteil);
					}
				}
				return result;
			},
			byOrtId(idOrt: number | null): Map<number, OrtsteilKatalogEintrag> {
				const result = new Map<number, OrtsteilKatalogEintrag>();
				if (idOrt === null) {
					return result;
				}
				for (const ortsteil of this.list) {
					if (ortsteil.idOrt === idOrt) {
						result.set(ortsteil.id, ortsteil);
					}
				}
				return result;
			},
		};
	}

	/** Lädt alle adressbezogenen Kataloge vom Server. */
	public async init(): Promise<void> {
		try {
			// paralleles Laden aller Kataloge
			const [orte, ortsteile] = await Promise.all([
				api.server.getOrte(api.schema),
				api.server.getOrtsteile(api.schema),
			]);

			// bevor einmalig der Defaultstate gesetzt wird
			this.setPatchedDefaultState({
				orte,
				orteById: toMap(orte),
				ortsteile,
				ortsteileById: toMap(ortsteile),
			});
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				"Die adressbezogenen Kataloge konnten nicht geladen werden."
			);
		}
	}

	public get orte(): KatalogState<OrtKatalogEintrag> {
		return this._orte;
	}

	public get ortsteile(): OrtsteileKatalogState {
		return this._ortsteile;
	}
}

export const orteStateImpl = new OrteStateImpl();
