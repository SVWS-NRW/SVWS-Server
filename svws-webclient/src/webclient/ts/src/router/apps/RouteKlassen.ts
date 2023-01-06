import { KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
import { ListKlassen } from "~/apps/klassen/ListKlassen";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { RouteNode } from "~/router/RouteNode";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";

export class RouteDataKlassen {
	item: KlassenListeEintrag | undefined = undefined;
	auswahl: ListKlassen = new ListKlassen();
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<Number, LehrerListeEintrag> = new Map();
}

const SKlassenAuswahl = () => import("~/components/klassen/SKlassenAuswahl.vue")
const SKlassenApp = () => import("~/components/klassen/SKlassenApp.vue")

export class RouteKlassen extends RouteNodeListView<KlassenListeEintrag, RouteDataKlassen> {

	public constructor() {
		super("klassen", "/klassen/:id(\\d+)?", SKlassenAuswahl, SKlassenApp, new RouteDataKlassen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
        super.setView("liste", SKlassenAuswahl, (route) => this.getProps(route));
		super.children = [
			routeKlassenDaten
		];
		super.defaultChild = routeKlassenDaten;
	}

    public async beforeEach(to: RouteNode<unknown>, to_params: RouteParams, from: RouteNode<unknown> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			return { name: redirect_name, params: { id: this.data.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

    public async enter(to: RouteNode<unknown>, to_params: RouteParams) {
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
		await this.data.listLehrer.update_list();
		this.data.mapLehrer.clear();
		this.data.listLehrer.liste.forEach(l => this.data.mapLehrer.set(l.id, l));
		await this.data.auswahl.update_list();  // Die Auswahlliste wird als letztes geladen
	}

    public async update(to: RouteNode<unknown>, to_params: RouteParams) {
	}

	protected onSelect(item?: KlassenListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<KlassenListeEintrag | undefined> {
		return this.getSelectorByID<KlassenListeEintrag, ListKlassen>(this.data.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop: Record<string, any> = RouteNodeListView.getPropsByAuswahlID(to, this.data.auswahl);
		this.onSelect(prop.item as KlassenListeEintrag | undefined);
		prop.schule = this.data.schule;
		prop.listLehrer = this.data.listLehrer;
		prop.mapLehrer = this.data.mapLehrer;
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

export const routeKlassen = new RouteKlassen();
