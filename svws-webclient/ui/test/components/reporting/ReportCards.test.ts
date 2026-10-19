import type { VueWrapper } from "@vue/test-utils";
import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { defineComponent, h, nextTick } from "vue";

import { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";
import { ReportingReportvorlageDatenContext } from "@core/core/types/reporting/ReportingReportvorlageDatenContext";
import { ReportingReportvorlageUiNutzung } from "@core/core/types/reporting/ReportingReportvorlageUiNutzung";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import ReportCards from "@ui/components/reporting/ReportCards.vue";
import UiCard from "@ui/ui/layout/UiCard.vue";

const nichtZulaessig = new Set<ReportingReportvorlage>();

vi.mock("@ui/states/ReportingState", () => ({
	useReportingState: () => ({ istVorlageZulaessig: (vorlage: ReportingReportvorlage) => !nichtZulaessig.has(vorlage) }),
}));

function stundenplan(id: number): StundenplanListeEintrag {
	const s = new StundenplanListeEintrag();
	s.id = id;
	s.bezeichnung = "Stundenplan " + id;
	s.gueltigAb = "2026-08-01";
	s.gueltigBis = "2027-07-31";
	return s;
}

const idsKlassen = (): Map<ReportingReportvorlageDatenContext, () => List<number>> => new Map([
	[ReportingReportvorlageDatenContext.KLASSEN, () => ArrayList.of(1, 2)],
	[ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN, () => ArrayList.of(1, 2)],
	[ReportingReportvorlageDatenContext.SCHUELER, () => ArrayList.of(11, 12, 13)],
]);

/** Bindet ReportCards über eine Elternkomponente ein. mount würde die Props sonst tief reaktiv machen, anders als ein Bereich im Client. */
function erzeuge(props: InstanceType<typeof ReportCards>["$props"]) {
	const Bereich = defineComponent({ setup: () => () => h(ReportCards, props) });
	return mount(Bereich, { global: { stubs: { ReportParameters: true, UiSelect: true } } });
}

function emitted(wrapper: VueWrapper, event: string): unknown[] | undefined {
	return wrapper.findComponent(ReportCards).emitted(event)?.at(-1);
}

function karten(wrapper: VueWrapper) {
	return wrapper.findAllComponents(UiCard);
}

function titel(wrapper: VueWrapper): string[] {
	return karten(wrapper).map(k => k.props("title") as string);
}

function karte(wrapper: VueWrapper, vorlage: ReportingReportvorlage) {
	const k = karten(wrapper).find(c => c.props("title") === vorlage.getUiTitel());
	if (k === undefined) {
		throw new Error("Keine Card für " + vorlage.name());
	}
	return k;
}

function titelImBereich(uiNutzung: ReportingReportvorlageUiNutzung): string[] {
	return [...ReportingReportvorlage.getByUiNutzung(uiNutzung)].map(v => v.getUiTitel());
}

beforeEach(() => {
	nichtZulaessig.clear();
});

describe("ReportCards", () => {

	test("zeigt die Vorlagen eines Bereichs nach Titel sortiert", () => {
		const wrapper = erzeuge({ uiNutzung: ReportingReportvorlageUiNutzung.KLASSEN, ids: idsKlassen(), stundenplaene: [stundenplan(7)] });
		expect(titel(wrapper)).toEqual(titelImBereich(ReportingReportvorlageUiNutzung.KLASSEN));
		expect(titel(wrapper)).toHaveLength(6);
		expect(emitted(wrapper, "update:hatVorlagen")).toEqual([true]);
	});

	test("übergibt die IDs passend zum Daten-Context der Vorlage", () => {
		const wrapper = erzeuge({ uiNutzung: ReportingReportvorlageUiNutzung.KLASSEN, ids: idsKlassen(), stundenplaene: [stundenplan(7)] });
		const ids = (vorlage: ReportingReportvorlage) => [...karte(wrapper, vorlage).findComponent({ name: "ReportParameters" }).props("idsHauptdaten") as Iterable<number>];
		expect(ids(ReportingReportvorlage.SCHUELER_V_GRUNDSCHULZEUGNIS_KLASSE_4)).toEqual([11, 12, 13]);
		expect(ids(ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN)).toEqual([1, 2]);
	});

	test("setzt die Stundenplan-ID nur bei Stundenplan-Vorlagen und zeigt dort die Auswahl", () => {
		const wrapper = erzeuge({ uiNutzung: ReportingReportvorlageUiNutzung.KLASSEN, ids: idsKlassen(), stundenplaene: [stundenplan(7), stundenplan(8)] });
		const stundenplanKarte = karte(wrapper, ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN);
		expect(stundenplanKarte.findComponent({ name: "ReportParameters" }).props("idHauptdatenObjekt")).toBe(7);
		expect(stundenplanKarte.findComponent({ name: "UiSelect" }).exists()).toBe(true);
		const listenKarte = karte(wrapper, ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN);
		expect(listenKarte.findComponent({ name: "ReportParameters" }).props("idHauptdatenObjekt")).toBeUndefined();
		expect(listenKarte.findComponent({ name: "UiSelect" }).exists()).toBe(false);
	});

	test("übernimmt einen anderen gewählten Stundenplan", async () => {
		const plaene = [stundenplan(7), stundenplan(8)];
		const wrapper = erzeuge({ uiNutzung: ReportingReportvorlageUiNutzung.KLASSEN, ids: idsKlassen(), stundenplaene: plaene });
		const stundenplanKarte = () => karte(wrapper, ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN);
		stundenplanKarte().findComponent({ name: "UiSelect" }).vm.$emit("update:modelValue", plaene[1]);
		await nextTick();
		expect(stundenplanKarte().findComponent({ name: "ReportParameters" }).props("idHauptdatenObjekt")).toBe(8);
	});

	test("blendet Vorlagen ohne IDs für ihren Daten-Context aus", () => {
		const ids = idsKlassen();
		ids.delete(ReportingReportvorlageDatenContext.SCHUELER);
		const wrapper = erzeuge({ uiNutzung: ReportingReportvorlageUiNutzung.KLASSEN, ids, stundenplaene: [stundenplan(7)] });
		expect(titel(wrapper)).not.toContain(ReportingReportvorlage.SCHUELER_V_GRUNDSCHULZEUGNIS_KLASSE_4.getUiTitel());
		expect(titel(wrapper)).toHaveLength(5);
	});

	test("blendet Vorlagen ohne ausgewählte Daten aus, meldet sie aber als nutzbar", () => {
		const ids = new Map<ReportingReportvorlageDatenContext, () => List<number>>([
			[ReportingReportvorlageDatenContext.KLASSEN, () => new ArrayList<number>()],
			[ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN, () => new ArrayList<number>()],
			[ReportingReportvorlageDatenContext.SCHUELER, () => new ArrayList<number>()],
		]);
		const wrapper = erzeuge({ uiNutzung: ReportingReportvorlageUiNutzung.KLASSEN, ids, stundenplaene: [stundenplan(7)] });
		expect(titel(wrapper)).toEqual([]);
		expect(emitted(wrapper, "update:hatVorlagen")).toEqual([true]);
	});

	test("blendet Stundenplan-Vorlagen ohne Stundenplan aus", () => {
		const wrapper = erzeuge({ uiNutzung: ReportingReportvorlageUiNutzung.KLASSEN, ids: idsKlassen() });
		expect(titel(wrapper)).not.toContain(ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN.getUiTitel());
		expect(titel(wrapper)).toHaveLength(5);
		expect(emitted(wrapper, "update:hatVorlagen")).toEqual([true]);
	});

	test("blendet unzulässige Vorlagen aus", () => {
		nichtZulaessig.add(ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN);
		const wrapper = erzeuge({ uiNutzung: ReportingReportvorlageUiNutzung.KLASSEN, ids: idsKlassen(), stundenplaene: [stundenplan(7)] });
		expect(titel(wrapper)).not.toContain(ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN.getUiTitel());
		expect(titel(wrapper)).toHaveLength(5);
	});

	test("meldet keine nutzbaren Vorlagen, wenn keine Vorlage des Bereichs zulässig ist", () => {
		for (const vorlage of ReportingReportvorlage.getByUiNutzung(ReportingReportvorlageUiNutzung.KLASSEN)) {
			nichtZulaessig.add(vorlage);
		}
		const wrapper = erzeuge({ uiNutzung: ReportingReportvorlageUiNutzung.KLASSEN, ids: idsKlassen(), stundenplaene: [stundenplan(7)] });
		expect(titel(wrapper)).toEqual([]);
		expect(emitted(wrapper, "update:hatVorlagen")).toEqual([false]);
	});

	test("ergänzt einzeln übergebene Vorlagen hinter dem Bereich und zeigt keine doppelt", () => {
		const ids = new Map<ReportingReportvorlageDatenContext, () => List<number>>([
			[ReportingReportvorlageDatenContext.KURSE, () => ArrayList.of(5)],
			[ReportingReportvorlageDatenContext.SCHUELER, () => ArrayList.of(11)],
		]);
		const wrapper = erzeuge({
			uiNutzung: ReportingReportvorlageUiNutzung.KURSE, ids,
			vorlagen: [ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG, ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_FOTOS_NAMEN],
		});
		expect(titel(wrapper)).toEqual([...titelImBereich(ReportingReportvorlageUiNutzung.KURSE), ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG.getUiTitel()]);
	});

	test("meldet die geöffnete Card über currentAction", () => {
		const wrapper = erzeuge({ uiNutzung: ReportingReportvorlageUiNutzung.KURSE, ids: new Map([[ReportingReportvorlageDatenContext.KURSE, () => ArrayList.of(5)]]) });
		karten(wrapper)[0].vm.$emit("update:isOpen", true);
		const erste = ReportingReportvorlage.getByUiNutzung(ReportingReportvorlageUiNutzung.KURSE).get(0);
		expect(emitted(wrapper, "update:currentAction")).toEqual(["report:" + erste.name()]);
	});

});
