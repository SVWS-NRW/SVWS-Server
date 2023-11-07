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

	public patchPasswort = async (eins: string, zwei: string): Promise<boolean> => {
		if (eins !== zwei)
			return false;
		const password = eins.length > 0 ? eins : null;
		try {
			await api.server.setPassword(password, api.schema, api.benutzerdaten.id);
			return true;
		} catch (e) {
			return false;
		}
	}
}
