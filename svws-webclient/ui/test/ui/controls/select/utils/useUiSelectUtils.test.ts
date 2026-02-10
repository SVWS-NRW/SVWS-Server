import { type Ref, ref } from "vue";
import type { VueWrapper } from "@vue/test-utils";
import { mount } from "@vue/test-utils";
import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";
import UiSelect from "../../../../../src/ui/controls/select/UiSelect.vue";
import { SelectManager } from "../../../../../src/ui/controls/select/manager/SelectManager";

type MockBounding = {
	x: Ref<number>;
	y: Ref<number>;
	width: Ref<number>;
	height: Ref<number>;
	top: Ref<number>;
	left: Ref<number>;
	right: Ref<number>;
	bottom: Ref<number>;
};

let mockBounding: MockBounding;

const createMockBounding = () => ({
	x: ref(0),
	y: ref(0),
	width: ref(0),
	height: ref(0),
	top: ref(0),
	left: ref(0),
	right: ref(0),
	bottom: ref(0),
});

vi.mock('@vueuse/core', async () => {
	const actual = vi.importActual('@vueuse/core');
	return {
		...await actual,
		useElementBounding: () => mockBounding,
	};
});

beforeEach(() => {
	mockBounding = createMockBounding();
	HTMLElement.prototype.showPopover = vi.fn(function(this: HTMLElement) {
		this.dataset.popoverOpen = 'true';
	});
	HTMLElement.prototype.hidePopover = vi.fn(function(this: HTMLElement) {
		delete this.dataset.popoverOpen;
	});

	vi.stubGlobal('innerHeight', 1000);
});

describe("UiSelect Utils", () => {
	const { manager, firstOption } = createTestData();
	let scrollHeightSpy: ReturnType<typeof vi.spyOn>;
	let scrollTopSpy: ReturnType<typeof vi.spyOn>;

	describe.concurrent("Selektion", () => {
		test("Nach der Selektion wird das Dropdown geschlossen", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

			const { combobox, dropdown } = getElements(wrapper);

			await combobox.trigger("click");

			expect(dropdown.attributes("data-popover-open")).toBe("true");

			const firstOption = dropdown.find("li");

			await firstOption.trigger('mousedown');
			expect(dropdown.attributes("data-popover-open")).toBeUndefined();
		});

		test("Wenn die Option noch nicht selektiert ist, wird sie durch Klick selektiert", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

			const { combobox, dropdown } = getElements(wrapper);

			// Dropdown öffnen
			await combobox.trigger("click");

			const firstDropdownOption = dropdown.find("li");

			await firstDropdownOption.trigger('mousedown');
			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toBe(firstOption);
		});

		test("Wenn die Option schon selektiert ist und removable = true, wird sie durch Klick deselektiert", async () => {
			const wrapper = mount(UiSelect<cars>, {
				props: { manager: manager, removable: true, modelValue: firstOption },
			});

			const { combobox, dropdown } = getElements(wrapper);

			// Dropdown öffnen
			await combobox.trigger("click");

			const firstDropdownOption = dropdown.find("li");

			await firstDropdownOption.trigger('mousedown');
			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toBeUndefined();
		});

		test("Wenn die Option schon selektiert ist und removable = false, wird sie durch Klick nicht deselektiert", async () => {
			const wrapper = mount(UiSelect<cars>, {
				props: { manager: manager, removable: false, modelValue: firstOption },
			});

			const { combobox, dropdown } = getElements(wrapper);

			// Dropdown öffnen
			await combobox.trigger("click");

			const firstDropdownOption = dropdown.find("li");

			await firstDropdownOption.trigger('mousedown');
			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeUndefined();
		});

		test("Wenn eine Option geklickt wird, wird der Suchbegriff zurückgesetzt", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });

			const { combobox, dropdown, input, vm } = getElements(wrapper);

			// Dropdown öffnen
			await combobox.trigger("click");
			await input.setValue("Sub");
			expect(vm.state.search).toBe("Sub");

			const firstDropdownOption = dropdown.find("li");

			await firstDropdownOption.trigger('mousedown');

			expect(vm.state.search).toBe("");
		});
	});

	describe("Dropdown Styles", () => {

		afterEach(() => {
			if (scrollHeightSpy !== undefined) {
				scrollHeightSpy.mockRestore();
			}
			if (scrollTopSpy !== undefined) {
				scrollTopSpy.mockRestore();
			}
		});

		test("Das Dropdown wird unter dem Select angezeigt, wenn Platz da ist", async () => {
			mockBounding.x.value = 150;
			mockBounding.y.value = 50;
			mockBounding.width.value = 200;
			mockBounding.height.value = 40;
			mockBounding.top.value = 50;
			mockBounding.left.value = 150;
			mockBounding.right.value = 200;
			mockBounding.bottom.value = 90;

			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

			// Dropdown öffnen
			const { combobox, vm } = getElements(wrapper);
			await combobox.trigger('click');

			const positionStyles = vm.dropdownPositionStyles;

			expect(positionStyles.top).toBe("93px");
			expect(positionStyles.left).toBe("150px");
			expect(positionStyles.width).toBe("200px");
			expect(positionStyles.maxHeight).toBe("235px");
		});

		test("Das Dropdown wird über dem Select angezeigt, wenn unten zu wenig Platz ist", async () => {
			mockBounding.x.value = 150;
			mockBounding.y.value = 900;
			mockBounding.width.value = 200;
			mockBounding.height.value = 40;
			mockBounding.top.value = 900;
			mockBounding.left.value = 150;
			mockBounding.right.value = 200;
			mockBounding.bottom.value = 940;

			scrollHeightSpy = vi.spyOn(HTMLElement.prototype, 'scrollHeight', 'get').mockReturnValue(800);

			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

			// Dropdown öffnen
			const { combobox, vm } = getElements(wrapper);
			await combobox.trigger('click');

			const positionStyles = vm.dropdownPositionStyles;

			expect(positionStyles.top).toBe("663px");
			expect(positionStyles.left).toBe("150px");
			expect(positionStyles.width).toBe("200px");
			expect(positionStyles.maxHeight).toBe("235px");
		});

		test("Das Dropdown ist maximal 235px hoch", async () => {
			mockBounding.x.value = 150;
			mockBounding.y.value = 50;
			mockBounding.width.value = 200;
			mockBounding.height.value = 40;
			mockBounding.top.value = 50;
			mockBounding.left.value = 150;
			mockBounding.right.value = 200;
			mockBounding.bottom.value = 90;

			scrollHeightSpy = vi.spyOn(HTMLElement.prototype, 'scrollHeight', 'get').mockReturnValue(800);

			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

			// Dropdown öffnen
			const { combobox, vm } = getElements(wrapper);
			await combobox.trigger('click');

			const positionStyles = vm.dropdownPositionStyles;

			expect(positionStyles.top).toBe("93px");
			expect(positionStyles.left).toBe("150px");
			expect(positionStyles.width).toBe("200px");
			expect(positionStyles.maxHeight).toBe("235px");
		});

		test("Das Dropdown nimmt nur den vorhandenen Platz ein", async () => {
			mockBounding.x.value = 150;
			mockBounding.y.value = 800;
			mockBounding.width.value = 200;
			mockBounding.height.value = 40;
			mockBounding.top.value = 800;
			mockBounding.left.value = 150;
			mockBounding.right.value = 200;
			mockBounding.bottom.value = 840;
			scrollHeightSpy = vi.spyOn(HTMLElement.prototype, 'scrollHeight', 'get').mockReturnValue(800);

			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

			// Dropdown öffnen
			const { combobox, vm } = getElements(wrapper);
			await combobox.trigger('click');

			const positionStyles = vm.dropdownPositionStyles;

			expect(positionStyles.top).toBe("843px");
			expect(positionStyles.left).toBe("150px");
			expect(positionStyles.width).toBe("200px");
			expect(positionStyles.maxHeight).toBe("155px");
		});

		test("Bei einer Navigation nach unten, wird das Dropdown zur unteren Optionsgrenze gescrollt, falls die Option außerhalb des Sichtbereiches ist", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager }, attachTo: document.body });
			const { combobox, dropdown } = getElements(wrapper);

			// Größen und Abstände mocken
			const options = dropdown.findAll("li");
			options.forEach((item, i) => {
				Object.defineProperty(item.element, "offsetTop", { get: () => i * 20 });
				Object.defineProperty(item.element, "offsetHeight", { get: () => 20 });
			});
			Object.defineProperty(dropdown.element, "clientHeight", { get: () => 100 });

			// Dropdown öffnen durch Navigation nach unten ein Element hervorheben, das nicht sichtbar ist
			await combobox.trigger('click');
			for (let i = 0; i < 9; i++) {
				await combobox.trigger('keydown', { key: 'ArrowDown' });
			}

			expect(dropdown.element.scrollTop).toBe(86);

			wrapper.unmount();
		});

		test("Bei einer Navigation nach unten, wird das Dropdown zur unteren Optionsgrenze gescrollt, falls die Option außerhalb des Sichtbereiches ist", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager }, attachTo: document.body });
			const { combobox, dropdown } = getElements(wrapper);

			// Größen und Abstände mocken
			const options = dropdown.findAll("li");
			options.forEach((item, i) => {
				Object.defineProperty(item.element, "offsetTop", { get: () => i * 20 });
				Object.defineProperty(item.element, "offsetHeight", { get: () => 20 });
			});
			Object.defineProperty(dropdown.element, "clientHeight", { get: () => 100 });

			// Dropdown öffnen durch Navigation nach oben ein Element hervorheben, das nicht sichtbar ist
			for (let i = 0; i < 9; i++) {
				await combobox.trigger('keydown', { key: 'ArrowUp' });
			}

			expect(dropdown.element.scrollTop).toBe(314);

			wrapper.unmount();
		});

		test("Das Dropdown wird zurück nach oben gescrollt, wenn kein Element hervorgehoben ist", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager }, attachTo: document.body });
			const { combobox, dropdown } = getElements(wrapper);

			// Größen und Abstände mocken
			const options = dropdown.findAll("li");
			options.forEach((item, i) => {
				Object.defineProperty(item.element, "offsetTop", { get: () => i * 20 });
				Object.defineProperty(item.element, "offsetHeight", { get: () => 20 });
			});
			Object.defineProperty(dropdown.element, "clientHeight", { get: () => 100 });

			// Dropdown öffnen
			await combobox.trigger("click");

			expect(dropdown.element.scrollTop).toBe(0);

			wrapper.unmount();
		});
	});

	describe("Dropdown Sichtbarkeit", () => {

		afterEach(() => {
			if (scrollHeightSpy !== undefined) {
				scrollHeightSpy.mockRestore();
			}
		});

		test("Das Dropdown geht auf und zu, wenn mehrfach darauf geklickt wird", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
			const { combobox, dropdown } = getElements(wrapper);


			expect(dropdown.attributes("data-popover-open")).toBeUndefined();

			// Dropdown öffnen
			await combobox.trigger('click');

			expect(dropdown.attributes("data-popover-open")).toBe("true");

			// Dropdown schließen
			await combobox.trigger('click');

			expect(dropdown.attributes("data-popover-open")).toBeUndefined();
		});

		test("Beim Öffnen des Dropdowns wird dessen Position und Größe neu berechnet", async () => {
			mockBounding.x.value = 150;
			mockBounding.y.value = 50;
			mockBounding.width.value = 200;
			mockBounding.height.value = 40;
			mockBounding.top.value = 50;
			mockBounding.left.value = 150;
			mockBounding.right.value = 200;
			mockBounding.bottom.value = 90;

			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
			const { combobox } = getElements(wrapper);


			const oldPositionStyles = wrapper.findComponent({ name: "UiSelect" }).vm.dropdownPositionStyles;

			// Dropdown öffnen
			await combobox.trigger('click');

			expect(oldPositionStyles).toEqual({ top: '3px', left: '0px', width: '0px', maxHeight: '235px' });

			const newPositionStyles = wrapper.findComponent({ name: "UiSelect" }).vm.dropdownPositionStyles;
			expect(newPositionStyles).toEqual({ top: '93px', left: '150px', width: '200px', maxHeight: '235px' });
		});

		test("Beim Öffnen des Dropdowns wird die Scrollposition auf 0 gesetzt, wenn kein Element hervorgehoben ist", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
			const { combobox, dropdown } = getElements(wrapper);

			dropdown.element.scrollTop = 20;
			expect(dropdown.element.scrollTop).toBe(20);

			// Dropdown öffnen
			await combobox.trigger('click');
			expect(dropdown.element.scrollTop).toBe(0);
		});

		test("Beim Öffnen des Dropdowns wird ein EventListener auf window resize gesetzt", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
			const { combobox } = getElements(wrapper);

			const eventSpy = vi.spyOn(globalThis, "addEventListener");

			// Dropdown öffnen
			await combobox.trigger('click');
			expect(eventSpy).toHaveBeenCalledWith("resize", expect.any(Function));
			eventSpy.mockRestore();
		});

		test("Beim Schließen des Dropdowns wird ein EventListener auf window resize entfernt", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
			const { combobox } = getElements(wrapper);

			const eventSpy = vi.spyOn(globalThis, "removeEventListener");

			// Dropdown öffnen und schließen
			await combobox.trigger('click');
			await combobox.trigger('click');

			expect(eventSpy).toHaveBeenCalledWith("resize", expect.any(Function));
			eventSpy.mockRestore();
		});

		test("Das Dropdown schließt sich bei einem window resize", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
			const { combobox, dropdown } = getElements(wrapper);


			// Dropdown öffnen
			await combobox.trigger('click');

			globalThis.dispatchEvent(new Event("resize"));

			expect(dropdown.attributes("data-popover-open")).toBeUndefined();
		});

		test("Das Dropdown schließt sich nicht, wenn sich die Position verändert, aber das Dropdown gerade geöffnet wurde", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
			const { combobox, dropdown, vm } = getElements(wrapper);

			// Dropdown öffnen
			await combobox.trigger('click');

			mockBounding.x.value = 50;
			mockBounding.left.value = 50;
			await vm.$nextTick();

			expect(dropdown.attributes("data-popover-open")).toBe("true");
		});

		test("Das Dropdown schließt sich, wenn sich die Position verändert, aber das Dropdown schon offen war", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
			const { combobox, dropdown, vm } = getElements(wrapper);
			vi.useFakeTimers();
			// Dropdown öffnen
			await combobox.trigger('click');
			vi.runAllTimers();

			mockBounding.x.value = 50;
			await vm.$nextTick();

			expect(dropdown.attributes("data-popover-open")).toBeUndefined();
		});
	});

	describe("Fokusfunktionen", () => {
		test("Beim Öffnen des Dropdowns wird die Combobox fokussiert (searchable = false)", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager }, attachTo: document.body });
			const { combobox } = getElements(wrapper);

			// Dropdown öffnen
			await combobox.trigger('click');

			expect(document.activeElement).toBe(combobox.element);
			wrapper.unmount();
		});

		test("Beim Öffnen des Dropdowns wird das Input fokussiert (searchable = true)", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true }, attachTo: document.body });
			const { combobox, input } = getElements(wrapper);

			// Dropdown öffnen
			await combobox.trigger('click');

			expect(document.activeElement).toBe(input.element);
			wrapper.unmount();
		});

		test.each([
			["disabled = true", { disabled: true }],
			["readonly = true", { readonly: true }],
		])("%s: Combobox ist nicht fokussierbar", async (_, props) => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, ...props }, attachTo: document.body });
			const { combobox } = getElements(wrapper);

			await combobox.trigger("focus");

			expect(document.activeElement).not.toBe(combobox.element);
			wrapper.unmount();
		});

		test("Klicke außerhalb der Komponente deaktiviert diese", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });
			const { combobox, dropdown, input, vm } = getElements(wrapper);
			const eventSpy = vi.spyOn(document, "removeEventListener");

			// Dropdown öffnen und Suchbegriff setzen
			await combobox.trigger('click');
			await input.setValue("Subaru");

			expect(dropdown.attributes("data-popover-open")).toBe("true");
			expect(vm.state.search).toBe("Subaru");

			document.body.dispatchEvent(new MouseEvent('click', { bubbles: true }));

			expect(dropdown.attributes("data-popover-open")).toBeUndefined();
			expect(vm.state.search).toBe("");
			expect(eventSpy).toHaveBeenCalledWith("click", expect.any(Function));
			eventSpy.mockRestore();
		});

		test("Focus out deaktiviert das Select", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });
			const { combobox, dropdown, input, vm } = getElements(wrapper);
			const eventSpy = vi.spyOn(document, "removeEventListener");
			vi.useFakeTimers();

			// Dropdown öffnen und Suchbegriff setzen
			await combobox.trigger('click');
			await input.setValue("Subaru");

			expect(dropdown.attributes("data-popover-open")).toBe("true");
			expect(vm.state.search).toBe("Subaru");

			input.element.dispatchEvent(new FocusEvent('focusout', { bubbles: true }));
			vi.runAllTimers();

			expect(dropdown.attributes("data-popover-open")).toBeUndefined();
			expect(vm.state.search).toBe("");
			expect(eventSpy).toHaveBeenCalledWith("click", expect.any(Function));
			eventSpy.mockRestore();
		});

		test("Input blur setzt focusOnInput auf false", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });
			const { input, vm } = getElements(wrapper);

			input.element.dispatchEvent(new FocusEvent('blur', { bubbles: true }));
			expect(vm.focusOnInput).toBe(false);
		});

		test("Wenn eine Option selektiert wird, wird der Fokus wieder auf die Combobox gesetzt (searchable = false)", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false }, attachTo: document.body });

			const { combobox, dropdown } = getElements(wrapper);
			vi.useFakeTimers();

			// Dropdown öffnen
			await combobox.trigger("click");

			const firstDropdownOption = dropdown.find("li");
			await firstDropdownOption.trigger('mousedown');
			vi.runAllTimers();

			expect(document.activeElement).toBe(combobox.element);
			wrapper.unmount();
		});

		test("Wenn eine Option selektiert wird, wird der Fokus wieder auf das Search-Input gesetzt (searchable = true)", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true }, attachTo: document.body });

			const { input, combobox, dropdown } = getElements(wrapper);
			vi.useFakeTimers();

			// Dropdown öffnen
			await combobox.trigger("click");

			const firstDropdownOption = dropdown.find("li");
			await firstDropdownOption.trigger('mousedown');
			vi.runAllTimers();

			expect(document.activeElement).toBe(input.element);
			wrapper.unmount();
		});

	});

	describe.concurrent("Styles", () => {
		test("Von außen gesetzte Attribute werden an den Root-Knoten gesetzt", () => {
			const wrapper = mount(UiSelect<cars>, {
				props: { manager: manager },
				attrs: { class: "my-class another-class" },
			});

			const { select } = getElements(wrapper);

			expect(select.classes()).toContain("my-class");
			expect(select.classes()).toContain("another-class");
		});

		test("Mit headless = true wird für Padding py-0 genutzt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: true } });

			const { combobox } = getElements(wrapper);

			expect(combobox.find("div").classes()).toContain("py-0");
		});

		test("Mit headless = false wird für Padding py-1 genutzt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: false } });

			const { combobox } = getElements(wrapper);

			expect(combobox.find("div").classes()).toContain("py-1");
		});

		describe("Icon-Farben", () => {
			test("Alle Icons sind bei disabled = true auf 'icon-ui-disabled' gesetzt", async () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, disabled: true },
					attrs: { class: "icon-ui-success" },
				});

				const iconSpans = wrapper.findAll('span.icon, span[class^="icon"]');
				iconSpans.forEach((iconSpan) => expect(iconSpan.classes()).toContain("icon-ui-disabled"));

				await wrapper.setProps({ modelValue: firstOption });
				const { label } = getElements(wrapper);
				const labelIconSpans = label.findAll('span.icon, span[class^="icon"]');
				labelIconSpans.forEach((iconSpan) => expect(iconSpan.classes()).toContain("icon-ui-disabled"));
			});

			test("Von außen gesetzte CSS-Klassen werden nicht an Root-Knoten gesetzt, sondern an bestimmte Icons", async () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, statistics: true, modelValue: firstOption },
					attrs: { class: "icon-ui-success" },
				});

				const { select, label } = getElements(wrapper);

				expect(select.classes()).not.toContain("icon-ui-success");
				expect(wrapper.find(".ui-select--icons-right span").classes()).toContain("icon-ui-success");
				expect(wrapper.find(".ui-select--icons-right button span").classes()).toContain("icon-ui-success");
				// Statistik-Icon bleibt unberührt
				expect(label.find(".ui-select--label--statistics span").classes()).not.toContain("icon-ui-success");

				await wrapper.setProps({ required: true });
				expect(label.find(".ui-select--label--required span").classes()).toContain("icon-ui-success");

				await wrapper.setProps({ required: false, readonly: true });
				expect(label.find(".ui-select--label--readonly span").classes()).toContain("icon-ui-success");

				await wrapper.setProps({ modelValue: undefined });
				expect(label.find(".ui-select--label--readonly span").classes()).toContain("icon-ui-success-secondary");

				await wrapper.setProps({ readonly: false, modelValue: firstOption, headless: true });

				expect(wrapper.find(".ui-select--icons-left span").classes()).toContain("icon-ui-success");
				expect(wrapper.find(".ui-select--icons-left button span").classes()).toContain("icon-ui-success");
			});

			test.each([
				["icon-uistatic", "icon-uistatic-25"],
				["icon-ui", "icon-ui-secondary"],
				["icon-ui-brand", "icon-ui-brand-secondary"],
				["icon-ui-statistic", "icon-ui-statistic-secondary"],
				["icon-ui-selected", "icon-ui-selected-secondary"],
				["icon-ui-danger", "icon-ui-danger-secondary"],
				["icon-ui-success", "icon-ui-success-secondary"],
				["icon-ui-warning", "icon-ui-warning-secondary"],
				["icon-ui-caution", "icon-ui-caution-secondary"],
				["icon-ui-neutral", "icon-ui-neutral-secondary"],
				["icon-ui-disabled", "icon-ui-disabled-secondary"],
				["icon-ui-onbrand", "icon-ui-onbrand-secondary"],
				["icon-ui-onstatistic", "icon-ui-onstatistic-secondary"],
				["icon-ui-onselected", "icon-ui-onselected-secondary"],
				["icon-ui-ondanger", "icon-ui-ondanger-secondary"],
				["icon-ui-onsuccess", "icon-ui-onsuccess-secondary"],
				["icon-ui-onwarning", "icon-ui-onwarning-secondary"],
				["icon-ui-oncaution", "icon-ui-oncaution-secondary"],
				["icon-ui-onneutral", "icon-ui-onneutral-secondary"],
				["icon-ui-ondisabled", "icon-ui-ondisabled-secondary"],
				["icon-ui-unknown", "icon-ui-secondary"], // default case
			])("Für die Iconfarbe %s wird sie sekundäre Icon-Farbe %s gesetzt", (iconColor, secondaryColor) => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, readonly: true },
					attrs: { class: iconColor } });
				const { label } = getElements(wrapper);

				expect(label.find(".ui-select--label--readonly span").classes()).toContain(secondaryColor);
			});
		});

		describe("Background-Farben", () => {
			test("Von außen gesetzte CSS-Klassen werden nicht an den Root-Knoten, sondern an die Combobox gesetzt", () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, headless: false },
					attrs: { class: "bg-ui-success" },
				});

				const { combobox, select } = getElements(wrapper);

				expect(select.classes()).not.toContain("bg-ui-success");
				expect(combobox.classes()).toContain("bg-ui-success");
			});

			test("Bei headless = true hat die Combobox keine Hintergrundfarbe", () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, headless: true },
				});

				const { combobox } = getElements(wrapper);

				expect(combobox.classes().some(c => c.startsWith('bg-ui'))).toBe(false);
			});

			test("Bei headless = false hat die Combobox die Hintergrundfarbe 'bg-ui'", () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, headless: false },
				});

				const { combobox } = getElements(wrapper);

				expect(combobox.classes()).toContain("bg-ui");
			});
		});

		describe("Text-Farben", () => {
			test("Alle Textfarben sind bei disabled = true auf 'text-ui-disabled' gesetzt", async () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, disabled: true },
					attrs: { class: "text-ui-success" },
				});

				const textElements = wrapper.findAll('*').filter(el => el.text().trim().length > 0);

				textElements.forEach(textElement => expect(textElement.classes()).toContain("text-ui-disabled"));

				await wrapper.setProps({ modelValue: firstOption });
				const { label } = getElements(wrapper);

				expect(label.classes()).toContain("text-ui-disabled");
			});

			test("Von außen gesetzte Text-Farbklasse verändert den Label- und Selektionstext", async () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, label: "Mein Label" },
					attrs: { class: "text-ui-success" },
				});

				const { label } = getElements(wrapper);

				expect(label.find(".ui-select--label--text").classes()).toContain("text-ui-success-secondary");

				await wrapper.setProps({ modelValue: firstOption });

				expect(label.find(".ui-select--label--text").classes()).toContain("text-ui-success");
				expect(wrapper.find(".ui-select--selection div").classes()).toContain("text-ui-success");
			});

			test.each([
				["text-uistatic", "text-uistatic-25"],
				["text-ui", "text-ui-secondary"],
				["text-ui-brand", "text-ui-brand-secondary"],
				["text-ui-statistic", "text-ui-statistic-secondary"],
				["text-ui-selected", "text-ui-selected-secondary"],
				["text-ui-danger", "text-ui-danger-secondary"],
				["text-ui-success", "text-ui-success-secondary"],
				["text-ui-warning", "text-ui-warning-secondary"],
				["text-ui-caution", "text-ui-caution-secondary"],
				["text-ui-neutral", "text-ui-neutral-secondary"],
				["text-ui-disabled", "text-ui-disabled-secondary"],
				["text-ui-onbrand", "text-ui-onbrand-secondary"],
				["text-ui-onstatistic", "text-ui-onstatistic-secondary"],
				["text-ui-onselected", "text-ui-onselected-secondary"],
				["text-ui-ondanger", "text-ui-ondanger-secondary"],
				["text-ui-onsuccess", "text-ui-onsuccess-secondary"],
				["text-ui-onwarning", "text-ui-onwarning-secondary"],
				["text-ui-oncaution", "text-ui-oncaution-secondary"],
				["text-ui-onneutral", "text-ui-onneutral-secondary"],
				["text-ui-ondisabled", "text-ui-ondisabled-secondary"],
				["text-ui-unknown", "text-ui-secondary"], // default case
			])(
				"Für die Textfarbe %s wird die sekundäre Textfarbe %s gesetzt",
				(textColor, secondaryColor) => {
					const wrapper = mount(UiSelect<cars>, {
						props: { manager: manager, readonly: true },
						attrs: { class: textColor },
					});
					const { label } = getElements(wrapper);

					expect(label.find(".ui-select--label--text").classes()).toContain(secondaryColor);
				}
			);
		});

		describe("Border-Farben", () => {
			test("Alle Borderfarben sind bei disabled = true auf 'border-ui-disabled' gesetzt", () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, disabled: true },
					attrs: { class: "border-ui-success" },
				});

				const { combobox } = getElements(wrapper);

				expect(combobox.classes()).toContain("border-ui-disabled");
			});

			test("Von außen gesetzte Border-Farbklasse verändert den Rahmen der Combobox", () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager },
					attrs: { class: "border-ui-success" },
				});

				const { combobox } = getElements(wrapper);

				expect(combobox.classes()).toContain("border-ui-success");
			});
		});

		describe("Combobox Styling und Attribute", () => {
			test("Aria Attribute bei editierbarem Select und searchable = false sind richtig gesetzt.", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

				const { combobox, vm } = getElements(wrapper);

				expect(combobox.attributes("aria-labelledby")).toBe(`uiSelectLabel_${vm.state.instanceId}`);
				expect(combobox.attributes("aria-controls")).toBe(`uiSelectDropdown_${vm.state.instanceId}`);
				expect(combobox.attributes("aria-autocomplete")).toBe("none");
				expect(combobox.attributes("aria-expanded")).toBe("false");
				// Dropdown öffnen
				await combobox.trigger("click");
				expect(combobox.attributes("aria-expanded")).toBe("true");
				expect(combobox.attributes("aria-disabled")).toBeUndefined();
				expect(combobox.attributes("aria-activedescendant")).toBeUndefined();
				// Erste Option hervorheben
				await combobox.trigger('keydown', { key: 'ArrowDown' });
				expect(combobox.attributes("aria-activedescendant")).toBe(`uiSelectOption_0_${vm.state.instanceId}`);
			});

			test.each([
				["readonly", { readonly: true }],
				["disabled", { disabled: true }],
			])("Aria Attribute bei %s = true und searchable = false sind richtig gesetzt.", (_, props) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, ...props } });

				const { combobox, vm } = getElements(wrapper);

				expect(combobox.attributes("aria-labelledby")).toBe(`uiSelectLabel_${vm.state.instanceId}`);
				expect(combobox.attributes("aria-controls")).toBeUndefined();
				expect(combobox.attributes("aria-autocomplete")).toBeUndefined();
				expect(combobox.attributes("aria-expanded")).toBeUndefined();
				const disabledValue = (wrapper.props().disabled === true) ? "true" : undefined;
				expect(combobox.attributes("aria-disabled")).toBe(disabledValue);
				expect(combobox.attributes("aria-activedescendant")).toBeUndefined();
			});

			test("Bei searchable = true und editierbarem Select werden keine Attribute gesetzt", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

				const { combobox, vm } = getElements(wrapper);

				expect(combobox.attributes("aria-labelledby")).toBe(`uiSelectLabel_${vm.state.instanceId}`);
				expect(combobox.attributes("aria-controls")).toBe(`uiSelectDropdown_${vm.state.instanceId}`);
				expect(combobox.attributes("aria-autocomplete")).toBe("none");
				expect(combobox.attributes("aria-expanded")).toBe("false");
				// Dropdown öffnen
				await combobox.trigger("click");
				expect(combobox.attributes("aria-expanded")).toBe("true");
				expect(combobox.attributes("aria-disabled")).toBeUndefined();
				expect(combobox.attributes("aria-activedescendant")).toBeUndefined();
				// Erste Option hervorheben
				await combobox.trigger('keydown', { key: 'ArrowDown' });
				expect(combobox.attributes("aria-activedescendant")).toBe(`uiSelectOption_0_${vm.state.instanceId}`);
			});

			test.each([
				["readonly", { readonly: true }],
				["disabled", { disabled: true }],
			])("Bei searchable = true und nicht editierbarem Select werden keine Attribute gesetzt", (_, props) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true, ...props } });

				const { combobox, vm } = getElements(wrapper);

				expect(combobox.attributes("aria-labelledby")).toBe(`uiSelectLabel_${vm.state.instanceId}`);
				expect(combobox.attributes("aria-controls")).toBeUndefined();
				expect(combobox.attributes("aria-autocomplete")).toBeUndefined();
				expect(combobox.attributes("aria-expanded")).toBeUndefined();
			});

			test.each([
				["readonly", { readonly: true }],
				["disabled", { disabled: true }],
			])("Aria Attribute bei %s = true und searchable = false sind richtig gesetzt.", (_, props) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, ...props } });

				const { combobox, vm } = getElements(wrapper);

				expect(combobox.attributes("aria-labelledby")).toBe(`uiSelectLabel_${vm.state.instanceId}`);
				expect(combobox.attributes("aria-controls")).toBeUndefined();
				expect(combobox.attributes("aria-autocomplete")).toBeUndefined();
				expect(combobox.attributes("aria-expanded")).toBeUndefined();
				const disabledValue = (wrapper.props().disabled === true) ? "true" : undefined;
				expect(combobox.attributes("aria-disabled")).toBe(disabledValue);
				expect(combobox.attributes("aria-activedescendant")).toBeUndefined();
			});

			test("Bei searchable = true werden keine Attribute gesetzt", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

				const { combobox, vm } = getElements(wrapper);

				expect(combobox.attributes("aria-labelledby")).toBe(`uiSelectLabel_${vm.state.instanceId}`);
				expect(combobox.attributes("aria-controls")).toBe(`uiSelectDropdown_${vm.state.instanceId}`);
				expect(combobox.attributes("aria-autocomplete")).toBe("none");
				expect(combobox.attributes("aria-expanded")).toBe("false");
				// Dropdown öffnen
				await combobox.trigger("click");
				expect(combobox.attributes("aria-expanded")).toBe("true");
				expect(combobox.attributes("aria-disabled")).toBeUndefined();
				expect(combobox.attributes("aria-activedescendant")).toBeUndefined();
				// Erste Option hervorheben
				await combobox.trigger('keydown', { key: 'ArrowDown' });
				expect(combobox.attributes("aria-activedescendant")).toBe(`uiSelectOption_0_${vm.state.instanceId}`);
			});

			test.each([
				["readonly", { readonly: true }],
				["disabled", { disabled: true }],
				["searchable", { disabled: true }],
			])("Mit %s = true hat die Combobox den Tabindex -1", (_, props) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, ...props } });

				const { combobox } = getElements(wrapper);

				expect(combobox.attributes("tabindex")).toBe("-1");
			});

			test("Tabindex ist 0, wenn searchable = disabled = readonly = false", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false, readonly: false, disabled: false } });

				const { combobox } = getElements(wrapper);

				expect(combobox.attributes("tabindex")).toBe("0");
			});

			test.each([
				["readonly = true", { readonly: true }],
				["disabled = true", { disabled: true }],
				["searchable = false", { searchable: false }],
			])("Role wird bei %s auf 'combobox' gesetzt", (_, props) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, ...props } });

				const { combobox } = getElements(wrapper);

				expect(combobox.attributes("role")).toBe("combobox");
			});

			test("Role wird bei disabled = readonly = false und searchable = true auf 'undefined' gesetzt", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true, disabled: false, readonly: false } });

				const { combobox } = getElements(wrapper);

				expect(combobox.attributes("role")).toBeUndefined();
			});

			test("Bei headless = true hat die Combobox folgende Klassen: pl-1 min-h-6", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: true } });

				const { combobox } = getElements(wrapper);

				expect(combobox.classes()).toContain("pl-1");
				expect(combobox.classes()).toContain("min-h-6");
			});

			test("Bei headless = false hat die Combobox folgende Klassen: border mt-[0.8em] pl-3 pr-1 min-h-9", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: false } });

				const { combobox } = getElements(wrapper);

				["border", "mt-[0.8em]", "pl-3", "pr-1", "min-h-9"].forEach(cssClass => expect(combobox.classes()).toContain(cssClass));
			});

			test("Bei disabled = true hat die Combobox folgende Klassen: pointer-events-none", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, disabled: true } });

				const { combobox } = getElements(wrapper);

				expect(combobox.classes()).toContain("pointer-events-none");
			});

			test("Bei disabled = false hat die Combobox nicht die Klasse 'pointer-events-none'", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, disabled: false } });

				const { combobox } = getElements(wrapper);

				expect(combobox.classes()).not.toContain("pointer-events-none");
			});

			test("Bei readonly = true hat die Combobox die Klasse 'cursor-not-allowed'", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, readonly: true } });

				const { combobox } = getElements(wrapper);

				expect(combobox.classes()).toContain("cursor-not-allowed");
			});

			test("Bei searchable = true hat die Combobox die Klasse 'cursor-text'", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });

				const { combobox } = getElements(wrapper);

				expect(combobox.classes()).toContain("cursor-text");
			});

			test("Bei readonly = searchable = false hat die Combobox die Klasse 'cursor-pointer'", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false, readonly: false } });

				const { combobox } = getElements(wrapper);

				expect(combobox.classes()).toContain("cursor-pointer");
			});
		});

		describe("Search-Input Styling und Attribute", () => {
			test("Aria Attribute bei editierbarem Select und searchable = true sind richtig gesetzt.", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });

				const { combobox, input, vm } = getElements(wrapper);

				expect(input.attributes("aria-labelledby")).toBe(`uiSelectLabel_${vm.state.instanceId}`);
				expect(input.attributes("aria-controls")).toBe(`uiSelectDropdown_${vm.state.instanceId}`);
				expect(input.attributes("aria-autocomplete")).toBe("none");
				expect(input.attributes("aria-expanded")).toBe("false");
				// Dropdown öffnen
				await combobox.trigger("click");
				expect(input.attributes("aria-expanded")).toBe("true");
				expect(input.attributes("aria-activedescendant")).toBeUndefined();
				// Erste Option hervorheben
				await combobox.trigger('keydown', { key: 'ArrowDown' });
				expect(input.attributes("aria-activedescendant")).toBe(`uiSelectOption_0_${vm.state.instanceId}`);
			});

			test("Tabindex ist 0, wenn searchable = true und disabled = readonly = false", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true, readonly: false, disabled: false } });

				const { input } = getElements(wrapper);

				expect(input.attributes("tabindex")).toBe("0");
			});
		});

		describe("Label Styling", () => {
			test("Wenn das Label in der Combobox steht und removable = false wird folgende Klasse gesetzt: right-6", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, removable: false } });

				const { label } = getElements(wrapper);

				expect(label.classes()).toContain("right-6");
			});

			test("Wenn das Label in der Combobox steht und removable = true wird folgende Klasse gesetzt: right-11", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, removable: true } });

				const { label } = getElements(wrapper);

				expect(label.classes()).toContain("right-11");
			});

			test("Wenn das Label über der Combobox steht wird folgende Klasse gesetzt: right-2", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, modelValue: firstOption } });

				const { label } = getElements(wrapper);

				expect(label.classes()).toContain("right-2");
			});

			test("Bei headless = removable = true wird folgende Klasse gesetzt: left-10", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: true, removable: true } });

				const { label } = getElements(wrapper);

				expect(label.classes()).toContain("left-10");
			});

			test("Bei headless = true und removable = false wird folgende Klasse gesetzt: left-6", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: true, removable: false } });

				const { label } = getElements(wrapper);

				expect(label.classes()).toContain("left-6");
			});

			test("Bei headless = false wird folgende Klasse gesetzt: left-2", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: false } });

				const { label } = getElements(wrapper);

				expect(label.classes()).toContain("left-2");
			});

			test("Wenn das Label in der Combobox steht werden folgende Klasse gesetzt: absolute top-1/2 font-normal", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

				const { label } = getElements(wrapper);

				["absolute", "top-1/2", "font-normal"].forEach(cssClass => expect(label.classes()).toContain(cssClass));
			});

			test("Wenn das Label über der Combobox steht werden folgende Klasse gesetzt: absolute -top-0.5 text-xs", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, modelValue: firstOption } });

				const { label } = getElements(wrapper);

				["absolute", "-top-0.5", "text-xs"].forEach(cssClass => expect(label.classes()).toContain(cssClass));
			});

			test("Label wird über der Combobox dargestellt, wenn etwas selektiert ist", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, modelValue: firstOption } });

				const { label } = getElements(wrapper);

				["absolute", "-top-0.5", "text-xs"].forEach(cssClass => expect(label.classes()).toContain(cssClass));
			});

			test("Label wird über der Combobox dargestellt, wenn searchable = true und ein Suchbegriff wurde eingegeben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });

				const { label, input } = getElements(wrapper);

				await input.setValue("Text");

				["absolute", "-top-0.5", "text-xs"].forEach(cssClass => expect(label.classes()).toContain(cssClass));
			});

			test("Label wird in der Combobox dargestellt, wenn searchable = true und kein Suchbegriff wurde eingegeben", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });

				const { label } = getElements(wrapper);

				["absolute", "top-1/2", "font-normal"].forEach(cssClass => expect(label.classes()).toContain(cssClass));
			});

			test("Label wird in der Combobox dargestellt, wenn searchable = false nichts ist selektiert", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: false } });

				const { label } = getElements(wrapper);

				["absolute", "top-1/2", "font-normal"].forEach(cssClass => expect(label.classes()).toContain(cssClass));
			});

			test("Label wird angezeigt, wenn headless = false und keine Selektion/Suchbegriff vorhanden ist", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: false } });

				const { label } = getElements(wrapper);

				expect(label.exists()).toBeTruthy();
			});

			test("Label wird angezeigt, wenn headless = true und keine Selektion/Suchbegriff vorhanden ist", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: true } });

				const { label } = getElements(wrapper);

				expect(label.exists()).toBeTruthy();
			});

			test("Label wird nicht angezeigt, wenn headless = true und eine Selektion/Suchbegriff vorhanden ist", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, headless: true, modelValue: firstOption } });

				const { label } = getElements(wrapper);

				expect(label.exists()).toBeFalsy();
			});
		});

		describe("Optionen Styling", () => {
			test("Wenn eine Option selektiert ist, erhält sie folgende Klassen: bg-ui-selected text-ui-onselected font-medium border border-ui-selected", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, modelValue: firstOption } });

				const { dropdown } = getElements(wrapper);
				const firstDropdownOption = dropdown.find("li");

				["bg-ui-selected",
					"text-ui-onselected",
					"font-medium",
					"border",
					"border-ui-selected"].forEach(cssClass => expect(firstDropdownOption.classes()).toContain(cssClass));
				expect(firstDropdownOption.classes()).not.toContain("text-ui");
			});

			test("Wenn eine Option nicht selektiert ist, erhält sie folgende Klassen: text-ui", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

				const { dropdown } = getElements(wrapper);
				const firstDropdownOption = dropdown.find("li");

				["bg-ui-selected",
					"text-ui-onselected",
					"font-medium",
					"border",
					"border-ui-selected"].forEach(cssClass => expect(firstDropdownOption.classes()).not.toContain(cssClass));
				expect(firstDropdownOption.classes()).toContain("text-ui");
			});

			test("Wenn eine Option hervorgehoben ist, erhält sie folgende Klassen: bg-ui-hover inset-ring-2 inset-ring-ui-neutral", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

				const { dropdown, combobox } = getElements(wrapper);

				// Dropdown öffnen und erste Option hervorheben
				await combobox.trigger('keydown', { key: 'ArrowDown' });
				const firstDropdownOption = dropdown.find("li");

				["bg-ui-hover",
					"inset-ring-2",
					"inset-ring-ui-neutral",
				].forEach(cssClass => expect(firstDropdownOption.classes()).toContain(cssClass));
			});

			test("Wenn eine Option nicht hervorgehoben ist, erhält sie folgende Klassen nicht: bg-ui-hover inset-ring-2 inset-ring-ui-neutral", () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

				const { dropdown } = getElements(wrapper);
				const firstDropdownOption = dropdown.find("li");

				["bg-ui-hover",
					"inset-ring-2",
					"inset-ring-ui-neutral",
				].forEach(cssClass => expect(firstDropdownOption.classes()).not.toContain(cssClass));
			});
		});

		describe("Fokusklassen", () => {
			test("Fokusklasse 'contentFocusField' wird bei searchable = false an die Combobox gesetzt", () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, searchable: false },
					attrs: { class: "contentFocusField" },
				});

				const { combobox, select } = getElements(wrapper);

				expect(select.classes()).not.toContain("contentFocusField");
				expect(combobox.classes()).toContain("contentFocusField");
			});

			test("Fokusklasse 'contentFocusField' wird bei searchable = true an das Input gesetzt", () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, searchable: true },
					attrs: { class: "contentFocusField" },
				});

				const { input, select } = getElements(wrapper);

				expect(select.classes()).not.toContain("contentFocusField");
				expect(input.classes()).toContain("contentFocusField");
			});

			test("Fokusklasse 'subNavigationFocusField' wird bei searchable = false an die Combobox gesetzt", () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, searchable: false },
					attrs: { class: "subNavigationFocusField" },
				});

				const { combobox, select } = getElements(wrapper);

				expect(select.classes()).not.toContain("subNavigationFocusField");
				expect(combobox.classes()).toContain("subNavigationFocusField");
			});

			test("Fokusklasse 'subNavigationFocusField' wird bei searchable = true an das Input gesetzt", () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, searchable: true },
					attrs: { class: "subNavigationFocusField" },
				});

				const { input, select } = getElements(wrapper);

				expect(select.classes()).not.toContain("subNavigationFocusField");
				expect(input.classes()).toContain("subNavigationFocusField");
			});
		});

	});

	describe("Tastaturbedienung", () => {
		const allKeys: Array<[string, string]> = [
			["Enter", "Enter"],
			["ArrowDown", "ArrowDown"],
			["ArrowUp", "ArrowUp"],
			["Tab", "Tab"],
			["PageUp", "PageUp"],
			["PageDown", "PageDown"],
			["Home", "Home"],
			["End", "End"],
			["Escape", "Escape"],
			["Space", " "],
			["Printable Character", "a"],
			["Andere Taste", "F1"],
		];

		describe("Geschlossenes Dropdown", () => {

			test.each([
				["disabled = true", { disabled: true }],
				["readonly = true", { readonly: true }],
			])("Beliebige Taste (%s): Dropdown wird nicht geöffnet", async (_, props) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, ...props } });
				const { combobox, dropdown } = getElements(wrapper);

				for (const key of allKeys) {
					await combobox.trigger("keydown", { key: key[1] });
					expect(dropdown.exists()).toBe(false);
				}
			});

			test.each([
				["Enter", "Enter"],
				["ArrowDown", "ArrowDown"],
				["ArrowUp", "ArrowUp"],
				["PageUp", "PageUp"],
				["PageDown", "PageDown"],
				["Home", "Home"],
				["End", "End"],
				["Space", " "],
				["Printable Character", "a"],
				["Andere Taste", "F1"],
			])("%s: Dropdown öffnet sich", async (_, key) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				expect(dropdown.attributes("data-popover-open")).toBeUndefined();

				await combobox.trigger('keydown', { key });

				expect(dropdown.attributes("data-popover-open")).toBe("true");
			});

			test.each([
				["Home", "Home"],
				["End", "End"],
				["Printable Character", "a"],
			])("%s (searchable = true): Keine Option wird hervorgehoben", async (_keyName, keyValue) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: true } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('keydown', { key: keyValue });

				const options = dropdown.findAll("li");

				options.forEach(option => {
					expect(option.classes()).not.toContain("inset-ring-2");
					expect(option.classes()).not.toContain("inset-ring-ui-neutral");
				});
			});

			test("ArrowDown: Die erste Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('keydown', { key: 'ArrowDown' });

				const firstOption = dropdown.find("li");
				expect(firstOption.classes()).toContain("inset-ring-2");
				expect(firstOption.classes()).toContain("inset-ring-ui-neutral");
			});

			test("ArrowDown und Alt: Die erste Option wird nicht hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				await combobox.trigger('keydown', { key: 'ArrowDown', altKey: true });

				const firstOption = dropdown.find("li");
				expect(firstOption.classes()).not.toContain("inset-ring-2");
				expect(firstOption.classes()).not.toContain("inset-ring-ui-neutral");
			});

			test("ArrowUp: Die letzte Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('keydown', { key: 'ArrowUp' });

				const lastOption = dropdown.findAll("li").at(-1);
				expect(lastOption).toBeDefined();
				expect(lastOption!.classes()).toContain("inset-ring-2");
				expect(lastOption!.classes()).toContain("inset-ring-ui-neutral");
			});

			test("ArrowUp und Alt: Die letzte Option wird nicht hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				await combobox.trigger('keydown', { key: 'ArrowDown', altKey: true });

				const lastOption = dropdown.findAll("li").at(-1);
				expect(lastOption).toBeDefined();
				expect(lastOption!.classes()).not.toContain("inset-ring-2");
				expect(lastOption!.classes()).not.toContain("inset-ring-ui-neutral");
			});

			test("Home (searchable = false): Die erste Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('keydown', { key: 'Home' });

				const firstOption = dropdown.find("li");
				expect(firstOption.classes()).toContain("inset-ring-2");
				expect(firstOption.classes()).toContain("inset-ring-ui-neutral");
			});

			test("Home (searchable = true): Keine Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('keydown', { key: 'Home' });

				const options = dropdown.findAll("li");

				options.forEach(option => {
					expect(option.classes()).not.toContain("inset-ring-2");
					expect(option.classes()).not.toContain("inset-ring-ui-neutral");
				});
			});

			test("End (searchable = false): Die letzte Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('keydown', { key: 'End' });

				const lastOption = dropdown.findAll("li").at(-1);

				expect(lastOption).toBeDefined();
				expect(lastOption!.classes()).toContain("inset-ring-2");
				expect(lastOption!.classes()).toContain("inset-ring-ui-neutral");
			});

			test("End (searchable = true): Keine Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });

				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('keydown', { key: 'End' });

				const options = dropdown.findAll("li");

				options.forEach(option => {
					expect(option.classes()).not.toContain("inset-ring-2");
					expect(option.classes()).not.toContain("inset-ring-ui-neutral");
				});
			});

			test("Escape (searchable = true): Suchtext wird zurückgesetzt", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });

				const vm = wrapper.findComponent({ name: "UiSelect" }).vm;
				const { combobox, input } = getElements(wrapper);
				await input.setValue('BMW');

				expect(vm.state.search).toBe("BMW");

				await combobox.trigger('keydown', { key: 'Escape' });

				expect(vm.state.search).toBe("");
			});

			test("Printable Character (searchable = false): Erste passende Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false } });
				const { combobox, dropdown } = getElements(wrapper);
				const audiOption = dropdown.findAll("li").find(li => li.text() === "Audi");

				await combobox.trigger('keydown', { key: 'a' });

				expect(audiOption).toBeDefined();
				expect(audiOption!.classes()).toContain("inset-ring-2");
				expect(audiOption!.classes()).toContain("inset-ring-ui-neutral");
			});

			test("Printable Character (searchable = true): Keine Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('keydown', { key: 'a' });

				const options = dropdown.findAll("li");

				options.forEach(option => {
					expect(option.classes()).not.toContain("inset-ring-2");
					expect(option.classes()).not.toContain("inset-ring-ui-neutral");
				});
			});

			test("Shift: Nichts passiert", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger("keydown", { key: "Shift", shiftKey: true });

				expect(dropdown.attributes("data-popover-open")).toBeUndefined();

				const options = dropdown.findAll("li");

				options.forEach(option => {
					expect(option.classes()).not.toContain("inset-ring-2");
					expect(option.classes()).not.toContain("inset-ring-ui-neutral");
				});
			});
		});

		describe("Offenes Dropdown", () => {
			test.each([
				["Enter", "Enter"],
				["Tab", "Tab"],
				["Escape", "Escape"],
				["Space", " "],
			])("%s: Dropdown schließt sich", async (_, key) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				expect(dropdown.attributes("data-popover-open")).toBe("true");

				await combobox.trigger('keydown', { key });
				expect(dropdown.attributes("data-popover-open")).toBeUndefined();
			});
			test.each([
				["Enter (mit hervorgehobene Option)", "Enter"],
				["Space (mit hervorgehobene Option)", " "],
			])("%s: Option wird selektiert", async (_, key) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox } = getElements(wrapper);

				await combobox.trigger('click');
				// Erste Option hervorheben
				await combobox.trigger('keydown', { key: 'ArrowDown' });
				await combobox.trigger('keydown', { key });

				const emits = wrapper.emitted("update:modelValue");
				expect(emits).toBeDefined();
				expect(emits![0][0]).toBe(firstOption);
			});

			test("Tab (mit Shift und mit hervorgehobene Option): Keine Option wird selektiert", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox } = getElements(wrapper);

				await combobox.trigger('click');
				// Erste Option hervorheben
				await combobox.trigger('keydown', { key: 'ArrowDown' });
				await combobox.trigger('keydown', { key: 'Tab', shiftKey: true });

				const emits = wrapper.emitted("update:modelValue");
				expect(emits).toBeUndefined();
			});

			test("ArrowDown: Erste Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				await combobox.trigger('keydown', { key: 'ArrowDown' });

				const firstOption = dropdown.find("li");
				expect(firstOption.classes()).toContain("inset-ring-2");
				expect(firstOption.classes()).toContain("inset-ring-ui-neutral");
			});

			test("ArrowUp: Letzte Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');

				await combobox.trigger('keydown', { key: 'ArrowUp' });

				const lastOption = dropdown.findAll("li").at(-1);
				expect(lastOption).toBeDefined();
				expect(lastOption!.classes()).toContain("inset-ring-2");
				expect(lastOption!.classes()).toContain("inset-ring-ui-neutral");
			});

			test("ArrowUp und Alt: Hervorgehobene Option wird selektiert und Dropdown geschlossen", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				// Erste Option hervorheben
				await combobox.trigger('keydown', { key: 'ArrowDown' });
				await combobox.trigger('keydown', { key: 'ArrowUp', altKey: true });

				const emits = wrapper.emitted("update:modelValue");
				expect(emits).toBeDefined();
				expect(emits![0][0]).toBe(firstOption);

				expect(dropdown.attributes("data-popover-open")).toBeUndefined();
			});

			test("Home (searchable = false): Erste Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');

				await combobox.trigger('keydown', { key: 'Home' });

				const firstOption = dropdown.find("li");
				expect(firstOption.classes()).toContain("inset-ring-2");
				expect(firstOption.classes()).toContain("inset-ring-ui-neutral");

			});

			test("End (searchable = false): Letzte Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				await combobox.trigger('keydown', { key: 'End' });

				const lastOption = dropdown.findAll("li").at(-1);
				expect(lastOption).toBeDefined();
				expect(lastOption!.classes()).toContain("inset-ring-2");
				expect(lastOption!.classes()).toContain("inset-ring-ui-neutral");

			});

			test("Escape (searchable = true): Suchtext wird zurückgesetzt", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });
				const { combobox, input, vm } = getElements(wrapper);

				await input.setValue('BMW');
				await combobox.trigger('click');

				expect(vm.state.search).toBe("BMW");

				await combobox.trigger('keydown', { key: 'Escape' });

				expect(vm.state.search).toBe("");
			});

			test("PageUp: Die Option 10 Positionen vor der ersten wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				await combobox.trigger('keydown', { key: 'PageUp' });

				const minusTenOption = dropdown.findAll("li").at(-10);
				expect(minusTenOption).toBeDefined();
				expect(minusTenOption!.classes()).toContain("inset-ring-2");
				expect(minusTenOption!.classes()).toContain("inset-ring-ui-neutral");
			});

			test("PageDown: Die 10. Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				await combobox.trigger('keydown', { key: 'PageDown' });

				const plusTenOption = dropdown.findAll("li").at(9);
				expect(plusTenOption).toBeDefined();
				expect(plusTenOption!.classes()).toContain("inset-ring-2");
				expect(plusTenOption!.classes()).toContain("inset-ring-ui-neutral");
			});

			test("Space (searchable = true): Optionsliste enthält passende Einträge mit dem String ' ' und es wird nicht selektiert.", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });
				const { combobox, dropdown, input } = getElements(wrapper);

				await input.setValue(" ");
				const options = dropdown.findAll("li");
				expect(options.length).toBe(1);
				expect(options[0].text()).toBe("Land Rover");

				await combobox.trigger('keydown', { key: 'ArrowDown' });
				await combobox.trigger('keydown', { key: ' ' });

				const emits = wrapper.emitted("update:modelValue");
				expect(emits).toBeUndefined();
			});

			test("Printable Character (searchable = false, nichts hervorgehoben): Erste passende Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false } });
				const { combobox, dropdown } = getElements(wrapper);
				const seatOption = dropdown.findAll("li").find(li => li.text() === "Seat");

				await combobox.trigger('click');
				await combobox.trigger('keydown', { key: 's' });

				expect(seatOption).toBeDefined();
				expect(seatOption!.classes()).toContain("inset-ring-2");
				expect(seatOption!.classes()).toContain("inset-ring-ui-neutral");
			});

			test("Printable Character (searchable = false, Option hervorgehoben): Erste passende Option nach der hervorgehobenen wird hervorgehoben",
				async () => {
					const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false } });
					const { combobox, dropdown } = getElements(wrapper);
					const seatOption = dropdown.findAll("li").find(li => li.text() === "Seat");
					const skodaOption = dropdown.findAll("li").find(li => li.text() === "Skoda");

					await combobox.trigger('click');

					await combobox.trigger('keydown', { key: 's' });

					expect(seatOption).toBeDefined();
					expect(seatOption!.classes()).toContain("inset-ring-2");
					expect(seatOption!.classes()).toContain("inset-ring-ui-neutral");

					await combobox.trigger('keydown', { key: 's' });

					expect(skodaOption).toBeDefined();
					expect(skodaOption!.classes()).toContain("inset-ring-2");
					expect(skodaOption!.classes()).toContain("inset-ring-ui-neutral");
				});

			test("Printable Character (searchable = false 2x gleicher Buchstabe): Zweite passende Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false } });
				const { combobox, dropdown } = getElements(wrapper);
				const skodaOption = dropdown.findAll("li").find(li => li.text() === "Skoda");

				await combobox.trigger('click');

				vi.useFakeTimers(); // Fake-Timer aktivieren

				// erster Tastendruck
				await combobox.trigger('keydown', { key: 's' });

				// kleine Zeit vorspulen (innerhalb des Such-Timeouts)
				vi.advanceTimersByTime(200); // z.B. 200ms, abhängig von deiner Komponente

				// zweiter Tastendruck
				await combobox.trigger('keydown', { key: 's' });

				// Timer auflösen, damit die Komponente reagiert
				await vi.runAllTimersAsync();

				expect(skodaOption).toBeDefined();
				expect(skodaOption!.classes()).toContain("inset-ring-2");
				expect(skodaOption!.classes()).toContain("inset-ring-ui-neutral");
			});

			test("Printable Character (searchable = false, 2 unterschiedliche Buchstaben im Timer): Erste passende Option mit String wird hervorgehoben",
				async () => {
					const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false } });
					const { combobox, dropdown } = getElements(wrapper);
					const skodaOption = dropdown.findAll("li").find(li => li.text() === "Skoda");

					await combobox.trigger('click');

					vi.useFakeTimers(); // Fake-Timer aktivieren

					// erster Tastendruck
					await combobox.trigger('keydown', { key: 's' });

					// kleine Zeit vorspulen (innerhalb des Such-Timeouts)
					vi.advanceTimersByTime(200); // z.B. 200ms, abhängig von deiner Komponente

					// zweiter Tastendruck
					await combobox.trigger('keydown', { key: 'k' });

					// Timer auflösen, damit die Komponente reagiert
					await vi.runAllTimersAsync();

					expect(skodaOption).toBeDefined();
					expect(skodaOption!.classes()).toContain("inset-ring-2");
					expect(skodaOption!.classes()).toContain("inset-ring-ui-neutral");
				});

			test("Printable Character (searchable = false, 2 unterschiedliche Buchstaben außerhalb Timer): Erste passende Option beginnend mit zweitem eingegebenen Buchstaben wird hervorgehoben",
				async () => {
					const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false } });
					const { combobox, dropdown } = getElements(wrapper);
					const kiaOption = dropdown.findAll("li").find(li => li.text() === "Kia");

					await combobox.trigger('click');

					vi.useFakeTimers(); // Fake-Timer aktivieren

					// erster Tastendruck
					await combobox.trigger('keydown', { key: 's' });

					// kleine Zeit vorspulen (außerhalb des Such-Timeouts)
					vi.advanceTimersByTime(550); // z.B. 200ms, abhängig von deiner Komponente

					// zweiter Tastendruck
					await combobox.trigger('keydown', { key: 'k' });

					// Timer auflösen, damit die Komponente reagiert
					await vi.runAllTimersAsync();

					expect(kiaOption).toBeDefined();
					expect(kiaOption!.classes()).toContain("inset-ring-2");
					expect(kiaOption!.classes()).toContain("inset-ring-ui-neutral");
				});

			test("Printable Character (searchable = false, 2 unterschiedliche Buchstaben im Timer): Wenn keine passende Option gefunden wird, wird nichts hervorgehoben",
				async () => {
					const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: false } });
					const { combobox, dropdown } = getElements(wrapper);

					await combobox.trigger('click');

					vi.useFakeTimers(); // Fake-Timer aktivieren

					// erster Tastendruck
					await combobox.trigger('keydown', { key: 's' });

					// kleine Zeit vorspulen (innerhalb des Such-Timeouts)
					vi.advanceTimersByTime(200); // z.B. 200ms, abhängig von deiner Komponente

					// zweiter Tastendruck
					await combobox.trigger('keydown', { key: 'm' });

					// Timer auflösen, damit die Komponente reagiert
					await vi.runAllTimersAsync();

					const options = dropdown.findAll("li");

					options.forEach(option => {
						expect(option.classes()).not.toContain("inset-ring-2");
						expect(option.classes()).not.toContain("inset-ring-ui-neutral");
					});
				});

			test.each([
				["Home", "Home"],
				["End", "End"],
				["Printable Character", "a"],
			])("%s (searchable = true): Keine Option wird hervorgehoben", async (_keyName, keyValue) => {
				const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: true } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				await combobox.trigger('keydown', { key: keyValue });

				const options = dropdown.findAll("li");

				options.forEach(option => {
					expect(option.classes()).not.toContain("inset-ring-2");
					expect(option.classes()).not.toContain("inset-ring-ui-neutral");
				});
			});

			test("Andere Tasten: Keine Option wird hervorgehoben", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				await combobox.trigger('keydown', { key: 'Shift' });

				const options = dropdown.findAll("li");

				options.forEach(option => {
					expect(option.classes()).not.toContain("inset-ring-2");
					expect(option.classes()).not.toContain("inset-ring-ui-neutral");
				});
			});

			test("'ar' (searchable = true): Optionsliste enthält passende Einträge mit dem String 'ar'", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });
				const { dropdown, input } = getElements(wrapper);

				await input.setValue("ar");
				const options = dropdown.findAll("li");

				expect(options.length).toBe(2);
				expect(options[0].text()).toBe("Subaru");
				expect(options[1].text()).toBe("Jaguar");
			});

			test("'suba' (searchable = true): Optionsliste enthält den Eintrag 'Subaru', auch wenn es groß geschrieben ist", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });
				const { dropdown, input } = getElements(wrapper);

				await input.setValue("suba");
				const options = dropdown.findAll("li");

				expect(options.length).toBe(1);
				expect(options[0].text()).toBe("Subaru");
			});

			test("'ar' (searchable = true, deepSearchAttributes = 'color'): Optionsliste enthält passende Einträge mit dem String 'ar'", async () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, searchable: true, deepSearchAttributes: ['color'] },
				});
				const { dropdown, input } = getElements(wrapper);

				await input.setValue("ar");
				const options = dropdown.findAll("li");

				expect(options.length).toBe(6);
				expect(options[0].text()).toBe("Opel");
				expect(options[1].text()).toBe("Nissan");
				expect(options[2].text()).toBe("Skoda");
				expect(options[3].text()).toBe("Subaru");
				expect(options[4].text()).toBe("Jaguar");
				expect(options[5].text()).toBe("Porsche");
			});

			test("'ar' (searchable = true, deepSearchAttributes = 'ungültig'): Optionsliste enthält passende Einträge mit dem String 'ar'", async () => {
				const wrapper = mount(UiSelect<cars>, {
					props: { manager: manager, searchable: true, deepSearchAttributes: ['ungültig'] },
				});
				const { dropdown, input } = getElements(wrapper);

				await input.setValue("ar");
				const options = dropdown.findAll("li");

				expect(options.length).toBe(2);
				expect(options[0].text()).toBe("Subaru");
				expect(options[1].text()).toBe("Jaguar");
			});

			test("Keine Navigation im Dropdown, wenn keine Optionen enthalten sind", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: undefined } });
				const { combobox, dropdown } = getElements(wrapper);

				await combobox.trigger('click');
				await combobox.trigger('keydown', { key: 'ArrowDown' });

				const options = dropdown.findAll("li");

				options.forEach(option => {
					expect(option.classes()).not.toContain("inset-ring-2");
					expect(option.classes()).not.toContain("inset-ring-ui-neutral");
				});
			});

			test("manager = undefined und Suchbegriff: Das Select enthält keine Optionen", async () => {
				const wrapper = mount(UiSelect<cars>, { props: { manager: undefined, searchable: true } });
				const { dropdown, input } = getElements(wrapper);

				await input.setValue("Testeingabe");

				const options = dropdown.findAll("li");
				expect(options.length).toBe(1);
				expect(options[0].text()).toBe("Keine passenden Einträge gefunden");
			});
		});
	});
});

function getElements(wrapper: VueWrapper) {
	const select = wrapper.find(".ui-select");
	const label = wrapper.find(".ui-select--label");
	const combobox = wrapper.find(".ui-select--combobox");
	const dropdown = wrapper.find(".ui-select--dropdown");
	const input = wrapper.find('input.ui-select--search');
	const vm = wrapper.findComponent({ name: "UiSelect" }).vm;

	return { select, combobox, dropdown, input, vm, label };
}


function createTestData() {
	const options: cars[] = [
		{ marke: "BMW", color: "blue", baujahr: 2006 },
		{ marke: "Audi", color: "red", baujahr: 2008 },
		{ marke: "Opel", color: "schwarz", baujahr: 2006 },
		{ marke: "Mercedes", color: "weiß", baujahr: 2010 },
		{ marke: "VW", color: "grau", baujahr: 2012 },
		{ marke: "Ford", color: "grün", baujahr: 2009 },
		{ marke: "Toyota", color: "gelb", baujahr: 2015 },
		{ marke: "Honda", color: "blau", baujahr: 2011 },
		{ marke: "Mazda", color: "rot", baujahr: 2013 },
		{ marke: "Nissan", color: "schwarz", baujahr: 2007 },
		{ marke: "Kia", color: "weiß", baujahr: 2014 },
		{ marke: "Hyundai", color: "grau", baujahr: 2012 },
		{ marke: "Peugeot", color: "grün", baujahr: 2008 },
		{ marke: "Citroën", color: "gelb", baujahr: 2010 },
		{ marke: "Fiat", color: "blau", baujahr: 2009 },
		{ marke: "Seat", color: "rot", baujahr: 2011 },
		{ marke: "Skoda", color: "schwarz", baujahr: 2013 },
		{ marke: "Renault", color: "weiß", baujahr: 2007 },
		{ marke: "Mitsubishi", color: "grau", baujahr: 2015 },
		{ marke: "Subaru", color: "grün", baujahr: 2014 },
		{ marke: "Volvo", color: "gelb", baujahr: 2012 },
		{ marke: "Jaguar", color: "blau", baujahr: 2006 },
		{ marke: "Land Rover", color: "rot", baujahr: 2008 },
		{ marke: "Porsche", color: "schwarz", baujahr: 2010 },
		{ marke: "Tesla", color: "weiß", baujahr: 2020 },
	];

	const optionDisplayText = (option: cars) => option.marke;
	const selectionDisplayText = (option: cars) => option.marke;
	const manager = new SelectManager<{ marke: string, color: string, baujahr: number }>({
		options: options, optionDisplayText: optionDisplayText, selectionDisplayText: selectionDisplayText,
	});
	const firstOption = manager.filteredOptions.getFirst();
	return { manager, firstOption };
}

type cars = { marke: string, color: string, baujahr: number };
