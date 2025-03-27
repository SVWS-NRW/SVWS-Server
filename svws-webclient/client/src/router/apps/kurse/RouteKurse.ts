
import type { KursListeManager} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { type RouteApp } from "~/router/apps/RouteApp";
import { RouteDataKurse } from "~/router/apps/kurse/RouteDataKurse";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";

import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeKurseGruppenprozesse } from "./RouteKurseGruppenprozesse";
import { routeKurseNeu } from "./RouteKurseNeu";
import type { KurseAuswahlProps } from "~/components/kurse/SKurseAuswahlProps";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";
import { AppMenuGroup } from "@ui";


const SKurseAuswahl = () => import("~/components/kurse/SKurseAuswahl.vue");
const SKurseApp = () => import("~/components/kurse/SKurseApp.vue");

export class RouteKurse extends RouteAuswahlNode<KursListeManager, RouteDataKurse, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN ], "kurse", "kurse/:id(\\d+)?", SKurseApp, SKurseAuswahl, new RouteDataKurse());
		super.mode = ServerMode.STABLE;
		super.text = "Kurse";
		super.children = [
			routeKursDaten,
			routeKurseGruppenprozesse,
			routeKurseNeu,
		];
		super.defaultChild = routeKursDaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-book-2-line";
		super.getAuswahlListProps = (props) => (<KurseAuswahlProps>{
			...props,
			setFilterNurSichtbar: this.data.setFilterNurSichtbar,
		});
		api.config.addElements([
			new ConfigElement("kurse.auswahl.filterNurSichtbar", "user", "true"),
		]);
	}

}

export const routeKurse = new RouteKurse();
