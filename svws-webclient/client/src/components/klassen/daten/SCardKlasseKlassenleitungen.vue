<template>
	<svws-ui-content-card title="Klassenleitung">
		<svws-ui-table :columns="cols" :items="liste" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { KlassenDaten, KlassenListeManager} from "@core";
	import type { DataTableColumn } from "@ui";
	import { computed } from "vue";
	import { PersonalTyp } from "@core";

	type Lehrer = {
		kuerzel?: string;
		nachname?: string;
		vorname?: string;
		typ?: string;
	}

	const props = defineProps<{
		patch: (data : Partial<KlassenDaten>) => Promise<void>;
		klassenListeManager: () => KlassenListeManager;
	}>();

	const liste = computed<Lehrer[]>(() => {
		const a = [];
		for (const id of props.klassenListeManager().daten().klassenLeitungen) {
			const lehrer = props.klassenListeManager().lehrer.get(id);
			if (lehrer)
				a.push({
					kuerzel: lehrer.kuerzel,
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
