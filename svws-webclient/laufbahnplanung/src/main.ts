import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "../../ui/src/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";
import { ServerStateKey } from "@ui/states/ServerState";
import { serverState } from "./states/ServerStateImpl";

const app = createApp(SWrapper);
app.use(router);
app.provide(ServerStateKey, serverState);

await router.isReady();
app.mount("#app");
