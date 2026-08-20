import type { Ref } from "vue";
import { onUnmounted, shallowRef, watch } from "vue";
import type { ModelProxy } from "./ModelProxy";

/**
 * Erzeugt und verwaltet eine reaktive Liste von ModelProxy-Instanzen basierend
 * auf einer reaktiven Quellliste. Bestehende Instanzen werden anhand der ID
 * wiederverwendet, um Pending-State und Validierungszustände zu erhalten.
 * Nicht mehr vorhandene Einträge werden automatisch aus dem Cache entfernt.
 *
 * @param source - Reaktive Quellliste (z.B. computed oder Arrow-Function)
 * @param getId - Funktion zum Ermitteln der ID eines Eintrags
 * @param createProxy - Factory-Funktion zum Erzeugen eines neuen ModelProxy für einen Eintrag
 * @returns Reaktive Liste der ModelProxy-Instanzen
 *
 * @example
 * const myModel = useModelProxyList(
 *     reactiveData,
 *     (entry) => entry.id,
 *     (entry) => new ModelProxy(
 *         () => entry,
 *         ...
 *     )
 * );
 */
export function useModelProxyList<T extends object, P extends ModelProxy<T>>(
	source: Ref<Iterable<T>> | (() => Iterable<T>),
	getId: (item: T) => number | string,
	createProxy: (item: T) => P
) {
	// persistiert die ModelProxy Instanzen über reaktiven Updates hinweg
	const cache = new Map<number | string, P>();

	// reaktive Liste der ModelProxies
	const list = shallowRef<P[]>([]);

	// initial, sowie nach Updates von `source` wird `list` neu erstellt
	watch(source, (items) => {
		const aktuelleIds = new Set<number | string>();
		const neueModels: P[] = [];

		for (const item of items) {
			const id = getId(item);
			aktuelleIds.add(id);

			// aktualisiert Cache bei neuen IDs oder neuen Objektreferenzen
			if ((cache.get(id) === undefined) || cache.get(id)?.data !== item) {
				cache.set(id, createProxy(item));
			}

			// existierende ModelProxies nutzen, um Pending State und Validierung nicht zu verlieren
			neueModels.push(cache.get(id)!);
		}

		// Entfernte ModelProxies aus dem Cache räumen
		for (const id of cache.keys()) {
			if (!aktuelleIds.has(id)) {
				cache.delete(id);
			}
		}

		// aktualisiert die reaktive Liste der Model Proxies
		list.value = neueModels;
	}, { immediate: true });

	onUnmounted(() => {
		// Cache der ModelProxies leeren
		cache.clear();
	});

	return list;
}
