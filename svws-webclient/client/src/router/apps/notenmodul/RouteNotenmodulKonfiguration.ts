import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { RouteLocationNormalized } from "vue-router";
import type { NotenmodulKonfigurationProps } from "~/components/notenmodul/NotenmodulKonfigurationProps";
import { api } from "~/router/Api";
import { ConfigElement } from "@ui";

const NotenmodulKonfiguration = () => import("~/components/notenmodul/NotenmodulKonfiguration.vue");

export class RouteNotenmodulKonfiguration extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.konfiguration", "konfiguration", NotenmodulKonfiguration);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konfiguration";
		api.config.addElements([
			new ConfigElement("notenmodul.leistungen.tabelle.spaltenanzeige", "global", `[["Kurs", "true"], ["Kursart", "true"], ["Lehrer", "true"], ["Quartal", "true"], ["Note", "true"], ["Mahnung", "true"], ["FS", "true"], ["FSU", "true"], ["Bemerkung", " true"]]`),
			new ConfigElement("notenmodul.teilleistungen.tabelle.spaltenanzeige", "global", "null"),
		]);
	}


	public getProps(to: RouteLocationNormalized): NotenmodulKonfigurationProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			serverConfig: () => routeNotenmodulAdministration.data.mapEnmServerConfigServer,
			setServerConfigElement: routeNotenmodulAdministration.data.wenomSetServerConfigElement,
			updateServerConnection: routeNotenmodulAdministration.data.wenomUpdateServerConnection,
			mapLeistungenTabelleSpaltenanzeige: () => routeNotenmodulAdministration.data.mapLeistungenTabelleSpaltenanzeige,
			setMapLeistungenTabelleSpaltenanzeige: routeNotenmodulAdministration.data.setMapLeistungenTabelleSpaltenanzeige,
			mapTeilleistungenTabelleSpaltenanzeige: () => routeNotenmodulAdministration.data.mapTeilleistungenTabelleSpaltenanzeige,
			setMapTeilleistungenTabelleSpaltenanzeige: routeNotenmodulAdministration.data.setMapTeilleistungenTabelleSpaltenanzeige,
		};
	}

}

export const routeNotenmodulKonfiguration = new RouteNotenmodulKonfiguration();
