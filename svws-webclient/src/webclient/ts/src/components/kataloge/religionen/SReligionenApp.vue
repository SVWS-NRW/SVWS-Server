<template>
	<div class="flex h-full flex-row">
		<div v-if="app.dataReligion.daten" class="flex w-full flex-col">
			<svws-ui-header :badge="inputId" badge-variant="light" badge-size="normal"><span>{{ inputBezeichnung }}</span>
				<svws-ui-badge variant="highlight" size="normal">{{
						inputKuerzel
				}}</svws-ui-badge>
			</svws-ui-header>
			<svws-ui-tab-bar>
				<template #tabs>
					<svws-ui-tab-button>Daten</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
						<s-religion-daten />
					</svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">
import { computed, ComputedRef, defineAsyncComponent } from "vue";

import { injectMainApp, Main } from "~/apps/Main";

const SReligionDaten = defineAsyncComponent(
	() => import("~/components/kataloge/religionen/daten/SReligionDaten.vue")
);
const main: Main = injectMainApp();

const app = main.apps.religionen;

const inputKuerzel: ComputedRef<string | false> = computed(() => {
	if (app.auswahl.ausgewaehlt?.kuerzel) {
		return app.auswahl.ausgewaehlt.kuerzel.toString();
	}
	return false;
});

const inputId: ComputedRef<string | false> = computed(() => {
	if (app.auswahl.ausgewaehlt) {
		return "ID: " + app.auswahl.ausgewaehlt.id;
	}
	return false;
});

const inputBezeichnung: ComputedRef<string | undefined> = computed(() => {
	if (app.auswahl.ausgewaehlt) {
		return app.dataReligion.daten?.text?.toString();
	}
	return "";
});
</script>
