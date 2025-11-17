import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { RouteLocationNormalized } from "vue-router";
import type { NotenmodulKonfigurationProps } from "~/components/notenmodul/NotenmodulKonfigurationProps";

const NotenmodulKonfiguration = () => import("~/components/notenmodul/NotenmodulKonfiguration.vue");

export class RouteNotenmodulKonfigurationData extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.daten", "daten", NotenmodulKonfiguration);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konfiguration";
	}

	public getProps(to: RouteLocationNormalized): NotenmodulKonfigurationProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			connected: routeNotenmodulAdministration.data.connected,
			connect: routeNotenmodulAdministration.data.connect,
			trustCertificate: routeNotenmodulAdministration.data.trustCertificate,
			serverConfig: () => routeNotenmodulAdministration.data.mapEnmServerConfigServer,
			setServerConfigElement: routeNotenmodulAdministration.data.wenomSetServerConfigElement,
			updateServerConnection: routeNotenmodulAdministration.data.wenomUpdateServerConnection,
		};
	}

}

export const routeNotenmodulKonfigurationData = new RouteNotenmodulKonfigurationData();
