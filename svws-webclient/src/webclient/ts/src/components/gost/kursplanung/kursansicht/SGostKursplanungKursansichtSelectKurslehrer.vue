<template>
	<div class="flex flex-col gap-2 text-left">
		<div class="flex w-full items-center" v-for="lehrer, i of kurslehrer" :key="lehrer.id">
			<svws-ui-multi-select :model-value="lehrer" @update:model-value="(val) => update_kurslehrer(val, lehrer)" class="flex-1"
				autocomplete :item-filter="lehrer_filter" :items="lehrer_liste" removable
				:item-text="(l: LehrerListeEintrag)=> `${l.nachname}, ${l.vorname} (${l.kuerzel})`" />
			<svws-ui-icon v-if="!new_kurs_lehrer && i === kurslehrer.length-1" class="ml-3 cursor-pointer text-black hover:text-primary" @click="new_kurs_lehrer=true">
				<i-ri-user-add-line />
			</svws-ui-icon>
		</div>
		<div v-if="!kurslehrer.length || new_kurs_lehrer">
			<svws-ui-multi-select
				:model-value="undefined" @update:model-value="update_kurslehrer" class="flex-1" autocomplete :item-filter="lehrer_filter" :items="lehrer_liste" :item-text="(l: LehrerListeEintrag)=> `${l.nachname}, ${l.vorname} (${l.kuerzel})`" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungRegel, GostBlockungsdatenManager, GostKursblockungRegelTyp, LehrerListeEintrag } from '@svws-nrw/svws-core-ts';
	import { ComputedRef, computed, Ref, ref } from 'vue';
	import { DataGostKursblockung } from '~/apps/gost/DataGostKursblockung';
	import { lehrer_filter } from '~/helfer';

	const props = defineProps<{
		kurs: GostBlockungKurs;
		manager: GostBlockungsdatenManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		blockung: DataGostKursblockung;
	}>();

	const new_kurs_lehrer: Ref<boolean> = ref(false);

	const kurslehrer: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		const liste = props.manager.getOfKursLehrkraefteSortiert(props.kurs.id);
		const lehrer = new Set();
		for (const l of liste)
			lehrer.add(l.id)
		const result = [];
		for (const l of props.mapLehrer.values())
			if (lehrer.has(l.id))
				result.push(l);
		return result;
	})

	const lehrer_liste: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		const vergeben = new Set();
		for (const l of kurslehrer.value)
			vergeben.add(l.id);
		const result = [];
		for (const l of props.mapLehrer.values())
			if (!vergeben.has(l.id))
				result.push(l);
		return result;
	})

	async function remove_kurslehrer(lehrer: LehrerListeEintrag) {
		await props.blockung.del_blockung_lehrer(props.kurs.id, lehrer.id);
		props.manager.patchOfKursRemoveLehrkraft(props.kurs.id, lehrer.id);
	}

	async function update_kurslehrer(lehrer: unknown, lehrer_alt?: LehrerListeEintrag) {
		if ((lehrer === undefined) && lehrer_alt) {
			await remove_kurslehrer(lehrer_alt);
			return;
		}
		if (lehrer instanceof LehrerListeEintrag) {
			const kurslehrer = await props.blockung.add_blockung_lehrer(props.kurs.id, lehrer.id);
			if (!kurslehrer)
				throw new Error("Fehler beim Anlegen des Kurslehrers");
			await add_lehrer_regel();
			props.manager.patchOfKursAddLehrkraft(props.kurs.id, kurslehrer);
			new_kurs_lehrer.value = false;
		}
	}

	const lehrer_regel: ComputedRef<GostBlockungRegel | undefined> = computed(() => {
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN;
		const regeln = props.manager.getMengeOfRegeln();
		if (!regeln)
			return undefined;
		for (const r of regeln)
			if (r.typ === regel_typ.typ)
				return r;
		return undefined;
	})

	async function add_lehrer_regel() {
		if (lehrer_regel.value !== undefined)
			return;
		const r = new GostBlockungRegel();
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
		r.typ = regel_typ.typ;
		r.parameter.add(1);
		await props.blockung.add_blockung_regel(r);
	}

</script>
