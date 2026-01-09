import type { DeveloperNotificationException } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { RouteLocationNormalized, RouteParams } from "vue-router";
import type { NotenmodulKonfigurationProps } from "~/components/notenmodul/NotenmodulKonfigurationProps";
import { api } from "~/router/Api";
import { ConfigElement } from "@ui";
import { routeError } from "~/router/error/RouteError";

const NotenmodulKonfiguration = () => import("~/components/notenmodul/NotenmodulKonfiguration.vue");

export class RouteNotenmodulKonfiguration extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.konfiguration", "konfiguration", NotenmodulKonfiguration);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konfiguration";
		this.isHidden = (params?: RouteParams) => this.checkHidden(params);
		api.config.addElements([
			new ConfigElement("notenmodul.konfiguration.tabelle.gruppierung", "user", "Keine"),
		]);
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { id } = (params === undefined) ? { id: undefined } : RouteNode.getIntParams(params, ["id"]);
			if ((id !== -1) && (id !== undefined) && !routeNotenmodulAdministration.data.manager.getConnectionResponse(id).success) {
				return routeNotenmodulAdministration.getRouteDefaultChild({ id });
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): NotenmodulKonfigurationProps {
		return {
			istLokal: (routeNotenmodulAdministration.data.manager.auswahlID() === -1),
			syncWithLocalConfig: routeNotenmodulAdministration.data.syncWithLocalConfig,
			managerSperrungen: () => routeNotenmodulAdministration.data.managerSperrungen,
			managerSichtbareSpalten: () => routeNotenmodulAdministration.data.managerSichtbareSpalten,
		};
	}

}

export const routeNotenmodulKonfiguration = new RouteNotenmodulKonfiguration();
