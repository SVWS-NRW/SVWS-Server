<template>
	<div v-if="visible">
		<svws-ui-table v-model="selected_ergebnis" v-model:selection="selected_ergebnisse" is-multi-select class="mt-10"
			:columns="[{ key: 'id', label: 'ID'}, { key: 'bewertung', label: 'Bewertungen', span: 15 }]" :data="(rows_ergebnisse.toArray() as DataTableItem[])"
			:footer="selected_ergebnisse.length > 0">
			<template #cell-bewertung="{ row }: {row: GostBlockungsergebnisListeneintrag}">
				<span class="flex gap-1 cell--bewertung">
					<span :style="{'background-color': color1(row)}">{{ manager?.getOfBewertung1Wert(row.id) }}</span>
					<span :style="{'background-color': color2(row)}">{{ manager?.getOfBewertung2Wert(row.id) }}</span>
					<span :style="{'background-color': color3(row)}">{{ manager?.getOfBewertung3Wert(row.id) }}</span>
					<span :style="{'background-color': color4(row)}">{{ manager?.getOfBewertung4Wert(row.id) }}</span>
				</span>
				<svws-ui-icon v-if="row.istVorlage"> <i-ri-pushpin-fill /></svws-ui-icon>
				<div v-if="(row.id === selected_ergebnis?.id && !blockung_aktiv)" class="flex gap-1">
					<svws-ui-button size="small" type="secondary" class="cursor-pointer" @click.stop="derive_blockung" :disabled="pending"> Ableiten </svws-ui-button>
					<svws-ui-button v-if="rows_ergebnisse.size() > 1" size="small" type="error" class="cursor-pointer" @click.stop="remove_ergebnis" :disabled="pending">
						<svws-ui-icon><i-ri-delete-bin-2-line /></svws-ui-icon>
					</svws-ui-button>
				</div>
			</template>
			<template #footer>
				<span v-if="selected_ergebnisse.length === rows_ergebnisse.size()">Mindestens ein Ergebnis behalten!</span>
				<svws-ui-button @click="remove_ergebnisse" type="error" size="small" :disabled="selected_ergebnisse.length > rows_ergebnisse.size() - 1">
					Auswahl löschen
				</svws-ui-button>
			</template>
		</svws-ui-table>
	</div>
</template>

<script setup lang="ts">

	import { GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, GostHalbjahr, GostJahrgang, LehrerListeEintrag, List, Vector } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, ref, Ref, ShallowRef, WritableComputedRef } from 'vue';
	import { App } from '~/apps/BaseApp';
	import { DataGostJahrgang } from '~/apps/gost/DataGostJahrgang';
	import { DataTableItem, SvwsUiButton, SvwsUiIcon, SvwsUiTable } from '@svws-nrw/svws-ui';
	import { DataSchuleStammdaten } from '~/apps/schule/DataSchuleStammdaten';
	import { DataGostSchuelerFachwahlen } from '~/apps/gost/DataGostSchuelerFachwahlen';
	import { ListLehrer } from '~/apps/lehrer/ListLehrer';
	import { DataGostFaecher } from '~/apps/gost/DataGostFaecher';
	import { DataGostKursblockung } from '~/apps/gost/DataGostKursblockung';
	import { DataGostKursblockungsergebnis } from '~/apps/gost/DataGostKursblockungsergebnis';
	import { ListKursblockungen } from '~/apps/gost/ListKursblockungen';
	import { routeGostKursplanungBlockung } from '~/router/apps/gost/kursplanung/RouteGostKursplanungBlockung';

	const props = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
		halbjahr: ShallowRef<GostHalbjahr>;
		listBlockungen: ListKursblockungen;
		blockung: DataGostKursblockung;
		ergebnis: DataGostKursblockungsergebnis;
		listLehrer: ListLehrer;
		mapLehrer: Map<Number, LehrerListeEintrag>;
		dataFachwahlen: DataGostSchuelerFachwahlen;
	}>();

	const selected_ergebnisse: Ref<GostBlockungsergebnisListeneintrag[]> = ref([]);

	const manager: ComputedRef<GostBlockungsdatenManager | undefined> = computed(() => props.blockung.datenmanager);

	const rows_ergebnisse: ComputedRef<List<GostBlockungsergebnisListeneintrag>> =
		computed(() => manager.value?.getErgebnisseSortiertNachBewertung() || new Vector<GostBlockungsergebnisListeneintrag>());

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.blockung.daten?.istAktiv || false);

	const selected_ergebnis: WritableComputedRef<GostBlockungsergebnisListeneintrag | undefined> = routeGostKursplanungBlockung.getSelector();

	const pending: ComputedRef<boolean> = computed(()=> props.blockung.pending);

	async function remove_ergebnisse() {
		if (props.halbjahr.value === undefined)
			return;
		const abiturjahr = props.jahrgangsdaten.daten?.abiturjahr?.valueOf() || -1;
		if (selected_ergebnisse.value.length > 0 && abiturjahr) {
			for (const ergebnis of selected_ergebnisse.value) {
				await App.api.deleteGostBlockungsergebnis(App.schema, ergebnis.id);
			}
			selected_ergebnisse.value = [];
			await props.listBlockungen.update_list(abiturjahr, props.halbjahr.value);
		}
	}

	async function remove_ergebnis() {
		if (!selected_ergebnis.value)
			return;
		await App.api.deleteGostBlockungsergebnis(App.schema, selected_ergebnis.value.id);
		const abiturjahr = props.jahrgangsdaten.daten?.abiturjahr?.valueOf() || -1;
		await props.listBlockungen.update_list(abiturjahr, props.halbjahr.value)
	}

	async function derive_blockung() {
		if (!selected_ergebnis.value)
			return;
		await App.api.dupliziereGostBlockungMitErgebnis(App.schema, selected_ergebnis.value.id);
		const abiturjahr = props.jahrgangsdaten.daten?.abiturjahr?.valueOf() || -1;
		await props.listBlockungen.update_list(abiturjahr, props.halbjahr.value, true);
	}

	function color1(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (manager.value?.getOfBewertung1Intervall(ergebnis.id)||0)) * 120)},100%,80%)`
	}
	function color2(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (manager.value?.getOfBewertung2Intervall(ergebnis.id)||0)) * 120)},100%,80%)`
	}
	function color3(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (manager.value?.getOfBewertung3Intervall(ergebnis.id)||0)) * 120)},100%,80%)`
	}
	function color4(ergebnis: GostBlockungsergebnisListeneintrag): string {
		return `hsl(${Math.round((1 - (manager.value?.getOfBewertung4Intervall(ergebnis.id)||0)) * 120)},100%,80%)`
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
		min-width: 5ex;
		padding: 0.05em 0.2em;
	}

	.vt-clicked .cell--bewertung span {
		filter: brightness(0.8) saturate(200%);
	}
</style>
