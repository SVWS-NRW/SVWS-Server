import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import type { RouteApp } from "../RouteApp";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteLocationRaw, RouteParams } from "vue-router";
import { RouteDataNotenmodulAnkreuzkompetenzen } from "./RouteDataNotenmodulAnkreuzkompetenzen";
import { routeNotenmodulAnkreuzkompetenzenData } from "./RouteNotenmodulAnkreuzkompetenzenData";
import { routeNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { notenmodulStateImpl } from "~/states/NotenmodulStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { EnmLerngruppenAuswahlListeManager } from "@ui/components/enm/EnmLerngruppenAuswahlListeManager";
import { ConfigElement } from "@ui/utils/Config";

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
		super.getAuswahlListProps = (props) => ({
			...props,
			enmManager: () => notenmodulStateImpl.manager,
			setAuswahlEinzel: notenmodulStateImpl.setAuswahlKlasse,
			auswahlEinzel: () => notenmodulStateImpl.auswahlKlasse,
			setAuswahlMehrfach: notenmodulStateImpl.setAuswahlKlassen,
			auswahlMehrfach: () => notenmodulStateImpl.auswahlKlassenNurMehrfachauswahl,
		});
		super.getAuswahlProps = props => ({
			...props,
			enmManager: () => notenmodulStateImpl.manager,
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
		if (notenmodulStateImpl.hideAnkreuzkompetenzen) {
			return routeNotenmodulLeistungen.getRoute();
		}
		return false;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await notenmodulStateImpl.ladeDaten();
		}
		if (to.name === this.name) {
			return routeNotenmodulAnkreuzkompetenzenData.getRoute();
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
		notenmodulStateImpl.setAuswahlKlassen([]);
		if (!(to.name.startsWith("notenmodul"))) {
			notenmodulStateImpl.reset();
		}
		await super.leave(from, from_params, to, to_params);
	}

}

export const routeNotenmodulAnkreuzkompetenzen = new RouteNotenmodulAnkreuzkompetenzen();
