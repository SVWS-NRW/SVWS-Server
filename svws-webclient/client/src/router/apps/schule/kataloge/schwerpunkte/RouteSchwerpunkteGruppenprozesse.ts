import { RouteNode } from "~/router/RouteNode";
import { routeSchwerpunkte, type RouteSchwerpunkte } from "./RouteSchwerpunkte";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { SchwerpunkteGruppenprozesseProps } from "~/components/schule/kataloge/schwerpunkte/gruppenprozesse/SchwerpunkteGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const SchwerpunkteGruppenprozesse = () => import(
	"~/components/schule/kataloge/schwerpunkte/gruppenprozesse/SchwerpunkteGruppenprozesse.vue");

class RouteSchwerpunkteGruppenprozesse extends RouteNode<any, RouteSchwerpunkte> {
	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB, Schulform.R], [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.schwerpunkte.gruppenprozesse", "gruppenprozesse", SchwerpunkteGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchwerpunkteGruppenprozesseProps {
		return {
			manager: () => routeSchwerpunkte.data.manager,
			delete: routeSchwerpunkte.data.delete,
			deleteCheck: routeSchwerpunkte.data.deleteCheck,
			gotoDefaultView: routeSchwerpunkte.data.gotoDefaultView,
		};
	}
}

export const routeSchwerpunkteGruppenprozesse = new RouteSchwerpunkteGruppenprozesse();

