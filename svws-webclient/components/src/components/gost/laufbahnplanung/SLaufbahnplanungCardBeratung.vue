<template>
	<svws-ui-content-card title="Beratung">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-select title="Zuletzt beraten von" :items="mapLehrer.values()" :model-value="getBeratungslehrer"
				:item-text="i=>`${i.kuerzel} (${i.vorname} ${i.nachname})`"
				:item-filter="filter" removable autocomplete ref="refLehrer" />
			<svws-ui-text-input :model-value="beratungsdatum" type="date" placeholder="Datum" ref="refBeratungsdatum" />
			<svws-ui-textarea-input placeholder="Kommentar" :model-value="gostLaufbahnBeratungsdaten().kommentar" :autoresize="true" ref="refKommentar" span="full" />
			<svws-ui-button @click="speichern" :disabled="!dirty">Beratungsdaten speichern</svws-ui-button>
		</svws-ui-input-wrapper>
		<svws-ui-modal size="small" :show="()=>ref(show)">
			<template #modalTitle>Ungesicherte Beratungsdaten</template>
			<template #modalContent>
				Die Änderungen an den Beratungsdaten wurden noch nicht gespeichert.
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="show = false">Nicht sichern</svws-ui-button>
				<svws-ui-button type="secondary" @click="show = false">Änderungen speichern</svws-ui-button>
			</template>
		</svws-ui-modal>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Ref } from "vue";
	import type { ComponentExposed } from 'vue-component-type-helpers'
	import { ref, computed } from "vue";
	import { GostLaufbahnplanungBeratungsdaten, LehrerListeEintrag } from "@core";
	import { SvwsUiTextInput, SvwsUiTextareaInput, SvwsUiSelect } from "@ui";

	const props = defineProps<{
		gostLaufbahnBeratungsdaten: () => GostLaufbahnplanungBeratungsdaten;
		patchBeratungsdaten: (data : Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		id?: number;
	}>();

	const refLehrer: Ref<ComponentExposed<typeof SvwsUiSelect<LehrerListeEintrag>> | null> = ref(null);
	const refBeratungsdatum: Ref<InstanceType<typeof SvwsUiTextInput> | null> = ref(null);
	const refKommentar: Ref<InstanceType<typeof SvwsUiTextareaInput> | null> = ref(null);
	const show: Ref<boolean> = ref<boolean>(false);

	const beratungsdatum = computed(()=> props.gostLaufbahnBeratungsdaten().beratungsdatum || new Date().toISOString().slice(0, -14))

	const dirty = computed(()=>{
		const lehrerIDNeu = refLehrer.value?.content?.id || null;
		const kommentarNeu = refKommentar.value?.content || null;
		const lehrerIDalt = props.gostLaufbahnBeratungsdaten().beratungslehrerID;
		const kommentarAlt = props.gostLaufbahnBeratungsdaten().kommentar || null;
		const datumNeu = refBeratungsdatum.value?.content || null;
		const datumAlt = beratungsdatum.value;
		return lehrerIDNeu !== lehrerIDalt || kommentarNeu !== kommentarAlt || datumAlt !== datumNeu;
	})

	const getBeratungslehrer = computed(()=> {
		let id = props.gostLaufbahnBeratungsdaten().beratungslehrerID;
		if (id === null)
			id = (props.id === undefined) ? -1 : props.id;
		return props.mapLehrer.get(id);
	})

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
