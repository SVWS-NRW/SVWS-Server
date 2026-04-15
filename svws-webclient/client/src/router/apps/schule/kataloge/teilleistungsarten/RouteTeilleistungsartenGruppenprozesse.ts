import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { ViewType } from "@ui";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { api } from "~/router/Api";
import type { TeilleistungsartenGruppenprozesseProps } from
	"~/components/schule/kataloge/teilleistungsarten/gruppenprozesse/TeilleistungsartenGruppenprozesseProps";
import { routeTeilleistungsarten, type RouteTeilleistungsarten } from "./RouteTeilleistungsarten";

const TeilleistungsartenGruppenprozesse = () => import(
	"~/components/schule/kataloge/teilleistungsarten/gruppenprozesse/TeilleistungsartenGruppenprozesse.vue");

class RouteTeilleistungsartenGruppenprozesse extends RouteNode<any, RouteTeilleistungsarten> {
	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.teilleistungsarten.gruppenprozesse",
			"gruppenprozesse",
			TeilleistungsartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): TeilleistungsartenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeTeilleistungsarten.data.manager,
			delete: routeTeilleistungsarten.data.delete,
			deleteCheck: routeTeilleistungsarten.data.deleteCheck,
			gotoDefaultView: routeTeilleistungsarten.data.gotoDefaultView,
		};
	}
}

export const routeTeilleistungsartenGruppenprozesse = new RouteTeilleistungsartenGruppenprozesse();

