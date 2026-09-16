import type { Beschaeftigungsart } from "@core/core/data/schule/Beschaeftigungsart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { BeschaeftigungsartState } from "@ui/states/kataloge/BeschaeftigungsartState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface BeschaeftigungsartReactiveState {
	beschaeftigungsarten: List<Beschaeftigungsart>;
	beschaeftigungsartenById: Map<number, Beschaeftigungsart>;
}

/** Implementierung des States für Leitungsfunktionen */
export class BeschaeftigungsartStateImpl extends StateManager<BeschaeftigungsartReactiveState> implements BeschaeftigungsartState {

	private readonly _beschaeftigungsarten: KatalogState<Beschaeftigungsart>;

	public constructor() {
		super({
			beschaeftigungsarten: new ArrayList(),
			beschaeftigungsartenById: new Map(),
		});

		this._beschaeftigungsarten = this.createBeschaeftigungsartState();
	}

	/** Lädt den Beschäftigungsarten Katalog vom Server. */
	public async init(): Promise<void> {
		try {
			const beschaeftigungsarten = await api.server.getBeschaeftigungsarten(api.schema);

			this.setPatchedDefaultState({
				beschaeftigungsarten,
				beschaeftigungsartenById: this.toMap(beschaeftigungsarten),
			});
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				"Die Leitungsfunktionen konnten nicht geladen werden."
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


	private createBeschaeftigungsartState(): KatalogState<Beschaeftigungsart> {
		// eslint-disable-next-line @typescript-eslint/no-this-alias
		const self = this;

		return {
			get list() {
				// "self" wird als explizite Referenz auf die Klasseninstanz im Getter benötigt
				return self.state.beschaeftigungsarten;
			},
			get byId() {
				// "self" wird als explizite Referenz auf die Klasseninstanz im Getter benötigt
				return self.state.beschaeftigungsartenById;
			},
			update: async () => {
				try {
					const beschaeftigungsarten = await api.server.getBeschaeftigungsarten(api.schema);
					this.setPatchedState({
						beschaeftigungsarten,
						beschaeftigungsartenById: this.toMap(beschaeftigungsarten),
					});
				} catch {
					throw new DeveloperNotificationException(`Das Laden der Beschäftigungsarten ist fehlgeschlagen.`);
				}
			},
			add: async (data) => {
				let beschaeftigungsart: Beschaeftigungsart;
				try {
					beschaeftigungsart = await api.server.addBeschaeftigungsart(data, api.schema);
				} catch {
					throw new DeveloperNotificationException(`Das Hinzufügen der Beschäftigungsart ist fehlgeschlagen.`);
				}
				await this.beschaeftigungsarten.update();
				return beschaeftigungsart;
			},
			patch: async (id, data) => {
				try {
					await api.server.patchBeschaeftigungsart(data, api.schema, id);
				} catch {
					throw new DeveloperNotificationException("Das Bearbeiten der Beschäftigungsart ist fehlgeschlagen.");
				}
				await this.beschaeftigungsarten.update();
			},
			delete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				try {
					await api.server.deleteBeschaeftigungsarten(ids, api.schema);
				} catch {
					throw new DeveloperNotificationException("Das Löschen der Beschäftigungsarten ist fehlgeschlagen.");
				}
				await this.beschaeftigungsarten.update();
			},
		};
	}

	public get beschaeftigungsarten(): KatalogState<Beschaeftigungsart> {
		return this._beschaeftigungsarten;
	}

}

export const beschaeftigungsartStateImpl = new BeschaeftigungsartStateImpl();
