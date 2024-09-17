import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { StundenplanAufsichtsbereich } from "@core";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";

import type { AufsichtsbereicheProps } from "~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereicheProps";
import type { AufsichtsbereicheAuswahlProps } from "~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereicheAuswahlProps";
import { RouteDataKatalogAufsichtsbereiche } from "./RouteDataKatalogAufsichtsbereiche";


const SAufsichtsbereicheAuswahl = () => import("~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereicheAuswahl.vue")
const SAufsichtsbereiche = () => import("~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereiche.vue")

export class RouteKatalogAufsichtsbereiche extends RouteNode<RouteDataKatalogAufsichtsbereiche, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kataloge.aufsichtsbereiche", "aufsichtsbereiche/:id(\\d+)?", SAufsichtsbereiche, new RouteDataKatalogAufsichtsbereiche());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Aufsichtsbereiche";
		super.setView("eintraege", SAufsichtsbereicheAuswahl, (route) => this.getAuswahlProps(route));
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeListe();
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.stundenplanManager.aufsichtsbereichGetMengeAsList().isEmpty())
			return;
		let eintrag: StundenplanAufsichtsbereich | undefined;
		if (!to_params.id && this.data.auswahl)
			return this.getRoute(this.data.auswahl.id);
		if (!to_params.id) {
			eintrag = this.data.stundenplanManager.aufsichtsbereichGetMengeAsList().get(0);
			return this.getRoute(eintrag.id);
		} else {
			const id = parseInt(to_params.id);
			eintrag = this.data.stundenplanManager.aufsichtsbereichGetByIdOrException(id);
		}
		await this.data.setEintrag(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): AufsichtsbereicheAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			stundenplanManager: () => this.data.stundenplanManager,
		};
	}

	public getProps(to: RouteLocationNormalized): AufsichtsbereicheProps {
		return {
			auswahl: this.data.auswahl,
			patch: this.data.patch,
		};
	}

}

export const routeKatalogAufsichtsbereiche = new RouteKatalogAufsichtsbereiche();
