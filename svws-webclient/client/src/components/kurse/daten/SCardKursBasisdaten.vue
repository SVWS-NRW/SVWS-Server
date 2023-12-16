<template>
	<svws-ui-content-card title="Allgemein">
		<template #actions>
			<svws-ui-checkbox v-model="istSichtbar"> Ist sichtbar </svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>patch({kuerzel})" type="text" />
			<svws-ui-select title="Lehrer" v-model="lehrer" :items="mapLehrer.values()" :item-text="l => l.kuerzel" />
			<svws-ui-multi-select title="Jahrgänge" v-model="jahrgaenge" :items="mapJahrgaenge" :item-text="jg => jg?.kuerzel ?? ''" />
			<svws-ui-text-input placeholder="Schuljahresabschnitt" :model-value="data.idSchuljahresabschnitt" @change="idSchuljahresabschnitt=>patch({idSchuljahresabschnitt: Number(idSchuljahresabschnitt)})" type="text" />
			<svws-ui-input-number placeholder="Fach-ID" :model-value="data.idFach" @change="idFach=>idFach && patch({idFach})" />
			<svws-ui-input-number placeholder="Sortierung" :model-value="data.sortierung" @change="sortierung=> sortierung && patch({sortierung})" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { JahrgangsListeEintrag, KursDaten, LehrerListeEintrag} from "@core";
	import { ArrayList } from "@core";

	const props = defineProps<{
		data: KursDaten;
		mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		patch: (data : Partial<KursDaten>) => Promise<void>;
	}>();

	const jahrgaenge = computed<JahrgangsListeEintrag[]>({
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
			const result = new ArrayList<number>();
			value.forEach(j => result.add(j.id));
			void props.patch({ idJahrgaenge: result });
		}
	});

	const lehrer = computed<LehrerListeEintrag | undefined>({
		get: () => ((props.data === undefined) || (props.data.lehrer === null)) ? undefined : props.mapLehrer.get(props.data.lehrer),
		set: (value) => void props.patch({lehrer: value === undefined ? null : value.id })
	});

	const istSichtbar = computed<boolean>({
		get: () => props.data === undefined ? false : props.data.istSichtbar,
		set: (value) => void props.patch({ istSichtbar: value })
	});

</script>
