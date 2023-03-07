<template>
	<div v-if="visible">
		<svws-ui-table :model-value="auswahlErgebnis" @update:model-value="setAuswahlErgebnis" v-model:selection="selected_ergebnisse" is-multi-select class="mt-10 z-20 relative"
			:columns="[{ key: 'id', label: 'ID'}, { key: 'bewertung', label: 'Bewertungen', span: 10 }]" :data="(rows_ergebnisse.toArray() as DataTableItem[])"
			:footer="true">
			<template #head-bewertung>
				<th style="flex-grow: 10;">
					<div class="z-50">
						<svws-ui-popover placement="top">
							<template #trigger>
								<span class="inline-flex items-center cursor-pointer">
									Bewertungen
									<svws-ui-icon class="ml-1">
										<i-ri-question-line />
									</svws-ui-icon>
								</span>
							</template>
							<template #content>
								<div class="normal-case text-base rich-text">
									<div class="my-1">Anzahl von Problemen durch:</div>
									<ul class="font-bold">
										<li>Regelverletzungen</li>
										<li>Wahlkonflikte</li>
										<li>max. Kursdifferenz</li>
										<li>Fächerparallelität</li>
									</ul>
								</div>
							</template>
						</svws-ui-popover>
					</div>
				</th>
			</template>
			<template #cell-bewertung="{ row }: {row: GostBlockungsergebnisListeneintrag}">
				<span class="flex gap-1 cell--bewertung items-center text-sm">
					<span :style="{'background-color': color1(row)}">{{ getDatenmanager().getOfBewertung1Wert(row.id) }}</span>
					<span :style="{'background-color': color2(row)}">{{ getDatenmanager().getOfBewertung2Wert(row.id) }}</span>
					<span :style="{'background-color': color3(row)}">{{ getDatenmanager().getOfBewertung3Wert(row.id) }}</span>
					<span :style="{'background-color': color4(row)}">{{ getDatenmanager().getOfBewertung4Wert(row.id) }}</span>
				</span>
				<svws-ui-icon v-if="row.istVorlage"> <i-ri-pushpin-fill /></svws-ui-icon>
				<div v-if="(row.id === auswahlErgebnis?.id && !blockung_aktiv)" class="flex gap-1">
					<svws-ui-button size="small" type="secondary" class="cursor-pointer" @click.stop="derive_blockung" :disabled="apiStatus.pending"> Ableiten </svws-ui-button>
					<svws-ui-button v-if="rows_ergebnisse.size() > 1" type="trash" class="cursor-pointer" @click.stop="remove_ergebnis" :disabled="apiStatus.pending" title="Ergebnis löschen" />
				</div>
			</template>
			<template #footer>
				<span v-if="selected_ergebnisse.length > 0" class="text-sm normal-case mr-auto">
					<span v-if="selected_ergebnisse.length === rows_ergebnisse.size()">Es muss mindestens ein Ergebnis behalten werden.</span>
					<span v-else>{{ selected_ergebnisse.length }}/{{ rows_ergebnisse.size() }} ausgewählt</span>
				</span>
				<div v-if="selected_ergebnisse.length > 0 && selected_ergebnisse.length !== rows_ergebnisse.size()" class="overflow-hidden flex items-center justify-end pr-1 h-full">
					<svws-ui-popover class="popper--danger" :open-delay="200">
						<template #trigger>
							<svws-ui-button @click="remove_ergebnisse" type="trash" class="cursor-pointer"
								:disabled="selected_ergebnisse.length > rows_ergebnisse.size() - 1" />
						</template>
						<template #content>
							Auswahl löschen
						</template>
					</svws-ui-popover>
				</div>
			</template>
		</svws-ui-table>
	</div>
</template>

<script setup lang="ts">

	import { GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, GostHalbjahr, GostJahrgangsdaten, List } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, ref, Ref } from 'vue';
	import { DataTableItem, SvwsUiButton, SvwsUiIcon, SvwsUiTable } from '@svws-nrw/svws-ui';
	import { ApiStatus } from '~/components/ApiStatus';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		removeErgebnisse: (ergebnisse: GostBlockungsergebnisListeneintrag[]) => Promise<void>;
		ergebnisZuNeueBlockung: (idErgebnis: number) => Promise<void>;
		setAuswahlErgebnis: (value: GostBlockungsergebnisListeneintrag | undefined) => Promise<void>;
		auswahlErgebnis: GostBlockungsergebnisListeneintrag | undefined;
		jahrgangsdaten: GostJahrgangsdaten | undefined;
		halbjahr: GostHalbjahr;
		apiStatus: ApiStatus;
	}>();

	const selected_ergebnisse: Ref<GostBlockungsergebnisListeneintrag[]> = ref([]);

	const rows_ergebnisse: ComputedRef<List<GostBlockungsergebnisListeneintrag>> = computed(() => props.getDatenmanager().getErgebnisseSortiertNachBewertung());

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

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

	async function derive_blockung() {
		if (!props.auswahlErgebnis)
			return;
		await props.ergebnisZuNeueBlockung(props.auswahlErgebnis.id);
	}

	function color1(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (props.getDatenmanager().getOfBewertung1Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function color2(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (props.getDatenmanager().getOfBewertung2Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function color3(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (props.getDatenmanager().getOfBewertung3Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function color4(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (props.getDatenmanager().getOfBewertung4Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function colorMix(ergebnis: GostBlockungsergebnisListeneintrag): string {
		const combined = (props.getDatenmanager().getOfBewertung1Intervall(ergebnis.id))
			+ (props.getDatenmanager().getOfBewertung2Intervall(ergebnis.id))
			+ (props.getDatenmanager().getOfBewertung3Intervall(ergebnis.id))
			+ (props.getDatenmanager().getOfBewertung4Intervall(ergebnis.id));
		return `hsl(${Math.round((1 - (combined > 1 ? 1 : combined)) * 120)},100%,70%)`
	}

	const visible: ComputedRef<boolean> = computed(() => {
		return props.getDatenmanager().getErgebnisseSortiertNachBewertung().size() > 0;
	});

</script>

<style>
	.loading-disclaimer {
		background-color: rgba(var(--svws-ui-color-dark-20), var(--tw-border-opacity));
		--tw-bg-opacity: 1;
		--tw-border-opacity: 1;
		border-width: 1px;
		padding: .25rem .75rem;
		line-height: 1.125;
	}

	.loading-spinner-dimensions {
		height: 1rem;
		width: 1rem;
	}

	.loading-display {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
	}

	.loading-rotation {
		display: block;
		position: relative;
		-webkit-animation: spin 6s steps(11, end) infinite;
		-moz-animation: spin 6s steps(11, end) infinite;
		animation: spin 6s steps(11, end) infinite;
	}

	.api-error-text {
		color: rgb(var(--svws-ui-color-error));
		font-weight: 700;
	}

	@-webkit-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

	@-moz-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

	@keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}
</style>

<style lang="postcss" scoped>
	.cell--bewertung span {
		@apply inline-block text-center text-black rounded font-normal;
		min-width: 3em;
		padding: 0.1em 0.2em;
	}

	.bewertung--letter {
		@apply inline-block text-center rounded font-bold bg-white leading-tight;
		padding: 0.05em 0.1em;
		min-width: 2em;
		font-size: 0.8em;
	}

	.vt-clicked .cell--bewertung span {
		@apply font-bold saturate-200;
	}

	.v-table tr {
		@apply overflow-visible;
	}
</style>
