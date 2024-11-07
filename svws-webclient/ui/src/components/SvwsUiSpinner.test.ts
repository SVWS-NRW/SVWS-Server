import { beforeEach, expect, test, afterEach } from "vitest";
import SvwsUiSpacing from "./SvwsUiSpacing.vue";
import type { VueWrapper } from "@vue/test-utils";
import { mount } from "@vue/test-utils";
import SvwsUiSpinner from "./SvwsUiSpinner.vue";

let wrapper: VueWrapper<InstanceType<typeof SvwsUiSpinner>>;

beforeEach( () => {
	wrapper = mount(SvwsUiSpinner, { props: { spinning: false}});
})

test("Rendert svg nicht, wenn spinning false ist", async () => {
	expect(wrapper.find("svg").exists()).toBeFalsy();
})

test("Rendert HTML korrekt, wenn spinning true ist", async () => {
	await wrapper.setProps({ spinning: true})
	expect(wrapper.find("svg.animate-spin.ml-1.h-4.w-4").exists()).toBeTruthy();

})

afterEach(() => {
	wrapper.unmount();
})