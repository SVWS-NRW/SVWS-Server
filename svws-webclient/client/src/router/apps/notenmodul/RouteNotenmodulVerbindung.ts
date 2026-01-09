import { BenutzerKompetenz, Schulform, ServerMode, type DeveloperNotificationException } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { RouteLocationNormalized, RouteParams } from "vue-router";
import type { NotenmodulVerbindungProps } from "~/components/notenmodul/NotenmodulVerbindungProps";
import { routeError } from "~/router/error/RouteError";
import { routeNotenmodulKonfiguration } from "./RouteNotenmodulKonfiguration";

const NotenmodulVerbindung = () => import("~/components/notenmodul/NotenmodulVerbindung.vue");

export class RouteNotenmodulVerbindung extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.verbindung", "verbindung", NotenmodulVerbindung);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Verbindung";
		this.isHidden = (params?: RouteParams) => this.checkHidden(params);
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { id } = (params === undefined) ? { id: undefined } : RouteNode.getIntParams(params, ["id"]);
			if (id === -1) {
				return routeNotenmodulKonfiguration.getRoute({ id });
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}


	public getProps(to: RouteLocationNormalized): NotenmodulVerbindungProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			connected: routeNotenmodulAdministration.data.connected,
			connect: routeNotenmodulAdministration.data.connect,
			trustCertificate: routeNotenmodulAdministration.data.trustCertificate,
			updateServerConnection: routeNotenmodulAdministration.data.wenomUpdateServerConnection,
		};
	}

}

export const routeNotenmodulVerbindung = new RouteNotenmodulVerbindung();
