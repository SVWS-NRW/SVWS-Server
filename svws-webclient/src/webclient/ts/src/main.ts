import { createApp } from "vue";
import { SvwsUiPlugin } from "@svws-nrw/svws-ui";
import { mainApp, mainInjectKey } from "~/apps/Main";
import { router } from "./router";

import "./tailwind.css";
import "@svws-nrw/svws-ui/dist/style.css";
import SWrapper from "~/components/SWrapper.vue";

const app = createApp(SWrapper);
app.use(SvwsUiPlugin);
app.use(router);
// to access app with composition API/ script-setup (use injectMainApp() in components)
app.provide(mainInjectKey, mainApp);

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
