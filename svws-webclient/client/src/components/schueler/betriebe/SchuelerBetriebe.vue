<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Betriebe" class="col-span-full">
			<schueler-betriebe-table :manager
				:delete-betriebe
				v-model:selected-betrieb="selectedBetrieb"
				@create="openModal()" />
			<schueler-betriebe-create-form :manager
				:create-modal-is-open
				:add
				@close-modal="closeModal()" />
			<schueler-betriebe-patch-form v-if="(selectedBetrieb !== null) && !createModalIsOpen"
				:manager
				:selected-betrieb
				:patch
				:go-to-betrieb />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerBetriebeProps } from "~/components/schueler/betriebe/SchuelerBetriebeProps";
	import { computed, ref, watch } from 'vue';
	import type { SchuelerBetrieb } from "@core/asd/data/schueler/SchuelerBetrieb";

	const props = defineProps<SchuelerBetriebeProps>();

	const betriebe = computed(() => [...props.manager().schuelerBetriebeById.values()]);
	const selectedBetrieb = ref<SchuelerBetrieb | null>(null);

	// --- create modal---
	const createModalIsOpen = ref(false);

	function openModal() {
		createModalIsOpen.value = true;
	}

	function closeModal() {
		createModalIsOpen.value = false;
		selectedBetrieb.value = null;
	}

	watch(betriebe, (neu) => {
		if (neu.length === 0) {
			selectedBetrieb.value = null;
		} else if (selectedBetrieb.value === null) {
			selectedBetrieb.value = neu[0];
		} else {
			const current = neu.find(e => e.id === selectedBetrieb.value?.id);
			selectedBetrieb.value = current ?? neu[0];
		}
	}, { immediate: true });

</script>
