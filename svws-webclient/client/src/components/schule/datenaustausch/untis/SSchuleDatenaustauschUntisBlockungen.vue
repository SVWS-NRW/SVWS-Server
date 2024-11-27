<template>
	<div class="page--content">
		<svws-ui-content-card title="Blockungen für Untis exportieren">
			<div class="flex flex-col gap-2 mb-4 lg:mb-8">
				Um eine ZIP-Datei zu erzeugen, werden die folgenden Angaben benötigt:
			</div>
			<svws-ui-input-wrapper v-if="jahrgaenge.length" :grid="2">
				<svws-ui-select :items="jahrgaenge" :model-value="abiturjahrgang()" :item-text="i=>i.abiturjahr.toString()" @update:model-value="jahrgang => jahrgang && gotoAbiturjahrgang(jahrgang.abiturjahr)" title="Abiturjahrgang" />
				<svws-ui-select v-if="abiturjahrgang()" :items="GostHalbjahr.values()" :model-value="halbjahr()" @update:model-value="hj => hj && gotoHalbjahr(hj.id)" :item-text="i=>i.beschreibung" title="Halbjahr" ref="selectHalbjahr" />
				<template v-if="blockungen.size()">
					<svws-ui-select :items="blockungen" :model-value="blockung()" @update:model-value="blockung => blockung && gotoBlockung(blockung.id)" :item-text="i=>i.name" title="Blockung" />
					<template v-if="ergebnisse.size()">
						<svws-ui-select :items="ergebnisse" :model-value="ergebnis()" @update:model-value="ergebnis => ergebnis && gotoErgebnis(ergebnis.id)" :item-text="i=>i.id.toString()" title="Ergebnis-ID" ref="selectErgebnis" />
						<svws-ui-input-number v-model="unterrichtID" :min="1" placeholder="Unterricht-ID" />
					</template>
					<div v-else>Diese Blockung hat keine Ergebnisse</div>
				</template>
				<div v-else>Für diesen Abiturjahrgang existieren keine Blockungen</div>
			</svws-ui-input-wrapper>
			<div v-else>Es liegen keine Abiturjahrgänge vor</div>
			<svws-ui-button v-if="ergebnisse.size()" type="primary" @click="untisExport" :disabled="!ergebnis" class="mt-2">
				<svws-ui-spinner :spinning /> Exportieren
			</svws-ui-button>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from 'vue';
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { SchuleDatenaustauschUntisBlockungenProps } from './SSchuleDatenaustauschUntisBlockungenProps';
	import type { GostBlockungsergebnis } from '@core';
	import { GostHalbjahr } from '@core';
	import { SvwsUiSelect } from '@ui';

	const props = defineProps<SchuleDatenaustauschUntisBlockungenProps>();
	const selectErgebnis = ref<ComponentExposed<typeof SvwsUiSelect<GostBlockungsergebnis>>>();
	const selectHalbjahr = ref<ComponentExposed<typeof SvwsUiSelect<GostHalbjahr>>>();

	const jahrgaenge = computed(() => [...props.mapAbiturjahrgaenge().values()].filter(j => j.abiturjahr > 1));
	const blockungen = computed(() => props.listBlockungen());

	const ergebnisse = computed(() => props.listErgebnisse());
	watch(() => props.abiturjahrgang(), () => selectHalbjahr.value?.reset());

	const unterrichtID = ref<number>(1);
	const spinning = ref<boolean>(false);


	async function untisExport() {
		const id = selectErgebnis.value?.content?.id;
		if (id === undefined)
			return;
		spinning.value = true;
		const formData = new FormData();
		formData.append("ergebnisID", id.toString());
		formData.append("unterrichtID", unterrichtID.value.toString());
		formData.append("sidvariante", "1"); // TODO
		const { data, name } = await props.exportUntisBlockungenZIP(formData);
		spinning.value = false;
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
