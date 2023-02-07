import { BetriebAnsprechpartner, BetriebListeEintrag, BetriebStammdaten, Exception, KatalogEintrag, LehrerListeEintrag, List,
	SchuelerBetriebsdaten, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchueler, RouteSchueler } from "~/router/apps/RouteSchueler";
import { routeApp } from "~/router/RouteApp";
import { App } from "~/apps/BaseApp";
import { Ref, ref } from "vue";

const SSchuelerAdressen = () => import("~/components/schueler/adressen/SSchuelerAdressen.vue");

export class RouteDataSchuelerAdressen {

	_daten: Ref<BetriebStammdaten | undefined> = ref(undefined);
	item: SchuelerListeEintrag | undefined = undefined;
	betrieb: Ref<SchuelerBetriebsdaten | undefined> = ref(undefined);
	listSchuelerbetriebe : Ref<List<SchuelerBetriebsdaten>> = ref(new Vector());
	mapBeschaeftigungsarten: Map<number, KatalogEintrag> = new Map();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();
	mapBetriebe: Map<number, BetriebListeEintrag> = new Map();
	mapAnsprechpartner: Ref<Map<number, BetriebAnsprechpartner>> = ref(new Map());

	public async onSelect(item?: SchuelerListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
			this._daten.value = undefined;
		} else {
			this.item = item;
			this.listSchuelerbetriebe.value = await App.api.getSchuelerBetriebe(App.schema, item.id);
			await this.setSchuelerBetrieb((this.listSchuelerbetriebe.value.size() > 0) ? this.listSchuelerbetriebe.value.get(0) : undefined);
		}
	}

	public async ladeAnsprechpartner(betrieb_id : number) {
		// Lade die Liste der Ansprechpartner eines Betriebs als Katalog, der nur lesend genutzt wird
		const ansprechpartner = await App.api.getBetriebAnsprechpartner(App.schema);
		const mapAnsprechpartner = new Map<number, BetriebAnsprechpartner>();
		for (const a of ansprechpartner)
			if (a.betrieb_id === betrieb_id) // TODO API-Aufruf mit Betriebs-ID, so dass nur die Ansprechpartner des konkreten Betriebs geladen werden und hier nicht gefiltert werden muss
				mapAnsprechpartner.set(a.id, a);
		this.mapAnsprechpartner.value = mapAnsprechpartner;
	}

	setSchuelerBetrieb = async (betrieb : SchuelerBetriebsdaten | undefined) => {
		if (betrieb === undefined) {
			this._daten.value = undefined;
			this.mapAnsprechpartner.value = new Map();
		} else {
			this._daten.value = await App.api.getBetriebStammdaten(App.schema, betrieb.betrieb_id);
			await this.ladeAnsprechpartner(betrieb.betrieb_id);
		}
		this.betrieb.value = betrieb;
	}

	patchBetrieb = async (data : Partial<BetriebStammdaten>, id : number) => {
		if (this._daten.value === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await App.api.patchBetriebStammdaten(data, App.schema, id);
	}

	patchSchuelerBetriebsdaten = async (data : Partial<SchuelerBetriebsdaten>, id : number) => {
		if (this.listSchuelerbetriebe.value === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		if (data.betrieb_id !== undefined) {
			// TODO neuladen der Ansprechpartner
		}
		await App.api.patchSchuelerBetriebsdaten(data, App.schema, id);
	}

	patchAnsprechpartner = async (data : Partial<BetriebAnsprechpartner>, id : number) => {
		if ((this.mapAnsprechpartner.value.size === 0) || (this.mapAnsprechpartner.value.get(id) === undefined))
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await App.api.patchBetriebanpsrechpartnerdaten(data, App.schema, id);
	}

	createAnsprechpartner = async (data: BetriebAnsprechpartner) => {
		if ((this._daten.value === undefined) || (this._daten.value.id !== data.betrieb_id))
			throw new Exception("Für das Erstellen eines Ansprechpartners von einem Betrieb muss eine gültige ID des Betriebes angegeben sein.");
		await App.api.createBetriebansprechpartner(data, App.schema, this._daten.value.id);
		await this.ladeAnsprechpartner(data.betrieb_id);
	}

	createSchuelerBetriebsdaten = async (data: SchuelerBetriebsdaten) => {
		if ((data.schueler_id === undefined) || (data.betrieb_id === undefined))
			throw new Exception("Für das Zuweisen eines Betriebs zu einem Schüler müssen die Schüler- und die Betriebs-ID angegeben werden.");
		await App.api.createSchuelerbetrieb(data, App.schema, data.schueler_id, data.betrieb_id);
		// Lade die Liste der Schülerbetriebe neu
		this.listSchuelerbetriebe.value = await App.api.getSchuelerBetriebe(App.schema, data.schueler_id);
		await this.setSchuelerBetrieb((this.listSchuelerbetriebe.value.size() > 0) ? this.listSchuelerbetriebe.value.get(0) : undefined);
	}

}

export class RouteSchuelerAdressen extends RouteNode<RouteDataSchuelerAdressen, RouteSchueler> {

	public constructor() {
		super("schueler_adressen", "adressen", SSchuelerAdressen, new RouteDataSchuelerAdressen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Adressen / Betriebe";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id === undefined)
			return false;
		// Lade den Katalog der Beschäftigungsarten
		const beschaeftigungsarten = await App.api.getKatalogBeschaeftigungsart(App.schema);
		const mapBeschaeftigungsarten = new Map<number, KatalogEintrag>();
		for (const ba of beschaeftigungsarten)
			mapBeschaeftigungsarten.set(ba.id, ba);
		this.data.mapBeschaeftigungsarten = mapBeschaeftigungsarten;
		// Lade die Liste der Lehrer als Katalog, der nur lesend genutzt wird
		const lehrer = await App.api.getLehrer(App.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of lehrer)
			mapLehrer.set(l.id, l);
		this.data.mapLehrer = mapLehrer;
		// Lade die Liste der Betriebe als Katalog, der nur lesend genutzt wird
		const betriebe = await App.api.getBetriebe(App.schema);
		const mapBetriebe = new Map<number, BetriebListeEintrag>();
		for (const b of betriebe)
			mapBetriebe.set(b.id, b);
		this.data.mapBetriebe = mapBetriebe;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id === undefined)
			return false;
		const tmp = parseInt(to_params.id as string);
		await this.data.onSelect(this.parent!.liste.liste.find(s => s.id === tmp));
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patchBetrieb: this.data.patchBetrieb,
			patchSchuelerBetriebsdaten: this.data.patchSchuelerBetriebsdaten,
			patchAnsprechpartner: this.data.patchAnsprechpartner,
			setSchuelerBetrieb: this.data.setSchuelerBetrieb,
			createAnsprechpartner: this.data.createAnsprechpartner,
			createSchuelerBetriebsdaten: this.data.createSchuelerBetriebsdaten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			idSchueler: routeSchueler.data.stammdaten.daten!.id,
			listSchuelerbetriebe: this.data.listSchuelerbetriebe.value,
			betrieb: this.data.betrieb.value,
			betriebsStammdaten: this.data._daten.value,
			mapBeschaeftigungsarten: this.data.mapBeschaeftigungsarten,
			mapLehrer: this.data.mapLehrer,
			mapBetriebe: this.data.mapBetriebe,
			mapAnsprechpartner: this.data.mapAnsprechpartner.value
		};
	}

}

export const routeSchuelerAdressen = new RouteSchuelerAdressen();

