import { createApp } from "vue";
import { router } from "./router/RouteManager";

import "../../ui/src/assets/styles/index.css";
import "./svws-client.css";

import SWrapper from "~/components/SWrapper.vue";

const app = createApp(SWrapper);
app.use(router);

app.mixin({
	created() {
		const title = this.$options.title;
		if (title) {
			document.title = title;
		}
	}
});
router.isReady()
	.then(() => app.mount("#app"))
	.catch(e => { throw e });
