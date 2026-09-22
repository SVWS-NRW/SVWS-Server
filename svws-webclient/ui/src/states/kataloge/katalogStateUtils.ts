import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";

import type { KatalogState } from "./KatalogState";

export function toMap<T extends { id: number }>(list: Iterable<T>): Map<number, T> {
	const map = new Map<number, T>();
	for (const item of list) {
		map.set(item.id, item);
	}
	return map;
}

export interface KatalogStateConfig<T extends { id: number }, S extends object> {
	/** Für Fehlermeldungen, z. B. "Betriebe", "Orte" */
	katalogLabel: string;
	/** Liefert die reaktive Liste aus dem State */
	getList: () => List<T>;
	/** Liefert die reaktive Map aus dem State */
	getById: () => Map<number, T>;
	/** Aktualisiert Daten im StateManager */
	updateState: (list: List<T>, byId: Map<number, T>) => void;
	/** Aktualisiert den Defaultstate im StateManager */
	updateDefaultState: (list: List<T>, byId: Map<number, T>) => void;
	/** API-Calls für CRUD */
	apiGet: () => Promise<List<T>>;
	apiAdd: (data: Partial<T>) => Promise<T>;
	apiPatch: (id: number, data: Partial<T>) => Promise<void | T>;
	apiDelete: (id: number) => Promise<void>;
}

export function createKatalogState<T extends { id: number }>(
	config: KatalogStateConfig<T, any>
): KatalogState<T> {
	const update = async (initial: boolean = false) => {
		try {
			const list = await config.apiGet();
			if (initial) {
				config.updateDefaultState(list, toMap(list));
			} else {
				config.updateState(list, toMap(list));
			}
		} catch {
			throw new DeveloperNotificationException(
				`Das Laden der Kataloge '${config.katalogLabel}' ist fehlgeschlagen.`
			);
		}
	};

	return {
		get list() {
			return config.getList();
		},
		get byId() {
			return config.getById();
		},
		update,
		add: async (data) => {
			let entity: T;
			try {
				entity = await config.apiAdd(data);
			} catch {
				throw new DeveloperNotificationException(
					`Das Hinzufügen des Katalogs '${config.katalogLabel}' ist fehlgeschlagen.`
				);
			}
			await update();
			return entity;
		},
		patch: async (id, data) => {
			try {
				await config.apiPatch(id, data);
			} catch {
				throw new DeveloperNotificationException(
					`Das Bearbeiten des Katalogs '${config.katalogLabel}' ist fehlgeschlagen.`
				);
			}
			await update();
		},
		delete: async (id) => {
			try {
				await config.apiDelete(id);
			} catch {
				throw new DeveloperNotificationException(
					`Das Löschen des Katalogs '${config.katalogLabel}' ist fehlgeschlagen.`
				);
			}
			await update();
		},
	};
}
