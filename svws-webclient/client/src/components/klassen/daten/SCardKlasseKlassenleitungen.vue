<template>
	<svws-ui-content-card title="Klassenleitung">
		<svws-ui-table :columns="cols" :items="liste" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { KlassenDaten, LehrerListeEintrag} from "@core";
	import { computed } from "vue";
	import { PersonalTyp } from "@core";
	import type { DataTableColumn } from "@ui";

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

	const liste = computed<Lehrer[]>(() => {
		const a = [];
		for (const id of props.data.klassenLeitungen) {
			const lehrer = props.mapLehrer.get(id);
			if (lehrer)
				a.push(
					{ kuerzel: lehrer.kuerzel,
						nachname: lehrer.nachname,
						vorname: lehrer.vorname,
						typ: PersonalTyp.fromBezeichnung(lehrer.personTyp)?.bezeichnung ?? undefined
					});
		}
		return a;
	});

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", span: 1, sortable: false },
		{ key: "nachname", label: "Nachname", span: 2, sortable: false },
		{ key: "vorname", label: "Vorname", span: 2, sortable: false },
		{ key: "personTyp", label: "Typ", span: 1, sortable: false },
	];

</script>
