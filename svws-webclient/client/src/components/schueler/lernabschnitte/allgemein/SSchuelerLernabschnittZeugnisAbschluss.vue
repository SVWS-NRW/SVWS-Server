<template>
	<svws-ui-content-card title="Zeugnis und Abschluss">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Konferenz-Datum" :model-value="manager().lernabschnittGet().datumKonferenz" @change="datumKonferenz=>patch({ datumKonferenz })" type="date" />
			<svws-ui-text-input placeholder="Zeugnis-Datum" :model-value="manager().lernabschnittGet().datumZeugnis" @change="datumZeugnis=>patch({ datumZeugnis })" type="date" />
			<svws-ui-spacing />
			<svws-ui-input-wrapper :grid="2">
				<div class="opacity-50"> TODO: istAbschlussPrognose </div>
				<div class="opacity-50"> TODO: versetzungsvermerk </div>
				<div class="opacity-50"> TODO: abschlussart </div>
				<svws-ui-select title="Folgeklasse" v-model="folgeklasse" :items="props.manager().klasseGetMenge()" :item-text="i => `${i.kuerzel}`" autocomplete />
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
				<svws-ui-textarea-input placeholder="Zeugnisbemerkungen" :model-value="manager().lernabschnittGet().bemerkungen.zeugnisAllgemein"
					@change="zeugnisAllgemein => patchBemerkungen({ zeugnisAllgemein: zeugnisAllgemein === null ? '' : zeugnisAllgemein })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Arbeits- und Sozialverhalten" :model-value="manager().lernabschnittGet().bemerkungen.zeugnisASV"
					@change="zeugnisASV => patchBemerkungen({ zeugnisASV: zeugnisASV === null ? '' : zeugnisASV })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Außerunterrichtliches Engagement" :model-value="manager().lernabschnittGet().bemerkungen.zeugnisAUE"
					@change="zeugnisAUE => patchBemerkungen({ zeugnisAUE: zeugnisAUE === null ? '' : zeugnisAUE })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Bemerkung Versetzung" :model-value="manager().lernabschnittGet().bemerkungen.versetzungsentscheidung"
					@change="versetzungsentscheidung => patchBemerkungen({ versetzungsentscheidung: versetzungsentscheidung === null ? '' : versetzungsentscheidung })"
					resizeable="vertical" :autoresize="true" />
			</svws-ui-input-wrapper>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { KlassenListeEintrag, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittManager, SchuelerLernabschnittsdaten } from "@core";
	import { computed } from 'vue';

	const props = defineProps<{
		manager: () => SchuelerLernabschnittManager;
		patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
		patchBemerkungen: (data : Partial<SchuelerLernabschnittBemerkungen>) => Promise<void>;
	}>();

	const folgeklasse = computed<KlassenListeEintrag | undefined>({
		get: () => {
			const id = props.manager().lernabschnittGet().folgeklassenID;
			if (id === null)
				return undefined;
			return props.manager().klasseGetByIdOrException(id);
		},
		set: (value) => void props.patch({ folgeklassenID: (value === undefined) ? null : value.id })
	});

</script>
