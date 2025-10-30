import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { routeNotenmodul } from "./RouteNotenmodul";
import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import type { EnmLerngruppenAuswahlListeManager } from "@ui";
import { ConfigElement } from "@ui";
import { api } from "~/router/Api";
import type { RouteApp } from "../RouteApp";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteDataNotenmodulLeistungen } from "./RouteDataNotenmodulLeistungen";
import type { NotenmodulLeistungenAppProps } from "~/components/notenmodul/NotenmodulLeistungenAppProps";
import type { NotenmodulLeistungenAuswahlProps } from "~/components/notenmodul/NotenmodulLeistungenAuswahlProps";
import { routeNotenmodulLeistungenData } from "./RouteNotenmodulLeistungenData";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteLocationRaw, RouteParams } from "vue-router";

const NotenmodulLeistungenApp = () => import("~/components/notenmodul/NotenmodulLeistungenApp.vue");
const NotenmodulLeistungenAuswahl = () => import("~/components/notenmodul/NotenmodulLeistungenAuswahl.vue");

export class RouteNotenmodulLeistungen extends RouteAuswahlNode<EnmLerngruppenAuswahlListeManager, RouteDataNotenmodulLeistungen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul.leistungen", "notenmodul/leistungen", NotenmodulLeistungenApp, NotenmodulLeistungenAuswahl, new RouteDataNotenmodulLeistungen());
		super.mode = ServerMode.ALPHA;
		super.getAuswahlListProps = (props) => (<NotenmodulLeistungenAuswahlProps>{
			...props,
			enmManager: () => routeNotenmodul.data.manager,
			setAuswahlEinzel: routeNotenmodul.data.setAuswahlLerngruppe,
			auswahlEinzel: () => routeNotenmodul.data.auswahlLerngruppe,
			setAuswahlMehrfach: routeNotenmodul.data.setAuswahlLerngruppen,
			auswahlMehrfach: () => routeNotenmodul.data.auswahlLerngruppenNurMehrfachauswahl,
		});
		super.getAuswahlProps = props => (<NotenmodulLeistungenAppProps>{
			...props,
			enmManager: () => routeNotenmodul.data.manager,
		});
		super.text = "Leistungsdaten";
		api.config.addElements([
			new ConfigElement("notenmodul.leistungen.floskelEditorVisible", "user", 'true'),
			new ConfigElement("notenmodul.leistungen.table.columns", "user", JSON.stringify([
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
		super.children = [
			routeNotenmodulLeistungenData,
		];
		super.defaultChild = routeNotenmodulLeistungenData;
		super.menugroup = RouteNotenmodulMenuGroup.ALLGEMEIN;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering && (!(from?.name.startsWith("notenmodul") ?? false)))
			await routeNotenmodul.data.ladeDaten();
		if (to.name === this.name)
			return routeNotenmodulLeistungenData.getRoute();
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
		if (!(to.name.startsWith("notenmodul")))
			await routeNotenmodul.data.entferneDaten();
		await super.leave(from, from_params, to, to_params);
	}

}

export const routeNotenmodulLeistungen = new RouteNotenmodulLeistungen();
