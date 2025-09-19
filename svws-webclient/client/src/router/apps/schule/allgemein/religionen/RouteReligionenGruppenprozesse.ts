import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { routeReligionen } from "~/router/apps/schule/allgemein/religionen/RouteReligionen";
import type { RouteReligionen } from "~/router/apps/schule/allgemein/religionen/RouteReligionen";
import { ReligionenGruppenprozesseProps } from "~/components/schule/allgemein/religionen/gruppenprozesse/SReligionenGruppenprozesseProps";

const SReligionenGruppenprozesse = () => import("~/components/schule/allgemein/religionen/gruppenprozesse/SReligionenGruppenprozesse.vue");

export class RouteReligionenGruppenprozesse extends RouteNode<any, RouteReligionen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ], "schule.religionen.gruppenprozesse", "gruppenprozesse", SReligionenGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): ReligionenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			delete: routeReligionen.data.delete,
			checkBeforeDelete: routeReligionen.data.checkBeforeDelete,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeReligionenGruppenprozesse = new RouteReligionenGruppenprozesse();

