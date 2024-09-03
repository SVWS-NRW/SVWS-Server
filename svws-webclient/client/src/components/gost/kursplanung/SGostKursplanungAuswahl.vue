<template>
	<template v-if="visible">
		<svws-ui-table clickable :clicked="halbjahr" @update:clicked="select_hj" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :items="GostHalbjahr.values()">
			<template #cell(kuerzel)="{ rowData: halbjahr }">
				<div class="flex justify-between w-full pr-1">
					<span>{{ halbjahr.kuerzel }}</span>
					<div v-if="hatUpdateKompetenz" class="flex w-fit gap-1">
						<s-gost-kursplanung-modal-blockung-revert v-if="istRueckgaengigMoeglich[halbjahr.id]" v-slot="{ openModal }" :revert-blockung>
							<svws-ui-button :disabled="apiStatus.pending" type="transparent" @click="openModal" class="-my-1" title="Die Persistierung der Blockung für dieses Halbjahr rückgängig machen">
								<span class="icon-sm i-ri-eraser-line -mb-0.5" /> Rückgängig
							</svws-ui-button>
						</s-gost-kursplanung-modal-blockung-revert>
						<s-gost-kursplanung-modal-blockung-recover v-if="istBlockungPersistiert(halbjahr)" v-slot="{ openModal }" :restore-blockung>
							<svws-ui-button :disabled="apiStatus.pending" type="transparent" @click="openModal" class="-my-1" title="Erstelle eine Blockung aus der Persistierung in den Leistungsdaten">
								<span class="icon-sm i-ri-arrow-turn-back-line -mb-0.5" /> Wiederherstellen
							</svws-ui-button>
						</s-gost-kursplanung-modal-blockung-recover>
					</div>
				</div>
			</template>
		</svws-ui-table>
		<s-gost-kursplanung-blockung-auswahl :ist-blockung-persistiert="istBlockungPersistiert(halbjahr)"
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

	const istBlockungPersistiert = (row: GostHalbjahr): boolean => {
		const festgelegt = props.jahrgangsdaten()?.istBlockungFestgelegt[row.id];
		return festgelegt === true;
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
