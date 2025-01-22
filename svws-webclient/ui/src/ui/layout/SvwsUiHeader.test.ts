import { beforeEach, expect, test, afterEach } from "vitest";
import type { VueWrapper } from "@vue/test-utils";
import { mount } from "@vue/test-utils";
import SvwsUiHeader from "./SvwsUiHeader.vue";

let wrapper: VueWrapper<InstanceType<typeof SvwsUiHeader>>;

beforeEach( () => {
	wrapper = mount(SvwsUiHeader);
})

test("Rendert HTML korrekt", async () => {
	expect(wrapper.find("div.page--header").exists()).toBeTruthy();
	expect(wrapper.find("h2.text-headline").exists()).toBeTruthy();
})

test("Der übergeben Slot-Inhalt wird korrekt gerendert.", () => {
	// Vorbereiten
	wrapper = mount(SvwsUiHeader, {
		slots: {
			default: "Custom Slot Inhalt",
		},
	})

	// Testen
	expect(wrapper.text()).toContain("Custom Slot Inhalt");
})

afterEach(() => {
	wrapper.unmount();
})