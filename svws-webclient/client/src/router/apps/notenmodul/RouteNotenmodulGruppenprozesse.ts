import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { NotenmodulKonfigurationGruppenprozesseProps } from "~/components/notenmodul/NotenmodulKonfigurationGruppenprozesseProps";
import { routeApp } from "../RouteApp";

const NotenmodulKonfigurationGruppenprozesse = () => import("~/components/notenmodul/NotenmodulKonfigurationGruppenprozesse.vue");

export class RouteNotenmodulKonfigurationGruppenprozesse extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.gruppenprozesse", "gruppenprozesse", NotenmodulKonfigurationGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): NotenmodulKonfigurationGruppenprozesseProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			deleteKonfiguration: routeNotenmodulAdministration.data.delete,
		};
	}

}

export const routeNotenmodulKonfigurationGruppenprozesse = new RouteNotenmodulKonfigurationGruppenprozesse();

