import { shallowRef } from "vue";

import type { RouteNode } from "~/router/RouteNode";

import { routeSchema } from "~/router/apps/schema/RouteSchema";
import { routeSchemaUebersicht } from "~/router/apps/schema/uebersicht/RouteSchemaUebersicht";


interface RouteStateConfig {
	view: RouteNode<any, any>;
}

export class RouteDataConfig {

	private static _defaultState : RouteStateConfig = {
		view: routeSchemaUebersicht,
	};

	private _state = shallowRef(RouteDataConfig._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateConfig>) {
		this._state.value = Object.assign({ ... RouteDataConfig._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateConfig>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeSchema.children.includes(view))
			this.setPatchedState({ view });
		else
			throw new Error("Diese für die Konfiguration gewählte Ansicht wird nicht unterstützt.");
	}

}
