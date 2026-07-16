import type { RouteLocationNormalized, RouteParams } from "vue-router";
import type { NutzereinstellungenAppProps } from "~/components/benutzerprofil/einstellungen/SNutzereinstellungenAppProps";
import type { RouteApp } from "~/router/apps/RouteApp";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { RouteDataBenutzerprofilNutzereinstellungen } from "~/router/apps/benutzerprofil/nutzereinstellungen/RouteDataBenutzerprofilNutzereinstellungen";
import { RouteBenutzerprofilMenuGroup } from "~/router/apps/benutzerprofil/RouteBenutzerprofilMenuGroup";

const SBenutzerprofilApp = () => import("~/components/benutzerprofil/einstellungen/SNutzereinstellungenApp.vue");

export class RouteBenutzerprofilNutzereinstellungen extends RouteNode<RouteDataBenutzerprofilNutzereinstellungen, RouteApp> {

	public constructor() {
		super(
			Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"benutzerprofil.nutzereinstellungen",
			"benutzerprofil/nutzereinstellungen",
			SBenutzerprofilApp,
			new RouteDataBenutzerprofilNutzereinstellungen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Nutzereinstellungen";
		super.menugroup = RouteBenutzerprofilMenuGroup.EINSTELLUNGEN;
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public getProps(to: RouteLocationNormalized): NutzereinstellungenAppProps {
		return { };
	}

}

export const routeBenutzerprofilNutzereinstellungen = new RouteBenutzerprofilNutzereinstellungen();
