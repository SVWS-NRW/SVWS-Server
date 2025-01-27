import type { RouteLocationNormalized } from "vue-router";
import type { SchuelerEinwilligungsartenZusammenfassung} from "@core";
import {BenutzerKompetenz, Schulform, ServerMode} from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogEinwilligungsarten, type RouteKatalogEinwilligungsarten } from "~/router/apps/schule/einwilligungsarten/RouteKatalogEinwilligungsarten";
import type { EinwilligungsartenDatenProps } from "~/components/schule/kataloge/einwilligungsarten/daten/SEinwilligungsartenDatenProps";
import {RouteManager} from "~/router/RouteManager";
import {routeSchuelerEinwilligungen} from "~/router/apps/schueler/einwilligungen/RouteSchuelerEinwilligungen";

const SEinwilligungsartDaten = () => import("~/components/schule/kataloge/einwilligungsarten/daten/SEinwilligungsartenDaten.vue");

export class RouteKatalogEinwilligungsartenDaten extends RouteNode<any, RouteKatalogEinwilligungsarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.einwilligungsarten.daten", "daten", SEinwilligungsartDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einwilligungsart";
	}

	gotoSchueler = async (schuelerEinwilligungsartenZusammenfassung: SchuelerEinwilligungsartenZusammenfassung) => {
		await RouteManager.doRoute(routeSchuelerEinwilligungen.getRoute({ id: schuelerEinwilligungsartenZusammenfassung.id }));
	}

	public getProps(to: RouteLocationNormalized): EinwilligungsartenDatenProps {
		return {
			patch: routeKatalogEinwilligungsarten.data.patch,
			einwilligungsartenListeManager: () => routeKatalogEinwilligungsarten.data.manager,
			gotoSchueler: this.gotoSchueler,
		};
	}

}

export const routeKatalogEinwilligungsartenDaten = new RouteKatalogEinwilligungsartenDaten();
