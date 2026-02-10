import { mount } from "@vue/test-utils";
import UiSelectMulti from "../../../../src/ui/controls/select/UiSelectMulti.vue";
import { describe, test, expect, vi, beforeAll } from "vitest";
import { SelectManager } from "../../../../src/ui/controls/select/manager/SelectManager";
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";

beforeAll(() => {
	HTMLElement.prototype.showPopover = vi.fn(function(this: HTMLElement) {
		this.dataset.popoverOpen = 'true';
	});
	HTMLElement.prototype.hidePopover = vi.fn(function(this: HTMLElement) {
		delete this.dataset.popoverOpen;
	});
});

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

		test("props.searchable entspricht false", () => {
			expect(props.searchable).toBe(false);
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

		test("props.doValidate ist definiert und eine Funktion", () => {
			expect(props.validation).toBeDefined();
			expect(typeof props.validation).toBe('function');
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

	test("Mit Prop 'required = false' wird die Komponente ohne Stern-Icon angezeigt", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, required: false } });
		const labelRequired = wrapper.find('.ui-select-multi--label--required');

		expect(labelRequired.exists()).toBeFalsy();

	});

	test("Mit Prop 'required = true' wird die Komponente mit Stern-Icon angezeigt", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, required: true } });
		const labelRequired = wrapper.find(".ui-select-multi--label--required");

		expect(labelRequired.find('span.i-ri-asterisk').exists()).toBeTruthy();
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
			const { manager, multiSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: multiSelection } });

			const firstOption = wrapper.find('.ui-select-multi--dropdown li');

			await firstOption.trigger('mousedown');
			await wrapper.vm.$nextTick();
			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toEqual([manager.filteredOptions.get(1)]);
		});

		test("bei zwei selektierten Elementen werden Löschenbuttons in den Selektionen angezeigt", () => {
			const { manager, multiSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: multiSelection } });

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
		const { manager, multiSelection } = createTestData();
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
			await wrapper.setProps({ modelValue: multiSelection });
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

		test("Die Selektion wird nicht angepasst, wenn alle Elemente in manager.filteredOptions enthalten sind", async () => {
			const { manager, multiSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: multiSelection } });

			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeUndefined();

		});

		test("Die Selektion wird angepasst, wenn nicht alle Elemente in manager.filteredOptions enthalten sind", async () => {
			const { manager, multiSelection, nonExistingOption } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: [...multiSelection, ...nonExistingOption] } });

			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toEqual(multiSelection);
		});
	});

	describe.concurrent("Watcher auf props.manager.filteredOptions:", () => {
		test("Die Selektion wird nicht angepasst, wenn alle Elemente im neuen manager.filteredOptions enthalten sind", async () => {
			const { manager, multiSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: multiSelection } });

			const newFilteredOptions = new ArrayList<cars>();
			newFilteredOptions.add(multiSelection[0]);
			newFilteredOptions.add(multiSelection[1]);
			manager.unfilteredOptions = newFilteredOptions;

			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeUndefined();

		});

		test("Die Selektion wird angepasst, wenn nicht alle Elemente im neuen manager.filteredOptions enthalten sind", async () => {
			const { manager, multiSelection, nonExistingOption } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: multiSelection } });

			const newFilteredOptions = new ArrayList<cars>();
			newFilteredOptions.add(nonExistingOption[0]);
			manager.unfilteredOptions = newFilteredOptions;

			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toEqual([]);
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
		const { manager, multiSelection } = createTestData();
		const selectionAsArrayList = new ArrayList<cars>();
		selectionAsArrayList.add(multiSelection[0]);
		selectionAsArrayList.add(multiSelection[1]);

		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: selectionAsArrayList } });
		expect(Array.isArray(selectionAsArrayList)).toBeFalsy();

		const modelArray = wrapper.findComponent({ name: "UiSelectMulti" }).vm.modelArray;

		expect(modelArray).toEqual(multiSelection);
		expect(Array.isArray(modelArray)).toBeTruthy();
	});

	describe.concurrent("computed->state bildet alle relevanten Eigenschaften korrekt ab", () => {
		const { manager, multiSelection } = createTestData();
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
				validator: undefined,
				modelValue: multiSelection,
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

		test("state.validator entspricht props.validation", () => {
			expect(state.validation).toBe(undefined);
		});

		test("state.isValid entspricht true", () => {
			expect(state.isValid).toBe(true);
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

		test("gibt null zurück, wenn minOptions <= 0 ist", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, minOptions: -1 } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionLimitText).toBeNull();
		});

		test("gibt null zurück, wenn maxOptions <= 0 ist", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, maxOptions: -1 } });
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
		const { manager } = createTestData();

		test("disabled = true, headless = false → Styles enthalten bg-ui-disabled, text-ui-ondisabled, border-ui-disabled und mt-[0.35rem]", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, disabled: true, headless: false } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionBubbleClasses).toContain("bg-ui-disabled");
			expect(vm.selectionBubbleClasses).toContain("text-ui-ondisabled");
			expect(vm.selectionBubbleClasses).toContain("border-ui-disabled");
			expect(vm.selectionBubbleClasses).toContain("mt-[0.35rem]");
		});

		test("disabled = true, headless = true → Styles enthalten bg-ui-disabled, text-ui-ondisabled, border-ui-disabled ohne mt-[0.35rem]", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, disabled: true, headless: true } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionBubbleClasses).toContain("bg-ui-disabled");
			expect(vm.selectionBubbleClasses).toContain("text-ui-ondisabled");
			expect(vm.selectionBubbleClasses).toContain("border-ui-disabled");
			expect(vm.selectionBubbleClasses).not.toContain("mt-[0.35rem]");
		});

		test("disabled = false, headless = false → Styles enthalten bg-ui-selected, text-ui-onselected, border-ui-selected und mt-[0.35rem]", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, disabled: false, headless: false } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionBubbleClasses).toContain("bg-ui-selected");
			expect(vm.selectionBubbleClasses).toContain("text-ui-onselected");
			expect(vm.selectionBubbleClasses).toContain("border-ui-selected");
			expect(vm.selectionBubbleClasses).toContain("mt-[0.35rem]");
		});

		test("disabled = false, headless = true → Styles enthalten bg-ui-selected, text-ui-onselected, border-ui-selected ohne mt-[0.35rem]", () => {
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, disabled: false, headless: true } });
			const vm = wrapper.findComponent({ name: "UiSelectMulti" }).vm;
			expect(vm.selectionBubbleClasses).toContain("bg-ui-selected");
			expect(vm.selectionBubbleClasses).toContain("text-ui-onselected");
			expect(vm.selectionBubbleClasses).toContain("border-ui-selected");
			expect(vm.selectionBubbleClasses).not.toContain("mt-[0.35rem]");
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
		const { manager, multiSelection } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: multiSelection } });

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
			const { manager, multiSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: multiSelection } });

			const clearSelection = wrapper.findComponent({ name: "UiSelectMulti" }).vm.clearSelection;
			expect(() => clearSelection()).toThrow("Das Select ist auf removable=false gesetzt, daher kann die komplette Selektion nicht gelöscht werden.");
		});

		test("setzt model.value auf [], wenn 'removable=true'", async () => {
			const { manager, multiSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: true, modelValue: multiSelection } });

			const clearSelection = wrapper.findComponent({ name: "UiSelectMulti" }).vm.clearSelection;
			clearSelection();
			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toEqual([]);
		});

		test("setzt search zurück, wenn 'removable=true'", async () => {
			const { manager, multiSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: true, searchable: true, modelValue: multiSelection } });

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
			const { manager, multiSelection } = createTestData();
			const wrapper = mount(UiSelectMulti<cars>, { props: { manager, removable: false, modelValue: multiSelection } });

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
		const { manager, multiSelection } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: multiSelection } });

		const hasSelection = wrapper.findComponent({ name: "UiSelectMulti" }).vm.hasSelection;
		expect(hasSelection()).toBe(true);
	});

	test("function->hasSelection() ist false, wenn model.value leer ist", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager } });

		const hasSelection = wrapper.findComponent({ name: "UiSelectMulti" }).vm.hasSelection;
		expect(hasSelection()).toBe(false);
	});

	test("function->getSelectionDiff() gibt null zurück wenn nichts selektiert ist", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager } });
		const getSelectionDiff = wrapper.findComponent({ name: "UiSelectMulti" }).vm.getSelectionDiff;

		expect(getSelectionDiff()).toBeNull();
	});

	test("function->getSelectionDiff() gibt null zurück, wenn alle ausgewählten Optionen in filteredOptions sind", () => {
		const { manager, singleSelection } = createTestData();
		const wrapper = mount(UiSelectMulti<cars>, { props: { manager, modelValue: singleSelection } });
		const getSelectionDiff = wrapper.findComponent({ name: "UiSelectMulti" }).vm.getSelectionDiff;

		expect(getSelectionDiff()).toBeNull();
	});

	// Weitere Tests zu getSelectionDiff werden hier nicht implementiert, weil sie automatisch mit den WatcherTests getestet werden. Anders ist das Testen
	// weiterer Ergebnisse von getSelectionDiff nicht möglich


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
	const multiSelection = [manager.filteredOptions.get(0), manager.filteredOptions.get(1)];
	const singleSelection = [manager.filteredOptions.get(0)];
	const nonExistingOption = [{ marke: "VW", color: "schwarz", baujahr: 2012 }];
	return { manager, singleSelection, multiSelection, nonExistingOption };
}

type cars = { marke: string, color: string, baujahr: number };

