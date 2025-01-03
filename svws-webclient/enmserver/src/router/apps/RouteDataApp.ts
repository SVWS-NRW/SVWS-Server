import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeLeistungen } from "~/router/apps/RouteLeistungen";
import { api } from "~/router/Api";
import type { ENMLeistung } from "@core/core/data/enm/ENMLeistung";


const defaultState = <RouteStateInterface>{
	view: routeLeistungen,
};

export class RouteDataApp extends RouteData<RouteStateInterface> {

	public constructor() {
		super(defaultState);
	}

	/**
	 * Patch-Methode für ENM-Leistungsdaten.
	 *
	 * @param patch   die Daten des Patches mit gültiger ID
	 *
	 * @returns true, falls der Patch erfolgreich war, und ansonsten false
	 */
	public patchLeistung = async (patch: Partial<ENMLeistung>): Promise<boolean> => {
		await api.server.patchENMLeistung(patch);
		return true;
	}

}

