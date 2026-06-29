import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "@ui/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";
import { ActivityStateKey } from "./states/ActivityState";
import { activityStateImpl } from "./states/ActivityStateImpl";
import { AuthStateKey } from "./states/AuthState";
import { authStateImpl } from "./states/AuthStateImpl";
import { AuskunftStateKey } from "@ui/states/AuskunftState";
import { auskunftStateImpl } from "./states/AuskunftStateImpl";

await auskunftStateImpl.init();

const app = createApp(SWrapper);
app.use(router);
app.provide(ActivityStateKey, activityStateImpl);
app.provide(AuthStateKey, authStateImpl);
app.provide(AuskunftStateKey, auskunftStateImpl);

await router.isReady();
app.mount("#app");
