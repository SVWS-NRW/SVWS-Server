import type { RouteLocationNormalized, RouteLocationRaw, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import type { LehrerGruppenprozesseProps } from "~/components/lehrer/gruppenprozesse/SLehrerGruppenprozesseProps";

const SLehrerGruppenprozesse = () => import("~/components/lehrer/gruppenprozesse/SLehrerGruppenprozesse.vue");

export class RouteLehrerGruppenprozesse extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRERDATEN_ANSEHEN ], "lehrer.gruppenprozesse", "gruppenprozesse", SLehrerGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : "" };
	}

	protected async update() : Promise<void | Error | RouteLocationRaw> {
		await routeLehrer.data.updateMapStundenplaene();
	}

	public getProps(to: RouteLocationNormalized): LehrerGruppenprozesseProps {
		return {
			apiStatus: api.status,
			getPDF: routeLehrer.data.getPDF,
			mapStundenplaene: routeLehrer.data.mapStundenplaene,
			benutzerKompetenzen : api.benutzerKompetenzen,
			serverMode: api.mode,
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			lehrerListeManager: () => routeLehrer.data.manager,
			deleteLehrer: routeLehrer.data.delete,
			deleteLehrerCheck: routeLehrer.data.deleteLehrerCheck,
		};
	}

}

export const routeLehrerGruppenprozesse = new RouteLehrerGruppenprozesse();
