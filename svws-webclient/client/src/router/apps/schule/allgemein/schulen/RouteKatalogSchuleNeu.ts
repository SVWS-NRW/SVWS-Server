import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { KatalogSchuleNeuProps } from "~/components/schule/allgemein/schulen/SKatalogSchuleNeuProps";
import type { RouteKatalogSchulen } from "~/router/apps/schule/allgemein/schulen/RouteKatalogSchulen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "../../../RouteApp";
import { routeKatalogSchulen } from "~/router/apps/schule/allgemein/schulen/RouteKatalogSchulen";
import { api } from "~/router/Api";

const SKatalogSchuleNeu = () => import("~/components/schule/allgemein/schulen/SKatalogSchuleNeu.vue");

export class RouteKatalogSchuleNeu extends RouteNode<any, RouteKatalogSchulen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.schulen.neu", "neu", SKatalogSchuleNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): KatalogSchuleNeuProps {
		return {
			schuleListeManager: () => routeKatalogSchulen.data.manager,
			add: routeKatalogSchulen.data.add,
			gotoDefaultView: routeKatalogSchulen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeKatalogSchuleNeu = new RouteKatalogSchuleNeu();
