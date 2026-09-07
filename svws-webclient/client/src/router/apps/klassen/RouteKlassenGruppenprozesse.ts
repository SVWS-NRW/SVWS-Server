import type { RouteLocationNormalized, RouteLocationRaw, RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { KlassenGruppenprozesseProps } from "~/components/klassen/gruppenprozesse/KlassenGruppenprozesseProps";
import { api } from "~/router/Api";
import { type RouteKlassen, routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { RouteNode } from "~/router/RouteNode";

const KlassenGruppenprozesse = () => import("~/components/klassen/gruppenprozesse/KlassenGruppenprozesse.vue");

export class RouteKlasseGruppenprozesse extends RouteNode<any, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "klassen.gruppenprozesse", "gruppenprozesse",
			KlassenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	protected async update(): Promise<void | Error | RouteLocationRaw> {
		await routeKlassen.data.updateMapStundenplaene();
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): KlassenGruppenprozesseProps {
		return {
			apiStatus: api.status,
			mapStundenplaene: routeKlassen.data.mapStundenplaene,
			manager: () => routeKlassen.data.manager,
			deleteKlassen: routeKlassen.data.delete,
		};
	}

}

export const routeKlasseGruppenprozesse = new RouteKlasseGruppenprozesse();

