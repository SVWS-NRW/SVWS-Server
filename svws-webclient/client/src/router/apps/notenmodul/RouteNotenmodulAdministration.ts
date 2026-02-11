import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { WenomAuswahlListeManager } from "@ui";

import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "../RouteApp";
import { RouteDataNotenmodulAdministration } from "./RouteDataNotenmodulAdministration";
import { routeNotenmodulKonfiguration } from "./RouteNotenmodulKonfiguration";
import type { NotenmodulAdministrationAppProps } from "~/components/notenmodul/NotenmodulAdministrationAppProps";
import type { NotenmodulAdministrationAuswahlProps } from "~/components/notenmodul/NotenmodulAdministrationAuswahlProps";
import { routeNotenmodulSynchronisation } from "./RouteNotenmodulSynchronisation";
import { routeNotenmodulVerbindungNeu } from "./RouteNotenmodulVerbindungNeu";
import { routeNotenmodulVerbindungGruppenprozesse } from "./RouteNotenmodulGruppenprozesse";
import { routeNotenmodulVerbindung } from "./RouteNotenmodulVerbindung";
import { routeNotenmodulMail } from "./RouteNotenmodulMail";

const NotenmodulAdministrationApp = () => import("~/components/notenmodul/NotenmodulAdministrationApp.vue");
const NotenmodulAdministrationAuswahl = () => import("~/components/notenmodul/NotenmodulAdministrationAuswahl.vue");

export class RouteNotenmodulAdministration extends RouteAuswahlNode<WenomAuswahlListeManager, RouteDataNotenmodulAdministration, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration", String.raw`notenmodul/administration/:id(-?\d+)?`, NotenmodulAdministrationApp, NotenmodulAdministrationAuswahl, new RouteDataNotenmodulAdministration());
		super.mode = ServerMode.ALPHA;
		super.getAuswahlListProps = (props) => (<NotenmodulAdministrationAuswahlProps>{
			...props,
			manager: () => routeNotenmodulAdministration.data.manager,
		});
		super.getAuswahlProps = props => (<NotenmodulAdministrationAppProps>{
			...props,
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
	}

	protected doUpdateIfTarget = async () => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};

}

export const routeNotenmodulAdministration = new RouteNotenmodulAdministration();
