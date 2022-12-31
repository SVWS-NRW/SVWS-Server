<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Nachname" v-model="nachname" type="text" />
				<svws-ui-text-input placeholder="Zusatz" v-model="zusatzNachname" type="text" />
				<svws-ui-text-input placeholder="Rufname" v-model="vorname" type="text" />
				<svws-ui-text-input placeholder="Alle Vornamen" v-model="alleVornamen" type="text" />
				<svws-ui-multi-select title="Geschlecht" v-model="geschlecht" :items="Geschlecht.values()" statistics />
				<svws-ui-text-input placeholder="Geburtsdatum" v-model="inputGeburtsdatum" type="date" required statistics />
				<svws-ui-text-input placeholder="Geburtsort" v-model="inputGeburtsort" type="text" statistics />
				<svws-ui-text-input placeholder="Geburtsname" v-model="inputGeburtsname" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { Geschlecht, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
	import { UseSchuelerStammdaten } from "~/utils/composables/stammdaten"
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());

	const use = new UseSchuelerStammdaten(props.stammdaten)

	const { vorname, alleVornamen, nachname, zusatzNachname, geschlecht } = use;

	const inputGeburtsdatum: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.geburtsdatum?.toString(),
		set: (value) => props.stammdaten.patch({ geburtsdatum: value })
	});

	const inputGeburtsort: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.geburtsort?.toString(),
		set: (value) => props.stammdaten.patch({ geburtsort: value })
	});

	const inputGeburtsname: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.geburtsname?.toString(),
		set: (value) => props.stammdaten.patch({ geburtsname: value })
	});

</script>
