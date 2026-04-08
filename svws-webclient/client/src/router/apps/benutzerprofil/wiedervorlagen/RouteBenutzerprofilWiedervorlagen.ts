// Note: Minimal Placeholder Route for Wiedervorlage Implementation
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { type RouteApp } from "~/router/apps/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { RouteBenutzerprofilMenuGroup } from "~/router/apps/benutzerprofil/RouteBenutzerprofilMenuGroup";

export class RouteBenutzerprofilWiedervorlagen extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"benutzerprofil.wiedervorlagen",
			"benutzerprofil/wiedervorlagen");
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Wiedervorlagen";
		super.menugroup = RouteBenutzerprofilMenuGroup.AUFGABEN;
	}
}

export const routeBenutzerprofilWiedervorlagen = new RouteBenutzerprofilWiedervorlagen();
