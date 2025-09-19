import type { RouteLocationNormalized } from "vue-router";
import type { MerkmaleDatenProps } from "~/components/schule/schulbezogen/merkmale/daten/SMerkmaleDatenProps";
import type { RouteMerkmale } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmale";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeMerkmale } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmale";

const SMerkmaleDaten = () => import("~/components/schule/schulbezogen/merkmale/daten/SMerkmaleDaten.vue")

export class RouteMerkmaleDaten extends RouteNode<any, RouteMerkmale> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.merkmale.daten",
			"daten", SMerkmaleDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Merkmale"
	}

	public getProps(to: RouteLocationNormalized): MerkmaleDatenProps {
		return {
			manager: () => routeMerkmale.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeMerkmale.data.patch,
		}
	}
}

export const routeMerkmaleDaten = new RouteMerkmaleDaten();
