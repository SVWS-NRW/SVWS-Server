import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteVermerkarten } from "./RouteVermerkarten";
import type { VermerkartenGruppenprozesseProps } from "~/components/schule/kataloge/vermerkarten/gruppenprozesse/VermerkartenGruppenprozesseProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { routeVermerkarten } from "./RouteVermerkarten";

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
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): VermerkartenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeVermerkarten.data.manager,
			delete: routeVermerkarten.data.delete,
			deleteCheck: routeVermerkarten.data.deleteCheck,
		};
	}

}

export const routeVermerkartenGruppenprozesse = new RouteVermerkartenGruppenprozesse();

