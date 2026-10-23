import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { SchulinterneFunktionenGruppenprozesseProps } from "~/components/schule/kataloge/schulinternefunktionen/gruppenprozesse/SchulinterneFunktionenGruppenprozesseProps";
import type { RouteSchulinterneFunktionen } from "~/router/apps/schule/kataloge/schulinternefunktionen/RouteSchulinterneFunktionen";
import { routeSchulinterneFunktionen } from "~/router/apps/schule/kataloge/schulinternefunktionen/RouteSchulinterneFunktionen";
import { RouteNode } from "~/router/RouteNode";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

const SchulinterneFunktionenGruppenprozesse = () => import("~/components/schule/kataloge/schulinternefunktionen/gruppenprozesse/SchulinterneFunktionenGruppenprozesse.vue");

export class RouteSchulinterneFunktionenGruppenprozesse extends RouteNode<any, RouteSchulinterneFunktionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.schulinternefunktionen.gruppenprozesse", "gruppenprozesse", SchulinterneFunktionenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchulinterneFunktionenGruppenprozesseProps {
		return {
			delete: routeSchulinterneFunktionen.data.delete,
			deleteCheck: routeSchulinterneFunktionen.data.deleteCheck,
			manager: () => routeSchulinterneFunktionen.data.manager,
		};
	}
}

export const routeSchulinterneFunktionenGruppenprozesse = new RouteSchulinterneFunktionenGruppenprozesse();
