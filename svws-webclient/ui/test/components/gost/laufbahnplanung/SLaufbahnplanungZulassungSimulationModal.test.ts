import { flushPromises, mount } from "@vue/test-utils";
import { readdirSync, readFileSync } from "node:fs";
import { resolve } from "node:path";
import { gunzipSync } from "node:zlib";
import { beforeAll, describe, expect, test, vi } from "vitest";

import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";
import { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import { GostBelegpruefungsArt } from "@core/core/abschluss/gost/GostBelegpruefungsArt";
import { Abiturdaten } from "@core/core/data/gost/Abiturdaten";
import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
import { GostZulassungSimulation } from "@core/core/data/gost/GostZulassungSimulation";
import type { GostZulassungSimulationAbiturfach } from "@core/core/data/gost/GostZulassungSimulationAbiturfach";
import type { GostZulassungSimulationNote } from "@core/core/data/gost/GostZulassungSimulationNote";
import { GostLaufbahnplanungExportV1 } from "@core/core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1";
import { GostLaufbahnplanungExportV2 } from "@core/core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import { GostLaufbahnplanungDataHandler } from "@core/core/utils/gost/GostLaufbahnplanungDataHandler";
import type { List } from "@core/java/util/List";
import SLaufbahnplanungZulassungSimulationModal from "@ui/components/gost/laufbahnplanung/SLaufbahnplanungZulassungSimulationModal.vue";
import SLaufbahnplanungZulassungSimulationNoten from "@ui/components/gost/laufbahnplanung/SLaufbahnplanungZulassungSimulationNoten.vue";
import SLaufbahnplanungZulassungSimulationPruefung from "@ui/components/gost/laufbahnplanung/SLaufbahnplanungZulassungSimulationPruefung.vue";
import UiSelect from "@ui/ui/controls/select/UiSelect.vue";

const pfad2030 = resolve(import.meta.dirname, "../../../../../../svws-core/src/test/resources/de/svws_nrw/core/abschluss/gost/belegpruefung/abi2030");

const state = vi.hoisted(() => ({
	abiturdatenManager: null as unknown,
	getZulassungSimulation: vi.fn(),
	setZulassungSimulation: vi.fn(),
}));

vi.mock("@ui/states/GostLaufbahnplanungState", () => ({
	useGostLaufbahnplanungState: () => state,
}));

function ladeManager(): AbiturdatenManager {
	const datei = readdirSync(pfad2030).find(name => name.includes("_Belegung")) ?? "";
	const json = gunzipSync(readFileSync(resolve(pfad2030, datei))).toString("utf8");
	const handler = (JSON.parse(json).lpRevision === 1)
		? GostLaufbahnplanungDataHandler.importV1(GostLaufbahnplanungExportV1.transpilerFromJSON(json))
		: GostLaufbahnplanungDataHandler.importV2(GostLaufbahnplanungExportV2.transpilerFromJSON(json));
	return new AbiturdatenManager(handler.getAbiturdaten(), handler.getGostJahrgangsdaten(), handler.getFaecherManager(), GostBelegpruefungsArt.GESAMT);
}

function mounten(hatUpdateKompetenz = true, vorbereiten?: (manager: AbiturdatenManager) => void) {
	const manager = ladeManager();
	vorbereiten?.(manager);
	state.abiturdatenManager = manager;
	return mount(SLaufbahnplanungZulassungSimulationModal, {
		props: { show: true, hatUpdateKompetenz },
		global: { stubs: { SvwsUiModal: { template: "<div><h1><slot name='modalTitle' /></h1><p class='beschreibung'><slot name='modalDescription' /></p><slot name='modalContent' /><div class='aktionen'><slot name='modalActions' /></div></div>" } } },
	});
}

function findeKnopf(wrapper: ReturnType<typeof mounten>, text: string) {
	const knopf = wrapper.findAll("button").find(b => b.text().includes(text));
	if (knopf === undefined) {
		throw new Error("Knopf nicht gefunden: " + text);
	}
	return knopf;
}

function findeTabelle(wrapper: ReturnType<typeof mounten>, name: string) {
	const tabelle = wrapper.findAll("table").find(t => t.attributes("aria-label") === name);
	if (tabelle === undefined) {
		throw new Error("Tabelle nicht gefunden: " + name);
	}
	return tabelle;
}

function pruefeHintergrund(tabelle: ReturnType<typeof findeTabelle>) {
	for (const zelle of tabelle.findAll("td")) {
		const istEingabe = zelle.classes().includes("ui-table-grid-input") || zelle.classes().includes("ui-table-grid-button");
		expect(zelle.classes().includes("bg-ui-neutral")).toBe(istEingabe);
	}
}

function auswahlProps(auswahl: { props: () => unknown }) {
	return auswahl.props() as { label: string; modelValue: { label: string } | null };
}

function auswahlOptionen<T>(auswahl: { props: () => unknown }): T[] {
	return (auswahl.props() as { manager: { filteredOptions: List<T> } }).manager.filteredOptions.toArray(new Array<T>());
}

function ersteBelegungQ11(manager: AbiturdatenManager) {
	const belegung = manager.daten().fachbelegungen.toArray(new Array<AbiturFachbelegung>())
		.map(fach => fach.belegungen[GostHalbjahr.Q11.id]).find(b => b !== null);
	if (belegung === undefined) {
		throw new Error("Der Testfall hat keine Belegung in der Q1.1");
	}
	return belegung;
}

function ersteBelegungAlsVertiefungskurs(manager: AbiturdatenManager) {
	const belegung = ersteBelegungQ11(manager);
	for (const fachbelegung of manager.daten().fachbelegungen) {
		const fach = manager.faecher().get(fachbelegung.fachID);
		if ((fachbelegung.belegungen[GostHalbjahr.Q11.id] === belegung) && (fach !== null)) {
			fach.kuerzel = "VX";
		}
	}
	return belegung;
}

function vorAbitur2030(manager: AbiturdatenManager) {
	manager.daten().abiturjahr = 2029;
}

async function importiere(wrapper: ReturnType<typeof mounten>, inhalt: string) {
	const eingabe = wrapper.find<HTMLInputElement>("input[type=file]");
	Object.defineProperty(eingabe.element, "files", { value: [new File([inhalt], "stand.json")], configurable: true });
	await eingabe.trigger("change");
	await flushPromises();
}

beforeAll(() => {
	new JsonCoreTypeReaderStatic().readAll();
});

describe("SLaufbahnplanungZulassungSimulationModal", () => {

	test("Zeigt Noteneingabe, Prüfungseingabe und Zulassungstabelle", () => {
		const wrapper = mounten();

		const tabellen = wrapper.findAll("table").map(t => t.attributes("aria-label"));

		expect(tabellen).toContain("Eingabe der Notenpunkte in der Qualifikationsphase");
		expect(tabellen).toContain("Eingabe der Prüfungsergebnisse");
		expect(tabellen).toContain("Übersicht über die Prüfungsergebnisse");
	});

	test("Die Überschrift nennt den Hinweis, aber weder Abiturjahr noch einen weiteren Beschreibungstext", () => {
		const wrapper = mounten();

		expect(wrapper.find("h1").text()).toBe("Simulation der Abiturzulassung – Noteneingaben werden nicht übernommen");
		expect(wrapper.find(".beschreibung").text()).toBe("");
	});

	test("Jede Tabelle hat eine Überschrift", () => {
		const ueberschriften = mounten().findAll(".text-headline-md").map(u => u.text());

		expect(ueberschriften).toEqual(["Noteneingabe Block I", "Noteneingabe Block II", "Zulassungsberechnung"]);
	});

	test("Die Knöpfe stehen in der Leiste, unten gibt es keine Knöpfe", () => {
		const wrapper = mounten();

		const leiste = wrapper.find(".svws-ui-sub-nav").findAll("button").map(b => b.text());

		expect(leiste).toEqual(["Noteneingabe fixiert", "Sichern", "Wiederherstellen", "Leeren", "Exportieren", "Importieren…"]);
		expect(wrapper.find(".aktionen").findAll("button")).toHaveLength(0);
	});

	test("Der Knopf für die Fixierung wechselt seinen Text und ersetzt die Checkbox", async () => {
		const wrapper = mounten();
		expect(wrapper.find("input[type=checkbox]").exists()).toBe(false);

		await findeKnopf(wrapper, "Noteneingabe fixiert").trigger("click");
		expect(findeKnopf(wrapper, "Noteneingabe frei").exists()).toBe(true);

		await findeKnopf(wrapper, "Noteneingabe frei").trigger("click");
		expect(findeKnopf(wrapper, "Noteneingabe fixiert").exists()).toBe(true);
	});

	test("Die Fixierung wird nicht gespeichert und bleibt beim Wiederherstellen erhalten", async () => {
		state.setZulassungSimulation.mockClear();
		state.getZulassungSimulation.mockResolvedValue(new GostZulassungSimulation());
		const wrapper = mounten();

		await findeKnopf(wrapper, "Noteneingabe fixiert").trigger("click");
		await findeKnopf(wrapper, "Sichern").trigger("click");
		expect(GostZulassungSimulation.transpilerToJSON(state.setZulassungSimulation.mock.calls[0][0])).not.toContain("fixiere");

		await findeKnopf(wrapper, "Wiederherstellen").trigger("click");
		await flushPromises();
		expect(findeKnopf(wrapper, "Noteneingabe frei").exists()).toBe(true);
	});

	test("Leeren setzt auch die Fixierung auf den Standard zurück", async () => {
		const wrapper = mounten();

		await findeKnopf(wrapper, "Noteneingabe fixiert").trigger("click");
		await findeKnopf(wrapper, "Leeren").trigger("click");

		expect(findeKnopf(wrapper, "Noteneingabe fixiert").exists()).toBe(true);
	});

	test("In Block I haben nur Eingabezellen den Hintergrund, fixierte Noten und Halbjahre ohne Belegung nicht", async () => {
		const wrapper = mounten(true, (manager) => {
			const belegung = manager.daten().fachbelegungen.toArray(new Array<AbiturFachbelegung>())
				.map(fach => fach.belegungen[GostHalbjahr.Q11.id]).find(b => b !== null);
			if (belegung === undefined) {
				throw new Error("Der Testfall hat keine Belegung in der Q1.1");
			}
			belegung.notenkuerzel = "2";
		});
		const tabelle = () => findeTabelle(wrapper, "Eingabe der Notenpunkte in der Qualifikationsphase");

		expect(tabelle().findAll("td").find(z => z.text() === "11")?.classes()).not.toContain("bg-ui-neutral");
		pruefeHintergrund(tabelle());
		const eingabenFixiert = tabelle().findAll("td.ui-table-grid-input").length;
		expect(eingabenFixiert).toBeGreaterThan(0);

		await findeKnopf(wrapper, "Noteneingabe fixiert").trigger("click");

		pruefeHintergrund(tabelle());
		expect(tabelle().findAll("td.ui-table-grid-input").length).toBe(eingabenFixiert + 1);
	});

	test("In Block II haben nur Eingabezellen und Schalter den Hintergrund", () => {
		const tabelle = findeTabelle(mounten(), "Eingabe der Prüfungsergebnisse");

		expect(tabelle.findAll("td.ui-table-grid-button").length).toBeGreaterThan(0);
		pruefeHintergrund(tabelle);
	});

	test("Die Zellen ohne Eingabe haben keinen eigenen Hintergrund mehr", () => {
		const wrapper = mounten();

		for (const name of ["Eingabe der Notenpunkte in der Qualifikationsphase", "Eingabe der Prüfungsergebnisse"]) {
			expect(findeTabelle(wrapper, name).findAll("td.bg-ui-50, td.bg-ui-75")).toHaveLength(0);
		}
	});

	test("Ein Attest wird auch bei freier Noteneingabe angezeigt und ist nicht editierbar", async () => {
		const wrapper = mounten(true, (manager) => {
			vorAbitur2030(manager);
			ersteBelegungQ11(manager).notenkuerzel = "AT";
		});
		const zelle = () => findeTabelle(wrapper, "Eingabe der Notenpunkte in der Qualifikationsphase").findAll("td").find(z => z.text() === "AT");

		await findeKnopf(wrapper, "Noteneingabe fixiert").trigger("click");

		expect(zelle()).toBeDefined();
		expect(zelle()?.classes()).not.toContain("ui-table-grid-input");
	});

	test("Die Pseudonote eines Vertiefungskurses wird in beiden Modi angezeigt und ist nicht editierbar", async () => {
		const wrapper = mounten(true, (manager) => {
			ersteBelegungAlsVertiefungskurs(manager).notenkuerzel = "NT";
		});
		const zelle = () => findeTabelle(wrapper, "Eingabe der Notenpunkte in der Qualifikationsphase").findAll("td").find(z => z.text() === "NT");

		expect(zelle()).toBeDefined();
		expect(zelle()?.classes()).not.toContain("ui-table-grid-input");

		await findeKnopf(wrapper, "Noteneingabe fixiert").trigger("click");

		expect(zelle()).toBeDefined();
		expect(zelle()?.classes()).not.toContain("ui-table-grid-input");
	});

	test("Ein Vertiefungskurs ohne Note ist in allen Halbjahren und in beiden Modi nicht editierbar", async () => {
		const anzahlEingaben = (w: ReturnType<typeof mounten>) => findeTabelle(w, "Eingabe der Notenpunkte in der Qualifikationsphase").findAll("td.ui-table-grid-input").length;
		const ohne = mounten();
		let anzahlBelegungen = 0;
		const mit = mounten(true, (manager) => {
			const belegung = ersteBelegungAlsVertiefungskurs(manager);
			const fach = manager.daten().fachbelegungen.toArray(new Array<AbiturFachbelegung>()).find(f => f.belegungen[GostHalbjahr.Q11.id] === belegung);
			anzahlBelegungen = GostHalbjahr.getQualifikationsphase().filter(hj => fach?.belegungen[hj.id] !== null).length;
		});

		expect(anzahlEingaben(mit)).toBe(anzahlEingaben(ohne) - anzahlBelegungen);

		await findeKnopf(ohne, "Noteneingabe fixiert").trigger("click");
		await findeKnopf(mit, "Noteneingabe fixiert").trigger("click");

		expect(anzahlEingaben(mit)).toBe(anzahlEingaben(ohne) - anzahlBelegungen);
	});

	test("Defizite in der Noteneingabe sind fett und rot", () => {
		const wrapper = mounten(true, (manager) => {
			const belegung = manager.daten().fachbelegungen.toArray(new Array<AbiturFachbelegung>())
				.map(fach => fach.belegungen[GostHalbjahr.Q11.id]).find(b => b !== null);
			if (belegung === undefined) {
				throw new Error("Der Testfall hat keine Belegung in der Q1.1");
			}
			belegung.notenkuerzel = "5";
		});

		const zelle = wrapper.findAll("td").find(z => z.text() === "02");

		expect(zelle?.classes()).toEqual(expect.arrayContaining(["font-bold", "text-ui-danger"]));
	});

	test("Die Summenzeile ist die einzige Fußzeile und die Note ist eine Spalte breit", () => {
		const zeilen = findeTabelle(mounten(), "Eingabe der Prüfungsergebnisse").findAll("tfoot tr");

		expect(zeilen).toHaveLength(1);
		const note = zeilen[0].find("td.bg-ui-brand-secondary");
		expect(note.exists()).toBe(true);
		expect(note.classes().some(k => k.startsWith("col-span"))).toBe(false);
		const spalten = zeilen[0].findAll("td").map(z => Number(z.classes().find(k => k.startsWith("col-span-"))?.substring(9) ?? 1));
		expect(spalten.reduce((a, b) => a + b, 0)).toBe(10);
	});

	test("Die besondere Lernleistung steht als Auswahl über Block II, standardmäßig keine und ohne Punkte", () => {
		const wrapper = mounten();

		const auswahl = wrapper.findAllComponents(UiSelect);

		expect(wrapper.text()).not.toContain("BLL:");
		expect(auswahl.map(s => auswahlProps(s).label)).toEqual(["Besondere Lernleistung"]);
		expect(auswahlProps(auswahl[0]).modelValue?.label).toBe("Keine");
	});

	test("Die Auswahllisten sind fachlich sortiert und nicht alphabetisch", async () => {
		const wrapper = mounten(true, vorAbitur2030);
		const art = wrapper.findAllComponents(UiSelect)[0];
		expect(auswahlOptionen<{ label: string }>(art).map(o => o.label)).toEqual(["Keine", "Projektkurs", "Extern"]);

		await art.vm.$emit("update:modelValue", { kuerzel: "E", label: "Extern" });
		await flushPromises();

		expect(auswahlOptionen<number>(wrapper.findAllComponents(UiSelect)[1])).toEqual([15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0]);
	});

	test("Ab Abitur 2030 gibt es keine eigene Auswahl für die Punkte der besonderen Lernleistung", async () => {
		const wrapper = mounten();
		const art = wrapper.findAllComponents(UiSelect)[0];

		await art.vm.$emit("update:modelValue", { kuerzel: "E", label: "Extern" });
		await flushPromises();

		expect(wrapper.findAllComponents(UiSelect).map(s => auswahlProps(s).label)).toEqual(["Besondere Lernleistung"]);
	});

	test("Vor Abitur 2030 lassen sich mit gewählter besonderer Lernleistung Punkte auswählen und werden gespeichert", async () => {
		state.setZulassungSimulation.mockClear();
		const wrapper = mounten(true, vorAbitur2030);

		const art = wrapper.findAllComponents(UiSelect)[0];
		await art.vm.$emit("update:modelValue", { kuerzel: "E", label: "Extern" });
		await flushPromises();

		const punkte = wrapper.findAllComponents(UiSelect)[1];
		expect(auswahlProps(punkte).label).toBe("Notenpunkte");
		await punkte.vm.$emit("update:modelValue", 11);
		await flushPromises();
		await findeKnopf(wrapper, "Sichern").trigger("click");

		const gespeichert = state.setZulassungSimulation.mock.calls[0][0];
		expect(gespeichert.besondereLernleistung).toBe("E");
		expect(gespeichert.besondereLernleistungNotenpunkte).toBe(11);
	});

	test("Eine erneute Note ersetzt den Eintrag des Halbjahres, die echten Abiturdaten bleiben unverändert", async () => {
		state.setZulassungSimulation.mockClear();
		const wrapper = mounten();
		const manager = state.abiturdatenManager as AbiturdatenManager;
		const vorher = Abiturdaten.transpilerToJSON(manager.daten());
		const noten = wrapper.findComponent(SLaufbahnplanungZulassungSimulationNoten);
		const setNote = (noten.props() as { setNote: (id: number, hj: GostHalbjahr, np: number | null) => void }).setNote;

		setNote(42, GostHalbjahr.Q21, 7);
		setNote(42, GostHalbjahr.Q21, 9);
		setNote(42, GostHalbjahr.Q22, 3);
		await findeKnopf(wrapper, "Sichern").trigger("click");

		const gespeichert = state.setZulassungSimulation.mock.calls[0][0] as GostZulassungSimulation;
		expect(gespeichert.noten.toArray(new Array<GostZulassungSimulationNote>()).map(n => n.notenpunkte)).toEqual([9, 3]);
		expect(Abiturdaten.transpilerToJSON(manager.daten())).toBe(vorher);
	});

	test("Eine Prüfungsnote legt je Fach nur einen Eintrag an und bestimmt die Pflichtprüfungen neu", async () => {
		state.setZulassungSimulation.mockClear();
		const wrapper = mounten();
		const manager = state.abiturdatenManager as AbiturdatenManager;
		const abiturfaecher = manager.daten().fachbelegungen.toArray(new Array<AbiturFachbelegung>()).filter(f => f.abiturFach !== null);
		const pruefung = wrapper.findComponent(SLaufbahnplanungZulassungSimulationPruefung);
		const setPruefung = (pruefung.props() as { setPruefung: (id: number, patch: object, neu: boolean) => void }).setPruefung;

		for (const fach of abiturfaecher) {
			setPruefung(fach.fachID, { pruefungNotenpunkte: 0 }, false);
		}
		setPruefung(abiturfaecher[0].fachID, { pruefungNotenpunkte: 0 }, true);
		await findeKnopf(wrapper, "Sichern").trigger("click");

		const gespeichert = (state.setZulassungSimulation.mock.calls[0][0] as GostZulassungSimulation).abiturfaecher.toArray(new Array<GostZulassungSimulationAbiturfach>());
		expect(gespeichert).toHaveLength(abiturfaecher.length);
		expect(gespeichert.map(e => e.muendlichBestehen)).not.toContain(null);
	});

	test("Ohne Noten erscheint das Log des Algorithmus als Hinweis", () => {
		const wrapper = mounten();

		expect(wrapper.text()).toContain("Hinweise:");
		expect(wrapper.findAll(".whitespace-pre-wrap").length).toBeGreaterThan(0);
	});

	test("Speichern ist ohne Update-Kompetenz nicht möglich", () => {
		expect(findeKnopf(mounten(false), "Sichern").attributes("disabled")).toBeDefined();
		expect(findeKnopf(mounten(true), "Sichern").attributes("disabled")).toBeUndefined();
	});

	test("Speichern übergibt den aktuellen Stand an den State", async () => {
		state.setZulassungSimulation.mockClear();
		const wrapper = mounten();

		await findeKnopf(wrapper, "Sichern").trigger("click");

		expect(state.setZulassungSimulation).toHaveBeenCalledOnce();
		expect(state.setZulassungSimulation.mock.calls[0][0]).toBeInstanceOf(GostZulassungSimulation);
	});

	test("Exportieren liefert den Stand als JSON-Datei, die sich über das Core-DTO wieder lesen lässt", async () => {
		const erzeugeUrl = vi.spyOn(URL, "createObjectURL").mockReturnValue("blob:test");
		const freigeben = vi.spyOn(URL, "revokeObjectURL").mockImplementation(() => undefined);
		let dateiname = "";
		const klick = vi.spyOn(HTMLAnchorElement.prototype, "click").mockImplementation(function(this: HTMLAnchorElement) {
			dateiname = this.download;
		});
		const wrapper = mounten();
		const art = wrapper.findAllComponents(UiSelect)[0];
		await art.vm.$emit("update:modelValue", { kuerzel: "E", label: "Extern" });
		await flushPromises();

		await findeKnopf(wrapper, "Exportieren").trigger("click");

		expect(klick).toHaveBeenCalledOnce();
		expect(dateiname).toMatch(/^ZulassungSimulation_.*\.json$/u);
		const datei = erzeugeUrl.mock.calls[0][0] as Blob;
		expect(GostZulassungSimulation.transpilerFromJSON(await datei.text()).besondereLernleistung).toBe("E");
		expect(freigeben).toHaveBeenCalledWith("blob:test");
		vi.restoreAllMocks();
	});

	test("Importieren liest den Stand über das Core-DTO ein, ohne ihn zu speichern", async () => {
		state.setZulassungSimulation.mockClear();
		const wrapper = mounten();
		const stand = new GostZulassungSimulation();
		stand.besondereLernleistung = "E";

		await importiere(wrapper, GostZulassungSimulation.transpilerToJSON(stand));

		await vi.waitFor(() => expect(auswahlProps(wrapper.findAllComponents(UiSelect)[0]).modelValue?.label).toBe("Extern"));
		expect(state.setZulassungSimulation).not.toHaveBeenCalled();
	});

	test("Eine ungültige Datei ändert den Stand nicht und zeigt einen Hinweis, der beim nächsten gültigen Import verschwindet", async () => {
		const wrapper = mounten();
		const hinweis = "Die Datei enthält keinen gültigen Stand der Simulation.";

		await importiere(wrapper, "kein JSON");

		await vi.waitFor(() => expect(wrapper.text()).toContain(hinweis));
		expect(auswahlProps(wrapper.findAllComponents(UiSelect)[0]).modelValue?.label).toBe("Keine");

		await importiere(wrapper, GostZulassungSimulation.transpilerToJSON(new GostZulassungSimulation()));

		await vi.waitFor(() => expect(wrapper.text()).not.toContain(hinweis));
	});

	test.each(["Leeren", "Wiederherstellen"])("%s entfernt den Hinweis auf eine ungültige Datei", async (knopf) => {
		state.getZulassungSimulation.mockResolvedValue(new GostZulassungSimulation());
		const wrapper = mounten();
		const hinweis = "Die Datei enthält keinen gültigen Stand der Simulation.";
		await importiere(wrapper, "kein JSON");
		await vi.waitFor(() => expect(wrapper.text()).toContain(hinweis));

		await findeKnopf(wrapper, knopf).trigger("click");
		await flushPromises();

		expect(wrapper.text()).not.toContain(hinweis);
	});

	test("Der Knopf öffnet die Dateiauswahl", async () => {
		const wrapper = mounten();
		const auswahl = vi.spyOn(wrapper.find<HTMLInputElement>("input[type=file]").element, "click");

		await findeKnopf(wrapper, "Importieren").trigger("click");

		expect(auswahl).toHaveBeenCalledOnce();
	});

	test("Laden holt den Stand vom State und berechnet neu", async () => {
		const gespeichert = new GostZulassungSimulation();
		gespeichert.besondereLernleistung = "E";
		state.getZulassungSimulation.mockClear();
		state.getZulassungSimulation.mockResolvedValue(gespeichert);
		const wrapper = mounten();

		await findeKnopf(wrapper, "Wiederherstellen").trigger("click");
		await flushPromises();

		expect(state.getZulassungSimulation).toHaveBeenCalledOnce();
		expect(auswahlProps(wrapper.findAllComponents(UiSelect)[0]).modelValue?.label).toBe("Extern");
	});

});
