import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
import type { SchuleStammdaten } from "@core";
import { api } from "~/router/Api";

interface RouteStateSchuleAdressdaten extends RouteStateInterface {
}

const defaultState = <RouteStateSchuleAdressdaten>{
};

export class RouteDataSchuleAdressdaten extends RouteData<RouteStateSchuleAdressdaten> {

	public constructor() {
		super(defaultState);
	}

	patch = async (data: Partial<SchuleStammdaten>) => {
		const stammdaten = api.schuleStammdaten;
		await api.server.patchSchuleStammdaten(data, api.schema);
		Object.assign(stammdaten, data);
		api.updatedApiData();
		this.commit();
	};

}
