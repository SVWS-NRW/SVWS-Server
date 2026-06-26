import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "../../ui/src/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";
import { AuskunftStateKey } from "@ui/states/AuskunftState";
import { auskunftState } from "./states/AuskunftStateImpl";

await auskunftState.init();

const app = createApp(SWrapper);
app.provide(AuskunftStateKey, auskunftState);

app.use(router);

await router.isReady();
app.mount("#app");
