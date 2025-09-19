import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeJahrgaenge } from "./RouteJahrgaenge";
import type { RouteJahrgaenge } from "~/router/apps/schule/schulbezogen/jahrgaenge/RouteJahrgaenge";
import type { SchuleJahrgangNeuProps } from "~/components/schule/schulbezogen/jahrgaenge/SJahrgaengeNeuProps";
import { api } from "~/router/Api";

const SJahrgaengeNeu = () => import("~/components/schule/schulbezogen/jahrgaenge/SJahrgaengeNeu.vue");

export class RouteJahrgaengeNeu extends RouteNode<any, RouteJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.jahrgaenge.neu", "neu", SJahrgaengeNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchuleJahrgangNeuProps {
		return {
			manager: () => routeJahrgaenge.data.manager,
			schuljahr: routeApp.data.aktAbschnitt.value.schuljahr,
			schulform: api.schulform,
			addJahrgang: routeJahrgaenge.data.add,
			goToDefaultView: routeJahrgaenge.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeJahrgaengeNeu = new RouteJahrgaengeNeu();
