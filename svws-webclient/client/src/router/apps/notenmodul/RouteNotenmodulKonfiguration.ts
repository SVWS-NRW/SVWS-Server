import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { RouteLocationNormalized, RouteParams } from "vue-router";
import type { NotenmodulKonfigurationProps } from "~/components/notenmodul/NotenmodulKonfigurationProps";
import { routeError } from "~/router/error/RouteError";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ConfigElement } from "@ui/utils/Config";

const NotenmodulKonfiguration = () => import("~/components/notenmodul/NotenmodulKonfiguration.vue");

export class RouteNotenmodulKonfiguration extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.konfiguration", "konfiguration", NotenmodulKonfiguration);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konfiguration";
		this.isHidden = (params?: RouteParams) => this.checkHidden(params);
		configStateImpl.config.addElements([
			new ConfigElement("notenmodul.konfiguration.tabelle.gruppierung", "user", "Keine"),
		]);
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { id } = (params === undefined) ? { id: undefined } : RouteNode.getIntParams(params, ["id"]);
			if ((id !== -1) && (id !== undefined) && !(routeNotenmodulAdministration.data.manager.getConnectionResponse(id)?.success ?? false)) {
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
