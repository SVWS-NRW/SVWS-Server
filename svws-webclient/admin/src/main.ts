import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "@ui/assets/styles/index.css";

import SWrapper from "~/components/SWrapper.vue";
import { AuskunftStateKey } from "@ui/states/AuskunftState";
import { auskunftStateImpl } from "./states/AuskunftStateImpl";

const app = createApp(SWrapper);
app.provide(AuskunftStateKey, auskunftStateImpl);

app.use(router);

await router.isReady();
app.mount("#app");
