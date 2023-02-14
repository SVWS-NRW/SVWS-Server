<template>
	<div v-if="visible">
		<svws-ui-table v-model="selected_ergebnis" v-model:selection="selected_ergebnisse" is-multi-select class="mt-10"
			:columns="[{ key: 'id', label: 'ID'}, { key: 'bewertung', label: 'Bewertungen', span: 10 }]" :data="(rows_ergebnisse.toArray() as DataTableItem[])"
			:footer="true">
			<template #head-bewertung>
				<th style="flex-grow: 10;">
					<div>
						<svws-ui-popover placement="bottom">
							<template #trigger>
								<span class="inline-flex items-center">
									Bewertungen
									<svws-ui-icon class="ml-1">
										<i-ri-information-line />
									</svws-ui-icon>
								</span>
							</template>
							<template #content>
								<span class="normal-case text-base font-normal">
									Regelverletzungen, Wahlkonflikte, maximale Kursdifferenz, Fächerparallelität<br>
								</span>
							</template>
						</svws-ui-popover>
					</div>
				</th>
			</template>
			<template #cell-bewertung="{ row }: {row: GostBlockungsergebnisListeneintrag}">
				<span class="flex gap-1 cell--bewertung items-center text-sm">
					<span :style="{'background-color': color1(row)}">{{ manager?.getOfBewertung1Wert(row.id) }}</span>
					<span :style="{'background-color': color2(row)}">{{ manager?.getOfBewertung2Wert(row.id) }}</span>
					<span :style="{'background-color': color3(row)}">{{ manager?.getOfBewertung3Wert(row.id) }}</span>
					<span :style="{'background-color': color4(row)}">{{ manager?.getOfBewertung4Wert(row.id) }}</span>
				</span>
				<svws-ui-icon v-if="row.istVorlage"> <i-ri-pushpin-fill /></svws-ui-icon>
				<div v-if="(row.id === selected_ergebnis?.id && !blockung_aktiv)" class="flex gap-1">
					<svws-ui-button size="small" type="secondary" class="cursor-pointer" @click.stop="derive_blockung" :disabled="pending"> Ableiten </svws-ui-button>
					<svws-ui-button v-if="rows_ergebnisse.size() > 1" type="trash" class="cursor-pointer" @click.stop="remove_ergebnis" :disabled="pending" title="Ergebnis löschen" />
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
								@click.stop="remove_ergebnis" :disabled="selected_ergebnisse.length > rows_ergebnisse.size() - 1" />
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

	import { GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, GostHalbjahr, List, Vector } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from 'vue';
	import { routeLogin } from "~/router/RouteLogin";
	import { DataGostJahrgang } from '~/apps/gost/DataGostJahrgang';
	import { DataTableItem, SvwsUiButton, SvwsUiIcon, SvwsUiTable } from '@svws-nrw/svws-ui';
	import { DataGostKursblockung } from '~/apps/gost/DataGostKursblockung';
	import { ListKursblockungen } from '~/apps/gost/ListKursblockungen';
	import { routeGostKursplanungBlockung } from '~/router/apps/gost/kursplanung/RouteGostKursplanungBlockung';

	const props = defineProps<{
		jahrgangsdaten: DataGostJahrgang;
		halbjahr: GostHalbjahr;
		listBlockungen: ListKursblockungen;
		blockung: DataGostKursblockung;
	}>();

	const selected_ergebnisse: Ref<GostBlockungsergebnisListeneintrag[]> = ref([]);

	const manager: ComputedRef<GostBlockungsdatenManager | undefined> = computed(() => props.blockung.datenmanager);

	const rows_ergebnisse: ComputedRef<List<GostBlockungsergebnisListeneintrag>> =
		computed(() => manager.value?.getErgebnisseSortiertNachBewertung() || new Vector<GostBlockungsergebnisListeneintrag>());

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.blockung.daten?.istAktiv || false);

	const selected_ergebnis: WritableComputedRef<GostBlockungsergebnisListeneintrag | undefined> = routeGostKursplanungBlockung.getSelector();

	const pending: ComputedRef<boolean> = computed(()=> props.blockung.pending);

	async function remove_ergebnisse() {
		if (props.halbjahr === undefined)
			return;
		const abiturjahr = props.jahrgangsdaten.daten?.abiturjahr ?? -1;
		if (selected_ergebnisse.value.length > 0 && abiturjahr) {
			for (const ergebnis of selected_ergebnisse.value) {
				await routeLogin.data.api.deleteGostBlockungsergebnis(routeLogin.data.schema, ergebnis.id);
			}
			selected_ergebnisse.value = [];
			await props.listBlockungen.update_list(abiturjahr, props.halbjahr);
		}
	}

	async function remove_ergebnis() {
		if (!selected_ergebnis.value)
			return;
		await routeLogin.data.api.deleteGostBlockungsergebnis(routeLogin.data.schema, selected_ergebnis.value.id);
		const abiturjahr = props.jahrgangsdaten.daten?.abiturjahr ?? -1;
		await props.listBlockungen.update_list(abiturjahr, props.halbjahr)
	}

	async function derive_blockung() {
		if (!selected_ergebnis.value)
			return;
		await routeLogin.data.api.dupliziereGostBlockungMitErgebnis(routeLogin.data.schema, selected_ergebnis.value.id);
		const abiturjahr = props.jahrgangsdaten.daten?.abiturjahr ?? -1;
		await props.listBlockungen.update_list(abiturjahr, props.halbjahr);
	}

	function color1(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (manager.value?.getOfBewertung1Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function color2(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (manager.value?.getOfBewertung2Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function color3(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (manager.value?.getOfBewertung3Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function color4(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (manager.value?.getOfBewertung4Intervall(ergebnis.id)||0)) * 120)},100%,75%)`
	}
	function colorMix(ergebnis: GostBlockungsergebnisListeneintrag): string {
		const combined = (manager.value?.getOfBewertung1Intervall(ergebnis.id)||0) + (manager.value?.getOfBewertung2Intervall(ergebnis.id)||0) + (manager.value?.getOfBewertung3Intervall(ergebnis.id)||0) + (manager.value?.getOfBewertung4Intervall(ergebnis.id)||0);
		return `hsl(${Math.round((1 - (combined > 1 ? 1 : combined)) * 120)},100%,70%)`
	}

	const visible: ComputedRef<boolean> = computed(() => {
		return (props.blockung.daten !== undefined) && (props.blockung.daten?.ergebnisse.size() > 0);
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
