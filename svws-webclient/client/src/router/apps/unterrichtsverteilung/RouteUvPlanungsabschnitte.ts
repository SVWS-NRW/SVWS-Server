import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import type { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { UvPlanungsabschnitteListeManager } from "@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager";

import SUvPlanungsArbeitsflaeche from "~/components/unterrichtsverteilung/planung/SUvPlanungsArbeitsflaeche.vue";
import { istUvPlanungsAuswahlArt, type UvDetailZiel } from "~/components/unterrichtsverteilung/planung/UvPlanung";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import { schuleStateImpl as schuleState } from "~/states/SchuleStateImpl";
import { uvStateImpl } from "~/states/UvStateImpl";

export class RouteUvPlanungsabschnitte extends RouteNode<any, RouteUv> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN,
		], "uv.planungsabschnitt", String.raw`planungsabschnitt/:bereich?/:idObjekt(\d+)?`, SUvPlanungsArbeitsflaeche);
		super.mode = ServerMode.DEV;
		super.propHandler = to => this.getProps(to.params);
		super.text = "Planung";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(false, true, params);
	}

	public getProps(params: RouteParams) {
		return {
			detailZiel: this.detailZiel(params),
			gotoGrunddaten: this.gotoGrunddaten,
			gotoSchildSchueler: this.gotoSchildSchueler,
			schuljahr: schuleState.abschnitt.schuljahr,
			assignLehrer: uvStateImpl.planung.lerngruppenLehrerZuordnen,
			gotoDetail: this.gotoDetail,
			gotoOverview: this.gotoOverview,
			planungsabschnittManager: this.planungsabschnittManager,
			patchPlanungsabschnitt: this.patchPlanungsabschnitt,
			benutzerKompetenzen: benutzerStateImpl.kompetenzen,
			patchZuordnung: uvStateImpl.planung.patchLerngruppenLehrer,
			removeZuordnung: uvStateImpl.planung.lerngruppenLehrerEntfernen,
			addSchueler: this.addSchueler,
			removeSchueler: this.removeSchueler,
		};
	}

	// Bestehende Kurs- und Schülergruppen-Links in aktuelle Detailziele übersetzen.

	protected async update(_to: RouteNode<any, any>, params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		if (params.bereich !== 'kurse' && params.bereich !== 'schuelergruppen') {
			return;
		}
		const ziel = this.detailZiel(params);
		return this.getRoute({ bereich: ziel?.art ?? '', idObjekt: ziel !== null && 'id' in ziel ? String(ziel.id) : '' });
	}

	private detailZiel(params: RouteParams): UvDetailZiel | null {
		const bereich = params.bereich;
		if (bereich === 'abschnitt') {
			return { art: 'abschnitt' };
		}
		const objekt = params.idObjekt;
		const id = typeof objekt === 'string' && /^\d+$/.test(objekt) && Number.isSafeInteger(Number(objekt)) ? Number(objekt) : null;
		if (id === null) {
			return null;
		}
		const plan = uvStateImpl.planungsabschnitt;
		const manager = uvStateImpl.uvManager;
		if (bereich === 'kurse' && plan !== null) {
			const gruppe = [...manager.lerngruppeGetMengeByPlanungsabschnitt(plan)].find(item => item.idKurs === id);
			return gruppe === undefined ? null : { art: 'lerngruppen', id: gruppe.id };
		}
		if (bereich === 'schuelergruppen' && plan !== null) {
			return this.schuelergruppenDetailZiel(plan, id);
		}
		return istUvPlanungsAuswahlArt(bereich) ? { art: bereich, id } : null;
	}

	private schuelergruppenDetailZiel(plan: UvPlanungsabschnitt, id: number): UvDetailZiel | null {
		const manager = uvStateImpl.uvManager;
		const klasse = [...manager.klasseGetMengeByPlanungsabschnitt(plan)].find(item => item.idSchuelergruppe === id);
		if (klasse !== undefined) {
			return { art: 'klassen', id: klasse.id };
		}
		const schuelergruppe = manager.schuelergruppeGetByIdOrNull(id);
		if (schuelergruppe === null) {
			return null;
		}
		const gruppe = [...manager.lerngruppeGetMengeBySchuelergruppe(schuelergruppe)].find(item => item.idPlanungsabschnitt === plan.id);
		return gruppe === undefined ? null : { art: 'lerngruppen', id: gruppe.id };
	}

	private readonly gotoDetail = async (ziel: UvDetailZiel): Promise<void> => {
		const planId = this.parent?.data.manager.auswahl().id;
		if (planId === undefined) {
			return;
		}
		await RouteManager.doRoute(this.getRoute({ id: planId, bereich: ziel.art, idObjekt: ziel.art === 'abschnitt' ? '' : String(ziel.id) }));
	};

	private readonly gotoOverview = async (): Promise<void> => {
		const planId = this.parent?.data.manager.auswahl().id;
		if (planId !== undefined) {
			await RouteManager.doRoute(this.getRoute({ id: planId, bereich: '', idObjekt: '' }));
		}
	};

	private readonly patchPlanungsabschnitt = async (data: Partial<UvPlanungsabschnitt>): Promise<boolean> => {
		if (this.parent === undefined) {
			throw new Error('Der Planungsabschnitt ist nicht verfügbar.');
		}
		return this.parent.data.patch(data);
	};

	private readonly planungsabschnittManager = (): UvPlanungsabschnitteListeManager => {
		if (this.parent === undefined) {
			throw new Error('Der Planungsabschnitt ist nicht verfügbar.');
		}
		return this.parent.data.manager;
	};

	// Schüleraktionen für die Detailansicht

	private readonly addSchueler = async (idSchuelergruppe: number, idSchueler: number): Promise<void> => {
		await uvStateImpl.planung.addSchuelerToSchuelergruppe(idSchuelergruppe, [idSchueler]);
	};

	private readonly removeSchueler = async (idSchuelergruppe: number, idSchueler: number): Promise<void> => {
		await uvStateImpl.planung.removeSchuelerFromSchuelergruppe(idSchuelergruppe, [idSchueler]);
	};

	private readonly gotoGrunddaten = async (art: 'lehrer' | 'stundentafeln' | 'raeume' | 'zeitraster', idObjekt: number): Promise<void> => {
		const node = RouteNode.getNodeByName(`uv.grunddaten.${art}`);
		if (node === undefined) {
			throw new Error('Der Grunddatenbereich ist nicht verfügbar.');
		}
		const parameter = {
			lehrer: 'idLehrer',
			stundentafeln: 'idStundentafel',
			raeume: 'idRaum',
			zeitraster: 'idZeitraster',
		};
		await RouteManager.doRoute(node.getRoute({ id: -1, [parameter[art]]: idObjekt }));
	};

	private readonly gotoSchildSchueler = async (id: number): Promise<void> => {
		await RouteManager.doRoute(routeSchueler.getRoute({ id }));
	};
}

export const routeUvPlanungsabschnitt = new RouteUvPlanungsabschnitte();
