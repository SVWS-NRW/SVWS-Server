import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "@ui/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";
import { AuthStateKey } from "./states/AuthState";
import { authState } from "./states/AuthStateImpl";

const app = createApp(SWrapper);
app.use(router);
app.provide(AuthStateKey, authState);

await router.isReady();
app.mount("#app");
