<template>
	<svws-ui-content-card title="Zeugnis und Abschluss">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Konferenz-Datum" :model-value="data.datumKonferenz" @change="datumKonferenz=>doPatch({ datumKonferenz })" type="date" />
			<svws-ui-text-input placeholder="Zeugnis-Datum" :model-value="data.datumZeugnis" @change="datumZeugnis=>doPatch({ datumZeugnis })" type="date" />
			<svws-ui-spacing />
			<svws-ui-input-wrapper :grid="2">
				<div class="opacity-50"> TODO: istAbschlussPrognose </div>
				<div class="opacity-50"> TODO: versetzungsvermerk </div>
				<div class="opacity-50"> TODO: abschlussart </div>
				<svws-ui-select title="Folgeklasse" v-model="folgeklasse" :items="mapKlassen" :item-text="i => `${i.kuerzel}`" autocomplete />
			</svws-ui-input-wrapper>
			<svws-ui-spacing />
			<svws-ui-input-wrapper class="opacity-50">
				<div> TODO: abschluss </div>
				<div> TODO: abschlussBerufsbildend </div>
				<div> TODO: zeugnisart </div>
			</svws-ui-input-wrapper>
			<div class="col-span-full flex gap-4">
				<svws-ui-button type="primary"> Versetzungs-/Abschluss-Berechnung </svws-ui-button>
				<svws-ui-button type="secondary"> Nachprüfungen </svws-ui-button>
			</div>
			<svws-ui-spacing />
			<svws-ui-input-wrapper>
				<svws-ui-textarea-input placeholder="Zeugnisbemerkungen" :model-value="data.bemerkungen.zeugnisAllgemein"
					@change="zeugnisAllgemein => doPatchBemerkungen({ zeugnisAllgemein: zeugnisAllgemein === null ? '' : zeugnisAllgemein })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Arbeits- und Sozialverhalten" :model-value="data.bemerkungen.zeugnisASV"
					@change="zeugnisASV => doPatchBemerkungen({ zeugnisASV: zeugnisASV === null ? '' : zeugnisASV })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Außerunterrichtliches Engagement" :model-value="data.bemerkungen.zeugnisAUE"
					@change="zeugnisAUE => doPatchBemerkungen({ zeugnisAUE: zeugnisAUE === null ? '' : zeugnisAUE })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Bemerkung Versetzung" :model-value="data.bemerkungen.versetzungsentscheidung"
					@change="versetzungsentscheidung => doPatchBemerkungen({ versetzungsentscheidung: versetzungsentscheidung === null ? '' : versetzungsentscheidung })"
					resizeable="vertical" :autoresize="true" />
			</svws-ui-input-wrapper>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { KlassenListeEintrag, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittsdaten } from "@core";
	import type { WritableComputedRef } from 'vue';
	import { computed } from 'vue';

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten;
		mapKlassen: Map<number, KlassenListeEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerLernabschnittsdaten>) : void;
		(e: 'patchBemerkungen', data: Partial<SchuelerLernabschnittBemerkungen>) : void;
	}>()

	function doPatch(data: Partial<SchuelerLernabschnittsdaten>) {
		emit('patch', data);
	}

	function doPatchBemerkungen(data: Partial<SchuelerLernabschnittBemerkungen>) {
		emit('patchBemerkungen', data);
	}

	const folgeklasse: WritableComputedRef<KlassenListeEintrag | undefined> = computed({
		get: () => props.data.folgeklassenID === null ? undefined : props.mapKlassen.get(props.data.folgeklassenID),
		set: (value) => emit('patch', { folgeklassenID: (value === undefined) ? null : value.id })
	});

</script>
