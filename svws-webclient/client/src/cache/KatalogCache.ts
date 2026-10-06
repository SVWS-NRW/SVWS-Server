import type { Katalog } from "~/cache/Katalog";


export class KatalogCache {

	/**
	 * Mappt jeden {@link Katalog} auf eine asynchrone Funktion,
	 * die die zugehörigen Daten lädt und als `Partial<KatalogCache>` zurückgibt.
	 * Wird zur Cache-Aktualisierung verwendet.
	 */
	private _katalogCacheUpdater = new Map<Katalog, () => Promise<Partial<KatalogCache>>>();

	get katalogCacheUpdater(): Map<Katalog, () => Promise<Partial<KatalogCache>>> {
		return this._katalogCacheUpdater;
	}

	set katalogCacheUpdater(value: Map<Katalog, () => Promise<Partial<KatalogCache>>>) {
		this._katalogCacheUpdater = value;
	}
}
