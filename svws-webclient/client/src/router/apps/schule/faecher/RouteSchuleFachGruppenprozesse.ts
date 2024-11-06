import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../RouteApp";
import { ViewType } from "@ui";
import { routeSchuleFaecher, type RouteSchuleFaecher } from "./RouteSchuleFaecher";
import type { SchuleFachGruppenprozesseProps } from "~/components/schule/faecher/gruppenprozesse/SSchuleFachGruppenprozesseProps";

const SSchuleFachGruppenprozesse = () => import("~/components/schule/faecher/gruppenprozesse/SSchuleFachGruppenprozesse.vue");

export class RouteSchuleFachGruppenprozesse extends RouteNode<any, RouteSchuleFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.faecher.gruppenprozesse", "gruppenprozesse", SSchuleFachGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): SchuleFachGruppenprozesseProps {
		return {
			mode: api.mode,
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			fachListeManager: () => routeSchuleFaecher.data.fachListeManager,
			deleteFaecher: routeSchuleFaecher.data.deleteFaecher,
			deleteFaecherCheck: routeSchuleFaecher.data.deleteFaecherCheck,
		};
	}

}

export const routeSchuleFachGruppenprozesse = new RouteSchuleFachGruppenprozesse();

