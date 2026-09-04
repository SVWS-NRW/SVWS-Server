<template>
	<svws-ui-content-card title="Beratung">
		<svws-ui-input-wrapper :grid="2">
			<div class="col-span-full">
				Letzter Import mit Änderungen:&nbsp;&nbsp;
				<template v-if="gostLaufbahnplanungState.gostLaufbahnBeratungsdaten.ruecklaufdatum === null">'—'</template>
				<template v-else> {{ new Date(gostLaufbahnplanungState.gostLaufbahnBeratungsdaten.ruecklaufdatum!).toLocaleDateString("de-DE", { year: "numeric", month: "2-digit", day: "2-digit" }) }} </template>
			</div>
			<svws-ui-select title="Zuletzt beraten von" :items="gostLaufbahnplanungState.listeLehrer" :model-value="getBeratungslehrer" :item-text="i => `${i.kuerzel} (${i.vorname} ${i.nachname})`" :item-filter removable autocomplete ref="refLehrer" />
			<svws-ui-text-input :model-value="beratungsdatum" type="date" placeholder="Datum" ref="refBeratungsdatum" />
			<svws-ui-textarea-input placeholder="Kommentar" :model-value="gostLaufbahnplanungState.gostLaufbahnBeratungsdaten.kommentar" autoresize ref="refKommentar" span="full" />
			<svws-ui-button @click="speichern" :disabled="!dirty">Beratungsdaten speichern</svws-ui-button>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComponentExposed } from 'vue-component-type-helpers';
	import { ref, computed, watch } from "vue";
	import { GostLaufbahnplanungBeratungsdaten } from '@core/core/data/gost/GostLaufbahnplanungBeratungsdaten';
	import { LehrerListeEintrag } from '@core/core/data/lehrer/LehrerListeEintrag';
	import { useGostLaufbahnplanungState } from '@ui/states/GostLaufbahnplanungState';
	import SvwsUiSelect from '@ui/ui/controls/SvwsUiSelect.vue';
	import SvwsUiTextareaInput from '@ui/ui/controls/SvwsUiTextareaInput.vue';
	import SvwsUiTextInput from '@ui/ui/controls/SvwsUiTextInput.vue';


	const props = defineProps<{
		patchBeratungsdaten: (data: Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
		updated?: boolean;
	}>();
	const gostLaufbahnplanungState = useGostLaufbahnplanungState();

	const refLehrer = ref<ComponentExposed<typeof SvwsUiSelect<LehrerListeEintrag>>>();
	const refBeratungsdatum = ref<ComponentExposed<typeof SvwsUiTextInput>>();
	const refKommentar = ref<ComponentExposed<typeof SvwsUiTextareaInput>>();
	const beratungsdatum = computed<string>(() => gostLaufbahnplanungState.gostLaufbahnBeratungsdaten.beratungsdatum ?? new Date().toISOString().slice(0, -14));

	watch(() => gostLaufbahnplanungState.schuelerOrNull, () => {
		if ((refBeratungsdatum.value?.input?.value === undefined) || refKommentar.value?.content === undefined) {
			return;
		}
		refBeratungsdatum.value.input.value = beratungsdatum.value;
		refKommentar.value.data = gostLaufbahnplanungState.gostLaufbahnBeratungsdaten.kommentar;
	});

	watch(() => props.updated, (neu) => {
		if (neu && (refBeratungsdatum.value?.input?.value !== undefined)) {
			refBeratungsdatum.value.input.value = new Date().toISOString().slice(0, -14);
		}
	});

	const dirty = computed<boolean>(() => {
		const lehrerIDNeu = refLehrer.value?.content?.id ?? null;
		const kommentarNeu = refKommentar.value?.content ?? null;
		const lehrerIDalt = gostLaufbahnplanungState.gostLaufbahnBeratungsdaten.beratungslehrerID;
		const kommentarAlt = gostLaufbahnplanungState.gostLaufbahnBeratungsdaten.kommentar;
		const datumNeu = refBeratungsdatum.value?.content ?? null;
		const datumAlt = beratungsdatum.value;
		return (lehrerIDNeu !== lehrerIDalt) || (kommentarNeu !== kommentarAlt) || (datumAlt !== datumNeu) || (props.updated === true);
	});

	const getBeratungslehrer = computed<LehrerListeEintrag | null>(() => {
		let id = gostLaufbahnplanungState.gostLaufbahnBeratungsdaten.beratungslehrerID;
		if (id === null) {
			id = (gostLaufbahnplanungState.id === undefined) ? -1 : gostLaufbahnplanungState.id;
		}
		for (const l of gostLaufbahnplanungState.listeLehrer) {
			if (l.id === id) {
				return l;
			}
		}
		return null;
	});

	async function speichern() {
		const result = new GostLaufbahnplanungBeratungsdaten();
		result.beratungslehrerID = (refLehrer.value?.content instanceof LehrerListeEintrag)
			? refLehrer.value.content.id : null;
		result.beratungsdatum = (refBeratungsdatum.value?.content === undefined) ? null : refBeratungsdatum.value.content;
		if (result.beratungsdatum !== refBeratungsdatum.value?.input?.value) {
			result.beratungsdatum = refBeratungsdatum.value?.input?.value ?? null;
		}
		result.kommentar = (refKommentar.value?.content === undefined) ? null : refKommentar.value.content;
		await props.patchBeratungsdaten(result);
	}

	function itemFilter(items: LehrerListeEintrag[], search: string) {
		return items.filter(i => (i.istSichtbar === true)
			&& (i.kuerzel.includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase())));
	}

</script>

