import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { EnmLerngruppenAuswahlListeManager } from "@ui/components/enm/EnmLerngruppenAuswahlListeManager";
import { useAbteilungenState } from "@ui/states/kataloge/AbteilungenState";
import { useNotenmodulState } from "@ui/states/NotenmodulState";
import { ConfigElement } from "@ui/utils/Config";

import type { RouteApp } from "../RouteApp";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteNode } from "~/router/RouteNode";
import { configStateImpl } from "~/states/ConfigStateImpl";

import { RouteDataNotenmodulAnkreuzkompetenzen } from "./RouteDataNotenmodulAnkreuzkompetenzen";
import { routeNotenmodulAnkreuzkompetenzenData } from "./RouteNotenmodulAnkreuzkompetenzenData";
import { routeNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";

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
		super.getAuswahlListProps = (props) => {
			const notenmodulState = useNotenmodulState();
			return {
				...props,
				enmManager: () => notenmodulState.manager,
				setAuswahlEinzel: notenmodulState.setAuswahlKlasse,
				auswahlEinzel: () => notenmodulState.auswahlKlasse,
				setAuswahlMehrfach: notenmodulState.setAuswahlKlassen,
				auswahlMehrfach: () => notenmodulState.auswahlKlassenNurMehrfachauswahl,
			};
		};
		super.getAuswahlProps = props => {
			const notenmodulState = useNotenmodulState();
			return {
				...props,
				enmManager: () => notenmodulState.manager,
			};
		};
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
		const notenmodulState = useNotenmodulState();
		if (notenmodulState.hideAnkreuzkompetenzen) {
			return routeNotenmodulLeistungen.getRoute();
		}
		return false;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering && !(from?.name.startsWith("notenmodul.") ?? false)) {
			const notenmodulState = useNotenmodulState();
			const abteilungenState = useAbteilungenState();
			await abteilungenState.init();
			await notenmodulState.ladeDaten();
		}
		if (to.name === this.name) {
			return routeNotenmodulAnkreuzkompetenzenData.getRoute();
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
		const notenmodulState = useNotenmodulState();
		notenmodulState.setAuswahlKlassen([]);
		if (!(to.name.startsWith("notenmodul"))) {
			notenmodulState.reset();
		}
		await super.leave(from, from_params, to, to_params);
	}

}

export const routeNotenmodulAnkreuzkompetenzen = new RouteNotenmodulAnkreuzkompetenzen();
