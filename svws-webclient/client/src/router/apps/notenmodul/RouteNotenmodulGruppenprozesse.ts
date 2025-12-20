import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { NotenmodulVerbindungGruppenprozesseProps } from "~/components/notenmodul/NotenmodulVerbindungGruppenprozesseProps";
import { routeApp } from "../RouteApp";

const NotenmodulVerbindungGruppenprozesse = () => import("~/components/notenmodul/NotenmodulVerbindungGruppenprozesse.vue");

export class RouteNotenmodulVerbindungGruppenprozesse extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.gruppenprozesse", "gruppenprozesse", NotenmodulVerbindungGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): NotenmodulVerbindungGruppenprozesseProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			deleteVerbindung: routeNotenmodulAdministration.data.delete,
		};
	}

}

export const routeNotenmodulVerbindungGruppenprozesse = new RouteNotenmodulVerbindungGruppenprozesse();

