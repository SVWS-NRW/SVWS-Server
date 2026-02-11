import type { DeveloperNotificationException } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { RouteLocationNormalized, RouteParams } from "vue-router";
import type { NotenmodulSynchronisationProps } from "~/components/notenmodul/NotenmodulSynchronisationProps";
import { routeError } from "~/router/error/RouteError";

const NotenmodulSynchronisation = () => import("~/components/notenmodul/NotenmodulSynchronisation.vue");

export class RouteNotenmodulSynchronisation extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.synchronisation", "synchronisation", NotenmodulSynchronisation);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Synchronisation";
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

	public getProps(to: RouteLocationNormalized): NotenmodulSynchronisationProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			synchronize: routeNotenmodulAdministration.data.wenomSynchronize,
			download: routeNotenmodulAdministration.data.wenomDownload,
			upload: routeNotenmodulAdministration.data.wenomUpload,
			truncate: routeNotenmodulAdministration.data.wenomTruncate,
			reset: routeNotenmodulAdministration.data.wenomReset,
		};
	}

}

export const routeNotenmodulSynchronisation = new RouteNotenmodulSynchronisation();
