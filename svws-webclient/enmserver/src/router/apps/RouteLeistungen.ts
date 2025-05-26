import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { api } from "../Api";
import type { EnmLeistungenProps } from "~/components/leistungen/EnmLeistungenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ConfigElement } from "~/components/Config";

const EnmLeistungenAuswahl = () => import("~/components/leistungen/EnmLeistungenAuswahl.vue")
const EnmLeistungen = () => import("~/components/leistungen/EnmLeistungen.vue")

export class RouteLeistungen extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "leistungen", "leistungen", EnmLeistungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Leistungsdaten";
		super.setView("liste", EnmLeistungenAuswahl, (route) => this.getProps());
		api.config.addElements([
			new ConfigElement("floskelEditorVisible", "user", 'true'),
			new ConfigElement("leistungen.table.columns", "user", JSON.stringify([
				["Klasse", null],
				["Name", null],
				["Fach", null],
				["Kurs", true],
				["Kursart", true],
				["Lehrer", true],
				["Quartal", true],
				["Note", null],
				["Mahnung", true],
				["FS", true],
				["FSU", true],
				["Bemerkung", true],
			])),
		]);
	}

	public getProps(): EnmLeistungenProps {
		return {
			manager: api.manager,
			auswahlmanager: routeApp.data.auswahlmanagerLeistungen,
			patchLeistung: routeApp.data.patchLeistung,
			columnsVisible: () => routeApp.data.leistungenColumnsVisible,
			setColumnsVisible: routeApp.data.setLeistungenColumnsVisible,
			floskelEditorVisible: routeApp.data.floskelEditorVisible,
			setFloskelEditorVisible: routeApp.data.setFloskelEditorVisible,
		};
	}
}

export const routeLeistungen = new RouteLeistungen();
