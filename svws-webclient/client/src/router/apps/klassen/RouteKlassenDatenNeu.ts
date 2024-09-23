import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeApp } from "../RouteApp";
import type { KlassenDatenNeuProps } from "~/components/klassen/daten/SKlassenDatenNeuProps";
import { api } from "~/router/Api";

const SKlassenDatenNeu = () => import("~/components/klassen/daten/SKlassenDatenNeu.vue");

export class RouteKlassenDatenNeu extends RouteNode<any, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN ], "klassen.datenNeu", "neu", SKlassenDatenNeu);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klasse Neu";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): KlassenDatenNeuProps {
		return {
			klassenListeManager: () => routeKlassen.data.klassenListeManager,
			schulform: api.schulform,
			mapKlassenVorigerAbschnitt: () => routeKlassen.data.mapKlassenVorigerAbschnitt,
			mapKlassenFolgenderAbschnitt: () => routeKlassen.data.mapKlassenFolgenderAbschnitt,
			add: routeKlassen.data.add,
			gotoEintrag: routeKlassen.data.gotoEintrag,
		};
	}

}

export const routeKlassenDatenNeu = new RouteKlassenDatenNeu();
