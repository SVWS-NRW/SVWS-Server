<template>
	<template v-if="!getErgebnisse().isEmpty()">
		<svws-ui-table clickable :clicked="auswahlErgebnis" @update:clicked="setAuswahlErgebnis" v-model="selected_ergebnisse" :selectable="getErgebnisse().size() > 1" class="z-20 relative"
			:columns="[{ key: 'id', label: 'ID', fixedWidth: getErgebnisse().size() > 1 ? 3 : 4.75, align: 'left'}, { key: 'bewertung', label: 'Ergebnis' }]"
			:items="getErgebnisse()" :count="getErgebnisse().size() > 1">
			<template #header(id)>
				<span class="font-mono">ID</span>
			</template>
			<template #header(bewertung)>
				<svws-ui-tooltip indicator="help">
					<span class="my-0.5">Ergebnis</span>
					<template #content>
						<div class="normal-case text-base rich-text">
							<div class="my-1">Bewertung durch:</div>
							<ul class="mb-1 list-disc pl-4">
								<li>Regelverletzungen</li>
								<li>Wahlkonflikte</li>
								<li>max. Kursdifferenz</li>
								<li>Fächerparallelität</li>
							</ul>
						</div>
					</template>
				</svws-ui-tooltip>
			</template>
			<template #cell(bewertung)="{ rowData: row }">
				<div class="inline-flex flex-wrap w-full gap-x-1 gap-y-2.5">
					<span class="flex gap-1 items-center ml-0.5" :class="{'filter saturate-200': auswahlErgebnis === row}">
						<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${getDatenmanager().ergebnisGetBewertung1Wert(row.id)} Regelverletzungen`" :style="{'background-color': color1(row)}">{{ getDatenmanager().ergebnisGetBewertung1Wert(row.id) }}</span>
						<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${getDatenmanager().ergebnisGetBewertung2Wert(row.id)} Wahlkonflikte`" :style="{'background-color': color2(row)}">{{ getDatenmanager().ergebnisGetBewertung2Wert(row.id) }}</span>
						<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`Maximale Kursdifferenz: ${getDatenmanager().ergebnisGetBewertung3Wert(row.id)}`" :style="{'background-color': color3(row)}">{{ getDatenmanager().ergebnisGetBewertung3Wert(row.id) }}</span>
						<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${getDatenmanager().ergebnisGetBewertung4Wert(row.id)} Fächer parallel`" :style="{'background-color': color4(row)}">{{ getDatenmanager().ergebnisGetBewertung4Wert(row.id) }}</span>
					</span>
					<div v-if="auswahlErgebnis === row || row.istVorlage" class="ml-auto inline-flex">
						<template v-if="auswahlErgebnis === row">
							<svws-ui-button type="icon" @click.stop="remove_ergebnis" title="Ergebnis löschen" :disabled="apiStatus.pending || getErgebnisse().size() <= 1" class="text-black dark:text-white">
								<i-ri-delete-bin-line class="-mx-0.5" />
							</svws-ui-button>
						</template>
						<svws-ui-tooltip v-if="row.istVorlage">
							<i-ri-checkbox-circle-fill class="text-svws text-headline-md relative -my-0.5 ml-2" />
							<template #content>
								Aktiviertes Ergebnis
							</template>
						</svws-ui-tooltip>
					</div>
				</div>
			</template>
			<template #actions v-if="selected_ergebnisse.length">
				<svws-ui-button @click="remove_ergebnisse" type="transparent" :disabled="getErgebnisse().size() === 0 || selected_ergebnisse.length >= getErgebnisse().size() || selected_ergebnisse.length === 0" class="text-error">
					<i-ri-delete-bin-line />
					<span>{{ selected_ergebnisse.length }} {{ selected_ergebnisse.length > 1 ? 'Ergebnisse' : 'Ergebnis' }} löschen</span>
				</svws-ui-button>
			</template>
		</svws-ui-table>
	</template>
</template>

<script setup lang="ts">

	import type { GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, GostHalbjahr, List } from "@core";
	import type { ApiStatus } from '~/components/ApiStatus';
	import { ref } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		removeErgebnisse: (ergebnisse: GostBlockungsergebnisListeneintrag[]) => Promise<void>;
		setAuswahlErgebnis: (value: GostBlockungsergebnisListeneintrag | undefined) => Promise<void>;
		auswahlErgebnis: GostBlockungsergebnisListeneintrag | undefined;
		halbjahr: GostHalbjahr;
		apiStatus: ApiStatus;
	}>();

	const selected_ergebnisse = ref<GostBlockungsergebnisListeneintrag[]>([]);

	function getErgebnisse() : List<GostBlockungsergebnisListeneintrag> {
		return props.getDatenmanager().ergebnisGetListeSortiertNachBewertung();
	}

	async function remove_ergebnisse() {
		if (props.halbjahr === undefined)
			return;
		await props.removeErgebnisse(selected_ergebnisse.value);
		selected_ergebnisse.value = [];
	}

	async function remove_ergebnis() {
		if (!props.auswahlErgebnis)
			return;
		await props.removeErgebnisse([props.auswahlErgebnis]);
	}

	function color1(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (props.getDatenmanager().ergebnisGetBewertung1Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function color2(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (props.getDatenmanager().ergebnisGetBewertung2Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function color3(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (props.getDatenmanager().ergebnisGetBewertung3Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function color4(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (props.getDatenmanager().ergebnisGetBewertung4Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function colorMix(ergebnis: GostBlockungsergebnisListeneintrag): string {
		const combined = (props.getDatenmanager().ergebnisGetBewertung1Intervall(ergebnis.id))
			+ (props.getDatenmanager().ergebnisGetBewertung2Intervall(ergebnis.id))
			+ (props.getDatenmanager().ergebnisGetBewertung3Intervall(ergebnis.id))
			+ (props.getDatenmanager().ergebnisGetBewertung4Intervall(ergebnis.id));
		return `hsl(${Math.round((1 - (combined > 1 ? 1 : combined)) * 120)},100%,70%)`
	}

</script>
