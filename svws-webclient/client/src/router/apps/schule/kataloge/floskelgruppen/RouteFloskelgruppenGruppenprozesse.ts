import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { FloskelgruppenGruppenprozesseProps } from "~/components/schule/kataloge/floskelgruppen/gruppenprozesse/FloskelgruppenGruppenprozesseProps";
import type { RouteFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import { routeFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import { RouteNode } from "~/router/RouteNode";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

const FloskelgruppenGruppenprozesse = () => import(
	"~/components/schule/kataloge/floskelgruppen/gruppenprozesse/FloskelgruppenGruppenprozesse.vue");

export class RouteFloskelgruppenGruppenprozesse extends RouteNode<any, RouteFloskelgruppen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.floskelgruppen.gruppenprozesse", "gruppenprozesse", FloskelgruppenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): FloskelgruppenGruppenprozesseProps {
		return {
			delete: routeFloskelgruppen.data.delete,
			deleteCheck: routeFloskelgruppen.data.deleteCheck,
			manager: () => routeFloskelgruppen.data.manager,
		};
	}
}

export const routeFloskelgruppenGruppenprozesse = new RouteFloskelgruppenGruppenprozesse();
