import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "../RouteApp";
import { RouteDataNotenmodulAdministration } from "./RouteDataNotenmodulAdministration";
import { routeNotenmodulKonfiguration } from "./RouteNotenmodulKonfiguration";
import { routeNotenmodulSynchronisation } from "./RouteNotenmodulSynchronisation";
import { routeNotenmodulVerbindungNeu } from "./RouteNotenmodulVerbindungNeu";
import { routeNotenmodulVerbindungGruppenprozesse } from "./RouteNotenmodulGruppenprozesse";
import { routeNotenmodulVerbindung } from "./RouteNotenmodulVerbindung";
import { routeNotenmodulMail } from "./RouteNotenmodulMail";
import { notenmodulStateImpl } from "~/states/NotenmodulStateImpl";
import { routeNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import { routeError } from "~/router/error/RouteError";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { WenomAuswahlListeManager } from "@ui/components/enm/WenomAuswahlListeManager";

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
