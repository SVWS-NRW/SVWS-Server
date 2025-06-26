import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { EnmTeilleistungenProps } from "@ui/components/enm/EnmTeilleistungenProps";
import type { EnmTeilleistungenAuswahlProps } from "@ui/components/enm/EnmTeilleistungenAuswahlProps";

const EnmTeilleistungenAuswahl = () => import("@ui/components/enm/EnmTeilleistungenAuswahl.vue")
const EnmTeilleistungen = () => import("@ui/components/enm/EnmTeilleistungen.vue")

export class RouteTeilleistungen extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "teilleistungen", "teilleistungen", EnmTeilleistungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Teilleistungen";
		super.setView("liste", EnmTeilleistungenAuswahl, (route) => this.getAuswahlProps());
	}

	public getProps(): EnmTeilleistungenProps {
		return {
			enmManager: () => routeApp.data.manager,
			auswahl: () => routeApp.data.auswahlLerngruppen,
			patchLeistung: routeApp.data.patchLeistung,
			patchTeilleistung: routeApp.data.patchTeilleistung,
			columnsVisible: () => routeApp.data.teilleistungenColumnsVisible,
			setColumnsVisible: routeApp.data.setTeilleistungenColumnsVisible,
		};
	}

	public getAuswahlProps(): EnmTeilleistungenAuswahlProps {
		return {
			enmManager: () => routeApp.data.manager,
			setAuswahlMehrfach: routeApp.data.setAuswahlLerngruppen,
			auswahlMehrfach: () => routeApp.data.auswahlLerngruppenNurMehrfachauswahl,
			setAuswahlEinzel: routeApp.data.setAuswahlLerngruppe,
			auswahlEinzel: () => routeApp.data.auswahlLerngruppe,
		};
	}

}

export const routeTeilleistungen = new RouteTeilleistungen();
