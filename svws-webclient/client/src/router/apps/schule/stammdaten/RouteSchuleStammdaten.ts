import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { RouteSchule } from "../RouteSchule";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import type { SchuleStammdatenProps } from "~/components/schule/stammdaten/SchuleStammdatenProps";
import { routeSchuleAdressdaten } from "~/router/apps/schule/stammdaten/adressdaten/RouteSchuleAdressdaten";
import { routeSchuleLogoverwaltung } from "~/router/apps/schule/stammdaten/logoverwaltung/RouteSchuleLogoverwaltung";
import { routeSchuleMerkmale } from "~/router/apps/schule/stammdaten/merkmale/RouteSchuleMerkmale";
import { RouteDataSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteDataSchuleStammdaten";
import { routeSchuleSchuljahreswechsel } from "~/router/apps/schule/stammdaten/schuljahreswechsel/RouteSchuleSchuljahreswechsel";
import { RouteTabNode } from "~/router/RouteTabNode";

const SchuleStammdaten = () => import("~/components/schule/stammdaten/SchuleStammdaten.vue");

export class RouteSchuleStammdaten extends RouteTabNode<RouteDataSchuleStammdaten, RouteSchule> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"schule.stammdaten",
			"schule/stammdaten",
			SchuleStammdaten,
			new RouteDataSchuleStammdaten()
		);
		super.mode = ServerMode.STABLE;
		super.text = "Stammdaten der Schule";
		super.getProps = (props) => (<SchuleStammdatenProps>{
			...props,
		});
		super.children = [
			routeSchuleAdressdaten,
			routeSchuleMerkmale,
			routeSchuleLogoverwaltung,
			routeSchuleSchuljahreswechsel,
		];
		super.defaultChild = routeSchuleAdressdaten;
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
	}

}

export const routeSchuleStammdaten = new RouteSchuleStammdaten();
