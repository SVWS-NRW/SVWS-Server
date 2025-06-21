import type { EnmKlassenleitungProps } from "@ui/components/enm/EnmKlassenleitungProps";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { api } from "../Api";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ConfigElement } from "~/components/Config";

const EnmKlassenleitungAuswahl = () => import("@ui/components/enm/EnmKlassenleitungAuswahl.vue")
const EnmKlassenleitung = () => import("@ui/components/enm/EnmKlassenleitung.vue")

export class RouteKlassenleitung extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassenleitung", "klassenleitung", EnmKlassenleitung);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Klassenleitung";
		super.setView("liste", EnmKlassenleitungAuswahl, (route) => this.getProps());
		this.isHidden = () => api.manager.listKlassenKlassenlehrer.isEmpty() ? routeApp.getRouteDefaultChild() : false;
		api.config.addElements([
			new ConfigElement("floskelEditorVisible", "user", 'true'),
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
			auswahlmanager: routeApp.data.auswahlmanagerKlassenleitung,
			patchBemerkungen: routeApp.data.patchBemerkungen,
			patchLernabschnitt: routeApp.data.patchLernabschnitt,
			columnsVisible: () => routeApp.data.klassenleitungColumnsVisible,
			setColumnsVisible: routeApp.data.setKlassenleitungColumnsVisible,
			floskelEditorVisible: routeApp.data.floskelEditorVisible,
			setFloskelEditorVisible: routeApp.data.setFloskelEditorVisible,
		};
	}
}

export const routeKlassenleitung = new RouteKlassenleitung();
