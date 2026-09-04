import { type RouteApp } from "~/router/apps/RouteApp";
import { RouteDataKurse } from "~/router/apps/kurse/RouteDataKurse";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeKurseGruppenprozesse } from "./RouteKurseGruppenprozesse";
import { routeKurseNeu } from "./RouteKurseNeu";
import type { KursListeManager } from "~/states/kurse/KursListeManager";
import type { KurseAuswahlProps } from "~/components/kurse/SKurseAuswahlProps";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import { ConfigElement } from "@ui/utils/Config";


const SKurseAuswahl = () => import("~/components/kurse/SKurseAuswahl.vue");
const SKurseApp = () => import("~/components/kurse/SKurseApp.vue");

export class RouteKurse extends RouteAuswahlNode<KursListeManager, RouteDataKurse, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "kurse", String.raw`kurse/:id(\d+)?`, SKurseApp, SKurseAuswahl, new RouteDataKurse());
		super.mode = ServerMode.STABLE;
		super.text = "Kurse";
		super.children = [
			routeKursDaten,
			routeKurseGruppenprozesse,
			routeKurseNeu,
		];
		super.defaultChild = routeKursDaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-book-2-line";
		super.getAuswahlListProps = (props) => (<KurseAuswahlProps>{
			...props,
			setFilterNurSichtbar: this.data.setFilterNurSichtbar,
		});
		configStateImpl.config.addElements([
			new ConfigElement("kurse.auswahl.filterNurSichtbar", "user", "true"),
		]);
	}

}

export const routeKurse = new RouteKurse();
