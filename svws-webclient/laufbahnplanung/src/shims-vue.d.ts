/// <reference types="vite/client" />
declare module "*.vue" {
	import type { defineComponent } from "vue";
	const Component: ReturnType<typeof defineComponent>;
	export default Component;
}
// declare module "*.vue" {
// 	import type { ComponentOptions } from "vue";
// 	const Component: ComponentOptions;
// 	export default Component;
// }

// declare module "*.md" {
// 	import type { ComponentOptions } from "vue";
// 	const Component: ComponentOptions;
// 	export default Component;
// }
