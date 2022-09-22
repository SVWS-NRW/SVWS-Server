<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col px-4">
			<svws-ui-header :badge="jahrgang">{{ bezeichnung_abiturjahr }}
			</svws-ui-header>
			<svws-ui-tab-bar v-model="app.selectedTab">
				<template #tabs>
					<svws-ui-tab-button :hidden="!jahrgang">Stammdaten</svws-ui-tab-button>
					<svws-ui-tab-button>Fächer</svws-ui-tab-button>
					<svws-ui-tab-button :hidden="!jahrgang">Fachwahlen</svws-ui-tab-button>
					<svws-ui-tab-button :hidden="!abiturjahr">Kursplanung</svws-ui-tab-button>
					<svws-ui-tab-button :hidden="!abiturjahr">Kurs/Schüler</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel :hidden="!jahrgang"> <s-gost-stammdaten /> </svws-ui-tab-panel>
					<svws-ui-tab-panel> <s-gost-faecher /> </svws-ui-tab-panel>
					<svws-ui-tab-panel :hidden="!jahrgang"> <s-gost-fachwahlen /> </svws-ui-tab-panel>
					<svws-ui-tab-panel :hidden="!abiturjahr"> <s-gost-kursplanung /> </svws-ui-tab-panel>
					<svws-ui-tab-panel :hidden="!abiturjahr"> <s-gost-kurs-schueler /> </svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">
import { computed, ComputedRef } from "vue";

import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.gost;

const jahrgang: ComputedRef<string | undefined> = computed(() => {
	return app.auswahl.ausgewaehlt?.jahrgang?.toString();
});

const abiturjahr: ComputedRef<number | undefined> = computed(() => {
	return app.dataJahrgang.daten?.abiturjahr?.valueOf();
});

const bezeichnung_abiturjahr: ComputedRef<string | undefined> = computed(
	() => {
		return app.auswahl.ausgewaehlt?.bezeichnung?.toString();
	}
);
</script>
