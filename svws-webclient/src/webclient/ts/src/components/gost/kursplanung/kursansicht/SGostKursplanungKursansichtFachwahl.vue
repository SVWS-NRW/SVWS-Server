<template>
	<template v-if="kurszahlen.get(kursart.id) === 0 && wahlen.get(kursart.id) && allowRegeln">
		<tr class="text-left" :style="{ 'background-color': bgColor }" :key="kursart.id">
			<td colspan="3">
				{{ fach.kuerzel }}-{{ kursart.kuerzel }}
			</td>
			<td class="text-center" colspan="1">
				{{ wahlen.get(kursart.id) }}
			</td>
			<td :colspan="schienen.size()+2">
				<svws-ui-button class="" type="secondary" size="small" @click="add_kurs(kursart)" title="Kurs hinzufügen">Kurs hinzufügen</svws-ui-button>
			</td>
		</tr>
	</template>
	<template v-else>
		<s-gost-kursplanung-kursansicht-kurs v-for="kurs in vorhandene_kurse(kursart)" :key="kurs.id" :kurs="kurs" :bg-color="bgColor"
			:map-lehrer="mapLehrer" :allow-regeln="allowRegeln" :schueler-filter="schuelerFilter" :datenmanager="datenmanager" :ergebnismanager="ergebnismanager"
			:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
			:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer" />
	</template>
</template>

<script setup lang="ts">

	import { List, GostBlockungKurs, GostBlockungSchiene, GostKursart, GostStatistikFachwahl, GostStatistikFachwahlHalbjahr,
		ZulaessigesFach, GostFach, LehrerListeEintrag, GostBlockungRegel, GostFaecherManager, GostBlockungKursLehrer, GostBlockungsergebnisManager, GostBlockungsdatenManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { injectMainApp, Main } from "~/apps/Main";
	import type { UserConfigKeys } from "~/utils/userconfig/keys"
	import { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<void>;
		removeRegel: (id: number) => Promise<void>;
		updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<void>;
		patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		fach: GostStatistikFachwahl;
		faecherManager: GostFaecherManager;
		kursart: GostKursart;
		dataFaecher: DataGostFaecher;
		halbjahr: number;
		datenmanager: GostBlockungsdatenManager;
		ergebnismanager: GostBlockungsergebnisManager;
		listLehrer: ListLehrer;
		mapLehrer: Map<number, LehrerListeEintrag>;
		allowRegeln: boolean;
	}>();

	const main: Main = injectMainApp();

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

	const sort_by: ComputedRef<UserConfigKeys['gost.kursansicht.sortierung']> = computed(() => main.config.user_config.get('gost.kursansicht.sortierung') || 'kursart');

	const sorted_kurse: ComputedRef<List<GostBlockungKurs>> = computed(() => {
		if (sort_by.value === 'kursart')
			return props.datenmanager.getKursmengeSortiertNachKursartFachNummer()
		else return props.datenmanager.getKursmengeSortiertNachFachKursartNummer()
	})

	const schienen: ComputedRef<List<GostBlockungSchiene>> = computed(() => props.datenmanager.getMengeOfSchienen())

	const fach_halbjahr: ComputedRef<GostStatistikFachwahlHalbjahr> = computed(() => props.fach.fachwahlen[props.halbjahr] ||	new GostStatistikFachwahlHalbjahr());

	const wahlen: ComputedRef<Map<number, number>> = computed(() => {
		const wahlen : Map<number, number> = new Map();
		const gostfach : GostFach | undefined = props.dataFaecher.manager?.get(props.fach.id) || undefined;
		if (gostfach === undefined)
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
