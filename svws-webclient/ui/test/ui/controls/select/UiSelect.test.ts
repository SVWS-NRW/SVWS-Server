import { DOMWrapper, mount } from "@vue/test-utils";
import UiSelect from "../../../../src/ui/controls/select/UiSelect.vue";
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

describe.concurrent("Komponente kann gemounted werden", () => {
	test("HTML wird erzeugt", () => {
		const wrapper = mount(UiSelect);
		expect(wrapper.html()).includes("ui-select");
	});
});

describe.concurrent("PropHandhabung läuft korrekt", () => {

	describe.concurrent("Mount mit default Props", () => {
		const wrapper = mount(UiSelect);
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

		test("props.validator entspricht undefined", () => {
			expect(props.validator).toBeUndefined();
		});

		test("props.doValidate ist definiert und eine Funktion", () => {
			expect(props.validation).toBeDefined();
			expect(typeof props.validation).toBe('function');
		});
	});

	test("Mit Prop 'label = Mein Label' wird 'Mein Label' als Label angezeigt", () => {
		const wrapper = mount(UiSelect, { props: { label: 'Mein Label' } });
		const label = wrapper.find('.ui-select--label--text');
		expect(label.text()).toBe('Mein Label');
	});

	test("Mit Prop 'searchable = true' wird die Komponente mit einem Input gerendert", () => {
		const wrapper = mount(UiSelect, { props: { searchable: true } });
		const input = wrapper.find('input.ui-select--search');
		expect(input.exists()).toBeTruthy();
		expect(input.element.tagName).toBe('INPUT');
	});

	test("Mit Prop 'searchable = false' wird die Komponente ohne ein Input gerendert", () => {
		const wrapper = mount(UiSelect, { props: { searchable: false } });
		const input = wrapper.find('input.ui-select--search input');
		expect(input.exists()).toBeFalsy();
	});

	test("Mit Prop 'deepSearchAttributes' werden bei einer Suche auch Treffer in den Attributen angezeigt", async () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true, deepSearchAttributes: ['color'] } });
		const input = wrapper.find('input.ui-select--search');
		await input.setValue('red');
		await wrapper.vm.$nextTick();

		const options = wrapper.findAll('li');
		expect(options).toHaveLength(1);
		expect(options[0].find('span').text()).toBe('Audi');
	});

	test("Mit Prop 'required = false' wird die Komponente ohne Stern-Icon angezeigt", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, required: false } });
		const labelSpan = wrapper.find('[id^="uiSelectLabel_"]');
		const labelWrapper = new DOMWrapper(labelSpan.element.parentElement);

		expect(labelWrapper.find('span.i-ri-asterisk').exists()).toBeFalsy();
	});

	test("Mit Prop 'required = true' wird die Komponente mit Stern-Icon angezeigt", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, required: true } });
		const labelSpan = wrapper.find('[id^="uiSelectLabel_"]');
		const labelWrapper = new DOMWrapper(labelSpan.element.parentElement);

		expect(labelWrapper.find('span.i-ri-asterisk').exists()).toBeTruthy();
	});

	describe.concurrent("Mit Prop 'readonly = false'", () => {
		test("wird kein readonly-Icon angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: createTestData().manager, readonly: false } });
			expect(wrapper.find('.ui-select--label--readonly').exists()).toBeFalsy();
		});

		test("und 'headless = false' werden Lösch- und Aufklapp-Icons rechts angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: createTestData().manager, headless: false, readonly: false } });
			const iconsRight = wrapper.find('.ui-select--icons-right');
			expect(iconsRight.find('span.i-ri-expand-up-down-line').exists()).toBeTruthy();
			expect(iconsRight.find('button span.i-ri-close-line').exists()).toBeTruthy();
		});

		test("und 'headless = true' werden Lösch- und Aufklapp-Icons links angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: createTestData().manager, headless: true, readonly: false } });
			const iconsLeft = wrapper.find('.ui-select--icons-left');
			expect(iconsLeft.find('span.i-ri-expand-up-down-line').exists()).toBeTruthy();
			expect(iconsLeft.find('button span.i-ri-close-line').exists()).toBeTruthy();
		});

		test("und 'searchable = true' wird das Suchfeld angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: createTestData().manager, searchable: true, readonly: false } });
			expect(wrapper.find('input.ui-select--search').exists()).toBeTruthy();
		});

		test("existiert das Dropdown", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: createTestData().manager, readonly: false } });
			expect(wrapper.find('.ui-select--dropdown').exists()).toBeTruthy();
		});
	});

	describe.concurrent("Mit Prop 'readonly = true'", () => {
		test("wird das readonly-Icon angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: createTestData().manager, readonly: true } });
			const readonly = wrapper.find('.ui-select--label--readonly');
			expect(readonly.exists()).toBeTruthy();
			expect(readonly.find('span.i-ri-lock-line').exists()).toBeTruthy();
		});

		test("und 'headless = false' werden Lösch- und Aufklapp-Icons rechts nicht angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: createTestData().manager, headless: false, readonly: true } });
			const iconsRight = wrapper.find('.ui-select--icons-right');
			expect(iconsRight.exists()).toBeFalsy();
		});

		test("und 'headless = true' werden Lösch- und Aufklapp-Icons links nicht angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: createTestData().manager, headless: true, readonly: true } });
			const iconsLeft = wrapper.find('.ui-select--icons-left');
			expect(iconsLeft.exists()).toBeFalsy();
		});

		test("und 'searchable = true' wird das Suchfeld nicht angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: createTestData().manager, readonly: true } });
			expect(wrapper.find('input.ui-select--search').exists()).toBeFalsy();
		});

		test("existiert das Dropdown nicht", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager: createTestData().manager, readonly: true } });
			expect(wrapper.find('.ui-select--dropdown').exists()).toBeFalsy();
		});
	});

	describe.concurrent("Mit Prop 'removable = false'", () => {
		const { manager, selection } = createTestData();

		test("und 'headless = false' wird kein Löschenbutton im Input rechts angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, headless: false, removable: false,
				modelValue: selection } });
			const iconsRight = wrapper.find('.ui-select--icons-right');
			expect(iconsRight.find('span.i-ri-close-line').exists()).toBeFalsy();
		});

		test("und 'headless = true' wird kein Löschenbutton im Input links angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, headless: true, removable: false,
				modelValue: selection } });
			const iconsLeft = wrapper.find('.ui-select--icons-left');
			expect(iconsLeft.find('span.i-ri-close-line').exists()).toBeFalsy();
		});

		test("bei einem selektierten Element ist dieses nicht löschbar", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, removable: false, modelValue: selection } });

			const firstOption = wrapper.find('.ui-select--dropdown li');

			await firstOption.trigger('mousedown');
			await wrapper.vm.$nextTick();
			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeUndefined();
		});
	});

	describe.concurrent("Mit Prop 'removable = true'", () => {
		const { manager, selection } = createTestData();

		test("und 'headless = false' wird ein Löschenbutton im Input rechts angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, headless: false, removable: true,
				modelValue: selection } });
			const iconsRight = wrapper.find('.ui-select--icons-right');
			expect(iconsRight.find('span.i-ri-close-line').exists()).toBeTruthy();
		});

		test("und 'headless = true' wird ein Löschenbutton in Input links angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, headless: true, removable: true,
				modelValue: selection } });
			const iconsLeft = wrapper.find('.ui-select--icons-left');
			expect(iconsLeft.find('span.i-ri-close-line').exists()).toBeTruthy();
		});

		test("bei einem selektierten Element ist dieses löschbar", async () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, removable: true,
				modelValue: selection } });

			const firstOption = wrapper.find('.ui-select--dropdown li');

			await firstOption.trigger('mousedown');
			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toBeUndefined();
		});
	});

	test("Mit Prop 'nullable = false' wird in der Komponente kein null als v-model zugelassen", () => {
		const { manager } = createTestData();

		expect(() => mount(UiSelect<cars>, {
			props: { manager, nullable: false, modelValue: null },
		})).toThrow("Ungültiges v-model: null oder undefined bei nullable = false");
	});

	test("Mit Prop 'nullable = false' wird in der Komponente kein undefined als v-model zugelassen", () => {
		const { manager } = createTestData();

		expect(() => mount(UiSelect<cars>, {
			props: { manager, nullable: false, modelValue: undefined },
		})).toThrow("Ungültiges v-model: null oder undefined bei nullable = false");
	});

	test("Mit Prop 'nullable = true' wird in der Komponente null als v-model zugelassen", () => {
		const { manager } = createTestData();

		expect(() => mount(UiSelect<cars>, {
			props: { manager, nullable: true, modelValue: null },
		})).not.toThrow("Ungültiges v-model: null oder undefined bei nullable = false");
	});
	test("Mit Prop 'nullable = true' wird in der Komponente undefined als v-model zugelassen", () => {
		const { manager } = createTestData();

		expect(() => mount(UiSelect<cars>, {
			props: { manager, nullable: true, modelValue: undefined },
		})).not.toThrow("Ungültiges v-model: null oder undefined bei nullable = false");
	});

	describe.concurrent("Mit Prop 'disabled = false'", () => {
		const { manager } = createTestData();

		test("wird kein Button disabled dargestellt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, disabled: false } });
			const buttons = wrapper.findAll('button');
			for (const button of buttons) {
				expect(button.attributes('disabled')).toBeUndefined();
			}
		});

		test("und 'searchable = true' ist das Suchfeld sichtbar", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: true, disabled: false } });
			expect(wrapper.find('input.ui-select--search').exists()).toBeTruthy();
		});

		test("ist das Dropdown sichtbar", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, disabled: false } });
			expect(wrapper.find('.ui-select--dropdown').exists()).toBeTruthy();
		});
	});

	describe.concurrent("Mit Prop 'disabled = true'", () => {
		const { manager } = createTestData();

		test("werden alle Buttons disabled dargestellt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, disabled: true } });
			const buttons = wrapper.findAll('button');
			for (const button of buttons) {
				expect(button.attributes('disabled')).toBeDefined();
			}
		});

		test("und 'searchable = true' ist das Suchfeld nicht sichtbar", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: true, disabled: true } });
			expect(wrapper.find('input.ui-select--search').exists()).toBeFalsy();
		});

		test("ist das Dropdown nicht sichtbar", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, disabled: true } });
			expect(wrapper.find('.ui-select--dropdown').exists()).toBeFalsy();
		});
	});

	test("Mit Prop 'statistics = false' wird in der Komponente kein Statistik-Icon angezeigt", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, statistics: false } });
		const statistics = wrapper.find('.ui-select--label--statistics');

		expect(statistics.exists()).toBeFalsy();
	});

	test("Mit Prop 'statistics = true' wird in der Komponente ein Statistik-Icon angezeigt", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, statistics: true } });
		const statistics = wrapper.find('.ui-select--label--statistics');

		expect(statistics.find('span.i-ri-bar-chart-2-line').exists()).toBeTruthy();
	});

	describe.concurrent("Mit Prop 'headless = false'", () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, label: "Mein Label", headless: false } });

		test("werden die Icons nicht links angezeigt", () => {
			const iconsLeft = wrapper.find('.ui-select--icons-left');
			expect(iconsLeft.exists()).toBeFalsy();
		});

		test("werden die Icons rechts angezeigt", () => {
			const iconsRight = wrapper.find('.ui-select--icons-right');
			expect(iconsRight.exists()).toBeTruthy();
		});

		test("wird die Komponente nicht kompakt dargestellt", () => {
			const combobox = wrapper.find('.ui-select--combobox');
			expect(combobox.classes()).toContain('border');
			expect(combobox.element.firstElementChild?.classList).toContain('py-1');
		});

		test("wird das Label ohne Selektion angezeigt", () => {
			const label = wrapper.find('.ui-select--label');
			expect(label.exists()).toBeTruthy();

			const labelText = label.find('.ui-select--label--text');
			expect(labelText.exists()).toBeTruthy();
		});

		test("wird das Label mit Selektion angezeigt", async () => {
			await wrapper.setProps({ modelValue: selection });
			await wrapper.vm.$nextTick();

			const label = wrapper.find('.ui-select--label');
			expect(label.exists()).toBeTruthy();

			const labelText = label.find('.ui-select--label--text');
			expect(labelText.exists()).toBeTruthy();
		});
	});

	describe.concurrent("Mit Prop 'headless = true'", () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, label: "Mein Label", headless: true } });

		test("werden die Icons links angezeigt", () => {
			const iconsLeft = wrapper.find('.ui-select--icons-left');
			expect(iconsLeft.exists()).toBeTruthy();

			expect(iconsLeft.find('span.i-ri-expand-up-down-line').exists()).toBeTruthy();
			expect(iconsLeft.find('span.i-ri-close-line').exists()).toBeTruthy();
		});

		test("werden die Icons rechts nicht angezeigt", () => {
			const iconsRight = wrapper.find('.ui-select--icons-right');
			expect(iconsRight.exists()).toBeFalsy();
		});

		test("wird die Komponente kompakt dargestellt", () => {
			const combobox = wrapper.find('.ui-select--combobox');
			expect(combobox.classes()).not.toContain('border');
			expect(combobox.element.firstElementChild?.classList).toContain('py-0');
		});

		test("und ohne Selektion wird das Label angezeigt", () => {
			const label = wrapper.find('.ui-select--label');
			expect(label.exists()).toBeTruthy();

			const labelText = label.find('.ui-select--label--text');
			expect(labelText.exists()).toBeTruthy();
		});

		test("und mit Selektion wird das Label nicht angezeigt", () => {
			const wrapper = mount(UiSelect<cars>, { props: { manager, label: "Mein Label",
				modelValue: selection, headless: true } });
			const label = wrapper.find('.ui-select--label');
			expect(label.exists()).toBeFalsy();
		});
	});
});

describe.concurrent("Teste Watcher und Computeds", () => {

	describe.concurrent("Watcher auf model.value:", () => {
		test("Exception, wenn model.value null bei 'nullable=false' enthält", () => {
			const { manager } = createTestData();

			expect(() => mount(UiSelect<cars>, {
				props: { manager, nullable: false, modelValue: null },
			})).toThrow('Ungültiges v-model: null oder undefined bei nullable = false');
		});

		test("Exception, wenn model.value undefined bei 'nullable=false' enthält", () => {
			const { manager } = createTestData();

			expect(() => mount(UiSelect<cars>, {
				props: { manager, nullable: false, modelValue: undefined },
			})).toThrow('Ungültiges v-model: null oder undefined bei nullable = false');
		});

		test("Keine Exception, wenn model.value null bei 'nullable=true' enthält", () => {
			const { manager } = createTestData();

			expect(() => mount(UiSelect<cars>, {
				props: { manager, nullable: true, modelValue: null },
			})).not.toThrow('Ungültiges v-model: null oder undefined bei nullable = false');
		});

		test("Keine Exception, wenn model.value undefined bei 'nullable=true' enthält", () => {
			const { manager } = createTestData();

			expect(() => mount(UiSelect<cars>, {
				props: { manager, nullable: true, modelValue: undefined },
			})).not.toThrow('Ungültiges v-model: null oder undefined bei nullable = false');
		});

		test("Keine Exception, wenn Selektion in manager.filteredOptions enthalten ist", async () => {
			const { manager, selection } = createTestData();
			expect(() => mount(UiSelect<cars>, {
				props: { manager, modelValue: selection },
			})).not.toThrow('Ungültiges v-model: {"marke":"VW","color":"schwarz","baujahr":2012} ist keine gültige Selektion');
		});

		test("Exception, wenn Selektion nicht in manager.filteredOptions enthalten ist", async () => {
			const { manager, nonExistingOption } = createTestData();

			expect(() => mount(UiSelect<cars>, {
				props: { manager, modelValue: nonExistingOption },
			})).toThrow('Ungültiges v-model: {"marke":"VW","color":"schwarz","baujahr":2012} ist keine gültige Selektion');
		});
	});

	describe.concurrent("Watcher auf props.manager.filteredOptions:", () => {
		test("Die Selektion wird nicht angepasst, wenn sie im neuen manager.filteredOptions enthalten ist", async () => {
			const { manager, selection } = createTestData();
			const wrapper = mount(UiSelect<cars>, { props: { manager, modelValue: selection } });

			const newFilteredOptions = new ArrayList<cars>();
			newFilteredOptions.add(selection);
			manager.unfilteredOptions = newFilteredOptions;

			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeUndefined();

		});

		test("Die Selektion wird angepasst, wenn sie nicht im neuen manager.filteredOptions enthalten ist", async () => {
			const { manager, selection, nonExistingOption } = createTestData();
			const wrapper = mount(UiSelect<cars>, { props: { manager, modelValue: selection } });

			const newFilteredOptions = new ArrayList<cars>();
			newFilteredOptions.add(nonExistingOption);
			manager.unfilteredOptions = newFilteredOptions;

			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toBeUndefined();
		});
	});

	test("computed->isValid ist true, wenn required = false, unabhängig von der Selektion", async () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, required: false, modelValue: undefined } });

		expect(wrapper.findComponent({ name: "UiSelect" }).vm.validation().isEmpty()).toBe(true);

		await wrapper.setProps({ modelValue: selection });
		expect(wrapper.findComponent({ name: "UiSelect" }).vm.validation().isEmpty()).toBe(true);
	});

	test("computed->isValid ist true, wenn required = true und Auswahl vorhanden", () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, required: true, modelValue: selection } });

		expect(wrapper.findComponent({ name: "UiSelect" }).vm.validation().isEmpty()).toBe(true);
	});

	test("computed->isValid ist false, wenn required = true und keine Auswahl vorhanden", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, required: true } });

		expect(wrapper.findComponent({ name: "UiSelect" }).vm.isValid).toBe(false);
	});

	test("computed->showSelection ist true, wenn eine Auswahl vorhanden ist und search leer", () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: true, modelValue: selection } });

		expect(wrapper.findComponent({ name: "UiSelect" }).vm.showSelection).toBe(true);
	});

	test("computed->showSelection ist false, wenn keine Auswahl vorhanden ist, search leer", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: true } });

		expect(wrapper.findComponent({ name: "UiSelect" }).vm.showSelection).toBe(false);
	});

	test("computed->showSelection ist false, wenn Auswahl vorhanden, aber search nicht leer", async () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: true, modelValue: selection } });

		const input = wrapper.find('input.ui-select--search');
		await input.setValue('BMW');
		expect(wrapper.findComponent({ name: "UiSelect" }).vm.showSelection).toBe(false);
	});

	test("computed->showSelection ist false, wenn keine Auswahl vorhanden und search nicht leer", async () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: true } });

		const input = wrapper.find('input.ui-select--search');
		await input.setValue('BMW');
		expect(wrapper.findComponent({ name: "UiSelect" }).vm.showSelection).toBe(false);
	});

	test("computed->selectionTextColor gibt 'text-ui-secondary' zurück, wenn searchable = true und Input fokussiert", async () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: true } });
		const vm = wrapper.findComponent({ name: "UiSelect" }).vm;

		const input = wrapper.find('input.ui-select--search');
		await input.trigger('focus');
		expect(vm.selectionTextColor).toBe('text-ui-secondary');
	});

	test("computed->selectionTextColor gibt 'text-ui' zurück, wenn searchable = true und Input nicht fokussiert", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: true } });
		const vm = wrapper.findComponent({ name: "UiSelect" }).vm;

		expect(vm.selectionTextColor).toBe('text-ui');
	});

	test("computed->selectionTextColor gibt 'text-ui' zurück, wenn searchable = false ist", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager, searchable: false } });
		const vm = wrapper.findComponent({ name: "UiSelect" }).vm;

		expect(vm.selectionTextColor).toBe('text-ui');
	});

	describe.concurrent("computed->state bildet alle relevanten Eigenschaften korrekt ab", () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, {
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
				modelValue: selection,
			},
		});

		const vm = wrapper.findComponent({ name: "UiSelect" }).vm;
		const state = vm.state;
		const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

		test("state.instanceId ist eine UUID", () => {
			expect(state.instanceId).toMatch(uuidRegex);
		});

		test("state.multi ist false", () => {
			expect(state.multi).toBe(false);
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

		test("state.validation entspricht props.validation", () => {
			expect(state.validation).toBe(undefined);
		});

		test("state.search entspricht ''", () => {
			expect(state.search).toBe("");
		});

		test("state.search wird bei Eingabe aktualisiert", async () => {
			const input = wrapper.find("input.ui-select--search");
			await input.setValue("BMW");
			expect(vm.state.search).toBe("BMW");
		});
	});
});

describe.concurrent("Teste Funktionen", () => {

	test("function->isSelected(option) ist false, wenn model.value = null ist", () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, modelValue: null } });

		const isSelected = wrapper.findComponent({ name: "UiSelect" }).vm.isSelected;
		expect(isSelected(selection)).toBe(false);
	});

	test("function->isSelected(option) ist false, wenn model.value nicht option entspricht", () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, modelValue: selection } });

		const isSelected = wrapper.findComponent({ name: "UiSelect" }).vm.isSelected;
		expect(isSelected(manager.filteredOptions.get(1))).toBe(false);
	});

	test("function->isSelected(option) ist true, wenn model.value option entspricht", () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, modelValue: selection } });

		const isSelected = wrapper.findComponent({ name: "UiSelect" }).vm.isSelected;
		expect(isSelected(selection)).toBe(true);
	});

	test("function->selectOption(option) setzt model.value wenn noch nichts selektiert ist und setzt die Suche zurück", async () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, searchable: true } });
		const vm = wrapper.findComponent({ name: "UiSelect" }).vm;

		vm.selectOption(selection);
		await wrapper.vm.$nextTick();

		const emits = wrapper.emitted("update:modelValue");
		expect(emits).toBeDefined();
		expect(emits![0][0]).toBe(selection);
	});

	test("function->selectOption(option) wenn die Option schon selektiert ist erzeugt eine Fehlermeldung", () => {
		const { manager, selection } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, modelValue: selection } });

		const selectOption = wrapper.findComponent({ name: "UiSelect" }).vm.selectOption;
		expect(() => selectOption(selection)).toThrow("Die Option BMW ist bereits selektiert.");
	});

	describe.concurrent("function->clearSelection()", () => {

		test("wirft Exception, wenn removable=false", () => {
			const { manager, selection } = createTestData();
			const wrapper = mount(UiSelect<cars>, { props: { manager, removable: false, modelValue: selection } });

			const clearSelection = wrapper.findComponent({ name: "UiSelect" }).vm.clearSelection;
			expect(() => clearSelection()).toThrow("Das Select ist auf removable=false gesetzt, daher kann der Eintrag nicht deselektiert werden");
		});

		test("setzt model.value auf undefined, wenn 'removable=true'", async () => {
			const { manager, selection } = createTestData();
			const wrapper = mount(UiSelect<cars>, { props: { manager, removable: true, modelValue: selection } });

			const clearSelection = wrapper.findComponent({ name: "UiSelect" }).vm.clearSelection;
			clearSelection();
			await wrapper.vm.$nextTick();

			const emits = wrapper.emitted("update:modelValue");
			expect(emits).toBeDefined();
			expect(emits![0][0]).toBeUndefined();
		});

		test("setzt search zurück, wenn 'removable=true'", async () => {
			const { manager, selection } = createTestData();
			const wrapper = mount(UiSelect<cars>, { props: { manager, removable: true, searchable: true, modelValue: selection } });

			const vm = wrapper.findComponent({ name: "UiSelect" }).vm;

			const input = wrapper.find('input.ui-select--search');
			await input.setValue("BMW");
			expect(vm.state.search).toBe("BMW");

			const clearSelection = vm.clearSelection;
			clearSelection();
			await wrapper.vm.$nextTick();

			expect(vm.state.search).toBe("");
		});

		test("schließt das Dropdown", async () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelect<cars>, { props: { manager } });

			const dropdown = wrapper.find('.ui-select--dropdown');
			const combobox = wrapper.find('.ui-select--combobox');
			const vm = wrapper.findComponent({ name: "UiSelect" }).vm;

			await combobox.trigger("click");

			expect(dropdown.attributes("data-popover-open")).toBe("true");

			const clearSelection = vm.clearSelection;
			clearSelection();
			expect(dropdown.attributes("data-popover-open")).toBeUndefined();
		});
	});

	test("function->deselectAllowed() ist true wenn 'removable=true'", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, removable: true } });

		const deselectAllowed = wrapper.findComponent({ name: "UiSelect" }).vm.deselectAllowed;
		expect(deselectAllowed()).toBe(true);
	});

	test("function->deselectAllowed() ist false wenn 'removable=false'", () => {
		const { manager } = createTestData();
		const wrapper = mount(UiSelect<cars>, { props: { manager: manager, removable: false } });

		const deselectAllowed = wrapper.findComponent({ name: "UiSelect" }).vm.deselectAllowed;
		expect(deselectAllowed()).toBe(false);
	});

	describe.concurrent("function->hasSelection()", () => {
		test("ist true, wenn model.value gesetzt ist", () => {
			const { manager, selection } = createTestData();
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, modelValue: selection } });

			const hasSelection = wrapper.findComponent({ name: "UiSelect" }).vm.hasSelection;
			expect(hasSelection()).toBe(true);
		});

		test("ist false, wenn model.value nicht definiert ist", () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager } });

			const hasSelection = wrapper.findComponent({ name: "UiSelect" }).vm.hasSelection;
			expect(hasSelection()).toBe(false);
		});

		test("ist false, wenn model.value=null ist", () => {
			const { manager } = createTestData();
			const wrapper = mount(UiSelect<cars>, { props: { manager: manager, modelValue: null } });

			const hasSelection = wrapper.findComponent({ name: "UiSelect" }).vm.hasSelection;
			expect(hasSelection()).toBe(false);
		});
	});
});

/**
 * Erstellt Testdaten für die Tests des UiSelects.
 *
 * @returns die testdaten
 */
function createTestData() {
	const options: cars[] = [{ marke: "BMW", color: "blue", baujahr: 2006 },
		{ marke: "Audi", color: "red", baujahr: 2008 }, { marke: "Opel", color: "schwarz", baujahr: 2006 }];
	const optionDisplayText = (option: cars) => option.marke;
	const selectionDisplayText = (option: cars) => option.marke;
	const manager = new SelectManager<{ marke: string, color: string, baujahr: number }>({ options: options, optionDisplayText: optionDisplayText, selectionDisplayText: selectionDisplayText });
	const selection = manager.filteredOptions.get(0);
	const nonExistingOption = { marke: "VW", color: "schwarz", baujahr: 2012 };
	return { manager, selection, nonExistingOption };
}

type cars = { marke: string, color: string, baujahr: number };

