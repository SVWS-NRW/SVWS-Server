import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
import { UvFach } from "@core/core/data/uv/UvFach";
import { UvLehrer } from "@core/core/data/uv/UvLehrer";
import { UvLehrerAnrechnungsstunden } from "@core/core/data/uv/UvLehrerAnrechnungsstunden";
import { UvLehrerPflichtstundensoll } from "@core/core/data/uv/UvLehrerPflichtstundensoll";
import { UvRaum } from "@core/core/data/uv/UvRaum";
import { UvRaumgruppe } from "@core/core/data/uv/UvRaumgruppe";
import { UvStundentafel } from "@core/core/data/uv/UvStundentafel";
import { UvStundentafelFach } from "@core/core/data/uv/UvStundentafelFach";
import { UvStundentafelImportOptions } from "@core/core/data/uv/UvStundentafelImportOptions";
import { UvZeitraster } from "@core/core/data/uv/UvZeitraster";
import { UvZeitrasterEintrag } from "@core/core/data/uv/UvZeitrasterEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ListUtils } from "@core/core/utils/ListUtils";
import { ArrayList } from "@core/java/util/ArrayList";
import type { UvState } from "@ui/states/UvState";

import { uvFachGueltigkeitValidator } from "~/components/unterrichtsverteilung/UvGueltigkeitValidator";
import { api } from "~/router/Api";

import type { UvStateAktionskontext } from "./UvStateImpl";

type UvGrunddatenAktionen = UvState['grunddaten'];

export class UvGrunddatenStateAktionen implements UvGrunddatenAktionen {

	public constructor(
		private readonly kontext: UvStateAktionskontext,
		private readonly removeLehrerFromPlanungsabschnitt: (idsLehrer: number[]) => Promise<void>
	) {
	}

	/** Löscht die ausgewählten UV-Fächer. */
	public delFaecher = async (faecher: UvFach[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const f of faecher) {
			ids.add(f.id);
		}
		await api.server.deleteUvFaecher(ids, api.schema);
		this.kontext.state.uvManager.fachRemoveAllById(ids);
		faecher.length = 0;
		this.kontext.commit();
	};

	/** Prüft und speichert die Änderungen eines UV-Fachs. */
	public patchFach = async (id: number, fach: Partial<UvFach>): Promise<void> => {
		const fachAlt = this.kontext.state.uvManager.fachGetByIdOrException(id);
		this.pruefeFachGueltigkeit(Object.assign(new UvFach(), fachAlt, fach, { id }));
		await api.server.patchUvFach({ ...fach, id }, api.schema);
		this.kontext.patchManagerDto(fachAlt, fach, UvFach, patched => this.kontext.state.uvManager.fachPatchAttributes(patched));
		this.kontext.commit();
	};

	/** Prüft die Gültigkeit eines Fachs gegen den Bestand. */
	private pruefeFachGueltigkeit(fach: UvFach): void {
		const validator = uvFachGueltigkeitValidator(this.kontext.state.uvManager, fach);
		if (!validator.run()) {
			throw new Error(validator.getFehler().getFirst().getFehlermeldung() ?? 'Ungültiger Gültigkeitszeitraum des Fachs.');
		}
	}

	/** Ermittelt das heutige Datum im ISO-Format. */
	private todayISO(): string {
		return new Date().toISOString().slice(0, 10);
	}

	/** Ermittelt den nächsten Kalendertag eines ISO-Datums. */
	private addOneDayISO(dateISO: string): string {
		const d = new Date(dateISO);
		d.setDate(d.getDate() + 1);
		return d.toISOString().slice(0, 10);
	}

	/** Übernimmt ausgewählte Schulfächer in die UV-Grunddaten. */
	public addFaecher = async (fachdaten: FachDaten[]): Promise<void> => {
		const faecherNeu = new ArrayList<Partial<UvFach>>();
		const ids = new Set<number>();
		const heute = this.todayISO();
		for (const fachDaten of fachdaten) {
			if (ids.has(fachDaten.id)) {
				throw new Error('Dasselbe Fach darf nicht mehrfach gleichzeitig importiert werden.');
			}
			ids.add(fachDaten.id);
			const pFach = Object.assign(new UvFach(), { idFach: fachDaten.id });
			const faecherExistierend = this.kontext.state.uvManager.fachGetMengeByFachdaten(fachDaten);
			if (faecherExistierend.isEmpty() === true) {
				pFach.gueltigVon = heute;
			} else {
				const gueltigBis = faecherExistierend.getLast().gueltigBis;
				pFach.gueltigVon = (gueltigBis === null || gueltigBis < heute) ? heute : this.addOneDayISO(gueltigBis);
			}
			this.pruefeFachGueltigkeit(pFach);
			faecherNeu.add({ idFach: pFach.idFach, gueltigVon: pFach.gueltigVon, gueltigBis: pFach.gueltigBis });
		}
		const listFaecherNeu = await api.server.createUvFaecher(faecherNeu, api.schema);
		this.kontext.state.uvManager.fachAddAll(listFaecherNeu);
		this.kontext.commit();
	};

	/** Löscht die ausgewählten Räume. */
	public delRaeume = async (raeume: UvRaum[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const r of raeume) {
			ids.add(r.id);
		}
		await api.server.deleteUvRaeume(ids, api.schema);
		this.kontext.state.uvManager.raumRemoveAllById(ids);
		raeume.length = 0;
		this.kontext.commit();
	};

	/** Legt einen Raum an. */
	public createRaum = async (raum: Partial<UvRaum>): Promise<void> => {
		const payload = this.kontext.createPayload(raum);
		const neu = await api.server.createUvRaum(payload, api.schema);
		this.kontext.state.uvManager.raumAdd(neu);
		this.kontext.commit();
	};

	/** Aktualisiert einen Raum. */
	public patchRaum = async (id: number, raum: Partial<UvRaum>): Promise<void> => {
		await api.server.patchUvRaum({ ...raum, id }, api.schema);
		const raumAlt = this.kontext.state.uvManager.raumGetByIdOrException(id);
		this.kontext.patchManagerDto(raumAlt, raum, UvRaum, patched => this.kontext.state.uvManager.raumAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Importiert Räume aus einer JSON-Datei. */
	public setUvRaeumeImportJSON = async (formData: FormData): Promise<void> => {
		await api.call(async (data: FormData) => {
			const jsonFile = data.get("data");
			if (!(jsonFile instanceof File)) {
				return;
			}
			const json = await jsonFile.text();
			const raeume: Partial<UvRaum>[] = JSON.parse(json);
			const list = new ArrayList<Partial<UvRaum>>();
			for (const item of raeume) {
				if ((item.kuerzel !== undefined) && (this.kontext.state.uvManager.raumGetByKuerzelOrNull(item.kuerzel) === null)) {
					delete item.id;
					item.gueltigVon ??= this.todayISO();
					list.add(item);
				}
			}
			if (list.isEmpty()) {
				return;
			}
			const res = await api.server.createUvRaeume(list, api.schema);
			this.kontext.state.uvManager.raumAddAll(res);
			this.kontext.commit();
		})(formData);
	};

	/** Löscht die ausgewählten Raumgruppen. */
	public delRaumgruppen = async (raumgruppen: UvRaumgruppe[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const r of raumgruppen) {
			ids.add(r.id);
		}
		await api.server.deleteUvRaumgruppen(ids, api.schema);
		for (const r of raumgruppen) {
			this.kontext.state.uvManager.raumgruppeRemoveById(r.id);
		}
		raumgruppen.length = 0;
		this.kontext.commit();
	};

	/** Legt eine Raumgruppe an. */
	public createRaumgruppe = async (raumgruppe: Partial<UvRaumgruppe>): Promise<void> => {
		const payload = this.kontext.createPayload(raumgruppe);
		const neu = await api.server.createUvRaumgruppe(payload, api.schema);
		this.kontext.state.uvManager.raumgruppeAdd(neu);
		this.kontext.commit();
	};

	/** Aktualisiert eine Raumgruppe. */
	public patchRaumgruppe = async (id: number, raumgruppe: Partial<UvRaumgruppe>): Promise<void> => {
		await api.server.patchUvRaumgruppe({ ...raumgruppe, id }, api.schema);
		this.kontext.patchManagerDto(this.kontext.state.uvManager.raumgruppeGetByIdOrException(id), raumgruppe, UvRaumgruppe,
			patched => this.kontext.state.uvManager.raumgruppeAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Legt eine Stundentafel an. */
	public createStundentafel = async (stundentafel: Partial<UvStundentafel>): Promise<void> => {
		const payload = this.kontext.createPayload(stundentafel);
		const neu = await api.server.createUvStundentafel(payload, api.schema);
		this.kontext.state.uvManager.stundentafelAdd(neu);
		this.kontext.commit();
	};

	/** Importiert eine Stundentafel aus einer Klasse. */
	public importStundentafel = async (options: Partial<UvStundentafel> & { schuljahr: number; idKlasse: number; fehlendeUvFaecherAnlegen: boolean }): Promise<void> => {
		const request = Object.assign(new UvStundentafelImportOptions(), options);
		const daten = await api.server.importUvStundentafel(request, api.schema);
		this.kontext.state.uvManager.grunddatenBundleAdd(daten);
		this.kontext.commit();
	};

	/** Löscht die ausgewählten Stundentafeln. */
	public delStundentafel = async (stundentafeln: UvStundentafel[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const s of stundentafeln) {
			ids.add(s.id);
		}
		await api.server.deleteUvStundentafeln(ids, api.schema);
		this.kontext.state.uvManager.stundentafelRemoveAllById(ids);
		stundentafeln.length = 0;
		this.kontext.commit();
	};

	/** Aktualisiert eine Stundentafel. */
	public patchStundentafel = async (id: number, stundentafel: Partial<UvStundentafel>): Promise<void> => {
		await api.server.patchUvStundentafel({ ...stundentafel, id }, api.schema);
		this.kontext.patchManagerDto(this.kontext.state.uvManager.stundentafelGetByIdOrException(id), stundentafel, UvStundentafel,
			patched => this.kontext.state.uvManager.stundentafelAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Fügt einer Stundentafel ein Fach hinzu. */
	public addStundentafelFach = async (fach: Partial<UvStundentafelFach>): Promise<void> => {
		const payload = this.kontext.createPayload(fach);
		const neu = await api.server.createUvStundentafelFach(payload, api.schema);
		this.kontext.state.uvManager.stundentafelFachAdd(neu);
		this.kontext.commit();
	};

	/** Aktualisiert einen Facheintrag der Stundentafel. */
	public patchStundentafelFach = async (id: number, fach: Partial<UvStundentafelFach>): Promise<void> => {
		await api.server.patchUvStundentafelFach({ ...fach, id }, api.schema);
		this.kontext.patchManagerDto(this.kontext.state.uvManager.stundentafelFachGetByIdOrException(id), fach, UvStundentafelFach,
			patched => this.kontext.state.uvManager.stundentafelFachAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Entfernt die ausgewählten Facheinträge aus der Stundentafel. */
	public delStundentafelFach = async (faecher: UvStundentafelFach[]): Promise<void> => {
		const ids = new ArrayList<number>();
		const listFaecher = new ArrayList<UvStundentafelFach>();
		for (const f of faecher) {
			ids.add(f.id);
			listFaecher.add(this.kontext.state.uvManager.stundentafelFachGetByIdOrException(f.id));
		}
		await api.server.deleteUvStundentafelFaecher(ids, api.schema);
		this.kontext.state.uvManager.stundentafelFachRemoveAll(listFaecher);
		faecher.length = 0;
		this.kontext.commit();
	};

	/** Legt ein Zeitraster an. */
	public addZeitraster = async (data: Partial<UvZeitraster>): Promise<UvZeitraster> => {
		const neu = await api.server.createUvZeitraster(data, api.schema);
		this.kontext.state.uvManager.zeitrasterAdd(neu);
		this.kontext.commit();
		return neu;
	};

	/** Aktualisiert ein Zeitraster. */
	public patchZeitraster = async (id: number, data: Partial<UvZeitraster>): Promise<void> => {
		await api.server.patchUvZeitraster({ ...data, id }, api.schema);
		const zeitraster = this.kontext.state.uvManager.zeitrasterGetByIdOrNull(id);
		if (zeitraster !== null) {
			this.kontext.patchManagerDto(zeitraster, data, UvZeitraster, patched => this.kontext.state.uvManager.zeitrasterAllPatchAttributes(ListUtils.create1(patched)));
		}
		this.kontext.commit();
	};

	/** Löscht die ausgewählten Zeitraster. */
	public deleteZeitraster = async (ids: number[]): Promise<void> => {
		const listIds = new ArrayList<number>();
		for (const id of ids) {
			listIds.add(id);
		}
		await api.server.deleteUvZeitrasterMultiple(listIds, api.schema);
		this.kontext.state.uvManager.zeitrasterRemoveAllById(listIds);
		this.kontext.commit();
	};

	/** Fügt einem Zeitraster einen Eintrag hinzu. */
	public addZeitrasterEintrag = async (data: Partial<UvZeitrasterEintrag>): Promise<UvZeitrasterEintrag> => {
		const neu = await api.server.createUvZeitrasterEintrag(data, api.schema);
		this.kontext.state.uvManager.zeitrasterEintragAdd(neu);
		this.kontext.commit();
		return neu;
	};

	/** Fügt mehrere Einträge aus einem Zeitrasterentwurf hinzu. */
	public addZeitrasterEintraege = async (eintraege: Partial<UvZeitrasterEintrag>[]): Promise<UvZeitrasterEintrag[]> => {
		const listData = new ArrayList<Partial<UvZeitrasterEintrag>>();
		for (const e of eintraege) {
			listData.add({ idZeitraster: e.idZeitraster, wochentag: e.wochentag, stunde: e.stunde, beginn: e.beginn, ende: e.ende });
		}
		const neueEintraege = await api.server.createUvZeitrasterEintraege(listData, api.schema);
		this.kontext.state.uvManager.zeitrasterEintragAddAll(neueEintraege);
		this.kontext.commit();
		return [...neueEintraege];
	};

	/** Aktualisiert einen Zeitrastereintrag. */
	public patchZeitrasterEintrag = async (idZeitraster: number, id: number, data: Partial<UvZeitrasterEintrag>): Promise<void> => {
		await api.server.patchUvZeitrasterEintrag({ ...data, id }, api.schema);
		const eintrag = this.kontext.state.uvManager.zeitrasterEintragGetByIdOrException(idZeitraster, id);
		this.kontext.patchManagerDto(eintrag, data, UvZeitrasterEintrag, patched => this.kontext.state.uvManager.zeitrasterEintragAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Löscht die ausgewählten Einträge eines Zeitrasters. */
	public deleteZeitrasterEintraege = async (idZeitraster: number, ids: number[]): Promise<void> => {
		const listIds = new ArrayList<number>();
		for (const id of ids) {
			listIds.add(id);
		}
		await api.server.deleteUvZeitrasterEintraege(listIds, api.schema);
		for (const id of ids) {
			const eintrag = this.kontext.state.uvManager.zeitrasterEintragGetByIdOrException(idZeitraster, id);
			this.kontext.state.uvManager.zeitrasterEintragRemove(eintrag);
		}
		this.kontext.commit();
	};

	/** Löscht die ausgewählten Lehrkräfte aus den UV-Grunddaten. */
	public delLehrer = async (lehrer: UvLehrer[]): Promise<void> => {
		const plan = this.kontext.state.planungsabschnitt;
		if (plan !== null && plan.id !== -1) {
			await this.removeLehrerFromPlanungsabschnitt(lehrer.map(item => item.id));
			lehrer.length = 0;
			return;
		}
		const verwendet = new Set([...this.kontext.state.uvManager.lehrerGetMengeVerwendetByLehrerMenge(this.kontext.state.uvManager.lehrerGetMengeAsList())].map(item => item.id));
		if (lehrer.some(item => verwendet.has(item.id))) {
			throw new DeveloperNotificationException('Die Auswahl enthält verwendete Lehrkräfte.');
		}
		const ids = new ArrayList<number>();
		for (const l of lehrer) {
			ids.add(l.id);
		}
		const deleted = await api.server.deleteUvLehrerMultiple(ids, api.schema);
		this.kontext.state.uvManager.lehrerRemoveAll(deleted);
		lehrer.length = 0;
		this.kontext.commit();
	};


	/** Löscht das Pflichtstundensoll der ausgewählten Lehrkräfte. */
	public delPflichtstundensollBeiLehrern = async (lehrer: UvLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const l of lehrer) {
			for (const p of this.kontext.state.uvManager.lehrerPflichtstundensollGetMengeByLehrer(l)) {
				ids.add(p.id);
			}
		}
		if (ids.isEmpty()) {
			return;
		}
		const deleted = await api.server.deleteUvLehrerPflichtstundensollMultiple(ids, api.schema);
		this.kontext.state.uvManager.lehrerPflichtstundensollRemoveAll(deleted);
		this.kontext.commit();
	};

	/** Übernimmt das Pflichtstundensoll der ausgewählten Lehrkräfte. */
	public importPflichtstundensollBeiLehrern = async (lehrer: UvLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const l of lehrer) {
			if ((l.idKLehrer !== null) && (this.kontext.state.uvManager.lehrerPflichtstundensollGetMengeByLehrer(l).isEmpty() === true)) {
				ids.add(l.id);
			}
		}
		if (ids.isEmpty()) {
			return;
		}
		const neu = await api.server.importUvLehrerPflichtstundensollFromPersonalabschnittsdaten(ids, api.schema);
		this.kontext.state.uvManager.lehrerPflichtstundensollAddAll(neu);
		this.kontext.commit();
	};

	/** Löscht die Anrechnungsstunden der ausgewählten Lehrkräfte. */
	public delAnrechnungsstundenBeiLehrern = async (lehrer: UvLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const l of lehrer) {
			for (const a of this.kontext.state.uvManager.lehrerAnrechnungsstundenGetMengeByLehrer(l)) {
				ids.add(a.id);
			}
		}
		if (ids.isEmpty()) {
			return;
		}
		const deleted = await api.server.deleteUvLehrerAnrechnungsstunden(ids, api.schema);
		this.kontext.state.uvManager.lehrerAnrechnungsstundenRemoveAll(deleted);
		this.kontext.commit();
	};

	/** Übernimmt die Anrechnungsstunden der ausgewählten Lehrkräfte. */
	public importAnrechnungsstundenBeiLehrern = async (lehrer: UvLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const l of lehrer) {
			if ((l.idKLehrer !== null) && (this.kontext.state.uvManager.lehrerAnrechnungsstundenGetMengeByLehrer(l).isEmpty() === true)) {
				ids.add(l.id);
			}
		}
		if (ids.isEmpty()) {
			return;
		}
		const neu = await api.server.importUvLehrerAnrechnungsstundenFromPersonalabschnittsdaten(ids, api.schema);
		this.kontext.state.uvManager.lehrerAnrechnungsstundenAddAll(neu);
		this.kontext.commit();
	};

	/** Legt eine UV-Lehrkraft an. */
	public createLehrer = async (lehrer: Partial<UvLehrer>): Promise<void> => {
		const payload = this.kontext.createPayload(lehrer);
		const neu = await api.server.createUvLehrer(payload, api.schema);
		this.kontext.state.uvManager.lehrerAdd(neu);
		this.kontext.commit();
	};

	/** Aktualisiert die Grunddaten einer UV-Lehrkraft. */
	public patchLehrer = async (id: number, lehrer: Partial<UvLehrer>): Promise<void> => {
		await api.server.patchUvLehrer({ ...lehrer, id }, api.schema);
		this.kontext.patchManagerDto(this.kontext.state.uvManager.lehrerGetByIdOrException(id), lehrer, UvLehrer,
			patched => this.kontext.state.uvManager.lehrerAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Legt einen Anrechnungseintrag an. */
	public addAnrechnungsstunden = async (anrechnung: Partial<UvLehrerAnrechnungsstunden>): Promise<void> => {
		const payload = this.kontext.createPayload(anrechnung);
		if (payload.gueltigBis?.trim().length === 0) {
			payload.gueltigBis = null;
		}
		const neu = await api.server.createUvLehrerAnrechnungsstunde(payload, api.schema);
		this.kontext.state.uvManager.lehrerAnrechnungsstundenAdd(neu);
		this.kontext.commit();
	};

	/** Übernimmt Anrechnungsstunden aus den Personalabschnittsdaten. */
	public importAnrechnungsstundenFromPersonalabschnittsdaten = async (lehrer: UvLehrer): Promise<void> => {
		const neu = await api.server.importUvLehrerAnrechnungsstundenFromPersonalabschnittsdaten(ListUtils.create1(lehrer.id), api.schema);
		this.kontext.state.uvManager.lehrerAnrechnungsstundenAddAll(neu);
		this.kontext.commit();
	};

	/** Aktualisiert einen Anrechnungseintrag. */
	public patchAnrechnungsstunde = async (id: number, anrechnung: Partial<UvLehrerAnrechnungsstunden>): Promise<void> => {
		await api.server.patchUvLehrerAnrechnungsstunde({ ...anrechnung, id }, api.schema);
		this.kontext.patchManagerDto(this.kontext.state.uvManager.lehrerAnrechnungsstundenGetByIdOrException(id), anrechnung, UvLehrerAnrechnungsstunden,
			patched => this.kontext.state.uvManager.lehrerAnrechnungsstundenAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Löscht die ausgewählten Anrechnungseinträge. */
	public delAnrechnungsstunden = async (anrechnungen: UvLehrerAnrechnungsstunden[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const a of anrechnungen) {
			ids.add(a.id);
		}
		const deleted = await api.server.deleteUvLehrerAnrechnungsstunden(ids, api.schema);
		this.kontext.state.uvManager.lehrerAnrechnungsstundenRemoveAll(deleted);
		anrechnungen.length = 0;
		this.kontext.commit();
	};

	/** Legt einen Pflichtstundeneintrag an. */
	public addPflichtstundensoll = async (pflichtstundensoll: Partial<UvLehrerPflichtstundensoll>): Promise<void> => {
		const payload = this.kontext.createPayload(pflichtstundensoll);
		if (payload.gueltigBis?.trim().length === 0) {
			payload.gueltigBis = null;
		}
		const neu = await api.server.createUvLehrerPflichtstundensoll(payload, api.schema);
		this.kontext.state.uvManager.lehrerPflichtstundensollAdd(neu);
		this.kontext.commit();
	};

	/** Übernimmt Pflichtstunden aus den Personalabschnittsdaten. */
	public importPflichtstundensollFromPersonalabschnittsdaten = async (lehrer: UvLehrer): Promise<void> => {
		const neu = await api.server.importUvLehrerPflichtstundensollFromPersonalabschnittsdaten(ListUtils.create1(lehrer.id), api.schema);
		this.kontext.state.uvManager.lehrerPflichtstundensollAddAll(neu);
		this.kontext.commit();
	};

	/** Aktualisiert einen Pflichtstundeneintrag. */
	public patchPflichtstundensoll = async (id: number, pflichtstundensoll: Partial<UvLehrerPflichtstundensoll>): Promise<void> => {
		await api.server.patchUvLehrerPflichtstundensoll({ ...pflichtstundensoll, id }, api.schema);
		this.kontext.patchManagerDto(this.kontext.state.uvManager.lehrerPflichtstundensollGetByIdOrException(id), pflichtstundensoll, UvLehrerPflichtstundensoll,
			patched => this.kontext.state.uvManager.lehrerPflichtstundensollAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Löscht die ausgewählten Pflichtstundeneinträge. */
	public delPflichtstundensoll = async (pflichtstundensoll: UvLehrerPflichtstundensoll[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const p of pflichtstundensoll) {
			ids.add(p.id);
		}
		const deleted = await api.server.deleteUvLehrerPflichtstundensollMultiple(ids, api.schema);
		this.kontext.state.uvManager.lehrerPflichtstundensollRemoveAll(deleted);
		pflichtstundensoll.length = 0;
		this.kontext.commit();
	};

	/** Fügt einer Lehrkraft ein Unterrichtsfach hinzu. */
	public addLehrerUnterrichtsfach = async (lehrer: UvLehrer, eintrag: Partial<LehrerUnterrichtsfach>): Promise<void> => {
		const payload = { ...eintrag, idLehrer: lehrer.idKLehrer ?? lehrer.id };
		delete payload.id;
		const fachNeu = lehrer.idKLehrer === null ? await api.server.createUvLehrerUnterrichtsfach(payload, api.schema) : await api.server.addLehrerUnterrichtsfach(payload, api.schema);
		this.kontext.state.uvManager.lehrerUnterrichtsfachAdd(fachNeu);
		this.kontext.commit();
	};

	/** Aktualisiert ein Unterrichtsfach einer Lehrkraft. */
	public patchLehrerUnterrichtsfach = async (eintrag: LehrerUnterrichtsfach, patch: Partial<LehrerUnterrichtsfach>): Promise<void> => {
		if (eintrag.istKLehrer) {
			await api.server.patchLehrerUnterrichtsfach(patch, api.schema, eintrag.id);
		} else {
			await api.server.patchUvLehrerUnterrichtsfach({ id: eintrag.id, ...patch }, api.schema);
		}
		const existing = this.kontext.state.uvManager.lehrerUnterrichtsfachGetByIdOrException(eintrag.istKLehrer, eintrag.id);
		this.kontext.patchManagerDto(existing, patch, LehrerUnterrichtsfach,
			patched => this.kontext.state.uvManager.lehrerUnterrichtsfachAllPatchAttributes(ListUtils.create1(patched)));
		this.kontext.commit();
	};

	/** Entfernt ein Unterrichtsfach einer Lehrkraft. */
	public removeLehrerUnterrichtsfach = async (eintrag: LehrerUnterrichtsfach): Promise<void> => {
		if (eintrag.istKLehrer) {
			await api.server.deleteLehrerUnterrichtsfach(api.schema, eintrag.id);
		} else {
			await api.server.deleteUvLehrerUnterrichtsfach(api.schema, eintrag.id);
		}
		this.kontext.state.uvManager.lehrerUnterrichtsfachRemove(eintrag);
		this.kontext.commit();
	};

	/** Lädt die noch nicht importierten Lehrkräfte der Schule. */
	public getSchulLehrer = async (): Promise<LehrerListeEintrag[]> => {
		const listLehrer = await api.server.getLehrer(api.schema);
		const bereitsImportiert = new Set(this.kontext.state.uvManager.lehrerGetMengeAsList().toArray(new Array<UvLehrer>()).map(l => l.idKLehrer));
		return listLehrer.toArray(new Array<LehrerListeEintrag>()).filter(l => !bereitsImportiert.has(l.id));
	};

	/** Übernimmt ausgewählte Lehrkräfte aus der Schule in die UV-Grunddaten. */
	public importLehrerFromSchule = async (lehrer: LehrerListeEintrag[]): Promise<void> => {
		const list = new ArrayList<Partial<UvLehrer>>();
		for (const l of lehrer) {
			list.add({ idKLehrer: l.id });
		}
		const created = await api.server.createUvLehrerMultiple(list, api.schema);
		this.kontext.state.uvManager.lehrerAddAll(created);
		this.kontext.commit();
	};

}
