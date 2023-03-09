<template>
	<div v-if="visible" class="mt-10">
		<svws-ui-table :model-value="halbjahr" @update:model-value="select_hj" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :data="GostHalbjahr.values()" class="mb-10">
			<template #body="{rows}: {rows: GostHalbjahr[]}">
				<template v-for="row in rows" :key="row.id">
					<tr :class="{'vt-clicked': row.id === halbjahr.id}" @click="select_hj(row)">
						<td>
							{{ row.kuerzel }}
							<svws-ui-button type="secondary" v-if="allow_add_blockung(row)" @click.stop="blockung_hinzufuegen">Blockung hinzufügen</svws-ui-button>
						</td>
					</tr>
				</template>
			</template>
		</svws-ui-table>
		<s-gost-kursplanung-blockung-auswahl :halbjahr="halbjahr" :patch-blockung="patchBlockung" :jahrgangsdaten="jahrgangsdaten" :remove-blockung="removeBlockung"
			:set-auswahl-blockung="setAuswahlBlockung" :auswahl-blockung="auswahlBlockung" :map-blockungen="mapBlockungen" :api-status="apiStatus"
			:get-datenmanager="getDatenmanager" :remove-ergebnisse="removeErgebnisse" :ergebnis-zu-neue-blockung="ergebnisZuNeueBlockung"
			:set-auswahl-ergebnis="setAuswahlErgebnis" :hat-blockung="hatBlockung" :auswahl-ergebnis="auswahlErgebnis" :rechne-gost-blockung="rechneGostBlockung" />
	</div>
</template>

<script setup lang="ts">

	import { GostHalbjahr } from "@svws-nrw/svws-core";
	import { computed, ComputedRef } from 'vue';
	import { GostKursplanungAuswahlProps } from './SGostKursplanungAuswahlProps';

	const props = defineProps<GostKursplanungAuswahlProps>();


	const allow_add_blockung = (row: GostHalbjahr): boolean => {
		const curr_hj = row.id === props.halbjahr.id;
		if (!curr_hj || props.jahrgangsdaten === undefined)
			return false;
		return props.jahrgangsdaten.istBlockungFestgelegt[row.id] ? false : true
	}

	async function select_hj(halbjahr: GostHalbjahr) {
		await props.setHalbjahr(halbjahr);
	}

	async function blockung_hinzufuegen() {
		if (props.jahrgangsdaten?.abiturjahr === undefined)
			return;
		await props.addBlockung();
	}

	const visible: ComputedRef<boolean> = computed(() => {
		return (props.jahrgangsdaten !== undefined) && (props.jahrgangsdaten.abiturjahr > 0);
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
