import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { WenomAuswahlListeManager } from "@ui/components/enm/WenomAuswahlListeManager";

import type { RouteApp } from "../RouteApp";
import { routeError } from "~/router/error/RouteError";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { notenmodulStateImpl } from "~/states/NotenmodulStateImpl";

import { RouteDataNotenmodulAdministration } from "./RouteDataNotenmodulAdministration";
import { routeNotenmodulVerbindungGruppenprozesse } from "./RouteNotenmodulGruppenprozesse";
import { routeNotenmodulKonfiguration } from "./RouteNotenmodulKonfiguration";
import { routeNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import { routeNotenmodulMail } from "./RouteNotenmodulMail";
import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import { routeNotenmodulSynchronisation } from "./RouteNotenmodulSynchronisation";
import { routeNotenmodulVerbindung } from "./RouteNotenmodulVerbindung";
import { routeNotenmodulVerbindungNeu } from "./RouteNotenmodulVerbindungNeu";

const NotenmodulAdministrationApp = () => import("~/components/notenmodul/NotenmodulAdministrationApp.vue");
const NotenmodulAdministrationAuswahl = () => import("~/components/notenmodul/NotenmodulAdministrationAuswahl.vue");

export class RouteNotenmodulAdministration extends RouteAuswahlNode<WenomAuswahlListeManager, RouteDataNotenmodulAdministration, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration", String.raw`notenmodul/administration/:id(-?\d+)?`, NotenmodulAdministrationApp, NotenmodulAdministrationAuswahl, new RouteDataNotenmodulAdministration());
		super.mode = ServerMode.STABLE;
		super.getAuswahlListProps = (props) => ({
			...props,
			manager: () => routeNotenmodulAdministration.data.manager,
		});
		super.text = "Serververbindungen";
		super.children = [
			routeNotenmodulVerbindung,
			routeNotenmodulKonfiguration,
			routeNotenmodulMail,
			routeNotenmodulSynchronisation,
			routeNotenmodulVerbindungNeu,
			routeNotenmodulVerbindungGruppenprozesse,
		];
		super.defaultChild = routeNotenmodulVerbindung;
		super.menugroup = RouteNotenmodulMenuGroup.ADMINISTRATION;
		super.updateIfTarget = this.doUpdateIfTarget;
		this.isHidden = () => this.checkHidden();
	}

	protected checkHidden() {
		try {
			if (notenmodulStateImpl.istAdminLehrer === false) {
				return routeNotenmodulLeistungen.getRouteDefaultChild();
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected doUpdateIfTarget = async () => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};

}

export const routeNotenmodulAdministration = new RouteNotenmodulAdministration();
