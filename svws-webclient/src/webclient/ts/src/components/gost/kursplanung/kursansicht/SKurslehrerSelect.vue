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

	import { GostBlockungKurs, GostBlockungRegel, GostBlockungsergebnisManager, GostFach, GostKursblockungRegelTyp, LehrerListeEintrag, ZulaessigesFach } from '@svws-nrw/svws-core-ts';
	import { ComputedRef, computed, Ref, ref } from 'vue';
	import { DataGostFaecher } from '~/apps/gost/DataGostFaecher';
	import { DataGostKursblockung } from '~/apps/gost/DataGostKursblockung';
	import { ListLehrer } from '~/apps/lehrer/ListLehrer';
	import { lehrer_filter } from '~/helfer';

	const props = defineProps<{
		kurs: GostBlockungKurs;
		dataFaecher: DataGostFaecher;
		blockung: DataGostKursblockung;
		listLehrer: ListLehrer;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const new_kurs_lehrer: Ref<boolean> = ref(false);

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => props.blockung.ergebnismanager);

	const gostFach: ComputedRef<GostFach | null> = computed(() => {
		let fach: GostFach | null = null
		if (!props.dataFaecher.manager)
			return null
		for (const f of props.dataFaecher.manager.values())
			if (f.id === props.kurs.fach_id) {
				fach = f
				break
			}
		return fach;
	});

	const fach: ComputedRef<ZulaessigesFach> = computed(() => ZulaessigesFach.getByKuerzelASD(gostFach.value?.kuerzel || null));

	const bgColor: ComputedRef<string> = computed(() => fach.value ? fach.value.getHMTLFarbeRGB() : "#ffffff");

	const kursbezeichnung: ComputedRef<String> = computed(()=> manager.value?.getOfKursName(props.kurs.id) || "")

	const kurslehrer: ComputedRef<LehrerListeEintrag[]> = computed(()=> {
		if (!props.blockung.datenmanager )
			return [];
		const liste = props.blockung.datenmanager.getOfKursLehrkraefteSortiert(props.kurs.id);
		const lehrer = new Set();
		for (const l of liste)
			lehrer.add(l.id)
		return props.listLehrer.liste.filter(l => lehrer.has(l.id));
	})

	const lehrer_liste: ComputedRef<LehrerListeEintrag[]> = computed(()=>{
		const vergeben = new Set();
		for (const l of kurslehrer.value)
			vergeben.add(l.id);
		return props.listLehrer.liste.filter(l => !vergeben.has(l.id));
	})

	async function remove_kurslehrer(lehrer: LehrerListeEintrag) {
		if (!props.blockung.datenmanager )
			return;
		await props.blockung.del_blockung_lehrer(props.kurs.id, lehrer.id);
		props.blockung.datenmanager.patchOfKursRemoveLehrkraft(props.kurs.id, lehrer.id);
	}

	async function update_kurslehrer(lehrer: unknown, lehrer_alt?: LehrerListeEintrag) {
		if (!props.blockung.datenmanager)
			return;
		if ((lehrer === undefined) && lehrer_alt) {
			await remove_kurslehrer(lehrer_alt);
			return;
		}
		if (lehrer instanceof LehrerListeEintrag) {
			const kurslehrer = await props.blockung.add_blockung_lehrer(props.kurs.id, lehrer.id);
			if (!kurslehrer)
				throw new Error("Fehler beim Anlegen des Kurslehrers");
			add_lehrer_regel();
			props.blockung.datenmanager.patchOfKursAddLehrkraft(props.kurs.id, kurslehrer);
			new_kurs_lehrer.value = false;
		}
	}

	const lehrer_regel: ComputedRef<GostBlockungRegel | undefined> = computed(() => {
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
		const regeln = props.blockung.datenmanager?.getMengeOfRegeln()
		if (!regeln)
			return undefined;
		for (const r of regeln)
			if (r.typ === regel_typ.typ)
				return r;
		return undefined;
	})

	function add_lehrer_regel() {
		if (lehrer_regel.value !== undefined)
			return;
		const r = new GostBlockungRegel();
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
		r.typ = regel_typ.typ;
		r.parameter.add(1);
		void props.blockung.add_blockung_regel(r);
	}

</script>
