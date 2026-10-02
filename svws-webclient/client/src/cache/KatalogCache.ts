import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
import type { Floskel } from "@core/core/data/schule/Floskel";
import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
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
	private _floskelgruppenById: Map<number, Floskelgruppe> = new Map();
	private _floskelnById: Map<number, Floskel> = new Map();
	private _foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag> = new Map();
	private _faecherById: Map<number, FachDaten> = new Map();
	private _lernplattformenById: Map<number, Lernplattform> = new Map();

	public constructor() {
		this.initializeCacheUpdater();
	}

	private initializeCacheUpdater() {
		this._katalogCacheUpdater.set(Katalog.BETRIEBSARTEN, async () => {
			const result = await api.server.getBetriebsarten(api.schema);
			return { betriebsartenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.FAECHER, async () => {
			const result = await api.server.getFaecher(api.schema);
			return { faecherById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.FLOSKELGRUPPEN, async () => {
			const result = await api.server.getFloskelgruppen(api.schema);
			return { floskelgruppenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.FLOSKELN, async () => {
			const result = await api.server.getFloskeln(api.schema);
			return { floskelnById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.FOERDERSCHWERPUNKTE, async () => {
			const result = await api.server.getKatalogFoerderschwerpunkte(api.schema);
			return { foerderschwerpunkteById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.LERNPLATTFORMEN, async () => {
			const result = await api.server.getLernplattformen(api.schema);
			return { lernplattformenById: this.convertToMap(result) };
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

	get floskelgruppenById(): Map<number, Floskelgruppe> {
		return this._floskelgruppenById;
	}

	set floskelgruppenById(value: Map<number, Floskelgruppe>) {
		this._floskelgruppenById = value;
	}

	get floskelnById(): Map<number, Floskel> {
		return this._floskelnById;
	}

	set floskelnById(value: Map<number, Floskel>) {
		this._floskelnById = value;
	}

	get foerderschwerpunkteById(): Map<number, FoerderschwerpunktEintrag> {
		return this._foerderschwerpunkteById;
	}

	set foerderschwerpunkteById(value: Map<number, FoerderschwerpunktEintrag>) {
		this._foerderschwerpunkteById = value;
	}

	get faecherById(): Map<number, FachDaten> {
		return this._faecherById;
	}

	set faecherById(value: Map<number, FachDaten>) {
		this._faecherById = value;
	}

	get lernplattformenById(): Map<number, Lernplattform> {
		return this._lernplattformenById;
	}

	set lernplattformenById(value: Map<number, Lernplattform>) {
		this._lernplattformenById = value;
	}

}
