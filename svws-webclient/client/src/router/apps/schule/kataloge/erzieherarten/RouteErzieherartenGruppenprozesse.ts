import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import type { RouteErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import { routeErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import type { ErzieherartenGruppenprozesseProps } from "~/components/schule/kataloge/erzieherarten/gruppenprozesse/ErzieherartenGruppenprozesseProps";

const ErzieherartenGruppenprozesse = () => import("~/components/schule/kataloge/erzieherarten/gruppenprozesse/ErzieherartenGruppenprozesse.vue");

export class RouteErzieherartenGruppenprozesse extends RouteNode<any, RouteErzieherarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.erzieherarten.gruppenprozesse", "gruppenprozesse", ErzieherartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): ErzieherartenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeErzieherarten.data.manager,
			delete: routeErzieherarten.data.delete,
			deleteCheck: routeErzieherarten.data.deleteCheck,
			gotoDefaultView: routeErzieherarten.data.gotoDefaultView,
		};
	}
}

export const routeErzieherartenGruppenprozesse = new RouteErzieherartenGruppenprozesse();

