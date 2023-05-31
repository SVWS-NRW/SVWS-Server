<template>
	<svws-ui-content-card title="Zeugnis und Abschluss">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Konferenz-Datum" :model-value="data.datumKonferenz || undefined"
				@update:model-value="doPatch({ datumKonferenz: String($event) })" type="date" />
			<svws-ui-text-input placeholder="Zeugnis-Datum" :model-value="data.datumZeugnis || undefined"
				@update:model-value="doPatch({ datumZeugnis: String($event) })" type="date" />
			<svws-ui-spacing />
			<svws-ui-input-wrapper :grid="2">
				<div class="opacity-50"> TODO: istAbschlussPrognose </div>
				<div class="opacity-50"> TODO: versetzungsvermerk </div>
				<div class="opacity-50"> TODO: abschlussart </div>
				<svws-ui-multi-select title="Folgeklasse" v-model="folgeklasse" :items="mapKlassen" :item-text="i => `${i.kuerzel}`" autocomplete />
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
				<svws-ui-textarea-input placeholder="Zeugnisbemerkungen" :model-value="data.bemerkungen.zeugnisAllgemein || undefined"
					@update:model-value="doPatchBemerkungen({ zeugnisAllgemein : String($event) })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Arbeits- und Sozialverhalten" :model-value="data.bemerkungen.zeugnisASV || undefined"
					@update:model-value="doPatchBemerkungen({ zeugnisASV : String($event) })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Außerunterrichtliches Engagement" :model-value="data.bemerkungen.zeugnisAUE || undefined"
					@update:model-value="doPatchBemerkungen({ zeugnisAUE : String($event) })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Bemerkung Versetzung" :model-value="data.bemerkungen.versetzungsentscheidung || undefined"
					@update:model-value="doPatchBemerkungen({ versetzungsentscheidung : String($event) })" resizeable="vertical" :autoresize="true" />
			</svws-ui-input-wrapper>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { KlassenListeEintrag, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittsdaten } from "@svws-nrw/svws-core";
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
