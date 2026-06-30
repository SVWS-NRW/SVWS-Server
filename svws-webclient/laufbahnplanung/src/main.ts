import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "../../ui/src/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";
import { ServerStateKey } from "@ui/states/ServerState";
import { serverStateImpl } from "./states/ServerStateImpl";
import { AuskunftStateKey } from "@ui/states/AuskunftState";
import { auskunftStateImpl } from "./states/AuskunftStateImpl";
import { GostLaufbahnplanungStateKey } from "@ui/states/GostLaufbahnplanungState";
import { gostLaufbahnplanungStateImpl } from "./states/GostLaufbahnplanungStateImpl";
import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";

await auskunftStateImpl.init();

const app = createApp(SWrapper);
app.use(router);
app.provide(ServerStateKey, serverStateImpl);
app.provide(AuskunftStateKey, auskunftStateImpl);
app.provide(GostLaufbahnplanungStateKey, gostLaufbahnplanungStateImpl);

// Lese die Daten für die Core-Types ein
new JsonCoreTypeReaderStatic().readAll();

await router.isReady();
app.mount("#app");
