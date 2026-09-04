import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import { routeTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import type { TelefonartenGruppenprozesseProps } from "~/components/schule/kataloge/telefonarten/gruppenprozesse/TelefonartenGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const TelefonartenGruppenprozesse = () => import("~/components/schule/kataloge/telefonarten/gruppenprozesse/TelefonartenGruppenprozesse.vue");

export class RouteTelefonartenGruppenprozesse extends RouteNode<any, RouteTelefonarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.telefonarten.gruppenprozesse", "gruppenprozesse", TelefonartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): TelefonartenGruppenprozesseProps {
		return {
			manager: () => routeTelefonarten.data.manager,
			delete: routeTelefonarten.data.delete,
			deleteCheck: routeTelefonarten.data.deleteCheck,
			gotoDefaultView: routeTelefonarten.data.gotoDefaultView,
		};
	}
}

export const routeTelefonartenGruppenprozesse = new RouteTelefonartenGruppenprozesse();

