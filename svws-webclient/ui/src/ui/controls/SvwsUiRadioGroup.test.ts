import { mount } from '@vue/test-utils';
import { test, expect } from 'vitest';
import SvwsUiRadioGroup from './SvwsUiRadioGroup.vue';

test('testet den prop_row_wert auf false (Standardwert)', () => {
	const wrapper = mount(SvwsUiRadioGroup);
	expect(wrapper.props().row).toBe(false);
	['flex', 'flex-col', 'items-start', 'gap-1'].forEach((value) => {
		expect(wrapper.classes()).toContain(value);
	});
});

test('testet den prop_row_wert auf true', async () => {
	// Vorbereiten
	const wrapper = mount(SvwsUiRadioGroup, {
		props: {
			row: true,
		},
	});

	//Testen
	['flex-row', 'flex-wrap', 'gap-0.5'].forEach((value) => {
		expect(wrapper.classes()).toContain(value);
	});
});

test('teste den Slot-Inhalt', () => {
	// Vorbreiten
	const wrapper = mount(SvwsUiRadioGroup, {
		slots: {
			default: '<div class="test-slot">Test Slot</div>',
		},
	});

	// Testen
	expect(wrapper.html()).toContain('<div class="test-slot">Test Slot</div>');
});
