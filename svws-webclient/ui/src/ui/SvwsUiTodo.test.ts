import { beforeEach, expect, test, afterEach, describe } from "vitest";
import type { VueWrapper } from "@vue/test-utils";
import { mount } from "@vue/test-utils";
import SvwsUiTodo from "./SvwsUiTodo.vue";

let wrapper: VueWrapper<InstanceType<typeof SvwsUiTodo>>;

beforeEach( () => {
	wrapper = mount(SvwsUiTodo, { props: { spinning: false}});
})

test("Rendert HTML korrekt", async () => {
	expect(wrapper.find("div.flex.flex-col.gap-4.mb-1").exists()).toBeTruthy();
	expect(wrapper.find("div.leading-tight.max-w-sm.opacity-50.inline-flex").exists()).toBeTruthy();
	expect(wrapper.find("div.leading-tight").classes().includes("gap-0.5")).toBeTruthy();
	expect(wrapper.find("div.flex.flex-col.gap-4.mb-1").text()).toBe("Dieser Bereich ist noch in Entwicklung.")

})

describe("Bedingstes Rendern", () => {
	test("Überschrift wird gerendert, wenn titel übergeben wird", async() => {
		// Vorbereiten
		await wrapper.setProps({ title: "Titel"});

		// Testen
		expect(wrapper.find("div.text-headline-md.inline-flex.gap-1.opacity-50").exists()).toBeTruthy();
		expect(wrapper.find("div.text-headline-md.inline-flex.gap-1.opacity-50").text()).toBe("Titel");
	})

	test("Icon Box mit Titel wird gerendert, wenn titel übergeben wird und hideIcon false ist.", async() => {
		// Vorbereiten
		await wrapper.setProps({ title: "Titel"});

		// Testen
		expect(wrapper.find("div.text-headline-md.inline-flex.gap-1.opacity-50").exists()).toBeTruthy();
		expect(wrapper.find("span.icon.i-ri-box-3-line.opacity-50").exists()).toBeTruthy();
	})

	test("Icon Box mit Titel wird nicht gerendert, wenn titel übergeben wird und hideIcon true ist.", async() => {
		// Vorbereiten
		await wrapper.setProps({ title: "Titel", hideIcon: true});

		// Testen
		expect(wrapper.find("div.text-headline-md.inline-flex.gap-1.opacity-50"));
		expect(wrapper.find("span.icon.i-ri-box-3-line.opacity-50").exists()).toBeFalsy();
	})

	test("Icon Box ohne Titel wird gerendert, wenn titel leer und hideIcon false sind.", () => {
		expect(wrapper.find("span.icon.i-ri-box-3-line.opacity-75.shrink-0").exists()).toBeTruthy();
	})

	test("Icon ohne Titel wird nicht gerendert, wenn titel leer und hideIcon true sind.", async() => {
		// Vorbereiten
		await wrapper.setProps({ hideIcon: true});

		// Testen
		expect(wrapper.find("span.icon.i-ri-box-3-line.opacity-75.shrink-0").exists()).toBeFalsy();
	})

	test("Default-Slot-Inhalt wird gerendert, wenn kein Slot-Inhalt definiert wird.", async() => {
		// Testen
		expect(wrapper.find("div.flex.flex-col.gap-4.mb-1").text()).toBe("Dieser Bereich ist noch in Entwicklung.")
	})

	test("Benutzerdefinierter Slot-Inhalt wird gerendert.", async() => {
		// Vorbereiten
		wrapper = mount(SvwsUiTodo,{
			slots: {
				default: '<p>Neuer Inhalt für den Slot</p>',
			},
		})

		// Testen
		expect(wrapper.find("div.flex.flex-col.gap-4.mb-1").text()).toBe("Neuer Inhalt für den Slot")
	})
})

afterEach(() => {
	wrapper.unmount();
})