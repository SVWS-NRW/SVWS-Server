import { describe, test, expect } from "vitest";
import { mount } from "@vue/test-utils";
import SvwsUiInputWrapper from "./SvwsUiInputWrapper.vue";

describe("SvwsUiInputWrapper", () => {
	// Testet, ob HTML gerendert wird
	test("Dummy Platzhalter test", () => {
		const wrapper = mount(SvwsUiInputWrapper);
		expect(wrapper.html()).toContain(">");
	});
});
