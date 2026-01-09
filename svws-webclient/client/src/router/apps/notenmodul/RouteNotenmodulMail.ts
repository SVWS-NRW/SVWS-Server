import { BenutzerKompetenz, Schulform, ServerMode, type DeveloperNotificationException } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { RouteLocationNormalized, RouteParams } from "vue-router";
import type { NotenmodulMailProps } from "~/components/notenmodul/NotenmodulMailProps";
import { routeError } from "~/router/error/RouteError";

const NotenmodulMail = () => import("~/components/notenmodul/NotenmodulMail.vue");

export class RouteNotenmodulMail extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.mail", "mail", NotenmodulMail);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Mail";
		this.isHidden = (params?: RouteParams) => this.checkHidden(params);
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { id } = (params === undefined) ? { id: undefined } : RouteNode.getIntParams(params, ["id"]);
			if (id === -1) {
				return routeNotenmodulAdministration.getRouteDefaultChild({ id });
			}
			if ((id !== undefined) && !routeNotenmodulAdministration.data.manager.getConnectionResponse(id).success) {
				return routeNotenmodulAdministration.getRouteDefaultChild({ id });
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}


	public getProps(to: RouteLocationNormalized): NotenmodulMailProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			serverConfig: () => routeNotenmodulAdministration.data.mapEnmServerConfigServer,
			setServerConfigElement: routeNotenmodulAdministration.data.wenomSetServerConfigElement,
			updateServerConnection: routeNotenmodulAdministration.data.wenomUpdateServerConnection,
		};
	}

}

export const routeNotenmodulMail = new RouteNotenmodulMail();
