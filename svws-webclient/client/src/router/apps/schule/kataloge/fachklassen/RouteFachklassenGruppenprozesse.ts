import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import type { RouteFachklassen } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassen";
import { routeFachklassen } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassen";
import type { FachklassenGruppenprozesseProps } from "~/components/schule/kataloge/fachklassen/gruppenprozesse/FachklassenGruppenprozesseProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const FachklassenGruppenprozesse = () => import(
	"~/components/schule/kataloge/fachklassen/gruppenprozesse/FachklassenGruppenprozesse.vue");

class RouteFachklassenGruppenprozesse extends RouteNode<any, RouteFachklassen> {
	public constructor() {
		super([Schulform.BK, Schulform.SB],
			[BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.fachklassen.gruppenprozesse",
			"fachklassen", FachklassenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): FachklassenGruppenprozesseProps {
		return {
			manager: () => routeFachklassen.data.manager,
			delete: routeFachklassen.data.delete,
			deleteCheck: routeFachklassen.data.deleteCheck,
			gotoDefaultView: routeFachklassen.data.gotoDefaultView,
		};
	}
}

export const routeFachklassenGruppenprozesse = new RouteFachklassenGruppenprozesse();

