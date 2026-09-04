import { createApp, defineCustomElement } from "vue";
import SWrapper from "~/components/SWrapper.vue";
import HtmlPreview from "@ui/components/reporting/HtmlPreview.ce.vue";
import { registerStates } from "./states/registerStates";
import { RouteManager } from "./router/RouteManager";

import "@ui/assets/styles/index.css";
import "./main.css";
import { AppContext } from "@ui/AppContext";

const CustomElementConstructor = defineCustomElement(HtmlPreview);
customElements.define('html-preview', CustomElementConstructor);

const context = AppContext.init(createApp(SWrapper));
RouteManager.create(AppContext.instance.router);

registerStates();

if (process.env.NODE_ENV === 'development') {
	const { registerSVWSDevTools } = await import("@ui/devtools/stateInspector");
	const { registerSVWSModelProxyDevTools } = await import("@ui/devtools/modelProxyInspector");
	registerSVWSDevTools(context.app);
	registerSVWSModelProxyDevTools(context.app);
}

context.app.directive('autofocus', {
	mounted: (el: HTMLInputElement, binding) => {
		if (<boolean>binding.instance?.$props.autofocus) {
			el.focus();
		}
	},
});

await context.mount();

