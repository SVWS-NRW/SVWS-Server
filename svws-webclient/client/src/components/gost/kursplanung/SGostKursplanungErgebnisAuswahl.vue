<template>
	<template v-if="!getErgebnisse().isEmpty()">
		<svws-ui-table clickable :clicked="auswahlErgebnis" @update:clicked="gotoErgebnis" v-model="selected_ergebnisse" :selectable="(getErgebnisse().size() > 1) && hatUpdateKompetenz" class="z-20 relative"
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
			<template #cell(bewertung)="{ rowData: ergebnis }">
				<div v-if="hatErgebnismanager(ergebnis)" class="inline-flex flex-wrap w-full gap-x-1 gap-y-2.5">
					<span class="flex gap-1 items-center ml-0.5" :class="{'filter saturate-200': auswahlErgebnis === ergebnis}">
						<!-- Wert 1  -->
						<svws-ui-tooltip v-if="getErgebnismanager(ergebnis).getOfBewertung1Wert() > 0" autosize>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': bgColor(getErgebnismanager(ergebnis).getOfBewertung1Farbcode())}">{{ getErgebnismanager(ergebnis).getOfBewertung1Wert() }}</span>
							<template #content>
								<pre>{{ getErgebnismanager(ergebnis).regelGetTooltipFuerRegelverletzungen() }}</pre>
							</template>
						</svws-ui-tooltip>
						<span v-else class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': bgColor(getErgebnismanager(ergebnis).getOfBewertung1Farbcode())}">0</span>
						<!-- Wert 2-->
						<svws-ui-tooltip v-if="getErgebnismanager(ergebnis).getOfBewertung2Wert() > 0" autosize>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': bgColor(getErgebnismanager(ergebnis).getOfBewertung2Farbcode())}">{{ getErgebnismanager(ergebnis).getOfBewertung2Wert() }}</span>
							<template #content>
								<pre>{{ getErgebnismanager(ergebnis).regelGetTooltipFuerWahlkonflikte() }}</pre>
							</template>
						</svws-ui-tooltip>
						<span v-else class="svws-ui-badge min-w-[2.75rem] text-center justify-center" title="0 Wahlkonflikte" :style="{'background-color': bgColor(getErgebnismanager(ergebnis).getOfBewertung2Farbcode())}">0</span>
						<!-- Wert 3-->
						<svws-ui-tooltip autosize>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': bgColor(getErgebnismanager(ergebnis).getOfBewertung3Farbcode())}"> {{ getErgebnismanager(ergebnis).getOfBewertung3Wert() }} </span>
							<template #content>
								<pre>{{ getErgebnismanager(ergebnis).regelGetTooltipFuerKursdifferenzen() }}</pre>
							</template>
						</svws-ui-tooltip>
						<!-- Wert 4-->
						<svws-ui-tooltip v-if="getErgebnismanager(ergebnis).getOfBewertung4Wert() > 0" autosize>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': bgColor(getErgebnismanager(ergebnis).getOfBewertung4Farbcode())}">{{ getErgebnismanager(ergebnis).getOfBewertung4Wert() }}</span>
							<template #content>
								<pre>{{ getErgebnismanager(ergebnis).regelGetTooltipFuerFaecherparallelitaet() }}</pre>
							</template>
						</svws-ui-tooltip>
						<span v-else class="svws-ui-badge min-w-[2.75rem] text-center justify-center" title="0 Fächer parallel" :style="{'background-color': bgColor(getErgebnismanager(ergebnis).getOfBewertung4Farbcode())}">0</span>
						<!-- Ende Wertanzeige-->
					</span>
					<div class="ml-auto inline-flex">
						<template v-if="hatUpdateKompetenz">
							<template v-if="auswahlErgebnis === ergebnis">
								<svws-ui-button type="icon" @click.stop="remove_ergebnis" title="Ergebnis löschen" :disabled="apiStatus.pending || (getErgebnisse().size() <= 1)" class="text-black dark:text-white">
									<span class="icon-sm i-ri-delete-bin-line -mx-0.5" />
								</svws-ui-button>
							</template>
							<svws-ui-tooltip v-if="ergebnis.istAktiv">
								<span @click="patchErgebnis({ istAktiv: false }, ergebnis.id)" class="icon icon-primary i-ri-checkbox-circle-fill relative -my-0.5 ml-2 hover:opacity-50" />
								<template #content>Aktiviertes Ergebnis</template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else>
								<span @click="patchErgebnis({ istAktiv: true }, ergebnis.id)" class="icon icon-primary i-ri-checkbox-circle-line relative -my-0.5 ml-2 opacity-25 hover:opacity-75" />
								<template #content> Ergebnis als aktiv markieren </template>
							</svws-ui-tooltip>
						</template>
						<template v-else>
							<span v-if="ergebnis.istAktiv" class="icon icon-primary i-ri-checkbox-circle-fill relative -my-0.5 ml-2 hover:opacity-50" title="Aktiviertes Ergebnis" />
						</template>
					</div>
				</div>
			</template>
			<template #actions v-if="selected_ergebnisse.length && hatUpdateKompetenz">
				<svws-ui-button @click="remove_ergebnisse" type="transparent" :disabled="(getErgebnisse().size() === 0) || (selected_ergebnisse.length >= getErgebnisse().size()) || (selected_ergebnisse.length === 0)" class="text-error">
					<span class="icon-sm icon-error i-ri-delete-bin-line" />
					<span>{{ selected_ergebnisse.length }} {{ (selected_ergebnisse.length > 1) ? 'Ergebnisse' : 'Ergebnis' }} löschen</span>
				</svws-ui-button>
			</template>
		</svws-ui-table>
	</template>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { GostBlockungsdatenManager, GostBlockungsergebnis, GostBlockungsergebnisManager, GostHalbjahr, List } from "@core";
	import type { ApiStatus } from '~/components/ApiStatus';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		patchErgebnis: (data: Partial<GostBlockungsergebnis>, idErgebnis: number) => Promise<boolean>;
		removeErgebnisse: (ergebnisse: GostBlockungsergebnis[]) => Promise<void>;
		gotoErgebnis: (value: GostBlockungsergebnis | undefined) => Promise<void>;
		auswahlErgebnis: GostBlockungsergebnis | undefined;
		halbjahr: GostHalbjahr;
		apiStatus: ApiStatus;
		hatUpdateKompetenz: boolean;
	}>();

	const selected_ergebnisse = ref<GostBlockungsergebnis[]>([]);

	function getErgebnisse() : List<GostBlockungsergebnis> {
		return props.getDatenmanager().ergebnisGetListeSortiertNachBewertung();
	}

	async function remove_ergebnisse() {
		await props.removeErgebnisse(selected_ergebnisse.value);
		selected_ergebnisse.value = [];
	}

	async function remove_ergebnis() {
		if (!props.auswahlErgebnis)
			return;
		await props.removeErgebnisse([props.auswahlErgebnis]);
	}

	function hatErgebnismanager(ergebnis: GostBlockungsergebnis) : boolean {
		return props.getDatenmanager().ergebnisManagerExists(ergebnis.id);
	}

	function getErgebnismanager(ergebnis: GostBlockungsergebnis) : GostBlockungsergebnisManager {
		return props.getDatenmanager().ergebnisManagerGet(ergebnis.id);
	}

	function bgColor(code: number): string {
		return `hsl(${Math.round((1 - code) * 120)},100%,75%)`
	}

</script>
