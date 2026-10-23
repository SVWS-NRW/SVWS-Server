import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { SchulinterneFunktionenDatenProps } from "~/components/schule/kataloge/schulinternefunktionen/daten/SchulinterneFunktionenDatenProps";
import { RouteNode } from "~/router/RouteNode";

import type { RouteSchulinterneFunktionen } from "./RouteSchulinterneFunktionen";
import { routeSchulinterneFunktionen } from "./RouteSchulinterneFunktionen";

const SchulinterneFunktionenDaten = () => import("~/components/schule/kataloge/schulinternefunktionen/daten/SchulinterneFunktionenDaten.vue");

export class RouteSchulinterneFunktionenDaten extends RouteNode<any, RouteSchulinterneFunktionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schulinternefunktionen.daten",
			"daten", SchulinterneFunktionenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulinterne Funktion";
	}

	public getProps(to: RouteLocationNormalized): SchulinterneFunktionenDatenProps {
		return {
			manager: () => routeSchulinterneFunktionen.data.manager,
			patch: routeSchulinterneFunktionen.data.patch,
		};
	}
}

export const routeSchulinterneFunktionenDaten = new RouteSchulinterneFunktionenDaten();
