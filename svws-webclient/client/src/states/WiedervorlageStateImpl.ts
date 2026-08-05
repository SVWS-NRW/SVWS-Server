
import type { WiedervorlageState } from "@ui";
import { StateManager } from "@ui";
import { api } from "~/router/Api";
import type { List, BenutzergruppeListeEintrag, WiedervorlageEintrag } from "@core";
import { ArrayList } from "@core";
import { benutzerStateImpl } from "./BenutzerStateImpl";

interface State {
	valid: boolean;
}

interface BenutzerGruppen {
	data: List<BenutzergruppeListeEintrag>
}

interface Wiedervorlagen {
	data: List<WiedervorlageEintrag>;
}

interface WiedervorlageReactiveState {
	benutzerGruppen: BenutzerGruppen & State;
	wiedervorlagenListe: Wiedervorlagen & State;
}

/**
 * Die Schnittstelle die Anzeige der Wiedervorlagenliste und ihrer API-Methoden
 */
export class WiedervorlageStateImpl extends StateManager<WiedervorlageReactiveState> implements WiedervorlageState {

	public constructor() {
		super({
			benutzerGruppen: { data: new ArrayList(), valid: false },
			wiedervorlagenListe: { data: new ArrayList<WiedervorlageEintrag>(), valid: false },
		});
	}

	public get benutzerGruppen(): List<BenutzergruppeListeEintrag> {
		return this.state.benutzerGruppen.data;
	}

	/** Getter für die Wiedervorlage-Liste */
	public get wiedervorlagenListe(): List<WiedervorlageEintrag> {
		return this.state.wiedervorlagenListe.data;
	}

	/** Initialisierung des States - lädt alle Daten */
	public async init() {
		await Promise.all([
			this.getBenutzergruppen(),
			this.ladeWiedervorlagen(),
		]);
	}

	/** Lädt die Wiedervorlagen */
	public async ladeWiedervorlagen() {
		// for invalid data fetch fresh data
		if (!this.state.wiedervorlagenListe.valid) {
			const wiedervorlagenListe = await api.server.getWiedervorlageListe(api.schema);
			// keep state stale to always get the newest list
			this.setPatchedState({ wiedervorlagenListe: { data: wiedervorlagenListe, valid: false } });
		}
	};

	/** Erstelle ine Wiedervorlage */
	public async addWiedervorlage(data: Partial<WiedervorlageEintrag>) {
		return await api.server.addWiedervorlageEintrag(
			data,
			api.schema);
	};

	/** Patched eine Wiedervorlage */
	public async patchWiedervorlage(data: Partial<WiedervorlageEintrag>, id: number) {
		await api.server.patchWiedervorlageEintrag(data, api.schema, id);
		await this.ladeWiedervorlagen();
	};

	/** Get Benutzergruppen */
	public async getBenutzergruppen() {
		// for invalid data fetch fresh data
		if (!this.state.benutzerGruppen.valid) {
			let data;

			if (benutzerStateImpl.istAdmin) {
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
