<template>
	<svws-ui-multi-select title="—" v-model="inputNote" :items="Note.values()" :item-text="(item: Note) => item?.kuerzel" headless class="w-full" />
</template>

<script setup lang="ts">

	import type { SchuelerLeistungsdaten, SchuelerLernabschnittsdaten } from "@svws-nrw/svws-core";
	import { Note } from "@svws-nrw/svws-core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten;
		note: string
		patchLeistung: (data : Partial<SchuelerLeistungsdaten>, id : number) => Promise<void>;
	}>();
	const inputNote: WritableComputedRef<Note | undefined> = computed({
		get: () => Note.fromKuerzel(props.note),
		set: (value) => void props.patchLeistung({ note: value?.kuerzel }, props.data.id)
	});

</script>
