import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "../../ui/src/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";
import { AuskunftStateKey } from "../../ui/src/states/AuskunftState";
import { auskunftState } from "./states/AuskunftStateImpl";

const app = createApp(SWrapper);
app.use(router);
app.provide(AuskunftStateKey, auskunftState);

app.directive('autofocus', {
	mounted: (el: HTMLInputElement, binding) => {
		if (<boolean>binding.instance?.$props.autofocus) {
			el.focus();
		}
	},
});

await router.isReady();
app.mount("#app");
