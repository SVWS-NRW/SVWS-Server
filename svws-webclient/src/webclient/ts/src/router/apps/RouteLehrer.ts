import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
import { RouteNodeListView } from "../RouteNodeListView";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { computed, WritableComputedRef } from "vue";
import { RouteNode } from "~/router/RouteNode";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";


export class RouteDataLehrer {
	item: LehrerListeEintrag | undefined = undefined;
	auswahl: ListLehrer = new ListLehrer();
	stammdaten: DataLehrerStammdaten = new DataLehrerStammdaten();
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
}


const SLehrerAuswahl = () => import("~/components/lehrer/SLehrerAuswahl.vue")
const SLehrerApp = () => import("~/components/lehrer/SLehrerApp.vue")


export class RouteLehrer extends RouteNodeListView<LehrerListeEintrag, RouteDataLehrer> {

	public constructor() {
		super("lehrer", "/lehrkraefte/:id(\\d+)?", SLehrerAuswahl, SLehrerApp, new RouteDataLehrer());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrkräfte";
        super.setView("liste", SLehrerAuswahl, (route) => this.getProps(route));
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerUnterrichtsdaten
		];
		super.defaultChild = routeLehrerIndividualdaten;
	}

    public async beforeEach(to: RouteNode<unknown>, to_params: RouteParams, from: RouteNode<unknown> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.data.auswahl.update_list();
			return { name: redirect_name, params: { id: this.data.auswahl.liste.at(0)?.id }};  // TODO auswahl.gefiltert statt auswahl.liste nutzen
		}
        return true;
    }

    public async enter(to: RouteNode<unknown>, to_params: RouteParams) {
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
		await this.data.auswahl.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	protected onSelect(item?: LehrerListeEintrag) {
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

    protected getAuswahlComputedProperty(): WritableComputedRef<LehrerListeEintrag | undefined> {
		return this.getSelectorByID<LehrerListeEintrag, ListLehrer>(this.data.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop: Record<string, any> = RouteNodeListView.getPropsByAuswahlID(to, this.data.auswahl);
		this.onSelect(prop.item as LehrerListeEintrag | undefined);
		prop.stammdaten = this.data.stammdaten;
		prop.schule = this.data.schule;
		return prop;
	}

    /**
     * TODO
     * 
     * @returns 
     */
    public getChildRouteSelector() {
        const router = useRouter();
        const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
            get: () => this.selectedChildRecord || this.defaultChild!.record,
            set: (value) => {
                this.selectedChildRecord = value;
				const id = (this.data.item === undefined) ? undefined : "" + this.data.item.id;
                router.push({ name: value.name, params: { id: id } });
            }
        });
        return selectedRoute;
    }

}

export const routeLehrer = new RouteLehrer();