import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "../../ui/src/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";
import { ServerStateKey } from "@ui/states/ServerState";
import { serverStateImpl } from "./states/ServerStateImpl";
import { AuskunftStateKey } from "@ui/states/AuskunftState";
import { auskunftStateImpl } from "./states/AuskunftStateImpl";

await auskunftStateImpl.init();

const app = createApp(SWrapper);
app.provide(AuskunftStateKey, auskunftStateImpl);

app.use(router);
app.provide(ServerStateKey, serverStateImpl);

await router.isReady();
app.mount("#app");
