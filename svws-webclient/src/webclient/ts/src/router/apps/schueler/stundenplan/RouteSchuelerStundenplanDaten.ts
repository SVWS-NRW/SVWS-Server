import { StundenplanListeEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "@vue/reactivity";
import { RouteLocationNormalized, useRoute, useRouter } from "vue-router";
import { DataStundenplan } from "~/apps/schueler/DataStundenplan";
import { ListStundenplaene } from "~/apps/schueler/ListStundenplaene";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { computed } from "vue";

export class RouteDataSchuelerStundenplan {
	auswahl: ListStundenplaene = new ListStundenplaene();
	item: StundenplanListeEintrag | undefined = undefined;
	daten: DataStundenplan = new DataStundenplan();
}

const SSchuelerStundenplanDaten = () => import("~/components/schueler/stundenplan/SSchuelerStundenplanDaten.vue");
const SSchuelerStundenplanAuswahl = () => import("~/components/schueler/stundenplan/SSchuelerStundenplanAuswahl.vue")

export class RouteSchuelerStundenplanDaten extends RouteNodeListView<StundenplanListeEintrag, RouteDataSchuelerStundenplan> {

	public constructor() {
		super("schueler_stundenplan_daten", ":idStundenplan(\\d+)?", SSchuelerStundenplanAuswahl, SSchuelerStundenplanDaten, new RouteDataSchuelerStundenplan());
		super.propHandler = (route) => this.getProps(route, true);
		super.text = "Stundenplan";
        super.setView("stundenplanauswahl", SSchuelerStundenplanAuswahl, (route) => this.getProps(route, false));
		super.children = [
		];
	}

    /**
     * TODO see RouterManager - global hook
     * 
     * @param to    die Ziel-Route
     * @param from   die Quell-Route
     */
    public async beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized): Promise<any> {
		await this.data.auswahl.update_list();
		if ((to.name?.toString() === this.name) && (to.params.idStundenplan === undefined))
			return { name: this.name, params: { id: to.params.id, idStundenplan: this.data.auswahl.liste.at(0)?.id }};
        return true;
    }

	protected getAuswahlComputedProperty(): WritableComputedRef<StundenplanListeEintrag | undefined> {
		const router = useRouter();
		const route = useRoute();
		const self = this;

		const selected = computed({
			get(): StundenplanListeEintrag | undefined {
				if (route.params.id === undefined)
					return undefined;
				let tmp = self.data.auswahl.ausgewaehlt;
				if ((tmp === undefined) || (tmp.id.toString() !== route.params.idStundenplan))
					tmp = self.data.auswahl.liste.find(s => s.id.toString() === route.params.idStundenplan);
				return tmp;
			},
			set(value: StundenplanListeEintrag | undefined) {
				self.data.auswahl.ausgewaehlt = value;
				const from_name = route.name?.toString() || "";
				if ((from_name !== self.name) && from_name?.startsWith(self.name)) {  // TODO Ergänze Methode bei RouteNode isNested und nutze diese 
					const params = {...route.params};
					params.idStundenplan = "" + value?.id;
					router.push({ name: from_name, params: params });
				} else {
					router.push({ name: self.name, params: { idStundenplan: value?.id } });
				}
			}
		});
		return selected;
	}

	protected onSelect(item?: StundenplanListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten.unselect();
		} else {
			this.data.item = item;
			this.data.daten.select(this.data.item);
		}
	}

	public getProps(to: RouteLocationNormalized, doSelect: boolean): Record<string, any> {
		let prop: Record<string, any> = routeSchueler.getProps(to);
		let idStundenplan: number | undefined = undefined;
		let itemStundenplan: StundenplanListeEintrag | undefined = undefined;
		if (to.params.idStundenplan !== undefined) {
			idStundenplan = parseInt(to.params.idStundenplan as string);
			itemStundenplan = this.data.auswahl.liste.find(s => s.id === idStundenplan);
		}
		if (doSelect)
			this.onSelect(itemStundenplan);
		prop.stundenplan = this.data.item;
		prop.data = this.data.daten;
		return prop;
	}

}

export const routeSchuelerStundenplanDaten = new RouteSchuelerStundenplanDaten();

