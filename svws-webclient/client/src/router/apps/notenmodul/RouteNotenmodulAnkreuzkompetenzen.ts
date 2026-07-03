import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { routeNotenmodul } from "./RouteNotenmodul";
import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import type { EnmLerngruppenAuswahlListeManager } from "@ui";
import { ConfigElement } from "@ui";
import { api } from "~/router/Api";
import type { RouteApp } from "../RouteApp";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteLocationRaw, RouteParams } from "vue-router";
import type { NotenmodulAnkreuzkompetenzenAppProps } from "~/components/notenmodul/NotenmodulAnkreuzkompetenzenAppProps";
import type { NotenmodulAnkreuzkompetenzenAuswahlProps } from "~/components/notenmodul/NotenmodulAnkreuzkompetenzenAuswahlProps";
import { RouteDataNotenmodulAnkreuzkompetenzen } from "./RouteDataNotenmodulAnkreuzkompetenzen";
import { routeNotenmodulAnkreuzkompetenzenData } from "./RouteNotenmodulAnkreuzkompetenzenData";
import { routeNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import { configStateImpl } from "~/states/ConfigStateImpl";

const NotenmodulAnkreuzkompetenzenApp = () => import("~/components/notenmodul/NotenmodulAnkreuzkompetenzenApp.vue");
const NotenmodulAnkreuzkompetenzenAuswahl = () => import("~/components/notenmodul/NotenmodulAnkreuzkompetenzenAuswahl.vue");

export class RouteNotenmodulAnkreuzkompetenzen extends RouteAuswahlNode<EnmLerngruppenAuswahlListeManager, RouteDataNotenmodulAnkreuzkompetenzen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul.ankreuzkompetenzen", "notenmodul/ankreuzkompetenzen", NotenmodulAnkreuzkompetenzenApp, NotenmodulAnkreuzkompetenzenAuswahl, new RouteDataNotenmodulAnkreuzkompetenzen());
		super.mode = ServerMode.DEV;
		super.getAuswahlListProps = (props) => (<NotenmodulAnkreuzkompetenzenAuswahlProps>{
			...props,
			enmManager: () => routeNotenmodul.data.manager,
			setAuswahlEinzel: routeNotenmodul.data.setAuswahlKlasse,
			auswahlEinzel: () => routeNotenmodul.data.auswahlKlasse,
			setAuswahlMehrfach: routeNotenmodul.data.setAuswahlKlassen,
			auswahlMehrfach: () => routeNotenmodul.data.auswahlKlassenNurMehrfachauswahl,
		});
		super.getAuswahlProps = props => (<NotenmodulAnkreuzkompetenzenAppProps>{
			...props,
			enmManager: () => routeNotenmodul.data.manager,
		});
		super.text = "Ankreuzkompetenzen";
		this.isHidden = () => this.checkHidden();
		configStateImpl.config.addElements([
			new ConfigElement("notenmodul.ankreuzkompetenzen.table.columns", "user", "null"),
		]);
		super.children = [
			routeNotenmodulAnkreuzkompetenzenData,
		];
		super.defaultChild = routeNotenmodulAnkreuzkompetenzenData;
		super.menugroup = RouteNotenmodulMenuGroup.ALLGEMEIN;
	}

	protected checkHidden() {
		if (routeNotenmodul.data.hideAnkreuzkompetenzen) {
			return routeNotenmodulLeistungen.getRoute();
		}
		return false;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await routeNotenmodul.data.ladeDaten();
		}
		if (to.name === this.name) {
			return routeNotenmodulAnkreuzkompetenzenData.getRoute();
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
		routeNotenmodul.data.setAuswahlKlassen([]);
		if (!(to.name.startsWith("notenmodul"))) {
			await routeNotenmodul.data.entferneDaten();
		}
		await super.leave(from, from_params, to, to_params);
	}

}

export const routeNotenmodulAnkreuzkompetenzen = new RouteNotenmodulAnkreuzkompetenzen();
