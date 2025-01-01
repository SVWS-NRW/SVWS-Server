import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "../../ui/src/assets/styles/index.css";
import "../../ui/src/assets/styles/colors.css";
import "./main.css";

import SWrapper from "~/components/SWrapper.vue";

const app = createApp(SWrapper);
app.use(router);

router.isReady()
	.then(() => app.mount("#app"))
	.catch((e: unknown) => { throw e });
