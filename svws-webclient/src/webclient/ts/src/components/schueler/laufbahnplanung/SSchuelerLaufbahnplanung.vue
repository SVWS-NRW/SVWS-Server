<template>
	<svws-ui-badge class="cursor-pointer print:hidden" size="tiny" variant="primary" @click="modal.openModal()"> Hilfe </svws-ui-badge>
	<svws-ui-modal ref="modal">
		<template #modalTitle>Hilfe</template>
		<template #modalDescription>
			<hilfe-laufbahnplanung />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal">
				Schließen
			</svws-ui-button>
		</template>
	</svws-ui-modal>
	<s-card-schueler-laufbahnplanung v-if="visible" />
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ref } from "vue";
	import { App } from "~/apps/BaseApp";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const current_abiturjahr = App.apps.gost.auswahl.ausgewaehlt;
	const schueler_abiturjahr = app.dataGostLaufbahndaten?.abiturjahr;

	if (current_abiturjahr !== undefined && schueler_abiturjahr !== undefined && current_abiturjahr.abiturjahr !== schueler_abiturjahr)
			App.apps.gost.auswahl.ausgewaehlt = App.apps.gost.auswahl.liste.find(jahr => jahr.abiturjahr === schueler_abiturjahr);

	const visible: ComputedRef<boolean> = computed<boolean>(() =>
		//return this.$app.gostLaufbahn.visible; //TODO: richtige Bedingung einpflegen
		!!schueler_abiturjahr
		&& !!app.dataGostLaufbahndaten?.daten
		&& main.config.hasGost
	);

	const modal = ref();

</script>
