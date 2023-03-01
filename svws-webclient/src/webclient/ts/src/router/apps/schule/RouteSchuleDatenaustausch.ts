import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { SchuleDatenaustauschProps } from "~/components/schule/datenaustausch/SSchuleDatenaustauschAppProps";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchule } from "../RouteSchule";
import SSchuleDatenaustausch from "~/components/schule/datenaustausch/SSchuleDatenaustauschApp.vue"

class RouteDataSchuleDatenaustausch {

	setGostLupoImportMDBFuerJahrgang = async (formData: FormData) => {
		try {
			const res = await api.server.setGostLupoImportMDBFuerJahrgang( formData, api.schema);
			return res.success;
		} catch(e) {
			return false;
		}
	}
}

export class RouteSchuleDatenaustausch extends RouteNode<RouteDataSchuleDatenaustausch, RouteSchule> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule_datenaustausch", "datenaustausch", SSchuleDatenaustausch, new RouteDataSchuleDatenaustausch());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Datenaustausch";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschProps {
		return {
			setGostLupoImportMDBFuerJahrgang: this.data.setGostLupoImportMDBFuerJahrgang,
		};
	}

}

export const routeSchuleDatenaustausch = new RouteSchuleDatenaustausch();

