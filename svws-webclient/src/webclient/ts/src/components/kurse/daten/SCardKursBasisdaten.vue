<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
				<svws-ui-text-input placeholder="Schuljahresabschnitt" v-model="schuljahresabschnitt" type="text" />
				<svws-ui-multi-select title="Jahrgaenge" v-model="jahrgaenge" tags :items="listJahrgaenge.liste"
					:item-text="(item: JahrgangsListeEintrag) => item?.kuerzel?.toString() || ''" />
				<svws-ui-text-input placeholder="Fach-ID" v-model="fach" type="number" />
				<svws-ui-multi-select title="Lehrer" v-model="lehrer" :items="listLehrer.liste" :item-text="(item:LehrerListeEintrag) => item.kuerzel.toString()" />
				<svws-ui-text-input placeholder="Sortierung" v-model="sortierung" type="number" />
				<svws-ui-checkbox v-model="istSichtbar"> Ist sichtbar </svws-ui-checkbox>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, WritableComputedRef } from "vue";

	import { JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataKurs } from "~/apps/kurse/DataKurs";
	import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";

	const { item, data, listLehrer, mapLehrer, listJahrgaenge, mapJahrgaenge } = defineProps<{ 
		item?: KursListeEintrag;
		data: DataKurs;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listLehrer: ListLehrer;
		mapLehrer: Map<Number, LehrerListeEintrag>;
	}>();

	const schuljahresabschnitt: WritableComputedRef<number | undefined> = computed({
		get: () => data.daten?.idSchuljahresabschnitt,
		set: (value) => data.patch({idSchuljahresabschnitt: value})
	});

	const kuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => data.daten?.kuerzel.toString(),
		set: (value) => data.patch({ kuerzel: value })
	});

	const jahrgaenge: WritableComputedRef<JahrgangsListeEintrag[]> = computed({
		get: () => data.daten === undefined ? [] : (data.daten.idJahrgaenge.toArray() as Number[]).map(id => mapJahrgaenge.get(id)).filter(j => j !== undefined) as JahrgangsListeEintrag[],
		set: (value) => { 
			const result: Vector<Number> = new Vector();
			value.forEach(j => result.add(Number(j.id)));
			data.patch({ idJahrgaenge: result });
		}
	});

	const fach: WritableComputedRef<number | undefined> = computed({
		get: () => data.daten?.idFach,
		set: (value) => data.patch({idFach: value})
	});

	const lehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => ((data.daten === undefined) || (data.daten.lehrer === null)) ? undefined : mapLehrer.get(data.daten.lehrer),
		set: (value) => data.patch({lehrer: value === undefined ? null : value.id })
	});

	const istSichtbar: WritableComputedRef<boolean> = computed({
		get: () => item === undefined ? false : item.istSichtbar,
		set: (value) => data.patch({ istSichtbar: value })
	});

	const sortierung: WritableComputedRef<number> = computed({
		get: () => item?.sortierung || 32000,
		set: (value) => data.patch({ sortierung: value })
	});

</script>
