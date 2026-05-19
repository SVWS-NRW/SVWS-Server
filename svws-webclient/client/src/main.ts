import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "../../ui/src/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";
import { AbschnittStateKey } from "../../ui/src/states/AbschnittState";
import { abschnittState } from "./states/AbschnittStateImpl";
import { schuleState } from "./states/SchuleStateImpl";
import { SchuleStateKey } from "../../ui/src/states/SchuleState";
import { serverState } from "./states/ServerStateImpl";
import { ServerStateKey } from "../../ui/src/states/ServerState";

const app = createApp(SWrapper);
app.use(router);
app.provide(AbschnittStateKey, abschnittState);
app.provide(SchuleStateKey, schuleState);
app.provide(ServerStateKey, serverState);

app.directive('autofocus', {
	mounted: (el: HTMLInputElement, binding) => {
		if (<boolean>binding.instance?.$props.autofocus) {
			el.focus();
		}
	},
});

await router.isReady();
app.mount("#app");
