<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
				<svws-ui-text-input placeholder="Schuljahresabschnitt" v-model="schuljahresabschnitt" type="text" />
				<svws-ui-multi-select title="Jahrgaenge" v-model="jahrgaenge" tags :items="listJahrgaenge.liste"
					:item-text="(jg: JahrgangsListeEintrag) => jg?.kuerzel ?? ''" />
				<svws-ui-text-input placeholder="Fach-ID" v-model="fach" type="number" />
				<svws-ui-multi-select title="Lehrer" v-model="lehrer" :items="listLehrer.liste" :item-text="(l: LehrerListeEintrag) => l.kuerzel" />
				<svws-ui-text-input placeholder="Sortierung" v-model="sortierung" type="number" />
				<svws-ui-checkbox v-model="istSichtbar"> Ist sichtbar </svws-ui-checkbox>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ShallowRef, WritableComputedRef } from "vue";
	import { JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataKurs } from "~/apps/kurse/DataKurs";
	import { ListJahrgaenge } from "~/apps/kataloge/jahrgaenge/ListJahrgaenge";

	const props = defineProps<{
		item: ShallowRef<KursListeEintrag | undefined>;
		data: DataKurs;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listLehrer: ListLehrer;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const schuljahresabschnitt: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.idSchuljahresabschnitt,
		set: (value) => void props.data.patch({idSchuljahresabschnitt: value})
	});

	const kuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.kuerzel,
		set: (value) => void props.data.patch({ kuerzel: value })
	});

	const jahrgaenge: WritableComputedRef<JahrgangsListeEintrag[]> = computed({
		get: () => props.data.daten === undefined ? [] : (props.data.daten.idJahrgaenge.toArray() as number[]).map(id => props.mapJahrgaenge.get(id)).filter(j => j !== undefined) as JahrgangsListeEintrag[],
		set: (value) => {
			const result: Vector<number> = new Vector();
			value.forEach(j => result.add(j.id));
			void props.data.patch({ idJahrgaenge: result });
		}
	});

	const fach: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.idFach,
		set: (value) => void props.data.patch({idFach: value})
	});

	const lehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => ((props.data.daten === undefined) || (props.data.daten.lehrer === null)) ? undefined : props.mapLehrer.get(props.data.daten.lehrer),
		set: (value) => void props.data.patch({lehrer: value === undefined ? null : value.id })
	});

	const istSichtbar: WritableComputedRef<boolean> = computed({
		get: () => props.data.daten === undefined ? false : props.data.daten.istSichtbar,
		set: (value) => void props.data.patch({ istSichtbar: value })
	});

	const sortierung: WritableComputedRef<number> = computed({
		get: () => props.data.daten?.sortierung || 32000,
		set: (value) => void props.data.patch({ sortierung: value })
	});

</script>
