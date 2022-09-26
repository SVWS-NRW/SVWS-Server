<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col px-4">
			<svws-ui-header
				:badge="inputId"
				badge-variant="light"
				badge-size="normal"
				><span>{{ inputKuerzel }}</span
				><svws-ui-badge
					v-for="(l, i) in inputKlassenlehrer"
					:key="i"
					variant="highlight"
					size="normal"
					span
				>
					{{ l.kuerzel }}
				</svws-ui-badge></svws-ui-header
			>
			<svws-ui-tab-bar v-model="app.selectedTab.value">
				<template #tabs>
					<svws-ui-tab-button>Daten</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
					</svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	// TODO Implementierung Klassendaten beim Server benötigt
	// const SKlassenDaten = defineAsyncComponent(() => import("~/components/klassen/daten/SKlassenDaten.vue"));

	// TODO Implementierung Klassendaten beim Server benötigt
	// @Options({ name: 's-klassen-app', components: { SKlassenDaten } })
	const main: Main = injectMainApp();
	const app = main.apps.klassen;
	const appLehrer = main.apps.lehrer;

	const inputKuerzel: ComputedRef<string | null> = computed(() => {
		if (app.auswahl.ausgewaehlt && app.auswahl.ausgewaehlt !== null) {
			return String(app.auswahl.ausgewaehlt.kuerzel);
		} else if (
			app.auswahl.ausgewaehlt &&
			app.auswahl.ausgewaehlt === null
		) {
			return null;
		}
		return "";
	});

	const inputId: ComputedRef<string> = computed(() => {
		if (app.auswahl.ausgewaehlt) {
			return "ID: " + app.auswahl.ausgewaehlt.id;
		}
		return "";
	});

	const inputKlassenlehrer: ComputedRef<Array<LehrerListeEintrag>> = computed(
		() => {
			const liste: Array<LehrerListeEintrag> = [];
			const ids = app.auswahl.ausgewaehlt?.klassenLehrer || [];
			for (const id of ids) {
				const lehrer = appLehrer.auswahl.liste.find(l => l.id === id);
				if (lehrer) liste.push(lehrer);
			}
			return liste;
		}
	);
</script>
