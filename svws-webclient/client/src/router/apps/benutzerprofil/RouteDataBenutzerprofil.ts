import type { BenutzerDaten } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";


interface RouteStateBenutzerprofil extends RouteStateInterface {
}

const defaultState = <RouteStateBenutzerprofil> {
};

export class RouteDataBenutzerprofil extends RouteData<RouteStateBenutzerprofil> {

	public constructor() {
		super(defaultState);
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
