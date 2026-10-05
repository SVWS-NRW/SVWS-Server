import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import type { List } from "@core/java/util/List";

import { Katalog } from "~/cache/Katalog";
import { api } from "~/router/Api";


export class KatalogCache {

	/**
	 * Mappt jeden {@link Katalog} auf eine asynchrone Funktion,
	 * die die zugehörigen Daten lädt und als `Partial<KatalogCache>` zurückgibt.
	 * Wird zur Cache-Aktualisierung verwendet.
	 */
	private _katalogCacheUpdater = new Map<Katalog, () => Promise<Partial<KatalogCache>>>();
	private _betriebsartenById: Map<number, Betriebsart> = new Map();
	private _foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag> = new Map();

	public constructor() {
		this.initializeCacheUpdater();
	}

	private initializeCacheUpdater() {
		this._katalogCacheUpdater.set(Katalog.BETRIEBSARTEN, async () => {
			const result = await api.server.getBetriebsarten(api.schema);
			return { betriebsartenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.FOERDERSCHWERPUNKTE, async () => {
			const result = await api.server.getKatalogFoerderschwerpunkte(api.schema);
			return { foerderschwerpunkteById: this.convertToMap(result) };
		});

	}

	private convertToMap<T extends { id: number }>(list: List<T>): Map<number, T> {
		const map = new Map<number, T>();
		for (const item of list) {
			map.set(item.id, item);
		}
		return map;
	}


	get katalogCacheUpdater(): Map<Katalog, () => Promise<Partial<KatalogCache>>> {
		return this._katalogCacheUpdater;
	}

	set katalogCacheUpdater(value: Map<Katalog, () => Promise<Partial<KatalogCache>>>) {
		this._katalogCacheUpdater = value;
	}

	get betriebsartenById(): Map<number, Betriebsart> {
		return this._betriebsartenById;
	}

	set betriebsartenById(value: Map<number, Betriebsart>) {
		this._betriebsartenById = value;
	}

	get foerderschwerpunkteById(): Map<number, FoerderschwerpunktEintrag> {
		return this._foerderschwerpunkteById;
	}

	set foerderschwerpunkteById(value: Map<number, FoerderschwerpunktEintrag>) {
		this._foerderschwerpunkteById = value;
	}

}
