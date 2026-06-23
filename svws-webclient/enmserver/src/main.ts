import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "@ui/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";
import { ActivityStateKey } from "./states/ActivityState";
import { activityState } from "./states/ActivityStateImpl";
import { AuthStateKey } from "./states/AuthState";
import { authState } from "./states/AuthStateImpl";
import { AuskunftStateKey } from "@ui/states/AuskunftState";
import { auskunftState } from "./states/AuskunftStateImpl";

await auskunftState.init();

const app = createApp(SWrapper);
app.use(router);
app.provide(ActivityStateKey, activityState);
app.provide(AuthStateKey, authState);
app.provide(AuskunftStateKey, auskunftState);

await router.isReady();
app.mount("#app");
