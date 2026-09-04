import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import type { FoerderschwerpunkteGruppenprozesseProps } from "~/components/schule/kataloge/foerderschwerpunkte/gruppenprozesse/FoerderschwerpunkteGruppenprozesseProps";
import { RouteNode } from "~/router/RouteNode";
import { routeFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

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
