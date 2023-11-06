import type { BenutzerDaten } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { routeBenutzerprofil } from "./RouteBenutzerprofil";
import { routeBenutzerprofilDaten } from "./daten/RouteBenutzerprofilDaten";


interface RouteStateBenutzerprofil {
	view: RouteNode<any, any>;
}

export class RouteDataBenutzerprofil {

	private static _defaultState: RouteStateBenutzerprofil = {
		view: routeBenutzerprofilDaten,
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
		if (routeBenutzerprofil.children.includes(view))
			this.setPatchedState({ view });
		else
			throw new Error("Diese für das Benutzerprofil gewählte Ansicht wird nicht unterstützt.");
	}

	public get benutzer(): BenutzerDaten {
		return api.benutzerdaten;
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	public patch = async (data: Partial<BenutzerDaten>) => {
		console.log("TODO: Benutzerdaten patchen")
		//api.server.patch
	}
}
