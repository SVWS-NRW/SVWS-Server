<template>
	<div>
		<svws-ui-header>Datenaustausch</svws-ui-header>
		<svws-ui-tab-bar v-model="app.selectedTab">
			<template #tabs>
				<svws-ui-tab-button>Laufbahnplanung</svws-ui-tab-button>
			</template>
			<template #panels>
				<svws-ui-tab-panel>
					<s-schule-datenaustausch-laufbahnplanung />
				</svws-ui-tab-panel>
			</template>
		</svws-ui-tab-bar>
	</div>
</template>

<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.schule;
</script>
