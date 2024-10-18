import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeKurse, type RouteKurse } from "~/router/apps/kurse/RouteKurse";

import type { KursDatenProps } from "~/components/kurse/daten/SKursDatenProps";
import { routeApp } from "../RouteApp";

const SKursDaten = () => import("~/components/kurse/daten/SKursDaten.vue");

export class RouteKursDaten extends RouteNode<any, RouteKurse> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN ], "kurse.daten", "daten", SKursDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurs";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): KursDatenProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeKurse.data.patch,
			kursListeManager: () => routeKurse.data.kursListeManager,
			setFilter: routeKurse.data.setFilter,
			gotoSchueler: routeKurse.data.gotoSchueler,
		};
	}

}

export const routeKursDaten = new RouteKursDaten();

