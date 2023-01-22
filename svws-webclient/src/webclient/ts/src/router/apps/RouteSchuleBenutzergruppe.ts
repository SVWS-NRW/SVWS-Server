import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { routeSchuleBenutzergruppeDaten } from "~/router/apps/benutzergruppe/RouteSchuleBenutzergruppeDaten";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { ListBenutzergruppe } from "~/apps/schule/benutzerverwaltung/ListBenutzergruppe";
import { WritableComputedRef } from "vue";
import { RouteNode } from "~/router/RouteNode";
import { RouteApp } from "~/router/RouteApp";

const SBenutzergruppeAuswahl = () => import("~/components/schule/benutzergruppen/SBenutzergruppeAuswahl.vue")
const SBenutzergruppeApp = () => import("~/components/schule/benutzergruppen/SBenutzergruppeApp.vue")

export class RouteSchuleBenutzergruppe extends RouteNodeListView<ListBenutzergruppe, BenutzergruppeListeEintrag, unknown, RouteApp> {

	public constructor() {
		super("benutzergruppen", "/schule/benutzergruppe/:id(\\d+)?", SBenutzergruppeAuswahl, SBenutzergruppeApp, new ListBenutzergruppe(), 'id');
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzergruppen";
		super.setView("liste", SBenutzergruppeAuswahl, (route) => this.getProps(route));
		super.children = [
			routeSchuleBenutzergruppeDaten
		];
		super.defaultChild = routeSchuleBenutzergruppeDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.liste.update_list();
			return { name: redirect_name, params: { id: this.liste.liste.at(0)?.id }};
		}
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.liste.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.liste.liste.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: BenutzergruppeListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
		} else {
			this.item = item;
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<BenutzergruppeListeEintrag | undefined> {
		return this.getSelector();
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to)
		};
	}

}

export const routeSchuleBenutzergruppe = new RouteSchuleBenutzergruppe();
