import type { RouteLocationNormalized, RouteLocationRaw, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { ViewType } from "@ui";
import type { SchuelerGruppenprozesseProps } from "~/components/schueler/gruppenprozesse/SSchuelerGruppenprozesseProps";
import { api } from "~/router/Api";

const SSchuelerGruppenprozesse = () => import("~/components/schueler/gruppenprozesse/SSchuelerGruppenprozesse.vue");

export class RouteSchuelerGruppenprozesse extends RouteNode<any, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.gruppenprozesse", "gruppenprozesse", SSchuelerGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	protected async update() : Promise<void | Error | RouteLocationRaw> {
		await routeSchueler.data.updateMapStundenplaene();
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : "" };
	}

	public getProps(to: RouteLocationNormalized): SchuelerGruppenprozesseProps {
		return {
			apiStatus: api.status,
			getPDF: routeSchueler.data.getPDF,
			mapStundenplaene: routeSchueler.data.mapStundenplaene,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schuelerListeManager: () => routeSchueler.data.schuelerListeManager,
			deleteSchueler: routeSchueler.data.delete,
			deleteSchuelerCheck: routeSchueler.data.deleteSchuelerCheck,
		};
	}

}

export const routeSchuelerGruppenprozesse = new RouteSchuelerGruppenprozesse();

