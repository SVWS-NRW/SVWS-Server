import type { List, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";
import { StateManager } from "@ui";
import type { OrteState, OrtsteileKatalogState, KatalogState } from "@ui";
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

		this._orte = this.createOrteState();
		this._ortsteile = this.createOrtsteileState();
	}

	/** Lädt alle adressbezogenen Kataloge vom Server. */
	public async init(): Promise<void> {
		try {
			const [orte, ortsteile] = await Promise.all([
				api.server.getOrte(api.schema),
				api.server.getOrtsteile(api.schema),
			]);

			this.setPatchedDefaultState({
				orte,
				orteById: this.toMap(orte),
				ortsteile,
				ortsteileById: this.toMap(ortsteile),
			});
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				"Die adressbezogenen Kataloge konnten nicht geladen werden."
			);
		}
	}


	/** Konvertiert eine Core-List in eine Map<id, T> */
	private toMap<T extends { id: number }>(list: Iterable<T>): Map<number, T> {
		const map = new Map<number, T>();
		for (const item of list) {
			map.set(item.id, item);
		}
		return map;
	}


	private createOrteState(): KatalogState<OrtKatalogEintrag> {
		// eslint-disable-next-line @typescript-eslint/no-this-alias
		const self = this;

		return {
			get list() {
				// "self" wird als explizite Referenz auf die Klasseninstanz im Getter benötigt
				return self.state.orte;
			},
			get byId() {
				// "self" wird als explizite Referenz auf die Klasseninstanz im Getter benötigt
				return self.state.orteById;
			},
			update: async () => {
				try {
					const orte = await api.server.getOrte(api.schema);
					this.setPatchedState({
						orte,
						orteById: this.toMap(orte),
					});
				} catch {
					throw new DeveloperNotificationException(`Das Laden der Orte ist fehlgeschlagen.`);
				}
			},
			add: async (data) => {
				let ort: OrtKatalogEintrag;
				try {
					ort = await api.server.addOrt(data, api.schema);
				} catch {
					throw new DeveloperNotificationException(`Das Hinzufügen des Orts ist fehlgeschlagen.`);
				}
				await this.orte.update();
				return ort;
			},
			patch: async (id, data) => {
				try {
					await api.server.patchOrt(data, api.schema, id);
				} catch {
					throw new DeveloperNotificationException("Das Bearbeiten des Orts ist fehlgeschlagen.");
				}
				await this.orte.update();
			},
			delete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				try {
					await api.server.deleteOrte(ids, api.schema);
				} catch {
					throw new DeveloperNotificationException("Das Löschen des Orts ist fehlgeschlagen.");
				}
				await this.orte.update();
			},
		};
	}

	private createOrtsteileState(): OrtsteileKatalogState {
		// eslint-disable-next-line @typescript-eslint/no-this-alias
		const self = this;

		return {
			get list() {
				// "self" wird als explizite Referenz auf die Klasseninstanz im Getter benötigt
				return self.state.ortsteile;
			},
			get byId() {
				// "self" wird als explizite Referenz auf die Klasseninstanz im Getter benötigt
				return self.state.ortsteileById;
			},
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
			update: async () => {
				try {
					const ortsteile = await api.server.getOrtsteile(api.schema);
					this.setPatchedState({
						ortsteile,
						ortsteileById: this.toMap(ortsteile),
					});
				} catch {
					throw new DeveloperNotificationException(`Das Laden der Orte ist fehlgeschlagen.`);
				}
			},
			add: async (data) => {
				let ortsteil: OrtsteilKatalogEintrag;
				try {
					ortsteil = await api.server.addOrtsteil(data, api.schema);
				} catch {
					throw new DeveloperNotificationException("Das Hinzufügen des Ortsteils ist fehlgeschlagen.");
				}
				await this.ortsteile.update();
				return ortsteil;
			},
			patch: async (id, data) => {
				try {
					await api.server.patchOrtsteil(data, api.schema, id);
				} catch {
					throw new DeveloperNotificationException("Das Bearbeiten des Ortsteils ist fehlgeschlagen.");
				}
				await this.ortsteile.update();
			},
			delete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				try {
					await api.server.deleteOrtsteile(ids, api.schema);
				} catch {
					throw new DeveloperNotificationException("Das Löschen des Ortsteils ist fehlgeschlagen.");
				}
				await this.ortsteile.update();
			},
		};
	}

	public get orte(): KatalogState<OrtKatalogEintrag> {
		return this._orte;
	}

	public get ortsteile(): OrtsteileKatalogState {
		return this._ortsteile;
	}
}

export const orteStateImpl = new OrteStateImpl();
