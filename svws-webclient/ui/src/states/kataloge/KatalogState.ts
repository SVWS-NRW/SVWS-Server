import type { List } from "../../../../core/src/java/util/List";

/**
 * Die Schnittstelle für den Aufbau eines Kataloge States
 */
export interface KatalogState<T> {
	readonly list: List<T>;
	readonly byId: Map<number, T>;
	update(): Promise<void>;
	add(data: Partial<T>): Promise<T>;
	patch(id: number, data: Partial<T>): Promise<void>;
	delete(id: number): Promise<void>;
}
