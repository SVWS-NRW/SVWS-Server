import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import type { RouteApp } from "../RouteApp";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteDataNotenmodulLeistungen } from "./RouteDataNotenmodulLeistungen";
import { routeNotenmodulLeistungenData } from "./RouteNotenmodulLeistungenData";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteLocationRaw, RouteParams } from "vue-router";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { notenmodulStateImpl } from "~/states/NotenmodulStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { EnmLerngruppenAuswahlListeManager } from "@ui/components/enm/EnmLerngruppenAuswahlListeManager";
import { ConfigElement } from "@ui/utils/Config";

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
		super.mode = ServerMode.STABLE;
		super.getAuswahlListProps = (props) => ({
			...props,
			enmManager: () => notenmodulStateImpl.manager,
			setAuswahlEinzel: notenmodulStateImpl.setAuswahlLerngruppe,
			auswahlEinzel: () => notenmodulStateImpl.auswahlLerngruppe,
			setAuswahlMehrfach: notenmodulStateImpl.setAuswahlLerngruppen,
			auswahlMehrfach: () => notenmodulStateImpl.auswahlLerngruppenNurMehrfachauswahl,
		});
		super.getAuswahlProps = props => ({
			...props,
			enmManager: () => notenmodulStateImpl.manager,
		});
		super.text = "Leistungsdaten";
		configStateImpl.config.addElements([
			new ConfigElement("notenmodul.leistungen.table.columns", "user", "null"),
		]);
		super.children = [
			routeNotenmodulLeistungenData,
		];
		super.defaultChild = routeNotenmodulLeistungenData;
		super.menugroup = RouteNotenmodulMenuGroup.ALLGEMEIN;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await notenmodulStateImpl.ladeDaten();
		}
		if (to.name === this.name) {
			return routeNotenmodulLeistungenData.getRoute();
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
		if (!(to.name.startsWith("notenmodul"))) {
			notenmodulStateImpl.reset();
		}
		await super.leave(from, from_params, to, to_params);
	}

}

export const routeNotenmodulLeistungen = new RouteNotenmodulLeistungen();
