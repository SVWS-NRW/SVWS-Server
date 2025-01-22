import { describe, test, expect} from "vitest";

import SvwsUiToggle from "./SvwsUiToggle.vue";
import { mount, shallowMount } from "@vue/test-utils";

type prop_names = "statistics" | "headless" | "disabled";

describe.concurrent("Component kann gemounted werden", () => {
	test("HTML wird erzeugt", () => {
		const wrapper = shallowMount(SvwsUiToggle);
		expect(wrapper.html()).includes("toggle");
		const indicatorSpan = wrapper.find("span.toggle--indicator");
		expect(indicatorSpan.exists()).toBeTruthy();
	});
});

describe.concurrent("Tests für die CSS-Props", () => {
	// [Propname, Klassen- oder Stylename, Beschreibung, Class(0)|Style(1)]
	test.each([
		["statistics", "toggle--statistics", "Prop statistics wird an CSS übergeben"],
		["headless", "toggle--headless", "Prop headless wird an CSS übergeben"],
		["disabled", "toggle--disabled", "Prop disabled wird an CSS übergeben"],
	])("%s(%s) => %s", async (x, y) => {
		// Vorbereiten
		const wrapper = mount(SvwsUiToggle);
		expect(wrapper.props(x as prop_names)).toBe(false);
		expect(wrapper.classes().filter((value) => value === y).length).toBe(0);

		// Testen
		switch (x) {
			case "statistics":
				await wrapper.setProps({ statistics: true });
				break;
			case "headless":
				await wrapper.setProps({ headless: true });
				break;
			case "disabled":
				await wrapper.setProps({ disabled: true });
				break;
			default:
				break;
		}
		expect(wrapper.find("label").classes()).toContain(y);
	});
});

describe("Bedingtes Rendern", () => {
	test("Slot-Inhalt wird korrekt gerendert, wenn er übergeben wird.", () => {
		// Vorbereiten
		const wrapper = shallowMount(SvwsUiToggle, {
			slots: {
				default: "Custom label",
			},
		});
		// Testen
		expect(wrapper.text()).toContain("Custom label");
	});

	test("Statistik-Icon wird bei statistic=false nicht gerendert, wenn ein Slot-Inhalt übergeben wird.", async () => {
		// Vorbereiten
		const wrapper = shallowMount(SvwsUiToggle, {
			slots: {
				default: "Custom label",
			},
		});

		// Testen
		const outerSpan = wrapper.find("span.toggle--label");
		expect(outerSpan.exists()).toBeTruthy();
		const innerSpan = outerSpan.find("span.icon");
		expect(innerSpan.exists()).toBeFalsy();
		expect(wrapper.text()).toContain("Custom label");
	});

	test("Statistik-Icon wird bei statistic=true und einem leeren Slot-Inhalt gerendert.", async () => {
		// Vorbereiten
		const wrapper = shallowMount(SvwsUiToggle, {
			props: { statistics: true },
		});

		// Testen
		const outerSpan = wrapper.find("span.toggle--label");
		expect(outerSpan.exists()).toBeTruthy();
		const innerSpan = outerSpan.find(
			"span.icon.i-ri-bar-chart-2-line.icon-statistics.ml-2"
		);
		expect(innerSpan.exists()).toBeTruthy();
	});
});

test("computed->value->get liefert aktullen Checkbox-Value", async () => {
	// Vorereiten
	const wrapper = mount(SvwsUiToggle);
	expect(wrapper.props("modelValue")).toBe(false);
	expect(
		await wrapper.findComponent({
			name: "SvwsUiToggle",
		}).vm.value
	).toBeFalsy();

	// Aktion
	await wrapper.setProps({ modelValue: true });

	// Testen
	expect(
		await wrapper.findComponent({
			name: "SvwsUiToggle",
		}).vm.value
	).toBeTruthy();
});

test("Computed-value -> set ändert den Checkbox-Value von true auf false", async () => {
	// Vorereiten
	const wrapper = mount(SvwsUiToggle, {
		props: {
			modelValue: true,
			"onUpdate:modelValue": async (value) =>
				await wrapper.setProps({ modelValue: value }),
		},
	});
	expect(wrapper.props("modelValue")).toBe(true);

	// Aktion
	wrapper.findComponent({
		name: "SvwsUiToggle",
	}).vm.value = false;
	await wrapper.vm.$nextTick();

	// Testen
	expect(
		await wrapper.findComponent({
			name: "SvwsUiToggle",
		}).vm.modelValue
	).toBeFalsy();
});

test("Computed-value -> set ändert den Checkbox-Value von false auf true", async () => {
	// Vorereiten
	const wrapper = mount(SvwsUiToggle, {
		props: {
			modelValue: false,
			"onUpdate:modelValue": async (value) =>
				await wrapper.setProps({ modelValue: value }),
		},
	});
	expect(wrapper.props("modelValue")).toBe(false);

	// Aktion
	wrapper.findComponent({
		name: "SvwsUiToggle",
	}).vm.value = true;
	await wrapper.vm.$nextTick();

	// Testen
	expect(
		await wrapper.findComponent({
			name: "SvwsUiToggle",
		}).vm.modelValue
	).toBeTruthy();
});

test("Event-> löst update:modelValue aus, wenn checkbox geklickt wird.", async () => {
	// Vorbereiten
	const wrapper = shallowMount(SvwsUiToggle);
	expect(wrapper.props("modelValue")).toBe(false);
	const checkbox = wrapper.find("input[type='checkbox']");

	// Aktion
	await checkbox.trigger("click");
	await checkbox.trigger("change");

	// Testen
	const emittedEvents = wrapper.emitted("update:modelValue");
	expect(emittedEvents).toBeTruthy(); // Überprüft, ob Events emittiert wurden
	expect(emittedEvents?.length).toBeGreaterThan(0);
	if (emittedEvents !== undefined)
		expect(emittedEvents[0][0]).toEqual(true);
	else
		throw new Error("Keine Ereignisse ausgesendet");
});
