import { JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { ListKurse } from "~/apps/kurse/ListKurse";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKurseDaten } from "~/router/apps/kurse/RouteKurseDaten";
import { RouteNode } from "~/router/RouteNode";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";

export class RouteDataKurse {
	item: KursListeEintrag | undefined = undefined;
	auswahl: ListKurse = new ListKurse();
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
	listJahrgaenge: ListJahrgaenge = new ListJahrgaenge();
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag> = new Map();
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<Number, LehrerListeEintrag> = new Map();
}

const SKurseAuswahl = () => import("~/components/kurse/SKurseAuswahl.vue")
const SKurseApp = () => import("~/components/kurse/SKurseApp.vue")

export class RouteKurse extends RouteNodeListView<KursListeEintrag, RouteDataKurse> {

	public constructor() {
		super("kurse", "/kurse/:id(\\d+)?", SKurseAuswahl, SKurseApp, new RouteDataKurse());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurse";
        super.setView("liste", SKurseAuswahl, (route) => this.getProps(route));
		super.children = [
			routeKurseDaten
		];
		super.defaultChild = routeKurseDaten;
	}

		public async beforeEach(to: RouteNode<unknown>, to_params: RouteParams, from: RouteNode<unknown> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.data.auswahl.update_list();
			return { name: redirect_name, params: { id: this.data.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

    public async enter(to: RouteNode<unknown>, to_params: RouteParams) {
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
		await this.data.listJahrgaenge.update_list();
		this.data.mapJahrgaenge.clear();
		this.data.listJahrgaenge.liste.forEach(j => this.data.mapJahrgaenge.set(j.id, j));
		await this.data.listLehrer.update_list();
		this.data.mapLehrer.clear();
		this.data.listLehrer.liste.forEach(k => this.data.mapLehrer.set(k.id, k));
		await this.data.auswahl.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	protected onSelect(item?: KursListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<KursListeEintrag | undefined> {
		return this.getSelectorByID<KursListeEintrag, ListKurse>(this.data.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop: Record<string, any> = RouteNodeListView.getPropsByAuswahlID(to, this.data.auswahl);
		this.onSelect(prop.item as KursListeEintrag | undefined);
		prop.schule = this.data.schule;
		prop.listJahrgaenge = this.data.listJahrgaenge;
		prop.mapJahrgaenge = this.data.mapJahrgaenge;
		prop.listLehrer = this.data.listLehrer;
		prop.mapLehrer = this.data.mapLehrer;
		return prop;
	}

    /**
     * TODO
     * 
     * @returns 
     */
    public getChildRouteSelector() : WritableComputedRef<RouteRecordRaw> {
        return computed({
            get: () => this.selectedChildRecord || this.defaultChild!.record,
            set: (value) => {
                this.selectedChildRecord = value;
				const id = (this.data.item === undefined) ? undefined : "" + this.data.item.id;
                useRouter().push({ name: value.name, params: { id: id } });
            }
        });
    }

}

export const routeKurse = new RouteKurse();
