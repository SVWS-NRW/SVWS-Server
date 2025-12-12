import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { NotenmodulKonfigurationNeuProps } from "~/components/notenmodul/NotenmodulKonfigurationNeuProps";
import { routeApp } from "../RouteApp";

const NotenmodulKonfigurationNeu = () => import("~/components/notenmodul/NotenmodulKonfigurationNeu.vue");

export class RouteNotenmodulKonfigurationNeu extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.neu", "neu", NotenmodulKonfigurationNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konfiguration Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): NotenmodulKonfigurationNeuProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			addCredentials: routeNotenmodulAdministration.data.wenomAddCredentials,
			gotoDefaultView: routeNotenmodulAdministration.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeNotenmodulKonfigurationNeu = new RouteNotenmodulKonfigurationNeu();
