<template>
	<template v-if="kurszahlen.get(kursart.id) === 0 && wahlen.get(kursart.id) && allowRegeln">
		<tr class="text-left" :style="{ 'background-color': bgColor }" :key="kursart.id">
			<td colspan="3">
				{{ fach.kuerzel }}-{{ kursart.kuerzel }}
			</td>
			<td class="text-center cursor-pointer hover:bg-yellow-200" colspan="1" @click="toggle_active_fachwahl">
				{{ wahlen.get(kursart.id) }}
			</td>
			<td :colspan="schienen.size()+2">
				<svws-ui-button class="" type="secondary" size="small" @click="add_kurs(kursart)" title="Kurs hinzufügen">Kurs hinzufügen</svws-ui-button>
			</td>
		</tr>
	</template>
	<template v-else>
		<s-gost-kursplanung-kursansicht-kurs v-for="kurs in vorhandene_kurse(kursart)" :key="kurs.id" :kurs="kurs" :bg-color="bgColor"
			:map-lehrer="mapLehrer" :allow-regeln="allowRegeln" :schueler-filter="schuelerFilter" :get-datenmanager="getDatenmanager"
			:hat-ergebnis="hatErgebnis" :get-ergebnismanager="getErgebnismanager"
			:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
			:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer"
			:add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" />
	</template>
</template>

<script setup lang="ts">

	import type { List, GostBlockungKurs, GostBlockungSchiene, GostStatistikFachwahl, GostFach, LehrerListeEintrag, GostBlockungRegel, GostFaecherManager, GostBlockungKursLehrer,
		GostBlockungsergebnisManager, GostBlockungsdatenManager } from "@svws-nrw/svws-core";
	import { GostKursart, GostStatistikFachwahlHalbjahr,
		ZulaessigesFach } from "@svws-nrw/svws-core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { Config } from "~/components/Config";
	import type { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";

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
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		config: Config;
		hatErgebnis: boolean;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		fach: GostStatistikFachwahl;
		faecherManager: GostFaecherManager;
		kursart: GostKursart;
		halbjahr: number;
		mapLehrer: Map<number, LehrerListeEintrag>;
		allowRegeln: boolean;
	}>();

	// const selected_fachwahl: ComputedRef<boolean> = computed(() => {
	// 	const filter_fach_id = props.schuelerFilter?.fach?.value;
	// 	return (kurs_blockungsergebnis.value !== undefined) && (kurs_blockungsergebnis.value?.id === filter_kurs_id)
	// });

	function toggle_active_fachwahl() {
		if (props.schuelerFilter === undefined)
			return;
		if (props.schuelerFilter.fach.value !== props.fach.id) {
			props.schuelerFilter.kursart.value = props.kursart;
			props.schuelerFilter.fach.value = props.fach.id;
		}
		else
			props.schuelerFilter.reset();
	}

	const kurszahlen: ComputedRef<Map<number, number>> = computed(() => {
		const kurszahlen : Map<number, number> = new Map();
		for (const kursart of GostKursart.values())
			kurszahlen.set(kursart.id, 0);
		for (const k of sorted_kurse.value) {
			if (k.fach_id !== props.fach.id)
				continue;
			let anzahl = kurszahlen.get(k.kursart);
			anzahl = (anzahl === undefined) ? 1 : anzahl + 1;
			kurszahlen.set(k.kursart, anzahl);
		}
		return kurszahlen;
	});

	function vorhandene_kurse(kursart: GostKursart): GostBlockungKurs[] {
		const liste = [];
		for (const kurs of sorted_kurse.value)
			if (kurs.fach_id === props.fach.id && kurs.kursart === kursart.id)
				liste.push(kurs);
		return liste;
	}

	const sort_by: ComputedRef<string> = computed(() => props.config.getValue('gost.kursansicht.sortierung'));

	const sorted_kurse: ComputedRef<List<GostBlockungKurs>> = computed(() => {
		if (sort_by.value === 'kursart')
			return props.getDatenmanager().getKursmengeSortiertNachKursartFachNummer()
		else return props.getDatenmanager().getKursmengeSortiertNachFachKursartNummer()
	})

	const schienen: ComputedRef<List<GostBlockungSchiene>> = computed(() => props.getDatenmanager().getMengeOfSchienen())

	const fach_halbjahr: ComputedRef<GostStatistikFachwahlHalbjahr> = computed(() => props.fach.fachwahlen[props.halbjahr] ||	new GostStatistikFachwahlHalbjahr());

	const wahlen: ComputedRef<Map<number, number>> = computed(() => {
		const wahlen : Map<number, number> = new Map();
		const gostfach : GostFach | null = props.faecherManager.get(props.fach.id);
		if (gostfach === null)
			return wahlen;
		const zulFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(gostfach.kuerzel);
		wahlen.set(GostKursart.LK.id, fach_halbjahr.value.wahlenLK);
		wahlen.set(GostKursart.GK.id, zulFach === ZulaessigesFach.PX || zulFach === ZulaessigesFach.VX ? 0 : fach_halbjahr.value.wahlenGK);
		wahlen.set(GostKursart.ZK.id, fach_halbjahr.value.wahlenZK);
		wahlen.set(GostKursart.PJK.id, zulFach === ZulaessigesFach.PX ? fach_halbjahr.value.wahlenGK : 0);
		wahlen.set(GostKursart.VTF.id, zulFach === ZulaessigesFach.VX ? fach_halbjahr.value.wahlenGK : 0);
		return wahlen;
	});

	const bgColor: ComputedRef<string> = computed(() => ZulaessigesFach.getByKuerzelASD(props.fach.kuerzelStatistik).getHMTLFarbeRGBA(1.0));

	async function add_kurs(art: GostKursart) {
		await props.addKurs(props.fach.id, art.id);
	}

</script>
