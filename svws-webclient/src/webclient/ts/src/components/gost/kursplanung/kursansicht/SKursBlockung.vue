<template>
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
		<!-- eslint-disable-next-line vue/no-use-v-if-with-v-for-->
		<svws-ui-drop-data v-if="!blockung_aktiv"
			v-for="(schiene) in schienen"
			v-slot="{ active }"
			:key="schiene.id"
			class="text-center"
			:class="{'bg-yellow-200': drag_data.kurs?.id === kurs.id && drag_data.schiene?.id !== schiene.id, 'schiene-gesperrt': schiene_gesperrt(schiene)}"
			tag="td"
			@drop="drop_aendere_kursschiene($event, schiene)">
			<svws-ui-drag-data v-if="kurs_schiene_zugeordnet(schiene)"
				:key="kurs.id"
				tag="div"
				:data="{kurs, schiene}"
				class="select-none leading-5"
				:draggable="true"
				:class="{'schiene-gesperrt': schiene_gesperrt(schiene)}"
				:style="{ 'background-color': schiene_gesperrt(schiene)? '':bgColor}"
				@drag-start="drag_started"
				@drag-end="drag_ended">
				<svws-ui-badge size="tiny" class="cursor-grab" :type="selected_kurs ? 'primary' : fixier_regeln.length ? 'error' : active && drag_data?.kurs?.id !== kurs.id ? 'success' : 'highlight'" @click="toggle_active_kurs">
					{{ kurs_blockungsergebnis?.schueler.size() }}
					<svws-ui-icon class="cursor-pointer" @click="fixieren_regel_toggle">
						<i-ri-pushpin-fill v-if="fixier_regeln.length" class="inline-block" />
						<i-ri-pushpin-line v-if="!fixier_regeln.length && allowRegeln" class="inline-block" />
					</svws-ui-icon>
				</svws-ui-badge>
			</svws-ui-drag-data>
			<template v-else>
				<div class="cursor-pointer" @click="sperren_regel_toggle(schiene)"
					:class="{'bg-green-400': active && drag_data?.schiene?.id !== schiene.id && drag_data.kurs?.id === kurs.id && drag_data.schiene?.id !== schiene.id,
						'schiene-gesperrt': schiene_gesperrt(schiene)}">
					<svws-ui-icon>
						<i-ri-forbid-fill v-if="sperr_regeln.find(r=>r.parameter.get(1) === ermittel_parent_schiene(schiene).nummer)" class="inline-block text-red-500" />
						<i-ri-forbid-line v-if="allowRegeln && !sperr_regeln.find(r=>r.parameter.get(1) === ermittel_parent_schiene(schiene).nummer)" class="inline-block opacity-0 hover:opacity-25" />
					</svws-ui-icon>
				</div>
			</template>
		</svws-ui-drop-data>
		<template v-else v-for="schiene in schienen" :key="schiene.nummer">
			<td class="text-center leading-5 select-none">
				<svws-ui-badge v-if="kurs_schiene_zugeordnet(schiene)"
					size="tiny" :type="selected_kurs?'primary':'highlight'" class="cursor-pointer"
					@click="toggle_active_kurs">
					{{ kurs_blockungsergebnis?.schueler.size() }}
					<svws-ui-icon v-if="fixier_regeln.length">
						<i-ri-pushpin-fill class="inline-block" />
					</svws-ui-icon>
					<svws-ui-icon class="px-4 py-2" v-if="sperr_regeln.find(r=>r.parameter.get(1) === ermittel_parent_schiene(schiene).nummer)">
						<i-ri-forbid-fill class="inline-block text-red-500" />
					</svws-ui-icon>
				</svws-ui-badge>
			</td>
		</template>
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
	<s-gost-kursplanung-kursansicht-kurs-details v-if="kursdetail_anzeige" :bg-color="bgColor" :anzahl-spalten="6 + schienen.size()"
		:kurs="kurs" :kurse-mit-kursart="kurseMitKursart" :manager="blockung.datenmanager!" :map-lehrer="mapLehrer" :blockung="blockung" />
	<s-gost-kursplanung-kursansicht-modal-regel-kurse v-model="isModalOpen_KurseZusammen" :manager="blockung.datenmanager!"
		:kurs1-id="kurs1?.id" :kurs2-id="kurs.id" @regel-hinzufuegen="regelHinzufuegen" />
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungRegel, GostBlockungSchiene, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostBlockungsergebnisSchiene,
		GostFach, GostKursart, GostKursblockungRegelTyp, LehrerListeEintrag, List, Vector, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { lehrer_filter } from "~/helfer";
	import { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";

	const props = defineProps<{
		kurs: GostBlockungKurs
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

	const drag_data: Ref<{kurs: GostBlockungKurs|undefined; schiene: GostBlockungSchiene|undefined}> = ref({schiene: undefined, kurs: undefined})

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

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => props.blockung.ergebnismanager);

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
		if (kurs_id === undefined || !manager.value)
			return ""
		return manager.value.getOfKursName(kurs_id);
	}

	const schienen: ComputedRef<List<GostBlockungsergebnisSchiene>> = computed(() =>
		manager.value?.getMengeAllerSchienen() || new Vector<GostBlockungsergebnisSchiene>()
	);

	const kurs_blockungsergebnis: ComputedRef<GostBlockungsergebnisKurs|undefined> = computed(() => manager.value?.getKursE(props.kurs.id));

	const selected_kurs: ComputedRef<boolean> = computed(() => {
		const filter_kurs_id = props.schuelerFilter?.kurs?.value?.id;
		return (kurs_blockungsergebnis.value !== undefined) && (kurs_blockungsergebnis.value?.id === filter_kurs_id)
	});

	const kurseMitKursart: ComputedRef<Vector<GostBlockungsergebnisKurs>> = computed(() => {
		const fachart = GostKursart.getFachartID(props.kurs.fach_id, props.kurs.kursart);
		return manager.value?.getOfFachartKursmenge(fachart) || new Vector();
	});

	const filtered_by_kursart: ComputedRef<GostBlockungsergebnisKurs[]> = computed(() => {
		const kurse = manager.value?.getOfFachKursmenge(props.kurs.fach_id);
		if (!kurse)
			return [];
		const arr = kurse.toArray(new Array<GostBlockungsergebnisKurs>())
		return arr.filter(k => k.kursart === art.id).sort((a,b)=>{
			const a_name: string | undefined = manager.value?.getOfKursName(a.id)
			const b_name = manager.value?.getOfKursName(b.id)
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

	const regeln: ComputedRef<List<GostBlockungRegel>> = computed(() =>
		props.blockung.datenmanager?.getMengeOfRegeln() || new Vector<GostBlockungRegel>()
	);

	const sperr_regeln: ComputedRef<GostBlockungRegel[]> = computed(() => {
		const arr = []
		for (const regel of regeln.value)
			if (regel.typ === GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ && regel.parameter.get(0) === props.kurs.id)
				arr.push(regel)
		return arr;
	})

	const fixier_regeln: ComputedRef<GostBlockungRegel[]> = computed(() => {
		const arr = []
		for (const regel of regeln.value)
			if (regel.typ === GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ && regel.parameter.get(0) === props.kurs.id)
				arr.push(regel)
		return arr;
	});

	const blockung_aktiv: ComputedRef<boolean> = computed(() => (props.blockung.daten !== undefined) && props.blockung.daten?.istAktiv);

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "") as {kurs: GostBlockungKurs; schiene: GostBlockungSchiene} | undefined;
		if (!data)
			return;
		drag_data.value.kurs = data.kurs;
		drag_data.value.schiene = data.schiene;
	}

	function drag_ended() {
		drag_data.value.kurs = undefined;
		drag_data.value.schiene = undefined;
	}

	const schiene_gesperrt = (schiene: GostBlockungsergebnisSchiene): boolean => {
		for (const regel of regeln.value) {
			const { nummer } = ermittel_parent_schiene(schiene)
			if (regel.typ === GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ
				&& ((regel.parameter.get(0) !== props.kurs.kursart && (nummer >= regel.parameter.get(1) && nummer <= regel.parameter.get(2)))
					|| (regel.parameter.get(0) === props.kurs.kursart && (nummer < regel.parameter.get(1) || nummer > regel.parameter.get(2)))))
				return true;
			else if (regel.typ === GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ
				&& regel.parameter.get(0) === props.kurs.kursart
				&& (nummer >= regel.parameter.get(1) && nummer <= regel.parameter.get(2)))
				return true;
		}
		return false;
	}

	const ermittel_parent_schiene = (ergebnis_schiene: GostBlockungsergebnisSchiene): GostBlockungSchiene => {
		const schiene =	manager.value?.getSchieneG(ergebnis_schiene.id)
		if (!schiene)
			throw new Error("Schiene fehlt in der Definition")
		return schiene
	}

	const fixieren_regel_toggle = () => {
		if (props.blockung.pending || !props.allowRegeln)
			return;
		fixier_regeln.value.length ? fixieren_regel_entfernen() : fixieren_regel_hinzufuegen()
	}
	const fixieren_regel_hinzufuegen = () => {
		if (props.blockung.pending || !manager.value)
			return;
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		const kurs = kurs_blockungsergebnis.value
		if (!kurs)
			return;
		const schienen = manager.value.getOfKursSchienenmenge(kurs.id)
		if (!schienen)
			return;
		const schiene = manager.value.getSchieneG([...schienen][0].id)
		if (!schiene)
			return;
		regel.parameter.add(kurs.id);
		regel.parameter.add(schiene.nummer);
		void props.blockung.add_blockung_regel(regel)
	}

	const fixieren_regel_entfernen = () => {
		if (!fixier_regeln.value || props.blockung.pending)
			return;
		for (const regel of fixier_regeln.value)
			void props.blockung.del_blockung_regel(regel.id);
	}

	const sperren_regel_toggle = (schiene: GostBlockungsergebnisSchiene) => {
		if (props.blockung.pending || !props.allowRegeln)
			return;
		const { nummer } = ermittel_parent_schiene(schiene);
		return sperr_regeln.value.find(r=>r.parameter.get(1) === nummer)
			? sperren_regel_entfernen(nummer)
			: sperren_regel_hinzufuegen(nummer)
	}

	const sperren_regel_hinzufuegen = async (nummer: number) => {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		regel.parameter.add(props.kurs.id);
		regel.parameter.add(nummer);
		await props.blockung.add_blockung_regel(regel);
	}

	const sperren_regel_entfernen = async (nummer: number) => {
		if (!sperr_regeln.value.length)
			return
		const regel = sperr_regeln.value.find(r=>r.parameter.get(1) === nummer)
		if (!regel) return
		await props.blockung.del_blockung_regel(regel.id)
	}

	let kurs1: GostBlockungsergebnisKurs | undefined = undefined;
	function drop_aendere_kursschiene(drag_data: {kurs: GostBlockungsergebnisKurs; schiene: GostBlockungSchiene}, schiene: GostBlockungsergebnisSchiene) {
		if (!drag_data.kurs || !drag_data.schiene || props.ergebnis.pending || kurs_blockungsergebnis.value === undefined)
			return
		if (drag_data.kurs.id !== kurs_blockungsergebnis.value.id && kurs_schiene_zugeordnet(schiene)) {
			kurs1 = drag_data.kurs;
			isModalOpen_KurseZusammen.value = true;
			return;
		}
		if (drag_data.kurs.id === kurs_blockungsergebnis.value.id && schiene.id !== drag_data.schiene.id) {
			if (fixier_regeln.value) fixieren_regel_entfernen()
			void props.ergebnis.assignKursSchiene(drag_data.kurs.id, drag_data.schiene.id, schiene.id)
		}
	}

	function toggle_active_kurs() {
		if (props.schuelerFilter === undefined)
			return;
		if (props.schuelerFilter.kurs.value?.id !== props.kurs.id)
			props.schuelerFilter.kurs.value = props.kurs;
		else
			props.schuelerFilter.reset();
	}

	function kurs_schiene_zugeordnet(schiene: GostBlockungsergebnisSchiene): boolean {
		return manager.value?.getOfKursOfSchieneIstZugeordnet(props.kurs.id, schiene.id) || false
	}

	const toggle_kursdetail_anzeige = () => kursdetail_anzeige.value = !kursdetail_anzeige.value

	const isModalOpen_KurseZusammen: Ref<boolean> = ref(false);

	async function regelHinzufuegen(regel: GostBlockungRegel) {
		await props.blockung.add_blockung_regel(regel);
	}

</script>

<style lang="postcss">
	.schiene-gesperrt {
		background-image: url("/images/table-cell--stripes.svg");
		background-size: auto 100%;
	}
</style>