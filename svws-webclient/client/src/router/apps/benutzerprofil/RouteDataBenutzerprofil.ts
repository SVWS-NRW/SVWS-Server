import type { BenutzerDaten } from "@core";
import { shallowRef } from "vue";
import { api } from "~/router/Api";


interface RouteStateBenutzerprofil {
}

export class RouteDataBenutzerprofil {

	private static _defaultState: RouteStateBenutzerprofil = {
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

	public get benutzer(): BenutzerDaten {
		return api.benutzerdaten;
	}

	public patch = async (data: Partial<BenutzerDaten>) => {
		console.log("TODO: Benutzerdaten patchen")
		//api.server.patch
	}

	public patchPasswort = async (alt: string, neu: string) => {
		console.log("TODO Passwort ändern")
		const password = neu.length > 0 ? neu : null;
		await api.server.setPassword(neu, api.schema, api.benutzerdaten.id);
	}
}
