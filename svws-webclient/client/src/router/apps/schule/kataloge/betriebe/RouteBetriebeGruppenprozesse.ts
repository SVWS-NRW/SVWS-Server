import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import { routeBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import type { BetriebeGruppenprozesseProps } from "~/components/schule/kataloge/betriebe/gruppenprozesse/BetriebeGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

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
