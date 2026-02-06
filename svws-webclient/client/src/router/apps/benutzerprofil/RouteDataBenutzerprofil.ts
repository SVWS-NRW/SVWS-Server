import { BenutzerEMailDaten, BenutzerTyp, type BenutzerDaten } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";


interface RouteStateBenutzerprofil extends RouteStateInterface {
	benutzerEMailDaten: BenutzerEMailDaten;
}

const defaultState = <RouteStateBenutzerprofil> {
	benutzerEMailDaten: new BenutzerEMailDaten(),
};

export class RouteDataBenutzerprofil extends RouteData<RouteStateBenutzerprofil> {

	public constructor() {
		super(defaultState);
	}

	public get benutzer(): BenutzerDaten {
		return api.benutzerdaten;
	}

	public get benutzerEMailDaten(): BenutzerEMailDaten {
		return this._state.value.benutzerEMailDaten;
	}

	public patch = async (data: Partial<BenutzerDaten>) => {
		console.log("TODO: Benutzerdaten patchen");
		// api.server.patch
	};

	public patchPasswort = async (eins: string, zwei: string): Promise<boolean> => {
		if (eins !== zwei) {
			return false;
		}
		const password = eins.length > 0 ? eins : null;
		try {
			await api.server.setPassword(password, api.schema, api.benutzerdaten.id);
			return true;
		} catch {
			return false;
		}
	};

	public patchPasswortWenom = async (eins: string, zwei: string): Promise<boolean> => {
		if ((eins !== zwei) || (api.benutzertyp !== BenutzerTyp.LEHRER)) {
			return false;
		}
		const password = eins.length > 0 ? eins : null;
		try {
			await api.server.setENMLehrerPassword(password, api.schema, api.benutzerIDLehrer);
			return true;
		} catch {
			return false;
		}
	};

	public passwordResetWenom = async () => {
		try {
			if (api.benutzertyp !== BenutzerTyp.LEHRER) {
				return false;
			}
			await api.server.resetENMLehrerPasswordToInitial(api.schema, api.benutzerIDLehrer);
			return true;
		} catch {
			return false;
		}
	};

	public patchBenutzerEMailDaten = async (data: Partial<BenutzerEMailDaten>) => {
		await api.server.patchBenutzerEmailDaten(data, api.schema);
	};

	public async ladeDaten() {
		const benutzerEMailDaten = await api.server.getBenutzerEmailDaten(api.schema);
		this.setPatchedState({ benutzerEMailDaten });
	}
}
