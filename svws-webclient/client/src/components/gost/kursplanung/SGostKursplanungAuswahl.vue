<template>
	<template v-if="visible">
		<svws-ui-table clickable :clicked="halbjahr" @update:clicked="select_hj" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :items="GostHalbjahr.values()">
			<template #cell(kuerzel)="{ rowData: row }">
				<div class="flex justify-between w-full pr-1">
					<span>{{ row.kuerzel }}</span>
					<svws-ui-tooltip v-if="istBlockungPersistiert(row)">
						<i-ri-information-line class="-mb-0.5" />
						<template #content>Es liegt bereits eine persistierte Blockung vor, die wiederhergestellt werden kann.</template>
					</svws-ui-tooltip>
				</div>
			</template>
		</svws-ui-table>
		<s-gost-kursplanung-blockung-auswahl :halbjahr="halbjahr" :patch-blockung="patchBlockung" :remove-blockung="removeBlockung"
			:set-auswahl-blockung="setAuswahlBlockung" :auswahl-blockung="auswahlBlockung" :map-blockungen="mapBlockungen" :api-status="apiStatus"
			:get-datenmanager="getDatenmanager" :remove-ergebnisse="removeErgebnisse" :ergebnis-zu-neue-blockung="ergebnisZuNeueBlockung"
			:set-auswahl-ergebnis="setAuswahlErgebnis" :hat-blockung="hatBlockung" :auswahl-ergebnis="auswahlErgebnis" :rechne-gost-blockung="rechneGostBlockung"
			:restore-blockung="restoreBlockung" :ist-blockung-persistiert="istBlockungPersistiert(halbjahr)">
			<template #blockungRecoverAction>
				<s-gost-kursplanung-modal-blockung-recover v-if="istBlockungPersistiert(halbjahr)" v-slot="{ openModal }" :restore-blockung="restoreBlockung">
					<svws-ui-button type="transparent" @click="openModal()" class="-my-1" title="Aus bestehenden Leistungsdaten wiederherstellen">Wiederherstellen</svws-ui-button>
				</s-gost-kursplanung-modal-blockung-recover>
			</template>
			<template #blockungAuswahlActions>
				<svws-ui-button type="icon" @click.stop="blockung_hinzufuegen">
					<i-ri-add-line class="-mx-0.5" />
				</svws-ui-button>
			</template>
		</s-gost-kursplanung-blockung-auswahl>
	</template>
</template>

<script setup lang="ts">
	import type { GostKursplanungAuswahlProps } from './SGostKursplanungAuswahlProps';
	import { computed } from 'vue';
	import { GostHalbjahr } from "@core";

	const props = defineProps<GostKursplanungAuswahlProps>();

	const istBlockungPersistiert = (row: GostHalbjahr): boolean => {
		const festgelegt = props.jahrgangsdaten()?.istBlockungFestgelegt[row.id];
		return festgelegt === true;
	}

	async function select_hj(halbjahr: GostHalbjahr | null) {
		if (halbjahr !== null)
			await props.setHalbjahr(halbjahr);
	}

	async function blockung_hinzufuegen() {
		if (props.jahrgangsdaten()?.abiturjahr === undefined)
			return;
		await props.addBlockung();
	}

	const visible = computed<boolean>(() => {
		const res = props.jahrgangsdaten()?.abiturjahr;
		return (res && res > 0) ? true : false;
	})

</script>

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
