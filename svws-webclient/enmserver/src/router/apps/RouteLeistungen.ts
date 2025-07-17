import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import type { EnmLeistungenProps } from "@ui/components/enm/EnmLeistungenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { EnmLeistungenAuswahlProps } from "@ui/components/enm/EnmLeistungenAuswahlProps";

const EnmLeistungenAuswahl = () => import("@ui/components/enm/EnmLeistungenAuswahl.vue")
const EnmLeistungen = () => import("@ui/components/enm/EnmLeistungen.vue")

export class RouteLeistungen extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "leistungen", "leistungen", EnmLeistungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Leistungsdaten";
		super.setView("liste", EnmLeistungenAuswahl, (route) => this.getAuswahlProps());
	}

	public getProps(): EnmLeistungenProps {
		return {
			enmManager: () => routeApp.data.manager,
			auswahl: () => routeApp.data.auswahlLerngruppen,
			patchLeistung: routeApp.data.patchLeistung,
			columnsVisible: () => routeApp.data.leistungenColumnsVisible,
			setColumnsVisible: routeApp.data.setLeistungenColumnsVisible,
			floskelEditorVisible: routeApp.data.floskelEditorVisible,
			setFloskelEditorVisible: routeApp.data.setFloskelEditorVisible,
		};
	}

	public getAuswahlProps(): EnmLeistungenAuswahlProps {
		return {
			enmManager: () => routeApp.data.manager,
			setAuswahlMehrfach: routeApp.data.setAuswahlLerngruppen,
			auswahlMehrfach: () => routeApp.data.auswahlLerngruppenNurMehrfachauswahl,
			setAuswahlEinzel: routeApp.data.setAuswahlLerngruppe,
			auswahlEinzel: () => routeApp.data.auswahlLerngruppe,
		};
	}

}

export const routeLeistungen = new RouteLeistungen();
