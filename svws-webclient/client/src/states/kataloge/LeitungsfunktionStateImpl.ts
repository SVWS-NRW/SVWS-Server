import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import type { LeitungsfunktionState } from "@ui/states/kataloge/LeitungsfunktionState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface LeitungsfunktionReactiveState {
	leitungsfunktionen: List<Leitungsfunktion>;
	leitungsfunktionenById: Map<number, Leitungsfunktion>;
}

/** Implementierung des States für Leitungsfunktionen */
export class LeitungsfunktionStateImpl extends StateManager<LeitungsfunktionReactiveState> implements LeitungsfunktionState {

	private readonly _leitungsfunktionen: KatalogState<Leitungsfunktion>;

	public constructor() {
		super({
			leitungsfunktionen: new ArrayList(),
			leitungsfunktionenById: new Map(),
		});

		this._leitungsfunktionen = this.createLeitungsfunktionenState();
	}

	/** Lädt alle adressbezogenen Kataloge vom Server. */
	public async init(): Promise<void> {
		try {
			const leitungsfunktionen = await api.server.getLeitungsfunktionen(api.schema);

			this.setPatchedDefaultState({
				leitungsfunktionen,
				leitungsfunktionenById: this.toMap(leitungsfunktionen),
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


	private createLeitungsfunktionenState(): KatalogState<Leitungsfunktion> {
		// eslint-disable-next-line @typescript-eslint/no-this-alias
		const self = this;

		return {
			get list() {
				// "self" wird als explizite Referenz auf die Klasseninstanz im Getter benötigt
				return self.state.leitungsfunktionen;
			},
			get byId() {
				// "self" wird als explizite Referenz auf die Klasseninstanz im Getter benötigt
				return self.state.leitungsfunktionenById;
			},
			update: async () => {
				try {
					const leitungsfunktionen = await api.server.getLeitungsfunktionen(api.schema);
					this.setPatchedState({
						leitungsfunktionen,
						leitungsfunktionenById: this.toMap(leitungsfunktionen),
					});
				} catch {
					throw new DeveloperNotificationException(`Das Laden der Leitungsfunktionen ist fehlgeschlagen.`);
				}
			},
			add: async (data) => {
				let leitungsfunktion: Leitungsfunktion;
				try {
					leitungsfunktion = await api.server.addLeitungsfunktion(data, api.schema);
				} catch {
					throw new DeveloperNotificationException(`Das Hinzufügen der Leitungsfunktion ist fehlgeschlagen.`);
				}
				await this.leitungsfunktionen.update();
				return leitungsfunktion;
			},
			patch: async (id, data) => {
				try {
					await api.server.patchLeitungsfunktion(data, api.schema, id);
				} catch {
					throw new DeveloperNotificationException("Das Bearbeiten der Leitungsfunktion ist fehlgeschlagen.");
				}
				await this.leitungsfunktionen.update();
			},
			delete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				try {
					await api.server.deleteLeitungsfunktionen(ids, api.schema);
				} catch {
					throw new DeveloperNotificationException("Das Löschen der Leitungsfunktionen ist fehlgeschlagen.");
				}
				await this.leitungsfunktionen.update();
			},
		};
	}

	public get leitungsfunktionen(): KatalogState<Leitungsfunktion> {
		return this._leitungsfunktionen;
	}

}

export const leitungsfunktionStateImpl = new LeitungsfunktionStateImpl();
