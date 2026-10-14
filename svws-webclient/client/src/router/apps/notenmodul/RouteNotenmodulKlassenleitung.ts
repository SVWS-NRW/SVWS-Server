import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { EnmKlassenleitungAuswahlListeManager } from "@ui/components/enm/EnmKlassenleitungAuswahlListeManager";
import { useAbteilungenState } from "@ui/states/kataloge/AbteilungenState";
import { useNotenmodulState } from "@ui/states/NotenmodulState";
import { ConfigElement } from "@ui/utils/Config";

import type { RouteApp } from "../RouteApp";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteNode } from "~/router/RouteNode";
import { configStateImpl } from "~/states/ConfigStateImpl";

import { RouteDataNotenmodulKlassenleitung } from "./RouteDataNotenmodulKlassenleitung";
import { routeNotenmodulKlassenleitungData } from "./RouteNotenmodulKlassenleitungData";
import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";

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
		super.mode = ServerMode.STABLE;
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
		super.text = "Klassenleitung";
		// TODO this.isHidden = () => notenmodulStateImpl.manager.listKlassenKlassenlehrer.isEmpty() ? routeNotenmodul.getRouteDefaultChild() : false;
		configStateImpl.config.addElements([
			new ConfigElement("notenmodul.klassenleitung.table.columns", "user", "null"),
		]);
		super.children = [
			routeNotenmodulKlassenleitungData,
		];
		super.defaultChild = routeNotenmodulKlassenleitungData;
		super.menugroup = RouteNotenmodulMenuGroup.ALLGEMEIN;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering && !(from?.name.startsWith("notenmodul.") ?? false)) {
			const notenmodulState = useNotenmodulState();
			const abteilungenState = useAbteilungenState();
			await abteilungenState.init();
			await notenmodulState.ladeDaten();
		}
		if (to.name === this.name) {
			return routeNotenmodulKlassenleitungData.getRoute();
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		const notenmodulState = useNotenmodulState();
		if (notenmodulState.manager.listKlassenMitAnkreuzkompetenzen.size() > 0) {
			notenmodulState.setAuswahlKlassen([]);
		}
		await this.data.entferneDaten();
		if (!(to.name.startsWith("notenmodul"))) {
			notenmodulState.reset();
		}
		await super.leave(from, from_params, to, to_params);
	}

}

export const routeNotenmodulKlassenleitung = new RouteNotenmodulKlassenleitung();
