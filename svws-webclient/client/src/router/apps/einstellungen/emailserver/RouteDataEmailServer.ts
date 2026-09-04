import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
import { api } from "~/router/Api";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import { SMTPServerKonfiguration } from "@core/core/data/email/SMTPServerKonfiguration";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";


interface RouteStateEmailServer extends RouteStateInterface {
	smtpServerKonfiguration: SMTPServerKonfiguration;
}

const defaultState = <RouteStateEmailServer> {
	smtpServerKonfiguration: new SMTPServerKonfiguration(),
};

export class RouteDataEmailServer extends RouteData<RouteStateEmailServer> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		let smtpServerKonfiguration = new SMTPServerKonfiguration();
		if (benutzerStateImpl.istAdmin || benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN)) {
			smtpServerKonfiguration = await api.server.getSMTPServerKonfiguration(api.schema);
		}
		this.setPatchedState({ smtpServerKonfiguration });
	}

	public async entferneDaten() {
		const smtpServerKonfiguration = new SMTPServerKonfiguration();
		this.setPatchedState({ smtpServerKonfiguration });
	}

	get smtpServerKonfiguration(): SMTPServerKonfiguration {
		return this._state.value.smtpServerKonfiguration;
	}

	patchSMTServerKonfiguration = async (data: Partial<SMTPServerKonfiguration>) => {
		const smtpServerKonfiguration = this._state.value.smtpServerKonfiguration;
		await api.server.patchSMTPServerKonfiguration(data, api.schema);
		Object.assign(smtpServerKonfiguration, data);
		this.setPatchedState({ smtpServerKonfiguration });
	};

}
