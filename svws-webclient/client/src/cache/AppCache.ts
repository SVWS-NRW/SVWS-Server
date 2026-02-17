import type { Katalog } from "~/cache/Katalog";
import { KatalogCache } from "~/cache/KatalogCache";


export class AppCache {

	private _katalogCache: KatalogCache = new KatalogCache();

	public async refreshKataloge(...kataloge: Katalog[]): Promise<void> {
		const results = await Promise.all(
			kataloge.map(k => this._katalogCache.katalogCacheUpdater.get(k)?.() ?? Promise.resolve({}))
		);
		this._katalogCache = Object.assign(this._katalogCache, ...results);
	}

	public get kataloge(): KatalogCache {
		return this._katalogCache;
	}

}
