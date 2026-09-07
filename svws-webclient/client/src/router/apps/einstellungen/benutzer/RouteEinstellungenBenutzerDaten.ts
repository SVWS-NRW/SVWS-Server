import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { BenutzerProps } from "~/components/einstellungen/benutzer/daten/SBenutzerProps";
import { type RouteEinstellungenBenutzer, routeEinstellungenBenutzer } from "~/router/apps/einstellungen/benutzer/RouteEinstellungenBenutzer";
import { routeEinstellungen } from "~/router/apps/einstellungen/RouteEinstellungen";
import { RouteNode } from "~/router/RouteNode";

const SBenutzer = () => import("~/components/einstellungen/benutzer/daten/SBenutzer.vue");

export class RouteEinstellungenBenutzerDaten extends RouteNode<any, RouteEinstellungenBenutzer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "einstellungen.benutzer.daten", "daten", SBenutzer);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzer";
	}

	public getProps(to: RouteLocationNormalized): BenutzerProps {
		return {
			listBenutzergruppen: routeEinstellungenBenutzer.data.listBenutzergruppen,
			getBenutzerManager: routeEinstellungenBenutzer.data.getBenutzerManager,
			setAnzeigename: routeEinstellungenBenutzer.data.setAnzeigename,
			setAnmeldename: routeEinstellungenBenutzer.data.setAnmeldename,
			setIstAdmin: routeEinstellungenBenutzer.data.setIstAdmin,
			setPassword: routeEinstellungenBenutzer.data.setPassword,
			addBenutzerToBenutzergruppe: routeEinstellungenBenutzer.data.addBenutzerToBenutzergruppe,
			removeBenutzerFromBenutzergruppe: routeEinstellungenBenutzer.data.removeBenutzerFromBenutzergruppe,
			addKompetenz: routeEinstellungenBenutzer.data.addKompetenz,
			removeKompetenz: routeEinstellungenBenutzer.data.removeKompetenz,
			addBenutzerKompetenzGruppe: routeEinstellungenBenutzer.data.addBenutzerKompetenzGruppe,
			removeBenutzerKompetenzGruppe: routeEinstellungenBenutzer.data.removeBenutzerKompetenzGruppe,
			gotoBenutzergruppe: routeEinstellungenBenutzer.data.gotoBenutzergruppe,
			benutzerKompetenzen: routeEinstellungen.benutzerKompetenzen,
		};
	}

}

export const routeEinstellungenBenutzerDaten = new RouteEinstellungenBenutzerDaten();

