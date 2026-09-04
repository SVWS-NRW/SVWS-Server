import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { TeilleistungsartenGruppenprozesseProps } from "~/components/schule/kataloge/teilleistungsarten/gruppenprozesse/TeilleistungsartenGruppenprozesseProps";
import { routeTeilleistungsarten, type RouteTeilleistungsarten } from "./RouteTeilleistungsarten";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const TeilleistungsartenGruppenprozesse = () => import(
	"~/components/schule/kataloge/teilleistungsarten/gruppenprozesse/TeilleistungsartenGruppenprozesse.vue");

class RouteTeilleistungsartenGruppenprozesse extends RouteNode<any, RouteTeilleistungsarten> {
	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.teilleistungsarten.gruppenprozesse",
			"gruppenprozesse",
			TeilleistungsartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): TeilleistungsartenGruppenprozesseProps {
		return {
			manager: () => routeTeilleistungsarten.data.manager,
			delete: routeTeilleistungsarten.data.delete,
			deleteCheck: routeTeilleistungsarten.data.deleteCheck,
			gotoDefaultView: routeTeilleistungsarten.data.gotoDefaultView,
		};
	}
}

export const routeTeilleistungsartenGruppenprozesse = new RouteTeilleistungsartenGruppenprozesse();

