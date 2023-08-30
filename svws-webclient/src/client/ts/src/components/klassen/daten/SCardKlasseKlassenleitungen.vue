<template>
	<svws-ui-content-card title="Klassenleitung">
		<svws-ui-data-table :columns="cols" :items="liste" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { KlassenDaten, LehrerListeEintrag} from "@core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import { PersonalTyp } from "@core";

	type Lehrer = {
		kuerzel?: string;
		nachname?: string;
		vorname?: string;
		typ?: string;
	}

	const props = defineProps<{
		data: KlassenDaten,
		mapLehrer: Map<number, LehrerListeEintrag>
	}>();

	const liste: ComputedRef<Lehrer[]> = computed(() => {
		if (props.data.klassenLeitungen === undefined)
			return [];
		return (props.data.klassenLeitungen?.toArray() as number[]).map((id) => {
			const lehrer = props.mapLehrer.get(id);
			if (lehrer === undefined)
				return {};
			return {
				kuerzel: lehrer.kuerzel,
				nachname: lehrer.nachname,
				vorname: lehrer.vorname,
				typ: PersonalTyp.fromBezeichnung(lehrer.personTyp)?.bezeichnung ?? undefined
			};
		});
	});

	const cols = [
		{ key: "kuerzel", label: "Kürzel", span: 1, sortable: false },
		{ key: "nachname", label: "Nachname", span: 2, sortable: false },
		{ key: "vorname", label: "Vorname", span: 2, sortable: false },
		{ key: "personTyp", label: "Typ", span: 1, sortable: false },
	];

</script>
