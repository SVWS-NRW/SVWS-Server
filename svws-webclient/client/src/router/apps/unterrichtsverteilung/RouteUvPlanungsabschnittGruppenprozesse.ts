import type { RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";

const SUvPlanungsabschnittGruppenprozesse = () => import("~/components/unterrichtsverteilung/gruppenprozesse/SUvPlanungsabschnittGruppenprozesse.vue");

export class RouteUvPlanungsabschnittGruppenprozesse extends RouteNode<any, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "uv.planungsabschnitt.gruppenprozesse", "gruppenprozesse", SUvPlanungsabschnittGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Gruppenprozesse";
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps() {
		const parent = this.parent;
		if (parent === undefined) {
			throw new DeveloperNotificationException('Die UV-Gruppenprozesse-Route hat keine Elternroute.');
		}
		return {
			benutzerKompetenzen: benutzerStateImpl.kompetenzen,
			manager: () => parent.data.manager,
			deleteAuswahl: parent.data.delete,
		};
	}

}

export const routeUvPlanungsabschnittGruppenprozesse = new RouteUvPlanungsabschnittGruppenprozesse();
