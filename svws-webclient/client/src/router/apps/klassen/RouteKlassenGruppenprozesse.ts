import type { RouteLocationNormalized, RouteLocationRaw, RouteParamsRawGeneric } from "vue-router";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import type { KlassenGruppenprozesseProps } from "~/components/klassen/gruppenprozesse/KlassenGruppenprozesseProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

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

