import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { RouteBenutzerprofilMenuGroup } from "~/router/apps/benutzerprofil/RouteBenutzerprofilMenuGroup";
import { RouteDataBenutzerprofilWiedervorlagen } from "~/router/apps/benutzerprofil/wiedervorlagen/RouteDataBenutzerprofilWiedervorlagen";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { WiedervorlagenAppProps } from "~/components/benutzerprofil/wiedervorlagen/WiedervorlagenAppProps";
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
		// initialize used states
		await Promise.all([
			wiedervorlageStateImpl.init(),
			wiedervorlageStateImpl.ladeWiedervorlagen(),
		]);
	}

	public getProps(to: RouteLocationNormalized): WiedervorlagenAppProps {

		return {
			goToPerson: this.data.goToPerson,
		};
	}
}

export const routeBenutzerprofilWiedervorlagen = new RouteBenutzerprofilWiedervorlagen();
