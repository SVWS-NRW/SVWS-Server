import type { RouteLocationNormalized, RouteLocationRaw, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import type { KlassenGruppenprozesseProps } from "~/components/klassen/gruppenprozesse/SKlassenGruppenprozesseProps";
import { ViewType } from "@ui";

const SKlassenGruppenprozesse = () => import("~/components/klassen/gruppenprozesse/SKlassenGruppenprozesse.vue");

export class RouteKlasseGruppenprozesse extends RouteNode<any, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN ], "klassen.gruppenprozesse", "gruppenprozesse", SKlassenGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	protected async update() : Promise<void | Error | RouteLocationRaw> {
		await routeKlassen.data.updateMapStundenplaene();
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : "" };
	}

	public getProps(to: RouteLocationNormalized): KlassenGruppenprozesseProps {
		return {
			apiStatus: api.status,
			getPDF: routeKlassen.data.getPDF,
			mapStundenplaene: routeKlassen.data.mapStundenplaene,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schulgliederungen: api.schulgliederungen,
			manager: () => routeKlassen.data.manager,
			deleteKlassen: routeKlassen.data.delete,
		};
	}

}

export const routeKlasseGruppenprozesse = new RouteKlasseGruppenprozesse();

