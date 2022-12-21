<template>
	<div v-if="app.auswahl.ausgewaehlt && app.auswahl.ausgewaehlt !== null" class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header>
				<span class="inline-block mr-3">{{ inputKuerzel }}</span>
				<svws-ui-badge variant="light">{{ inputId }}</svws-ui-badge>
				<br/>
				<div class="separate-items--custom">
					<span
						v-for="(l, i) in inputKlassenlehrer"
						:key="i"
						class="opacity-50"
					>
						{{ l.kuerzel }}
					</span>
				</div>
			</svws-ui-header
			>
			<svws-ui-tab-bar v-model="app.selectedTab.value">
				<template #tabs>
					<svws-ui-tab-button>Daten</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
						<s-klassen-daten/>
					</svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-group-line/>
	</div>
</template>

<script setup lang="ts">
	import type { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, defineAsyncComponent } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	// TODO Implementierung Klassendaten beim Server benötigt
	const SKlassenDaten = defineAsyncComponent(() => import("~/components/klassen/daten/SKlassenDaten.vue"));

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
