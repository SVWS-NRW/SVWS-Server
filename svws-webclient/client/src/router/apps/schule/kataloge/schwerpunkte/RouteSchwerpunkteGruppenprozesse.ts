import { RouteNode } from "~/router/RouteNode";
import { routeSchwerpunkte, type RouteSchwerpunkte } from "./RouteSchwerpunkte";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { ViewType } from "@ui";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { SchwerpunkteGruppenprozesseProps } from "~/components/schule/kataloge/schwerpunkte/gruppenprozesse/SchwerpunkteGruppenprozesseProps";
import { api } from "~/router/Api";
import { abschnittState } from "~/states/AbschnittStateImpl";

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
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittState.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchwerpunkteGruppenprozesseProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeSchwerpunkte.data.manager,
			delete: routeSchwerpunkte.data.delete,
			deleteCheck: routeSchwerpunkte.data.deleteCheck,
			gotoDefaultView: routeSchwerpunkte.data.gotoDefaultView,
		};
	}
}

export const routeSchwerpunkteGruppenprozesse = new RouteSchwerpunkteGruppenprozesse();

