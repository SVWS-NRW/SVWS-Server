import { beforeEach, expect, test, afterEach } from "vitest";
import SvwsUiSpacing from "./SvwsUiSpacing.vue";
import type { VueWrapper } from "@vue/test-utils";
import { mount } from "@vue/test-utils";

let wrapper: VueWrapper<InstanceType<typeof SvwsUiSpacing>>;

beforeEach( () => {
	wrapper = mount(SvwsUiSpacing);
})

test("Rendert HTML korrekt", async () => {
	expect(wrapper.find("div.svws-ui-spacing").exists()).toBeTruthy();
})

test("Korrekter CSS-Wert bei einem Mount mit default prop", () => {
	// Vorbereiten
	expect(wrapper.props('size')).toBe(1);

	// Testen
	expect(wrapper.find("div.svws-ui-spacing.svws-ui-spacing--2").exists()).toBeFalsy();
})

test("Korrekter CSS-Wert bei einem Mount mit prop size = 2", async () => {
	// Vorbereiten
	await wrapper.setProps({ size: 2})
	expect(wrapper.props('size')).toBe(2);

	// Testen
	expect(wrapper.find("div.svws-ui-spacing.svws-ui-spacing--2").exists()).toBeTruthy();
})

test("Slot-Element wird gerendert.", async () => {
	// Vorbereiten
	wrapper = mount(SvwsUiSpacing, {
		slots: { default: "Test Spacing Slot" },
	});

	// Testen
	expect(wrapper.text()).toContain("Test Spacing Slot");
});

afterEach(() => {
	wrapper.unmount();
})