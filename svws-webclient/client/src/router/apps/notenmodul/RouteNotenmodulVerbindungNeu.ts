import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { NotenmodulVerbindungNeuProps } from "~/components/notenmodul/NotenmodulVerbindungNeuProps";
import { routeApp } from "../RouteApp";

const NotenmodulVerbindungNeu = () => import("~/components/notenmodul/NotenmodulVerbindungNeu.vue");

export class RouteNotenmodulVerbindungNeu extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.neu", "neu", NotenmodulVerbindungNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Verbindung Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): NotenmodulVerbindungNeuProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			addCredentials: routeNotenmodulAdministration.data.wenomAddCredentials,
			gotoDefaultView: routeNotenmodulAdministration.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeNotenmodulVerbindungNeu = new RouteNotenmodulVerbindungNeu();
