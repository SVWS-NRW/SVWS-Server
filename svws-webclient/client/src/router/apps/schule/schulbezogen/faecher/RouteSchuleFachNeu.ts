import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { SchuleFachNeuProps } from "~/components/schule/schulbezogen/faecher/SSchuleFachNeuProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuleFaecher, type RouteSchuleFaecher } from "./RouteSchuleFaecher";
import { routeApp } from "../../../RouteApp";
import { api } from "~/router/Api";

const SSchuleFachNeu = () => import("~/components/schule/schulbezogen/faecher/SSchuleFachNeu.vue");

export class RouteSchuleFachNeu extends RouteNode<any, RouteSchuleFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.faecher.neu", "neu", SSchuleFachNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fach Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchuleFachNeuProps {
		return {
			manager: () => routeSchuleFaecher.data.manager,
			add: routeSchuleFaecher.data.add,
			gotoDefaultView: routeSchuleFaecher.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeSchuleFachNeu = new RouteSchuleFachNeu();
