import { mount } from "@vue/test-utils";
import { describe, expect, test } from "vitest";

import { NavigationGuardQuery } from "@ui/ui/modal/NavigationGuardQuery";
import SvwsUiNavigationGuardModal from "@ui/ui/modal/SvwsUiNavigationGuardModal.vue";

/** Ersetzt das Modal und den Button durch einfache Elemente, damit die Knöpfe und der Inhalt direkt prüfbar sind */
const stubs = {
	SvwsUiModal: { props: ["show"], emits: ["update:show"], template: "<div v-if='show' class='modal'><slot name='modalTitle' /><slot name='modalDescription' /><slot name='modalActions' /></div>" },
	SvwsUiButton: { template: "<button><slot /></button>" },
};

describe("SvwsUiNavigationGuardModal", () => {

	test("zeigt nichts, solange die Abfrage geschlossen ist", () => {
		const wrapper = mount(SvwsUiNavigationGuardModal, { props: { query: new NavigationGuardQuery() }, global: { stubs } });
		expect(wrapper.find(".modal").exists()).toBe(false);
	});

	test("zeigt bei offener Abfrage drei Knöpfe und beantwortet sie", async () => {
		const query = new NavigationGuardQuery();
		const wrapper = mount(SvwsUiNavigationGuardModal, { props: { query }, global: { stubs } });
		for (const [text, erwartet] of [["Abbrechen", "cancel"], ["Verwerfen und fortfahren", "discard"], ["Speichern und fortfahren", "save"]] as const) {
			const reply = query.query();
			await wrapper.vm.$nextTick();
			const knoepfe = wrapper.findAll("button");
			expect(knoepfe.map(k => k.text())).toEqual(["Abbrechen", "Verwerfen und fortfahren", "Speichern und fortfahren"]);
			await knoepfe.find(k => k.text() === text)!.trigger("click");
			await expect(reply).resolves.toBe(erwartet);
		}
	});

	test("ohne speichern wird 'Speichern und fortfahren' nicht angeboten", async () => {
		const query = new NavigationGuardQuery();
		const wrapper = mount(SvwsUiNavigationGuardModal, { props: { query, speichern: false }, global: { stubs } });
		void query.query();
		await wrapper.vm.$nextTick();
		expect(wrapper.findAll("button").map(k => k.text())).toEqual(["Abbrechen", "Verwerfen und fortfahren"]);
	});

	test("ein Schließen des Modals ohne Knopf gilt als Abbrechen", async () => {
		const query = new NavigationGuardQuery();
		const wrapper = mount(SvwsUiNavigationGuardModal, { props: { query }, global: { stubs } });
		const reply = query.query();
		await wrapper.vm.$nextTick();
		wrapper.findComponent(stubs.SvwsUiModal).vm.$emit("update:show", false);
		await expect(reply).resolves.toBe("cancel");
	});

	test("bei gesperrtem Speichern ist 'Speichern und fortfahren' deaktiviert, die anderen Knöpfe bleiben aktiv", async () => {
		const query = new NavigationGuardQuery();
		const wrapper = mount(SvwsUiNavigationGuardModal, { props: { query }, global: { stubs } });
		void query.query({ bereiche: [], isPatchAllowed: false, patchHinweise: [] });
		await wrapper.vm.$nextTick();
		const knoepfe = wrapper.findAll("button");
		expect(knoepfe.find(k => k.text() === "Speichern und fortfahren")!.attributes("disabled")).toBeDefined();
		expect(knoepfe.find(k => k.text() === "Abbrechen")!.attributes("disabled")).toBeUndefined();
		expect(knoepfe.find(k => k.text() === "Verwerfen und fortfahren")!.attributes("disabled")).toBeUndefined();
	});

	test("der Hinweis, warum das Speichern nicht möglich ist, wird angezeigt", async () => {
		const query = new NavigationGuardQuery();
		const wrapper = mount(SvwsUiNavigationGuardModal, { props: { query }, global: { stubs } });
		void query.query({ bereiche: [], isPatchAllowed: false, patchHinweise: ["Zuzugsjahr ungültig"] });
		await wrapper.vm.$nextTick();
		expect(wrapper.text()).toContain("Speichern nicht möglich: Zuzugsjahr ungültig");
	});

	test("mehrere Hinweise werden mit Semikolon getrennt angezeigt", async () => {
		const query = new NavigationGuardQuery();
		const wrapper = mount(SvwsUiNavigationGuardModal, { props: { query }, global: { stubs } });
		void query.query({ bereiche: [], isPatchAllowed: false, patchHinweise: ["Zuzugsjahr ungültig", "Aufnahmedatum ungültig"] });
		await wrapper.vm.$nextTick();
		expect(wrapper.text()).toContain("Speichern nicht möglich: Zuzugsjahr ungültig; Aufnahmedatum ungültig");
	});

	test("ohne Sperre erscheint kein Hinweis und das Speichern ist aktiv", async () => {
		const query = new NavigationGuardQuery();
		const wrapper = mount(SvwsUiNavigationGuardModal, { props: { query }, global: { stubs } });
		void query.query();
		await wrapper.vm.$nextTick();
		expect(wrapper.text()).not.toContain("Speichern nicht möglich");
		expect(wrapper.findAll("button").find(k => k.text() === "Speichern und fortfahren")!.attributes("disabled")).toBeUndefined();
	});

	test("nennt die Bereiche mit Änderungen", async () => {
		const query = new NavigationGuardQuery();
		const wrapper = mount(SvwsUiNavigationGuardModal, { props: { query }, global: { stubs } });
		void query.query({ bereiche: ["Individualdaten", "Personaldaten"], isPatchAllowed: true, patchHinweise: [] });
		await wrapper.vm.$nextTick();
		expect(wrapper.text()).toContain("Änderungen in: Individualdaten, Personaldaten");
	});

	test("ohne Bereiche wird keine Aufzählung angezeigt", async () => {
		const query = new NavigationGuardQuery();
		const wrapper = mount(SvwsUiNavigationGuardModal, { props: { query }, global: { stubs } });
		void query.query();
		await wrapper.vm.$nextTick();
		expect(wrapper.text()).not.toContain("Änderungen in:");
	});

});
