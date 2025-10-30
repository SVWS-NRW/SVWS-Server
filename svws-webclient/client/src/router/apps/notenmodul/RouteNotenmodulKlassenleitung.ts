import type { RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { routeNotenmodul } from "./RouteNotenmodul";
import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import type { EnmKlassenleitungAuswahlListeManager } from "@ui";
import { RouteDataNotenmodulKlassenleitung } from "./RouteDataNotenmodulKlassenleitung";
import { ConfigElement } from "@ui";
import { api } from "~/router/Api";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "../RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulKlassenleitungData } from "./RouteNotenmodulKlassenleitungData";
import type { NotenmodulKlassenleitungAuswahlProps } from "~/components/notenmodul/NotenmodulKlassenleitungAuswahlProps";
import type { NotenmodulKlassenleitungAppProps } from "~/components/notenmodul/NotenmodulKlassenleitungAppProps";

const NotenmodulKlassenleitungApp = () => import("~/components/notenmodul/NotenmodulKlassenleitungApp.vue");
const NotenmodulKlassenleitungAuswahl = () => import("~/components/notenmodul/NotenmodulKlassenleitungAuswahl.vue");

export class RouteNotenmodulKlassenleitung extends RouteAuswahlNode<EnmKlassenleitungAuswahlListeManager, RouteDataNotenmodulKlassenleitung, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul.klassenleitung", "klassenleitung", NotenmodulKlassenleitungApp, NotenmodulKlassenleitungAuswahl, new RouteDataNotenmodulKlassenleitung());
		super.mode = ServerMode.ALPHA;
		super.getAuswahlListProps = (props) => (<NotenmodulKlassenleitungAuswahlProps>{
			...props,
			enmManager: () => routeNotenmodul.data.manager,
			setAuswahlEinzel: routeNotenmodul.data.setAuswahlKlasse,
			auswahlEinzel: () => routeNotenmodul.data.auswahlKlasse,
			setAuswahlMehrfach: routeNotenmodul.data.setAuswahlKlassen,
			auswahlMehrfach: () => routeNotenmodul.data.auswahlKlassenNurMehrfachauswahl,
		});
		super.getAuswahlProps = props => (<NotenmodulKlassenleitungAppProps>{
			...props,
			enmManager: () => routeNotenmodul.data.manager,
		});
		super.text = "Klassenleitung";
		// TODO this.isHidden = () => routeNotenmodul.data.manager.listKlassenKlassenlehrer.isEmpty() ? routeNotenmodul.getRouteDefaultChild() : false;
		api.config.addElements([
			new ConfigElement("notenmodul.klassenleitung.floskelEditorVisible", "user", 'true'),
			new ConfigElement("notenmodul.klassenleitung.table.columns", "user", JSON.stringify([
				["Klasse", null],
				["Name", null],
				["FS", null],
				["FSU", null],
				["ASV", true],
				["AUE", true],
				["ZB", true],
			])),
		]);
		super.children = [
			routeNotenmodulKlassenleitungData,
		];
		super.defaultChild = routeNotenmodulKlassenleitungData;
		super.menugroup = RouteNotenmodulMenuGroup.ALLGEMEIN;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering && (!(from?.name.startsWith("notenmodul") ?? false)))
			await routeNotenmodul.data.ladeDaten();
		if (to.name === this.name)
			return routeNotenmodulKlassenleitungData.getRoute();
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
		if (!(to.name.startsWith("notenmodul")))
			await routeNotenmodul.data.entferneDaten();
		await super.leave(from, from_params, to, to_params);
	}

}

export const routeNotenmodulKlassenleitung = new RouteNotenmodulKlassenleitung();
