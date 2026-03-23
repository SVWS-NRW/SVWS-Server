<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Betriebe" class="col-span-full">
			<schueler-betriebe-table :manager
				:delete-entries
				@create="openModal()"
				:schulform
				@update:selected-betrieb="(v) => selectedBetrieb = v ?? null" />
			<schueler-betriebe-create-form :manager
				:create-modal-is-open
				:add
				:schulform
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
	import { computed, onMounted, onUpdated, ref } from 'vue';
	import type { SchuelerBetrieb } from "@core";

	const props = defineProps<SchuelerBetriebeProps>();
	const entries = computed(() => [...props.manager().schuelerBetriebeById.values()]);
	const selectedBetrieb = ref<SchuelerBetrieb | null>(null);

	function updateSelectedBetrieb() {
		selectedBetrieb.value = entries.value.length > 0 ? entries.value.at(0) ?? null : null;
	}

	onMounted(updateSelectedBetrieb);
	onUpdated(updateSelectedBetrieb);

	// --- create modal---
	const createModalIsOpen = ref(false);

	function openModal() {
		createModalIsOpen.value = true;
	}

	function closeModal() {
		createModalIsOpen.value = false;
		selectedBetrieb.value = null;
	}


</script>
