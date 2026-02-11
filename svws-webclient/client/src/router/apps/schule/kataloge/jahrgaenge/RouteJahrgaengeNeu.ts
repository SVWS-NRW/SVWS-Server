import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeJahrgaenge } from "./RouteJahrgaenge";
import type { RouteJahrgaenge } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaenge";
import type { JahrgaengeNeuProps } from "~/components/schule/kataloge/jahrgaenge/JahrgaengeNeuProps";
import { api } from "~/router/Api";

const JahrgaengeNeu = () => import("~/components/schule/kataloge/jahrgaenge/JahrgaengeNeu.vue");

export class RouteJahrgaengeNeu extends RouteNode<any, RouteJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.jahrgaenge.neu", "neu", JahrgaengeNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): JahrgaengeNeuProps {
		return {
			manager: () => routeJahrgaenge.data.manager,
			schuljahr: routeApp.data.aktAbschnitt.value.schuljahr,
			schulform: api.schulform,
			add: routeJahrgaenge.data.add,
			goToDefaultView: routeJahrgaenge.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeJahrgaengeNeu = new RouteJahrgaengeNeu();
