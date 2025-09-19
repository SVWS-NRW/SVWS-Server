import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { routeSchuleFaecher, type RouteSchuleFaecher } from "./RouteSchuleFaecher";
import type { SchuleFachGruppenprozesseProps } from "~/components/schule/schulbezogen/faecher/gruppenprozesse/SSchuleFachGruppenprozesseProps";

const SSchuleFachGruppenprozesse = () => import("~/components/schule/schulbezogen/faecher/gruppenprozesse/SSchuleFachGruppenprozesse.vue");

export class RouteSchuleFachGruppenprozesse extends RouteNode<any, RouteSchuleFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ], "schule.faecher.gruppenprozesse", "gruppenprozesse", SSchuleFachGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	protected async update() : Promise<void | Error | RouteLocationRaw> {
		await routeSchuleFaecher.data.updateMapStundenplaene();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): SchuleFachGruppenprozesseProps {
		return {
			apiStatus: api.status,
			getPDF: routeSchuleFaecher.data.getPDF,
			mapStundenplaene: routeSchuleFaecher.data.mapStundenplaene,
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schulgliederungen: api.schulgliederungen,
			manager: () => routeSchuleFaecher.data.manager,
			deleteFaecher: routeSchuleFaecher.data.delete,
			deleteFaecherCheck: routeSchuleFaecher.data.deleteFaecherCheck,
		};
	}

}

export const routeSchuleFachGruppenprozesse = new RouteSchuleFachGruppenprozesse();

