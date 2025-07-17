import type { EnmKlassenleitungProps } from "@ui/components/enm/EnmKlassenleitungProps";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { EnmKlassenleitungAuswahlProps } from "@ui/components/enm/EnmKlassenleitungAuswahlProps";

const EnmKlassenleitungAuswahl = () => import("@ui/components/enm/EnmKlassenleitungAuswahl.vue")
const EnmKlassenleitung = () => import("@ui/components/enm/EnmKlassenleitung.vue")

export class RouteKlassenleitung extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassenleitung", "klassenleitung", EnmKlassenleitung);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Klassenleitung";
		super.setView("liste", EnmKlassenleitungAuswahl, (route) => this.getAuswahlProps());
		this.isHidden = () => routeApp.data.manager.listKlassenKlassenlehrer.isEmpty() ? routeApp.getRouteDefaultChild() : false;
	}

	public getProps(): EnmKlassenleitungProps {
		return {
			enmManager: () => routeApp.data.manager,
			auswahl: () => routeApp.data.auswahlKlassen,
			patchBemerkungen: routeApp.data.patchBemerkungen,
			patchLernabschnitt: routeApp.data.patchLernabschnitt,
			columnsVisible: () => routeApp.data.klassenleitungColumnsVisible,
			setColumnsVisible: routeApp.data.setKlassenleitungColumnsVisible,
			floskelEditorVisible: routeApp.data.floskelEditorVisible,
			setFloskelEditorVisible: routeApp.data.setFloskelEditorVisible,
		};
	}

	public getAuswahlProps(): EnmKlassenleitungAuswahlProps {
		return {
			enmManager: () => routeApp.data.manager,
			setAuswahlMehrfach: routeApp.data.setAuswahlKlassen,
			auswahlMehrfach: () => routeApp.data.auswahlKlassenNurMehrfachauswahl,
			setAuswahlEinzel: routeApp.data.setAuswahlKlasse,
			auswahlEinzel: () => routeApp.data.auswahlKlasse,
		};
	}

}

export const routeKlassenleitung = new RouteKlassenleitung();
