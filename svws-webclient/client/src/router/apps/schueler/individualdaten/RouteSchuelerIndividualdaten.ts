import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { SchuelerIndividualdatenProps } from "~/components/schueler/individualdaten/SchuelerIndividualdatenProps";
import { routeApp } from "~/router/apps/RouteApp";
import { type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteNode } from "~/router/RouteNode";

const SSchuelerIndividualdaten = () => import("~/components/schueler/individualdaten/SchuelerIndividualdaten.vue");


export class RouteSchuelerIndividualdaten extends RouteNode<any, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "schueler.daten", "daten", SSchuelerIndividualdaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
	}

	public getProps(to: RouteLocationNormalized): SchuelerIndividualdatenProps {
		return {
			zeigeAlles: true,
		};
	}

}

export const routeSchuelerIndividualdaten = new RouteSchuelerIndividualdaten();

