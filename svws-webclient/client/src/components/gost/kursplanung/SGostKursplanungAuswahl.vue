<template>
	<template v-if="visible">
		<svws-ui-table clickable :clicked="halbjahr" @update:clicked="select_hj" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :items="GostHalbjahr.values()">
			<template #cell(kuerzel)="{ rowData: gostHalbjahr }">
				<div class="flex justify-between w-full pr-1">
					<span>{{ gostHalbjahr.kuerzel }}</span>
					<div v-if="hatUpdateKompetenz" class="flex w-fit gap-1">
						<s-gost-kursplanung-modal-blockung-revert v-if="istRueckgaengigMoeglich[gostHalbjahr.id]" v-slot="{ openModal }" :revert-blockung>
							<svws-ui-button :disabled="apiStatus.pending" type="transparent" @click="openModal" class="-my-1" title="Die Persistierung der Blockung für dieses Halbjahr rückgängig machen">
								<span class="icon-sm i-ri-eraser-line -mb-0.5" /> Rückgängig
							</svws-ui-button>
						</s-gost-kursplanung-modal-blockung-revert>
						<s-gost-kursplanung-modal-blockung-recover v-if="istBlockungPersistiert(gostHalbjahr)" v-slot="{ openModal }" :restore-blockung>
							<svws-ui-button :disabled="apiStatus.pending" type="transparent" @click="openModal" class="-my-1" title="Erstelle eine Blockung aus der Persistierung in den Leistungsdaten">
								<span class="icon-sm i-ri-arrow-turn-back-line -mb-0.5" /> Wiederherstellen
							</svws-ui-button>
						</s-gost-kursplanung-modal-blockung-recover>
					</div>
				</div>
			</template>
		</svws-ui-table>
		<s-gost-kursplanung-blockung-auswahl :ist-blockung-persistiert="istBlockungPersistiert(halbjahr)" :map-core-type-name-json-data
			:halbjahr :patch-blockung :remove-blockung :ausfuehrliche-darstellung-kursdifferenz :set-ausfuehrliche-darstellung-kursdifferenz
			:goto-blockung :auswahl-blockung :map-blockungen :api-status :get-datenmanager :add-ergebnisse :patch-ergebnis :remove-ergebnisse :add-blockung
			:goto-ergebnis :hat-blockung :auswahl-ergebnis :rechne-gost-blockung :restore-blockung :mode :get-ergebnismanager :hat-update-kompetenz />
	</template>
</template>

<script setup lang="ts">
	import type { GostKursplanungAuswahlProps } from './SGostKursplanungAuswahlProps';
	import { computed } from 'vue';
	import { BenutzerKompetenz, GostHalbjahr } from "@core";

	const props = defineProps<GostKursplanungAuswahlProps>();

	const hatUpdateKompetenz = computed<boolean>(() => {
		const abiturjahr = props.jahrgangsdaten()?.abiturjahr;
		return (props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN)
			|| (props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)
				&& abiturjahr !== undefined
				&& props.benutzerKompetenzenAbiturjahrgaenge.has(abiturjahr)));
	});

	function istBlockungPersistiert(row: GostHalbjahr): boolean {
		return props.jahrgangsdaten()?.istBlockungFestgelegt[row.id] === true;
	}

	const istRueckgaengigMoeglich = computed<boolean[]>(() => {
		const jgdaten = props.jahrgangsdaten();
		if (jgdaten === undefined)
			return [ false, false, false, false, false, false ];
		const result : boolean[] = [];
		for (const hj of GostHalbjahr.values())
			result.push(!jgdaten.existierenNotenInLeistungsdaten[hj.id] && jgdaten.istBlockungFestgelegt[hj.id]);
		return result;
	});

	async function select_hj(halbjahr: GostHalbjahr | null) {
		if (halbjahr !== null)
			await props.setHalbjahr(halbjahr);
	}

	const visible = computed<boolean>(() => (props.jahrgangsdaten()?.abiturjahr ?? -1) > 0);

</script>

<style lang="postcss" scoped>

	@reference "../../../../../ui/src/assets/styles/index.css"

	.cell--bewertung span {
		@apply inline-block text-center text-ui-100 rounded-sm font-normal;
		min-width: 5ex;
		padding: 0.05em 0.2em;
	}

	.vt-clicked .cell--bewertung span {
		filter: brightness(0.8) saturate(200%);
	}
</style>
