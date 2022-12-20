<template>
	<tr :style="{'background-color': bgColor}">
			<td><span class="w-20">{{kursbezeichnung}}</span></td>
			<td>
				<div class="flex flex-col gap-2">
					<div class="flex flex-row gap-1" v-for="lehrer, i of kurslehrer" :key="lehrer.id">
						<svws-ui-multi-select :modelValue="lehrer" @update:modelValue="(val: LehrerListeEintrag|undefined) => update_kurslehrer(val, lehrer)" class="w-52"
							autocomplete :item-filter="lehrer_filter" :items="lehrer_liste" removable
							:item-text="(l: LehrerListeEintrag)=> `${l.nachname}, ${l.vorname} (${l.kuerzel})`"/>
						<svws-ui-icon v-if="!new_kurs_lehrer && i === kurslehrer.length-1" class="cursor-pointer text-black" @click="new_kurs_lehrer=true"><i-ri-user-add-line /></svws-ui-icon>
					</div>
					<div v-if="!kurslehrer.length || new_kurs_lehrer">
						<svws-ui-multi-select
						:modelValue="undefined" @update:modelValue="update_kurslehrer" class="w-52" autocomplete :item-filter="lehrer_filter" :items="lehrer_liste" :item-text="(l: LehrerListeEintrag)=> `${l.nachname}, ${l.vorname} (${l.kuerzel})`"/>
					</div>
				</div>
			</td>
			<td>tu was</td>
	</tr>
</template>

<script setup lang="ts">
	import { GostBlockungKurs, GostBlockungRegel, GostBlockungsergebnisManager, GostFach, GostKursblockungRegelTyp, LehrerListeEintrag, ZulaessigesFach } from '@svws-nrw/svws-core-ts';
	import { ComputedRef, computed, Ref, ref } from 'vue';
	import { App } from '~/apps/BaseApp';
	import { injectMainApp, Main } from '~/apps/Main';
	import { lehrer_filter } from '~/helfer';

	const main: Main = injectMainApp();
	const app = main.apps.gost

	const props = defineProps({ kurs: { type: Object as () => GostBlockungKurs, required: true } });
	const new_kurs_lehrer: Ref<boolean> = ref(false);

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(()=> app.dataKursblockung.ergebnismanager);

const gostFach: ComputedRef<GostFach | null> =
	computed(() => {
		let fach: GostFach | null = null
		if (!app.dataFaecher.manager) return null
		for (const f of app.dataFaecher.manager.values())
			if (f.id === props.kurs.fach_id) {
				fach = f
				break
			}
		return fach;
	});

const fach: ComputedRef<ZulaessigesFach> =
	computed(() => ZulaessigesFach.getByKuerzelASD(gostFach.value?.kuerzel || null));

const bgColor: ComputedRef<string> =
	computed(() => fach.value ? fach.value.getHMTLFarbeRGB().valueOf() : "#ffffff");

	const kursbezeichnung: ComputedRef<String> =
		computed(()=> manager.value?.getOfKursName(props.kurs.id)||"")

	const kurslehrer: ComputedRef<LehrerListeEintrag[]> =
		computed(()=> {
			if (!app.dataKursblockung.datenmanager )
				return [];
			const liste = app.dataKursblockung.datenmanager.getOfKursLehrkraefteSortiert(props.kurs.id);
				const lehrer = new Set();
				for (const l of liste)
					lehrer.add(l.id)
				return App.apps.lehrer.auswahl.liste.filter(l=> lehrer.has(l.id));
		})

	const lehrer_liste: ComputedRef<LehrerListeEintrag[]> =
		computed(()=>{
			const vergeben = new Set();
			for (const l of kurslehrer.value)
				vergeben.add(l.id);
			return main.apps.lehrer.auswahl.liste.filter(l=>!vergeben.has(l.id));
		})

	function remove_kurslehrer(lehrer: LehrerListeEintrag) {
		if (!app.dataKursblockung.datenmanager )
			return;
		app.dataKursblockung.del_blockung_lehrer(props.kurs.id, lehrer.id);
		app.dataKursblockung.datenmanager.patchOfKursRemoveLehrkraft(props.kurs.id, lehrer.id);
	}

	async function update_kurslehrer(lehrer: LehrerListeEintrag|undefined, lehrer_alt: LehrerListeEintrag) {
		if (!app.dataKursblockung.datenmanager)
			return;
		if (lehrer === undefined) {
			remove_kurslehrer(lehrer_alt);
			return;
		}
		const kurslehrer = await app.dataKursblockung.add_blockung_lehrer(props.kurs.id, lehrer.id);
		if (!kurslehrer)
			throw new Error("Fehler beim Anlegen des Kurslehrers");
		add_lehrer_regel();
		app.dataKursblockung.datenmanager.patchOfKursAddLehrkraft(props.kurs.id, kurslehrer);
		new_kurs_lehrer.value = false;
	}
	
	const lehrer_regel: ComputedRef<GostBlockungRegel | undefined> =
		computed(()=> {
			const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
			const regeln = app.dataKursblockung.datenmanager?.getMengeOfRegeln()
			if (!regeln)
				return undefined;
			for (const r of regeln)
				if (r.typ === regel_typ.typ)
					return r;
		})

	function add_lehrer_regel() {
		if (lehrer_regel.value !== undefined)
			return;
		const r = new GostBlockungRegel();
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
		r.typ = regel_typ.typ;
		r.parameter.add(1);
		app.dataKursblockung.add_blockung_regel(r);
	}
</script>