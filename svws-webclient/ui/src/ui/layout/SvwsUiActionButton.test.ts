import { beforeEach, expect, test, afterEach, describe, vi } from "vitest";
import type { VueWrapper } from "@vue/test-utils";
import { mount } from "@vue/test-utils";
import SvwsUiButton from "../controls/SvwsUiButton.vue";
import SvwsUiSpinner from "../SvwsUiSpinner.vue";
import SvwsUiActionButton from "./SvwsUiActionButton.vue";

let wrapper: VueWrapper<InstanceType<typeof SvwsUiActionButton>>;

beforeEach( () => {
	wrapper = mount(SvwsUiActionButton, { props: { spinning: false}});
})

test("Rendert HTML korrekt, wenn spinning true ist", async () => {
	expect(wrapper.find("div.svws-ui-action-button").exists()).toBeTruthy();
	expect(wrapper.find("button.svws-ui-action-button--button").exists()).toBeTruthy();
	expect(wrapper.find("div.svws-icon").exists()).toBeTruthy();
	expect(wrapper.find("div.flex.flex-col.overflow-x-hidden.overflow-y-hidden").exists()).toBeTruthy();
})

describe("Bedingstes Rendern", () => {

	test("Default-Slot #icon wird mit default-prop-icon korrekt gerendert", async() => {
		// Vorbereiten
		await wrapper.setProps({ icon: "i-ri-settings-2-line"})

		// Testen
		expect(wrapper.find("span.icon.i-ri-settings-2-line").exists()).toBeTruthy();
	})

	test("Der übergebene Slot #icon wird korrekt gerendert", async() => {
		// Vorbereiten
		wrapper = mount(SvwsUiActionButton,{
			slots:{
				icon: "Custom Slot Inhalt",
			},
		})

		// Testen
		expect(wrapper.text()).toContain("Custom Slot Inhalt");
	})

	test("prop 'description' wird gerendert, wenn sie definiert ist.", async() => {
		// Vorbereiten
		await wrapper.setProps({ description: "Beschreibung"});

		// Testen
		expect(wrapper.find("div.svws-description").exists()).toBeTruthy();
		expect(wrapper.find("div.svws-description").text()).toContain("Beschreibung");
	})

	test("prop 'description' wird nicht gerendert, wenn sie nicht definiert ist.", async() => {
		// Testen
		expect(wrapper.find("div.svws-description").exists()).toBeFalsy();
	})

	test("prop 'description' wird nicht gerendert, wenn sie nicht definiert ist.", async() => {
		// Testen
		expect(wrapper.find("div.svws-description").exists()).toBeFalsy();
	})

	test("div.svws-ui-action-button--content wird mit einem Slot gerendert, wenn prop 'isActive true ist.", async() => {
		// Vorbereiten
		wrapper = mount(SvwsUiActionButton,{
			props:{ isActive: true},
			slots:{
				default: "Custom Slot Inhalt",
			},
		})

		// Testen
		expect(wrapper.find("div.svws-ui-action-button--content").exists()).toBeTruthy();
		expect(wrapper.find("div.svws-ui-action-button--content").text()).toContain("Custom Slot Inhalt")

	})

	test("div.svws-ui-action-button--content wird nicht gerendert, wenn prop 'isActive false ist.", async() => {
		// Testen
		expect(wrapper.find("div.svws-ui-action-button--content").exists()).toBeFalsy();
	})


	describe("Tests für die Komponente svws-ui-button mit isActive = true", () => {
		test("Die Komponente svws-ui-button wird korrekt gerendert, wenn actionFunction definiert ist..", async() => {
			// Vorbereiten
			wrapper = mount(SvwsUiActionButton,{
				props:{
					isActive: true,
					actionFunction: () => true,
				},
			})

			// Testen
			const svwsuibutton = wrapper.findComponent(SvwsUiButton);
			expect(svwsuibutton.exists()).toBeTruthy();
			expect(svwsuibutton.attributes('title')).toBe("Aktion ausführen")
		})

		test("Die Komponente svws-ui-button wird nicht gerendert, wenn actionFunction nicht definiert ist.", async() => {
			// Vorbereiten
			wrapper = mount(SvwsUiActionButton,{
				props:{
					isActive: true,
				},
			})

			// Testen
			const svwsuibutton = wrapper.findComponent(SvwsUiButton);
			expect(svwsuibutton.exists()).toBeFalsy();
		})

		test("Die Komponente svws-ui-button ist disabled, wenn actionDisabled true ist.", async() => {
			// Vorbereiten
			wrapper = mount(SvwsUiActionButton,{
				props:{
					isActive: true,
					actionFunction: () => true,
					actionDisabled: true,
				},
			})

			// Testen
			const svwsuibutton = wrapper.findComponent(SvwsUiButton);
			expect(svwsuibutton.attributes('disabled')).toBeDefined();
		})

		test("Die Komponente svws-ui-button ist nicht disabled, wenn actionDisabled false ist.", async() => {
			// Vorbereiten
			wrapper = mount(SvwsUiActionButton,{
				props:{
					isActive: true,
					actionFunction: () => true,
					actionDisabled: false,
				},
			})

			// Testen
			const svwsuibutton = wrapper.findComponent(SvwsUiButton);
			expect(svwsuibutton.attributes('disabled')).toBeUndefined();
		})

		test("Die Komponente svws-ui-button ist disabled, wenn isLoading true ist.", async() => {
			// Vorbereiten
			wrapper = mount(SvwsUiActionButton,{
				props:{
					isActive: true,
					actionFunction: () => true,
					isLoading: true,
				},
			})

			// Testen
			const svwsuibutton = wrapper.findComponent(SvwsUiButton);
			expect(svwsuibutton.attributes('disabled')).toBeDefined();
		})

		test("Die Komponente svws-ui-button ist nicht disabled, wenn isLoading false ist.", async() => {
			// Vorbereiten
			wrapper = mount(SvwsUiActionButton,{
				props:{
					isActive: true,
					actionFunction: () => true,
					isLoading: false,
				},
			})

			// Testen
			const svwsuibutton = wrapper.findComponent(SvwsUiButton);
			expect(svwsuibutton.attributes('disabled')).toBeUndefined();
		})

		test("Die Komponente svws-ui-spinner wird korrekt gerendert, wenn actionFunction definiert und isLoading true sing.", async() => {
			// Vorbereiten
			wrapper = mount(SvwsUiActionButton,{
				props:{
					isActive: true,
					actionFunction: () => true,
					isLoading: true,
				},
			})

			// Testen
			const svwsuispinner = wrapper.findComponent(SvwsUiSpinner);
			expect(svwsuispinner.exists()).toBeTruthy();
		})

		test("Play-Icon i-ri-play-line wird korrekt gerendert, wenn actionFunction definiert und isLoading false sind.", async() => {
			// Vorbereiten
			wrapper = mount(SvwsUiActionButton,{
				props:{
					isActive: true,
					actionFunction: () => true,
				},
			})

			// Testen
			const svwsuispinner = wrapper.findComponent(SvwsUiSpinner);
			expect(svwsuispinner.exists()).toBeFalsy();
			expect(wrapper.find("span.i-ri-play-line").exists()).toBeTruthy();
		})

		test(" Wert von actionLabel wird gerendert, wenn actionFunction und actionLabel definiert sind.", async() => {
			// Vorbereiten
			wrapper = mount(SvwsUiActionButton,{
				props:{
					isActive: true,
					actionFunction: () => true,
					actionLabel: "actionLabel",
				},
			})

			// Testen
			const svwsuibutton = wrapper.findComponent(SvwsUiButton);
			expect(svwsuibutton.text()).toContain("actionLabel");
		})

		test("Der Text 'Ausführen' gerendert, wenn actionFunction definiert, aber actionLabel nicht definiert ist.", async() => {
			// Vorbereiten
			wrapper = mount(SvwsUiActionButton,{
				props:{
					isActive: true,
					actionFunction: () => true,
				},
			})

			// Testen
			const svwsuibutton = wrapper.findComponent(SvwsUiButton);
			expect(svwsuibutton.text()).toContain("Ausführen");
		})
	})
});

describe("Tests für die CSS-Props", () => {

	test("Prop isActive wird an CSS nicht übergeben", async () => {
		expect(wrapper.find("div.svws-active").exists()).toBeFalsy();
	})

	test("Prop isActive wird an CSS übergeben", async () => {
		// Vorbereiten
		await wrapper.setProps({ isActive: true });

		// Testen
		expect(wrapper.find("div.svws-active").exists()).toBeTruthy();
	})

	test("Prop description wird an CSS übergeben", async () => {
		expect(wrapper.find("div.svws-title.my-auto").exists()).toBeTruthy();
	})

	test("Prop description wird an CSS nicht übergeben", async () => {
		// Vorbereiten
		await wrapper.setProps({ description: "Titel" });

		// Testen
		expect(wrapper.find("div.svws-active.my-auto").exists()).toBeFalsy();
	})

	test("CSS-Klasse pt-5, welche von slots.default abhängen, wird angewendet.", async () => {
		// Vorbereiten
		wrapper = mount(SvwsUiActionButton,{
			slots:{
				default: "Custom Slot Inhalt",
			},
		})
		await wrapper.setProps({ isActive: true});

		// Testen
		expect(wrapper.find("div.p-4.svws-ui-action-button--content.pt-5").exists()).toBeTruthy();
	})

	test("CSS-Klasse pt-5, welche von slots.default abhängen, wird nicht angewendet.", async () => {
		// Vorbereiten
		await wrapper.setProps({ isActive: true});

		// Testen
		expect(wrapper.find("div.p-4.svws-ui-action-button--content.pt-5").exists()).toBeFalsy();
	})

	test("CSS-Klasse mt-8, welche von slots.default abhängen, wird auf SvwsUiButton angewendet.", async () => {
		// Vorbereiten
		wrapper = mount(SvwsUiActionButton,{
			props:{
				isActive: true,
				actionFunction: () => true,
			},
			slots:{
				default: "Custom Slot Inhalt",
			},
		})

		// Testen
		expect(wrapper.findComponent(SvwsUiButton).classes().includes("mt-8")).toBeTruthy();
	})

	test("CSS-Klasse mt-8, welche von slots.default abhängen, wird nicht auf SvwsUiButton angewendet.", async () => {
		// Vorbereiten
		await wrapper.setProps({isActive: true,
			actionFunction: () => true})

		// Testen
		expect(wrapper.findComponent(SvwsUiButton).classes().includes("mt-8")).toBeFalsy();
	})
})

describe("Tests für Events/Emits", () => {
	test("Event-> löst click aus, wenn svws-ui-action-button geklickt wird.", async () => {
	// Vorbereiten
		const button = wrapper.find("button.svws-ui-action-button--button");

		// Aktion
		await button.trigger("click");

		// Testen
		const emittedEvents = wrapper.emitted("click");
		expect(emittedEvents).toBeTruthy();
		expect(emittedEvents?.length).toBeGreaterThan(0);
		if (emittedEvents !== undefined)
			expect(emittedEvents[0][0]).toBeInstanceOf(MouseEvent);
	});

	test("Event-> actionFunction wird aufgerufen, wenn svwsuibutton geklickt wird(synchronous).", async () => {
	// Vorbereiten
		const actionFunction = vi.fn();
		wrapper = mount(SvwsUiActionButton,{
			props: { actionFunction, isActive: true},
			slots:{
				default: "Custom Slot Inhalt",
			},
		})
		const svwsuibutton = wrapper.findComponent(SvwsUiButton);

		// Aktion
		await svwsuibutton.trigger("click");

		// Testen
		expect(actionFunction).toHaveBeenCalledTimes(1);
	})

	test("Event-> actionFunction wird aufgerufen, wenn svwsuibutton geklickt wird(asynchronous).", async () => {
	// Vorbereiten
	// Mock für die asynchrone actionFunction
		const actionFunction = vi.fn(async () => {
			return new Promise((resolve) => setTimeout(resolve, 100));
		});
		wrapper = mount(SvwsUiActionButton,{
			props: { actionFunction, isActive: true},
			slots:{
				default: "Custom Slot Inhalt",
			},
		})
		const svwsuibutton = wrapper.findComponent(SvwsUiButton);

		// Aktion
		await svwsuibutton.trigger("click");

		// Testen
		expect(actionFunction).toHaveBeenCalledTimes(1);
	})
})

afterEach(() => {
	wrapper.unmount();
})