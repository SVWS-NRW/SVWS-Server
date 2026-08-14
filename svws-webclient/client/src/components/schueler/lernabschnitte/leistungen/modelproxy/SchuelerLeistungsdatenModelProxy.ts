import { ModelProxy } from "@ui";
import type { FachDaten, KursDaten, LehrerListeEintrag, List, SchuelerLeistungsdaten } from "@core";
import { ZulaessigeKursart, Note, Fach } from "@core";
import { computed } from "vue";
import type { SchuelerLernabschnittManager } from "~/components/schueler/lernabschnitte/SchuelerLernabschnittManager";

export class SchuelerLeistungsdatenModelProxy extends ModelProxy<SchuelerLeistungsdaten> {

	private readonly schuelerLernabschnittManager: () => SchuelerLernabschnittManager;
	private readonly schuljahr: () => number;

	constructor(
		data: () => SchuelerLeistungsdaten,
		schuelerLernabschnittManager: () => SchuelerLernabschnittManager,
		schuljahr: () => number,
		patchLeistung?: (data: Partial<SchuelerLeistungsdaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerLeistungsdaten> = ['kursart', 'lehrerID', 'note', 'noteQuartal'];
		super({ data, patch: patchLeistung, listOfAutopatchProps });
		this.schuelerLernabschnittManager = schuelerLernabschnittManager;
		this.schuljahr = schuljahr;

		this.validate();
	}

	fach = computed<FachDaten | undefined>({
		get: () => this.schuelerLernabschnittManager().fachGetByLeistungId(this.proxy.id) ?? undefined,
		set: (value: FachDaten | undefined) => void this.patchFach(value, this.proxy),
	});

	kurs = computed<KursDaten | undefined>({
		get: () => this.schuelerLernabschnittManager().kursGetByLeistungIdOrNull(this.proxy.id) ?? undefined,
		set: (value: KursDaten | undefined) => void this.patchKurs(value, this.proxy),
	});

	kursart = computed<ZulaessigeKursart | undefined>({
		get: () => (this.proxy.kursart === null) ? ZulaessigeKursart.PUK : ZulaessigeKursart.data().getWertByKuerzel(this.proxy.kursart) ?? undefined,
		set: (value: ZulaessigeKursart | undefined) => this.proxy.kursart = value?.daten(this.schuljahr())?.kuerzel ?? null,
	});

	lehrer = computed<LehrerListeEintrag | undefined>({
		get: () => this.schuelerLernabschnittManager().lehrerGetByLeistungIdOrNull(this.proxy.id) ?? undefined,
		set: (value: LehrerListeEintrag | undefined) => this.proxy.lehrerID = value?.id ?? null,
	});

	noteQuartal = computed<Note | undefined>({
		get: () => {
			const note = Note.fromKuerzel(this.proxy.noteQuartal);
			return (note !== Note.KEINE) ? note : undefined;
		},
		set: (value: Note | undefined) => this.proxy.noteQuartal = value?.daten(this.schuljahr())?.kuerzel ?? null,
	});

	noteHalbjahr = computed<Note | undefined>({
		get: () => {
			const note = Note.fromKuerzel(this.proxy.note);
			return (note !== Note.KEINE) ? note : undefined;
		},
		set: (value: Note | undefined) => this.proxy.note = value?.daten(this.schuljahr())?.kuerzel ?? null,
	});

	patchFach = async (fach: FachDaten | undefined, leistung: SchuelerLeistungsdaten) => {
		// Fach-Eintrag bei den Leistungsdaten wird entfernt
		if (fach === undefined) {
			this.proxy.fachID = -1;
			this.proxy.kursID = null;
			await this.patch();
			return;
		}

		// Spezialfälle
		const asdFach: Fach = Fach.getBySchluesselOrDefault(fach.kuerzelStatistik);
		if (asdFach === Fach.VX) { // Speziallfall Gymnasiale Oberstufe - Vertiefungsfach
			this.proxy.kursart = ZulaessigeKursart.VTF.daten(this.schuljahr())?.kuerzel ?? null;
		} else if (asdFach === Fach.PX) { // Speziallfall Gymnasiale Oberstufe - Projektkursfach
			this.proxy.kursart = ZulaessigeKursart.PJK.daten(this.schuljahr())?.kuerzel ?? null;
		} else { // Allgemeiner Fall: Entfernen des Kurses und setzen einer speziellen Kursart, wenn die kursart der Leistung null ist
			let kursart = (leistung.kursart === null) ? null : ZulaessigeKursart.data().getWertByKuerzel(leistung.kursart);
			if (kursart === null) {
				kursart = ZulaessigeKursart.PUK;
			}
			this.proxy.kursart = kursart.daten(this.schuljahr())?.kuerzel ?? null;
		}

		this.proxy.fachID = fach.id;
		this.proxy.kursID = null;
		await this.patch();
	};

	patchKurs = async (kurs: KursDaten | undefined, leistung: SchuelerLeistungsdaten) => {
		if (kurs === undefined) {
			this.proxy.kursID = null;
			this.proxy.kursart = ZulaessigeKursart.PUK.daten(this.schuljahr())?.kuerzel ?? null;
			this.proxy.abifach = null;
			await this.patch();
			return;
		}

		const kursart = (leistung.kursart === null) ? ZulaessigeKursart.PUK : ZulaessigeKursart.data().getWertByKuerzel(leistung.kursart);
		if (kurs.kursartAllg !== kursart?.daten(this.schuljahr())?.kuerzelAllg) {
			const { kursart, abifach } = this.bestimmeKursartUndAbifach(kurs, leistung);
			this.proxy.kursID = kurs.id;
			this.proxy.lehrerID = kurs.lehrer;
			this.proxy.kursart = kursart?.daten(this.schuljahr())?.kuerzel ?? null;
			this.proxy.abifach = abifach;
			this.proxy.wochenstunden = kurs.wochenstunden;
		} else {
			this.proxy.kursID = kurs.id;
			this.proxy.lehrerID = kurs.lehrer;
			this.proxy.wochenstunden = kurs.wochenstunden;
		}

		await this.patch();
	};

	bestimmeKursartUndAbifach = (kurs: KursDaten, leistung: SchuelerLeistungsdaten) => {
		const kursarten: List<ZulaessigeKursart> = ZulaessigeKursart.getByAllgemeinerKursart(this.schuljahr(), kurs.kursartAllg);
		let kursart: ZulaessigeKursart | null;
		let abifach: number | null = leistung.abifach;

		if (kurs.kursartAllg === ZulaessigeKursart.E.daten(this.schuljahr())?.kuerzel) { // Speziallfall Gesamtschule E-Kurs
			kursart = ZulaessigeKursart.E;
		} else if (kurs.kursartAllg === ZulaessigeKursart.G.daten(this.schuljahr())?.kuerzel) { // Speziallfall Gesamtschule G-Kurs
			kursart = ZulaessigeKursart.G;
		} else if (kurs.kursartAllg === ZulaessigeKursart.E.daten(this.schuljahr())?.kuerzelAllg) { // Spezialfall Gesamtschule DK-Kurs -> nehme G als Default
			kursart = ZulaessigeKursart.G;
		} else if (kurs.kursartAllg === ZulaessigeKursart.GKM.daten(this.schuljahr())?.kuerzelAllg) { // Spezialfall Gymnasiale Oberstufe GK -> Berücksichtige Abiturfach, Default GKM
			kursart = ZulaessigeKursart.GKM;
			if ((leistung.abifach === 1) || (leistung.abifach === 2)) {
				abifach = null;
			}
			if (leistung.abifach === 3) {
				kursart = ZulaessigeKursart.AB3;
			} else if (leistung.abifach === 4) {
				kursart = ZulaessigeKursart.AB4;
			}
		} else if (kurs.kursartAllg === ZulaessigeKursart.LK1.daten(this.schuljahr())?.kuerzelAllg) { // Spezialfall Gymnasiale Oberstufe LK -> Berücksichtige Abiturfach, Default LK1
			// TODO Prüfen, ob das Fach für LK1 zulässig ist -> wenn nicht immer LK2, ansonsten prüfen, ob LK1 bereits bei den Lernabschnittsdaten zugeordnet ist und LK2 nicht. Ist dies der Fall -> LK2, sonst LK1
			kursart = ZulaessigeKursart.LK1;
			if (leistung.abifach === 2) {
				kursart = ZulaessigeKursart.LK2;
			}
			if (leistung.abifach === null) {
				abifach = 1;
			}
		} else {
			kursart = kursarten.isEmpty() ? null : kursarten.get(0);
		}
		return { kursart, abifach };
	};
}
