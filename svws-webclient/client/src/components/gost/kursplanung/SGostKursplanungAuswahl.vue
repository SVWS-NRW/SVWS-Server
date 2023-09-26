<template>
	<template v-if="visible">
		<svws-ui-table clickable :clicked="halbjahr" @update:clicked="select_hj" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :items="GostHalbjahr.values()" class="mb-10">
			<template #cell(kuerzel)="{ rowData: row }">
				<div class="flex justify-between w-full">
					<div class="inline-flex gap-3">
						{{ row.kuerzel }}
						<svws-ui-tooltip v-if="istBlockungPersistiert(row)" position="right">
							<i-ri-forbid2-line /> <template #content> Es liegt bereits eine persistierte Blockung vor. </template>
						</svws-ui-tooltip>
					</div>
					<div v-if="row === halbjahr" class="inline-flex gap-1 -my-0.5">
						<svws-ui-button type="secondary" @click.stop="blockung_hinzufuegen">
							<span class="inline-flex items-center text-button">
								<i-ri-add-line class="-mx-0.5" />
							</span>
							Blockung hinzufügen
						</svws-ui-button>
						<s-gost-kursplanung-modal-blockung-recover v-if="istBlockungPersistiert(row)" v-slot="{ openModal }" :restore-blockung="restoreBlockung">
							<svws-ui-button type="secondary" @click="openModal()">Wiederherstellen</svws-ui-button>
						</s-gost-kursplanung-modal-blockung-recover>
					</div>
				</div>
			</template>
		</svws-ui-table>
		<s-gost-kursplanung-blockung-auswahl :halbjahr="halbjahr" :patch-blockung="patchBlockung" :jahrgangsdaten="jahrgangsdaten" :remove-blockung="removeBlockung"
			:set-auswahl-blockung="setAuswahlBlockung" :auswahl-blockung="auswahlBlockung" :map-blockungen="mapBlockungen" :api-status="apiStatus"
			:get-datenmanager="getDatenmanager" :remove-ergebnisse="removeErgebnisse" :ergebnis-zu-neue-blockung="ergebnisZuNeueBlockung"
			:set-auswahl-ergebnis="setAuswahlErgebnis" :hat-blockung="hatBlockung" :auswahl-ergebnis="auswahlErgebnis" :rechne-gost-blockung="rechneGostBlockung"
			:restore-blockung="restoreBlockung" />
	</template>
</template>

<script setup lang="ts">
	import type { GostKursplanungAuswahlProps } from './SGostKursplanungAuswahlProps';
	import { computed } from 'vue';
	import { GostHalbjahr } from "@core";

	const props = defineProps<GostKursplanungAuswahlProps>();

	const istBlockungPersistiert = (row: GostHalbjahr): boolean => {
		if (props.jahrgangsdaten === undefined)
			return true;
		return props.jahrgangsdaten.istBlockungFestgelegt[row.id];
	}

	async function select_hj(halbjahr: GostHalbjahr | null) {
		if (halbjahr !== null)
			await props.setHalbjahr(halbjahr);
	}

	async function blockung_hinzufuegen() {
		if (props.jahrgangsdaten?.abiturjahr === undefined)
			return;
		await props.addBlockung();
	}

	const visible = computed<boolean>(() =>
		(props.jahrgangsdaten !== undefined) && (props.jahrgangsdaten.abiturjahr > 0));

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
