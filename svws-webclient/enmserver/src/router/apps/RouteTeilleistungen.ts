import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { api } from "../Api";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ConfigElement } from "~/components/Config";
import type { EnmTeilleistungenProps } from "@ui/components/enm/EnmTeilleistungenProps";

const EnmTeilleistungenAuswahl = () => import("@ui/components/enm/EnmTeilleistungenAuswahl.vue")
const EnmTeilleistungen = () => import("@ui/components/enm/EnmTeilleistungen.vue")

export class RouteTeilleistungen extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "teilleistungen", "teilleistungen", EnmTeilleistungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Teilleistungen";
		super.setView("liste", EnmTeilleistungenAuswahl, (route) => this.getProps());
		api.config.addElements([
			new ConfigElement("teilleistungen.table.columns", "user", JSON.stringify([
				["Klasse", null],
				["Name", null],
				["Fach", null],
				["Kurs", true],
				["Kursart", true],
				["Lehrer", true],
				["Teilleistung", null],
				["Quartal", true],
				["Note", null],
			])),
		]);
	}

	public getProps(): EnmTeilleistungenProps {
		return {
			manager: api.manager,
			auswahlmanager: routeApp.data.auswahlmanagerTeilleistungen,
			patchLeistung: routeApp.data.patchLeistung,
			patchTeilleistung: routeApp.data.patchTeilleistung,
			columnsVisible: () => routeApp.data.teilleistungenColumnsVisible,
			setColumnsVisible: routeApp.data.setTeilleistungenColumnsVisible,
		};
	}
}

export const routeTeilleistungen = new RouteTeilleistungen();
