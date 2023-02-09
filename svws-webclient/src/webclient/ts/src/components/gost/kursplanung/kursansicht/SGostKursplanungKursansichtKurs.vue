<template>
	<template v-if="ergebnismanager !== undefined">
		<tr :style="{ 'background-color': bgColor }">
			<td>
				<div class="flex gap-1">
					<template v-if="kurs === edit_name">
						{{ kursbezeichnung }}-
						<svws-ui-text-input v-model="suffix" focus headless style="width: 2rem" @blur="edit_name=undefined" @keyup.enter="edit_name=undefined" />
					</template>
					<template v-else>
						<span class="underline decoration-dashed underline-offset-2 cursor-text" @click="edit_name = kurs">
							{{ kursbezeichnung }}</span>
					</template>
				</div>
			</td>
			<td class="text-center cell--has-multiselect">
				<template v-if="allowRegeln">
					<svws-ui-multi-select v-model="kurslehrer" class="w-20" autocomplete :item-filter="lehrer_filter" removable headless
						:items="listLehrer.liste" :item-text="(l: LehrerListeEintrag)=> `${l.kuerzel}`" />
				</template>
				<template v-else>
					{{ kurslehrer?.kuerzel }}
				</template>
			</td>
			<td class="text-center">
				<svws-ui-checkbox headless v-if="allowRegeln" v-model="koop" />
				<template v-else>{{ koop ? "&#x2713;" : "&#x2717;" }}</template>
			</td>
			<template v-if="setze_kursdifferenz && kurs_blockungsergebnis">
				<td class="text-center blockung--kursdifferenz" :rowspan="kursdifferenz[0] + (kursdetail_anzeige ? 1:0)">
					{{ kursdifferenz[2] }}
				</td>
				<td class="text-center blockung--kursdifferenz" :rowspan="kursdifferenz[0] + (kursdetail_anzeige ? 1:0)">{{ kursdifferenz[1] }}</td>
			</template>
			<template v-if="!kurs_blockungsergebnis">
				<td />
			</template>
			<s-gost-kursplanung-kursansicht-kurs-schienen :blockung-aktiv="blockung_aktiv" :allow-regeln="allowRegeln" :kurs="kurs" :bg-color="bgColor"
				:datenmanager="blockung.datenmanager!" :ergebnismanager="ergebnismanager" :schueler-filter="schuelerFilter"
				:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung" />
			<template v-if="allowRegeln">
				<td class="cursor-pointer text-center hover:opacity-100" :class="{'opacity-100' : kursdetail_anzeige, 'opacity-25' : !kursdetail_anzeige}" @click="toggle_kursdetail_anzeige" title="Kursdetails anzeigen">
					<div class="inline-block">
						<i-ri-arrow-up-s-line v-if="kursdetail_anzeige" class="relative top-0.5" />
						<i-ri-arrow-down-s-line v-else class="relative top-0.5" />
					</div>
				</td>
			</template>
		</tr>
		<!--Wenn Kursdtails angewählt sind, erscheint die zusätzliche Zeile-->
		<s-gost-kursplanung-kursansicht-kurs-details v-if="kursdetail_anzeige" :bg-color="bgColor" :anzahl-spalten="6 + anzahlSchienen"
			:kurs="kurs" :kurse-mit-kursart="kurseMitKursart" :manager="blockung.datenmanager!" :map-lehrer="mapLehrer" :blockung="blockung" />
	</template>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungRegel, GostBlockungsergebnisKurs, GostBlockungsergebnisManager,
		GostFach, GostKursart, GostKursblockungRegelTyp, LehrerListeEintrag, Vector, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { lehrer_filter } from "~/helfer";
	import { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<void>;
		removeRegel: (id: number) => Promise<void>;
		updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<void>;
		kurs: GostBlockungKurs;
		dataFaecher: DataGostFaecher;
		blockung: DataGostKursblockung;
		ergebnis: DataGostKursblockungsergebnis;
		listLehrer: ListLehrer;
		mapLehrer: Map<number, LehrerListeEintrag>;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		allowRegeln: boolean;
	}>();

	const art = GostKursart.fromID(props.kurs.kursart)

	const edit_name: Ref<GostBlockungKurs | undefined> = ref(undefined)
	const kursdetail_anzeige: Ref<boolean> = ref(false)

	const gostFach: ComputedRef<GostFach | null> = computed(() => {
		let fach: GostFach | null = null;
		if (!props.dataFaecher.manager)
			return null;
		for (const f of props.dataFaecher.manager.values())
			if (f.id === props.kurs.fach_id) {
				fach = f
				break
			}
		return fach;
	});

	const fach: ComputedRef<ZulaessigesFach> = computed(() => ZulaessigesFach.getByKuerzelASD(gostFach.value?.kuerzel || null));

	const bgColor: ComputedRef<string> = computed(() => fach.value ? fach.value.getHMTLFarbeRGB() : "#ffffff");

	const koop: WritableComputedRef<boolean> = computed({
		get: () => props.kurs.istKoopKurs,
		set: (value) => {
			const k = props.blockung.datenmanager?.getKurs(props.kurs.id)
			if (!k)
				return;
			props.blockung.patch_kurs(k.id, { istKoopKurs: value })
				.then(() => k.istKoopKurs = value)
				.catch(error => { throw error });
		}
	});

	const suffix: WritableComputedRef<string> = computed({
		get: () => props.kurs.suffix,
		set: (value) => {
			const k = props.blockung.datenmanager?.getKurs(props.kurs.id)
			if (k === undefined)
				return;
			void props.blockung.patch_kurs(k.id, { suffix: value });
		}
	});

	const ergebnismanager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => props.blockung.ergebnismanager);

	const kursbezeichnung: ComputedRef<String> = computed(() => get_kursbezeichnung(props.kurs.id));

	const kurslehrer: WritableComputedRef<LehrerListeEintrag|undefined> = computed({
		get: () => {
			if (!props.blockung.datenmanager || !props.kurs)
				return;
			const liste = props.blockung.datenmanager.getOfKursLehrkraefteSortiert(props.kurs.id);
			return liste.size() > 0 ? props.mapLehrer.get(liste.get(0).id) : undefined;
		},
		set: (value) => {
			if (!props.kurs)
				return;
			if (value !== undefined) {
				props.blockung.add_blockung_lehrer(props.kurs.id, value.id)
					.then(lehrer => {
						if (!lehrer || !props.blockung.datenmanager || !props.kurs)
							throw new Error("Fehler beim Anlegen des Kurslehrers");
						props.blockung.datenmanager.patchOfKursAddLehrkraft(props.kurs.id, lehrer);
						add_lehrer_regel();
					})
					.catch(error => { throw error });
			} else
				void remove_kurslehrer();
		}
	});

	async function remove_kurslehrer() {
		if (!props.blockung.datenmanager || !props.kurs || !kurslehrer.value)
			return;
		await props.blockung.del_blockung_lehrer(props.kurs.id, kurslehrer.value.id);
		props.blockung.datenmanager.patchOfKursRemoveLehrkraft(props.kurs.id, kurslehrer.value.id);
	}

	const lehrer_regel: ComputedRef<GostBlockungRegel | undefined> = computed(()=> {
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

	function get_kursbezeichnung(kurs_id: number | undefined): string {
		if (kurs_id === undefined || !ergebnismanager.value)
			return ""
		return ergebnismanager.value.getOfKursName(kurs_id);
	}

	const anzahlSchienen: ComputedRef<number> = computed(() => ergebnismanager.value?.getOfSchieneAnzahl() || 0);

	const kurs_blockungsergebnis: ComputedRef<GostBlockungsergebnisKurs|undefined> = computed(() => ergebnismanager.value?.getKursE(props.kurs.id));

	const kurseMitKursart: ComputedRef<Vector<GostBlockungsergebnisKurs>> = computed(() => {
		const fachart = GostKursart.getFachartID(props.kurs.fach_id, props.kurs.kursart);
		return ergebnismanager.value?.getOfFachartKursmenge(fachart) || new Vector();
	});

	const filtered_by_kursart: ComputedRef<GostBlockungsergebnisKurs[]> = computed(() => {
		const kurse = ergebnismanager.value?.getOfFachKursmenge(props.kurs.fach_id);
		if (!kurse)
			return [];
		const arr = kurse.toArray(new Array<GostBlockungsergebnisKurs>())
		return arr.filter(k => k.kursart === art.id).sort((a,b)=>{
			const a_name: string | undefined = ergebnismanager.value?.getOfKursName(a.id)
			const b_name = ergebnismanager.value?.getOfKursName(b.id)
			return a_name?.localeCompare(b_name ?? '', "de-DE") || 0
		})
	})

	const setze_kursdifferenz: ComputedRef<boolean> = computed(() => filtered_by_kursart.value[0] === kurs_blockungsergebnis.value);

	const kursdifferenz: ComputedRef<[number, number, number]> = computed(() => {
		if (!filtered_by_kursart.value.length)
			return [-1,-1, -1]
		const fachart_id = GostKursart.getFachartID(props.kurs.fach_id, props.kurs.kursart);
		const wahlen = props.blockung.datenmanager?.getOfFachartMengeFachwahlen(fachart_id).size() || 0;
		if (filtered_by_kursart.value.length === 2)
			return [2, Math.abs(filtered_by_kursart.value[0].schueler.size() - filtered_by_kursart.value[1].schueler.size()), wahlen];
		const sorted = [...filtered_by_kursart.value].sort((a, b) => b.schueler.size() - a.schueler.size());
		return [filtered_by_kursart.value.length, sorted[0].schueler.size() - sorted[sorted.length - 1].schueler.size(), wahlen]
	});

	const blockung_aktiv: ComputedRef<boolean> = computed(() => (props.blockung.daten !== undefined) && props.blockung.daten?.istAktiv);

	const toggle_kursdetail_anzeige = () => kursdetail_anzeige.value = !kursdetail_anzeige.value

</script>
