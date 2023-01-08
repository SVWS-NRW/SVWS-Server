import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
import { RouteNodeListView } from "../RouteNodeListView";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { computed, shallowRef, ShallowRef, WritableComputedRef } from "vue";
import { RouteNode } from "~/router/RouteNode";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
import { RouteApp } from "~/router/RouteApp";


export class RouteDataLehrer {
	item: ShallowRef<LehrerListeEintrag | undefined> = shallowRef(undefined);
	auswahl: ListLehrer = new ListLehrer();
	stammdaten: DataLehrerStammdaten = new DataLehrerStammdaten();
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
}


const SLehrerAuswahl = () => import("~/components/lehrer/SLehrerAuswahl.vue")
const SLehrerApp = () => import("~/components/lehrer/SLehrerApp.vue")


export class RouteLehrer extends RouteNodeListView<LehrerListeEintrag, RouteDataLehrer, RouteApp> {

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

    public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.data.auswahl.update_list();
			return { name: redirect_name, params: { id: this.data.auswahl.liste.at(0)?.id }};  // TODO auswahl.gefiltert statt auswahl.liste nutzen
		}
        return true;
    }

    public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
		await this.data.auswahl.update_list();  // Die Auswahlliste wird als letztes geladen
	}

    protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			this.onSelect(this.data.auswahl.liste.find(l => l.id === id));
		}
	}

	protected onSelect(item?: LehrerListeEintrag) {
		if (item === this.data.item.value)
			return;
		if (item === undefined) {
			this.data.item.value = undefined;
			this.data.stammdaten.unselect();
		} else {
			this.data.item.value = item;
			this.data.stammdaten.select(this.data.item.value);
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<LehrerListeEintrag | undefined> {
		return this.getSelectorByID<LehrerListeEintrag, ListLehrer>(this.data.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			item: this.data.item,
			stammdaten: this.data.stammdaten,
			schule: this.data.schule
		};
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
				const id = (this.data.item.value === undefined) ? undefined : "" + this.data.item.value.id;
                router.push({ name: value.name, params: { id: id } });
            }
        });
        return selectedRoute;
    }

}

export const routeLehrer = new RouteLehrer();