import { describe, test, expect } from "vitest";
import { mount } from "@vue/test-utils";
import SvwsUiButton from "./SvwsUiButton.vue";

describe("SvwsUiButton", () => {
	describe("Komponente kann gemounted werden", () => {
		test("HTML wird erzeugt", () => {
			const wrapper = mount(SvwsUiButton);
			expect(wrapper.html()).includes("text-input-component");
		});
	});
});
