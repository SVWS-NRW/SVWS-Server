<template>
	<tr :style="{ 'background-color': bgColor }">
		<td>
			<div class="flex gap-1">
				<template v-if="kurs.id === edit_name">
					{{ kursbezeichnung }}-
					<svws-ui-text-input :model-value="tmp_name" @update:model-value="tmp_name=String($event)" focus headless style="width: 2rem" @blur="edit_name=undefined" @keyup.enter="setSuffix" />
				</template>
				<template v-else>
					<span class="underline decoration-dashed underline-offset-2 cursor-pointer" @click="edit_name = kurs.id">
						{{ kursbezeichnung }}</span>
				</template>
			</div>
		</td>
		<td class="text-center cell--has-multiselect">
			<template v-if="allowRegeln">
				<svws-ui-multi-select :model-value="kurslehrer" @update:model-value="setKurslehrer($event as LehrerListeEintrag | undefined)" class="w-20" autocomplete :item-filter="lehrer_filter" removable headless
					:items="mapLehrer" :item-text="(l: LehrerListeEintrag)=> `${l.kuerzel}`" />
			</template>
			<template v-else>
				{{ kurslehrer?.kuerzel }}
			</template>
		</td>
		<td class="text-center">
			<svws-ui-checkbox headless v-if="allowRegeln" :model-value="kurs.istKoopKurs" @update:model-value="setKoop(Boolean($event))" />
			<svws-ui-icon v-else class="inline-block opacity-50">
				<i-ri-check-line v-if="kurs.istKoopKurs" />
				<i-ri-close-line v-else />
			</svws-ui-icon>
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
			:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager" :schueler-filter="schuelerFilter"
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
		:kurs="kurs" :kurse-mit-kursart="kurseMitKursart" :get-datenmanager="getDatenmanager" :map-lehrer="mapLehrer" :add-regel="addRegel"
		:add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer" />
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager,
		GostKursart, GostKursblockungRegelTyp, LehrerListeEintrag, Vector } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, Ref, ref } from "vue";
	import { lehrer_filter } from "~/helfer";
	import { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
		patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		hatErgebnis: boolean;
		kurs: GostBlockungKurs;
		bgColor: string;
		mapLehrer: Map<number, LehrerListeEintrag>;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		allowRegeln: boolean;
	}>();

	const edit_name: Ref<number | undefined> = ref(undefined);
	const tmp_name = ref(props.kurs.suffix);
	const kursdetail_anzeige: Ref<boolean> = ref(false)

	async function setKoop(value: boolean) {
		await props.patchKurs({ istKoopKurs: value }, props.kurs.id);
	}

	async function setSuffix() {
		await props.patchKurs({ suffix: tmp_name.value }, props.kurs.id);
		tmp_name.value = props.kurs.suffix;
		edit_name.value = undefined;
	}

	const kursbezeichnung: ComputedRef<string> = computed(() => props.getDatenmanager().getNameOfKurs(props.kurs.id))

	const kurslehrer: ComputedRef<LehrerListeEintrag | undefined> = computed(() => {
		const liste = props.getDatenmanager().getOfKursLehrkraefteSortiert(props.kurs.id);
		return liste.size() > 0 ? props.mapLehrer.get(liste.get(0).id) : undefined;
	});

	async function setKurslehrer(value: LehrerListeEintrag | undefined) {
		if (value !== undefined) {
			const lehrer = await props.addKursLehrer(props.kurs.id, value.id);
			if (lehrer === undefined)
				throw new Error("Fehler beim Anlegen des Kurslehrers");
			await add_lehrer_regel();
		} else {
			await remove_kurslehrer();
		}
	}

	async function remove_kurslehrer() {
		if (!props.kurs || !kurslehrer.value)
			return;
		await props.removeKursLehrer(props.kurs.id, kurslehrer.value.id);
	}

	const lehrer_regel: ComputedRef<GostBlockungRegel | undefined> = computed(()=> {
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
		const regeln = props.getDatenmanager().getMengeOfRegeln()
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
		await props.addRegel(r);
	}

	const anzahlSchienen: ComputedRef<number> = computed(() => props.getDatenmanager().getSchienenAnzahl());

	const kurs_blockungsergebnis: ComputedRef<GostBlockungsergebnisKurs | undefined> = computed(() =>
		props.hatErgebnis ? props.getErgebnismanager().getKursE(props.kurs.id) : undefined
	);

	const kurseMitKursart: ComputedRef<Vector<GostBlockungsergebnisKurs>> = computed(() => {
		const fachart = GostKursart.getFachartID(props.kurs.fach_id, props.kurs.kursart);
		return props.getErgebnismanager().getOfFachartKursmenge(fachart);
	});

	const filtered_by_kursart: ComputedRef<GostBlockungsergebnisKurs[]> = computed(() => {
		const kurse = props.getErgebnismanager().getOfFachKursmenge(props.kurs.fach_id);
		const arr = kurse.toArray(new Array<GostBlockungsergebnisKurs>())
		return arr.filter(k => k.kursart === props.kurs.kursart).sort((a, b) => {
			const a_name: string = props.getDatenmanager().getNameOfKurs(a.id);
			const b_name: string = props.getDatenmanager().getNameOfKurs(b.id);
			return a_name.localeCompare(b_name, "de-DE");
		})
	})

	const setze_kursdifferenz: ComputedRef<boolean> = computed(() => filtered_by_kursart.value[0] === kurs_blockungsergebnis.value);

	const kursdifferenz: ComputedRef<[number, number, number]> = computed(() => {
		if (!filtered_by_kursart.value.length)
			return [-1,-1, -1]
		const fachart_id = GostKursart.getFachartID(props.kurs.fach_id, props.kurs.kursart);
		const wahlen = props.getDatenmanager().getOfFachartMengeFachwahlen(fachart_id).size() || 0;
		if (filtered_by_kursart.value.length === 2)
			return [2, Math.abs(filtered_by_kursart.value[0].schueler.size() - filtered_by_kursart.value[1].schueler.size()), wahlen];
		const sorted = [...filtered_by_kursart.value].sort((a, b) => b.schueler.size() - a.schueler.size());
		return [filtered_by_kursart.value.length, sorted[0].schueler.size() - sorted[sorted.length - 1].schueler.size(), wahlen]
	});

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	const toggle_kursdetail_anzeige = () => kursdetail_anzeige.value = !kursdetail_anzeige.value

</script>
