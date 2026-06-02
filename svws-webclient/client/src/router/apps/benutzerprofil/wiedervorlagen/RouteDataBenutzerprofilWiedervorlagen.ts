import { ArrayList, type BenutzerDaten, type List, type WiedervorlageEintrag } from "@core";

import { StateManager } from "@ui";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";

interface RouteStateWiedervorlagen {
	wiedervorlagenListe: List<WiedervorlageEintrag>;
}

const defaultState = <RouteStateWiedervorlagen> {
	wiedervorlagenListe: new ArrayList<WiedervorlageEintrag>(),
};

export class RouteDataBenutzerprofilWiedervorlagen extends StateManager<RouteStateWiedervorlagen> {

	public constructor() {
		super(defaultState);
	}

	public get benutzer(): BenutzerDaten {
		return api.benutzerdaten;
	}

	/** Lädt die Wiedervorlagen */
	public async ladeWiedervorlagen(): Promise<void> {
		const wiedervorlagenListe = await api.server.getWiedervorlageListe(api.schema);
		this.setPatchedState({ wiedervorlagenListe });
	}

	/** Getter für die Wiedervorlage-Liste */
	get wiedervorlagenListe(): List<WiedervorlageEintrag> {
		return this._state.value.wiedervorlagenListe;
	}

	/**
	 * Navigate to Route of Person
	 * @param eintrag
	 */
	goToPerson = async (eintrag: WiedervorlageEintrag) => {
		const { typPerson } = eintrag;

		switch (typPerson) {
			case 1:
				await RouteManager.doRoute(routeLehrer.getRoute({ id: eintrag.idPerson }));
				break;
			case 2:
				await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.idPerson }));
				break;
			case 3:
				// add route to erzieher
				break;
			default:
				break;
		}
	};

}
