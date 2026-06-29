
import type { WiedervorlageState } from "@ui";
import { StateManager } from "@ui";
import { api } from "~/router/Api";
import type { List, BenutzergruppeListeEintrag, WiedervorlageEintrag } from "@core";
import { ArrayList } from "@core";

interface State {
	valid: boolean;
}

interface BenutzerGruppen {
	data: List<BenutzergruppeListeEintrag>
}

interface WiedervorlageReactiveState {
	benutzerGruppen: BenutzerGruppen & State;
}

/**
 * Die Schnittstelle für den Zustand der Schuljahresabschnitte und des aktuell ausgewählten Wiedervorlages
 */
export class WiedervorlageStateImpl extends StateManager<WiedervorlageReactiveState> implements WiedervorlageState {

	public constructor() {
		super({
			benutzerGruppen: { data: new ArrayList(), valid: false },
		});
	}

	get benutzerGruppen(): List<BenutzergruppeListeEintrag> {
		return this.state.benutzerGruppen.data;
	}

	init = async () => {
		await this.getBenutzergruppen();
	};

	addWiedervorlage = async (data: Partial<WiedervorlageEintrag>) => {
		const wiedervorlage =	await api.server.addWiedervorlageEintrag(
			data,
			api.schema);
		return wiedervorlage;
	};

	getBenutzergruppen = async () => {
		if (!this.state.benutzerGruppen.valid) {
			// only for invalid data fetch fresh data
			let data;

			if (api.benutzerIstAdmin) {
				// for admin get all benutzergruppen
				data = await api.server.getBenutzergruppenliste(api.schema);
			} else {
				// else get own benutzergruppen
				const benutzerdaten = await api.server.getBenutzerDatenEigene(api.schema);
				data = benutzerdaten.gruppen;
			}

			// update state and mark as valid
			this.setPatchedState({ benutzerGruppen: { valid: true, data } });
		}

		// return data
		return this.state.benutzerGruppen.data;
	};






}

export const wiedervorlageStateImpl = new WiedervorlageStateImpl();
