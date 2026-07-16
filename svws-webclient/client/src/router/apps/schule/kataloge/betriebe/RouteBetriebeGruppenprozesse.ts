import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import type { RouteBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import { routeBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import type { BetriebeGruppenprozesseProps } from "~/components/schule/kataloge/betriebe/gruppenprozesse/BetriebeGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

const BetriebeGruppenprozesse = () => import("~/components/schule/kataloge/betriebe/gruppenprozesse/BetriebeGruppenprozesse.vue");

export class RouteBetriebeGruppenprozesse extends RouteNode<any, RouteBetriebe> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.betriebe.gruppenprozesse", "gruppenprozesse", BetriebeGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(_: RouteLocationNormalized): BetriebeGruppenprozesseProps {
		return {
			manager: () => routeBetriebe.data.manager,
			delete: routeBetriebe.data.delete,
			deleteCheck: routeBetriebe.data.deleteCheck,
		};
	}

}

export const routeBetriebeGruppenprozesse = new RouteBetriebeGruppenprozesse();
