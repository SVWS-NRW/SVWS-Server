import { createApp } from "vue";
import { SvwsUiPlugin } from "@svws-nrw/svws-ui";
import { router } from "./router/RouteManager";

import "./tailwind.css";
if (process.env.NODE_ENV === 'production')
	import("@svws-nrw/svws-ui/style.css");
import SWrapper from "~/components/SWrapper.vue";


const app = createApp(SWrapper);
app.use(SvwsUiPlugin);
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
