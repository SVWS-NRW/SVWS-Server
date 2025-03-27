import type { VueWrapper } from '@vue/test-utils';
import { mount } from '@vue/test-utils';
import { test, expect, beforeEach, afterEach, describe } from 'vitest';
import SvwsUiContentCard from './SvwsUiContentCard.vue';

let wrapper: VueWrapper<InstanceType<typeof SvwsUiContentCard>>;
let idComponent: string;

const defaultProps: Partial<{
	title?: string;
	overflowScroll?: boolean;
	largeTitle?: boolean;
	hasBackground?: boolean;
}> = {
	title: '',
	overflowScroll: false,
	largeTitle: false,
	hasBackground: false,
};

beforeEach(async () => {
	wrapper = mount(SvwsUiContentCard);
	idComponent ="#" + wrapper.findComponent({ name: "SvwsUiContentCard" }).vm.idComponent;
});

afterEach(() => {
	wrapper.unmount();
})

test("Rendert HTML korrekt", async () => {
	expect(wrapper.find(idComponent).exists()).toBeTruthy();
	expect(wrapper.find(idComponent).classes()).toContain("content-card");
});

test("Teste die default-Werte der props", () => {
	for (const [prop, expectedValue] of Object.entries(defaultProps))
		expect(wrapper.props()[prop as keyof typeof defaultProps]).toBe(expectedValue);
});

describe("Tests für die CSS-Props", () => {
	// [Propname, Klassen, Beschreibung]
	test.each([
		["overflowScroll", "h-full", "Prop overflowScroll wird an CSS übergeben"],
		["hasBackground", "svws-has-background", "Prop hasBackground wird an CSS übergeben"],
	])("%s(%s) => %s", async (x, y) => {
		// Vorbereiten
		// Testen der default-Werte
		expect(wrapper.find(idComponent).classes()).not.toContain(y);

		// Änderen der props-Werte
		switch (x) {
			case "overflowScroll":
				await wrapper.setProps({ overflowScroll: true });
				break;
			case "hasBackground":
				await wrapper.setProps({ hasBackground: true });
				break;
			default:
				break;
		}
		// Testen der geänderten props-Werte
		expect(wrapper.find(idComponent).classes()).toContain(y);
	});

	test("Prop largeTitle wird an CSS übergeben", async () => {
		// Vorbereiten
		await wrapper.setProps({ largeTitle: true});
		await wrapper.setProps({ title: "dummyTitle" });

		// Testen
		expect(wrapper.find('.content-card--headline').classes()).toContain("content-card--headline--large");
	});

	test("Prop title wird an CSS übergeben", async () => {
		// Vorbereiten
		await wrapper.setProps({ title: "dummyTitle" });

		// Testen
		expect(wrapper.find('.content-card--headline').text()).toBe('dummyTitle');
	});
});

describe('Tests für Slotinhalte', () => {

	test('teste Slot title', () => {
		// Vorbereiten
		const wrapper = mount(SvwsUiContentCard, {
			slots: {
				title: '<div class="test-title">Custom Title</div>',
			},
		});

		// Testen
		expect(wrapper.find('.test-title').exists()).toBe(true);
	});

	test('teste Slot "actions"', () => {
		// Vorbereiten
		const wrapper = mount(SvwsUiContentCard, {
			slots: {
				actions: '<button class="test-action">Action</button>',
			},
		});

		// Testen
		expect(wrapper.find('.test-action').exists()).toBe(true);
	});

	test('teste Slot "default"', () => {
		// Vorbereiten
		const wrapper = mount(SvwsUiContentCard, {
			slots: {
				default: '<p class="test-content">Content</p>',
			},
		});

		// Testen
		expect(wrapper.find('.test-content').exists()).toBe(true);
	});
});