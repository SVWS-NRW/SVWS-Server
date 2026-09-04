import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import { routeErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import type { ErzieherartenDatenProps } from "~/components/schule/kataloge/erzieherarten/daten/ErzieherartenDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const ErzieherartenDaten = () => import("~/components/schule/kataloge/erzieherarten/daten/ErzieherartenDaten.vue");

export class RouteErzieherartenDaten extends RouteNode<any, RouteErzieherarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.erzieherarten.daten", "daten", ErzieherartenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erzieherarten";
	}

	public getProps(to: RouteLocationNormalized): ErzieherartenDatenProps {
		return {
			patch: routeErzieherarten.data.patch,
			manager: () => routeErzieherarten.data.manager,
		};
	}
}

export const routeErzieherartenDaten = new RouteErzieherartenDaten();
