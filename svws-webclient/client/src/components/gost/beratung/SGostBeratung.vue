<template>
	<div v-if="jahrgangsdaten() !== undefined" class="page--content page--content--full relative">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<svws-ui-button :type="!istManuellerModus ? 'transparent' : 'danger'" @click="switchManuellerModus" title="Modus wechseln">
					<i-ri-loop-right-line />
					Modus: <span>{{ istManuellerModus ? 'manuell' : 'normal' }}</span>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen :gost-jahrgangsdaten="jahrgangsdaten()" :reset-fachwahlen="resetFachwahlen" />
				<s-modal-laufbahnplanung-alle-fachwahlen-loeschen v-if="jahrgangsdaten().abiturjahr !== -1" :gost-jahrgangsdaten="jahrgangsdaten" :reset-fachwahlen="resetFachwahlenAlle" />
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-modal-hilfe> <hilfe-gost-beratung /> </svws-ui-modal-hilfe>
		</Teleport>
		<s-laufbahnplanung-card-planung title="Vorlage für Schüler des Abiturjahrgangs" :goto-kursblockung="gotoKursblockung"
			:abiturdaten-manager="abiturdatenManager" :manueller-modus="istManuellerModus"
			:gost-jahrgangsdaten="jahrgangsdaten()" :set-wahl="setWahl" ignoriere-sprachenfolge />
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card v-if="istAbiturjahrgang" title="Beratungslehrer">
				<svws-ui-table :items="beratungslehrer()" selectable :model-value="selected" @update:model-value="selected=$event" count :columns="[{key: 'kuerzel', label: 'Kürzel', span: 0.25}, {key: 'name', label: 'Name'}]">
					<template #cell(kuerzel)="{ rowData:l }">
						{{ `${l.kuerzel}` }}
					</template>
					<template #cell(name)="{ rowData:l }">
						{{ `${l.nachname}, ${l.vorname}` }}
					</template>
					<template #actions>
						<!-- TODO: Hier kommt das Dropdown hin -->
						<svws-ui-select :model-value="undefined" @update:model-value="lehrer => addLehrer(lehrer || undefined)" headless indeterminate
							autocomplete :item-filter="lehrer_filter" :items="lehrer" removable title="Lehrkraft hinzufügen…" :item-text="l=> `${l.nachname}, ${l.vorname} (${l.kuerzel})`" />
						<svws-ui-button @click="removeBeratungslehrer(selected)" type="trash" :disabled="!selected.length" />
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
			<s-laufbahnplanung-card-status :abiturdaten-manager="abiturdatenManager"
				:fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
			<svws-ui-content-card title="Textvorlagen">
				<svws-ui-input-wrapper>
					<svws-ui-textarea-input placeholder="Beratungsbögen" :model-value="jahrgangsdaten().textBeratungsbogen"
						@change="textBeratungsbogen => doPatch({ textBeratungsbogen })" resizeable="vertical" autoresize />
					<svws-ui-textarea-input placeholder="Mailversand" :model-value="jahrgangsdaten().textMailversand"
						@change="textMailversand => doPatch({ textMailversand })" resizeable="vertical" autoresize />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostBeratungProps } from "./SGostBeratungProps";
	import type { GostBeratungslehrer, LehrerListeEintrag, GostJahrgangsdaten } from "@core";
	import { onMounted, computed, ref } from "vue";
	import { lehrer_filter } from '~/utils/helfer';

	const props = defineProps<GostBeratungProps>();

	const selected = ref<GostBeratungslehrer[]>([]);

	const istAbiturjahrgang = computed<boolean>(() => (props.jahrgangsdaten().abiturjahr > 0));

	const istManuellerModus = ref(false)
	function switchManuellerModus() {
		istManuellerModus.value = !istManuellerModus.value;
	}

	const lehrer = computed<Map<number, LehrerListeEintrag>>(() => {
		const map = new Map<number, LehrerListeEintrag>(props.mapLehrer);
		for (const l of props.beratungslehrer())
			map.delete(l.id);
		return map;
	})

	async function addLehrer(lehrer?: LehrerListeEintrag) {
		if (lehrer === undefined)
			return;
		await props.addBeratungslehrer(lehrer.id);
	}

	async function doPatch(data: Partial<GostJahrgangsdaten>) {
		return await props.patchJahrgangsdaten(data, props.jahrgangsdaten().abiturjahr);
	}
	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>

<style lang="postcss" scoped>
.page--content {
  @apply grid;
  grid-template-columns: 1fr minmax(36rem, 1fr);
}
</style>
