import { BenutzerEMailDaten, type BenutzerDaten } from "@core";

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

	get backticks(): boolean {
		return api.config.getValue("benutzerprofil.notifications.backticks") === 'true';
	}

	setBackticks = async (value: boolean) => {
		await api.config.setValue('benutzerprofil.notifications.backticks', value ? "true" : "false");
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

	public patchBenutzerEMailDaten = async (data: Partial<BenutzerEMailDaten>) => {
		await api.server.patchBenutzerEmailDaten(data, api.schema);
	}

	public async ladeDaten() {
		const benutzerEMailDaten = await api.server.getBenutzerEmailDaten(api.schema);
		this.setPatchedState({benutzerEMailDaten});
	}
}
