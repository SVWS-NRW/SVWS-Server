import type { EnmKlassenleitungProps } from "~/components/leistungen/EnmKlassenleitungProps";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { api } from "../Api";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ConfigElement } from "~/components/Config";

const EnmKlassenleitungAuswahl = () => import("~/components/leistungen/EnmKlassenleitungAuswahl.vue")
const EnmKlassenleitung = () => import("~/components/leistungen/EnmKlassenleitung.vue")

export class RouteKlassenleitung extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassenleitung", "klassenleitung", EnmKlassenleitung);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Klassenleitung";
		super.setView("liste", EnmKlassenleitungAuswahl, (route) => this.getProps());
		api.config.addElements([
			new ConfigElement("klassenleitung.table.columns", "user", JSON.stringify([
				["Klasse", null],
				["Name", null],
				["FS", null],
				["FSU", null],
				["ASV", true],
				["AUE", true],
				["ZB", true],
			])),
		]);
	}

	public getProps(): EnmKlassenleitungProps {
		return {
			manager: api.manager,
			patchBemerkungen: routeApp.data.patchBemerkungen,
			patchLernabschnitt: routeApp.data.patchLernabschnitt,
			columnsVisible: () => routeApp.data.klassenleitungColumnsVisible,
			setColumnsVisible: routeApp.data.setKlassenleitungColumnsVisible,
		};
	}
}

export const routeKlassenleitung = new RouteKlassenleitung();
