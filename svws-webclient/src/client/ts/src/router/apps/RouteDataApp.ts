import type { WritableComputedRef } from "vue";
import { computed, shallowRef } from "vue";

import type { OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
import { Schuljahresabschnitt } from "@core";

import type { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchueler } from "~/router/apps/RouteSchueler";


interface RouteStateApp {
	idSchuljahresabschnitt: number,
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataApp {

	private static _defaultState : RouteStateApp = {
		idSchuljahresabschnitt: -1,
		mapOrte: new Map(),
		mapOrtsteile: new Map(),
		view: routeSchueler
	}

	private _state = shallowRef<RouteStateApp>(RouteDataApp._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateApp>) {
		this._state.value = Object.assign({ ... RouteDataApp._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateApp>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	aktAbschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get: () => {
			let abschnitt = api.config.getObjectValue("app.akt_abschnitt", Schuljahresabschnitt.transpilerFromJSON);
			if (abschnitt === null) {
				void api.config.setObjectValue("app.akt_abschnitt", api.abschnitt, Schuljahresabschnitt.transpilerToJSON);
				abschnitt = api.abschnitt;
			}
			return abschnitt;
		},
		set: (abschnitt: Schuljahresabschnitt) => {
			void api.config.setObjectValue("app.akt_abschnitt", abschnitt, Schuljahresabschnitt.transpilerToJSON);
			// TODO was tun, wenn das akt Halbjahr neu gesetzt wurde?
		}
	})

	setAbschnitt = (abschnitt: Schuljahresabschnitt): void => {
		this.aktAbschnitt.value = abschnitt;
	}

	/**
	 * Setzt den Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt?: number) {
		if (idSchuljahresabschnitt === undefined) {
			this._state.value = RouteDataApp._defaultState;
			return;
		}
		// Lade den Katalog der Orte
		const orte = await api.server.getOrte(api.schema);
		const mapOrte = new Map();
		for (const o of orte)
			mapOrte.set(o.id, o);
		// Lade den Katalog der Ortsteile
		const ortsteile = await api.server.getOrtsteile(api.schema);
		const mapOrtsteile = new Map();
		for (const o of ortsteile)
			mapOrtsteile.set(o.id, o);
		this.setPatchedDefaultState({
			idSchuljahresabschnitt: idSchuljahresabschnitt,
			mapOrte,
			mapOrtsteile,
			view: this._state.value.view,
		});
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeApp.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese gewählte Ansicht wird nicht unterstützt.");
	}

	public get mapOrte() {
		return this._state.value.mapOrte;
	}

	public get mapOrtsteile() {
		return this._state.value.mapOrtsteile;
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

}

