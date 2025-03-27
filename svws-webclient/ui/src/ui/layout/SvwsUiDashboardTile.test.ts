import type { VueWrapper } from "@vue/test-utils";
import { mount } from '@vue/test-utils';
import SvwsUiDashboardTile from "./SvwsUiDashboardTile.vue";
import { afterEach, beforeEach, describe, expect, test } from "vitest";


let wrapper: VueWrapper<InstanceType<typeof SvwsUiDashboardTile>>;
let idComponent: string;

const defaultProps: Partial<{
	color?: 'light' | 'dark' | 'transparent';
	span?: 1 | 2 | 'full';
	title?: string;
	number?: string;
	numberLabel?: string;
	clickable?: boolean;
}> = {
	color: 'light',
	span: 1,
	clickable: false,
	title: undefined,
	number: undefined,
	numberLabel: undefined,
};

beforeEach(async () => {
	wrapper = mount(SvwsUiDashboardTile);
	idComponent ="#" + wrapper.findComponent({ name: "SvwsUiDashboardTile" }).vm.idComponent;
});

afterEach(() => {
	wrapper.unmount();
});

test("Rendert HTML korrekt", () => {
	expect(wrapper.find(idComponent).exists()).toBe(true);
	expect(wrapper.find(idComponent).classes()).toContain("svws-ui-dashboard-tile");
});

test("Teste die default-Werte der props", () => {
	for (const [prop, expectedValue] of Object.entries(defaultProps)){
		expect(wrapper.props()[prop as keyof typeof defaultProps]).toBe(expectedValue);
	}
});

describe("Tests für die CSS-Props", () => {

	// PROP-Color [Propvalue, CSS-Klasse, Beschreibung]
	test.each([
		['ligt', 'light', 'Prop color wird mit dem Wert null an CSS übergeben'],
		['dark','svws-ui-dashboard-tile--dark', 'Prop color wird mit dem Wert dark an CSS übergeben'],
		['transparent','svws-ui-dashboard-tile--transparent', 'Prop color wird mit dem Wert transparent an CSS übergeben'],
	])("%s(%s) => %s", async (x, y) => {
		// Vorbereiten
		// Testen des default-Werts
		expect(wrapper.find(idComponent).classes()).not.toContain("y");

		// Aktion
		await wrapper.setProps({ color: x as 'light' | 'dark' | 'transparent' });

		// Testen
		if( y !== 'light' ) {
			expect(wrapper.find(idComponent).classes()).toContain(y);
		}
	});

	// PROP-Span [Propvalue, CSS-Klasse, Beschreibung]
	test.each([
		['1', '1', 'Prop span wird mit dem Wert null an CSS übergeben'],
		['2','col-span-2', 'Prop span wird mit dem Wert 2 an CSS übergeben'],
		['full','col-span-full', 'Prop color wird mit dem Wert full an CSS übergeben'],
	])("%s(%s) => %s", async (x, y) => {
		// Vorbereiten
		// Testen des default-Werts
		expect(wrapper.find(idComponent).classes()).not.toContain("y");

		// Aktion
		const c = x!=='full' ? parseInt(x) : x;
		await wrapper.setProps({ span: c as 1 | 2 | 'full' });

		// Testen
		if( y !== '1' ) {
			expect(wrapper.find(idComponent).classes()).toContain(y);
		}
	});

	test("Prop clickable wird mit dem Wert true an CSS übergeben", async () => {
		// Vorbereiten
		// Testen des default-Werts
		expect(wrapper.find(idComponent).classes()).not.toContain("svws-ui-dashboard-tile--clickable");

		// Aktion
		await wrapper.setProps({ clickable: true });

		// Testen
		expect(wrapper.find(idComponent).classes()).toContain("svws-ui-dashboard-tile--clickable");
	});
});

describe("Bedingte Rendern", () => {
	test("Rendert den Titel im vorgegebenem Slot-Inhalt nicht, wenn title nicht gesetzt ist", async () => {
		// Testen
		expect(wrapper.find(idComponent).find(".svws-ui-dashboard-tile__title").exists()).toBe(false);
	})

	test("Rendert den Titel im vorhandenen Slot-Inhalt, wenn title gesetzt ist", async () => {
		// Vorbereiten
		const title = "Titel";

		// Aktion
		await wrapper.setProps({ title: title });

		// Testen
		expect(wrapper.find(idComponent).find(".dashboard-tile--title").exists()).toBe(true);
		expect(wrapper.find(idComponent).find(".dashboard-tile--title").text()).toBe(title);
	})

	test("Rendert den Titel in dem mitgegebenen title-Slot", async () => {
		// Vorbereiten
		const wrapper = mount(SvwsUiDashboardTile, {
			props: {
				title: 'Title',
			},
			slots: {
				title: '<div class="test-title">Custom Title</div>',
			},
		});

		// Testen
		console.log(wrapper.html());
		expect(wrapper.find(idComponent).find('.test-title').text()).toBe('Custom Title');
	});

	test("Rendert die Nummer im vorgegebenem Slot-Inhalt nicht, wenn number nicht gesetzt ist", async () => {
		// Testen
		expect(wrapper.find(idComponent).find(".svws-ui-dashboard-tile__number").exists()).toBe(false);
		console.log(wrapper.html());
	});

	test("Rendert die Nummer im vorhandenen Slot-Inhalt, wenn number gesetzt ist", async () => {
		// Vorbereiten
		const number = "123";

		// Aktion
		await wrapper.setProps({ number: number });

		// Testen
		expect(wrapper.find(idComponent).find(".dashboard-tile--number").exists()).toBe(true);
		expect(wrapper.find(idComponent).find(".dashboard-tile--number").text()).toBe(number);
	});

	test("Rendert die Nummer in dem mitgegebenen number-Slot", async () => {
		// Vorbereiten
		const wrapper = mount(SvwsUiDashboardTile, {
			props: {
				number: '123',
			},
			slots: {
				number: '<div class="test-number">456</div>',
			},
		});

		// Testen
		console.log(wrapper.html());
		expect(wrapper.find(idComponent).find('.test-number').text()).toBe('456');
	});

	test("Rendert nummerLabel nicht, wenn numberLabel nicht gesetzt ist", async () => {
		// Vorbereiten
		await wrapper.setProps({ number: '123' });

		// Testen
		expect(wrapper.find(idComponent).find(".dashboard-tile--number-label").exists()).toBe(false);
	});

	test("Rendert nummerLabel, wenn numberLabel gesetzt ist", async () => {
		// Vorbereiten
		const numberLabel = "Label";

		// Aktion
		await wrapper.setProps({ numberLabel: numberLabel });

		// Testen
		expect(wrapper.find(idComponent).find(".dashboard-tile--number-label").exists()).toBe(true);
		expect(wrapper.find(idComponent).find(".dashboard-tile--number-label").text()).toBe(numberLabel);
	});


});