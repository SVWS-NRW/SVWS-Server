<template>
	<template v-if="!getErgebnisse().isEmpty()">
		<svws-ui-data-table clickable :clicked="auswahlErgebnis" @update:clicked="setAuswahlErgebnis" v-model="selected_ergebnisse" selectable class="mt-10 z-20 relative"
			:columns="[{ key: 'id', label: 'ID'}, { key: 'bewertung', label: 'Bewertungen', span: 10 }]" :items="getErgebnisse()" :count="selected_ergebnisse.length !== getErgebnisse().size()">
			<template #header(bewertung)>
				<div style="flex-grow: 10;" class="inline-flex">
					<svws-ui-tooltip position="top">
						Bewertungen
						<template #content>
							<div class="normal-case text-base rich-text">
								<div class="my-1">Anzahl von Problemen durch:</div>
								<ul class="mb-1">
									<li>Regelverletzungen</li>
									<li>Wahlkonflikte</li>
									<li>max. Kursdifferenz</li>
									<li>Fächerparallelität</li>
								</ul>
							</div>
						</template>
					</svws-ui-tooltip>
				</div>
			</template>
			<template #cell(bewertung)="{ rowData: row }">
				<span class="flex gap-1 cell--bewertung items-center text-sm cursor-pointer">
					<svws-ui-tooltip position="right">
						<span :style="{'background-color': getDatenmanager().ergebnisGetBewertung1Wert(row.id) === 0 ? 'rgba(0,0,0,0.1)' : color1(row)}">{{ getDatenmanager().ergebnisGetBewertung1Wert(row.id) }}</span>
						<template #content>
							<span class="inline-flex items-center gap-0.5">
								{{ getDatenmanager().ergebnisGetBewertung1Wert(row.id) }} Regelverletzungen
								<span v-if="getDatenmanager().ergebnisGetBewertung1Wert(row.id) === 0">
									<i-ri-check-line class="opacity-25" />
								</span>
							</span>
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip position="right">
						<span :style="{'background-color': getDatenmanager().ergebnisGetBewertung2Wert(row.id) === 0 ? 'rgba(0,0,0,0.1)' : color2(row)}">{{ getDatenmanager().ergebnisGetBewertung2Wert(row.id) }}</span>
						<template #content>
							<span class="inline-flex items-center gap-0.5">
								{{ getDatenmanager().ergebnisGetBewertung2Wert(row.id) }} Wahlkonflikte
								<span v-if="getDatenmanager().ergebnisGetBewertung2Wert(row.id) === 0">
									<i-ri-check-line class="opacity-25" />
								</span>
							</span>
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip position="right">
						<span :style="{'background-color': getDatenmanager().ergebnisGetBewertung3Wert(row.id) === 0 ? 'rgba(0,0,0,0.1)' : color3(row)}">{{ getDatenmanager().ergebnisGetBewertung3Wert(row.id) }}</span>
						<template #content>
							<span class="inline-flex items-center gap-0.5">
								max. Kursdifferenz: {{ getDatenmanager().ergebnisGetBewertung3Wert(row.id) }}
								<span v-if="getDatenmanager().ergebnisGetBewertung3Wert(row.id) === 0">
									<i-ri-check-line class="opacity-25" />
								</span>
							</span>
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip position="right">
						<span :style="{'background-color': getDatenmanager().ergebnisGetBewertung4Wert(row.id) === 0 ? 'rgba(0,0,0,0.1)' : color4(row)}">{{ getDatenmanager().ergebnisGetBewertung4Wert(row.id) }}</span>
						<template #content>
							<span class="inline-flex items-center gap-0.5">
								Fächerparallelität: {{ getDatenmanager().ergebnisGetBewertung4Wert(row.id) }}
								<span v-if="getDatenmanager().ergebnisGetBewertung4Wert(row.id) === 0">
									<i-ri-check-line class="opacity-25" />
								</span>
							</span>
						</template>
					</svws-ui-tooltip>
				</span>
				<span class="ml-auto">
					<svws-ui-tooltip v-if="row.istVorlage" position="right">
						<i-ri-pushpin-line />
						<template #content>
							Aktiviertes Ergebnis
						</template>
					</svws-ui-tooltip>
				</span>
				<div v-if="(row.id === auswahlErgebnis?.id && !istAktiveBlockung())" class="flex gap-1">
					<svws-ui-button size="small" type="secondary" class="cursor-pointer" @click.stop="derive_blockung" :disabled="apiStatus.pending" title="Eine neue Blockung auf Grundlage dieses Ergebnisses erstellen."> Ableiten </svws-ui-button>
					<svws-ui-button v-if="getErgebnisse().size() > 1" type="trash" class="cursor-pointer" @click.stop="remove_ergebnis" :disabled="apiStatus.pending || selected_ergebnisse.length > 0" title="Ergebnis löschen" />
				</div>
			</template>
			<template #footerActions>
				<span v-if="selected_ergebnisse.length === getErgebnisse().size()" class="text-sm normal-case mr-auto inline-flex items-center gap-0.5">
					<i-ri-alert-fill class="text-error" />
					<span>Es muss mindestens ein Ergebnis behalten werden.</span>
				</span>
				<div v-if="selected_ergebnisse.length > 0 && selected_ergebnisse.length !== getErgebnisse().size()" class="flex items-center justify-end pr-1 h-full">
					<svws-ui-button @click="remove_ergebnisse" type="trash" class="cursor-pointer"
						:disabled="selected_ergebnisse.length > getErgebnisse().size() - 1" />
				</div>
			</template>
		</svws-ui-data-table>
	</template>
</template>

<script setup lang="ts">

	import type { GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, GostHalbjahr, GostJahrgangsdaten, List } from "@core";
	import type { ApiStatus } from '~/components/ApiStatus';
	import { ref } from 'vue';

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

	const selected_ergebnisse = ref<GostBlockungsergebnisListeneintrag[]>([]);

	function getErgebnisse() : List<GostBlockungsergebnisListeneintrag> {
		return props.getDatenmanager().ergebnisGetListeSortiertNachBewertung();
	}

	function istAktiveBlockung() : boolean {
		return props.getDatenmanager().daten().istAktiv;
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

	async function derive_blockung() {
		if (!props.auswahlErgebnis)
			return;
		await props.ergebnisZuNeueBlockung(props.auswahlErgebnis.id);
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
