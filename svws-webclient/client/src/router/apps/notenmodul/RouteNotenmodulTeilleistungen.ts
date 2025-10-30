import type { RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { ConfigElement } from "@ui";
import type { EnmLerngruppenAuswahlListeManager } from "@ui";

import { routeNotenmodul } from "./RouteNotenmodul";
import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import { RouteDataNotenmodulTeilleistungen } from "./RouteDataNotenmodulTeilleistungen";
import { api } from "~/router/Api";
import type { RouteApp } from "../RouteApp";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { NotenmodulTeilleistungenAuswahlProps } from "~/components/notenmodul/NotenmodulTeilleistungenAuswahlProps";
import type { NotenmodulTeilleistungenAppProps } from "~/components/notenmodul/NotenmodulTeilleistungenAppProps";
import { routeNotenmodulTeilleistungenData } from "./RouteNotenmodulTeilleistungenData";
import type { RouteNode } from "~/router/RouteNode";

const NotenmodulTeilleistungenApp = () => import("~/components/notenmodul/NotenmodulTeilleistungenApp.vue");
const NotenmodulTeilleistungenAuswahl = () => import("~/components/notenmodul/NotenmodulTeilleistungenAuswahl.vue");

export class RouteNotenmodulTeilleistungen extends RouteAuswahlNode<EnmLerngruppenAuswahlListeManager, RouteDataNotenmodulTeilleistungen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul.teilleistungen", "notenmodul/teilleistungen", NotenmodulTeilleistungenApp, NotenmodulTeilleistungenAuswahl, new RouteDataNotenmodulTeilleistungen());
		super.mode = ServerMode.ALPHA;
		super.getAuswahlListProps = (props) => (<NotenmodulTeilleistungenAuswahlProps>{
			...props,
			enmManager: () => routeNotenmodul.data.manager,
			setAuswahlEinzel: routeNotenmodul.data.setAuswahlLerngruppe,
			auswahlEinzel: () => routeNotenmodul.data.auswahlLerngruppe,
			setAuswahlMehrfach: routeNotenmodul.data.setAuswahlLerngruppen,
			auswahlMehrfach: () => routeNotenmodul.data.auswahlLerngruppenNurMehrfachauswahl,
		});
		super.getAuswahlProps = props => (<NotenmodulTeilleistungenAppProps>{
			...props,
			enmManager: () => routeNotenmodul.data.manager,
		});
		super.text = "Teilleistungen";
		api.config.addElements([
			new ConfigElement("notenmodul.teilleistungen.table.columns", "user", JSON.stringify([
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
		super.children = [
			routeNotenmodulTeilleistungenData,
		];
		super.defaultChild = routeNotenmodulTeilleistungenData;
		super.menugroup = RouteNotenmodulMenuGroup.ALLGEMEIN;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering && (!(from?.name.startsWith("notenmodul") ?? false)))
			await routeNotenmodul.data.ladeDaten();
		if (to.name === this.name)
			return routeNotenmodulTeilleistungenData.getRoute();
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
		if (!(to.name.startsWith("notenmodul")))
			await routeNotenmodul.data.entferneDaten();
		await super.leave(from, from_params, to, to_params);
	}

}

export const routeNotenmodulTeilleistungen = new RouteNotenmodulTeilleistungen();
