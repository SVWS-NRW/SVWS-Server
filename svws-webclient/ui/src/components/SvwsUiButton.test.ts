import { describe, test, expect } from "vitest";
import { mount } from "@vue/test-utils";
import SvwsUiButton from "./SvwsUiButton.vue";
import type { ButtonType } from '../types';
describe("SvwsUiButton", () => {
	describe("Komponente kann gemounted werden", () => {
		test("HTML wird erzeugt", () => {
			const wrapper = mount(SvwsUiButton);
			expect(wrapper.html()).includes("<");
		});
	});
});

describe.concurrent("SvwsUiButton", () => {
	test.each([
		["primary", "button--primary", "Überprüft, ob die Klasse 'button--primary' für den Typ 'primary' korrekt gerendert wird."],
		["secondary", "button--secondary", "Überprüft, ob die Klasse 'button--secondary' für den Typ 'secondary' korrekt gerendert wird."],
		["error", "button--danger", "Überprüft, ob die Klasse 'button--danger' für den Typ 'error' korrekt gerendert wird."],
		["danger", "button--danger", "Überprüft, ob die Klasse 'button--danger' für den Typ 'danger' korrekt gerendert wird."],
		["transparent", "button--transparent", "Überprüft, ob die Klasse 'button--transparent' für den Typ 'transparent' korrekt gerendert wird."],
		["icon", "button--icon", "Überprüft, ob die Klasse 'button--icon' für den Typ 'icon' korrekt gerendert wird."],
		["trash", "button--trash", "Überprüft, ob die Klasse 'button--trash' für den Typ 'trash' korrekt gerendert wird."]
	])(
		'Mit Props type="%s" HTML enthält "%s" | %s"',
		async (_type, expectedClass, _) => {
			const type = _type as ButtonType
			const wrapper = mount(SvwsUiButton, { props: { type } });
			expect(wrapper.find("button").classes()).toContain(expectedClass);
		}
	);

	test.each([
		["small", "button--small", "Überprüft, ob die Klasse 'button--small' für das prop size 'small' korrekt gerendert wird."],
		["normal", "", "Überprüft, ob die Klasse 'button--normal' für den prop size 'normal' korrekt gerendert wird."],
		[undefined, "", "Überprüft, ob die Klasse 'button--normal' falls das prop size übergeben wurde, nicht gerendert wurde"],
		["big", "button--big", "Überprüft, ob die Klasse 'button--big' für den prop size 'big' korrekt gerendert wird."],
	])(
		'rendert die korrekte Klasse für size="%s"',
		async (_size, expectedClass, _) => {
			const size = _size as "normal" | "small" | "big" | undefined
			const wrapper = mount(SvwsUiButton, { props: { size } });
			if (expectedClass) {
				expect(wrapper.find("button").classes()).toContain(
					expectedClass
				);
			} else {
				expect(wrapper.find("button").classes()).not.toContain(
					"button--small"
				);
				expect(wrapper.find("button").classes()).not.toContain(
					"button--big"
				);
			}
		}
	);

	test("Button ist deaktiviert, wenn die Prop disabled wahr ist", () => {
		const wrapper = mount(SvwsUiButton, { props: { disabled: true } });
		expect(wrapper.find("button").attributes("disabled")).toBe("");
	});

	test("Button ist nicht deaktiviert, wenn die Prop disabled falsch ist", () => {
		const wrapper = mount(SvwsUiButton, { props: { disabled: false } });
		expect(wrapper.find("button").attributes("disabled")).toBeUndefined();
	});

	test("löst ein Klick-Emit aus, wenn geklickt und nicht deaktiviert", async () => {
		const wrapper = mount(SvwsUiButton, { props: { disabled: false } });
		await wrapper.find("button").trigger("click");
		expect(wrapper.emitted("click")).toHaveLength(1);
	});

	test("löst kein Klick-Emit aus, wenn geklickt und deaktiviert", async () => {
		const wrapper = mount(SvwsUiButton, { props: { disabled: true } });
		await wrapper.find("button").trigger("click");
		expect(wrapper.emitted("click")).toBeUndefined();
	});

	test('rendert das Mülleimer-Symbol, wenn type "trash" ist', () => {
		const wrapper = mount(SvwsUiButton, { props: { type: "trash" } });
		expect(wrapper.find(".button--trash-icon").exists()).toBe(true);
	});

	test("rendert den Badge-Slot, wenn vorhanden", () => {
		const wrapper = mount(SvwsUiButton, {
			slots: { badge: '<span class="badge">Badge</span>' },
		});
		expect(wrapper.find(".button--badge .badge").exists()).toBe(true);
	});
});
