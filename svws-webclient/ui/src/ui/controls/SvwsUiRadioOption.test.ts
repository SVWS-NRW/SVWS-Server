import type { VueWrapper } from "@vue/test-utils";
import { mount } from "@vue/test-utils";
import { afterEach, beforeEach, describe, expect, test } from "vitest";
import SvwsUiRadioOption from "./SvwsUiRadioOption.vue";

let wrapper: VueWrapper<InstanceType<typeof SvwsUiRadioOption>>;
let idComponent: string;
let idInputField: string;
let idIcon: string;
let idLabel: string;

const defaultProps: Partial<{
	name: string;
	label: string;
	value: string | number | boolean | object;
	disabled: boolean;
	statistics: boolean;
	icon: boolean;
	iconType: string;
	modelValue: string | number | boolean | object;
	forceChecked: boolean;
}> = {
	name: "",
	label: "",
	value: "",
	disabled: false,
	statistics: false,
	icon: true,
	iconType: "default",
	modelValue: "",
	forceChecked: false,
};

beforeEach(() => {
	wrapper = mount(SvwsUiRadioOption);

	idComponent ="#"+wrapper.findComponent({ name: "SvwsUiRadioOption" }).vm.idComponent;
	idInputField ="#"+wrapper.findComponent({ name: "SvwsUiRadioOption" }).vm.idInputField;
	idIcon ="#"+wrapper.findComponent({ name: "SvwsUiRadioOption" }).vm.idIcon;
	idLabel ="#"+wrapper.findComponent({ name: "SvwsUiRadioOption" }).vm.idLabel;
});

afterEach(() => {
	wrapper.unmount();
})

test("Rendert HTML korrekt", async () => {
	expect(wrapper.find(idComponent).exists()).toBeTruthy();
	expect(wrapper.find(idInputField).exists()).toBeTruthy();
	expect(wrapper.find(idIcon).exists()).toBeTruthy();
	expect(wrapper.find(idLabel).exists()).toBeTruthy();
});

test("Teste die default-Werte der props", () => {
	for (const [prop, expectedValue] of Object.entries(defaultProps))
		expect(wrapper.props()[prop as keyof typeof defaultProps]).toBe(expectedValue);
});

describe("Tests für die CSS-Props", () => {
	// [Propname, Klassen- oder Stylename, Beschreibung, Class(0)Style(1)]
	test.each([
		["disabled", "radio--label--disabled", "Prop disabled wird an CSS übergeben"],
		["statistics", "radio--statistics", "Prop statisctics wird an CSS übergeben"],
		// ["forceChecked", "radio--label--checked", "Prop forceChecked wird an CSS übergeben"],
		// ["modelValue", "radio--label--checked", "Prop modelValue wird an CSS übergeben"],
		// ["value", "radio--label--checked", "Prop value wird an CSS übergeben"],
		["icon", "radio--label--no-icon", "Prop icon wird an CSS übergeben"],
		["label", "radio--label--no-text", "Prop label wird an CSS übergeben"],
		["iconType", "radio--icon-type-view", "Prop iconType wird an CSS übergeben"],
	])("%s(%s) => %s", async (x, y) => {
		// Vorbereiten
		// Testen der default-Werte
		switch (x) {
			case "disable":
			case "statistics":
			case "icon":
			case "iconType":
				expect(wrapper.find(idComponent).classes()).not.toContain(y);
				break;
			case "forceChecked":
			case "modelValue":
			case "value":
			case "label":
				expect(wrapper.find(idComponent).classes()).toContain(y);
				break;
			default:
				break;
		}
		// Änderen der props-Werte
		switch (x) {
			case "disabled":
				await wrapper.setProps({ disabled: true });
				break;
			case "statistics":
				await wrapper.setProps({ statistics: true });
				break;
			case "forceChecked":
				await wrapper.setProps({
					modelValue: undefined,
					value: "Test2",
				});
				break;
			case "modelValue":
				await wrapper.setProps({
					modelValue: undefined,
					value: "Test2",
				});
				break;
			case "value":
				await wrapper.setProps({
					value: "Test2",
					modelValue: undefined,
				});
				break;
			case "icon":
				await wrapper.setProps({ icon: false });
				break;
			case "label":
				await wrapper.setProps({ label: "Label" });
				break;
			case "iconType":
				await wrapper.setProps({ iconType: "view" });
				break;
			default:
				break;
		}
		// Testen
		console.log(wrapper.html());
		switch (x) {
			case "disable":
			case "statistics":
			case "icon":
			case "iconType":
				expect(wrapper.find(idComponent).classes()).toContain(y);
				break;
			case "forceChecked":
			case "modelValue":
			case "value":
			case "label":
				expect(wrapper.find(idComponent).classes()).not.toContain(y);
				break;
			default:
				break;
		}
	});
});

describe("Bedingtes Rendern der HTML-Elemenete", () => {
	test("Span->icon wird gerendert, wenn props->icon true ist", () => {
		// Vorbereiten
		expect(wrapper.props("icon")).toBe(true);

		//Testen
		expect(wrapper.find(idIcon).exists()).toBeTruthy();
	});

	test("Span->icon wird nicht gerendert, wenn props->icon false ist", async () => {
		// Vorbereiten
		await wrapper.setProps({ icon: false });
		expect(wrapper.props("icon")).toBe(false);

		//Testen
		expect(wrapper.find(idIcon).exists()).toBeFalsy();
	});

	test("Span->iconType->view wird gerendert, wenn props->icon true und iconType view ist", async () => {
		// Vorbereiten
		expect(wrapper.props("icon")).toBe(true);
		await wrapper.setProps({ iconType: "view" });

		//Testen
		expect(wrapper.find(idIcon).find("span.i-ri-eye-line").exists()).toBeTruthy();
	});

	test("Span->iconType->view wird nicht gerendert, wenn props->icon true und iconType view ist", async () => {
		// Vorbereiten
		expect(wrapper.props("icon")).toBe(true);

		//Testen
		expect(wrapper.find(idIcon).find("span.i-ri-eye-line").exists()).toBeFalsy();
	});

	test("Span->iconType->view wird gerendert, wenn props->icon true und iconType view ist", async () => {
		// Vorbereiten
		expect(wrapper.props("icon")).toBe(true);

		//Testen
		expect(wrapper.find(idIcon).find("span.i-ri-eye-line").exists()).toBeFalsy()
	});

	test("Slot-Inhalt wird korrekt gerendert, wenn er übergeben wird.", () => {
		// Vorbereiten
		const wrapper = mount(SvwsUiRadioOption, { slots: { default: "Custom label" } });

		// Testen
		expect(wrapper.find("#"+wrapper.findComponent({ name: "SvwsUiRadioOption" }).vm.idIcon).text()).toContain("Custom label");
	});

	test("span-label wird mit einem leeren Wert für prop->label gerendert", async () => {
		//Testen
		expect(wrapper.find(idLabel).text()).toBe("");
	});

	test("span-label wird mit dem richtigen Wert für prop->label gerendert", async () => {
		// Vorbereiten
		expect(wrapper.find(idLabel).text()).toBe("");
		await wrapper.setProps({ label: "Test" });

		//Testen
		expect(wrapper.find(idLabel).text()).toBe("Test");
	});

	test("statistics-icon wird nicht gerendert, weil prop-statistic false ist", () => {
		//Testen
		expect(wrapper.find(idLabel).find("icon").exists()).toBeFalsy()
	});

	test("statistics-icon wird gerendert, weil prop-statistic true ist", async () => {
		// Vorbereiten
		await wrapper.setProps({ statistics: true });
		//Testen
		expect(wrapper.find(idLabel).find("span.icon.i-ri-bar-chart-2-line.icon-ui-statistic.inline-block").exists()).toBeTruthy();
		expect(wrapper.find(idLabel).find("span").classes()).toContain("-my-0.5");
	});
});

test("computed->checked->get liefert props.modelValue", async () => {
	// Vorbereiten
	await wrapper.setProps({ modelValue: "Test" });

	//Testen
	expect(wrapper.findComponent({ name: "SvwsUiRadioOption" }).vm.checked).toBe("Test");
});

test("computed->checked->set emittiert das Event update:modelValue mit dem richtigen Wert", async () => {
	// Vorbereiten
	await wrapper.setProps({ modelValue: "Test" });
	expect(wrapper.findComponent({ name: "SvwsUiRadioOption" }).vm.checked).toBe("Test");

	// Aktion
	wrapper.findComponent({ name: "SvwsUiRadioOption" }).vm.checked = "Test2";

	// Testen
	expect(wrapper.emitted("update:modelValue")?.at(0)?.at(0)).toBe("Test2");
});

test("emits->update:modelValue wird mit dem richtigem Parameterwert durchgereicht", async () => {
	// Vorbereiten
	await wrapper.setProps({ value: "Test" });

	// Aktion
	await wrapper.find(idInputField).trigger("change");

	// Testen
	expect(wrapper.emitted("update:modelValue")?.at(0)?.at(0)).toBe("Test");
});