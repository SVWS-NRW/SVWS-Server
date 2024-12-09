
import type { KursListeManager} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { type RouteApp } from "~/router/apps/RouteApp";
import { RouteDataKurse } from "~/router/apps/kurse/RouteDataKurse";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";

import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeKurseGruppenprozesse } from "./RouteKurseGruppenprozesse";
import { routeKurseNeu } from "./RouteKurseNeu";


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
	}

}

export const routeKurse = new RouteKurse();
