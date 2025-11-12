import type { RouteLocationNormalized } from "vue-router";
import type { SchuelerEinwilligungsartenZusammenfassung } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeEinwilligungsarten, type RouteEinwilligungsarten } from "~/router/apps/schule/schulbezogen/einwilligungsarten/RouteEinwilligungsarten";
import type { EinwilligungsartenDatenProps } from "~/components/schule/schulbezogen/einwilligungsarten/daten/EinwilligungsartenDatenProps";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerEinwilligungen } from "~/router/apps/schueler/einwilligungen/RouteSchuelerEinwilligungen";
import { api } from "~/router/Api";

const EinwilligungsartenDaten = () => import("~/components/schule/schulbezogen/einwilligungsarten/daten/EinwilligungsartenDaten.vue");

export class RouteEinwilligungsartenDaten extends RouteNode<any, RouteEinwilligungsarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.einwilligungsarten.daten", "daten", EinwilligungsartenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einwilligungsart";
	}

	gotoSchueler = async (schuelerEinwilligungsartenZusammenfassung: SchuelerEinwilligungsartenZusammenfassung) => {
		await RouteManager.doRoute(routeSchuelerEinwilligungen.getRoute({ id: schuelerEinwilligungsartenZusammenfassung.id }));
	};

	public getProps(to: RouteLocationNormalized): EinwilligungsartenDatenProps {
		return {
			patch: routeEinwilligungsarten.data.patch,
			manager: () => routeEinwilligungsarten.data.manager,
			gotoSchueler: this.gotoSchueler,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeEinwilligungsartenDaten = new RouteEinwilligungsartenDaten();
