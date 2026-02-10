import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { api } from "~/router/Api";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import type { SLehrerAllgemeinesGruppenprozesseProps } from "~/components/lehrer/allgemeines/SLehrerAllgemeinesGruppenprozesseProps";

const SLehrerAllgemeinesGruppenprozesse = () => import("~/components/lehrer/allgemeines/SLehrerAllgemeinesGruppenprozesse.vue");


export class RouteLehrerAllgemeinesGruppenprozesse extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "lehrer.gruppenprozesse.allgemeines", "gruppenprozesse/allgemeines", SLehrerAllgemeinesGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Allgemeines";
	}

	protected async update(): Promise<void | Error | RouteLocationRaw> {
		await routeLehrer.data.updateMapStundenplaene();
	}

	public getProps(to: RouteLocationNormalized): SLehrerAllgemeinesGruppenprozesseProps {
		return {
			apiStatus: api.status,
			serverMode: api.mode,
			getPDF: routeLehrer.data.getPDF,
			sendEMail: routeLehrer.data.sendEMail,
			mapStundenplaene: routeLehrer.data.mapStundenplaene,
			benutzerKompetenzen: api.benutzerKompetenzen,
			lehrerListeManager: () => routeLehrer.data.manager,
			deleteLehrer: routeLehrer.data.delete,
			deleteCheck: routeLehrer.data.deleteCheck,
		};
	}

}

export const routeLehrerAllgemeinesGruppenprozesse = new RouteLehrerAllgemeinesGruppenprozesse();

