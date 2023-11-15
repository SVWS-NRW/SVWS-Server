import { shallowRef } from "vue";


import type { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchema } from "~/router/apps/schema/RouteSchema";


interface RouteStateApp {
	view: RouteNode<any, any>;
}

export class RouteDataApp {

	private static _defaultState : RouteStateApp = {
		view: routeSchema
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

	public async setView(view: RouteNode<any,any>) {
		if (routeApp.children.includes(view))
			this.setPatchedDefaultState({ view: view });
		else
			throw new Error("Diese gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

}

