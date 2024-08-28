import { describe, test, expect } from "vitest";
import { mount } from "@vue/test-utils";
import SvwsUiInputWrapper from "./SvwsUiInputWrapper.vue";

describe("SvwsUiInputWrapper", () => {
	// Testet, ob das HTML korrekt gerendert wird
	test("Rendert HTML korrekt", () => {
		const wrapper = mount(SvwsUiInputWrapper);
		expect(wrapper.html()).toContain("<div");
	});

	test("Die Komponente mit den Standard-Props korrekt gerendert", () => {
		const wrapper = mount(SvwsUiInputWrapper);
		expect(wrapper.classes()).toContain("input-wrapper");
		expect(wrapper.classes()).not.toContain("input-wrapper--2");
		expect(wrapper.classes()).not.toContain("input-wrapper--4");
		expect(wrapper.classes()).not.toContain("input-wrapper--center");
	});

	test("Die Komponente mit dem Prop 'grid' auf '2' wird korrekt gerendert", () => {
		const wrapper = mount(SvwsUiInputWrapper, {
			props: { grid: 2 },
		});
		expect(wrapper.classes()).toContain("input-wrapper--2");
	});

	test("Die Komponente mit dem Prop 'grid' auf '4' wird richtig gerendert ", () => {
		const wrapper = mount(SvwsUiInputWrapper, {
			props: { grid: 4 },
		});
		expect(wrapper.classes()).toContain("input-wrapper--4");
	});

	test("Die Komponente mit dem Prop 'center' wird richtig gerendert ", () => {
		const wrapper = mount(SvwsUiInputWrapper, {
			props: { center: true },
		});
		expect(wrapper.classes()).toContain("input-wrapper--center");
	});

	test("Der Slot-Inhalt wird korrekt gerendert", () => {
		const wrapper = mount(SvwsUiInputWrapper, {
			slots: {
				default: "<span class='slot-content'>Slot-Inhalt</span>",
			},
		});
		expect(wrapper.find(".slot-content").exists()).toBe(true);
	});
});
