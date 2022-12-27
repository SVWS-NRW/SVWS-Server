import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRouter } from "vue-router";
import { mainApp } from "~/apps/Main";
import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
import { RouteNodeListView } from "../RouteNodeListView";
import { routeSchuelerAbschnitt } from "~/router/apps/schueler/RouteSchuelerAbschnitt";
import { routeSchuelerAdressen } from "~/router/apps/schueler/RouteSchuelerAdressen";
import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/RouteSchuelerErziehungsberechtigte";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/RouteSchuelerIndividualdaten";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/RouteSchuelerLaufbahnplanung";
import { routeSchuelerLeistungsdaten } from "~/router/apps/schueler/RouteSchuelerLeistungsdaten";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/RouteSchuelerSchulbesuch";
import { routeSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";
import { ListSchueler } from "~/apps/schueler/ListSchueler";

export class RouteDataSchueler {
	item: SchuelerListeEintrag | undefined = undefined;
	stammdaten: DataSchuelerStammdaten = new DataSchuelerStammdaten();
}

const SSchuelerAuswahl = () => import("~/components/schueler/SSchuelerAuswahl.vue")
const SSchuelerApp = () => import("~/components/schueler/SSchuelerApp.vue")

export class RouteSchueler extends RouteNodeListView<SchuelerListeEintrag, RouteDataSchueler> {

	protected defaultChildNode = routeSchuelerIndividualdaten;

	public constructor() {
		super("schueler", "/schueler/:id(\\d+)?", SSchuelerAuswahl, SSchuelerApp, new RouteDataSchueler());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler";
        super.setView("liste", SSchuelerAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.schueler.auswahl));
		super.children = [
			routeSchuelerIndividualdaten,
			routeSchuelerErziehungsberechtigte,
			routeSchuelerAdressen,
			routeSchuelerSchulbesuch,
			routeSchuelerAbschnitt,
			routeSchuelerLeistungsdaten,
			routeSchuelerLaufbahnplanung,
			routeSchuelerStundenplan
		];
	}

    /**
     * TODO see RouterManager - global hook
     * 
     * @param to    die Ziel-Route
     * @param from   die Quell-Route
     */
    public beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized): any {
		if ((to.name?.toString() === this.name) && (to.params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChildNode.name : this.selectedChild.name;
			return { name: redirect_name, params: { id: mainApp.apps.schueler.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

	protected onSelect(item?: SchuelerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.stammdaten.unselect();
		} else {
			this.data.item = item;
			this.data.stammdaten.select(this.data.item);
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<SchuelerListeEintrag | undefined> {
		return this.getSelectorByID<SchuelerListeEintrag, ListSchueler>(mainApp.apps.schueler.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.schueler.auswahl);
		this.onSelect(prop.item as SchuelerListeEintrag | undefined);
		return prop;
	}

    /**
     * TODO
     * 
     * @returns 
     */
    public getChildRouteSelector() {
        const router = useRouter();
        const self = this;
        const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
            get(): RouteRecordRaw {
                return self.selectedChildRecord || self.defaultChildNode.record;
            },
            set(value: RouteRecordRaw) {
                self.selectedChildRecord = value;
				const id = (self.data.item === undefined) ? undefined : "" + self.data.item.id;
                router.push({ name: value.name, params: { id: id } });
            }
        });
        return selectedRoute;
    }

}

export const routeSchueler = new RouteSchueler();
