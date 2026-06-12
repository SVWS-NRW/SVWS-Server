import { createApp, defineCustomElement } from "vue";
import { router } from "./router/RouteManager";

import "../../ui/src/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";
import HtmlPreview from "../../ui/src/components/reporting/HtmlPreview.ce.vue";
import { AbschnittStateKey } from "../../ui/src/states/AbschnittState";
import { abschnittState } from "./states/AbschnittStateImpl";
import { schuleState } from "./states/SchuleStateImpl";
import { SchuleStateKey } from "../../ui/src/states/SchuleState";
import { serverState } from "./states/ServerStateImpl";
import { ServerStateKey } from "../../ui/src/states/ServerState";
import { reportingState } from "./states/ReportingStateImpl";
import { ReportingStateKey } from "../../ui/src/states/ReportingState";
import { wiedervorlageState } from "~/states/WiedervorlageStateImpl";
import { WiedervorlageStateKey } from "../../ui/src/states/WiedervorlageState";

const CustomElementConstructor = defineCustomElement(HtmlPreview);
customElements.define('html-preview', CustomElementConstructor);

const app = createApp(SWrapper);
app.use(router);
app.provide(AbschnittStateKey, abschnittState);
app.provide(SchuleStateKey, schuleState);
app.provide(ServerStateKey, serverState);
app.provide(ReportingStateKey, reportingState);
app.provide(WiedervorlageStateKey, wiedervorlageState);

if (process.env.NODE_ENV === 'development') {
	const { registerSVWSDevTools } = await import("../../ui/src/devtools/stateInspector");
	const { registerSVWSModelProxyDevTools } = await import("../../ui/src/devtools/modelProxyInspector");
	registerSVWSDevTools(app);
	registerSVWSModelProxyDevTools(app);
}

app.directive('autofocus', {
	mounted: (el: HTMLInputElement, binding) => {
		if (<boolean>binding.instance?.$props.autofocus) {
			el.focus();
		}
	},
});

await router.isReady();
app.mount("#app");
