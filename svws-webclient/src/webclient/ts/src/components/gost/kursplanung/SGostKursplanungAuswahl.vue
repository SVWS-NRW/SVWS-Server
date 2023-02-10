<template>
	<div v-if="visible" class="mt-10">
		<svws-ui-table :v-model="selected_hj" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :data="GostHalbjahr.values()" class="mb-10">
			<template #body="{rows}: {rows: GostHalbjahr[]}">
				<template v-for="row in rows" :key="row.id">
					<tr :class="{'vt-clicked': row.id === selected_hj.id}" @click="select_hj(row)">
						<td>
							{{ row.kuerzel }}
							<svws-ui-button type="transparent" v-if="allow_add_blockung(row)" @click.stop="blockung_hinzufuegen">Blockung hinzufügen</svws-ui-button>
						</td>
					</tr>
				</template>
			</template>
		</svws-ui-table>
		<router-view name="gost_kursplanung_blockung_auswahl" />
	</div>
</template>

<script setup lang="ts">

	import { GostHalbjahr } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, WritableComputedRef } from 'vue';
	import { RouterView, useRouter } from 'vue-router';
	import { App } from '~/apps/BaseApp';
	import { DataGostJahrgang } from '~/apps/gost/DataGostJahrgang';
	import { routeGostKursplanungHalbjahr } from '~/router/apps/gost/kursplanung/RouteGostKursplanungHalbjahr';
	import { routeGostKursplanung } from '~/router/apps/gost/RouteGostKursplanung';

	const props = defineProps<{
		jahrgangsdaten: DataGostJahrgang;
		halbjahr: GostHalbjahr;
	}>();

	const router = useRouter();

	const selected_hj: WritableComputedRef<GostHalbjahr> = routeGostKursplanung.getSelector();

	const allow_add_blockung = (row: GostHalbjahr): boolean => {
		const curr_hj = row.id === selected_hj.value?.id;
		if (!curr_hj || props.jahrgangsdaten.daten === undefined)
			return false;
		return props.jahrgangsdaten.daten.istBlockungFestgelegt[row.id] ? false : true
	}

	function select_hj(halbjahr: GostHalbjahr) {
		selected_hj.value = halbjahr;
	}

	async function blockung_hinzufuegen() {
		if (props.jahrgangsdaten.daten?.abiturjahr === undefined || !selected_hj.value)
			return;
		const result = await App.api.createGostAbiturjahrgangBlockung(App.schema, props.jahrgangsdaten.daten.abiturjahr, selected_hj.value.id);
		const abiturjahr = props.jahrgangsdaten.daten.abiturjahr;
		await router.push({ name: routeGostKursplanungHalbjahr.name, params: { abiturjahr: abiturjahr, halbjahr: props.halbjahr.id, idblockung: result.id } });
	}

	const visible: ComputedRef<boolean> = computed(() => {
		return (props.jahrgangsdaten.daten !== undefined) && (props.jahrgangsdaten.daten.abiturjahr > 0);
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
