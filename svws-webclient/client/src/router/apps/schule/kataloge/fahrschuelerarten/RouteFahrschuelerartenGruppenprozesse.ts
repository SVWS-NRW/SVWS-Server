import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { FahrschuelerartenGruppenprozesseProps } from "~/components/schule/kataloge/fahrschuelerarten/gruppenprozesse/FahrschuelerartenGruppenprozesseProps";
import type { RouteFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";
import { RouteNode } from "~/router/RouteNode";
import { routeFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const FahrschuelerartenGruppenprozesse = () => import("~/components/schule/kataloge/fahrschuelerarten/gruppenprozesse/FahrschuelerartenGruppenprozesse.vue");

export class RouteFahrschuelerartenGruppenprozesse extends RouteNode<any, RouteFahrschuelerarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.fahrschuelerarten.gruppenprozesse", "gruppenprozesse", FahrschuelerartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): FahrschuelerartenGruppenprozesseProps {
		return {
			deleteCheck: routeFahrschuelerarten.data.deleteCheck,
			delete: routeFahrschuelerarten.data.delete,
			manager: () => routeFahrschuelerarten.data.manager,
			gotoDefaultView: routeFahrschuelerarten.data.gotoDefaultView,
		};
	}
}

export const routeFahrschuelerartenGruppenprozesse = new RouteFahrschuelerartenGruppenprozesse();
