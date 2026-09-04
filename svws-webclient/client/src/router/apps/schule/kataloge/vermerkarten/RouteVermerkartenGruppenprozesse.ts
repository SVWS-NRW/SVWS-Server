import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteVermerkarten } from "./RouteVermerkarten";
import type { VermerkartenGruppenprozesseProps } from "~/components/schule/kataloge/vermerkarten/gruppenprozesse/VermerkartenGruppenprozesseProps";
import { RouteNode } from "~/router/RouteNode";
import { routeVermerkarten } from "./RouteVermerkarten";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const VermerkartenGruppenprozesse = () => import("~/components/schule/kataloge/vermerkarten/gruppenprozesse/VermerkartenGruppenprozesse.vue");

export class RouteVermerkartenGruppenprozesse extends RouteNode<any, RouteVermerkarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.vermerkarten.gruppenprozesse", "gruppenprozesse", VermerkartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): VermerkartenGruppenprozesseProps {
		return {
			manager: () => routeVermerkarten.data.manager,
			delete: routeVermerkarten.data.delete,
			deleteCheck: routeVermerkarten.data.deleteCheck,
		};
	}

}

export const routeVermerkartenGruppenprozesse = new RouteVermerkartenGruppenprozesse();

