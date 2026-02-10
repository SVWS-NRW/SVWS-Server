import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import type { RouteTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import { routeTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import type { TelefonartenGruppenprozesseProps } from "~/components/schule/kataloge/telefonarten/gruppenprozesse/TelefonartenGruppenprozesseProps";

const TelefonartenGruppenprozesse = () => import("~/components/schule/kataloge/telefonarten/gruppenprozesse/TelefonartenGruppenprozesse.vue");

export class RouteTelefonartenGruppenprozesse extends RouteNode<any, RouteTelefonarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.telefonarten.gruppenprozesse", "gruppenprozesse", TelefonartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): TelefonartenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeTelefonarten.data.manager,
			delete: routeTelefonarten.data.delete,
			deleteCheck: routeTelefonarten.data.deleteCheck,
			gotoDefaultView: routeTelefonarten.data.gotoDefaultView,
		};
	}
}

export const routeTelefonartenGruppenprozesse = new RouteTelefonartenGruppenprozesse();

