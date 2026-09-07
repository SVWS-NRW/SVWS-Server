import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { WiedervorlagenAppProps } from "~/components/benutzerprofil/wiedervorlagen/WiedervorlagenAppProps";
import { RouteBenutzerprofilMenuGroup } from "~/router/apps/benutzerprofil/RouteBenutzerprofilMenuGroup";
import { RouteDataBenutzerprofilWiedervorlagen } from "~/router/apps/benutzerprofil/wiedervorlagen/RouteDataBenutzerprofilWiedervorlagen";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { wiedervorlageStateImpl } from "~/states/wiedervorlage/WiedervorlageStateImpl";

const App = () => import("~/components/benutzerprofil/wiedervorlagen/WiedervorlagenApp.vue");

export class RouteBenutzerprofilWiedervorlagen extends RouteNode<RouteDataBenutzerprofilWiedervorlagen, RouteApp> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"benutzerprofil.wiedervorlagen",
			"benutzerprofil/wiedervorlagen",
			App,
			new RouteDataBenutzerprofilWiedervorlagen()
		);

		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Wiedervorlagen";
		super.menugroup = RouteBenutzerprofilMenuGroup.AUFGABEN;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		// initialize states, load data etc
		await Promise.all([
			wiedervorlageStateImpl.updateWiedervorlagen(),
		]);
	}

	public getProps(to: RouteLocationNormalized): WiedervorlagenAppProps {

		return {
			goToPerson: this.data.goToPerson,
		};
	}
}

export const routeBenutzerprofilWiedervorlagen = new RouteBenutzerprofilWiedervorlagen();
