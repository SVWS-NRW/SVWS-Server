<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Nachname" :model-value="data().nachname"
					@update:model-value="doPatch({ nachname: String($event) })" type="text" />
				<svws-ui-text-input placeholder="Zusatz" type="text" />
				<svws-ui-text-input placeholder="Rufname" :model-value="data().vorname"
					@update:model-value="doPatch({ vorname: String($event) })" type="text" />
				<svws-ui-text-input placeholder="Alle Vornamen" :model-value="data().alleVornamen"
					@update:model-value="doPatch({ alleVornamen: String($event) })" type="text" />
				<svws-ui-multi-select title="Geschlecht" v-model="geschlecht" :items="Geschlecht.values()" statistics />
				<svws-ui-text-input placeholder="Geburtsdatum" :model-value="data().geburtsdatum"
					@update:model-value="doPatch({ geburtsdatum: String($event) })" type="date" required statistics />
				<svws-ui-text-input placeholder="Geburtsort" :model-value="data().geburtsort"
					@update:model-value="doPatch({ geburtsort: String($event) })" type="text" statistics />
				<svws-ui-text-input placeholder="Geburtsname" :model-value="data().geburtsname"
					@update:model-value="doPatch({ geburtsname: String($event) })" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import type { SchuelerStammdaten } from "@svws-nrw/svws-core";
	import { Geschlecht } from "@svws-nrw/svws-core";

	const props = defineProps<{
		data: () => SchuelerStammdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}

	const geschlecht: WritableComputedRef<Geschlecht> = computed({
		get: () => Geschlecht.fromValue(props.data().geschlecht) ?? Geschlecht.X,
		set: (value) => doPatch({ geschlecht: value.id })
	});

</script>
