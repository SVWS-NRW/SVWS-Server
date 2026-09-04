import { RouteManager } from "~/router/RouteManager";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { type RouteStateInterface, RouteData } from "~/router/RouteData";
import type { WiedervorlageEintrag } from "@core/core/data/schule/WiedervorlageEintrag";

interface RouteStateWiedervorlagen extends RouteStateInterface {};

const defaultState = <RouteStateWiedervorlagen> {};

export class RouteDataBenutzerprofilWiedervorlagen extends RouteData<RouteStateInterface> {

	public constructor() {
		super(defaultState);
	}

	/**
	 * Navigate to Route of Person
	 * @param eintrag
	 */
	goToPerson = async (eintrag: WiedervorlageEintrag) => {
		const { idPerson, typPerson } = eintrag;

		switch (typPerson) {
			case 1:
				await RouteManager.doRoute(routeLehrer.getRoute({ id: idPerson }));
				break;
			case 2:
				await RouteManager.doRoute(routeSchueler.getRoute({ id: idPerson }));
				break;
			case 3:
				// add route to erzieher
				break;
			default:
				break;
		}
	};

}
