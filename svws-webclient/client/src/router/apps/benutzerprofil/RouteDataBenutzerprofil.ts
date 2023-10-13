import { BenutzerDaten } from "@core";
import { shallowRef } from "vue";


import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { type RouteNode } from "~/router/RouteNode";

import { routeKurse } from "~/router/apps/kurse/RouteKurse";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";


interface RouteStateBenutzerprofil {
	benutzer: BenutzerDaten;
	view: RouteNode<any, any>;
}

export class RouteDataBenutzerprofil {

	private static _defaultState: RouteStateBenutzerprofil = {
		benutzer: new BenutzerDaten(),
		view: routeKursDaten,
	}
	private _state = shallowRef(RouteDataBenutzerprofil._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateBenutzerprofil>) {
		this._state.value = Object.assign({ ... RouteDataBenutzerprofil._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateBenutzerprofil>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKurse.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für das Benutzerprofil gewählte Ansicht wird nicht unterstützt.");
	}

	public get benutzer(): BenutzerDaten {
		return this._state.value.benutzer;
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

}
