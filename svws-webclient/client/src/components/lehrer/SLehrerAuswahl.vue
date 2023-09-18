<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Lehrkräfte</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" @update:clicked="gotoLehrer" v-model="selectedItems" :items="rowsFiltered.values()"
				:columns="cols" clickable selectable count>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="personaltyp" :items="PersonalTyp.values()" :item-text="p => p.bezeichnung" title="Personaltyp" removable class="col-span-full" />
					<div class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="sichtbar">Sichtbar</svws-ui-checkbox>
						<svws-ui-checkbox type="toggle" v-model="statistikrelevant">Statistikrelevant</svws-ui-checkbox>
					</div>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { LehrerListeEintrag} from "@core";
	import type { LehrerAuswahlProps } from "./SLehrerAuswahlProps";
	import type { ComputedRef, Ref} from "vue";
	import { computed, ref } from "vue";
	import { PersonalTyp } from "@core";

	const props = defineProps<LehrerAuswahlProps>();

	const cols = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", sortable: true, span: 2 }
	];

	const actions = [
		{ label: "Löschen", action: "delete" },
		{ label: "Kopieren", action: "copy" }
	];

	const search: Ref<string> = ref("");
	const selectedItems = ref([]);
	const sichtbar: Ref<boolean> = ref(true);
	const statistikrelevant: Ref<boolean> = ref(true);
	const personaltyp: Ref<PersonalTyp | undefined> = ref(undefined);

	const rowsFiltered: ComputedRef<Map<number, LehrerListeEintrag>> = computed(() => {
		const result = new Map<number, LehrerListeEintrag>();
		for (const l of props.mapLehrer.values()) {
			let pt = true;
			if (personaltyp.value)
				pt = personaltyp.value.kuerzel === l.personTyp;
			if ((l.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| l.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| l.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				&& l.istSichtbar === sichtbar.value
				&& l.istRelevantFuerStatistik === statistikrelevant.value
				&& pt)
				result.set(l.id, l);
		}
		return result;
	});

	function onAction(action: string, item: LehrerListeEintrag) {
		console.log(action, item);
	}

</script>
