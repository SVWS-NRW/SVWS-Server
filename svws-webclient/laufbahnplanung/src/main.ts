import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "../../ui/src/assets/styles/index.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";

const app = createApp(SWrapper);
app.use(router);

await router.isReady();
app.mount("#app");
