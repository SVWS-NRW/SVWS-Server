import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { api } from "../Api";
import type { EnmLeistungenProps } from "~/components/leistungen/EnmLeistungenProps";

const EnmLeistungenAuswahl = () => import("~/components/leistungen/EnmLeistungenAuswahl.vue")
const EnmLeistungen = () => import("~/components/leistungen/EnmLeistungen.vue")

export class RouteLeistungen extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "leistungen", "leistungen", EnmLeistungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Leistungsdaten";
		super.setView("liste", EnmLeistungenAuswahl, (route) => this.getProps());
	}

	public getProps(): EnmLeistungenProps {
		return {
			manager: api.manager,
		};
	}
}

export const routeLeistungen = new RouteLeistungen();
