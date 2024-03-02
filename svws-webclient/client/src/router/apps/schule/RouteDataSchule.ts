import type { SchuleStammdaten } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeSchuleBenutzer } from "~/router/apps/schule/benutzer/RouteSchuleBenutzer";


interface RouteStateSchule extends RouteStateInterface {
}

const defaultState = <RouteStateSchule> {
	view: routeSchuleBenutzer,
};

export class RouteDataSchule extends RouteData<RouteStateSchule> {

	public constructor() {
		super(defaultState);
	}

	patch = async (data : Partial<SchuleStammdaten>) => {
		const stammdaten = api.schuleStammdaten;
		await api.server.patchSchuleStammdaten(data, api.schema);
		Object.assign(stammdaten, data);
		api.updatedApiData();
		this.commit();
	}

}
