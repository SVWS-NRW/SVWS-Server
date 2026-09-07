import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { FoerderschwerpunkteGruppenprozesseProps } from "~/components/schule/kataloge/foerderschwerpunkte/gruppenprozesse/FoerderschwerpunkteGruppenprozesseProps";
import type { RouteFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { routeFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { RouteNode } from "~/router/RouteNode";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

const FoerderschwerpunkteGruppenprozesse = () => import(
	"~/components/schule/kataloge/foerderschwerpunkte/gruppenprozesse/FoerderschwerpunkteGruppenprozesse.vue");

export class RouteFoerderschwerpunkteGruppenprozesse extends RouteNode<any, RouteFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.foerderschwerpunkte.gruppenprozesse", "gruppenprozesse", FoerderschwerpunkteGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteGruppenprozesseProps {
		return {
			delete: routeFoerderschwerpunkte.data.delete,
			deleteCheck: routeFoerderschwerpunkte.data.deleteCheck,
			manager: () => routeFoerderschwerpunkte.data.manager,
		};
	}
}

export const routeFoerderschwerpunkteGruppenprozesse = new RouteFoerderschwerpunkteGruppenprozesse();
