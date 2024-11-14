import { describe, expect, test } from "vitest";
import SvwsUiBadge from "./SvwsUiBadge.vue";
import { mount } from "@vue/test-utils";
import SvwsUiTooltip from "./SvwsUiTooltip.vue";
import type { Size, Type } from "../types";

describe("SvwsUiInputWrapper", () => {
	// Testet, ob das HTML korrekt gerendert wird
	test("Rendert HTML korrekt", async () => {
		const wrapper = mount(SvwsUiBadge);

		expect(wrapper.html()).includes("svws-ui-badge");
	});
});

describe("Bedingtes Rendern mit CSS-Prüfung", () => {
	test("Korrekte CSS-Werte bei einem mount mit default Props", () => {
		// Vorbereiten
		const wrapper = mount(SvwsUiBadge);
		const spanClasses = wrapper.find("span").classes();

		// Testen
		expect(spanClasses.length).toBe(4);
		expect(spanClasses).toContain("svws-ui-badge");
		expect(spanClasses).toContain("badge");
		expect(spanClasses).toContain("badge--light");
		expect(spanClasses).toContain("badge--normal");
	});
	test("svws-ui-tooltip wird gerendert, weil 'short' true ist.", () => {
		// Vorbereiten
		const wrapper = mount(SvwsUiBadge, { props: { short: true } });

		// Testen
		expect(wrapper.findComponent(SvwsUiTooltip).exists()).toBeTruthy();
	});

	test("Defaultslot-Element in Tooltip wird gerendert.", async () => {
		// Vorbereiten
		const wrapper = mount(SvwsUiBadge, {
			props: { short: true },
			slots: { default: "Test Badge Slot" },
		});

		// Testen
		expect(wrapper.text()).toContain("Test Badge Slot");
	});

	test("svws-ui-tooltip wird nicht gerendert, weil 'short' false ist.", () => {
		// Vorbereiten
		const wrapper = mount(SvwsUiBadge);

		// Testen
		expect(wrapper.findComponent(SvwsUiTooltip).exists()).toBeFalsy();
	});
	describe("'props.short' ist false", () => {
		test.each([
			[
				"primary",
				"badge--primary",
				"badge--primary wird auf die Komponenente angewendet",
			],
			[
				"success",
				"badge--success",
				"badge--success wird auf die Komponenente angewendet",
			],
			[
				"error",
				"badge--error",
				"badge--error wird auf die Komponenente angewendet",
			],
			[
				"highlight",
				"badge--highlight",
				"badge--highlight wird auf die Komponenente angewendet",
			],
			[
				"light",
				"badge--light",
				"badge--light wird auf die Komponenente angewendet",
			],
		])("%s(%s) => %s", async (x, y) => {
			// Vorbereiten
			const wrapper = mount(SvwsUiBadge, { props: { type: x as Type } });
			const spanClasses = wrapper.find("span").classes();

			// Testen
			expect(spanClasses).toContain(y);
		});
		// [props.size,CSS-Name, Beschreibung]
		test.each([
			[
				"big",
				"badge--lg",
				"badge--lg wird auf die Komponenente angewendet",
			],
			[
				"normal",
				"badge--normal",
				"badge--normal wird auf die Komponenente angewendet",
			],
			[
				"small",
				"badge--small",
				"badge--small wird auf die Komponenente angewendet",
			],
		])("%s(%s) => %s", async (x, y) => {
			// Vorbereiten
			const wrapper = mount(SvwsUiBadge, { props: { size: x as Size } });
			const spanClasses = wrapper.find("span").classes();

			// Testen
			expect(spanClasses).toContain(y);
		});

		test("badge--short wird nicht auf die Komponenente angewendet", () => {
			// Vorbereiten
			const wrapper = mount(SvwsUiBadge, {
				props: { type: "success" },
			});
			const spanClasses = wrapper.find("span").classes();

			// Testen
			expect(spanClasses).not.toContain("badge--short");
		});

		test("Slot-Element wird gerendert.", async () => {
			// Vorbereiten
			const wrapper = mount(SvwsUiBadge, {
				props: { type: "success" },
				slots: { default: "Test Badge Slot" },
			});

			// Testen
			expect(wrapper.text()).toContain("Test Badge Slot");
		});
	});
});
