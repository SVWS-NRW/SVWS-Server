import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { GostBlockungListeneintrag } from "@core/core/data/gost/GostBlockungListeneintrag";
import type { GostBlockungsdaten } from "@core/core/data/gost/GostBlockungsdaten";
import type { GostBlockungsergebnis } from "@core/core/data/gost/GostBlockungsergebnis";
import type { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import { UvKlasse } from "@core/core/data/uv/UvKlasse";
import { UvKlassenLehrer } from "@core/core/data/uv/UvKlassenLehrer";
import { UvKurs } from "@core/core/data/uv/UvKurs";
import { UvKursCreateRequest } from "@core/core/data/uv/UvKursCreateRequest";
import type { UvKursImportDaten } from "@core/core/data/uv/UvKursImportDaten";
import { UvLerngruppe } from "@core/core/data/uv/UvLerngruppe";
import { UvLerngruppeCreateRequest } from "@core/core/data/uv/UvLerngruppeCreateRequest";
import { UvLerngruppenLehrer } from "@core/core/data/uv/UvLerngruppenLehrer";
import { UvLerngruppenLehrerCreateRequest } from "@core/core/data/uv/UvLerngruppenLehrerCreateRequest";
import type { UvLerngruppenSchiene } from "@core/core/data/uv/UvLerngruppenSchiene";
import type { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
import { UvPlanungsabschnittLehrer } from "@core/core/data/uv/UvPlanungsabschnittLehrer";
import { UvPlanungsabschnittSchueler } from "@core/core/data/uv/UvPlanungsabschnittSchueler";
import type { UvPlanungsabschnittsdatenBundle } from "@core/core/data/uv/UvPlanungsabschnittsdatenBundle";
import { UvPlanungsabschnittZeitraster } from "@core/core/data/uv/UvPlanungsabschnittZeitraster";
import { UvSchiene } from "@core/core/data/uv/UvSchiene";
import { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
import { UvSchuelergruppeCreateRequest } from "@core/core/data/uv/UvSchuelergruppeCreateRequest";
import type { UvSchuelergruppeSchueler } from "@core/core/data/uv/UvSchuelergruppeSchueler";
import { UvSchuelerImportOptions } from "@core/core/data/uv/UvSchuelerImportOptions";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ListUtils } from "@core/core/utils/ListUtils";
import { UvManager } from "@core/core/utils/uv/UvManager";
import { UvRegelManager } from "@core/core/utils/uv/UvRegelManager";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { UvState, UvZuordnungArt } from "@ui/states/UvState";

import { istUvPlanungsAuswahlArt } from "~/components/unterrichtsverteilung/planung/UvPlanung";
import { UvPlanungsManager } from "~/components/unterrichtsverteilung/planung/UvPlanungsManager";
import { api } from "~/router/Api";

import type { UvRequestKontext, UvStateAktionskontext } from "./UvStateImpl";

type UvPlanungsAktionen = UvState['planung'];

interface UvPlanungsStateKontext extends UvStateAktionskontext {
	planungsabschnitt: () => UvPlanungsabschnitt;
	requestKontext: () => UvRequestKontext;
	istRequestKontextAktuell: (kontext: UvRequestKontext, mitPlanungsabschnitt?: boolean) => boolean;
	objektAenderungEinreihen: (schluessel: string, aktion: (kontext: UvRequestKontext) => Promise<void>) => Promise<void>;
}

export class UvPlanungsStateAktionen implements UvPlanungsAktionen {

	public constructor(private readonly kontext: UvPlanungsStateKontext) {
	}

	/** Ordnet dem Planungsabschnitt ein Zeitraster zu. */
	public addZeitrasterZuordnung = async (idZeitraster: number): Promise<void> => {
		const planungsabschnitt = this.kontext.state.planungsabschnitt;
		if (planungsabschnitt === null) {
			return;
		}
		const zuordnung: Partial<UvPlanungsabschnittZeitraster> = {
			idPlanungsabschnitt: planungsabschnitt.id,
			idZeitraster: idZeitraster,
		};
		const neu = await api.server.createUvPlanungsabschnittZeitraster(zuordnung, api.schema);
		this.kontext.state.uvManager.planungsabschnittZeitrasterAdd(neu);
		this.kontext.commit();
	};

	/** Ändert die Jahrgänge eines zugeordneten Zeitrasters. */
	public patchZeitrasterZuordnung = async (id: UvPlanungsabschnittZeitraster, data: JahrgangsDaten[]): Promise<void> => {
		const idsJahrgaenge = new ArrayList<number>();
		for (const d of data) {
			idsJahrgaenge.add(d.id);
		}
		const patch: Partial<UvPlanungsabschnittZeitraster> = { idPlanungsabschnitt: id.idPlanungsabschnitt, idZeitraster: id.idZeitraster, idsJahrgaenge };
		await api.server.patchUvPlanungsabschnittZeitraster(patch, api.schema);
		const existing = this.kontext.state.uvManager.planungsabschnittZeitrasterGetByIdOrException(id.idPlanungsabschnitt, id.idZeitraster);
		this.kontext.patchManagerDto(existing, patch, UvPlanungsabschnittZeitraster,
			patched => this.kontext.state.uvManager.planungsabschnittZeitrasterAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Ordnet dem Planungsabschnitt mehrere Zeitraster zu. */
	public addZeitrasterZuordnungen = async (idZeitrasterList: number[]): Promise<void> => {
		const planungsabschnitt = this.kontext.state.planungsabschnitt;
		if (planungsabschnitt === null) {
			return;
		}
		const zuordnungen = new ArrayList<Partial<UvPlanungsabschnittZeitraster>>();
		for (const idZeitraster of idZeitrasterList) {
			zuordnungen.add({
				idPlanungsabschnitt: planungsabschnitt.id,
				idZeitraster: idZeitraster,
			});
		}
		const neueZuordnungen = await api.server.createUvPlanungsabschnittZeitrasterMultiple(zuordnungen, api.schema);
		this.kontext.state.uvManager.planungsabschnittZeitrasterAddAll(neueZuordnungen);
		this.kontext.commit();
	};

	/** Entfernt ein Zeitraster aus dem Planungsabschnitt. */
	public removeZeitrasterZuordnung = async (idZeitraster: number): Promise<void> => {
		const planungsabschnitt = this.kontext.state.planungsabschnitt;
		if (planungsabschnitt === null) {
			return;
		}
		await api.server.deleteUvPlanungsabschnittZeitraster(api.schema, planungsabschnitt.id, idZeitraster);
		this.kontext.state.uvManager.planungsabschnittZeitrasterRemoveById(planungsabschnitt.id, idZeitraster);
		this.kontext.commit();
	};

	/** Entfernt mehrere Zeitraster aus dem Planungsabschnitt. */
	public removeZeitrasterZuordnungen = async (idZeitrasterList: number[]): Promise<void> => {
		const planungsabschnitt = this.kontext.state.planungsabschnitt;
		if (planungsabschnitt === null) {
			return;
		}
		const listIds = new ArrayList<number>();
		for (const id of idZeitrasterList) {
			listIds.add(id);
		}
		await api.server.deleteUvPlanungsabschnittZeitrasterMultiple(listIds, api.schema, planungsabschnitt.id);
		this.kontext.state.uvManager.planungsabschnittZeitrasterRemoveAllById(planungsabschnitt.id, listIds);
		this.kontext.commit();
	};

	/** Legt eine Klasse mit ihrer Schülergruppe gemeinsam an. */
	public createKlasseMitSchuelergruppe = async (klasse: Partial<UvKlasse>, idsJahrgaenge: number[] | null): Promise<UvKlasse> => {
		const plan = this.kontext.planungsabschnitt();
		const daten = Object.assign(new UvKlasse(), klasse, { idPlanungsabschnitt: plan.id });
		const schuelergruppe = idsJahrgaenge === null ? null : new UvSchuelergruppeCreateRequest();
		if (schuelergruppe !== null && idsJahrgaenge !== null) {
			schuelergruppe.idPlanungsabschnitt = plan.id;
			schuelergruppe.bezeichnung = `Klasse ${daten.kuerzel}`.trim();
			for (const id of idsJahrgaenge) {
				schuelergruppe.idsJahrgaengeErlaubt.add(id);
			}
		}
		const result: UvPlanungsabschnittsdatenBundle = await api.server.createUvKlasseMitSchuelergruppe({ klasse: daten, schuelergruppe }, api.schema);
		const neu = result.klassen.get(0);
		this.kontext.state.uvManager.planungsabschnittsdatenBundleAdd(result);
		this.kontext.commit();
		return neu;
	};

	/** Löscht die ausgewählten Klassen. */
	public delKlasse = async (klassen: UvKlasse[]): Promise<void> => {
		this.kontext.planungsabschnitt();
		const ids = new ArrayList<number>();
		const listKlassen = new ArrayList<UvKlasse>();
		for (const k of klassen) {
			ids.add(k.id);
			listKlassen.add(k);
		}
		await api.server.deleteUvKlassen(ids, api.schema);
		this.kontext.state.uvManager.klasseRemoveAll(listKlassen);
		klassen.length = 0;
		this.kontext.commit();
	};

	/** Aktualisiert eine Klasse. */
	public patchKlasse = async (idKlasse: number, klasse: Partial<UvKlasse>): Promise<void> => {
		await api.server.patchUvKlasse({ ...klasse, id: idKlasse }, api.schema);
		const existing = this.kontext.state.uvManager.klasseGetByIdOrException(idKlasse);
		this.kontext.patchManagerDto(existing, klasse, UvKlasse, patched => this.kontext.state.uvManager.klassePatchAttributes(patched));
		this.kontext.commit();
	};

	/** Ordnet einer Klasse eine Lehrkraft zu. */
	public addKlassenLehrer = async (data: Partial<UvKlassenLehrer>): Promise<void> => {
		const planungsabschnitt = this.kontext.planungsabschnitt();
		const payload = this.kontext.createPayload(data);
		payload.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvKlassenLehrer(payload, api.schema);
		this.kontext.state.uvManager.klassenLehrerAdd(neu);
		this.kontext.commit();
	};

	/** Prüft, ob eine Klassenlehrkraftzuordnung geändert werden darf. */
	private klassenLehrerAenderungPruefen(id: number): UvKlassenLehrer {
		const zuordnung = this.kontext.state.uvManager.klassenLehrerGetByZuordnungsIdOrException(id);
		const klasse = this.kontext.state.uvManager.klasseGetByIdOrNull(zuordnung.idKlasse);
		const plan = this.kontext.planungsabschnitt();
		if (!this.kontext.state.hatKompetenzAendern || zuordnung.idPlanungsabschnitt !== plan.id || klasse?.idPlanungsabschnitt !== plan.id) {
			throw new Error('Die Klassenlehrerzuordnung gehört nicht zum aktuellen Planungsabschnitt oder darf nicht bearbeitet werden.');
		}
		if (UvRegelManager.klassenlehrerIstFixiert(ArrayList.of(...this.kontext.state.regelliste), klasse.id, zuordnung.idLehrer)) {
			throw new Error('Bitte zuerst die Fixierung der Klassenlehrkraft lösen.');
		}
		return zuordnung;
	}

	/** Aktualisiert eine Klassenlehrkraftzuordnung. */
	public patchKlassenLehrer = async (id: number, data: Partial<UvKlassenLehrer>): Promise<void> => {
		const zuordnung = this.klassenLehrerAenderungPruefen(id);
		const reihenfolge = data.reihenfolge ?? zuordnung.reihenfolge;
		if (!UvManager.lehrerZuordnungswerteGueltig(reihenfolge, 0, 0, 0)) {
			throw new Error('Die Reihenfolge muss eine ganze Zahl ab 1 sein.');
		}
		await api.server.patchUvKlassenLehrer({ reihenfolge, id }, api.schema);
		this.kontext.patchManagerDto(zuordnung, { reihenfolge }, UvKlassenLehrer,
			patched => this.kontext.state.uvManager.klassenLehrerAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Entfernt Klassenlehrkraftzuordnungen. */
	public delKlassenLehrer = async (lehrerList: UvKlassenLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		const listZuordnungen = new ArrayList<UvKlassenLehrer>();
		for (const item of lehrerList) {
			const zuordnung = this.klassenLehrerAenderungPruefen(item.id);
			if (zuordnung.idKlasse !== item.idKlasse) {
				throw new Error('Die Zuordnung gehört nicht zur gewählten Klasse.');
			}
			ids.add(zuordnung.id);
			listZuordnungen.add(zuordnung);
		}
		await api.server.deleteUvKlassenLehrerMultiple(ids, api.schema);
		this.kontext.state.uvManager.klassenLehrerRemoveAll(listZuordnungen);
		lehrerList.length = 0;
		this.kontext.commit();
	};

	/** Legt Kurs und Lerngruppe gemeinsam mit der Schülerzuordnung an. */
	public createKursLerngruppe = async (
		kursDaten: Partial<UvKurs>,
		idSchuelergruppe: number | null,
		idsJahrgaenge: number[],
		idsSchueler: number[],
		wochenstunden: number,
		wochenstundenUnterrichtet: number
	): Promise<UvLerngruppe> => {
		const plan = this.kontext.planungsabschnitt();
		const kurs = Object.assign(new UvKursCreateRequest(), kursDaten, { idPlanungsabschnitt: plan.id, idSchuelergruppe: idSchuelergruppe ?? 0 });
		const lerngruppe = Object.assign(new UvLerngruppeCreateRequest(), { idPlanungsabschnitt: plan.id, wochenstunden, wochenstundenUnterrichtet });
		const schuelergruppe = idSchuelergruppe === null ? new UvSchuelergruppeCreateRequest() : null;
		if (schuelergruppe !== null) {
			schuelergruppe.idPlanungsabschnitt = plan.id;
			schuelergruppe.bezeichnung = `Kurs ${kurs.kursart} ${kurs.kursnummer}`;
			for (const id of idsJahrgaenge) {
				schuelergruppe.idsJahrgaengeErlaubt.add(id);
			}
		}
		const schuelerIds = new ArrayList<number>();
		for (const id of idsSchueler) {
			schuelerIds.add(id);
		}
		const result: UvPlanungsabschnittsdatenBundle = await api.server.createUvKursMitLerngruppe({ kurs, lerngruppe, schuelergruppe, idsSchueler: schuelerIds }, api.schema);
		const neu = result.lerngruppen.get(0);
		this.kontext.state.uvManager.planungsabschnittsdatenBundleAdd(result);
		this.kontext.commit();
		return neu;
	};

	/** Löscht die ausgewählten Kurse. */
	public delKurs = async (kurse: UvKurs[]): Promise<void> => {
		this.kontext.planungsabschnitt();
		const ids = new ArrayList<number>();
		const listKurse = new ArrayList<UvKurs>();
		for (const k of kurse) {
			ids.add(k.id);
			listKurse.add(k);
		}
		await api.server.deleteUvKurse(ids, api.schema);
		this.kontext.state.uvManager.kursRemoveAll(listKurse);
		kurse.length = 0;
		this.kontext.commit();
	};

	/** Speichert Änderungen eines Kurses in ihrer Aufrufreihenfolge. */
	public patchKurs = (idKurs: number, kurs: Partial<UvKurs>): Promise<void> => {
		const daten = { ...kurs, id: idKurs };
		return this.kontext.objektAenderungEinreihen(`kurs:${idKurs}`, async kontext => {
			const plan = this.kontext.planungsabschnitt();
			const existing = kontext.manager.kursGetByIdOrException(idKurs);
			if (!this.kontext.state.hatKompetenzAendern || existing.idPlanungsabschnitt !== plan.id
				|| (daten.idPlanungsabschnitt !== undefined && daten.idPlanungsabschnitt !== plan.id)) {
				throw new Error('Der Kurs darf im aktuellen Planungsabschnitt nicht bearbeitet werden.');
			}
			if (daten.idSchuelergruppe !== undefined) {
				kontext.manager.schuelergruppenAenderungPruefen(plan, daten.idSchuelergruppe, new ArrayList<number>());
			}
			await api.server.patchUvKurs(daten, kontext.schema);
			if (!this.kontext.istRequestKontextAktuell(kontext, false)) {
				return;
			}
			const aktuell = kontext.manager.kursGetByIdOrException(idKurs);
			this.kontext.patchManagerDto(aktuell, daten, UvKurs, patched => kontext.manager.kursPatchAttributes(patched));
			this.kontext.commit();
		});
	};

	/** Importiert Kurse und übernimmt die gelieferten Planungsdaten. */
	public importKursdaten = async (daten: UvKursImportDaten): Promise<void> => {
		const planungsabschnitt = this.kontext.planungsabschnitt();
		const planungsabschnittsdaten = await api.server.importUvKurse(daten, api.schema, planungsabschnitt.id);
		this.kontext.state.uvManager.planungsabschnittsdatenBundleAdd(planungsabschnittsdaten);
		this.kontext.commit();
	};

	/** Legt eine Schiene im Planungsabschnitt an. */
	public createSchiene = async (schiene: Partial<UvSchiene>): Promise<UvSchiene> => {
		const planungsabschnitt = this.kontext.planungsabschnitt();
		const payload = this.kontext.createPayload(schiene);
		payload.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvSchiene(payload, api.schema);
		this.kontext.state.uvManager.schieneAdd(neu);
		this.kontext.commit();
		return neu;
	};

	/** Löscht die ausgewählten Schienen. */
	public delSchiene = async (schienen: UvSchiene[]): Promise<void> => {
		this.kontext.planungsabschnitt();
		const ids = new ArrayList<number>();
		const listSchienen = new ArrayList<UvSchiene>();
		for (const s of schienen) {
			ids.add(s.id);
			listSchienen.add(this.kontext.state.uvManager.schieneGetByIdOrException(s.id));
		}
		await api.server.deleteUvSchienen(ids, api.schema);
		this.kontext.state.uvManager.schieneRemoveAll(listSchienen);
		schienen.length = 0;
		this.kontext.commit();
	};

	/** Aktualisiert eine Schiene. */
	public patchSchiene = async (idSchiene: number, schiene: Partial<UvSchiene>): Promise<void> => {
		const manager = this.kontext.state.uvManager;
		await api.server.patchUvSchiene({ ...schiene, id: idSchiene }, api.schema);
		if (this.kontext.state.uvManager !== manager) {
			return;
		}
		const existing = manager.schieneGetByIdOrException(idSchiene);
		this.kontext.patchManagerDto(existing, schiene, UvSchiene, patched => manager.schienePatchAttributes(patched));
		this.kontext.commit();
	};

	/** Fügt einen erlaubten Jahrgang zu einer Schiene hinzu oder entfernt ihn. */
	public aendereSchienenJahrgang(idSchiene: number, idJahrgang: number, hinzufuegen: boolean): Promise<void> {
		return this.aendereErlaubtenJahrgang('schiene', idSchiene, idJahrgang, hinzufuegen);
	}

	/** Fügt einen erlaubten Jahrgang zu einer Schülergruppe hinzu oder entfernt ihn. */
	public aendereSchuelergruppenJahrgang(idGruppe: number, idJahrgang: number, hinzufuegen: boolean): Promise<void> {
		return this.aendereErlaubtenJahrgang('schuelergruppe', idGruppe, idJahrgang, hinzufuegen);
	}

	/** Ändert einen erlaubten Jahrgang auf Basis des zuletzt gespeicherten Stands. */
	private aendereErlaubtenJahrgang(art: 'schiene' | 'schuelergruppe', id: number, idJahrgang: number, hinzufuegen: boolean): Promise<void> {
		const idPlan = this.kontext.planungsabschnitt().id;
		return this.kontext.objektAenderungEinreihen(`${art}:${id}`, async ({ manager }) => {
			const ziel = art === 'schiene' ? manager.schieneGetByIdOrException(id) : manager.schuelergruppeGetByIdOrException(id);
			if (ziel.idPlanungsabschnitt !== idPlan || !this.kontext.state.hatKompetenzAendern) {
				throw new Error('Die Jahrgangszuordnung kann im aktuellen Planungsabschnitt nicht bearbeitet werden.');
			}
			const ids = new Set(ziel.idsJahrgaengeErlaubt);
			if (hinzufuegen === ids.has(idJahrgang)) {
				return;
			}
			if (hinzufuegen) {
				ids.add(idJahrgang);
			} else {
				ids.delete(idJahrgang);
			}
			const patch = { idsJahrgaengeErlaubt: ArrayList.of(...ids) };
			if (art === 'schiene') {
				await this.patchSchiene(id, patch);
			} else {
				await this.patchSchuelergruppe(id, patch);
			}
		});
	}

	/** Legt eine Lerngruppe im Planungsabschnitt an. */
	public createLerngruppe = async (lerngruppe: Partial<UvLerngruppe>): Promise<UvLerngruppe> => {
		const planungsabschnitt = this.kontext.planungsabschnitt();
		UvManager.lerngruppeCheckZuordnung(Object.assign(new UvLerngruppe(), lerngruppe));
		const payload = this.kontext.createPayload(lerngruppe);
		payload.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvLerngruppe(payload, api.schema);
		this.kontext.state.uvManager.lerngruppeAdd(neu);
		this.kontext.commit();
		return neu;
	};

	/** Führt die zur Zuordnungsart passende Aktion aus. */
	public addPlanungsZuordnung = async (art: UvZuordnungArt, idZiel: number, id: number): Promise<void> => {
		const plan = this.kontext.planungsabschnitt();
		if (!this.kontext.state.hatKompetenzAendern) {
			throw new Error('Der Planungsabschnitt kann derzeit nicht bearbeitet werden.');
		}
		const manager = this.kontext.state.uvManager;
		switch (art) {
			case 'lerngruppe-lehrer': {
				const gruppe = manager.lerngruppeGetByIdOrException(idZiel);
				await this.lerngruppenLehrerZuordnen(gruppe.id, id, gruppe.wochenstundenUnterrichtet, gruppe.wochenstundenUnterrichtet);
				break;
			}
			case 'lerngruppe-schiene': {
				await this.addLerngruppenSchiene({ idLerngruppe: idZiel, idSchiene: id });
				break;
			}
			case 'schiene-lerngruppe': {
				await this.addLerngruppenSchiene({ idLerngruppe: id, idSchiene: idZiel });
				break;
			}
			case 'gruppe-schueler': {
				await this.addSchuelerToSchuelergruppe(idZiel, [id]);
				break;
			}
			case 'klasse-lehrer': {
				const klasse = manager.klasseGetByIdOrException(idZiel);
				const reihenfolge = UvPlanungsManager.naechsteReihenfolge(manager.klassenLehrerGetMengeByKlasse(klasse));
				await this.addKlassenLehrer({ idKlasse: klasse.id, idLehrer: id, reihenfolge });
				break;
			}
			case 'abschnitt-zeitraster': {
				await this.addZeitrasterZuordnung(id);
				break;
			}
			case 'schiene-jahrgang': {
				await this.aendereSchienenJahrgang(idZiel, id, true);
				break;
			}
			case 'gruppe-jahrgang': {
				await this.aendereSchuelergruppenJahrgang(idZiel, id, true);
				break;
			}
			case 'zeitraster-jahrgang': {
				const raster = manager.zeitrasterGetByIdOrException(idZiel);
				const zuordnung = manager.planungsabschnittZeitrasterGetByIdOrException(plan.id, raster.id);
				const jahrgaenge = [...manager.jahrgangsdatenGetMengeByPlanungsabschnittAndZeitraster(plan, raster)];
				const jahrgang = [...manager.jahrgangsdatenGetMenge()].find(item => item.id === id);
				if (jahrgang === undefined) {
					throw new Error('Der Jahrgang ist nicht vorhanden.');
				}
				await this.patchZeitrasterZuordnung(zuordnung, [...jahrgaenge, jahrgang]);
				break;
			}
		}
	};

	/** Prüft und löscht die ausgewählten Einträge einer Planungsansicht. */
	public deletePlanungsAuswahl = async (art: string, selectedIds: number[]): Promise<void> => {
		const plan = this.kontext.planungsabschnitt();
		if (!this.kontext.state.hatKompetenzAendern) {
			throw new Error('Der Planungsabschnitt kann derzeit nicht bearbeitet werden.');
		}
		if (!istUvPlanungsAuswahlArt(art)) {
			throw new Error('Für diese Ansicht gibt es keine Sammellöschung.');
		}

		const ids = new UvPlanungsManager(this.kontext.state.uvManager, plan, this.kontext.state.regelliste).planungsLoeschauswahlPruefen(art, selectedIds);
		if (ids.size === 0) {
			return;
		}
		switch (art) {
			case 'lerngruppen': {
				await this.delLerngruppe([...this.kontext.state.uvManager.lerngruppeGetMengeByPlanungsabschnitt(plan)].filter(item => ids.has(item.id)));
				break;
			}
			case 'klassen': {
				await this.delKlasse([...this.kontext.state.uvManager.klasseGetMengeByPlanungsabschnitt(plan)].filter(item => ids.has(item.id)));
				break;
			}
			case 'schueler': {
				await this.delSchueler([...this.kontext.state.uvManager.planungsabschnittSchuelerGetMengeByPlanungsabschnitt(plan)].filter(item => ids.has(item.idSchueler)));
				break;
			}
			case 'schienen': {
				await this.delSchiene([...this.kontext.state.uvManager.schieneGetMengeByPlanungsabschnitt(plan)].filter(item => ids.has(item.id)));
				break;
			}
			default: {
				throw new Error('Für diese Ansicht gibt es keine Sammellöschung.');
			}
		}
	};

	/** Erzeugt Klassenunterrichte aus den Stundentafeln. */
	public createLerngruppenFromKlassen = async (): Promise<void> => {
		const planungsabschnitt = this.kontext.planungsabschnitt();
		const idsKlassen = new ArrayList<number>();
		for (const klasse of this.kontext.state.uvManager.klasseGetMengeByPlanungsabschnitt(planungsabschnitt)) {
			idsKlassen.add(klasse.id);
		}
		const lerngruppen = await api.server.createUvLerngruppenFromKlassen(idsKlassen, api.schema);
		this.kontext.state.uvManager.lerngruppeAddAll(lerngruppen);
		this.kontext.commit();
	};

	/** Löscht die ausgewählten Lerngruppen. */
	public delLerngruppe = async (lerngruppen: UvLerngruppe[]): Promise<void> => {
		this.kontext.planungsabschnitt();
		const ids = new ArrayList<number>();
		const listLerngruppen = new ArrayList<UvLerngruppe>();
		for (const lg of lerngruppen) {
			ids.add(lg.id);
			listLerngruppen.add(lg);
		}
		await api.server.deleteUvLerngruppen(ids, api.schema);
		this.kontext.state.uvManager.lerngruppeRemoveAll(listLerngruppen);
		lerngruppen.length = 0;
		this.kontext.commit();
	};

	/** Prüft und speichert Änderungen einer Lerngruppe in ihrer Aufrufreihenfolge. */
	public patchLerngruppe = (id: number, lerngruppe: Partial<UvLerngruppe>): Promise<void> => {
		const daten = { ...lerngruppe, id };
		return this.kontext.objektAenderungEinreihen(`lerngruppe:${id}`, async kontext => {
			const plan = this.kontext.planungsabschnitt();
			const existing = kontext.manager.lerngruppeGetByIdOrException(id);
			const result = Object.assign(new UvLerngruppe(), existing, daten);
			if (existing.idPlanungsabschnitt !== plan.id || result.idPlanungsabschnitt !== plan.id) {
				throw new Error('Die Lerngruppe darf im aktuellen Planungsabschnitt nicht bearbeitet werden.');
			}
			if ((existing.idKurs === null) !== (result.idKurs === null)) {
				throw new Error('Die Unterrichtsart einer Lerngruppe kann nach dem Erstellen nicht geändert werden.');
			}
			if (existing.idKurs !== null && result.idKurs !== existing.idKurs) {
				throw new Error('Der Kursunterricht einer Lerngruppe kann nach dem Erstellen nicht ausgetauscht werden.');
			}
			UvManager.lerngruppeCheckZuordnung(result);
			if (!new UvPlanungsManager(kontext.manager, plan, this.kontext.state.regelliste).lerngruppeStundenAenderungGueltig(result)) {
				throw new Error('Die Stunden sind ungültig oder unterschreiten einen vorhandenen Lehrkrafteinsatz.');
			}
			await api.server.patchUvLerngruppe(daten, kontext.schema);
			if (!this.kontext.istRequestKontextAktuell(kontext, false)) {
				return;
			}
			const aktuell = kontext.manager.lerngruppeGetByIdOrException(id);
			this.kontext.patchManagerDto(aktuell, daten, UvLerngruppe, patched => kontext.manager.lerngruppePatchAttributes(patched));
			this.kontext.commit();
		});
	};

	/** Speichert das Blockungsergebnis als Lehrkraftzuordnungen des Abschnitts. */
	public readonly ersetzeLehrkraftzuordnungen = async (lerngruppenLehrerList: List<Partial<UvLerngruppenLehrer>>): Promise<void> => {
		const plan = this.kontext.planungsabschnitt();
		const payload = new ArrayList<UvLerngruppenLehrerCreateRequest>();
		for (const eintrag of lerngruppenLehrerList) {
			const { idLerngruppe, idLehrer, reihenfolge, wochenstunden, wochenstundenAngerechnet } = eintrag;
			if (idLerngruppe === undefined || idLehrer === undefined || reihenfolge === undefined
				|| wochenstunden === undefined || wochenstundenAngerechnet === undefined) {
				throw new Error('Die Lehrkraftzuordnung ist unvollständig.');
			}
			const request = new UvLerngruppenLehrerCreateRequest();
			request.idPlanungsabschnitt = plan.id;
			request.idLerngruppe = idLerngruppe;
			request.idLehrer = idLehrer;
			request.reihenfolge = reihenfolge;
			request.wochenstunden = wochenstunden;
			request.wochenstundenAngerechnet = wochenstundenAngerechnet;
			payload.add(request);
		}
		const kontext = this.kontext.requestKontext();
		const neu: List<UvLerngruppenLehrer> = await api.server.replaceUvLerngruppenLehrer(payload, kontext.schema, plan.id);
		if (!this.kontext.istRequestKontextAktuell(kontext, false)) {
			return;
		}
		const alt = kontext.manager.lerngruppenLehrerGetMengeByPlanungsabschnitt(plan);
		kontext.manager.lerngruppenLehrerRemoveAll(alt);
		kontext.manager.lerngruppenLehrerAddAll(neu);
		this.kontext.commit();
	};

	/** Ordnet eine Lehrkraft an der nächsten freien Position zu. */
	public lerngruppenLehrerZuordnen = async (
		idLerngruppe: number,
		idLehrer: number,
		wochenstunden: number,
		wochenstundenAngerechnet: number
	): Promise<void> => {
		const plan = this.kontext.planungsabschnitt();
		const lerngruppe = this.kontext.state.uvManager.lerngruppeGetByIdOrException(idLerngruppe);
		if (lerngruppe.idPlanungsabschnitt !== plan.id) {
			throw new Error('Die Lerngruppe gehört nicht zu diesem Planungsabschnitt.');
		}
		const reihenfolge = UvPlanungsManager.naechsteReihenfolge(this.kontext.state.uvManager.lerngruppenLehrerGetMengeByLerngruppe(lerngruppe));
		await this.addLerngruppenLehrer({ idLerngruppe, idLehrer, reihenfolge, wochenstunden, wochenstundenAngerechnet });
	};

	/** Prüft und speichert eine Lehrkraftzuordnung. */
	public addLerngruppenLehrer = async (data: Partial<UvLerngruppenLehrer>): Promise<void> => {
		const planungsabschnitt = this.kontext.planungsabschnitt();
		const payload = this.kontext.createPayload(data);
		payload.idPlanungsabschnitt = planungsabschnitt.id;
		const entwurf = Object.assign(new UvLerngruppenLehrer(), payload);
		const gruppe = this.kontext.state.uvManager.lerngruppeGetByIdOrException(entwurf.idLerngruppe);
		const manager = new UvPlanungsManager(this.kontext.state.uvManager, planungsabschnitt, this.kontext.state.regelliste);
		if (!this.kontext.state.hatKompetenzAendern || !manager.neueLehrerZuordnungGueltig(gruppe, entwurf.idLehrer, entwurf.wochenstunden, entwurf.wochenstundenAngerechnet)
			|| !UvPlanungsManager.lehrerZuordnungswerteGueltig(entwurf.reihenfolge, entwurf.wochenstunden, gruppe.wochenstundenUnterrichtet, entwurf.wochenstundenAngerechnet)) {
			throw new Error('Die Lehrkraftzuordnung ist ungültig oder bereits vorhanden.');
		}
		const neu = await api.server.createUvLerngruppenLehrer(payload, api.schema);
		this.kontext.state.uvManager.lerngruppenLehrerAdd(neu);
		this.kontext.commit();
	};

	/** Aktualisiert eine Lehrkraftzuordnung. */
	public patchLerngruppenLehrer = async (id: number, data: Partial<UvLerngruppenLehrer>): Promise<void> => {
		await this.patchLerngruppenLehrerMultiple([{ id, data }]);
	};

	/** Prüft und speichert mehrere Lehrkraftzuordnungen gemeinsam. */
	public patchLerngruppenLehrerMultiple = async (patches: { id: number; data: Partial<UvLerngruppenLehrer> }[]): Promise<void> => {
		if (patches.length === 0) {
			return;
		}
		const manager = new UvPlanungsManager(this.kontext.state.uvManager, this.kontext.planungsabschnitt(), this.kontext.state.regelliste);
		const payload = new ArrayList<Partial<UvLerngruppenLehrer>>();
		const entwuerfe = new ArrayList<UvLerngruppenLehrer>();
		for (const { id, data } of patches) {
			const existing = this.kontext.state.uvManager.lerngruppenLehrerGetByZuordnungsIdOrException(id);
			const entwurf = Object.assign(new UvLerngruppenLehrer(), existing, data, { id });
			const gruppe = this.kontext.state.uvManager.lerngruppeGetByIdOrException(existing.idLerngruppe);
			if (!this.kontext.state.hatKompetenzAendern || !manager.lehrerZuordnungGueltig(gruppe, entwurf.idLehrer, id, entwurf.reihenfolge, entwurf.wochenstunden, entwurf.wochenstundenAngerechnet)) {
				throw new Error('Die Lehrkraft oder die Stunden der Zuordnung sind ungültig.');
			}
			payload.add({ ...data, id });
			entwuerfe.add(entwurf);
		}
		await api.server.patchUvLerngruppenLehrerMultiple(payload, api.schema);
		this.kontext.state.uvManager.lerngruppenLehrerAllPatchAttributes(entwuerfe);
		this.kontext.commit();
	};

	/** Entfernt eine Lehrkraftzuordnung aus dem aktiven Abschnitt. */
	public lerngruppenLehrerEntfernen = async (id: number): Promise<void> => {
		const plan = this.kontext.planungsabschnitt();
		if (!this.kontext.state.hatKompetenzAendern) {
			throw new Error('Der Planungsabschnitt kann derzeit nicht bearbeitet werden.');
		}
		const zuordnung = this.kontext.state.uvManager.lerngruppenLehrerGetByZuordnungsIdOrException(id);
		if (zuordnung.idPlanungsabschnitt !== plan.id) {
			throw new Error('Die Zuordnung gehört nicht zu diesem Planungsabschnitt.');
		}
		await this.delLerngruppenLehrer([zuordnung]);
	};

	/** Löscht die angegebenen Lehrkraftzuordnungen. */
	public delLerngruppenLehrer = async (lehrerList: UvLerngruppenLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		const listZuordnungen = new ArrayList<UvLerngruppenLehrer>();
		for (const lgl of lehrerList) {
			ids.add(lgl.id);
			listZuordnungen.add(this.kontext.state.uvManager.lerngruppenLehrerGetByZuordnungsIdOrException(lgl.id));
		}
		await api.server.deleteUvLerngruppenLehrerMultiple(ids, api.schema);
		this.kontext.state.uvManager.lerngruppenLehrerRemoveAll(listZuordnungen);
		lehrerList.length = 0;
		this.kontext.commit();
	};

	/** Ordnet eine Lerngruppe einer Schiene zu. */
	public addLerngruppenSchiene = async (data: Partial<UvLerngruppenSchiene>): Promise<void> => {
		const planungsabschnitt = this.kontext.planungsabschnitt();
		if ((data.idLerngruppe === undefined) || (data.idSchiene === undefined)) {
			throw new DeveloperNotificationException("Ungültige Lerngruppe-Schiene-Zuordnung.");
		}
		const payload = { ...data, idPlanungsabschnitt: planungsabschnitt.id };
		const neu = await api.server.createUvLerngruppenSchiene(payload, api.schema);
		this.kontext.state.uvManager.lerngruppenSchieneAdd(neu);
		this.kontext.commit();
	};

	/** Entfernt die angegebenen Lerngruppen-Schienen-Zuordnungen. */
	public delLerngruppenSchiene = async (schienenList: UvLerngruppenSchiene[]): Promise<void> => {
		const listZuordnungen = new ArrayList<UvLerngruppenSchiene>();
		for (const lgl of schienenList) {
			listZuordnungen.add(lgl);
		}
		await api.server.deleteUvLerngruppenSchienen(listZuordnungen, api.schema);
		this.kontext.state.uvManager.lerngruppenSchieneRemoveAll(listZuordnungen);
		schienenList.length = 0;
		this.kontext.commit();
	};

	/** Entfernt die ausgewählten Schüler aus dem Planungsabschnitt. */
	public delSchueler = async (schueler: UvPlanungsabschnittSchueler[]): Promise<void> => {
		const planungsabschnitt = this.kontext.planungsabschnitt();
		const ids = new ArrayList<number>();
		for (const s of schueler) {
			ids.add(s.idSchueler);
		}
		await api.server.deleteUvPlanungsabschnittSchuelerMultiple(ids, api.schema, planungsabschnitt.id);
		this.kontext.state.uvManager.planungsabschnittSchuelerRemoveAllById(planungsabschnitt.id, ids);
		schueler.length = 0;
		this.kontext.commit();
	};

	/** Aktualisiert die Planungsdaten eines Schülers. */
	public patchSchueler = async (idPlanungsabschnitt: number, idSchueler: number, schueler: Partial<UvPlanungsabschnittSchueler>): Promise<void> => {
		await api.server.patchUvPlanungsabschnittSchueler({ ...schueler, idSchueler, idPlanungsabschnitt }, api.schema);
		const existing = this.kontext.state.uvManager.planungsabschnittSchuelerGetByIdOrException(idPlanungsabschnitt, idSchueler);
		this.kontext.patchManagerDto(existing, schueler, UvPlanungsabschnittSchueler,
			patched => this.kontext.state.uvManager.planungsabschnittSchuelerAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Importiert Schüler und die gewählten Klassenzuweisungen. */
	public importSchueler = async (options: { idSchuljahresabschnitt: number; folgejahrgang: boolean; klassenzuweisungenUebernehmen: boolean; versetzungsvermerkeBeruecksichtigen: boolean; createMissingKlassen: boolean }): Promise<void> => {
		const planungsabschnitt = this.kontext.planungsabschnitt();
		const data = new UvSchuelerImportOptions();
		data.idSchuljahresabschnitt = options.idSchuljahresabschnitt;
		data.folgejahrgang = options.folgejahrgang;
		data.klassenzuweisungenUebernehmen = options.klassenzuweisungenUebernehmen;
		data.versetzungsvermerkeBeruecksichtigen = options.versetzungsvermerkeBeruecksichtigen;
		data.createMissingKlassen = options.createMissingKlassen;
		const planungsabschnittsdaten = await api.server.importUvPlanungsabschnittSchueler(data, api.schema, planungsabschnitt.id);
		this.kontext.state.uvManager.planungsabschnittsdatenBundleAdd(planungsabschnittsdaten);
		this.kontext.commit();
	};

	/** Legt eine Schülergruppe für eine bestehende Klasse oder einen bestehenden Kurs an. */
	public createSchuelergruppeFuerZiel = async (art: 'klassen' | 'kurse', id: number, gruppe: Partial<UvSchuelergruppe>): Promise<void> => {
		const daten = Object.assign(new UvSchuelergruppeCreateRequest(), gruppe, { idPlanungsabschnitt: this.kontext.planungsabschnitt().id });
		const result: UvPlanungsabschnittsdatenBundle = art === 'klassen'
			? await api.server.createUvSchuelergruppeFuerKlasse(daten, api.schema, id)
			: await api.server.createUvSchuelergruppeFuerKurs(daten, api.schema, id);
		this.kontext.state.uvManager.schuelergruppeAddAll(result.schuelergruppen);
		for (const klasse of result.klassen) {
			this.kontext.state.uvManager.klassePatchAttributes(klasse);
		}
		for (const kurs of result.kurse) {
			this.kontext.state.uvManager.kursPatchAttributes(kurs);
		}
		this.kontext.commit();
	};

	/** Fügt einer Schülergruppe die ausgewählten Schüler hinzu. */
	public addSchuelerToSchuelergruppe = async (idSchuelergruppe: number, idsSchueler: number[]): Promise<void> => {
		const plan = this.kontext.planungsabschnitt();
		if (!this.kontext.state.hatKompetenzAendern) {
			throw new Error('Der Planungsabschnitt kann derzeit nicht bearbeitet werden.');
		}
		const manager = this.kontext.state.uvManager;
		const gruppe = new UvPlanungsManager(manager, plan, this.kontext.state.regelliste).schuelergruppenAenderungPruefen(idSchuelergruppe, idsSchueler);
		const vorhanden = new Set([...manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe)].map(s => s.idSchueler));
		const daten = new ArrayList<Partial<UvSchuelergruppeSchueler>>();
		for (const idSchueler of new Set(idsSchueler)) {
			if (!vorhanden.has(idSchueler)) {
				daten.add({ idPlanungsabschnitt: gruppe.idPlanungsabschnitt, idSchuelergruppe, idSchueler });
			}
		}
		if (daten.isEmpty()) {
			return;
		}
		const zuordnungen = await api.server.createUvSchuelergruppeSchuelerMultiple(daten, api.schema);
		manager.schuelergruppeSchuelerAddAll(zuordnungen);
		this.kontext.commit();
	};

	/** Entfernt die ausgewählten Schüler aus einer Schülergruppe. */
	public removeSchuelerFromSchuelergruppe = async (idSchuelergruppe: number, idsSchueler: number[]): Promise<void> => {
		const plan = this.kontext.planungsabschnitt();
		if (!this.kontext.state.hatKompetenzAendern) {
			throw new Error('Der Planungsabschnitt kann derzeit nicht bearbeitet werden.');
		}
		const manager = this.kontext.state.uvManager;
		const gruppe = new UvPlanungsManager(manager, plan, this.kontext.state.regelliste).schuelergruppenAenderungPruefen(idSchuelergruppe, idsSchueler);
		const ids = new Set(idsSchueler);
		const zuordnungen = new ArrayList<UvSchuelergruppeSchueler>();
		const zuLoeschen = new ArrayList<number>();
		for (const zuordnung of manager.schuelergruppeSchuelerGetMengeBySchuelergruppe(gruppe.idPlanungsabschnitt, idSchuelergruppe)) {
			if (ids.has(zuordnung.idSchueler)) {
				zuordnungen.add(zuordnung);
				zuLoeschen.add(zuordnung.idSchueler);
			}
		}
		if (zuLoeschen.isEmpty()) {
			return;
		}
		await api.server.deleteUvSchuelergruppeSchuelerMultiple(zuLoeschen, api.schema, idSchuelergruppe);
		manager.schuelergruppeSchuelerRemoveAll(zuordnungen);
		this.kontext.commit();
	};

	/** Aktualisiert eine Schülergruppe. */
	public patchSchuelergruppe = async (id: number, schuelergruppe: Partial<UvSchuelergruppe>): Promise<void> => {
		const manager = this.kontext.state.uvManager;
		await api.server.patchUvSchuelergruppe({ ...schuelergruppe, id }, api.schema);
		if (this.kontext.state.uvManager !== manager) {
			return;
		}
		const existing = manager.schuelergruppeGetByIdOrException(id);
		this.kontext.patchManagerDto(existing, schuelergruppe, UvSchuelergruppe, patched => manager.schuelergruppePatchAttributes(patched));
		this.kontext.commit();
	};

	/** Fügt die ausgewählten Lehrkräfte zum Planungsabschnitt hinzu. */
	public addLehrerToPlanungsabschnitt = async (idsLehrer: number[]): Promise<void> => {
		const planungsabschnitt = this.kontext.planungsabschnitt();
		if (!this.kontext.state.hatKompetenzAendern) {
			throw new Error('Der Planungsabschnitt kann derzeit nicht bearbeitet werden.');
		}
		const lehrer = new UvPlanungsManager(this.kontext.state.uvManager, planungsabschnitt, this.kontext.state.regelliste).lehrerPlanungsabschnittHinzufuegenPruefen(idsLehrer);
		if (lehrer.length === 0) {
			return;
		}
		const lehrerPlanungsabschnitt = new ArrayList<UvPlanungsabschnittLehrer>();
		for (const l of lehrer) {
			const pal = new UvPlanungsabschnittLehrer();
			pal.idPlanungsabschnitt = planungsabschnitt.id;
			pal.idLehrer = l.id;
			lehrerPlanungsabschnitt.add(pal);
		}
		const listPalNeu = await api.server.createUvPlanungsabschnittLehrerMultiple(lehrerPlanungsabschnitt, api.schema);
		this.kontext.state.uvManager.planungsabschnittLehrerAddAll(listPalNeu);
		this.kontext.commit();
	};

	/** Entfernt die ausgewählten Lehrkräfte aus dem Planungsabschnitt. */
	public removeLehrerFromPlanungsabschnitt = async (idsLehrer: number[]): Promise<void> => {
		const plan = this.kontext.planungsabschnitt();
		if (!this.kontext.state.hatKompetenzAendern) {
			throw new Error('Der Planungsabschnitt kann derzeit nicht bearbeitet werden.');
		}
		const lehrer = new UvPlanungsManager(this.kontext.state.uvManager, plan, this.kontext.state.regelliste).lehrerPlanungsabschnittEntfernenPruefen(idsLehrer);
		if (lehrer.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const eintrag of lehrer) {
			ids.add(eintrag.id);
		}
		await api.server.deleteUvPlanungsabschnittLehrerMultiple(ids, api.schema, plan.id);
		this.kontext.state.uvManager.planungsabschnittLehrerRemoveAllById(plan.id, ids);
		this.kontext.commit();
	};


	/** Lädt die gültigen Abiturjahrgänge für den Kursimport. */
	public getGostAbiturjahrgaenge = async (): Promise<GostJahrgang[]> => {
		const jahrgaenge = await api.server.getGostAbiturjahrgaenge(api.schema);
		return [...jahrgaenge].filter(jahrgang => jahrgang.abiturjahr > 0);
	};

	/** Lädt die Blockungen eines Abiturjahrgangs und Halbjahrs. */
	public getGostBlockungen = async (abiturjahr: number, idHalbjahr: number): Promise<GostBlockungListeneintrag[]> => {
		return [...await api.server.getGostAbiturjahrgangBlockungsliste(api.schema, abiturjahr, idHalbjahr)];
	};

	/** Lädt die Ergebnisse einer GOST-Blockung. */
	public getGostBlockungsergebnisse = async (idBlockung: number): Promise<GostBlockungsergebnis[]> => {
		const blockung = await api.server.getGostBlockung(api.schema, idBlockung);
		return [...blockung.ergebnisse];
	};

	/** Lädt eine GOST-Blockung als Importquelle. */
	public getGostBlockung = async (idBlockung: number): Promise<GostBlockungsdaten> => {
		return await api.server.getGostBlockung(api.schema, idBlockung);
	};

	/** Lädt die Kurse des Quellabschnitts für den Import. */
	public getKurseFuerSchuljahresabschnitt = async (idSchuljahresabschnitt: number): Promise<List<KursDaten>> => {
		return await api.server.getKurseFuerAbschnitt(api.schema, idSchuljahresabschnitt);
	};

	/** Lädt belegte Klassen des ausgewählten Jahrgangs für den Import. */
	public getImportKlassen = async (idSchuljahresabschnitt: number, idJahrgang: number): Promise<KlassenListeEintrag[]> => {
		const klassen = await api.server.getListKlassenListeEintragBySchuljahresabschnitt(api.schema, idSchuljahresabschnitt);
		return [...klassen].filter(k => (k.idJahrgang === idJahrgang) && (k.anzahlZugeordneteSchueler > 0));
	};

}
