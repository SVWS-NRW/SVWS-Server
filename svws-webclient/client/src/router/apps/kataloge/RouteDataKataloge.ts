import { shallowRef } from "vue";

import { type RouteNode } from "~/router/RouteNode";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogFaecher } from "~/router/apps/kataloge/faecher/RouteKatalogFaecher";

interface RouteStateKataloge {
	view: RouteNode<any, any>;
}

export class RouteDataKataloge {

	private static _defaultState: RouteStateKataloge = {
		view: routeKatalogFaecher,
	}
	private _state = shallowRef(RouteDataKataloge._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKataloge>) {
		this._state.value = Object.assign({ ... RouteDataKataloge._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKataloge>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKataloge.menu.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Kataloge gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}
}
