<template>
	<svws-ui-content-card title="Allgemein">
		<template #actions>
			<svws-ui-checkbox v-model="istSichtbar"> Ist sichtbar </svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>doPatch({kuerzel})" type="text" />
			<svws-ui-text-input placeholder="Schuljahresabschnitt" :model-value="data.idSchuljahresabschnitt" @change="idSchuljahresabschnitt=>doPatch({idSchuljahresabschnitt: Number(idSchuljahresabschnitt)})" type="text" />
			<svws-ui-multi-select title="Jahrgänge" v-model="jahrgaenge" tags :items="mapJahrgaenge"
				:item-text="(jg: JahrgangsListeEintrag) => jg?.kuerzel ?? ''" />
			<svws-ui-text-input placeholder="Fach-ID" :model-value="data.idFach" @change="idFach=>doPatch({idFach: Number(idFach)})" type="number" />
			<svws-ui-multi-select title="Lehrer" v-model="lehrer" :items="mapLehrer.values()" :item-text="(l: LehrerListeEintrag) => l.kuerzel" />
			<svws-ui-text-input placeholder="Sortierung" :model-value="data.sortierung" @change="sortierung=>doPatch({sortierung: Number(sortierung)})" type="number" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { JahrgangsListeEintrag, KursDaten, LehrerListeEintrag} from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { ArrayList } from "@core";

	const props = defineProps<{
		data: KursDaten;
		mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<KursDaten>): void;
	}>()

	function doPatch(data: Partial<KursDaten>) {
		emit('patch', data);
	}

	const jahrgaenge: WritableComputedRef<JahrgangsListeEintrag[]> = computed({
		get: () => {
			const arr = [];
			for (const id of props.data.idJahrgaenge) {
				const e = props.mapJahrgaenge.get(id);
				if (e !== undefined)
					arr.push(e);
			}
			return arr;
		},
		set: (value) => {
			const result: ArrayList<number> = new ArrayList();
			value.forEach(j => result.add(j.id));
			doPatch({ idJahrgaenge: result });
		}
	});

	const lehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => ((props.data === undefined) || (props.data.lehrer === null)) ? undefined : props.mapLehrer.get(props.data.lehrer),
		set: (value) => doPatch({lehrer: value === undefined ? null : value.id })
	});

	const istSichtbar: WritableComputedRef<boolean> = computed({
		get: () => props.data === undefined ? false : props.data.istSichtbar,
		set: (value) => doPatch({ istSichtbar: value })
	});

</script>
