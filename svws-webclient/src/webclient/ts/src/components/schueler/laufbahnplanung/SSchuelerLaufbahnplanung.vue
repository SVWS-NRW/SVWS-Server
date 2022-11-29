<template>
	<svws-ui-badge class="cursor-pointer print:hidden" size="tiny" variant="primary" @click="modal.openModal()">Hilfe
	</svws-ui-badge>
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
	import { computed, ComputedRef, defineAsyncComponent, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const HilfeLaufbahnplanung = defineAsyncComponent(() => import( "~/components/schueler/laufbahnplanung/HilfeLaufbahnplanung.md" ));

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const visible: ComputedRef<boolean> = computed<boolean>(() =>
		//return this.$app.gostLaufbahn.visible; //TODO: richtige Bedingung einpflegen
		!!app.dataGostLaufbahndaten?.abiturjahr && !!app.dataGostLaufbahndaten?.daten && main.config.hasGost
	);

	const modal = ref();
</script>
