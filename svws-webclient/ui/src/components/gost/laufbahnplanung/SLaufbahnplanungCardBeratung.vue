<template>
	<svws-ui-content-card title="Beratung">
		<svws-ui-input-wrapper :grid="2">
			<div class="col-span-full">
				Letzer Import mit Änderungen:&nbsp;&nbsp;
				<template v-if="gostLaufbahnBeratungsdaten().ruecklaufdatum === null">'—'</template>
				<template v-else> {{ new Date(gostLaufbahnBeratungsdaten().ruecklaufdatum!).toLocaleDateString("de-DE", { year: "numeric", month: "2-digit", day: "2-digit" }) }} </template>
			</div>
			<svws-ui-select title="Zuletzt beraten von" :items="listLehrer" :model-value="getBeratungslehrer"
				:item-text="i=>`${i.kuerzel} (${i.vorname} ${i.nachname})`"
				:item-filter="filter" removable autocomplete ref="refLehrer" />
			<svws-ui-text-input :model-value="beratungsdatum" type="date" placeholder="Datum" ref="refBeratungsdatum" />
			<svws-ui-textarea-input placeholder="Kommentar" :model-value="gostLaufbahnBeratungsdaten().kommentar" :autoresize="true" ref="refKommentar" span="full" />
			<svws-ui-button @click="speichern" :disabled="!dirty">Beratungsdaten speichern</svws-ui-button>
		</svws-ui-input-wrapper>
		<template v-if="(checkpoint !== undefined) && (continueRoutingAfterCheckpoint !== undefined)">
			<svws-ui-checkpoint-modal :checkpoint :continue-routing="continueRoutingAfterCheckpoint" />
		</template>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComponentExposed } from 'vue-component-type-helpers'
	import { ref, computed, watch, watchEffect } from "vue";
	import { GostLaufbahnplanungBeratungsdaten } from '../../../../../core/src/core/data/gost/GostLaufbahnplanungBeratungsdaten';
	import { LehrerListeEintrag } from '../../../../../core/src/core/data/lehrer/LehrerListeEintrag';
	import type { ArrayList } from '../../../../../core/src/java/util/ArrayList';
	import type { SchuelerListeEintrag } from '../../../../../core/src/core/data/schueler/SchuelerListeEintrag';
	import SvwsUiSelect from "../../../ui/controls/SvwsUiSelect.vue"
	import SvwsUiTextareaInput from "../../../ui/controls/SvwsUiTextareaInput.vue"
	import SvwsUiTextInput from "../../../ui/controls/SvwsUiTextInput.vue"
	import type { Checkpoint } from '../../../ui/modal/Checkpoint';
	import type { RoutingStatus } from './SSchuelerLaufbahnplanungProps';


	const props = defineProps<{
		gostLaufbahnBeratungsdaten: () => GostLaufbahnplanungBeratungsdaten;
		patchBeratungsdaten: (data : Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
		listLehrer: ArrayList<LehrerListeEintrag>;
		schueler: SchuelerListeEintrag;
		updated?: boolean;
		id?: number;
		checkpoint?: Checkpoint;
		continueRoutingAfterCheckpoint?: () => Promise<RoutingStatus>;
	}>();

	const refLehrer = ref<ComponentExposed<typeof SvwsUiSelect<LehrerListeEintrag>>>();
	const refBeratungsdatum = ref<ComponentExposed<typeof SvwsUiTextInput>>();
	const refKommentar = ref<ComponentExposed<typeof SvwsUiTextareaInput>>();

	const beratungsdatum = computed<string>(() => props.gostLaufbahnBeratungsdaten().beratungsdatum ?? new Date().toISOString().slice(0, -14))

	watch(() => props.schueler, () => {
		if ((refBeratungsdatum.value?.input?.value === undefined) || refKommentar.value?.content === undefined)
			return;
		refBeratungsdatum.value.input.value = beratungsdatum.value;
		refKommentar.value.content = props.gostLaufbahnBeratungsdaten().kommentar;
	})

	watch(() => props.updated, (neu) => {
		if (neu && (refBeratungsdatum.value?.input?.value !== undefined))
			refBeratungsdatum.value.input.value = new Date().toISOString().slice(0, -14);
	})

	const dirty = computed<boolean>(() => {
		const lehrerIDNeu = refLehrer.value?.content?.id ?? null;
		const kommentarNeu = refKommentar.value?.content ?? null;
		const lehrerIDalt = props.gostLaufbahnBeratungsdaten().beratungslehrerID;
		const kommentarAlt = props.gostLaufbahnBeratungsdaten().kommentar;
		const datumNeu = refBeratungsdatum.value?.content ?? null;
		const datumAlt = beratungsdatum.value;
		return (lehrerIDNeu !== lehrerIDalt) || (kommentarNeu !== kommentarAlt) || (datumAlt !== datumNeu) || (props.updated === true);
	})

	watchEffect(() => {
		if (props.checkpoint?.active !== undefined)
			props.checkpoint.active = dirty.value;
	})

	const getBeratungslehrer = computed<LehrerListeEintrag | undefined>(() => {
		let id = props.gostLaufbahnBeratungsdaten().beratungslehrerID;
		if (id === null)
			id = (props.id === undefined) ? -1 : props.id;
		for (const l of props.listLehrer)
			if (l.id === id)
				return l;
		return undefined;
	})

	async function speichern() {
		const result = new GostLaufbahnplanungBeratungsdaten();
		result.beratungslehrerID = (refLehrer.value?.content instanceof LehrerListeEintrag)
			? refLehrer.value.content.id : null;
		result.beratungsdatum = (refBeratungsdatum.value?.content === undefined) ? null : refBeratungsdatum.value.content;
		if (result.beratungsdatum !== refBeratungsdatum.value?.input?.value)
			result.beratungsdatum = refBeratungsdatum.value?.input?.value ?? null;
		result.kommentar = (refKommentar.value?.content === undefined) ? null : refKommentar.value.content;
		if (props.checkpoint)
			props.checkpoint.active = false;
		await props.patchBeratungsdaten(result);
	}

	const filter = (items: LehrerListeEintrag[], search: string) => {
		return items.filter(i => (i.istSichtbar === true) && (i.kuerzel.includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase())));
	};

</script>

