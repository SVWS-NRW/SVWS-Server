import { mount } from "@vue/test-utils";
import UiSelectMulti from "@ui/ui/controls/select/UiSelectMulti.vue";
import { describe, test, expect, vi, beforeAll } from "vitest";
import type { KlassenartKatalogEintrag } from "@core/asd/data/klassen/KlassenartKatalogEintrag";
import { Klassenart } from "@core/asd/types/klassen/Klassenart";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";
import { BasicValidator } from "@core/asd/validate/BasicValidator";
import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

const reader = new JsonCoreTypeReaderStatic();
vi.mock("@json/klassen/Klassenart.json", async () => ({
	default: (await import("./manager/Klassenart.mock.json")).default,
}));

beforeAll(() => {
	HTMLElement.prototype.showPopover = vi.fn(function(this: HTMLElement) {
		this.dataset.popoverOpen = 'true';
	});
	HTMLElement.prototype.hidePopover = vi.fn(function(this: HTMLElement) {
		delete this.dataset.popoverOpen;
	});

	reader.readAll();

	describe("Komponente kann gemounted werden", () => {
		test("HTML wird erzeugt", () => {
			const wrapper = mount(UiSelectMulti);
			expect(wrapper.html()).includes("ui-select");
		});
	});

	describe.concurrent("PropHandhabung läuft korrekt", () => {

		describe("Mount mit default Props", () => {
			const wrapper = mount(UiSelectMulti);
			const props = wrapper.props();

			test("props.label entspricht ''", () => {
				expect(props.label).toBe('');
			});

			test("props.manager ist eine Instanz von SelectManager", () => {
				expect(props.manager).toBeInstanceOf(SelectManager);
			});

			test("props.searchable entspricht true", () => {
				expect(props.searchable).toBe(true);
			});

			test("props.deepSearchAttributes ist ein leeres Array", () => {
				expect(props.deepSearchAttributes?.length).toBe(0);
			});

			test("props.required entspricht false", () => {
				expect(props.required).toBe(false);
			});

			test("props.readonly entspricht false", () => {
				expect(props.readonly).toBe(false);
			});

			test("props.removable entspricht true", () => {
				expect(props.removable).toBe(true);
			});

			test("props.nullable entspricht true", () => {
				expect(props.nullable).toBe(true);
			});

			test("props.disabled entspricht false", () => {
				expect(props.disabled).toBe(false);
			});

			test("props.statistics entspricht false", () => {
				expect(props.statistics).toBe(false);
			});

			test("props.headless entspricht false", () => {
				expect(props.headless).toBe(false);
			});

			test("props.validation ist undefined", () => {
				expect(props.validation).toBeUndefined();
			});
		});


		test("Mit Prop 'label = Mein Label' wird 'Mein Label' als Label angezeigt", () => {
			const wrapper = mount(UiSelectMulti, { props: { label: 'Mein Label' } });
			const label = wrapper.find('.ui-select-multi--label--text');
			expect(label.text()).toBe('Mein Label');
		});

		test("Mit Prop 'searchable = true' wird die Komponente mit einem Input gerendert", () => {
			const wrapper = mount(UiSelectMulti, { props: { searchable: true } });
			const input = wrapper.find('.ui-select-multi--search input');
			expect(input.exists()).toBeTruthy();
			expect(input.element.tagName).toBe('INPUT');
		});

		test("Mit Prop 'searchable = false' wird die Komponente ohne ein Input gerendert", () => {
			const wrapper = mount(UiSelectMulti, { props: { searchable: false } });
			const input = wrapper.find('.ui-select-multi--search input');
			expect(input.exists()).toBeFalsy();
		});

		test("Mit Prop 'deepSearchAttributes' werden bei einer Suche auch Treffer in den Attributen angezeigt", async () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, searchable: true, deepSearchAttributes: ['color'] } });
			const input = wrapper.find('.ui-select-multi--search input');
			await input.setValue('red');
			await wrapper.vm.$nextTick();

			const options = wrapper.findAll('.ui-select-multi--dropdown li');
			expect(options).toHaveLength(1);
			expect(options[0].find('span').text()).toBe('Audi');
		});

		describe.concurrent("Mit Prop 'required = false'", () => {
			test("wird die Komponente ohne Stern-Icon angezeigt", () => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, required: false } });
				const labelRequired = wrapper.find('.ui-select-multi--label--required');

				expect(labelRequired.exists()).toBeFalsy();
			});

		});

		describe.concurrent("Mit Prop 'required = true'", () => {

			test("wird die Komponente mit Stern-Icon angezeigt", () => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, required: true } });
				const labelRequired = wrapper.find(".ui-select-multi--label--required");

				expect(labelRequired.find('span.i-ri-asterisk').exists()).toBeTruthy();
			});
		});

		describe.concurrent("Mit Prop 'readonly = false'", () => {
			test("wird kein readonly-Icon angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: createTestData().manager, readonly: false } });
				expect(wrapper.find('.ui-select-multi--label--readonly').exists()).toBeFalsy();
			});

			test("und 'headless = false' werden Lösch- und Aufklapp-Icons rechts angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: createTestData().manager, headless: false, readonly: false } });
				const iconsRight = wrapper.find('.ui-select-multi--icons-right');
				expect(iconsRight.find('span.i-ri-expand-up-down-line').exists()).toBeTruthy();
				expect(iconsRight.find('button span.i-ri-close-line').exists()).toBeTruthy();
			});

			test("und 'headless = true' werden Lösch- und Aufklapp-Icons links angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: createTestData().manager, headless: true, readonly: false } });
				const iconsLeft = wrapper.find('.ui-select-multi--icons-left');
				expect(iconsLeft.find('span.i-ri-expand-up-down-line').exists()).toBeTruthy();
				expect(iconsLeft.find('button span.i-ri-close-line').exists()).toBeTruthy();
			});

			test("und 'searchable = true' wird das Suchfeld angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: createTestData().manager, searchable: true, readonly: false } });
				expect(wrapper.find('.ui-select-multi--search input').exists()).toBeTruthy();
			});

			test("existiert das Dropdown", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: createTestData().manager, readonly: false } });
				expect(wrapper.find('.ui-select-multi--dropdown').exists()).toBeTruthy();
			});
		});

		describe.concurrent("Mit Prop 'readonly = true'", () => {
			test("wird das readonly-Icon angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: createTestData().manager, readonly: true } });
				const readonly = wrapper.find('.ui-select-multi--label--readonly');
				expect(readonly.exists()).toBeTruthy();
				expect(readonly.find('span.i-ri-lock-line').exists()).toBeTruthy();
			});

			test("und 'headless = false' werden Lösch- und Aufklapp-Icons rechts nicht angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: createTestData().manager, headless: false, readonly: true } });
				const iconsRight = wrapper.find('.ui-select-multi--icons-right');
				expect(iconsRight.exists()).toBeFalsy();
			});

			test("und 'headless = true' werden Lösch- und Aufklapp-Icons links nicht angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: createTestData().manager, headless: true, readonly: true } });
				const iconsLeft = wrapper.find('.ui-select-multi--icons-left');
				expect(iconsLeft.exists()).toBeFalsy();
			});

			test("und 'searchable = true' wird das Suchfeld nicht angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: createTestData().manager, readonly: true } });
				expect(wrapper.find('.ui-select-multi--search input').exists()).toBeFalsy();
			});

			test("existiert das Dropdown nicht", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: createTestData().manager, readonly: true } });
				expect(wrapper.find('.ui-select-multi--dropdown').exists()).toBeFalsy();
			});
		});

		describe.concurrent("Mit Prop 'removable = false'", () => {
			const { manager, singleSelection } = createTestData();

			test("und 'headless = false' wird kein Löschenbutton im Input rechts angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, headless: false, removable: false,
					modelValue: singleSelection } });
				const iconsRight = wrapper.find('.ui-select-multi--icons-right');
				expect(iconsRight.find('span.i-ri-close-line').exists()).toBeFalsy();
			});

			test("und 'headless = true' wird kein Löschenbutton im Input links angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, headless: true, removable: false,
					modelValue: singleSelection } });
				const iconsLeft = wrapper.find('.ui-select-multi--icons-left');
				expect(iconsLeft.find('span.i-ri-close-line').exists()).toBeFalsy();
			});

			test("bei einem selektierten Element ist dieses nicht löschbar", async () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: singleSelection } });

				const firstOption = wrapper.find('.ui-select-multi--dropdown li');

				await firstOption.trigger('mousedown');
				await wrapper.vm.$nextTick();
				const emits = wrapper.emitted("update:modelValue");
				expect(emits).toBeUndefined();
			});


			test("bei einem selektierten Element wird kein Löschenbutton in der Selektion angezeigt", () => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: singleSelection } });

				const selectionRemoveButtons = wrapper.findAll('.ui-select-multi--selection--removebutton');

				expect(selectionRemoveButtons.length).toBe(0);
			});

			test("bei zwei selektierten Elementen ist ein Element löschbar", async () => {
				const { manager, twoSelections } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: twoSelections } });

				const firstOption = wrapper.find('.ui-select-multi--dropdown li');

				await firstOption.trigger('mousedown');
				await wrapper.vm.$nextTick();
				const emits = wrapper.emitted("update:modelValue");
				expect(emits).toBeDefined();
				expect(emits![0][0]).toEqual([manager.filteredOptions.get(1)]);
			});

			test("bei zwei selektierten Elementen werden Löschenbuttons in den Selektionen angezeigt", () => {
				const { manager, twoSelections } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: twoSelections } });

				const selectionRemoveButtons = wrapper.findAll('.ui-select-multi--selection--removebutton');

				expect(selectionRemoveButtons.length).toBe(2);
			});
		});

		describe.concurrent("Mit Prop 'removable = true'", () => {
			const { manager, singleSelection } = createTestData();

			test("und 'headless = false' wird ein Löschenbutton im Input rechts angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, headless: false, removable: true,
					modelValue: singleSelection } });
				const iconsRight = wrapper.find('.ui-select-multi--icons-right');
				expect(iconsRight.find('span.i-ri-close-line').exists()).toBeTruthy();
			});

			test("und 'headless = true' wird ein Löschenbutton in Input links angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, headless: true, removable: true,
					modelValue: singleSelection } });
				const iconsLeft = wrapper.find('.ui-select-multi--icons-left');
				expect(iconsLeft.find('span.i-ri-close-line').exists()).toBeTruthy();
			});

			test("bei einem selektierten Element ist dieses löschbar", async () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: true,
					modelValue: singleSelection } });

				const firstOption = wrapper.find('.ui-select-multi--dropdown li');

				await firstOption.trigger('mousedown');
				await wrapper.vm.$nextTick();

				const emits = wrapper.emitted("update:modelValue");
				expect(emits).toBeDefined();
				expect(emits![0][0]).toEqual([]);
			});

			test("bei einem selektierten Element wird in dieser Selektion ein Löschbutton angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: true, modelValue: singleSelection } });

				const selectionRemoveButtons = wrapper.findAll('.ui-select-multi--selection--removebutton');

				expect(selectionRemoveButtons.length).toBe(1);
			});
		});

		test("Mit Prop 'nullable = false' wird in der Komponente kein null als v-model zugelassen", () => {
			const { manager } = createTestData();

			expect(() => mount(UiSelectMulti<cars>, {
				props: { manager, nullable: false, modelValue: null },
			})).toThrow("Ungültiges v-model: null oder undefined bei nullable = false");
		});

		test("Mit Prop 'nullable = false' wird in der Komponente kein undefined als v-model zugelassen", () => {
			const { manager } = createTestData();
			expect(() => mount(UiSelectMulti<cars>, {
				props: { manager, nullable: false, modelValue: undefined },
			})).toThrow("Ungültiges v-model: null oder undefined bei nullable = false");
		});

		test("Mit Prop 'nullable = true' wird in der Komponente null als v-model zugelassen", () => {
			const { manager } = createTestData();

			expect(() => mount(UiSelectMulti<cars>, {
				props: { manager, nullable: true, modelValue: null },
			})).not.toThrow("Ungültiges v-model: null oder undefined bei nullable = false");
		});

		test("Mit Prop 'nullable = true' wird in der Komponente undefined als v-model zugelassen", () => {
			const { manager } = createTestData();
			expect(() => mount(UiSelectMulti<cars>, {
				props: { manager, nullable: true, modelValue: undefined },
			})).not.toThrow("Ungültiges v-model: null oder undefined bei nullable = false");

		});

		describe.concurrent("Mit Prop 'disabled = false'", () => {
			const { manager } = createTestData();

			test("wird kein Button disabled dargestellt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, disabled: false } });
				const buttons = wrapper.findAll('button');
				for (const button of buttons) {
					expect(button.attributes('disabled')).toBeUndefined();
				}
			});

			test("und 'searchable = true' ist das Suchfeld sichtbar", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, searchable: true, disabled: false } });
				expect(wrapper.find('.ui-select-multi--search input').exists()).toBeTruthy();
			});

			test("ist das Dropdown sichtbar", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, disabled: false } });
				expect(wrapper.find('.ui-select-multi--dropdown').exists()).toBeTruthy();
			});
		});

		describe.concurrent("Mit Prop 'disabled = true'", () => {
			const { manager } = createTestData();

			test("werden alle Buttons disabled dargestellt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, disabled: true } });
				const buttons = wrapper.findAll('button');
				for (const button of buttons) {
					expect(button.attributes('disabled')).toBeDefined();
				}
			});

			test("und 'searchable = true' ist das Suchfeld nicht sichtbar", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, searchable: true, disabled: true } });
				expect(wrapper.find('.ui-select-multi--search input').exists()).toBeFalsy();
			});

			test("ist das Dropdown nicht sichtbar", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, disabled: true } });
				expect(wrapper.find('.ui-select-multi--dropdown').exists()).toBeFalsy();
			});
		});

		test("Mit Prop 'statistics = false' wird in der Komponente kein Statistik-Icon angezeigt", () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, statistics: false } });
			const statistics = wrapper.find('.ui-select-multi--label--statistics');

			expect(statistics.exists()).toBeFalsy();
		});

		test("Mit Prop 'statistics = true' wird in der Komponente ein Statistik-Icon angezeigt", () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, statistics: true } });
			const statistics = wrapper.find('.ui-select-multi--label--statistics');

			expect(statistics.exists()).toBeTruthy();
			expect(statistics.find('span.i-ri-bar-chart-2-line').exists()).toBeTruthy();
		});

		describe.concurrent("Mit Prop 'headless = false'", () => {
			const { manager, twoSelections } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, label: "Mein Label", headless: false } });

			test("werden die Icons nicht links angezeigt", () => {
				const iconsLeft = wrapper.find('.ui-select-multi--icons-left');
				expect(iconsLeft.exists()).toBeFalsy();
			});

			test("werden die Icons rechts angezeigt", () => {
				const iconsRight = wrapper.find('.ui-select-multi--icons-right');
				expect(iconsRight.exists()).toBeTruthy();
			});

			test("wird die Komponente nicht kompakt dargestellt", () => {
				const combobox = wrapper.find('.ui-select-multi--combobox');
				expect(combobox.classes()).toContain('border');
				expect(combobox.element.firstElementChild?.classList).toContain('py-1');
			});

			test("wird das Label ohne Selektion angezeigt", () => {
				const label = wrapper.find('.ui-select-multi--label');
				expect(label.exists()).toBeTruthy();

				const labelText = label.find('.ui-select-multi--label--text');
				expect(labelText.exists()).toBeTruthy();
			});

			test("wird das Label mit Selektion angezeigt", async () => {
				await wrapper.setProps({ modelValue: twoSelections });
				await wrapper.vm.$nextTick();

				const label = wrapper.find('.ui-select-multi--label');
				expect(label.exists()).toBeTruthy();

				const labelText = label.find('.ui-select-multi--label--text');
				expect(labelText.exists()).toBeTruthy();
			});
		});

		describe.concurrent("Mit Prop 'headless = true'", () => {
			const { manager, singleSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, label: "Mein Label", headless: true } });

			test("werden die Icons links angezeigt", () => {
				const iconsLeft = wrapper.find('.ui-select-multi--icons-left');
				expect(iconsLeft.exists()).toBeTruthy();

				expect(iconsLeft.find('span.i-ri-expand-up-down-line').exists()).toBeTruthy();
				expect(iconsLeft.find('span.i-ri-close-line').exists()).toBeTruthy();
			});

			test("werden die Icons rechts nicht angezeigt", () => {
				const iconsRight = wrapper.find('.ui-select-multi--icons-right');
				expect(iconsRight.exists()).toBeFalsy();
			});

			test("wird die Komponente kompakt dargestellt", () => {
				const combobox = wrapper.find('.ui-select-multi--combobox');
				expect(combobox.classes()).not.toContain('border');
				expect(combobox.element.firstElementChild?.classList).toContain('py-0');
			});

			test("und ohne Selektion wird das Label angezeigt", () => {
				const label = wrapper.find('.ui-select-multi--label');
				expect(label.exists()).toBeTruthy();

				const labelText = label.find('.ui-select-multi--label--text');
				expect(labelText.exists()).toBeTruthy();
			});

			test("und mit Selektion wird das Label nicht angezeigt", () => {
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, label: "Mein Label",
					modelValue: singleSelection, headless: true } });
				const label = wrapper.find('.ui-select-multi--label');
				expect(label.exists()).toBeFalsy();
			});
		});
	});

	describe.concurrent("Validierung", () => {
		test("Mit Prop 'validation' wird eine Validierung von außen ausgeführt", () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, {
				props: { manager, validation: () => getValidatorFehler() },
			});
			const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
		});

		test("Bei Validierungsfehlern wird ein Validation-Icon angezeigt", () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, {
				props: { manager, placeholder: "Enter Number", validation: () => getValidatorFehler() },
			});
			expect(wrapper.find(".validation-tooltip-icon").exists()).toBeTruthy();
		});

		describe.concurrent("required", () => {
			test.each([
				[false, "ohne Prop 'validation'", undefined],
				[false, "mit Prop 'validation'", () => getValidatorFehler()],
				[true, "mit Prop 'validation'", () => getValidatorFehler()],
			])(
				"Mit Prop 'required = %s' und %s wird kein Required-Validator hinzugefügt",
				(required, _vString, validation) => {
					const { manager } = createTestData();
					const wrapper = mount(UiSelectMulti<cars>, { props: { manager, required, validation } });
					const validatorRequired = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validatorRequired;

					expect(validatorRequired).toBeNull();
				}
			);

			test("Mit Prop 'required = true' wird ein Required-Validator hinzugefügt", () => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: manager, required: true } });
				const validatorRequired = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validatorRequired;

				expect(validatorRequired).not.toBeNull();
				expect(validatorRequired).toBeInstanceOf(BasicValidator);
			});

			test("Mit Prop 'required = true' ohne Selektion wird ein Fehler für die Required-Validierung generiert", () => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: manager, required: true } });
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Eine Option muss ausgewählt werden.");
			});

			test("Mit Prop 'required = true' und Selektion ergibt die Validierung keine Fehler", () => {
				const { manager, singleSelection } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: manager, required: true, modelValue: singleSelection } });
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			});
		});

		describe.concurrent("minOptions", () => {
			test.each([
				[undefined, "ohne Prop 'validation'", undefined],
				[undefined, "mit Prop 'validation'", () => getValidatorFehler()],
				[2, "mit Prop 'validation'", () => getValidatorFehler()],
			])(
				"Mit Prop 'minOptions = %s' und %s wird kein OptionsRange-Validator hinzugefügt",
				(minOptions, _vString, validation) => {
					const { manager } = createTestData();
					const wrapper = mount(UiSelectMulti<cars>, { props: { manager, minOptions, validation } });
					const validatorOptionsRange = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validatorOptionsRange;

					expect(validatorOptionsRange).toBeNull();
				}
			);
			test("Mit Prop 'minOptions = 2' ohne Prop 'validation' wird ein OptionsRange-Validator hinzugefügt", () => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, minOptions: 2 } });
				const validatorOptionsRange = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validatorOptionsRange;

				expect(validatorOptionsRange).not.toBeNull();
				expect(validatorOptionsRange).toBeInstanceOf(BasicValidator);
			});

			test("Mit Prop 'minOptions = 2' und nur einer selektierten Option wird ein Fehler für die OptionsRange-Validierung generiert", () => {
				const { manager, singleSelection } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: manager, minOptions: 2, modelValue: singleSelection } });
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Es müssen mindestens 2 Optionen ausgewählt sein.");
			});

			test("Mit Prop 'minOptions = 2' und 2 selektierten Optionen  wird kein Fehler für die OptionsRange-Validierung generiert", () => {
				const { manager, twoSelections } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: manager, minOptions: 2, modelValue: twoSelections } });
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			});

			test("Mit Prop 'minOptions = 2' und ohne Selektion wird kein Fehler für die OptionsRange-Validierung generiert", () => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: manager, minOptions: 2, modelValue: [] } });
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			});
		});

		describe.concurrent("maxOptions", () => {
			test.each([
				[undefined, "ohne Prop 'validation'", undefined],
				[undefined, "mit Prop 'validation'", () => getValidatorFehler()],
				[2, "mit Prop 'validation'", () => getValidatorFehler()],
			])(
				"Mit Prop 'maxOptions = %s' und %s wird kein OptionsRange-Validator hinzugefügt",
				(maxOptions, _vString, validation) => {
					const { manager } = createTestData();
					const wrapper = mount(UiSelectMulti<cars>, { props: { manager, maxOptions, validation } });
					const validatorOptionsRange = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validatorOptionsRange;

					expect(validatorOptionsRange).toBeNull();
				}
			);

			test("Mit Prop 'maxOptions = 2' ohne Prop 'validation' wird ein OptionsRange-Validator hinzugefügt", () => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, maxOptions: 2 } });
				const validatorOptionsRange = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validatorOptionsRange;

				expect(validatorOptionsRange).not.toBeNull();
				expect(validatorOptionsRange).toBeInstanceOf(BasicValidator);
			});

			test("Mit Prop 'maxOptions = 2' und 3 selektierten Optionen wird ein Fehler für die OptionsRange-Validierung generiert", () => {
				const { manager, threeSelections } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: manager, maxOptions: 2, modelValue: threeSelections } });
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Es dürfen maximal 2 Optionen ausgewählt sein.");
			});

			test("Mit Prop 'maxOptions = 2' und 2 selektierten Optionen wird kein Fehler für die OptionsRange-Validierung generiert", () => {
				const { manager, twoSelections } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: manager, maxOptions: 2, modelValue: twoSelections } });
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			});

			test("Mit Prop 'maxOptions = 2' und ohne Selektion wird kein Fehler für die OptionsRange-Validierung generiert", () => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager: manager, maxOptions: 2, modelValue: [] } });
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			});
		});

		describe.concurrent("Selektionsvalidierung", () => {

			test("SelectManager: Bei einer bestehenden Selektion, die nicht mehr gültig ist, wird ein Validatorfehler angezeigt.", () => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { manager, required: true, modelValue: [{ marke: 'Opel', color: 'blau', baujahr: 2000 }] } });
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der ausgewählte Wert ist nicht mehr gültig.");
			});

			test("Kein SelectManager: Bei einer bestehenden Selektion, die nicht mehr gültig ist, wird ein entsprechender Validatorfehler angezeigt.", () => {
				const { singleSelection } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, { props: { modelValue: singleSelection } });
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der ausgewählte Wert ist nicht mehr gültig.");
			});

			test("CoreTypeSelectManager: Kein CoreTypeDataManager: Bei einer bestehenden Selektion, die nicht mehr gültig ist, wird ein entsprechender Validatorfehler angezeigt.", () => {
				const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>();
				const wrapper = mount(UiSelectMulti<KlassenartKatalogEintrag>, { props:
					{
						manager,
						modelValue: [Klassenart.data().getEintragBySchuljahrUndWert(2010, Klassenart.HA_AB)!],
					},
				});
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der ausgewählte Wert ist nicht mehr gültig.");
			});

			test("CoreTypeSelectManager: Bei einer bestehenden Selektion, die wegen des Schuljahrs nicht mehr gültig ist, wird ein entsprechender Validatorfehler angezeigt.", () => {
				const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>(
					{
						clazz: Klassenart.class,
						schuljahr: 2011,
					});
				const wrapper = mount(UiSelectMulti<KlassenartKatalogEintrag>, { props:
					{
						manager,
						modelValue: [Klassenart.data().getEintragBySchuljahrUndWert(2010, Klassenart.HA_AB)!],
					},
				});
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der ausgewählte Wert ist nur bis zum Schuljahr 2010 gültig.");
			});

			test("CoreTypeSelectManager: Bei einer bestehenden Selektion, die wegen des Schuljahrs noch nicht gültig ist, wird ein entsprechender Validatorfehler angezeigt.", () => {
				const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>(
					{
						clazz: Klassenart.class,
						schuljahr: 2009,
					});
				const wrapper = mount(UiSelectMulti<KlassenartKatalogEintrag>, { props:
					{
						manager,
						modelValue: [Klassenart.data().getEintragBySchuljahrUndWert(2015, Klassenart.HA_AB)!],
					},
				});
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der ausgewählte Wert ist erst ab dem Schuljahr 2011 gültig.");
			});

			test("CoreTypeSelectManager: Bei einer bestehenden Selektion, die wegen des Schuljahrs nicht mehr gültig ist, wird ein entsprechender Validatorfehler angezeigt (manager ohne Schuljahr).", () => {
				const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: Klassenart.class });
				const wrapper = mount(UiSelectMulti<KlassenartKatalogEintrag>, { props:
					{
						manager,
						modelValue: [Klassenart.data().getEintragBySchuljahrUndWert(2000, Klassenart.HA_AB)!],
					},
				});
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der ausgewählte Wert ist nur bis zum Schuljahr 2010 gültig.");
			});

			test.each([
				{ label: "einzelne Schulform", schulformen: Schulform.GY },
				{ label: "Array aus Schulformen", schulformen: [Schulform.GY] },
			])("CoreTypeSelectManager: Bei einer bestehenden Selektion, die wegen der Schulform nicht mehr gültig ist, wird ein entsprechender Validatorfehler angezeigt. ($label)", ({ schulformen }) => {
				const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>(
					{
						clazz: Klassenart.class,
						schulformen,
					});
				const wrapper = mount(UiSelectMulti<KlassenartKatalogEintrag>, { props:
					{
						manager,
						modelValue: [Klassenart.data().getEintragBySchuljahrUndWert(2020, Klassenart.HA_AB)!],
					},
				});
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der ausgewählte Wert ist für die Schulform ihrer Schule nicht mehr gültig.");
			});

			test.each([
				{
					label: "keine Selektion",
					managerConfig: { clazz: Klassenart.class, schuljahr: 2020 },
					getModelValue: () => null,
				},
				{
					label: "Selektion ist im Manager-Schuljahr gültig",
					managerConfig: { clazz: Klassenart.class, schuljahr: 2020 },
					getModelValue: () => [Klassenart.data().getEintragBySchuljahrUndWert(2020, Klassenart.HA_AB)!],
				},
				{
					label: "Manager ohne Schuljahr, Selektion ist der aktuellste Eintrag",
					managerConfig: { clazz: Klassenart.class },
					getModelValue: () => [Klassenart.data().getEintragBySchuljahrUndWert(2020, Klassenart.HA_AB)!],
				},
				{
					label: "Manager ohne Schulformen, Schulformprüfung entfällt",
					managerConfig: { clazz: Klassenart.class, schuljahr: 2020 },
					getModelValue: () => [Klassenart.data().getEintragBySchuljahrUndWert(2020, Klassenart.HA_AB)!],
				},
				{
					label: "Schulform passt – einzelne Schulform",
					managerConfig: { clazz: Klassenart.class, schuljahr: 2020, schulformen: Schulform.H },
					getModelValue: () => [Klassenart.data().getEintragBySchuljahrUndWert(2020, Klassenart.HA_AB)!],
				},
				{
					label: "Schulform passt – Array aus Schulformen",
					managerConfig: { clazz: Klassenart.class, schuljahr: 2020, schulformen: [Schulform.H] },
					getModelValue: () => [Klassenart.data().getEintragBySchuljahrUndWert(2020, Klassenart.HA_AB)!],
				},
				{
					label: "Eintrag ist für alle Schulformen gültig (keine Schulformeinschränkung im Eintrag selbst)",
					managerConfig: { clazz: Klassenart.class, schuljahr: 2020, schulformen: Schulform.GY },
					getModelValue: () => [Klassenart.data().getEintragBySchuljahrUndWert(2020, Klassenart.RK)!],
				},
			])("CoreTypeSelectManager: Kein Validatorfehler bei gültiger Selektion: $label", ({ managerConfig, getModelValue }) => {
				const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>(managerConfig);
				const wrapper = mount(UiSelectMulti<KlassenartKatalogEintrag>, { props:
						{
							manager,
							modelValue: getModelValue(),
						},
				});
				const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			});
		});
	});

});

describe.concurrent("Teste Watcher und Computeds", () => {

	describe.concurrent("Watcher auf model.value:", () => {
		test("Exception, wenn model.value null bei 'nullable=false' enthält", () => {
			const { manager } = createTestData();

			expect(() => mount(UiSelectMulti<cars>, {
				props: { manager, nullable: false, modelValue: null },
			})).toThrow('Ungültiges v-model: null oder undefined bei nullable = false');
		});

		test("Exception, wenn model.value undefined bei 'nullable=false' enthält", () => {
			const { manager } = createTestData();

			expect(() => mount(UiSelectMulti<cars>, {
				props: { manager, nullable: false, modelValue: undefined },
			})).toThrow('Ungültiges v-model: null oder undefined bei nullable = false');
		});

		test("Keine Exception, wenn model.value null bei 'nullable=true' enthält", () => {
			const { manager } = createTestData();

			expect(() => mount(UiSelectMulti<cars>, {
				props: { manager, nullable: true, modelValue: null },
			})).not.toThrow('Ungültiges v-model: null oder undefined bei nullable = false');
		});

		test("Keine Exception, wenn model.value undefined bei 'nullable=true' enthält", () => {
			const { manager } = createTestData();

			expect(() => mount(UiSelectMulti<cars>, {
				props: { manager, nullable: true, modelValue: undefined },
			})).not.toThrow('Ungültiges v-model: null oder undefined bei nullable = false');
		});
	});

	test("computed->modelArray ist [], wenn model.value leer ist", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager } });

		expect(wrapper.findComponent({ name: "UiSelectMulti" }).vm.modelArray).toEqual([]);
	});

	test("computed->modelArray ist [], wenn model.value null ist", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: null } });

		expect(wrapper.findComponent({ name: "UiSelectMulti" }).vm.modelArray).toEqual([]);
	});

	test("computed->modelArray ist [], wenn model.value undefined ist", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: undefined } });

		expect(wrapper.findComponent({ name: "UiSelectMulti" }).vm.modelArray).toEqual([]);
	});

	test("computed->modelArray ist model.value (kein Proxy) als Array", () => {
		const { manager, twoSelections } = createTestData();
		const selectionAsArrayList = new ArrayList<cars>();
		selectionAsArrayList.add(twoSelections[0]);
		selectionAsArrayList.add(twoSelections[1]);

		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: selectionAsArrayList } });
		expect(Array.isArray(selectionAsArrayList)).toBeFalsy();

		const modelArray = wrapper.findComponent({ name: "UiSelectMulti" }).vm.modelArray;

		expect(modelArray).toEqual(twoSelections);
		expect(Array.isArray(modelArray)).toBeTruthy();
	});

	describe.concurrent("computed->state bildet alle relevanten Eigenschaften korrekt ab", () => {
		const { manager, twoSelections } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, {
			props: {
				manager,
				searchable: true,
				deepSearchAttributes: ["baujahr"],
				required: true,
				removable: true,
				disabled: false,
				readonly: false,
				headless: false,
				label: "Car",
				validation: () => new ArrayList<ValidatorFehler>(),
				modelValue: twoSelections,
			},
		});

		const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
		const state = vm.state;
		const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

		test("state.instanceId ist eine UUID", () => {
			expect(state.instanceId).toMatch(uuidRegex);
		});

		test("state.multi ist true", () => {
			expect(state.multi).toBe(true);
		});

		test("state.label entspricht props.label", () => {
			expect(state.label).toBe("Car");
		});

		test("state.manager entspricht props.manager", () => {
			expect(state.manager).toBe(manager);
		});

		test("state.searchable entspricht props.searchable", () => {
			expect(state.searchable).toBe(true);
		});

		test("state.deepSearchAttributes entspricht props.deepSearchAttributes", () => {
			expect(state.deepSearchAttributes).toEqual(["baujahr"]);
		});

		test("state.required entspricht props.required", () => {
			expect(state.required).toBe(true);
		});

		test("state.removable entspricht props.removable", () => {
			expect(state.removable).toBe(true);
		});

		test("state.disabled entspricht props.disabled", () => {
			expect(state.disabled).toBe(false);
		});

		test("state.readonly entspricht props.readonly", () => {
			expect(state.readonly).toBe(false);
		});

		test("state.headless entspricht props.headless", () => {
			expect(state.headless).toBe(false);
		});

		test("state.validationResult entspricht validationResult der Komponente", () => {
			const validatorResult = wrapper.findComponent({ name: "UiSelectMulti" }).vm.validationResult;
			expect(state.validationResult).toBe(validatorResult);
		});

		test("state.search entspricht ''", () => {
			expect(state.search).toBe("");
		});

		test("state.search wird bei Eingabe aktualisiert", async () => {
			const input = wrapper.find(".ui-select-multi--search input");
			await input.setValue("BMW");
			expect(vm.state.search).toBe("BMW");
		});
	});

	describe.concurrent("computed->selectionLimitText", () => {
		const { manager } = createTestData();

		test("gibt null zurück, wenn minOptions und maxOptions nicht gesetzt sind", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionLimitText).toBeNull();
		});

		test("gibt 'min. 2' zurück, wenn 'minOptions = 2' gesetzt ist", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, minOptions: 2 } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionLimitText).toBe("min. 2");
		});

		test("gibt 'max. 5' zurück, wenn 'maxOptions = 5' gesetzt ist", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, maxOptions: 5 } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionLimitText).toBe("max. 5");
		});

		test("gibt '1 Option' zurück, wenn 'minOptions = maxOptions = 1' ist", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, minOptions: 3, maxOptions: 3 } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionLimitText).toBe("3 Optionen");
		});

		test("gibt '3 Optionen' zurück, wenn 'minOptions = maxOptions = 3'", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, minOptions: 3, maxOptions: 3 } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionLimitText).toBe("3 Optionen");
		});

		test("gibt '2 - 5 Optionen' zurück, wenn 'minOptions = 2' und 'maxOptions = 5'", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, minOptions: 2, maxOptions: 5 } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionLimitText).toBe("2 - 5 Optionen");
		});

		test("tauscht automatisch min und max, wenn min > max", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, minOptions: 6, maxOptions: 3 } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionLimitText).toBe("3 - 6 Optionen");
		});
	});

	describe.concurrent("computed->selectionBubbleClasses", () => {
		test.each([
			{ disabled: true, headless: false, expectedClasses: "bg-ui-disabled border-ui-ondisabled mt-[0.35rem]" },
			{ disabled: true, headless: true, expectedClasses: "bg-ui-disabled border-ui-ondisabled" },
			{ disabled: false, headless: false, expectedClasses: "bg-ui-selected border-ui-selected mt-[0.35rem]" },
			{ disabled: false, headless: true, expectedClasses: "bg-ui-selected border-ui-selected" },

		])(
			"disabled=$disabled, headless=$headless → Klassen enthalten '$expectedClasses'",
			({ disabled, headless, expectedClasses }) => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, {
					props: { manager, disabled, headless },
				});
				const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;

				expect(vm.selectionBubbleClasses).toContain(expectedClasses);
			}
		);
	});

	describe.concurrent("computed->selectionBubbleIconClasses", () => {
		test.each([
			{ disabled: true, expectedClasses: "icon-ui-ondisabled" },
			{ disabled: false, expectedClasses: "icon-ui-onselected" },

		])(
			"disabled=$disabled → Klassen enthalten '$expectedClasses'",
			({ disabled, expectedClasses }) => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, {
					props: { manager, disabled },
				});
				const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;

				expect(vm.selectionBubbleIconClasses).toContain(expectedClasses);
			}
		);
	});

	describe.concurrent("computed->selectionBubbleTextClasses", () => {
		test.each([
			{ disabled: true, expectedClasses: "text-ui-ondisabled" },
			{ disabled: false, expectedClasses: "text-ui-onselected" },

		])(
			"disabled=$disabled → Klassen enthalten '$expectedClasses'",
			({ disabled, expectedClasses }) => {
				const { manager } = createTestData();
				const wrapper = mount(UiSelectMulti<cars>, {
					props: { manager, disabled },
				});
				const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;

				expect(vm.selectionBubbleTextClasses).toContain(expectedClasses);
			}
		);
	});

	describe.concurrent("computed->selectionBubbleIconClasses", () => {
		const { manager } = createTestData();

		test("disabled = true → Styles enthält Klasse icon-ui-ondisabled", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, disabled: true } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionBubbleIconClasses).toContain("icon-ui-ondisabled");
		});

		test("disabled = false → Styles enthält Klasse icon-ui-onselected", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, disabled: false } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionBubbleIconClasses).toContain("icon-ui-onselected");
		});
	});
});

describe.concurrent("Teste Funktionen", () => {

	test("function->isSelected(option) ist false, wenn model.value = [] ist", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: [] } });

		const isSelected = wrapper.findComponent({ name: "UiSelectMulti" }).vm.isSelected;
		expect(isSelected(manager.filteredOptions.get(0))).toBe(false);
	});

	test("function->isSelected(option) ist false, wenn model.value nicht option entspricht", () => {
		const { manager, singleSelection } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: singleSelection } });

		const isSelected = wrapper.findComponent({ name: "UiSelectMulti" }).vm.isSelected;
		expect(isSelected(manager.filteredOptions.get(1))).toBe(false);
	});

	test("function->isSelected(option) ist true, wenn model.value option entspricht", () => {
		const { manager, singleSelection } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: singleSelection } });

		const isSelected = wrapper.findComponent({ name: "UiSelectMulti" }).vm.isSelected;
		expect(isSelected(manager.filteredOptions.get(0))).toBe(true);
	});

	test("function->selectOption(option) wenn die Option noch nicht selektiert ist setzt model.value auf die Option", async () => {
		const { manager, singleSelection } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, searchable: true } });
		const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;

		vm.selectOption(manager.filteredOptions.get(0));
		await wrapper.vm.$nextTick();

		const emits = wrapper.emitted('update:modelValue');
		expect(emits).toBeDefined();
		expect(emits![0][0]).toEqual(singleSelection);
	});

	test("function->selectOption(option) wenn die Option schon selektiert ist erzeugt eine Fehlermeldung", () => {
		const { manager, twoSelections } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: twoSelections } });

		const selectOption = wrapper.findComponent({ name: "UiSelectMulti" }).vm.selectOption;
		expect(() => selectOption(manager.filteredOptions.get(0))).toThrow("Die Option BMW ist bereits selektiert.");
	});

	describe.concurrent("function->deselectOption(option)", () => {
		test("bei 'deselectAllowed() = false' wirft eine Fehlermeldung", () => {
			const { manager, singleSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: singleSelection } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;

			expect(() => vm.deselectOption(manager.filteredOptions.get(0))).toThrow("Das Select ist auf removable=false gesetzt, daher kann der Eintrag nicht deselektiert werden");


			const deselectOption = wrapper.findComponent({ name: "UiSelectMulti" }).vm.deselectOption;
			expect(() => deselectOption(manager.filteredOptions.get(0))).toThrow("Das Select ist auf removable=false gesetzt, daher kann der Eintrag nicht deselektiert werden");
		});

		test("entfernt eine ausgewählte Option, wenn sie in der Liste enthalten ist, aus model.value", async () => {
			const { manager, singleSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: true, modelValue: singleSelection } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;

			vm.deselectOption(manager.filteredOptions.get(0));
			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toEqual([]);
		});

		test("verändert model.value nicht, wenn die Option nicht in der Liste enthalten ist.", async () => {
			const { manager, nonExistingOption, singleSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: singleSelection } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;

			vm.deselectOption(nonExistingOption);
			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeUndefined();
		});

		test("setzt den Suchbegriff bei 'searchable=true' zurück", async () => {
			const { manager, singleSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, searchable: true, modelValue: singleSelection } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;

			const input = wrapper.find('.ui-select-multi--search input');
			await input.setValue("BMW");
			expect(vm.state.search).toBe("BMW");

			vm.deselectOption(singleSelection[0]);
			await wrapper.vm.$nextTick();

			expect(vm.state.search).toBe("");
		});
	});

	describe.concurrent("function->clearSelection()", () => {

		test("wirft Exception, wenn removable=false", () => {
			const { manager, twoSelections } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: twoSelections } });

			const clearSelection = wrapper.findComponent({ name: "UiSelectMulti" }).vm.clearSelection;
			expect(() => clearSelection()).toThrow("Das Select ist auf removable=false gesetzt, daher kann die komplette Selektion nicht gelöscht werden.");
		});

		test("setzt model.value auf [], wenn 'removable=true'", async () => {
			const { manager, twoSelections } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: true, modelValue: twoSelections } });

			const clearSelection = wrapper.findComponent({ name: "UiSelectMulti" }).vm.clearSelection;
			clearSelection();
			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toEqual([]);
		});

		test("setzt search zurück, wenn 'removable=true'", async () => {
			const { manager, twoSelections } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: true, searchable: true, modelValue: twoSelections } });

			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;

			const input = wrapper.find('.ui-select-multi--search input');
			await input.setValue("BMW");
			expect(vm.state.search).toBe("BMW");

			const clearSelection = vm.clearSelection;
			clearSelection();
			await wrapper.vm.$nextTick();

			expect(vm.state.search).toBe("");
		});

		test("schließt das Dropdown", async () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager } });

			const dropdown = wrapper.find('.ui-select-multi--dropdown');
			const combobox = wrapper.find('.ui-select-multi--combobox');
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;

			await combobox.trigger("click");

			expect(dropdown.attributes("data-popover-open")).toBe("true");

			const clearSelection = vm.clearSelection;
			clearSelection();
			expect(dropdown.attributes("data-popover-open")).toBeUndefined();
		});
	});

	describe.concurrent("function->deselectAllowed()", () => {
		test("ist true wenn 'removable=true'", () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: true } });

			const deselectAllowed = wrapper.findComponent({ name: "UiSelectMulti" }).vm.deselectAllowed;
			expect(deselectAllowed()).toBe(true);
		});

		test("ist true wenn 'removable=false' und mehr als eine Option selektiert ist", () => {
			const { manager, twoSelections } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: twoSelections } });

			const deselectAllowed = wrapper.findComponent({ name: "UiSelectMulti" }).vm.deselectAllowed;
			expect(deselectAllowed()).toBe(true);
		});

		test("ist false wenn 'removable=false'", () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false } });

			const deselectAllowed = wrapper.findComponent({ name: "UiSelectMulti" }).vm.deselectAllowed;
			expect(deselectAllowed()).toBe(false);
		});
	});

	test("function->hasSelection() ist true, wenn model.value Optionen enthält", () => {
		const { manager, twoSelections } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: twoSelections } });

		const hasSelection = wrapper.findComponent({ name: "UiSelectMulti" }).vm.hasSelection;
		expect(hasSelection()).toBe(true);
	});

	test("function->hasSelection() ist false, wenn model.value leer ist", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager } });

		const hasSelection = wrapper.findComponent({ name: "UiSelectMulti" }).vm.hasSelection;
		expect(hasSelection()).toBe(false);
	});
});

/**
 * Erstellt Testdaten für die Tests des UiSelectMultis.
 *
 * @returns die testdaten
 */
function createTestData() {
	const options: cars[] = [{ marke: "BMW", color: "blue", baujahr: 2006 },
		{ marke: "Audi", color: "red", baujahr: 2008 }, { marke: "Opel", color: "schwarz", baujahr: 2006 }];
	const optionDisplayText = (option: cars) => option.marke;
	const selectionDisplayText = (option: cars) => option.marke;
	const manager = new SelectManager<{ marke: string, color: string, baujahr: number }>({ options: options, optionDisplayText: optionDisplayText, selectionDisplayText: selectionDisplayText });
	const twoSelections = [manager.filteredOptions.get(0), manager.filteredOptions.get(1)];
	const threeSelections = [manager.filteredOptions.get(0), manager.filteredOptions.get(1), manager.filteredOptions.get(2)];
	const singleSelection = [manager.filteredOptions.get(0)];
	const nonExistingOption = [{ marke: "VW", color: "schwarz", baujahr: 2012 }];
	return { manager, singleSelection, twoSelections, threeSelections, nonExistingOption };
}

type cars = { marke: string, color: string, baujahr: number };

class CustomValidatorRequired extends BasicValidator {

	constructor() {
		super(ValidatorFehlerart.MUSS);
		this.run();
	}

	protected pruefe(): boolean {
		this.addFehler(0, "Custom-Validierung fehlgeschlagen");
		return false;
	}
}

function getValidatorFehler(): List<ValidatorFehler> {
	const customValidator = new CustomValidatorRequired();
	customValidator.run();
	return customValidator.getFehler();
}
