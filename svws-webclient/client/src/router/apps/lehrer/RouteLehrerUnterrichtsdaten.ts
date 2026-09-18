import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { type RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { RouteNode } from "~/router/RouteNode";

const LehrerUnterrichtsdaten = () => import("~/components/lehrer/unterrichtsdaten/LehrerUnterrichtsdaten.vue");

export class RouteLehrerUnterrichtsdaten extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRERDATEN_ANSEHEN], "lehrer.unterrichtsdaten", "unterrichtsdaten", LehrerUnterrichtsdaten);
		super.mode = ServerMode.DEV;
		super.text = "Unterricht";
	}

}

export const routeLehrerUnterrichtsdaten = new RouteLehrerUnterrichtsdaten();
