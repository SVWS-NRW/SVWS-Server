<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-schueler-lernabschnitt-foerderempfehlungen /> </svws-ui-modal-hilfe>
	</Teleport>
	<svws-ui-content-card class="w-full pl-1">
		<schueler-lernabschnitt-foerderempfehlungen-tabelle @open-modal="modalIsShown = true"
			:foerderempfehlungen="sortedFoerderempfehlungen"
			v-model:selected-foerderempfehlung="selectedFoerderempfehlung"
			:delete="props.delete" />
		<schueler-lernabschnitt-foerderempfehlungen-neu-modal v-model:is-open="modalIsShown"
			:add="props.add" />
		<schueler-lernabschnitt-foerderempfehlungen-daten v-if="selectedFoerderempfehlung !== undefined"
			:selected-foerderempfehlung="selectedFoerderempfehlung"
			:patch="patch" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, onMounted, onUpdated, ref } from 'vue';
	import { ArrayList, type SchuelerFoerderempfehlung, type Comparator } from '@core';
	import type { SchuelerLernabschnittFoerderempfehlungenProps } from './SchuelerLernabschnittFoerderempfehlungenProps';

	const props = defineProps<SchuelerLernabschnittFoerderempfehlungenProps>();
	const selectedFoerderempfehlung = ref<SchuelerFoerderempfehlung | undefined>(undefined);
	const modalIsShown = ref<boolean>(false);

	onUpdated(() => {
		selectedFoerderempfehlung.value = sortedFoerderempfehlungen.value.size() > 0 ? sortedFoerderempfehlungen.value.getFirst() : undefined;
	});

	onMounted(() => {
		selectedFoerderempfehlung.value = sortedFoerderempfehlungen.value.size() > 0 ? sortedFoerderempfehlungen.value.getFirst() : undefined;
	});

	const sortedFoerderempfehlungen = computed(() => {
		const list = new ArrayList<SchuelerFoerderempfehlung>();
		list.addAll(props.foerderempfehlungen());
		list.sort(vergleicheNachDatumAngelegtAbsteigend);
		return list;
	});

	const vergleicheNachDatumAngelegtAbsteigend: Comparator<SchuelerFoerderempfehlung> = {
		compare: (a, b) => {
			const dateA = a.datumAngelegt === null ? 0 : new Date(a.datumAngelegt).getTime();
			const dateB = b.datumAngelegt === null ? 0 : new Date(b.datumAngelegt).getTime();
			return dateB - dateA;
		},
	};

</script>
