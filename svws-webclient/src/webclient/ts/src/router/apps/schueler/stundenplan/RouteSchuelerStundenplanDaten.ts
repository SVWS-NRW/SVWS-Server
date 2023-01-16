import { StundenplanListeEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteParams, useRoute, useRouter } from "vue-router";
import { DataStundenplan } from "~/apps/schueler/DataStundenplan";
import { ListStundenplaene } from "~/apps/schueler/ListStundenplaene";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { computed } from "vue";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";

export class RouteDataSchuelerStundenplan {
	auswahl: ListStundenplaene = new ListStundenplaene();
	item: StundenplanListeEintrag | undefined = undefined;
	daten: DataStundenplan = new DataStundenplan();
}

const SSchuelerStundenplanDaten = () => import("~/components/schueler/stundenplan/SSchuelerStundenplanDaten.vue");
const SSchuelerStundenplanAuswahl = () => import("~/components/schueler/stundenplan/SSchuelerStundenplanAuswahl.vue")

export class RouteSchuelerStundenplanDaten extends RouteNodeListView<ListStundenplaene, StundenplanListeEintrag, RouteDataSchuelerStundenplan, RouteSchuelerStundenplan> {

	public constructor() {
		super("schueler_stundenplan_daten", ":idStundenplan(\\d+)?", SSchuelerStundenplanAuswahl, SSchuelerStundenplanDaten, new ListStundenplaene(), 'id', new RouteDataSchuelerStundenplan());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.setView("stundenplanauswahl", SSchuelerStundenplanAuswahl, (route) => this.getProps(route));
		super.children = [
		];
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		await this.data.auswahl.update_list();
		if ((to.name === this.name) && (to_params.idStundenplan === undefined))
			return { name: this.name, params: { id: to_params.id, idStundenplan: this.data.auswahl.liste.at(0)?.id }};
		return true;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.idStundenplan === undefined) {
			this.onSelect(undefined);
		} else {
			const idStundenplan = parseInt(to_params.idStundenplan as string);
			this.onSelect(this.data.auswahl.liste.find(s => s.id === idStundenplan));
		}
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
					router.push({ name: self.name, params: { id: route.params.id, idStundenplan: value?.id } });
				}
			}
		});
		return selected;
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop: Record<string, any> = routeSchueler.getProps(to);
		prop.stundenplan = this.data.item;
		prop.data = this.data.daten;
		return prop;
	}

}

export const routeSchuelerStundenplanDaten = new RouteSchuelerStundenplanDaten();

