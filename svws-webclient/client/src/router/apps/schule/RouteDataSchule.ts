import { shallowRef } from "vue";

import { type RouteNode } from "~/router/RouteNode";

import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeSchuleBenutzer } from "~/router/apps/schule/benutzer/RouteSchuleBenutzer";

interface RouteStateSchule {
	view: RouteNode<any, any>;
}

export class RouteDataSchule {

	private static _defaultState: RouteStateSchule = {
		view: routeSchuleBenutzer,
	}
	private _state = shallowRef(RouteDataSchule._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchule>) {
		this._state.value = Object.assign({ ... RouteDataSchule._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchule>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeSchule.menu.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Schule gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}
}
