<template>
	<svws-ui-content-card title="Beratung" class="mt-9">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-multi-select title="Letzte Beratung durchgeführt von" :items="mapLehrer.values()" :model-value="getBeratungslehrer()"
				:item-text="(i: LehrerListeEintrag)=>`${i.kuerzel} (${i.vorname} ${i.nachname})`"
				:item-filter="filter" removable autocomplete ref="refLehrer" />
			<svws-ui-text-input :model-value="gostLaufbahnBeratungsdaten().beratungsdatum || new Date().toISOString().slice(0, -14)" type="date"
				placeholder="Beratungsdatum" ref="refBeratungsdatum" />
			<svws-ui-textarea-input placeholder="Kommentar" :model-value="gostLaufbahnBeratungsdaten().kommentar" :autoresize="true" ref="refKommentar"
				span="full" />
			<svws-ui-button @click="speichern()">Beratungsdaten speichern</svws-ui-button>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostLaufbahnplanungBeratungsdaten, LehrerListeEintrag } from "@core";
	import { type SvwsUiMultiSelect, type SvwsUiTextInput, type SvwsUiTextareaInput } from "@ui";
	import { ref, type Ref } from "vue";
	import type { ComponentExposed } from 'vue-component-type-helpers'

	const props = defineProps<{
		gostLaufbahnBeratungsdaten: () => GostLaufbahnplanungBeratungsdaten;
		patchBeratungsdaten: (data : Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		id?: number;
	}>();

	const refLehrer: Ref<ComponentExposed<typeof SvwsUiMultiSelect<LehrerListeEintrag>> | null> = ref(null);
	const refBeratungsdatum: Ref<InstanceType<typeof SvwsUiTextInput> | null> = ref(null);
	const refKommentar: Ref<InstanceType<typeof SvwsUiTextareaInput> | null> = ref(null);

	function getBeratungslehrer() : LehrerListeEintrag | undefined {
		let id = props.gostLaufbahnBeratungsdaten().beratungslehrerID;
		if (id === null)
			id = (props.id === undefined) ? -1 : props.id;
		return props.mapLehrer.get(id);
	}

	async function speichern() {
		const result = new GostLaufbahnplanungBeratungsdaten();
		result.beratungslehrerID = (refLehrer.value?.content instanceof LehrerListeEintrag)
			? refLehrer.value.content.id : null;
		result.beratungsdatum = ((refBeratungsdatum.value === null) || (refBeratungsdatum.value.content === null)) ? null : String(refBeratungsdatum.value.content);
		result.kommentar = ((refKommentar.value === null) || (refKommentar.value.content === null)) ? null : String(refKommentar.value.content);
		await props.patchBeratungsdaten(result);
	}

	const filter = (items: LehrerListeEintrag[], search: string) => {
		return items.filter(i => (i.istSichtbar === true) && (i.kuerzel.includes(search.toLocaleLowerCase()) || i.nachname?.toLocaleLowerCase().includes(search.toLocaleLowerCase())));
	};

</script>
