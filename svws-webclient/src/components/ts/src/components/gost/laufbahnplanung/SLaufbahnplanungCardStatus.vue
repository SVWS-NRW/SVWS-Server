<template>
	<svws-ui-content-card title="Belegprüfungsergebnisse" class="sticky -top-8 pt-8">
		<template #actions>
			<s-laufbahnplanung-belegpruefungsart v-model="art" />
		</template>
		<div class="print:hidden -mt-4">
			<s-laufbahnplanung-fehler :fehlerliste="fehlerliste" />
			<s-laufbahnplanung-informationen :fehlerliste="fehlerliste" />
			<s-laufbahnplanung-fachkombinationen :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen" />
			<div class="mt-16 flex flex-col gap-2">
				<svws-ui-text-input v-model="inputBeratungsdatum" type="date" placeholder="Beratungsdatum" />
				<svws-ui-textarea-input placeholder="Kommentar" v-model="kommentar" resizeable="vertical" :autoresize="true" />
			</div>
			<s-laufbahnplanung-sprachpruefungen v-if="sprachendaten" :sprachendaten="sprachendaten" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { List, GostBelegpruefungErgebnisFehler, GostJahrgangFachkombination, AbiturdatenManager, GostFaecherManager,
		GostBelegpruefungsArt, Sprachendaten, GostLaufbahnplanungBeratungsdaten } from "@svws-nrw/svws-core";

	const props = defineProps<{
		gostLaufbahnBeratungsdaten: () => GostLaufbahnplanungBeratungsdaten;
		patchBeratungsdaten: (data : Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
		abiturdatenManager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
		fehlerliste: List<GostBelegpruefungErgebnisFehler>;
		gostBelegpruefungsArt: GostBelegpruefungsArt;
	}>();

	const emit = defineEmits<{
		(e: 'update:gost-belegpruefungs-art', value: GostBelegpruefungsArt): void,
	}>();

	const art: WritableComputedRef<GostBelegpruefungsArt> = computed({
		get: () => props.gostBelegpruefungsArt,
		set: (value) => emit('update:gost-belegpruefungs-art', value)
	});

	const inputBeratungsdatum: WritableComputedRef<string> = computed({
		get: () => props.gostLaufbahnBeratungsdaten().beratungsdatum || "",
		set: (value) => {
			if (value === "")
				void props.patchBeratungsdaten({ beratungsdatum: null });
			else
				void props.patchBeratungsdaten({ beratungsdatum: value });
		}
	});

	const kommentar: WritableComputedRef<string> = computed({
		get: () => props.gostLaufbahnBeratungsdaten().kommentar || "",
		set: (value) => void props.patchBeratungsdaten({ kommentar: value })
	});

	const sprachendaten: ComputedRef<Sprachendaten | null> = computed(() => props.abiturdatenManager.getSprachendaten());

</script>
