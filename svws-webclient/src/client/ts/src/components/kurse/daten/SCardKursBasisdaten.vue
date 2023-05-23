<template>
	<svws-ui-content-card title="Basisdaten">
		<template #actions>
			<svws-ui-checkbox v-model="istSichtbar"> Ist sichtbar </svws-ui-checkbox>
		</template>
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
			<svws-ui-text-input placeholder="Schuljahresabschnitt" v-model="schuljahresabschnitt" type="text" />
			<svws-ui-multi-select title="Jahrgaenge" v-model="jahrgaenge" tags :items="mapJahrgaenge"
				:item-text="(jg: JahrgangsListeEintrag) => jg?.kuerzel ?? ''" />
			<svws-ui-text-input placeholder="Fach-ID" v-model="fach" type="number" />
			<svws-ui-multi-select title="Lehrer" v-model="lehrer" :items="mapLehrer.values()" :item-text="(l: LehrerListeEintrag) => l.kuerzel" />
			<svws-ui-text-input placeholder="Sortierung" v-model="sortierung" type="number" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { JahrgangsListeEintrag, KursDaten, LehrerListeEintrag} from "@svws-nrw/svws-core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { ArrayList } from "@svws-nrw/svws-core";

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

	const schuljahresabschnitt: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.idSchuljahresabschnitt,
		set: (value) => doPatch({idSchuljahresabschnitt: value})
	});

	const kuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.kuerzel,
		set: (value) => doPatch({ kuerzel: value })
	});

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

	const fach: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.idFach,
		set: (value) => doPatch({idFach: value})
	});

	const lehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => ((props.data === undefined) || (props.data.lehrer === null)) ? undefined : props.mapLehrer.get(props.data.lehrer),
		set: (value) => doPatch({lehrer: value === undefined ? null : value.id })
	});

	const istSichtbar: WritableComputedRef<boolean> = computed({
		get: () => props.data === undefined ? false : props.data.istSichtbar,
		set: (value) => doPatch({ istSichtbar: value })
	});

	const sortierung: WritableComputedRef<number> = computed({
		get: () => props.data.sortierung || 32000,
		set: (value) => doPatch({ sortierung: value })
	});

</script>
