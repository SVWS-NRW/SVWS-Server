import { describe, expect, beforeEach, afterEach, test } from 'vitest';
import type { VueWrapper } from '@vue/test-utils';
import { flushPromises, mount } from '@vue/test-utils';
import SvwsUiModal from './SvwsUiModal.vue';
import { Dialog, DialogDescription, DialogTitle, TransitionChild, TransitionRoot } from '@headlessui/vue';
import type { Size } from "../../types";



let wrapper: VueWrapper<any>;
let idComponent: string;
let idTC_overlay: string;
let idTC_div: string;

const defaultProps : Partial<{
	show: boolean;
	size?: Extract<Size, 'small' | 'medium' | 'big'> | 'help';
	type?: 'default' | 'danger';
	autoClose?: boolean;
	closeInTitle?: boolean;
	noScroll?: boolean;
}> = {
	size: 'small',
	type: 'default',
	autoClose: true,
	closeInTitle: true,
	noScroll: false,
};

beforeEach(async () => {
	wrapper = mount(SvwsUiModal,{
		props: {
			show: true,
		},
		attachTo: document.body,
	});
	idComponent =wrapper.vm.idComponent;
	idTC_overlay =wrapper.vm.idTC_overlay;
	idTC_div =wrapper.vm.idTC_div;
});

afterEach(() => {
	wrapper.unmount();
	document.body.innerHTML = '';
});

test("Testen der Teleportierung", async () => {
	/* Die Kommentare <!--teleport start--><!--teleport end--> zeigen an, dass der Dialog-Inhalt an
	eine andere Stelle teleportiert wurde, was zu erwarten ist.
	*/
	await wrapper.vm.$nextTick()
	expect(wrapper.html()).toContain('<!--teleport start-->');
	expect(wrapper.html()).toContain('<!--teleport end-->');
});

test("Teste die default-Werte der props", () => {
	for (const [prop, expectedValue] of Object.entries(defaultProps)){
		expect(wrapper.props()[prop as keyof typeof defaultProps]).toBe(expectedValue);
	}
});

describe("Tests für die CSS-Props", () => {
	// Test für die Prop 'size'
	test.each([
		['small', 'modal--sm'],
		['medium', 'modal--md'],
		['big', 'modal--lg'],
		['help', 'modal--help'],
	])('wendet die Klasse %s an, wenn size="%s"', async (s, expectedClass) => {
		document.body.innerHTML = '';
		const wrapper: VueWrapper<any> = mount(SvwsUiModal, {
			props: { show: true, size: s as "small" | "medium" | "big" | "help" | undefined },
			slots: { modalTitle: 'Test-Titel', modalActions: '<button>test</button>' },
			global: {
				components: { Dialog, DialogTitle, DialogDescription, TransitionRoot, TransitionChild },
				stubs: { 'svws-ui-tooltip': true, 'svws-ui-button': true },
			},
			attachTo: document.body,
		});

		await flushPromises();
		await wrapper.vm.$nextTick();
		idTC_overlay = wrapper.vm.idTC_overlay;
		const modal = document.getElementById(idTC_div);
		expect(modal?.classList.contains(expectedClass)).toBe(true);
	});


	// Test für die Prop 'type'
	test("wendet die Klasse modal--danger an, wenn type='danger'", async () => {
		document.body.innerHTML = '';
		const wrapper: VueWrapper<any> = mount(SvwsUiModal, {
			props: { show: true, type: "danger" },
			slots: { modalTitle: 'Test-Titel', modalActions: '<button>test</button>' },
			attachTo: document.body,
		});

		await flushPromises();
		await wrapper.vm.$nextTick();
		idTC_overlay = wrapper.vm.idTC_overlay;
		const modal = document.getElementById(idTC_div);
		expect(modal?.classList.contains('modal--danger')).toBe(true);
	});

	test("wendet die Klasse modal--danger nicht an, wenn type ungleich'danger'", async () => {
		document.body.innerHTML = '';
		const wrapper: VueWrapper<any> = mount(SvwsUiModal, {
			props: { show: true, type: "default" },
			slots: { modalTitle: 'Test-Titel', modalActions: '<button>test</button>' },
			attachTo: document.body,
		});

		await flushPromises();
		await wrapper.vm.$nextTick();
		idTC_overlay = wrapper.vm.idTC_overlay;
		const modal = document.getElementById(idTC_div);
		expect(modal?.classList.contains('modal--danger')).toBeFalsy()
	});

	test("wendet die Klasse modal--content-noscroll an, wenn noScroll true ist.", async () => {
		document.body.innerHTML = '';
		const wrapper: VueWrapper<any> = mount(SvwsUiModal, {
			props: { show: true, noScroll: true },
			slots: { modalTitle: 'Test-Titel', modalContent: 'Test Content', modalActions: '<button>test</button>' },
			attachTo: document.body,
		});

		await flushPromises();
		await wrapper.vm.$nextTick();
		const contentWrapper1 = document.body.querySelector('.modal--content-wrapper');
		expect(contentWrapper1?.classList.contains('modal--content-noscroll')).toBeTruthy();
		const contentWrapper2 = document.body.querySelector('.modal--content');
		expect(contentWrapper2?.classList.contains('modal--content-noscroll')).toBeTruthy();
	});

	test("wendet die Klasse modal--content-noscroll nicht an, wenn noScroll false ist.", async () => {
		document.body.innerHTML = '';
		const wrapper: VueWrapper<any> = mount(SvwsUiModal, {
			props: { show: true},
			slots: { modalTitle: 'Test-Titel', modalContent: 'Test Content', modalActions: '<button>test</button>' },
			attachTo: document.body,
		});

		await flushPromises();
		await wrapper.vm.$nextTick();
		const contentWrapper1 = document.body.querySelector('.modal--content-wrapper');
		expect(contentWrapper1?.classList.contains('modal--content-noscroll')).toBeFalsy();
		const contentWrapper2 = document.body.querySelector('.modal--content');
		expect(contentWrapper2?.classList.contains('modal--content-noscroll')).toBeFalsy();
	});


});

describe("Bedingstes Rendern", () => {
	test("rendert das Modal nur, wenn show true ist", async () => {
		document.body.innerHTML = '';
		const wrapperTrue: VueWrapper<any> = mount(SvwsUiModal, {
			props: { show: true },
			slots: { modalTitle: 'Test-Titel', modalActions: '<button>test</button>' },
			attachTo: document.body,
		});
		await flushPromises();
		idComponent = wrapperTrue.vm.idComponent;
		idTC_overlay = wrapperTrue.vm.idTC_overlay;
		idTC_div = wrapperTrue.vm.idTC_div;
		const dialog = document.getElementById(idComponent);
		expect(dialog).not.toBeNull();
		expect(dialog?.classList.contains('modal--wrapper')).toBeTruthy();
		wrapperTrue.unmount();
	});

	test("rendert das Modal nicht, wenn show false ist", async () => {
		document.body.innerHTML = '';
		const wrapperFalse: VueWrapper<any> = mount(SvwsUiModal, {
			props: { show: false },
			slots: { modalTitle: 'Test-Titel', modalActions: '<button>test</button>' },
			attachTo: document.body,
		});
		await flushPromises();
		idComponent = wrapperFalse.vm.idComponent;
		idTC_overlay = wrapperFalse.vm.idTC_overlay;
		idTC_div = wrapperFalse.vm.idTC_div;
		const dialog = document.getElementById(idComponent);
		expect(dialog).toBeNull();
		wrapperFalse.unmount();
	});

});
